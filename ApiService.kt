package com.iflytek.easytrans.crossdevice.translation.net

import com.iflytek.easytrans.crossdevice.translation.db.entity.Conversation
import com.iflytek.easytrans.crossdevice.translation.net.core.HttpResult
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    companion object {
        const val BASE_URL = "https://www.wanandroid.com/"
    }

    @POST("upload")
    suspend fun uploadRecords(@Body callRecords: List<Conversation>): HttpResult<String>

   /* @GET("article/list/0/json")
    suspend fun getHomeList(): HttpResult<Data>*/
}
