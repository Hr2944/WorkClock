package com.hrb.holidays.ui.components.datepicker.fullscreen

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.*

enum class Reached {
    Top,
    Bottom
}

@Composable
fun LazyListState.OnTopOrBottomReached(
    updateWhenListEmpty: Boolean = true,
    buffer: Int = 0,
    updateList: (reached: Reached) -> Unit,
) {
    require(buffer >= 0) { "buffer cannot be negative, but was $buffer" }

    val shouldUpdateTopList by remember(isScrollInProgress) {
        derivedStateOf {
            val firstVisibleItem =
                layoutInfo.visibleItemsInfo.firstOrNull()
                    ?: return@derivedStateOf updateWhenListEmpty

            firstVisibleItem.index == buffer
        }
    }


    val shouldUpdateBottomList by remember(isScrollInProgress) {
        derivedStateOf {
            val lastVisibleItem =
                layoutInfo.visibleItemsInfo.lastOrNull()
                    ?: return@derivedStateOf updateWhenListEmpty
            lastVisibleItem.index == layoutInfo.totalItemsCount - 1 - buffer
        }
    }

    LaunchedEffect(isScrollInProgress) {
        snapshotFlow { shouldUpdateBottomList or shouldUpdateTopList }
            .collect {
                if (it) updateList(
                    if (shouldUpdateBottomList) Reached.Bottom
                    else Reached.Top
                )
            }
    }
}
