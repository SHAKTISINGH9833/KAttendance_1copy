package kattendance.dashboard.kanalytics.in.kattendance;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by Devlopment on 22/09/17.
 */
public class FireIDService extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        String tkn = FirebaseInstanceId.getInstance().getToken();
        //Log.d("Not","Token ["+tkn+"]");

    }

}
