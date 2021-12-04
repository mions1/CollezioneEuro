package com.example.collezioneeuro.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
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
        repository = CEFakeRepository()
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
    }

    private fun initRecyclerView() {
        binding.rvCountries.adapter = CountriesAdapter(ceCountries)
    }

    override fun onGetCountries(countries: ArrayList<CECountry>) {
        this.ceCountries = countries
        initRecyclerView()
    }

}