package multunus.buyittracker;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Telephony;
import android.support.v7.app.NotificationCompat;
import android.telephony.SmsMessage;
import android.util.Log;

public class SMSReader extends BroadcastReceiver {
    public SMSReader() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("BuytItTracker","inside onReceive of BroadcastReceiver");
        Bundle bundle = intent.getExtras();
        SmsMessage[] messages = null;
        String messageBody;
        if (bundle != null) {
            try {
//                    Object[] pdus = (Object[]) bundle.get("pdus");
//                    messages = new SmsMessage[pdus.length];
                messages = Telephony.Sms.Intents.getMessagesFromIntent(intent);
                Log.d("BuytItTracker","inside if");
                for (int i = 0; i < messages.length; i++) {
                    Log.d("BuytItTracker","inside loop "+i);
//                        messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);

                    messageBody = messages[i].getMessageBody();

                    //or just check in the message for a word
                    String content = "";
                    if (messageBody.toLowerCase().contains("hdfcbank credit card ending 8216".toLowerCase())) {
                        content = messageBody;
                    }
                    else{
                        content = "Non HDFC SMS";
                    }

                    NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
                    builder.setAutoCancel(false);
                    builder.setDefaults(Notification.DEFAULT_ALL);
                    builder.setWhen(System.currentTimeMillis());
                    String title = "BuyIt Tracker";
                    builder.setContentTitle(title);
                    builder.setSmallIcon(R.drawable.ic_stat_buyit);

                    builder.setContentText(content);
                    builder.setTicker(title);

                    Intent actionIntent = new Intent(context, BlankActivity.class);
                    PendingIntent actionPendingIntent = PendingIntent.getActivity(context, 0, actionIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT);
                    builder.addAction(android.R.drawable.ic_menu_send, "Open", actionPendingIntent);

                    NotificationManager manager = (NotificationManager) context.getSystemService(
                            Context.NOTIFICATION_SERVICE);
                    manager.notify(0, builder.build());
                    Log.d("BuytItTracker", "set notification " + i);

                }
            } catch (Exception e) {
                Log.e("Exception caught", e.getMessage());
            }
        }
    }
}