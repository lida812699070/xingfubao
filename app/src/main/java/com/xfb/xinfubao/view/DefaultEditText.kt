package com.xfb.xinfubao.view

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.text.InputFilter
import android.text.InputType
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.view.LayoutInflater
import com.xfb.xinfubao.R
import kotlinx.android.synthetic.main.view_default_edittext.view.*


class DefaultEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    private var maxlength = 20
    //0 text    1 password    2 number
    private var inputType = 0
    private var hint: String? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.view_default_edittext, this)
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.DefaultEditText)
        maxlength = typedArray.getInt(R.styleable.DefaultEditText_edit_input_max_length, 20)
        hint = typedArray.getString(R.styleable.DefaultEditText_edit_input_hint)
        inputType = typedArray.getInt(R.styleable.DefaultEditText_edit_input_type, 0)
        typedArray.recycle()
        initView()
    }

    private fun initView() {
        etInput.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(maxlength)) //最大输入长度
        if (inputType == 0) {
        } else if (inputType == 1) {
            etInput.transformationMethod = PasswordTransformationMethod.getInstance() //设置为密码输入框
        } else if (inputType == 2) {
            etInput.inputType = InputType.TYPE_CLASS_NUMBER //输入类型
        }
        etInput.hint = hint
    }

}