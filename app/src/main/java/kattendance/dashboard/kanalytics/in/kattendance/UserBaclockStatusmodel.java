package kattendance.dashboard.kanalytics.in.kattendance;

public class UserBaclockStatusmodel {
  String Date;
  String In;
  String Out;
  String Name;
  String Userid;
  String backlogid;
  String authority_id;
  String status;
  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }



  public String getAuthority_id() {
    return authority_id;
  }

  public void setAuthority_id(String authority_id) {
    this.authority_id = authority_id;
  }



  public String getBacklogid() {
    return backlogid;
  }

  public void setBacklogid(String backlogid) {
    this.backlogid = backlogid;
  }



  public String getUserid() {
    return Userid;
  }

  public void setUserid(String userid) {
    Userid = userid;
  }



  public String getName() {
    return Name;
  }

  public void setName(String name) {
    Name = name;
  }

  String reason;

  public String getDate() {
    return Date;
  }

  public void setDate(String date) {
    Date = date;
  }

  public String getIn() {
    return In;
  }

  public void setIn(String in) {
    In = in;
  }

  public String getOut() {
    return Out;
  }

  public void setOut(String out) {
    Out = out;
  }

  public String getReason() {
    return reason;
  }

  public void setReason(String reason) {
    this.reason = reason;
  }
}
