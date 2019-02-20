package com.example.beachfitlogin;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;

public class QR_Scan extends AppCompatActivity {
    TextView barcodeResultText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr__scan);
        barcodeResultText = (TextView)findViewById(R.id.barcodeResultText);


    }
    public void scanBarcode(View view)
    {
        Intent intent = new Intent(this, Camera.class);
        startActivityForResult(intent,0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode== 0){
            if (resultCode==CommonStatusCodes.SUCCESS)
            {
                if (data != null)
                {
                    Barcode barcode = data.getParcelableExtra("barcode");
                    barcodeResultText.setText("P"+barcode.displayValue+"P");
                    barcodeCheck(barcode);
                }else{
                    barcodeResultText.setText("No barcode found");
                }
            }

        }else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    protected void barcodeCheck(Barcode barcode){
        Toast.makeText(getApplicationContext(), "BarcodeCheck", Toast.LENGTH_LONG).show();
        if(barcode.displayValue.contentEquals("Hi")){
            startActivity(new Intent(QR_Scan.this, Fitness.class));
        }
        else{
            Toast.makeText(getApplicationContext(),"Does not match", Toast.LENGTH_LONG).show();
        }
    }
}
