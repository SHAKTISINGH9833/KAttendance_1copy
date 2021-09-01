package kattendance.dashboard.kanalytics.in.kattendance;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class HolidayofCity implements Parcelable {
  @SerializedName("Mumbai")
  @Expose
  private ArrayList<MumbaiModel> mumbai = new ArrayList<>();
  @SerializedName("Delhi")
  @Expose
  private ArrayList<DelhiModel> delhi = new ArrayList<>();

  public ArrayList<MumbaiModel> getMumbai() {
    return mumbai;
  }

  public void setMumbai(ArrayList<MumbaiModel> mumbai) {
    this.mumbai = mumbai;
  }

  public ArrayList<DelhiModel> getDelhi() {
    return delhi;
  }

  public void setDelhi(ArrayList<DelhiModel> delhi) {
    this.delhi = delhi;
  }

  protected HolidayofCity(Parcel in) {
    if (in.readByte() == 0x01) {
      mumbai = new ArrayList<MumbaiModel>();
      in.readList(mumbai, MumbaiModel.class.getClassLoader());
    } else {
      mumbai = null;
    }
    if (in.readByte() == 0x01) {
      delhi = new ArrayList<DelhiModel>();
      in.readList(delhi, DelhiModel.class.getClassLoader());
    } else {
      delhi = null;
    }
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    if (mumbai == null) {
      dest.writeByte((byte) (0x00));
    } else {
      dest.writeByte((byte) (0x01));
      dest.writeList(mumbai);
    }
    if (delhi == null) {
      dest.writeByte((byte) (0x00));
    } else {
      dest.writeByte((byte) (0x01));
      dest.writeList(delhi);
    }
  }

  @SuppressWarnings("unused")
  public static final Parcelable.Creator<HolidayofCity> CREATOR = new Parcelable.Creator<HolidayofCity>() {
    @Override
    public HolidayofCity createFromParcel(Parcel in) {
      return new HolidayofCity(in);
    }

    @Override
    public HolidayofCity[] newArray(int size) {
      return new HolidayofCity[size];
    }
  };
}
