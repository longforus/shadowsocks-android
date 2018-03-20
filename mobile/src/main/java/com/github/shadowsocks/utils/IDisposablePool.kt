package com.github.shadowsocks.utils

import io.reactivex.disposables.Disposable

/**
 * Created by XQ Yang on 2017/9/6  17:15.
 * Description : 连接池
 */

interface IDisposablePool {
    fun addDisposable(disposable: Disposable)

    /**
     * 丢弃连接 在view销毁时调用
     */
    fun clearPool()

    companion object {
        val STRATEGY_STOP = "stop"
        val STRATEGY_DESTROY = "destroy"
    }

}
