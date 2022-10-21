package com.hrb.holidays.ui.views.holidays

import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExtendedFloatingActionButton
import androidx.compose.material.FloatingActionButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hrb.holidays.business.entities.holidays.HolidayPeriod

@Composable
fun AddHolidayButton(onAddHoliday: (HolidayPeriod) -> Unit) {
    var addDialogIsOpen by remember {
        mutableStateOf(false)
    }

    ExtendedFloatingActionButton(
        modifier = Modifier.padding(bottom = 8.dp),
        text = { Text("ADD") },
        onClick = { addDialogIsOpen = true },
        elevation = FloatingActionButtonDefaults.elevation(0.dp, 0.dp, 0.dp, 0.dp)
    )

    AddHolidayDialog(
        isOpen = addDialogIsOpen,
        onDismissRequest = { addDialogIsOpen = false },
        onAdd = onAddHoliday
    )
}
