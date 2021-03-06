package com.example.collezioneeuro.model.repository

import com.example.collezioneeuro.model.CECoin
import com.example.collezioneeuro.model.CECountry
import io.realm.*
import io.realm.annotations.PrimaryKey
import io.realm.exceptions.RealmException

open class RealmCountry(
    @PrimaryKey
    var country: String = "",
    var countryTag: String = "",
    var coins: RealmList<RealmCoin> = RealmList(),
    var drawableId: Int? = null,
    var drawableUrl: String? = null
) : RealmObject()

open class RealmCoin(
    var value: Double = 0.01,
    var owned: Boolean = false,
    var drawableId: Int? = null,
    var drawableUrl: String? = null
) : RealmObject()

class CERealmRepository : CERepositoryInterface {

    /**
     * Mappa la lista di monete di CE in una RealmList di monete di Realm
     */
    private fun mapCECoinsToRealmCoin(
        realm: Realm,
        ceCoins: ArrayList<CECoin>
    ): RealmList<RealmCoin> {
        val realmCoins = RealmList<RealmCoin>()
        for (ceCoin in ceCoins) {
            val realmCoin = realm.createObject(RealmCoin::class.java)
            realmCoin.value = ceCoin.value
            realmCoin.owned = ceCoin.owned
            realmCoin.drawableId = ceCoin.drawableId
            realmCoin.drawableUrl = ceCoin.drawableUrl
            realmCoins.add(realmCoin)
        }
        return realmCoins
    }

    /**
     * Mappa la una country di CE in una country di Realm
     */
    private fun mapCECountryToRealmCountry(realm: Realm, ceCountry: CECountry): RealmModel {
        val realmCountry = realm.createObject(RealmCountry::class.java, ceCountry.country)
        realmCountry.countryTag = ceCountry.countryTag
        realmCountry.coins = mapCECoinsToRealmCoin(realm, ceCountry.coins)
        realmCountry.drawableId = ceCountry.drawableId
        realmCountry.drawableUrl = ceCountry.drawableUrl
        return realmCountry
    }

    /**
     * Mappa la lista di monete di RealmList in una lista di monete di CE
     */
    private fun mapRealmCoinsToCECoins(
        results: RealmList<RealmCoin>
    ): ArrayList<CECoin> {
        val ceCoins = ArrayList<CECoin>()
        for (realmCoin in results) {
            val ceCoin = CECoin(
                realmCoin.value,
                realmCoin.drawableId,
                realmCoin.owned,
                realmCoin.drawableUrl
            )
            ceCoins.add(ceCoin)
        }
        return ceCoins
    }

    /**
     * Mappa una country di Realm in una country di CE
     */
    private fun mapRealmCountryToCECountry(
        results: RealmResults<RealmCountry>
    ): ArrayList<CECountry> {
        val ceCountries = ArrayList<CECountry>()
        for (ceCountry in results) {
            ceCountries.add(
                CECountry(
                    ceCountry.country,
                    CECountry.getTag(ceCountry.country),
                    mapRealmCoinsToCECoins(ceCountry.coins),
                    ceCountry.drawableId,
                    ceCountry.drawableUrl
                )
            )
        }
        return ceCountries
    }

    override suspend fun saveCountry(ceCountry: CECountry) {
        val realm = Realm.getDefaultInstance()
        realm.executeTransaction {
            mapCECountryToRealmCountry(it, ceCountry)
        }
        realm.close()
    }

    override suspend fun saveCountries(ceCountries: ArrayList<CECountry>) {
        for (ceCountry in ceCountries)
            saveCountry(ceCountry)
    }

    override suspend fun editCountry(oldCountry: CECountry, newCountry: CECountry) {
        val realm = Realm.getDefaultInstance()
        realm.executeTransaction {
            it.insertOrUpdate(
                RealmCountry(
                    newCountry.country,
                    CECountry.getTag(newCountry.countryTag),
                    mapCECoinsToRealmCoin(realm, newCountry.coins),
                    newCountry.drawableId,
                    newCountry.drawableUrl
                )
            )
        }
        realm.close()
    }

    override suspend fun getCountries(): ArrayList<CECountry> {
        val realm = Realm.getDefaultInstance()
        val results = realm.where(RealmCountry::class.java).findAll()
        val response = mapRealmCountryToCECountry(results)
        realm.close()
        return response
    }

    override suspend fun setOwned(ceCountry: CECountry, ceCoin: CECoin, owned: Boolean) {
        editCountry(ceCountry, ceCountry)
    }

    override suspend fun clearAndSet(ceCountries: ArrayList<CECountry>) {
        clear()
        saveCountries(CEFakeRepository.countries)
    }

    override suspend fun clear() {
        val realm = Realm.getDefaultInstance()
        try {
            realm.executeTransaction {
                it.delete(RealmCountry::class.java)
            }
        } catch (exception: RealmException) {
        }
        realm.close()
    }

}