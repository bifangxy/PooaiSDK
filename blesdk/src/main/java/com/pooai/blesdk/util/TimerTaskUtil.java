package com.pooai.blesdk.util;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

/**
 * 作者：created by xieying on 2020-01-12 18:17
 * 功能：
 */
public class TimerTaskUtil {

    private static Scheduler getMainThread() {
        return AndroidSchedulers.mainThread();
    }

    public static Task timerRx(long delay, OnRxListener lis) {
        Disposable d = Observable.timer(delay, TimeUnit.MILLISECONDS)
                //主线程中回调
                .observeOn(getMainThread())
                .subscribe(aLong -> {
                    if (lis != null) {
                        lis.onNext(aLong);
                    }
                }, throwable -> {
                    if (lis != null) {
                        lis.onError(throwable);
                    }
                }, () -> {
                    if (lis != null) {
                        lis.onComplete();
                    }
                }, disposable -> {
                    if (lis != null) {
                        lis.onSubscribe(disposable);
                    }
                });
        return new Task(d);
    }

    public static Task intervalRx(long initialDelay, long period, OnRxListener lis) {
        Disposable d = Observable.interval(initialDelay, period, TimeUnit.MILLISECONDS)
                //主线程中回调
                .observeOn(getMainThread())
                .subscribe(aLong -> {
                    if (lis != null) {
                        lis.onNext(aLong);
                    }
                }, throwable -> {
                    if (lis != null) {
                        lis.onError(throwable);
                    }
                }, () -> {
                    if (lis != null) {
                        lis.onComplete();
                    }
                }, disposable -> {
                    if (lis != null) {
                        lis.onSubscribe(disposable);
                    }
                });
        return new Task(d);
    }

    public interface OnRxListener {

        void onNext(Long aLong);

        void onError(Throwable throwable);

        void onComplete();

        void onSubscribe(Disposable disposable);
    }

    public interface onSimpleRxListener extends OnRxListener{
        @Override
        void onComplete();
    }

    public static class Task {
        private Disposable mDisposable;

        public Task(Disposable disposable) {
            mDisposable = disposable;
        }

        /**
         * 任务是否处理
         */
        public boolean isDisposed() {
            return mDisposable.isDisposed();
        }

        /**
         * 取消任务
         */
        public void cancel() {
            mDisposable.dispose();
        }
    }

}
