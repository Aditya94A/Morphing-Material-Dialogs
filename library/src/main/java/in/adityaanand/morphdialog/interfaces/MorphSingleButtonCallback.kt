package `in`.adityaanand.morphdialog.interfaces

import `in`.adityaanand.morphdialog.MorphDialog
import `in`.adityaanand.morphdialog.utils.MorphDialogAction

internal interface MorphSingleButtonCallback {

    fun onClick(dialog: MorphDialog, which: MorphDialogAction)
}