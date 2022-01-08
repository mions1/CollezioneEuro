package com.example.collezioneeuro.ui.activity.activityresultcontract

import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import com.example.collezioneeuro.utils.fileutils.CEFileUtils

/**
 * Gestisce l'onactivityresult per l'activity che apre un file
 */
class OpenFileActivityResultContract : ActivityResultContract<Intent, Intent>() {

    /**
     * Restituisce l'intent di default per l'apertura di un file
     */
    companion object {
        fun newIntent(): Intent = CEFileUtils.getOpenFileIntent()
    }

    /**
     * Apre l'intent passato in input, praticamente quello creato da newIntent (perché di base si passa quello)
     */
    override fun createIntent(context: Context, input: Intent): Intent = input

    /**
     * Restituisce l'intent del onResult, praticamente tiene i dati dell'uri del file selezionato
     */
    override fun parseResult(resultCode: Int, intent: Intent?): Intent? = intent

}