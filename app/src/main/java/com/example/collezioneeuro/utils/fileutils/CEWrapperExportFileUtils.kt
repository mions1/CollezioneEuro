package com.example.collezioneeuro.utils.fileutils

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import java.io.File

/**
 * Questa classe wrappa i metodi di CEFileUtils in modo che la gestione dei file sia ad-hoc per l'export
 * dei dati delle monete (quindi per salvare lo stato di owned delle monete)
 */
class CEWrapperExportFileUtils(val activity: AppCompatActivity) : CEFileUtils() {

    enum class DefaultFileName(val text: String) {
        EXPORT("export.json")
    }

    enum class Directory {
        EXTERNAL_FILES, FILES
    }

    /**
     * Condivide il file passato nell'uri
     */
    fun shareExportedFile(uri: Uri) {
        activity.startActivity(getShareFileIntent(uri, "text/json"))
    }

    /**
     * Scrive il testo nel file passato
     */
    fun writeExportedFile(uri: Uri, text: String) {
        putTextIntoFile(activity, uri, text)
    }

    /**
     * Crea e salva il file da esportare, quello che salva lo stato delle countries (quindi le monete owned).
     * Lo salva nella cartella privata del progetto.
     * Ritorna il file salvato
     */
    fun saveExportFilePrivately(jsonExport: String): File {
        var directory: File
        getDirectory(Directory.EXTERNAL_FILES)?.let {
            directory = it
        }.run {
            directory = activity.filesDir
        }
        return saveAndCreateTextFile(directory, DefaultFileName.EXPORT.text, jsonExport)
    }

    fun openAnExportedFile() {
        getOpenFileIntent()
    }

    /**
     * Ritorna la cartella selezionata come File
     */
    private fun getDirectory(directory: Directory): File? {
        return when (directory) {
            Directory.EXTERNAL_FILES -> {
                activity.getExternalFilesDir(null)
            }
            Directory.FILES -> {
                activity.filesDir
            }
        }
    }

}