package com.hrb.holidays.ui.layouts

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.isSpecified
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.hrb.holidays.ui.layouts.SideBackdropDefaults.gesturesEnabled
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

enum class RevealDirection {
    Left,
    Right
}

enum class SideBackdropValue {
    Concealed,
    RevealedLeft,
    RevealedRight
}

@ExperimentalMaterialApi
class SideBackdropState(
    initialValue: SideBackdropValue,
    animationSpec: AnimationSpec<Float> = SwipeableDefaults.AnimationSpec,
    confirmStateChange: (SideBackdropValue) -> Boolean = { true },
) : SwipeableState<SideBackdropValue>(
    initialValue = initialValue,
    animationSpec = animationSpec,
    confirmStateChange = confirmStateChange
) {
    val isRevealed: Boolean
        get() = isRevealedLeft || isRevealedRight

    val isRevealedLeft: Boolean
        get() = currentValue == SideBackdropValue.RevealedLeft
    val isRevealedRight: Boolean
        get() = currentValue == SideBackdropValue.RevealedRight

    val isConcealed: Boolean
        get() = currentValue == SideBackdropValue.Concealed

    suspend fun revealLeft() = animateTo(targetValue = SideBackdropValue.RevealedLeft)
    suspend fun revealRight() = animateTo(targetValue = SideBackdropValue.RevealedRight)

    suspend fun conceal() = animateTo(targetValue = SideBackdropValue.Concealed)

    companion object {
        fun Saver(
            animationSpec: AnimationSpec<Float>,
            confirmStateChange: (SideBackdropValue) -> Boolean,
        ): Saver<SideBackdropState, *> = Saver(
            save = { it.currentValue },
            restore = {
                SideBackdropState(
                    initialValue = it,
                    animationSpec = animationSpec,
                    confirmStateChange = confirmStateChange,
                )
            }
        )
    }
}

@Composable
@ExperimentalMaterialApi
fun rememberSideBackdropState(
    initialValue: SideBackdropValue,
    animationSpec: AnimationSpec<Float> = SwipeableDefaults.AnimationSpec,
    confirmStateChange: (SideBackdropValue) -> Boolean = { true },
): SideBackdropState {
    return rememberSaveable(
        animationSpec,
        confirmStateChange,
        saver = SideBackdropState.Saver(
            animationSpec = animationSpec,
            confirmStateChange = confirmStateChange,
        )
    ) {
        SideBackdropState(
            initialValue = initialValue,
            animationSpec = animationSpec,
            confirmStateChange = confirmStateChange,
        )
    }
}

object SideBackdropDefaults {
    @Composable
    fun getFrontLayerShape(directions: Set<RevealDirection>, cornerSize: Dp): CornerBasedShape {
        return when {
            RevealDirection.Right in directions && RevealDirection.Left in directions ->
                RoundedCornerShape(cornerSize)
            RevealDirection.Left in directions ->
                RoundedCornerShape(
                    topStart = cornerSize,
                    topEnd = 0.dp,
                    bottomStart = cornerSize,
                    bottomEnd = 0.dp
                )
            RevealDirection.Right in directions ->
                RoundedCornerShape(
                    topStart = 0.dp,
                    topEnd = cornerSize,
                    bottomStart = 0.dp,
                    bottomEnd = cornerSize
                )
            else -> RoundedCornerShape(0.dp)
        }
    }

    val frontLayerElevation: Dp = 5.dp

    val frontLayerScrimColor: Color
        @Composable
        get() = MaterialTheme.colors.surface.copy(alpha = 0.60f)

    val backLayerScrimColor: Color
        @Composable
        get() = MaterialTheme.colors.surface.copy(alpha = 0.60f)

    const val gesturesEnabled: Boolean = true

    val frontLayerCornerSize: Dp = 16.dp

    const val revealThresholdWidthFraction: Float = 3f / 5f

    val directions: ImmutableSet<RevealDirection> =
        persistentSetOf(RevealDirection.Left, RevealDirection.Right)
}

