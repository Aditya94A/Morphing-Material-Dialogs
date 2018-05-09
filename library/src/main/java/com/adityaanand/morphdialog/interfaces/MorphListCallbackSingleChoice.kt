package com.adityaanand.morphdialog.interfaces

import com.adityaanand.morphdialog.MorphDialog

interface MorphListCallbackSingleChoice {

    fun onSelection(dialog: MorphDialog, which: Int, text: CharSequence?)
}