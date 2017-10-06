package me.arakmmis.contactsapp.ui.addContact

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import kotlinx.android.synthetic.main.add_contact_activity.*
import me.arakmmis.contactsapp.R
import me.arakmmis.contactsapp.mvpcontracts.AddContactContract
import java.util.*

/**
 * Created by arakm on 10/5/2017.
 */
class AddContactActivity : AppCompatActivity(), AddContactContract.AddContactView, DatePickerDialog.OnDateSetListener {

    private lateinit var presenter: AddContactContract.AddContactPresenter

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

    }

    fun addContact(v: View) {

    }
}