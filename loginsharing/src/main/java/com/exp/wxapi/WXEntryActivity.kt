package com.exp.wxapi

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.gq.loginsharing.common.WeChatCommon
import com.tencent.mm.opensdk.constants.ConstantsAPI
import com.tencent.mm.opensdk.modelbase.BaseReq
import com.tencent.mm.opensdk.modelbase.BaseResp
import com.tencent.mm.opensdk.modelbiz.SubscribeMessage
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram
import com.tencent.mm.opensdk.modelbiz.WXOpenBusinessView
import com.tencent.mm.opensdk.modelbiz.WXOpenBusinessWebview
import com.tencent.mm.opensdk.modelmsg.SendAuth
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler
import timber.log.Timber


class WXEntryActivity: Activity(), IWXAPIEventHandler {

    private var result: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            val intent = intent
            WeChatCommon.api.handleIntent(intent, this)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
        WeChatCommon.api.handleIntent(intent, this)
    }


    override fun onReq(req: BaseReq) {
        Timber.i("${req.type}")
    }

    override fun onResp(resp: BaseResp) {
        result = when (resp.errCode) {
            BaseResp.ErrCode.ERR_OK -> "操作成功"
            BaseResp.ErrCode.ERR_USER_CANCEL -> "已取消"
            BaseResp.ErrCode.ERR_AUTH_DENIED -> "授权被拒绝"
            BaseResp.ErrCode.ERR_UNSUPPORT -> "不支持该操作"
            else -> resp.errStr
        }
        Timber.i("${resp.errCode}")

        /**
         * 订阅消息
         */
        if (resp.type == ConstantsAPI.COMMAND_SUBSCRIBE_MESSAGE) {
            val subscribeMsgResp = resp as SubscribeMessage.Resp
            val text = String.format("openid=%s\ntemplate_id=%s\nscene=%d\naction=%s\nreserved=%s",
                subscribeMsgResp.openId,
                subscribeMsgResp.templateID,
                subscribeMsgResp.scene,
                subscribeMsgResp.action,
                subscribeMsgResp.reserved)
            Toast.makeText(this, text, Toast.LENGTH_LONG).show()
        }

        /**
         * 启动微信小程序
         */
        if (resp.type == ConstantsAPI.COMMAND_LAUNCH_WX_MINIPROGRAM) {
            val launchMiniProgramResp = resp as WXLaunchMiniProgram.Resp
            val text = String.format("openid=%s\nextMsg=%s\nerrStr=%s",
                launchMiniProgramResp.openId,
                launchMiniProgramResp.extMsg,
                launchMiniProgramResp.errStr)
            Toast.makeText(this, text, Toast.LENGTH_LONG).show()
        }

        /**
         * 命令开放式业务视图
         */
        if (resp.type == ConstantsAPI.COMMAND_OPEN_BUSINESS_VIEW) {
            val launchMiniProgramResp = resp as WXOpenBusinessView.Resp
            val text = String.format("openid=%s\nextMsg=%s\nerrStr=%s\nbusinessType=%s",
                launchMiniProgramResp.openId,
                launchMiniProgramResp.extMsg,
                launchMiniProgramResp.errStr,
                launchMiniProgramResp.businessType)
            Toast.makeText(this, text, Toast.LENGTH_LONG).show()
        }

        /**
         * 命令打开业务网络视图
         */
        if (resp.type == ConstantsAPI.COMMAND_OPEN_BUSINESS_WEBVIEW) {
            val response = resp as WXOpenBusinessWebview.Resp
            val text = String.format("businessType=%d\nresultInfo=%s\nret=%d",
                response.businessType,
                response.resultInfo,
                response.errCode)
            Toast.makeText(this, text, Toast.LENGTH_LONG).show()
        }

        /**
         * 命令发送授权
         */
        if (resp.type == ConstantsAPI.COMMAND_SENDAUTH) {
            val authResp = resp as SendAuth.Resp
            val code = authResp.code
            WeChatCommon.onCommandSendAuth(code,result)
        }

        WeChatCommon.onInfoSuccess(resp.errCode == BaseResp.ErrCode.ERR_OK, result)

        finish()
    }


    /* private void articleCallback(int type, int aid) {
        HttpRetrofitRequest.retrofit(HttpApi_xie.class)
                .shapeDrainageCallback(Hawk.get(HawkConstant.TOKEN), type, aid)
                .compose(RxSchedulers.applySchedulers())
                .subscribe();
    }*/

}