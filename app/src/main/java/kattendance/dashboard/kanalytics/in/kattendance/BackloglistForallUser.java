package kattendance.dashboard.kanalytics.in.kattendance;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.Objects;

public class BackloglistForallUser extends AppCompatActivity implements Spinner.OnItemSelectedListener{
  public static final String Feedbacksave = "https://dashboard.kanalytics.in/mobile_app/grumble_morth/android/questioners_list_save_v2.php";
  //Declaring an Spinner
TextView message;
    String notiuserid;
  private Spinner spinner;
  EditText inputDate;
  LinearLayout NoDAta,list;
  Intent myIntent;
  String compareValue = "87";
  String currentDateandTime,notificationuserid;
  DatePickerDialog.OnDateSetListener date;
  java.util.Calendar myCalendar;
  DatePickerDialog datePickerDialog;
  String userid,authority_id,User_id;
  SharedPreferences pref,shared;
  private ArrayList<String> students;
  private ArrayList<String> studentsid;
  private static final String USERBACKLOCKLIST_ADMIN = "https://dashboard.kanalytics.in/mobile_app/attendance/backlog_adminlist.php";
  private JSONArray result;

  Adminbaclockadapter adminbaclockadapter;
  ActionBar ab;
  List<Backlockuserlistadminmodel> backlockuserlistadminmodelList;

  RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backlocklist_forall_user);
      ab = getSupportActionBar();
      ab.setDisplayHomeAsUpEnabled(true);
      message =findViewById(R.id.displayid);

      if (getIntent().getExtras() != null)
      {
        for(String key : getIntent().getExtras().keySet())
        {
          if (key.equals("message"));

          notiuserid=getIntent().getExtras().getString("userid");
          Log.i("notifyid",notiuserid);
        }
      }



      students = new ArrayList<String>();
      studentsid = new ArrayList<String>();
      recyclerView =findViewById(R.id.userdetailsid);
     NoDAta=findViewById(R.id.nodataid);
      list=findViewById(R.id.listid);
      backlockuserlistadminmodelList=new ArrayList<>();
      //Initializing Spinner
      spinner = (Spinner) findViewById(R.id.userlistid);
      shared = getSharedPreferences("info", MODE_PRIVATE);
      User_id=shared.getString("user_id","");
      authority_id=shared.getString("team_lead","");
      Log.e("adimdid",authority_id);
      inputDate=findViewById(R.id.inputTypeDateRepotingPeople);
      inputDate.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          selectDate();
        }
      });
      //Adding an Item Selected Listener to our Spinner
      //As we have implemented the class Spinner.OnItemSelectedListener to this class iteself we are passing this to setOnItemSelectedListener
      spinner.setOnItemSelectedListener(this);
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
          backlockuserlistadminmodelList.clear();
          Listuserbacklockaadminside(sdf.format(myCalendar.getTime()));
        }
      };

      getData();

    }


  private void getQuestionlist(JSONArray j) {
    //Traversing through all the items in the json array
    for (int i = 0; i < j.length(); i++) {
      try {
        //Getting json object
        JSONObject json = j.getJSONObject(i);

        //Adding the name of the student to array list
        students.add(json.getString(Config.TAG_USERNAME));
        studentsid.add(json.getString(Config.TAG_USERID));
      } catch (JSONException e) {
        e.printStackTrace();
      }
    }

    //Setting adapter to show the items in the spinner

    spinner.setAdapter(new ArrayAdapter<String>(BackloglistForallUser.this, android.R.layout.simple_spinner_dropdown_item, students));
    if (notiuserid != null) {
      int firstIndex = studentsid.indexOf(notiuserid);

      spinner.setSelection(firstIndex);
    }


  }
  public void SelectSpinnerValue(View view)

  {
    spinner.setSelection(536);
  }

  //Method to get student name of a particular position


  //Doing the same with this method as we did with getName()
  private String getuser_id(int position) {
    String course = "";
    try {
      JSONObject json = result.getJSONObject(position);
      course = json.getString(Config.TAG_USERID);
      if (compareValue != null) {

        spinner.setSelection(position);
      }
    } catch (JSONException e) {
      e.printStackTrace();
    }
    return course;
  }

  //Doing the same with this method as we did with getName()



  //this method will execute when we pic an item from the spinner
  @Override
  public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
    //Setting the values to textviews for a selected item
