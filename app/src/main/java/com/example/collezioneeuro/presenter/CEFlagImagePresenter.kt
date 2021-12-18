package com.example.collezioneeuro.presenter

import com.example.collezioneeuro.contract.CEFlagImageContract
import com.example.collezioneeuro.model.repository.image.CEFlagImageRepositoryInterface
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.nodes.Document
import kotlin.coroutines.CoroutineContext

class CEFlagImagePresenter(
    private val dispatcherProvider: DispatcherProvider,
    private val imageRepository: CEFlagImageRepositoryInterface
) : CoroutineScope, CEFlagImageContract.Presenter {

    private var view: CEFlagImageContract.View? = null
    override val coroutineContext: CoroutineContext
        get() = SupervisorJob() + dispatcherProvider.main()

    /**
     * Salva la view chiamante, così da poterla notificare con le callback se necessario
     */
    override fun bindView(view: CEFlagImageContract.View) {
        this.view = view
    }


    override fun getHtmlUrl(countryTag: String): String {
        return imageRepository.getHtmlUrl(countryTag)
    }

    override fun getHtmlCode(url: String, countryName: String) {
        launch {
            withContext(dispatcherProvider.io()) {
                val document = imageRepository.getHtml(url)
                document?.let { view?.onGetHtml(it, countryName) }
            }
        }

    }

    override fun getUrlImage(document: Document, countryName: String): String? {
        return imageRepository.getImageUrl(document, countryName)
    }

}