package com.hrb.holidays.ui.views.holidays

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.hrb.holidays.business.entities.holidays.HolidayPeriod
import kotlinx.coroutines.launch
import java.util.*


@ExperimentalMaterialApi
@Composable
fun HolidayPeriodSwappableItem(
    modifier: Modifier = Modifier,
    holiday: HolidayPeriod,
    onDelete: (HolidayPeriod) -> Unit,
    dismissState: DismissState = rememberDismissState()
) {
    val confirmationDialogIsOpen = dismissState.isDismissed(DismissDirection.EndToStart) ||
            dismissState.isDismissed(DismissDirection.StartToEnd)
    val scope = rememberCoroutineScope()

    DeleteHolidayDialog(
        isOpen = confirmationDialogIsOpen,
        onDelete = { onDelete(holiday) },
        onCancel = { scope.launch { dismissState.reset() } }
    )

    val isAtInitialPosition = dismissState.progress.fraction == 0f ||
            dismissState.progress.fraction == 1f
    val cardShapeCornerSize by animateDpAsState(
        targetValue = if (isAtInitialPosition) 0.dp else 4.dp
    )
    val cardElevation by animateDpAsState(
        targetValue = if (isAtInitialPosition) 0.dp else 4.dp
    )
    SwipeToDismiss(
        state = dismissState,
        modifier = modifier.padding(vertical = 4.dp),
        background = {
            val direction = dismissState.dismissDirection ?: return@SwipeToDismiss
            val alignment = when (direction) {
                DismissDirection.StartToEnd -> Alignment.CenterStart
                DismissDirection.EndToStart -> Alignment.CenterEnd
            }

            Box(
                Modifier
                    .fillMaxSize()
                    .background(Color.Red)
                    .padding(horizontal = 20.dp),
                contentAlignment = alignment
            ) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Delete holiday",
                )
            }
        },

        dismissContent = {
            Card(
                elevation = cardElevation,
                modifier = Modifier.clickable { },
                shape = RoundedCornerShape(cardShapeCornerSize)
            ) {
                HolidayPeriodItem(holiday = holiday)
            }
        }
    )
}

@ExperimentalMaterialApi
@Composable
fun HolidayPeriodItem(holiday: HolidayPeriod) {
    ListItem(
        text = {
            Text(
                holiday.name.replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase(Locale.ROOT)
                    else it.toString()
                },
                fontWeight = FontWeight.Bold
            )
        },
        secondaryText = { Text("From ${holiday.fromDate} to ${holiday.toDate}") }
    )
}
