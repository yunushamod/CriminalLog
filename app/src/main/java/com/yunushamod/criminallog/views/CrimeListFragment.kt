package com.yunushamod.criminallog.views

import android.content.Context
import android.media.Image
import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yunushamod.criminallog.R
import com.yunushamod.criminallog.models.Crime
import com.yunushamod.criminallog.viewmodels.CrimeListViewModel
import java.text.SimpleDateFormat
import java.util.*

class CrimeListFragment private constructor() : Fragment() {
    interface Callbacks{
        fun onCrimeSelected(crimeId: UUID)
    }
    private val crimeListViewModel: CrimeListViewModel by lazy{
        ViewModelProvider(this).get(CrimeListViewModel::class.java)
    }
    private lateinit var crimeRecyclerView: RecyclerView
    private var adapter: CrimeListViewAdapter? = CrimeListViewAdapter(emptyList())
    private var callbacks: Callbacks? = null

    private inner class CrimeListViewAdapter(val crimes: List<Crime>): RecyclerView.Adapter<CrimeListViewHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CrimeListViewHolder {
            val view = layoutInflater.inflate(R.layout.list_item_crime, parent, false)
            return CrimeListViewHolder(view)
        }

        override fun onBindViewHolder(holder: CrimeListViewHolder, position: Int) {
            val crime = crimes[position]
            holder.bind(crime)
        }

        override fun getItemCount(): Int = crimes.size

    }

    private inner class CrimeListViewHolder(view: View): RecyclerView.ViewHolder(view), View.OnClickListener{
        private lateinit var crime: Crime
        val titleText = itemView.findViewById(R.id.crime_title) as TextView
        val dateText = itemView.findViewById(R.id.crime_date) as TextView
        val crimeSolved = itemView.findViewById(R.id.crime_solved) as ImageView
        val simpleDateTime = SimpleDateFormat("EEE, dd-MM-yyyy")
        fun bind(crime: Crime){
            this.crime = crime
            titleText.text = crime.title
            dateText.text = simpleDateTime.format(crime.date)
            crimeSolved.visibility = if(crime.isSolved) View.VISIBLE else View.GONE
        }

        init{
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            //Toast.makeText(context, "${crime.title} was clicked", Toast.LENGTH_SHORT).show()
            callbacks?.onCrimeSelected(crime.id)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = layoutInflater.inflate(R.layout.fragment_crime_list, container, false)
        crimeRecyclerView = view.findViewById(R.id.crime_recycler_view) as RecyclerView
        crimeRecyclerView.layoutManager = LinearLayoutManager(context)
        crimeRecyclerView.adapter = adapter
        //updateUI()
        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_crime_list, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.new_crime -> {
                val crime = Crime()
                crimeListViewModel.addCrime(crime)
                callbacks?.onCrimeSelected(crime.id)
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        crimeListViewModel.crimeListLiveData.observe(viewLifecycleOwner) {
            it?.let{
                Log.i(TAG, "Got crimes: #${it.size}")
                updateUI(it)
            }
        }
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    private fun updateUI(crimes: List<Crime>){
        crimeRecyclerView.adapter = CrimeListViewAdapter(crimes)
    }

    companion object{
        private const val TAG = "CrimeListFragment"
        fun newInstance() = CrimeListFragment()
    }
}