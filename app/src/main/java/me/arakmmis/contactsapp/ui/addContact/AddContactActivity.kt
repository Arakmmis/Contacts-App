package me.arakmmis.contactsapp.ui.addContact

import android.Manifest
import android.content.Intent
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
import kotlinx.android.synthetic.main.add_contact_activity.*
import kotlinx.android.synthetic.main.add_contact_dialog_address.view.*
import kotlinx.android.synthetic.main.add_contact_dialog_email_address.view.*
import kotlinx.android.synthetic.main.add_contact_dialog_phone_number.view.*
import kotlinx.android.synthetic.main.dialog_upload_pic.view.*
import me.arakmmis.contactsapp.R
import me.arakmmis.contactsapp.businesslogic.models.Address
import me.arakmmis.contactsapp.businesslogic.models.EmailAddress
import me.arakmmis.contactsapp.businesslogic.models.PhoneNumber
import me.arakmmis.contactsapp.mvpcontracts.AddContactContract
import me.arakmmis.contactsapp.ui.addContact.adapter.DetailsAdapter
import me.arakmmis.contactsapp.utils.Cache
import me.arakmmis.contactsapp.utils.Callback
import pl.aprilapps.easyphotopicker.DefaultCallback
import pl.aprilapps.easyphotopicker.EasyImage
import java.io.File
import java.util.*

/**
 * Created by arakm on 10/5/2017.
 */
class AddContactActivity : AllowMeActivity(), AddContactContract.AddContactView, DatePickerDialog.OnDateSetListener {

    private lateinit var presenter: AddContactContract.AddContactPresenter
    private var profilePicFile: File? = null

    private lateinit var adapterPhoneNumbers: DetailsAdapter<PhoneNumber>
    private lateinit var adapterAddresses: DetailsAdapter<Address>
    private lateinit var adapterEmailAddress: DetailsAdapter<EmailAddress>

    private lateinit var dialogViewPhoneNumber: View
    private lateinit var alertDialogPhoneNumber: AlertDialog

    private lateinit var dialogViewAddress: View
    private lateinit var alertDialogAddress: AlertDialog

