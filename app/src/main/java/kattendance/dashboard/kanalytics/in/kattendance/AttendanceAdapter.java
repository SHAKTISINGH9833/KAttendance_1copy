package kattendance.dashboard.kanalytics.in.kattendance;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableRow;
import android.widget.TextView;



import java.util.ArrayList;

/**
 * Created by Devlopment on 19/09/17.
 */
public class AttendanceAdapter  extends RecyclerView.Adapter<AttendanceAdapter.ViewHolder> {

    ArrayList<AttendanceItem> data;
    Context context;
    TableRow tRow;
    public AttendanceAdapter(Context context, ArrayList<AttendanceItem> data) {
        super();
        this.context = context;
        this.data = data;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.activity_attendance_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v.getRootView());
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        final AttendanceItem message = data.get(position);

        viewHolder.txtDateList.setText(message.getTxtDate());
        viewHolder.txtTimeInList.setText(message.getTxtTimeIn()+"\n"+message.getReasonIn());
        viewHolder.txtTimeOutList.setText(message.getTxtTimeOut()+"\n"+message.getReasonOut());

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {


        public TextView txtDateList,txtTimeInList,txtTimeOutList;
        private ItemClickListener clickListener;

        public ViewHolder(View itemView) {
            super(itemView);
            txtDateList=(TextView)  itemView.findViewById(R.id.txtDateList);
            txtTimeInList=(TextView)  itemView.findViewById(R.id.txtTimeInList);
            txtTimeOutList=(TextView)  itemView.findViewById(R.id.txtTimeOutList);

          //  itemView.setOnClickListener(this);
          //  itemView.setOnLongClickListener(this);
        }

        public void setClickListener(ItemClickListener itemClickListener) {
            this.clickListener = itemClickListener;
        }

        @Override
        public void onClick(View view) {
            clickListener.onClick(view, getPosition(), false);
        }

        @Override
        public boolean onLongClick(View view) {
            clickListener.onClick(view, getPosition(), true);
            return true;
        }
    }

    public class ItemClickListener {
        void onClick(View view, int position, boolean isLongClick) {

        }
    }
}
