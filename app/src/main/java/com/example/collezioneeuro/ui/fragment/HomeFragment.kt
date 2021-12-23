package com.example.collezioneeuro.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.collezioneeuro.contract.CEContract
import com.example.collezioneeuro.databinding.FragmentHomeBinding
import com.example.collezioneeuro.model.CECountry
import com.example.collezioneeuro.model.repository.CEFakeRepository
import com.example.collezioneeuro.model.repository.CERepositoryInterface
import com.example.collezioneeuro.presenter.CEPresenter
import com.example.collezioneeuro.presenter.RuntimeDispatcherProvider
import com.example.collezioneeuro.ui.activity.ActivityInterface
import com.example.collezioneeuro.ui.activity.MainActivity
import com.example.collezioneeuro.ui.adapter.CountriesAdapter

class HomeFragment : Fragment(), CEContract.View {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var activityParent: ActivityInterface

    private lateinit var repository: CERepositoryInterface
    private lateinit var presenter: CEContract.Presenter

    private var ceCountries = ArrayList<CECountry>()

    companion object {
        const val TAG = "HomeFragment"
        fun newInstance(): HomeFragment = HomeFragment()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activityParent = context as MainActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        repository = CEFakeRepository.SingleInstance
        presenter = CEPresenter(RuntimeDispatcherProvider(), repository)
        presenter.bindView(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter.getCountries()
        initRecyclerView()

        setSearchViewListener()
    }

    private fun initRecyclerView() {
        val mLayoutManager: RecyclerView.LayoutManager = GridLayoutManager(context, 2)
        binding.rvCountries.layoutManager = mLayoutManager
    }

    private fun setRecyclerViewAdapter(countries: ArrayList<CECountry>) {
        binding.rvCountries.adapter =
            CountriesAdapter(countries, object : CountriesAdapter.OnClickListener {
                override fun onClick(clicked: CECountry) {
                    activityParent.replaceFragmentToCoinsFragment(clicked)
                }
            })
    }

    /**
     * Quando viene effettuata una ricerca, chiama il relativo metodo nell'adapter
     */
    private fun setSearchViewListener() {
        binding.svCountry.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                (binding.rvCountries.adapter as? CountriesAdapter)?.filter?.filter(newText)
                return true
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }
        })
    }

    override fun onGetCountries(countries: ArrayList<CECountry>) {
        if (countries.isEmpty()) {
            presenter.getCountries()
        } else {
            this.ceCountries = countries
            setRecyclerViewAdapter(countries)
        }
    }

}