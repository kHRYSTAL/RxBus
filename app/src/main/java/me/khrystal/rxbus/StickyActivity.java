package me.khrystal.rxbus;

import android.os.Bundle;
import android.widget.TextView;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import me.khrystal.rxbus.eb.StickyEvent;
import me.khrystal.rxbus.lifecycle.ActivityEvent;
import me.khrystal.rxbus.lifecycle.BaseActivity;
import me.khrystal.util.RxBus;

public class StickyActivity extends BaseActivity {

    private TextView tvReceive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sticky);
        tvReceive = (TextView) findViewById(R.id.tvReceiveSticky);
        RxBus.getDefault().tObservableSticky(StickyEvent.class)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.<StickyEvent>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new Consumer<StickyEvent>() {
                    @Override
                    public void accept(StickyEvent event) throws Exception {
                        tvReceive.setText(event.msg);
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // has consume clear map cache, do not remove sticky event in accept method
        RxBus.getDefault().removeStickyEvent(StickyEvent.class);
    }
}
