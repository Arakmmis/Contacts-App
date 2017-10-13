package me.arakmmis.contactsapp.ui.edit

import android.Manifest
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import com.aitorvs.android.allowme.AllowMe
import com.aitorvs.android.allowme.AllowMeActivity
import com.bumptech.glide.Glide
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import io.reactivex.Single
import io.reactivex.SingleEmitter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.add_contact_dialog_address.view.*
import kotlinx.android.synthetic.main.add_contact_dialog_email_address.view.*
import kotlinx.android.synthetic.main.add_contact_dialog_phone_number.view.*
import kotlinx.android.synthetic.main.dialog_upload_pic.view.*
import kotlinx.android.synthetic.main.edit_contact_activity.*
import me.arakmmis.contactsapp.R
import me.arakmmis.contactsapp.businesslogic.models.Address
import me.arakmmis.contactsapp.businesslogic.models.Contact
import me.arakmmis.contactsapp.businesslogic.models.EmailAddress
import me.arakmmis.contactsapp.businesslogic.models.PhoneNumber
import me.arakmmis.contactsapp.customlisteners.EditContactCallback
import me.arakmmis.contactsapp.mvpcontracts.EditContactContract
import me.arakmmis.contactsapp.ui.details.ContactDetailsActivity
import me.arakmmis.contactsapp.ui.edit.adapter.DetailsAdapter
import me.arakmmis.contactsapp.utils.ByteArrayUtil
import me.arakmmis.contactsapp.utils.Cache
import me.arakmmis.contactsapp.utils.Const
import me.arakmmis.contactsapp.utils.ValidationUtil
import pl.aprilapps.easyphotopicker.DefaultCallback
import pl.aprilapps.easyphotopicker.EasyImage
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

class EditContactActivity : AllowMeActivity(), EditContactContract.EditContactView, DatePickerDialog.OnDateSetListener {

    lateinit var presenter: EditContactContract.EditContactPresenter
    private lateinit var profilePic: ByteArray

    private lateinit var adapterPhoneNumbers: DetailsAdapter<PhoneNumber>
    private lateinit var adapterAddresses: DetailsAdapter<Address>
    private lateinit var adapterEmailAddress: DetailsAdapter<EmailAddress>

    // Add Dialogs
    private var dvAddPhoneNumber: View? = null
    private var adAddPhoneNumber: AlertDialog? = null

    private var dvAddAddress: View? = null
    private var adAddAddress: AlertDialog? = null

    private var dvAddEmailAddress: View? = null
    private var adAddEmailAddress: AlertDialog? = null

    // Edit Dialogs
    private var dvEditPhoneNumber: View? = null
    private var adEditPhoneNumber: AlertDialog? = null

    private var dvEditAddress: View? = null
    private var adEditAddress: AlertDialog? = null

    private var dvEditEmailAddress: View? = null
    private var adEditEmailAddress: AlertDialog? = null

    private val contactId by lazy {
        intent.extras.getInt(Const.CONTACT_ID_KEY)
    }

    companion object {
        fun start(context: Context, contactId: Int) {
            val intent = Intent(context, EditContactActivity::class.java)
            intent.putExtra(Const.CONTACT_ID_KEY, contactId)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_contact_activity)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        Cache.removePhoneNumbers()
        Cache.setDefaultTypeUsed(false)
        Cache.removeAddress()
        Cache.removeEmails()

        presenter = EditContactPresenter(this, contactId)

        initUI()
    }

    private fun initUI() {
        rv_phone_numbers.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rv_phone_numbers.isNestedScrollingEnabled = false

        rv_email_addresses.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rv_email_addresses.isNestedScrollingEnabled = false

        rv_addresses.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rv_addresses.isNestedScrollingEnabled = false
    }

    override fun setContactData(contact: Contact) {
        profilePic = contact.profilePic
        Glide.with(this@EditContactActivity)
                .load(contact.profilePic)
                .into(iv_contact_pic)

        et_contact_name.setText(contact.name)
        tv_birth_date.text = contact.dateOfBirth

        setPhoneNumbers(contact)
        setEmails(contact)
        setAddresses(contact)
    }

