package com.hrb.holidays.commons

import android.util.Log
import androidx.compose.runtime.Composable

class ExecTime {
    private var total = 0L
    private var lastStart = 0L
    private var count = 0
    val mean: Long
        get() = total / count

    fun start() {
        lastStart = System.nanoTime()
    }

    fun end(): Long {
        val took = System.nanoTime() - lastStart
        add(took)
        return took
    }

    fun add(nanos: Long) {
        count++
        total += nanos
    }

    companion object {
        private val timerInstances: MutableMap<String, ExecTime> = mutableMapOf()

        @Composable
        fun execComposable(tag: String, block: @Composable () -> Unit) {
            val timer = timerInstances.getOrPut(tag) { ExecTime() }
            timer.start()
            block()
            val took = timer.end()
            Log.d(
                "ExecTime::$tag",
                "Execution number ${timer.count} :: Took $took nanos :: Mean is ${timer.mean} nanos"
            )
        }

        fun <T> execBlock(tag: String, block: () -> T): T {
            val timer = timerInstances.getOrPut(tag) { ExecTime() }
            timer.start()
            val result = block()
            val took = timer.end()
            Log.d(
                "ExecTime::$tag",
                "Execution number ${timer.count} :: Took $took nanos :: Mean is ${timer.mean} nanos"
            )
            return result
        }
    }
}
