package kattendance.dashboard.kanalytics.in.kattendance;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;
import androidx.core.util.Pair;

import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Devlopment on 13/09/17.
 */
public class Login extends AppCompatActivity implements Animation.AnimationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {
    Animation animBounce;
    Button btnSubmitNow,btnRegister;
    EditText inputTypeMobile,inputTypeOTP;
    SessionManager session;
    SharedPreferences pref;
    private ProgressDialog pDialog;
    private static  String deviceIMEI;
    private String URL_LOGIN="https://dashboard.kanalytics.in/mobile_app/attendance/login.php";
  private static final String MY_PREFS_NAME = "MyPrefs" ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
    }

  private void initView() {
    btnSubmitNow=(Button) findViewById(R.id.btnLogin);
    inputTypeMobile=(EditText) findViewById(R.id.inputTypeMobile);
    btnRegister = findViewById(R.id.btnRegister);
    pDialog = new ProgressDialog(this);
    pDialog.setCancelable(false);
    session = new SessionManager(getApplicationContext());
    requestForSpecificPermission();
    EnableGPSAutoMatically();
    btnSubmitNow.setOnClickListener(this);
    btnRegister.setOnClickListener(this);
    createAnimation();
  }

  private void EnableGPSAutoMatically() {
    GoogleApiClient googleApiClient = null;
    if (googleApiClient == null) {
      googleApiClient = new GoogleApiClient.Builder(this)
        .addApi(LocationServices.API).addConnectionCallbacks(this)
        .addOnConnectionFailedListener(this).build();
      googleApiClient.connect();
      LocationRequest locationRequest = LocationRequest.create();
      locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
      locationRequest.setInterval(30 * 1000);
      locationRequest.setFastestInterval(5 * 1000);
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
          switch (status.getStatusCode()) {
            case LocationSettingsStatusCodes.SUCCESS:
              break;
            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
              Toast.makeText(Login.this, "GPS is not on", Toast.LENGTH_SHORT).show();
              try {
                status.startResolutionForResult(Login.this, 1000);
              } catch (IntentSender.SendIntentException e) {
                // Ignore the error.
              }
              break;
            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
              Toast.makeText(Login.this, "Setting change not allowed", Toast.LENGTH_SHORT).show();
              break;
          }
        }
      });
    }
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == 1000) {
      if (resultCode == Activity.RESULT_OK) {
        String result = data.getStringExtra("result");
      }
      if (resultCode == Activity.RESULT_CANCELED) {
      }
    }
  }
  public void onConnected(@Nullable Bundle bundle) {
  }
  @Override
  public void onConnectionSuspended(int i) {
    toast("Suspended");
  }
  @Override
  public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    toast("Failed");
  }
  private void toast(String message) {
    try {
      Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    } catch (Exception ex) {

    }
  }

  private boolean isValidPhoneNumber(String phone) {
    return (phone.length()==10) && android.util.Patterns.PHONE.matcher(phone).matches();
  }

  private void requestForSpecificPermission() {
    ActivityCompat.requestPermissions(this, new String[]{ android.Manifest.permission.ACCESS_NETWORK_STATE, android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.READ_SMS, android.Manifest.permission.READ_PHONE_STATE}, 101);
  }

  @Override
  public void onRequestPermissionsResult(int requestCode,  String[] permissions,  int[] grantResults) {
    switch (requestCode) {
      case 101:
        if(grantResults.length != 0){
          if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getIMEIdivceNumber();
          } else {
            requestForSpecificPermission();
          }
        }
        break;
      default:
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
  }

  private void getIMEIdivceNumber(){
    int hasPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
    if (hasPermission == PackageManager.PERMISSION_GRANTED) {
      TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);

      if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        deviceIMEI = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
      } else {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
          String imei = telephonyManager.getImei(); // Requires permission READ_PHONE_STATE
          deviceIMEI = imei == null ? telephonyManager.getMeid() : imei; // Requires permission READ_PHONE_STATE
        } else {
          deviceIMEI=telephonyManager.getDeviceId();// Requires permission READ_PHONE_STATE
        }
      }
      Log.i("IMEI", deviceIMEI);
      pref = getSharedPreferences("info", MODE_PRIVATE);
      SharedPreferences.Editor editor = pref.edit();
      editor.putString("deviceIMEI",deviceIMEI);
      editor.commit();
    }else{
      requestForSpecificPermission();
    }
  }
    private void CheckUserExists(final String mobile_number){
        pDialog.setMessage("Please wait....");
        showDialog();
        StringRequest strReq = new StringRequest(Request.Method.POST,
          URL_LOGIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                hideDialog();
              try{
                JSONObject jObj = new JSONObject(response);
                JSONArray jsonMainNode = jObj.optJSONArray("results");
                JSONObject result=jsonMainNode.getJSONObject(0);
                Log.i("result", String.valueOf(result));
                String statusCode =result.getString("statusCode");
                if(statusCode.trim().equals("0")) {
                  Toast.makeText(Login.this, result.getString("statusMessage"), Toast.LENGTH_SHORT).show();
                }else if (statusCode.trim().equals("2")){
                  moveToNextActivity();
                }else if (statusCode.trim().equals("1")){
                  String user_id=result.getJSONObject("datas").getString("user_id");
                  String username=result.getJSONObject("datas").getString("name");
                  String contact=result.getJSONObject("datas").getString("contact");
                  String office_address=result.getJSONObject("datas").getString("office_address");
                  String team=result.getJSONObject("datas").getString("team_lead");
                  String dept_id=result.getJSONObject("datas").getString("dept_id");
                  String office_in=result.getJSONObject("datas").getString("ofc_in_time");
                  String office_out=result.getJSONObject("datas").getString("ofc_out_time");
                  String working_day=result.getJSONObject("datas").getString("working_days");
                  String location_latitude=result.getJSONObject("datas").getString("ofc_latitude");
                  String location_longitude=result.getJSONObject("datas").getString("ofc_longitude");
                  String pending_leave=result.getJSONObject("datas").getString("leaves");
                  pref = getSharedPreferences("info", MODE_PRIVATE);
                  SharedPreferences.Editor editor = pref.edit();
                  editor.putString("user_id",user_id);
                  editor.putString("user_name",username);
                  editor.putString("contact",contact);
                  editor.putString("office_address",office_address);
                  editor.putString("team_lead",team);
                  editor.putString("dept_id",dept_id);
                  editor.putString("office_in",office_in);
                  editor.putString("office_out",office_out);
                  editor.putString("working_day",working_day);
                  editor.putString("ofc_lat",location_latitude);
                  editor.putString("ofc_long",location_longitude);
                  editor.putString("leaves",pending_leave);
                  editor.commit();
                  session.setLogin(true);
                  Intent intent=new Intent(Login.this,NavigationDrawerActivity.class);
                  SharedPreferences.Editor editor1 = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                  editor1.putString("newUserstatus", "false");
                  editor1.apply();
                  startActivity(intent);
                }
              } catch (JSONException e) {
                e.printStackTrace();
              }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("contact", mobile_number);
                params.put("imei_no", deviceIMEI);
                return params;
            }
        };
        MyApplication.getInstance().addToRequestQueue(strReq);
    }

    public void moveToNextActivity(){
        Intent intent=new Intent(Login.this,Registration.class);
        intent.putExtra("contact",inputTypeMobile.getText().toString().trim());
        startActivity(intent);
        finish();
    }

     public void createAnimation(){
         animBounce = AnimationUtils.loadAnimation(getApplicationContext(),
                 R.anim.bounce);
         animBounce.setAnimationListener(this);
         btnSubmitNow.startAnimation(animBounce);
     }
    @Override
    public void onAnimationEnd(final Animation animation) {

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        if (animation == animBounce) {
                            createAnimation();
                        }
                    }
                }, 3000);


    }

    @Override
    public void onAnimationRepeat(Animation animation) {


    }

    @Override
    public void onAnimationStart(Animation animation) {
        // TODO Auto-generated method stub

    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

  @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
  @Override
  public void onClick(View view) {
     switch (view.getId()){
       case R.id.btnRegister:
         Intent intent = new Intent(this,Registration.class);
         intent.putExtra("contact","");
         startActivity(intent);
         break;
       case R.id.btnLogin:
         if(TextUtils.isEmpty(inputTypeMobile.getText().toString().trim())){
           Toast.makeText(Login.this, "Please Enter Mobile Number", Toast.LENGTH_SHORT).show();
         }else{
           if(isValidPhoneNumber(inputTypeMobile.getText().toString().trim())){
             CheckUserExists(inputTypeMobile.getText().toString().trim());
           }else {
             Toast.makeText(Login.this, "Invalid Mobile Number", Toast.LENGTH_SHORT).show();
           }
         }
         break;
     }
  }
}
