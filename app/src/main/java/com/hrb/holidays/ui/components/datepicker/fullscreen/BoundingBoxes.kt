package com.hrb.holidays.ui.components.datepicker.fullscreen

import android.os.Parcel
import android.os.Parcelable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import kotlinx.parcelize.Parceler
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.WriteWith

private object RectMutableListParceler : Parceler<MutableList<Rect>> {
    override fun create(parcel: Parcel): MutableList<Rect> {
        val retrievedArray = floatArrayOf()
        val boxesList = mutableListOf<Rect>()
        parcel.readFloatArray(retrievedArray)
        for (i in (0..retrievedArray.size step 4)) {
            boxesList.add(
                Rect(
                    left = retrievedArray[i],
                    top = retrievedArray[i + 1],
                    right = retrievedArray[i + 2],
                    bottom = retrievedArray[i + 3]
                )
            )
        }
        return boxesList
    }

    override fun MutableList<Rect>.write(parcel: Parcel, flags: Int) {
        val l = this.flatMap { listOf(it.left, it.top, it.right, it.bottom) }
        parcel.writeFloatArray(l.toFloatArray())
    }
}

@Parcelize
internal data class DayBoundingBoxes(
    val boxes: @WriteWith<RectMutableListParceler>() MutableList<Rect> = mutableListOf()
) : Parcelable

internal class DayBoundingBoxesState(
    private val onClick: (Int) -> Unit,
    private val dayBoundingBoxes: DayBoundingBoxes = DayBoundingBoxes(),
) {
    fun click(offset: Offset) {
        dayBoundingBoxes.boxes.forEachIndexed { index, box ->
            if (box.contains(offset)) {
                onClick(index)
            }
        }
    }

    fun addDay(boundingBox: Rect) = dayBoundingBoxes.takeIf {
        boundingBox !in dayBoundingBoxes.boxes
    }?.boxes?.add(boundingBox)

    fun getBoxOrNull(index: Int): Rect? = dayBoundingBoxes.boxes.getOrNull(index)

    companion object {
        @Suppress("UNCHECKED_CAST")
        fun saver(onClick: (Int) -> Unit): Saver<DayBoundingBoxesState, *> = Saver(
            save = {
                mapOf("days" to it.dayBoundingBoxes)
            },
            restore = {
                DayBoundingBoxesState(
                    onClick = onClick,
                    dayBoundingBoxes = it["days"] as DayBoundingBoxes,
                )
            }
        )
    }
}

@Composable
internal fun rememberDayBoundingBoxesState(
    key1: Any?,
    onClick: (Int) -> Unit
): DayBoundingBoxesState {
    return rememberSaveable(key1, saver = DayBoundingBoxesState.saver(onClick)) {
        DayBoundingBoxesState(
            onClick = onClick
        )
    }
}
