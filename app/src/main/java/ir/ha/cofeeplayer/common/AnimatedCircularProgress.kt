package ir.ha.cofeeplayer.common

import android.util.Log
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


val COMMON_TAG = "COMMON_TAG"

@Composable
fun AnimatedCircleProgressBar(
    progress: Float, // Progress (0 to 100)
    modifier: Modifier = Modifier,
    size: Dp = 200.dp, // Configurable size
    strokeWidth: Dp = 12.dp, // Progress bar thickness
    progressColor: Color = Color.Blue, // Progress color
    backgroundColor: Color = Color.Gray, // Background circle color
    animDuration: Int = 1000, // Animation duration (milliseconds)
    fontSize: TextUnit = 28.sp // Progress Text size
) {
    // Animate progress change
    val animatedProgress = animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = animDuration), label = ""
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.size(size)
    ) {
        Canvas(modifier = Modifier.size(size)) {
            // Background circle
            drawArc(
                color = backgroundColor,
                startAngle = 270f, // Start from top (270 degrees)
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(strokeWidth.toPx(), cap = StrokeCap.Round)
            )

            // Progress circle
            drawArc(
                color = progressColor,
                startAngle = 270f, // Start from top (270 degrees)
                sweepAngle = (animatedProgress.value / 100) * 360, // Calculate sweep angle based on progress
                useCenter = false,
                style = Stroke(strokeWidth.toPx(), cap = StrokeCap.Round)
            )
        }

        // Progress percentage text
        Text(
            text = "${animatedProgress.value.toInt()}%",
            fontSize = fontSize,
            fontWeight = FontWeight.Bold,
            color = progressColor
        )
    }
}

@Composable
fun CircleProgressBarDemo() {
    var progress by remember { mutableFloatStateOf(24f) }

    // Slider to simulate progress change
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxSize()) {
        AnimatedCircleProgressBar(
            progress = progress,
            size = 150.dp,
            strokeWidth = 8.dp,
            progressColor = Color.Black,
            backgroundColor = Color.LightGray
        )
        Spacer(modifier = Modifier.height(20.dp))
        Slider(
            value = progress,
            onValueChange = { progress = it },
            valueRange = 0f..100f
        )
    }
}

@Composable
fun LinearProgressBar(
    modifier: Modifier = Modifier,
    progress: Float, // Progress (0 to 100)
    valueRange: ClosedFloatingPointRange<Float> = 0f..100f, // Range 0..100
    colors: SliderColors = SliderDefaults.colors(),
    onValueChange: (progress : Float) -> Unit,
    onValueChangeFinished: (() -> Unit)? = null,
) {

    // animateFloatAsState to animate the progress change
    val animatedProgress by animateFloatAsState(
        targetValue = progress, // Target progress value
        animationSpec = tween(500), label = "" // Animation duration in milliseconds
    )

    Log.i(BASIC_TAG, "LinearProgressBar: $animatedProgress")

    Slider(
        modifier = modifier,
        value = animatedProgress, // Use the animated value for smooth transition
        onValueChange = {
            onValueChange.invoke(it)
        },
        valueRange = valueRange,
        colors = colors,
        onValueChangeFinished = onValueChangeFinished,
    )
}




@Composable
fun LinearProgressBar(
    modifier: Modifier = Modifier,
    progress: Float, // Progress (0 to 100)
    valueRange: ClosedFloatingPointRange<Float> = 0f..100f, // Range 0..100
    onValueChange: (progress : Float) -> Unit,
    onValueChangeFinished: (() -> Unit)? = null
) {
    Slider(
        modifier = modifier,
        value = progress,
        onValueChange = { onValueChange.invoke(progress) },
        valueRange = valueRange,
        onValueChangeFinished = onValueChangeFinished, // Non-composable lambda
    )
}
