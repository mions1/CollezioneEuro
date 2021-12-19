package com.example.collezioneeuro.ui.fragment

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.collezioneeuro.contract.CEContract
import com.example.collezioneeuro.databinding.FragmentStatisticsBinding
import com.example.collezioneeuro.model.CECountry
import com.example.collezioneeuro.model.repository.CEFakeRepository
import com.example.collezioneeuro.model.repository.CERepositoryInterface
import com.example.collezioneeuro.presenter.CEPresenter
import com.example.collezioneeuro.presenter.RuntimeDispatcherProvider
import com.example.collezioneeuro.ui.activity.ActivityInterface
import com.example.collezioneeuro.ui.activity.MainActivity
import com.example.collezioneeuro.ui.adapter.StatisticsAdapter

data class Statistics(
    val title: String,
    val text: String,
    val image: Drawable? = null,
    val args: Any? = null
)

/**
 * Mostra le statistiche riguardanti le monete possedute.
 * Aggiungere qui altre statistiche da calcolare e visualizzare.
 */
class StatisticsFragment : Fragment(), CEContract.View {

    private lateinit var binding: FragmentStatisticsBinding
    private lateinit var activityParent: ActivityInterface

    private lateinit var repository: CERepositoryInterface
    private lateinit var presenter: CEContract.Presenter

    private var ceCountries = ArrayList<CECountry>()

    companion object {
        const val TAG = "StatisticsFragment"
        fun newInstance(): StatisticsFragment = StatisticsFragment()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activityParent = context as MainActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                activityParent.setWhichItemIsSelectedBottomNavigationMenu(MainActivity.BottomNavigationItem.HOME)
            }
        })

        repository = CEFakeRepository.SingleInstance
        presenter = CEPresenter(RuntimeDispatcherProvider(), repository)
        presenter.bindView(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStatisticsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter.getCountries()
        initRecyclerView()
    }

    /**
     * Calcola le statistiche che si vogliono visualizzare e le ritorna.
     * Aggiungere a questa lista eventuali altre statistiche che si voglionio visualizzare,
     * saranno automaticamente mostrare nel fragment.
     */
    private fun computeStatistics(countries: ArrayList<CECountry>): ArrayList<Statistics> {
        return arrayListOf(
            Statistics(
                "Totale valore monete possedute:",
                getTotalValue(countries).toString() + "€"
            ),
            Statistics("Totale monete possedute:", getTotalOwned(countries).toString())
        )
    }

    /**
     * Ritorna quante monete posseggo
     */
    private fun getTotalOwned(countries: ArrayList<CECountry>): Int {
        return countries.sumOf { it.ownedCount() }
    }

    /**
     * Ritorna quanti soldi posseggo in base alle monete possedute
     */
    private fun getTotalValue(countries: ArrayList<CECountry>): Double {
        return countries.sumOf { it.getTotal() }
    }

    private fun initRecyclerView() {
        binding.rvStatistics.layoutManager = LinearLayoutManager(context)
    }

    private fun setRecyclerViewAdapter(statistics: ArrayList<Statistics>) {
        binding.rvStatistics.adapter = StatisticsAdapter(statistics)
    }

    override fun onGetCountries(countries: ArrayList<CECountry>) {
        this.ceCountries = countries
        setRecyclerViewAdapter(computeStatistics(countries))
    }

}