package com.laimiux.rxnetwork;


import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import io.reactivex.Observable;


public class RxNetwork {
    private RxNetwork() {
    }

    private static NetworkInfo getNetworkInfo(Context context){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm != null ? cm.getActiveNetworkInfo() : null;
    }

    private static Status getNetworkType(Context context) {
        NetworkInfo networkInfo = getNetworkInfo(context);
        if (networkInfo != null && networkInfo.isConnected()) {
            String type = networkInfo.getTypeName();
            if (type.equalsIgnoreCase("MOBILE")) {
                return Status.MOBILE;
            } else {
                return Status.WIFI;
            }
        } else {
            return Status.ABSENT;
        }
    }

  public static Observable<Boolean> stream(Context context) {
    final Context applicationContext = context.getApplicationContext();
    final IntentFilter action = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
    return ContentObservable.fromBroadcast(context, action)
        .map(intent -> getNetworkType(applicationContext))
        .distinctUntilChanged()
        .map(status -> status != Status.ABSENT);
  }

    public static Observable<Status> streamDetail(Context context) {
        final Context applicationContext = context.getApplicationContext();
        final IntentFilter action = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        return ContentObservable.fromBroadcast(context, action)
            .map(intent -> getNetworkType(applicationContext))
            .distinctUntilChanged()
            .startWith(getNetworkType(applicationContext));
    }
}
