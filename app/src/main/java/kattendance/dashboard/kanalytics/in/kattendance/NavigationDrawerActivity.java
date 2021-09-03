package kattendance.dashboard.kanalytics.in.kattendance;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Camera;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.inforoeste.mocklocationdetector.MockLocationDetector;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Devlopment on 14/09/17.
 */
public class NavigationDrawerActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener, OnMapReadyCallback,
  GoogleApiClient.ConnectionCallbacks,
  GoogleApiClient.OnConnectionFailedListener,
  GoogleMap.OnMarkerDragListener,
  GoogleMap.OnMapLongClickListener,
  View.OnClickListener, LocationListener {
  private static final int CAMERA_PICTURE = 1;
  private DrawerLayout drawerLayout;
  private ProgressDialog pDialog;
  private Toolbar toolbar;
  private static final String BACKLOCK_LIST = "https://dashboard.kanalytics.in/mobile_app/attendance/backlog_check.php";
  String  device_imei;
  SharedPreferences pref;
  ImageView circularButtonIn;
  Button circularButtonSubmit;
  GPSTracker gps;
  final Context context = this;
  private static final String TAG = "NavigationDrawerActvity";
  //CameraSource mCameraSource;
  String counter;
  Dialog dialog;
  SessionManager session;
  private static final int REQUEST_CHECK_SETTINGS = 214;
  TextView txtUsername, txtOfficeAddress, txtOfcInOut, txtError;
  private static String user_id, user_name, office_address, ofc_in, ofc_out, team, mobile_number, ofc_latitude, ofc_longitude, stored_date, imei_no, dept_id;
  String txtReason, requestleave_id;
  private static String CHECK_USER = "https://dashboard.kanalytics.in/mobile_app/attendance/check_user.php";
  private static String CHECK_TL_STATUS = "https://dashboard.kanalytics.in/mobile_app/attendance/check_tl_status.php";
  private static String FILE_UPLOAD_URL = "https://dashboard.kanalytics.in/mobile_app/attendance/proof_pic.php";
  private static String SEND_NOTIFICATION_AMDIN = "https://dashboard.kanalytics.in/mobile_app/attendance/notification_pro.php";
  private static String REGISTRATION_TOKEN = "https://dashboard.kanalytics.in/mobile_app/attendance/regristration_token.php";
  private static String ATTENDANCE_LINK = "https://dashboard.kanalytics.in/mobile_app/attendance/attendance.php";
  private static String NOTI_BADGE_STATUS = "https://dashboard.kanalytics.in/mobile_app/attendance/noti_badge_status.php";
  private static String NOTI_BADGE_STATUS_TO_ZERO = "https://dashboard.kanalytics.in/mobile_app/attendance/noti_badge_status_to_zero.php";
  private static String ADMIN_NOTI_BADGE_STATUS_LEAVE_TO_ZERO = "https://dashboard.kanalytics.in/mobile_app/attendance/admin_noti_badge_status_leave_zero.php";
  private static String ADMIN_NOTI_BADGE_STATUS_LEAVE = "https://dashboard.kanalytics.in/mobile_app/attendance/admin_noti_badge_status_leave.php";
  private static final int IMAGE_CAPTURE = 1;
  private GoogleMap mMap;
  private GoogleApiClient googleApiClient;
  //To store longitude and latitude from map
  private static double longitude;
  private static double latitude;
  Intent intent;
  NavigationView navigationView;
  TextView approvalList, badger, leaveApplicants;
  Bitmap chosenImage;
  File destination = null;
  String filePath = null;
  Location location;
  private SettingsClient mSettingsClient;
  String address;
  private LocationSettingsRequest mLocationSettingsRequest;
  private static final int REQUEST_ENABLE_GPS = 516;
  private ProgressDialog progress;
  private LocationRequest locationRequest;
  private FusedLocationProviderClient fusedLocationClient;
  private LocationCallback mLocationCallback;
  List<Backlockmodel> backlocklist =new ArrayList<>();

  ArrayList<String> Backlockarray=new ArrayList();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.navigation_activity);
    SharedPreferences shared = getSharedPreferences("info", MODE_PRIVATE);

    device_imei = shared.getString("deviceIMEI", "");

    AppUpdateChecker appUpdateChecker=new AppUpdateChecker(this);  //pass the activity in constructure
    appUpdateChecker.checkForUpdate(false);
    initView();

  }

  private void initView() {
    checkConnection();


    SharedPreferences shared = getSharedPreferences("info", MODE_PRIVATE);
    SharedPreferences Status = getSharedPreferences("Status", MODE_PRIVATE);
    requestleave_id = shared.getString("requestleave_id", "");
    user_id = shared.getString("user_id", "");
    user_name = shared.getString("user_name", "");
    office_address = shared.getString("office_address", "");
    ofc_in = shared.getString("office_in", "");
    ofc_out = shared.getString("office_out", "");
    team = shared.getString("team", "");
    dept_id = shared.getString("dept_id", "");
    FirebaseApp.initializeApp(NavigationDrawerActivity.this);
    String tkn = FirebaseInstanceId.getInstance().getToken();
    mobile_number = shared.getString("contact", "");
    ofc_latitude = shared.getString("ofc_lat", "");
    ofc_longitude = shared.getString("ofc_long", "");
    stored_date = shared.getString("stored_date", "");
    imei_no = shared.getString("imei_no", "");
    View content = findViewById(R.id.contentMain);
    intent = getIntent();
    circularButtonSubmit = content.findViewById(R.id.attendanceButton);

    Calendar calendar = Calendar.getInstance();
    DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
    Date today = calendar.getTime();
    String todayAsString = dateFormat.format(today);
    String serverdate=Status.getString("TodateHome", "");
    Log.i("serevdatelog",serverdate);
    if (Status.getString("TodateHome", "").equals(todayAsString)) {
      if (Status.getInt("check_out", 0) == 2) {
        circularButtonSubmit.setText("check out");
      }else if(Status.getInt("check_out", 0) == 1) {
        circularButtonSubmit.setText("check out");
      }

    } else {
      circularButtonSubmit.setText("Check-In");
      SharedPreferences.Editor editor = Status.edit();
      editor.putInt("check_out", 0);
    }


    counter = intent.getStringExtra("counter");
    navigationView = (NavigationView) findViewById(R.id.navigation_view);
    approvalList = (TextView) MenuItemCompat.getActionView(navigationView.getMenu().findItem(R.id.approvallist));
    leaveApplicants = (TextView) MenuItemCompat.getActionView(navigationView.getMenu().findItem(R.id.leaveapplicants));
    badger = (TextView) findViewById(R.id.badger);
    toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    txtUsername = (TextView) content.findViewById(R.id.txtUsername);
    txtOfficeAddress = (TextView) content.findViewById(R.id.txtOfficeAddress);
    txtOfcInOut = (TextView) content.findViewById(R.id.txtOfficeInout);
    txtUsername.setText(user_name);
    txtOfficeAddress.setText(office_address);
    txtOfcInOut.setText(getFormatedDateTime(ofc_in, "HH:mm:ss", "hh:mm a") + "-" + getFormatedDateTime(ofc_out, "HH:mm:ss", "hh:mm a"));
    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
    mapFragment.getMapAsync(this);
    fusedLocationClient = LocationServices.getFusedLocationProviderClient(NavigationDrawerActivity.this);
    mLocationCallback = new LocationCallback() {
      @Override
      public void onLocationResult(LocationResult locationResult) {
        for (Location locations : locationResult.getLocations()) {
          gps = new GPSTracker(NavigationDrawerActivity.this);
          if(location == null && gps != null){
            location = gps.getLocation();
          }else {
            location = locations;
          }
          fusedLocationClient.removeLocationUpdates(mLocationCallback);
        }
      };
    };

    googleApiClient = new GoogleApiClient.Builder(this)
      .addConnectionCallbacks(this)
      .addOnConnectionFailedListener(this)
      .addApi(LocationServices.API)
      .build();

    pDialog = new ProgressDialog(this);
    pDialog.setCancelable(false);
    session = new SessionManager(getApplicationContext());
    circularButtonIn = (ImageView) content.findViewById(R.id.attendanceButtonIn);


    circularButtonIn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        new Handler().postDelayed(new Runnable() {
          @Override
          public void run() {
            getCurrentLocation(1);
          }
        }, 1000);
      }
    });

    circularButtonSubmit.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        checkConnection();
        Checkbacklock(user_id);
        String[] permissions = new String[]{
          android.Manifest.permission.ACCESS_NETWORK_STATE,
          android.Manifest.permission.ACCESS_FINE_LOCATION,
          android.Manifest.permission.ACCESS_COARSE_LOCATION,
          android.Manifest.permission.READ_SMS,
          android.Manifest.permission.READ_PHONE_STATE};
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permissions) {
          int result = ContextCompat.checkSelfPermission(NavigationDrawerActivity.this, p);
          if (result != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(p);
          }
        }

        if (!listPermissionsNeeded.isEmpty()) {
          boolean ans = backlocklist.isEmpty();
          if(listPermissionsNeeded.size() > -1){
            if(ans == false)
            {
              Toast.makeText(NavigationDrawerActivity.this, "Plz Updated Logout 1st", Toast.LENGTH_SHORT).show();
              Intent backlcok = new Intent(NavigationDrawerActivity.this, BacklockList.class);
              startActivity(backlcok);
            }else {

              showLoadingDialog();

            }




          }else{
            dismissLoadingDialog();

          }
          requestForSpecificPermission();

        } else {
          openGpsEnableSetting();

        }

      }
    });

    checkUser(mobile_number, imei_no);
    senRegistrationToken(tkn);
    initNavigationDrawer();
    checkNotificationStatusForLeave(user_id);
    checkNotificationStatusForLeaveAuth(user_id);
    check_tl_status(user_id);
  }

  private void setAlarm(long time, String jsonMainNode) {

    AlarmManager alarmManager = (AlarmManager) getSystemService(NavigationDrawerActivity.ALARM_SERVICE);
    //creating a new intent specifying the broadcast receiver
    Intent i = new Intent(this, MyAlarm.class);
    //creating a pending intent using the intent
    PendingIntent pendingIntent = PendingIntent.getBroadcast(NavigationDrawerActivity.this, 1, i, 0);
    if (alarmManager != null) {
      if (Build.VERSION.SDK_INT >= 23) {
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,
          time, pendingIntent);
      } else if (Build.VERSION.SDK_INT >= 19) {
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, time, pendingIntent);
      } else {
        alarmManager.set(AlarmManager.RTC_WAKEUP, time, pendingIntent);
      }
    }
    Toast.makeText(NavigationDrawerActivity.this, jsonMainNode, Toast.LENGTH_SHORT).show();
  }

  public void showLoadingDialog() {


    if (progress == null) {
      progress = new ProgressDialog(this);
      progress.setTitle("");
      progress.setMessage("Please Wait");
    }
    progress.show();
  }

  public void dismissLoadingDialog() {


    if (progress != null && progress.isShowing()) {
      progress.dismiss();
    }
  }

  public static String getFormatedDateTime(String dateStr, String strReadFormat, String strWriteFormat) {


    String formattedDate = dateStr;

    DateFormat readFormat = new SimpleDateFormat(strReadFormat, Locale.getDefault());
    DateFormat writeFormat = new SimpleDateFormat(strWriteFormat, Locale.getDefault());

    Date date = null;

    try {
      date = readFormat.parse(dateStr);
    } catch (ParseException e) {
    }

    if (date != null) {
      formattedDate = writeFormat.format(date);
    }

    return formattedDate;
  }

  private void check_tl_status(final String user_id) {


    String tag_string_req = "req_register";
    //Toast.makeText(this, "Checking user", Toast.LENGTH_SHORT).show();
    StringRequest strReq = new StringRequest(Request.Method.POST, CHECK_TL_STATUS, new Response.Listener<String>() {
      @Override
      public void onResponse(String response) {

        try {
          JSONObject jObj = new JSONObject(response);
          JSONArray jsonMainNode = jObj.optJSONArray("results");
          JSONObject result = jsonMainNode.getJSONObject(0);
          String error = result.getString("error");
          if (error.trim().equals("true")) {
            /*tl_status="0";
            Toast.makeText(NavigationDrawerActivity.this, ""+tl_status, Toast.LENGTH_SHORT).show();*/
            showNavigationItem();
          } else {
            hideNavigationItem();
            /*tl_status="1";
            Toast.makeText(NavigationDrawerActivity.this, ""+tl_status, Toast.LENGTH_SHORT).show();*/
          }

        } catch (JSONException e) {
          e.printStackTrace();
        }
      }
    }, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {

      }
    }) {

      @Override
      protected Map<String, String> getParams() {


        Map<String, String> params = new HashMap<String, String>();

        params.put("user_id", user_id);
        return params;
      }

    };
    MyApplication.getInstance().addToRequestQueue(strReq, tag_string_req);
  }

  private void checkUser(final String mobile_number, final String imei_no) {

    String tag_string_req = "req_register";

    //Toast.makeText(this, "Checking user", Toast.LENGTH_SHORT).show();
    StringRequest strReq = new StringRequest(Request.Method.POST, CHECK_USER, new Response.Listener<String>() {
      @Override
      public void onResponse(String response) {

        try {
          JSONObject jObj = new JSONObject(response);
          JSONArray jsonMainNode = jObj.optJSONArray("results");
          JSONObject result = jsonMainNode.getJSONObject(0);
          String error = result.getString("error");
          if (error.trim().equals("true")) {
            //Toast.makeText(NavigationDrawerActivity.this, "error : "+error, Toast.LENGTH_SHORT).show();
            String dept_id = result.getString("dept_id");
            if (dept_id.trim().equals("NULL") || dept_id.trim().equals("0")) {
//              AlertDialog.Builder alertBuilder = new AlertDialog.Builder(NavigationDrawerActivity.this);
//              alertBuilder.setMessage("We have updated to new version");
//              alertBuilder.setCancelable(true);
//
//              alertBuilder.setPositiveButton(
//                "Yes",
//                new DialogInterface.OnClickListener() {
//                  public void onClick(DialogInterface dialog, int id) {
//                    //Toast.makeText(NavigationDrawerActivity.this, "Send to Registration page", Toast.LENGTH_SHORT).show();
//                    //deleteGeneratedId(user_id,expense_id);
//                    // dialog.cancel();
//                    updateUserStatus = "true";
//                    Intent mainIntent = new Intent(NavigationDrawerActivity.this, Registration.class);
//                    mainIntent.putExtra("updateUserStatus", updateUserStatus);
//                    startActivity(mainIntent);
//                  }
//                });
//
//              alertBuilder.setNegativeButton(
//                "No",
//                new DialogInterface.OnClickListener() {
//                  public void onClick(DialogInterface dialog, int id) {
//                    dialog.cancel();
//                    finish();
//                  }
//                });
//
//              builder1 = alertBuilder.create();
//              builder1.show();
            } else {

            }
          } else {

          }

        } catch (JSONException e) {
          e.printStackTrace();
        }
      }
    }, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {

      }
    }) {

      @Override
      protected Map<String, String> getParams() {


        Map<String, String> params = new HashMap<String, String>();

        params.put("user_contact", mobile_number);
        params.put("user_phone_imei", imei_no);
        return params;
      }

    };
    MyApplication.getInstance().addToRequestQueue(strReq, tag_string_req);
  }

  /*private void moveToNextActivity(String name,String contact,String office_address,String existUser) {
    Intent intent=new Intent(NavigationDrawerActivity.this,Registration.class);
    intent.putExtra("name",name);
    intent.putExtra("contact",contact);
    intent.putExtra("office_address",office_address);
    intent.putExtra("existUser",existUser);
    startActivity(intent);
  }*/

  private void checkNotificationStatusForLeaveAuth(final String user_id) {

    String tag_string_req = "req_register";


    StringRequest strReq = new StringRequest(Request.Method.POST,
      ADMIN_NOTI_BADGE_STATUS_LEAVE, new Response.Listener<String>() {

      @Override
      public void onResponse(String response) {

        //Toast.makeText(getApplicationContext(),"REGISTRATION_TOKEN "+response,Toast.LENGTH_LONG).show();

        try {
          JSONObject jObj = new JSONObject(response);
          JSONArray jsonMainNode = jObj.optJSONArray("results");

          JSONObject result = jsonMainNode.getJSONObject(0);
          String error = result.getString("error");
          if (error.trim().equals("true")) {
            String notify_admin = result.getString("notify_admin");
            //Toast.makeText(getApplicationContext(), "notify_admin : "+notify_admin, Toast.LENGTH_LONG).show();
            if (notify_admin.trim().equals("1")) {
              initializeCountDrawerLeaveAuth();
              badger.setVisibility(View.VISIBLE);
            }

          }
          else {

          }
        } catch (JSONException e) {
          e.printStackTrace();
        }
      }
    }, new Response.ErrorListener() {

      @Override
      public void onErrorResponse(VolleyError error) {
        // Log.e(TAG, "Registration Error: " + error.getMessage());
        Toast.makeText(getApplicationContext(),
          error.getMessage(), Toast.LENGTH_LONG).show();

      }
    }) {

      @Override
      protected Map<String, String> getParams() {

        Map<String, String> params = new HashMap<String, String>();
        params.put("user_id", user_id);
        return params;
      }

    };

    MyApplication.getInstance().addToRequestQueue(strReq, tag_string_req);
  }

  private void showNavigationItem() {
    Menu nav_menu = navigationView.getMenu();
//    nav_menu.findItem(R.id.leaveapplicants).setVisible(true);
//    nav_menu.findItem(R.id.leaveapplicantshistory).setVisible(true);
    nav_menu.findItem(R.id.Backlocklist).setVisible(false);
  }


  private void hideNavigationItem() {
    Menu nav_menu = navigationView.getMenu();
    nav_menu.findItem(R.id.leaveapplicants).setVisible(false);
    nav_menu.findItem(R.id.leaveapplicantshistory).setVisible(false);
    nav_menu.findItem(R.id.baclockuserid).setVisible(false);



  }



  /*private void createCameraSource(int cameraFacing) {

    Context context = getApplicationContext();
    FaceDetector detector = new FaceDetector.Builder(context)
      .setClassificationType(FaceDetector.ALL_CLASSIFICATIONS)
      .build();



    if (!detector.isOperational()) {
      // Note: The first time that an app using face API is installed on a device, GMS will
      // download a native library to the device in order to do detection.  Usually this
      // completes before the app is run for the first time.  But if that download has not yet
      // completed, then the above call will not detect any faces.
      //
      // isOperational() can be used to check if the required native library is currently
      // available.  The detector will automatically become operational once the library
      // download completes on device.
      Log.w(TAG, "Face detector dependencies are not yet available.");
    }

    mCameraSource = new CameraSource.Builder(context, detector)
      .setRequestedPreviewSize(640, 480)
      .setFacing(cameraFacing)
      .setRequestedFps(30.0f)
      .build();
  }*/

  private void checkNotificationStatusForLeave(final String user_id) {

    String tag_string_req = "req_register";


    StringRequest strReq = new StringRequest(Request.Method.POST,
      NOTI_BADGE_STATUS, new Response.Listener<String>() {

      @Override
      public void onResponse(String response) {

        //Toast.makeText(getApplicationContext(),"REGISTRATION_TOKEN "+response,Toast.LENGTH_LONG).show();
        //hideDialog();
        try {

          JSONObject jObj = new JSONObject(response);
          JSONArray jsonMainNode = jObj.optJSONArray("results");

          JSONObject result = jsonMainNode.getJSONObject(0);
          String error = result.getString("error");
          if (error.trim().equals("true")) {
            String updatedleave= result.getString("available_leaves");
            Log.i("updatedleavepp", updatedleave);
            pref = getSharedPreferences("info", MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("updatedleave",updatedleave);
            editor.commit();
            String noti_badge_status = result.getString("noti_badge_status");
            if (noti_badge_status.trim().equals("1")) {
              initializeCountDrawerLeave();
              badger.setVisibility(View.VISIBLE);
            }

          }else
          {

          }


        } catch (JSONException e) {

          e.printStackTrace();

        }


      }
    }, new Response.ErrorListener() {

      @Override
      public void onErrorResponse(VolleyError error) {
        // Log.e(TAG, "Registration Error: " + error.getMessage());
        Toast.makeText(getApplicationContext(),
          error.getMessage(), Toast.LENGTH_LONG).show();
        hideDialog();
      }
    }) {

      @Override
      protected Map<String, String> getParams() {

        Map<String, String> params = new HashMap<String, String>();
        params.put("user_id", user_id);
        return params;
      }

    };

    MyApplication.getInstance().addToRequestQueue(strReq, tag_string_req);
  }

  private void changeNotificationBadgeStatusLeave(final String user_id) {

    String tag_string_req = "req_register";

    StringRequest strReq = new StringRequest(Request.Method.POST,
      NOTI_BADGE_STATUS_TO_ZERO, new Response.Listener<String>() {

      @Override
      public void onResponse(String response) {
      }
    }, new Response.ErrorListener() {

      @Override
      public void onErrorResponse(VolleyError error) {
        // Log.e(TAG, "Registration Error: " + error.getMessage());
        Toast.makeText(getApplicationContext(),
          error.getMessage(), Toast.LENGTH_LONG).show();
        hideDialog();
      }
    }) {

      @Override
      protected Map<String, String> getParams() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("user_id", user_id);
        return params;
      }
    };
    MyApplication.getInstance().addToRequestQueue(strReq, tag_string_req);
  }

  private void initializeCountDrawerLeave() {

    approvalList.setGravity(Gravity.CENTER_VERTICAL);
    approvalList.setTypeface(null, Typeface.BOLD);
    approvalList.setTextColor(getResources().getColor(R.color.btn_login));
    approvalList.setText("1");

  }

  private void initializeCountDrawerLeaveAuth() {

    leaveApplicants.setGravity(Gravity.CENTER_VERTICAL);
    leaveApplicants.setTypeface(null, Typeface.BOLD);
    leaveApplicants.setTextColor(getResources().getColor(R.color.btn_login));
    leaveApplicants.setText("1");

  }

  private void initNavigationDrawer() {



    navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
      @Override
      public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        int id = menuItem.getItemId();


        switch (id) {
          case R.id.home:
            //   Toast.makeText(getApplicationContext(),"Home",Toast.LENGTH_SHORT).show();

            Intent i = new Intent(NavigationDrawerActivity.this, NavigationDrawerActivity.class);
            startActivity(i);
            drawerLayout.closeDrawers();
            break;
          case R.id.attendanceSheet:

            Intent moveToNextActivity = new Intent(NavigationDrawerActivity.this, AttendanceList.class);
            startActivity(moveToNextActivity);
            drawerLayout.closeDrawers();
            break;
          case R.id.leaveapplication:

            Intent e = new Intent(NavigationDrawerActivity.this, LeaveApplication.class);
            startActivity(e);
            drawerLayout.closeDrawers();
            break;
          case R.id.approvallist:

            Intent d = new Intent(NavigationDrawerActivity.this, ListOfApproval.class);
            startActivity(d);
            changeNotificationBadgeStatusLeave(user_id);
            badger.setVisibility(View.GONE);
            drawerLayout.closeDrawers();
            break;
          case R.id.expenses:

            Intent f = new Intent(NavigationDrawerActivity.this, Expenses.class);
            startActivity(f);
            drawerLayout.closeDrawers();
            break;
          case R.id.leaveapplicants:

            Intent NextActivity = new Intent(NavigationDrawerActivity.this, LeaveApplicants.class);
            startActivity(NextActivity);
            changeNotificationBadgeStatusLeaveAuth(user_id);
            badger.setVisibility(View.GONE);
            drawerLayout.closeDrawers();
            break;
          case R.id.leaveapplicantshistory:

            Intent moveToNext = new Intent(NavigationDrawerActivity.this, LeaveApplicantsHistory.class);
            startActivity(moveToNext);
            drawerLayout.closeDrawers();
            break;
          case R.id.reportingPerson:

            Intent reporting = new Intent(NavigationDrawerActivity.this, ReportingPeopleActivity.class);
            startActivity(reporting);
            drawerLayout.closeDrawers();
            break;
          case R.id.holidayLists:

            Intent holiday = new Intent(NavigationDrawerActivity.this, HolidayListActivity.class);
            startActivity(holiday);
            drawerLayout.closeDrawers();
            break;

        case R.id.Backlocklist:

        Intent backlcok = new Intent(NavigationDrawerActivity.this, BacklockList.class);
        startActivity(backlcok);
        drawerLayout.closeDrawers();
        break;

          case R.id.baclockuserid:

            Intent backlockusre = new Intent(NavigationDrawerActivity.this, BacklocklistForallUser.class);
            startActivity(backlockusre);
            drawerLayout.closeDrawers();
            break;
          case R.id.backlockstatus:

            Intent backlockstatus = new Intent(NavigationDrawerActivity.this, Backlockstatus.class);
            startActivity(backlockstatus);
            drawerLayout.closeDrawers();
            break;


      }

        return false;
      }
    });

    drawerLayout = (DrawerLayout) findViewById(R.id.drawer);

    ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
      @Override
      public void onDrawerClosed(View v) {
        super.onDrawerClosed(v);
      }

      @Override
      public void onDrawerOpened(View v) {
        super.onDrawerOpened(v);
      }
    };
    drawerLayout.addDrawerListener(actionBarDrawerToggle);
    actionBarDrawerToggle.syncState();


  }

  private void changeNotificationBadgeStatusLeaveAuth(final String user_id) {

    String tag_string_req = "req_register";

    StringRequest strReq = new StringRequest(Request.Method.POST,
      ADMIN_NOTI_BADGE_STATUS_LEAVE_TO_ZERO, new Response.Listener<String>() {

      @Override
      public void onResponse(String response) {
      }
    }, new Response.ErrorListener() {

      @Override
      public void onErrorResponse(VolleyError error) {
        // Log.e(TAG, "Registration Error: " + error.getMessage());
        Toast.makeText(getApplicationContext(),
          error.getMessage(), Toast.LENGTH_LONG).show();
      }
    }) {

      @Override
      protected Map<String, String> getParams() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("user_id", user_id);
        return params;
      }
    };
    MyApplication.getInstance().addToRequestQueue(strReq, tag_string_req);
  }


  private void moveMap(final int status, String Address) {

    //String to display current latitude and longitude
    final String msg = latitude + ", " + longitude;
    if(TextUtils.isEmpty(Address)){
      Geocoder geocoder = new Geocoder(NavigationDrawerActivity.this.getApplicationContext(), Locale.getDefault());
      try {
        List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
        if (addresses.size() > 0) {
          address = addresses.get(0).getAddressLine(0);
          Log.i("address", address);
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }else {
      address = Address;
    }

    if(TextUtils.isEmpty(address)){
      address = "-";
    }

    //Creating a LatLng Object to store Coordinates
    final LatLng latLng = new LatLng(latitude, longitude);
    mMap.addMarker(new MarkerOptions()
      .position(latLng) //setting position
      .draggable(true) //Making the marker draggable
      .title("Current Location")); //Adding a title
    CameraPosition cameraPosition = new CameraPosition.Builder()
      .target(new LatLng(latitude, longitude))
      .zoom(14)
      .bearing(0)
      .tilt(45)
      .build();

    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    //setAttendanceOfUserIn(lat, lang);
    if (status == 2) {
      showLoadingDialog();
      setAttendanceOfUserIn(String.valueOf(latitude), String.valueOf(longitude), address);
    }

    hideDialog();
  }

  private void moveToFingerPrint() {

    Intent moveToFingerPrint = new Intent(NavigationDrawerActivity.this, FingerprintActivity.class);
    startActivity(moveToFingerPrint);
  }

  private void senRegistrationToken(final String token) {


    String tag_string_req = "req_register";


    StringRequest strReq = new StringRequest(Request.Method.POST,
      REGISTRATION_TOKEN, new Response.Listener<String>() {

      @Override
      public void onResponse(String response) {

        //Toast.makeText(getApplicationContext(),"REGISTRATION_TOKEN "+response,Toast.LENGTH_LONG).show();
        //hideDialog();
      /* try{

            JSONObject jObj = new JSONObject(response);
            JSONArray jsonMainNode = jObj.optJSONArray("results");

            JSONObject result=jsonMainNode.getJSONObject(0);
            String error =result.getString("error");
            if (error.trim().equals("true")) {

                txtError.setVisibility(View.VISIBLE);
                txtError.setText("You are On time");


            }

        } catch (JSONException e) {

            e.printStackTrace();

        }*/

      }
    }, new Response.ErrorListener() {

      @Override
      public void onErrorResponse(VolleyError error) {
        // Log.e(TAG, "Registration Error: " + error.getMessage());
        Toast.makeText(getApplicationContext(),
          error.getMessage(), Toast.LENGTH_LONG).show();
        hideDialog();
      }
    }) {

      @Override
      protected Map<String, String> getParams() {

        Map<String, String> params = new HashMap<String, String>();
        params.put("user_id", user_id);
        params.put("registration_id", token);
        params.put("user_flag", "0");
        return params;
      }

    };

    MyApplication.getInstance().addToRequestQueue(strReq, tag_string_req);
  }


  public void showConfrimAlertToUser(final String lat, final String lang, final String text, final String address) {


    dialog = new Dialog(context);
    dialog.setContentView(R.layout.activity_alert_layout);
    dialog.setTitle("Confirm Alert");

    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    Button dialogButtonOk = (Button) dialog.findViewById(R.id.dialogButtonOK);
    Button dialogButtonCancel = (Button) dialog.findViewById(R.id.dialogButtonCancel);
    Button dialogButtonWorkFromHome = (Button) dialog.findViewById(R.id.dialogButtonWorkFromHome);
    final EditText inputTypeReason = (EditText) dialog.findViewById(R.id.inputTypeReason);
    TextView headline = (TextView) dialog.findViewById(R.id.text);
    headline.setText(text);

    if (text.trim().toLowerCase().equals("You are not at your location".trim().toLowerCase())) {
      dialogButtonWorkFromHome.setVisibility(View.VISIBLE);
    } else {
      dialogButtonWorkFromHome.setVisibility(View.GONE);
    }

    dialogButtonWorkFromHome.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        dialog.dismiss();
        showLoadingDialog();
        setAttendanceWithReason(lat, lang, "Work From Home", address);
      }
    });

    dialogButtonCancel.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        dialog.dismiss();
      }
    });


    dialogButtonOk.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        txtReason = inputTypeReason.getText().toString();
        // Toast.makeText(NavigationDrawerActivity.this, "Reason 1" +txtReason, Toast.LENGTH_SHORT).show();
        if (!TextUtils.isEmpty(txtReason)) {

          showLoadingDialog();
          setAttendanceWithReason(lat, lang, txtReason, address);
          dialog.dismiss();
        } else {
          Toast.makeText(context, "please enter a reason", Toast.LENGTH_SHORT).show();
        }
      }
    });


    dialog.show();
    Window window = dialog.getWindow();
    window.setLayout(FrameLayout.LayoutParams.FILL_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
  }


  public void setAttendanceOfUserIn(final String lat, final String lang, final String address) {

    boolean isMock = MockLocationDetector.isLocationFromMockProvider(this, location);

    if (isMock != true) {


      String tag_string_req = "req_register";
      pDialog.setMessage("Please Wait ...");
      showDialog();
      StringRequest strReq = new StringRequest(Request.Method.POST,
        ATTENDANCE_LINK, new Response.Listener<String>() {

        @Override
        public void onResponse(String response) {
          //Toast.makeText(getApplicationContext(),"Response "+response,Toast.LENGTH_LONG).show();

          try {

            JSONObject jObj = new JSONObject(response);
            JSONArray jsonMainNode = jObj.optJSONArray("results");

            JSONObject result = jsonMainNode.getJSONObject(0);
            String error = result.getString("error");
            if (error.trim().equals("true")) {

              //  txtError.setVisibility(View.VISIBLE);
              // txtError.setText("You are On time");

              sendNotificationToAdmin();


            } else if (error.trim().equals("login late")) {

              dismissLoadingDialog();
              String text = "You are late today please write reason on given textbox";
              showConfrimAlertToUser(lat, lang, text, address);

            } else if (error.trim().equals("logout early")) {

              dismissLoadingDialog();
              String text = "You are leaving early today please write reason on given textbox";
              showConfrimAlertToUser(lat, lang, text, address);

            } else if (error.trim().equals("not in location area")) {
              dismissLoadingDialog();
              String text = "You are not at your location";
              showConfrimAlertToUser(lat, lang, text, address);

            }

          } catch (JSONException e) {
            dismissLoadingDialog();
            e.printStackTrace();
          }


        }
      }, new Response.ErrorListener() {

        @Override
        public void onErrorResponse(VolleyError error) {
          //Log.e(TAG, "Registration Error: " + error.getMessage());
          Toast.makeText(getApplicationContext(),
            error.getMessage(), Toast.LENGTH_LONG).show();
          hideDialog();
          dismissLoadingDialog();
        }
      }) {

        @Override
        protected Map<String, String> getParams() {

          Map<String, String> params = new HashMap<String, String>();
          params.put("user_id", user_id);
          params.put("actual_in_latitude", lat);
          params.put("actual_in_longitude", lang);
          params.put("actual_in_location", address);
          return params;
        }

      };


      MyApplication.getInstance().addToRequestQueue(strReq, tag_string_req);

    } else {

      dismissLoadingDialog();
      AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
      builder1.setMessage("This application is not supported when mock locations are turned on.");
      builder1.setCancelable(true);


      builder1.setNegativeButton(
        "Ok",
        new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int id) {
            dialog.cancel();
          }
        });

      AlertDialog alert11 = builder1.create();
      alert11.show();

    }
  }

  private void sendNotificationToAdmin() {

    String tag_string_req = "req_register";

    pDialog.setMessage("Please Wait ...");
    //showDialog();

    StringRequest strReq = new StringRequest(Request.Method.POST,
      SEND_NOTIFICATION_AMDIN, new Response.Listener<String>() {

      @Override
      public void onResponse(String response) {
        // Toast.makeText(getApplicationContext(),"Response "+response,Toast.LENGTH_LONG).show();
        // hideDialog();

        try {

          JSONObject jObj = new JSONObject(response);

          String jsonMainNode = jObj.optString("result");
          SharedPreferences shared = getSharedPreferences("Status", MODE_PRIVATE);
          SharedPreferences.Editor editor = shared.edit();
          DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
          Calendar calendar = Calendar.getInstance();
         dismissLoadingDialog();
          if (jObj.optString("check_key").trim().toLowerCase().equals("check_out".trim().toLowerCase())) {
            circularButtonSubmit.setText("Check-Out");
            Toast.makeText(NavigationDrawerActivity.this, jsonMainNode, Toast.LENGTH_SHORT).show();
            editor.putInt("check_out", 2);
            Date today = calendar.getTime();
            String todayAsString = dateFormat.format(today);
            editor.putString("TodateHome", todayAsString);
            editor.commit();
          } else {
            circularButtonSubmit.setText("Check-In");
            editor.putInt("check_out", 1);
            Date today = calendar.getTime();
            String todayAsString = dateFormat.format(today);
            editor.putString("TodateHome", todayAsString);
            editor.commit();
            Calendar calendar1 = Calendar.getInstance();
            calendar1.set(calendar1.get(Calendar.YEAR), calendar1.get(Calendar.MONTH), calendar1.get(Calendar.DAY_OF_MONTH),
              20, 30, 0);
            setAlarm(calendar1.getTimeInMillis(),jsonMainNode);
          }


        } catch (JSONException e) {
          e.printStackTrace();
          dismissLoadingDialog();
        }

      }
    }, new Response.ErrorListener() {

      @Override
      public void onErrorResponse(VolleyError error) {
        // Log.e(TAG, "Registration Error: " + error.getMessage());
        Toast.makeText(getApplicationContext(),
          error.getMessage(), Toast.LENGTH_LONG).show();
        dismissLoadingDialog();
      }
    }) {

      @Override
      protected Map<String, String> getParams() {


        Map<String, String> params = new HashMap<String, String>();
        params.put("user_id", user_id);
        params.put("user_name", user_name);


        return params;
      }

    };


    MyApplication.getInstance().addToRequestQueue(strReq, tag_string_req);
  }

  public void setAttendanceWithReason(final String lat, final String lang, final String reason, final String address) {
checkConnection();

    String tag_string_req = "req_register";
    pDialog.setMessage("Please Wait ...");
    showDialog();

    StringRequest strReq = new StringRequest(Request.Method.POST,
      ATTENDANCE_LINK, new Response.Listener<String>() {

      @Override
      public void onResponse(String response) {
        // Toast.makeText(getApplicationContext(),"Response "+response,Toast.LENGTH_LONG).show();

        hideDialog();


        try {

          JSONObject jObj = new JSONObject(response);
          JSONArray jsonMainNode = jObj.optJSONArray("results");

          Log.i("RESPONSE", String.valueOf(jsonMainNode));

          JSONObject result = jsonMainNode.getJSONObject(0);
          String error = result.getString("error");
          if (error.trim().equals("true")) {

            // txtError.setVisibility(View.VISIBLE);
            //  txtError.setText("You are On time");
            sendNotificationToAdmin();


          } else if (error.trim().equals("login late")) {
            // txtError.setVisibility(View.VISIBLE);
            //  txtError.setText("You are Late today");

          } else if (error.trim().equals("login early")) {
            //  txtError.setVisibility(View.VISIBLE);
            //  txtError.setText("You are Leaving Early today");

          }


        } catch (JSONException e) {

          e.printStackTrace();

        }


      }
    }, new Response.ErrorListener() {

      @Override
      public void onErrorResponse(VolleyError error) {
        Log.e(TAG, "Registration Error: " + error.getMessage());
        Toast.makeText(getApplicationContext(),
          "Error Message " + error.getMessage(), Toast.LENGTH_LONG).show();
        hideDialog();
      }
    }) {

      @Override
      protected Map<String, String> getParams() {


        Map<String, String> params = new HashMap<String, String>();
        params.put("user_id", user_id);
        params.put("actual_in_latitude", lat);
        params.put("actual_in_longitude", lang);
        params.put("reason_late", reason);
        params.put("actual_in_location", address);

        return params;
      }

    };


    MyApplication.getInstance().addToRequestQueue(strReq, tag_string_req);
  }

  private void requestForSpecificPermission() {


    ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_NETWORK_STATE, android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.READ_SMS, android.Manifest.permission.READ_PHONE_STATE}, 101);
  }


  @Override
  public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
    switch (requestCode) {
      case 101:
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
          showLoadingDialog();
          EnableGPSAutoMatically();
        } else {
          requestForSpecificPermission();

          Toast.makeText(getApplicationContext(), "Please allow permission", Toast.LENGTH_SHORT).show();
        }
        break;
      default:
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
  }

  private void EnableGPSAutoMatically() {
    GoogleApiClient googleApiClient = null;
    if (googleApiClient == null) {
      googleApiClient = new GoogleApiClient.Builder(this)
        .addApi(LocationServices.API)
        .addConnectionCallbacks(this)
        .addOnConnectionFailedListener(this).build();
      if (googleApiClient != null) {
        googleApiClient.connect();
      }

      LocationManager   locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);

      if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
       showLoadingDialog();
      }else {
        dismissLoadingDialog();
      }
      locationRequest = LocationRequest.create();
      locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
      locationRequest.setInterval(10 * 1000);
      locationRequest.setFastestInterval(3 * 1000);
      LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
        .addLocationRequest(locationRequest);
      // **************************
      builder.setAlwaysShow(true); // this is the key ingredient
      // **************************
      PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi
        .checkLocationSettings(googleApiClient, builder.build());
      result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
        @Override
        public void onResult(LocationSettingsResult result) {
          final Status status = result.getStatus();
          final LocationSettingsStates state = result
            .getLocationSettingsStates();
          dismissLoadingDialog();
          switch (status.getStatusCode()) {
            case LocationSettingsStatusCodes.SUCCESS:
              getCurrentLocation(2);
              break;
            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
              Toast.makeText(NavigationDrawerActivity.this, "Your GPS is not on", Toast.LENGTH_SHORT).show();
              try {
                status.startResolutionForResult(NavigationDrawerActivity.this, 1000);
              } catch (IntentSender.SendIntentException e) {
              }
              break;
            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
              Toast.makeText(NavigationDrawerActivity.this, "Setting change not allowed", Toast.LENGTH_SHORT).show();
              break;
          }
        }
      });
    }
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == CAMERA_PICTURE) {
      //Toast.makeText(this, "Checking camera", Toast.LENGTH_SHORT).show();
      chosenImage = (Bitmap) data.getExtras().get("data");
      ByteArrayOutputStream bytes = new ByteArrayOutputStream();
      chosenImage.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
      destination = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis() + ".jpg");
      FileOutputStream fo;
      filePath = destination.toString();

      try {
        destination.createNewFile();
        fo = new FileOutputStream(destination);
        fo.write(bytes.toByteArray());
        fo.close();
      } catch (FileNotFoundException e) {
        e.printStackTrace();
      } catch (IOException e) {
        e.printStackTrace();
      }

      new uploadAsynctask().execute(FILE_UPLOAD_URL);
    } else if (requestCode == 1000) {
      if (resultCode == Activity.RESULT_OK) {
        getCurrentLocation(2);
      }
    } else if (requestCode == REQUEST_CHECK_SETTINGS) {
      switch (resultCode) {
        case Activity.RESULT_OK:
          //Success Perform Task Here
          gps = new GPSTracker(NavigationDrawerActivity.this);
          if (gps.canGetLocation) {
            mMap.clear();
            pDialog.setMessage("Please Wait ...");
            if (ActivityCompat.checkSelfPermission(NavigationDrawerActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(NavigationDrawerActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
              return;
            }
            if(location == null){
              location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            }

            if(location == null && gps != null){
              location = gps.getLocation();
            }

            if (location != null) {
              longitude = location.getLongitude();
              latitude = location.getLatitude();
              if (gps.canGetLocation()) {
                double latitude = gps.getLatitude();
                double longitude = gps.getLongitude();
                Log.i("latitude", String.valueOf(latitude));
                Log.i("longitude", String.valueOf(longitude));
                Log.i("user_id", user_id);
                List<Address> addresses;
                Geocoder geocoder = new Geocoder(NavigationDrawerActivity.this.getApplicationContext(), Locale.getDefault());
                try {
                  addresses = geocoder.getFromLocation(Double.valueOf(String.valueOf(latitude)), Double.valueOf(String.valueOf(longitude)), 1);
                  if (addresses.size() != 0) {
                    address = addresses.get(0).getAddressLine(0);
                    Log.i("address", address);
                  }
                } catch (IOException e) {
                  e.printStackTrace();
                }
              } else {
                LocationFinder finder;
                double longitude = 0.0, latitude = 0.0;
                finder = new LocationFinder(NavigationDrawerActivity.this);
                if (finder.canGetLocation()) {
                  latitude = finder.getLatitude();
                  longitude = finder.getLongitude();
                  List<Address> addresses;
                  Geocoder geocoder = new Geocoder(NavigationDrawerActivity.this.getApplicationContext(), Locale.getDefault());
                  try {
                    addresses = geocoder.getFromLocation(Double.valueOf(String.valueOf(latitude)), Double.valueOf(String.valueOf(longitude)), 1);
                    if (addresses.size() != 0) {
                      address = addresses.get(0).getAddressLine(0);
                      Log.i("address", address);
                    }
                  } catch (IOException e) {
                    e.printStackTrace();
                  }
                } else {
                  finder.showSettingsAlert();
                }
              }
              //moving the map to location
              new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                  moveMap(1, address);
                }
              }, 2000);
            } else {
              Toast.makeText(getApplicationContext(), "Location not found please click again", Toast.LENGTH_LONG).show();
              hideDialog();
            }
          } else {
            gps.showSettingsAlert();
            hideDialog();
          }
          break;
        case Activity.RESULT_CANCELED:
          Log.e("GPS", "User denied to access location");
          openGpsEnableSetting();
          break;
      }
    } else if (requestCode == REQUEST_ENABLE_GPS) {
      LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
      boolean isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
      if (!isGpsEnabled) {
        openGpsEnableSetting();

      }
    }
  }

  private void openGpsEnableSetting() {

    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
    startActivityForResult(intent, REQUEST_ENABLE_GPS);
  }

  private void checkConnection() {
    boolean isConnected = ConnectivityReceiver.isConnected();
    showSnack(isConnected);
  }

  private void showSnack(boolean isConnected) {
    String message;
    int color;
    if (isConnected) {
      message = "";
      color = Color.WHITE;

    } else {

      message = "Sorry! Not connected to internet";
      // color = Color.RED;
      Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
      moveToNextActivityRegistration();
    }


  }

  @Override
  public void onNetworkConnectionChanged(boolean isConnected) {
    showSnack(isConnected);
  }

  @Override
  protected void onResume() {
    super.onResume();
    dismissLoadingDialog();
    MyApplication.getInstance().setConnectivityListener(this);
  }


  @Override
  protected void onPause() {
    super.onPause();


    MyApplication.getInstance().setConnectivityListener(this);
  }

  private void showDialog() {
    if (!pDialog.isShowing())
      pDialog.dismiss();
  }

  private void hideDialog() {
    if (pDialog.isShowing())
      pDialog.dismiss();
  }

  @Override
  protected void onStart() {
    super.onStart();
    if (googleApiClient != null) {
      googleApiClient.connect();
    }
  }

  @Override
  protected void onStop() {
    super.onStop();
    if (googleApiClient.isConnected()) {
      googleApiClient.disconnect();
      Log.i(TAG, "GoogleApiClient DisConnected");
    }
  }

  private void getCurrentLocation(final int status) {

    showDialog();

   LocationManager   locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);

    if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){

    }else {
      dismissLoadingDialog();
    }

    LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
    builder.addLocationRequest(new LocationRequest().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY));
    builder.setAlwaysShow(true);
    mLocationSettingsRequest = builder.build();
    mSettingsClient = LocationServices.getSettingsClient(NavigationDrawerActivity.this);
    mSettingsClient
      .checkLocationSettings(mLocationSettingsRequest)
      .addOnSuccessListener(new OnSuccessListener<LocationSettingsResponse>() {
        @Override
        public void onSuccess(LocationSettingsResponse locationSettingsResponse) {

          gps = new GPSTracker(NavigationDrawerActivity.this);
          if (gps.canGetLocation()) {
            mMap.clear();
            pDialog.setMessage("Please Wait ...");
            if (ActivityCompat.checkSelfPermission(NavigationDrawerActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(NavigationDrawerActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
              return;
            }

            if(location == null){
              location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            }

            if(location == null && gps != null){
              location = gps.getLocation();
            }

            if (location != null) {
              longitude = location.getLongitude();
              latitude = location.getLatitude();
              if (gps.canGetLocation()) {
                double latitude = gps.getLatitude();
                double longitude = gps.getLongitude();
                List<Address> addresses;
                Geocoder geocoder = new Geocoder(NavigationDrawerActivity.this.getApplicationContext(), Locale.getDefault());
                try {
                  addresses = geocoder.getFromLocation(Double.valueOf(String.valueOf(latitude)), Double.valueOf(String.valueOf(longitude)), 1);
                  if (addresses.size() != 0) {
                    address = addresses.get(0).getAddressLine(0);
                    Log.i("address", address);
                  }
                } catch (IOException e) {
                  e.printStackTrace();
                }
              } else {
                LocationFinder finder;
                double longitude = 0.0, latitude = 0.0;
                finder = new LocationFinder(NavigationDrawerActivity.this);
                if (finder.canGetLocation()) {
                  latitude = finder.getLatitude();
                  longitude = finder.getLongitude();
                  List<Address> addresses;
                  Geocoder geocoder = new Geocoder(NavigationDrawerActivity.this.getApplicationContext(), Locale.getDefault());
                  try {
                    addresses = geocoder.getFromLocation(Double.valueOf(String.valueOf(latitude)), Double.valueOf(String.valueOf(longitude)), 1);
                    if (addresses.size() != 0) {
                      address = addresses.get(0).getAddressLine(0);
                      Log.i("address", address);
                    }
                  } catch (IOException e) {
                    e.printStackTrace();
                  }
                } else {
                  finder.showSettingsAlert();
                }
              }
              //moving the map to location
              new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                  moveMap(status, address);
                }
              }, 2000);
            } else {
              Toast.makeText(getApplicationContext(), "Location not found please click again", Toast.LENGTH_LONG).show();
              hideDialog();
            }
          } else {
            gps.showSettingsAlert();
            hideDialog();
          }
        }
      })
      .addOnFailureListener(new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
          int statusCode = ((ApiException) e).getStatusCode();
          switch (statusCode) {
            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
              try {
                ResolvableApiException rae = (ResolvableApiException) e;
                rae.startResolutionForResult(NavigationDrawerActivity.this, REQUEST_CHECK_SETTINGS);
              } catch (IntentSender.SendIntentException sie) {
                Log.e("GPS", "Unable to execute request.");
              }
              break;
            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
              Log.e("GPS", "Location settings are inadequate, and cannot be fixed here. Fix in Settings.");
          }
        }
      })
      .addOnCanceledListener(new OnCanceledListener() {
        @Override
        public void onCanceled() {
          Log.e("GPS", "checkLocationSettings -> onCanceled");
        }
      });

    //Creating a location object

  }

  @Override
  public void onClick(View view) {

  }

  @Override
  public void onConnected(@Nullable Bundle bundle) {

    locationRequest = new LocationRequest();
    locationRequest.setInterval(2000);
    locationRequest.setFastestInterval(2000);
    locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
      fusedLocationClient.requestLocationUpdates(locationRequest,mLocationCallback,null);
    }
      getCurrentLocation(1);
  }

  @Override
  public void onConnectionSuspended(int i) {

  }

  @Override
  public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

  }

  @Override
  public void onMapLongClick(LatLng latLng) {

  }

  @Override
  public void onMarkerDragStart(Marker marker) {

  }

  @Override
  public void onMarkerDrag(Marker marker) {

  }

  @Override
  public void onMarkerDragEnd(Marker marker) {

  }

  @Override
  public void onMapReady(GoogleMap googleMap) {

    mMap = googleMap;
    CameraPosition cameraPosition = new CameraPosition.Builder()
      .target(new LatLng(20.7709746, 73.7257803))
      .zoom(14)
      .bearing(0)
      .tilt(45)
      .build();
    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


  }


  @Override
  public void onBackPressed() {
    Intent a = new Intent(Intent.ACTION_MAIN);
    a.addCategory(Intent.CATEGORY_HOME);
    a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    startActivity(a);
  }

  @Override
  public void onLocationChanged(@NonNull Location locations) {
    this.location = locations;
    if(locations != null){
      latitude = locations.getLatitude();
      longitude = locations.getLongitude();
    }

  }


  private class uploadAsynctask extends AsyncTask<String, Void, String>{
    @Override
    protected String doInBackground(String... params) {

      Log.e("filepath",filePath);

      File file1=new File(filePath);
      Log.e("okay","lol"+file1);
      Log.e("okay",user_id);
      MultipartEntity reqEntity;
      HttpEntity resEntity;
      try {
        HttpClient client = new DefaultHttpClient();
        String postURL = params[0];
        HttpPost post = new HttpPost(postURL);

        FileBody bin1=new FileBody(file1);

        reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

        reqEntity.addPart("uploadedfile1",bin1);
        reqEntity.addPart("user_id",new StringBody(user_id));

        post.setEntity(reqEntity);
        HttpResponse response = client.execute(post);
        resEntity = response.getEntity();
        String entityContentAsString = EntityUtils.toString(resEntity);
        Log.d("stream:", entityContentAsString);

        return entityContentAsString;
      }catch(Exception e)
      {
        e.printStackTrace();
      }
      return null;
    }
  }


  private void moveToNextActivityRegistration() {

     /*   if(session.isLoggedIn()){
            new Handler().postDelayed(new Runnable(){
                @Override
                public void run() {

                    Intent mainIntent = new Intent(MainActivity.this,NavigationDrawerActivity.class);
                    startActivity(mainIntent);
                    finish();
                }
            }, 3000);

        }else{
            new Handler().postDelayed(new Runnable(){
                @Override
                public void run() {

                    Intent mainIntent = new Intent(MainActivity.this,Registration.class);
                    startActivity(mainIntent);
                    finish();
                }
            }, 3000);
        }*/


    new Handler().postDelayed(new Runnable(){
      @Override
      public void run() {

        Intent mainIntent = new Intent(NavigationDrawerActivity.this,Login.class);
        mainIntent.putExtra("device_imei",device_imei);
        startActivity(mainIntent);
        finish();
      }
    }, 3000);
  }






