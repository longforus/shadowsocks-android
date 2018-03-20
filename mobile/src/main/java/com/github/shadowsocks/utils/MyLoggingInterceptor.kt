package com.github.shadowsocks.utils

import android.util.Log
import okhttp3.Headers
import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.internal.platform.Platform
import okio.Buffer
import java.io.IOException
import java.nio.charset.Charset
import java.nio.charset.UnsupportedCharsetException
import java.util.concurrent.TimeUnit

/**
 * Created by XQ Yang on 2017/6/16  10:55.
 * Description :
 */

class MyLoggingInterceptor @JvmOverloads constructor(private val logger: Logger = Logger.DEFAULT) : Interceptor {
    @Volatile private var level: Level? = null

    init {
        this.level = Level.NONE
    }

    fun setLevel(level: Level?): MyLoggingInterceptor {
        if (level == null) {
            throw NullPointerException("level == null. Use Level.NONE instead.")
        } else {
            this.level = level
            return this
        }
    }

    fun getLevel(): Level? {
        return this.level
    }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val level = this.level
        val request = chain.request()
        if (level == Level.NONE) {
            return chain.proceed(request)
        } else {
            val logBody = level == Level.BODY
            val logHeaders = logBody || level == Level.HEADERS
            val requestBody = request.body()
            val hasRequestBody = requestBody != null
            val connection = chain.connection()
            val protocol = if (connection != null) connection.protocol() else Protocol.HTTP_1_1
            var requestStartMessage = "--> " + request.method() + ' ' + request.url() + ' ' + protocol
            if (!logHeaders && hasRequestBody) {
                requestStartMessage = requestStartMessage + " (" + requestBody!!.contentLength() + "-byte body)"
            }
            var bodyStr = ""
            this.logger.log(requestStartMessage)
            if (logHeaders) {
                if (hasRequestBody) {
                    if (requestBody!!.contentType() != null) {
                        this.logger.log("Content-Type: " + requestBody.contentType()!!)
                    }

                    if (requestBody.contentLength() != -1L) {
                        this.logger.log("Content-Length: " + requestBody.contentLength())
                    }
                }

                val startNs = request.headers()
                var buffer = 0

                val response = startNs.size()
                while (buffer < response) {
                    val tookMs = startNs.name(buffer)
                    if (!"Content-Type".equals(tookMs, ignoreCase = true) && !"Content-Length".equals(tookMs, ignoreCase = true)) {
                        this.logger.log(tookMs + ": " + startNs.value(buffer))
                    }
                    ++buffer
                }

                if (logBody && hasRequestBody && !request.url().toString().contains("upload") && !request.url().toString().contains("downLoad")) {//上传下载就不打了
                    if (this.bodyEncoded(request.headers())) {
                        this.logger.log("--> END " + request.method() + " (encoded body omitted)")
                    } else {
                        val buf = Buffer()
                        requestBody!!.writeTo(buf)
                        var var29: Charset? = UTF8
                        val var31 = requestBody.contentType()
                        if (var31 != null) {
                            var29 = var31.charset(UTF8)
                        }

                        this.logger.log("")
                        bodyStr = buf.readString(var29!!)

                        this.logger.log(bodyStr)
                        this.logger.log("--> END " + request.method() + " (" + requestBody.contentLength() + "-byte body)")
                    }
                } else {
                    this.logger.log("--> END " + request.method())
                }
            }

            val var27 = System.nanoTime()


            val old = chain.proceed(request)
            val var32 = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - var27)
            val responseBody = old.body()
            val contentLength = responseBody!!.contentLength()
            val bodySize = if (contentLength != -1L) contentLength.toString() + "-byte" else "unknown-length"
            this.logger.log("<-- " + old.code() + ' ' + old.message() + ' ' + old.request().url() + '?' + bodyStr + " (" + var32 + "ms" +
                (if (!logHeaders) ", $bodySize body" else "") + ')')
            if (logHeaders) {
                val headers = old.headers()
                var source = 0

                val buffer1 = headers.size()
                while (source < buffer1) {
                    this.logger.log(headers.name(source) + ": " + headers.value(source))
                    ++source
                }

                if (logBody && old.body() != null && false) {//返回的body也不要了
                    if (this.bodyEncoded(old.headers())) {
                        this.logger.log("<-- END HTTP (encoded body omitted)")
                    } else {
                        val var33 = responseBody.source()
                        var33.request(9223372036854775807L)
                        val var34 = var33.buffer()
                        var charset: Charset? = UTF8
                        val contentType = responseBody.contentType()
                        if (contentType != null) {
                            try {
                                charset = contentType.charset(UTF8)
                            } catch (var26: UnsupportedCharsetException) {
                                this.logger.log("")
                                this.logger.log("Couldn\'t decode the response body; charset is likely malformed.")
                                this.logger.log("<-- END HTTP")
                                return old
                            }

                        }

                        if (contentLength != 0L) {
                            this.logger.log("")
                            val string = var34.clone().readString(charset!!)
                            this.logger.log(string)
                        }

                        this.logger.log("<-- END HTTP (" + var34.size() + "-byte body)")
                    }
                } else {
                    this.logger.log("<-- END HTTP")
                }
            }

            return old
        }
    }

    private fun bodyEncoded(headers: Headers): Boolean {
        val contentEncoding = headers.get("Content-Encoding")
        return contentEncoding != null && !"identity".equals(contentEncoding, ignoreCase = true)
    }

    interface Logger {

        fun log(var1: String)

        companion object {
            val DEFAULT: Logger = object : Logger {
                override fun log(message: String) {
                    Platform.get().log(Log.DEBUG, message, null)
                }
            }
        }
    }

    enum class Level private constructor() {
        NONE,
        BASIC,
        HEADERS,
        BODY
    }

    companion object {
        private val UTF8 = Charset.forName("UTF-8")
    }
}
