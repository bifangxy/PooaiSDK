package com.pooai.blesdk;

import android.util.Log;

import androidx.annotation.NonNull;

import com.pooai.blesdk.data.PooaiOvulationData;
import com.pooai.blesdk.data.PooaiPregnancyData;
import com.pooai.blesdk.data.PooaiUrineData;
import com.pooai.blesdk.data.ToiletCommand;
import com.pooai.blesdk.data.ToiletConfig;
import com.pooai.blesdk.data.ToiletRegisterData;
import com.pooai.blesdk.data.ToiletState;
import com.pooai.blesdk.observer.HeartParamsObservable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

/**
 * 作者：created by xieying on 2020-01-26 12:02
 * 功能：
 */
public class PooaiDetectionManager {
    private static final String TAG = PooaiDetectionManager.class.getSimpleName();

    private static final long URINE_WAITING_TIME = 20000;

    private static final String START_HEART_TEST = "m00000-140-01\r\n";
    private static final String STOP_HEAR_TEST = "m00000-000-00\r\n";

    private Disposable mUrineDispose;

    private Disposable mPregnancyDispose;

    private Disposable mOvulationDispose;

    private Disposable mHeartDispose;

    private List<Long> mDataList = new ArrayList<>();

    private static class SingletonHolder {
        private static final PooaiDetectionManager INSTANCE = new PooaiDetectionManager();
    }

    public static PooaiDetectionManager getInstance() {
        return PooaiDetectionManager.SingletonHolder.INSTANCE;
    }

    //切换成为检测模式(注意 开始检测前需要切换成检测模式)
    public void switchDetectionMode() {
        PooaiToiletCommandManager.getInstance().changeToiletState(ToiletState.DETECTION);
    }

    //开始尿检
    public void startUrineTest(@NonNull OnDetectionListener<PooaiUrineData> onDetectionListener) {
        if (mUrineDispose != null && !mUrineDispose.isDisposed()) {
            return;
        }
        mUrineDispose = isToiletConnect()
                .flatMap((Function<Boolean, ObservableSource<Long>>) aBoolean -> {
                    if (aBoolean) {
                        Log.d(TAG, "--开始倒计时--");
                        return Observable.timer(URINE_WAITING_TIME, TimeUnit.MILLISECONDS);
                    } else {
                        Log.d(TAG, "--设备未连接--");
                        return observer -> observer.onError(new Exception("device not connected"));
                    }
                })
                .map(aLong -> {
                    Log.d(TAG, "--发送开始检测命令--");
                    sendStartUrineCommand();
                    onDetectionListener.start();
                    return true;
                })
                .flatMap((Function<Boolean, ObservableSource<Boolean>>) aBoolean -> getUrineFinishObservable())
                .filter(aBoolean -> aBoolean)
                .doOnComplete(() -> {
                    Log.d(TAG, "--检测完成--");
                    PooaiUrineData pooaiUrineData = getUrineTestResult();
                    onDetectionListener.complete(pooaiUrineData);
                })
                .doOnDispose(onDetectionListener::cancel)
                .doOnError(throwable -> onDetectionListener.error(throwable))
                .subscribe();
    }

    //停止尿检
    public void stopUrineTest() {
        if (mUrineDispose != null && !mUrineDispose.isDisposed()) {
            mUrineDispose.dispose();
            mUrineDispose = null;
            ToiletCommand toiletCommand = ToiletRegisterData.getInstance().getRegisterCommand(ToiletConfig.REGISTER_URINE_DOOR, 2);
            PooaiToiletCommandManager.getInstance().addToiletCommand(toiletCommand);
        }
    }

    private Observable<Boolean> getUrineFinishObservable() {
        return Observable.timer(6000, TimeUnit.MILLISECONDS)
                .flatMap((Function<Long, ObservableSource<Long>>) aLong -> Observable.interval(1000, TimeUnit.MILLISECONDS))
                .map(aLong -> isUrineTestFinish())
                .takeUntil((Predicate<Boolean>) aBoolean -> aBoolean);
    }

    private void sendStartUrineCommand() {
        ToiletCommand toiletCommand = ToiletRegisterData.getInstance().getRegisterCommand(ToiletConfig.REGISTER_URINE_TEST1, 1);
        PooaiToiletCommandManager.getInstance().addToiletCommand(toiletCommand);
    }

