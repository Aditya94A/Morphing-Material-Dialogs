package in.adityaanand.morphdialogsample;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.aditya.morphdialogstandalone.R;
import com.aditya.morphdialogstandalone.databinding.ActivityMainBinding;

import java.util.Random;

import in.adityaanand.morphdialog.MorphDialog;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding ui;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ui = DataBindingUtil.setContentView(this, R.layout.activity_main);
    }

    public void morph(View view) {
        new MorphDialog.Builder(this, (FloatingActionButton) view)
                .title("Title")
                .content("This is a sentence. Here is another one.")
                .useDarkTheme(new Random().nextBoolean())
                .show();
    }
}
