package com.example.tp2inf8405;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class LightSensorActivity extends AppCompatActivity {

    private Sensor lightSensor;
    private TextView temperaturelabel;
    private Sensor temperature;
    private SensorManager lightSensorManager;
    private SensorEventListener lightEventListener;
    private SensorEventListener tempEventListener;
    private TextView lightTextView;
    private View root;
    private double maxValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_light_sensor);

        root = findViewById(R.id.root);

        lightTextView = (TextView) findViewById(R.id.lightTextView);
        temperaturelabel = (TextView) findViewById(R.id.temperatureTextView);

        //Get an instance of SensorManager//
        lightSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        //Check for a light sensor//
        lightSensor = lightSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        temperature = lightSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        //If the light sensor doesn’t exist, then display an error message//
        String sensor_error = getResources().getString(R.string.no_sensor);
        String sensor_error_temp = getResources().getString(R.string.no_sensor_temp);
        if (lightSensor == null) { lightTextView.setText(sensor_error); }
        if (temperature == null) { temperaturelabel.setText(sensor_error_temp); }

        // max value for light sensor
        maxValue = lightSensor.getMaximumRange();

        temperaturelabel.setText("");

        lightEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                if (sensorEvent.sensor.getType() != Sensor.TYPE_LIGHT) return;
                double value = sensorEvent.values[0];
                //getSupportActionBar().setTitle("Luminosity : " + value + " lx");

                // between 0 and 255
                int newValue = (int) ((255.0 * value) / maxValue);
                lightTextView.setText(getResources().getString(R.string.light_sensor, (float)value));
                //root.setBackgroundColor(Color.rgb(newValue, newValue, newValue));


                Log.i("SenSorBackground1",  Integer.toString(newValue));
                Log.i("SenSorBackground2",  Double.toString(value));
                Log.i("SenSorBackground3",  Double.toString(maxValue));
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        };
        tempEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                    String textInput = "acceleration en x: " + String.format("%.2f", sensorEvent.values[0]) + " m/s2" + '\n' +
                            "acceleration en y: " + String.format("%.2f", sensorEvent.values[1]) + " m/s2" + '\n' +
                            "acceleration en z: " + String.format("%.2f", sensorEvent.values[2]) + " m/s2" + '\n' ;
                    temperaturelabel.setText(textInput);

                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        };
    }



    @Override
    protected void onResume() {
        super.onResume();
        lightSensorManager.registerListener(lightEventListener, lightSensor, SensorManager.SENSOR_DELAY_FASTEST);
        lightSensorManager.registerListener(tempEventListener, temperature, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        lightSensorManager.unregisterListener(lightEventListener);
        lightSensorManager.unregisterListener(tempEventListener);
    }
}