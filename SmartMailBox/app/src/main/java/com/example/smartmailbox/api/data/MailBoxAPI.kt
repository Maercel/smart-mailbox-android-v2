package com.example.smartmailbox.api.data

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface MailBoxAPI {
    @POST("v1/Access/openbox")
    suspend fun postMailBoxData(@Body request: PostMailBoxData): Response<PostMailBoxDataResponse>
}