    private PooaiUrineData getUrineTestResult() {
        PooaiUrineData pooaiUrineData = new PooaiUrineData();
        ToiletRegisterData toiletRegisterData = ToiletRegisterData.getInstance();
        pooaiUrineData.gluValue = toiletRegisterData.getRegisterLowValue(ToiletConfig.REGISTER_URINE_COLOR1) + "," + toiletRegisterData.getRegisterHighValue(ToiletConfig.REGISTER_URINE_COLOR1) + "," + toiletRegisterData.getRegisterLowValue(ToiletConfig.REGISTER_URINE_COLOR2);
        pooaiUrineData.bilValue = toiletRegisterData.getRegisterHighValue(ToiletConfig.REGISTER_URINE_COLOR2) + "," + toiletRegisterData.getRegisterLowValue(ToiletConfig.REGISTER_URINE_COLOR3) + "," + toiletRegisterData.getRegisterHighValue(ToiletConfig.REGISTER_URINE_COLOR3);
        pooaiUrineData.ketValue = toiletRegisterData.getRegisterLowValue(ToiletConfig.REGISTER_URINE_COLOR4) + "," + toiletRegisterData.getRegisterHighValue(ToiletConfig.REGISTER_URINE_COLOR4) + "," + toiletRegisterData.getRegisterLowValue(ToiletConfig.REGISTER_URINE_COLOR5);
        pooaiUrineData.vcValue = toiletRegisterData.getRegisterHighValue(ToiletConfig.REGISTER_URINE_COLOR5) + "," + toiletRegisterData.getRegisterLowValue(ToiletConfig.REGISTER_URINE_COLOR6) + "," + toiletRegisterData.getRegisterHighValue(ToiletConfig.REGISTER_URINE_COLOR6);
        pooaiUrineData.sgValue = toiletRegisterData.getRegisterLowValue(ToiletConfig.REGISTER_URINE_COLOR7) + "," + toiletRegisterData.getRegisterHighValue(ToiletConfig.REGISTER_URINE_COLOR7) + "," + toiletRegisterData.getRegisterLowValue(ToiletConfig.REGISTER_URINE_COLOR8);
        pooaiUrineData.bldValue = toiletRegisterData.getRegisterHighValue(ToiletConfig.REGISTER_URINE_COLOR8) + "," + toiletRegisterData.getRegisterLowValue(ToiletConfig.REGISTER_URINE_COLOR9) + "," + toiletRegisterData.getRegisterHighValue(ToiletConfig.REGISTER_URINE_COLOR9);
        pooaiUrineData.phValue = toiletRegisterData.getRegisterLowValue(ToiletConfig.REGISTER_URINE_COLOR10) + "," + toiletRegisterData.getRegisterHighValue(ToiletConfig.REGISTER_URINE_COLOR10) + "," + toiletRegisterData.getRegisterLowValue(ToiletConfig.REGISTER_URINE_COLOR11);
        pooaiUrineData.proValue = toiletRegisterData.getRegisterHighValue(ToiletConfig.REGISTER_URINE_COLOR11) + "," + toiletRegisterData.getRegisterLowValue(ToiletConfig.REGISTER_URINE_COLOR12) + "," + toiletRegisterData.getRegisterHighValue(ToiletConfig.REGISTER_URINE_COLOR12);
        pooaiUrineData.ubgValue = toiletRegisterData.getRegisterLowValue(ToiletConfig.REGISTER_URINE_COLOR13) + "," + toiletRegisterData.getRegisterHighValue(ToiletConfig.REGISTER_URINE_COLOR13) + "," + toiletRegisterData.getRegisterLowValue(ToiletConfig.REGISTER_URINE_COLOR14);
        pooaiUrineData.nitValue = toiletRegisterData.getRegisterHighValue(ToiletConfig.REGISTER_URINE_COLOR14) + "," + toiletRegisterData.getRegisterLowValue(ToiletConfig.REGISTER_URINE_COLOR15) + "," + toiletRegisterData.getRegisterHighValue(ToiletConfig.REGISTER_URINE_COLOR15);
        pooaiUrineData.leuValue = toiletRegisterData.getRegisterLowValue(ToiletConfig.REGISTER_URINE_COLOR16) + "," + toiletRegisterData.getRegisterHighValue(ToiletConfig.REGISTER_URINE_COLOR16) + "," + toiletRegisterData.getRegisterLowValue(ToiletConfig.REGISTER_URINE_COLOR17);
        pooaiUrineData.sourceData = pooaiUrineData.leuValue + "," + pooaiUrineData.nitValue + "," + pooaiUrineData.ubgValue + "," + pooaiUrineData.proValue + "," + pooaiUrineData.phValue + "," + pooaiUrineData.bldValue + "," + pooaiUrineData.sgValue + "," + pooaiUrineData.vcValue + "," + pooaiUrineData.ketValue + "," + pooaiUrineData.bilValue + "," + pooaiUrineData.gluValue;
        return pooaiUrineData;
    }

