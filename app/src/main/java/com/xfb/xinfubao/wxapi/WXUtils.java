package com.xfb.xinfubao.wxapi;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.careagle.sdk.utils.MyToast;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.xfb.xinfubao.MyApplication;
import com.xfb.xinfubao.callback.ShareCallback;
import com.xfb.xinfubao.model.WechatPaymentModel;
import com.xfb.xinfubao.wxapi.listener.WxAuthListener;
import com.xfb.xinfubao.wxapi.listener.WxPayListener;

import java.io.ByteArrayOutputStream;

public class WXUtils {
    //微信API
    private IWXAPI iwxapi = null;
    //微信APPID
    public static String WX_APP_ID = "wx690e0544bdec7cdb";
    //微信secret
//    private String APP_SECRET = "12ac158e4a327b7186b8690d564a3b76";
    //微信商品分享回调
    private ShareCallback shareListener;
    //微信支付回调
    private WxPayListener wxPayListener;
    //是否微信登陆，否绑定微信
    private boolean isLogin;
    //邀请码
    private String parentInvitationCode;
    //用户id
    private String userId;

    private Context context;

    @SuppressLint("StaticFieldLeak")
    private static WXUtils wxUtils;

    public static WXUtils getInstance() {
        if (wxUtils == null) wxUtils = new WXUtils();
        wxUtils.getIWXAPI();
        return wxUtils;
    }

    public WXUtils setShareListener(ShareCallback shareListener) {
        this.shareListener = shareListener;
        return wxUtils;
    }

    /**
     * 获取WX API
     *
     * @return iwxapi
     */
    public IWXAPI getIWXAPI() {
        return getIWXAPI(false);
    }

    private IWXAPI getIWXAPI(boolean isShare) {
        String appId;
        appId = WX_APP_ID;
        iwxapi = WXAPIFactory.createWXAPI(MyApplication.getInstance(), appId);
        iwxapi.registerApp(appId);
        return iwxapi;
    }

    /**
     * 支付
     *
     * @param context       context
     * @param model         支付数据
     * @param wxPayListener 监听
     */
    public void wechatPayment(Context context, WechatPaymentModel model, WxPayListener wxPayListener) {
        if (model == null) {
            MyToast.toast("微信支付失败，支付请求体参数丢失！");
            return;
        }
        this.wxPayListener = wxPayListener;
        PayReq request = new PayReq();
        request.appId = WX_APP_ID;
        request.partnerId = model.getPartnerId();
        request.prepayId = model.getPrepayId();
        request.packageValue = model.getPackageValue();
        request.nonceStr = model.getNonceStr();
        request.timeStamp = model.getTimeStamp();
        request.sign = model.getSign();
        getIWXAPI().sendReq(request);
    }


    private String buildTransaction() {
        return "miniProgram" + System.currentTimeMillis();
    }

    /**
     * 微信会话分享图片
     *
     * @param path 图片本地路径
     */
    public void shareWX(String path) {
        shareImage(SendMessageToWX.Req.WXSceneSession, path);
    }

    /**
     * 微信会话分享链接
     *
     * @param context     context
     * @param imageUrl    imageUrl
     * @param title       title
     * @param description description
     */
    public void shareWXUrl(Context context, String url, byte[] imageUrl, String title, String description) {
        shareUrl(context, SendMessageToWX.Req.WXSceneSession, url, imageUrl, title, description);
    }

    public void shareWXUrl(Context context, String url, Bitmap imageUrl, String title, String description) {
        shareUrl(context, SendMessageToWX.Req.WXSceneSession, url, imageUrl, title, description);
    }

    public void shareWXUrlCircle(Context context, String url, Bitmap imageUrl, String title, String description) {
        shareUrl(context, SendMessageToWX.Req.WXSceneTimeline, url, imageUrl, title, description);
    }

    /**
     * 微信朋友圈分享图片
     *
     * @param path 图片本地路径
     */
    public void shareWxPyq(String path) {
        shareImage(SendMessageToWX.Req.WXSceneTimeline, path);
    }


