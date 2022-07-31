package com.hrb.holidays.app.presenters.office

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.hrb.holidays.business.controllers.office.IOfficeTimeBeforeHolidaysController


class OfficeTimeBeforeHolidaysScreenPresenter(
    private val controller: IOfficeTimeBeforeHolidaysController
) : ViewModel() {

    var uiState: OfficeTimeBeforeHolidaysState by mutableStateOf(initState())
        private set

    fun updateRemainingTime() {
        this.updateUiState(
            remainingTime = getRemainingTime()
        )
    }

    private fun initState(): OfficeTimeBeforeHolidaysState {
        return OfficeTimeBeforeHolidaysState(
            remainingTime = getRemainingTime(),
            progressInRemainingTime = getProgressInRemainingTime(),
            isPaused = false
        )
    }

    private fun getProgressInRemainingTime(): ProgressInDatesRange? {
        return controller.getTimeProgressBeforeNextHolidays()
    }

    private fun getRemainingTime(): RemainingTime? {
        val timeBeforeHolidays = controller.getTimeBeforeNextHolidays() ?: return null
        return RemainingTimeFactory.fromDuration(timeBeforeHolidays)
    }

    fun updateProgressInRemainingTime() {
        updateUiState(
            progressInRemainingTime = getProgressInRemainingTime()
        )
    }

    fun setPause(pause: Boolean) {
        updateUiState(isPaused = pause)
    }

    private fun updateUiState(
        remainingTime: RemainingTime? = uiState.remainingTime,
        progressInRemainingTime: ProgressInDatesRange? = uiState.progressInRemainingTime,
        isPaused: Boolean = uiState.isPaused
    ) {
        val newUiState = uiState.copy(
            remainingTime = remainingTime,
            progressInRemainingTime = progressInRemainingTime,
            isPaused = isPaused
        )
        if (newUiState != uiState) {
            this.uiState = newUiState
        }
    }


}
