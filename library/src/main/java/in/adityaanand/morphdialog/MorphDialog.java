package in.adityaanand.morphdialog;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.FloatingActionButton;

import in.adityaanand.morphdialog.util.Utils;

public class MorphDialog {

    /* // TODO: 14-Aug-17 not sure if we'll need this?
    private CharSequence title, content, positiveText, negativeText;

    private MorphDialog(Builder builder) {
        this.title = builder.title;
        this.content = builder.content;
        this.positiveText = builder.positiveText;
        this.negativeText = builder.negativeText;
    }
*/

    public static class Builder {
        private final Activity activity;
        private CharSequence title, content, positiveText, negativeText;
        private final FloatingActionButton fab;
        private boolean darkTheme;


        public Builder(Activity activity, FloatingActionButton fab) {
            this.activity = activity;
            this.fab = fab;
        }

        public Builder title(@StringRes int titleRes) {
            title(this.activity.getText(titleRes));
            return this;
        }

        public Builder title(@NonNull CharSequence title) {
            this.title = title;
            return this;
        }

        public Builder content(@StringRes int contentRes) {
            content(this.activity.getText(contentRes));
            return this;
        }

        public Builder content(@NonNull CharSequence content) {
            this.content = content;
            return this;
        }

        public Builder positiveText(@StringRes int positiveTextRes) {
            positiveText(this.activity.getText(positiveTextRes));
            return this;
        }

        public Builder positiveText(@NonNull CharSequence positiveText) {
            this.positiveText = positiveText;
            return this;
        }

        public Builder negativeText(@StringRes int negativeTextRes) {
            negativeText(this.activity.getText(negativeTextRes));
            return this;
        }

        public Builder negativeText(@NonNull CharSequence negativeText) {
            this.negativeText = negativeText;
            return this;
        }

        public Builder useDarkTheme(@NonNull boolean darkTheme) {
            this.darkTheme = darkTheme;
            return this;
        }

        public void show() {
            Utils.open(activity, fab, darkTheme, title, content, positiveText, negativeText);
        }
    }
}