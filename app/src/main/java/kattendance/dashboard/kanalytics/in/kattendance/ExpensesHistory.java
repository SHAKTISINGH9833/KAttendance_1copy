package kattendance.dashboard.kanalytics.in.kattendance;

public class ExpensesHistory {
  String expense_id,description,particulars,amount,receipt_img,date;

  public ExpensesHistory(){}

  public ExpensesHistory(String expense_id,String description,String particulars,String amount,String receipt_img,String date){
    this.expense_id=expense_id;
    this.description=description;
    this.particulars=particulars;
    this.amount=amount;
    this.receipt_img=receipt_img;
    this.date=date;
  }

  public void setExpense_id(String expense_id) {this.expense_id = expense_id;}
  public String getExpense_id() {return expense_id;}

  public void setDescription(String description) {this.description = description;}
  public String getDescription() {return description;}

  public void setParticulars(String particulars) {this.particulars = particulars;}
  public String getParticulars() {return particulars;}

  public void setAmount(String amount) {this.amount = amount;}
  public String getAmount() {return amount;}

  public void setReceipt_img(String receipt_img) {this.receipt_img = receipt_img;}
  public String getReceipt_img() {return receipt_img;}

  public void setDate(String date) {this.date = date;}
  public String getDate() {return date;}
}
