package kattendance.dashboard.kanalytics.in.kattendance;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class HolidayListAdapter extends RecyclerView.Adapter<HolidayListAdapter.ViewHoler> {

  Context context;
  ArrayList<MumbaiModel> mumbaiModels = new ArrayList<>();
  ArrayList<DelhiModel>  delhiModels = new ArrayList<>();

  public HolidayListAdapter(Context context, ArrayList<MumbaiModel> mumbaiModels,ArrayList<DelhiModel>  delhiModels) {
    this.context = context;
    this.mumbaiModels = mumbaiModels;
    this.delhiModels = delhiModels;
  }

  @NonNull
  @Override
  public ViewHoler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View itemView = LayoutInflater.from(parent.getContext())
      .inflate(R.layout.holiday_card, parent, false);
    return new ViewHoler(itemView);
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHoler holder, int position) {
   if(mumbaiModels.size()!=0){
     MumbaiModel mumbaiModel = mumbaiModels.get(position);
     holder.holiday_name.setText(mumbaiModel.getName());
     holder.holiday_date.setText(DateFormater(mumbaiModel.getDate()));

     if (mumbaiModel.getName().trim().toLowerCase().equals( "New Year Day".trim().toLowerCase())){
       Glide.with(context)
         .load(context.getResources().getDrawable(R.drawable.icon_new_year))
         .thumbnail(0.5f)
         .dontAnimate()
         .into(holder.img);
     }else if(mumbaiModel.getName().trim().toLowerCase().equals( "Republic Day".trim().toLowerCase())){
       Glide.with(context)
         .load(context.getResources().getDrawable(R.drawable.icon_republic_day))
         .thumbnail(0.5f)
         .dontAnimate()
         .into(holder.img);
     }else if(mumbaiModel.getName().trim().toLowerCase().equals( "Holi".trim().toLowerCase())){
       Glide.with(context)
         .load(context.getResources().getDrawable(R.drawable.holi_day))
         .thumbnail(0.5f)
         .dontAnimate()
         .into(holder.img);
     }else if(mumbaiModel.getName().trim().toLowerCase().equals( "Gudi Padwa".trim().toLowerCase())){
       Glide.with(context)
         .load(context.getResources().getDrawable(R.drawable.icon_gudi_padwa))
         .thumbnail(0.5f)
         .dontAnimate()
         .into(holder.img);
     }else if(mumbaiModel.getName().trim().toLowerCase().equals( "Maharashtra Day".trim().toLowerCase())){
       Glide.with(context)
         .load(context.getResources().getDrawable(R.drawable.icon_maharashtra_day))
         .thumbnail(0.5f)
         .dontAnimate()
         .into(holder.img);
     }else if(mumbaiModel.getName().trim().toLowerCase().equals( "Ganesh Chaturthi".trim().toLowerCase())){
       Glide.with(context)
         .load(context.getResources().getDrawable(R.drawable.icon_ganesh_chaturthi))
         .thumbnail(0.5f)
         .dontAnimate()
         .into(holder.img);
     }else if(mumbaiModel.getName().trim().toLowerCase().equals( "Mahatma Gandhi Jayanti".trim().toLowerCase())){
       Glide.with(context)
         .load(context.getResources().getDrawable(R.drawable.icon_mahatma_gandhi))
         .thumbnail(0.5f)
         .dontAnimate()
         .into(holder.img);
     }else if(mumbaiModel.getName().trim().toLowerCase().equals( "Dussehra".trim().toLowerCase())){
       Glide.with(context)
         .load(context.getResources().getDrawable(R.drawable.icon_dussehra))
         .thumbnail(0.5f)
         .dontAnimate()
         .into(holder.img);
     }else if(mumbaiModel.getName().trim().toLowerCase().equals( "Diwali".trim().toLowerCase())){
       Glide.with(context)
         .load(context.getResources().getDrawable(R.drawable.icon_diwali))
         .thumbnail(0.5f)
         .dontAnimate()
         .into(holder.img);
     }else if(mumbaiModel.getName().trim().toLowerCase().equals( "Christamas".trim().toLowerCase())){
       Glide.with(context)
         .load(context.getResources().getDrawable(R.drawable.christmas_day))
         .thumbnail(0.5f)
         .dontAnimate()
         .into(holder.img);
     }

   }else if(delhiModels.size()!=0){
     DelhiModel delhiModel = delhiModels.get(position);
     holder.holiday_name.setText(delhiModel.getName());
     holder.holiday_date.setText(DateFormater(delhiModel.getDate()));

     if (delhiModel.getName().trim().toLowerCase().equals( "New Year Day".trim().toLowerCase())){
       Glide.with(context)
         .load(context.getResources().getDrawable(R.drawable.icon_new_year))
         .thumbnail(0.5f)
         .dontAnimate()
         .into(holder.img);
     }else if(delhiModel.getName().trim().toLowerCase().equals( "Republic Day".trim().toLowerCase())){
       Glide.with(context)
         .load(context.getResources().getDrawable(R.drawable.icon_republic_day))
         .thumbnail(0.5f)
         .dontAnimate()
         .into(holder.img);
     }else if(delhiModel.getName().trim().toLowerCase().equals( "Holi".trim().toLowerCase())){
       Glide.with(context)
         .load(context.getResources().getDrawable(R.drawable.holi_day))
         .thumbnail(0.5f)
         .dontAnimate()
         .into(holder.img);
     }else if(delhiModel.getName().trim().toLowerCase().equals( "Good Friday".trim().toLowerCase())){
       Glide.with(context)
         .load(context.getResources().getDrawable(R.drawable.icon_good_friday))
         .thumbnail(0.5f)
         .dontAnimate()
         .into(holder.img);
     }else if(delhiModel.getName().trim().toLowerCase().equals( "Ram Navmi".trim().toLowerCase())){
       Glide.with(context)
         .load(context.getResources().getDrawable(R.drawable.icon_ramnavmi))
         .thumbnail(0.5f)
         .dontAnimate()
         .into(holder.img);
     }else if(delhiModel.getName().trim().toLowerCase().equals( "Ramjan Eid".trim().toLowerCase())){
       Glide.with(context)
         .load(context.getResources().getDrawable(R.drawable.icon_eid))
         .thumbnail(0.5f)
         .dontAnimate()
         .into(holder.img);
     }else if(delhiModel.getName().trim().toLowerCase().equals( "Muharram".trim().toLowerCase())){
       Glide.with(context)
         .load(context.getResources().getDrawable(R.drawable.icon_muharram))
         .thumbnail(0.5f)
         .dontAnimate()
         .into(holder.img);
     }else if(delhiModel.getName().trim().toLowerCase().equals( "Dussehra".trim().toLowerCase())){
       Glide.with(context)
         .load(context.getResources().getDrawable(R.drawable.icon_dussehra))
         .thumbnail(0.5f)
         .dontAnimate()
         .into(holder.img);
     }else if(delhiModel.getName().trim().toLowerCase().equals( "Diwali".trim().toLowerCase())){
       Glide.with(context)
         .load(context.getResources().getDrawable(R.drawable.icon_diwali))
         .thumbnail(0.5f)
         .dontAnimate()
         .into(holder.img);
     }else if(delhiModel.getName().trim().toLowerCase().equals( "Guru Nanak Jayanti".trim().toLowerCase())){
       Glide.with(context)
         .load(context.getResources().getDrawable(R.drawable.icon_guru_nanak))
         .thumbnail(0.5f)
         .dontAnimate()
         .into(holder.img);
     }
   }
  }

  public String DateFormater (String date)
  {
    SimpleDateFormat sdf;
    sdf = new SimpleDateFormat("yyyy-MM-dd");//format of the date which you send as parameter(if the date is like 08-Aug-2016 then use dd-MMM-yyyy)
    String s = "";
    try {
      Date dt = sdf.parse(date);
      sdf = new SimpleDateFormat("dd MMM yyyy");
      s = sdf.format(dt);

    } catch (ParseException e) {
      e.printStackTrace();
    }
    return s;
  }

  @Override
  public int getItemCount() {
    return (mumbaiModels.size() != 0 ? mumbaiModels.size(): delhiModels.size());
  }

  public class ViewHoler extends RecyclerView.ViewHolder{

    ImageView img;
    TextView holiday_date,holiday_name;

    public ViewHoler(@NonNull View itemView) {
      super(itemView);
      img = itemView.findViewById(R.id.src_festival);
      holiday_name = itemView.findViewById(R.id.festival_name);
      holiday_date = itemView.findViewById(R.id.holiday_date);
    }
  }
}
