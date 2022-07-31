package com.hrb.holidays.ui.views.office

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import com.hrb.holidays.app.presenters.office.OfficeWeekScreenPresenter
import com.hrb.holidays.business.entities.office.OfficeDay
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel
import java.time.DayOfWeek
import java.time.LocalTime

@Composable
fun WeekCalendarEditorScreenActivity(
    controller: OfficeWeekScreenPresenter = getViewModel()
) {
    val scope = rememberCoroutineScope()
    WeekCalendarEditorScreen(
        days = controller.uiState.weekDays,
        onChangeTime = { startTime: LocalTime, endTime: LocalTime, day: DayOfWeek ->
            scope.launch {
                controller.updateTimeForDay(
                    startTime,
                    endTime,
                    day
                )
            }
        }
    )
}

@Composable
fun WeekCalendarEditorScreen(
    days: Set<OfficeDay>,
    onChangeTime: (startTime: LocalTime, endTime: LocalTime, day: DayOfWeek) -> Unit,
) {
    WeekCalendarEditor(days, onChangeTime)
}
