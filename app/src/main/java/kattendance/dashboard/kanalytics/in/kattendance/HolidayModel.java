package kattendance.dashboard.kanalytics.in.kattendance;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HolidayModel implements Parcelable {
  @SerializedName("results")
  @Expose
  private HolidayofCity results;

  public HolidayofCity getResults() {
    return results;
  }

  public void setResults(HolidayofCity results) {
    this.results = results;
  }

  protected HolidayModel(Parcel in) {
    results = (HolidayofCity) in.readValue(HolidayofCity.class.getClassLoader());
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeValue(results);
  }

  @SuppressWarnings("unused")
  public static final Parcelable.Creator<HolidayModel> CREATOR = new Parcelable.Creator<HolidayModel>() {
    @Override
    public HolidayModel createFromParcel(Parcel in) {
      return new HolidayModel(in);
    }

    @Override
    public HolidayModel[] newArray(int size) {
      return new HolidayModel[size];
    }
  };
}
