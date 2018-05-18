package com.adityaanand.morphdialog.interfaces

import com.adityaanand.morphdialog.MorphDialog

interface MorphListCallbackMultiChoice {

    fun onSelection(dialog: MorphDialog, which: Array<Int>, texts: Array<CharSequence>)
}