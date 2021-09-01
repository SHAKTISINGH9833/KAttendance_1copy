package kattendance.dashboard.kanalytics.in.kattendance;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.annotation.Nullable;
import com.google.android.material.textfield.TextInputLayout;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ExpenseForm extends AppCompatActivity {
  private static final int CAMERA_PICTURE = 1;
  private static final int GALLERY_PICTURE = 2;
  ActionBar ab;
  EditText in_description,in_Date,in_particulars,in_amount;
  Button in_addMore,in_submit,btn_upload;
  TextView imguploadstatus;
  ImageView img_upload;
  String st_desc,st_particular,st_date,st_amt,user_id,user_name,authority_id;
  TextInputLayout til_amount,til_description,til_date,til_particulars;
  float fl_amount;
  Bitmap chosenImage;
  Image normalImage;
  String filePath = null,imgSelected=null;
  File destination=null;
  SharedPreferences shared;
  Dialog dialog1;
  private ProgressDialog pDialog;
  private Uri fileUri,selectedImageUri;
  private static String FILE_UPLOAD_URL="https://dashboard.kanalytics.in/mobile_app/attendance/image_upload.php";
  private static String GENERATE_EXPENSE_ID="https://dashboard.kanalytics.in/mobile_app/attendance/generate_expense_id.php";
  private static String REMOVE_GENERATED_ID="https://dashboard.kanalytics.in/mobile_app/attendance/remove_generated_id.php";
  //private static String SUBMIT_FINAL_EXPENSEFORM="https://dashboard.kanalytics.in/mobile_app/attendance/final_expense_generation.php";
  private static String NOTIFICATION_TO_ADMIN_FOR_EXPENSE="https://dashboard.kanalytics.in/mobile_app/attendance/noti_from_user_expense.php";
  String expense_id;
  AlertDialog builder1;
  ScrollView sv;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.expenses_form);
    ab = getSupportActionBar();
    ab.setDisplayHomeAsUpEnabled(true);

    pDialog=new ProgressDialog(this);
    dialog1=new Dialog(this);
    sv=(ScrollView)findViewById(R.id.scrollView1);
    shared = getSharedPreferences("info", MODE_PRIVATE);
    user_id = shared.getString("user_id", "");
    user_name=shared.getString("user_name","");
    authority_id=shared.getString("team_lead","");

    //Toast.makeText(this, "", Toast.LENGTH_SHORT).show();

    in_description=(EditText)findViewById(R.id.description);
    in_Date=(EditText)findViewById(R.id.inputTypeDate);
    in_particulars=(EditText)findViewById(R.id.particulars);
    in_amount=(EditText)findViewById(R.id.amount);

    //in_addMore=(Button)findViewById(R.id.addMore);
    in_submit=(Button)findViewById(R.id.submit);
    //btn_upload=(Button)findViewById(R.id.btnupload);
    img_upload=(ImageView)findViewById(R.id.upload);

    til_amount=(TextInputLayout)findViewById(R.id.tilamount);
    til_description=(TextInputLayout)findViewById(R.id.tildescription);
    til_date=(TextInputLayout)findViewById(R.id.tilDate);
    til_particulars=(TextInputLayout)findViewById(R.id.tilParticulars);

    SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String currentDateandTime = date_format.format(new Date());

    final java.util.Calendar myCalendar = java.util.Calendar.getInstance();
    //Date
    final DatePickerDialog.OnDateSetListener inputDate = new DatePickerDialog.OnDateSetListener() {
      @Override
      public void onDateSet(DatePicker view, int year, int monthOfYear,
                            int dayOfMonth) {
        myCalendar.set(java.util.Calendar.YEAR, year);
        myCalendar.set(java.util.Calendar.MONTH, monthOfYear);
        myCalendar.set(java.util.Calendar.DAY_OF_MONTH, dayOfMonth);
        String myFormat = "dd-MM-yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
        in_Date.setText(sdf.format(myCalendar.getTime()));
        st_date=sdf.format(myCalendar.getTime());
      }
    };
    in_Date.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        DatePickerDialog dp = new DatePickerDialog(ExpenseForm.this,inputDate, myCalendar
          .get(java.util.Calendar.YEAR), myCalendar.get(java.util.Calendar.MONTH),
          myCalendar.get(java.util.Calendar.DAY_OF_MONTH));
        dp.show();
      }
    });


      img_upload.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          imgSelected = "fulfilled";
          if(TextUtils.isEmpty(st_date))
          {
            Toast.makeText(ExpenseForm.this, "Date field cannot be empty !!!", Toast.LENGTH_SHORT).show();
          }
          else {
            requestForSpecificPermission();
            choosePictureAction();
          }
        }
      });

    // ====================================== On Text Change Listener ================================================== //

    in_description.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {  }
      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) { }
      @Override
      public void afterTextChanged(Editable s) {
        til_description.setError(null);
        til_description.setErrorEnabled(false);
      }
    });

    in_Date.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) { }
      @Override
      public void afterTextChanged(Editable s) {
        til_date.setError(null);
        til_date.setErrorEnabled(false);
      }
    });

    in_particulars.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) { }
      @Override
      public void afterTextChanged(Editable s) {
        til_particulars.setError(null);
        til_particulars.setErrorEnabled(false);
      }
    });

    in_amount.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {  }
      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) { }
      @Override
      public void afterTextChanged(Editable s) {
        til_amount.setError(null);
        til_amount.setErrorEnabled(false);
      }
    });
    // ====================================== On Click Listener ===================================================== //

