package com.xfb.xinfubao.wxapi.listener;

import com.tencent.mm.opensdk.modelbase.BaseResp;

public interface WxPayListener {
    void result(BaseResp req);
}
