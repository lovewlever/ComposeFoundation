package com.gq.basic.common

import timber.log.Timber

class TimberCloseTree: Timber.Tree() {
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {

    }
}