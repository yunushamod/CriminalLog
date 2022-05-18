package com.yunushamod.criminallog.views

import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.yunushamod.criminallog.R
import com.yunushamod.criminallog.models.Crime
import com.yunushamod.criminallog.viewmodels.CrimeDetailViewModel
import java.text.SimpleDateFormat
import java.util.*

class CrimeFragment private constructor() : Fragment(), DatePickerFragment.Callbacks {
    private lateinit var crime: Crime
    private lateinit var titleField: EditText
    private lateinit var solvedCheckBox: CheckBox
    private lateinit var dateButton: Button
    private var simpleDateFormat = SimpleDateFormat("EEE, dd-MM-yyyy")
    private val crimeDetailViewModel: CrimeDetailViewModel by lazy{
        ViewModelProvider(this).get(CrimeDetailViewModel::class.java)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val crimeId = arguments?.getSerializable(CRIME_KEY) as UUID
        crimeDetailViewModel.loadCrime(crimeId)
        crime = Crime()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_crime, container, false)
        titleField = view.findViewById(R.id.crime_title) as EditText
        solvedCheckBox = view.findViewById(R.id.crime_solved) as CheckBox
        dateButton = view.findViewById(R.id.crime_date) as Button
        dateButton.text = simpleDateFormat.format(crime.date)
        return view
    }

    override fun onStart() {
        super.onStart()
        solvedCheckBox.setOnCheckedChangeListener{
            _, isChecked ->
            crime.isSolved = isChecked
        }
        val textChangedListener = object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                // Not needed
            }

            override fun onTextChanged(sequence: CharSequence?, p1: Int, p2: Int, p3: Int) {
                crime.title = sequence.toString()
            }

            override fun afterTextChanged(p0: Editable?) {
                // Not needed too
            }
        }
        titleField.addTextChangedListener(textChangedListener)
        dateButton.setOnClickListener{
            DatePickerFragment.newInstance(crime.date).apply {
                setTargetFragment(this@CrimeFragment, REQUEST_DATE)
                show(this@CrimeFragment.parentFragmentManager, DIALOG_DATE)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        crimeDetailViewModel.crimeLiveData.observe(
            viewLifecycleOwner
        ) {
            it?.let {
                this.crime = it
                updateUI()
            }
        }
    }

    override fun onStop() {
        super.onStop()
        crimeDetailViewModel.saveCrime(crime)
    }

    private fun updateUI(){
        titleField.setText(crime.title)
        dateButton.text = simpleDateFormat.format(crime.date)
        solvedCheckBox.apply {
            isChecked = crime.isSolved
            jumpDrawablesToCurrentState()
        }
    }

    companion object{
        private const val CRIME_KEY: String = "CRIME_ID"
        private const val DIALOG_DATE: String = "DialogDate"
        private const val REQUEST_DATE: Int = 0
        fun newInstance(crimeId: UUID): CrimeFragment{
            val args = Bundle().apply {
                putSerializable(CRIME_KEY, crimeId)
            }
            return CrimeFragment().apply {
                arguments = args
            }
        }
    }

    override fun onDateSelected(date: Date) {
        crime.date = date
        updateUI()
    }
}