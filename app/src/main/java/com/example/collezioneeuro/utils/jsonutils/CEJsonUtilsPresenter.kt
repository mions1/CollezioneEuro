package com.example.collezioneeuro.utils.jsonutils

import com.example.collezioneeuro.model.CECountry
import com.example.collezioneeuro.presenter.DispatcherProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class CEJsonUtilsPresenter(
    private val dispatcherProvider: DispatcherProvider
) : CoroutineScope, CEJsonUtilsContract.Presenter {

    private var view: CEJsonUtilsContract.View? = null

    override val coroutineContext: CoroutineContext
        get() = SupervisorJob() + dispatcherProvider.main()

    /**
     * Salva la view chiamante, così da poterla notificare con le callback se necessario
     */
    override fun bindView(view: CEJsonUtilsContract.View) {
        this.view = view
    }

    /**
     * Crea il json file dalle countries passate
     */
    override fun getJsonCountries(countries: ArrayList<CECountry>) {
        launch {
            val json = CEJsonUtils.getJsonCountries(countries)
            view?.onGetJson(json)
        }
    }

    /**
     * Legge il json file e crea la lista delle countries dal json letto
     */
    override fun readCountryJson(countryJson: String) {
        launch {
            val countries = CEJsonUtils.readCountryJson(countryJson)
            view?.onReadJson(countries)
        }
    }

}