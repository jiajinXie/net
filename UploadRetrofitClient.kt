package com.iflytek.easytrans.crossdevice.translation.net

import com.iflytek.easytrans.crossdevice.translation.net.interceptor.CommonRequestInterceptor
import com.iflytek.easytrans.crossdevice.translation.net.interceptor.CommonResponseInterceptor
import okhttp3.OkHttpClient


object UploadRetrofitClient : BaseRetrofitClient() {

    val service by lazy { getService(ApiService::class.java, ApiService.BASE_URL) }

    override fun handleBuilder(builder: OkHttpClient.Builder) {
        builder.addInterceptor(CommonRequestInterceptor())
            .addInterceptor(CommonResponseInterceptor())

        /* val httpCacheDirectory = File(App.CONTEXT.cacheDir, "responses")
         val cacheSize = 10 * 1024 * 1024L // 10 MiB
         val cache = Cache(httpCacheDirectory, cacheSize)
         builder.cache(cache)
                 .addInterceptor { chain ->
                     var request = chain.request()
                     if (!NetUtils.isNetworkAvailable(App.CONTEXT)) {
                         request = request.newBuilder()
                                 .cacheControl(CacheControl.FORCE_CACHE)
                                 .build()
                     }
                     val response = chain.proceed(request)
                     response
                 }*/
    }
}