package com.gq.basic.extension

val RegexPunctuation = Regex(",|\\.|\\?|\"|:|~|!|《|》|？|~|！|-|，|。|“|”|\"|\"|@|\\\$|#|%")

/**
 * 用于匹配Html标签
 */
val regexHtmlLabel = Regex("<[a-zA-Z]>|<\\/[a-zA-Z]>")