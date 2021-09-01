package kattendance.dashboard.kanalytics.in.kattendance;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import static android.content.Context.MODE_PRIVATE;


public class ReportinPeopleAttendanceList extends BottomSheetDialogFragment {

  @BindView(R.id.inputTypeDateRepotingPeople)
  EditText inputDate;
  RecyclerView.Adapter mAdapter;
  @BindView(R.id.cancelBtns)
  ImageView backbtn;
  ArrayList<AttendanceItem> productionItems= new ArrayList<AttendanceItem>();
  @BindView(R.id.rv_recycler_viewRepotingPeople)
  RecyclerView mRecyclerView;
  @BindView(R.id.name)
  TextView name;
  private static String user_id;
  private ProgressDialog pDialog;
  RecyclerView.LayoutManager mLayoutManager;
  ActionBar ab;
  private static String GETATTENDANCE_LIST="https://dashboard.kanalytics.in/mobile_app/attendance/attendance_sheet.php";
  java.util.Calendar myCalendar;
  DatePickerDialog.OnDateSetListener date;
  Context context;
  private String people_name;
  private BottomSheetDialog bottomSheetDialog;

  public ReportinPeopleAttendanceList() {

  }

  @Override
  public void onAttach(@NonNull Context context) {
    super.onAttach(context);
    this.context = context;
  }

  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    Dialog dialog = super.onCreateDialog(savedInstanceState);
    dialog.setOnShowListener(new DialogInterface.OnShowListener() {
      @Override public void onShow(DialogInterface dialogInterface) {
        bottomSheetDialog = (BottomSheetDialog) dialogInterface;
        setupFullHeight(bottomSheetDialog);
      }
    });
    return dialog;
  }

  @OnClick(R.id.cancelBtns)
  public void close(){
    bottomSheetDialog.cancel();
  }

  private void setupFullHeight(BottomSheetDialog bottomSheetDialog) {
    FrameLayout bottomSheet = (FrameLayout) bottomSheetDialog.findViewById(R.id.design_bottom_sheet);
    BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
    ViewGroup.LayoutParams layoutParams = bottomSheet.getLayoutParams();
    int windowHeight = getWindowHeight();
    if (layoutParams != null) {
      layoutParams.height = windowHeight;
    }
    bottomSheet.setLayoutParams(layoutParams);
    behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
  }

  private int getWindowHeight() {
    DisplayMetrics displayMetrics = new DisplayMetrics();
    ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
    return displayMetrics.heightPixels;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      user_id = getArguments().getString("userId");
      people_name = getArguments().getString("userName");
    }
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_reportin_people_attendance_list, container, false);
    ButterKnife.bind(this, rootView);
    return rootView;
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    oninit();
  }

  private void oninit() {

    ab = ((AppCompatActivity)getActivity()).getSupportActionBar();
    ab.setDisplayHomeAsUpEnabled(true);
    pDialog = new ProgressDialog(getContext());
    pDialog.setCancelable(false);
    name.setText(people_name);
    SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String currentDateandTime = date_format.format(new Date());
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
        productionItems.clear();
        getFirstJsonValue(sdf.format(myCalendar.getTime()));
      }
    };
    getFirstJsonValue(currentDateandTime);
  }

  private void getFirstJsonValue(final String currentDateandTime) {
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

          mAdapter = new AttendanceAdapter(context, productionItems);
          LinearLayoutManager manager = new LinearLayoutManager(getContext());
          mRecyclerView.setLayoutManager(manager);
          mRecyclerView.setHasFixedSize(true);
          mRecyclerView.setAdapter(mAdapter);
          mAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
          e.printStackTrace();
        }
      }
    }, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {
        // Log.e(TAG, "Registration Error: " + error.getMessage());
        Toast.makeText(getContext(),
          error.getMessage(), Toast.LENGTH_LONG).show();
        hideDialog();
      }
    }) {
      @Override
      protected Map<String, String> getParams() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("user_id", user_id);
        params.put("attendance_date",currentDateandTime);
        return params;
      }
    };
    MyApplication.getInstance().addToRequestQueue(strReq,tag_string_req);
  }

  private void hideDialog() {
    if (pDialog.isShowing())
      pDialog.dismiss();
  }

  private void showDialog() {
    if (!pDialog.isShowing())
      pDialog.show();
  }

  @OnClick(R.id.inputTypeDateRepotingPeople)
  public void selectDate(){
    DatePickerDialog dp = new DatePickerDialog(getContext(), date, myCalendar
      .get(java.util.Calendar.YEAR), myCalendar.get(java.util.Calendar.MONTH),
      myCalendar.get(java.util.Calendar.DAY_OF_MONTH));
    dp.show();
  }
}
