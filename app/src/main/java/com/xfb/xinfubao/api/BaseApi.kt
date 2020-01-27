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

    /** 保存订单 */
    @POST("api/saveOrder")
    fun saveOrder(@Body map: RequestSaveOrderModel): Observable<Result<ArrayList<String>>>

    /** 收银台 */
    @POST("api/cashRegister")
    fun cashRegister(@Body map: RequestCashRegisterDto): Observable<Result<CashRegisterModel>>

    /** 立即支付 */
    @POST("api/pay")
    fun pay(@Body map: RequestPay): Observable<Result<Any?>>

    /** 获取产品类型 */
    @POST("api/findProductType")
    fun findProductType(@Body map: Map<String, String>): Observable<Result<List<ProductType>>>

    /** 获取产品 */
    @POST("api/findProductByCategory")
    fun findProductByCategory(@Body map: Map<String, String>): Observable<Result<List<Product>>>

    /** 修改支付密码 */
    @POST("api/updpaypsw")
    fun updpaypsw(@Body map: Map<String, String>): Observable<Result<Any?>>

    /** 修改登录密码 */
    @POST("api/updloginpsw")
    fun updloginpsw(@Body map: Map<String, String>): Observable<Result<Any?>>

    /** 修改绑定手机号 */
    @POST("api/bindnumber")
    fun bindnumber(@Body map: Map<String, String>): Observable<Result<Any?>>

    /** 实物订单列表 */
    @POST("api/listRealOrder")
    fun listRealOrder(@Body map: Map<String, String>): Observable<Result<List<MyOrderModel>>>

    /** 取消订单 */
    @POST("api/cancelOrder")
    fun cancelOrder(@Body map: Map<String, String>): Observable<Result<Any?>>

    /** 实物订单详情 */
    @POST("api/getRealOrder")
    fun getRealOrder(@Body map: Map<String, String>): Observable<Result<OrderDetailModel>>

    /** 实物订单确认发货 */
    @POST("api/confirmReceipt")
    fun confirmReceipt(@Body map: Map<String, String>): Observable<Result<Any?>>

    /** 愿力值 */
    @POST("api/findPowerAvow")
    fun findPowerAvow(@Body map: Map<String, String>): Observable<Result<List<ItemBalanceModel>>>

    /** 银杏果 */
    @POST("api/ginkgoFruitInfo")
    fun ginkgoFruitInfo(@Body map: Map<String, String>): Observable<Result<List<ItemBalanceModel>>>

    /** 银杏叶 */
    @POST("api/ginkgoLeafInfo")
    fun ginkgoLeafInfo(@Body map: Map<String, String>): Observable<Result<List<ItemBalanceModel>>>

    /** 银杏宝 */
    @POST("api/ginkgoTreasureInfo")
    fun ginkgoTreasureInfo(@Body map: Map<String, String>): Observable<Result<List<ItemBalanceModel>>>

    /** 积分商城 */
    @POST("api/integralInfo")
    fun integralInfo(@Body map: Map<String, String>): Observable<Result<List<ItemBalanceModel>>>

    /** NAT资产 */
    @POST("api/natAssets")
    fun natAssets(@Body map: Map<String, String>): Observable<Result<List<ItemBalanceModel>>>

    /** 获取用户购物车 */
    @POST("api/listUserCart")
    fun listUserCart(@Body map: Map<String, String>): Observable<Result<List<Product>>>

    /** 身份证认证 */
    @POST("api/checkidentitycard")
    fun checkidentitycard(@Body map: Map<String, String>): Observable<Result<Any?>>

    /** 护照认证 */
    @POST("api/checkpassport")
    fun checkpassport(@Body map: Map<String, String>): Observable<Result<Any?>>

    /** 获取认证情况 */
    @POST("api/querycertificationdetails")
    fun querycertificationdetails(@Body map: Map<String, String>): Observable<Result<AuthResultModel>>

    /** 获取用户信息 */
    @POST("api/getUserInfo")
    fun getUserInfo(@Body map: Map<String, String>): Observable<Result<UserInfo>>

    /** 杏福宝消息类型列表 */
    @GET("noticexfb")
    fun noticexfb(@QueryMap map: Map<String, String>): Observable<Result<List<FindNewsTab>>>

    /** 杏福宝消息列表 */
    @GET("noticelistxfb")
    fun noticelistxfb(@QueryMap map: Map<String, String>): Observable<Result<List<NewsModel>>>

    /** 消息详情 */
    @GET("noticedetails")
    fun noticedetails(@QueryMap map: Map<String, String>): Observable<Result<Any?>>

    /** 删除购物车 */
    @POST("api/deleteCart")
    fun deleteCart(@Body map: Map<String, String>): Observable<Result<Any?>>

    /** 用户信息编辑 */
    @POST("api/updateUser")
    fun updateUser(@Body map: Map<String, String>): Observable<Result<Any?>>


}
