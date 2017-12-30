package com.adityaanand.morphdialog

import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Build
import android.support.annotation.AttrRes
import android.support.annotation.ColorInt
import android.support.annotation.ColorRes
import android.support.annotation.StringRes
import android.support.design.widget.FloatingActionButton
import com.adityaanand.morphdialog.interfaces.MorphSingleButtonCallback
import com.adityaanand.morphdialog.utils.MorphDialogAction
import com.afollestad.materialdialogs.util.DialogUtils
import hugo.weaving.DebugLog
import java.util.*

/**
 * @author Aditya Anand (AdityaAnand1)
 */
@DebugLog
class MorphDialog private constructor(var builder: Builder) {

    //todo How do we let other devs know that this ^ is mine to avoid conflict?
    private val id: Long

    init {
        //generate a unique ID for each dialog
        var temp: Long
        do {
            temp = Random().nextLong()
        } while (temp == 0L)
        id = temp
    }

    private fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode != REQUEST_CODE || data == null || resultCode != Activity.RESULT_OK)
        //this is not ours
            return
        val paramId = data.getLongExtra(Constants.MORPH_DIALOG_ID, 0)
        if (this.id != paramId || paramId == 0L)
            return  //this is some other dialogs call back

        val actionType = data.getSerializableExtra(Constants.MORPH_DIALOG_ACTION_TYPE) as MorphDialogAction? ?: return

        when (actionType) {
            MorphDialogAction.POSITIVE -> if (builder.onPositiveCallback != null)
                builder.onPositiveCallback!!.onClick(this, actionType)

            MorphDialogAction.NEGATIVE -> if (builder.onNegativeCallback != null)
                builder.onNegativeCallback!!.onClick(this, actionType)

            MorphDialogAction.NEUTRAL -> if (builder.onNeutralCallback != null)
                builder.onNeutralCallback!!.onClick(this, actionType)

        }
        if (builder.onAnyCallback != null) {
            builder.onAnyCallback!!.onClick(this, actionType)
        }
    }

    fun show(): MorphDialog {
        val intent = Intent(builder.activity, if (builder.data.darkTheme)
            MorphDialogActivityDark::class.java
        else
            MorphDialogActivity::class.java)
        intent.putExtra(Constants.MORPH_DIALOG_BUILDER_DATA, builder.data)
        intent.putExtra(Constants.MORPH_DIALOG_ID, id)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val options = ActivityOptions.makeSceneTransitionAnimation(
                    builder.activity, builder.fab, "morph_transition")
            builder.activity.startActivityForResult(intent, REQUEST_CODE, options.toBundle())
        } else
            builder.activity.startActivityForResult(intent, REQUEST_CODE)
        return this
    }

    class Registerer(val requestCode: Int,
                     val resultCode: Int,
                     val data: Intent?) {

        fun forDialogs(vararg dialogs: MorphDialog?) {
            if (requestCode == REQUEST_CODE)
                for (dialog in dialogs)
                    dialog?.onActivityResult(requestCode, resultCode, data)
        }
    }

    class Builder(val activity: Activity, val fab: FloatingActionButton) {
        val data: DialogBuilderData = DialogBuilderData()

        var onPositiveCallback: MorphSingleButtonCallback? = null
        var onNegativeCallback: MorphSingleButtonCallback? = null
        var onNeutralCallback: MorphSingleButtonCallback? = null
        var onAnyCallback: MorphSingleButtonCallback? = null

        fun title(@StringRes titleRes: Int): Builder {
            title(activity.getText(titleRes))
            return this
        }

        fun title(title: CharSequence): Builder {
            data.title = title
            return this
        }

        fun content(@StringRes contentRes: Int): Builder {
            content(activity.getText(contentRes))
            return this
        }

        fun content(content: CharSequence): Builder {
            data.content = content
            return this
        }

        fun positiveText(@StringRes positiveTextRes: Int): Builder {
            positiveText(activity.getText(positiveTextRes))
            return this
        }

        fun positiveText(positiveText: CharSequence): Builder {
            data.positiveText = positiveText
            return this
        }

        fun negativeText(@StringRes negativeTextRes: Int): Builder {
            negativeText(activity.getText(negativeTextRes))
            return this
        }

        fun negativeText(negativeText: CharSequence): Builder {
            data.negativeText = negativeText
            return this
        }

        fun neutralText(@StringRes neutralRes: Int): Builder {
            return if (neutralRes == 0) {
                this
            } else neutralText(this.activity.getText(neutralRes))
        }


        fun positiveColor(@ColorInt color: Int): Builder {
            return positiveColor(DialogUtils.getActionTextStateList(activity, color))
        }

        fun positiveColorRes(@ColorRes colorRes: Int): Builder {
            return positiveColor(DialogUtils.getActionTextColorStateList(this.activity, colorRes))
        }

        fun positiveColorAttr(@AttrRes colorAttr: Int): Builder {
            return positiveColor(
                    DialogUtils.resolveActionTextColorStateList(this.activity, colorAttr, null))
        }

        fun positiveColor(colorStateList: ColorStateList): Builder {
            this.data.positiveColor = colorStateList
            return this
        }

        fun negativeColor(@ColorInt color: Int): Builder {
            return negativeColor(DialogUtils.getActionTextStateList(activity, color))
        }

        fun negativeColorRes(@ColorRes colorRes: Int): Builder {
            return negativeColor(DialogUtils.getActionTextColorStateList(this.activity, colorRes))
        }

        fun negativeColorAttr(@AttrRes colorAttr: Int): Builder {
            return negativeColor(
                    DialogUtils.resolveActionTextColorStateList(this.activity, colorAttr, null))
        }

        fun negativeColor(colorStateList: ColorStateList): Builder {
            this.data.negativeColor = colorStateList
            return this
        }

        fun neutralColor(@ColorInt color: Int): Builder {
            return neutralColor(DialogUtils.getActionTextStateList(activity, color))
        }

        fun neutralColorRes(@ColorRes colorRes: Int): Builder {
            return neutralColor(DialogUtils.getActionTextColorStateList(this.activity, colorRes))
        }

        fun neutralColorAttr(@AttrRes colorAttr: Int): Builder {
            return neutralColor(
                    DialogUtils.resolveActionTextColorStateList(this.activity, colorAttr, null))
        }

        fun neutralColor(colorStateList: ColorStateList): Builder {
            this.data.neutralColor = colorStateList
            return this
        }

        fun neutralText(message: CharSequence): Builder {
            this.data.neutralText = message
            return this
        }

        fun useDarkTheme(darkTheme: Boolean): Builder {
            data.darkTheme = darkTheme
            return this
        }

        fun onPositive(callback: MorphSingleButtonCallback): Builder {
            this.onPositiveCallback = callback
            return this
        }

        fun onNegative(callback: MorphSingleButtonCallback): Builder {
            this.onNegativeCallback = callback
            return this
        }

        fun onNeutral(callback: MorphSingleButtonCallback): Builder {
            this.onNeutralCallback = callback
            return this
        }

        fun onAny(callback: MorphSingleButtonCallback): Builder {
            this.onAnyCallback = callback
            return this
        }

        fun cancelable(cancelable: Boolean): Builder {
            this.data.cancelable = cancelable
            this.data.canceledOnTouchOutside = cancelable
            return this
        }

        fun canceledOnTouchOutside(canceledOnTouchOutside: Boolean): Builder {
            this.data.canceledOnTouchOutside = canceledOnTouchOutside
            return this
        }

        fun backgroundColor(@ColorInt color: Int): Builder {
            this.data.backgroundColor = color
            return this
        }

        fun backgroundColorRes(@ColorRes colorRes: Int): Builder {
            return backgroundColor(DialogUtils.getColor(this.activity, colorRes))
        }

        fun backgroundColorAttr(@AttrRes colorAttr: Int): Builder {
            return backgroundColor(DialogUtils.resolveColor(this.activity, colorAttr))
        }

        fun titleColor(@ColorInt color: Int): Builder {
            this.data.titleColor = color
            return this
        }

        fun titleColorRes(@ColorRes colorRes: Int): Builder {
            return titleColor(DialogUtils.getColor(this.activity, colorRes))
        }

        fun titleColorAttr(@AttrRes colorAttr: Int): Builder {
            return titleColor(DialogUtils.resolveColor(this.activity, colorAttr))
        }

        fun contentColor(@ColorInt color: Int): Builder {
            this.data.contentColor = color
            return this
        }

        fun contentColorRes(@ColorRes colorRes: Int): Builder {
            contentColor(DialogUtils.getColor(this.activity, colorRes))
            return this
        }

        fun contentColorAttr(@AttrRes colorAttr: Int): Builder {
            contentColor(DialogUtils.resolveColor(this.activity, colorAttr))
            return this
        }

        fun build(): MorphDialog {
            return MorphDialog(this)
        }

        fun show(): MorphDialog {
            return MorphDialog(this).show()
        }
    }

    companion object {

        private val REQUEST_CODE = 7324

        fun registerOnActivityResult(requestCode: Int, resultCode: Int, data: Intent?): Registerer {
            return Registerer(requestCode, resultCode, data)
        }
    }
}