package kattendance.dashboard.kanalytics.in.kattendance;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MyAlarm extends BroadcastReceiver {


  //the method will be fired when the alarm is triggerred
  @Override
  public void onReceive(Context context, Intent intent) {

    Intent i = new Intent(context, AlarmService.class);
    context.startService(i);

  }

}