    private fun setAddresses(contact: Contact) {
        convertAddresses(contact)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { addresses, e ->
                    if (addresses != null) {
                        Cache.setAddresses(addresses)
                        adapterAddresses = DetailsAdapter<Address>(R.layout.edit_contact_rv_item_address,
                                Cache.getAddresses(),
                                object : EditContactCallback<Address> {
                                    override fun onEditClicked(item: Address, position: Int) {
                                        editAddress(item, position)
                                    }

                                    override fun onDeleteClicked(item: Address) {
                                        presenter.deleteAddress(item)
                                    }
                                })
                        rv_addresses.adapter = adapterAddresses
                    }

                    if (e != null) {
                        Log.e("ECA: setContact add", e.message)
                    }
                }
    }

    private fun setEmails(contact: Contact) {
        convertEmailAddresses(contact)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { emails, e ->
                    if (emails != null) {
                        Cache.setEmails(emails)
                        adapterEmailAddress = DetailsAdapter<EmailAddress>(R.layout.edit_contact_rv_item_email_address,
                                Cache.getEmails(),
                                object : EditContactCallback<EmailAddress> {
                                    override fun onEditClicked(item: EmailAddress, position: Int) {
                                        editEmailAddress(item, position)
                                    }

                                    override fun onDeleteClicked(item: EmailAddress) {
                                        presenter.deleteEmailAddress(item)
                                    }
                                })
                        rv_email_addresses.adapter = adapterEmailAddress
                    }

                    if (e != null) {
                        Log.e("ECA: setContact, ea", e.message)
                    }
                }
    }

    private fun setPhoneNumbers(contact: Contact) {
        convertPhoneNumbers(contact)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { phoneNumbers, e ->
                    if (phoneNumbers != null) {
                        Cache.setPhoneNumbers(phoneNumbers)
                        adapterPhoneNumbers = DetailsAdapter<PhoneNumber>(R.layout.edit_contact_rv_item_phone_number,
                                Cache.getPhoneNumbers(),
                                object : EditContactCallback<PhoneNumber> {
                                    override fun onEditClicked(item: PhoneNumber, position: Int) {
                                        editPhoneNumber(item, position)
                                    }

                                    override fun onDeleteClicked(item: PhoneNumber) {
                                        presenter.deletePhoneNumber(item)
                                    }
                                })
                        rv_phone_numbers.adapter = adapterPhoneNumbers
                    }

                    if (e != null) {
                        Log.e("ECA: setContact pn", e.message)
                    }
                }
    }

    private fun convertPhoneNumbers(contact: Contact): Single<ArrayList<PhoneNumber>> = Single.create { received: SingleEmitter<ArrayList<PhoneNumber>> ->
        val contactPhoneNumbers = ArrayList<PhoneNumber>()
        contact.phoneNumbers.forEach { phoneNumber ->
            if (phoneNumber.type == "Default") Cache.setDefaultTypeUsed(true)

            contactPhoneNumbers.add(PhoneNumber(number = phoneNumber.number, type = phoneNumber.type))
        }
        received.onSuccess(contactPhoneNumbers)
    }

    private fun convertEmailAddresses(contact: Contact): Single<ArrayList<EmailAddress>> = Single.create { received: SingleEmitter<ArrayList<EmailAddress>> ->
        val contactEmails = ArrayList<EmailAddress>()
        contact.emailAddresses.forEach { email ->
            contactEmails.add(EmailAddress(emailAddress = email.emailAddress, type = email.type))
        }
        received.onSuccess(contactEmails)
    }

    private fun convertAddresses(contact: Contact): Single<ArrayList<Address>> = Single.create { received: SingleEmitter<ArrayList<Address>> ->
        val contactAddresses = ArrayList<Address>()
        contact.addresses.forEach { address ->
            contactAddresses.add(Address(address = address.address, type = address.type))
        }
        received.onSuccess(contactAddresses)
    }

    fun openDatePickerDialog(v: View) {
        val now = Calendar.getInstance()
        val dpd = DatePickerDialog.newInstance(
                this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        )

        val maxCalendar = Calendar.getInstance()
        maxCalendar.set(Calendar.YEAR, 2013)
        maxCalendar.set(Calendar.MONTH, 11)
        maxCalendar.set(Calendar.DAY_OF_MONTH, 31)

        val minCalendar = Calendar.getInstance()
        minCalendar.set(Calendar.YEAR, 1930)
        minCalendar.set(Calendar.MONTH, 0)
        minCalendar.set(Calendar.DAY_OF_MONTH, 1)

        dpd.minDate = minCalendar
        dpd.maxDate = maxCalendar

        dpd.setVersion(DatePickerDialog.Version.VERSION_1)
        dpd.show(fragmentManager, "DatePickerDialog")
    }

