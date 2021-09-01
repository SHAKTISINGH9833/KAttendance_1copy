package kattendance.dashboard.kanalytics.in.kattendance;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ListOfApprovalAdapter extends BaseAdapter {
  private Activity activity;
  private LayoutInflater inflater;
  private List<ListOfApprovalHistory> listOfApprovalHistories;
  String outputFromDate,outputToDate,outputRejoinDate;


  public ListOfApprovalAdapter(Activity activity,List <ListOfApprovalHistory> listOfApprovalHistories)
  {
    this.activity=activity;
    this.listOfApprovalHistories=listOfApprovalHistories;
  }

  @Override
  public int getCount() {
    return listOfApprovalHistories.size();
  }

  @Override
  public Object getItem(int position) {
    return listOfApprovalHistories.get(position);
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    if (inflater == null)
      inflater = (LayoutInflater) activity
        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    if (convertView == null)
      convertView = inflater.inflate(R.layout.activity_list_of_approval_item, null);

    TextView title = (TextView) convertView.findViewById(R.id.txttitle);
    TextView datefrom = (TextView) convertView.findViewById(R.id.txtFromDate);
    TextView dateto = (TextView) convertView.findViewById(R.id.txtToDate);
    TextView daterejoin = (TextView) convertView.findViewById(R.id.txtRejoinDate);
    TextView status=(TextView) convertView.findViewById(R.id.txtStatus);
    TextView description=(TextView) convertView.findViewById(R.id.txtDescription);
    ListOfApprovalHistory m=listOfApprovalHistories.get(position);
    title.setText(m.getTitle());
    description.setText(m.getDescription());
    final String statusValue=m.getStatus();
    final String titleValue=m.getTitle();
    String getdate=m.getDate();
    String todate=m.getTodate();
    String rejoindate=m.getRejoindate();
    SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat output = new SimpleDateFormat("dd-MMM-yyyy");
    try {
      Date parsed = input.parse(getdate);
      Date parsed1=input.parse(todate);
      Date parsed2= input.parse(rejoindate);
      outputFromDate = output.format(parsed);
      outputToDate=output.format(parsed1);
      outputRejoinDate=output.format(parsed2);
    } catch (ParseException e) {
      e.printStackTrace();
    }
        datefrom.setText("FROM : "+outputFromDate);
        dateto.setText("TO : "+outputToDate);
        daterejoin.setText("Rejoin Date : "+outputRejoinDate);


    if(statusValue.trim().equals("0"))
    {
      status.setText("Open");
      status.setTextColor(Color.parseColor("#ffa500"));
    }
    else if(statusValue.trim().equals("1"))
    {
      status.setText("Accepted");
      status.setTextColor(Color.parseColor("#10EB4C"));
    }
    else if(statusValue.trim().equals("2"))
    {
      status.setText("Denied");
      status.setTextColor(Color.parseColor("#F9190B"));
    }
    else
    {
      status.setText("Closed");
      status.setTextColor(Color.parseColor("#1222DE"));
    }

    return convertView;
  }
}
