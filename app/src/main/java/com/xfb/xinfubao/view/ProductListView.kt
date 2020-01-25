package com.xfb.xinfubao.view

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import com.careagle.sdk.utils.PriceChangeUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.xfb.xinfubao.R
import com.xfb.xinfubao.model.Product
import com.xfb.xinfubao.utils.loadUri
import kotlinx.android.synthetic.main.view_product_list.view.*

class ProductListView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    val list = arrayListOf<Product>()
    var adapter: BaseQuickAdapter<Product, BaseViewHolder>? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.view_product_list, this)
        itemRecyclerView.layoutManager = LinearLayoutManager(context)
        adapter =
            object :
                BaseQuickAdapter<Product, BaseViewHolder>(
                    R.layout.item_my_order_product_item,
                    list
                ) {
                override fun convert(helper: BaseViewHolder, item: Product) {
                    helper.setText(R.id.tvTitle, item.productName)
                        .setText(
                            R.id.tvPrice,
                            mContext.getString(
                                R.string.rmb_tag,
                                PriceChangeUtils.getNumKb(item.productPrice)
                            )
                        )
                        .setText(R.id.tvNumber, "x${item.num}")
                    val ivProductPic = helper.getView<ImageView>(R.id.ivProductPic)
                    ivProductPic.loadUri(item.imgUrl)
                }
            }
        itemRecyclerView.adapter = adapter
    }

    fun bindData(datas: List<Product>) {
        list.clear()
        list.addAll(datas)
        adapter?.notifyDataSetChanged()
    }
}