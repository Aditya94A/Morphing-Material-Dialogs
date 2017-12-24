package in.adityaanand.morphdialog;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.FloatingActionButton;

import com.afollestad.materialdialogs.util.DialogUtils;

import java.util.Random;

import hugo.weaving.DebugLog;
import in.adityaanand.morphdialog.interfaces.MorphSingleButtonCallback;
import in.adityaanand.morphdialog.utils.MorphDialogAction;

/**
 * @author Aditya Anand (AdityaAnand1)
 */
@DebugLog
public class MorphDialog {

    private static final int REQUEST_CODE = 7324;

    //todo How do we let other devs know that this ^ is mine to avoid conflict?
    private final long id;
    private Builder builder;

    private MorphDialog(Builder builder) {
        this.builder = builder;
        //generate a unique ID for each dialog
        long temp;
        do {
            temp = new Random().nextLong();
        } while (temp == 0);
        id = temp;
    }

    public void setBuilder(Builder builder) {
        this.builder = builder;
    }

    public Builder getBuilder() {
        return builder;
    }

    private void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (builder == null || requestCode != REQUEST_CODE || data == null || resultCode != Activity.RESULT_OK) //this is not ours
            return;
        long paramId = data.getLongExtra(Constants.MORPH_DIALOG_ID, 0);
        if (this.id != paramId || paramId == 0)
            return; //this is some other dialogs call back

        MorphDialogAction tag = (MorphDialogAction) data.getSerializableExtra(Constants.MORPH_DIALOG_ACTION_TYPE);

