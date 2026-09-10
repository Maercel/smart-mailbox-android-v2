package com.example.smartmailbox.api.data

import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface AuthAPI {
    @POST("users/mobile-login")
    suspend fun postMobileLogin(@Body request: MobileLoginRequest): Response<MobileLoginResponse>

    /*
    @GET("users/profile")
    suspend fun getProfile(): Response<ProfileResponse>
    */

    @POST("users/register")
    suspend fun postRegister(@Body registerRequest: RegisterRequest
    ): Response<RegisterResponse>

    @Multipart
    @POST("users/mobile-login/face-verify")
    suspend fun postFaceVerify(@Part currentImage: MultipartBody.Part
    ): Response<FaceVerifyResponse>
}