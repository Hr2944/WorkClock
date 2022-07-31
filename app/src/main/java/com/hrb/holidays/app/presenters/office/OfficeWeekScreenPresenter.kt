package com.hrb.holidays.app.presenters.office

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.hrb.holidays.business.controllers.office.IOfficeWeekController
import com.hrb.holidays.business.entities.office.OfficeDay
import java.time.DayOfWeek
import java.time.LocalTime

class OfficeWeekScreenPresenter(
    private val controller: IOfficeWeekController
) : ViewModel() {
    var uiState: OfficeWeekState by mutableStateOf(initState())
        private set

    private fun initState(): OfficeWeekState {
        return OfficeWeekState(weekDays = fetchOfficeWeek())
    }

    private fun fetchOfficeWeek(): Set<OfficeDay> {
        return controller.getWeek().week
    }

    private fun updateUiState(
        week: Set<OfficeDay>?
    ) {
        if (week != null && week != uiState.weekDays) {
            uiState = uiState.copy(weekDays = week)
        }
    }

    suspend fun updateTimeForDay(startTime: LocalTime, endTime: LocalTime, day: DayOfWeek) {
        updateUiState(
            week = controller.updateDay(OfficeDay(day, startTime, endTime)).week
        )
    }
}
