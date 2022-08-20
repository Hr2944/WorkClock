package com.hrb.holidays.ui.components.datepicker

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import kotlinx.collections.immutable.ImmutableList
import java.time.DayOfWeek
import java.time.LocalDate


@Composable
internal fun FullScreenDateRangePicker(
    onDismissRequest: () -> Unit,
    onSave: () -> Unit,
    onToggleFullScreen: () -> Unit,
    onSelectDate: (LocalDate) -> Unit,
    daysOfWeek: ImmutableList<DayOfWeek>,
    pickerState: DateRangePickerState
) {
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        when (LocalConfiguration.current.orientation) {
            Configuration.ORIENTATION_LANDSCAPE -> {
                Row(modifier = Modifier.fillMaxSize()) {
                    FullScreenLandscapeHeader(
                        onDismissRequest = onDismissRequest,
                        onSave = onSave,
                        onToggleFullScreen = onToggleFullScreen,
                        pickerState = pickerState
                    )
                    FullScreenCalendar(
                        onSelectDate = onSelectDate,
                        daysOfWeek = daysOfWeek,
                        pickerState = pickerState,
                    )
                }
            }
            else -> {
                Column(modifier = Modifier.fillMaxSize()) {
                    FullScreenPortraitHeader(
                        onDismissRequest = onDismissRequest,
                        onSave = onSave,
                        onToggleFullScreen = onToggleFullScreen,
                        pickerState = pickerState,
                    )
                    FullScreenCalendar(
                        onSelectDate = onSelectDate,
                        daysOfWeek = daysOfWeek,
                        pickerState = pickerState,
                    )
                }
            }
        }
    }
}
