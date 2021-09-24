package kattendance.dashboard.kanalytics.in.kattendance;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Backlogstatus extends AppCompatActivity {
  Backlogstatusadapter backlogstatusadapter;
  ActionBar ab;
  DatePickerDialog.OnDateSetListener date;
  EditText inputDate;
  LinearLayout NoDAta,list;
  String currentDateandTime;
  java.util.Calendar myCalendar;
  List<Backlogmodelstatus> statuslistt;
  RecyclerView recyclerView;
  String user_id;
  private static final String BACKLOCK_LIST = "https://dashboard.kanalytics.in/mobile_app/attendance/backlog_adminlist.php";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_backlockstatus);

    ab = getSupportActionBar();
    ab.setDisplayHomeAsUpEnabled(true);
    SharedPreferences shared = getSharedPreferences("info", MODE_PRIVATE);
    SharedPreferences Status = getSharedPreferences("Status", MODE_PRIVATE);
    NoDAta=findViewById(R.id.nodataid);
    list=findViewById(R.id.listid);
    user_id = shared.getString("user_id", "");
    recyclerView =findViewById(R.id.usarbacklockstatus);

    statuslistt=new ArrayList<>();
    inputDate=findViewById(R.id.inputTypeDateRepotingPeople);
    inputDate.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        selectDate();
      }
    });
    SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    currentDateandTime = date_format.format(new Date());

    myCalendar = java.util.Calendar.getInstance();
    date = new DatePickerDialog.OnDateSetListener() {
      @Override
      public void onDateSet(DatePicker view, int year, int monthOfYear,
                            int dayOfMonth) {
        myCalendar.set(java.util.Calendar.YEAR, year);
        myCalendar.set(java.util.Calendar.MONTH, monthOfYear);
        myCalendar.set(java.util.Calendar.DAY_OF_MONTH, dayOfMonth);
        String myFormat = "dd-MM-yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
        inputDate.setText(sdf.format(myCalendar.getTime()));
        statuslistt.clear();
        Checkbacklock(sdf.format(myCalendar.getTime()));
      }
    };
    Checkbacklock(currentDateandTime);
  }
  private void Checkbacklock(final String currentDateandTime) {
//    checkConnection();
    String tag_string_req = "req_register";

    //Toast.makeText(this, "Checking user", Toast.LENGTH_SHORT).show();
    StringRequest strReq = new StringRequest(Request.Method.POST, BACKLOCK_LIST, new Response.Listener<String>() {
      @Override
      public void onResponse(String response) {

        try {
          JSONObject jObj = new JSONObject(response);
          Log.i("dataresponse", String.valueOf(jObj));
          JSONArray jsonMainNode = jObj.optJSONArray("results");
          String Error = jObj.getString("error");
          if(Error.equals("false"))
          {
            NoDAta.setVisibility(View.VISIBLE);
            list.setVisibility(View.GONE);
          }else {
            list.setVisibility(View.VISIBLE);
            NoDAta.setVisibility(View.GONE);
            for (int i = 0; i < jsonMainNode.length(); i++) {


              JSONObject result = jsonMainNode.getJSONObject(i);


              Backlogmodelstatus backlogmodelstatus = new Backlogmodelstatus();
              String username = result.getString("username");
              String attendance_date = result.getString("attendance_date");
              String login = result.getString("login");
              String logout = result.getString("logout");
              String reason = result.getString("reason");
              String status = result.getString("status");

              backlogmodelstatus.setUsername(username);
              backlogmodelstatus.setAttendance_date(attendance_date);
              backlogmodelstatus.setLogin(login);
              backlogmodelstatus.setLogout(logout);
              backlogmodelstatus.setReason(reason);
              backlogmodelstatus.setStatus(status);

              statuslistt.add(backlogmodelstatus);


              Log.i("checkinacklocb", username);
              Log.i("checkinacklocb", attendance_date);
              Log.i("checkinacklocb", status);
//              Toast.makeText(BacklockList.this, "checkin empty", Toast.LENGTH_SHORT).show();


            }

            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            backlogstatusadapter = new Backlogstatusadapter(getApplicationContext(), statuslistt);
            recyclerView.setAdapter(backlogstatusadapter);
            RecyclerView.ItemDecoration decoration = new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL);
            recyclerView.addItemDecoration(decoration);
            backlogstatusadapter.notifyDataSetChanged();
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

        params.put("user", user_id);
        params.put("action", "userhistory");
        params.put("month", currentDateandTime);

//        params.put("user_phone_imei", imei_no);
        return params;
      }

    };
    MyApplication.getInstance().addToRequestQueue(strReq, tag_string_req);
  }
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    onBackPressed();
    return true;
  }
  public void selectDate(){
    DatePickerDialog dp = new DatePickerDialog(Backlogstatus.this, date, myCalendar
      .get(java.util.Calendar.YEAR), myCalendar.get(java.util.Calendar.MONTH),
      myCalendar.get(java.util.Calendar.DAY_OF_MONTH));
    dp.show();
  }
}
