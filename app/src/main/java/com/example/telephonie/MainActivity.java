package com.example.telephonie;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Telephony;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import static android.Manifest.permission.CALL_PHONE;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int PERMISSIONS_REQUEST_PHONE_CALL = 100;
    private SmsMessage currentSMS;
    public static TextView sms_txt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        envoieSMS();

        Intent broadcastIntent = new Intent();
        broadcastIntent.putExtra("your key", "your Value");
        broadcastIntent.setAction("link from  you have recieve a text");
        this.sendBroadcast(broadcastIntent);
    }

    @Override
    public void onClick(View view) {

        if (view.getId()==R.id.btn_etat_telephone)
        {
            etatDuTelphone();
        }
        if(view.getId()==R.id.btn_telephoner)
        {
            passerAppel();
        }
        if(view.getId()==R.id.btn_quitter)
        {
            finish();
        }
    }

    private void envoieSMS()
    {
        String mes = "SMS TEST";
        String num = "06810920";
        PendingIntent pi = PendingIntent.getActivities(this, 0, new Intent[]{new Intent(this, ReceptionSMS.class)}, 0);
        SmsManager sms = SmsManager.getDefault();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[] { Manifest.permission.SEND_SMS}, 1);
        } else {
            sms.sendTextMessage(num, null, mes.toString(), pi, null);
        }
    }

    private void etatDuTelphone() {
        TelephonyManager tel = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        int etat = tel.getCallState();
        // Etat inactif
        if (etat == TelephonyManager.CALL_STATE_IDLE) {
            Toast.makeText(this, "Telephone inactif", Toast.LENGTH_SHORT).show();
        }
        // le telephone sonne
        if (etat == TelephonyManager.CALL_STATE_RINGING) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            Toast.makeText(this, "le telephone sonne : appel du numero : " + tel.getSimSerialNumber(), Toast.LENGTH_SHORT).show();
        }
        //le telephone est utilisé
        if (etat == TelephonyManager.CALL_STATE_OFFHOOK) {
            Toast.makeText(this, "Telephone en cours d'utilisation", Toast.LENGTH_SHORT).show();
        }
    }

    public void passerAppel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, PERMISSIONS_REQUEST_PHONE_CALL);
        } else {
            //Open call function
            String number = "+33670160517";
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:" + number));
            startActivity(intent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_PHONE_CALL) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                passerAppel();

            } else {
                Toast.makeText(this, "Sorry!!! Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

}

