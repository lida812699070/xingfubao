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
     * 杏福宝新闻首页列表数据
     */
    @GET("getShareInfo")
    fun newsdetails(@QueryMap map: Map<String, String>): Observable<Result<NewsDetail>>

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
    fun pay(@Body map: RequestPay): Observable<Result<PayOrderModel>>

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

    /** 重置密码 */
    @POST("api/resetPwd")
    fun resetPwd(@Body map: Map<String, String>): Observable<Result<Any?>>

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
    fun noticedetails(@QueryMap map: Map<String, String>): Observable<Result<NewsModel>>

    /** 删除购物车 */
    @POST("api/deleteCart")
    fun deleteCart(@Body map: Map<String, String>): Observable<Result<Any?>>

    /** 用户信息编辑 */
    @POST("api/updateUser")
    fun updateUser(@Body map: Map<String, String>): Observable<Result<Any?>>

    /** NAT基金会 */
    @POST("api/getNatInfo")
    fun getNatInfo(@Body map: Map<String, String>): Observable<Result<List<ItemBalanceModel>>>

    /** NAT质押详情 */
    @POST("api/pledgeList")
    fun pledgeList(@Body map: Map<String, String>): Observable<Result<List<ItemBalanceModel>>>

    /** 转账 */
    @POST("api/transfer")
    fun transfer(@Body map: Map<String, String>): Observable<Result<Any?>>

    /** 银杏果提现记录 */
    @POST("api/getWithdrawalsRecord")
    fun getWithdrawalsRecord(@Body map: Map<String, String>): Observable<Result<List<ItemBalanceModel>>>

    /** 抵押记录 */
    @POST("api/findNatMortgageLogs")
    fun findNatMortgageLogs(@Body map: Map<String, String>): Observable<Result<List<ItemBalanceModel>>>

    /** 财务记录 */
    @POST("api/financialRecord")
    fun financialRecord(@Body map: Map<String, String>): Observable<Result<List<ItemBalanceModel>>>

    /** 提现记录 */
    @POST("api/findNatMortgageLogs")
    fun cashOutLogs(@Body map: Map<String, String>): Observable<Result<List<ItemBalanceModel>>>

    /** NAT置换 */
    @POST("api/mortgageNat")
    fun mortgageNat(@Body map: Map<String, String>): Observable<Result<DiyaModel>>

    /** NAT去质押 */
    @POST("api/natPledgeApply")
    fun natPledgeApply(@Body map: Map<String, String>): Observable<Result<NATResultModel>>

    /** NAT去使用 */
    @POST("api/natMakeUseOf")
    fun natMakeUseOf(@Body map: Map<String, String>): Observable<Result<NATResultModel>>

    /** 资产互兑 */
    @POST("api/assetsExchange")
    fun assetsExchange(@Body map: Map<String, String>): Observable<Result<Any?>>

    /** 资产互兑左边列表 */
    @POST("api/findAssetsType")
    fun findAssetsType(@Body map: Map<String, String>): Observable<Result<List<ExchangeModel>>>

    /** 资产互兑右边列表 */
    @POST("api/findExchangeConfig")
    fun findExchangeConfig(@Body map: Map<String, String>): Observable<Result<List<ExchangeModel>>>

    /** 我的团队 */
    @POST("api/getTeam")
    fun getTeam(@Body map: Map<String, String>): Observable<Result<List<MyTeamModel>>>

    /** 转账手续费 */
    @POST("api/findTransferInfo")
    fun findTransferInfo(@Body map: Map<String, String>): Observable<Result<FindTransferInfoModel>>

    /** 银杏果提现 */
    @POST("api/ginkgoFruitTurnOut")
    fun ginkgoFruitTurnOut(@Body map: Map<String, String>): Observable<Result<Any?>>

    /** 银杏宝转出 */
    @POST("api/ginkgoTreasureTurnOut")
    fun ginkgoTreasureTurnOut(@Body map: Map<String, String>): Observable<Result<Any?>>

    /** 获取银杏宝转出详情 */
    @POST("api/findTurnOutInfo")
    fun findTurnOutInfo(@Body map: Map<String, String>): Observable<Result<YXBCashOutDetail>>

    /** nat提币 */
    @POST("api/natTurnOut")
    fun natTurnOut(@Body map: Map<String, String>): Observable<Result<Any?>>

    /** nat提币 */
    @POST("api/getNatUnlockPackage")
    fun getNatUnlockPackage(@Body map: Map<String, String>): Observable<Result<List<NatUnlockPakeageModel>>>

    /** 转换余额 */
    @POST("api/exchangeBalance")
    fun exchangeBalance(@Body map: Map<String, String>): Observable<Result<ItemBalanceModel>>

    /** NAT基金会-质押-退货地址信息 */
    @POST("api/returnAddress")
    fun returnAddress(@Body map: Map<String, String>): Observable<Result<AddressModel>>

    /** 赎回 */
    @POST("api/redeem")
    fun redeem(@Body map: Map<String, String>): Observable<Result<RedeemResult>>

    /** 转入NAT基本信息 */
    @POST("api/walletInfo")
    fun walletInfo(@Body map: Map<String, String>): Observable<Result<WalletInfoModel>>

    /** 转入NAT 打款申请 */
    @POST("api/aForPayment")
    fun aForPayment(@Body map: Map<String, String>): Observable<Result<RedeemResult>>

    /** 物流公司 */
    @POST("api/logisticsList")
    fun logisticsList(@Body map: Map<String, String>): Observable<Result<List<CompanyModel>>>

    /** 版本更新 */
    @POST("api/getEdition")
    fun getEdition(@Body map: Map<String, String>): Observable<Result<VersionInfoModel>>

    /** 转入明细 */
    @POST("api/exchangeBalanceRecord")
    fun exchangeBalanceRecord(@Body map: Map<String, String>): Observable<Result<List<ItemBalanceModel>>>

    /** 赎回扣除nat数量 */
    @POST("api/redeemDeduct")
    fun redeemDeduct(@Body map: Map<String, String>): Observable<Result<RedeemDeductNumModel>>

    /** 质押转出 */
    @POST("api/pledgeRollOut")
    fun pledgeRollOut(@Body map: Map<String, String>): Observable<Result<Any?>>

    /** 转入矿场 */
    @POST("api/transferOrePond")
    fun transferOrePond(@Body map: Map<String, String>): Observable<Result<Any?>>

    /** nat基金会告知函 */
    @POST("api/notificationLetter")
    fun notificationLetter(@Body map: Map<String, String>): Observable<Result<NatNotfModel>>

    /** 银杏宝 产品使用 */
    @POST("api/natMakeUseOfProduct")
    fun natMakeUseOfProduct(@Body map: Map<String, String>): Observable<Result<List<YinXinBaoUseProductModel>>>

}
