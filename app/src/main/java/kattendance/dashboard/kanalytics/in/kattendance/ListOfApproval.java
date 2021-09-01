package kattendance.dashboard.kanalytics.in.kattendance;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ListView;

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



public class ListOfApproval extends AppCompatActivity {
  ActionBar ab;
  FrameLayout NoWifiLayout;
  ListView listView;
  ProgressDialog pDialog;

  private List<ListOfApprovalHistory>   approvalList=new ArrayList<ListOfApprovalHistory>();
  private static final String REQUEST_FOR_LEAVE_LIST = "https://dashboard.kanalytics.in/mobile_app/attendance/request_for_leave_list.php";
  private ListOfApprovalAdapter adapter;
  String user_id, reason1, reason2, singledate, multiplefromdate, multipletodate;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_approval_list);
    pDialog = new ProgressDialog(this);
    pDialog.setCancelable(false);
    ab = getSupportActionBar();
    ab.setDisplayHomeAsUpEnabled(true);
    SharedPreferences shared = getSharedPreferences("info", MODE_PRIVATE);
    user_id = shared.getString("user_id", "");
    listView = (ListView) findViewById(R.id.list);
    adapter=new ListOfApprovalAdapter(this,approvalList);
    listView.setAdapter(adapter);
    /*singledate=shared.getString("singledate","");
    reason1=shared.getString("reason1","");
    multiplefromdate=shared.getString("multiplefromdate","");
    multipletodate= shared.getString("multipletodate","");
    reason2=shared.getString("reason2","");*/


    checkConnection();
    getJsonDataFromUrl(user_id);

  }

  private void getJsonDataFromUrl(final String user_id) {

    pDialog.setMessage("Loading Please wait....");
    showDialog();

    StringRequest strReq = new StringRequest(Request.Method.POST,
      REQUEST_FOR_LEAVE_LIST, new Response.Listener<String>() {
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
            String authority_id=result.getString("authority_id");

            //Toast.makeText(ListOfApproval.this, "auth id" + authority_id, Toast.LENGTH_SHORT).show();

            SharedPreferences sharedPreferences=getSharedPreferences("info",MODE_PRIVATE);
            SharedPreferences.Editor editor=sharedPreferences.edit();
            editor.putString("authority_id",authority_id);
            editor.putString("requestleave_id",id);
            editor.commit();


            final ListOfApprovalHistory approval=new ListOfApprovalHistory();

            approval.setDate(result.getString("from_date"));
            approval.setTitle(result.getString("title"));
            approval.setDescription(result.getString("reason"));
            approval.setId(result.getString("requestleave_id"));
            approval.setStatus(result.getString("status"));
            approval.setTodate(result.getString("to_date"));
            approval.setRejoindate(result.getString("rejoin_date"));
            approvalList.add(approval);

          }

        } catch (JSONException e) {
          e.printStackTrace();
        }
        adapter.notifyDataSetChanged();
      }
    }, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError volleyError) {

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

  private void checkConnection() {
    boolean isConnected = ConnectivityReceiver.isConnected();
    showSnack(isConnected);
  }
  private void showSnack(boolean isConnected) {

    if (isConnected) {



    } else {
      NoWifiLayout=(FrameLayout) findViewById(R.id.internetConnectivityLayout);
      NoWifiLayout.setVisibility(View.VISIBLE);
      //  Toast.makeText(getApplicationContext(), "Sorry! Not connected to internet", Toast.LENGTH_LONG).show();

    }
  }
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    // getMenuInflater().inflate(R.menu.main, menu);
    return true;
  }
  @Override
  public void onResume()
  {
    // getJsonDataFromUrl(user_id);
    super.onResume();
  }
  @Override
  public void onRestart() {
    // getJsonDataFromUrl(user_id);
    super.onRestart();
    Intent previewMessage = new Intent(ListOfApproval.this, ListOfApproval.class);
    startActivity(previewMessage);

    //When BACK BUTTON is pressed, the activity on the stack is restarted
    //Do what you want on the refresh procedure here
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    onBackPressed();
    return true;
  }

  @Override
  public void onBackPressed() {
    super.onBackPressed();
    Intent moveToNextAcitivty=new Intent(ListOfApproval.this,NavigationDrawerActivity.class);

    //  moveToNextAcitivty.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
    startActivity(moveToNextAcitivty);
    finish();
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
