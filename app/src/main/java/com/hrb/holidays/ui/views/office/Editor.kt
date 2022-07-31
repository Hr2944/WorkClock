package com.hrb.holidays.ui.views.office

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.hrb.holidays.business.entities.office.OfficeDay
import com.hrb.holidays.ui.modifiers.frame
import java.time.DayOfWeek
import java.time.LocalTime


@Composable
fun WeekCalendarEditor(
    days: Set<OfficeDay>,
    onChangeTime: (startTime: LocalTime, endTime: LocalTime, day: DayOfWeek) -> Unit,
    daysPerRow: Int = 3
) {
    Column {

        val nbOfRows =
            if (days.size % daysPerRow == 0) (days.size / daysPerRow)
            else (days.size / daysPerRow + 1)

        for (rowIndex in 0 until nbOfRows) {
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                val rowSize = minOf(daysPerRow, days.size - rowIndex * daysPerRow)
                for (dayIndex in 0 until daysPerRow) {
                    val day = days.elementAtOrNull(rowIndex * daysPerRow + dayIndex)

                    if (day != null) {
                        Day(
                            modifier = Modifier
                                .fillMaxHeight(1f / (6 - rowIndex))
                                .aspectRatio(1f)
                                .frame(
                                    drawLines = getFrameLinesForPlaceInRow(
                                        rowSize = rowSize,
                                        placeInRow = dayIndex + 1,
                                    ).toTypedArray()
                                ),
                            day = day.dayOfWeek,
                            startTime = day.startAt,
                            endTime = day.endAt,
                            onChangeTime = onChangeTime
                        )
                    }
                }
            }
        }
    }
}
