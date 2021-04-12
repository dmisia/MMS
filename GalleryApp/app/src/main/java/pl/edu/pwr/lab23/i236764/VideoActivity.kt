package pl.edu.pwr.lab23.i236764

import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import android.widget.MediaController
import android.widget.SeekBar
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_video.*
import kotlinx.android.synthetic.main.audio_fullscreen.button
import kotlinx.android.synthetic.main.audio_fullscreen.button2
import java.util.*

class VideoActivity : AppCompatActivity() {

    var audioManager
            : AudioManager? = null
    var volumeControl
            : SeekBar? = null
    var scrubber
            : SeekBar? = null
    var audio: MediaPlayer? = null
    private lateinit var path: String


    var video: VideoView? = null
    var controller
            : MediaController? = null

    fun playVideo(view: View?) {
        video!!.start()
    }

    fun pauseVideo(view: View?) {
        if (video!!.isPlaying) video!!.pause()
    }

    fun playAudio(view: View?) {
        audio!!.start()
        button.setVisibility(View.INVISIBLE)
        button2.setVisibility(View.VISIBLE)
    }

    fun pauseAudio(view: View?) {
        if (audio!!.isPlaying) audio!!.pause()
        button.setVisibility(View.VISIBLE)
        button2.setVisibility(View.INVISIBLE)
    }

    fun play(view: View) {
        front.setVisibility(View.INVISIBLE)
        if (view.resources.getResourceEntryName(view.id) == "audioPlay") {
            audioLayout.setVisibility(View.VISIBLE)
            playAudio(view)
        } else {
            video?.setVisibility(View.VISIBLE)
            playVideo(view)
        }
    }

    fun back(view: View?) {
        pauseAudio(view)
        pauseVideo(view)
        audioLayout.setVisibility(View.INVISIBLE)
        video?.setVisibility(View.INVISIBLE)
        front.setVisibility(View.VISIBLE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video)
        path = intent.extras?.getString("path") as String

        //FOR VIDEO
        video = findViewById(R.id.videoView) as VideoView
        video!!.setVideoPath("android.resource://" + packageName.toString() + "/" + R.raw.video)
        controller = MediaController(this)
        controller!!.setAnchorView(video)
        video!!.setMediaController(controller)

        //FOR AUDIO
        volumeControl = findViewById(R.id.seekBar) as SeekBar
        audio = MediaPlayer.create(this, R.raw.music)
        audioManager =
            getSystemService(Context.AUDIO_SERVICE) as AudioManager
        val maxVolume =
            audioManager!!.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
        val curVolume = audioManager!!.getStreamVolume(AudioManager.STREAM_MUSIC)
        volumeControl!!.max = maxVolume
        volumeControl!!.progress = curVolume
        scrubber = findViewById(R.id.scrubber) as SeekBar
        scrubber!!.max = audio!!.duration

        Timer().scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                scrubber!!.progress = audio!!.currentPosition
                volumeControl!!.progress = audioManager!!.getStreamVolume(AudioManager.STREAM_MUSIC)
            }
        }, 0, 100)
        scrubber!!.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(
                seekBar: SeekBar,
                i: Int,
                b: Boolean
            ) {
                if (b) audio!!.seekTo(i)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })
        volumeControl!!.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(
                seekBar: SeekBar,
                i: Int,
                b: Boolean
            ) {
                audioManager!!.setStreamVolume(
                    AudioManager.STREAM_MUSIC,
                    i,
                    0
                )
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })
    }
}
