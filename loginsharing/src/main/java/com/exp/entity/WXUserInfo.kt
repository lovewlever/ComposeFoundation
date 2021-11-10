package com.exp.entity

class WXUserInfo {
    var openid: String = ""
    var nickname: String = ""
    var sex = 0
    var province: String = ""
    var city: String = ""
    var country: String = ""
    var headimgurl: String = ""
    var unionid: String = ""
    override fun toString(): String {
        return "WXUserInfo{" +
                "openid='" + openid + '\'' +
                ", sex=" + sex +
                ", nickname='" + nickname + '\'' +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                ", headimgurl='" + headimgurl + '\'' +
                ", unionid='" + unionid + '\'' +
                '}'
    }
}