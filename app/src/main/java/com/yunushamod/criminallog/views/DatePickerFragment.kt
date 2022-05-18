package com.yunushamod.criminallog.views

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import java.util.*

class DatePickerFragment private constructor(): DialogFragment() {
    interface Callbacks{
        fun onDateSelected(date: Date)
    }
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dateListener = DatePickerDialog.OnDateSetListener{
            _, year, month, day ->
            val resultDate: Date = GregorianCalendar(year, month, day).time
            targetFragment?.let {
                (it as Callbacks).onDateSelected(resultDate)
            }
        }
        val time = arguments?.getSerializable(DATE_KEY) as Date
        val calendar = Calendar.getInstance()
        calendar.time = time
        val initialYear = calendar.get(Calendar.YEAR)
        val initialMonth = calendar.get(Calendar.MONTH)
        val initialDay = calendar.get(Calendar.DAY_OF_MONTH)
        return DatePickerDialog(requireContext(),
        dateListener,
        initialYear,
        initialMonth,
        initialDay)
    }

    companion object{
        fun newInstance(date: Date): DatePickerFragment{
            val args = Bundle().apply {
                putSerializable(DATE_KEY, date)
            }
            return DatePickerFragment().apply {
                arguments = args
            }
        }

        private const val DATE_KEY: String = "DateKey"
    }
}