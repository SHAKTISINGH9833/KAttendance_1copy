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

public class BacklockList extends AppCompatActivity {
  Adapter adapter;
  ActionBar ab;
  List<Backlockmodel> questionlist;
  RecyclerView recyclerView;
  String user_id;
  private static final String BACKLOCK_LIST = "https://dashboard.kanalytics.in/mobile_app/attendance/backlog_check.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backlock_list);
      ab = getSupportActionBar();
      ab.setDisplayHomeAsUpEnabled(true);
      SharedPreferences shared = getSharedPreferences("info", MODE_PRIVATE);
      SharedPreferences Status = getSharedPreferences("Status", MODE_PRIVATE);

      user_id = shared.getString("user_id", "");
      recyclerView =findViewById(R.id.qeuetionrcyclivwe);
      questionlist=new ArrayList<>();
      Checkbacklock(user_id);

    }
  private void Checkbacklock(final String user_id) {
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
          JSONArray Error = jObj.optJSONArray("error");
          for (int i = 0; i < jsonMainNode.length(); i++) {


            JSONObject result = jsonMainNode.getJSONObject(i);

            String checkout = result.getString("checkout");
            String checkin = result.getString("checkin");

            if(checkout.equals("")|| checkin.equals(""))
            {
              Backlockmodel backlockmodel=new Backlockmodel();
              String date = result.getString("date");
              String newcheckout = result.getString("checkout");
              String newcheckin = result.getString("checkin");
              backlockmodel.setDate(date);
              backlockmodel.setCheckin(newcheckin);
              backlockmodel.setCheckout(newcheckout);
              questionlist.add(backlockmodel);


              Log.i("checkinacklocb",date);
              Log.i("checkinacklocb",newcheckout);
              Log.i("checkinacklocb",newcheckin);
//              Toast.makeText(BacklockList.this, "checkin empty", Toast.LENGTH_SHORT).show();
            }




          }
          recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
          adapter =new Adapter(getApplicationContext(),questionlist);
          recyclerView.setAdapter(adapter);
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

        params.put("user_id", user_id);
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
