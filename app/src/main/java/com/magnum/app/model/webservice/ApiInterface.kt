package com.magnum.app.model.webservice

import com.magnum.app.model.dto.response.*

import retrofit2.Call
import retrofit2.http.*

interface ApiInterface {


    @GET("v1/skinella/forceUpdateGet")
    fun getForceUpdate(): Call<ForceUpdateResponse>

  /*  @PUT("api/v1/customer/notificationRead")
    fun readNotificationApi(@Body readNotificationRequest: ReadNotificationRequest): Call<ReadNotificationResponse>

    @POST("api/v1/customer/shareApi")
    fun customerFeedShareApi(@Body shareRequestModel: LikeRequestModel): Call<BaseResponse>

    @Multipart
    @PUT("api/v1/community/contest/uploadVideo")
    fun uploadContestVideo(@Part contestMedia: MultipartBody.Part?): Call<BaseResponse>

    @Multipart
    @POST("api/v1/community/communityFeed")
    fun shareCommunityThoughts(
        @PartMap requestDataMap: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part mediaList: List<MultipartBody.Part?>
    ): Call<BaseResponse>

    @DELETE("api/v1/customer/{feedId}/removeFeed")
    fun removeCustomerFeed(@Path("feedId") feedId: String): Call<BaseResponse>*/

  }