package com.netease.vopen.base;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.support.multidex.MultiDex;

import com.netease.vopen.base.di.components.BaseAppComponent;
import com.netease.vopen.base.di.components.DaggerBaseAppComponent;
import com.netease.vopen.base.di.modules.BaseAppModule;

public class BaseApplication extends Application{

    BaseAppComponent mBaseAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        mBaseAppComponent = DaggerBaseAppComponent.builder().baseAppModule(new BaseAppModule(this)).build();
    }
    public BaseAppComponent getBaseAppComponent(){
        return mBaseAppComponent;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

}
