package kattendance.dashboard.kanalytics.in.kattendance;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Backlogstatusadapter extends RecyclerView.Adapter<Backlogstatusadapter.ViewHolder> {
  LayoutInflater inflater;
  public String value;
  //    List<questionanssetmodel> setlis =new ArrayList<>();
  ArrayList<String> listOfSelectedCheckBoxId = new ArrayList();
  List<Backlogmodelstatus> statuslistt;


  public Backlogstatusadapter(Context ctx, List<Backlogmodelstatus> statuslistt) {
    this.inflater = LayoutInflater.from(ctx);
    this.statuslistt = statuslistt;
  }




  @NonNull

  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = inflater.inflate(R.layout.backlock_status, parent, false);
    return new Backlogstatusadapter.ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    final Backlogmodelstatus backlogmodelstatus = statuslistt.get(position);
    String data = statuslistt.get(position).getAttendance_date();
    holder.date.setText(data);
    holder.name.setText(statuslistt.get(position).getUsername());
    holder.in.setText(statuslistt.get(position).getLogin());
    holder.out.setText(statuslistt.get(position).getLogout());
    holder.reason.setText(statuslistt.get(position).getReason());

    String Status = statuslistt.get(position).getStatus();
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

    holder.itemView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

      }
    });







  }

  @Override
  public int getItemCount() {
    return statuslistt.size();
  }

  public class ViewHolder extends RecyclerView.ViewHolder {
    public TextView date,in,out,name,reason,status;
    private AttendanceAdapter.ItemClickListener clickListener;


    TextView questionsetgradio;

    public ViewHolder(@NonNull View itemView) {
      super(itemView);
      name=(TextView)  itemView.findViewById(R.id.nameid);
      date=(TextView)  itemView.findViewById(R.id.dateid);
      in=(TextView)  itemView.findViewById(R.id.inid);
      out=(TextView)  itemView.findViewById(R.id.outid);
      reason=(TextView)  itemView.findViewById(R.id.reseonid);
      status=(TextView)  itemView.findViewById(R.id.Statusid);


    }
  }


}
