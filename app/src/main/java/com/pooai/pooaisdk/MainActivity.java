package com.pooai.pooaisdk;

import android.Manifest;
import android.bluetooth.BluetoothDevice;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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

    @BindView(R.id.tv_state)
    TextView mTvState;

    private BleAdapter mBleAdapter;

    private List<BluetoothDevice> mPooaiBleDeviceList;

    private PooaiBleManager mPooaiBleManager;

    private PooaiDetectionManager mPooaiDetectionManager;

    private PooaiControlManager mPooaiControlManager;

    private boolean isUrine;

    private boolean isPregnancy;

    private boolean isOvulation;

    private int water_pressure_stall;

    private int water_temp_stall;

    private int dry_temp_stall;

    private int nozzle_position_stall;

    private int cushion_temp_stall;

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
                    new Handler(Looper.getMainLooper()).post(() -> {
                        mBtStartScan.setText("已连接");
                        mRecyclerView.setVisibility(View.GONE);
                    });

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
        mRecyclerView.setVisibility(View.VISIBLE);
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

    @OnClick(R.id.bt_start_hip_wash)
    public void startHipWash() {
        mPooaiControlManager.switchControlMode();
        mPooaiControlManager.startHipWash();
        Log.d(TAG, "hip wash state = " + mPooaiControlManager.isHipWash());
    }

    @OnClick(R.id.bt_stop_hip_wash)
    public void stopHipWash() {
        mPooaiControlManager.switchControlMode();
        mPooaiControlManager.stopHipWash();
        Log.d(TAG, "hip wash state = " + mPooaiControlManager.isHipWash());
    }

    @OnClick(R.id.bt_start_women_wash)
    public void startWomenWash() {
        mPooaiControlManager.switchControlMode();
        mPooaiControlManager.startWomanWash();
        Log.d(TAG, "women wash state = " + mPooaiControlManager.isWomanWash());
    }

    @OnClick(R.id.bt_stop_women_wash)
    public void stopWomenWash() {
        mPooaiControlManager.switchControlMode();
        mPooaiControlManager.stopHipWash();
        Log.d(TAG, "women wash state = " + mPooaiControlManager.isWomanWash());
    }

    @OnClick(R.id.bt_start_lax)
    public void startLax() {
        mPooaiControlManager.switchControlMode();
        mPooaiControlManager.startLaxative();
        Log.d(TAG, "laxative state = " + mPooaiControlManager.isLaxative());
    }

    @OnClick(R.id.bt_stop_lax)
    public void stopLax() {
        mPooaiControlManager.switchControlMode();
        mPooaiControlManager.stopLaxative();
        Log.d(TAG, "laxative state = " + mPooaiControlManager.isLaxative());
    }

    @OnClick(R.id.bt_start_massage)
    public void startMassage() {
        mPooaiControlManager.switchControlMode();
        mPooaiControlManager.startMassage();
        Log.d(TAG, "massage state = " + mPooaiControlManager.isMassage());
    }

    @OnClick(R.id.bt_stop_massage)
    public void stopMassage() {
        mPooaiControlManager.switchControlMode();
        mPooaiControlManager.stopMassage();
        Log.d(TAG, "massage state = " + mPooaiControlManager.isMassage());
    }

    @OnClick(R.id.bt_start_dry)
    public void startDry() {
        mPooaiControlManager.switchControlMode();
        mPooaiControlManager.startDrying();
        Log.d(TAG, "dry state = " + mPooaiControlManager.isDrying());
    }

    @OnClick(R.id.bt_stop_dry)
    public void stopDry() {
        mPooaiControlManager.switchControlMode();
        mPooaiControlManager.stopDrying();
        Log.d(TAG, "dry state = " + mPooaiControlManager.isDrying());
    }

    @OnClick(R.id.bt_start_atomize)
    public void startAtomize() {
        mPooaiControlManager.switchControlMode();
        mPooaiControlManager.startAtomize();
        Log.d(TAG, "atomize state = " + mPooaiControlManager.isAtomize());
    }

    @OnClick(R.id.bt_stop_atomize)
    public void stopAtomize() {
        mPooaiControlManager.switchControlMode();
        mPooaiControlManager.stopAtomize();
        Log.d(TAG, "atomize state = " + mPooaiControlManager.isAtomize());
    }

    @OnClick(R.id.bt_open_double)
    public void openDouble() {
        mPooaiControlManager.switchControlMode();
        mPooaiControlManager.openLid();
    }

    @OnClick(R.id.bt_open_single)
    public void openSingle() {
        mPooaiControlManager.switchControlMode();
        mPooaiControlManager.openHalfLid();
    }

    @OnClick(R.id.bt_close_all_flip)
    public void closeAllFlip() {
        mPooaiControlManager.switchControlMode();
        mPooaiControlManager.closeLid();
    }


    @OnClick(R.id.bt_water_temp)
    public void waterTempStall() {
        water_temp_stall = water_temp_stall % 6;
        mPooaiControlManager.switchControlMode();
        mPooaiControlManager.waterTemp(water_temp_stall);
        water_temp_stall++;
    }

    @OnClick(R.id.bt_water_pressure)
    public void waterPressureStall() {
        water_pressure_stall = water_pressure_stall % 6;
        mPooaiControlManager.switchControlMode();
        mPooaiControlManager.waterPressure(water_pressure_stall);
        water_pressure_stall++;
    }

    @OnClick(R.id.bt_dry_temp)
    public void dryTempStall() {
        dry_temp_stall = dry_temp_stall % 6;
        mPooaiControlManager.switchControlMode();
        mPooaiControlManager.windTemp(dry_temp_stall);
        dry_temp_stall++;
    }

    @OnClick(R.id.bt_nozzle_position)
    public void nozzlePositionStall() {
        nozzle_position_stall = nozzle_position_stall % 6;
        mPooaiControlManager.switchControlMode();
        mPooaiControlManager.nozzlePosition(nozzle_position_stall);
        nozzle_position_stall++;
    }

    @OnClick(R.id.bt_cushion_temp)
    public void cushionTempStall() {
        cushion_temp_stall = cushion_temp_stall % 6;
        mPooaiControlManager.switchControlMode();
        mPooaiControlManager.cushionTemp(cushion_temp_stall);
        cushion_temp_stall++;
    }

    @OnClick(R.id.bt_get_state)
    public void getState() {
        mTvState.setText("臀洗 ：" + mPooaiControlManager.isHipWash()
                + "   妇洗：" + mPooaiControlManager.isHipWash()
                + "  通便：" + mPooaiControlManager.isLaxative()
                + "  按摩：" + mPooaiControlManager.isMassage()
                + "  烘干：" + mPooaiControlManager.isDrying()
                + "  雾化：" + mPooaiControlManager.isAtomize() + "\n"
                + "着坐：" + mPooaiControlManager.isSeat()
                + "   水温异常：" + mPooaiControlManager.isWaterTempError()
                + "   风温异常：" + mPooaiControlManager.isDryingTempError()
                + "   坐垫温度异常：" + mPooaiControlManager.isCushionTempError() + "\n"
                + "水温档位：" + mPooaiControlManager.getWaterTempStall()
                + "   水压档位：" + mPooaiControlManager.getWaterPressureStall()
                + "   风温档位：" + mPooaiControlManager.getWindTempStall()
                + "   喷嘴位置：" + mPooaiControlManager.getNozzlePositionStall()
                + "   坐垫温度：" + mPooaiControlManager.getCushionTempStall());
    }
}
