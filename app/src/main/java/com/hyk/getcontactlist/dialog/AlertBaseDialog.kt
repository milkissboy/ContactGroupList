package com.hyk.getcontactlist.dialog

import android.os.Bundle
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.widget.TextView
import com.hyk.getcontactlist.R
import com.hyk.getcontactlist.databinding.DialogAlertBaseBinding

class AlertBaseDialog : BaseDialogFragment<DialogAlertBaseBinding>() {

    override val layoutId: Int
        get() = R.layout.dialog_alert_base

    override val gravity: Int
        get() = Gravity.CENTER

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (!isAdded)
            return

        dialog?.setOnDismissListener {
            callBackListener.invoke(false, true)
        }

        val title = arguments?.getString(TITLE) ?: ""
        if (!TextUtils.isEmpty(title)) {
            binding.tvTitle.visibility = View.VISIBLE
            binding.tvTitle.text = title
        }

        binding.tvContent.text = arguments?.getString(CONTENT)

        val okName = arguments?.getString(OK_NAME)
        if (!TextUtils.isEmpty(okName)) {
            binding.tvOk.visibility = View.VISIBLE
            binding.tvOk.text = okName
        }
        setupOkButton(binding.tvOk, true)

        val hasCancel = arguments?.getBoolean(HAS_CANCEL) ?: false
        if (hasCancel) {
            val cancelName = arguments?.getString(CANCEL_NAME)
            if (!TextUtils.isEmpty(cancelName)) {
                binding.tvCancel.visibility = View.VISIBLE
                binding.tvCancel.text = cancelName
            }

            binding.tvCancel.visibility = View.VISIBLE
            setupOkButton(binding.tvCancel, false)
        }

        isCancelable = arguments?.getBoolean(CANCELABLE) ?: false
        if(!isCancelable) {
            dialog?.setOnKeyListener { _, _, _ -> /*dialogInterface, i, keyEvent*/
                callBackListener.invoke(false, true)
                true
            }
        }
    }

    private fun setupOkButton(button: TextView, callBackValue: Boolean) {
        button.setOnClickListener {
            callBackListener.invoke(callBackValue, false)
            dismissAllowingStateLoss()
        }
    }

    companion object {
        const val TAG = "alertDialog"

        private const val TITLE = "TITLE"
        private const val CONTENT = "CONTENT"
        private const val CONTENT_GRAVITY = "CONTENT_GRAVITY"
        private const val HAS_CANCEL = "HAS_CANCEL"
        private const val OK_NAME = "OK_NAME"
        private const val CANCEL_NAME = "CANCEL_NAME"
        private const val CANCELABLE = "CANCELABLE"

        private lateinit var callBackListener: (Boolean, Boolean) -> Unit

        fun newInstance(
            content: String,
            title: String = "",
            contentGravity: Int = -1,
            hasCancel: Boolean = false,
            okName: String = "",
            cancelName: String = "",
            isCancelable: Boolean = true,
            func: (Boolean, Boolean) -> Unit
        ): AlertBaseDialog {

            callBackListener = func

            val args = Bundle()
            args.putString(TITLE, title)
            args.putString(CONTENT, content)
            args.putInt(CONTENT_GRAVITY, contentGravity)
            args.putBoolean(HAS_CANCEL, hasCancel)
            args.putString(OK_NAME, okName)
            args.putString(CANCEL_NAME, cancelName)
            args.putBoolean(CANCELABLE, isCancelable)

            val fragment = AlertBaseDialog()
            fragment.arguments = args
            return fragment
        }
    }
}