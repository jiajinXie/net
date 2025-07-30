package com.iflytek.easytrans.crossdevice.translation.net

import com.iflytek.easytrans.crossdevice.translation.db.entity.Conversation
import com.iflytek.easytrans.crossdevice.translation.net.core.HttpResult

/**
 * @author jjxie9
 * @date 2024/9/20
 * @description
 */
class UploadRepository {

    private val apiService = UploadRetrofitClient.service

   suspend fun uploadRecords(callRecords: List<Conversation>): HttpResult<String> {
        return apiService.uploadRecords(callRecords)

    }

    /*suspend fun getHomeList(): HttpResult<Data> {
        return apiService.getHomeList() }*/

}
