package com.hrb.holidays.ui.layouts

import android.content.res.Configuration
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.TweenSpec
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.isSpecified
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch
import kotlin.math.max
import kotlin.math.roundToInt


@ExperimentalMaterialApi
enum class BottomDrawerValue {
    Closed,

    Open,

    Expanded
}


@ExperimentalMaterialApi
class BottomDrawerState(
    initialValue: BottomDrawerValue,
    animationSpec: AnimationSpec<Float> = BottomDrawerDefaults.animationSpec,
    confirmStateChange: (BottomDrawerValue) -> Boolean = { true }
) : SwipeableState<BottomDrawerValue>(
    initialValue = initialValue,
    animationSpec = animationSpec,
    confirmStateChange = confirmStateChange
) {
    val isOpen: Boolean
        get() = currentValue != BottomDrawerValue.Closed

    val isClosed: Boolean
        get() = currentValue == BottomDrawerValue.Closed

    val isExpanded: Boolean
        get() = currentValue == BottomDrawerValue.Expanded

    /**
     * Open the drawer with animation and suspend until it if fully opened or animation has been
     * cancelled.
     *
     * @throws [CancellationException] if the animation is interrupted
     *
     */
    suspend fun open() {
        animateTo(BottomDrawerValue.Open)
    }

    /**
     * Close the drawer with animation and suspend until it if fully closed or animation has been
     * cancelled.
     *
     * @throws [CancellationException] if the animation is interrupted
     *
     */
    suspend fun close() = animateTo(BottomDrawerValue.Closed)

    /**
     * Expand the drawer with animation and suspend until it if fully expanded or animation has
     * been cancelled.
     *
     * @throws [CancellationException] if the animation is interrupted
     *
     */
    suspend fun expand() = animateTo(BottomDrawerValue.Expanded)

    // Reflection is currently needed, as PreUpPostDownNestedScrollConnection is always internal
    @Suppress("INVISIBLE_MEMBER")
    val nestedScrollConnection: NestedScrollConnection = this.PreUpPostDownNestedScrollConnection

    companion object {
        fun Saver(
            animationSpec: AnimationSpec<Float>,
            confirmStateChange: (BottomDrawerValue) -> Boolean
        ): Saver<BottomDrawerState, *> =
            Saver(
                save = { it.currentValue },
                restore = {
                    BottomDrawerState(
                        initialValue = it,
                        animationSpec = animationSpec,
                        confirmStateChange = confirmStateChange
                    )
                }
            )
    }
}

object BottomDrawerDefaults {

    val animationSpec: AnimationSpec<Float> = TweenSpec(durationMillis = 256)

    val elevation: Dp = 16.dp

    val scrimColor: Color
        @Composable
        get() = MaterialTheme.colors.onSurface.copy(alpha = .32f)

    const val gesturesEnabled: Boolean = true

    val drawerCornerSize: Dp = 32.dp

    val drawerBackgroundColor: Color
        @Composable
        get() = MaterialTheme.colors.surface

    val drawerContentColor: Color
        @Composable
        get() = contentColorFor(drawerBackgroundColor)

    const val drawerOpenFraction: Float = .5f
}

@Composable
@ExperimentalMaterialApi
fun rememberBottomDrawerState(
    initialValue: BottomDrawerValue,
    animationSpec: AnimationSpec<Float> = BottomDrawerDefaults.animationSpec,
    confirmStateChange: (BottomDrawerValue) -> Boolean = { true }
): BottomDrawerState {
    return rememberSaveable(
        animationSpec,
        confirmStateChange,
        saver = BottomDrawerState.Saver(
            animationSpec = animationSpec,
            confirmStateChange = confirmStateChange,
        )
    ) {
        BottomDrawerState(
            initialValue = initialValue,
            animationSpec = animationSpec,
            confirmStateChange = confirmStateChange
        )
    }
}

