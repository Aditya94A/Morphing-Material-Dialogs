package in.adityaanand.morphdialog.util;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.View;

import in.adityaanand.morphdialog.MorphDialog;
import in.adityaanand.morphdialog.MorphDialogActivity;
import in.adityaanand.morphdialog.MorphDialogActivityDark;

public class Utils {

    public static Intent getIntent(Context context, boolean useDarkTheme, CharSequence title, CharSequence content, CharSequence positive, CharSequence negative) {

        Intent intent = new Intent(context, useDarkTheme ? MorphDialogActivityDark.class : MorphDialogActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("content", content);
        intent.putExtra("positive", positive);
        intent.putExtra("negative", negative);
        return intent;
    }

    public static void open(Activity activity, View fab, boolean useDarkTheme, CharSequence title, CharSequence content,
                            CharSequence positive, CharSequence negative) {
        Intent intent = getIntent(activity, useDarkTheme, title, content, positive, negative);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(activity, fab, "morph_transition");
            activity.startActivityForResult(intent, MorphDialog.REQUEST_CODE, options.toBundle());
        } else
            activity.startActivityForResult(intent, MorphDialog.REQUEST_CODE);
    }
}
