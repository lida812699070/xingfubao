package com.xfb.xinfubao.adapter

import android.widget.ImageView
import com.careagle.sdk.utils.PriceChangeUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.xfb.xinfubao.R
import com.xfb.xinfubao.constant.Constant
import com.xfb.xinfubao.model.Product
import com.xfb.xinfubao.utils.loadUri

class ProductAdapter(data: List<Product>) :
    BaseQuickAdapter<Product, BaseViewHolder>(R.layout.item_home_product, data) {
    override fun convert(helper: BaseViewHolder, item: Product) {
        helper.setText(R.id.tvProductName, item.productName)
            .setText(
                R.id.tvProductPrice,
                "${Constant.MONEY_RMB}${PriceChangeUtils.getNumKb(item.productPrice)}"
            )
        val ivProduct = helper.getView<ImageView>(R.id.ivProduct)
        ivProduct.loadUri(item.imgUrl)
    }

}
