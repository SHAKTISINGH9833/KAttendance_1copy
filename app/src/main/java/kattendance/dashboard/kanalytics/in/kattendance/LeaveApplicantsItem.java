package kattendance.dashboard.kanalytics.in.kattendance;

public class LeaveApplicantsItem {
  String requestleave_id,title,description,status,date,todate,username,user_id,auth_id,rejoindate,contactleave,locationleave;

  public LeaveApplicantsItem(){}

  public LeaveApplicantsItem(String requestleave_id,String title,String description,String status,String date,String todate,String username,String user_id,String auth_id,String rejoindate,String contactleave,String locationleave)
  {
    this.requestleave_id=requestleave_id;
    this.title=title;
    this.description=description;
    this.status=status;
    this.date=date;
    this.todate=todate;
    this.username=username;
    this.user_id=user_id;
    this.auth_id=auth_id;
    this.rejoindate=rejoindate;
    this.contactleave=contactleave;
    this.locationleave=locationleave;
  }

  public void setId(String requestleave_id) { this.requestleave_id=requestleave_id; }
  public String getId() {
    return requestleave_id;
  }

  public void setTitle(String title)
  {
    this.title=title;
  }
  public String getTitle() {
    return title;
  }

  public void setDescription(String description)
  {
    this.description=description;
  }
  public String getDescription() {
    return description;
  }

  public void setStatus(String status)
  {
    this.status=status;
  }
  public String getStatus() {
    return status;
  }

  public void setDate(String date)
  {
    this.date=date;
  }
  public String getDate()
  {
    return date;
  }

  public void setTodate(String todate) { this.todate=todate; }
  public String getTodate() { return todate; }

  public void setUsername(String username) { this.username=username; }
  public String getUsername() { return username; }

  public void setUser_id(String user_id) { this.user_id=user_id;}
  public String getUser_id() { return user_id; }

  public void setAuth_id(String auth_id) { this.auth_id=auth_id; }
  public String getAuth_id() { return auth_id; }

  public void setRejoindate(String rejoindate){this.rejoindate=rejoindate;}
  public String getRejoindate() {return rejoindate;}

  public void setContactleave(String contactleave){this.contactleave=contactleave;}
  public String getContactleave() {return contactleave;}

  public void setLocationleave(String locationleave){this.locationleave=locationleave;}
  public String getLocationleave(){return locationleave;}
}
