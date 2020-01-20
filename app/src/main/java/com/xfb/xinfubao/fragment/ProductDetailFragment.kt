package com.xfb.xinfubao.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.google.gson.Gson
import com.xfb.xinfubao.R
import com.xfb.xinfubao.activity.ConfirmOrderActivity
import com.xfb.xinfubao.model.ProductDetail
import kotlinx.android.synthetic.main.fragment_product_detail.*

class ProductDetailFragment : BaseFragment() {
    var productDetail: ProductDetail? = null

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
        if (productDetail == null) {
            return
        }

        initListener()
    }

    private fun initListener() {
        tvToBuy.setOnClickListener {
            val intent = Intent(activity!!, ConfirmOrderActivity::class.java)
            startActivity(intent)
        }
    }

}