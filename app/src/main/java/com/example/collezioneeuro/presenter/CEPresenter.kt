package com.example.collezioneeuro.presenter

import com.example.collezioneeuro.contract.CEContract
import com.example.collezioneeuro.model.repository.CERepositoryInterface
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class CEPresenter(
    private val dispatcherProvider: DispatcherProvider,
    private val repository: CERepositoryInterface
) : CoroutineScope, CEContract.Presenter {

    private var view: CEContract.View? = null

    override val coroutineContext: CoroutineContext
        get() = SupervisorJob() + dispatcherProvider.main()

    /**
     * Salva la view chiamante, così da poterla notificare con le callback se necessario
     */
    override fun bindView(view: CEContract.View) {
        this.view = view
    }

    /**
     * Recupera le countries dal repository e poi notifica la view chiamante tramite la callback
     * onGetCountries.
     */
    override fun getCountries() {
        launch {
            val countries = repository.getCountries()
            view?.onGetCountries(countries)
        }
    }

}