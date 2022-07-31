package com.hrb.holidays.ui.components.dialogs

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.rounded.Close
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.hrb.holidays.commons.capitalizeFirstChar
import com.hrb.holidays.ui.modifiers.applyIf
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.Month
import java.time.YearMonth
import java.time.format.DateTimeFormatter

private class DateRangePickerState(
    startDate: LocalDate? = null,
    endDate: LocalDate? = null
) {
    var startDate: LocalDate? by mutableStateOf(startDate)
        private set

    var endDate: LocalDate? by mutableStateOf(endDate)
        private set

    val isValidRange: Boolean
        get() = startDate != null && endDate != null

    fun selectDate(date: LocalDate) {
        if (startDate == null) {
            startDate = date
        } else if (endDate == null && startDate != date && date.isAfter(startDate)) {
            endDate = date
        } else {
            startDate = date
            endDate = null
        }
    }

    companion object {
        fun Saver(): Saver<DateRangePickerState, *> =
            Saver(
                save = { mapOf("startDate" to it.startDate, "endDate" to it.endDate) },
                restore = {
                    DateRangePickerState(
                        startDate = it["startDate"],
                        endDate = it["endDate"]
                    )
                }
            )
    }
}

@Composable
private fun rememberDateRangePickerState(
    startDate: LocalDate? = null,
    endDate: LocalDate? = null
): DateRangePickerState {
    return rememberSaveable(saver = DateRangePickerState.Saver()) {
        DateRangePickerState(
            startDate = startDate,
            endDate = endDate
        )
    }
}

@ExperimentalComposeUiApi
@Composable
fun DateRangePicker(
    onDismissRequest: () -> Unit,
    onSave: (startDate: LocalDate, endDate: LocalDate) -> Unit
) {
    var isFullScreen by rememberSaveable { mutableStateOf(true) }
    val state = rememberDateRangePickerState()

    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        if (isFullScreen) {
            FullScreenDateRangePicker(
                onDismissRequest = onDismissRequest,
                onSave = onSave,
                onToggleFullScreen = { isFullScreen = !isFullScreen },
                state = state
            )
        } else {
            DateRangeInput()
        }
    }
}

@Composable
private fun FullScreenDateRangePicker(
    onDismissRequest: () -> Unit,
    onSave: (startDate: LocalDate, endDate: LocalDate) -> Unit,
    onToggleFullScreen: () -> Unit,
    state: DateRangePickerState
) {
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        when (LocalConfiguration.current.orientation) {
            Configuration.ORIENTATION_PORTRAIT -> {
                Column(modifier = Modifier.fillMaxSize()) {
                    FullScreenHeader(
                        onDismissRequest = onDismissRequest,
                        onSave = {
                            if (state.startDate != null && state.endDate != null) {
                                onSave(
                                    state.startDate ?: return@FullScreenHeader,
                                    state.endDate ?: return@FullScreenHeader
                                )
                            }
                        },
                        onToggleFullScreen = onToggleFullScreen,
                        state = state
                    )
                    FullScreenCalendar(state = state)
                }
            }
            Configuration.ORIENTATION_LANDSCAPE -> {
                Row(modifier = Modifier.fillMaxSize()) {
                    FullScreenLandscapeHeader(
                        onDismissRequest = onDismissRequest,
                        onSave = {
                            if (state.startDate != null && state.endDate != null) {
                                onSave(
                                    state.startDate ?: return@FullScreenLandscapeHeader,
                                    state.endDate ?: return@FullScreenLandscapeHeader
                                )
                            }
                        },
                        onToggleFullScreen = onToggleFullScreen,
                        state = state,
                        modifier = Modifier
                            .fillMaxWidth(.3f)
                            .fillMaxHeight()
                    )
                    FullScreenCalendar(state = state)
                }
            }
        }
    }
}

@Composable
private fun FullScreenCalendar(
    state: DateRangePickerState
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
        ) {
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                for (dayTag in listOf("S", "M", "T", "W", "T", "F", "S")) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.size(48.dp)
                    ) {
                        Text(text = dayTag, style = MaterialTheme.typography.body2)
                    }
                }
            }
        }
        Divider(modifier = Modifier.fillMaxWidth())

        LazyColumn {
            items(1) {
                MonthCalendar(
                    year = LocalDate.now().year,
                    month = LocalDate.now().month,
                    onSelect = {
                        state.selectDate(it)
                    },
                    state = state
                )
            }
        }
    }
}

