package kattendance.dashboard.kanalytics.in.kattendance;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

/**
 * Created by Devlopment on 15/09/17.
 */
public class SessionManager {

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;
    private static final String KEY_IS_WAITING_FOR_SMS = "IsWaitingForSms";
    private static final String KEY_IS_FIRST_TIME = "IsFirstTime";
    // shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "Kanalytics-Grumble";

    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";
    private static final String IS_FIRST_TIME_LAUNCH_PROFILE = "IsFirstTimeLaunchProfile";

    private static final String KEY_IS_LOGGEDIN = "isLoggedIn";

    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);

        editor = pref.edit();
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }


    public void setFirstTimeProfileLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH_PROFILE, isFirstTime);
        editor.commit();
    }

    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }

    public boolean isFirstTimeProfileLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH_PROFILE, true);
    }

    public void setLogin(boolean isLoggedIn) {

        editor.putBoolean(KEY_IS_LOGGEDIN, isLoggedIn);


        editor.commit();


    }

    public void setAttendanceIn(boolean isLoggedIn) {

        editor.putBoolean(KEY_IS_LOGGEDIN, isLoggedIn);


        editor.commit();


    }

    public void setIsWaitingForSms(boolean isWaiting) {

        editor.putBoolean(KEY_IS_WAITING_FOR_SMS, isWaiting);
        editor.commit();

    }

    public boolean isWaitingForSms() {
        return pref.getBoolean(KEY_IS_WAITING_FOR_SMS, false);
    }

    public boolean isFirstTime() {
        return pref.getBoolean(KEY_IS_FIRST_TIME, true);
    }

    public void setIsFirstTime(boolean isFirstTime) {
        editor.putBoolean(KEY_IS_FIRST_TIME, isFirstTime);
        editor.commit();
    }




    public boolean isLoggedIn(){
        return pref.getBoolean(KEY_IS_LOGGEDIN, false);
    }

    public boolean isAttendanceIn(){
        return pref.getBoolean(KEY_IS_LOGGEDIN, false);
    }
}
