package kattendance.dashboard.kanalytics.in.kattendance;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.CancellationSignal;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by whit3hawks on 11/16/16.
 */
@RequiresApi(api = Build.VERSION_CODES.M)
public class FingerprintHandler extends FingerprintManager.AuthenticationCallback {
    private Context context;
    Intent intent1;
CancellationSignal cancellationSignal;


  public Intent getIntent() {
    return intent1;
  }

  // Constructor
    public FingerprintHandler(Context mContext) {
      context = mContext;

    }
    public void startAuth(FingerprintManager manager, FingerprintManager.CryptoObject cryptoObject) {
      cancellationSignal=new CancellationSignal();
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        manager.authenticate(cryptoObject, cancellationSignal, 0, this, null);
        //cancellationSignal.cancel();
    }


  @Override
    public void onAuthenticationError(int errMsgId, CharSequence errString) {
    this.update("Fingerprint Authentication error\n" + errString);
    //cancellationSignal.cancel();
  }

    @Override
    public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
        this.update("Fingerprint Authentication help\n" + helpString);
    }

    @Override
    public void onAuthenticationFailed() {
      this.update("Fingerprint Authentication failed.");
      Button btnFp=(Button)((Activity)context).findViewById(R.id.btnGoBack);
      btnFp.setVisibility(View.VISIBLE);
      btnFp.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          Intent i=new Intent(context,NavigationDrawerActivity.class);
          context.startActivity(i);
          cancellationSignal.cancel();
        }
      });

    }

    @Override
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
        ((Activity) context).finish();
    }
    private void update(String e){
        TextView textView = (TextView) ((Activity)context).findViewById(R.id.errorText);
        textView.setText(e);
    }

    }






