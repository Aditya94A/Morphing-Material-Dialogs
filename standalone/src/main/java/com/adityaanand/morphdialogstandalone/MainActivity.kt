package com.adityaanand.morphdialogstandalone

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.adityaanand.morphdialogstandalone.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var ui: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ui = DataBindingUtil.setContentView(this, R.layout.activity_main)
    }

    fun morph(view: View) {
        DialogActivity.open(this, view, "Title", "This is a sentence. Here is another one.")
    }
}
