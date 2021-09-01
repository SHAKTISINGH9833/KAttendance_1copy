package kattendance.dashboard.kanalytics.in.kattendance;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.Context;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import java.util.Calendar;

public class AlarmService extends IntentService {


  public AlarmService(String name) {
    super(name);
  }

  public AlarmService() {
    super("AlarmService");
  }

  @Override
  protected void onHandleIntent(@Nullable Intent intent) {
    scheduleNotificationWithButton(getApplicationContext(),"You have not check-out for the day");
  }

  private static void scheduleNotificationWithButton(Context context, String message) {

    //What happen when you will click on button
    Intent intent = new Intent(context, NavigationDrawerActivity.class);
    PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, intent, PendingIntent.FLAG_ONE_SHOT);
    NotificationManager notificationManager = (NotificationManager)
      context.getSystemService(Context.NOTIFICATION_SERVICE);

    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
      NotificationChannel channel = new NotificationChannel("simplifiedcoding", "simplifiedcoding", NotificationManager.IMPORTANCE_DEFAULT);
      notificationManager.createNotificationChannel(channel);
    }

    //Notification
    NotificationCompat.Builder notification = new NotificationCompat.Builder(context, "simplifiedcoding")
      .setContentText("KAttendance")
      .setContentTitle(message)
      .setAutoCancel(true)
      .setContentIntent(pendingIntent)
      .setSmallIcon(R.mipmap.ic_icons);

    //Send notification

    notificationManager.notify(1, notification.build());
  }

}
