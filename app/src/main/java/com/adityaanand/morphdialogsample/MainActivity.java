package com.adityaanand.morphdialogsample;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.util.Random;

import com.adityaanand.morphdialog.MorphDialog;
import com.adityaanand.morphdialog.interfaces.MorphSingleButtonCallback;
import com.adityaanand.morphdialog.utils.MorphDialogAction;
import com.adityaanand.morphdialogsample.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding ui;
    MorphDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ui = DataBindingUtil.setContentView(this, R.layout.activity_main);
    }

    public void morph(View view) {
        dialog = new MorphDialog.Builder(this, (FloatingActionButton) view)
                .title("Title")
                .content("This is a sentence. Here is another one.")
                .positiveText("Ok")
                .negativeText("Cancel")
                .neutralText("More")
                .onPositive(new MorphSingleButtonCallback() {
                    @Override
                    public void onClick(@NotNull MorphDialog dialog1, @NotNull MorphDialogAction which) {
                        Toast.makeText(MainActivity.this, "onPositive", Toast.LENGTH_SHORT).show();
                    }
                })
                .onNegative(new MorphSingleButtonCallback() {
                    @Override
                    public void onClick(@NotNull MorphDialog dialog1, @NotNull MorphDialogAction which) {
                        Toast.makeText(MainActivity.this, "onNegative", Toast.LENGTH_SHORT).show();
                    }
                })
                .onNeutral(new MorphSingleButtonCallback() {
                    @Override
                    public void onClick(@NotNull MorphDialog dialog1, @NotNull MorphDialogAction which) {
                        Toast.makeText(MainActivity.this, "onNeutral", Toast.LENGTH_SHORT).show();
                    }
                })
                .cancelable(false)
                .contentColor(Color.BLUE)
                .backgroundColor(Color.GREEN)
                .neutralColorRes(R.color.primary)
                .positiveColor(Color.BLACK)
                .titleColor(Color.YELLOW)
                .useDarkTheme(new Random().nextBoolean())
                .build();
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        MorphDialog.Companion.registerOnActivityResult(requestCode, resultCode, data)
                .forDialogs(dialog);
    }
}
