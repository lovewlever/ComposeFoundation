package com.gq.loginsharing.common

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import android.widget.Toast
import com.exp.api.TPLSApi
import com.exp.entity.WXUserInfo
import com.google.gson.JsonParser
import com.gq.basic.AppContext
import com.gq.basic.common.GsonCommon
import com.gq.basic.common.MetadataCommon
import com.tencent.mm.opensdk.constants.ConstantsAPI
import com.tencent.mm.opensdk.modelmsg.*
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import timber.log.Timber
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer

/**
 * 转ByteArray
 */
fun Bitmap.toByteArray(): ByteArray {
    val byteCount = this.byteCount
    val buf = ByteBuffer.allocate(byteCount)
    this.copyPixelsToBuffer(buf)
    return buf.array()
}

object WeChatCommon {


    lateinit var api: IWXAPI
    private lateinit var appId: String
    var onLoginSuccessCallback: (userInfo: WXUserInfo) -> Unit = {}
    var onSuccessMessageCallback: (isSuccess: Boolean, str: String) -> Unit = { _,_ ->}


    fun regToWx(context: Context, appId: String) {
        this.appId = appId

        api = WXAPIFactory.createWXAPI(context, appId, true)

        context.registerReceiver(object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                // 将该app注册到微信
                api.registerApp(appId)
            }
        }, IntentFilter(ConstantsAPI.ACTION_REFRESH_WXAPP))
    }

    /**
     * 命令发送授权
     */
    internal fun onCommandSendAuth(code: String, str: String) {
        getAccessToken(code)
    }

    internal fun onInfoSuccess(isSuccess: Boolean, str: String) {
        onSuccessMessageCallback(isSuccess, str)
    }




    /**
     * 微信授权登录
     */
    fun weChatLogin() {
        // 判断是否安装了微信客户端
        if (!api.isWXAppInstalled) {
            Toast.makeText(AppContext.application, "您还未安装微信客户端！", Toast.LENGTH_SHORT)
                .show()
            return
        }
        val req = SendAuth.Req()
        req.scope = "snsapi_userinfo"
        req.state = "wechat_sdk_demo_test"
        val sendReq = api.sendReq(req)
        Timber.i("$sendReq")
    }

    /**
     * 微信分享
     */
    fun shareToWeChatWebpage(title: String, description: String, shareUrl: String, bitmap: Bitmap, scene: Int) {
        val webpage = WXWebpageObject()
        webpage.webpageUrl = shareUrl
        val msg = WXMediaMessage(webpage)
        msg.title = title
        msg.description = description
        msg.thumbData = ByteArrayOutputStream().also { bos: ByteArrayOutputStream ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos)
        }.toByteArray()
        val req = SendMessageToWX.Req()
        req.transaction = "webpage${System.currentTimeMillis()}"
        req.message = msg
        req.scene = scene
        val send: Boolean = api.sendReq(req)
        Timber.i("$send")
    }

    /**
     * 分享图片到微信
     */
    fun shareToWeChatImage(bitmap: Bitmap, scene: Int) {

        val wexinImage = WXImageObject(bitmap)

        val msg = WXMediaMessage(wexinImage)
        val req = SendMessageToWX.Req()
        val thumbBitmap = Bitmap.createScaledBitmap(bitmap, 100, 100, true)
        bitmap.recycle()

        msg.thumbData = thumbBitmap.toByteArray()
        req.transaction = "${System.currentTimeMillis()}"
        req.message = msg
        req.scene = scene
        val send: Boolean = api.sendReq(req)
        Timber.i("$send")
    }



    /**
     * 获取授权口令
     */
    private fun getAccessToken(code: String) {
        val appId = MetadataCommon.getMetadataString("WX_APP_ID")
        val secret = MetadataCommon.getMetadataString("WX_APP_SECRET")
        getRetrofit().create(TPLSApi::class.java).accessToken(
            String.format("https://api.weixin.qq.com/sns/oauth2/access_token?" +
                    "appid=%s&secret=%s&code=%s&grant_type=authorization_code",
                appId,
                secret,
                code)
        ).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>,
            ) {
                response.body()?.let { rb: ResponseBody ->
                    val parseString = JsonParser.parseString(rb.string())
                    if (parseString.isJsonObject) {
                        val access_token = parseString.asJsonObject.get("access_token").asString
                        val openid = parseString.asJsonObject.get("openid").asString
                        getUserWxInfo(access_token, openid)
                    }
                    Timber.i(rb.string())
                }
                //getUserWxInfo(access_token, openid)
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Timber.e(t)
            }
        })
    }


    /**
     * 获取用户个人信息
     */
    private fun getUserWxInfo(access_token: String, openid: String) {
        getRetrofit().create(TPLSApi::class.java).userWxInfo(
            String.format("https://api.weixin.qq.com/sns/userinfo?access_token=%s&openid=%s",
                access_token,
                openid)
        ).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val str = response.body()?.string() ?: ""
                val parseString = JsonParser.parseString(str)
                if (parseString.isJsonObject) {
                    val fromJson = GsonCommon.gson.fromJson(parseString, WXUserInfo::class.java)
                    onLoginSuccessCallback(fromJson)
                }

            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Timber.e(t)
            }
        })
    }

    private fun getRetrofit() =
        Retrofit
            .Builder()
            .baseUrl("https://api.weixin.qq.com/")
            .build()
}