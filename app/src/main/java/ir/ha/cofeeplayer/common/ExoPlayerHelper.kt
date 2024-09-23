package ir.ha.cofeeplayer.common

import android.content.Context
import android.net.Uri
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer

class ExoPlayerHelper(private val context: Context) {
    private var exoPlayer: SimpleExoPlayer? = null

    fun initializePlayer(uri: Uri) {
        if (exoPlayer == null) {
            exoPlayer = SimpleExoPlayer.Builder(context).build()
        }

        val mediaItem = MediaItem.fromUri(uri)
        exoPlayer?.setMediaItem(mediaItem)
        exoPlayer?.prepare()
    }

    fun play() {
        exoPlayer?.play()
    }

    fun pause() {
        exoPlayer?.pause()
    }

    fun releasePlayer() {
        exoPlayer?.release()
        exoPlayer = null
    }

    fun isPaying() = exoPlayer?.isPlaying

    fun duration() = exoPlayer?.duration

    fun mute(mute : Boolean) {
        exoPlayer?.setDeviceMuted(mute)
    }


    fun exoObject() : SimpleExoPlayer? = exoPlayer

}
