package com.careagle.sdk.base.activity;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.careagle.sdk.AppManager;
import com.careagle.sdk.R;
import com.careagle.sdk.base.IBaseView;
import com.careagle.sdk.callback.MyDialogCallback;
import com.careagle.sdk.callback.MyTakePhotoCallBack;
import com.careagle.sdk.callback.PermissionCallBack;
import com.careagle.sdk.utils.MyToast;
import com.careagle.sdk.utils.NavigationBarUtil;
import com.careagle.sdk.utils.RxPermissionsUtil;
import com.careagle.sdk.utils.StateBarUtils;
import com.careagle.sdk.weight.CustomProgress;

import java.lang.ref.WeakReference;
import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by lida on 2018/4/3.
 */

public abstract class BaseActivity extends AppCompatActivity implements IBaseView {

    private CustomProgress progressDialog;
    private AlertDialog selectDialog;
    protected Dialog customerDialog;
    /**
     * 当前Activity的弱引用，防止内存泄露
     **/
    private WeakReference<BaseActivity> context = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //简书类似效果的状态栏
        Window window = getWindow();
        window.getDecorView().findViewById(android.R.id.content).setBackgroundColor(Color.WHITE);
        if (isWhiteStatusBar() && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(Color.TRANSPARENT);
            //白底黑色
            StateBarUtils.statusBarLightMode(this);
        } else if (isWhiteStatusBar() && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window = getWindow();
            WindowManager.LayoutParams localLayoutParams = window.getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        } else {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            setStatusBarColor(Color.TRANSPARENT);
            decorView.setSystemUiVisibility(option);
        }
        if (isNeedPaddingTop()) {
            setPaddingTop();
        }
        setContentView(getLayoutId());
        ButterKnife.bind(this);
        initView(savedInstanceState);
        AppManager.getAppManager().addActivity(this);
    }

    /**
     * 钩子
     * 是否需要给跟布局设置一个paddingtop 防止自己的布局到状态栏
     *
     * @return
     */
    protected boolean isNeedPaddingTop() {
        return false;
    }

    protected int rootViewBgColor() {
        return Color.WHITE;
    }

    private void setPaddingTop() {
        ViewGroup rootView = (ViewGroup) getWindow().getDecorView().findViewById(android.R.id.content);
        rootView.setBackgroundColor(rootViewBgColor());
        rootView.setPadding(0, getStatusBarHeight(), 0, 0);
    }

    protected int getStatusBarHeight() {
        int statusBarHeight1 = -1;
//获取status_bar_height资源的ID
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值
            statusBarHeight1 = getResources().getDimensionPixelSize(resourceId);
        }
        return statusBarHeight1;
    }

    public abstract void initView(Bundle savedInstanceState);

    @Override
    public Resources getResources() {//还原字体大小
        Resources res = super.getResources();
        Configuration configuration = res.getConfiguration();
        if (configuration.fontScale != 1.0f) {
            configuration.fontScale = 1.0f;
            res.updateConfiguration(configuration, res.getDisplayMetrics());
        }
        return res;
    }

    /**
     * 设置状态栏颜色
     *
     * @param colorId
     */
    protected void setStatusBarColor(int colorId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //设置状态栏颜色
            if (!isTransparent()) {
                window.setStatusBarColor(colorId);
            }
        }
    }

    public boolean isTransparent() {
        return false;
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.activity_finish_trans_in, R.anim
                .activity_finish_trans_out);
    }

    @Override
    public void startActivity(Intent intent, @Nullable Bundle options) {
        super.startActivity(intent, options);
        overridePendingTransition(R.anim.activity_start_zoom_in, R.anim
                .activity_start_zoom_out);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        overridePendingTransition(R.anim.activity_start_zoom_in, R.anim
                .activity_start_zoom_out);
    }

    @Override
    public void showMessage(String msg) {
        if (TextUtils.isEmpty(msg)) return;
        MyToast.toast(msg);
    }

    /**
     * @param isNormal 是否是正常状态栏
     *                 true     黑底白色状态栏文字
     *                 false    白底黑色文字
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void changeStatusBarNormal(boolean isNormal) {
        if (isNormal) {
            StateBarUtils.normalLightMode(this);
        } else {
            StateBarUtils.statusBarLightMode(this);
        }
    }

    protected void initToolbar(String title) {
        ImageView ivBack = findViewById(R.id.ivBack);
        TextView tvTitle = findViewById(R.id.tvTitle);
        if (tvTitle != null && ivBack != null) {
            tvTitle.setText(title);
            ivBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
        }
    }

    protected TextView tvSubTitle() {
        return findViewById(R.id.tvSubTitle);
    }

    protected ImageView ivBack() {
        return findViewById(R.id.ivBack);
    }

    protected ImageView ivRight() {
        return findViewById(R.id.ivRight);
    }

    protected TextView tvLeft() {
        return findViewById(R.id.tvLeft);
    }

    protected void initToolbar(String title, String subTitle) {
        initToolbar(title);
        TextView tvSubTitle = findViewById(R.id.tvSubTitle);
        if (tvSubTitle != null) {
            tvSubTitle.setText(subTitle);
            tvSubTitle.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showProgress(String message) {
        showProgress(message, false);
    }

    public void showProgress(String msg, boolean cancelable) {
        hideProgress();
        if (TextUtils.isEmpty(msg)) return;
//        getWindow().getDecorView().setSystemUiVisibility(4102);
        progressDialog = CustomProgress.show(this, msg, cancelable, null);
    }

    @Override
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

    /**
     * 是否设置状态栏为白底黑色的样式
     * 一般和paddingTop配合使用
     *
     * @return
     */
    public boolean isWhiteStatusBar() {
        return true;
    }

    protected abstract int getLayoutId();

    public void showMyDialog(String msg, final MyDialogCallback callback) {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setTitle("提示");
        builder.setMessage(msg);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                callback.onClickOk(dialogInterface, i);
                dialogInterface.dismiss();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        android.support.v7.app.AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void showSelectDialog(final MyTakePhotoCallBack callBack) {
        RxPermissionsUtil.requestPermission(this, new PermissionCallBack() {
                    @Override
                    public void success() {
                        toShowSelectDialog(callBack);
                    }
                }, Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    protected void toShowSelectDialog(final MyTakePhotoCallBack callBack) {
        if (selectDialog == null) {
            View.OnClickListener selectDialogListener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int i = view.getId();
                    if (i == R.id.tv_cancel) {
                        selectDialog.dismiss();

                    } else if (i == R.id.tv_select_photo) {
                        callBack.selectPhoto();
                        selectDialog.dismiss();

                    } else if (i == R.id.tv_take_photo) {
                        callBack.takePhoto();
                        selectDialog.dismiss();

                    }
                }
            };
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_take_photo, null);
            dialogView.findViewById(R.id.tv_take_photo).setOnClickListener(selectDialogListener);
            dialogView.findViewById(R.id.tv_select_photo).setOnClickListener(selectDialogListener);
            dialogView.findViewById(R.id.tv_cancel).setOnClickListener(selectDialogListener);
            builder.setView(dialogView);
            selectDialog = builder.create();
            selectDialog.setCancelable(true);
            selectDialog.show();
            //此处设置位置窗体大小
            Window window = selectDialog.getWindow();
            window.setGravity(Gravity.BOTTOM);
            window.setWindowAnimations(R.style.MyDialogAnim);
            window.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_white_radio_bg));
            WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
            window.setLayout((int) (wm.getDefaultDisplay().getWidth() * 0.9), LinearLayout.LayoutParams.WRAP_CONTENT);
        } else {
            selectDialog.show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.getAppManager().finishActivity(this);
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
            progressDialog = null;
        }
        if (selectDialog != null && selectDialog.isShowing()) {
            selectDialog.dismiss();
            selectDialog = null;
        }
        if (customerDialog != null && customerDialog.isShowing()) {
            customerDialog.dismiss();
            customerDialog = null;
        }
    }

    public void baseDialogAnim() {
        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_down);
    }


    /**
     * 程序是否在前台运行
     *
     * @return
     */
    public boolean isAppOnForeground() {
        // Returns a list of application processes that are running on the
        // device

        ActivityManager activityManager = (ActivityManager) getApplicationContext()
                .getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = getApplicationContext().getPackageName();

        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        if (appProcesses == null)
            return false;

        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            // The name of the process that this object is associated with.
            if (appProcess.processName.equals(packageName)
                    && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }

        return false;
    }


    //是否在前台
    public static boolean inForeground = false;

    @Override
    protected void onResume() {
        super.onResume();
        /**
         * 在这里本来可以使用if (!isAppOnForeground()) {//to do
         * sth}，但为了避免再次调用isAppOnForeground()而造成费时且增大系统的开销，故在这里我应用了一个标志位来判断
         */
        if (!inForeground) {
            onForGround();
            inForeground = true;
        }
//        getWindow().getDecorView().setSystemUiVisibility(4102);
    }

    @Override
    protected void onStop() {
        if (!isAppOnForeground()) {
            onBackGround();
            inForeground = false;
        }
        super.onStop();
    }

    //进入后台回调
    protected void onBackGround() {
    }

    //进入前台回调
    protected void onForGround() {
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param alpha
     */
    public void backgroundAlpha(float alpha) {
        if (isDestroyed())
            return;
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = alpha;
        if (alpha == 1) {
            //不移除该Flag的话,在有视频的页面上的视频会出现黑屏的bug
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        } else {
            //此行代码主要是解决在华为手机上半透明效果无效的bug
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        }
        getWindow().setAttributes(lp);
    }

    public void closeKeyBoards() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
        }
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
        view.requestFocus();
    }

}
