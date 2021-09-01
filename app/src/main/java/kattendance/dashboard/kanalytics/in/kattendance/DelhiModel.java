package kattendance.dashboard.kanalytics.in.kattendance;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DelhiModel implements Parcelable {
  @SerializedName("name")
  @Expose
  private String name;
  @SerializedName("date")
  @Expose
  private String date;

  public DelhiModel() {

  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }

  protected DelhiModel(Parcel in) {
    name = in.readString();
    date = in.readString();
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(name);
    dest.writeString(date);
  }

  @SuppressWarnings("unused")
  public static final Parcelable.Creator<DelhiModel> CREATOR = new Parcelable.Creator<DelhiModel>() {
    @Override
    public DelhiModel createFromParcel(Parcel in) {
      return new DelhiModel(in);
    }

    @Override
    public DelhiModel[] newArray(int size) {
      return new DelhiModel[size];
    }
  };
}
