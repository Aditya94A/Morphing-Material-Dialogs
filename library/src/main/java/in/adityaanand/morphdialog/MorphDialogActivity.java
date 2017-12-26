package in.adityaanand.morphdialog;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.transition.ArcMotion;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;

import com.afollestad.materialdialogs.MaterialDialog;

import java.io.Serializable;

import hugo.weaving.DebugLog;
import in.adityaanand.morphdialog.databinding.ActivityDialogBinding;
import in.adityaanand.morphdialog.morphutil.MorphDialogToFab;
import in.adityaanand.morphdialog.morphutil.MorphFabToDialog;
import in.adityaanand.morphdialog.utils.MorphDialogAction;

// TODO: 22-Dec-17 I'm not sure if we need AppCompatActivity here...
@DebugLog
public class MorphDialogActivity extends Activity {

    ActivityDialogBinding ui;
    long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ui = DataBindingUtil.setContentView(this, R.layout.activity_dialog);
        Bundle params = getIntent().getExtras();
        id = params.getLong(Constants.MORPH_DIALOG_ID);
        DialogBuilderData data = params.getParcelable(Constants.MORPH_DIALOG_BUILDER_DATA);
        MaterialDialog.Builder builder = new MaterialDialog.Builder(this)
                .content(data.getContent())
                .title(data.getTitle())
                .positiveText(data.getPositiveText())
                .onPositive((dialog, which) -> onActionButtonClicked(MorphDialogAction.POSITIVE))
                .negativeText(data.getNegativeText())
                .onNegative((dialog, which) -> onActionButtonClicked(MorphDialogAction.NEGATIVE))
                .neutralText(data.getNeutralText())
                .onNeutral((dialog, which) -> onActionButtonClicked(MorphDialogAction.NEUTRAL))
                .canceledOnTouchOutside(data.getCanceledOnTouchOutside())
                .cancelable(data.getCancelable());
        if (data.getNeutralColor() != null)
            builder.neutralColor(data.getNeutralColor());
        if (data.getNegativeColor() != null)
            builder.negativeColor(data.getNegativeColor());
        if (data.getPositiveColor() != null)
            builder.positiveColor(data.getPositiveColor());

        if (data.getBackgroundColor() != 0) {
            //   builder.backgroundColor(data.getBackgroundColor()); //doesn't work for some reason
            ui.container.setBackgroundColor(data.getBackgroundColor());
        }
        if (data.getTitleColor() != -1)
            builder.titleColor(data.getTitleColor());
        if (data.getContentColor() != -1) {
            builder.backgroundColor(data.getContentColor());
        }

        MaterialDialog dialog = builder.build();
        ((ViewGroup) dialog.getView().getParent()).removeView(dialog.getView()); //remove old parent
        ui.container.addView(dialog.getView()); //add new parent
        ui.root.setOnClickListener(v -> {
            if (data.getCanceledOnTouchOutside()) onBackPressed();
        }); //closes dialog if you click outside of it
        ui.container.setOnClickListener(v -> {
        });  //leaving empty so that nothing happens when you click on non-clickable bits of the dialog

        setupTransition();
    }

    void onActionButtonClicked(Serializable actionType) {
        Intent returnData = new Intent();
        returnData.putExtra(Constants.MORPH_DIALOG_ACTION_TYPE, actionType);
        closeDialog(returnData);
    }

    public void setupTransition() {
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

        sharedEnter.addTarget(ui.container);
        sharedReturn.addTarget(ui.container);
        getWindow().setSharedElementEnterTransition(sharedEnter);
        getWindow().setSharedElementReturnTransition(sharedReturn);
    }

    int getBackgroundColor() {
        Drawable background = ui.container.getBackground();
        return ((ColorDrawable) background).getColor();
    }

    @Override
    public void onBackPressed() {
    //    Intent returnData = new Intent();
     //   returnData.putExtra("wasDismissed", true); // TODO: 23-Dec-17
    //    setResult(Activity.RESULT_OK, returnData);
        closeDialog();
    }

    public void closeDialog(){
        closeDialog(new Intent());
    }

    public void closeDialog(Intent returnData) {
        returnData.putExtra(Constants.MORPH_DIALOG_ID, id);
        setResult(Activity.RESULT_OK, returnData);
        int height = ui.container.getHeight();
        ui.container.setMinimumHeight(height); //need to remember the height for smooth animation
        ui.container.removeAllViews();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            finishAfterTransition();
        else finish();
    }
}
