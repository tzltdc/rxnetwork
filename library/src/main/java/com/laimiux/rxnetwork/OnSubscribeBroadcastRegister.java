package com.laimiux.rxnetwork;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;

import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;


class OnSubscribeBroadcastRegister implements ObservableOnSubscribe<Intent> {

    private final Context context;
    private final IntentFilter intentFilter;
    private final String broadcastPermission;
    private final Handler schedulerHandler;

    OnSubscribeBroadcastRegister(Context context, IntentFilter intentFilter, String broadcastPermission, Handler schedulerHandler) {
        this.context = context;
        this.intentFilter = intentFilter;
        this.broadcastPermission = broadcastPermission;
        this.schedulerHandler = schedulerHandler;
    }


    @Override
    public void subscribe(final @NonNull ObservableEmitter<Intent> observableEmitter) {
        final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                observableEmitter.onNext(intent);
            }
        };
        observableEmitter.setDisposable(new Disposable() {
            boolean disposed;

            @Override
            public void dispose() {
                this.disposed = true;
                context.unregisterReceiver(broadcastReceiver);
            }

            @Override
            public boolean isDisposed() {
                return disposed;
            }
        });

        context.registerReceiver(broadcastReceiver, intentFilter, broadcastPermission, schedulerHandler);
    }
}