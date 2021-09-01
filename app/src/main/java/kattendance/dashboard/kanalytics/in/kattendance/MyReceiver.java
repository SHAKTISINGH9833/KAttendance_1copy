package kattendance.dashboard.kanalytics.in.kattendance;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by Devlopment on 14/09/17.
 */
public class MyReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        Toast.makeText(context, "Times Up!!!!", Toast.LENGTH_LONG).show();
    }

}
