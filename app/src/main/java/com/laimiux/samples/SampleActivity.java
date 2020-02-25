package com.laimiux.samples;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;

import com.laimiux.rxnetwork.RxNetwork;

import com.laimiux.rxnetwork.Status;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

public class SampleActivity extends Activity {
    Button sendButton;
    Button networkTypeButton;
    private Disposable sendStateSubscription;
    private Disposable netWorkTypeSubscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample_view);


        sendButton = findViewById(R.id.send_button);
        networkTypeButton = findViewById(R.id.network_type_button);


        final Observable<ButtonState> sendStateStream =
            RxNetwork.stream(this).map(this::map);

        sendStateSubscription =
            sendStateStream.observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onSignalReceived);

        final Observable<ButtonState> streamDetail =
            RxNetwork.streamDetail(this).map(this::map);

        netWorkTypeSubscription =
            streamDetail.observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onTypeSignalReceived);
    }

    private ButtonState map(boolean hasInternet) {
        return !hasInternet ? new ButtonState(R.string.not_connected, false) : new ButtonState(R.string.send, true);
    }

    private ButtonState map(Status status) {
        return status == Status.MOBILE
            ? new ButtonState(R.string.mobile, false)
            : status == Status.WIFI
                ? new ButtonState(R.string.wifi, false)
                : new ButtonState(R.string.absent, false);
    }

    private void onSignalReceived(ButtonState buttonState) {
        sendButton.setText(buttonState.textId);
        sendButton.setEnabled(buttonState.isEnabled);
    }

    private void onTypeSignalReceived(ButtonState buttonState) {
        networkTypeButton.setText(buttonState.textId);
        networkTypeButton.setEnabled(buttonState.isEnabled);
    }

    @Override
    protected void onDestroy() {
        sendStateSubscription.dispose();
        sendStateSubscription = null;
        netWorkTypeSubscription.dispose();
        netWorkTypeSubscription = null;
        super.onDestroy();
    }

    static class ButtonState {
        final int textId;
        final boolean isEnabled;

        ButtonState(int textId, boolean isEnabled) {
            this.textId = textId;
            this.isEnabled = isEnabled;
        }
    }
}