    private boolean isUrineTestFinish() {
        Log.d(TAG, "value = " + ToiletRegisterData.getInstance().getRegisterValue(ToiletConfig.REGISTER_URINE_TEST1));
        return ToiletRegisterData.getInstance().getRegisterValue(ToiletConfig.REGISTER_URINE_TEST1) >= 38;
    }

    //开始孕检
    public void startPregnancyTest(OnDetectionListener<PooaiPregnancyData> onDetectionListener) {
        if (mPregnancyDispose != null && !mPregnancyDispose.isDisposed()) {
            return;
        }
        mDataList.clear();
        mPregnancyDispose = isToiletConnect()
                .flatMap((Function<Boolean, ObservableSource<Long>>) aBoolean -> {
                    if (aBoolean) {
                        Log.d(TAG, "--开始倒计时--");
                        return Observable.timer(URINE_WAITING_TIME, TimeUnit.MILLISECONDS);
                    } else {
                        Log.d(TAG, "--设备未连接--");
                        return observer -> observer.onError(new Exception("device not connected"));
                    }
                })
                .map(aLong -> {
                    Log.d(TAG, "--发送开始检测命令--");
                    sendStartPregnancyCommand();
                    onDetectionListener.start();
                    return true;
                })
                .flatMap((Function<Boolean, ObservableSource<Boolean>>) aBoolean -> {
                    Log.d(TAG, "--正在检测--");
                    return getPregnancyFirstStepObservable();
                })
                .filter(aBoolean -> aBoolean)
                .flatMap((Function<Boolean, ObservableSource<Boolean>>) aBoolean -> {
                    Log.d(TAG, "--第一步检测完成--");
                    getToiletDetectionResult();
                    sendSecondPregnancyCommand();
                    return getPregnancySecondStepObservable();
                })
                .filter(aBoolean -> aBoolean)
                .doOnNext(aBoolean -> {
                    getToiletDetectionResult();
                    Log.d(TAG, "--第二步检测完成，获取值--");
                })
                .doOnComplete(() -> {
                    PooaiPregnancyData pooaiPregnancyData = new PooaiPregnancyData();
                    pooaiPregnancyData.pregnancyResult = conversionDetectionResult(mDataList);
                    pooaiPregnancyData.sourceData = mDataList.toString();
                    onDetectionListener.complete(pooaiPregnancyData);
                    Log.d(TAG, "--检测完成--");
                })
                .doOnDispose(onDetectionListener::cancel)
                .doOnError(onDetectionListener::error)
                .subscribe();
    }

    public void stopPregnancyTest() {
        if (mPregnancyDispose != null && !mPregnancyDispose.isDisposed()) {
            mPregnancyDispose.dispose();
            mPregnancyDispose = null;
            ToiletCommand toiletCommand = ToiletRegisterData.getInstance().getRegisterCommand(ToiletConfig.REGISTER_URINE_DOOR, 4);
            PooaiToiletCommandManager.getInstance().addToiletCommand(toiletCommand);
        }
    }

    //延时6s再去读取是否第一步完成
    private Observable<Boolean> getPregnancyFirstStepObservable() {
        return Observable.timer(6000, TimeUnit.MILLISECONDS)
                .flatMap((Function<Long, ObservableSource<Long>>) aLong -> Observable.interval(1000, TimeUnit.MILLISECONDS))
                .map(aLong -> isPregnancyFirstStepFinish())
                .takeUntil((Predicate<Boolean>) aBoolean -> aBoolean);
    }

