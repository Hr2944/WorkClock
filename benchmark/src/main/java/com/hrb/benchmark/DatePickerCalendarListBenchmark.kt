package com.hrb.benchmark

import androidx.benchmark.macro.*
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Direction
import androidx.test.uiautomator.Until
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class DatePickerCalendarListBenchmark {
    @get:Rule
    val benchmarkRule: MacrobenchmarkRule = MacrobenchmarkRule()

    @OptIn(ExperimentalMetricApi::class)
    @Test
    fun scrollComposeList() {
        benchmarkRule.measureRepeated(
            packageName = "com.hrb.holidays",
            metrics = listOf(
                StartupTimingMetric(),
                FrameTimingMetric(),
                TraceSectionMetric("DatePickerTraceSection")
            ),
            startupMode = StartupMode.COLD,
            iterations = 1,
            setupBlock = {
                // Before starting to measure, navigate to the UI to be measured
                startActivityAndWait()
                device.wait(
                    Until.hasObject(By.res("CalendarBodyLazyColumn")),
                    500
                )
            }
        ) {
            /**
             * To access a composable we need to set:
             * 1) Modifier.semantics { testTagsAsResourceId = true } once
             * 2) Add Modifier.testTag("someIdentifier") to all of the composables you want to access
             */

            val column = device.findObject(By.res("CalendarBodyLazyColumn"))

            // Set gesture margin to avoid triggering gesture navigation
            // with input events from automation.
            column.setGestureMargin(device.displayWidth / 5)

            // Scroll down several times
            repeat(20) {
                column.scroll(Direction.DOWN, .3f)
            }
        }
    }
}
