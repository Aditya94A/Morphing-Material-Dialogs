package com.adityaanand.morphdialogstandalone

import android.app.Activity
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.transition.ArcMotion
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.adityaanand.morphdialogstandalone.databinding.ActivityDialogBinding
import com.adityaanand.morphdialogstandalone.util.MorphDialogToFab
import com.adityaanand.morphdialogstandalone.util.MorphFabToDialog
import com.afollestad.materialdialogs.MaterialDialog


class DialogActivity : AppCompatActivity() {

    lateinit var ui: ActivityDialogBinding

    internal val backgroundColor: Int
        get() {
            val background = ui.container.background
            return (background as ColorDrawable).color
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //set theme to dark if you want
        //setTheme(shouldUseDarkTheme() ? R.style.MorphDialog_Dark : R.style.MorphDialog_Light);
        ui = DataBindingUtil.setContentView(this, R.layout.activity_dialog)
        val params = intent.extras        //get the text and add to MaterialDialog if !null
        val content = params!!.getCharSequence("content")
        val title = params.getString("title")
        val positive = params.getString("positive")
        val negative = params.getString("negative")
        val builder = MaterialDialog.Builder(this)
                .content(content)
                .title(title)
                .positiveText(positive)
                .onPositive { dialog, which ->
                    this@DialogActivity.setResult(Activity.RESULT_OK)//useful if you care about startActivityForResult()'s result
                    this@DialogActivity.closeDialog()
                }
                .negativeText(negative!!)
                .onNegative { dialog, which -> this@DialogActivity.onBackPressed() }

        val dialog = builder.build()
        (dialog.view.parent as ViewGroup).removeView(dialog.view) //remove old parent
        ui.container.addView(dialog.view) //add new parent

        ui.root.setOnClickListener { this@DialogActivity.closeDialog() } //closes dialog if you click outside of it

        ui.container.setOnClickListener {
            //leaving empty so that nothing happens when you click on non-clickable bits of the dialog
        }

        setupSharedEelementTransitions()
    }

    fun setupSharedEelementTransitions() {
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

        if (ui.container != null) {
            sharedEnter.addTarget(ui.container)
            sharedReturn.addTarget(ui.container)
        }
        window.sharedElementEnterTransition = sharedEnter
        window.sharedElementReturnTransition = sharedReturn
    }

    override fun onBackPressed() {
        setResult(Activity.RESULT_CANCELED)
        closeDialog()
    }

    fun closeDialog() {
        val height = ui.container.height
        ui.container.minimumHeight = height //need to remember the height for smooth animation
        ui.container.removeAllViews()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            finishAfterTransition()
        else
            finish()
    }

    companion object {


        private fun getIntent(context: Context, title: String, content: CharSequence,
                              positive: String?, negative: String): Intent {
            val intent = Intent(context, DialogActivity::class.java)
            intent.putExtra("title", title)
            intent.putExtra("content", content.toString())
            intent.putExtra("positive", positive)
            intent.putExtra("negative", negative)
            return intent
        }

        fun open(context: AppCompatActivity, fab: View, title: String, content: CharSequence) {
            val intent = getIntent(context, title, content, null, "OK")

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                val options = ActivityOptions.makeSceneTransitionAnimation(context, fab, "morph_transition")
                context.startActivityForResult(intent, 1, options.toBundle())
            } else
                context.startActivityForResult(intent, 1)
        }
    }
}
