package ru.meseen.dev.datapickertest.ui.main

import android.content.ClipData
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.OpenableColumns
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class StorageRead(
    fragment: Fragment,
    private val pikedFileSet: PikedFileSet = PikedFileSet(5, 30, Conversion.MB)
) {

    private val context: Context = fragment.requireContext()
    private val contentResolver = context.contentResolver

    private val _listPickedFiles = MutableLiveData<List<PikedFile>>()
    val listPickedFiles: LiveData<List<PikedFile>> = _listPickedFiles

    val requestLauncher =
        fragment.registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            val resultIntent = it.data
            val clipData = resultIntent?.clipData
            if (resultIntent != null) {
                val uri = resultIntent.data
                if (uri != null) {
                    addSinglePikedFile(uri)
                }
                if (clipData != null) {
                    addSeveralPikedFile(clipData)
                }
            }
        }

    private fun addSinglePikedFile(uri: Uri) {
        val cursor = query(uri)
        cursor?.moveToFirst()
        if (cursor != null) {
            val result = pikedFileSet.add(PikedFile(getNameAndSize(cursor), uri))
            if (result is PikedFileSet.PikedFileSetResult.Success) {
                _listPickedFiles.value = pikedFileSet.toList()
            }
        }

    }

    private fun addSeveralPikedFile(clipData: ClipData) {
        clipData.forEachIndexed { _, item ->
            val uri = item.uri
            addSinglePikedFile(uri)
        }

    }

    private fun getNameAndSize(cursor: Cursor): Pair<String, Long> =
        cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)) to cursor.getLong(
            cursor.getColumnIndex(OpenableColumns.SIZE)
        )

    private fun query(uri: Uri) =
        contentResolver.query(uri, null, null, null, null)

    private fun ClipData.forEachIndexed(action: (index: Int, ClipData.Item) -> Unit) {
        for (i in 0 until itemCount) {
            action.invoke(i, getItemAt(i))
        }
    }
}

data class PikedFile(
    val nameAndSize: Pair<String, Long>,
    val uri: Uri
)

enum class Conversion(val long: Long) {
    MB(1_048_576L), KB(1024)
}


