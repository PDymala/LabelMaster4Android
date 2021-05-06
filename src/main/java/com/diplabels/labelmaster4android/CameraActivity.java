package com.diplabels.labelmaster4android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

import java.util.Collections;

public class CameraActivity extends AppCompatActivity {
    private CodeScanner mCodeScanner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);


        CodeScannerView scannerView = findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(this, scannerView);


        String temp = getIntent().getStringExtra(("CodeType"));
        if (temp.equals("QM")){
            mCodeScanner.setFormats(Collections.singletonList(BarcodeFormat.QR_CODE));
        } else if (temp.equals("DM")){
            mCodeScanner.setFormats(Collections.singletonList(BarcodeFormat.DATA_MATRIX));
        } else {
            mCodeScanner.setFormats(CodeScanner.ALL_FORMATS);
        }
        ;


        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {

                Intent returnIntent = new Intent();
                returnIntent.putExtra("result", String.valueOf(result));
                setResult(Activity.RESULT_OK,returnIntent);
                finish();


            }
        });
        mCodeScanner.startPreview();


    }
    @Override
    protected void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
    }

    @Override
    protected void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }
}