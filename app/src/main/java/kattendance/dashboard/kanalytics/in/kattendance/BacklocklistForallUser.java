package kattendance.dashboard.kanalytics.in.kattendance;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BacklocklistForallUser extends AppCompatActivity implements Spinner.OnItemSelectedListener{
  public static final String Feedbacksave = "https://dashboard.kanalytics.in/mobile_app/grumble_morth/android/questioners_list_save_v2.php";
  //Declaring an Spinner

  private Spinner spinner;

  String user_id,authority_id;
  SharedPreferences pref,shared;
  private ArrayList<String> students;
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
      students = new ArrayList<String>();
      recyclerView =findViewById(R.id.userdetailsid);
      backlockuserlistadminmodelList=new ArrayList<>();
      //Initializing Spinner
      spinner = (Spinner) findViewById(R.id.userlistid);
      shared = getSharedPreferences("info", MODE_PRIVATE);
      authority_id=shared.getString("team_lead","");

      //Adding an Item Selected Listener to our Spinner
      //As we have implemented the class Spinner.OnItemSelectedListener to this class iteself we are passing this to setOnItemSelectedListener
      spinner.setOnItemSelectedListener(this);
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
      } catch (JSONException e) {
        e.printStackTrace();
      }
    }

    //Setting adapter to show the items in the spinner
    spinner.setAdapter(new ArrayAdapter<String>(BacklocklistForallUser.this, android.R.layout.simple_spinner_dropdown_item, students));
  }

  //Method to get student name of a particular position


  //Doing the same with this method as we did with getName()
  private String getuser_id(int position) {
    String course = "";
    try {
      JSONObject json = result.getJSONObject(position);
      course = json.getString(Config.TAG_USERID);
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

    user_id=getuser_id(position);

    Log.i("selectedid",user_id);
    Listuserbacklockaadminside(user_id);

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

          //Storing the Array of JSON String to our JSON Array
          result = j.getJSONArray(Config.JSON_ARRAY);



          //Calling method getStudents to get the students from the JSON Array
          getQuestionlist(result);


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
        params.put("admin", authority_id);
        params.put("action", "userlist");
        return params;
      }
    };
    MyApplication.getInstance().addToRequestQueue(strReq);

  }



  private void Listuserbacklockaadminside(String user_id) {
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

      @Override
      protected Map<String, String> getParams() {


        Map<String, String> params = new HashMap<String, String>();

        params.put("admin", authority_id);
        params.put("action", "backlog");
        params.put("user", user_id);
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
}