    //延时1s再去读取是否第二步完成
    private Observable<Boolean> getPregnancySecondStepObservable() {
        return Observable.timer(1000, TimeUnit.MILLISECONDS)
                .flatMap((Function<Long, ObservableSource<Long>>) aLong -> Observable.interval(1000, TimeUnit.MILLISECONDS))
                .map(aLong -> isPregnancySecondStepFinish())
                .takeUntil((Predicate<Boolean>) aBoolean -> aBoolean);
    }

    private void sendStartPregnancyCommand() {
        ToiletCommand toiletCommand = ToiletRegisterData.getInstance().getRegisterCommand(ToiletConfig.REGISTER_URINE_TEST1, 2);
        PooaiToiletCommandManager.getInstance().addToiletCommand(toiletCommand);
    }

    private void sendSecondPregnancyCommand() {
        ToiletCommand toiletCommand = ToiletRegisterData.getInstance().getRegisterCommand(ToiletConfig.REGISTER_URINE_TEST1, 39);
        PooaiToiletCommandManager.getInstance().addToiletCommand(toiletCommand);
    }

    private boolean isPregnancyFirstStepFinish() {
        return ToiletRegisterData.getInstance().getRegisterValue(ToiletConfig.REGISTER_URINE_TEST1) == 38;
    }


    private boolean isPregnancySecondStepFinish() {
        return ToiletRegisterData.getInstance().getRegisterValue(ToiletConfig.REGISTER_URINE_TEST1) >= 40;
    }

    //开始排卵检测
    public void startOvulationTest(OnDetectionListener<PooaiOvulationData> onDetectionListener) {
        if (mOvulationDispose != null && !mOvulationDispose.isDisposed()) {
            return;
        }
        mOvulationDispose = isToiletConnect()
                .flatMap((Function<Boolean, ObservableSource<Long>>) aBoolean -> {
                    if (aBoolean) {
                        Log.d(TAG, "--开始倒计时--");
                        return Observable.timer(URINE_WAITING_TIME, TimeUnit.MILLISECONDS);
                    } else {
                        Log.d(TAG, "--设备未连接--");
                        return observer -> observer.onError(new Exception("device not connected"));
                    }
                })
                .map(aLong -> {
                    Log.d(TAG, "--发送开始检测命令--");
                    onDetectionListener.start();
                    sendStartOvulationCommand();
                    return true;
                })
                .flatMap((Function<Boolean, ObservableSource<Boolean>>) aBoolean -> {
                    Log.d(TAG, "--正在检测--");
                    return getOvulationFirstStepObservable();
                })
                .filter(aBoolean -> aBoolean)
                .flatMap((Function<Boolean, ObservableSource<Boolean>>) aBoolean -> {
                    Log.d(TAG, "--第一步检测完成开始第二步检测--");
                    getToiletDetectionResult();
                    sendSecondOvulationCommand();
                    return getOvulationSecondStepObservable();
                })
                .filter(aBoolean -> aBoolean)
                .doOnNext(aBoolean -> {
                    getToiletDetectionResult();
                    Log.d(TAG, "--第二步检测完成，获取值--");
                })
                .doOnComplete(() -> {
                    PooaiOvulationData pooaiOvulationData = new PooaiOvulationData();
                    pooaiOvulationData.ovulationResult = conversionDetectionResult(mDataList);
                    pooaiOvulationData.sourceData = mDataList.toString();
                    onDetectionListener.complete(pooaiOvulationData);
                    Log.d(TAG, "--检测完成--");
                })
                .doOnDispose(onDetectionListener::cancel)
                .doOnError(onDetectionListener::error)
                .subscribe();
    }


    public void stopOvulationTest() {
        if (mOvulationDispose != null && !mOvulationDispose.isDisposed()) {
            mOvulationDispose.dispose();
            mOvulationDispose = null;
            ToiletCommand toiletCommand = ToiletRegisterData.getInstance().getRegisterCommand(ToiletConfig.REGISTER_URINE_DOOR, 4);
            PooaiToiletCommandManager.getInstance().addToiletCommand(toiletCommand);
        }
    }

    private Observable<Boolean> getOvulationFirstStepObservable() {
        return Observable.timer(6000, TimeUnit.MILLISECONDS)
                .flatMap((Function<Long, ObservableSource<Long>>) aLong -> Observable.interval(1000, TimeUnit.MILLISECONDS))
                .map(aLong -> isOvulationFirstStepFinish())
                .takeUntil((Predicate<Boolean>) aBoolean -> aBoolean);
    }

