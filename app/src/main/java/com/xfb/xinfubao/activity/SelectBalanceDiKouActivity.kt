package com.xfb.xinfubao.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.widget.TextView
import com.careagle.sdk.utils.PriceChangeUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.xfb.xinfubao.R
import com.xfb.xinfubao.model.SelectBalanceModel
import kotlinx.android.synthetic.main.activity_select_balance_dikou.*

/** 选择资产抵扣 */
class SelectBalanceDiKouActivity : DefaultActivity() {
    val list = arrayListOf<SelectBalanceModel>()
    var selectBalanceModel: SelectBalanceModel? = null

    companion object {
        fun toActivity(
            context: Activity,
            list: ArrayList<SelectBalanceModel>?,
            selectBalanceModel: SelectBalanceModel?
        ) {
            val intent = Intent(context, SelectBalanceDiKouActivity::class.java)
            intent.putExtra("list", list)
            intent.putExtra("selectBalanceModel", selectBalanceModel)
            context.startActivityForResult(intent, 1)
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_select_balance_dikou
    }

    override fun initView(savedInstanceState: Bundle?) {
        myToolbar.setTitle("选择资产抵扣")
        myToolbar.setClick { finish() }
        selectBalanceModel =
            intent.getSerializableExtra("selectBalanceModel") as SelectBalanceModel?
        val datas = intent.getSerializableExtra("list") as List<SelectBalanceModel>
        list.addAll(datas)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = object : BaseQuickAdapter<SelectBalanceModel, BaseViewHolder>(
            R.layout.item_select_balance_di_kou,
            list
        ) {
            override fun convert(helper: BaseViewHolder, item: SelectBalanceModel) {
                helper.setText(R.id.tvName, item.assetsName)
                    .setText(R.id.tvBalance, "余额：${item.assetsAmount}")
                    .setText(
                        R.id.tvCanDiKou,
                        "可抵扣：${getString(
                            R.string.rmb_tag,
                            PriceChangeUtils.getDoubleKb(item.maxFavorable)
                        )}"
                    )

                val tvCanDiKou = helper.getView<TextView>(R.id.tvCanDiKou)
                tvCanDiKou.isSelected = item.id == selectBalanceModel?.id
            }
        }
        recyclerView.adapter = adapter
        adapter.setOnItemClickListener { adapter, view, position ->
            selectBalanceModel = list[position]
            adapter.notifyDataSetChanged()
        }
        tvOK.setOnClickListener {
            val intent = Intent()
            intent.putExtra("data", selectBalanceModel)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }

}