package com.xfb.xinfubao.view

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.view.LayoutInflater
import com.xfb.xinfubao.R
import com.xfb.xinfubao.model.ReceiveVo
import com.xfb.xinfubao.utils.setVisible
import kotlinx.android.synthetic.main.view_address_take.view.*

class AddressTakeView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    init {
        LayoutInflater.from(context).inflate(R.layout.view_address_take, this)
    }

    fun bindData(data: ReceiveVo?) {
        tvSelectAddress.setVisible(data == null)
        clAddressValue.setVisible(data != null)
        data?.let {
            tvAddressName.text = data.consignee
            tvAddressValue.text = data.address
            tvAddressMobile.text = data.phone
        }
    }
}