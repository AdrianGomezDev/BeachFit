package com.example.beachfitlogin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceView;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.barcode.BarcodeDetector;

public class camera extends AppCompatActivity {
    SurfaceView cameraPreview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        cameraPreview = (SurfaceView) findViewById(R.id.cameraView);

    }

    private void useCamera(){
        BarcodeDetector barcodeDetector = new BarcodeDetector.Builder(this).build();
        CameraSource cameraSource = new CameraSource.Builder(this, barcodeDetector).build();

    }
}
