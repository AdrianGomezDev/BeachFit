package com.example.beachfitlogin;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

public class Camera extends AppCompatActivity {
    SurfaceView cameraPreview;
    public static final int MY_PERMISSIONS_REQUEST_CAMERA = 4142;
    CameraSource cameraSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        cameraPreview = (SurfaceView) findViewById(R.id.cameraView);
        useCamera();
    }

    private void useCamera() {
        BarcodeDetector barcodeDetector = new BarcodeDetector.Builder(this).build();
        cameraSource = new CameraSource.Builder(this, barcodeDetector).setAutoFocusEnabled(true).setRequestedPreviewSize(1600, 1000).build();
        cameraPreview.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                if (ActivityCompat.checkSelfPermission(Camera.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    // Request permission to use the camera
                    ActivityCompat.requestPermissions(Camera.this,
                            new String[]{Manifest.permission.CAMERA},
                            MY_PERMISSIONS_REQUEST_CAMERA);
                } else {
                    try {
                        cameraSource.start(cameraPreview.getHolder());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });
        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodeSparseArray = detections.getDetectedItems();
                if (barcodeSparseArray.size() > 0) {
                    Intent intent = new Intent();
                    intent.putExtra("barcode", barcodeSparseArray.valueAt(0));
                    setResult(CommonStatusCodes.SUCCESS, intent);
                    finish();
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Reload camera activity
                    finish();
                    startActivity(getIntent());
                } else {
                    Toast.makeText(getApplicationContext(),"Must allow camera access to use this feature.", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        }
    }
}
