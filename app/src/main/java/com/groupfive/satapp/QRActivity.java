package com.groupfive.satapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.groupfive.satapp.commons.Constants;
import com.groupfive.satapp.commons.MyApp;
import com.groupfive.satapp.commons.SharedPreferencesManager;
import com.groupfive.satapp.ui.inventariable.InvDetailActivity;
import com.groupfive.satapp.ui.tickets.ticketdetail.TicketDetailActivity;

public class QRActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_q_r);

        IntentIntegrator intent = new IntentIntegrator(QRActivity.this);
        intent.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);

        intent.setPrompt("Scan");
        intent.setCameraId(0);
        intent.setBeepEnabled(false);
        intent.setBarcodeImageEnabled(false);
        intent.initiateScan();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null){
            if (result.getContents() == null){
            }
            else{
                String base = result.getContents();
                if (base.contains("e-")){
                    Intent i = new Intent(QRActivity.this, InvDetailActivity.class);
                    i.putExtra("id",result.getContents().split("-")[1]);
                    startActivity(i);
                }else if (base.contains("t-")){
                    Intent i = new Intent(QRActivity.this, TicketDetailActivity.class);
                    i.putExtra(Constants.EXTRA_TICKET_ID,result.getContents().split("-")[1]);
                    startActivity(i);
                    Toast.makeText(this, "entra", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this, "El codigo QR leido no se reconoce.", Toast.LENGTH_SHORT).show();
                }
                finish();
            }
        }
        else
        {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        finish();
    }
}
