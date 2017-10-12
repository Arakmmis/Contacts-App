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
import me.arakmmis.contactsapp.mvpcontracts.EditContactContract
import me.arakmmis.contactsapp.ui.details.ContactDetailsActivity
import me.arakmmis.contactsapp.ui.edit.adapter.DetailsAdapter
import me.arakmmis.contactsapp.utils.*
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

    private var dialogViewPhoneNumber: View? = null
    private var alertDialogPhoneNumber: AlertDialog? = null

    private var dialogViewAddress: View? = null
    private var alertDialogAddress: AlertDialog? = null

    private var dialogViewEmailAddress: View? = null
    private var alertDialogEmailAddress: AlertDialog? = null

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

        convertPhoneNumbers(contact)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { phoneNumbers, e ->
                    if (phoneNumbers != null) {
                        Cache.setPhoneNumbers(phoneNumbers)
                        adapterPhoneNumbers = DetailsAdapter<PhoneNumber>(R.layout.edit_contact_rv_item_phone_number,
                                Cache.getPhoneNumbers(),
                                object : Callback<PhoneNumber> {
                                    override fun onClick(item: PhoneNumber) {
                                        presenter.deletePhoneNumber(item)
                                    }
                                })
                        rv_phone_numbers.adapter = adapterPhoneNumbers
                    }

                    if (e != null) {
                        Log.e("ECA: setContact pn", e.message)
                    }
                }


        convertEmailAddresses(contact)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { emails, e ->
                    if (emails != null) {
                        Cache.setEmails(emails)
                        adapterEmailAddress = DetailsAdapter<EmailAddress>(R.layout.edit_contact_rv_item_email_address,
                                Cache.getEmails(),
                                object : Callback<EmailAddress> {
                                    override fun onClick(item: EmailAddress) {
                                        presenter.deleteEmailAddress(item)
                                    }
                                })
                        rv_email_addresses.adapter = adapterEmailAddress
                    }

                    if (e != null) {
                        Log.e("ECA: setContact, ea", e.message)
                    }
                }

        convertAddresses(contact)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { addresses, e ->
                    if (addresses != null) {
                        Cache.setAddresses(addresses)
                        adapterAddresses = DetailsAdapter<Address>(R.layout.edit_contact_rv_item_address,
                                Cache.getAddresses(),
                                object : Callback<Address> {
                                    override fun onClick(item: Address) {
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
        dialogViewPhoneNumber = layoutInflater.inflate(R.layout.add_contact_dialog_phone_number, null)
        alertDialogPhoneNumber = AlertDialog.Builder(this).setView(dialogViewPhoneNumber).create()

        val adapterPhoneNumbers: ArrayAdapter<CharSequence>

        if (Cache.isDefaultTypeUsed()) {
            adapterPhoneNumbers = ArrayAdapter.createFromResource(dialogViewPhoneNumber?.context,
                    R.array.phone_number_types_without_default_array, android.R.layout.simple_spinner_item)
        } else {
            adapterPhoneNumbers = ArrayAdapter.createFromResource(dialogViewPhoneNumber?.context,
                    R.array.phone_number_types_array, android.R.layout.simple_spinner_item)
        }

        adapterPhoneNumbers.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        dialogViewPhoneNumber?.spinner_phone_number_type?.adapter = adapterPhoneNumbers

        dialogViewPhoneNumber?.ok_pn?.setOnClickListener { _ ->
            presenter.addPhoneNumber(PhoneNumber(number = dialogViewPhoneNumber?.et_phone_number?.text.toString().trim(),
                    type = dialogViewPhoneNumber?.spinner_phone_number_type?.selectedItem.toString()))
        }

        dialogViewPhoneNumber?.cancel_pn?.setOnClickListener { _ ->
            alertDialogPhoneNumber?.dismiss()
        }

        alertDialogPhoneNumber?.show()
    }

    override fun showPhoneNumberError(errorMessage: String) {
        dialogViewPhoneNumber?.til_phone_number?.error = errorMessage
    }

    override fun updatePhoneList(phoneNumbers: List<PhoneNumber>) {
        adapterPhoneNumbers.setData(phoneNumbers)

        alertDialogPhoneNumber?.dismiss()
        disableFieldError(ValidationUtil.PHONE_NUMBERS_KEY)
    }

    fun addAddress(v: View) {
        dialogViewAddress = layoutInflater.inflate(R.layout.add_contact_dialog_address, null)
        alertDialogAddress = AlertDialog.Builder(this).setView(dialogViewAddress).create()

        val adapterAddress = ArrayAdapter.createFromResource(dialogViewAddress?.context,
                R.array.address_types_array, android.R.layout.simple_spinner_item)

        adapterAddress.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        dialogViewAddress?.spinner_address_type?.adapter = adapterAddress

        dialogViewAddress?.ok_a?.setOnClickListener { _ ->
            presenter.addAddress(Address(address = dialogViewAddress?.et_address?.text.toString().trim(),
                    type = dialogViewAddress?.spinner_address_type?.selectedItem.toString()))
        }

        dialogViewAddress?.cancel_a?.setOnClickListener { _ ->
            alertDialogAddress?.dismiss()
        }

        alertDialogAddress?.show()
    }

    override fun showAddressError(errorMessage: String) {
        dialogViewAddress?.til_address?.error = errorMessage
    }

    override fun updateAddressList(addresses: List<Address>) {
        adapterAddresses.setData(addresses)

        alertDialogAddress?.dismiss()
    }

    fun addEmailAddress(v: View) {
        dialogViewEmailAddress = layoutInflater.inflate(R.layout.add_contact_dialog_email_address, null)
        alertDialogEmailAddress = AlertDialog.Builder(this).setView(dialogViewEmailAddress).create()

        val adapterEmailAddress = ArrayAdapter.createFromResource(dialogViewEmailAddress?.context,
                R.array.email_address_array, android.R.layout.simple_spinner_item)

        adapterEmailAddress.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        dialogViewEmailAddress?.spinner_email_address_type?.adapter = adapterEmailAddress

        dialogViewEmailAddress?.ok_ea?.setOnClickListener { _ ->
            presenter.addEmailAddress(EmailAddress(emailAddress = dialogViewEmailAddress?.et_email_address?.text.toString().trim(),
                    type = dialogViewEmailAddress?.spinner_email_address_type?.selectedItem.toString()))
        }

        dialogViewEmailAddress?.cancel_ea?.setOnClickListener { _ ->
            alertDialogEmailAddress?.dismiss()
        }

        alertDialogEmailAddress?.show()
    }

    override fun showEmailAddressError(errorMessage: String) {
        dialogViewEmailAddress?.til_email_address?.error = errorMessage
    }

    override fun updateEmailAddressesList(emails: List<EmailAddress>) {
        adapterEmailAddress.setData(emails)

        alertDialogEmailAddress?.dismiss()
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