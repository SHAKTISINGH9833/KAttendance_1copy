package kattendance.dashboard.kanalytics.in.kattendance;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Userbaclockadapter extends RecyclerView.Adapter<Userbaclockadapter.ViewHolder> {
  private Activity activity;
  LayoutInflater inflater;
  public String value;
  private Context context;

  SharedPreferences pref, shared;
  String userid, baclockid, data, admin,sttsua;
  Dialog dialog, dialog1;

  //    List<questionanssetmodel> setlis =new ArrayList<>();
  ArrayList<String> listOfSelectedCheckBoxId = new ArrayList();
  List<UserBaclockStatusmodel> questionlist;


  public Userbaclockadapter(Context ctx, List<UserBaclockStatusmodel> questionlist) {
    this.inflater = LayoutInflater.from(ctx);
    this.questionlist = questionlist;

  }

  public Userbaclockadapter() {

  }

  @NonNull

  @Override
  public Userbaclockadapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = inflater.inflate(R.layout.useradminlist, parent, false);
    return new Userbaclockadapter.ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull Userbaclockadapter.ViewHolder holder, int position) {

    data = questionlist.get(position).getDate();

    holder.date.setText(data);
    holder.in.setText(questionlist.get(position).getIn());
    holder.out.setText(questionlist.get(position).getOut());
    holder.name.setText(questionlist.get(position).getName());
    holder.reason.setText(questionlist.get(position).getReason());

    String Status = questionlist.get(position).getStatus();
    if(Status.equals("2"))
    {
      holder.status.setText("Denied");
      holder.status.setTextColor(Color.parseColor("#F9190B"));
    }
    else if(Status.equals("0")) {
      holder.status.setText("Pending");
      holder.status.setTextColor(Color.parseColor("#ffa500"));
    }
    else {
      holder.status.setText("Approved");
      holder.status.setTextColor(Color.parseColor("#10EB4C"));
    }







  }

  @Override
  public int getItemCount() {
    return questionlist.size();
  }

  public class ViewHolder extends RecyclerView.ViewHolder {
    public TextView date, in, out, name, reason,status;

    public ImageButton update;
    private AttendanceAdapter.ItemClickListener clickListener;


    TextView questionsetgradio;

    public ViewHolder(@NonNull View itemView) {
      super(itemView);

      date = (TextView) itemView.findViewById(R.id.dateid);
      in = (TextView) itemView.findViewById(R.id.inid);
      out = (TextView) itemView.findViewById(R.id.outid);
      name = (TextView) itemView.findViewById(R.id.nameid);
      reason = (TextView) itemView.findViewById(R.id.reseonid);
      status = (TextView) itemView.findViewById(R.id.Status);



    }
  }


}
