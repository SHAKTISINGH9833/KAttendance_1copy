package kattendance.dashboard.kanalytics.in.kattendance;

import java.util.Date;

/**
 * Created by Devlopment on 19/09/17.
 */
public class AttendanceItem {
    String txtDate;
    String txtTimeIn;
    String txtTimeOut;
    String reasonIn;
    String reasonOut;
    public AttendanceItem() {

    }
    public  AttendanceItem(String txtDate,String txtTimeIn,String txtTimeOut,String reasonIn,String reasonOut){
        this.txtDate=txtDate;
        this.txtTimeIn=txtTimeIn;
        this.txtTimeOut=txtTimeOut;
        this.reasonIn=reasonIn;
        this.reasonOut=reasonOut;

    }

    public void setTxtDate(String txtDate){
        this.txtDate=txtDate;

    }
    public String getTxtDate(){
        return txtDate;
    }

    public void setTxtTimeIn(String txtTimeIn){
        this.txtTimeIn=txtTimeIn;

    }
    public String getTxtTimeIn(){
        return txtTimeIn;
    }

    public void setTxtTimeOut(String txtTimeOut){
        this.txtTimeOut=txtTimeOut;

    }
    public String getTxtTimeOut(){
        return txtTimeOut;
    }

    public void setReasonIn(String reasonIn){
        this.reasonIn=reasonIn;

    }
    public String getReasonIn(){
        return reasonIn;
    }

    public void setReasonOut(String reasonOut){
        this.reasonOut=reasonOut;

    }
    public String getReasonOut(){
        return reasonOut;
    }

}
