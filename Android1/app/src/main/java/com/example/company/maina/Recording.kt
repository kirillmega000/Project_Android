package com.example.company.maina

import android.content.ClipData
import android.content.Context
import android.support.v7.widget.RecyclerView

import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.recording_layout.view.*

class Recording(val title: String, val context: Context):Item(){

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.recording_title_textview.text = title
        viewHolder.itemView.recording_image.setOnClickListener {
            try {
                RecordingRepository.playRecording(context, title)
            }catch (e:Exception){
                e.printStackTrace()
            }
        }
        val b:Boolean=RecordingRepository.checkSend(context,title)
        if(b==true) {
            viewHolder.itemView.send_image.setImageResource(R.drawable.ic_done_black_24dp)}
    }

    override fun getLayout(): Int {
        return R.layout.recording_layout
    }

}