@Composable
private fun MonthCalendar(
    month: Month,
    year: Int,
    onSelect: (LocalDate) -> Unit,
    state: DateRangePickerState
) {
    CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
        val monthFormatter = DateTimeFormatter.ofPattern("MMMM yyyy")
        val date = LocalDate.of(year, month, 1)
        Text(
            text = date.format(monthFormatter).capitalizeFirstChar(),
            style = MaterialTheme.typography.subtitle2.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(horizontal = 32.dp, vertical = 12.dp)
        )
    }

    val nbCellsPerRow = 7
    val days = getDaysForCalendar(
        month = month,
        year = year,
        daysPerRow = nbCellsPerRow
    )
    for (i in 0 until (days.size / nbCellsPerRow)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            for (j in 0 until nbCellsPerRow) {
                val day = days[i * nbCellsPerRow + j]
                if (day == null) {
                    Box(modifier = Modifier.size(48.dp))
                } else {
                    DayCalendar(
                        day = day,
                        modifier = Modifier.size(48.dp),
                        onSelect = onSelect,
                        state = state,
                        isAtLeftBorder = j == 0,
                        isAtRightBorder = j == nbCellsPerRow - 1
                    )
                }
            }
        }
    }
}

@Composable
private fun DayCalendar(
    day: LocalDate,
    modifier: Modifier = Modifier,
    onSelect: (LocalDate) -> Unit,
    state: DateRangePickerState,
    isAtLeftBorder: Boolean = false,
    isAtRightBorder: Boolean = false
) {
    val isAtBorder = isAtLeftBorder || isAtRightBorder
    val isSelected = state.startDate == day || state.endDate == day
    val isInDateRange = (state.startDate?.isBefore(day) == true) &&
            (state.endDate?.isAfter(day) == true)
    val isToday = day.atStartOfDay() == LocalDate.now().atStartOfDay()
    val dayFormatter = DateTimeFormatter.ofPattern("d")
    val inRangeBackgroundColor = MaterialTheme.colors.primarySurface.copy(alpha = .13f)
    val backgroundColor =
        if (isSelected) MaterialTheme.colors.primarySurface
        else MaterialTheme.colors.surface
    Box(
        modifier = modifier
            .clickable(
                onClick = { onSelect(day) },
                indication = null,
                interactionSource = remember { MutableInteractionSource() })
            .padding(vertical = 4.dp)
            .applyIf(isInDateRange && !isAtBorder) {
                it.background(color = inRangeBackgroundColor)
            }
            .applyIf(isInDateRange && isAtLeftBorder) {
                it.drawBehind {
                    drawRect(
                        color = inRangeBackgroundColor,
                        size = Size(size.width + 12.dp.toPx(), size.height),
                        topLeft = Offset(-12.dp.toPx(), 0f)
                    )
                }
            }
            .applyIf(isInDateRange && isAtRightBorder) {
                it.drawBehind {
                    drawRect(
                        color = inRangeBackgroundColor,
                        size = Size(size.width + 12.dp.toPx(), size.height)
                    )
                }
            }
            .applyIf(isSelected && isAtLeftBorder) {
                it.drawBehind {
                    if (state.endDate != null) {
                        if (day == state.startDate) drawRect(
                            color = inRangeBackgroundColor,
                            topLeft = Offset(size.width / 2f, 0f)
                        )
                        else drawRect(
                            color = inRangeBackgroundColor,
                            size = Size((size.width / 2) + 12.dp.toPx(), size.height),
                            topLeft = Offset(-12.dp.toPx(), 0f)
                        )
                    }
                }
            }
            .applyIf(isSelected && isAtRightBorder) {
                it.drawBehind {
                    if (state.endDate != null) {
                        if (day == state.startDate) drawRect(
                            color = inRangeBackgroundColor,
                            topLeft = Offset(size.width / 2f, 0f),
                            size = Size((size.width / 2) + 12.dp.toPx(), size.height)
                        )
                        else drawRect(
                            color = inRangeBackgroundColor,
                            size = Size(size.width / 2, size.height)
                        )
                    }
                }
            }
            .applyIf(isSelected && !isAtBorder) {
                it.drawBehind {
                    if (state.endDate != null) {
                        if (day == state.startDate) drawRect(
                            color = inRangeBackgroundColor,
                            topLeft = Offset(size.width / 2f, 0f)
                        )
                        else drawRect(
                            color = inRangeBackgroundColor,
                            size = Size(size.width / 2, size.height)
                        )
                    }
                }
            }
            .padding(horizontal = 4.dp)
            .applyIf(isSelected) {
                it.background(
                    color = backgroundColor,
                    shape = CircleShape
                )
            }
            .applyIf(isToday) {
                it.border(
                    width = 1.dp,
                    color = LocalContentColor.current,
                    shape = CircleShape
                )
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = day.format(dayFormatter),
            style = MaterialTheme.typography.body2.copy(color = contentColorFor(backgroundColor))
        )
    }
}

