package com.example.company.maina

import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Environment
import android.support.v4.content.ContextCompat.getSystemService
import android.util.Log
import android.widget.Toast
import java.io.File
import java.nio.charset.Charset
import java.nio.file.Files

class RecordingRepository{
    companion object {
        @Volatile
        private var instance: RecordingRepository? = null
        var mediaPlayer: MediaPlayer?=null
        fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: RecordingRepository().also { instance = it }
            }


        fun playRecording(context: Context, title: String){
            val path = Uri.parse(Environment.getExternalStorageDirectory().absolutePath + "/soundrecorder/recordings/$title")


            val manager: AudioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
            if(manager.isMusicActive) {
                Toast.makeText(context, "Another recording is just playing! Wait until it's finished!", Toast.LENGTH_SHORT).show()
            }else{
                mediaPlayer= MediaPlayer().apply {
                    setAudioStreamType(AudioManager.STREAM_MUSIC)
                    setDataSource(context, path)
                    prepare()
                    start()
                }
            }

        }
        fun checkSend(context: Context,title: String):Boolean{
            var metaFile=("meta"+title.substring(9,title.length-4))
            var send:String
            send=context.openFileInput(metaFile).readBytes().toString(Charset.forName("UTF-8"))
            Log.d("SendingMethod",send.split(";")[2].split(":")[1])
            if(send.split(";")[2].split(":")[1].equals("false")) return false
            else return true

        }
    }

    private var recorderDirectory = File(Environment.getExternalStorageDirectory().absolutePath + "/soundrecorder/recordings/")
    private var file : ArrayList<String>? = null

    init {
        file = ArrayList<String>()
        getRecordings()
    }

     fun getRecordings():ArrayList<String>?{
        file?.clear()
        recorderDirectory = File(Environment.getExternalStorageDirectory().absolutePath + "/soundrecorder/recordings/")
        val files: Array<out File>? = recorderDirectory.listFiles()
        for(i in files!!){
            println(i.name)
            file?.add(i.name)
        }
        return file
    }



}