@Composable
@ExperimentalMaterialApi
fun BottomDrawer(
    drawerState: BottomDrawerState = rememberBottomDrawerState(BottomDrawerValue.Closed),
    gesturesEnabled: Boolean = BottomDrawerDefaults.gesturesEnabled,
    drawerCornerSize: Dp = BottomDrawerDefaults.drawerCornerSize,
    drawerElevation: Dp = BottomDrawerDefaults.elevation,
    drawerBackgroundColor: Color = BottomDrawerDefaults.drawerBackgroundColor,
    drawerContentColor: Color = BottomDrawerDefaults.drawerContentColor,
    scrimColor: Color = BottomDrawerDefaults.scrimColor,
    bottomDrawerOpenFraction: Float = BottomDrawerDefaults.drawerOpenFraction,
    content: @Composable () -> Unit,
    drawerContent: @Composable ColumnScope.() -> Unit
) {
    BoxWithConstraints(Modifier.fillMaxSize()) {
        val scope = rememberCoroutineScope()

        val fullHeight = constraints.maxHeight.toFloat()
        var drawerHeight by remember(fullHeight) { mutableStateOf(fullHeight) }
        val isLandscape = LocalConfiguration.current.orientation ==
                Configuration.ORIENTATION_LANDSCAPE

        val minHeight = 0f
        val peekHeight = fullHeight * bottomDrawerOpenFraction
        val expandedHeight = max(minHeight, fullHeight - drawerHeight)

        val anchors = if (drawerHeight < peekHeight || isLandscape) {
            mapOf(
                fullHeight to BottomDrawerValue.Closed,
                expandedHeight to BottomDrawerValue.Expanded
            )
        } else {
            mapOf(
                fullHeight to BottomDrawerValue.Closed,
                peekHeight to BottomDrawerValue.Open,
                expandedHeight to BottomDrawerValue.Expanded
            )
        }
        val drawerConstraints = with(LocalDensity.current) {
            Modifier
                .sizeIn(
                    maxWidth = constraints.maxWidth.toDp(),
                    maxHeight = constraints.maxHeight.toDp()
                )
        }

        Box(
            modifier = Modifier
                .let {
                    if (gesturesEnabled) Modifier.nestedScroll(drawerState.nestedScrollConnection)
                    else it
                }
                .swipeable(
                    state = drawerState,
                    anchors = anchors,
                    orientation = Orientation.Vertical,
                    enabled = gesturesEnabled,
                    resistance = null
                )
        ) {
            content()

            val alpha = drawerState.progress.run {
                when {
                    from == BottomDrawerValue.Closed && to == BottomDrawerValue.Open -> fraction
                    from == BottomDrawerValue.Open && to == BottomDrawerValue.Closed -> 1 - fraction
                    from == to && to == BottomDrawerValue.Closed -> 0f
                    else -> 1f
                }
            }

            BottomDrawerScrim(
                color = scrimColor,
                onTap = {
                    if (gesturesEnabled) {
                        scope.launch { drawerState.close() }
                    }
                },
                active = drawerState.targetValue != BottomDrawerValue.Closed,
                alpha = alpha
            )

            val cornerSize = drawerState.progress.run {
                when {
                    from == BottomDrawerValue.Open && to == BottomDrawerValue.Expanded -> drawerCornerSize * (1 - fraction)
                    from == BottomDrawerValue.Expanded && to == BottomDrawerValue.Open -> drawerCornerSize * fraction
                    from == to && to == BottomDrawerValue.Expanded -> 0.dp
                    else -> drawerCornerSize
                }
            }
            Surface(
                modifier = drawerConstraints
                    .offset { IntOffset(x = 0, y = drawerState.offset.value.roundToInt()) }
                    .onGloballyPositioned { position ->
                        drawerHeight = position.size.height.toFloat()
                    },
                shape = RoundedCornerShape(cornerSize, cornerSize, 0.dp, 0.dp),
                color = drawerBackgroundColor,
                contentColor = drawerContentColor,
                elevation = drawerElevation
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    val topPadding = 8.dp
                    Divider(
                        modifier = Modifier
                            .padding(0.dp, topPadding, 0.dp, drawerCornerSize - topPadding)
                            .fillMaxWidth(.4f),
                        thickness = 3.dp
                    )

                    drawerContent()
                }
            }
        }
    }
}

@Composable
private fun BottomDrawerScrim(
    color: Color,
    onTap: () -> Unit,
    active: Boolean,
    alpha: Float
) {
    if (color.isSpecified) {
        val pointerModifier =
            if (active) Modifier.pointerInput(onTap) { detectTapGestures { onTap() } }
            else Modifier

        Canvas(
            Modifier
                .fillMaxSize()
                .then(pointerModifier)
        ) {
            drawRect(color = color, alpha = alpha)
        }
    }
}