private fun getDaysForCalendar(month: Month, year: Int, daysPerRow: Int): List<LocalDate?> {
    val daysRow = mutableListOf<LocalDate?>()
    val daysInMont = YearMonth.of(year, month)
    // Cannot use DayOfWeek.values() as it may not be in the same order as the calendar
    val daysOfWeek = listOf(
        DayOfWeek.SUNDAY,
        DayOfWeek.MONDAY,
        DayOfWeek.TUESDAY,
        DayOfWeek.WEDNESDAY,
        DayOfWeek.THURSDAY,
        DayOfWeek.FRIDAY,
        DayOfWeek.SATURDAY,
    )
    val firstDayOfWeek = daysInMont.atDay(1)
    for (dayOfWeek in daysOfWeek) {
        if (dayOfWeek == firstDayOfWeek.dayOfWeek) {
            break
        }
        daysRow.add(null)
    }
    for (dayNb in 1..daysInMont.lengthOfMonth()) {
        daysRow.add(daysInMont.atDay(dayNb))
    }
    while (daysRow.size % daysPerRow != 0) {
        daysRow.add(null)
    }
    return daysRow
}


@Composable
private fun FullScreenLandscapeHeader(
    onDismissRequest: () -> Unit,
    onSave: () -> Unit,
    onToggleFullScreen: () -> Unit,
    state: DateRangePickerState,
    modifier: Modifier = Modifier
) {
    Surface(color = MaterialTheme.colors.primarySurface, elevation = 16.dp, modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("SELECT RANGE", style = MaterialTheme.typography.overline)
                IconButton(onClick = onToggleFullScreen) {
                    Icon(Icons.Filled.Edit, "Toggle to date range input")
                }
            }
            Column(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp)
            ) {
                val dateFormatter = DateTimeFormatter.ofPattern("MMM dd")
                val alphaStart =
                    if (state.startDate == null) ContentAlpha.disabled
                    else LocalContentAlpha.current
                val alphaEnd =
                    if (state.endDate == null) ContentAlpha.disabled
                    else LocalContentAlpha.current
                Text(
                    text = (state.startDate?.format(dateFormatter)?.capitalizeFirstChar()
                        ?: "Start") + ",",
                    style = MaterialTheme.typography.h4,
                    color = LocalContentColor.current.copy(alpha = alphaStart)
                )
                Text(
                    text = state.endDate?.format(dateFormatter)?.capitalizeFirstChar()
                        ?: "End",
                    style = MaterialTheme.typography.h4,
                    color = LocalContentColor.current.copy(alpha = alphaEnd)
                )
            }
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(onClick = onDismissRequest) {
                    Icon(Icons.Rounded.Close, "Close this dialog")
                }
                IconButton(onClick = onSave, enabled = state.isValidRange) {
                    Text(text = "SAVE", style = MaterialTheme.typography.button)
                }
            }
        }
    }
}

@Composable
private fun FullScreenHeader(
    onDismissRequest: () -> Unit,
    onSave: () -> Unit,
    onToggleFullScreen: () -> Unit,
    state: DateRangePickerState,
    modifier: Modifier = Modifier
) {
    Surface(color = MaterialTheme.colors.primarySurface, elevation = 16.dp, modifier = modifier) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = onDismissRequest) {
                    Icon(Icons.Rounded.Close, "Close this dialog")
                }
                IconButton(onClick = onSave, enabled = state.isValidRange) {
                    Text(text = "SAVE", style = MaterialTheme.typography.button)
                }
            }
            Column(modifier = Modifier.padding(start = 60.dp)) {
                Text("SELECT RANGE", style = MaterialTheme.typography.overline)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row {
                        val dateFormatter = DateTimeFormatter.ofPattern("MMM dd")
                        val alphaStart =
                            if (state.startDate == null) ContentAlpha.disabled
                            else LocalContentAlpha.current
                        val alphaEnd =
                            if (state.endDate == null) ContentAlpha.disabled
                            else LocalContentAlpha.current

                        Text(
                            text = state.startDate?.format(dateFormatter)?.capitalizeFirstChar()
                                ?: "Start",
                            style = MaterialTheme.typography.h5,
                            color = LocalContentColor.current.copy(alpha = alphaStart)
                        )
                        Text(
                            text = " – ",
                            style = MaterialTheme.typography.h5,
                            color = LocalContentColor.current.copy(alpha = alphaEnd)
                        )
                        Text(
                            text = state.endDate?.format(dateFormatter)?.capitalizeFirstChar()
                                ?: "End",
                            style = MaterialTheme.typography.h5,
                            color = LocalContentColor.current.copy(alpha = alphaEnd)
                        )
                    }
                    IconButton(onClick = onToggleFullScreen) {
                        Icon(Icons.Filled.Edit, "Toggle to date range input")
                    }
                }
            }
        }
    }
}

@Composable
private fun DateRangeInput() {
}

@OptIn(ExperimentalComposeUiApi::class)
@Preview(showBackground = true)
@Composable
fun DateRangePickerPreview() {
    var showDialog by rememberSaveable {
        mutableStateOf(true)
    }
    Button(onClick = {
        showDialog = true
    }) {
        Text("Show Dialog")
    }
    if (showDialog) {
        DateRangePicker(
            onDismissRequest = {
                showDialog = false
            },
            onSave = { _, _ ->
                showDialog = false
            }
        )
    }
}
