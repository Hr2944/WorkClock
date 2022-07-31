package com.hrb.holidays.ui.views.holidays

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.hrb.holidays.business.entities.holidays.HolidayPeriod

@ExperimentalFoundationApi
@ExperimentalMaterialApi
@Composable
fun HolidaysList(
    holidays: Array<HolidayPeriod>,
    onDismissHoliday: (HolidayPeriod) -> Unit
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(items = holidays, key = { it.hashCode() }) { holiday ->
            HolidayPeriodSwappableItem(
                modifier = Modifier.animateItemPlacement(),
                holiday = holiday,
                onDelete = onDismissHoliday
            )
        }
    }
}

@Composable
fun EmptyHolidaysList() {
    Column(
        modifier = Modifier
            .fillMaxHeight(.5f)
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = "No holidays",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.h5,
            color = MaterialTheme.typography.h5.color.copy(alpha = .5f)
        )
    }
}