@ExperimentalMaterialApi
@Composable
fun SideBackdrop(
    backLayerContent: @Composable () -> Unit,
    frontLayerContent: @Composable () -> Unit,
    scaffoldState: SideBackdropState = rememberSideBackdropState(SideBackdropValue.Concealed),
    gesturesEnabled: Boolean = SideBackdropDefaults.gesturesEnabled,
    frontLayerScrimColor: Color = SideBackdropDefaults.frontLayerScrimColor,
    backLayerScrimColor: Color = SideBackdropDefaults.backLayerScrimColor,
    frontLayerElevation: Dp = SideBackdropDefaults.frontLayerElevation,
    directions: ImmutableSet<RevealDirection> = SideBackdropDefaults.directions,
    frontLayerCornerSize: Dp = SideBackdropDefaults.frontLayerCornerSize,
    revealThresholdWidthFraction: Float = SideBackdropDefaults.revealThresholdWidthFraction
) {
    BoxWithConstraints {
        val width = constraints.maxWidth * revealThresholdWidthFraction

        val anchors = mutableMapOf(0f to SideBackdropValue.Concealed)
        if (RevealDirection.Left in directions) {
            anchors += width to SideBackdropValue.RevealedLeft
        }
        if (RevealDirection.Right in directions) {
            anchors += -width to SideBackdropValue.RevealedRight
        }

        val minFactor =
            if (RevealDirection.Right in directions) SwipeableDefaults.StandardResistanceFactor
            else 0f
        val maxFactor =
            if (RevealDirection.Left in directions) SwipeableDefaults.StandardResistanceFactor
            else 0f

        Box(
            modifier = Modifier.let {
                if (gesturesEnabled) {
                    it.swipeable(
                        state = scaffoldState,
                        anchors = anchors,
                        thresholds = { _, _ -> FractionalThreshold(0.5f) },
                        orientation = Orientation.Horizontal,
                        reverseDirection = LocalLayoutDirection.current == LayoutDirection.Rtl,
                        resistance = ResistanceConfig(
                            basis = width,
                            factorAtMin = minFactor,
                            factorAtMax = maxFactor
                        )
                    )
                } else it
            }
        ) {
            BackLayer(
                scaffoldState = scaffoldState,
                backLayerContent = backLayerContent,
                backLayerScrimColor = backLayerScrimColor,
                gesturesEnabled = gesturesEnabled,
            )

            FrontLayer(
                scaffoldState = scaffoldState,
                directions = directions,
                frontLayerContent = frontLayerContent,
                frontLayerScrimColor = frontLayerScrimColor,
                frontLayerElevation = frontLayerElevation,
                frontLayerCornerSize = frontLayerCornerSize
            )
        }
    }
}

@ExperimentalMaterialApi
@Composable
private fun BoxScope.BackLayer(
    scaffoldState: SideBackdropState,
    backLayerContent: @Composable () -> Unit,
    backLayerScrimColor: Color,
    gesturesEnabled: Boolean
) {
    val scope = rememberCoroutineScope()
    Surface(
        modifier = Modifier.matchParentSize()
    ) {
        val alpha =
            if (scaffoldState.progress.to == SideBackdropValue.Concealed) scaffoldState.progress.fraction
            else 1 - scaffoldState.progress.fraction
        backLayerContent()
        Scrim(
            alpha = alpha,
            color = backLayerScrimColor,
            onTap = {
                if (gesturesEnabled) {
                    scope.launch { scaffoldState.conceal() }
                }
            },
            isActive = scaffoldState.isConcealed
        )
    }
}

@ExperimentalMaterialApi
@Composable
private fun FrontLayer(
    scaffoldState: SideBackdropState,
    directions: ImmutableSet<RevealDirection>,
    frontLayerContent: @Composable () -> Unit,
    frontLayerScrimColor: Color,
    frontLayerElevation: Dp,
    frontLayerCornerSize: Dp
) {
    val scope = rememberCoroutineScope()

    val elevation by animateDpAsState(
        targetValue = if (scaffoldState.isRevealed && scaffoldState.progress.fraction == 1f) 0.dp else frontLayerElevation
    )
    val cornerDpShape =
        if (scaffoldState.progress.to == SideBackdropValue.Concealed) frontLayerCornerSize * (1 - scaffoldState.progress.fraction)
        else frontLayerCornerSize * scaffoldState.progress.fraction
    val shape = SideBackdropDefaults.getFrontLayerShape(
        directions = directions,
        cornerSize = cornerDpShape
    )
    Surface(
        shape = shape,
        modifier = Modifier
            .offset { IntOffset(scaffoldState.offset.value.roundToInt(), 0) }
            .shadow(elevation, shape, clip = false)
    ) {
        val alpha by animateFloatAsState(
            targetValue =
            if (scaffoldState.run { isRevealed && progress.fraction == 1f && progress.to != SideBackdropValue.Concealed }) 1f
            else 0f
        )
        frontLayerContent()
        Scrim(
            alpha = alpha,
            color = frontLayerScrimColor,
            onTap = {
                if (gesturesEnabled) {
                    scope.launch { scaffoldState.conceal() }
                }
            },
            isActive = scaffoldState.isRevealed
        )
    }
}

@Composable
private fun Scrim(
    color: Color,
    onTap: () -> Unit,
    isActive: Boolean,
    alpha: Float
) {
    if (color.isSpecified) {
        val pointerModifier =
            if (isActive) Modifier.pointerInput(onTap) { detectTapGestures { onTap() } }
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
