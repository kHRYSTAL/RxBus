package me.khrystal.rxbus;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import me.khrystal.rxbus.eb.Event;
import me.khrystal.rxbus.eb.StickyEvent;
import me.khrystal.rxbus.lifecycle.ActivityEvent;
import me.khrystal.rxbus.lifecycle.BaseActivity;
import me.khrystal.util.RxBus;

public class MainActivity extends BaseActivity {

    TextView tvMsg;
    TextView tvError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvMsg = (TextView) findViewById(R.id.tvEventMsg);
        tvError = (TextView) findViewById(R.id.tvErrorMsg);
        findViewById(R.id.btnBackpressure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < 100; i++) {
                    RxBus.getDefault().post(new Event(i));
                }
            }
        });

        findViewById(R.id.btnSticky).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RxBus.getDefault().postSticky(new StickyEvent("this is stickyEvent"));
                startActivity(new Intent(MainActivity.this, StickyActivity.class));
            }
        });

        registerRxBus();
    }

    private void registerRxBus() {
        RxBus.getDefault().tObservable(Event.class)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.<Event>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new Consumer<Event>() {
                    @Override
                    public void accept(Event event) throws Exception {
                        tvMsg.setText(String.valueOf(event.number));
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        tvError.setText(throwable.getMessage());
                    }
                });

    }
}