    private lateinit var dialogViewEmailAddress: View
    private lateinit var alertDialogEmailAddress: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_contact_activity)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        Cache.removePhoneNumbers()
        Cache.setDefaultTypeUsed(false)
        Cache.removeAddress()

        presenter = AddContactPresenter(this)

        initUI()
    }

    private fun initUI() {
        rv_phone_numbers.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        adapterPhoneNumbers = DetailsAdapter<PhoneNumber>(
                R.layout.add_contact_rv_item_phone_number,
                Cache.getPhoneNumbers(),
                object : Callback<PhoneNumber> {
                    override fun onClick(item: PhoneNumber) {
                        presenter.deletePhoneNumber(item)
                    }
                })
        rv_phone_numbers.adapter = adapterPhoneNumbers

        rv_email_addresses.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        adapterEmailAddress = DetailsAdapter<EmailAddress>(
                R.layout.add_contact_rv_item_email_address,
                Cache.getEmails(),
                object : Callback<EmailAddress> {
                    override fun onClick(item: EmailAddress) {
                        presenter.deleteEmailAddress(item)
                    }
                })
        rv_email_addresses.adapter = adapterEmailAddress

        rv_addresses.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        adapterAddresses = DetailsAdapter<Address>(
                R.layout.add_contact_rv_item_address,
                Cache.getAddresses(),
                object : Callback<Address> {
                    override fun onClick(item: Address) {
                        presenter.deleteAddress(item)
                    }
                })
        rv_addresses.adapter = adapterAddresses
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
                Glide.with(this@AddContactActivity)
                        .load(imageFiles[0])
                        .into(iv_contact_pic)

                profilePicFile = imageFiles[0]
            }

            override fun onImagePickerError(e: Exception?, source: EasyImage.ImageSource?, type: Int) {
                Log.e("ACA: EIError", e?.message)
            }

            override fun onCanceled(source: EasyImage.ImageSource?, type: Int) {
                if (source == EasyImage.ImageSource.CAMERA) {
                    val photoFile = EasyImage.lastlyTakenButCanceledPhoto(this@AddContactActivity)
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
            adapterPhoneNumbers = ArrayAdapter.createFromResource(dialogViewPhoneNumber.context,
                    R.array.phone_number_types_without_default_array, android.R.layout.simple_spinner_item)
        } else {
            adapterPhoneNumbers = ArrayAdapter.createFromResource(dialogViewPhoneNumber.context,
                    R.array.phone_number_types_array, android.R.layout.simple_spinner_item)
        }

        adapterPhoneNumbers.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        dialogViewPhoneNumber.spinner_phone_number_type?.adapter = adapterPhoneNumbers

        dialogViewPhoneNumber.ok_pn.setOnClickListener { _ ->
            presenter.addPhoneNumber(PhoneNumber(number = dialogViewPhoneNumber.et_phone_number.text.toString().trim(),
                    type = dialogViewPhoneNumber.spinner_phone_number_type.selectedItem.toString()))
        }

        dialogViewPhoneNumber.cancel_pn.setOnClickListener { _ ->
            alertDialogPhoneNumber.dismiss()
        }

        alertDialogPhoneNumber.show()
    }

    override fun showPhoneNumberError(errorMessage: String) {
        dialogViewPhoneNumber.til_phone_number.error = errorMessage
    }

    override fun updatePhoneList(phoneNumbers: List<PhoneNumber>) {
        adapterPhoneNumbers.setData(phoneNumbers)

        if (alertDialogPhoneNumber.isShowing) {
            alertDialogPhoneNumber.dismiss()
        }
    }

    fun addAddress(v: View) {
        dialogViewAddress = layoutInflater.inflate(R.layout.add_contact_dialog_address, null)
        alertDialogAddress = AlertDialog.Builder(this).setView(dialogViewAddress).create()

        val adapterAddress = ArrayAdapter.createFromResource(dialogViewAddress.context,
                R.array.address_types_array, android.R.layout.simple_spinner_item)

        adapterAddress.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        dialogViewAddress.spinner_address_type?.adapter = adapterAddress

        dialogViewAddress.ok_a.setOnClickListener { _ ->
            presenter.addAddress(Address(address = dialogViewAddress.et_address.text.toString().trim(),
                    type = dialogViewAddress.spinner_address_type.selectedItem.toString()))
        }

        dialogViewAddress.cancel_a.setOnClickListener { _ ->
            alertDialogAddress.dismiss()
        }

        alertDialogAddress.show()
    }

    override fun showAddressError(errorMessage: String) {
        dialogViewAddress.til_address.error = errorMessage
    }

    override fun updateAddressList(addresses: List<Address>) {
        adapterAddresses.setData(addresses)

        if (alertDialogAddress.isShowing) {
            alertDialogAddress.dismiss()
        }
    }

    fun addEmailAddress(v: View) {
        dialogViewEmailAddress = layoutInflater.inflate(R.layout.add_contact_dialog_email_address, null)
        alertDialogEmailAddress = AlertDialog.Builder(this).setView(dialogViewEmailAddress).create()

        val adapterEmailAddress = ArrayAdapter.createFromResource(dialogViewEmailAddress.context,
                R.array.email_address_array, android.R.layout.simple_spinner_item)

        adapterEmailAddress.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        dialogViewEmailAddress.spinner_email_address_type?.adapter = adapterEmailAddress

        dialogViewEmailAddress.ok_ea.setOnClickListener { _ ->
            presenter.addEmailAddress(EmailAddress(emailAddress = dialogViewEmailAddress.et_email_address.text.toString().trim(),
                    type = dialogViewEmailAddress.spinner_email_address_type.selectedItem.toString()))
        }

        dialogViewEmailAddress.cancel_ea.setOnClickListener { _ ->
            alertDialogEmailAddress.dismiss()
        }

        alertDialogEmailAddress.show()
    }

    override fun showEmailAddressError(errorMessage: String) {
        dialogViewEmailAddress.til_email_address.error = errorMessage
    }

    override fun updateEmailAddressesList(emails: List<EmailAddress>) {
        adapterEmailAddress.setData(emails)

        if (alertDialogEmailAddress.isShowing) {
            alertDialogEmailAddress.dismiss()
        }
    }

    fun addContact(v: View) {

    }
}