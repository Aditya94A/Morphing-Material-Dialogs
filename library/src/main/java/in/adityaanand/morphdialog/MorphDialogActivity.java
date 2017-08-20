package in.adityaanand.morphdialog;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.transition.ArcMotion;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;

import com.afollestad.materialdialogs.MaterialDialog;

import in.adityaanand.morphdialog.databinding.ActivityDialogBinding;
import in.adityaanand.morphdialog.morphutil.MorphDialogToFab;
import in.adityaanand.morphdialog.morphutil.MorphFabToDialog;


public class MorphDialogActivity extends AppCompatActivity {

    ActivityDialogBinding ui;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ui = DataBindingUtil.setContentView(this, R.layout.activity_dialog);
        Bundle params = getIntent().getExtras();        //get the text and add to MaterialDialog if !null
        CharSequence content = params.getCharSequence("content"),
                title = params.getString("title"),
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

        new MaterialDialog.Builder(this);
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
}
