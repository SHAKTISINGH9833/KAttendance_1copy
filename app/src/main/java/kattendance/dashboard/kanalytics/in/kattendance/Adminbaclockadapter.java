package kattendance.dashboard.kanalytics.in.kattendance;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import java.util.regex.Pattern;

public class Adminbaclockadapter extends RecyclerView.Adapter<Adminbaclockadapter.ViewHolder> {
  private Activity activity;
  LayoutInflater inflater;
  public String value;
  private Context context;

  SharedPreferences pref,shared;
  String userid,baclockid,data,admin;
  Dialog dialog,dialog1;

  //    List<questionanssetmodel> setlis =new ArrayList<>();
  ArrayList<String> listOfSelectedCheckBoxId = new ArrayList();
  List<Backlockuserlistadminmodel> questionlist;
  private static final String ACCEPTED_Att="https://dashboard.kanalytics.in/mobile_app/attendance/backlog_action.php";
  private static final String DENIED_Att="https://dashboard.kanalytics.in/mobile_app/attendance/backlog_action.php";
  private static final String NOTIFICATION_FROM_USER="https://dashboard.kanalytics.in/mobile_app/attendance/backlog_action.php";

  public Adminbaclockadapter(Context ctx, List<Backlockuserlistadminmodel> questionlist) {
    this.inflater = LayoutInflater.from(ctx);
    this.questionlist = questionlist;

  }

  public Adminbaclockadapter() {

  }

  @NonNull