    override fun onDateSet(view: DatePickerDialog?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        var month = ""
        when (monthOfYear + 1) {
            1 -> month = "Jan"
            2 -> month = "Feb"
            3 -> month = "Mar"
            4 -> month = "Apr"
            5 -> month = "May"
            6 -> month = "Jun"
            7 -> month = "Jul"
            8 -> month = "Aug"
            9 -> month = "Sep"
            10 -> month = "Oct"
            11 -> month = "Nov"
            12 -> month = "Dec"
        }

        tv_birth_date.text = String.format(Locale.US, "%s %s %s", dayOfMonth, month, year)
        disableFieldError(ValidationUtil.DATE_KEY)
    }

    fun openChooserDialog(v: View) {
        val builder = AlertDialog.Builder(this)

        val dialogView = layoutInflater.inflate(R.layout.dialog_upload_pic, null)
        builder.setView(dialogView)

        val alertDialog = builder.create()

        dialogView.tv_upload_via_camera.setOnClickListener { _ ->
            if (!AllowMe.isPermissionGranted(Manifest.permission.CAMERA)) {
                AllowMe.Builder()
                        .setPermissions(Manifest.permission.CAMERA)
                        .setRationale("To take a picture the app needs access to the Camera")
                        .setCallback({ _, result ->
                            if (result.isGranted(Manifest.permission.CAMERA)) {
                                EasyImage.openCamera(this, 0)
                                alertDialog.dismiss()
                            }
                        }).request(1)
            } else {
                EasyImage.openCamera(this, 0)
                alertDialog.dismiss()
            }
        }

        dialogView.tv_upload_via_gallery.setOnClickListener { _ ->
            if (!AllowMe.isPermissionGranted(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                AllowMe.Builder()
                        .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE)
                        .setRationale("To pick an image from the gallery the app needs access to the external storage")
                        .setCallback({ _, result ->
                            if (result.isGranted(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                                EasyImage.openGallery(this, 0)
                                alertDialog.dismiss()
                            }
                        }).request(2)
            } else {
                EasyImage.openGallery(this, 0)
                alertDialog.dismiss()
            }
        }

        alertDialog.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        EasyImage.handleActivityResult(requestCode, resultCode, data, this, object : DefaultCallback() {
            override fun onImagesPicked(imageFiles: MutableList<File>, source: EasyImage.ImageSource?, type: Int) {
                Glide.with(this@EditContactActivity)
                        .load(imageFiles[0])
                        .into(iv_contact_pic)

                profilePic = ByteArrayUtil.fromFile(imageFiles[0])
            }

            override fun onImagePickerError(e: Exception?, source: EasyImage.ImageSource?, type: Int) {
                Log.e("ACA: EIError", e?.message)
            }

            override fun onCanceled(source: EasyImage.ImageSource?, type: Int) {
                if (source == EasyImage.ImageSource.CAMERA) {
                    val photoFile = EasyImage.lastlyTakenButCanceledPhoto(this@EditContactActivity)
                    photoFile?.delete()
                }
            }
        })
    }

    fun addPhoneNumber(v: View) {
        dvAddPhoneNumber = layoutInflater.inflate(R.layout.add_contact_dialog_phone_number, null)
        adAddPhoneNumber = AlertDialog.Builder(this).setView(dvAddPhoneNumber).create()

        val adapterPhoneNumbers: ArrayAdapter<CharSequence>

        if (Cache.isDefaultTypeUsed()) {
            adapterPhoneNumbers = ArrayAdapter.createFromResource(dvAddPhoneNumber?.context,
                    R.array.phone_number_types_without_default_array, android.R.layout.simple_spinner_item)
        } else {
            adapterPhoneNumbers = ArrayAdapter.createFromResource(dvAddPhoneNumber?.context,
                    R.array.phone_number_types_array, android.R.layout.simple_spinner_item)
        }

        adapterPhoneNumbers.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        dvAddPhoneNumber?.spinner_phone_number_type?.adapter = adapterPhoneNumbers

        dvAddPhoneNumber?.ok_pn?.setOnClickListener { _ ->
            presenter.addPhoneNumber(PhoneNumber(number = dvAddPhoneNumber?.et_phone_number?.text.toString().trim(),
                    type = dvAddPhoneNumber?.spinner_phone_number_type?.selectedItem.toString()))
        }

        dvAddPhoneNumber?.cancel_pn?.setOnClickListener { _ ->
            adAddPhoneNumber?.dismiss()
        }

        adAddPhoneNumber?.show()
    }

    fun editPhoneNumber(phoneNumber: PhoneNumber, position: Int) {
        dvEditPhoneNumber = layoutInflater.inflate(R.layout.add_contact_dialog_phone_number, null)
        adEditPhoneNumber = AlertDialog.Builder(this).setView(dvEditPhoneNumber).create()

        val adapterPhoneNumbers: ArrayAdapter<CharSequence>

        if (phoneNumber.type != "Default") {
            if (Cache.isDefaultTypeUsed()) {
                adapterPhoneNumbers = ArrayAdapter.createFromResource(dvEditPhoneNumber?.context,
                        R.array.phone_number_types_without_default_array, android.R.layout.simple_spinner_item)
            } else {
                adapterPhoneNumbers = ArrayAdapter.createFromResource(dvEditPhoneNumber?.context,
                        R.array.phone_number_types_array, android.R.layout.simple_spinner_item)
            }
        } else {
            adapterPhoneNumbers = ArrayAdapter.createFromResource(dvEditPhoneNumber?.context,
                    R.array.phone_number_types_array, android.R.layout.simple_spinner_item)
        }

        adapterPhoneNumbers.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        dvEditPhoneNumber?.spinner_phone_number_type?.adapter = adapterPhoneNumbers

        dvEditPhoneNumber?.et_phone_number?.setText(phoneNumber.number)

        dvEditPhoneNumber?.ok_pn?.setOnClickListener { _ ->
            presenter.editPhoneNumber(PhoneNumber(number = dvEditPhoneNumber?.et_phone_number?.text.toString().trim(),
                    type = dvEditPhoneNumber?.spinner_phone_number_type?.selectedItem.toString()), phoneNumber, position)
        }

        dvEditPhoneNumber?.cancel_pn?.setOnClickListener { _ ->
            adEditPhoneNumber?.dismiss()
        }

        adEditPhoneNumber?.show()
    }

    override fun showPhoneNumberError(errorMessage: String) {
        dvAddPhoneNumber?.til_phone_number?.error = errorMessage
        dvEditPhoneNumber?.til_phone_number?.error = errorMessage
    }

    override fun updatePhoneList(phoneNumbers: List<PhoneNumber>) {
        adapterPhoneNumbers.setData(phoneNumbers)

        adAddPhoneNumber?.dismiss()
        adEditPhoneNumber?.dismiss()
        disableFieldError(ValidationUtil.PHONE_NUMBERS_KEY)
    }

    fun addAddress(v: View) {
        dvAddAddress = layoutInflater.inflate(R.layout.add_contact_dialog_address, null)
        adAddAddress = AlertDialog.Builder(this).setView(dvAddAddress).create()

        val adapterAddress = ArrayAdapter.createFromResource(dvAddAddress?.context,
                R.array.address_types_array, android.R.layout.simple_spinner_item)

        adapterAddress.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        dvAddAddress?.spinner_address_type?.adapter = adapterAddress

        dvAddAddress?.ok_a?.setOnClickListener { _ ->
            presenter.addAddress(Address(address = dvAddAddress?.et_address?.text.toString().trim(),
                    type = dvAddAddress?.spinner_address_type?.selectedItem.toString()))
        }

        dvAddAddress?.cancel_a?.setOnClickListener { _ ->
            adAddAddress?.dismiss()
        }

        adAddAddress?.show()
    }

    fun editAddress(address: Address, position: Int) {
        dvEditAddress = layoutInflater.inflate(R.layout.add_contact_dialog_address, null)
        adEditAddress = AlertDialog.Builder(this).setView(dvEditAddress).create()

        val adapterAddress = ArrayAdapter.createFromResource(dvEditAddress?.context,
                R.array.address_types_array, android.R.layout.simple_spinner_item)

        adapterAddress.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        dvEditAddress?.spinner_address_type?.adapter = adapterAddress

        dvEditAddress?.et_address?.setText(address.address)

        dvEditAddress?.ok_a?.setOnClickListener { _ ->
            presenter.editAddress(Address(address = dvEditAddress?.et_address?.text.toString().trim(),
                    type = dvEditAddress?.spinner_address_type?.selectedItem.toString()), address, position)
        }

        dvEditAddress?.cancel_a?.setOnClickListener { _ ->
            adEditAddress?.dismiss()
        }

        adEditAddress?.show()
    }

    override fun showAddressError(errorMessage: String) {
        dvAddAddress?.til_address?.error = errorMessage
        dvEditAddress?.til_address?.error = errorMessage
    }

    override fun updateAddressList(addresses: List<Address>) {
        adapterAddresses.setData(addresses)

        adAddAddress?.dismiss()
        adEditAddress?.dismiss()
    }

    fun addEmailAddress(v: View) {
        dvAddEmailAddress = layoutInflater.inflate(R.layout.add_contact_dialog_email_address, null)
        adAddEmailAddress = AlertDialog.Builder(this).setView(dvAddEmailAddress).create()

        val adapterEmailAddress = ArrayAdapter.createFromResource(dvAddEmailAddress?.context,
                R.array.email_address_array, android.R.layout.simple_spinner_item)

        adapterEmailAddress.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        dvAddEmailAddress?.spinner_email_address_type?.adapter = adapterEmailAddress

        dvAddEmailAddress?.ok_ea?.setOnClickListener { _ ->
            presenter.addEmailAddress(EmailAddress(emailAddress = dvAddEmailAddress?.et_email_address?.text.toString().trim(),
                    type = dvAddEmailAddress?.spinner_email_address_type?.selectedItem.toString()))
        }

        dvAddEmailAddress?.cancel_ea?.setOnClickListener { _ ->
            adAddEmailAddress?.dismiss()
        }

        adAddEmailAddress?.show()
    }

    fun editEmailAddress(email: EmailAddress, position: Int) {
        dvEditEmailAddress = layoutInflater.inflate(R.layout.add_contact_dialog_email_address, null)
        adEditEmailAddress = AlertDialog.Builder(this).setView(dvEditEmailAddress).create()

        val adapterEmailAddress = ArrayAdapter.createFromResource(dvEditEmailAddress?.context,
                R.array.email_address_array, android.R.layout.simple_spinner_item)

        adapterEmailAddress.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        dvEditEmailAddress?.spinner_email_address_type?.adapter = adapterEmailAddress

        dvEditEmailAddress?.et_email_address?.setText(email.emailAddress)

        dvEditEmailAddress?.ok_ea?.setOnClickListener { _ ->
            presenter.editEmailAddress(EmailAddress(emailAddress = dvEditEmailAddress?.et_email_address?.text.toString().trim(),
                    type = dvEditEmailAddress?.spinner_email_address_type?.selectedItem.toString()), email, position)
        }

        dvEditEmailAddress?.cancel_ea?.setOnClickListener { _ ->
            adEditEmailAddress?.dismiss()
        }

        adEditEmailAddress?.show()
    }

    override fun showEmailAddressError(errorMessage: String) {
        dvAddEmailAddress?.til_email_address?.error = errorMessage
        dvEditEmailAddress?.til_email_address?.error = errorMessage
    }

    override fun updateEmailAddressesList(emails: List<EmailAddress>) {
        adapterEmailAddress.setData(emails)

        adAddEmailAddress?.dismiss()
        adEditEmailAddress?.dismiss()
        disableFieldError(ValidationUtil.EMAILS_KEY)
    }

    override fun navigateToContactDetails(contact: Contact) {
        ContactDetailsActivity.start(this@EditContactActivity, contact.id)
        finish()
    }

    override fun showNameError(errorMessage: String) {
        til_contact_name.error = errorMessage
    }

    override fun showDateError(errorMessage: String) {
        tv_birth_date.setTextColor(Color.argb(255, 255, 0, 0))
    }

    override fun showPhoneNumbersListError(errorMessage: String) {
        iv_add_phone_number.setColorFilter(Color.argb(255, 255, 0, 0))
    }

    override fun showEmailsListError(errorMessage: String) {
        iv_add_email_address.setColorFilter(Color.argb(255, 255, 0, 0))
    }

    override fun disableFieldError(field: String) {
        when (field) {
            ValidationUtil.NAME_KEY -> til_contact_name.isErrorEnabled = false
            ValidationUtil.DATE_KEY -> tv_birth_date.setTextColor(Color.argb(255, 0, 0, 0))
            ValidationUtil.PHONE_NUMBERS_KEY -> iv_add_phone_number.setColorFilter(Color.argb(255, 174, 174, 174))
            ValidationUtil.EMAILS_KEY -> iv_add_email_address.setColorFilter(Color.argb(255, 174, 174, 174))
        }
    }

    fun submitChanges(v: View) {
        presenter.updateContact(contactId,
                profilePic,
                et_contact_name.text.toString().trim(),
                tv_birth_date.text.toString().trim())
    }
}