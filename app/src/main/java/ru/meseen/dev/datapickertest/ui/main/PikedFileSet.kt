package ru.meseen.dev.datapickertest.ui.main

class PikedFileSet(
    val maxQuantity: Int,
    maxDataSize: Long, conversion: Conversion
) {
    private val innerSet: MutableSet<PikedFile> = mutableSetOf()
    var curFilesSize = 0L
        private set

    val maxSize = maxDataSize * conversion.long

    fun size() = innerSet.size


    fun addAll(vararg element: PikedFile): List<PikedFileSetResult> =
        element.map { add(it) }

    fun add(element: PikedFile): PikedFileSetResult {
        return when {
            innerSet.size + 1 > maxQuantity -> {
                PikedFileSetResult.Error(
                    "Превышено общее клоличество " +
                            "элементов cur $curFilesSize max $maxSize "
                )
            }
            element.nameAndSize.second + curFilesSize > maxSize -> {
                PikedFileSetResult.Error(
                    "Превышен обищий объем" +
                            "max $maxSize cur $curFilesSize"
                )
            }
            else -> {
                curFilesSize += element.nameAndSize.second
                innerSet.add(element)
                PikedFileSetResult.Success
            }
        }
    }

    fun removeAll(vararg element: PikedFile): List<PikedFileSetResult> =
        element.map { remove(it) }

    fun remove(element: PikedFile): PikedFileSetResult =
        if (innerSet.remove(element)) {
            curFilesSize -= element.nameAndSize.second
            PikedFileSetResult.Success
        } else {
            PikedFileSetResult.Error("Удаление не удалось")
        }

    fun toList(): List<PikedFile> =
        innerSet.toList()

    fun clear() = innerSet.clear().also {
        curFilesSize = 0L
    }

    sealed class PikedFileSetResult {
        data class Error(val error: String) : PikedFileSetResult()
        object Success : PikedFileSetResult()
    }
}