package com.gq.basic.subclass

import android.net.Uri
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebView

class CustomWebChromeClient: WebChromeClient() {

     fun onShowFileChooseImage(
        webView: WebView?,
        filePathCallback: ValueCallback<Array<Uri>>?,
        fileChooserParams: FileChooserParams?
    ): Boolean {
         return this.onShowFileChooser(webView, filePathCallback, fileChooserParams)
     }

     fun onShowFileChooseVideo(
         webView: WebView?,
         filePathCallback: ValueCallback<Array<Uri>>?,
         fileChooserParams: FileChooserParams?
     ): Boolean {
         return this.onShowFileChooser(webView, filePathCallback, fileChooserParams)
     }

    override fun onShowFileChooser(
        webView: WebView?,
        filePathCallback: ValueCallback<Array<Uri>>?,
        fileChooserParams: FileChooserParams?
    ): Boolean {
        fileChooserParams?.acceptTypes?.let { accepts ->
            if (accepts.isNotEmpty()) {
                val s = accepts[0]
                if (s.contains("image")) {
                    onShowFileChooseImage(webView, filePathCallback, fileChooserParams)
                } else if (s.contains("video")) {
                    onShowFileChooseVideo(webView, filePathCallback, fileChooserParams)
                }
            }
        }
        return true
    }
}