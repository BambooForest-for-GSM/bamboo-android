package com.example.data.network.common

import com.example.data.base.BaseDataResponse
import com.example.data.base.BaseResponse
import com.example.data.model.admin.response.AlgorithmResponse
import com.example.data.model.common.LoginResponse
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import retrofit2.http.*

interface CommonApi {

    // 게시물 페이지로 조히
    @GET("algorithm/page")
    fun getAlgorithmPage(
        @Header("Authorization") authorization: String,
        @Query("count") count: Int,
        @Query("page") page: Int,
        @Query("status") status: String
    ): Flowable<BaseDataResponse<AlgorithmResponse>>

    @POST("login")
    fun postLogin(
        @Header("Authorization") Authorization: String,
    ): Single<BaseDataResponse<LoginResponse>>
    @DELETE("logout")
    fun deleteLogOut(
        @Header("Authorization") authorization: String
    ): Single<BaseResponse>



}