package com.adityaanand.morphdialogsample

import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.adityaanand.morphdialog.MorphDialog
import com.adityaanand.morphdialog.interfaces.MorphListCallbackMultiChoice
import com.adityaanand.morphdialog.interfaces.MorphSingleButtonCallback
import com.adityaanand.morphdialog.utils.MorphDialogAction
import com.adityaanand.morphdialogsample.databinding.ActivityMain2Binding
import java.util.*


class MainActivity : AppCompatActivity() {

    lateinit var ui: ActivityMain2Binding
    lateinit var dialog: MorphDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ui = DataBindingUtil.setContentView(this, R.layout.activity_main2)
    }

    fun morph(view: View) {
        dialog = MorphDialog.Builder(this, view as FloatingActionButton)
                .title("Title")
                .iconRes(R.drawable.ic_launcher_background)
                .items("Item 1", "Item 2")
                // .content("This is a sentence. Here is another one.")
                .positiveText("Ok")
                .negativeText("Cancel")
                .neutralText("More")
                .itemsCallbackMultiChoice(object : MorphListCallbackMultiChoice {
                    override fun onSelection(dialog: MorphDialog, which: Array<Int>, texts: Array<CharSequence>) {
                        Toast.makeText(this@MainActivity, "onSelection${which.size}", Toast.LENGTH_SHORT).show()
                    }

                })
                .onPositive({ morphDialog: MorphDialog, morphDialogAction: MorphDialogAction ->
                    Toast.makeText(this@MainActivity, "onPositive", Toast.LENGTH_SHORT).show()
                })
                .onNegative(object : MorphSingleButtonCallback {
                    override fun onClick(a: MorphDialog, which: MorphDialogAction) {
                        Toast.makeText(this@MainActivity, "onNegative", Toast.LENGTH_SHORT).show()
                    }
                })
                .onNeutral(object : MorphSingleButtonCallback {
                    override fun onClick(dialog1: MorphDialog, which: MorphDialogAction) {
                        Toast.makeText(this@MainActivity, "onNeutral", Toast.LENGTH_SHORT).show()
                    }
                })
                //                .cancelable(false)
                .useDarkTheme(Random().nextBoolean())
                .build()
        dialog.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        MorphDialog.registerOnActivityResult(requestCode, resultCode, data)
                .forDialogs(dialog)
    }
}
