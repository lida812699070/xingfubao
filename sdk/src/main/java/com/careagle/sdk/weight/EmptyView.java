package com.careagle.sdk.weight;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.careagle.sdk.R;


/**
 * Created by lida on 2018/1/10.
 */

public class EmptyView extends ConstraintLayout implements View.OnClickListener {

    private View loadingLayout;
    private View emptyLayout;
    private View errorLayout;
    private ReloadListener reloadListener;
    private FrameLayout fl;
    private TextView tvEmpty;
    private TextView tvEmptyBtn;
    private ImageView ivEmpty;

    public enum LoadState {
        LOAD_STATE_EMPTY,
        LOAD_STATE_ERROR,
        LOAD_STATE_LOADING,
    }

    public EmptyView(Context context) {
        this(context, null);
    }

    public EmptyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        View emptyView = LayoutInflater.from(context).inflate(R.layout.empty_view, this);
        fl = emptyView.findViewById(R.id.fl);
        loadingLayout = emptyView.findViewById(R.id.layout_loading);
        emptyLayout = emptyView.findViewById(R.id.layout_empty);
        errorLayout = emptyView.findViewById(R.id.layout_error);
        tvEmptyBtn = emptyView.findViewById(R.id.tvEmptyBtn);
        tvEmpty = emptyLayout.findViewById(R.id.tv_empty);
        ivEmpty = emptyLayout.findViewById(R.id.iv_empty);
        TextView tvReload = emptyView.findViewById(R.id.tv_reload);
        tvReload.setOnClickListener(this);
    }

    public void setEmptyView(int imageId, String text) {
        ivEmpty.setImageResource(imageId);
        tvEmpty.setText(text);
    }

    public void setEmptyStr(String emptyStr) {
        tvEmpty.setText(emptyStr);
    }

    @Override
    public void onClick(View view) {
        if (reloadListener != null) {
            setLoadState(LoadState.LOAD_STATE_LOADING);
            reloadListener.onReload();
        }
    }

    public void setLoadState(LoadState loadState) {
        switch (loadState) {
            case LOAD_STATE_EMPTY:
                loadingLayout.setVisibility(GONE);
                emptyLayout.setVisibility(VISIBLE);
                errorLayout.setVisibility(GONE);
                break;
            case LOAD_STATE_ERROR:
                loadingLayout.setVisibility(GONE);
                emptyLayout.setVisibility(GONE);
                errorLayout.setVisibility(VISIBLE);
                break;
            case LOAD_STATE_LOADING:
                loadingLayout.setVisibility(VISIBLE);
                emptyLayout.setVisibility(GONE);
                errorLayout.setVisibility(GONE);
                break;
        }
    }

    public TextView getTvEmptyBtn() {
        return tvEmptyBtn;
    }

    public void setReloadListener(ReloadListener reloadListener) {
        this.reloadListener = reloadListener;
    }

    public interface ReloadListener {
        void onReload();
    }
}
