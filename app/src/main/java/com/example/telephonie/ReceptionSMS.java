package com.example.telephonie;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class ReceptionSMS extends BroadcastReceiver {

    private SmsMessage currentSMS;
    private String message;

    @Override
    public void onReceive(Context ctx, Intent intent) {
        //lecture du dernier message recu
        if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
            Bundle bundle = intent.getExtras();

            Object[] pdu_Objects = (Object[]) bundle.get("pdus");

            if (bundle != null) {
                for(Object aObject : pdu_Objects)
                {
                    currentSMS = getIncomingMessage(aObject, bundle);
                    String senderNo = currentSMS.getDisplayOriginatingAddress();
                    message = currentSMS.getMessageBody();
                    Toast.makeText(ctx, "Vous avez recu un nouveau SMS : " +"\n"+ "Expediteur : "+senderNo+ "\n" + "Message " + message, Toast.LENGTH_SHORT).show();
                }
                this.abortBroadcast();
            }
        }
    }

    private SmsMessage getIncomingMessage(Object aObject, Bundle bundle) {
        SmsMessage currentSMS;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String format = bundle.getString("format");
            currentSMS = SmsMessage.createFromPdu((byte[]) aObject, format);

            return currentSMS;
        }
        return null;
    }
}
