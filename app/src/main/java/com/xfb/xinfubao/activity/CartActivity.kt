package com.xfb.xinfubao.activity

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.RecyclerView
import android.widget.ImageView
import android.widget.TextView
import com.careagle.sdk.helper.RetrofitCreateHelper
import com.careagle.sdk.utils.PriceChangeUtils
import com.careagle.sdk.weight.EmptyView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.xfb.xinfubao.R
import com.xfb.xinfubao.api.BaseApi
import com.xfb.xinfubao.model.Product
import com.xfb.xinfubao.utils.ConfigUtils
import com.xfb.xinfubao.utils.loadUri
import kotlinx.android.synthetic.main.activity_cart.*

/** 购物车 */
class CartActivity : BaseRecyclerViewActivity<Product>() {
    val itemSelectList = arrayListOf<Product>()

    val adapter = object : BaseQuickAdapter<Product, BaseViewHolder>(R.layout.item_cart, list) {
        override fun convert(helper: BaseViewHolder, item: Product) {
            val ivImages = helper.getView<ImageView>(R.id.ivImages)
            val ivCheckBox = helper.getView<ImageView>(R.id.ivCheckBox)
            val tvCount = helper.getView<TextView>(R.id.tvCount)
            val tvTitle = helper.getView<TextView>(R.id.tvTitle)
            val tvPrice = helper.getView<TextView>(R.id.tvPrice)
            ivImages.loadUri(item.productMainImg)
            tvPrice.text = mContext.getString(
                R.string.rmb_tag,
                PriceChangeUtils.getNumKb(item.productPrice)
            )
            ivCheckBox.isSelected = itemSelectList.contains(item)
            tvTitle.text = item.productName
            tvCount.text = "${item.quantity}"
            helper.addOnClickListener(R.id.btnDelete)
                .addOnClickListener(R.id.ivAdd)
                .addOnClickListener(R.id.ivJian)
                .addOnClickListener(R.id.ivCheckBox)
        }
    }

    override fun pageRecyclerView(): RecyclerView {
        return recyclerView
    }

    override fun initView(savedInstanceState: Bundle?) {

    }

    override fun pageAdapter(): BaseQuickAdapter<Product, BaseViewHolder> {
        return adapter
    }

    override fun pageSwipeRefreshLayout(): SwipeRefreshLayout? {
        return swipeRefreshLayout
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_cart
    }

    override fun initLogic() {
        myToolbar.setClick { finish() }
        adapter.setOnItemChildClickListener { adapter, view, position ->
            when (view.id) {
                R.id.btnDelete -> {
                    val map = hashMapOf<String, String>()
                    map["userId"] = "${ConfigUtils.userId()}"
                    map["cartId"] = list[position].cartId
                    showProgress("请稍候")
                    request(RetrofitCreateHelper.createApi(BaseApi::class.java).deleteCart(map)) {
                        deleteItem(position)
                    }
                }
                R.id.ivAdd -> {
                    showProgress("请稍候")
                    val map = hashMapOf<String, String>()
                    map["userId"] = "${ConfigUtils.getUserInfo()?.userId}"
                    map["productId"] = "${list[position].productId}"
                    map["quantity"] = "1"
                    request(RetrofitCreateHelper.createApi(BaseApi::class.java).addCart(map)) {
                        list[position].quantity += 1
                        adapter.notifyDataSetChanged()
                        countTotalPrice()
                    }
                }
                R.id.ivJian -> {
                    if (list[position].quantity <= 1) {
                        return@setOnItemChildClickListener
                    }
                    showProgress("请稍候")
                    val map = hashMapOf<String, String>()
                    map["userId"] = "${ConfigUtils.getUserInfo()?.userId}"
                    map["productId"] = "${list[position].productId}"
                    map["quantity"] = "-1"
                    request(RetrofitCreateHelper.createApi(BaseApi::class.java).addCart(map)) {
                        list[position].quantity -= 1
                        adapter.notifyDataSetChanged()
                        countTotalPrice()
                    }
                }
                R.id.ivCheckBox -> {
                    if (view.isSelected) {
                        itemSelectList.remove(list[position])
                    } else {
                        itemSelectList.add(list[position])
                    }
                    adapter.notifyDataSetChanged()
                    countTotalPrice()
                }
            }
        }

        tvAllSelect.setOnClickListener {
            tvAllSelect.isSelected = !tvAllSelect.isSelected
            if (tvAllSelect.isSelected) {
                itemSelectList.clear()
                list.forEach {
                    itemSelectList.add(it)
                }
            } else {
                itemSelectList.clear()
            }
            adapter.notifyDataSetChanged()
            countTotalPrice()
        }
        tvToOrder.setOnClickListener {
            var size = 0
            itemSelectList.forEach {
                size += it.quantity
                it.num = it.quantity
            }
            if (size == 0) {
                showMessage("请选择要购买的商品")
                return@setOnClickListener
            }
            ConfirmOrderActivity.toActivity(this, itemSelectList)
        }
        initData()
    }

    private fun deleteItem(position: Int) {
        val pos = list[position]
        itemSelectList.remove(pos)
        list.remove(pos)//集合移除该条
        adapter.notifyItemRemoved(position)//通知移除该条
        adapter.notifyItemRangeChanged(position, list.size - position)//更新适配器这条后面列表的变化
        if (list.isEmpty()) {
            emptyView?.setLoadState(EmptyView.LoadState.LOAD_STATE_EMPTY)
        }
    }

    override fun initData() {
        val map = hashMapOf<String, String>()
        map["userId"] = "${ConfigUtils.userId()}"
        map["pageNum"] = "$page"
        map["pageSize"] = "$pageSize"
        requestWithError(RetrofitCreateHelper.createApi(BaseApi::class.java).listUserCart(map), {
            loadData(it.data)
            countTotalPrice()
        }) {
            showLoadError()
        }
    }

    fun countTotalPrice() {
        var totalPrice = 0.0
        for (product in itemSelectList) {
            totalPrice += product.productPrice * product.quantity
        }
        tvTotalPrice.text = getString(R.string.rmb_tag, PriceChangeUtils.getNumKb(totalPrice))
    }
}
