package com.github.shadowsocks.utils

import com.github.shadowsocks.BuildConfig
import com.github.shadowsocks.utils.AbstractHttpManager.Companion.BASE_URL
import com.github.shadowsocks.utils.AbstractHttpManager.Companion.DEFAULT_TIMEOUT
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import java.nio.charset.Charset
import java.util.concurrent.TimeUnit
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory

/**
 * Http请求处理类
 * Created by Administrator on 2016-10-14.  框架基本 完全和项目无关
 */

 object AbstractHttpManager {



     val baseUrl: String=""

    protected fun init() {
        //手动创建一个OkHttpClient并设置超时时间
        val builder = OkHttpClient.Builder()
        builder.connectTimeout(DEFAULT_TIMEOUT.toLong(), TimeUnit.SECONDS)
        builder.readTimeout(0, TimeUnit.SECONDS)
        builder.writeTimeout(0, TimeUnit.SECONDS)
        if (BuildConfig.DEBUG) {
            //创建拦截器，记录网络请求的信息，包含 请求/响应行 + 头 + 体
            val logging = MyLoggingInterceptor()
            logging.setLevel(MyLoggingInterceptor.Level.BODY)
            builder.addInterceptor(logging)
        }
        BASE_URL = baseUrl

        val retrofit = Retrofit.Builder().client(builder.build()).addConverterFactory(ResponseConverterFactory.create()).addCallAdapterFactory(
            RxJava2CallAdapterFactory.create()).baseUrl(BASE_URL).build()
        httpService = retrofit.create(interfaceClass)
    }

    protected abstract fun <PARAM : RequestMapBuild> getMapBuild(): PARAM

    /**
     * 处理http请求
     * a
     *
     * @param basePar 封装的请求数据
     */
    fun <RESULT, PARAM : RequestMapBuild> deal(basePar: AbstractSubscribe<RESULT, HTTP_SERVICE, PARAM>): Observable<RESULT> {
        return basePar.getObservable(httpService, if (basePar.getParams() != null) basePar.getParams() else getMapBuild<RequestMapBuild>())
            .subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.io())
            .doOnSubscribe(Consumer<Disposable> { disposable ->
                val disposablePool = basePar.getDisposablePool()
                if (disposablePool != null) {
                    disposablePool!!.addDisposable(disposable)
                }
            })
            .map(basePar)
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError(Consumer<Throwable> { throwable ->
                basePar.onError(throwable)//公用错误处理, 这里处理后如果也添加订阅了onError  的consumer,也会被调用
            })
    }

    companion object {
        private val UTF8 = Charset.forName("UTF-8")
        private var BASE_URL = ""
        private val DEFAULT_TIMEOUT = 10
    }
}
