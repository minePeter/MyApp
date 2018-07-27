package com.netease.vopen.net;

import android.util.SparseArray;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.Result;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

public class NetManager {
    private static volatile NetManager ourInstance = null;

    public static NetManager getInstance(String baseUrl) {
        if (ourInstance == null) {
            synchronized (ourInstance){
                if (ourInstance == null) {
                    ourInstance = new NetManager(baseUrl);
                }
            }
        }
        return ourInstance;
    }
    private Retrofit rxJavaRetrofit;
    private SparseArray<Disposable> disposableSparseArray;
    private RetrofitCetHttpService service;
    private OkHttpClient okHttpClient;

    private NetManager(String baseUrl) {
        disposableSparseArray = new SparseArray<>();
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        client.addInterceptor(new Interceptor() {
                                  @Override
                                  public Response intercept(Chain chain) throws IOException {
                                      Request original = chain.request();
                                      Request request = original.newBuilder()
                                              .header("User-Agent", "NetEase")
                                              .header("Accept-Encoding", "gzip")
                                              .method(original.method(), original.body())
                                              .build();

                                      return chain.proceed(request);
                                  }
                              });

        okHttpClient = client.build();
        rxJavaRetrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build();
        service = rxJavaRetrofit.create(RetrofitCetHttpService.class);
    }

    public void requestGet(final int code, String path, HashMap<String, String> params, final NetWorkCallBack callBack) {
        String pathParams = path + "?" + appendParams(params);
        requestGet(code, pathParams, callBack);
    }

    private String appendParams(HashMap<String, String> params) {
        if (params == null) {
            return "";
        }
        StringBuilder resultBuilder = new StringBuilder();
        for (Map.Entry<String, String> param : params.entrySet()) {
            resultBuilder.append(param.getKey()).append("=").append(param.getValue()).append("&");
        }
        int len = resultBuilder.length();
        if (len > 0) {
            resultBuilder = resultBuilder.deleteCharAt(len - 1);
        }
        return resultBuilder.toString();
    }

    public void cancel(int code) {
        Disposable disposable = disposableSparseArray.get(code);
        if (disposable != null) {
            disposableSparseArray.delete(code);
            if (!disposable.isDisposed()) {
                disposable.dispose();
            }
        }
    }

    public void requestGet(final int code, String path, final NetWorkCallBack callBack) {
        service.Obget(path)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Result<ResponseBody>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposableSparseArray.put(code, d);
                    }

                    @Override
                    public void onNext(Result<ResponseBody> responseBodyResult) {
                        if (callBack != null) {
                            try {
                                callBack.onResponse(code, responseBodyResult.response().body().string());
                            } catch (Exception e) {
                                callBack.onResponse(code,"error");
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        callBack.onResponse(code, "error");
                    }

                    @Override
                    public void onComplete() {
                        disposableSparseArray.delete(code);
                    }
                });
    }
}
