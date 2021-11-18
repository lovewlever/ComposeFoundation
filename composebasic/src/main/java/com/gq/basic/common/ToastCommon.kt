package com.gq.basic.common

import android.view.Gravity
import android.widget.Toast
import com.gq.basic.AppContext

object ToastCommon {

    fun showCenterToast(str: CharSequence?, duration: Int = Toast.LENGTH_LONG) {
        getCenterToast(str, duration).show()
    }

    fun showCenterToast(resId: Int, duration: Int = Toast.LENGTH_LONG) {
        getCenterToast(AppContext.application.resources.getText(resId), duration).show()
    }

    fun showDefaultToast(str: CharSequence?, duration: Int = Toast.LENGTH_LONG) {
        getDefaultToast(str, duration).show()
    }

    fun showDefaultToast(resId: Int, duration: Int = Toast.LENGTH_LONG) {
        getDefaultToast(AppContext.application.resources.getText(resId), duration).show()
    }

    private fun getCenterToast(str: CharSequence?, duration: Int) =
        Toast.makeText(AppContext.application, str, duration).also { t: Toast ->
            t.setGravity(Gravity.CENTER, 0, 0)
        }

    private fun getDefaultToast(str: CharSequence?, duration: Int) =
        Toast.makeText(AppContext.application, str, duration)

}