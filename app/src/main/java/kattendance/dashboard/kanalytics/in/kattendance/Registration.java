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


import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;

import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
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
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Devlopment on 13/09/17.
 */
public class Registration extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,ConnectivityReceiver.ConnectivityReceiverListener {

    Button btnRegisterUser;
    EditText inputTypeFirstName,inputTypeMobileNumber;
    private static  String deviceIMEI,office_lat,office_log,name,contact,office_address,existUser;
    GPSTracker gps;
    private SessionManager session;
    private ProgressDialog pDialog;
    private SharedPreferences pref;
    private String URL_REGISTER="https://dashboard.kanalytics.in/mobile_app/attendance/regristration.php";
    private String CHECK_USER="https://dashboard.kanalytics.in/mobile_app/attendance/check.php";
    private String DEPARTMENT="https://dashboard.kanalytics.in/mobile_app/attendance/departmnt.php";
    private String UPDATE_DETAILS="https://dashboard.kanalytics.in/mobile_app/attendance/updateExistUser.php";
    private String REPORTING_TO="https://dashboard.kanalytics.in/mobile_app/attendance/reporting_to.php";
    String firstName,mobileNumber,officeAddress,dept,reporting_to = "";
    String newUserstatus="true";
    String updateUserStatus="false";
    Intent myIntent;
    private static final String LOG_TAG = "Registration";
    private static final int GOOGLE_API_CLIENT_ID = 0;
    //private Spinner spinnerRoles,spinnerPosition,spinnerDept,spinnerTeamLead;
    private Spinner spinnerReporting,spinnerDept;
    private AutoCompleteTextView mAutocompleteTextView;
    private TextView txtError;
    private GoogleApiClient mGoogleApiClient;
    private JSONArray jsonResult,jsonResponse;
    private ImageView back_btn;
    private LocationSettingsRequest mLocationSettingsRequest;
    private static final int REQUEST_CHECK_SETTINGS = 214;
    private static final int REQUEST_ENABLE_GPS = 516;
    private SettingsClient mSettingsClient;
    private static final String MY_PREFS_NAME = "MyPrefs" ;
    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
            new LatLng(37.398160, -122.180831), new LatLng(37.430610, -121.972090));
    private ArrayList<String> departmentArray;
    private ArrayList<String> reportingArray;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        initView();
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
    /*    spinnerDept.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
          @Override
          public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            dept = getDepartmentId(i);
            reportingArray.clear();
            reporting_to="";
            getDataOfReporting(dept);
          }
          @Override
          public void onNothingSelected(AdapterView<?> adapterView) {
          }
        });
        spinnerReporting.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
          reporting_to = getReportingId(i);
        }
        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
        }
      });*/
    }

  private void initView() {
   /* getDataOfDepartments();*/
    requestForSpecificPermission();
    EnableGPSAutoMatically();
    inputTypeMobileNumber=(EditText) findViewById(R.id.inputMobileNumber);
    myIntent=getIntent();
    back_btn = findViewById(R.id.back_btn);
    name=myIntent.getStringExtra("name");
    contact=myIntent.getStringExtra("contact");
    if(contact.equals("")){
      this.back_btn.setVisibility(View.VISIBLE);
      inputTypeMobileNumber.setEnabled(true);
    }else {
      this.back_btn.setVisibility(View.GONE);
      inputTypeMobileNumber.setEnabled(false);
    }
    this.back_btn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        onBackPressed();
      }
    });
    office_address=myIntent.getStringExtra("office_address");
    updateUserStatus=myIntent.getStringExtra("updateUserStatus");
    btnRegisterUser=(Button) findViewById(R.id.btnRegister);
    inputTypeFirstName=(EditText) findViewById(R.id.inputFirstName);
    inputTypeMobileNumber.setText(contact);
    txtError=(TextView) findViewById(R.id.txtError);
    session = new SessionManager(getApplicationContext());
