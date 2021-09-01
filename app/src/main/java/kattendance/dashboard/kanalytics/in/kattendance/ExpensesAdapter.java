package kattendance.dashboard.kanalytics.in.kattendance;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ExpensesAdapter extends BaseAdapter {
  private Activity activity;
  private LayoutInflater inflater;
  private List<ExpensesHistory> listOfExpensesHistory;
  String finaldate;

  public ExpensesAdapter(Activity activity,List<ExpensesHistory> listOfExpensesHistory)
  {
      this.activity=activity;
      this.listOfExpensesHistory=listOfExpensesHistory;
  }
  @Override
  public int getCount() { return listOfExpensesHistory.size(); }
  @Override
  public Object getItem(int position) { return listOfExpensesHistory.get(position); }

  @Override
  public long getItemId(int position) { return position; }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    if (inflater == null)
      inflater = (LayoutInflater) activity
        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    if (convertView == null)
      convertView = inflater.inflate(R.layout.activity_expense_item, null);

    TextView description = (TextView) convertView.findViewById(R.id.txtDescription);
    TextView particulars = (TextView) convertView.findViewById(R.id.txtParticulars);
    TextView amount=(TextView) convertView.findViewById(R.id.txtAmount);
    TextView date=(TextView) convertView.findViewById(R.id.txtDate);
    CircleImageView circularPic=(CircleImageView) convertView.findViewById(R.id.imgThumnail);

      final ExpensesHistory m=listOfExpensesHistory.get(position);
    Picasso.with(activity).load(m.getReceipt_img()).into(circularPic);
    description.setText(m.getDescription());
    particulars.setText(m.getParticulars());
    amount.setText("Amount: ₹"+m.getAmount());
    amount.setTextColor(Color.BLUE);

    String getdate=m.getDate();
    SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat output = new SimpleDateFormat("dd-MMM-yyyy");
    try {
      Date parsed = input.parse(getdate);
      finaldate=output.format(parsed);
    }
    catch (ParseException e )
    {
      e.printStackTrace();
    }

    date.setText("Date : "+finaldate);

    circularPic.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(activity,OnClickImagePreview.class);
        intent.putExtra("picture",m.getReceipt_img());
        v.getContext().startActivity(intent);
      }
    });
    return convertView;
  }
}
