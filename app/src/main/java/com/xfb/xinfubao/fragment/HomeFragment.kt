package com.xfb.xinfubao.fragment

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.careagle.sdk.helper.RetrofitCreateHelper
import com.careagle.sdk.utils.PriceChangeUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.xfb.xinfubao.MyApplication
import com.xfb.xinfubao.R
import com.xfb.xinfubao.api.BaseApi
import com.xfb.xinfubao.constant.Constant.MONEY_RMB
import com.xfb.xinfubao.model.HomeModel
import com.xfb.xinfubao.model.HomeModule
import com.xfb.xinfubao.model.Product
import com.xfb.xinfubao.utils.loadRound
import com.xfb.xinfubao.utils.loadUri
import com.youth.banner.loader.ImageLoader
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : BaseFragment() {

    var moduleList = arrayListOf<HomeModule>()
    var productList = arrayListOf<Product>()
    var moduleAdapter = object :
        BaseQuickAdapter<HomeModule, BaseViewHolder>(R.layout.item_home_module, moduleList) {
        override fun convert(helper: BaseViewHolder, item: HomeModule) {
            val ivImages = helper.getView<ImageView>(R.id.ivImages)
            ivImages.loadRound(item.iocUrl, 12f)
        }
    }

    var productAdapter =
        object :
            BaseQuickAdapter<Product, BaseViewHolder>(R.layout.item_home_product, productList) {
            override fun convert(helper: BaseViewHolder, item: Product) {
                helper.setText(R.id.tvProductName, item.productName)
                    .setText(
                        R.id.tvProductPrice,
                        "$MONEY_RMB${PriceChangeUtils.getNumKb(item.productPrice)}"
                    )
                val ivProduct = helper.getView<ImageView>(R.id.ivProduct)
                ivProduct.loadUri(item.imgUrl)
            }
        }

    override fun getLayoutId(): Int {
        return R.layout.fragment_home
    }

    override fun initUI(view: View?, savedInstanceState: Bundle?) {
        request(
            RetrofitCreateHelper.createApi(BaseApi::class.java)
                .index(mapOf())
        ) {
            bindData(it.data)
        }

        initModuleRecyclerView()
        initHomeProductRecyclerView()
    }

    private fun initHomeProductRecyclerView() {
        recyclerViewHomeProduct.layoutManager = GridLayoutManager(activity, 2)
        recyclerViewHomeProduct.adapter = productAdapter
        productAdapter.addFooterView(
            LayoutInflater.from(activity!!).inflate(
                R.layout.item_home_product_footer,
                null
            )
        )
    }

    private fun initModuleRecyclerView() {
        recyclerViewHomeModel.layoutManager = GridLayoutManager(activity, 2)
        moduleAdapter.setSpanSizeLookup { gridLayoutManager, position ->
            if (moduleList.size % 2 == 0) {
                return@setSpanSizeLookup 1
            } else if (position == moduleList.size - 1) {
                2
            } else {
                1
            }

        }
        recyclerViewHomeModel.adapter = moduleAdapter
    }

    private fun bindData(data: HomeModel?) {
        data?.let {
            initBanner(it.banner)
            initModule(it.module)
            tvHotProduct.text = it.moduleName
            initProduct(it.product)
        }
    }

    private fun initProduct(product: List<Product>) {
        productList.clear()
        productList.addAll(product)
        productAdapter.notifyDataSetChanged()
    }

    private fun initModule(module: List<HomeModule>) {
        moduleList.clear()
        moduleList.addAll(module)
        moduleAdapter.notifyDataSetChanged()
    }

    private var isInitBanner = false
    private fun initBanner(banners: List<String>) {
        if (banner == null) return
        if (!isInitBanner) {
            isInitBanner = true
            banner.setImages(banners).setDelayTime(4000).setImageLoader(imageLoader).start()
        } else {
            banner.update(banners)
        }
    }

    private val imageLoader = object : ImageLoader() {
        override fun displayImage(context: Context?, path: Any, imageView: ImageView) {
            Glide.with(MyApplication.getInstance()).load(path.toString())
                .into(imageView)
        }
    }
}