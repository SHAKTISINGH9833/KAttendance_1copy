package kattendance.dashboard.kanalytics.in.kattendance;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
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

public class Expenses extends AppCompatActivity {

  FloatingActionButton fab_button;
  ActionBar ab;
  ListView listView;
  ProgressDialog pDialog;
  String user_id;
  private List<ExpensesHistory> expensesList=new ArrayList<ExpensesHistory>();
  private static final String EXPENSE_LIST = "https://dashboard.kanalytics.in/mobile_app/attendance/expense_list.php";
  private ExpensesAdapter adapter;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_expenses_layout);
    fab_button=(FloatingActionButton)findViewById(R.id.fab);
    ab = getSupportActionBar();
    ab.setDisplayHomeAsUpEnabled(true);

    pDialog=new ProgressDialog(this);

    SharedPreferences shared = getSharedPreferences("info", MODE_PRIVATE);
    user_id = shared.getString("user_id", "");

    listView=(ListView)findViewById(R.id.lvToDoList);
    adapter=new ExpensesAdapter(this,expensesList);
    listView.setAdapter(adapter);

    fab_button.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent=new Intent(Expenses.this,ExpenseForm.class);
        startActivity(intent);
      }
    });

    getJsonDataFromUrl(user_id);
  }

  private void getJsonDataFromUrl(final String user_id) {
    pDialog.setMessage("Loading Please wait....");
    showDialog();

    StringRequest strReq = new StringRequest(Request.Method.POST,
      EXPENSE_LIST, new Response.Listener<String>() {
      @Override
      public void onResponse(String s) {
        hideDialog();
        JSONObject jsonObject = null;
        try {
          jsonObject = new JSONObject(s);
          JSONArray jsonMainNode = jsonObject.optJSONArray("results");

          for (int i = 0; i < jsonMainNode.length(); i++) {


            JSONObject result = jsonMainNode.getJSONObject(i);
            /*String date = result.getString("date");
            String expense_id = result.getString("expense_id");
            String description = result.getString("description");
            String user_id=result.getString("user_id");
            String particulars = result.getString("particulars");
            String amount = result.getString("amount");
            String receipt_img=result.getString("receipt_img");*/

            //Toast.makeText(ListOfApproval.this, "auth id" + authority_id, Toast.LENGTH_SHORT).show();

            /*SharedPreferences sharedPreferences=getSharedPreferences("info",MODE_PRIVATE);
            SharedPreferences.Editor editor=sharedPreferences.edit();
            editor.putString("authority_id",authority_id);
            editor.commit();
            */

            final ExpensesHistory expensesHistoryList=new ExpensesHistory();

            expensesHistoryList.setExpense_id(result.getString("expense_id"));
            expensesHistoryList.setDescription(result.getString("description"));
            expensesHistoryList.setParticulars(result.getString("particulars"));
            expensesHistoryList.setAmount(result.getString("amount"));
            expensesHistoryList.setReceipt_img(result.getString("receipt_img"));
            expensesHistoryList.setDate(result.getString("date"));
            expensesList.add(expensesHistoryList);

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

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    onBackPressed();
    return true;
  }

  @Override
  public void onBackPressed() {
    super.onBackPressed();
    Intent moveToNextAcitivty=new Intent(Expenses.this,NavigationDrawerActivity.class);

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
