package com.pooai.pooaisdk;

import android.Manifest;
import android.bluetooth.BluetoothDevice;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pooai.blesdk.PooaiBleManager;
import com.pooai.blesdk.PooaiControlManager;
import com.pooai.blesdk.PooaiDetectionManager;
import com.pooai.blesdk.data.PooaiOvulationData;
import com.pooai.blesdk.data.PooaiPregnancyData;
import com.pooai.blesdk.data.PooaiUrineData;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.rv_ble_device)
    RecyclerView mRecyclerView;

    @BindView(R.id.bt_start_scan)
    Button mBtStartScan;

    private BleAdapter mBleAdapter;

    private List<BluetoothDevice> mPooaiBleDeviceList;

    private PooaiBleManager mPooaiBleManager;

    private PooaiDetectionManager mPooaiDetectionManager;

    private PooaiControlManager mPooaiControlManager;

    private boolean isUrine;

    private boolean isPregnancy;

    private boolean isOvulation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Log.e("BRG", "没有权限");
            // 没有权限，申请权限。
            // 申请授权。
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }

        mPooaiBleDeviceList = new ArrayList<>();
        mBleAdapter = new BleAdapter(mPooaiBleDeviceList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mBleAdapter);


        mPooaiBleManager = PooaiBleManager.getInstance();
        mPooaiControlManager = PooaiControlManager.getInstance();
        mPooaiDetectionManager = PooaiDetectionManager.getInstance();
        mPooaiBleManager.initBLE();
        initListener();
    }

    private void initListener() {
        mBleAdapter.setOnItemClickListener((adapter, view, position) -> {
            BluetoothDevice pooaiBleDevice = mBleAdapter.getData().get(position);
            mPooaiBleManager.connectDevice(pooaiBleDevice, new PooaiBleManager.OnBleConnectListener() {
                @Override
                public void connect() {

                }

                @Override
                public void disconnect() {

                }
            });
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @OnClick(R.id.bt_start_scan)
    public void startScan() {
        mPooaiBleManager.startScan(new PooaiBleManager.OnBleScanListener() {
            @Override
            public void scanResult(BluetoothDevice bluetoothDevice) {
                if (!mPooaiBleDeviceList.contains(bluetoothDevice)) {
                    mPooaiBleDeviceList.add(bluetoothDevice);
                    mBleAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void startScan() {
                mBtStartScan.setText("正在扫描");
            }

            @Override
            public void stopScan() {
                mBtStartScan.setText("开始扫描");
            }
        });
    }

    @OnClick(R.id.bt_disconnect)
    public void sendCommand() {
        mPooaiBleManager.disconnectedDevice();
    }

    @OnClick(R.id.bt_start_urine)
    public void startUrine() {
        if (isUrine) {
            mPooaiDetectionManager.stopUrineTest();
            return;
        }
        isUrine = true;
        mPooaiDetectionManager.switchDetectionMode();
        mPooaiDetectionManager.openUrineTank();
        mPooaiDetectionManager.startUrineTest(new PooaiDetectionManager.OnDetectionListener<PooaiUrineData>() {
            @Override
            public void start() {
            }

            @Override
            public void complete(PooaiUrineData data) {
                Log.d(TAG, "尿检完成" + " data = " + data.sourceData);

                isUrine = false;
            }

            @Override
            public void cancel() {
                isUrine = false;
            }

            @Override
            public void error(Throwable throwable) {

            }
        });
    }

    @OnClick(R.id.bt_start_pregnancy)
    public void startPregnancy() {
        if (isPregnancy) {
            mPooaiDetectionManager.stopPregnancyTest();
            return;
        }
        isPregnancy = true;
        mPooaiDetectionManager.switchDetectionMode();
        mPooaiDetectionManager.openPregnancyAndOvulationTank();
        mPooaiDetectionManager.startPregnancyTest(new PooaiDetectionManager.OnDetectionListener<PooaiPregnancyData>() {
            @Override
            public void start() {
            }

            @Override
            public void complete(PooaiPregnancyData data) {
                Log.d(TAG, "孕检完成" + " data = " + data.sourceData);
                isPregnancy = false;
            }

            @Override
            public void cancel() {
                isPregnancy = false;
            }

            @Override
            public void error(Throwable throwable) {

            }
        });
    }

    @OnClick(R.id.bt_start_ovulation)
    public void startOvulation() {
        if (isOvulation) {
            mPooaiDetectionManager.stopOvulationTest();
            return;
        }
        isOvulation = true;
        mPooaiDetectionManager.switchDetectionMode();
        mPooaiDetectionManager.openPregnancyAndOvulationTank();
        mPooaiDetectionManager.startOvulationTest(new PooaiDetectionManager.OnDetectionListener<PooaiOvulationData>() {
            @Override
            public void start() {
            }

            @Override
            public void complete(PooaiOvulationData data) {
                isOvulation = false;
                Log.d(TAG, "排卵检测完成" + " data = " + data.sourceData);
            }

            @Override
            public void cancel() {
                isOvulation = false;
            }

            @Override
            public void error(Throwable throwable) {

            }
        });
    }

    @OnClick(R.id.bt_start_heart)
    public void startHeart() {
        mPooaiDetectionManager.startHeartTest(new PooaiDetectionManager.OnHeartDetectionListener() {
            @Override
            public void heartData(int heartData) {
                Log.d(TAG, "heartData =" + heartData);
            }

            @Override
            public void heartRate(int heartRate, int errorType) {
            }

            @Override
            public void complete() {

            }
        });
    }

    @OnClick(R.id.bt_stop_heart)
    public void stopHeart() {
        PooaiDetectionManager pooaiDetectionManager = PooaiDetectionManager.getInstance();
        pooaiDetectionManager.stopHeartTest();
    }

    @OnClick(R.id.bt_open_light)
    public void openLight() {
        mPooaiControlManager.switchControlMode();
        mPooaiControlManager.openLight();
    }

    @OnClick(R.id.bt_close_light)
    public void closeLight() {
        mPooaiControlManager.switchControlMode();
        mPooaiControlManager.closeLight();
    }

    @OnClick(R.id.bt_open_flip)
    public void openFlip() {
        mPooaiControlManager.switchControlMode();
        mPooaiControlManager.openAutoFlip();
    }

    @OnClick(R.id.bt_close_light)
    public void closeFlip() {
        mPooaiControlManager.switchControlMode();
        mPooaiControlManager.closeAutoFlip();
    }
}
