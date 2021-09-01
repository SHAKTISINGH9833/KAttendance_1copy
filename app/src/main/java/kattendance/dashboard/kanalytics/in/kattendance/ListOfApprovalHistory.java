package kattendance.dashboard.kanalytics.in.kattendance;

public class ListOfApprovalHistory {

String requestleave_id,title,description,status,date,todate,rejoindate;

public ListOfApprovalHistory(){}

public ListOfApprovalHistory(String requestleave_id,String title,String description,String status,String date,String todate,String rejoindate)
{
  this.requestleave_id=requestleave_id;
  this.title=title;
  this.description=description;
  this.status=status;
  this.date=date;
  this.todate=todate;
  this.rejoindate=rejoindate;
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

  public void setRejoindate(String rejoindate){ this.rejoindate=rejoindate; }
  public String getRejoindate() { return rejoindate;  }
}
