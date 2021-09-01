package kattendance.dashboard.kanalytics.in.kattendance;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class ReportingPeopleAdapter extends RecyclerView.Adapter<ReportingPeopleAdapter.ViewHolder> {

  Context context;
  ArrayList<ReportingPeople> arrayList = new ArrayList<>();
  ReportingAttendance reportingAttendance;

  public ReportingPeopleAdapter(Context context, ArrayList<ReportingPeople> arrayList, ReportingAttendance reportingAttendance) {
    this.context = context;
    this.arrayList = arrayList;
    this.reportingAttendance = reportingAttendance;
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View v  = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_peoples, parent, false);
    return  new ViewHolder(v.getRootView());
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    final ReportingPeople reportingPeople = arrayList.get(position);
    holder.name.setText(reportingPeople.getName());
    holder.checkIn.setText(reportingPeople.getActualInTime());
    holder.checkOut.setText(reportingPeople.getActualOutTime());
    holder.checkInReason.setText((!TextUtils.isEmpty(reportingPeople.getReasonIn())) ? reportingPeople.getReasonIn() : "-");
    holder.checkOutReason.setText((!TextUtils.isEmpty(reportingPeople.getReasonOut())) ? reportingPeople.getReasonOut() : "-");
    holder.cardView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        reportingAttendance.onClickReportingPeople(reportingPeople.getId(),reportingPeople.getName());
      }
    });
  }

  @Override
  public int getItemCount() {
    return arrayList.size();
  }

  public class ViewHolder extends RecyclerView.ViewHolder {

    TextView name, checkIn,checkOut,checkInReason,checkOutReason;
    CardView cardView;
    public ViewHolder(View v) {
      super(v);
      name = v.findViewById(R.id.name);
      checkIn = v.findViewById(R.id.checkIn);
      checkOut = v.findViewById(R.id.checkOut);
      checkInReason = v.findViewById(R.id.checkInReason);
      checkOutReason = v.findViewById(R.id.checkOutReason);
      cardView = v.findViewById(R.id.name_cardview);
    }
  }
}
