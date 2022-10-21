package com.hrb.holidays.ui.views.timer

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@Composable
fun TimerProgression(
    modifier: Modifier = Modifier,
    progress: Float,
    fromDate: LocalDate?,
    toDate: LocalDate?
) {
    val dateFormatter = remember { DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT) }

    Column(modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        ProgressionBar(progress, modifier = Modifier.fillMaxWidth(.95f))
        Spacer(modifier = Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            Text(
                text = fromDate?.format(dateFormatter) ?: "",
                style = MaterialTheme.typography.caption
            )
            Text(
                text = toDate?.format(dateFormatter) ?: "",
                style = MaterialTheme.typography.caption
            )
        }
    }
}

@Composable
fun ProgressionBar(progress: Float, modifier: Modifier = Modifier) {
    LinearProgressIndicator(
        progress = progress,
        modifier = modifier.clip(shape = RoundedCornerShape(8.dp))
    )
}
