package com.xfb.xinfubao.api

import com.xfb.xinfubao.model.HomeModel
import com.xfb.xinfubao.model.Result
import com.xfb.xinfubao.model.UserInfo
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.QueryMap

interface BaseApi {

    /**
     * 首页
     */
    @POST("api/index")
    fun index(@Body map: Map<String, String>): Observable<Result<HomeModel>>

    /**
     * 获取验证码
     */
    @POST("api/getCode")
    fun getCheckCode(@Body map: Map<String, String>): Observable<Result<Any?>>

    /**
     * 注册
     */
    @POST("api/register")
    fun toRegister(@Body map: Map<String, String>): Observable<Result<Any?>>

    /**
     * 登录
     */
    @POST("api/login")
    fun toLogin(@Body map: Map<String, String>): Observable<Result<UserInfo>>


}
