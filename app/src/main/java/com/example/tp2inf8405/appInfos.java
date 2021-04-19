package com.example.tp2inf8405;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class appInfos extends AppCompatActivity {
    private Context mContext;

    private TextView mTextViewInfo;
    private TextView mTextViewPercentage;
    private ProgressBar mProgressBar;
    private int mProgressStatus = 0;
    private TextView result;
    private Activity mActivity;

    private LinearLayout mLinearLayout;
    private Button mButton;
    private Button closeButton;
    private TextView bandwith;
    private ConnectivityManager cm;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.bandwith_popup);
        result = (TextView) findViewById(R.id.result);
        closeButton = (Button) findViewById(R.id.close_band);
        bandwith = (TextView) findViewById(R.id.bandwith);
        cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        //should check null because in airplane mode it will be null
        NetworkCapabilities nc = cm.getNetworkCapabilities(cm.getActiveNetwork());
        int downSpeed = nc.getLinkDownstreamBandwidthKbps();
        int upSpeed = nc.getLinkUpstreamBandwidthKbps();
        String DSpeed = "Downlink: " + downSpeed + " kbps";
        String USpeed = "Uplink: " + upSpeed + " kbps" +'\n' + DSpeed;
        bandwith.setText(USpeed);

        // Set a click listener for the popup window close button
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToMaps();
            }
        });
        loadBatteryInfo();
    }

    private void loadBatteryInfo(){
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_POWER_CONNECTED);
        intentFilter.addAction(Intent.ACTION_POWER_DISCONNECTED);
        intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);

        registerReceiver(batteryInfoReceiver, intentFilter);
    }

    private void updateBatteryData(Intent intent) {
        boolean present = intent.getBooleanExtra(BatteryManager.EXTRA_PRESENT, false);
        if(present){
            StringBuilder batteryInfo = new StringBuilder();


            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
            batteryInfo.append("Maximum Battery Level      : " + scale).append("\n");
            batteryInfo.append("Actual Battery Level             : " + level).append("\n");






// Information à propos de la sante de la batterie
            int health = intent.getIntExtra(BatteryManager.EXTRA_HEALTH, 0);
            if(health == BatteryManager.BATTERY_HEALTH_COLD){
                batteryInfo.append("Health                                    : Cold " ).append("\n");
            }
            if(health == BatteryManager.BATTERY_HEALTH_DEAD){
                batteryInfo.append("Health                                    : Dead " ).append("\n");
            }
            if (health == BatteryManager.BATTERY_HEALTH_GOOD){
                batteryInfo.append("Health                                    : Good " ).append("\n");
            }
            if(health == BatteryManager.BATTERY_HEALTH_OVERHEAT){
                batteryInfo.append("Health                                    : Overheat " ).append("\n");
            }
            if (health == BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE){
                batteryInfo.append("Health                                    : Over Voltage " ).append("\n");
            }
            if (health == BatteryManager.BATTERY_HEALTH_UNKNOWN){
                batteryInfo.append("Health                                    : Unknown " ).append("\n");
            }
            if (health == BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE){
                batteryInfo.append("Health                                    : Unspecified Failure " ).append("\n");
            }





//Plugged information
            int plugged = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, 0);
            if(plugged == BatteryManager.BATTERY_PLUGGED_AC) {
                batteryInfo.append("Plugged                           : AC " ).append("\n");

            } else{
                if(plugged == BatteryManager.BATTERY_PLUGGED_USB) {
                    batteryInfo.append("Plugged                                 : USB ").append("\n");
                }
                else{
                    if (plugged == BatteryManager.BATTERY_PLUGGED_WIRELESS) {
                        batteryInfo.append("Plugged                                 : Wireless ").append("\n");
                    }
                }if (plugged != BatteryManager.BATTERY_PLUGGED_AC && plugged != BatteryManager.BATTERY_PLUGGED_USB && plugged != BatteryManager.BATTERY_PLUGGED_WIRELESS) {
                    batteryInfo.append("Plugged                                 : No ").append("\n");
                }
            }






// Charging status Battery informations
            if (level != -1 && scale != -1){
                int batteryPct = (int) ((level / (float) scale) * 100f);
                batteryInfo.append("Actual % of Battery Level    : " + batteryPct).append("%\n");
                int usedByApp = (int) (100 - batteryPct);
                batteryInfo.append("Quantity Used by the App   : " + usedByApp).append("%\n");

                int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
                if(status == BatteryManager.BATTERY_STATUS_CHARGING){
                    batteryInfo.append("Charging at                       : " + batteryPct).append("%\n");
                }
                if(status == BatteryManager.BATTERY_STATUS_DISCHARGING) {
                    batteryInfo.append("Discharging at                    : " + batteryPct).append("%\n");
                }
                if (status == BatteryManager.BATTERY_STATUS_FULL){
                    batteryInfo.append("Battery Full at                   : " + batteryPct).append("%\n");
                }
                if(status == BatteryManager.BATTERY_STATUS_UNKNOWN){
                    batteryInfo.append("Unknown at                        : " + batteryPct).append("%\n");
                }
                if (status == BatteryManager.BATTERY_STATUS_NOT_CHARGING){
                    batteryInfo.append("Not Charging at                   : " + batteryPct).append("%\n");
                }
            }





//Information à propos de la technologie de la batterie
            if (intent.getExtras() != null) {
                String technology = intent.getExtras().getString(BatteryManager.EXTRA_TECHNOLOGY);
                batteryInfo.append("Technology                           : " + technology).append("\n");
            }

//Information à propos de la temperature de la batterie
            int temperature = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0);
            if (temperature > 0) {
                batteryInfo.append("Temperature                         : " + ((float) temperature / 10f)).append("°C\n");
            }



//Information à propos du voltage de la batterie
            int voltage = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 0);
            batteryInfo.append("Voltage                                  : " + voltage).append("mV\n");

            long capacity = getBatteryCapacity();
            batteryInfo.append("Capacity                                : " + capacity).append(" mAh\n");

            result.setText(batteryInfo.toString());

        } else {
            Toast.makeText(appInfos.this, "No Battery Present", Toast.LENGTH_SHORT).show();
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private long getBatteryCapacity(){
        if (Build.VERSION.SDK_INT >- Build.VERSION_CODES.LOLLIPOP) {
            BatteryManager mBatteryManager = (BatteryManager) getSystemService(Context.BATTERY_SERVICE);
            Long chargeCounter = mBatteryManager.getLongProperty(BatteryManager.BATTERY_PROPERTY_CHARGE_COUNTER);
            Long capacity = mBatteryManager.getLongProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);

            if(chargeCounter != null && capacity != null) {
                long value = (long) (((float) chargeCounter / (float) capacity) * 100f);
                return value;
            }
        }
        return 0;
    }

    private BroadcastReceiver batteryInfoReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateBatteryData(intent);
        }
    };

    private void goToMaps(){
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }
}
