package com.tawny.android

import android.os.Bundle
import android.os.Environment
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.TextView
import java.io.File
import kotlin.String as String1

class MainActivity : AppCompatActivity(), View.OnClickListener {


    override fun onClick(v: View) {
        when (v.id) {
            R.id.textView -> {

            }

        }
    }

    private var textView: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textView = findViewById(R.id.textView)

//        textView!!.text = ""
        textView!!.setOnClickListener(this)

//        textView!!.setOnClickListener { textView!!.text = "" }


    }
    fun test(): kotlin.String {
        return "test"
    }

    fun test(string: kotlin.String): kotlin.String {
        return string
    }

    fun paths(): kotlin.String {

        return ""
    }


    private val path: kotlin.String
        get() {
            val path = Environment.getExternalStorageDirectory().toString() + "/Luban/image/"
            val file = File(path)
            return if (file.mkdirs()) {
                path
            } else path
        }

}


