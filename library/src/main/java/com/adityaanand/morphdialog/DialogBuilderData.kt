package com.adityaanand.morphdialog

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Everything that is common between MorphDialog and MaterialDialogs is stored in here This is created from a MorphDialog, sent to [MorphDialogActivity] and a MaterialDialog is built from it.
 */
@SuppressLint("ParcelCreator")
@Parcelize
data class DialogBuilderData(var positiveText: CharSequence? = null,
                             var negativeText: CharSequence? = null,
                             var neutralText: CharSequence? = null,
                             var darkTheme: Boolean = false,
                             var title: CharSequence? = null,
                             var content: CharSequence? = null,
                             var cancelable: Boolean = true,
                             var canceledOnTouchOutside: Boolean = true,
                             var backgroundColor: Int = 0,
                             var titleColor: Int = -1,
                             var contentColor: Int = -1,
                             var positiveColor: ColorStateList?= null,
                             var negativeColor: ColorStateList? = null,
                             var neutralColor: ColorStateList? = null


) : Parcelable