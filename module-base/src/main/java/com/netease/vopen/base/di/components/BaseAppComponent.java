package com.netease.vopen.base.di.components;

import android.content.Context;

import com.netease.vopen.base.di.modules.BaseAppModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules={BaseAppModule.class})
public interface BaseAppComponent {
    Context getContext();
}
