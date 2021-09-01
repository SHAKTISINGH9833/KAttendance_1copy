package kattendance.dashboard.kanalytics.in.kattendance;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HolidayListFragment extends Fragment {

  Context context;
  @BindView(R.id.mumbai_rv)
  RecyclerView recyclerView;
  String type;
  HolidayListAdapter holidayListAdapter;
  GridLayoutManager layoutManager;
  private ProgressDialog pDialog;
  private String GETHolIDAYLIST = "https://dashboard.kanalytics.in/mobile_app/attendance/holiday_list.php";

  public HolidayListFragment() {

    }

  @Override
  public void onAttach(@NonNull Context context) {
    super.onAttach(context);
    this.context = context;
  }


  public static HolidayListFragment newInstance(String param1) {
        HolidayListFragment fragment = new HolidayListFragment();
        Bundle args = new Bundle();
        args.putString("type",param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      if(getArguments()!=null){
        type = getArguments().getString("type");
      }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
      View viewGroup = inflater.inflate(R.layout.fragment_mumbai_holiday_list, container, false);
      ButterKnife.bind(this,viewGroup);
        return viewGroup;
    }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    if(getArguments() != null){
      type = getArguments().getString("type");
    }
    pDialog = new ProgressDialog(getContext());
    pDialog.setCancelable(false);
    getHolidaysList();
  }

  private void getHolidaysList() {
    String tag_string_req = "req_register";

    pDialog.setMessage("Please Wait ...");
    showDialog();
    StringRequest strReq = new StringRequest(Request.Method.POST,
      GETHolIDAYLIST, new Response.Listener<String>() {
      @Override
      public void onResponse(String response) {

        hideDialog();
        try{
          JSONObject jObj = new JSONObject(response);
          JSONObject jsonMainNode = jObj.getJSONObject("results");
          JSONArray MumbaiArray = jsonMainNode.getJSONArray("Mumbai");
          JSONArray DelhiArray = jsonMainNode.getJSONArray("Delhi");
          ArrayList<MumbaiModel> mumbaiModelArrayList = new ArrayList<>();
          ArrayList<DelhiModel> delhiModelArrayList = new ArrayList<>();
          for(int i=0;i< MumbaiArray.length();i++){
            MumbaiModel mumbaiModel = new MumbaiModel();
            JSONObject jsonobject= (JSONObject) MumbaiArray.get(i);
            mumbaiModel.setName(jsonobject.getString("name"));
            mumbaiModel.setDate(jsonobject.getString("date"));
            mumbaiModelArrayList.add(mumbaiModel);
          }
          for(int i=0;i< DelhiArray.length();i++){
            DelhiModel delhiModel = new DelhiModel();
            JSONObject jsonobject= (JSONObject) DelhiArray.get(i);
            delhiModel.setName(jsonobject.getString("name"));
            delhiModel.setDate(jsonobject.getString("date"));
            delhiModelArrayList.add(delhiModel);
          }

          if(type.toLowerCase().trim().equals("Mumbai".toLowerCase())){
            delhiModelArrayList = new ArrayList<>();
            holidayListAdapter = new HolidayListAdapter(context,mumbaiModelArrayList,delhiModelArrayList);
          }else if(type.toLowerCase().trim().equals("Delhi".toLowerCase())){
            mumbaiModelArrayList = new ArrayList<>();
            holidayListAdapter = new HolidayListAdapter(context,mumbaiModelArrayList,delhiModelArrayList);
          }

          layoutManager = new GridLayoutManager(context,2);
          recyclerView.setLayoutManager(layoutManager);
          recyclerView.hasFixedSize();
          recyclerView.setNestedScrollingEnabled(false);
          recyclerView.setAdapter(holidayListAdapter);
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

}
