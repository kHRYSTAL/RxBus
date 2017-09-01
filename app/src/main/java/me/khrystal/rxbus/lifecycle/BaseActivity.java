package me.khrystal.rxbus.lifecycle;

import android.os.Bundle;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import com.trello.rxlifecycle2.LifecycleTransformer;

/**
 * usage: implement unSubscribe in lifecycle, cancel use unSubscribe
 * author: kHRYSTAL
 * create time: 17/9/1
 * update time:
 * email: 723526676@qq.com
 */

public abstract class BaseActivity extends AppCompatActivity{

    protected ActivityLifeProvider lifeProvider = new ActivityLifeProvider();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lifeProvider.onNext(ActivityEvent.CREATE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        lifeProvider.onNext(ActivityEvent.START);
    }

    @Override
    protected void onResume() {
        super.onResume();
        lifeProvider.onNext(ActivityEvent.RESUME);
    }

    @Override
    protected void onPause() {
        super.onPause();
        lifeProvider.onNext(ActivityEvent.PAUSE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        lifeProvider.onNext(ActivityEvent.STOP);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        lifeProvider.onNext(ActivityEvent.DESTROY);
    }

    @NonNull
    @CheckResult
    public final <T> LifecycleTransformer<T> bindUntilEvent(@NonNull ActivityEvent event) {
        return lifeProvider.bindUntilEvent(event);
    }
}
