package com.example.company.maina

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_auth.*

class AuthActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        but_submit.setOnClickListener {
            val intent=Intent()
            intent.putExtra("name","${edit_lastname.text} ${edit_name.text} ${edit_secname.text}")
            setResult(1,intent)
            finish()
        }
    }


}
