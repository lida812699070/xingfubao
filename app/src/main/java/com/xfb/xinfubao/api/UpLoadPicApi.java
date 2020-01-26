package com.xfb.xinfubao.api;

import com.xfb.xinfubao.model.Result;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface UpLoadPicApi {
    @Multipart
    @POST("api/uploadImages")
    Observable<Result<String>> uploadImages(@Part MultipartBody.Part part);
}
