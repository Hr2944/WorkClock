package com.hrb.holidays.app.presenters.holidays

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.hrb.holidays.business.controllers.holidays.IHolidaysTimetableController
import com.hrb.holidays.business.entities.holidays.HolidayPeriod

class HolidaysTimetableScreenPresenter(
    private val controller: IHolidaysTimetableController
) : ViewModel() {
    var uiState: HolidaysTimetableState by mutableStateOf(initState())
        private set

    private fun initState(): HolidaysTimetableState {
        return HolidaysTimetableState(holidays = fetchHolidays())
    }

    private fun fetchHolidays(): MutableSet<HolidayPeriod> {
        return controller.getTimetable().timetable
    }

    suspend fun addHoliday(holiday: HolidayPeriod) {
        updateUiState(controller.addHoliday(holiday).timetable)
    }

    suspend fun deleteHoliday(holiday: HolidayPeriod) {
        updateUiState(controller.deleteHoliday(holiday).timetable)
    }

    private fun updateUiState(
        holidays: Set<HolidayPeriod>?
    ) {
        if (holidays != null && holidays != uiState.holidays) {
            uiState = uiState.copy(holidays = holidays as MutableSet<HolidayPeriod>)
        }
    }
}
