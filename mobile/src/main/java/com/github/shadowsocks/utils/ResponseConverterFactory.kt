package com.github.shadowsocks.utils

import com.google.gson.Gson
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

/**
 * 重写ConverterFactory，调用我们自己的返回信息转换类
 * Created by Jiangwb on 2016-11-28.
 */

class ResponseConverterFactory private constructor(private val gson: Gson) : Converter.Factory() {


    override fun responseBodyConverter(type: Type, annotations: Array<out Annotation>?, retrofit: Retrofit?): Converter<ResponseBody, *>? {
        return  return GsonResponseBodyConverter(gson, type)
    }

    companion object {

        /**
         * Create an instance using `gson` for conversion. Encoding to JSON and
         * decoding from JSON (when no charset is specified by a header) will use UTF-8.
         */
        @JvmOverloads
        fun create(gson: Gson = Gson()): ResponseConverterFactory {
            return ResponseConverterFactory(gson)
        }
    }
}
/**
 * Create an instance using a default [Gson] instance for conversion. Encoding to JSON and
 * decoding from JSON (when no charset is specified by a header) will use UTF-8.
 */
