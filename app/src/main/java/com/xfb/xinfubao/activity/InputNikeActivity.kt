package com.xfb.xinfubao.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.xfb.xinfubao.R
import kotlinx.android.synthetic.main.activity_input_nike.*

/** 昵称 */
class InputNikeActivity : DefaultActivity() {
    override fun getLayoutId(): Int {
        return R.layout.activity_input_nike
    }

    override fun initView(savedInstanceState: Bundle?) {
        myToolbar.setClick { finish() }

        myToolbar.setRightClickStr("保存") {
            setResult(
                Activity.RESULT_OK,
                Intent().putExtra("data", etInputNikeName.text.toString())
            )
        }

        ivClear.setOnClickListener {
            etInputNikeName.setText("")
        }
    }

}
