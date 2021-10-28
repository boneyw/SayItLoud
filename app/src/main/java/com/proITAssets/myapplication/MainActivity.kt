package com.proITAssets.myapplication

import android.Manifest
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.SystemClock
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private var output = Environment.getExternalStorageDirectory().toString() + "/recording.mp3"


    private lateinit var mediaRecorder: MediaRecorder
    private var state: Boolean = false
    private var recordingStopped: Boolean = true

    private var recordingfile = ""
    private var chronometerPausedTime: Long = 0
    private var LOG_TAG = "AudioRecordTest"

    private val thename by lazy { findViewById<TextView>(R.id.ed_name) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val play = playingRecording()
        supportFragmentManager.beginTransaction().replace(R.id.fragment_play, play).commit()
        mediaRecorder = MediaRecorder()



        button_stop_recording.isEnabled = false
        button_pause_recording.isEnabled = false

        if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            val permissions = arrayOf(
                    android.Manifest.permission.RECORD_AUDIO,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            )
            ActivityCompat.requestPermissions(this, permissions, 0)
        }
        button_start_recording.setOnClickListener {
            if (!thename.text.toString().isNotEmpty()) {
                Toast.makeText(this, "User need to enter the file name", Toast.LENGTH_SHORT).show()
            } else {
                if (ContextCompat.checkSelfPermission(this,
                                Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    val permissions = arrayOf(android.Manifest.permission.RECORD_AUDIO,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    ActivityCompat.requestPermissions(this, permissions, 0)


                    //output = Environment.getExternalStorageDirectory().toString() + "/recording.mp3"
                    //output = "${externalCacheDir?.absolutePath}/audiorecordtest.mp3"
                    mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC)
                    mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
                    mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
                    mediaRecorder.setOutputFile(output)
                } else {
                    startRecording()
                    button_stop_recording.isEnabled = true
                    button_start_recording.isEnabled = false
                    button_pause_recording.isEnabled = true
                }
            }
        }

        button_stop_recording.setOnClickListener {
            stopRecording()
            button_start_recording.isEnabled = true
            button_stop_recording.isEnabled = false
        }

        button_pause_recording.setOnClickListener {
            pauseRecording()
            button_start_recording.isEnabled = true
        }

        btn_play.setOnClickListener {
            playRecordingmade()
        }
    }

    private fun startRecording() {
        startFromZero()
        var name = File(getExternalFilesDir("/"), recordingfile)

        val timestamp = SimpleDateFormat(
                "MM/dd/yyyy_HH:mm:ss:a",
                Locale.US,
        ).format(System.currentTimeMillis())
        val date = Date()
        //recordingfile = " ${ed_name.text} " + timestamp.format(date) + ".mp3"

        try {
            if (name != null) {
                //Toast.makeText(this, "This is your file name $recordingfile", Toast.LENGTH_SHORT).show()

                //output = "${externalCacheDir?.absolutePath}/audiorecordtest.mp3"
                mediaRecorder.setOutputFile(output)
                Toast.makeText(this, "Recording started! $output", Toast.LENGTH_SHORT).show()
                mediaRecorder.prepare()
                mediaRecorder.start()


            }
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    @SuppressLint("RestrictedApi", "SetTextI18n")
    @TargetApi(Build.VERSION_CODES.N)
    private fun pauseRecording() {

        if (state) {
            pauseTimer()
            Toast.makeText(this, "This is working", Toast.LENGTH_SHORT).show()
            if (!recordingStopped) {
                Toast.makeText(this, "Stopped!", Toast.LENGTH_SHORT).show()
                mediaRecorder.pause()
                recordingStopped = true
                button_pause_recording.text = "Resume"
            } else {
                resumeRecording()
                resumeTimer()
            }
        } else {
            Toast.makeText(this, "this is not working", Toast.LENGTH_SHORT).show()
        }
    }

    @SuppressLint("RestrictedApi", "SetTextI18n")
    @TargetApi(Build.VERSION_CODES.N)
    private fun resumeRecording() {
        Toast.makeText(this, "Resume!", Toast.LENGTH_SHORT).show()
        mediaRecorder.resume()
        button_pause_recording.text = "Pause"
        recordingStopped = false


    }

    private fun stopRecording() {

        stopTimer()
        if (state) {
            mediaRecorder.stop()
            mediaRecorder.release()
            state = false
            simpleChronometer.stop()
            recordingStopped = true
        } else {
            Toast.makeText(this, "You are not recording right now! $output", Toast.LENGTH_LONG)
                .show()
        }
    }

    private fun playRecordingmade() {

        val player = MediaPlayer()
        player.reset()
        player.setDataSource(this.output)
        player.prepare()
        player.start()
        Toast.makeText(this, output, Toast.LENGTH_LONG).show()


//        try{
//
//            player.setDataSource(this.output)
//            Toast.makeText(this, output, Toast.LENGTH_LONG).show()
//            player.prepare()
//            player.start()
//
//
//        }catch (ex: IOException){
//            Toast.makeText(this, "Audio is not playing ", Toast.LENGTH_LONG).show()
//        }

    }

    private fun savefilename() {

        val name = "rec_$thename.mp3"
        val timestamp = SimpleDateFormat("MMddyyyy_HHmmss",
                Locale.getDefault()).format(System.currentTimeMillis())

        try {
            val rec = File("$output/Recording/")
            mediaRecorder = MediaRecorder()
            output = Environment.getExternalStorageDirectory().toString() + "/$thename.mp3"

            val file = File(name)
            val fw = FileWriter(file.absoluteFile)
            val bw = BufferedWriter(fw)
            bw.write(name)
            bw.close()

            Toast.makeText(this, name + "is name of the file \n" + rec, Toast.LENGTH_LONG).show()

        } catch (e: Exception) {
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT)
        }
    }

    private fun startFromZero() {
        simpleChronometer.base = SystemClock.elapsedRealtime()
        simpleChronometer.start()
    }

    private fun pauseTimer() {
        chronometerPausedTime = SystemClock.elapsedRealtime() - this.simpleChronometer.base
        simpleChronometer.stop()
    }

    private fun resumeTimer() {
        simpleChronometer.base = SystemClock.elapsedRealtime() - chronometerPausedTime
        chronometerPausedTime = 0
        simpleChronometer.start()
    }

    private fun stopTimer() {
        simpleChronometer.base = SystemClock.elapsedRealtime()
        simpleChronometer.stop()
    }
}