//    spinnerDept=(Spinner) findViewById(R.id.spinrDept);
//    spinnerReporting=(Spinner) findViewById(R.id.spinrReporting);
    departmentArray=new ArrayList<String>();
    reportingArray=new ArrayList<String>();
    mAutocompleteTextView = (AutoCompleteTextView) findViewById(R.id.inputOfficeAddress);
    mGoogleApiClient = new GoogleApiClient.Builder(Registration.this)
      .addApi(Places.GEO_DATA_API)
      .enableAutoManage(this, GOOGLE_API_CLIENT_ID, this)
      .addConnectionCallbacks(this)
      .build();
    mGoogleApiClient.connect();

    btnRegisterUser.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        firstName=inputTypeFirstName.getText().toString();
        mobileNumber=inputTypeMobileNumber.getText().toString();
        officeAddress=mAutocompleteTextView.getText().toString();
        if(checkConnection()){
          if(!TextUtils.isEmpty(firstName.trim().toString()) && !TextUtils.isEmpty(mobileNumber.trim().toString()) && !TextUtils.isEmpty(officeAddress.trim().toString())){
            inputTypeFirstName.setError(null);
            mAutocompleteTextView.setError(null);
            if(isValidMobile(mobileNumber.trim())){
              inputTypeMobileNumber.setError(null);
              if(!TextUtils.isEmpty(office_lat) && !TextUtils.isEmpty(office_log)){
                checkUser(deviceIMEI);
              }else {
                Toast.makeText(Registration.this, "Location not Found", Toast.LENGTH_SHORT).show();
              }
            }else {
              Toast.makeText(getApplicationContext(), "Mobile number is invalid", Toast.LENGTH_SHORT).show();
              inputTypeMobileNumber.setError("Mobile number is invalid");
            }
          } else if(TextUtils.isEmpty(firstName.trim())){
            Toast.makeText(getApplicationContext(), "Full Name can not be blank", Toast.LENGTH_SHORT).show();
            inputTypeFirstName.setError("Full Name can not be blank");
          }else if(TextUtils.isEmpty(mobileNumber.trim())){
            Toast.makeText(getApplicationContext(), "Mobile Number can not be blank", Toast.LENGTH_SHORT).show();
            inputTypeMobileNumber.setError("Mobile Number can not be blank");
          }else if(TextUtils.isEmpty(officeAddress.trim())){
            Toast.makeText(getApplicationContext(), "Office Address can not be blank", Toast.LENGTH_SHORT).show();
            mAutocompleteTextView.setError("Office Address can not be blank");
          }
        }else {
          Toast.makeText(Registration.this, "Internet connection is not available", Toast.LENGTH_SHORT).show();
        }

      }
    });

    LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
    builder.addLocationRequest(new LocationRequest().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY));
    builder.setAlwaysShow(true);
    mLocationSettingsRequest = builder.build();
    mSettingsClient = LocationServices.getSettingsClient(Registration.this);
    mSettingsClient
      .checkLocationSettings(mLocationSettingsRequest)
      .addOnSuccessListener(new OnSuccessListener<LocationSettingsResponse>() {
        @Override
        public void onSuccess(LocationSettingsResponse locationSettingsResponse) {

          gps = new GPSTracker(Registration.this);
          if(gps.canGetLocation()){
            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();
            office_lat= String.valueOf(latitude);
            office_log=String.valueOf(longitude);
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
            try {
              addresses = geocoder.getFromLocation(Double.valueOf(office_lat), Double.valueOf(office_log), 1);
              if (addresses.size()!=0) {
                String address = addresses.get(0).getAddressLine(0);
                if (address!=null)
                  mAutocompleteTextView.setText(address);
                mAutocompleteTextView.setEnabled(false);
              }
            } catch (IOException e) {
              e.printStackTrace();
            }
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
                rae.startResolutionForResult(Registration.this, REQUEST_CHECK_SETTINGS);
              } catch (IntentSender.SendIntentException sie) {
                Log.e("GPS","Unable to execute request.");
              }
              break;
            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
              Log.e("GPS","Location settings are inadequate, and cannot be fixed here. Fix in Settings.");
          }
        }
      })
      .addOnCanceledListener(new OnCanceledListener() {
        @Override
        public void onCanceled() {
          Log.e("GPS","checkLocationSettings -> onCanceled");
        }
      });
  }

  private boolean isValidMobile(String phone) {
    return (phone.length()==10) && android.util.Patterns.PHONE.matcher(phone).matches();
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
              // All location settings are satisfied. The client can
              // initialize location
              // requests here.
              LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
              builder.addLocationRequest(new LocationRequest().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY));
              builder.setAlwaysShow(true);
              mLocationSettingsRequest = builder.build();
              mSettingsClient = LocationServices.getSettingsClient(Registration.this);
              mSettingsClient
                .checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(new OnSuccessListener<LocationSettingsResponse>() {
                  @Override
                  public void onSuccess(LocationSettingsResponse locationSettingsResponse) {

                    gps = new GPSTracker(Registration.this);
                    if(gps.canGetLocation()){
                      double latitude = gps.getLatitude();
                      double longitude = gps.getLongitude();
                      office_lat= String.valueOf(latitude);
                      office_log=String.valueOf(longitude);
                      Geocoder geocoder;
                      List<Address> addresses;
                      geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                      try {
                        addresses = geocoder.getFromLocation(Double.valueOf(office_lat), Double.valueOf(office_log), 1);
                        if (addresses.size()!=0) {
                          String address = addresses.get(0).getAddressLine(0);
                          if (address!=null)
                            mAutocompleteTextView.setText(address);
                          mAutocompleteTextView.setEnabled(false);
                        }
                      } catch (IOException e) {
                        e.printStackTrace();
                      }
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
                          rae.startResolutionForResult(Registration.this, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException sie) {
                          Log.e("GPS","Unable to execute request.");
                        }
                        break;
                      case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Log.e("GPS","Location settings are inadequate, and cannot be fixed here. Fix in Settings.");
                    }
                  }
                })
                .addOnCanceledListener(new OnCanceledListener() {
                  @Override
                  public void onCanceled() {
                    Log.e("GPS","checkLocationSettings -> onCanceled");
                  }
                });
              break;
            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
              Toast.makeText(Registration.this, "Your GPS is not on", Toast.LENGTH_SHORT).show();
              try {
                status.startResolutionForResult(Registration.this, 1000);
              } catch (IntentSender.SendIntentException e) {
              }
              break;
            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
              Toast.makeText(Registration.this, "Setting change not allowed", Toast.LENGTH_SHORT).show();
              break;
          }
        }
      });
    }
  }

