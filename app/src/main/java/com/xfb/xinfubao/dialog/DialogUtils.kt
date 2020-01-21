package com.xfb.xinfubao.dialog

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.support.v7.app.AlertDialog
import android.view.Gravity
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.xfb.xinfubao.R


class DialogUtils {

    companion object {
        /** 银杏宝 */
        fun showBalanceDialog(context: Context): AlertDialog {
            val builder = AlertDialog.Builder(context)
            builder.setCancelable(true)
            val view = LayoutInflater.from(context)
                .inflate(R.layout.dialog_balance, null)
            builder.setView(view)
            val changeShopDialog = builder.create()
            changeShopDialog?.show()
            val layoutParams = changeShopDialog?.window?.attributes
            layoutParams?.width = WindowManager.LayoutParams.MATCH_PARENT
            layoutParams?.gravity = Gravity.BOTTOM
            changeShopDialog?.window?.attributes = layoutParams
            changeShopDialog?.window?.setDimAmount(0.4f)
            val dw = ColorDrawable(0x00)
            changeShopDialog?.window?.setBackgroundDrawable(dw)
            view.findViewById<ImageView>(R.id.ivClose).setOnClickListener {
                changeShopDialog.dismiss()
            }
            return changeShopDialog
        }

        /** 抵押 */
        fun showDiYaDialog(context: Context, method: (payPassword: String) -> Unit): AlertDialog? {
            val builder = AlertDialog.Builder(context)
            builder.setCancelable(true)
            val view = LayoutInflater.from(context)
                .inflate(R.layout.dialog_diya, null)
            builder.setView(view)
            val dialog = builder.create()
            dialog?.show()
            val layoutParams = dialog?.window?.attributes
            layoutParams?.width = WindowManager.LayoutParams.MATCH_PARENT
            layoutParams?.gravity = Gravity.BOTTOM
            dialog?.window?.attributes = layoutParams
            dialog?.window?.setDimAmount(0.4f)
            val dw = ColorDrawable(0x00)
            dialog?.window?.setBackgroundDrawable(dw)
            view.findViewById<ImageView>(R.id.ivClose).setOnClickListener {
                dialog?.dismiss()
            }
            val etPayPassword = view.findViewById<EditText>(R.id.etPayPassword)
            view.findViewById<TextView>(R.id.tvOkCashOut).setOnClickListener {
                method(etPayPassword.text.toString())
            }
            return dialog
        }

        /** 照片 */
        fun showTakePicDialog(context: Context,selectPic:()->Unit,takePic:()->Unit) {
            val builder = AlertDialog.Builder(context)
            builder.setCancelable(true)
            val view = LayoutInflater.from(context)
                .inflate(R.layout.dialog_take_pic, null)
            builder.setView(view)
            val dialog = builder.create()
            dialog?.show()
            val layoutParams = dialog?.window?.attributes
            layoutParams?.width = WindowManager.LayoutParams.MATCH_PARENT
            layoutParams?.gravity = Gravity.BOTTOM
            dialog?.window?.attributes = layoutParams
            dialog?.window?.setDimAmount(0.4f)
            val dw = ColorDrawable(0x00)
            view.findViewById<TextView>(R.id.tvCancel).setOnClickListener {
                dialog?.dismiss()
            }
            view.findViewById<TextView>(R.id.tvSelectPic).setOnClickListener {
                selectPic()
                dialog?.dismiss()
            }
            view.findViewById<TextView>(R.id.tvTakePic).setOnClickListener {
                takePic()
                dialog?.dismiss()
            }
            dialog?.window?.setBackgroundDrawable(dw)

        }
    }
}