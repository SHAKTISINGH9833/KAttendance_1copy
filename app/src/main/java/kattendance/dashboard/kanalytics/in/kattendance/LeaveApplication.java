package kattendance.dashboard.kanalytics.in.kattendance;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import com.google.android.material.textfield.TextInputLayout;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

//import static com.google.android.gms.analytics.internal.zzy.e;


public class LeaveApplication extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
  ActionBar ab;
  EditText DateFrom,DateTo,rejoiningDate,reason_text,contact_On_Leave,location_On_Leave;
  TextInputLayout tilDate_From,tilDate_To,tilDate_Rejoin,tilReason_text,tilLocation_Leave,tilContact_Leave;
  Button submitbutton;
  TextView displayleave,totalleave;
  SharedPreferences pref,shared;
  String user_id,user_name,from_date,to_date,rejoin_date,reason,type_of_leave,contact_leave,location_leave,authority_id,tl_status,pendingleave,updatedleavestring;
  Spinner spinner;
  ScrollView sv;
  private ProgressDialog pDialog;
  private static String GET_AUTHORITY_ID="https://dashboard.kanalytics.in/mobile_app/attendance/get_authority_id.php";
  private static String SUBMIT_LEAVE_APPLICATION="https://dashboard.kanalytics.in/mobile_app/attendance/submit_leave_application.php";
  private static String NOTIFICATION_TO_ADMIN="https://dashboard.kanalytics.in/mobile_app/attendance/noti_from_user.php";//for admin notify
  private static String NOTIFICATION_TO_TEAM_LEAD="https://dashboard.kanalytics.in/mobile_app/attendance/noti_to_team_lead.php";//admin notify

  @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_leave_new_application);
    ab = getSupportActionBar();
    ab.setDisplayHomeAsUpEnabled(true);
    sv=(ScrollView)findViewById(R.id.scrollView);
    shared = getSharedPreferences("info", MODE_PRIVATE);
    user_id = shared.getString("user_id", "");
    user_name=shared.getString("user_name","");
    authority_id=shared.getString("team_lead","");
    pendingleave=shared.getString("leaves","");
    updatedleavestring=shared.getString("updatedleave","");

    Log.i("updatedleave", updatedleavestring);
    Log.i("updatedleavebefor", pendingleave);

    pDialog= new ProgressDialog(this);
    tilDate_From=(TextInputLayout) findViewById(R.id.tilDateFrom);
    tilDate_To=(TextInputLayout)findViewById(R.id.tilDateTo);
    tilDate_Rejoin=(TextInputLayout)findViewById(R.id.tilDateRejoin);
    tilReason_text=(TextInputLayout)findViewById(R.id.tilreasonText);
    tilLocation_Leave=(TextInputLayout)findViewById(R.id.tillocation);
    tilContact_Leave=(TextInputLayout)findViewById(R.id.tilcontact);
    DateFrom=(EditText) findViewById(R.id.inputTypeDatefrom);
    displayleave=(TextView) findViewById(R.id.leaveid);
    totalleave=(TextView) findViewById(R.id.leavetextnameid);
    if(pendingleave.equals(updatedleavestring)) {
      totalleave.setText("Total Leave:-");
      displayleave.setText(pendingleave);

    }if(pendingleave.equals("")){
      totalleave.setText("Total Leave:-");
      displayleave.setText(updatedleavestring);
    }
    if(updatedleavestring.equals("")){
      totalleave.setText("Total Leave:-");
      displayleave.setText(pendingleave);
    }
    else{
      totalleave.setText("Pending Leave:-");
      displayleave.setText(updatedleavestring);
    }


    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      DateFrom.setShowSoftInputOnFocus(false);
    }
    DateTo=(EditText) findViewById(R.id.inputTypeDateto);
    rejoiningDate=(EditText) findViewById(R.id.rejoinDate);
    reason_text=(EditText)findViewById(R.id.reasonText);
    contact_On_Leave=(EditText)findViewById(R.id.contactOnLeave);
    location_On_Leave=(EditText)findViewById(R.id.locationOnLeave);
    submitbutton=(Button) findViewById(R.id.submitleave);
    spinner=(Spinner)findViewById(R.id.spinner);
    spinner.setOnItemSelectedListener(this);

    final SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String currentDateandTime = date_format.format(new Date());

    final Calendar myCalendar = Calendar.getInstance();
    final Calendar myCalendar1 = Calendar.getInstance();

    //From Date
    final DatePickerDialog.OnDateSetListener fromdate = new DatePickerDialog.OnDateSetListener() {
      @Override
      public void onDateSet(DatePicker view, int year, int monthOfYear,
                            int dayOfMonth) {
        myCalendar.set(Calendar.YEAR, year);
        myCalendar.set(Calendar.MONTH, monthOfYear);
        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        myCalendar1.set(Calendar.YEAR, year);
        myCalendar1.set(Calendar.MONTH, monthOfYear);
        myCalendar1.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        String myFormat = "dd-MM-yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
        DateFrom.setText(sdf.format(myCalendar.getTime()));
        from_date=sdf.format(myCalendar.getTime());
        //validateFromDate();
      }
    };


    DateFrom.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        DatePickerDialog dp = new DatePickerDialog(LeaveApplication.this,fromdate, myCalendar
          .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
          myCalendar.get(Calendar.DAY_OF_MONTH));
        dp.getDatePicker().setMinDate(System.currentTimeMillis());
        dp.show();

      }
    });

    //To Date
    final DatePickerDialog.OnDateSetListener todate = new DatePickerDialog.OnDateSetListener() {
      @Override
      public void onDateSet(DatePicker view, int year, int monthOfYear,
                            int dayOfMonth) {
        myCalendar.set(Calendar.YEAR, year);
        myCalendar.set(Calendar.MONTH, monthOfYear);
        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String myFormat = "dd-MM-yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
        DateTo.setText(sdf.format(myCalendar.getTime()));
        to_date=sdf.format(myCalendar.getTime());
        validateToDate();
      }
    };
    DateTo.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        DatePickerDialog dp = new DatePickerDialog(LeaveApplication.this,todate, myCalendar
          .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
          myCalendar.get(Calendar.DAY_OF_MONTH));
        if (!TextUtils.isEmpty(from_date)) {
          dp.getDatePicker().setMinDate(myCalendar1.getTimeInMillis());
        }
        dp.show();
      }
    });

    //Rejoin date
    final DatePickerDialog.OnDateSetListener rejoin = new DatePickerDialog.OnDateSetListener() {
      @Override
      public void onDateSet(DatePicker view, int year, int monthOfYear,
                            int dayOfMonth) {
        myCalendar.set(Calendar.YEAR, year);
        myCalendar.set(Calendar.MONTH, monthOfYear);
        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String myFormat = "dd-MM-yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
        rejoiningDate.setText(sdf.format(myCalendar.getTime()));
        rejoin_date=sdf.format(myCalendar.getTime());
        validateRejoinDate();
      }
    };
    rejoiningDate.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        DatePickerDialog dp = new DatePickerDialog(LeaveApplication.this,rejoin, myCalendar
          .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
          myCalendar.get(Calendar.DAY_OF_MONTH));
        dp.show();
      }
    });


    // =========================== On Text Change Listener ==================================================== //

    DateFrom.addTextChangedListener(new TextWatcher() {

      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {}
      @Override
      public void afterTextChanged(Editable s) {
        tilDate_From.setError(null);
        tilDate_From.setErrorEnabled(false);   }});

    DateTo.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {}
      @Override
      public void afterTextChanged(Editable s) {
        tilDate_To.setError(null);
        tilDate_To.setErrorEnabled(false);      }});

    rejoiningDate.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {}
      @Override
      public void afterTextChanged(Editable s) {
        tilDate_Rejoin.setError(null);
        tilDate_Rejoin.setErrorEnabled(false);  }});

    reason_text.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {}
      @Override
      public void afterTextChanged(Editable s) {
        tilReason_text.setError(null);
        tilReason_text.setErrorEnabled(false);
              }});

    location_On_Leave.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {}
      @Override
      public void afterTextChanged(Editable s) {
        tilLocation_Leave.setError(null);
        tilLocation_Leave.setErrorEnabled(false);}});

    contact_On_Leave.addTextChangedListener(new TextWatcher() {

      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {}
      @Override
      public void afterTextChanged(Editable s) {
        contact_leave = contact_On_Leave.getText().toString();
        tilContact_Leave.setError(null);
        tilContact_Leave.setErrorEnabled(false);
        String regexStr = "^[0-9]*$";
        //String uber=contact_On_Leave.toString();
        if(contact_On_Leave.length()>10 || contact_On_Leave.length()<10)
        {
          tilContact_Leave.setError("Please enter a valid 10 digit number!");
        }
        else if(!contact_leave.trim().matches(regexStr))
        {
          tilContact_Leave.setError("Please enter a valid 10 digit number!");
        }
      }});

    //==================================================  Submit Button  ===============================================//
        submitbutton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        reason = reason_text.getText().toString();
        contact_leave = contact_On_Leave.getText().toString();
        location_leave = location_On_Leave.getText().toString();


        if(type_of_leave.trim().equals("Casual Leave") || type_of_leave.trim().equals("Privilege Leave") )
        {
        if (TextUtils.isEmpty(from_date)) {
          tilDate_From.setError("This field cannot be empty");
          return;
        } else if (!TextUtils.isEmpty(from_date)) {
          try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy"); // here set the pattern as you date in string was containing like date/month/year
            Date d = sdf.parse(from_date);
            Calendar cal = Calendar.getInstance();
            cal.setTime(d);
       //     if (isWithinDaysFuture(cal, 2)) {
              if (TextUtils.isEmpty(to_date)) {
                tilDate_To.setError("This field cannot be empty");
                return;
              } else if (!TextUtils.isEmpty(to_date)) {
                try {
                  SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy"); // here set the pattern as you date in string was containing like date/month/year
                  Date d1 = sdf1.parse(to_date);
                  Calendar cal1 = Calendar.getInstance();
                  cal1.setTime(d1);
                  if (cal1.before(cal)) {
                    sv.smoothScrollTo(0,0);
                    hideSoftKeyboard(LeaveApplication.this);
                    tilDate_To.setError("To-Date cannot be less than From-Date");
                  } else {
                    String regexStr = "^[0-9]*$";
                    if (TextUtils.isEmpty(reason)) {
                      sv.smoothScrollTo(0,0);
                      hideSoftKeyboard(LeaveApplication.this);
                      tilReason_text.setError("Please enter a reason for leave !");
                      return;
                    } else if (TextUtils.isEmpty(contact_leave)) {
                      sv.smoothScrollTo(0,0);
                      hideSoftKeyboard(LeaveApplication.this);
                      tilContact_Leave.setError("Please enter a valid contact number !");
                      return;
                    } else if (contact_leave.length() > 10 || contact_On_Leave.length() < 10) {
                        sv.smoothScrollTo(0,0);
                        hideSoftKeyboard(LeaveApplication.this);
                        tilContact_Leave.setError("Please enter a valid 10 digit number!");
                        return;
                    }
                      else if(!contact_leave.trim().matches(regexStr)){
                      sv.smoothScrollTo(0,0);
                      hideSoftKeyboard(LeaveApplication.this);
                      tilContact_Leave.setError("Please enter a valid 10 digit number!");
                    }
                      else if (TextUtils.isEmpty(location_leave)) {
                      sv.smoothScrollTo(0,0);
                      hideSoftKeyboard(LeaveApplication.this);
                      tilLocation_Leave.setError("Please enter a valid location of leave !");
                      return;
                    } else if (TextUtils.isEmpty(rejoin_date)) {
                      sv.smoothScrollTo(0,0);
                      hideSoftKeyboard(LeaveApplication.this);
                      tilDate_Rejoin.setError("This field cannot be empty");
                      return;
                    }
                    else if(!TextUtils.isEmpty(rejoin_date))
                    {
                      try{
                        SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MM-yyyy"); // here set the pattern as you date in string was containing like date/month/year
                        Date d2 = sdf2.parse(rejoin_date);
                        Calendar cal2 = Calendar.getInstance();
                        cal2.setTime(d2);
                        if(cal2.before(cal1) || cal2.equals(cal1))
                        {
                          sv.smoothScrollTo(0,0);
                          hideSoftKeyboard(LeaveApplication.this);
                          tilDate_Rejoin.setError("Rejoining-Date should be after To-Date");
                        }
                        else {
                          submitleaveapplication(user_id, from_date, to_date, rejoin_date, reason, type_of_leave, contact_leave, location_leave, user_name);
                          //Intent i = new Intent(LeaveApplication.this, ListOfApproval.class);
                          //startActivity(i);
                        }
                      }
                      catch (ParseException e)
                      {
                        e.printStackTrace();
                      }
                    }

                  }
                } catch (ParseException e) {
                  e.printStackTrace();
                }
              }
          //  }

           /* else {
              sv.smoothScrollTo(0,0);
              hideSoftKeyboard(LeaveApplication.this);
              tilDate_From.setError("You can only apply for leave after 2 days from current date");
            }*/
          } catch (ParseException e) {
            e.printStackTrace();
          }
        }
      }
      else
        {
          if (TextUtils.isEmpty(from_date)) {
            tilDate_From.setError("This field cannot be empty");
            return;
          } else if (!TextUtils.isEmpty(from_date)) {
            try {
              SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy"); // here set the pattern as you date in string was containing like date/month/year
              Date d = sdf.parse(from_date);
              Calendar cal = Calendar.getInstance();
              cal.setTime(d);
              if (isWithinDaysFuture(cal, -1)) {
                Toast.makeText(LeaveApplication.this, "Sick leave", Toast.LENGTH_SHORT).show();
                if (TextUtils.isEmpty(to_date)) {
                  sv.smoothScrollTo(0,0);
                  hideSoftKeyboard(LeaveApplication.this);
                  tilDate_To.setError("This field cannot be empty");
                  return;
                } else if (!TextUtils.isEmpty(to_date)) {
                  try {
                    SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy"); // here set the pattern as you date in string was containing like date/month/year
                    Date d1 = sdf1.parse(to_date);
                    Calendar cal1 = Calendar.getInstance();
                    cal1.setTime(d1);
                    if (cal1.before(cal)){
                      sv.smoothScrollTo(0,0);
                      hideSoftKeyboard(LeaveApplication.this);
                      tilDate_To.setError("To-Date cannot be less than From-Date");
                    } else {
                      if (TextUtils.isEmpty(reason)) {
                        sv.smoothScrollTo(0,0);
                        hideSoftKeyboard(LeaveApplication.this);
                        tilReason_text.setError("Please enter a reason for leave !");
                        return;
                      } else if (TextUtils.isEmpty(contact_leave)) {
                        sv.smoothScrollTo(0,0);
                        hideSoftKeyboard(LeaveApplication.this);
                        tilContact_Leave.setError("Please enter a valid contact number !");
                        return;
                      } else if (contact_leave.length() > 10 || contact_On_Leave.length() < 10) {
                        sv.smoothScrollTo(0,0);
                        hideSoftKeyboard(LeaveApplication.this);
                        tilContact_Leave.setError("Please enter a valid 10 digit number!");
                        return;
                      } else if (TextUtils.isEmpty(location_leave)) {
                        sv.smoothScrollTo(0,0);
                        hideSoftKeyboard(LeaveApplication.this);
                        tilLocation_Leave.setError("Please enter a valid location of leave !");
                        return;
                      } else if (TextUtils.isEmpty(rejoin_date)) {
                        sv.smoothScrollTo(0,0);
                        hideSoftKeyboard(LeaveApplication.this);
                        tilDate_Rejoin.setError("This field cannot be empty");
                        return;
                      }
                      else if(!TextUtils.isEmpty(rejoin_date))
                      {
                        try{
                          SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MM-yyyy"); // here set the pattern as you date in string was containing like date/month/year
                          Date d2 = sdf2.parse(rejoin_date);
                          Calendar cal2 = Calendar.getInstance();
                          cal2.setTime(d2);
                          if(cal2.before(cal1) || cal2.equals(cal1))
                          {
                            sv.smoothScrollTo(0,0);
                            hideSoftKeyboard(LeaveApplication.this);
                            tilDate_Rejoin.setError("Rejoining-Date should be after To-Date");
                          }
                          else {
                            submitleaveapplication(user_id, from_date, to_date, rejoin_date, reason, type_of_leave, contact_leave, location_leave, user_name);
                           // Intent i = new Intent(LeaveApplication.this, NavigationDrawerActivity.class);
                            //startActivity(i);
                          }
                        }
                        catch (ParseException e)
                        {
                          e.printStackTrace();
                        }


                     }
                    }
                  } catch (ParseException e) {
                    e.printStackTrace();
                  }
                }
              } else {
                sv.smoothScrollTo(0,0);
                hideSoftKeyboard(LeaveApplication.this);
                tilDate_From.setError("Sick Leave cannot be less than Current Date");
              }
            } catch (ParseException e) {
              e.printStackTrace();
            }
          }
        }
        //sendNotificationToAdmin(user_id,user_name,authority_id);
      }
    });

    //getAuthorityID(user_id);

  }

  /*private void getAuthorityID(final String user_id) {
    StringRequest strReq = new StringRequest(Request.Method.POST,GET_AUTHORITY_ID
      , new Response.Listener<String>() {
      @Override
      public void onResponse(String s) {
        try {

          JSONObject jObj = new JSONObject(s);
          JSONArray jsonMainNode = jObj.optJSONArray("results");

          JSONObject result = jsonMainNode.getJSONObject(0);
          String error = result.getString("error");
          if (error.trim().equals("true")) {

            String auth_id=result.getString("authority_id");
            authority_id=auth_id;

          }
          else
          {
            Toast.makeText(LeaveApplication.this, "Could not get authority id", Toast.LENGTH_SHORT).show();
          }

        } catch (JSONException e) {

          e.printStackTrace();

        }
        //Toast.makeText(LeaveApplication.this, "auth id : "+authority_id, Toast.LENGTH_SHORT).show();
      }
    }, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError volleyError) {
        Toast.makeText(getApplicationContext(),volleyError.getMessage(), Toast.LENGTH_LONG).show();

      }
    })
    {
      @Override
      protected Map<String, String> getParams() {
        // Posting params to register url

        Map<String, String> params = new HashMap<String, String>();
        params.put("user_id",user_id);
        return params;
      }
    };
    MyApplication.getInstance().addToRequestQueue(strReq);
  }*/

  private void validateRejoinDate() {
    try {
      SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy"); // here set the pattern as you date in string was containing like date/month/year
      Date d1 = sdf1.parse(to_date);
      Calendar cal1 = Calendar.getInstance();
      cal1.setTime(d1);
      SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MM-yyyy"); // here set the pattern as you date in string was containing like date/month/year
      Date d2 = sdf2.parse(rejoin_date);
      Calendar cal2 = Calendar.getInstance();
      cal2.setTime(d2);
      if (cal2.before(cal1) || cal2.equals(cal1)) {
        //sv.smoothScrollTo(0, 0);
        //hideSoftKeyboard(LeaveApplication.this);
        tilDate_Rejoin.setError("Rejoining-Date should be after To-Date");
      }
      else
      {

      }
    }catch(Exception e)
    {
      e.printStackTrace();
    }
  }

  private void validateToDate() {
    try {
      SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy"); // here set the pattern as you date in string was containing like date/month/year
      Date d = sdf.parse(from_date);
      Calendar cal = Calendar.getInstance();
      cal.setTime(d);
      SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy"); // here set the pattern as you date in string was containing like date/month/year
      Date d1 = sdf1.parse(to_date);
      Calendar cal1 = Calendar.getInstance();
      cal1.setTime(d1);
      if (cal1.before(cal)) {
        //sv.smoothScrollTo(0, 0);
        //hideSoftKeyboard(LeaveApplication.this);
        tilDate_To.setError("To-Date cannot be less than From-Date");
      }
      else
      {

      }
    }  catch (Exception e)
    {
      e.printStackTrace();
    }
  }

  private void validateFromDate() {
    try {
      SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy"); // here set the pattern as you date in string was containing like date/month/year
      Date d = sdf.parse(from_date);
      Calendar cal = Calendar.getInstance();
      cal.setTime(d);
      if (isWithinDaysFuture(cal, 2)) {

      }
      else
      {
        tilDate_From.setError("You can only apply for leave after 2 days from current date");
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }

  private void hideSoftKeyboard(Activity activity) {
    InputMethodManager inputMethodManager =
      (InputMethodManager) activity.getSystemService(
        Activity.INPUT_METHOD_SERVICE);
    inputMethodManager.hideSoftInputFromWindow(
      activity.getCurrentFocus().getWindowToken(), 0);
  }


  private void sendNotificationToAdmin(final String user_id,final String user_name,final String authority_id) {
    StringRequest strReq = new StringRequest(Request.Method.POST,
      NOTIFICATION_TO_ADMIN, new Response.Listener<String>() {
      @Override
      public void onResponse(String s) {


        new Handler().postDelayed(new Runnable(){
          @Override
          public void run() {
            //Intent startService=new Intent(MainActivity.this,FireMsgService.class);
            //  startService(startService);
            hideDialog();
            Intent mainIntent = new Intent(LeaveApplication.this,ListOfApproval.class);
            startActivity(mainIntent);
            finish();
          }
        }, 3000);

      }
    }, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError volleyError) {
        Toast.makeText(getApplicationContext(),volleyError.getMessage(), Toast.LENGTH_LONG).show();

      }
    })
    {
      @Override
      protected Map<String, String> getParams() {
        // Posting params to register url

        Map<String, String> params = new HashMap<String, String>();
        params.put("user_id",user_id);
        params.put("user_name",user_name);
        params.put("auth_id",authority_id);
        return params;
      }
    };
    MyApplication.getInstance().addToRequestQueue(strReq);

  }

  public static boolean isWithinDaysFuture(Calendar cal, int days) {
    if (cal == null) {
      throw new IllegalArgumentException("The date must not be null");
    }
    Calendar today = Calendar.getInstance();
    Calendar future = Calendar.getInstance();
    future.add(Calendar.DAY_OF_YEAR, days);
    return (isAfterDay(cal, future));
  }

  public static boolean isAfterDay(Calendar cal1, Calendar cal2) {
    if (cal1 == null || cal2 == null) {
      throw new IllegalArgumentException("The dates must not be null");
    }
    if (cal1.get(Calendar.ERA) < cal2.get(Calendar.ERA))
      return false;
    if (cal1.get(Calendar.ERA) > cal2.get(Calendar.ERA))
      return true;
    if (cal1.get(Calendar.YEAR) < cal2.get(Calendar.YEAR))
      return false;
    if (cal1.get(Calendar.YEAR) > cal2.get(Calendar.YEAR))
      return true;
    return cal1.get(Calendar.DAY_OF_YEAR) > cal2.get(Calendar.DAY_OF_YEAR);
  }


  private void submitleaveapplication(final String user_id,final String from_date,final String to_date,final String rejoin_date,final String reason,
                                      final String type_of_leave,final String contact_leave,final String location_leave,final String user_name) {
    showDialog();
    pDialog.setMessage("Please Wait ...");
    StringRequest strReq = new StringRequest(Request.Method.POST,
      SUBMIT_LEAVE_APPLICATION, new Response.Listener<String>() {
      @Override
      public void onResponse(String s) {

        sendNotificationToTeamLead(user_id, user_name, authority_id);
        //Toast.makeText(LeaveApplication.this, "Response"+s, Toast.LENGTH_SHORT).show();
        /*if(tl_status.trim().equals("1")) {
          sendNotificationToAdmin(user_id, user_name, authority_id);
        }
        else
        {

          //Toast.makeText(LeaveApplication.this, "HEllllllooooo"+authority_id, Toast.LENGTH_SHORT).show();
          sendNotificationToTeamLead(user_id, user_name, authority_id);
        }*/
      }
    }, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError volleyError) {

      }
    })
    {
      @Override
      protected Map<String, String> getParams() {

        Map<String, String> params = new HashMap<String, String>();
        params.put("user_id", user_id);
        params.put("from_date",from_date);
        params.put("to_date",to_date);
        params.put("rejoin_date",rejoin_date);
        params.put("reason",reason);
        params.put("type_of_leave",type_of_leave);
        params.put("contact_leave",contact_leave);
        params.put("location_leave",location_leave);
        params.put("user_name",user_name);
        return params;
      }
    };
    MyApplication.getInstance().addToRequestQueue(strReq);
  }

  private void sendNotificationToTeamLead(final String user_id,final String user_name,final String authority_id) {
    StringRequest strReq = new StringRequest(Request.Method.POST,
      NOTIFICATION_TO_TEAM_LEAD, new Response.Listener<String>() {
      @Override
      public void onResponse(String s) {

        //Toast.makeText(getApplicationContext(),"Team Leader Id "+authority_id,Toast.LENGTH_LONG).show();
        new Handler().postDelayed(new Runnable(){
          @Override
          public void run() {
            //Intent startService=new Intent(MainActivity.this,FireMsgService.class);
            //  startService(startService);
            hideDialog();
            Intent mainIntent = new Intent(LeaveApplication.this,ListOfApproval.class);
            startActivity(mainIntent);
            finish();
          }
        }, 3000);

      }
    }, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError volleyError) {
        Toast.makeText(getApplicationContext(),volleyError.getMessage(), Toast.LENGTH_LONG).show();

      }
    })
    {
      @Override
      protected Map<String, String> getParams() {
        // Posting params to register url

        Map<String, String> params = new HashMap<String, String>();
        params.put("user_id",user_id);
        params.put("user_name",user_name);
        params.put("auth_id",authority_id);
        return params;
      }
    };
    MyApplication.getInstance().addToRequestQueue(strReq);
  }


  private void showDialog() {
    if (!pDialog.isShowing())
      pDialog.show();
  }

  private void hideDialog() {
    if (pDialog.isShowing())
      pDialog.dismiss();
  }


  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    onBackPressed();
    return true;
  }


  @Override
  public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
    type_of_leave=parent.getItemAtPosition(position).toString();
  }

  @Override
  public void onNothingSelected(AdapterView<?> parent) {
  }


}
