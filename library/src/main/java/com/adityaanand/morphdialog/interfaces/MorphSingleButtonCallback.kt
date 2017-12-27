package com.adityaanand.morphdialog.interfaces

import com.adityaanand.morphdialog.MorphDialog
import com.adityaanand.morphdialog.utils.MorphDialogAction

interface MorphSingleButtonCallback {

    fun onClick(dialog: MorphDialog, which: MorphDialogAction)
}