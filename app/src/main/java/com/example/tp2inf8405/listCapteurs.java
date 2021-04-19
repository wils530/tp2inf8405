package com.example.tp2inf8405;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class listCapteurs extends AppCompatActivity {


    Button btnLightSensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_capteurs);

        btnLightSensor = (Button) findViewById(R.id.btnLightSensor);

        btnLightSensor.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent intent = new Intent(listCapteurs.this, LightSensorActivity.class);
                startActivity(intent);
            }
        });
    }
}