package com.example.collezioneeuro.ui.activity.activityresultcontract

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import com.example.collezioneeuro.utils.fileutils.CEFileUtils
import java.io.File

/**
 * Gestisce l'onactivityresult per l'activity che salva un file
 */
class CreateFileActivityResultContract : ActivityResultContract<Intent, Intent>() {

    private var fileContent: String? = null

    companion object {

        const val EXTRA_FILE_CONTENT = "EXTRA_FILE_CONTENT"

        fun newIntent(file: File, fileContent: String? = null): Intent {
            val intent = CEFileUtils.getSaveFileIntent(file)
            intent.putExtra(EXTRA_FILE_CONTENT, fileContent)
            return intent
        }
    }

    override fun createIntent(context: Context, input: Intent): Intent {
        fileContent = input.getStringExtra(EXTRA_FILE_CONTENT)
        return input
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Intent? {
        return when (resultCode) {
            Activity.RESULT_OK -> {
                intent?.putExtra(EXTRA_FILE_CONTENT, fileContent)
            }
            else -> null
        }
    }

}