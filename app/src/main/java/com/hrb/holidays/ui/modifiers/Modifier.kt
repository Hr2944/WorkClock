package com.hrb.holidays.ui.modifiers

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun Modifier.applyIf(condition: Boolean, block: @Composable (Modifier) -> Modifier?): Modifier =
    if (condition) block(this) ?: this else this
