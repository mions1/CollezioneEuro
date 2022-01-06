package com.example.collezioneeuro.ui.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
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
import com.example.collezioneeuro.utils.fileutils.CEExportFileUtils

class MainActivity : AppCompatActivity(), ActivityInterface, ActionBarActivityInterface,
    CEContract.View {

    enum class BottomNavigationItem() {
        HOME, STATISTICS
    }

    /**
     * Indica il tipo di bottone visualizzato al momento nell'action bar a sx
     */
    enum class ActionBarIconStatus {
        BURGER, BACK, NONE
    }

    lateinit var binding: ActivityMainBinding

    private lateinit var repository: CERepositoryInterface
    private lateinit var presenter: CEContract.Presenter

    private val countries: ArrayList<CECountry> = ArrayList()

    private var actionBarIconStatus: ActionBarIconStatus = ActionBarIconStatus.NONE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setSupportActionBar(binding.mainToolbar) // imposta la mia toolbar come actiontoolbar

        repository = CEFakeRepository.SingleInstance
        presenter = CEPresenter(RuntimeDispatcherProvider(), repository)
        presenter.bindView(this)

        presenter.getCountries()

        setNavigationDrawer()
        setBottomNavigatorClickListener()
        replaceFragmentToHomeFragment()
    }


    /* ------------------ Bottom Navigator methods ---------------------- */

    /**
     * Gestisce il comportamento dei bottoni premuti nel menù in basso
     * Aggiungere qui il comportamento di eventuali nuovi bottoni
     */
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


    /* ---------------- ReplaceFragment methods ------------------------ */

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


    /* ------------------------------ Toolbar and drawer methods -------------------------------- */
    /**
     * Imposta il titolo della toolbar
     */
    override fun setActionBarTitle(title: String) {
        supportActionBar?.title = title
    }

    /**
     * Imposta il sottotitolo della toolbar
     */
    override fun setActionBarSubtitle(subtitle: String) {
        supportActionBar?.subtitle = subtitle
    }

    /**
     * Imposta il pulsante burger menu ed abilita il menu
     */
    override fun enableDrawer() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true) // mostra il bottone
        supportActionBar?.setHomeAsUpIndicator(R.drawable.burger_menu)
        binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
        actionBarIconStatus = ActionBarIconStatus.BURGER
    }

    /**
     * Disabilita il menu e nasconde il pulsante
     */
    override fun disableDrawer() {
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        actionBarIconStatus = ActionBarIconStatus.NONE
    }

    /**
     * Imposta la freccia indietro invece che il menu
     */
    override fun setBackArrow() {
        disableDrawer()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.arrow_back)
        actionBarIconStatus = ActionBarIconStatus.BACK
    }

    /**
     * Imposta il navigation drawer (menù a scomparsa laterale).
     * Imposta il listener per gestire apertura, chiusura, bottone.
     * Gestisce alla selezione di uno dei bottoni del menù
     */
    private fun setNavigationDrawer() {
        val actionBarToggle = ActionBarDrawerToggle(this, binding.drawerLayout, 0, 0)

        binding.drawerLayout.addDrawerListener(actionBarToggle)
        actionBarToggle.syncState() // aggiorna il bottone in apertura e chiusura della drawer

        disableDrawer()

        binding.navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.dmExport -> {
                    shareExport()
                    Log.println(Log.DEBUG, "[MainActivity]", "[NavUi] - export pressed")
                    true
                }

                // aggiungi qui la gestione al click delle varie voci del menù

                else -> false
            }
        }
    }

    /**
     * Gestisce, in base al bottone impostato al momento del click, il comportamento da effettuare
     */
    override fun onSupportNavigateUp(): Boolean {
        return when (actionBarIconStatus) {
            ActionBarIconStatus.BURGER -> {
                binding.drawerLayout.openDrawer(binding.navView)
                true
            }
            ActionBarIconStatus.BACK -> {
                this.onBackPressed()
                true
            }
            ActionBarIconStatus.NONE -> {
                false
            }
        }
    }

    /* --------------- Other methods ---------------------- */

    override fun onGetCountries(countries: ArrayList<CECountry>) {
        if (countries.isEmpty())
            presenter.clear()
        else {
            this.countries.clear()
            this.countries.addAll(countries)
        }
    }

    /**
     * Aggiorna la variabile locale countries
     */
    override fun updateCountries(countries: ArrayList<CECountry>) {
        this.countries.clear()
        this.countries.addAll(countries)
    }

    /**
     * Salva e condividi l'export
     */
    private fun shareExport() {
        val ceExportFileUtils = CEExportFileUtils(this)
        ceExportFileUtils.shareExportFile(countries)
    }

}