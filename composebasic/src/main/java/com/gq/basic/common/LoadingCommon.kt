/*
package com.gq.basic.common

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Window
import android.widget.ProgressBar
import android.widget.TextView
import com.gq.basic.R

*/
/**
 * 加载提示
 * @Author: GQ
 * @Date: 2021/3/28 16:19
 *//*

object LoadingCommon {

    private val handler by lazy { Handler(Looper.getMainLooper()) }

    fun showLoadingDialog(
        context: Context?,
        msg: String = "加载中...",
        timeOut: Long = 6000,
        textSize: Int = 12
    ): LoadingDialog? {
        context?.let { ctx ->
            val dialog = LoadingDialog(ctx).apply {
                show()
                setText(msg, textSize)
            }
            handler.postDelayed({
                dialog.dismiss()
            }, timeOut)
            return dialog
        } ?: return null
    }

    fun showToastDialog(
        context: Context?,
        msg: String?,
        timeOut: Long = 1600,
        textSize: Int = 13
    ) {
        msg?.takeIf { it != "" }?.let { str ->
            context?.let { ctx ->
                val dialog = LoadingDialog(ctx).apply {
                    show()
                    setCancelable(true)
                    setCanceledOnTouchOutside(true)
                    setText(str, textSize)
                    hideProgressBar()
                }
                handler.postDelayed({
                    dialog.dismiss()
                }, timeOut)
            }
        }
    }
}


class LoadingDialog(context: Context) :
    android.app.AlertDialog(context, R.style.TransparentDialog) {

    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        super.onCreate(savedInstanceState)
        this.setContentView(R.layout.dialog_loading)
        setCancelable(false)
        setCanceledOnTouchOutside(false)

    }

    fun setText(str: String, ts: Int = 12) {
        findViewById<TextView>(R.id.tv_message).apply {
            text = str
            textSize = ts.toFloat()
        }
    }

    fun hideProgressBar() {
        findViewById<ProgressBar>(R.id.progressBar).gradientHideView(0)
    }
}*/
