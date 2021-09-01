package kattendance.dashboard.kanalytics.in.kattendance;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class Adminbaclockadapter extends RecyclerView.Adapter<Adminbaclockadapter.ViewHolder> {
  private Activity activity;
  LayoutInflater inflater;
  public String value;
  SharedPreferences pref,shared;
  String userid,baclockid,data,admin;

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
      admin =questionlist.get(position).getAuthority_id();
    holder.date.setText(data);
    holder.in.setText(questionlist.get(position).getIn());
    holder.out.setText(questionlist.get(position).getOut());
    holder.name.setText(questionlist.get(position).getName());
    holder.reason.setText(questionlist.get(position).getReason());
     userid = questionlist.get(position).getUserid();
    baclockid = questionlist.get(position).getBacklogid();
    holder.acceptbt.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        accepted_leave();

      }
    });
    holder.denybt.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        denied_leave();

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
  private void accepted_leave() {
    StringRequest strReq = new StringRequest(Request.Method.POST,
      ACCEPTED_Att, new Response.Listener<String>() {
      @Override
      public void onResponse(String s) {
        acceptnotificationToUser();
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
        params.put("date",data);
        params.put("backlogid",baclockid);

        return params;
      }
    };
    MyApplication.getInstance().addToRequestQueue(strReq);
  }

  private void denied_leave() {
    StringRequest strReq = new StringRequest(Request.Method.POST,
      DENIED_Att, new Response.Listener<String>() {
      @Override
      public void onResponse(String s) {
        denynotificationToUser();
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
        params.put("date",data);
        params.put("backlogid",baclockid);
        return params;
      }
    };
    MyApplication.getInstance().addToRequestQueue(strReq);
  }
  private void acceptnotificationToUser() {
    StringRequest strReq = new StringRequest(Request.Method.POST,
      NOTIFICATION_FROM_USER, new Response.Listener<String>() {
      @Override
      public void onResponse(String s) {

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
        params.put("date",data);
        params.put("to","user");
        return params;
      }
    };
    MyApplication.getInstance().addToRequestQueue(strReq);
  }
  private void denynotificationToUser() {
    StringRequest strReq = new StringRequest(Request.Method.POST,
      NOTIFICATION_FROM_USER, new Response.Listener<String>() {
      @Override
      public void onResponse(String s) {

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
        params.put("date",data);
        params.put("to","user");
        return params;
      }
    };
    MyApplication.getInstance().addToRequestQueue(strReq);
  }
}
