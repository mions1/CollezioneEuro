package com.example.collezioneeuro.model.repository

import com.example.collezioneeuro.R
import com.example.collezioneeuro.model.CECoin
import com.example.collezioneeuro.model.CECountry

/**
 * FakeRepository crea dei valori di default.
 * Utilizzato per effettuare delle prove usando dati in locale.
 * Utilizza il realm repository.
 */
class CEFakeRepository : CERepositoryInterface {

    companion object {
        val SingleInstance = CEFakeRepository()

        val countries = getOriginalCountries()

        /**
         * Recupera le hardcoded countries del fake repository
         */
        fun getOriginalCountries() =
            arrayListOf(
                CECountry("Italia", createItalianCoins(), R.drawable.flag_italia),
                CECountry("Spagna", CECountry.createCoins(), R.drawable.flag_default),
                CECountry("Grecia", CECountry.createCoins(), R.drawable.flag_default),
                CECountry("Germania", CECountry.createCoins(), R.drawable.flag_default),
                CECountry("Francia", CECountry.createCoins(), R.drawable.flag_default),
            )

        /**
         * Crea le monete ad-hoc per l'Italia
         */
        private fun createItalianCoins(): ArrayList<CECoin> {
            val coins = CECountry.createCoins()
            coins[0].drawableId = R.drawable.italia_001
            coins[0].owned = true
            coins[1].drawableId = R.drawable.italia_002
            coins[1].owned = true
            coins[2].drawableId = R.drawable.italia_005
            coins[3].drawableId = R.drawable.italia_010
            coins[3].owned = true
            coins[4].drawableId = R.drawable.italia_020
            coins[5].drawableId = R.drawable.italia_050
            coins[6].drawableId = R.drawable.italia_100
            coins[6].owned = true
            coins[7].drawableId = R.drawable.italia_200
            return coins
        }

    }

    override suspend fun saveCountries(ceCountries: ArrayList<CECountry>) {
        for (ceCountry in ceCountries) {
            saveCountry(ceCountry)
        }
    }

    override suspend fun saveCountry(ceCountry: CECountry) {
        CERealmRepository().saveCountry(ceCountry)
    }

    override suspend fun editCountry(oldCountry: CECountry, newCountry: CECountry) {
        CERealmRepository().editCountry(oldCountry, newCountry)
    }

    override suspend fun getCountries(): ArrayList<CECountry> {
        return CERealmRepository().getCountries()
    }

    override suspend fun setOwned(ceCountry: CECountry, ceCoin: CECoin, owned: Boolean) {
        CERealmRepository().setOwned(ceCountry, ceCoin, owned)
    }

    override suspend fun clear() {
        CERealmRepository().clear()
        countries.clear()
        countries.addAll(getOriginalCountries())
    }

}