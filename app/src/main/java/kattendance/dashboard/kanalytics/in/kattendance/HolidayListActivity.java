package kattendance.dashboard.kanalytics.in.kattendance;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HolidayListActivity extends AppCompatActivity {

  @BindView(R.id.viewpager)
  ViewPager viewPager;
  @BindView(R.id.mumbai_btn)
  Button mumbai_btn;
  @BindView(R.id.delhi_btn)
  Button delhi_btn;
  private MyPagerAdapter adapterViewPager;

  @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_holiday_list);
        ButterKnife.bind(this);
       getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setUpViewPager();
    }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    onBackPressed();
    return true;
  }

  @Override
  public void onBackPressed() {
    super.onBackPressed();

  }

  private void setUpViewPager() {
    adapterViewPager = new MyPagerAdapter(getSupportFragmentManager());
    viewPager.setAdapter(adapterViewPager);
    mumbaiTab();
    viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
      @Override
      public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

      }

      @Override
      public void onPageSelected(int position) {
           if(position == 0){
             mumbaiTab();
           }else  if(position == 1){
             delhiTab();
           }
      }

      @Override
      public void onPageScrollStateChanged(int state) {

      }
    });
  }

  @OnClick(R.id.mumbai_btn)
  public void mumbaiTab(){
    mumbai_btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_left_white_filled));
    delhi_btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_right_white_outline));
    mumbai_btn.setTextColor(getResources().getColor(R.color.white));
    delhi_btn.setTextColor(getResources().getColor(R.color.input_login));
    viewPager.setCurrentItem( 0, true);
    adapterViewPager.notifyDataSetChanged();
    }

  @OnClick(R.id.delhi_btn)
  public void delhiTab(){
    mumbai_btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_left_white_outline));
    delhi_btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_right_white_filled));
    mumbai_btn.setTextColor(getResources().getColor(R.color.input_login));
    delhi_btn.setTextColor(getResources().getColor(R.color.white));
    viewPager.setCurrentItem( 1, true);
    adapterViewPager.notifyDataSetChanged();
  }

  public static class MyPagerAdapter extends FragmentPagerAdapter {
    private static int NUM_ITEMS = 2;

    public MyPagerAdapter(FragmentManager fragmentManager) {
      super(fragmentManager);
    }

    @Override
    public int getCount() {
      return NUM_ITEMS;
    }

    @Override
    public Fragment getItem(int position) {
      switch (position) {
        case 0:
          return HolidayListFragment.newInstance("Mumbai");
        case 1:
          return HolidayListFragment.newInstance("Delhi");
        default:
          return null;
      }
    }

    @Override
    public CharSequence getPageTitle(int position) {
      return "Page " + position;
    }

  }
}
