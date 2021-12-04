package com.example.collezioneeuro.presenter

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

interface DispatcherProvider {
    fun io(): CoroutineDispatcher
    fun main(): CoroutineDispatcher
}

/**
 * Restituisce il dispatcher da utilizzare.
 * Classe utile per poter fare i test tramite mock, per le interfacce.
 *
 * Vedi com.example.collezioneeuro.presenter.CEPresenter
 */
class RuntimeDispatcherProvider : DispatcherProvider {
    override fun io() = Dispatchers.IO
    override fun main() = Dispatchers.Main
}