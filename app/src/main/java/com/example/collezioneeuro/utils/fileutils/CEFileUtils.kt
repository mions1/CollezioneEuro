package com.example.collezioneeuro.utils.fileutils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.core.content.FileProvider.getUriForFile
import com.example.collezioneeuro.R
import java.io.File

/**
 * Questa classe gestisce i metodi che lavorano con i file.
 * Da estendere se si vuole fare un wrap dei metodi cosi da renderli più ad alto livello ed adhoc per lo scopo.
 */
open class CEFileUtils(val context: Context) {

    /* --------------- Sharing ---------------------- */
    /**
     * Condivide un file dato l'uri
     */
    protected fun shareFile(uri: Uri, shareType: String) {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_STREAM, uri)
            type = shareType
        }
        val shareIntent = Intent.createChooser(sendIntent, null)
        context.startActivity(shareIntent)
    }

    /**
     * Condivide un file dato il file
     */
    protected fun shareFile(file: File, shareType: String) {
        val uri = getFileUri(file)
        shareFile(uri, shareType)
    }

    /* --------------- Save and Create ---------------------- */
    /**
     * Salva il testo passato in un file di testo
     */
    protected fun saveTextFile(directory: File, fileName: String, text: String): File {
        val file = File(directory, fileName)
        file.writeText(text)
        this.saveFile(file)
        return file
    }

    /**
     * Salva il file passato
     */
    protected fun saveFile(file: File) {
        file.createNewFile()
        Toast.makeText(
            context,
            context.getString(R.string.toast_file_salvato_in) + " ${file.absolutePath}",
            Toast.LENGTH_LONG
        ).show()
    }


    /* --------------- Other ---------------------- */
    /**
     * Restituisce l'uri dato il percorso del file
     */
    protected fun getFileUri(directory: File, fileName: String): Uri {
        val newFile = File(directory, fileName)
        return getUriForFile(context, "com.example.collezioneeuro.fileprovider", newFile)
    }

    /**
     * Restituisce l'uri dato il percorso del file
     */
    protected fun getFileUri(file: File): Uri {
        return getUriForFile(context, "com.example.collezioneeuro.fileprovider", file)
    }

}