  @Override
  public Adminbaclockadapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = inflater.inflate(R.layout.backlockuserlist_admin, parent, false);
    return new Adminbaclockadapter.ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull Adminbaclockadapter.ViewHolder holder, int position) {

     data = questionlist.get(position).getDate();

    holder.date.setText(data);
    holder.in.setText(questionlist.get(position).getIn());
    holder.out.setText(questionlist.get(position).getOut());
    holder.name.setText(questionlist.get(position).getName());
    holder.reason.setText(questionlist.get(position).getReason());


    holder.acceptbt.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
String secteddate=questionlist.get(position).getDate();
        baclockid = questionlist.get(position).getBacklogid();
        userid = questionlist.get(position).getUserid();
        admin =questionlist.get(position).getAuthority_id();
        accepted_leave(secteddate);
        Toast.makeText(v.getContext(),"Accepted", Toast.LENGTH_LONG).show();
        questionlist.remove(position);
        notifyDataSetChanged();

      }
    });
    holder.denybt.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        baclockid = questionlist.get(position).getBacklogid();
        userid = questionlist.get(position).getUserid();
        admin =questionlist.get(position).getAuthority_id();
        String secteddate=questionlist.get(position).getDate();
        denied_leave(secteddate);
        Toast.makeText(v.getContext(),"Deny", Toast.LENGTH_LONG).show();
        questionlist.remove(position);
        notifyDataSetChanged();

      }
    });










  }

  @Override
  public int getItemCount() {
    return questionlist.size();
  }

  public class ViewHolder extends RecyclerView.ViewHolder {
    public TextView date,in,out,name,reason;
    public Button acceptbt,denybt;
    public ImageButton update;
    private AttendanceAdapter.ItemClickListener clickListener;


    TextView questionsetgradio;

    public ViewHolder(@NonNull View itemView) {
      super(itemView);

      date=(TextView)  itemView.findViewById(R.id.dateid);
      in=(TextView)  itemView.findViewById(R.id.inid);
      out=(TextView)  itemView.findViewById(R.id.outid);
      name=(TextView)  itemView.findViewById(R.id.nameid);
      reason=(TextView)  itemView.findViewById(R.id.reseonid);
      acceptbt=(Button)itemView.findViewById(R.id.acceptid);
      denybt=(Button)itemView.findViewById(R.id.deainyid);


    }
  }
  private void accepted_leave(final String selecteddate) {

    StringRequest strReq = new StringRequest(Request.Method.POST,
      ACCEPTED_Att, new Response.Listener<String>() {
      @Override
      public void onResponse(String s) {

        try {
          JSONObject jObj = new JSONObject(s);
          Log.i("aceept_admin", String.valueOf(jObj));
          acceptnotificationToUser(selecteddate);
        } catch (JSONException e) {
          e.printStackTrace();
        }

      }
    }, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError volleyError) {
        Toast.makeText(activity,volleyError.getMessage(), Toast.LENGTH_LONG).show();

      }
    })
    {
      @Override
      protected Map<String, String> getParams() {
        // Posting params to register url

        Map<String, String> params = new HashMap<String, String>();
        params.put("user",userid);
        params.put("action", "accept");
        params.put("admin", admin);
        params.put("date",selecteddate);
        params.put("backlogid",baclockid);
        Log.i("adminacceptparata", String.valueOf(params));

        return params;
      }
    };
    MyApplication.getInstance().addToRequestQueue(strReq);
  }

  private void denied_leave(final String secteddate) {
    StringRequest strReq = new StringRequest(Request.Method.POST,
      DENIED_Att, new Response.Listener<String>() {
      @Override
      public void onResponse(String s) {
        try {
          JSONObject jObj = new JSONObject(s);
          Log.i("deny_admin", String.valueOf(jObj));
          denynotificationToUser(secteddate);
        } catch (JSONException e) {
          e.printStackTrace();
        }

      }
    }, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError volleyError) {
        Toast.makeText(activity,volleyError.getMessage(), Toast.LENGTH_LONG).show();

      }
    })
    {
      @Override
      protected Map<String, String> getParams() {
        // Posting params to register url

        Map<String, String> params = new HashMap<String, String>();
        params.put("user",userid);
        params.put("action", "deny");
        params.put("admin", admin);
        params.put("date",secteddate);
        params.put("backlogid",baclockid);
        return params;
      }
    };
    MyApplication.getInstance().addToRequestQueue(strReq);
  }
  private void acceptnotificationToUser(final String selecteddate) {
    StringRequest strReq = new StringRequest(Request.Method.POST,
      NOTIFICATION_FROM_USER, new Response.Listener<String>() {
      @Override
      public void onResponse(String s) {
        try {
          JSONObject jObj = new JSONObject(s);
          Log.i("acceptnotication_admin", String.valueOf(jObj));

        } catch (JSONException e) {
          e.printStackTrace();
        }
      }
    }, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError volleyError) {
        Toast.makeText(activity,volleyError.getMessage(), Toast.LENGTH_LONG).show();

      }
    })
    {
      @Override
      protected Map<String, String> getParams() {
        // Posting params to register url

        Map<String, String> params = new HashMap<String, String>();
        params.put("user",userid);
        params.put("action", "notify");
        params.put("date",selecteddate);
        params.put("to","user");
        Log.i("aceeptnotication", String.valueOf(params));
        return params;
      }
    };
    MyApplication.getInstance().addToRequestQueue(strReq);
  }
  private void denynotificationToUser(final String secteddate) {
    StringRequest strReq = new StringRequest(Request.Method.POST,
      NOTIFICATION_FROM_USER, new Response.Listener<String>() {
      @Override
      public void onResponse(String s) {
        try {
          JSONObject jObj = new JSONObject(s);
          Log.i("denynotication_admin", String.valueOf(jObj));

        } catch (JSONException e) {
          e.printStackTrace();
        }
      }
    }, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError volleyError) {
        Toast.makeText(activity,volleyError.getMessage(), Toast.LENGTH_LONG).show();

      }
    })
    {
      @Override
      protected Map<String, String> getParams() {
        // Posting params to register url

        Map<String, String> params = new HashMap<String, String>();
        params.put("user",userid);
        params.put("action", "notify");
        params.put("date",secteddate);
        params.put("to","user");
        Log.i("denynotication", String.valueOf(params));
        return params;
      }
    };
    MyApplication.getInstance().addToRequestQueue(strReq);
  }
}
