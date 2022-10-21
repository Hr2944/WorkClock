package com.hrb.holidays.ui.components.datepicker.fullscreen

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.boundsInParent
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.accompanist.flowlayout.FlowRow
import com.hrb.holidays.commons.capitalizeFirstChar
import com.hrb.holidays.commons.immutable.ImmutableYearMonth
import com.hrb.holidays.ui.components.datepicker.CalendarTypography
import com.hrb.holidays.ui.components.datepicker.DateFormatDefaults


@Composable
internal fun Month(
    month: CalendarMonth,
    modifier: Modifier = Modifier,
    horizontalPadding: Dp = 12.dp,
    daySize: Dp = 48.dp,
    onSelectDay: (Int, CalendarDay) -> Unit,
    dayContent: @Composable (CalendarDay) -> Unit
) {
    MonthName(month = month.month)

    val dayBoundingBoxesState = rememberDayBoundingBoxesState(month) { dayIndex ->
        month.findDayIndexed { i -> i == dayIndex }?.let {
            onSelectDay(dayIndex, it)
        }
    }
    val spacerSize = remember(daySize, month.firstDayIndex) {
        daySize * month.firstDayIndex
    }

    FlowRow(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = horizontalPadding)
            .pointerInput(Unit) {
                detectTapGestures {
                    dayBoundingBoxesState.click(it)
                }
            }
    ) {
        Spacer(modifier = Modifier.width(spacerSize))
        month.fastForEachDay { day ->
            Box(
                modifier = Modifier
                    .onGloballyPositioned {
                        dayBoundingBoxesState.addDay(boundingBox = it.boundsInParent())
                    }
            ) {
                dayContent(day)
            }
        }
    }
}

@Composable
internal fun MonthName(
    month: ImmutableYearMonth
) {
    val text = remember(month) {
        month.format(DateFormatDefaults.monthFormatter.toDateTimeFormatter()).capitalizeFirstChar()
    }

    CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
        Text(
            text = text,
            style = CalendarTypography.month,
            modifier = Modifier
                .padding(start = 12.dp)
                .paddingFromBaseline(top = 32.dp, bottom = 16.dp)
        )
    }
}
