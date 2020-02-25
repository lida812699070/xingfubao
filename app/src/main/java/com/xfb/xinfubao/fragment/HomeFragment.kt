package com.xfb.xinfubao.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import com.careagle.sdk.helper.RetrofitCreateHelper
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.xfb.xinfubao.MyImageLoader
import com.xfb.xinfubao.R
import com.xfb.xinfubao.activity.*
import com.xfb.xinfubao.adapter.ProductAdapter
import com.xfb.xinfubao.api.BaseApi
import com.xfb.xinfubao.model.BannerModel
import com.xfb.xinfubao.model.HomeModel
import com.xfb.xinfubao.model.HomeModule
import com.xfb.xinfubao.model.Product
import com.xfb.xinfubao.utils.loadRound
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

    var productAdapter = ProductAdapter(productList)

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
        tvHomeUserCenter.setOnClickListener {
            activity?.let {
                startActivity(Intent(it, UserInfoActivity::class.java))
            }
        }
        tvHomeOrder.setOnClickListener {
            activity?.let {
                startActivity(Intent(it, MyOrderActivity::class.java))
            }
        }
        tvHomeCategory.setOnClickListener {
            activity?.let {
                startActivity(Intent(it, CategoryActivity::class.java))
            }
        }
        tvHomeCat.setOnClickListener {
            activity?.let {
                startActivity(Intent(it, CartActivity::class.java))
            }
        }

        tvHomeMessage.setOnClickListener {
            activity?.let {
                startActivity(Intent(it, MyMessageActivity::class.java))
            }
        }

        tvHomeMore.setOnClickListener {
            activity?.let {
                startActivity(Intent(it, MyMessageActivity::class.java))
            }
        }
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
        productAdapter.setOnItemClickListener { adapter, view, position ->
            startActivity(
                Intent(activity, ProductDetailActivity::class.java)
                    .putExtra("productId", "${productList[position].productId}")
            )
        }
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

        moduleAdapter.setOnItemClickListener { adapter, view, position ->
            startActivity(
                Intent(activity, ProductDetailActivity::class.java)
                    .putExtra("productId", moduleList[position].productId)
            )
        }
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

    private fun initBanner(banners: List<BannerModel>) {
        if (banner == null) return
        banner.setImages(banners).setDelayTime(4000).setImageLoader(MyImageLoader()).start()
    }

}