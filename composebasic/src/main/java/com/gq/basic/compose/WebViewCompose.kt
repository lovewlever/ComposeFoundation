package com.gq.basic.compose

import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import java.net.URLEncoder

/**
 * WebView
 */
@Composable
fun WebViewCompose(
    modifier: Modifier = Modifier,
    url: String = "",
    data: String = "",
    mimeType: String? = null,
    encoding: String? = null,
    javascriptInterface: Any? = null,
    onProgressDoneCallback: (WebView) -> Unit = {}
) {

    val webViewProgressState = remember {
        mutableStateOf(0F)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        if (webViewProgressState.value != 0F && webViewProgressState.value != 1F) {
            LinearProgressIndicator(
                progress = webViewProgressState.value,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp)
            )
        }
        FWebView(
            progressState = webViewProgressState,
            onProgressDoneCallback = onProgressDoneCallback,
            javascriptInterface = javascriptInterface,
        ) { webView ->
            if (url != "") {
                webView.loadUrl(url)
            } else if (data != "") {
                webView.loadData(URLEncoder.encode(data, "UTF-8"), mimeType, encoding)
            }

        }
    }
}


@Composable
private fun FWebView(
    modifier: Modifier = Modifier,
    progressState: MutableState<Float>,
    onProgressDoneCallback: (WebView) -> Unit = {},
    javascriptInterface: Any? = null,
    block: (webView: WebView) -> Unit = {}
) {
    AndroidView(
        modifier = modifier.fillMaxSize(),
        factory = { ctx ->
            WebView(ctx).also { webView: WebView ->
                webView.settings.javaScriptEnabled = true
                webView.settings.domStorageEnabled = true
                //????????????JS??????
                webView.settings.javaScriptCanOpenWindowsAutomatically = true
                //??????????????????????????????????????????
                webView.settings.useWideViewPort = true
                webView.settings.loadWithOverviewMode = true
                //?????????WebView??????????????????????????????
                webView.settings.allowFileAccess = true
                webView.settings.loadsImagesAutomatically = true
                //?????????
                webView.settings.supportMultipleWindows()
                webView.webChromeClient = object : WebChromeClient() {
                    override fun onProgressChanged(view: WebView?, newProgress: Int) {
                        super.onProgressChanged(view, newProgress)
                        progressState.value = newProgress / 100F
                        if (newProgress == 100) {
                            view?.let { onProgressDoneCallback(it) }
                        }
                    }
                }
                webView.webViewClient = object : WebViewClient() {
                    override fun shouldOverrideUrlLoading(
                        view: WebView?,
                        request: WebResourceRequest?,
                    ): Boolean {
                        return false
                    }
                }
                javascriptInterface?.let {
                    webView.addJavascriptInterface(it, "android")
                }
            }
        }) { webView ->
        block(webView)
    }
}