package com.example.journalog.retrofit

import com.example.journalog.model.AuthRequestModel
import com.example.journalog.model.LogBookResponseModel
import com.example.journalog.model.SingleLogResponseModel
import com.example.journalog.model.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiInterface {
    @GET("get/user/by/id/11")
    suspend fun getUserById(): Response<User>

    @Headers("Content-Type: application/json")
    @POST("user/login")
    suspend fun loginUser(@Body authRequestModel: AuthRequestModel): Response<User>

    @Headers("Content-Type: application/json")
    @POST("user/register")
    suspend fun registerUser(@Body authRequestModel: AuthRequestModel): Response<User>

    @GET("log/book/userId={id}/size={size}")
    suspend fun getLogBooks(@Path("id") userId: String, @Path("size") size: Int): Response<List<LogBookResponseModel>>

    @DELETE("log/book/userId={userId}/logBookId={logBookId}")
    suspend fun deleteLogBook(@Path("userId") userId: String, @Path("logBookId") logBookId: Int): Response<Void>

    @POST("log/book/name={logBookName}/userId={userId}")
    suspend fun createLogBook(@Path("logBookName") logBookName: String, @Path("userId") userId: String): Response<Void>

    @GET("signle/log/logBookId={logBookId}/userId={userId}")
    suspend fun getLogs(@Path("logBookId") logBookId: Int, @Path("userId") userId: String): Response<List<SingleLogResponseModel>>

    @POST("signle/log/create/userId={userId}/logBookId={logBookId}/content={content}")
    suspend fun createLog(@Path("userId") userId: String, @Path("logBookId") logBookId: Int, @Path("content") content: String): Response<SingleLogResponseModel>

    @PUT("log/book/userId={userId}/logBookId={logBookId}/status={status}")
    suspend fun updateLogStatus(@Path("userId") userId: String, @Path("logBookId") logBookId: Int, @Path("status") status: String): Response<LogBookResponseModel>

    @GET("log/book/userId={userId}/bookId={bookId}")
    suspend fun getLogBookById(@Path("userId") userId: String, @Path("bookId") bookId: String): Response<LogBookResponseModel>

    @GET("log/book/userId={userId}")
    suspend fun getActiveLogBook(@Path("userId") userId: String): Response<LogBookResponseModel>

    @GET("temperature")
    suspend fun getCurrentTemperature(): Response<String>
}