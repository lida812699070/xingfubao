package com.careagle.sdk.base.dialog;

import android.graphics.Color;

import com.careagle.sdk.R;
import com.careagle.sdk.base.activity.BaseActivity;

public abstract class BaseDialog extends BaseActivity {

    @Override
    protected int rootViewBgColor() {
        return Color.TRANSPARENT;
    }

    @Override
    protected boolean isNeedPaddingTop() {
        return true;
    }

    @Override
    public boolean isTransparent() {
        return true;
    }

    @Override
    public boolean isWhiteStatusBar() {
        return false;
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
    }
}
