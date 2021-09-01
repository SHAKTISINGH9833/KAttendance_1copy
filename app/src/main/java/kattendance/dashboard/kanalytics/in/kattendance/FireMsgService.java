package kattendance.dashboard.kanalytics.in.kattendance;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.provider.Settings;
import androidx.core.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by Devlopment on 22/09/17.
 */
public class FireMsgService extends FirebaseMessagingService {
    //private static final String TAG = FireMsgService.class.getSimpleName();
    private static final String TAG = "FCM Service";

    private Context mContext;
    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder mBuilder;
    public static final String NOTIFICATION_CHANNEL_ID = "10001";


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

      Log.d("HELLO", "From: " + remoteMessage.getFrom());
      Log.d("Message", "Notification Message Body: " + remoteMessage.getNotification().getBody());
      Log.d("Body", "Data Body: " + remoteMessage.getData().get("AnotherActivity"));
      //String userid,authority_id;
      //Calling method to generate notification
      String message = remoteMessage.getNotification().getBody();
      String TrueOrFlase = remoteMessage.getData().get("AnotherActivity");
      String counter = "1";

      //sendNotification();
      //sendNotificationfromserver(message, counter);

      if (TrueOrFlase.trim().equals("false")) {

          sendNotification(message);

      } else if (TrueOrFlase.trim().equals("true")) {

          sendNotificationfromserver(message, counter);

      } else if (TrueOrFlase.trim().equals("leave")) {

          leaveNotificationFromUser(message, counter);
      }

      /*if (remoteMessage.getNotification() != null && remoteMessage.getData() != null) {
        sendNotificationfromserver(message, userid, authority_id);
      } else {
        // Log.e("null","No Value");}*/

