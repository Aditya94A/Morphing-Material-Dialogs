package com.adityaanand.morphdialog

import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Build
import android.support.annotation.*
import android.support.design.widget.FloatingActionButton
import com.adityaanand.morphdialog.interfaces.MorphListCallbackMultiChoice
import com.adityaanand.morphdialog.interfaces.MorphListCallbackSingleChoice
import com.adityaanand.morphdialog.interfaces.MorphSingleButtonCallback
import com.adityaanand.morphdialog.utils.MorphDialogAction
import com.afollestad.materialdialogs.util.DialogUtils
import java.util.*

/**
 * @author Aditya Anand (AdityaAnand1)
 */
@Suppress("unused")
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

        if (data.hasExtra(Constants.MORPH_DIALOG_ACTION_TYPE)) //is a button click
            handleButtonClick(data.getSerializableExtra(Constants.MORPH_DIALOG_ACTION_TYPE) as MorphDialogAction)
        else if (data.hasExtra(Constants.INTENT_KEY_SINGLE_CHOICE_LIST_ITEM_POSITION))
            handleSingleChoiceItemSelected(data.getIntExtra(Constants.INTENT_KEY_SINGLE_CHOICE_LIST_ITEM_POSITION, -1),
                    data.getCharSequenceExtra(Constants.INTENT_KEY_SINGLE_CHOICE_LIST_ITEM_TEXT))
        else if (data.hasExtra(Constants.INTENT_KEY_MULTI_CHOICE_LIST_ITEM_TEXTS))
            handleMultiChoiceItemSelected(data.getIntegerArrayListExtra(Constants.INTENT_KEY_MULTI_CHOICE_LIST_ITEM_POSITIONS).toTypedArray(),
                    data.getCharSequenceArrayListExtra(Constants.INTENT_KEY_MULTI_CHOICE_LIST_ITEM_TEXTS).toTypedArray())
    }

    fun handleButtonClick(actionType: MorphDialogAction) {
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

    fun handleSingleChoiceItemSelected(which: Int, text: CharSequence) {
        builder.singleChoiceCallback?.onSelection(this, which, text)
    }

    fun handleMultiChoiceItemSelected(which: Array<Int>, texts: Array<CharSequence>) {
        builder.multiChoiceCallback?.onSelection(this, which, texts)
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

        internal var onPositiveCallback: MorphSingleButtonCallback? = null
        internal var onNegativeCallback: MorphSingleButtonCallback? = null
        internal var onNeutralCallback: MorphSingleButtonCallback? = null
        internal var onAnyCallback: MorphSingleButtonCallback? = null
        internal var singleChoiceCallback: MorphListCallbackSingleChoice? = null
        internal var multiChoiceCallback: MorphListCallbackMultiChoice? = null

        fun iconRes(@DrawableRes iconRes: Int): Builder {
            data.iconRes = iconRes
            return this
        }

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

        fun items(vararg items: CharSequence): Builder {
            data.items = arrayOf(*items)
            return this
        }

        fun itemsCallbackSingleChoice(callback: MorphListCallbackSingleChoice): Builder {
            this.singleChoiceCallback = callback
            data.hasSingleChoiceCallback = true
            return this
        }

        fun itemsCallbackSingleChoice(callback: (MorphDialog, Int, CharSequence?) -> Unit): Builder {
            this.singleChoiceCallback = object : MorphListCallbackSingleChoice {
                override fun onSelection(dialog: MorphDialog, which: Int, text: CharSequence?) {
                    callback(dialog, which, text)
                }

            }
            data.hasSingleChoiceCallback = true
            return this
        }

        fun alwaysCallSingleChoiceCallback(alwaysCallSingleChoiceCallback: Boolean = true): Builder {
            data.alwaysCallSingleChoiceCallback = alwaysCallSingleChoiceCallback
            return this
        }

        fun itemsCallbackMultiChoice(callback: MorphListCallbackMultiChoice): Builder {
            this.multiChoiceCallback = callback
            data.hasMultiChoiceCallback = true
            return this
        }

        fun itemsCallbackMultiChoice(callback: (MorphDialog, Array<Int>, Array<CharSequence>) -> Unit): Builder {
            this.multiChoiceCallback = object : MorphListCallbackMultiChoice {
                override fun onSelection(dialog: MorphDialog, which: Array<Int>, texts: Array<CharSequence>) {
                    callback(dialog, which, texts)
                }

            }
            data.hasMultiChoiceCallback = true
            return this
        }

        fun alwaysCallMultiChoiceCallback(alwaysCallMultiChoiceCallback: Boolean = true): Builder {
            data.alwaysCallMultiChoiceCallback = alwaysCallMultiChoiceCallback
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

        fun onPositive(callback: (MorphDialog, MorphDialogAction) -> Unit): Builder {
            this.onPositiveCallback = object : MorphSingleButtonCallback {
                override fun onClick(dialog: MorphDialog, which: MorphDialogAction) {
                    callback(dialog, which)
                }

            }
            return this
        }

        fun onNegative(callback: MorphSingleButtonCallback): Builder {
            this.onNegativeCallback = callback
            return this
        }

        fun onNegative(callback: (MorphDialog, MorphDialogAction) -> Unit): Builder {
            this.onNegativeCallback = object : MorphSingleButtonCallback {
                override fun onClick(dialog: MorphDialog, which: MorphDialogAction) {
                    callback(dialog, which)
                }

            }
            return this
        }

        fun onNeutral(callback: MorphSingleButtonCallback): Builder {
            this.onNeutralCallback = callback
            return this
        }

        fun onNeutral(callback: (MorphDialog, MorphDialogAction) -> Unit): Builder {
            this.onNeutralCallback = object : MorphSingleButtonCallback {
                override fun onClick(dialog: MorphDialog, which: MorphDialogAction) {
                    callback(dialog, which)
                }
            }
            return this
        }

        fun onAny(callback: MorphSingleButtonCallback): Builder {
            this.onAnyCallback = callback
            return this
        }

        fun onAny(callback: (MorphDialog, MorphDialogAction) -> Unit): Builder {
            this.onAnyCallback = object : MorphSingleButtonCallback {
                override fun onClick(dialog: MorphDialog, which: MorphDialogAction) {
                    callback(dialog, which)
                }
            }
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

        @JvmStatic
        fun registerOnActivityResult(requestCode: Int, resultCode: Int, data: Intent?): Registerer {
            return Registerer(requestCode, resultCode, data)
        }
    }
}