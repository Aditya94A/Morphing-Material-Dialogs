package com.aditya.morph;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.aditya.morph.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding ui;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ui = DataBindingUtil.setContentView(this, R.layout.activity_main);
    }

    public void morph(View view) {
        DialogActivity.open(this, view, "Title", "This is a sentence. Here is another one.");
    }
}
