package com.github.shadowsocks.utils

import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonObject
import okhttp3.ResponseBody
import retrofit2.Converter
import java.io.IOException
import java.lang.reflect.Type


/**
 * 解析响应回来的数据，当接口为非正常状态时抛出异常，不再将data转成json
 * Created by Jiangwb on 2016-11-28.
 */

 class GsonResponseBodyConverter(private val gson: Gson, private val type: Type) : Converter<ResponseBody, JsonObject> {

    @Throws(IOException::class)
    override fun convert(value: ResponseBody): JsonObject {
        val response = value.string()
        Log.i("fecHttp", "response>>$response")
        return gson.fromJson(response, type)

    }


}
