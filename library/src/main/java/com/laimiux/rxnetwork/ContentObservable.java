package com.laimiux.rxnetwork;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import io.reactivex.Observable;


final class ContentObservable {
    private ContentObservable() {
        throw new AssertionError("No instances");
    }

    public static Observable<Intent> fromBroadcast(Context context, IntentFilter filter) {
        return Observable.create(new OnSubscribeBroadcastRegister(context, filter, null, null));
    }
}