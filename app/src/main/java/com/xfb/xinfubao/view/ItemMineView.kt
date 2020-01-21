package com.xfb.xinfubao.view

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.view.LayoutInflater
import com.xfb.xinfubao.R
import kotlinx.android.synthetic.main.item_mine_bottom.view.*

class ItemMineView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    var title: String? = null
    var leftImage: Int? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.item_mine_bottom, this)
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ItemMineView)
        title = typedArray.getString(R.styleable.ItemMineView_item_mine_title)
        leftImage = typedArray.getResourceId(
            R.styleable.ItemMineView_item_mine_left_img,
            R.mipmap.jjh_icon
        )
        typedArray.recycle()

        tvTitle.text = title
        ivIcon.setImageResource(leftImage!!)
    }
}