        //Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        /*if (remoteMessage.getData().size() > 0) {
          Log.d(TAG, "Message data payload: " + remoteMessage.getData());

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
          Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());

        }

      }*/
    }

  private void leaveNotificationFromUser(String message, String counter) {
    /*int notifyID = 0;
    try {
      //notifyID = Integer.parseInt(brand_id);
    } catch (NumberFormatException e) {

      e.printStackTrace();

    }

    String CHANNEL_ID = "my_channel_01";
    Intent intent=new Intent(this,LeaveApplicants.class);
    intent.putExtra("counter",counter);
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    PendingIntent pendingIntent1 = PendingIntent.getActivity(this, 0, intent,
      PendingIntent.FLAG_ONE_SHOT);
    Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
    NotificationCompat.Builder notificationBuilder1 = new NotificationCompat.Builder(this)
      .setSmallIcon(R.mipmap.ic_icons)
      .setContentTitle("Leave Notification")
      .setContentText(message)
      .setAutoCancel(true)
      .setSound(defaultSoundUri)
      .setContentIntent(pendingIntent1);

    NotificationManager notificationManager =
      (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

    notificationManager.notify(0, notificationBuilder1.build());*/
    Intent resultIntent = new Intent(mContext , LeaveApplicants.class);
    //resultIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    //resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

    PendingIntent resultPendingIntent = PendingIntent.getActivity(mContext,
      0 /* Request code */, resultIntent,
      PendingIntent.FLAG_UPDATE_CURRENT);

    mBuilder = new NotificationCompat.Builder(mContext);
    mBuilder.setSmallIcon(R.mipmap.ic_icons);
    mBuilder.setContentTitle("Leave Notification")
      .setContentText(message)
      .setAutoCancel(true)
      .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
      .setContentIntent(resultPendingIntent);

    mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
    {
      int importance = NotificationManager.IMPORTANCE_HIGH;
      NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "NOTIFICATION_CHANNEL_NAME", importance);
      notificationChannel.enableLights(true);
      notificationChannel.setLightColor(Color.RED);
      notificationChannel.enableVibration(true);
      notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
      assert mNotificationManager != null;
      mBuilder.setChannelId(NOTIFICATION_CHANNEL_ID);
      mNotificationManager.createNotificationChannel(notificationChannel);
    }
    assert mNotificationManager != null;
    mNotificationManager.notify(0 /* Request Code */, mBuilder.build());
  }

  private void sendNotificationfromserver(String messageBody,String counter) {
    /*int notifyID = 0;
    try {
     // notifyID = Integer.parseInt();
    } catch (NumberFormatException e) {
      e.printStackTrace();
    }
    String CHANNEL_ID = "my_channel_01";
      Intent intent=new Intent(this,ListOfApproval.class);
      intent.putExtra("counter",counter);
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
      PendingIntent.FLAG_ONE_SHOT);
    Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
    NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
      .setSmallIcon(R.mipmap.ic_icons)
      .setContentTitle("Message From HR")
      .setContentText(messageBody)
      .setAutoCancel(true)
      .setSound(defaultSoundUri)
      .setContentIntent(pendingIntent);

    NotificationManager notificationManager =
      (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

    notificationManager.notify(0, notificationBuilder.build());*/

    Intent resultIntent = new Intent(mContext , ListOfApproval.class);
    //resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

    PendingIntent resultPendingIntent = PendingIntent.getActivity(mContext,
      0 /* Request code */, resultIntent,
      PendingIntent.FLAG_UPDATE_CURRENT);

    mBuilder = new NotificationCompat.Builder(mContext);
    mBuilder.setSmallIcon(R.mipmap.ic_icons);
    mBuilder.setContentTitle("Message From HR")
      .setContentText(messageBody)
      .setAutoCancel(true)
      .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
      .setContentIntent(resultPendingIntent);

    mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
    {
      int importance = NotificationManager.IMPORTANCE_HIGH;
      NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "NOTIFICATION_CHANNEL_NAME", importance);
      notificationChannel.enableLights(true);
      notificationChannel.setLightColor(Color.RED);
      notificationChannel.enableVibration(true);
      notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
      assert mNotificationManager != null;
      mBuilder.setChannelId(NOTIFICATION_CHANNEL_ID);
      mNotificationManager.createNotificationChannel(notificationChannel);
    }
    assert mNotificationManager != null;
    mNotificationManager.notify(0 /* Request Code */, mBuilder.build());
  }

  private void sendNotification(String message) {
    /*int notifyID = 0;
    try {
     // notifyID = Integer.parseInt(brand_id);
    } catch (NumberFormatException e) {
      e.printStackTrace();
    }
    String CHANNEL_ID = "my_channel_01";
    Intent intent = new Intent(this, NavigationDrawerActivity.class);
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

    Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
    NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_icons)
                .setContentTitle("Kanalyics(In/Out)")
                .setContentText("Notification Recieved")
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

    NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

    notificationManager.notify(0, notificationBuilder.build());*/

    /**Creates an explicit intent for an Activity in your app**/
    Intent resultIntent = new Intent(mContext , MainActivity.class);
    resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

    PendingIntent resultPendingIntent = PendingIntent.getActivity(mContext,
      0 /* Request code */, resultIntent,
      PendingIntent.FLAG_UPDATE_CURRENT);

    mBuilder = new NotificationCompat.Builder(mContext);
    mBuilder.setSmallIcon(R.mipmap.ic_icons);
    mBuilder.setContentTitle("Kanalyics(In/Out)")
      .setContentText(message)
      .setAutoCancel(true)
      .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
      .setContentIntent(resultPendingIntent);

    mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
    {
      int importance = NotificationManager.IMPORTANCE_HIGH;
      NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "NOTIFICATION_CHANNEL_NAME", importance);
      notificationChannel.enableLights(true);
      notificationChannel.setLightColor(Color.RED);
      notificationChannel.enableVibration(true);
      notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
      assert mNotificationManager != null;
      mBuilder.setChannelId(NOTIFICATION_CHANNEL_ID);
      mNotificationManager.createNotificationChannel(notificationChannel);
    }
    assert mNotificationManager != null;
    mNotificationManager.notify(0 /* Request Code */, mBuilder.build());

    }




}