    private Observable<Boolean> getOvulationSecondStepObservable() {
        return Observable.timer(1000, TimeUnit.MILLISECONDS)
                .flatMap((Function<Long, ObservableSource<Long>>) aLong -> Observable.interval(1000, TimeUnit.MILLISECONDS))
                .map(aLong -> isOvulationSecondStepFinish())
                .takeUntil((Predicate<Boolean>) aBoolean -> aBoolean);
    }

    private void sendStartOvulationCommand() {
        ToiletCommand toiletCommand = ToiletRegisterData.getInstance().getRegisterCommand(ToiletConfig.REGISTER_URINE_TEST1, 3);
        PooaiToiletCommandManager.getInstance().addToiletCommand(toiletCommand);
    }

    private void sendSecondOvulationCommand() {
        ToiletCommand toiletCommand = ToiletRegisterData.getInstance().getRegisterCommand(ToiletConfig.REGISTER_URINE_TEST1, 39);
        PooaiToiletCommandManager.getInstance().addToiletCommand(toiletCommand);
    }

    private boolean isOvulationFirstStepFinish() {
        return ToiletRegisterData.getInstance().getRegisterValue(ToiletConfig.REGISTER_URINE_TEST1) == 38;
    }

    private boolean isOvulationSecondStepFinish() {
        return ToiletRegisterData.getInstance().getRegisterValue(ToiletConfig.REGISTER_URINE_TEST1) >= 40;
    }

    //打开尿检检测槽
    public void openUrineTank() {
        ToiletCommand toiletCommand = ToiletRegisterData.getInstance().getRegisterCommand(ToiletConfig.REGISTER_URINE_DOOR, 1);
        PooaiToiletCommandManager.getInstance().addToiletCommand(toiletCommand);
    }

    //打开孕检排卵检测槽
    public void openPregnancyAndOvulationTank() {
        ToiletCommand toiletCommand = ToiletRegisterData.getInstance().getRegisterCommand(ToiletConfig.REGISTER_URINE_DOOR, 3);
        PooaiToiletCommandManager.getInstance().addToiletCommand(toiletCommand);
    }

    //关闭检测槽
    public void closeDetectionTank() {
        ToiletCommand toiletCommand = ToiletRegisterData.getInstance().getRegisterCommand(ToiletConfig.REGISTER_URINE_DOOR, 2);
        PooaiToiletCommandManager.getInstance().addToiletCommand(toiletCommand);
    }

    /**
     * 开始心电检测
     *
     * @param onHeartDetectionListener
     */
    public void startHeartTest(OnHeartDetectionListener onHeartDetectionListener) {
        if (mHeartDispose != null && !mHeartDispose.isDisposed()) {
            return;
        }
        PooaiToiletCommandManager.getInstance().changeToiletState(ToiletState.HEART);
        PooaiToiletCommandManager.getInstance().addToiletCommand(START_HEART_TEST);
        mHeartDispose = Observable
                .create((ObservableOnSubscribe<byte[]>) emitter ->
                        HeartParamsObservable.getInstance().setOnHeartReceiverListener(emitter::onNext))
                .doOnNext(pooaiHeartData -> conversionHeartData(pooaiHeartData, onHeartDetectionListener))
                .doOnDispose(() -> {
                    PooaiToiletCommandManager.getInstance().addToiletCommand(STOP_HEAR_TEST);
                    onHeartDetectionListener.complete();
                })
                .subscribe();
    }

