package com.hrb.holidays.ui.views.holidays

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EditCalendar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.hrb.holidays.business.entities.holidays.HolidayPeriod
import com.hrb.holidays.ui.components.datepicker.RangePickerDialog
import com.hrb.holidays.ui.components.datepicker.rememberRangePickerState


@Composable
fun DeleteHolidayDialog(
    isOpen: Boolean,
    onDelete: () -> Unit = {},
    onCancel: () -> Unit = {},
    onComplete: () -> Unit = {}
) {
    if (isOpen) {
        AlertDialog(
            onDismissRequest = { onCancel(); onComplete() },
            confirmButton = {
                TextButton(
                    onClick = { onDelete(); onComplete() },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red)
                ) {
                    Text("CONFIRM")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { onCancel(); onComplete() }) {
                    Text("CANCEL")
                }
            },
            title = {
                Text(text = "Delete holiday")
            },
            text = {
                Text(
                    "Are you sure you want to delete this holiday? This action cannot be undone."
                )
            }
        )
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AddHolidayDialog(
    isOpen: Boolean,
    onAdd: (HolidayPeriod) -> Unit = { _ -> },
    onDismissRequest: () -> Unit
) {
    if (isOpen) {
        Dialog(onDismissRequest = onDismissRequest) {
            Surface(
                shape = MaterialTheme.shapes.medium,
                color = MaterialTheme.colors.surface,
                contentColor = contentColorFor(SnackbarDefaults.backgroundColor)
            ) {
                var name by rememberSaveable { mutableStateOf("New Holiday") }
                val pickerState = rememberRangePickerState()
                var showDialog by rememberSaveable { mutableStateOf(true) }

                Column(modifier = Modifier.padding(16.dp)) {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        singleLine = true,
                        placeholder = { Text("Name...") }
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        OutlinedButton(onClick = { showDialog = true }) {
                            Icon(
                                Icons.Filled.EditCalendar,
                                contentDescription = "Favorite",
                                modifier = Modifier.size(ButtonDefaults.IconSize)
                            )
                            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                            Text("${pickerState.startAsString()} – ${pickerState.endAsString()}")
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedButton(onClick = onDismissRequest) {
                            Text("CANCEL")
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = {
                                val startDate = pickerState.startDate
                                val endDate = pickerState.endDate
                                if (startDate != null && endDate != null) {
                                    onAdd(
                                        HolidayPeriod(
                                            name = name,
                                            fromDate = startDate.toLocalDate(),
                                            toDate = endDate.toLocalDate()
                                        )
                                    )
                                    onDismissRequest()
                                }
                            },
                            elevation = ButtonDefaults.elevation(0.dp, 0.dp, 0.dp, 0.dp),
                            enabled = pickerState.isValidRange
                        ) {
                            Text("ADD")
                        }
                    }
                }

                if (showDialog) {
                    RangePickerDialog(
                        onDismissRequest = {
                            showDialog = false
                        },
                        onSave = { _, _ ->
                            showDialog = false
                        },
                        pickerState = pickerState
                    )
                }
            }
        }
    }
}
