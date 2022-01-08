package com.example.collezioneeuro.ui.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.example.collezioneeuro.R
import com.example.collezioneeuro.contract.CEContract
import com.example.collezioneeuro.databinding.ActivityMainBinding
import com.example.collezioneeuro.listener.UpdateCountriesListener
import com.example.collezioneeuro.model.CECountry
import com.example.collezioneeuro.model.repository.CEFakeRepository
import com.example.collezioneeuro.model.repository.CERepositoryInterface
import com.example.collezioneeuro.presenter.CEPresenter
import com.example.collezioneeuro.presenter.RuntimeDispatcherProvider
import com.example.collezioneeuro.ui.activity.activityresultcontract.CreateFileActivityResultContract
import com.example.collezioneeuro.ui.activity.activityresultcontract.OpenFileActivityResultContract
import com.example.collezioneeuro.ui.fragment.CoinsFragment
import com.example.collezioneeuro.ui.fragment.HomeFragment
import com.example.collezioneeuro.ui.fragment.StatisticsFragment
import com.example.collezioneeuro.utils.fileutils.CEFileUtils
import com.example.collezioneeuro.utils.fileutils.CEWrapperExportFileUtils
import com.example.collezioneeuro.utils.jsonutils.CEJsonUtilsContract
import com.example.collezioneeuro.utils.jsonutils.CEJsonUtilsPresenter

class MainActivity : AppCompatActivity(), ActivityInterface, ActionBarActivityInterface,
    CEContract.View, CEJsonUtilsContract.View, UpdateCountriesListener {

    /**
     * Gestione dei bottoni della bottom navigation
     */
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
    private lateinit var jsonUtilsPresenter: CEJsonUtilsContract.Presenter

    private val ceExportFileUtils = CEWrapperExportFileUtils(this)

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
        jsonUtilsPresenter = CEJsonUtilsPresenter(RuntimeDispatcherProvider())
        jsonUtilsPresenter.bindView(this)

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
                    export()
                    Log.println(Log.DEBUG, "[MainActivity]", "[NavUi] - export pressed")
                    true
                }
                R.id.dmImport -> {
                    import()
                    Log.println(Log.DEBUG, "[MainActivity]", "[NavUi] - import pressed")
                    true
                }

                // aggiungi qui la gestione al click delle varie voci del menù

                else -> false
            }
        }
    }

    private fun openDrawer() {
        binding.drawerLayout.openDrawer(binding.navView)
    }

    private fun closeDrawer() {
        binding.drawerLayout.closeDrawer(binding.navView)
    }

    /**
     * Gestisce, in base al bottone impostato al momento del click, il comportamento da effettuare
     */
    override fun onSupportNavigateUp(): Boolean {
        return when (actionBarIconStatus) {
            ActionBarIconStatus.BURGER -> {
                openDrawer()
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

    /* --------------- Gestione bottoni drawer ------------ */

    /**
     * Salva e condividi l'export
     */
    private fun export() {
        closeDrawer()
        showProgressBar()
        jsonUtilsPresenter.getJsonCountries(countries)
    }

    /**
     * Quando il json è pronto, nasconde la progress bar e lo esporta
     */
    override fun onGetJson(json: String) {
        hideProgressBar()

        launchActivityCreateExportFile.launch(
            CreateFileActivityResultContract.newIntent(
                ceExportFileUtils.saveExportFilePrivately(
                    json
                ), json
            )
        )
    }

    /**
     * Quando il json è stato letto, nasconde la progress bar e salva nel repo le countries appena importate
     */
    override fun onReadJson(ceCountries: ArrayList<CECountry>) {
        presenter.clearAndSet(ceCountries)
        onUpdateCountries(ceCountries)
        // dico a tutti i fragment che osservano la lista che è stata aggiornata
        val fragments = supportFragmentManager.fragments
        for (fragment in fragments)
            (fragment as? UpdateCountriesListener)?.onUpdateCountries(ceCountries)
        hideProgressBar()
    }

    /**
     * Importa il json
     */
    private fun import() {
        closeDrawer()
        showProgressBar()
        launchActivityOpenExportedFile.launch(
            OpenFileActivityResultContract.newIntent()
        )
    }

    /* --------------- Other methods ---------------------- */

    /**
     * Quando recupero le countries dal repo, se non sono vuote le imposto come variabili cosi da poterle
     * usare
     */
    override fun onGetCountries(countries: ArrayList<CECountry>) {
        if (countries.isEmpty())
            presenter.clearAndSet(CEFakeRepository.getOriginalCountries())
        else {
            this.countries.clear()
            this.countries.addAll(countries)
        }
    }

    /**
     * Aggiorna la variabile locale countries
     */
    override fun onUpdateCountries(ceCountries: ArrayList<CECountry>) {
        this.countries.clear()
        this.countries.addAll(ceCountries)
    }

    private fun showProgressBar() {
        binding.includeProgressBar.progressOverlay.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        binding.includeProgressBar.progressOverlay.visibility = View.GONE
    }

    /* --------------- OnActivityResult ------------------- */
    /**
     * Gestisce il ritorno dall'activity che salva il file json esportato.
     * L'activity salva il file vuoto, dopo averlo salvato lo riempio e lo condivido
     */
    private val launchActivityCreateExportFile =
        registerForActivityResult(CreateFileActivityResultContract()) { result ->
            val text = result?.getStringExtra(CreateFileActivityResultContract.EXTRA_FILE_CONTENT)
            result?.data?.let { uri ->
                text?.let { jsonText ->
                    ceExportFileUtils.writeExportedFile(uri, jsonText)
                    ceExportFileUtils.shareExportedFile(uri)
                    Log.println(
                        Log.DEBUG,
                        "[MainActivity]",
                        "[onActivityResult] [createExportFile] - $uri $jsonText"
                    )
                }
            }
        }

    /**
     * Gestisce il ritorno dall'activity che apre il file json esportato.
     * L'activity apre il file, poi prendo il contenuto e lo deserializzo.
     * Poi verrà chiamato onReadJson e quello penserà a salvare le countries importate
     */
    private val launchActivityOpenExportedFile =
        registerForActivityResult(OpenFileActivityResultContract()) { result ->
            result?.data?.let { uri ->
                val text =
                    jsonUtilsPresenter.readCountryJson(CEFileUtils.readTextFromUri(this, uri))
                Log.println(
                    Log.DEBUG,
                    "[MainActivity]",
                    "[onActivityResult] [createExportFile] - $uri $text"
                )
            }
        }
}