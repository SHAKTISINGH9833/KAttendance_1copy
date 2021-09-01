package kattendance.dashboard.kanalytics.in.kattendance;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.MenuItem;
import android.view.View;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
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
import java.util.Date;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Devlopment on 15/09/17.
 */
public class AttendanceList extends AppCompatActivity {

    CalendarView calendarview;
    EditText inputDateOfBirth;
    RecyclerView.Adapter mAdapter;
    ArrayList<AttendanceItem> productionItems= new ArrayList<AttendanceItem>();
    RecyclerView mRecyclerView;
    private static String user_id;
    private ProgressDialog pDialog;
    RecyclerView.LayoutManager mLayoutManager;
    ActionBar ab;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private static String GETATTENDANCE_LIST="https://dashboard.kanalytics.in/mobile_app/attendance/attendance_sheet.php";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_list);
        SharedPreferences shared = getSharedPreferences("info",MODE_PRIVATE);
        user_id= shared.getString("user_id","");
        ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        inputDateOfBirth=(EditText) findViewById(R.id.inputTypeDate);
        /*Current Date*/

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new GridLayoutManager(getApplicationContext(),1);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.invalidate();
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentDateandTime = date_format.format(new Date());


        final java.util.Calendar myCalendar = java.util.Calendar.getInstance();

       final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(java.util.Calendar.YEAR, year);
                myCalendar.set(java.util.Calendar.MONTH, monthOfYear);
                myCalendar.set(java.util.Calendar.DAY_OF_MONTH, dayOfMonth);
                //final Calendar c = Calendar.getInstance();
                // mYear = c.get(Calendar.YEAR);
                // mMonth = c.get(Calendar.MONTH);
                //  mDay = c.get(Calendar.DAY_OF_MONTH);

                String myFormat = "dd-MM-yyyy";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
                inputDateOfBirth.setText(sdf.format(myCalendar.getTime()));
                productionItems.clear();
                getFirstJsonValue(sdf.format(myCalendar.getTime()));
            }
        };

        inputDateOfBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dp = new DatePickerDialog(AttendanceList.this, date, myCalendar
                        .get(java.util.Calendar.YEAR), myCalendar.get(java.util.Calendar.MONTH),
                        myCalendar.get(java.util.Calendar.DAY_OF_MONTH));
                dp.show();
            }
        });
      getFirstJsonValue(currentDateandTime);
    }


    private void getFirstJsonValue(final String date){

        String tag_string_req = "req_register";

        pDialog.setMessage("Please Wait ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                GETATTENDANCE_LIST, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                hideDialog();


                try{


                    JSONArray myListsAll= new JSONArray(response);
                    for(int i=0;i<myListsAll.length();i++){
                        JSONObject jsonobject= (JSONObject) myListsAll.get(i);
                        String attendance_date=jsonobject.getString("attendance_date");
                        String attendance_time_in=jsonobject.getString("actual_in_time");
                        String attendance_time_out=jsonobject.getString("actual_out_time");
                        String reason_in=jsonobject.getString("reason_in");
                        String reason_out=jsonobject.getString("reason_out");
                         //Log.e("ALL Value ",attendance_date+","+attendance_time_in+","+attendance_time_out);
                        final AttendanceItem attendanceItem=new AttendanceItem();
                        if(attendance_date.isEmpty()){
                            attendanceItem.setTxtDate("-");
                        }else{
                            attendanceItem.setTxtDate(attendance_date);
                        }
                        if(attendance_time_in.isEmpty()){
                            attendanceItem.setTxtTimeIn("-");
                        }else{
                            attendanceItem.setTxtTimeIn(attendance_time_in);
                        }
                        if(attendance_time_out.isEmpty()){
                            attendanceItem.setTxtTimeOut("-");
                        }else {
                            attendanceItem.setTxtTimeOut(attendance_time_out);
                        }
                        attendanceItem.setReasonIn(reason_in);
                        attendanceItem.setReasonOut(reason_out);

                        productionItems.add(attendanceItem);
                    }

                    mAdapter = new AttendanceAdapter(getApplication(), productionItems);


                    mAdapter.notifyDataSetChanged();
                    mRecyclerView.setAdapter(mAdapter);

                } catch (JSONException e) {

                    e.printStackTrace();

                }



            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // Log.e(TAG, "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", user_id);
                params.put("attendance_date",date);
                return params;
            }

        };


        MyApplication.getInstance().addToRequestQueue(strReq,tag_string_req);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }
    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }


}
