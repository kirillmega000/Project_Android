package com.example.company.maina

import android.Manifest
import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import kotlinx.android.synthetic.main.fragment_home.*
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.media.MediaRecorder
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.widget.Toast

import java.util.*

import android.content.Context.*

import android.content.pm.PackageManager
import android.os.FileUtils
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.internal.schedulers.IoScheduler
import io.reactivex.schedulers.Schedulers
import java.io.*
import java.nio.charset.Charset
import java.nio.file.Files


class HomeFragment : Fragment() {
    private var state: Boolean = false
    var mediaRecorder: MediaRecorder? = null
    var request: Disposable? = null
    private var output: String? = null
    private var recordingTime: Long = 0
    private var timer = Timer()
    private val dir: File = File(Environment.getExternalStorageDirectory().absolutePath + "/soundrecorder/recordings/")
    private var dirmeta:File=File(Environment.getExternalStorageDirectory().absolutePath + "/soundrecorder/metas/")

    private var outfile:String?=null
    companion object {

        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {


        return inflater.inflate(R.layout.fragment_home, container, false)


    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        try {
            // create a File object for the parent directory
            val recorderDirectory = File(Environment.getExternalStorageDirectory().absolutePath + "/soundrecorder/recordings/")
            val metasRepository= File(Environment.getExternalStorageDirectory().absolutePath + "/soundrecorder/metas/")
            // have the object build the directory structure, if needed.
            recorderDirectory.mkdirs()

            metasRepository.mkdirs()

        } catch (e: IOException) {
            e.printStackTrace()
        }

        if (dir.exists()) {
            val count = dir.listFiles().size
            output = Environment.getExternalStorageDirectory().absolutePath + "/soundrecorder/recordings/recording" + count + ".mp4"

        }
        if (dirmeta.exists()) {
            val count = dirmeta.listFiles().size
            outfile = Environment.getExternalStorageDirectory().absolutePath + "/soundrecorder/recordings/metas/meta" + count+".txt"

        }




        mediaRecorder = MediaRecorder()

        mediaRecorder?.setAudioSource(MediaRecorder.AudioSource.MIC)
        mediaRecorder?.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
        mediaRecorder?.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
        mediaRecorder?.setOutputFile(output)
        fab_start_recording.setOnClickListener {
            onButton()
        }


        sending()


    }
    private  fun sending(){
        var files=dir.listFiles().filter { file -> var metaFile=("meta"+file.name.substring(9,file.name.length-4))
            this?.context?.openFileInput(metaFile)?.readBytes()?.toString(Charset.forName("UTF-8"))?.contains("false")?:false }
        if(files.isNotEmpty())
       for(it in files)
           sendFile(it)
    }
    private fun sendFile( file:File){

        var metaFile=("meta"+file.name.substring(9,file.name.length-4))
        var info=this?.context?.openFileInput(metaFile)?.readBytes()?.toString(Charset.forName("UTF-8"))?:return

        Log.d("CheckGovno",metaFile)
        if(info.contains("true"))info="true"
        var obv=createRequest("http://192.168.100.222:8080/upload",file.absolutePath,info).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

        request=obv.subscribe({

                info =info.replace("false","true")
                Log.d("CheckGovno",info)
                context?.deleteFile(metaFile)
                this?.context?.openFileOutput(metaFile, MODE_PRIVATE)?.write(info.toByteArray(Charsets.UTF_8))


            initRecorder()
             },
                {Log.d("CheckSend","Fail")})


    }
    private fun initRecorder() {
        mediaRecorder = MediaRecorder()

        if (dir.exists()) {
            val count = dir.listFiles().size
            output = Environment.getExternalStorageDirectory().absolutePath + "/soundrecorder/recordings/recording" + count + ".mp4"
            Log.d("ENTERED", "sound" + count)
        }
        if (dirmeta.exists()) {
            val count = dirmeta.listFiles().size
            outfile = Environment.getExternalStorageDirectory().absolutePath + "/soundrecorder/recordings/metas/meta" + count+".txt"

        }
        mediaRecorder?.setAudioSource(MediaRecorder.AudioSource.MIC)
        mediaRecorder?.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
        mediaRecorder?.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
        mediaRecorder?.setOutputFile(output)

    }


    fun onButton() {
        if (state) stopRecording()
        else startRecording()

    }


    private fun startRecording() {
        if (ContextCompat.checkSelfPermission(context?:return, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            val count = dir.listFiles().size
            var fileName:String="meta"+count
            var prefs=this.context?.getSharedPreferences("NamePrefs",MODE_PRIVATE)
            var name:String?=prefs?.getString("name","NoName")?:"NoName"
            Log.d("FileCheck",count.toString()+formatLocation())
            this.context?.openFileOutput(fileName, MODE_PRIVATE)?.write(("Name=${name} ;"+formatLocation()+"; Send:false").toByteArray())?:return
            this.context?.openFileInput(fileName).use{
                Log.d("FileCheck",it?.readBytes()?.toString(Charset.forName("UTF-8"))) }
        } else {
            // Request permission from the user
            ActivityCompat.requestPermissions(activity?.parent?:return,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 0)

        }
        Toast.makeText(context, "Запись началась!", Toast.LENGTH_SHORT).show()
        fab_start_recording.setImageResource(R.drawable.ic_stop)
        state = true
        Log.d("ENTERED", "started")
        try {
            println("Starting recording!")
            mediaRecorder?.prepare()
            mediaRecorder?.start()
            startTimer()
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }


    fun stopRecording() {
        state = false
        Log.d("ENTERED", "stopped")
        mediaRecorder?.stop()
        mediaRecorder?.release()

        stopTimer()
        fab_start_recording.setImageResource(R.drawable.ic_mic_black_24dp)
        val path = File(Environment.getExternalStorageDirectory().absolutePath + "/soundrecorder/recordings/recording" + (dir.listFiles().size - 1) + ".mp4")
        val copy= File (Environment.getExternalStorageDirectory().absolutePath + "/soundrecorder/metas/recording" + ".mp4")
        if(copy.exists()){
            copy.delete()
        }
        copy.createNewFile()
        org.apache.commons.io.FileUtils.copyFile(path,copy)
        initRecorder()
        sending()
    }


    fun startTimer() {
        resetTimer()
        timer=Timer()
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                recordingTime += 1
                activity?.runOnUiThread { updateDisplay() }
            }
        }, 1000, 1000)
    }

    fun stopTimer() {
        timer.cancel()
    }


    fun resetTimer() {
        timer.cancel()
        recordingTime = 0
        textview_recording_time.text = "00:00"
    }

    fun updateDisplay() {

        val minutes = recordingTime / (60)
        val seconds = recordingTime % 60
        val str = String.format("%d:%02d", minutes, seconds)
        textview_recording_time.text = str
    }
    private fun formatLocation(): String {
        var location=MyLocationListener.imHere
        return if (location == null) "none" else (String.format(
                "Coordinates: lat = %1$.4f, lon = %2$.4f",
                location!!.getLatitude(), location!!.getLongitude())+", time="+time.gettime())

    }



}