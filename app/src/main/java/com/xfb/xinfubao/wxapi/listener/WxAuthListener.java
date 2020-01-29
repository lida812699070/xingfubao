package com.xfb.xinfubao.wxapi.listener;

import com.tencent.mm.opensdk.modelbase.BaseResp;

public interface WxAuthListener {
    void result(BaseResp baseResp, boolean isPay);

}