//  private void Checkbacklock(final String user_id) {
//
//    pDialog.setMessage("Loading Please wait....");
//    showDialog();
//
//    StringRequest strReq = new StringRequest(Request.Method.GET,
//      BACKLOCK_LIST,new Response.Listener<String>() {
//      @Override
//      public void onResponse(String s) {
//        hideDialog();
//        JSONObject jsonObject = null;
//        try {
//          jsonObject = new JSONObject(s);
//          JSONArray jsonMainNode = jsonObject.optJSONArray("result");
//
//          for (int i = 0; i < jsonMainNode.length(); i++) {
//
//
//            JSONObject result = jsonMainNode.getJSONObject(i);
//            String date = result.getString("date");
//            String checkin = result.getString("checkout");
//            String checkout = result.getString("checkin");
//
//
//          }
//
//        } catch (JSONException e) {
//          e.printStackTrace();
//        }
//
//      }
//    }, new Response.ErrorListener() {
//      @Override
//      public void onErrorResponse(VolleyError volleyError) {
//        Toast.makeText(getApplicationContext(),volleyError.getMessage(), Toast.LENGTH_LONG).show();
//        hideDialog();
//      }
//    })
//    {
//      @Override
//      protected Map<String, String> getParams() {
//        // Posting params to register url
//
//        Map<String, String> params = new HashMap<String, String>();
//        params.put("user_id", user_id);
//
//        return params;
//      }
//    };
//    MyApplication.getInstance().addToRequestQueue(strReq);
//  }




  private void Checkbacklock(final String user_id) {
//
    String tag_string_req = "req_register";

    //Toast.makeText(this, "Checking user", Toast.LENGTH_SHORT).show();
    StringRequest strReq = new StringRequest(Request.Method.POST, BACKLOCK_LIST, new Response.Listener<String>() {
      @Override
      public void onResponse(String response) {

        try {
          JSONObject jObj = new JSONObject(response);
          Log.i("dataresponse", String.valueOf(jObj));
          JSONArray jsonMainNode = jObj.optJSONArray("results");
          JSONArray Error = jObj.optJSONArray("error");
          for (int i = 0; i < jsonMainNode.length(); i++) {


            JSONObject result = jsonMainNode.getJSONObject(i);

            String checkout = result.getString("checkout");
            String checkin = result.getString("checkin");

            if(checkout.equals("")|| checkin.equals(""))
            {
             Backlockmodel backlockmodel=new Backlockmodel();
              String date = result.getString("date");
              String newcheckout = result.getString("checkout");
              String newcheckin = result.getString("checkin");
              backlockmodel.setDate(date);
              backlockmodel.setCheckin(newcheckin);
              backlockmodel.setCheckout(newcheckout);
              backlocklist.add(backlockmodel);


              Log.i("checkinacklocb",date);
              Log.i("checkinacklocb",newcheckout);
              Log.i("checkinacklocb",newcheckin);
//              Toast.makeText(NavigationDrawerActivity.this, "checkin empty", Toast.LENGTH_SHORT).show();
            }




          }

        } catch (JSONException e) {
          e.printStackTrace();
        }
      }
    }, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {

      }
    }) {

      @Override
      protected Map<String, String> getParams() {


        Map<String, String> params = new HashMap<String, String>();

        params.put("user_id", user_id);
//        params.put("user_phone_imei", imei_no);
        return params;
      }

    };
    MyApplication.getInstance().addToRequestQueue(strReq, tag_string_req);
  }


}


