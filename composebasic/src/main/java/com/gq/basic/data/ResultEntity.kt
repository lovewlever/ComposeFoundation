package com.gq.basic.data

import java.io.Serializable

class ResultEntity<T> (
    var msg: String = "",
    var code: Int = -1,
    var data: MutableList<T> = mutableListOf()
): Serializable
