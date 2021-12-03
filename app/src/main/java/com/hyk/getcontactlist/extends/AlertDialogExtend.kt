package com.hyk.getcontactlist.extends

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.hyk.getcontactlist.dialog.AlertBaseDialog

/** for only Kotlin */
fun Fragment.showCustomAlert(
    content: String,
    title: String = "",
    contentGravity: Int = -1,
    hasCancel: Boolean = false,
    okName: String = "",
    cancelName: String = ""
) {
    if (activity is FragmentActivity) {
        (activity as FragmentActivity).customAlert(
            content,
            title,
            contentGravity,
            hasCancel,
            okName,
            cancelName,
        ) { _, _ ->
        }
    }
}

private fun FragmentActivity.customAlert(
    content: String,
    title: String = "",
    contentGravity: Int = -1,
    hasCancel: Boolean = false,
    okName: String = "",
    cancelName: String = "",
    isCancelable: Boolean = true,
    func: (Boolean, Boolean) -> Unit
) {
    val dialog = AlertBaseDialog.newInstance(
        content,
        title,
        contentGravity,
        hasCancel,
        okName,
        cancelName,
        isCancelable,
        func
    )
    dialog.show(supportFragmentManager, AlertBaseDialog.TAG)
}