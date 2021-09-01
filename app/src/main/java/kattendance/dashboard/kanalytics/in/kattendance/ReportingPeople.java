package kattendance.dashboard.kanalytics.in.kattendance;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReportingPeople implements Parcelable {

  @SerializedName("id")
  @Expose
  private String id;
  @SerializedName("name")
  @Expose
  private String name;
  @SerializedName("contact")
  @Expose
  private String contact;
  @SerializedName("office_address")
  @Expose
  private String officeAddress;
  @SerializedName("team")
  @Expose
  private String team;
  @SerializedName("working_days")
  @Expose
  private String workingDays;
  @SerializedName("dept_id")
  @Expose
  private String deptId;
  @SerializedName("team_lead")
  @Expose
  private String teamLead;
  @SerializedName("actual_in_time")
  @Expose
  private String actualInTime;
  @SerializedName("actual_out_time")
  @Expose
  private String actualOutTime;
  @SerializedName("reason_in")
  @Expose
  private String reasonIn;
  @SerializedName("reason_out")
  @Expose
  private String reasonOut;

  public ReportingPeople() {

  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getContact() {
    return contact;
  }

  public void setContact(String contact) {
    this.contact = contact;
  }

  public String getOfficeAddress() {
    return officeAddress;
  }

  public void setOfficeAddress(String officeAddress) {
    this.officeAddress = officeAddress;
  }

  public String getTeam() {
    return team;
  }

  public void setTeam(String team) {
    this.team = team;
  }

  public String getWorkingDays() {
    return workingDays;
  }

  public void setWorkingDays(String workingDays) {
    this.workingDays = workingDays;
  }

  public String getDeptId() {
    return deptId;
  }

  public void setDeptId(String deptId) {
    this.deptId = deptId;
  }

  public String getTeamLead() {
    return teamLead;
  }

  public void setTeamLead(String teamLead) {
    this.teamLead = teamLead;
  }

  public String getActualInTime() {
    return actualInTime;
  }

  public void setActualInTime(String actualInTime) {
    this.actualInTime = actualInTime;
  }

  public String getActualOutTime() {
    return actualOutTime;
  }

  public void setActualOutTime(String actualOutTime) {
    this.actualOutTime = actualOutTime;
  }

  public String getReasonIn() {
    return reasonIn;
  }

  public void setReasonIn(String reasonIn) {
    this.reasonIn = reasonIn;
  }

  public String getReasonOut() {
    return reasonOut;
  }

  public void setReasonOut(String reasonOut) {
    this.reasonOut = reasonOut;
  }


  protected ReportingPeople(Parcel in) {
    id = in.readString();
    name = in.readString();
    contact = in.readString();
    officeAddress = in.readString();
    team = in.readString();
    workingDays = in.readString();
    deptId = in.readString();
    teamLead = in.readString();
    actualInTime = in.readString();
    actualOutTime = in.readString();
    reasonIn = in.readString();
    reasonOut = in.readString();
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(id);
    dest.writeString(name);
    dest.writeString(contact);
    dest.writeValue(officeAddress);
    dest.writeValue(team);
    dest.writeValue(workingDays);
    dest.writeValue(deptId);
    dest.writeValue(teamLead);
    dest.writeValue(actualInTime);
    dest.writeValue(actualOutTime);
    dest.writeValue(reasonIn);
    dest.writeValue(reasonOut);
  }

  @SuppressWarnings("unused")
  public static final Parcelable.Creator<ReportingPeople> CREATOR = new Parcelable.Creator<ReportingPeople>() {
    @Override
    public ReportingPeople createFromParcel(Parcel in) {
      return new ReportingPeople(in);
    }

    @Override
    public ReportingPeople[] newArray(int size) {
      return new ReportingPeople[size];
    }
  };
}
