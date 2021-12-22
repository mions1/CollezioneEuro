package com.example.collezioneeuro.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.collezioneeuro.R
import com.example.collezioneeuro.contract.CEContract
import com.example.collezioneeuro.databinding.ActivityMainBinding
import com.example.collezioneeuro.model.CECountry
import com.example.collezioneeuro.model.repository.CEFakeRepository
import com.example.collezioneeuro.model.repository.CERepositoryInterface
import com.example.collezioneeuro.presenter.CEPresenter
import com.example.collezioneeuro.presenter.RuntimeDispatcherProvider
import com.example.collezioneeuro.ui.fragment.CoinsFragment
import com.example.collezioneeuro.ui.fragment.HomeFragment
import com.example.collezioneeuro.ui.fragment.StatisticsFragment

class MainActivity : AppCompatActivity(), ActivityInterface, CEContract.View {

    enum class BottomNavigationItem() {
        HOME, STATISTICS
    }

    lateinit var binding: ActivityMainBinding

    private lateinit var repository: CERepositoryInterface
    private lateinit var presenter: CEContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        repository = CEFakeRepository.SingleInstance
        presenter = CEPresenter(RuntimeDispatcherProvider(), repository)
        presenter.bindView(this)

        presenter.getCountries()

        setBottomNavigatorClickListener()
        replaceFragmentToHomeFragment()
    }

    private fun setBottomNavigatorClickListener() {
        binding.bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.bnmHome -> {
                    replaceFragmentToHomeFragment()
                    true
                }
                R.id.bnmStatistiche -> {
                    replaceFragmentToStatisticsFragment()
                    true
                }
                else -> false
            }
        }
    }

    /**
     * Imposta l'item indicato come attivo nella bottom navigation.
     * Usato quando da un'altra tab fai "onBackPressed"
     */
    override fun setWhichItemIsSelectedBottomNavigationMenu(item: BottomNavigationItem) {
        when (item) {
            BottomNavigationItem.HOME -> binding.bottomNavigation.selectedItemId = R.id.bnmHome
            BottomNavigationItem.STATISTICS -> binding.bottomNavigation.selectedItemId =
                R.id.bnmStatistiche
        }
    }

    private fun replaceFragmentToStatisticsFragment() {
        val fragment = StatisticsFragment.newInstance()
        supportFragmentManager.beginTransaction()
            .addToBackStack(StatisticsFragment.TAG)
            .replace(binding.fragment.id, fragment, StatisticsFragment.TAG)
            .commit()
    }

    override fun replaceFragmentToHomeFragment() {
        val fragment = HomeFragment.newInstance()
        supportFragmentManager.beginTransaction()
            .replace(binding.fragment.id, fragment, HomeFragment.TAG)
            .commit()
    }

    override fun replaceFragmentToCoinsFragment(ceCountry: CECountry) {
        val fragment = CoinsFragment.newInstance(ceCountry)
        supportFragmentManager.beginTransaction()
            .addToBackStack(CoinsFragment.TAG)
            .replace(binding.fragment.id, fragment, CoinsFragment.TAG)
            .commit()
    }

    override fun onGetCountries(countries: ArrayList<CECountry>) {
        if (countries.isEmpty())
            presenter.clear()
    }

}