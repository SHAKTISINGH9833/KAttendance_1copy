package kattendance.dashboard.kanalytics.in.kattendance;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
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
import java.util.Map;


public class ReportingPeopleActivity extends AppCompatActivity implements ReportingAttendance {


  RecyclerView recyclerView;
  LinearLayoutManager manager;
  ArrayList<ReportingPeople> arrayList = new ArrayList<>();
  private static String team_attendance = "https://dashboard.kanalytics.in/mobile_app/attendance/team_attendance.php";
  private static String user_id;
  ReportingPeopleAdapter reportingPeopleAdapter;
  private ProgressDialog pDialog;
  TextView notFound;

  @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      setContentView(R.layout.fragment_reporting_people);
      recyclerView =  findViewById(R.id.people_list_rv);
      notFound = findViewById(R.id.notFound);
       getSupportActionBar().setDisplayHomeAsUpEnabled(true);
      pDialog = new ProgressDialog(this);
      pDialog.setCancelable(false);
     pDialog.setMessage("Loading Please wait....");
    showDialog();
      callWebService();
    }



  private void callWebService() {

      String tag_string_req = "req_register";
    SharedPreferences shared = getSharedPreferences("info", MODE_PRIVATE);
    user_id = shared.getString("user_id", "");

    StringRequest strReq = new StringRequest(Request.Method.POST,
      team_attendance, new Response.Listener<String>() {
      @Override
      public void onResponse(String response) {

        try {

          JSONObject jObj = new JSONObject(response);
          JSONArray jsonMainNode = jObj.optJSONArray("result");
          if (jObj.optString("statusMessage").trim().equals("Success")) {
            Repoting repoting = new Repoting();
            repoting.setStatusMessage(jObj.optString("statusMessage"));
            repoting.setStatusCode(jObj.optString("statusCode"));
         for(int a = 0; a< jsonMainNode.length() ; a++){
           ReportingPeople reportingPeople = new ReportingPeople();
           reportingPeople.setId(jsonMainNode.getJSONObject(a).optString("id"));
           reportingPeople.setName(jsonMainNode.getJSONObject(a).optString("name"));
           reportingPeople.setContact(jsonMainNode.getJSONObject(a).optString("contact"));
           reportingPeople.setActualInTime(jsonMainNode.getJSONObject(a).optString("actual_in_time"));
           reportingPeople.setActualOutTime(jsonMainNode.getJSONObject(a).optString("actual_out_time"));
           reportingPeople.setReasonIn(jsonMainNode.getJSONObject(a).optString("reason_in"));
           reportingPeople.setReasonOut(jsonMainNode.getJSONObject(a).optString("reason_out"));
           reportingPeople.setTeamLead(jsonMainNode.getJSONObject(a).optString("team_lead"));
         arrayList.add(reportingPeople);
         }

            manager = new LinearLayoutManager(ReportingPeopleActivity.this,LinearLayoutManager.VERTICAL,false);
            reportingPeopleAdapter = new ReportingPeopleAdapter(ReportingPeopleActivity.this,arrayList, ReportingPeopleActivity.this);
            recyclerView.setLayoutManager(manager);
            recyclerView.setAdapter(reportingPeopleAdapter);
            recyclerView.hasFixedSize();
            recyclerView.setNestedScrollingEnabled(false);
            notFound.setVisibility(View.GONE);
          }else {
            notFound.setVisibility(View.VISIBLE);
          }

          hideDialog();
        } catch (JSONException e) {

          e.printStackTrace();

        }
      }
    }, new Response.ErrorListener() {

      @Override
      public void onErrorResponse(VolleyError error) {
        Toast.makeText(ReportingPeopleActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
      }
    }) {

      @Override
      protected Map<String, String> getParams() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("team_lead", user_id);
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

  @Override
  public void onBackPressed() {
    super.onBackPressed();

  }

  private void showDialog() {
    if (!pDialog.isShowing())
      pDialog.show();
  }
  private void hideDialog() {
    if (pDialog.isShowing())
      pDialog.dismiss();
    pDialog = null;
  }

  @Override
  public void onClickReportingPeople(String id,String name) {
    ReportinPeopleAttendanceList bottomSheet = new ReportinPeopleAttendanceList();
    Bundle bundle = new Bundle();
    bundle.putString("userId", id);
    bundle.putString("userName",name);
    bottomSheet.setArguments(bundle);
    bottomSheet.show(getSupportFragmentManager(),bottomSheet.getTag());
  }
}
