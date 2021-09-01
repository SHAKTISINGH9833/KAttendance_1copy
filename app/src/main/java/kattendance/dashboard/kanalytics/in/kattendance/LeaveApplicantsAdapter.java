package kattendance.dashboard.kanalytics.in.kattendance;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.view.View.GONE;

public class LeaveApplicantsAdapter extends BaseAdapter {
  private Activity activity;
  private LayoutInflater inflater;
  private List<LeaveApplicantsItem> listofleaveapplicantsitem;
  String outputFromDate,outputToDate,outputRejoinDate;

  Context context;
  Dialog dialog,dialog1;

  private static final String ACCEPTED_LEAVE="https://dashboard.kanalytics.in/mobile_app/attendance/accepted_leave.php";
  private static final String DENIED_LEAVE="https://dashboard.kanalytics.in/mobile_app/attendance/leave_denied.php";
  private static final String NOTIFICATION_TO_USER="https://dashboard.kanalytics.in/mobile_app/attendance/noti_from_admin.php";
  private static final String NOTIFICATION_FROM_TEAM_LEAD="https://dashboard.kanalytics.in/mobile_app/attendance/noti_from_team_lead.php";

  public LeaveApplicantsAdapter(Activity activity,List<LeaveApplicantsItem> listofleaveapplicantsitem)
  {
    //this.context=context;
    this.activity=activity;
    this.listofleaveapplicantsitem=listofleaveapplicantsitem;
  }


  @Override
  public int getCount() {
    return listofleaveapplicantsitem.size();
  }

