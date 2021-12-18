package com.example.collezioneeuro.model

class CECountryConstants {

    enum class Countries(val itName: String, val tag: String, val enName: String) {
        ANDORRA("Andorra", "AD", "Andorra"),
        AUSTRIA("Austria", "AT", "Austria"),
        BELGIO("Belgio", "BE", "Belgium"),
        CIPRO("Cipro", "CY", "Cyprus"),
        VATICANO("Città del Vaticano", "VA", "the Vatican City"),
        ESTONIA("Estonia", "ET", "Estonia"),
        FINLANDIA("Finlandia", "FI", "Finland"),
        FRANCIA("Francia", "FR", "France"),
        GERMANIA("Germania", "DE", "Germany"),
        GRECIA("Grecia", "GR", "Greece"),
        IRLANDA("Irlanda", "IE", "Ireland"),
        ITALIA("Italia", "IT", "Italy"),
        LETTONIA("Lettonia", "LV", "Latvia"),
        LITUANIA("Lituania", "LT", "Lithuania"),
        LUSSEMBURGO("Lussemburgo", "LU", "Luxemburg"),
        MALTA("Malta", "MT", "Malta"),
        PAESI_BASSI("Paesi Bassi", "NL", "Netherlands"),
        PORTOGALLO("Portogallo", "PT", "Portugal"),
        MONACO("Principato di Monaco", "MO", "Monaco"),
        SAN_MARINO("Repubblica di San Marino", "SM", "San Marino"),
        SLOVACCHIA("Slovacchia", "SK", "Slovakia"),
        SLOVENIA("Slovenia", "SL", "Slovenia"),
        SPAGNA("Spagna", "ES", "Spain"),
    }

    companion object {

        val countriesTag = arrayListOf(
            Pair(Countries.ANDORRA.itName, Countries.ANDORRA.tag),
            Pair(Countries.AUSTRIA.itName, Countries.AUSTRIA.tag),
            Pair(Countries.BELGIO.itName, Countries.BELGIO.tag),
            Pair(Countries.CIPRO.itName, Countries.CIPRO.tag),
            Pair(Countries.VATICANO.itName, Countries.VATICANO.tag),
            Pair(Countries.ESTONIA.itName, Countries.ESTONIA.tag),
            Pair(Countries.FINLANDIA.itName, Countries.FINLANDIA.tag),
            Pair(Countries.FRANCIA.itName, Countries.FRANCIA.tag),
            Pair(Countries.GERMANIA.itName, Countries.GERMANIA.tag),
            Pair(Countries.GRECIA.itName, Countries.GRECIA.tag),
            Pair(Countries.IRLANDA.itName, Countries.IRLANDA.tag),
            Pair(Countries.ITALIA.itName, Countries.ITALIA.tag),
            Pair(Countries.LETTONIA.itName, Countries.LETTONIA.tag),
            Pair(Countries.LITUANIA.itName, Countries.LITUANIA.tag),
            Pair(Countries.LUSSEMBURGO.itName, Countries.LUSSEMBURGO.tag),
            Pair(Countries.MALTA.itName, Countries.MALTA.tag),
            Pair(Countries.PAESI_BASSI.itName, Countries.PAESI_BASSI.tag),
            Pair(Countries.PORTOGALLO.itName, Countries.PORTOGALLO.tag),
            Pair(Countries.MONACO.itName, Countries.MONACO.tag),
            Pair(Countries.SAN_MARINO.itName, Countries.SAN_MARINO.tag),
            Pair(Countries.SLOVACCHIA.itName, Countries.SLOVACCHIA.tag),
            Pair(Countries.SLOVENIA.itName, Countries.SLOVENIA.tag),
            Pair(Countries.SPAGNA.itName, Countries.SPAGNA.tag),
        )

        val countriesEnglish = arrayListOf(
            Pair(Countries.ANDORRA.itName, Countries.ANDORRA.enName),
            Pair(Countries.AUSTRIA.itName, Countries.AUSTRIA.enName),
            Pair(Countries.BELGIO.itName, Countries.BELGIO.enName),
            Pair(Countries.CIPRO.itName, Countries.CIPRO.enName),
            Pair(Countries.VATICANO.itName, Countries.VATICANO.enName),
            Pair(Countries.ESTONIA.itName, Countries.ESTONIA.enName),
            Pair(Countries.FINLANDIA.itName, Countries.FINLANDIA.enName),
            Pair(Countries.FRANCIA.itName, Countries.FRANCIA.enName),
            Pair(Countries.GERMANIA.itName, Countries.GERMANIA.enName),
            Pair(Countries.GRECIA.itName, Countries.GRECIA.enName),
            Pair(Countries.IRLANDA.itName, Countries.IRLANDA.enName),
            Pair(Countries.ITALIA.itName, Countries.ITALIA.enName),
            Pair(Countries.LETTONIA.itName, Countries.LETTONIA.enName),
            Pair(Countries.LITUANIA.itName, Countries.LITUANIA.enName),
            Pair(Countries.LUSSEMBURGO.itName, Countries.LUSSEMBURGO.enName),
            Pair(Countries.MALTA.itName, Countries.MALTA.enName),
            Pair(Countries.PAESI_BASSI.itName, Countries.PAESI_BASSI.enName),
            Pair(Countries.PORTOGALLO.itName, Countries.PORTOGALLO.enName),
            Pair(Countries.MONACO.itName, Countries.MONACO.enName),
            Pair(Countries.SAN_MARINO.itName, Countries.SAN_MARINO.enName),
            Pair(Countries.SLOVACCHIA.itName, Countries.SLOVACCHIA.enName),
            Pair(Countries.SLOVENIA.itName, Countries.SLOVENIA.enName),
            Pair(Countries.SPAGNA.itName, Countries.SPAGNA.enName),
        )
    }

}