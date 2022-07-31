package com.hrb.holidays.ui.views.holidays

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.hrb.holidays.business.entities.holidays.HolidayPeriod
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalDate


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

@Composable
fun AddHolidayDialog(
    isOpen: Boolean,
    onAdd: (HolidayPeriod) -> Unit = { _ -> },
    onCancel: () -> Unit = {},
    onComplete: () -> Unit = {}
) {
    if (isOpen) {
        Dialog(onDismissRequest = { onCancel(); onComplete() }) {
            Surface(
                shape = MaterialTheme.shapes.medium,
                color = MaterialTheme.colors.surface,
                contentColor = contentColorFor(SnackbarDefaults.backgroundColor)
            ) {
                var name by remember { mutableStateOf("New Holiday") }
                var fromDate by remember { mutableStateOf(LocalDate.now()) }
                var toDate by remember { mutableStateOf(LocalDate.now()) }

                var errorMsg by remember { mutableStateOf("") }

                val fromDatePickerState = rememberMaterialDialogState()
                val toDatePickerState = rememberMaterialDialogState()

                MaterialDialog(dialogState = fromDatePickerState,
                    buttons = {
                        positiveButton("OK")
                    }
                ) {
                    datepicker(
                        initialDate = fromDate,
                        title = "Select start date",
                    ) { date ->
                        fromDate = date
                        toDatePickerState.show()
                    }
                }
                MaterialDialog(dialogState = toDatePickerState,
                    buttons = {
                        positiveButton("OK")
                    }
                ) {
                    datepicker(
                        initialDate = toDate,
                        title = "Select end date",
                        allowedDateValidator = { date ->
                            date.isAfter(fromDate)
                        }
                    ) { date ->
                        toDate = date
                    }
                }

                Column(modifier = Modifier.padding(16.dp)) {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        singleLine = true,
                        placeholder = { Text("Type holiday name") }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("From:")
                        Spacer(modifier = Modifier.width(8.dp))
                        OutlinedButton(onClick = { fromDatePickerState.show() }) {
                            Text(fromDate.toString())
                        }
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("To:")
                        Spacer(modifier = Modifier.width(8.dp))
                        OutlinedButton(onClick = { toDatePickerState.show() }) {
                            Text(toDate.toString())
                        }
                    }

                    Spacer(modifier = Modifier.height(1.dp))
                    Text(
                        errorMsg,
                        style = MaterialTheme.typography.caption,
                        color = Color.Red
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedButton(onClick = { onCancel(); onComplete() }) {
                            Text("CANCEL")
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = {
                                if (toDate.isAfter(fromDate)) {
                                    onAdd(
                                        HolidayPeriod(
                                            name,
                                            fromDate,
                                            toDate
                                        )
                                    )
                                    onComplete()
                                } else {
                                    errorMsg = "End date must be after start date"
                                }
                            },
                            elevation = ButtonDefaults.elevation(0.dp, 0.dp, 0.dp, 0.dp)
                        ) {
                            Text("ADD")
                        }
                    }
                }
            }
        }
    }
}
