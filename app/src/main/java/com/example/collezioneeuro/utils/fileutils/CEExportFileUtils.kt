package com.example.collezioneeuro.utils.fileutils

import android.content.Context
import com.example.collezioneeuro.model.CECountry
import com.example.collezioneeuro.utils.CEJsonUtils
import java.io.File

/**
 * Questa classe wrappa i metodi di CEFileUtils in modo che la gestione dei file sia ad-hoc per l'export
 * dei dati delle monete (quindi per salvare lo stato di owned delle monete)
 */
class CEExportFileUtils(context: Context) : CEFileUtils(context) {

    enum class DefaultFileName(val text: String) {
        EXPORT("export.json")
    }

    enum class Directory {
        EXTERNAL_FILES, FILES
    }

    /**
     * Crea, salva e condivide il file da esportare, quello che salva lo stato delle countries (quindi le monete owned).
     */
    fun shareExportFile(countries: ArrayList<CECountry>) {
        val file = saveExportFile(countries)
        shareFile(file, "text/json")
    }

    /**
     * Crea e salva il file da esportare, quello che salva lo stato delle countries (quindi le monete owned).
     * Ritorna il file salvato
     */
    fun saveExportFile(countries: ArrayList<CECountry>): File {
        val jsonCountries = CEJsonUtils.getJsonCountries(countries)
        val directory = getDirectory(Directory.EXTERNAL_FILES)
        return when {
            directory != null -> {
                saveTextFile(directory, DefaultFileName.EXPORT.text, jsonCountries)
            }
            else -> context.filesDir
        }
    }

    /**
     * Ritorna la cartella selezionata come File
     */
    private fun getDirectory(directory: Directory): File? {
        return when (directory) {
            Directory.EXTERNAL_FILES -> {
                context.getExternalFilesDir(null)
            }
            Directory.FILES -> {
                context.filesDir
            }
        }
    }

}