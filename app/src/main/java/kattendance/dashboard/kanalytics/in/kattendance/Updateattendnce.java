package kattendance.dashboard.kanalytics.in.kattendance;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Updateattendnce extends AppCompatActivity {
  private Activity activity;
  Adapter adapter;
  TextView login,logout;
  String lat,Lon;
  Button update;
  BacklockList backlockList;
  List<Backlockmodel> questionlist;
  TextView date;
  EditText message;
  private GpsTrackeNew gpsTracker;
  String user_id;
  private static final String UPDATEDATTAENDACE_LIST = "https://dashboard.kanalytics.in/mobile_app/attendance/backlog_submit.php";
  private static final String NOTIFICATION_FROM_UPDATE_ADMIN = "https://dashboard.kanalytics.in/mobile_app/attendance/backlog_action.php";
  private Intent myIntent;
  String datetext,checkintext,checkouttext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updateattendnce);
      SharedPreferences shared = getSharedPreferences("info", MODE_PRIVATE);
      SharedPreferences Status = getSharedPreferences("Status", MODE_PRIVATE);

      questionlist=new ArrayList<>();
      user_id = shared.getString("user_id", "");
      Log.i("userid",user_id);
        login=findViewById(R.id.loginedit);
        logout=findViewById(R.id.logoutedit);

        date=findViewById(R.id.datefetch);
        update=findViewById(R.id.updateid);
        message=findViewById(R.id.resoenedit);
      myIntent=getIntent();
      datetext=myIntent.getStringExtra("Date");
      checkintext=myIntent.getStringExtra("checkin");
      checkouttext=myIntent.getStringExtra("checkout");
      date.setText(datetext);
      if(checkintext.equals(""))
      {
        login.setText("00:00:00");
        login.setOnClickListener(new View.OnClickListener() {

          @Override
          public void onClick(View v) {
            // TODO Auto-generated method stub
            Calendar mcurrentTime = Calendar.getInstance();
            int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
            int minute = mcurrentTime.get(Calendar.MINUTE);
            int second = mcurrentTime.get(Calendar.SECOND);
            TimePickerDialog mTimePicker;
            mTimePicker = new TimePickerDialog(Updateattendnce.this, new TimePickerDialog.OnTimeSetListener() {
              @Override
              public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
//              logout.setText( selectedHour + ":" + selectedMinute);
                login.setText(String.format("%02d:%02d", selectedHour, selectedMinute));
              }
            }, hour, minute, true);//Yes 24 hour time
            mTimePicker.setTitle("Select Time");
            mTimePicker.show();

          }
        });
      }else
      {
        login.setText(checkintext);
      }
        if(checkouttext.equals(""))
      {
        logout.setText("00:00:00");
        logout.setOnClickListener(new View.OnClickListener() {

          @Override
          public void onClick(View v) {
            // TODO Auto-generated method stub
            Calendar mcurrentTime = Calendar.getInstance();
            int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
            int minute = mcurrentTime.get(Calendar.MINUTE);
            int second = mcurrentTime.get(Calendar.SECOND);
            TimePickerDialog mTimePicker;
            mTimePicker = new TimePickerDialog(Updateattendnce.this, new TimePickerDialog.OnTimeSetListener() {
              @Override
              public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
//              logout.setText( selectedHour + ":" + selectedMinute);
                logout.setText(String.format("%02d:%02d", selectedHour, selectedMinute));
              }
            }, hour, minute, true);//Yes 24 hour time
            mTimePicker.setTitle("Select Time");
            mTimePicker.show();

          }
        });
      }else {

        logout.setText(checkouttext);
      }






      update.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

          getLocation();
          Updateattaendace();
        }
      });

    }

  private void Updateattaendace() {
      String reason=message.getText().toString();
      String intime=login.getText().toString();
      String outtime=logout.getText().toString();
      Log.i("loingintime",intime);
      if(intime.equals("00:00:00"))
      {
        Toast.makeText(Updateattendnce.this, "Enter IN Time", Toast.LENGTH_SHORT).show();
      }else if(outtime.equals("00:00:00"))
      {
        Toast.makeText(Updateattendnce.this, "Enter Out Time", Toast.LENGTH_SHORT).show();
      }else if(reason.equals(""))
      {
        Toast.makeText(Updateattendnce.this, "Enter Reason", Toast.LENGTH_SHORT).show();
      }
      else {

//
        String tag_string_req = "req_register";

        //Toast.makeText(this, "Checking user", Toast.LENGTH_SHORT).show();
        StringRequest strReq = new StringRequest(Request.Method.POST, UPDATEDATTAENDACE_LIST, new Response.Listener<String>() {
          @Override
          public void onResponse(String response) {

            try {
              JSONObject jObj = new JSONObject(response);
              Log.i("dataresponse", String.valueOf(jObj));
              JSONObject jsonMainNode = jObj.optJSONObject("result");
              String Msg = jsonMainNode.getString("msg");
              Toast.makeText(Updateattendnce.this, Msg, Toast.LENGTH_SHORT).show();
              acceptnotificationToadmin();

              finish();

//              List<Backlockmodel>newList =questionlist;
//              backlockList.finish();
//              Intent backlcok = new Intent(Updateattendnce.this, BacklockList.class);
//              startActivity(backlcok);




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

            params.put("userid", user_id);
            params.put("date", datetext);
            params.put("reason", reason);
            params.put("login", intime);
            params.put("logout", outtime);
            params.put("lat", lat);
            params.put("long", Lon);


//        params.put("user_phone_imei", imei_no);
            return params;
          }

        };
        MyApplication.getInstance().addToRequestQueue(strReq, tag_string_req);
      }
  }
  public void getLocation(){
    gpsTracker = new GpsTrackeNew(Updateattendnce.this);
    if(gpsTracker.canGetLocation()){
      double latitude = gpsTracker.getLatitude();
      double longitude = gpsTracker.getLongitude();
     lat=(String.valueOf(latitude));
     Lon=(String.valueOf(longitude));
      Log.i("currentlat",Lon);
    }else{
      gpsTracker.showSettingsAlert();
    }
  }
  private void acceptnotificationToadmin() {
    StringRequest strReq = new StringRequest(Request.Method.POST,
      NOTIFICATION_FROM_UPDATE_ADMIN, new Response.Listener<String>() {
      @Override
      public void onResponse(String s) {

      }
    }, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError volleyError) {
        Toast.makeText(activity,volleyError.getMessage(), Toast.LENGTH_LONG).show();

      }
    })
    {
      @Override
      protected Map<String, String> getParams() {
        // Posting params to register url

        Map<String, String> params = new HashMap<String, String>();
        params.put("user",user_id);
        params.put("action", "notify");
        params.put("date", String.valueOf(date));
        params.put("to","admin");
        return params;
      }
    };
    MyApplication.getInstance().addToRequestQueue(strReq);
  }
}
