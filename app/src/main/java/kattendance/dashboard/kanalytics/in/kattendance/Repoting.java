package kattendance.dashboard.kanalytics.in.kattendance;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Repoting implements Parcelable {
  @SerializedName("statusCode")
  @Expose
  private String statusCode;
  @SerializedName("statusMessage")
  @Expose
  private String statusMessage;
  @SerializedName("result")
  @Expose
  private ArrayList<ReportingPeople> result = null;

  public Repoting() {
  }

  public String getStatusCode() {
    return statusCode;
  }

  public void setStatusCode(String statusCode) {
    this.statusCode = statusCode;
  }

  public String getStatusMessage() {
    return statusMessage;
  }

  public void setStatusMessage(String statusMessage) {
    this.statusMessage = statusMessage;
  }

  public ArrayList<ReportingPeople> getResult() {
    return result;
  }

  public void setResult(ArrayList<ReportingPeople> result) {
    this.result = result;
  }


  protected Repoting(Parcel in) {
    statusCode = in.readString();
    statusMessage = in.readString();
    if (in.readByte() == 0x01) {
      result = new ArrayList<ReportingPeople>();
      in.readList(result, ReportingPeople.class.getClassLoader());
    } else {
      result = null;
    }
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(statusCode);
    dest.writeString(statusMessage);
    if (result == null) {
      dest.writeByte((byte) (0x00));
    } else {
      dest.writeByte((byte) (0x01));
      dest.writeList(result);
    }
  }

  @SuppressWarnings("unused")
  public static final Parcelable.Creator<Repoting> CREATOR = new Parcelable.Creator<Repoting>() {
    @Override
    public Repoting createFromParcel(Parcel in) {
      return new Repoting(in);
    }

    @Override
    public Repoting[] newArray(int size) {
      return new Repoting[size];
    }
  };
}
