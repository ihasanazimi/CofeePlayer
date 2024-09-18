package ir.ha.cofeeplayer.screens

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

class Timer(private val scope: CoroutineScope) {

    private var timerJob: Job? = null

    fun startTimer(
        timeInSeconds: Int,
        onTick: (Int) -> Unit,
        onComplete: () -> Unit = {}
    ) {
        timerJob?.cancel()

        timerJob = scope.launch {
            (timeInSeconds downTo 0).asFlow()
                .onEach { secondsLeft ->
                    onTick(secondsLeft)
                    delay(1.seconds)
                    Log.i(TAG, "startTimer: $secondsLeft")
                }
                .onCompletion {
                    onComplete()
                    Log.i(TAG, "startTimer - onCompletion")
                }
                .catch { e ->
                    e.printStackTrace()
                    Log.i(TAG, "startTimer - ${e.message}")
                }
                .collect()
        }
    }

    fun stopTimer() {
        timerJob?.cancel()
    }
}


/**
 *
 * val scope = CoroutineScope(Dispatchers.Default)
 * val timer = Timer(scope)
 *
 * // شروع یک تایمر برای ۳۰ ثانیه
 * timer.startTimer(30, onTick = { secondsLeft ->
 *     println("Seconds left: $secondsLeft")
 * }, onComplete = {
 *     println("Timer complete!")
 * })
 *
 * // در هر زمانی می‌توانید تایمر را متوقف کنید:
 * timer.stopTimer()
 *
 * // برای شروع یک تایمر جدید و ریست تایمر قبلی:
 * timer.startTimer(60, onTick = { secondsLeft ->
 *     println("Seconds left: $secondsLeft")
 * })
 *
 */