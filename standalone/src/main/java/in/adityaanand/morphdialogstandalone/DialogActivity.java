package in.adityaanand.morphdialogstandalone;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.transition.ArcMotion;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;

import com.afollestad.materialdialogs.MaterialDialog;

import in.adityaanand.morphdialogstandalone.databinding.ActivityDialogBinding;
import in.adityaanand.morphdialogstandalone.util.MorphDialogToFab;
import in.adityaanand.morphdialogstandalone.util.MorphFabToDialog;


public class DialogActivity extends AppCompatActivity {

    ActivityDialogBinding ui;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set theme to dark if you want
        //setTheme(shouldUseDarkTheme() ? R.style.MorphDialog_Dark : R.style.MorphDialog_Light);
        ui = DataBindingUtil.setContentView(this, R.layout.activity_dialog);
        Bundle params = getIntent().getExtras();        //get the text and add to MaterialDialog if !null
        CharSequence content = params.getCharSequence("content");
        String title = params.getString("title"),
                positive = params.getString("positive"),
                negative = params.getString("negative");
        MaterialDialog.Builder builder = new MaterialDialog.Builder(this)
                .content(content)
                .title(title)
                .positiveText(positive)
                .onPositive((dialog, which) -> {
                    setResult(Activity.RESULT_OK);//useful if you care about startActivityForResult()'s result
                    closeDialog();
                })
                .negativeText(negative)
                .onNegative((dialog, which) -> onBackPressed());

        MaterialDialog dialog = builder.build();
        ((ViewGroup) dialog.getView().getParent()).removeView(dialog.getView()); //remove old parent
        ui.container.addView(dialog.getView()); //add new parent

        ui.root.setOnClickListener(v -> closeDialog()); //closes dialog if you click outside of it

        ui.container.setOnClickListener(v -> { //leaving empty so that nothing happens when you click on non-clickable bits of the dialog
        });

        setupSharedEelementTransitions();
    }

    public void setupSharedEelementTransitions() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
            return; //Show dialog normally if below Lollipop
        ArcMotion arcMotion = new ArcMotion();
        arcMotion.setMinimumHorizontalAngle(50f);
        arcMotion.setMinimumVerticalAngle(50f);

        Interpolator easeInOut = AnimationUtils.loadInterpolator(this, android.R.interpolator.fast_out_slow_in);

        MorphFabToDialog sharedEnter = new MorphFabToDialog(getBackgroundColor());
        sharedEnter.setPathMotion(arcMotion);
        sharedEnter.setInterpolator(easeInOut);

        MorphDialogToFab sharedReturn = new MorphDialogToFab(getBackgroundColor());
        sharedReturn.setPathMotion(arcMotion);
        sharedReturn.setInterpolator(easeInOut);

        if (ui.container != null) {
            sharedEnter.addTarget(ui.container);
            sharedReturn.addTarget(ui.container);
        }
        getWindow().setSharedElementEnterTransition(sharedEnter);
        getWindow().setSharedElementReturnTransition(sharedReturn);
    }

    int getBackgroundColor() {
        Drawable background = ui.container.getBackground();
        return ((ColorDrawable) background).getColor();
    }

    @Override
    public void onBackPressed() {
        setResult(Activity.RESULT_CANCELED);
        closeDialog();
    }

    public void closeDialog() {
        int height = ui.container.getHeight();
        ui.container.setMinimumHeight(height); //need to remember the height for smooth animation
        ui.container.removeAllViews();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            finishAfterTransition();
        else finish();
    }


    private static Intent getIntent(Context context, String title, CharSequence content,
                                    String positive, String negative) {
        Intent intent = new Intent(context, DialogActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("content", content.toString());
        intent.putExtra("positive", positive);
        intent.putExtra("negative", negative);
        return intent;
    }

    public static void open(AppCompatActivity context, View fab, String title, CharSequence content) {
        Intent intent = getIntent(context, title, content, null, "OK");

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(context, fab, "morph_transition");
            context.startActivityForResult(intent, 1, options.toBundle());
        } else
            context.startActivityForResult(intent, 1);
    }
}
