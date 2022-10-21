package com.hrb.holidays.ui.views.base

import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FabPosition
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import com.hrb.holidays.app.presenters.office.OfficeTimeBeforeHolidaysScreenPresenter
import com.hrb.holidays.ui.layouts.*
import com.hrb.holidays.ui.views.holidays.HolidaysCalendarEditorScreenActivity
import com.hrb.holidays.ui.views.office.WeekCalendarEditorScreenActivity
import com.hrb.holidays.ui.views.settings.SettingsScreenActivity
import com.hrb.holidays.ui.views.timer.FloatingPauseButton
import com.hrb.holidays.ui.views.timer.TimerScreen
import kotlinx.collections.immutable.immutableSetOf
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel

@ExperimentalMaterialApi
@Composable
private fun onCloseBottomDrawer(drawerState: BottomDrawerState, block: () -> Unit) {
    var lastKnownDrawerValue by rememberSaveable { mutableStateOf(BottomDrawerValue.Closed) }
    if (drawerState.currentValue != lastKnownDrawerValue) {
        // Drawer was open and now it's closed, meaning the calendars were probably updated
        if (
            (lastKnownDrawerValue == BottomDrawerValue.Open ||
                    lastKnownDrawerValue == BottomDrawerValue.Expanded)
            && drawerState.currentValue == BottomDrawerValue.Closed
        ) {
            block()
        }
        @Suppress("UNUSED_VALUE")
        lastKnownDrawerValue = drawerState.currentValue
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BaseScreenActivity(
    timerPresenter: OfficeTimeBeforeHolidaysScreenPresenter = getViewModel(),
) {
    val scope = rememberCoroutineScope()
    val holidaysDrawerState = rememberBottomDrawerState(initialValue = BottomDrawerValue.Closed)
    val officeDrawerState = rememberBottomDrawerState(initialValue = BottomDrawerValue.Closed)
    val isPaused = timerPresenter.uiState.isPaused
    val remainingTime = timerPresenter.uiState.remainingTime
    val progress = timerPresenter.uiState.progressInRemainingTime
    val sideBackdropState = rememberSideBackdropState(SideBackdropValue.Concealed)

    onCloseBottomDrawer(drawerState = holidaysDrawerState) {
        timerPresenter.updateRemainingTime()
        timerPresenter.updateProgressInRemainingTime()
    }
    onCloseBottomDrawer(drawerState = officeDrawerState) {
        timerPresenter.updateRemainingTime()
        timerPresenter.updateProgressInRemainingTime()
    }

    LaunchedEffect(remainingTime) {
        if (!isPaused) {
            timerPresenter.updateRemainingTime()
        }
    }
    LaunchedEffect(progress) {
        if (!isPaused) {
            timerPresenter.updateProgressInRemainingTime()
        }
    }

    BaseScreen(
        isPaused = isPaused,
        onLaunchTimer = { timerPresenter.setPause(false); timerPresenter.updateRemainingTime() },
        onPauseTimer = { timerPresenter.setPause(true) },
        onOpenWeekCalendar = {
            scope.launch {
                holidaysDrawerState.snapTo(BottomDrawerValue.Closed)
                officeDrawerState.open()
            }
        },
        onOpenHolidaysCalendar = {
            scope.launch {
                officeDrawerState.snapTo(BottomDrawerValue.Closed)
                holidaysDrawerState.open()
            }
        },
        mainContent = {
            TimerScreen(remainingTime = remainingTime, progress = progress)
        },
        backLayerContent = {
            SettingsScreenActivity()
        },
        sideBackdropState = sideBackdropState,
        holidaysCalendarDrawerState = holidaysDrawerState,
        officeCalendarDrawerState = officeDrawerState,
        holidaysCalendarDrawerContent = { HolidaysCalendarEditorScreenActivity() },
        officeCalendarDrawerContent = { WeekCalendarEditorScreenActivity() },
    )
}

@ExperimentalMaterialApi
@Composable
fun BaseScreen(
    isPaused: Boolean,
    onLaunchTimer: () -> Unit,
    onPauseTimer: () -> Unit,
    onOpenWeekCalendar: () -> Unit,
    onOpenHolidaysCalendar: () -> Unit,
    mainContent: @Composable BoxScope.() -> Unit,
    backLayerContent: @Composable () -> Unit,
    sideBackdropState: SideBackdropState,
    holidaysCalendarDrawerState: BottomDrawerState,
    officeCalendarDrawerState: BottomDrawerState,
    holidaysCalendarDrawerContent: @Composable ColumnScope.() -> Unit,
    officeCalendarDrawerContent: @Composable ColumnScope.() -> Unit
) {
    BaseLayout(
        holidaysCalendarDrawerState = holidaysCalendarDrawerState,
        officeCalendarDrawerState = officeCalendarDrawerState,
        holidaysCalendarDrawerContent = holidaysCalendarDrawerContent,
        officeCalendarDrawerContent = officeCalendarDrawerContent,
        bottomContent = BottomContent(
            isFloatingActionButtonDocked = true,
            floatingActionButtonPosition = FabPosition.Center,
            floatingActionButton = {
                FloatingPauseButton(
                    isPaused = isPaused,
                    onLaunchTimer = onLaunchTimer,
                    onPauseTimer = onPauseTimer
                )
            },
            bottomBar = {
                BottomBar(
                    onOpenWeekCalendar = onOpenWeekCalendar,
                    onOpenHolidaysCalendar = onOpenHolidaysCalendar
                )
            }
        ),
        mainContent = mainContent,
        sideBackdropState = sideBackdropState,
        backLayerContent = backLayerContent,
    )
}

@ExperimentalMaterialApi
@Composable
fun BaseLayout(
    mainContent: @Composable BoxScope.() -> Unit,
    bottomContent: BottomContent,
    backLayerContent: @Composable () -> Unit,
    sideBackdropState: SideBackdropState,
    holidaysCalendarDrawerState: BottomDrawerState,
    officeCalendarDrawerState: BottomDrawerState,
    holidaysCalendarDrawerContent: @Composable ColumnScope.() -> Unit,
    officeCalendarDrawerContent: @Composable ColumnScope.() -> Unit
) {
    SideBackdrop(
        scaffoldState = sideBackdropState,
        directions = persistentSetOf(RevealDirection.Left),
        backLayerContent = backLayerContent,
        gesturesEnabled = !holidaysCalendarDrawerState.isOpen && !officeCalendarDrawerState.isOpen,
        frontLayerContent = {
            BottomDrawer(
                drawerState = holidaysCalendarDrawerState,
                drawerContent = holidaysCalendarDrawerContent,
                gesturesEnabled = holidaysCalendarDrawerState.isOpen,
                content = {
                    BottomDrawer(
                        drawerState = officeCalendarDrawerState,
                        drawerContent = officeCalendarDrawerContent,
                        gesturesEnabled = officeCalendarDrawerState.isOpen,
                        content = {
                            Scaffold(
                                floatingActionButton = bottomContent.floatingActionButton,
                                floatingActionButtonPosition = bottomContent.floatingActionButtonPosition,
                                isFloatingActionButtonDocked = bottomContent.isFloatingActionButtonDocked,
                                bottomBar = bottomContent.bottomBar
                            ) { innerPadding ->
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(innerPadding),
                                    content = mainContent
                                )
                            }
                        }
                    )
                }
            )
        }
    )
}
