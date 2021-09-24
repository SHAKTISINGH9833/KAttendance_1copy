package kattendance.dashboard.kanalytics.in.kattendance;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;



import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
  LayoutInflater inflater;
  public String value;
  //    List<questionanssetmodel> setlis =new ArrayList<>();
  ArrayList<String> listOfSelectedCheckBoxId = new ArrayList();
  List<Backlogmodel> questionlist;


  public Adapter(Context ctx, List<Backlogmodel> questionlist) {
    this.inflater = LayoutInflater.from(ctx);
    this.questionlist = questionlist;
  }


  public Adapter() {

  }

  @NonNull

  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = inflater.inflate(R.layout.backlock_layout, parent, false);
    return new ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    final Backlogmodel backlogmodel = questionlist.get(position);
    String data = questionlist.get(position).getDate();
    holder.txtDateList.setText(data);
    holder.txtTimeInList.setText(questionlist.get(position).getCheckin());
    holder.txtTimeOutList.setText(questionlist.get(position).getCheckout());
    holder.update.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {


        Intent intent = new Intent(v.getContext(),Updateattendnce.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("Date",questionlist.get(position).getDate());
        intent.putExtra("checkin",questionlist.get(position).getCheckin());
        intent.putExtra("checkout",questionlist.get(position).getCheckout());


        v.getContext().startActivity(intent);
//        String url = response.get(position).getUrl();
//        Bundle bundle = new Bundle();
//        bundle.putString("key_1",url);
//        intent.putExtras(bundle);

//        Toast.makeText(inflater.getContext(), "item selected", Toast.LENGTH_SHORT).show();
      }
    });
    holder.itemView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

      }
    });



    String newdata[] = data.split(Pattern.quote("|"));
    if (questionlist.get(position).getCheckout().equals("r")) {





    }



  }

  @Override
  public int getItemCount() {
    return questionlist.size();
  }

  public class ViewHolder extends RecyclerView.ViewHolder {
    public TextView txtDateList,txtTimeInList,txtTimeOutList;
    public ImageButton update;
    private AttendanceAdapter.ItemClickListener clickListener;


    TextView questionsetgradio;

    public ViewHolder(@NonNull View itemView) {
      super(itemView);

      txtDateList=(TextView)  itemView.findViewById(R.id.txtDateList);
      txtTimeInList=(TextView)  itemView.findViewById(R.id.txtTimeInList);
      txtTimeOutList=(TextView)  itemView.findViewById(R.id.txtTimeOutList);
      update=(ImageButton)itemView.findViewById(R.id.edite);

    }
  }


}
