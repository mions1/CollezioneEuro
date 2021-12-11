package com.example.collezioneeuro.ui

import android.app.Application
import io.realm.Realm
import io.realm.RealmConfiguration

/**
 * Attualmente utilizzata solo per inizializzare Realm
 */
class CollezioneEuroApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
        val realmConfiguration = RealmConfiguration.Builder().allowWritesOnUiThread(true).build()
        Realm.setDefaultConfiguration(realmConfiguration)
    }
}