//    if (compareValue != null) {
//      int spinnerPosition = getPosition(compareValue);
//      spinner.setSelection(spinnerPosition);
//    }
    userid=getuser_id(position);

    Log.i("selectedid",userid);
    Listuserbacklockaadminside(currentDateandTime);

  }

  //When no item is selected this method would execute
  @Override
  public void onNothingSelected(AdapterView<?> parent) {

  }

  private void getData() {

    StringRequest strReq = new StringRequest(Request.Method.POST, Config.DATA_URL, new Response.Listener<String>() {
      @Override
      public void onResponse(String s) {
        JSONObject j = null;
        try {



          j = new JSONObject(s);
          String error=j.getString("error");

          if(error.equals("false"))
          {
            NoDAta.setVisibility(View.VISIBLE);
            list.setVisibility(View.GONE);
          }
          else {
            //Storing the Array of JSON String to our JSON Array
            result = j.getJSONArray(Config.JSON_ARRAY);


            getQuestionlist(result);
          }

          //Calling method getStudents to get the students from the JSON Array



        } catch (JSONException e) {
          e.printStackTrace();
        }
      }
    }, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {
        Toast.makeText(getApplicationContext(),
          error.getMessage(), Toast.LENGTH_LONG).show();
      }
    }) {
      @Override
      protected Map<String, String> getParams() {
        Map<String, String> params = new HashMap<String, String>();

        params.put("admin", User_id);
        params.put("action", "userlist");
        return params;
      }
    };
    MyApplication.getInstance().addToRequestQueue(strReq);

  }



  private void Listuserbacklockaadminside(final String currentDateandTime) {


    backlockuserlistadminmodelList.clear();
//    checkConnection();
    String tag_string_req = "req_register";

    //Toast.makeText(this, "Checking user", Toast.LENGTH_SHORT).show();
    StringRequest strReq = new StringRequest(Request.Method.POST, USERBACKLOCKLIST_ADMIN, new Response.Listener<String>() {
      @Override
      public void onResponse(String response) {

        try {
          JSONObject jObj = new JSONObject(response);
          Log.i("dataresponse", String.valueOf(jObj));
          JSONArray jsonMainNode = jObj.optJSONArray("results");
          JSONArray Error = jObj.optJSONArray("error");
          for (int i = 0; i < jsonMainNode.length(); i++) {


            JSONObject result = jsonMainNode.getJSONObject(i);


              Backlockuserlistadminmodel backlockuserlistadminmodel=new Backlockuserlistadminmodel();
              String date = result.getString("attendance_date");
              String name = result.getString("username");
              String Intime = result.getString("login");
              String Out = result.getString("logout");
              String Reason = result.getString("reason");
            String Userid = result.getString("userid");
            String backlogid = result.getString("backlogid");
            String authority_id = result.getString("authority_id");




              backlockuserlistadminmodel.setDate(date);
              backlockuserlistadminmodel.setIn(Intime);
              backlockuserlistadminmodel.setOut(Out);
              backlockuserlistadminmodel.setName(name);
              backlockuserlistadminmodel.setReason(Reason);
            backlockuserlistadminmodel.setUserid(Userid);
            backlockuserlistadminmodel.setBacklogid(backlogid);
            backlockuserlistadminmodel.setAuthority_id(authority_id);

              backlockuserlistadminmodelList.add(backlockuserlistadminmodel);


              Log.i("checkinacklocb",date);

//              Toast.makeText(BacklockList.this, "checkin empty", Toast.LENGTH_SHORT).show();





          }

          recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
          adminbaclockadapter=new Adminbaclockadapter(getApplicationContext(),backlockuserlistadminmodelList);

          recyclerView.setAdapter(adminbaclockadapter);
          RecyclerView.ItemDecoration decoration=new DividerItemDecoration(getApplicationContext(),DividerItemDecoration.VERTICAL);
          recyclerView.addItemDecoration(decoration);

        } catch (JSONException e) {
          e.printStackTrace();
        }
      }
    }, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {

      }
    }) {

      @RequiresApi(api = Build.VERSION_CODES.N)
      @Override
      protected Map<String, String> getParams() {


        Map<String, String> params = new HashMap<String, String>();
if (notiuserid!=null)
{
  if(notiuserid.equals(""))
  {
    params.put("user", userid);
  }
  else {
    params.put("user", notiuserid);
  }
}else {
  params.put("user", userid);
}


        params.put("admin", User_id);
        params.put("action", "backlog");

        params.put("month", currentDateandTime);

//        params.put("user_phone_imei", imei_no);
        Log.i("notificationuserid", String.valueOf(params));
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
    DatePickerDialog dp = new DatePickerDialog(BackloglistForallUser.this, date, myCalendar
      .get(java.util.Calendar.YEAR), myCalendar.get(java.util.Calendar.MONTH),
      myCalendar.get(java.util.Calendar.DAY_OF_MONTH));
    dp.show();
  }
}
