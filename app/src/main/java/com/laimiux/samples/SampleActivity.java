package com.laimiux.samples;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;

import com.laimiux.rxnetwork.RxNetwork;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

public class SampleActivity extends Activity {
    Button sendButton;
    private Disposable sendStateSubscription;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample_view);


        sendButton = findViewById(R.id.send_button);

        final Observable<ButtonState> sendStateStream =
            RxNetwork.stream(this).map(this::map);

        sendStateSubscription =
            sendStateStream.observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onSignalReceived);
    }

    private ButtonState map(boolean hasInternet) {
        return !hasInternet ? new ButtonState(R.string.not_connected, false) : new ButtonState(R.string.send, true);
    }

    private void onSignalReceived(ButtonState buttonState) {
        sendButton.setText(buttonState.textId);
        sendButton.setEnabled(buttonState.isEnabled);
    }


    @Override
    protected void onDestroy() {
        sendStateSubscription.dispose();
        sendStateSubscription = null;
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
