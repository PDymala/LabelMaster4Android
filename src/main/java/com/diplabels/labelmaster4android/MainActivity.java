package com.diplabels.labelmaster4android;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
/**
 * Application for verification of genuine of a specially designed label
 *
 * @author Piotr Dymala p.dymala@gmail.com
 * @version 1.0
 * @since 2021-05-06
 */


/*
This software uses machine learning to verify if data in QR code i correct.
Note that data printed in QR code have to be prepared with use of LabelMaster4.0 software. No other will work.
 */




public class MainActivity extends AppCompatActivity {
    Button buttonScanQR;
    Button buttonReset;
    TextView textQRStatus;
    TextView textCodesVerif;
    View viewColoredBar;
    View viewColoredBar2;
    private String qrValue = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        permission();

    }

    private void permission() {

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                            Manifest.permission.CAMERA
                    },
                    100);

        }


    }

    public void init() {

        buttonScanQR = (Button) findViewById(R.id.buttonScanQR);
        buttonReset = (Button) findViewById(R.id.buttonReset);
        textQRStatus = (TextView) findViewById(R.id.textQRStatus);
        textCodesVerif = (TextView) findViewById(R.id.textCodesVerif);
        viewColoredBar = (View) findViewById(R.id.colored_bar);
        viewColoredBar2 = (View) findViewById(R.id.colored_bar2);
        viewColoredBar.setBackgroundColor(Color.WHITE);
        viewColoredBar2.setBackgroundColor(Color.WHITE);


    }

    public void scanQRCode(View view) {


        int LAUNCH_SECOND_ACTIVITY = 100;
        Intent i = new Intent(this, CameraActivity.class);
        i.putExtra("CodeType", "QR");
        startActivityForResult(i, LAUNCH_SECOND_ACTIVITY);


    }
    public void checkCodes() {

        if (qrValue.isEmpty() ) {
            textCodesVerif.setText("Scan codes");
        } else {


            SingleTestOnModel stom = new SingleTestOnModel(this);



            if (stom.singleTest(qrValue)) {
                textCodesVerif.setText("Code ok");
                viewColoredBar.setBackgroundColor(Color.GREEN);
                viewColoredBar2.setBackgroundColor(Color.GREEN);
            } else {
                textCodesVerif.setText("Code not ok");
                viewColoredBar.setBackgroundColor(Color.RED);

                viewColoredBar2.setBackgroundColor(Color.RED);

            }


        }

    }

    public void reset(View view) {
        textQRStatus.setText("X");
        qrValue = "";
         viewColoredBar.setBackgroundColor(Color.WHITE);
        viewColoredBar2.setBackgroundColor(Color.WHITE);

        checkCodes();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            if (resultCode == Activity.RESULT_OK) {

                qrValue = data.getStringExtra("result");
                checkCodes();

                textQRStatus.setText("OK");



            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
                textQRStatus.setText("Scanning canceled");
            }

        }

    }

}