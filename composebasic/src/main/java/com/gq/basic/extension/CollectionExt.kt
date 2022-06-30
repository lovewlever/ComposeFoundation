package com.gq.basic.extension

/**
 * 判断List是否不为null或不为空
 */
inline fun <E,R> Collection<E>?.ifNotNullAndEmpty(block: (MutableList<E>) -> R): R? {
    if (!this.isNullOrEmpty()) {
        return block(this.toMutableList())
    }
    return null
}