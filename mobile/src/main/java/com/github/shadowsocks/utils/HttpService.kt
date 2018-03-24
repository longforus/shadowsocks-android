package com.github.shadowsocks.utils

import com.google.gson.JsonObject
import io.reactivex.Observable
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.FieldMap
import retrofit2.http.GET
import retrofit2.http.POST

/**
 *
 * @author  XQ Yang
 * @date 3/24/2018  10:26 AM
 */
interface HttpService{
    @GET("/ssr/auth/login.do")
    fun login(@FieldMap map:Map<String,String>):Observable<JsonObject>

    @POST("/ssr/auth/register.do")
    fun register(@Body body:RequestBody):Observable<JsonObject>
}