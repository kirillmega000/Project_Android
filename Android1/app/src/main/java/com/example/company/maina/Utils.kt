package com.example.company.maina



import io.reactivex.Observable
import java.io.*
import java.net.HttpURLConnection
import java.net.URL

import java.lang.Exception
import java.lang.RuntimeException


fun createRequest(url: String,filename:String,second:String) = Observable.create<String> {
    val lineEnd = "\r\n"
    val twoHyphens = "--"
    val boundary = "*****"
    var bytesRead: Int
    var bytesAvailable: Int
    var bufferSize: Int
    var buffer: ByteArray
    val maxBufferSize = 1 * 1024 * 1024

    var FORM_FILE_NAME="file1"
    val urlConnection = URL(url).openConnection() as HttpURLConnection
    try {


        urlConnection.useCaches=false
        urlConnection.doInput=true
        urlConnection.doOutput=true
        urlConnection.requestMethod="POST"
        urlConnection.setRequestProperty("Connection", "Keep-Alive")
        urlConnection.setRequestProperty("ENCTYPE", "multipart/form-data")
        urlConnection.setRequestProperty("Content-Type", "multipart/form-data;boundary="+boundary)
        urlConnection.setRequestProperty("uploaded_file", filename)
        val outputStream = DataOutputStream(urlConnection.getOutputStream())

        outputStream.writeBytes(twoHyphens + boundary + lineEnd)
        outputStream.writeBytes("Content-Disposition: form-data; name=\"description\""+lineEnd)
        outputStream.writeBytes("Content-Type: text/plain; charset=utf-8\r\n\r\n")
        var b=second.toByteArray(Charsets.UTF_8)
        outputStream.write(b)
        outputStream.writeBytes(lineEnd)


        outputStream.writeBytes(twoHyphens + boundary + lineEnd)
        outputStream.writeBytes("Content-Disposition: form-data; name=\"" +
                FORM_FILE_NAME + "\"; filename=\"" + filename +  "\"\r\n\r\n")

         outputStream.write(org.apache.commons.io.FileUtils.readFileToByteArray(File(filename)))



        outputStream.writeBytes(lineEnd)
        outputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd)
        val serverResponseCode = urlConnection.responseCode

      var resp=urlConnection.responseCode
        outputStream.flush()
        outputStream.close()
        urlConnection.connect()
        if (resp==200){
            it.onNext("q")
        }
        else {
            it.onError(RuntimeException("Problem caused"))
        }

    }catch (e:Exception){
        e.printStackTrace()
    }
    finally {
        urlConnection.disconnect()
    }

}