        switch (tag) {
            case POSITIVE:
                if (builder.onPositiveCallback != null) {
                    builder.onPositiveCallback.onClick(this, tag);
                }

                break;
            case NEGATIVE:
                if (builder.onNegativeCallback != null) {
                    builder.onNegativeCallback.onClick(this, tag);
                }
                break;
            case NEUTRAL:
                if (builder.onNeutralCallback != null) {
                    builder.onNeutralCallback.onClick(this, tag);
                }
                break;
        }
        if (builder.onAnyCallback != null) {
            builder.onAnyCallback.onClick(this, tag);
        }
    }

    public MorphDialog show() {
        Intent intent = new Intent(builder.activity, builder.data.getDarkTheme() ?
                MorphDialogActivityDark.class : MorphDialogActivity.class);
        intent.putExtra(Constants.MORPH_DIALOG_BUILDER_DATA, builder.data);
        intent.putExtra(Constants.MORPH_DIALOG_ID, id);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(
                    builder.activity, builder.fab, "morph_transition");
            builder.activity.startActivityForResult(intent, MorphDialog.REQUEST_CODE, options.toBundle());
        } else
            builder.activity.startActivityForResult(intent, MorphDialog.REQUEST_CODE);
        return this;
    }

    public static Registerer registerOnActivityResult(int requestCode, int resultCode, Intent data) {
        return new Registerer(requestCode, resultCode, data);
    }

    public static class Registerer {
        int requestCode;
        int resultCode;
        Intent data;

        public Registerer(int requestCode, int resultCode, Intent data) {
            this.requestCode = requestCode;
            this.resultCode = resultCode;
            this.data = data;
        }

        public void forDialogs(MorphDialog... dialogs) {
            if (requestCode == MorphDialog.REQUEST_CODE)
                for (MorphDialog dialog : dialogs)
                    if (dialog != null)
                        dialog.onActivityResult(requestCode, resultCode, data);
        }
    }

    public static class Builder {
        private final Activity activity;
        private final FloatingActionButton fab;
        private final DialogBuilderData data;

        protected MorphSingleButtonCallback onPositiveCallback;
        protected MorphSingleButtonCallback onNegativeCallback;
        protected MorphSingleButtonCallback onNeutralCallback;
        protected MorphSingleButtonCallback onAnyCallback;

        public Builder(Activity activity, FloatingActionButton fab) {
            this.activity = activity;
            this.fab = fab;
            data = new DialogBuilderData();
        }

        public Builder title(@StringRes int titleRes) {
            title(activity.getText(titleRes));
            return this;
        }

        public Builder title(@NonNull CharSequence title) {
            data.setTitle(title);
            return this;
        }

        public Builder content(@StringRes int contentRes) {
            content(activity.getText(contentRes));
            return this;
        }

        public Builder content(@NonNull CharSequence content) {
            data.setContent(content);
            return this;
        }

        public Builder positiveText(@StringRes int positiveTextRes) {
            positiveText(activity.getText(positiveTextRes));
            return this;
        }

        public Builder positiveText(@NonNull CharSequence positiveText) {
            data.setPositiveText(positiveText);
            return this;
        }

        public Builder negativeText(@StringRes int negativeTextRes) {
            negativeText(activity.getText(negativeTextRes));
            return this;
        }

        public Builder negativeText(@NonNull CharSequence negativeText) {
            data.setNegativeText(negativeText);
            return this;
        }

        public Builder neutralText(@StringRes int neutralRes) {
            if (neutralRes == 0) {
                return this;
            }
            return neutralText(this.activity.getText(neutralRes));
        }


        public Builder positiveColor(@ColorInt int color) {
            return positiveColor(DialogUtils.getActionTextStateList(activity, color));
        }

        public Builder positiveColorRes(@ColorRes int colorRes) {
            return positiveColor(DialogUtils.getActionTextColorStateList(this.activity, colorRes));
        }

        public Builder positiveColorAttr(@AttrRes int colorAttr) {
            return positiveColor(
                    DialogUtils.resolveActionTextColorStateList(this.activity, colorAttr, null));
        }

        public Builder positiveColor(@NonNull ColorStateList colorStateList) {
            this.data.setPositiveColor(colorStateList);
            return this;
        }

        public Builder negativeColor(@ColorInt int color) {
            return negativeColor(DialogUtils.getActionTextStateList(activity, color));
        }

        public Builder negativeColorRes(@ColorRes int colorRes) {
            return negativeColor(DialogUtils.getActionTextColorStateList(this.activity, colorRes));
        }

        public Builder negativeColorAttr(@AttrRes int colorAttr) {
            return negativeColor(
                    DialogUtils.resolveActionTextColorStateList(this.activity, colorAttr, null));
        }

        public Builder negativeColor(@NonNull ColorStateList colorStateList) {
            this.data.setNegativeColor(colorStateList);
            return this;
        }

        public Builder neutralColor(@ColorInt int color) {
            return neutralColor(DialogUtils.getActionTextStateList(activity, color));
        }

        public Builder neutralColorRes(@ColorRes int colorRes) {
            return neutralColor(DialogUtils.getActionTextColorStateList(this.activity, colorRes));
        }

        public Builder neutralColorAttr(@AttrRes int colorAttr) {
            return neutralColor(
                    DialogUtils.resolveActionTextColorStateList(this.activity, colorAttr, null));
        }

        public Builder neutralColor(@NonNull ColorStateList colorStateList) {
            this.data.setNeutralColor(colorStateList);
            return this;
        }

        public Builder neutralText(@NonNull CharSequence message) {
            this.data.setNeutralText(message);
            return this;
        }

        public Builder useDarkTheme(@NonNull boolean darkTheme) {
            data.setDarkTheme(darkTheme);
            return this;
        }

        public Builder onPositive(MorphSingleButtonCallback callback) {
            this.onPositiveCallback = callback;
            return this;
        }

        public Builder onNegative(MorphSingleButtonCallback callback) {
            this.onNegativeCallback = callback;
            return this;
        }

        public Builder onNeutral(MorphSingleButtonCallback callback) {
            this.onNeutralCallback = callback;
            return this;
        }

        public Builder onAny(MorphSingleButtonCallback callback) {
            this.onAnyCallback = callback;
            return this;
        }

        public Builder cancelable(boolean cancelable) {
            this.data.setCancelable(cancelable);
            this.data.setCanceledOnTouchOutside(cancelable);
            return this;
        }

        public Builder canceledOnTouchOutside(boolean canceledOnTouchOutside) {
            this.data.setCanceledOnTouchOutside(canceledOnTouchOutside);
            return this;
        }

        public Builder backgroundColor(@ColorInt int color) {
            this.data.setBackgroundColor(color);
            return this;
        }

        public Builder backgroundColorRes(@ColorRes int colorRes) {
            return backgroundColor(DialogUtils.getColor(this.activity, colorRes));
        }

        public Builder backgroundColorAttr(@AttrRes int colorAttr) {
            return backgroundColor(DialogUtils.resolveColor(this.activity, colorAttr));
        }

        public Builder titleColor(@ColorInt int color) {
            this.data.setTitleColor(color);
            return this;
        }

        public Builder titleColorRes(@ColorRes int colorRes) {
            return titleColor(DialogUtils.getColor(this.activity, colorRes));
        }

        public Builder titleColorAttr(@AttrRes int colorAttr) {
            return titleColor(DialogUtils.resolveColor(this.activity, colorAttr));
        }

        public Builder contentColor(@ColorInt int color) {
            this.data.setContentColor(color);
            return this;
        }

        public Builder contentColorRes(@ColorRes int colorRes) {
            contentColor(DialogUtils.getColor(this.activity, colorRes));
            return this;
        }

        public Builder contentColorAttr(@AttrRes int colorAttr) {
            contentColor(DialogUtils.resolveColor(this.activity, colorAttr));
            return this;
        }

        public MorphDialog build() {
            return new MorphDialog(this);
        }

        public MorphDialog show() {
            return new MorphDialog(this).show();
        }
    }
}