package ir.ha.cofeeplayer.screens

import android.util.Log
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class CoroutineTimer(
    private val totalSeconds: Int,
    private val onTick: (Int) -> Unit,
    private val onFinished: () -> Unit
) {
    private var currentSecond: Int = 0
    private var isPaused: Boolean = false
    private var job: Job? = null
    private var coroutineContext: CoroutineContext = Dispatchers.Main

    // Start the timer
    fun start() {
        job?.cancel()
        job = CoroutineScope(coroutineContext).launch {
            for (second in currentSecond until totalSeconds) {
                if (isPaused) {
                    break // Stop the loop if paused
                }
                Log.i(TAG, "current time in the time is $currentSecond")
                onTick(second) // Callback with the current second
                currentSecond = second + 1
                delay(1000L) // Delay for 1 second
            }
            if (currentSecond == totalSeconds) {
                onFinished() // Callback when timer is finished
            }
        }
    }

    // Pause the timer
    fun pause() {
        isPaused = true
    }

    // Resume the timer from where it was paused
    fun resume() {
        if (isPaused) {
            isPaused = false
            start() // Restart the timer from the currentSecond
        }
    }

    // Stop and reset the timer
    fun stop() {
        job?.cancel() // Cancel the coroutine job
        currentSecond = 0 // Reset the counter
        isPaused = false // Reset the paused state
    }
}
