package com.xfb.xinfubao.view

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.view.LayoutInflater
import com.xfb.xinfubao.R
import com.xfb.xinfubao.utils.setVisible
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
        typedArray.recycle()

        tvTitle.text = title

        ivFinish.setOnClickListener {
            leftClick()
        }
        ivRight.setOnClickListener {
            rightClick()
        }
        tvSubTitle.setOnClickListener {
            rightClick()
        }
    }

    fun setClick(leftClick: () -> Unit) {
        this.leftClick = leftClick
    }

    fun setRightClickStr(subTitle: String?, rightClick: () -> Unit) {
        this.rightClick = rightClick
        tvSubTitle.setVisible(true)
        tvSubTitle.text = "$subTitle"
    }

    fun setRightClickRes(res: Int, rightClick: () -> Unit) {
        ivRight.setVisible(true)
        ivRight.setImageResource(res)
        this.rightClick = rightClick
    }

    fun setTitle(title: String?) {
        tvTitle.text = "$title"
    }

}