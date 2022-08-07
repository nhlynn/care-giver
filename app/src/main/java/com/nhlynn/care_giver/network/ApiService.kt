package com.nhlynn.care_giver.network

import com.nhlynn.care_giver.network.response.*
import retrofit2.Response
import retrofit2.http.*


interface ApiService {
    @FormUrlEncoded
    @POST(EndPoints.LOGIN)
    suspend fun login(
        @Field("username") userName: String,
        @Field("password") password: String
    ): Response<LoginResponse>

    @FormUrlEncoded
    @POST(EndPoints.REGISTER)
    suspend fun register(
        @Field("username") userName: String,
        @Field("contact") contact: String,
        @Field("password") password: String
    ): Response<BaseResponse>

    @FormUrlEncoded
    @POST(EndPoints.CREATE_NEW_FEED)
    suspend fun createNewFeed(
        @Field("name") name: String,
        @Field("instruction") instruction: String,
        @Field("caution") caution: String,
        @Field("photo") photo: String?
    ): Response<BaseResponse>

    @GET(EndPoints.NEW_FEED)
    suspend fun getNewFeed(): Response<NewFeedResponse>

    @FormUrlEncoded
    @POST(EndPoints.NEAREST_HOSPITAL)
    suspend fun nearestHospital(
        @Field("lat") lat: Double,
        @Field("lng") lng: Double
    ): Response<HospitalResponse>

    @GET(EndPoints.ALARM)
    suspend fun getAlarm(): Response<AlarmResponse>

    @FormUrlEncoded
    @POST(EndPoints.CREATE_ALARM)
    suspend fun createAlarm(
        @Field("alarm_time") alarmTime: String
    ): Response<BaseResponse>

    @FormUrlEncoded
    @POST(EndPoints.CLOSE_ALARM)
    suspend fun closeAlarm(
        @Field("alid") alId: Int,
        @Field("flag") flag: Int
    ): Response<BaseResponse>
}