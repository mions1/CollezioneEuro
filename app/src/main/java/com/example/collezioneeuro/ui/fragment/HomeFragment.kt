package com.example.collezioneeuro.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.collezioneeuro.R
import com.example.collezioneeuro.contract.CEContract
import com.example.collezioneeuro.databinding.FragmentHomeBinding
import com.example.collezioneeuro.listener.UpdateCountriesListener
import com.example.collezioneeuro.model.CECountry
import com.example.collezioneeuro.model.repository.CEFakeRepository
import com.example.collezioneeuro.model.repository.CERepositoryInterface
import com.example.collezioneeuro.presenter.CEPresenter
import com.example.collezioneeuro.presenter.RuntimeDispatcherProvider
import com.example.collezioneeuro.ui.activity.ActionBarActivityInterface
import com.example.collezioneeuro.ui.activity.ActivityInterface
import com.example.collezioneeuro.ui.activity.MainActivity
import com.example.collezioneeuro.ui.adapter.CountriesAdapter

class HomeFragment : Fragment(), CEContract.View, UpdateCountriesListener {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var activityParent: ActivityInterface

    private lateinit var repository: CERepositoryInterface
    private lateinit var presenter: CEContract.Presenter

    private var ceCountries = ArrayList<CECountry>()

    private var onlyCompleted = false // se si accede dalla bottom tab "completati"

    companion object {
        const val TAG = "HomeFragment"

        const val ARG_ONLY_COMPLETED = "ARG_ONLY_COMPLETED"

        fun newInstance(onlyCompleted: Boolean = false): HomeFragment =
            HomeFragment().apply {
                val args = Bundle()
                args.putBoolean(ARG_ONLY_COMPLETED, onlyCompleted)
                arguments = args
            }
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

        onlyCompleted = arguments?.getBoolean(ARG_ONLY_COMPLETED, false) == true

        setOnBackPressed()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        setupActionBar()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter.getCountries()
        initRecyclerView()

        setSearchViewListener()
    }

    private fun setupActionBar() {
        (activity as? ActionBarActivityInterface)?.enableDrawer()
        (activity as? ActionBarActivityInterface)?.setActionBarTitle(getString(R.string.app_name))
        (activity as? ActionBarActivityInterface)?.setActionBarSubtitle("")
    }

    private fun initRecyclerView() {
        val mLayoutManager: RecyclerView.LayoutManager = GridLayoutManager(context, 2)
        binding.rvCountries.layoutManager = mLayoutManager
    }

    private fun setRecyclerViewAdapter(countries: ArrayList<CECountry>) {
        if (onlyCompleted)
            countries.removeIf { it.getOwnedList().size < it.coins.size }
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
            (activityParent as? UpdateCountriesListener)?.onUpdateCountries(countries)
        }
    }

    /**
     * Listener per l'aggiornamento delle countries
     */
    override fun onUpdateCountries(ceCountries: ArrayList<CECountry>) {
        this.ceCountries = ceCountries
        setRecyclerViewAdapter(ceCountries)
    }

    private fun setOnBackPressed() {
        // se siamo in onlyCompleted, quindi nella relativa tab, all'onBackPressed deve andare alla HOME
        if (onlyCompleted)
            activity?.onBackPressedDispatcher?.addCallback(
                this,
                object : OnBackPressedCallback(true) {
                    override fun handleOnBackPressed() {
                        activityParent.setWhichItemIsSelectedBottomNavigationMenu(MainActivity.BottomNavigationItem.HOME)
                    }
                })
    }

}