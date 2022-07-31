package com.hrb.holidays.ui.views.holidays

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import com.hrb.holidays.app.presenters.holidays.HolidaysTimetableScreenPresenter
import com.hrb.holidays.business.entities.holidays.HolidayPeriod
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel


@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
fun HolidaysCalendarEditorScreenActivity(
    presenter: HolidaysTimetableScreenPresenter = getViewModel()
) {
    val holidays = presenter.uiState.holidays
    val scope = rememberCoroutineScope()

    HolidaysCalendarEditorScreen(
        holidays = holidays,
        onAddHoliday = {
            scope.launch {
                presenter.addHoliday(it)
            }
        },
        onDismissHoliday = {
            scope.launch {
                presenter.deleteHoliday(it)
            }
        }
    )
}

@ExperimentalMaterialApi
@ExperimentalFoundationApi
@Composable
fun HolidaysCalendarEditorScreen(
    holidays: Set<HolidayPeriod>,
    onAddHoliday: (HolidayPeriod) -> Unit,
    onDismissHoliday: (HolidayPeriod) -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        AddHolidayButton(onAddHoliday)
        if (holidays.isEmpty()) {
            EmptyHolidaysList()
        } else {
            HolidaysList(
                holidays = holidays.toTypedArray(),
                onDismissHoliday = onDismissHoliday,
            )
        }
    }
}
