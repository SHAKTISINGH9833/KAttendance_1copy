package kattendance.dashboard.kanalytics.in.kattendance;

import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import androidx.legacy.content.WakefulBroadcastReceiver;

/**
 * Created by Devlopment on 22/09/17.
 */
public class FirebaseDataReceiver extends WakefulBroadcastReceiver {

        private final String TAG = "FirebaseDataReceiver";

    public void onReceive(Context context, Intent intent) {
        //Log.d(TAG, "I'm in!!!");

        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(context, notification);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
