package kattendance.dashboard.kanalytics.in.kattendance;

public class Backlockmodel {
  private String Date;
  private String checkin;
  private String checkout;


  public Backlockmodel(String Date,String checkin,String checkout){
    this.Date=Date;
    this.checkin=checkin;
    this.checkout=checkout;



  }

  public Backlockmodel() {

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
