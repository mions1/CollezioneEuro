package com.example.collezioneeuro.utils.fileutils

import android.content.Intent
import android.net.Uri
import android.provider.DocumentsContract
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider.getUriForFile
import androidx.core.net.toUri
import java.io.*

/**
 * Questa classe gestisce i metodi che lavorano con i file.
 */
open class CEFileUtils {

    companion object {
        /* --------------- Sharing ---------------------- */
        /**
         * Ritorna l'intent da startare per condividere un file dato l'uri
         */
        fun getShareFileIntent(uri: Uri, shareType: String): Intent {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_STREAM, uri)
                type = shareType
            }
            return Intent.createChooser(sendIntent, null)
        }

        /* --------------- Save and Create ---------------------- */

        /**
         * Crea il testo passato in un file di testo e lo salva
         */
        fun saveAndCreateTextFile(directory: File, fileName: String, text: String): File {
            val file = createTextFile(directory, fileName, text)
            saveFile(file)
            return file
        }

        /**
         * Crea il testo passato in un file di testo
         */
        protected fun createTextFile(directory: File, fileName: String, text: String): File {
            val file = File(directory, fileName)
            file.writeText(text)
            return file
        }

        /**
         * Salva il file passato
         */
        fun saveFile(file: File) {
            file.createNewFile()
        }

        /**
         * Ritorna l'intent per il salvataggio di un file
         */
        fun getSaveFileIntent(file: File): Intent {
            val uri = file.toUri()
            val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = "text/json"
                putExtra(Intent.EXTRA_TITLE, file.name)
                putExtra(DocumentsContract.EXTRA_INITIAL_URI, uri)
            }
            return intent
        }

        /* ----------------- Pick file ------------------- */

        /**
         * Ritorna l'intent per l'apertura di un file di testo
         */
        fun getOpenFileIntent(): Intent {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                //addCategory(Intent.CATEGORY_OPENABLE)
                type = "*/*"
            }
            return Intent.createChooser(intent, null)
        }

        /* ---------------- Edit file ------------------ */

        /**
         * Modifica un file di testo mettendoci il testo passato
         */
        fun putTextIntoFile(activity: AppCompatActivity, uri: Uri, text: String) {
            try {
                activity.contentResolver.openFileDescriptor(uri, "w")?.use {
                    FileOutputStream(it.fileDescriptor).use { fileOutputStream ->
                        val outputStreamWriter = OutputStreamWriter(fileOutputStream)
                        outputStreamWriter.append(text)
                        outputStreamWriter.close()
                    }
                }
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }

        /* ----------- Read file ----------------- */

        @Throws(IOException::class)
        fun readTextFromUri(activity: AppCompatActivity, uri: Uri): String {
            val stringBuilder = StringBuilder()
            activity.contentResolver.openInputStream(uri)?.use { inputStream ->
                BufferedReader(InputStreamReader(inputStream)).use { reader ->
                    var line: String? = reader.readLine()
                    while (line != null) {
                        stringBuilder.append(line)
                        line = reader.readLine()
                    }
                }
            }
            return stringBuilder.toString()
        }


        /* --------------- Other ---------------------- */
        /**
         * Restituisce l'uri dato il percorso del file
         */
        protected fun getFileUri(
            activity: AppCompatActivity,
            directory: File,
            fileName: String
        ): Uri {
            val newFile = File(directory, fileName)
            return getUriForFile(activity, "com.example.collezioneeuro.fileprovider", newFile)
        }

    }
}