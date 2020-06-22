package com.xfb.xinfubao.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
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
import com.xfb.xinfubao.view.flowlayout.FlowLayout
import com.xfb.xinfubao.view.flowlayout.TagAdapter
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
            tvCat.setVisible(it.isIsReal && it.isProductState && !it.isChain)
            tvAddToCat.setVisible(it.isIsReal && it.isProductState && !it.isChain)
            if (!it.isProductState) {
                tvToBuy.text = "已下架"
                tvToBuy.setBackgroundColor(resources.getColor(R.color.color_text_888))
            }

            if (it.giveTypeVos == null || it.giveTypeVos.size == 0) {
                llGift.setVisible(false)
            } else {
                llGift.setVisible(true)
                tagEvaluationFlowLayout.setAdapter(object : TagAdapter<GiveTypeVo?>(it.giveTypeVos) {
                    override fun getView(parent: FlowLayout?, position: Int, item: GiveTypeVo?): View {
                        val tv = LayoutInflater.from(activity).inflate(R.layout.item_tag_goods_detail, null) as TextView
                        tv.text = "${item?.assetsName}${item?.maxFavorable}${item?.unit}"
                        return tv
                    }
                })
            }

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
        //加数量
        ivAdd.setOnClickListener {
            productDetail?.let {
                if (it.inventory > count) {
                    count++
                    tvCount.text = "$count"
                }
            }
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
            if (productDetail == null) return@setOnClickListener
            if (count == 0) {
                showMessage("请添加购买数量")
                return@setOnClickListener
            }
            if (count > productDetail!!.inventory) {
                showMessage("商品库存不足")
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
                if (count > it.inventory) {
                    showMessage("商品库存不足")
                    return@setOnClickListener
                }
                if (!it.isProductState) {
                    return@setOnClickListener
                }
                val products = arrayListOf<Product>()
                val productInfo = it.toProduct()
                productInfo.num = count
                products.add(productInfo)
                if (it.isIsReal) {
                    ConfirmOrderActivity.toActivity(activity!!, products)
                } else {
                    toCreateOrder(products)
                }
            }
        }
    }

    /** 创建订单 */
    private fun toCreateOrder(
            products: ArrayList<Product>
    ) {
        var totalAmount = 0.0
        products.forEach {
            it.num = count
        }
        products.forEach {
            totalAmount += it.productPrice * count
        }
        val requestSaveOrderModel = RequestSaveOrderModel()
        requestSaveOrderModel.userId = "${ConfigUtils.userId()}"
        requestSaveOrderModel.freight = "0"
        requestSaveOrderModel.totalAmount = "$totalAmount"
        requestSaveOrderModel.discount = "${totalAmount * (1.0 - ConfigUtils.mUserInfo.discount)}"
        requestSaveOrderModel.payAmount =
                "${totalAmount * ConfigUtils.mUserInfo.discount}"
        requestSaveOrderModel.productDtoList = products
        showProgress("请稍候")
        request(RetrofitCreateHelper.createApi(BaseApi::class.java).saveOrder(requestSaveOrderModel)) {
            startActivity(
                    Intent(activity!!, CashInActivity::class.java)
                            .putExtra("data", it.data)
            )
        }
    }

}