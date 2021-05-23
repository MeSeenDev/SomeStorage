package ru.meseen.dev.datapickertest

import android.net.Uri
import org.junit.Assert.assertEquals
import org.junit.Test
import ru.meseen.dev.datapickertest.ui.main.Conversion
import ru.meseen.dev.datapickertest.ui.main.PikedFile
import ru.meseen.dev.datapickertest.ui.main.PikedFileSet

class PikedFileSetTest {

    @Test
    fun first() {
        val pikedFileSet = PikedFileSet(5, 30, Conversion.MB)
        val piSi = PikedFile(
            "photo" to 100000L,
            Uri.parse("content://com.android.providers.downloads.documents")
        )
        (0..10).forEach {
            pikedFileSet.add(piSi)
        }
        assertEquals(1, pikedFileSet.size())
    }

    @Test
    fun second() {
        val pikedFileSet = PikedFileSet(5, 30, Conversion.MB)
        val piSi = PikedFile(
            "photo" to 100000L,
            Uri.parse("content://com.android.providers.downloads.documents")
        )
        val piSis = PikedFile(
            "photoS" to 100000L,
            Uri.parse("content://com.android.providers.downloads.documents")
        )
        val piSiz = PikedFile(
            "Image" to 50000L,
            Uri.parse("content://com.android.providers.downloads.documents")
        )
        pikedFileSet.add(piSi)
        pikedFileSet.add(piSis)
        pikedFileSet.add(piSiz)
        assertEquals(250000L, pikedFileSet.curFilesSize)
        assertEquals(3, pikedFileSet.size())
    }

    @Test
    fun third() {
        val pikedFileSet = PikedFileSet(5, 30, Conversion.MB)
        val piSi = PikedFile(
            "photo" to 100000L,
            Uri.parse("content://com.android.providers.downloads.documents")
        )
        val piSis = PikedFile(
            "photoz" to 100000L,
            Uri.parse("content://com.android.providers.downloads.documents")
        )
        val piSiz = PikedFile(
            "Fotoz" to 100000L,
            Uri.parse("content://com.android.providers.downloads.documents")
        )
        val piSiq = PikedFile(
            "Fotoza" to 50000L,
            Uri.parse("content://com.android.providers.downloads.documents")
        )
        val piSir0 = PikedFile(
            "Image" to 63000L,
            Uri.parse("content://com.android.providers.downloads.documents")
        )
        val piSir1 = PikedFile(
            "Image" to 50L,
            Uri.parse("content://com.android.providers.downloads.documents")
        )
        val piSir2 = PikedFile(
            "Image" to 800000L,
            Uri.parse("content://com.android.providers.downloads.documents")
        )

        pikedFileSet.add(piSi)
        pikedFileSet.add(piSis)
        pikedFileSet.add(piSiz)
        pikedFileSet.add(piSiq)
        pikedFileSet.add(piSir0)
        pikedFileSet.add(piSir1)
        pikedFileSet.add(piSir2)
        assertEquals(5, pikedFileSet.size())
        assertEquals(413000L, pikedFileSet.curFilesSize)

    }
}