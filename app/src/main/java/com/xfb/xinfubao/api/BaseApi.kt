package com.xfb.xinfubao.api

import com.xfb.xinfubao.model.*
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.GET
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
    @POST("api/sendicode")
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

    /**
     * 新闻
     */
    @GET("newslist")
    fun newslist(@QueryMap map: Map<String, String>): Observable<Result<List<NewsModel>>>

    /**
     * 杏福宝新闻首页列表数据
     */
    @GET("newsxfb")
    fun newsxfb(@QueryMap map: Map<String, String>): Observable<Result<List<FindNewsTab>>>

    /**
     * 商品信息
     */
    @POST("api/getProductInfo")
    fun getProductInfo(@Body map: Map<String, String>): Observable<Result<ProductDetail>>

    /**
     * 添加购物车
     */
    @POST("api/addCart")
    fun addCart(@Body map: Map<String, String>): Observable<Result<Any?>>

    /**
     * 确认订单
     */
    @POST("api/confirmOrder")
    fun confirmOrder(@Body map: RequestOrderModel): Observable<Result<OrderInfo>>

    /**
     * 查询收货地址
     */
    @POST("api/findReceive")
    fun findReceive(@Body map: Map<String, String>): Observable<Result<List<ReceiveVo>>>

    /**
     * 添加收货地址
     */
    @POST("api/addReceive")
    fun addReceive(@Body map: Map<String, String>): Observable<Result<Any?>>


}
