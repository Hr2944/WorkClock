package com.hrb.holidays.ui.components.datepicker.fullscreen

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import com.hrb.holidays.commons.immutable.ImmutableLocalDate
import com.hrb.holidays.ui.components.datepicker.RangePickerState
import kotlinx.collections.immutable.ImmutableList
import java.time.DayOfWeek


@Composable
internal fun AdaptiveRangePicker(
    onDismissRequest: () -> Unit,
    onSave: () -> Unit,
    onToggleFullScreen: () -> Unit,
    onSelectDate: (ImmutableLocalDate) -> Unit,
    daysOfWeek: ImmutableList<DayOfWeek>,
    pickerState: RangePickerState
) {
    val calendar = @Composable {
        Calendar(
            onSelectDate = onSelectDate,
            daysOfWeek = daysOfWeek,
            pickerState = pickerState
        )
    }
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        when (LocalConfiguration.current.orientation) {
            Configuration.ORIENTATION_LANDSCAPE -> {
                Row(modifier = Modifier.fillMaxSize()) {
                    LandscapeHeader(
                        onDismissRequest = onDismissRequest,
                        onSave = onSave,
                        onToggleFullScreen = onToggleFullScreen,
                        pickerState = pickerState
                    )
                    calendar()
                }
            }
            else -> {
                Column(modifier = Modifier.fillMaxSize()) {
                    PortraitHeader(
                        onDismissRequest = onDismissRequest,
                        onSave = onSave,
                        onToggleFullScreen = onToggleFullScreen,
                        pickerState = pickerState,
                    )
                    calendar()
                }
            }
        }
    }
}

@Composable
private fun Calendar(
    onSelectDate: (ImmutableLocalDate) -> Unit,
    daysOfWeek: ImmutableList<DayOfWeek>,
    pickerState: RangePickerState
) {
    EndlosCalendar(
        onSelectDate = onSelectDate,
        daysOfWeek = daysOfWeek,
        pickerState = pickerState,
    )
}
