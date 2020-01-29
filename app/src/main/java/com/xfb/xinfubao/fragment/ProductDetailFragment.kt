package com.xfb.xinfubao.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.careagle.sdk.helper.RetrofitCreateHelper
import com.careagle.sdk.utils.PriceChangeUtils
import com.google.gson.Gson
import com.xfb.xinfubao.MyImageLoader
import com.xfb.xinfubao.R
import com.xfb.xinfubao.activity.CashInActivity
import com.xfb.xinfubao.activity.ConfirmOrderActivity
import com.xfb.xinfubao.api.BaseApi
import com.xfb.xinfubao.callback.MyClickCallBack
import com.xfb.xinfubao.model.*
import com.xfb.xinfubao.utils.ConfigUtils
import com.xfb.xinfubao.utils.setVisible
import kotlinx.android.synthetic.main.fragment_product_detail.*

class ProductDetailFragment : BaseFragment() {
    var productDetail: ProductDetail? = null
    var myClickCallBack: MyClickCallBack? = null
    private var count = 1

    companion object {
        fun newInstance(productInfo: ProductDetail): ProductDetailFragment {
            val fragmentLogin = ProductDetailFragment()
            val bundle = Bundle()
            bundle.putString("productInfoJson", Gson().toJson(productInfo))
            fragmentLogin.arguments = bundle
            return fragmentLogin
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_product_detail
    }

    override fun initUI(view: View?, savedInstanceState: Bundle?) {
        val productJson = arguments?.getString("productInfoJson")
        try {
            productDetail = Gson().fromJson(productJson, ProductDetail::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        bindData()
        initListener()
        productDetail?.let {
            initBanner(it.productImg)
        }
    }

    private fun bindData() {
        productDetail?.let {
            tvTitle.text = it.productName
            tvPrice.text =
                getString(R.string.rmb_tag, PriceChangeUtils.getNumKb(it.productPrice))
            tvStock.text = "库存：${it.inventory}"
            tvCat.setVisible(it.isIsReal)
            tvAddToCat.setVisible(it.isIsReal)
        }
    }

    private var isInitBanner = false
    private fun initBanner(banners: MutableList<ProductImg>) {
        if (banner == null) return
        if (!isInitBanner) {
            isInitBanner = true
            banner.setImages(banners).setDelayTime(4000).setImageLoader(MyImageLoader()).start()
        } else {
            banner.update(banners)
        }
    }

    private fun initListener() {
        //TODO 分享
        ivShare.setOnClickListener {

        }
        //加数量
        ivAdd.setOnClickListener {
            count++
            tvCount.text = "$count"
        }
        //减数量
        ivJian.setOnClickListener {
            if (count > 0) {
                count--
            }
            tvCount.text = "$count"
        }
        //加入购物车
        tvAddToCat.setOnClickListener {
            if (count == 0) {
                showMessage("请添加购买数量")
                return@setOnClickListener
            }
            showProgress("请稍候")
            val map = hashMapOf<String, String>()
            map["userId"] = "${ConfigUtils.getUserInfo()?.userId}"
            map["productId"] = "${productDetail?.productId}"
            map["quantity"] = "$count"
            request(RetrofitCreateHelper.createApi(BaseApi::class.java).addCart(map)) {
                tvCat.isSelected = true
            }
        }
        //商品详情
        tvProductDetail.setOnClickListener {
            myClickCallBack?.myClick()
        }

        //立即购买
        tvToBuy.setOnClickListener {
            productDetail?.let {
                if (count <= 0) {
                    showMessage("请选择购买数量")
                    return@setOnClickListener
                }
                val products = arrayListOf<Product>()
                val productInfo = it.toProduct()
                productInfo.num = count
                products.add(productInfo)
                if (it.isIsReal) {
                    ConfirmOrderActivity.toActivity(activity!!, products)
                } else {
                    requestConfirmOrder(products)
                }
            }
        }
    }

    /** 确认订单 */
    private fun requestConfirmOrder(products: ArrayList<Product>) {
        val requestOrderModel = RequestOrderModel()
        requestOrderModel.userId = ConfigUtils.userId()
        requestOrderModel.productDtoList = products
        request(
            RetrofitCreateHelper.createApi(BaseApi::class.java).confirmOrder(
                requestOrderModel
            )
        ) {
            toCreateOrder(it.data, products)
        }
    }

    /** 创建订单 */
    private fun toCreateOrder(
        data: OrderInfo,
        products: ArrayList<Product>
    ) {
        val requestSaveOrderModel = RequestSaveOrderModel()
        requestSaveOrderModel.userId = "${ConfigUtils.userId()}"
        requestSaveOrderModel.freight = "${data.freight}"
        requestSaveOrderModel.totalAmount = "${data.orderTotalAmount}"
        requestSaveOrderModel.discount = "${data.discount}"
        requestSaveOrderModel.payAmount = "${data.payAmount}"
        requestSaveOrderModel.productDtoList = products
        request(RetrofitCreateHelper.createApi(BaseApi::class.java).saveOrder(requestSaveOrderModel)) {
            startActivity(
                Intent(activity!!, CashInActivity::class.java)
                    .putExtra("data", it.data)
            )
        }
    }


}