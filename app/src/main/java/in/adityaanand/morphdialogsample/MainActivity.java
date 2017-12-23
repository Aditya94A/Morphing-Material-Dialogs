package in.adityaanand.morphdialogsample;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import java.util.Random;

import in.adityaanand.morphdialog.MorphDialog;
import in.adityaanand.morphdialog.utils.MorphDialogAction;
import in.adityaanand.morphdialogsample.databinding.ActivityMainBinding;

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
                .onPositive((MorphDialog dialog1, MorphDialogAction which) -> {
                    Toast.makeText(this, "onPositive", Toast.LENGTH_SHORT).show();
                })
                .onNegative((MorphDialog dialog1, MorphDialogAction which) -> {
                    Toast.makeText(this, "onNegative", Toast.LENGTH_SHORT).show();
                })
                .onNeutral((MorphDialog dialog1, MorphDialogAction which) -> {
                    Toast.makeText(this, "onNeutral", Toast.LENGTH_SHORT).show();
                })
                .cancelable(false)
                .contentColor(Color.BLUE)
                .backgroundColor(Color.GREEN)
                .neutralColor(Color.BLUE)
                .positiveColor(Color.BLACK)
                .titleColor(Color.YELLOW)
                .useDarkTheme(new Random().nextBoolean())
                .build();
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        dialog.onActivityResult(requestCode, resultCode, data);
    }
}
