package kattendance.dashboard.kanalytics.in.kattendance;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Handler;

import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.play.core.appupdate.AppUpdateInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {
  GPSTracker gps;

  SessionManager session;
  private String URL_REGISTER = "https://dashboard.kanalytics.in/mobile_app/attendance/check_user.php";
  private ProgressDialog pDialog;
  String mobile_number, device_imei;
  SharedPreferences pref;
  private static final String MY_PREFS_NAME = "MyPrefs" ;
  private TelephonyManager telephonyManager;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    SharedPreferences shared = getSharedPreferences("info", MODE_PRIVATE);
    mobile_number = shared.getString("contact", "");
    device_imei = shared.getString("deviceIMEI", "");
  //  device_imei = shared.getString("imei_no", "");
    session = new SessionManager(this);
    requestForSpecificPermission();
    //Toast.makeText(this, "IMEI : "+device_imei, Toast.LENGTH_SHORT).show();

    AppUpdateChecker appUpdateChecker=new AppUpdateChecker(this);  //pass the activity in constructure
    appUpdateChecker.checkForUpdate(false);



  }

  private void requestForSpecificPermission() {
    ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_NETWORK_STATE, android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.READ_SMS, Manifest.permission.READ_PHONE_STATE,Manifest.permission.READ_PHONE_NUMBERS}, 101);
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {


    switch (requestCode) {
      case 101:
        if(grantResults.length != 0){
          if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {


            checkConnection();
          } else {


            requestForSpecificPermission();
          }
        }

        break;
      default:
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

  }

  public void checkConnection() {
    boolean isConnected = ConnectivityReceiver.isConnected();
    showSnack(isConnected);
  }

  private void showSnack(boolean isConnected) {

    if (isConnected) {

   /*   if (TextUtils.isEmpty(mobile_number) || TextUtils.isEmpty(device_imei)) {

        telephonyManager =(TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {

          return;
        }
        device_imei = telephonyManager.getDeviceId();
        mobile_number=telephonyManager.getLine1Number();

          }*/


            registerUserToServer(mobile_number,device_imei);

        } else {

            Toast.makeText(getApplicationContext(),"Sorry! Not connected to internet",Toast.LENGTH_LONG).show();
            moveToNextActivityRegistration();
        }



    }


    public void registerUserToServer(final String mobile_number, final String mobile_imei_no){
        String tag_string_req = "req_register";


        //Toast.makeText(getApplicationContext(), "Response From Main Activity Page "+mobile_number+","+mobile_imei_no, Toast.LENGTH_LONG).show();


        StringRequest strReq = new StringRequest(Request.Method.POST,
                URL_REGISTER, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                //  Log.d(TAG, "Register Response: " + response.toString());
                // Toast.makeText(getApplicationContext(), "Response From Main Activity Page "+response, Toast.LENGTH_LONG).show();



                try{

                    JSONObject jObj = new JSONObject(response);
                    JSONArray jsonMainNode = jObj.optJSONArray("results");

                    JSONObject result=jsonMainNode.getJSONObject(0);
                    String error =result.getString("error");
                    if (error.trim().equals("true")) {
                        String user_id=result.getString("id");
                        String username=result.getString("name");
                        String contact=result.getString("contact");
                        String office_address=result.getString("office_address");
                        String team=result.getString("team");
                        String office_in=result.getString("ofc_in_time");
                        String office_out=result.getString("ofc_out_time");
                        String working_day=result.getString("working_days");
                        String location_latitude=result.getString("ofc_latitude");
                        String location_longitude=result.getString("ofc_longitude");
                        String tl_status=result.getString("tl_status");
                        SharedPreferences shared = getSharedPreferences("info",MODE_PRIVATE);

                        int status=0;
                        pref = getSharedPreferences("info", MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("user_id",user_id);
                        editor.putString("user_name",username);
                        editor.putString("contact",contact);
                        editor.putString("office_address",office_address);
                        editor.putString("team",team);
                        editor.putString("office_in",office_in);
                        editor.putString("office_out",office_out);
                        editor.putString("working_day",working_day);
                        editor.putString("ofc_lat",location_latitude);
                        editor.putString("ofc_long",location_longitude);
                        editor.putString("tl_status",tl_status);
                        editor.putString("imei_no",mobile_imei_no);
                        editor.commit();




                      moveToNextActivity();


                    } else {

                      SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                      String newUser = prefs.getString("newUserstatus", "true");

                      if(newUser.trim().toLowerCase().toString().equals("false".trim().toLowerCase().toString())){
                        Intent intent=new Intent(MainActivity.this,NavigationDrawerActivity.class);
                        startActivity(intent);
                      } else {
                        moveToNextActivityRegistration();
                      }



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

                params.put("user_contact", mobile_number);

                params.put("user_phone_imei",mobile_imei_no);
                return params;
            }

        };



        MyApplication.getInstance().addToRequestQueue(strReq, tag_string_req);


    }

    private void moveToNextActivity() {

      /*  if(session.isLoggedIn()){
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
                //Intent startService=new Intent(MainActivity.this,FireMsgService.class);
              //  startService(startService);
                Intent mainIntent = new Intent(MainActivity.this,NavigationDrawerActivity.class);
                startActivity(mainIntent);
                finish();
            }
        }, 3000);

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

                Intent mainIntent = new Intent(MainActivity.this,Login.class);
                mainIntent.putExtra("device_imei",device_imei);
                startActivity(mainIntent);
                finish();
            }
        }, 3000);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack(isConnected);
    }








}
