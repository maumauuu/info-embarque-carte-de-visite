package com.CDV;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.util.Log;

public class MyReceiver extends BroadcastReceiver {
    private MainActivity act;
    public MyReceiver(MainActivity act) {
        this.act = act;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        assert(intent.getAction().equals(Telephony.Sms.Intents.SMS_RECEIVED_ACTION));
        SmsMessage [] messages = Telephony.Sms.Intents.getMessagesFromIntent(intent);

        for(SmsMessage message : messages) {
            String body = message.getMessageBody();
            Log.d("MyReceiver:", body);
            if (body == null || !body.startsWith(act.getExpectedPrefix())) {
                Log.d("MyReceiver:", "Not the right prefix => skipped");
                continue;
            }
            String from = message.getOriginatingAddress();
            if (body == null ){
                Log.d("MyReceiver:", "wrong number");
                continue;
            }
            act.sms(from, body);
        }

    }
}
