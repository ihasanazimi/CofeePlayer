package ir.ha.cofeeplayer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ir.ha.cofeeplayer.ui.theme.CofeePlayerTheme
import ir.ha.cofeeplayer.ui.theme.MusicPlayerScreen



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CofeePlayerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AnimatedCircleProgressBar(
                        progress = 24f,
                        modifier = Modifier.padding(innerPadding),
                        size = 200.dp,
                        strokeWidth = 52.dp,
                        progressColor = Color.Green,
                        backgroundColor = Color.White,
                        animDuration = 5000,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun GreetingPreview() {

//    val context = LocalContext.current // Get context in the composable
//
//    LinearProgressBar(
//        modifier = Modifier
//            .wrapContentSize()
//            .padding(horizontal = 16.dp, vertical = 8.dp)
//            .background(Color.Transparent),
//        progress = 24f, // Example progress value
//        valueRange = 0f..100f,
//        colors = SliderColors(
//            thumbColor = Color.Black,
//            activeTrackColor = Color.Black,
//            activeTickColor = Color.LightGray,
//            inactiveTrackColor = Color.LightGray,
//            inactiveTickColor = Color.LightGray,
//            disabledThumbColor = Color.LightGray,
//            disabledActiveTrackColor = Color.LightGray,
//            disabledActiveTickColor = Color.LightGray,
//            disabledInactiveTrackColor = Color.LightGray,
//            disabledInactiveTickColor = Color.LightGray
//        ),
//        onValueChangeFinished = {
//            // Toast should be triggered in a non-composable lambda
//            Toast.makeText(context, "Value change finished!", Toast.LENGTH_SHORT).show()
//        }
//    )

    MusicPlayerScreen(
        isPlaying = true,
        isRepeatOn = true,
        isShuffleOn = true,
        isFavorite = true,
        onPlayPauseClicked = {},
        onNextClicked = {},
        onPreviousClicked = {},
        onRepeatClicked = {},
        onShuffleClicked = {},
        onFavoriteClicked = {},
        onBackClicked = {},
        onMoreClicked = {}
    )
}