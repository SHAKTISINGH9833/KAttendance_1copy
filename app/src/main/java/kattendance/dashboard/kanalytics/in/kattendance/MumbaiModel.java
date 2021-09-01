package kattendance.dashboard.kanalytics.in.kattendance;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MumbaiModel implements Parcelable {
  @SerializedName("name")
  @Expose
  private String name;
  @SerializedName("date")
  @Expose
  private String date;

  public MumbaiModel() {

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

  protected MumbaiModel(Parcel in) {
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
  public static final Parcelable.Creator<MumbaiModel> CREATOR = new Parcelable.Creator<MumbaiModel>() {
    @Override
    public MumbaiModel createFromParcel(Parcel in) {
      return new MumbaiModel(in);
    }

    @Override
    public MumbaiModel[] newArray(int size) {
      return new MumbaiModel[size];
    }
  };
}
