package me.arakmmis.contactsapp.ui.addContact

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.View
import com.aitorvs.android.allowme.AllowMe
import com.aitorvs.android.allowme.AllowMeActivity
import com.bumptech.glide.Glide
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import kotlinx.android.synthetic.main.add_contact_activity.*
import kotlinx.android.synthetic.main.dialog_upload_pic.view.*
import me.arakmmis.contactsapp.R
import me.arakmmis.contactsapp.mvpcontracts.AddContactContract
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_contact_activity)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        presenter = AddContactPresenter(this)
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

    fun addContact(v: View) {

    }
}