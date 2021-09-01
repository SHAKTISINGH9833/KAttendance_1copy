package kattendance.dashboard.kanalytics.in.kattendance;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;
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

public class LeaveApplicantsHistory extends AppCompatActivity {
  ArrayList<LeaveApplicantsItem> leaveApplicantsItems=new ArrayList<LeaveApplicantsItem>();
  String user_id;
  SharedPreferences shared;
  ListView listView;
  private LeaveApplicantsAdapter adapter;
  androidx.appcompat.app.ActionBar ab;
  private ProgressDialog pDialog;
  private List<LeaveApplicantsItem> applicantList=new ArrayList<LeaveApplicantsItem>();
  private static final String LEAVE_APPLICANTS_LIST_HISTORY = "https://dashboard.kanalytics.in/mobile_app/attendance/leave_applicants_list_history.php";
  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_leave_applicants);
    pDialog = new ProgressDialog(this);
    pDialog.setCancelable(false);
    shared=getSharedPreferences("info",MODE_PRIVATE);
    user_id= shared.getString("user_id","");
    Log.i("userid", user_id);
    ab=getSupportActionBar();
    ab.setDisplayHomeAsUpEnabled(true);
    listView = (ListView) findViewById(R.id.list);
    adapter=new LeaveApplicantsAdapter(this,applicantList);
    listView.setAdapter(adapter);
    getValueFromJson(user_id);
  }

  private void getValueFromJson(final String user_id) {
    pDialog.setMessage("Loading Please wait....");
    showDialog();

    StringRequest strReq = new StringRequest(Request.Method.POST,
      LEAVE_APPLICANTS_LIST_HISTORY, new Response.Listener<String>() {
      @Override
      public void onResponse(String s) {
        hideDialog();
        JSONObject jsonObject = null;
        try {
          jsonObject = new JSONObject(s);
          JSONArray jsonMainNode = jsonObject.optJSONArray("results");

          for (int i = 0; i < jsonMainNode.length(); i++) {


            JSONObject result = jsonMainNode.getJSONObject(i);
            String title = result.getString("title");
            String id = result.getString("requestleave_id");
            String desc = result.getString("reason");
            String status=result.getString("status");
            String from_date = result.getString("from_date");
            String to_date = result.getString("to_date");
            String username=result.getString("username");
                        /*SharedPreferences shared=getSharedPreferences("info",MODE_PRIVATE);
                        SharedPreferences.Editor editor = shared.edit();
                        editor.putString("reason",desc);
                        editor.commit();*/

            final LeaveApplicantsItem applicantsItem=new LeaveApplicantsItem();

            applicantsItem.setDate(result.getString("from_date"));
            applicantsItem.setTitle(result.getString("title"));
            applicantsItem.setDescription(result.getString("reason"));
            applicantsItem.setId(result.getString("requestleave_id"));
            applicantsItem.setStatus(result.getString("status"));
            applicantsItem.setTodate(result.getString("to_date"));
            applicantsItem.setUsername(result.getString("username"));
            applicantsItem.setUser_id(result.getString("user_id"));
            applicantsItem.setAuth_id(result.getString("authority_id"));
            applicantsItem.setRejoindate(result.getString("rejoin_date"));
            applicantsItem.setContactleave(result.getString("contact_leave"));
            applicantsItem.setLocationleave(result.getString("location_leave"));
            applicantList.add(applicantsItem);
          }

        } catch (JSONException e) {
          e.printStackTrace();
        }
        adapter.notifyDataSetChanged();
      }
    }, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError volleyError) {
        Toast.makeText(getApplicationContext(),volleyError.getMessage(), Toast.LENGTH_LONG).show();
        hideDialog();
      }
    })
    {
      @Override
      protected Map<String, String> getParams() {
        // Posting params to register url

        Map<String, String> params = new HashMap<String, String>();
        params.put("user_id", user_id);


        return params;
      }
    };
    MyApplication.getInstance().addToRequestQueue(strReq);
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
}
