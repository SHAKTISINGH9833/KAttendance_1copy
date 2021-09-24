package kattendance.dashboard.kanalytics.in.kattendance;

public class Backlogmodel {
  private String Date;
  private String checkin;
  private String checkout;


  public Backlogmodel(String Date, String checkin, String checkout){
    this.Date=Date;
    this.checkin=checkin;
    this.checkout=checkout;



  }

  public Backlogmodel() {

  }

  public String getDate() {
    return Date;
  }

  public void setDate(String date) {
    Date = date;
  }

  public String getCheckin() {
    return checkin;
  }

  public void setCheckin(String checkin) {
    this.checkin = checkin;
  }

  public String getCheckout() {
    return checkout;
  }

  public void setCheckout(String checkout) {
    this.checkout = checkout;
  }
}
