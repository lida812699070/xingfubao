package com.xfb.xinfubao.view

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.view.LayoutInflater
import com.xfb.xinfubao.R
import kotlinx.android.synthetic.main.view_my_toolbar.view.*

class MyToolbarBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    var leftClick: () -> Unit = {}
    var rightClick: () -> Unit = {}

    init {
        LayoutInflater.from(context).inflate(R.layout.view_my_toolbar, this)
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.MyToolbarBar)
        var title = typedArray.getString(R.styleable.MyToolbarBar_str_title_toolbar)
        var rightImage = typedArray.getDrawable(R.styleable.MyToolbarBar_right_image_toolbar)
        typedArray.recycle()

        tvTitle.text = title
        ivRight.setImageDrawable(rightImage)

        ivFinish.setOnClickListener {
            leftClick()
        }
        ivRight.setOnClickListener {
            rightClick()
        }
    }


    fun setClick(leftClick: () -> Unit, rightClick: () -> Unit = {}) {
        this.leftClick = leftClick
        this.rightClick = rightClick
    }
}