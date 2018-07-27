package com.netease.vopen.net;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.adapter.rxjava2.Result;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface RetrofitCetHttpService {
    @GET()
    Observable<Result<ResponseBody>> Obget(@Url String path);
}