/*  private void getDataOfDepartments() {
    String tag_string_req = "req_register";

    StringRequest strReq=new StringRequest(Request.Method.POST, DEPARTMENT, new Response.Listener<String>() {
      @Override
      public void onResponse(String response) {
        JSONObject j = null;
        try {
          j = new JSONObject(response);
          jsonResult = j.getJSONArray("results");
          getDepartmentId(jsonResult);
        } catch (JSONException e) {
          e.printStackTrace();
        }
      }
    }, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {
        Toast.makeText(getApplicationContext(),
          "Check Database", Toast.LENGTH_LONG).show();
      }
    });
    MyApplication.getInstance().addToRequestQueue(strReq, tag_string_req);
  }*/

/*  private void getDepartmentId(JSONArray j){
    for(int i=0;i<j.length();i++){
      try {
        JSONObject json = j.getJSONObject(i);
        departmentArray.add(json.getString("deptname"));
      } catch (JSONException e) {
        e.printStackTrace();
      }
    }
    spinnerDept.setVisibility(View.VISIBLE);
    spinnerDept.setAdapter(new ArrayAdapter<String>(Registration.this, android.R.layout.simple_spinner_dropdown_item, departmentArray));
  }*/

  private void updateExistingUser(final String name, final String mobile,final String address,final String dept,final String reporting_to) {
    String tag_string_req = "req_register";
    final String mobile_imei_no= deviceIMEI.toString();
    StringRequest strReq = new StringRequest(Request.Method.POST,
      UPDATE_DETAILS, new Response.Listener<String>() {
      @Override
      public void onResponse(String response) {
        hideDialog();
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
            editor.commit();
            session.setLogin(true);
            moveToNextActivity();
          } else if(error.trim().equals("contact already exist")){
            txtError.setText("Mobile Number Already Registered");
            txtError.setVisibility(View.VISIBLE);
            hideDialog();
          }else if(error.trim().equals("imei already exist")){
            txtError.setText("Your device id is already available please contact to admin support");
            txtError.setVisibility(View.VISIBLE);
            hideDialog();
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
          "Network issue please try again later"+error, Toast.LENGTH_LONG).show();
        hideDialog();
      }
    }) {
      @Override
      protected Map<String, String> getParams() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("user_name", name);
        params.put("user_mobile_number", mobile);
        params.put("ofc_address", address);
        params.put("ofc_lat", office_lat);
        params.put("ofc_long", office_log);
        params.put("user_phone_imei",mobile_imei_no);
//        params.put("dept",dept);
//        params.put("reporting_to",reporting_to);
        return params;
      }
    };
    MyApplication.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

  private void checkUser(final String mobile_imei_no) {
    String tag_string_req = "req_register";
    pDialog.setMessage("Registering ...");
    Toast.makeText(getApplicationContext(),"Response "+mobile_imei_no,Toast.LENGTH_LONG).show();
    showDialog();
    StringRequest strReq = new StringRequest(Request.Method.POST, CHECK_USER, new Response.Listener<String>() {
      @Override
      public void onResponse(String response) {
        try{
          JSONObject jObj = new JSONObject(response);
          JSONArray jsonMainNode = jObj.optJSONArray("results");
          JSONObject result=jsonMainNode.getJSONObject(0);
          String error =result.getString("error");
          if (error.trim().equals("true")) {
            String dept_id=result.getString("dept_id");
            updateUserStatus="false";
            if(dept_id.trim().equals("null") || dept_id.trim().equals("0"))
            {
              updateUserStatus="true";
              updateExistingUser(firstName,mobileNumber,officeAddress,dept,reporting_to);
            }else {
              registerUserToServer(firstName,mobileNumber,officeAddress);
            }
          }
          else {
            newregisterUserToServer(firstName,mobileNumber,officeAddress,dept,reporting_to);
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
        params.put("user_phone_imei", mobile_imei_no);
        return params;
      }
    };
    MyApplication.getInstance().addToRequestQueue(strReq, tag_string_req);
  }

/*
  private void getDataOfReporting(final String reportingId) {
    String tag_string_req = "req_register";
    StringRequest strReq=new StringRequest(Request.Method.POST, REPORTING_TO, new Response.Listener<String>() {
      @Override
      public void onResponse(String response) {
        JSONObject j = null;
        try {
          j = new JSONObject(response);
          jsonResponse = j.getJSONArray("results");
          getReportingPersonId(jsonResponse);
        } catch (JSONException e) {
          e.printStackTrace();
        }
      }
    }, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {
        Toast.makeText(getApplicationContext(),
          "Check Database", Toast.LENGTH_LONG).show();
      }
    })
    {
      @Override
      protected Map<String, String> getParams() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("dept_id",reportingId);
        return params;
      }
    };
    MyApplication.getInstance().addToRequestQueue(strReq, tag_string_req);
  }

  private void getReportingPersonId(JSONArray j){
    for(int i=0;i<j.length();i++){
      try {
        JSONObject json = j.getJSONObject(i);
        reportingArray.add(json.getString("tl_name"));
      } catch (JSONException e) {
        e.printStackTrace();
      }
    }
    spinnerReporting.setVisibility(View.GONE);
    spinnerReporting.setAdapter(new ArrayAdapter<String>(Registration.this, android.R.layout.simple_spinner_dropdown_item, reportingArray));
  }
*/

  private boolean checkConnection() {
        return ConnectivityReceiver.isConnected();
    }

    private void showSnack(boolean isConnected) {
        if (isConnected) {
            //registerUserToServer(firstName,mobileNumber,officeAddress);
        } else {
            Toast.makeText(getApplicationContext(),"Sorry! Not connected to internet",Toast.LENGTH_LONG).show();
        }
    }

  public void newregisterUserToServer(final String name, final String mobile,final String address,final String dept,final String reporting_to)
  {
    String tag_string_req = "req_register";
    final String mobile_imei_no= deviceIMEI.toString();
    pDialog.setMessage("Registering ...");
    showDialog();
    StringRequest strReq = new StringRequest(Request.Method.POST,
      URL_REGISTER, new Response.Listener<String>() {
      @Override
      public void onResponse(String response) {

        hideDialog();
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
            String team=result.getString("team_lead");
            String office_in=result.getString("ofc_in_time");
            String office_out=result.getString("ofc_out_time");
            String working_day=result.getString("working_days");
            String location_latitude=result.getString("ofc_latitude");
            String location_longitude=result.getString("ofc_longitude");
            String dept_id=result.getString("dept_id");
            SharedPreferences shared = getSharedPreferences("info",MODE_PRIVATE);
            int status=0;
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
            editor.commit();
            session.setLogin(true);
            newUserstatus = "false";
            moveToNextActivity();
          } else if(error.trim().equals("contact already exist")){
            txtError.setText("Mobile Number Already Registered");
            txtError.setVisibility(View.VISIBLE);
            hideDialog();
            newUserstatus = "true";
          }else if(error.trim().equals("imei already exist")){
            txtError.setText("Your device id is already available please contact to admin support");
            txtError.setVisibility(View.VISIBLE);
            hideDialog();
            newUserstatus = "true";
          }
        } catch (JSONException e) {
          e.printStackTrace();
        }

      }
    }, new Response.ErrorListener() {

      @Override
      public void onErrorResponse(VolleyError error) {
        Toast.makeText(getApplicationContext(),
          "Network issue please try again later"+error, Toast.LENGTH_LONG).show();
        hideDialog();
      }
    }) {
      @Override
      protected Map<String, String> getParams() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("user_name", name);
        params.put("user_mobile_number", mobile);
        params.put("ofc_address", address);
        params.put("ofc_lat", office_lat);
        params.put("ofc_long", office_log);
        params.put("user_phone_imei",mobile_imei_no);
/*        params.put("dept",dept);
        params.put("reporting_to",reporting_to);*/
        return params;
      }
    };


    MyApplication.getInstance().addToRequestQueue(strReq, tag_string_req);
  }


    public void  registerUserToServer(final String name, final String mobile,final String address){
        String tag_string_req = "req_register";
        final String mobile_imei_no= deviceIMEI.toString();
        pDialog.setMessage("Registering ...");
        showDialog();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                URL_REGISTER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                hideDialog();
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
                    String team=result.getString("team_lead");
                    String dept_id=result.getString("dept_id");
                    String office_in=result.getString("ofc_in_time");
                    String office_out=result.getString("ofc_out_time");
                    String working_day=result.getString("working_days");
                    String location_latitude=result.getString("ofc_latitude");
                    String location_longitude=result.getString("ofc_longitude");
                    SharedPreferences shared = getSharedPreferences("info",MODE_PRIVATE);
                    int status=0;
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
                    editor.commit();
                    session.setLogin(true);
                    newUserstatus = "false";
                    moveToNextActivity();
                } else if(error.trim().equals("contact already exist")){
                    txtError.setText("Mobile Number Already Registered");
                    txtError.setVisibility(View.VISIBLE);
                    hideDialog();
                    newUserstatus = "true";
                }else if(error.trim().equals("imei already exist")){
                    txtError.setText("Your device id is already available please contact to admin support");
                    txtError.setVisibility(View.VISIBLE);
                    hideDialog();
                  newUserstatus = "true";
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Network issue please try again later", Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_name", name);
                params.put("user_mobile_number", mobile);
                params.put("ofc_address", address);
                params.put("ofc_lat", office_lat);
                params.put("ofc_long", office_log);
                params.put("user_phone_imei",mobile_imei_no);
                return params;
            }
        };
        MyApplication.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

     void moveToNextActivity(){
    Intent intent=new Intent(Registration.this,NavigationDrawerActivity.class);
    SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
    editor.putString("newUserstatus", newUserstatus);
    editor.apply();
    startActivity(intent);
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
        int hasPermission = ContextCompat.checkSelfPermission(this,Manifest.permission.READ_PHONE_STATE);
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
            pref = getSharedPreferences("info", MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("deviceIMEI",deviceIMEI);
            editor.commit();
        }else{
            requestForSpecificPermission();
        }
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i(LOG_TAG, "Google Places API connected.");
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.e(LOG_TAG, "Google Places API connection suspended.");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e(LOG_TAG, "Google Places API connection failed with error code: " + connectionResult.getErrorCode());
        Toast.makeText(this, "Google Places API connection failed with error code:" + connectionResult.getErrorCode(), Toast.LENGTH_LONG).show();
    }

    private String getDepartmentId(int position){
      String departmentId = "";
      try {
        JSONObject json = jsonResult.getJSONObject(position);
        departmentId = json.getString("deptId");
      } catch (JSONException e) {
        e.printStackTrace();
      }
      return departmentId;
    }

  private String getReportingId(int position){
    String reportingId = "";
    try {
      JSONObject json = jsonResponse.getJSONObject(position);
      reportingId = json.getString("tl_id");
    } catch (JSONException e) {
      e.printStackTrace();
    }
    return reportingId;
  }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack(isConnected);
    }

  /*@Override
  public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
      // Toast.makeText(getApplicationContext(),"Department Id "+getDepartmentId(position),Toast.LENGTH_LONG).show();
      dept = getDepartmentId(position);
      //getDataOfReporting(dept);
  }
  @Override
  public void onNothingSelected(AdapterView<?> parent) {
  }*/
  @Override
  public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    if (requestCode == REQUEST_CHECK_SETTINGS) {
      switch (resultCode) {
        case Activity.RESULT_OK:
          //Success Perform Task Here
          gps = new GPSTracker(this);
          if(gps.canGetLocation()){
            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();
            office_lat= String.valueOf(latitude);
            office_log=String.valueOf(longitude);
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
            try {
              addresses = geocoder.getFromLocation(Double.valueOf(office_lat), Double.valueOf(office_log), 1);
              if (addresses.size()!=0) {
                String address = addresses.get(0).getAddressLine(0);
                if (address!=null)
                  mAutocompleteTextView.setText(address);
                mAutocompleteTextView.setEnabled(false);
              }
            } catch (IOException e) {
              e.printStackTrace();
            }
          }
          break;
        case Activity.RESULT_CANCELED:
          Log.e("GPS","User denied to access location");
          openGpsEnableSetting();
          break;
      }
    } else if (requestCode == REQUEST_ENABLE_GPS) {
      LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
      boolean isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
      if (!isGpsEnabled) {
        openGpsEnableSetting();
      }
    }else if(requestCode == 1000){
      if(resultCode == Activity.RESULT_OK){
        String result=data.getStringExtra("result");

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(new LocationRequest().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY));
        builder.setAlwaysShow(true);
        mLocationSettingsRequest = builder.build();
        mSettingsClient = LocationServices.getSettingsClient(Registration.this);
        mSettingsClient
          .checkLocationSettings(mLocationSettingsRequest)
          .addOnSuccessListener(new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {

              gps = new GPSTracker(Registration.this);
              if(gps.canGetLocation()){
                double latitude = gps.getLatitude();
                double longitude = gps.getLongitude();
                office_lat= String.valueOf(latitude);
                office_log=String.valueOf(longitude);
                Geocoder geocoder;
                List<Address> addresses;
                geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                try {
                  addresses = geocoder.getFromLocation(Double.valueOf(office_lat), Double.valueOf(office_log), 1);
                  if (addresses.size()!=0) {
                    String address = addresses.get(0).getAddressLine(0);
                    if (address!=null)
                      mAutocompleteTextView.setText(address);
                    mAutocompleteTextView.setEnabled(false);
                  }
                } catch (IOException e) {
                  e.printStackTrace();
                }
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
                    rae.startResolutionForResult(Registration.this, REQUEST_CHECK_SETTINGS);
                  } catch (IntentSender.SendIntentException sie) {
                    Log.e("GPS","Unable to execute request.");
                  }
                  break;
                case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                  Log.e("GPS","Location settings are inadequate, and cannot be fixed here. Fix in Settings.");
              }
            }
          })
          .addOnCanceledListener(new OnCanceledListener() {
            @Override
            public void onCanceled() {
              Log.e("GPS","checkLocationSettings -> onCanceled");
            }
          });
      }
    }
  }

  private void openGpsEnableSetting() {
    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
    startActivityForResult(intent, REQUEST_ENABLE_GPS);
  }

}
