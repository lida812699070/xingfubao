package com.xfb.xinfubao.fragment

import android.os.Bundle
import android.os.Handler
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.xfb.xinfubao.R
import com.xfb.xinfubao.model.Product

/** 搜索 */
class SearchFragment : BaseRecyclerViewFragment<Product>() {
    var productAdapter =
        object :
            BaseQuickAdapter<Product, BaseViewHolder>(R.layout.item_home_product, list) {
            override fun convert(helper: BaseViewHolder, item: Product) {
//                helper.setText(R.id.tvProductName, item.productName)
//                    .setText(
//                        R.id.tvProductPrice,
//                        "${Constant.MONEY_RMB}${PriceChangeUtils.getNumKb(item.productPrice)}"
//                    )
//                val ivProduct = helper.getView<ImageView>(R.id.ivProduct)
//                ivProduct.loadUri(item.imgUrl)
            }
        }

    override fun pageRecyclerView(): RecyclerView? {
        return findViewById(R.id.recyclerView) as RecyclerView?
    }

    override fun pageAdapter(): BaseQuickAdapter<Product, BaseViewHolder>? {
        return productAdapter
    }

    override fun pageSwipeRefreshLayout(): SwipeRefreshLayout? {
        return findViewById(R.id.swipeRefreshLayout) as SwipeRefreshLayout?
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_search
    }

    override fun initUI(view: View?, savedInstanceState: Bundle?) {

    }

    override fun initData() {
        Handler().postDelayed({
            val data = listOf(Product(), Product(), Product(), Product(), Product())
            loadData(data)
        }, 1000)
    }

    override fun initLogic() {
        initData()
    }

    override fun pageLayoutManager(): RecyclerView.LayoutManager {
        return GridLayoutManager(activity, 2)
    }

}