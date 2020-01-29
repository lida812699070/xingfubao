package com.xfb.xinfubao.activity

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import com.careagle.sdk.helper.RetrofitCreateHelper
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.xfb.xinfubao.R
import com.xfb.xinfubao.adapter.ProductAdapter
import com.xfb.xinfubao.api.BaseApi
import com.xfb.xinfubao.model.Product
import com.xfb.xinfubao.model.ProductType
import com.xfb.xinfubao.utils.setVisible
import kotlinx.android.synthetic.main.activity_category.*

/** 搜索 分类 */
class CategoryActivity : BaseRecyclerViewActivity<Product>() {
    val adapter = ProductAdapter(list)
    var productType: ProductType? = null
    var types = arrayListOf<ProductType>()
    var key: String? = null
    override fun pageRecyclerView(): RecyclerView {
        return recyclerView
    }

    override fun pageAdapter(): BaseQuickAdapter<Product, BaseViewHolder> {
        return adapter
    }

    override fun pageSwipeRefreshLayout(): SwipeRefreshLayout? {
        return swipeRefreshLayout
    }


    override fun getLayoutId(): Int {
        return R.layout.activity_category
    }

    override fun initView(savedInstanceState: Bundle?) {

    }

    override fun pageLayoutManager(): RecyclerView.LayoutManager {
        return GridLayoutManager(this, 2)
    }

    override fun initLogic() {
        request(RetrofitCreateHelper.createApi(BaseApi::class.java).findProductType(hashMapOf())) {
            initTabs(it.data)
        }
        tvDefaultFilter.isSelected = true
        adapter.setOnItemClickListener { adapter, view, position ->
            val putExtra = Intent(this, ProductDetailActivity::class.java)
                .putExtra("productId", "${list[position].productId}")
            startActivity(putExtra)
        }
    }


    override fun initData() {
        val map = hashMapOf<String, String>()
        if (!TextUtils.isEmpty(key)) {
            map["key"] = key!!
        }
        map["pageNum"] = "$page"
        map["pageSize"] = "$pageSize"
        if (TextUtils.isEmpty(key)) {
            map["category"] = "${productType?.typeId}"
        }
        var sortType = 0
        if (tvDefaultFilter.isSelected) {
            sortType = 0
        } else if (tvSellCountFilter.isSelected) {
            sortType = 1
        } else if (tvPriceFilter.isSelected) {
            sortType = 2
        }
        map["sortColumn"] = "$sortType"
        map["sortType"] = "$sortType"
        showProgress("请稍候")
        requestWithError(
            RetrofitCreateHelper.createApi(BaseApi::class.java).findProductByCategory(
                map
            ), {
                loadData(it.data)
            }) {
            showLoadError()
        }
    }

    private fun initTabs(data: List<ProductType>) {
        types.clear()
        types.addAll(data)
        data.forEach {
            tabLayout.addTab(tabLayout.newTab().setText(it.typeName))
        }
        productType = data.first()
        initData()

        initListener()
    }

    private fun initListener() {
        tvDefaultFilter.setOnClickListener {
            tvDefaultFilter.isSelected = true
            tvPriceFilter.isSelected = false
            tvSellCountFilter.isSelected = false
            onRefresh()
        }
        tvPriceFilter.setOnClickListener {
            tvDefaultFilter.isSelected = false
            tvPriceFilter.isSelected = true
            tvSellCountFilter.isSelected = false
            onRefresh()
        }
        tvSellCountFilter.setOnClickListener {
            tvDefaultFilter.isSelected = false
            tvPriceFilter.isSelected = false
            tvSellCountFilter.isSelected = true
            onRefresh()
        }

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(p0: TabLayout.Tab?) {

            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {

            }

            override fun onTabSelected(p0: TabLayout.Tab?) {
                for (i in 0..types.size) {
                    if (p0?.text == types[i].typeName) {
                        productType = types[i]
                        break
                    }
                }
                onRefresh()
            }
        })

        etSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                ivSearchClear.setVisible(!TextUtils.isEmpty(getSearchStr()))
                if (TextUtils.isEmpty(getSearchStr())) {
                    tvSearch.text = "搜索"
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

        })
        ivSearchClear.setOnClickListener {
            etSearch.setText("")
        }

        tvSearch.setOnClickListener {
            if (tvSearch.text == "搜索") {
                if (!TextUtils.isEmpty(getSearchStr())) {
                    key = getSearchStr()
                    tabLayout.setVisible(false)
                    tvSearch.text = "取消"
                    onRefresh()
                }
            } else if (tvSearch.text == "取消") {
                finish()
//                key = null
//                tabLayout.setVisible(true)
//                tvSearch.text = "搜索"
//                etSearch.setText("")
//                onRefresh()
            }
        }
    }

    private fun getSearchStr(): String {
        return etSearch.text.toString()
    }

}
