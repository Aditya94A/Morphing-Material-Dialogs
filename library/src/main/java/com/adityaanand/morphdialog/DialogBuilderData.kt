package com.adityaanand.morphdialog

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

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
        /*color*/
                             var backgroundColor: Int = 0,
                             var titleColor: Int = -1,
                             var contentColor: Int = -1,
                             var positiveColor: ColorStateList? = null,
                             var negativeColor: ColorStateList? = null,
                             var neutralColor: ColorStateList? = null,
        /*items*/
                             var items: Array<CharSequence>? = null,
                             //single choice
                             var alwaysCallSingleChoiceCallback: Boolean = false,
                             var hasSingleChoiceCallback: Boolean = false,
                             var selectedIndex: Int = -1,
                             //multi choice
                             var alwaysCallMultiChoiceCallback: Boolean = false,
                             var hasMultiChoiceCallback: Boolean = false,
                             var selectedIndices: ArrayList<Int> = arrayListOf()
                             ) : Parcelable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DialogBuilderData

        if (positiveText != other.positiveText) return false
        if (negativeText != other.negativeText) return false
        if (neutralText != other.neutralText) return false
        if (darkTheme != other.darkTheme) return false
        if (title != other.title) return false
        if (content != other.content) return false
        if (cancelable != other.cancelable) return false
        if (canceledOnTouchOutside != other.canceledOnTouchOutside) return false
        if (backgroundColor != other.backgroundColor) return false
        if (titleColor != other.titleColor) return false
        if (contentColor != other.contentColor) return false
        if (positiveColor != other.positiveColor) return false
        if (negativeColor != other.negativeColor) return false
        if (neutralColor != other.neutralColor) return false
        if (!Arrays.equals(items, other.items)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = positiveText?.hashCode() ?: 0
        result = 31 * result + (negativeText?.hashCode() ?: 0)
        result = 31 * result + (neutralText?.hashCode() ?: 0)
        result = 31 * result + darkTheme.hashCode()
        result = 31 * result + (title?.hashCode() ?: 0)
        result = 31 * result + (content?.hashCode() ?: 0)
        result = 31 * result + cancelable.hashCode()
        result = 31 * result + canceledOnTouchOutside.hashCode()
        result = 31 * result + backgroundColor
        result = 31 * result + titleColor
        result = 31 * result + contentColor
        result = 31 * result + (positiveColor?.hashCode() ?: 0)
        result = 31 * result + (negativeColor?.hashCode() ?: 0)
        result = 31 * result + (neutralColor?.hashCode() ?: 0)
        result = 31 * result + (items?.let { Arrays.hashCode(it) } ?: 0)
        return result
    }
}