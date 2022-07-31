package com.hrb.holidays.ui.components.buttons

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.hrb.holidays.ui.modifiers.FrameLine
import com.hrb.holidays.ui.modifiers.frame

@Composable
fun TextButtonGroup(
    modifier: Modifier = Modifier,
    borderAlpha: Float = 1f,
    borderColor: Color = MaterialTheme.colors.onSurface.copy(alpha = borderAlpha),
    borderShape: Shape = MaterialTheme.shapes.small,
    borderWidth: Dp = 1.dp,
    onSelect: (String, Int) -> Unit,
    vararg buttonTexts: String
) {
    var selectedIndex: Int? by remember { mutableStateOf(null) }

    TextButtonGroup(
        modifier = modifier,
        borderAlpha = borderAlpha,
        borderColor = borderColor,
        borderShape = borderShape,
        borderWidth = borderWidth,
        onSelect = { text, index ->
            selectedIndex = index
            onSelect(text, index)
        },
        selectedIndex = selectedIndex,
        buttonTexts = buttonTexts
    )
}

@Composable
fun TextButtonGroup(
    modifier: Modifier = Modifier,
    borderAlpha: Float = 1f,
    borderColor: Color = LocalContentColor.current.copy(alpha = borderAlpha),
    borderShape: Shape = RoundedCornerShape(50),
    borderWidth: Dp = 1.dp,
    onSelect: (String, Int) -> Unit,
    selectedIndex: Int? = null,
    vararg buttonTexts: String
) {
    LazyRow(
        modifier = modifier
            .border(width = borderWidth, color = borderColor, shape = borderShape)
            .clip(borderShape),
    ) {
        itemsIndexed(buttonTexts) { index, text ->
            val isSelected = selectedIndex == index
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clickable { onSelect(text, index) }
                    .let {
                        if (index < buttonTexts.size - 1)
                            it.frame(FrameLine.RIGHT, color = borderColor, alpha = borderAlpha)
                        else it
                    }
                    .let {
                        if (isSelected)
                            it.background(LocalContentColor.current.copy(alpha = 0.25f))
                        else it
                    }
                    .padding(ButtonDefaults.ContentPadding)
                    .animateContentSize()
            ) {
                if (isSelected) {
                    Icon(
                        Icons.Outlined.Check,
                        contentDescription = "Selected option: $text",
                        modifier = Modifier.size(ButtonDefaults.IconSize)
                    )
                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                }
                Text(text = text)
            }
        }
    }
}
