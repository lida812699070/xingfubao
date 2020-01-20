package com.careagle.sdk.base.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;

import com.careagle.sdk.R;
import com.careagle.sdk.base.IBaseView;
import com.careagle.sdk.callback.MyDialogCallback;
import com.careagle.sdk.utils.MyToast;
import com.careagle.sdk.weight.CustomProgress;

/**
 * Created by Horrarndoo on 2017/9/26.
 * <p>
 */

public abstract class BaseCompatFragment extends Fragment implements IBaseView {

    protected String TAG;
    protected Context mContext;
    protected Activity mActivity;
    private AlertDialog okDialog;
    private CustomProgress progressDialog;
    protected Dialog customDialog;
    protected boolean isPrepared;
    protected boolean isLazyLoaded;
    public View rootView = null;

    @Override
    public void onAttach(Context context) {
        mActivity = (Activity) context;
        mContext = context;
        super.onAttach(context);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isPrepared = true;
        lazyLoad();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        lazyLoad();
    }

    private void lazyLoad() {
        if (getUserVisibleHint() && isPrepared && !isLazyLoaded) {
            onLazyLoad();
            isLazyLoaded = true;
        }
    }

    public void onLazyLoad() {
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        if (getLayoutView() != null) {
            return getLayoutView();
        } else {
            if (rootView == null)
                rootView = getActivity().getLayoutInflater().inflate(getLayoutId(), container, false);
            return rootView;
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TAG = getClass().getSimpleName();
        getBundle(getArguments());
        initUI(view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (okDialog != null && okDialog.isShowing()) {
            okDialog.dismiss();
        }
        okDialog = null;

        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        progressDialog = null;
        if (customDialog != null && customDialog.isShowing()) {
            customDialog.dismiss();
        }
        customDialog = null;


    }

    @Override
    public void onResume() {
        super.onResume();
//        getActivity().getWindow().getDecorView().setSystemUiVisibility(4102);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @LayoutRes
    protected abstract int getLayoutId();

    public View getLayoutView() {
        return null;
    }

    /**
     * 得到Activity传进来的值
     */
    public void getBundle(Bundle bundle) {
    }

    /**
     * 初始化UI
     */
    public abstract void initUI(View view, @Nullable Bundle savedInstanceState);


    public void showMyDialog(String msg, final MyDialogCallback callback) {
        if (okDialog == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("提示");
            builder.setMessage(msg);
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    callback.onClickOk(dialogInterface, i);
                }
            });
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            okDialog = builder.create();
        }
        okDialog.show();
    }

    public void showMessage(String str) {
        if (!TextUtils.isEmpty(str)) {
            MyToast.toast(str);
        }
    }

    protected void showProgress(int messageResId) {
        showProgress(getString(messageResId));
    }

    protected void showProgress(String message, boolean b) {
        hideProgress();
        progressDialog = CustomProgress.show(getActivity(), message, b, null);
    }

    public void showProgress(String message) {
        showProgress(message, false);
    }

    public void hideProgress() {
        if (progressDialog != null) {
            try {
                progressDialog.dismiss();
                progressDialog = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void startActivity(Intent intent, @Nullable Bundle options) {
        super.startActivity(intent, options);
        getActivity().overridePendingTransition(R.anim.activity_start_zoom_in, R.anim
                .activity_start_zoom_out);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        getActivity().overridePendingTransition(R.anim.activity_start_zoom_in, R.anim
                .activity_start_zoom_out);
    }

    public void setHideInput(final EditText... ets) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            for (EditText et : ets) {
                et.setShowSoftInputOnFocus(false);
            }
        }
    }

    protected void setFocusForView(View view) {
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.requestFocus();//不知道为什么  执行两次这个方法才可以获取焦点
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param alpha
     */
    public void backgroundAlpha(float alpha) {
        if (getActivity().isFinishing())
            return;
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = alpha;
        if (alpha == 1) {
            //不移除该Flag的话,在有视频的页面上的视频会出现黑屏的bug
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        } else {
            //此行代码主要是解决在华为手机上半透明效果无效的bug
            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        }
        getActivity().getWindow().setAttributes(lp);
    }

    public void refresh() {
    }

    public boolean isLazy() {
        return false;
    }

    public View findViewById(int resId) {
        return rootView.findViewById(resId);
    }

}
