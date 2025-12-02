package com.adityaanand.morphdialog

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.transition.ArcMotion
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import com.adityaanand.morphdialog.databinding.ActivityDialog2Binding
import com.adityaanand.morphdialog.morphutil.MorphDialogToFab
import com.adityaanand.morphdialog.morphutil.MorphFabToDialog
import com.adityaanand.morphdialog.utils.MorphDialogAction
import com.afollestad.materialdialogs.MaterialDialog
import java.io.Serializable

// TODO: 22-Dec-17 I'm not sure if we need AppCompatActivity here...
open class MorphDialogActivity : Activity() {

    lateinit var ui: ActivityDialog2Binding
    internal var id: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ui = ActivityDialog2Binding.inflate(layoutInflater)
        setContentView(ui.root)
        val params = intent.extras!!
        id = params.getLong(Constants.MORPH_DIALOG_ID)
        val data = params.getParcelable<DialogBuilderData>(Constants.MORPH_DIALOG_BUILDER_DATA)!!

        val builder = MaterialDialog.Builder(this)
                .onPositive { _, _ -> onActionButtonClicked(MorphDialogAction.POSITIVE) }
                .onNegative { _, _ -> onActionButtonClicked(MorphDialogAction.NEGATIVE) }
                .onNeutral { _, _ -> onActionButtonClicked(MorphDialogAction.NEUTRAL) }
                .canceledOnTouchOutside(data.canceledOnTouchOutside)
                .cancelable(data.cancelable)

        if (data.content != null)
            builder.content(data.content!!)
        if (data.title != null)
            builder.title(data.title!!)
        if (data.positiveText != null)
            builder.positiveText(data.positiveText!!)
        if (data.negativeText != null)
            builder.negativeText(data.negativeText!!)
        if (data.neutralText != null)
            builder.neutralText(data.neutralText!!)
        if (data.neutralColor != null)
            builder.neutralColor(data.neutralColor!!)
        if (data.negativeColor != null)
            builder.negativeColor(data.negativeColor!!)
        if (data.positiveColor != null)
            builder.positiveColor(data.positiveColor!!)
        if (data.iconRes != null)
            builder.iconRes(data.iconRes!!)

        /**items**/
        if (data.items != null)
            builder.items(*data.items!!)
        //single choice
        if (data.alwaysCallSingleChoiceCallback)
            builder.alwaysCallSingleChoiceCallback()
        if (data.hasSingleChoiceCallback)
            builder.itemsCallbackSingleChoice(data.selectedIndex) { _, _, which, text ->
                onSingleItemPicked(which, text.toString())
                true
            }

        //multi choice
        if (data.alwaysCallMultiChoiceCallback)
            builder.alwaysCallMultiChoiceCallback()
        if (data.hasMultiChoiceCallback)
            builder.itemsCallbackMultiChoice(data.selectedIndices.toTypedArray(), { dialog, which, text ->
                onMultiItemsPicked(which, text)
                true
            })

        if (data.backgroundColor != 0) {
            //   builder.backgroundColor(data.getBackgroundColor()); //doesn't work for some reason
            ui.container.setBackgroundColor(data.backgroundColor)
        }
        if (data.titleColor != -1)
            builder.titleColor(data.titleColor)
        if (data.contentColor != -1)
            builder.backgroundColor(data.contentColor)

        val dialog = builder.build()
        (dialog.view.parent as ViewGroup).removeView(dialog.view) //remove old parent
        ui.container.addView(dialog.view) //add new parent
        ui.root.setOnClickListener { if (data.canceledOnTouchOutside) onBackPressed() } //closes dialog if you click outside of it
        ui.container.setOnClickListener { }  //leaving empty so that nothing happens when you click on non-clickable bits of the dialog

        setupTransition()
    }

    fun onSingleItemPicked(which: Int, text: String) {
        val returnData = Intent()
                .putExtra(Constants.INTENT_KEY_SINGLE_CHOICE_LIST_ITEM_POSITION, which)
                .putExtra(Constants.INTENT_KEY_SINGLE_CHOICE_LIST_ITEM_TEXT, text)
        closeDialog(returnData)
    }

    fun onMultiItemsPicked(which: Array<Int>, texts: Array<CharSequence>) {
        val returnData = Intent()
                .putIntegerArrayListExtra(Constants.INTENT_KEY_MULTI_CHOICE_LIST_ITEM_POSITIONS, which.toCollection(ArrayList()))
                .putCharSequenceArrayListExtra(Constants.INTENT_KEY_MULTI_CHOICE_LIST_ITEM_TEXTS, texts.toCollection(ArrayList()))
        closeDialog(returnData)
    }

    fun onActionButtonClicked(actionType: Serializable) {
        val returnData = Intent().putExtra(Constants.MORPH_DIALOG_ACTION_TYPE, actionType)
        closeDialog(returnData)
    }

    fun setupTransition() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
            return  //Show dialog normally if below Lollipop
        val arcMotion = ArcMotion()
        arcMotion.minimumHorizontalAngle = 50f
        arcMotion.minimumVerticalAngle = 50f

        val easeInOut = AnimationUtils.loadInterpolator(this, android.R.interpolator.fast_out_slow_in)

        val background: Int = getBackgroundColor()
        val sharedEnter = MorphFabToDialog(background)
        sharedEnter.pathMotion = arcMotion
        sharedEnter.interpolator = easeInOut

        val sharedReturn = MorphDialogToFab(background)
        sharedReturn.pathMotion = arcMotion
        sharedReturn.interpolator = easeInOut

        sharedEnter.addTarget(ui.container)
        sharedReturn.addTarget(ui.container)
        window.sharedElementEnterTransition = sharedEnter
        window.sharedElementReturnTransition = sharedReturn
    }

    fun getBackgroundColor(): Int = (ui.container.background as ColorDrawable).color

    override fun onBackPressed() {
        //    Intent returnData = new Intent();
        //   returnData.putExtra("wasDismissed", true); // TODO: 23-Dec-17
        //    setResult(Activity.RESULT_OK, returnData);
        closeDialog()
    }

    @JvmOverloads
    fun closeDialog(returnData: Intent = Intent()) {
        returnData.putExtra(Constants.MORPH_DIALOG_ID, id)
        setResult(Activity.RESULT_OK, returnData)
        val height = ui.container.height
        ui.container.minimumHeight = height //need to remember the height for smooth animation
        ui.container.removeAllViews()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            finishAfterTransition()
        else
            finish() //boo
    }
}