  @Override
  public Object getItem(int position) {
    return listofleaveapplicantsitem.get(position);
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  @Override
  public View getView(final int position, View convertView, ViewGroup parent) {
    if (inflater == null)
      inflater = (LayoutInflater) activity
        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    if (convertView == null)
      convertView = inflater.inflate(R.layout.activity_leave_applicants_item, null);

    TextView username = (TextView) convertView.findViewById(R.id.txtUsername);
    final TextView reason = (TextView) convertView.findViewById(R.id.txtDescription);
    TextView from = (TextView) convertView.findViewById(R.id.from);
    TextView to = (TextView) convertView.findViewById(R.id.to);
    TextView rejoin = (TextView) convertView.findViewById(R.id.rejoin);
    final TextView typeofleave = (TextView) convertView.findViewById(R.id.typeofleave);
    final Button accept = (Button) convertView.findViewById(R.id.btnAccept);

    final Button deny = (Button) convertView.findViewById(R.id.btnDeny);
    final TextView txtstatus=(TextView) convertView.findViewById(R.id.status);

    final LeaveApplicantsItem m = listofleaveapplicantsitem.get(position);

    username.setText(m.getUsername());
    reason.setText("REASON : "+m.getDescription());
    typeofleave.setText(m.getTitle());

    final String auth_id=m.getAuth_id();
    final String user_id=m.getUser_id();
    final String typeofleavevalue = m.getTitle();
    final String reasondesc=m.getDescription();
    String status=m.getStatus();

    if(status.trim().equals("0"))
    {
      accept.setVisibility(View.VISIBLE);
      deny.setVisibility(View.VISIBLE);
      txtstatus.setVisibility(GONE);
    }
    else if(status.trim().equals("1"))
    {
      deny.setVisibility(View.GONE);
      accept.setVisibility(View.GONE);
      txtstatus.setVisibility(View.VISIBLE);
      txtstatus.setText("LEAVE REQUEST ACCEPTED");
      txtstatus.setTextColor(Color.parseColor("#10EB4C"));
    }
    else if(status.trim().equals("2"))
    {
      accept.setVisibility(View.GONE);
      deny.setVisibility(View.GONE);
      txtstatus.setVisibility(View.VISIBLE);
      txtstatus.setText("LEAVE REQUEST DENIED");
      txtstatus.setTextColor(Color.parseColor("#F9190B"));
    }

    final String fromdate=m.getDate();
    final String todate=m.getTodate();
    final String rejoindate=m.getRejoindate();
    SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat output = new SimpleDateFormat("dd-MMM-yyyy");
    try {
      Date parsed = input.parse(fromdate);
      Date parsed1=input.parse(todate);
      Date parsed2=input.parse(rejoindate);
      outputFromDate = output.format(parsed);
      outputToDate=output.format(parsed1);
      outputRejoinDate=output.format(parsed2);
    } catch (ParseException e) {
      e.printStackTrace();
    }

    from.setText("FROM DATE : "+outputFromDate);
    to.setText("TO DATE : "+outputToDate);
    rejoin.setText("REJOINING DATE : "+outputRejoinDate);

    accept.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage("Are you sure you want to accept this leave application?")
          .setCancelable(false)
          .setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
              String flag="1";
              accepted_leave(user_id,fromdate,todate,rejoindate);
              deny.setVisibility(View.GONE);
              accept.setVisibility(View.GONE);
              txtstatus.setVisibility(View.VISIBLE);
              txtstatus.setText("LEAVE REQUEST ACCEPTED");
              txtstatus.setTextColor(Color.parseColor("#10EB4C"));
              acceptnotificationToUser(auth_id,user_id,flag);
              listofleaveapplicantsitem.remove(position);
              notifyDataSetChanged();
            }
          })
          .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
              AlertDialog alert1=builder.create();
              alert1.dismiss();
            }
          });

        AlertDialog alert = builder.create();
        alert.show();
        //showConfirmAlertToUser();

      }

      private void accepted_leave(final String user_id,final String fromdate,final String todate,final String rejoindate) {
        StringRequest strReq = new StringRequest(Request.Method.POST,
          ACCEPTED_LEAVE, new Response.Listener<String>() {
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
            params.put("user_id", user_id);
            params.put("from_date_multiple", fromdate);
            params.put("to_date_multiple", todate);
            params.put("rejoin_date",rejoindate);
            return params;
          }
        };
        MyApplication.getInstance().addToRequestQueue(strReq);
      }

      private void acceptnotificationToUser(final String auth_id,final String user_id,final String flag) {
        StringRequest strReq = new StringRequest(Request.Method.POST,
          NOTIFICATION_FROM_TEAM_LEAD, new Response.Listener<String>() {
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
            params.put("auth_id", auth_id);
            params.put("user_id",user_id);
            params.put("flag",flag);
            return params;
          }
        };
        MyApplication.getInstance().addToRequestQueue(strReq);
      }


    });

    deny.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage("Are you sure you want to deny this leave application?")
          .setCancelable(false)
          .setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

              String flag ="2";
              denied_leave(user_id,fromdate,todate,rejoindate);
              accept.setVisibility(View.GONE);
              deny.setVisibility(View.GONE);
              txtstatus.setVisibility(View.VISIBLE);
              txtstatus.setText("LEAVE REQUEST DENIED");
              txtstatus.setTextColor(Color.parseColor("#F9190B"));
              denynotificationToUser(auth_id,user_id,flag);
              listofleaveapplicantsitem.remove(position);
              notifyDataSetChanged();
            }
          })
          .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
              AlertDialog alert1=builder.create();
              alert1.dismiss();
            }
          });

        AlertDialog alert = builder.create();
        alert.show();
      }


      private void denied_leave(final String user_id,final String fromdate,final String todate,final String rejoindate) {
        StringRequest strReq = new StringRequest(Request.Method.POST,
          DENIED_LEAVE, new Response.Listener<String>() {
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
            params.put("user_id", user_id);
            params.put("from_date_multiple", fromdate);
            params.put("to_date_multiple", todate);
            params.put("rejoin_date",rejoindate);
            return params;
          }
        };
        MyApplication.getInstance().addToRequestQueue(strReq);
      }

      private void denynotificationToUser(final String auth_id,final String user_id,final String flag) {
        StringRequest strReq = new StringRequest(Request.Method.POST,
          NOTIFICATION_FROM_TEAM_LEAD, new Response.Listener<String>() {
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
            params.put("auth_id", auth_id);
            params.put("user_id",user_id);
            params.put("flag",flag);
            return params;
          }
        };
        MyApplication.getInstance().addToRequestQueue(strReq);
      }

    });
    return convertView;
  }

}