    /**
     * 微信朋友圈分享链接
     *
     * @param context     context
     * @param imageUrl    imageUrl
     * @param title       title
     * @param description description
     */
    public void shareWxPyqUrl(Context context, String url, byte[] imageUrl, String title, String description) {
        shareUrl(context, SendMessageToWX.Req.WXSceneTimeline, url, imageUrl, title, description);
    }

    /**
     * 微信分享
     *
     * @param path 图片本地路径
     */
    private void shareImage(int scene, String path) {
        WXImageObject imgObj = new WXImageObject();
        imgObj.setImagePath(path);

        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = imgObj;
        msg.title = "杏福宝";

        Bitmap bmp = BitmapFactory.decodeFile(path);
        byte[] Thumbnail = getShareThumbnail(bmp);
        if (Thumbnail == null) {
            if (bmp != null) {
                Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, 150, 150, true);
                bmp.recycle();
                msg.thumbData = bmpToByteArray(thumbBmp, true);
            }
        } else {
            msg.thumbData = Thumbnail;
        }

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = "img" + System.currentTimeMillis();
        req.message = msg;
        req.scene = scene;
        getIWXAPI(true).sendReq(req);
    }

    /**
     * 微信分享链接
     *
     * @param context     context
     * @param scene       分享朋友圈 还是微信
     * @param url         url
     * @param title       title
     * @param description description
     */
    private void shareUrl(Context context, final int scene, String url, byte[] image, String title, String description) {
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = url;
        final WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = title;
        msg.description = description;
        msg.thumbData = image;
        shareUrl(msg, scene);
    }

    private void shareUrl(Context context, final int scene, String url, Bitmap image, String title, String description) {
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = url;
        final WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = title;
        msg.description = description;
        msg.setThumbImage(image);
        shareUrl(msg, scene);
    }

    /**
     * @param msg   msg
     * @param scene 分享朋友圈 还是微信
     */
    private void shareUrl(WXMediaMessage msg, int scene) {
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = "webpage" + System.currentTimeMillis();
        req.message = msg;
        req.scene = scene;
        getIWXAPI(true).sendReq(req);
    }

    /**
     * 获取微信分享缩略图
     *
     * @return result
     */
    private byte[] getShareThumbnail(final Bitmap bmp) {
        if (bmp == null) return null;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        //微信分享缩略图大小
        int IMAGE_SIZE = 32768;
        int quality = 100;
        while (output.toByteArray().length > IMAGE_SIZE && quality != 10) {
            //循环压缩图片直到，小于微信限制最大值
            bmp.compress(Bitmap.CompressFormat.JPEG, quality, output);
            bmp.recycle();
            quality = -10;
        }
        if (output.toByteArray().length > IMAGE_SIZE) {
            return null;
        }
        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     * 微信授权回掉监听
     */
    WxAuthListener wxAuthListener = new WxAuthListener() {
        @Override
        public void result(BaseResp baseResp, boolean isPay) {
            if (isPay) {
                wxPayListener.result(baseResp);
            } else {
                //判断是否是分享回调
                if (baseResp instanceof SendMessageToWX.Resp && baseResp.errCode == BaseResp.ErrCode.ERR_OK) {
                    if (shareListener != null) {
                        shareListener.onSuccess();
                        shareListener = null;
                    }
                }
            }
        }
    };

    /**
     * bitmap -> byte
     *
     * @param bmp
     * @param needRecycle
     * @return
     */
    public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 90, output);
        if (needRecycle) {
            bmp.recycle();
        }

        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public static byte[] resourceToByte(int resourceId, Context context) {
        Drawable drawable = context.getResources().getDrawable(resourceId);
        BitmapDrawable bg = (BitmapDrawable) drawable;
        Bitmap bmm = bg.getBitmap();
        return bmpToByteArray(bmm, true);
    }
}