/*    btn_upload.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        new uploadAsynctask().execute(FILE_UPLOAD_URL);
      }
    });*/

/*
    in_addMore.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
      }
    });*/

    in_submit.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        st_amt = in_amount.getText().toString();
        st_desc=in_description.getText().toString();
        st_particular=in_particulars.getText().toString();

        if(TextUtils.isEmpty(st_desc))
        {
          sv.smoothScrollTo(0,0);
          hideSoftKeyboard(ExpenseForm.this);
          til_description.setError("This field cannot be empty");
          return;
        }
        else if(TextUtils.isEmpty(st_date))
        {
          sv.smoothScrollTo(0,0);
          hideSoftKeyboard(ExpenseForm.this);
          til_date.setError("This field cannot be empty");
          return;
        }
        else if(!TextUtils.isEmpty(st_date))
        {
           // here set the pattern as you date in string was containing like date/month/year
            try {
            SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
            Date d1 = sdf1.parse(st_date);
            Calendar cal1 = Calendar.getInstance();
            cal1.setTime(d1);
          // Make a Calendar whose DATE part is some time yesterday.
            Calendar cal = Calendar.getInstance();
           //cal.roll(Calendar.DATE, -1);
            if (cal1.before(cal)) {
              //Toast.makeText(ExpenseForm.this, "This date is valid", Toast.LENGTH_SHORT).show();
              if(TextUtils.isEmpty(st_particular))
              {
                sv.smoothScrollTo(0,0);
                hideSoftKeyboard(ExpenseForm.this);
                til_particulars.setError("This field cannot be empty");
                return;
              }
              else if (TextUtils.isEmpty(st_amt)) {
                sv.smoothScrollTo(0,0);
                hideSoftKeyboard(ExpenseForm.this);
                til_amount.setError("This field cannot be empty");
                return;
              }
              else if(!TextUtils.isEmpty(st_amt))
              {
                String regexStr = "^[1-9]\\d*(\\.\\d+)?$"; //^[1-9]\d*(\.\d+)?$
                if(in_amount.getText().toString().trim().matches(regexStr)) {
                  if(chosenImage==null) {
                    Toast.makeText(ExpenseForm.this, "Please upload receipt image as proof", Toast.LENGTH_SHORT).show();
                  }
                  else
                  {
                    new uploadAsynctask().execute(FILE_UPLOAD_URL);
                  }
                }
                else{
                  sv.smoothScrollTo(0,0);
                  hideSoftKeyboard(ExpenseForm.this);
                  til_amount.setError("Invalid amount");
                }
              }

            } else {
            //  myDate must be today or later
              sv.smoothScrollTo(0,0);
              hideSoftKeyboard(ExpenseForm.this);
              til_date.setError("Voucher creation date cannot be greater than current date");
            }
            }
           catch (ParseException e)
            {
              e.printStackTrace();
            }
        }
        sendNotificationToAdmin(user_id,authority_id,user_name);
      }
    });
    generateExpenseId(user_id);
  }

  private void hideSoftKeyboard(Activity activity) {
    InputMethodManager inputMethodManager =
      (InputMethodManager) activity.getSystemService(
        Activity.INPUT_METHOD_SERVICE);
    inputMethodManager.hideSoftInputFromWindow(
      activity.getCurrentFocus().getWindowToken(), 0);
  }

  private void sendNotificationToAdmin(final String user_id,final String authority_id,final String user_name) {
    StringRequest strReq = new StringRequest(Request.Method.POST,
      NOTIFICATION_TO_ADMIN_FOR_EXPENSE, new Response.Listener<String>() {
      @Override
      public void onResponse(String s) {

      }
    }, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError volleyError) {
        Toast.makeText(getApplicationContext(),volleyError.getMessage(), Toast.LENGTH_LONG).show();
      }
    })
    {
      @Override
      protected Map<String, String> getParams() {        // Posting params to register url

        Map<String, String> params = new HashMap<String, String>();
        params.put("user_id",user_id);
        params.put("user_name",user_name);
        params.put("auth_id",authority_id);
        return params;
      }
    };
    MyApplication.getInstance().addToRequestQueue(strReq);
  }

  private void generateExpenseId(final String user_id) {
    String tag_string_req = "req_register";

    pDialog.setMessage("Please Wait ...");
    showDialog();

    StringRequest strReq = new StringRequest(Request.Method.POST,
      GENERATE_EXPENSE_ID, new Response.Listener<String>() {

      @Override
      public void onResponse(String response) {

        hideDialog();

        try {

          JSONObject jObj = new JSONObject(response);
          JSONArray jsonMainNode = jObj.optJSONArray("results");

          JSONObject result = jsonMainNode.getJSONObject(0);
          String error = result.getString("error");
          if (error.trim().equals("true")) {
           expense_id = result.getString("id");
            //Toast.makeText(ExpenseForm.this, "Expense ID : "+expense_id, Toast.LENGTH_LONG).show();
          }
        }
        catch (JSONException e) {

          e.printStackTrace();
        }
      }
    }, new Response.ErrorListener() {

      @Override
      public void onErrorResponse(VolleyError error) {
        // Log.e(TAG, "Registration Error: " + error.getMessage());
        Toast.makeText(getApplicationContext(),
          error.getMessage(), Toast.LENGTH_LONG).show();
        hideDialog();
      }
    }) {

      @Override
      protected Map<String, String> getParams() {

        Map<String, String> params = new HashMap<String, String>();
        params.put("user_id", user_id);

        return params;
      }

    };


    MyApplication.getInstance().addToRequestQueue(strReq,tag_string_req);

  }

  private class uploadAsynctask extends AsyncTask<String, Void, String>{

    ProgressDialog dialog1;
    @Override
    protected void onPreExecute() {
      super.onPreExecute();
      //Toast.makeText(ExpenseForm.this, "user_id : "+user_id, Toast.LENGTH_SHORT).show();
      dialog1 = ProgressDialog.show(ExpenseForm.this, null, null);
      ProgressBar spinner = new android.widget.ProgressBar(ExpenseForm.this, null,android.R.attr.progressBarStyle);
      spinner.getIndeterminateDrawable().setColorFilter(Color.parseColor("#009689"), android.graphics.PorterDuff.Mode.SRC_IN);
      dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
      dialog1.setContentView(spinner);
      dialog1.setCancelable(false);
      dialog1.show();
    }
    @Override
    protected String doInBackground(String... params) {
      Log.e("filepath",filePath);

      File file1=new File(filePath);
      Log.e("okay","lol"+file1);
      Log.e("okay",user_id);
      MultipartEntity reqEntity;
      HttpEntity resEntity;
      try {
        HttpClient client = new DefaultHttpClient();
        String postURL = params[0];
        HttpPost post = new HttpPost(postURL);

    FileBody bin1=new FileBody(file1);

        reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

        reqEntity.addPart("uploadedfile1",bin1);
        reqEntity.addPart("user_id",new StringBody(user_id));
        reqEntity.addPart("date",new StringBody(st_date));
        reqEntity.addPart("expense_id",new StringBody(expense_id));
        reqEntity.addPart("amount",new StringBody(st_amt));
        reqEntity.addPart("particular",new StringBody(st_particular));
        reqEntity.addPart("description",new StringBody(st_desc));
        //params.put("user_id", user_id);
        /*params.put("expense_id", expense_id);
        params.put("amount", st_amt);
        params.put("date", st_date);
        params.put("particular", st_particular);
        params.put("description", st_desc);*/

        post.setEntity(reqEntity);
        HttpResponse response = client.execute(post);
        resEntity = response.getEntity();
        String entityContentAsString = EntityUtils.toString(resEntity);
        Log.d("stream:", entityContentAsString);

        return entityContentAsString;

      } catch (Exception e) {
        e.printStackTrace();
      }
      return null;
    }

    @Override
    protected void onPostExecute(String s) {
      super.onPostExecute(s);
      //Log.e("Result Mukhi",result);
    //Toast.makeText(ExpenseForm.this, "Result"+s, Toast.LENGTH_LONG).show();
      dialog1.dismiss();
            if(s.trim().equals("success"))
           {
             Intent intent=new Intent(ExpenseForm.this,Expenses.class);
             startActivity(intent);
             finish();
             /*btn_upload.setVisibility(View.GONE);
             txt_upload.setVisibility(View.VISIBLE);
             txt_upload.setText("Image uploaded succesfully");
             txt_upload.setTextSize(15);
             txt_upload.setTextColor(Color.GREEN);*/
             //Toast.makeText(ExpenseForm.this, "Successfully inserted in Folder", Toast.LENGTH_SHORT).show();
           }
           else
           {
             Toast.makeText(ExpenseForm.this, "Please try again", Toast.LENGTH_SHORT).show();
           }
    }
  }

  private void choosePictureAction() {
    final CharSequence[] items = {"Camera", "Gallery", "Cancel"};
    AlertDialog.Builder builder = new AlertDialog.Builder(ExpenseForm.this);
    builder.setTitle("Add Photo");
    builder.setItems(items, new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        if(items[which].equals("Camera")){
          Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
          startActivityForResult(intent,CAMERA_PICTURE);
        }else if(items[which].equals("Gallery")){
          Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
          startActivityForResult(intent, GALLERY_PICTURE);
        } else if(items[which].equals("Cancel")){
          dialog.dismiss();
        }
      }
    });
    builder.show();
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if(requestCode == CAMERA_PICTURE && resultCode == RESULT_OK){

      chosenImage = (Bitmap)data.getExtras().get("data");
      ByteArrayOutputStream bytes = new ByteArrayOutputStream();
      chosenImage.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
      destination = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis() + ".jpg");
      FileOutputStream fo;
      if(imgSelected != null){
        filePath = destination.toString();
        //Toast.makeText(this, "Filepath"+filePath, Toast.LENGTH_SHORT).show();
      }
      try {
        destination.createNewFile();
        fo = new FileOutputStream(destination);
        fo.write(bytes.toByteArray());
        fo.close();
      } catch (FileNotFoundException e) {
        e.printStackTrace();
      } catch (IOException e) {
        e.printStackTrace();
      }

      if(chosenImage != null) {
        if (imgSelected != null) {
          //imgSelected = null;
          img_upload.setImageBitmap(chosenImage);
          img_upload.setOnClickListener(null);
          //img_upload.getLayoutParams().height=(int)getResources().getDimension(R.dimen.imageview_height);
        }
      }
    }
    else if (requestCode == GALLERY_PICTURE
      && resultCode == RESULT_OK) {
      selectedImageUri = data.getData();

      String[] projection = {MediaStore.MediaColumns.DATA};
      Cursor cursor = getContentResolver().query(selectedImageUri, projection, null, null, null);
      int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
      cursor.moveToFirst();
      String selectedImagePath = cursor.getString(column_index);
      //Toast.makeText(this, "Selected Image"+selectedImagePath, Toast.LENGTH_SHORT).show();
      String extension = selectedImagePath.substring(selectedImagePath.lastIndexOf("."));
      if (extension.equals(".jpg") || extension.equals(".png")) {
        destination = new File(selectedImagePath);
        if (imgSelected != null) {
          filePath = selectedImagePath;
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(selectedImagePath, options);
        final int REQUIRED_SIZE = 200;
        int scale = 1;
        while (options.outWidth / scale / 2 >= REQUIRED_SIZE
          && options.outHeight / scale / 2 >= REQUIRED_SIZE)
          scale *= 2;
        options.inSampleSize = scale;
        options.inJustDecodeBounds = false;
        chosenImage = BitmapFactory.decodeFile(selectedImagePath, options);
        if (chosenImage != null) {
          if (imgSelected != null) {
            img_upload.setImageBitmap(chosenImage);
            img_upload.setOnClickListener(null);
          }
        }
      }
    }
  }



  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    onBackPressed();
    return true;
  }

  @Override
  public void onBackPressed() {

    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
    alertBuilder.setMessage("Are you sure you want to discard");
    alertBuilder.setCancelable(true);

    alertBuilder.setPositiveButton(
      "Yes",
      new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int id) {
          deleteGeneratedId(user_id,expense_id);
         // dialog.cancel();
        }
      });

    alertBuilder.setNegativeButton(
      "No",
      new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int id) {
          dialog.cancel();
        }
      });

     builder1 = alertBuilder.create();
      builder1.show();
   //super.onBackPressed();
    /*Intent moveToNextAcitivty=new Intent(ExpenseForm.this,Expenses.class);

    //  moveToNextAcitivty.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
    startActivity(moveToNextAcitivty);
    finish();*/
  }

  private void deleteGeneratedId(final String user_id,final String expense_id) {
    String tag_string_req = "req_register";

    pDialog.setMessage("Please Wait ...");
    showDialog();

    StringRequest strReq = new StringRequest(Request.Method.POST,
      REMOVE_GENERATED_ID, new Response.Listener<String>() {

      @Override
      public void onResponse(String response) {
        //Toast.makeText(ExpenseForm.this, " Response "+response, Toast.LENGTH_SHORT).show();


        try {

          JSONObject jObj = new JSONObject(response);
          JSONArray jsonMainNode = jObj.optJSONArray("results");

          JSONObject result = jsonMainNode.getJSONObject(0);
          String error = result.getString("error");
          if (error.trim().equals("true")) {
            hideDialog();
            Intent intent=new Intent(ExpenseForm.this,Expenses.class);
            startActivity(intent);
            finish();
            //Toast.makeText(ExpenseForm.this, "Expense ID : "+expense_id, Toast.LENGTH_LONG).show();
          }
          else
          {
            Toast.makeText(ExpenseForm.this, "Please try again ", Toast.LENGTH_SHORT).show();
            builder1.hide();
            hideDialog();
          }
        }
        catch (JSONException e) {

          e.printStackTrace();
        }
      }
    }, new Response.ErrorListener() {

      @Override
      public void onErrorResponse(VolleyError error) {
        // Log.e(TAG, "Registration Error: " + error.getMessage());
        Toast.makeText(getApplicationContext(),
          error.getMessage(), Toast.LENGTH_LONG).show();
        hideDialog();
      }
    }) {

      @Override
      protected Map<String, String> getParams() {

        Map<String, String> params = new HashMap<String, String>();
        params.put("user_id", user_id);
        params.put("expense_id", expense_id);

        return params;
      }

    };


    MyApplication.getInstance().addToRequestQueue(strReq,tag_string_req);
  }

  private void requestForSpecificPermission() {
    ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA,android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 101);
  }
  @Override
  public void onRequestPermissionsResult(int requestCode,  String[] permissions,  int[] grantResults) {


    switch (requestCode) {
      case 101:
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
        } else {
        }
        break;
      default:
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

  }

  private void showDialog() {
    if (!pDialog.isShowing())
      pDialog.show();
  }

  private void hideDialog() {
    if (pDialog.isShowing())
      pDialog.dismiss();
  }
}
