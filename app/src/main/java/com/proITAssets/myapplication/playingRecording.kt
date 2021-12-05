package com.proITAssets.myapplication


import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Environment
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_playing_recording.*


class playingRecording : Fragment(R.layout.fragment_playing_recording) {


    var counter = 0

    // protected val thename = ed_name
    var time_in_millsec = 0L
    val done: String = "It's Done"
    private lateinit var countDown_Timer: CountDownTimer
    private var runningtime: Boolean = false
    private var START_MILLI_SECONDS = 60000L
    private val rnds = (0..10).random()
    private var runtime = rnds * 1000L
    private var mParam1 = ed_name.toString()

    private var output = Environment.getExternalStorageDirectory().toString() + "/$mParam1.mp3"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // cTimer = view.findViewById(R.id.mTextField)

//
//        play?.setOnClickListener {
//            val player = MediaPlayer()
//            player.reset()
//            player.setDataSource(this.output)
//            Toast.makeText(context, output, Toast.LENGTH_LONG).show()
//            Toast.makeText(context, "this is working", Toast.LENGTH_LONG).show()
//            if (runningtime)
//                pauseTimer()
//            countDown_Timer.cancel()
//
//
//            val time = mTextField.text.toString()
//            time_in_millsec = time.toLong() * 60000L
//            startTimer(time_in_millsec)
//            val rnds = (0..10).random()
//            player.setDataSource(this.output)
//            Toast.makeText(context, output, Toast.LENGTH_LONG)
//
//            while (time_in_millsec.equals(0)) {
//                var runtime = time.toLong() * rnds
//                //runtime *= 10000L
//
//                if (!player.isPlaying) {
//                    if (runtime.equals(0)){
//
//                        player.prepare()
//                        player.start()
//                    }
//                }
//            }
//
//
//        }
//        resetime?.setOnClickListener {
//            resetTheTime()
//        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_playing_recording, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (getArguments() != null) {
            mParam1 = getArguments()?.getString("params").toString();
        }

        play.setOnClickListener {

            //This is the  method that work for the app
            val player = MediaPlayer()
            player.reset()
            player.setDataSource(this.output)
            Toast.makeText(context, output, Toast.LENGTH_LONG).show()
//            player.prepare()
//            player.start()
            if (runningtime)
                countDown_Timer.cancel()
//                pauseTimer()


            val time = mTextField.text.toString()
            time_in_millsec = time.toLong() * 60000L
            startTimer(time_in_millsec)
            startSubTimer(runtime)
            //Toast.makeText(context, "$runtime", Toast.LENGTH_SHORT).show()


            if (runtime != 0L) {
                while (time_in_millsec != 0L) {
                    Toast.makeText(context, "$runtime", Toast.LENGTH_SHORT).show()
                    if (!player.isPlaying && runtime == 0L) {
                        player.prepare()
                        player.start()
                    } else {
                        Toast.makeText(context, "runtime is not 0", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(context, "Runtime is not 0", Toast.LENGTH_SHORT).show()
            }

        }
        resetime.setOnClickListener {
            resetTheTime()
        }
    }

    private fun resetTheTime() {
        time_in_millsec = START_MILLI_SECONDS
        updatetextUI()
        countDown_Timer.cancel()
        resetime.visibility = View.INVISIBLE
        countime.text = "Enter the new time"
        play.text = "Start"
    }

    private fun pauseTimer() {
        Toast.makeText(context, "This is working", Toast.LENGTH_LONG).show()
        play.text = "Start"
        countDown_Timer.cancel()
        runningtime = false
        resetime.visibility = View.VISIBLE
    }

    private fun startTimer(timeInMillsec: Long) {
        countDown_Timer = object : CountDownTimer(timeInMillsec, 1000) {

            override fun onTick(p0: Long) {
                time_in_millsec = p0
                updatetextUI()
            }

            override fun onFinish() {
                done.toEditable().also { mTextField.text = it }
                countime.text = "Finished"
            }
        }
        countDown_Timer.start()
        runningtime = true
        play?.text = "Pause"
        resetime.visibility = View.VISIBLE
    }

    private fun startSubTimer(runtime: Long) {
        countDown_Timer = object : CountDownTimer(runtime, 1000) {

            override fun onTick(p0: Long) {
            }

            override fun onFinish() {
            }
        }
        countDown_Timer.start()
        runningtime = true
        play?.text = "Pause"
        resetime.visibility = View.VISIBLE
    }

    private fun updatetextUI() {
        val mim = (time_in_millsec / 1000) / 60
        val sec = (time_in_millsec / 1000) % 60
        countime.text = "$mim:$sec"
    }


    fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)

}