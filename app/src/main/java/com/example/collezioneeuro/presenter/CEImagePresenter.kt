package com.example.collezioneeuro.presenter

import com.example.collezioneeuro.contract.CEImageContract
import com.example.collezioneeuro.model.repository.image.CECoinImageRepositoryInterface
import kotlinx.coroutines.*
import org.jsoup.nodes.Document
import kotlin.coroutines.CoroutineContext

class CEImagePresenter(
    private val dispatcherProvider: DispatcherProvider,
    private val imageRepository: CECoinImageRepositoryInterface
) : CoroutineScope, CEImageContract.Presenter {

    private var view: CEImageContract.View? = null
    override val coroutineContext: CoroutineContext
        get() = SupervisorJob() + dispatcherProvider.main()

    /**
     * Salva la view chiamante, così da poterla notificare con le callback se necessario
     */
    override fun bindView(view: CEImageContract.View) {
        this.view = view
    }


    override fun getHtmlUrl(countryTag: String): String {
        return imageRepository.getHtmlUrl(countryTag)
    }

    override fun getHtmlCode(url: String) {
        launch {
            withContext(Dispatchers.IO) {
                val document = imageRepository.getHtml(url)
                document?.let { view?.onGetHtml(it) }
            }
        }

    }

    override fun getUrlImage(document: Document, value: Double): String? {
        return imageRepository.getImageUrl(document, value)
    }

}