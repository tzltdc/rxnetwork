package com.laimiux.rxnetwork;


import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import io.reactivex.Observable;


public class RxNetwork {
    private RxNetwork() {
    }


    private static boolean getConnectivityStatus(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm != null ? cm.getActiveNetworkInfo() : null;
        return null != activeNetwork && activeNetwork.isConnected();

    }

    public static Observable<Boolean> stream(Context context) {
        final Context applicationContext = context.getApplicationContext();
        final IntentFilter action = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        return ContentObservable.fromBroadcast(context, action)
            .map(intent -> getConnectivityStatus(applicationContext)).distinctUntilChanged()
            .startWith(getConnectivityStatus(applicationContext));
    }
}