    private void conversionHeartData(byte[] pooaiHeartData, OnHeartDetectionListener onHeartDetectionListener) {
        int cmd, val, errorType = 808;
        try {
            int size = pooaiHeartData.length;
            for (int i = 0; i < size - 6; i++) {
                cmd = 0;
                val = 0;
                if (pooaiHeartData[i] == 0x01 &&
                        (pooaiHeartData[i + 1] & 0x80) == (int) 0x80 &&
                        (pooaiHeartData[i + 2] & 0x80) == (int) 0x80 &&
                        (pooaiHeartData[i + 3] & 0x80) == (int) 0x80 &&
                        pooaiHeartData[i + 4] == 0x01) {
                    int highbit1 = pooaiHeartData[i + 1] & 0x01;
                    int highbit2 = pooaiHeartData[i + 1] & 0x02 >> 1;
                    int high = pooaiHeartData[i + 2] & 0x7f;
                    int low = ((pooaiHeartData[i + 3] & 0x70) | (highbit2 << 7)) >> 4;
                    int isHeartbit = pooaiHeartData[i + 3] & 0x01;
                    val = (int) (((highbit1 << 7) | high) * 16 + (low));
                    cmd = 69;
                    i += 2;
                } else if (pooaiHeartData[i] == 0x02 &&
                        (pooaiHeartData[i + 1] & 0x80) == (int) 0x80 &&
                        (pooaiHeartData[i + 2] & 0x80) == (int) 0x80 &&
                        (pooaiHeartData[i + 3] & 0x80) == (int) 0x80 &&
                        (pooaiHeartData[i + 4] & 0x80) == (int) 0x80) {
                    int highbit1 = pooaiHeartData[i + 1] & 0x10 >> 4;
                    int highbit2 = pooaiHeartData[i + 1] & 0x08 >> 3;

                    int low = (highbit1 << 7) | (pooaiHeartData[i + 2] & 0x7f);
                    int high = (highbit2 << 7) | (pooaiHeartData[i + 3] & 0x7f);
                    int beat = high * 256 + low;
                    val = beat < 30000 ? beat : -1;
                    errorType = pooaiHeartData[i + 4] & 0x7f;
                    cmd = 79;
                    i += 7;
                }
                switch (cmd) {
                    case 69: //心电，返回的心电数据
                        onHeartDetectionListener.heartData(val);
                        break;
                    case 79:
                        onHeartDetectionListener.heartRate(val, errorType);
                        break;
                    default:
                        break;
                }
            }

        } catch (Exception e) {

        }

    }

    public void stopHeartTest() {
        if (mHeartDispose != null && !mHeartDispose.isDisposed()) {
            mHeartDispose.dispose();
            mHeartDispose = null;
            PooaiToiletCommandManager.getInstance().addToiletCommand(STOP_HEAR_TEST);
        }
    }

    /**
     * @param dataList 检测数值
     * @return 检测结果 0，为一条杠 1，两条杠 2，无效
     * 判断依据：取16个点的值，颜色越浅值越大 2～7为第一杠检测区域
     * 7～10 为中间区域，认为是白色的区域
     * 10～15为第二杠检测点的区域
     */
    private int conversionDetectionResult(List<Long> dataList) {
        if (dataList.size() != 16) {
            return 2;
        }
        long min1 = dataList.get(2);
        for (int i = 2; i < 7; i++) {
            if (min1 > dataList.get(i)) {
                min1 = dataList.get(i);
            }
        }

        long min2 = dataList.get(10);
        for (int i = 10; i < 15; i++) {
            if (min2 > dataList.get(i)) {
                min2 = dataList.get(i);
            }
        }

        long max3 = dataList.get(7);
        for (int i = 7; i < 10; i++) {
            if (max3 < dataList.get(i)) {
                max3 = dataList.get(i);
            }
        }
        int result;
        if (min1 <= max3) {
            if (min2 <= max3) {
                if (max3 - min1 > 12 && max3 - min2 > 12 && Math.abs((max3 - min2)) / (max3 - min1) < 3) {
                    result = 0;
                } else {
                    result = 1;
                }
            } else {
                result = 0;
            }
        } else {
            if (min2 <= max3) {
                result = 0;
            } else {
                result = 2;
            }
        }
        return result;
    }


    //获取孕检排卵检测每次的值
    private void getToiletDetectionResult() {
        ToiletRegisterData toiletRegisterData = ToiletRegisterData.getInstance();
        for (int i = 12; i < 30; i++) {
            long value = toiletRegisterData.getRegisterValue(i) * 65535 + toiletRegisterData.getRegisterValue(i + 1);
            mDataList.add(value);
            i++;
        }
    }

    private Observable<Boolean> isToiletConnect() {
        return Observable.create(emitter -> {
            emitter.onNext(true);
            emitter.onComplete();
        });
    }

    public interface OnDetectionListener<T> {

        void start();

        void complete(T data);

        void cancel();

        void error(Throwable throwable);
    }

    public interface OnHeartDetectionListener {
        void heartData(int heartData);

        void heartRate(int heartRate, int errorType);

        void complete();
    }
}
