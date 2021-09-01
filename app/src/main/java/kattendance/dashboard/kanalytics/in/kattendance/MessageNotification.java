package kattendance.dashboard.kanalytics.in.kattendance;

import androidx.core.app.NotificationCompat;

public class MessageNotification  {
  /*private static final String TAG = "FCM Service";
  @Override
  public void onMessageReceived(RemoteMessage remoteMessage) {
    Log.d(TAG, "From: " + remoteMessage.getFrom());
    Log.d(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());

    //Calling method to generate notification
    sendNotification(remoteMessage.getNotification().getBody());
  }

  private void sendNotification(String messageBody) {
    Intent intent = new Intent(this, NavigationDrawerActivity.class);
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
      PendingIntent.FLAG_ONE_SHOT);

    Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
    NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
      .setSmallIcon(R.mipmap.ic_icons)
      .setContentTitle("Mukhi")
      .setContentText(messageBody)
      .setAutoCancel(true)
      .setSound(defaultSoundUri)
      .setContentIntent(pendingIntent);

    NotificationManager notificationManager =
      (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

    notificationManager.notify(0, notificationBuilder.build());
  }
*/
}
