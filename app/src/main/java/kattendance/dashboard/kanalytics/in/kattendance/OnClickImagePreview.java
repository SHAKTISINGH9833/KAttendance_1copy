package kattendance.dashboard.kanalytics.in.kattendance;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class OnClickImagePreview extends AppCompatActivity {
  Intent myIntent;
  String img_url;
  ImageView imgview;
  ActionBar ab;
  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.enlarged_image);
    //ab = getSupportActionBar();
    //ab.setDisplayHomeAsUpEnabled(true);
    myIntent=getIntent();
    imgview=(ImageView)findViewById(R.id.image);
    img_url=myIntent.getStringExtra("picture");
    //Toast.makeText(this, "img_url"+img_url, Toast.LENGTH_LONG).show();
    Picasso.with(getApplicationContext()).load(img_url).into(imgview);

  }
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    onBackPressed();
    return true;
  }

  @Override
  public void onBackPressed() {
    super.onBackPressed();
    Intent moveToNextAcitivty=new Intent(OnClickImagePreview.this,Expenses.class);
    startActivity(moveToNextAcitivty);
    finish();
  }
}
