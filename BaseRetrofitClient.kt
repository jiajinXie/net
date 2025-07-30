package com.iflytek.easytrans.crossdevice.translation.net

import com.iflytek.easytrans.crossdevice.translation.net.core.HttpResponseCallAdapterFactory
import com.iflytek.easytrans.crossdevice.translation.net.core.MoshiResultTypeAdapterFactory
import com.iflytek.easytrans.crossdevice.translation.net.error.GlobalErrorHandler
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

/**
 * @author jjxie9
 * @date 2024/9/21
 * @description
 */
abstract class BaseRetrofitClient {
    private val globalErrorHandler = GlobalErrorHandler()

    companion object {
        private const val TIME_OUT = 5L
        const val DEBUG = true
    }

    private val moshi = Moshi.Builder()
        //添加返回的json 数据自定义解析器
        .add(MoshiResultTypeAdapterFactory(getHttpWrapperHandler()))
        .addLast(KotlinJsonAdapterFactory())
        .build()


    private val client: OkHttpClient
        get() {
            val builder = OkHttpClient.Builder()
            val logging = HttpLoggingInterceptor()
            if (DEBUG) {
                logging.level = HttpLoggingInterceptor.Level.BODY
            } else {
                logging.level = HttpLoggingInterceptor.Level.NONE
            }
            builder.addInterceptor(logging)
                .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
                .readTimeout(TIME_OUT, TimeUnit.SECONDS)
                .writeTimeout(TIME_OUT, TimeUnit.SECONDS)
                .callTimeout(TIME_OUT, TimeUnit.SECONDS)
            handleBuilder(builder)
            return builder.build()
        }

    protected abstract fun handleBuilder(builder: OkHttpClient.Builder)

    fun <S> getService(serviceClass: Class<S>, baseUrl: String): S {
        return Retrofit.Builder()
            .client(client)
//            .addConverterFactory(GsonConverterFactory.create())
            //1. 转换器，使用Moshi 是因为codegen 比GSON 反射更加的高效
            .addConverterFactory(MoshiConverterFactory.create(moshi))

            // 2. 就是为了返回HttpResult<T> 这个啊
            .addCallAdapterFactory(
                HttpResponseCallAdapterFactory(globalErrorHandler) //全局的错误处理器
            )
            .baseUrl(baseUrl)
            .build().create(serviceClass)
    }

    //可以改成抽象方法 子类实现
    private fun getHttpWrapperHandler(): MoshiResultTypeAdapterFactory.HttpWrapper {
        return object : MoshiResultTypeAdapterFactory.HttpWrapper {
            override fun getStatusCodeKey(): String = "errorCode"

            override fun getErrorMsgKey(): String = "errorMsg"

            override fun getDataKey(): String = "data"

            override fun isRequestSuccess(statusCode: Int?): Boolean = statusCode == 0
        }
    }
}
