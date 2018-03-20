package com.github.shadowsocks.utils

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Looper
import android.widget.Toast
import java.lang.ref.WeakReference

/**
 * 消息框工具类
 * Created by Jiangwb on 2016-11-08.
 */

object ToastUtil {
    var isShow = true
    private var sToast: WeakReference<Toast>? = null


    @SuppressLint("ShowToast")
    fun showStaticToast(context: Context, text: String) {
        val toast: Toast
        if (sToast == null || sToast!!.get() == null) {
            toast = Toast.makeText(context.applicationContext, text, Toast.LENGTH_SHORT)
            sToast = WeakReference(toast)
        } else {
            toast = sToast?.get() as Toast
            toast.setText(text)
        }
        toast.show()
    }

    /**
     * 短时间显示Toast
     *
     * @param context
     * @param message
     */
    fun showShort(context: Context, message: CharSequence) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    /**
     * 短时间显示Toast
     *
     * @param context
     * @param message
     */
    fun showOnSubThread(context: Context, message: CharSequence) {
        Looper.prepare()
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        Looper.loop()
        val looper = Looper.myLooper() ?: return
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            looper.quitSafely()
        } else {
            looper.quit()
        }
    }

    /**
     * 短时间显示Toast
     *
     * @param context
     * @param message
     */
    fun showShort(context: Context, message: Int) {

        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    /**
     * 短时间显示错误Toast
     *
     * @param context
     * @param errorItem 显示  errorItem+失败,请检查网络或稍后重试
     */
    fun showError(context: Context, errorItem: String) {

        Toast.makeText(context, "${errorItem}失败,请检查网络或稍后重试", Toast.LENGTH_SHORT).show()
    }

    /**
     * 短时间显示错误Toast
     *
     * @param context
     * @param errorItem 显示  errorItem+失败,请检查网络或稍后重试
     */
    fun nonNull(context: Context, errorItem: String) {

        Toast.makeText(context, "${errorItem}不能为空", Toast.LENGTH_SHORT).show()
    }

    /**
     * 长时间显示Toast
     *
     * @param context
     * @param message
     */
    fun showLong(context: Context, message: CharSequence) {

        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    /**
     * 长时间显示Toast
     *
     * @param context
     * @param message
     */
    fun showLong(context: Context, message: Int) {

        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    /**
     * 自定义显示Toast时间
     *
     * @param context
     * @param message
     * @param duration
     */
    fun show(context: Context, message: CharSequence, duration: Int) {

        Toast.makeText(context, message, duration).show()
    }

    /**
     * 自定义显示Toast时间
     *
     * @param context
     * @param message
     * @param duration
     */
    fun show(context: Context, message: Int, duration: Int) {

        Toast.makeText(context, message, duration).show()
    }
}
