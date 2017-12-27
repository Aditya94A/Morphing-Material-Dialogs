package com.adityaanand.morphdialog

import com.adityaanand.morphdialog.R
import com.adityaanand.morphdialog.databinding.ActivityDialogBinding
import com.adityaanand.morphdialog.morphutil.MorphDialogToFab
import com.adityaanand.morphdialog.morphutil.MorphFabToDialog
import com.adityaanand.morphdialog.utils.MorphDialogAction
import android.app.Activity
import android.content.Intent
import android.databinding.DataBindingUtil
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.transition.ArcMotion
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import com.afollestad.materialdialogs.MaterialDialog
import hugo.weaving.DebugLog
import java.io.Serializable

// TODO: 22-Dec-17 I'm not sure if we need AppCompatActivity here...
@DebugLog
open class MorphDialogActivity : Activity() {

    lateinit var ui: ActivityDialogBinding
    internal var id: Long = 0

    internal val backgroundColor: Int
        get() {
            val background = ui.container.background
            return (background as ColorDrawable).color
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ui = DataBindingUtil.setContentView(this, R.layout.activity_dialog)
        val params = intent.extras
        id = params!!.getLong(Constants.MORPH_DIALOG_ID)
        val data = params.getParcelable<DialogBuilderData>(Constants.MORPH_DIALOG_BUILDER_DATA)
        val builder = MaterialDialog.Builder(this)
                .content(data!!.content!!)
                .title(data.title!!)
                .positiveText(data.positiveText!!)
                .onPositive { dialog, which -> onActionButtonClicked(MorphDialogAction.POSITIVE) }
                .negativeText(data.negativeText!!)
                .onNegative { dialog, which -> onActionButtonClicked(MorphDialogAction.NEGATIVE) }
                .neutralText(data.neutralText!!)
                .onNeutral { dialog, which -> onActionButtonClicked(MorphDialogAction.NEUTRAL) }
                .canceledOnTouchOutside(data.canceledOnTouchOutside)
                .cancelable(data.cancelable)
        if (data.neutralColor != null)
            builder.neutralColor(data.neutralColor!!)
        if (data.negativeColor != null)
            builder.negativeColor(data.negativeColor!!)
        if (data.positiveColor != null)
            builder.positiveColor(data.positiveColor!!)

        if (data.backgroundColor != 0) {
            //   builder.backgroundColor(data.getBackgroundColor()); //doesn't work for some reason
            ui.container.setBackgroundColor(data.backgroundColor)
        }
        if (data.titleColor != -1)
            builder.titleColor(data.titleColor)
        if (data.contentColor != -1) {
            builder.backgroundColor(data.contentColor)
        }

        val dialog = builder.build()
        (dialog.view.parent as ViewGroup).removeView(dialog.view) //remove old parent
        ui.container.addView(dialog.view) //add new parent
        ui.root.setOnClickListener { if (data.canceledOnTouchOutside) onBackPressed() } //closes dialog if you click outside of it
        ui.container.setOnClickListener { }  //leaving empty so that nothing happens when you click on non-clickable bits of the dialog

        setupTransition()
    }

    fun onActionButtonClicked(actionType: Serializable) {
        val returnData = Intent()
        returnData.putExtra(Constants.MORPH_DIALOG_ACTION_TYPE, actionType)
        closeDialog(returnData)
    }

    fun setupTransition() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
            return  //Show dialog normally if below Lollipop
        val arcMotion = ArcMotion()
        arcMotion.minimumHorizontalAngle = 50f
        arcMotion.minimumVerticalAngle = 50f

        val easeInOut = AnimationUtils.loadInterpolator(this, android.R.interpolator.fast_out_slow_in)

        val sharedEnter = MorphFabToDialog(backgroundColor)
        sharedEnter.pathMotion = arcMotion
        sharedEnter.interpolator = easeInOut

        val sharedReturn = MorphDialogToFab(backgroundColor)
        sharedReturn.pathMotion = arcMotion
        sharedReturn.interpolator = easeInOut

        sharedEnter.addTarget(ui.container)
        sharedReturn.addTarget(ui.container)
        window.sharedElementEnterTransition = sharedEnter
        window.sharedElementReturnTransition = sharedReturn
    }

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
            finish()
    }
}
