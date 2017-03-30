package dunavnet.com.mojiodzaci.activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.*;

import dunavnet.com.mojiodzaci.R;
import dunavnet.com.mojiodzaci.interfaces.ResponseHandler;
import dunavnet.com.mojiodzaci.model.Categories;
import dunavnet.com.mojiodzaci.model.Event;
import dunavnet.com.mojiodzaci.model.EventData;
import dunavnet.com.mojiodzaci.model.GPSCoordinates;
import dunavnet.com.mojiodzaci.model.Topics;
import dunavnet.com.mojiodzaci.tasks.LoadCategories;
import dunavnet.com.mojiodzaci.tasks.LoadTopics;
import dunavnet.com.mojiodzaci.util.Base64;
import dunavnet.com.mojiodzaci.util.SettingsConstants;

public class ReportActivity extends Activity implements ResponseHandler, AdapterView.OnItemSelectedListener {

    public static final int CATEGORIES_RESPONSE = 1;
    public static final int REPORT_RESPONSE = 2;
    public static final int TOPIC_RESPONSE = 3;

    File img = null;
    Bitmap bmp = null;
    TextView mDateDisplay;
    static ImageView takeAPct;
    Button reportBtn, cancelBtn;
    //ImageView photoCheckIcon;
    ImageView logoutImage;
    public static String imagePath;
    public static boolean imageCheck;
    SharedPreferences prefs;
    public ArrayList<Topics> topics;

    Date eventeDate;

    String data_string = "";
    static double lat1 = 0;
    static double lon1 = 0;
    Spinner spinner;
    Spinner spinner1;
    EditText commentText, addressText;
    private Event reportEvent;
    public int mode;
    public ArrayList<Categories> categories;
    Calendar myCalendar = Calendar.getInstance();
    Calendar myCalendarexp = Calendar.getInstance();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        imagePath = null;
        // capture our View elements
        mDateDisplay = (TextView) findViewById(R.id.dateText);
        takeAPct = (ImageView) findViewById(R.id.cameraBtn);
        reportBtn = (Button) findViewById(R.id.reportbtn);
        cancelBtn = (Button) findViewById(R.id.buttonCancelProfile);
        commentText = (EditText) findViewById(R.id.reporttext);
        addressText = (EditText) findViewById(R.id.adressTxt);
        logoutImage = (ImageView) findViewById(R.id.logout);
        //photoCheckIcon = (ImageView) findViewById(R.id.photoCheck);

        spinner = (Spinner) findViewById(R.id.spinn);
        spinner1 = (Spinner) findViewById(R.id.spinn1);

        spinner.setOnItemSelectedListener(this);

        prefs = getSharedPreferences(SettingsConstants.PARTICIPATORY_SENSING_PREFERENCES, Context.MODE_PRIVATE);

        setMode(TOPIC_RESPONSE);
        new LoadTopics(this).execute();

        takeAPct.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                /* Intent cameraIntent = new Intent(v.getContext(),
                        CameraActivity.class);

                startActivity(cameraIntent);*/

                AlertDialog.Builder builder = new AlertDialog.Builder(ReportActivity.this);

                builder.setMessage(getResources().getString(R.string.picturechoicemessage))
                        .setCancelable(true)
                        .setPositiveButton(getResources().getString(R.string.deletephoto), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                imageCheck = false;
                                ReportActivity.imagePath = null;
                                //photoCheckIcon.setImageDrawable(ReportActivity.this.getResources().getDrawable(R.drawable.nosnapshot));
                                takeAPct.setImageDrawable(ReportActivity.this.getResources().getDrawable(R.drawable.add_photo));
                            }
                        })
                        /*.setNegativeButton(getResources().getString(R.string.fromgallery), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent pict = new Intent(ReportActivity.this, GalleryActivity.class);
                                startActivity(pict);
                            }
                        })*/
                        .setTitle(getResources().getString(R.string.picturechoicetitle))
                        .setNeutralButton(getResources().getString(R.string.takepict), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent pict = new Intent(ReportActivity.this, CameraActivity.class);
                                startActivity(pict);
                            }
                        });
                builder.create();
                builder.show();
            }
        });


        reportBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                //new ReportTask().execute();
                if (wrapData()) {
                    String email = prefs.getString("email","");
                    String name = prefs.getString("name","");
                    String surname = prefs.getString("surname","");
                    String token = prefs.getString("access_token", "");
                    Intent intent;
                    if(name.equals("") || email.equals("") || surname.equals("")){
                        intent = new Intent(v.getContext(),
                                ProfileActivity.class);
                        startActivity(intent);
                    }else{
                        //setMode(REPORT_RESPONSE);
                        //new ReportTask(ReportActivity.this, token, getReportEvent(), email, name, surname).execute();
                        intent = new Intent(v.getContext(),
                                MapPositioningActivity.class);
                        intent.putExtra("event", getReportEvent());
                        intent.putExtra("token", token);
                        intent.putExtra("email", email);
                        intent.putExtra("name", name);
                        intent.putExtra("surname", surname);
                        startActivity(intent);
                    }


                }
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        logoutImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //prefs.edit().putString("access_token", "").commit();
                prefs.edit().putInt("signed", 0).commit();
                Intent intent = new Intent(getApplicationContext(), LoginActivityNew.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                ActivityCompat.finishAffinity(ReportActivity.this);
            }
        });

        /*mDateDisplay.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(ReportActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });*/


        imageCheck = false;

        setStartDate();
    }

    public void setMode(int mode){
        this.mode = mode;
    }

    public int getMode(){
        return mode;
    }

    public static void setImagePath(String path) {
        ReportActivity.imagePath = path;
        ReportActivity.imageCheck = true;
    }

    public static void setImageCheck(boolean bool) {
        ReportActivity.imageCheck = bool;
        File img1 = null;
        Bitmap bmp1 = null;
        if (ReportActivity.imagePath == null && imageCheck) {
            img1 = new File("/sdcard/%d.jpg");

            if (img1.exists()) {
                bmp1 = BitmapFactory.decodeFile(img1.getAbsolutePath());
                takeAPct.setImageBitmap(Bitmap.createScaledBitmap(bmp1, 63, 43, false));
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ReportActivity.imageCheck) {
            setImageCheck(true);
            //photoCheckIcon.setImageDrawable(this.getResources().getDrawable(R.drawable.snapshot));
            //takeAPct.setImageDrawable(this.getResources().getDrawable(R.drawable.add_photo));
        } else {
            takeAPct.setImageDrawable(this.getResources().getDrawable(R.drawable.add_photo));
            //photoCheckIcon.setImageDrawable(this.getResources().getDrawable(R.drawable.nosnapshot));
        }

    }

    private void setStartDate()
    {
        updateLabel();
    }

    public void updateLabel(){
        eventeDate = myCalendar.getTime();
        int minute = myCalendar.get(Calendar.MINUTE);
        // / TimeLabeler.MINUTEINTERVAL * TimeLabeler.MINUTEINTERVAL;
        mDateDisplay.setText(String.format("%te %tB %tY, %tH:%02d",
                myCalendar, myCalendar, myCalendar, myCalendar, minute));

        Log.d("VREME @", eventeDate.toGMTString());
    }

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
        }

    };


    // @SuppressLint("SdCardPath")
    private boolean wrapData() {
        BitmapFactory.Options options=new BitmapFactory.Options();
        options.inSampleSize = 8;
        InputStream is = null;
        Bitmap preview_bitmap=BitmapFactory.decodeStream(is,null,options);
        try {
            if (ReportActivity.imagePath == null && imageCheck) {
                img = new File("/sdcard/%d.jpg");

                if (img.exists()) {
                    bmp = BitmapFactory.decodeFile(img.getAbsolutePath());

                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    Bitmap resized = getResizedBitmap(bmp, 800);
                    resized.compress(Bitmap.CompressFormat.JPEG, 50, stream);

                    byte[] data1 = stream.toByteArray();

                    byte[] encoded_data = Base64.encodeBytesToBytes(data1);
                    data_string = new String(encoded_data); // convert to string
                }
            } else if (imageCheck) {
                bmp = BitmapFactory.decodeFile(imagePath);

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                Bitmap resized = getResizedBitmap(bmp, 800);
                resized.compress(Bitmap.CompressFormat.JPEG, 50, stream);

                byte[] data1 = stream.toByteArray();

                byte[] encoded_data = Base64.encodeBytesToBytes(data1);
                data_string = new String(encoded_data); // convert to string

            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            data_string = "";
        }


        String comment = "";
        String address = "";

        try {
            comment = commentText.getText().toString().trim();
            address = addressText.getText().toString().trim();

        } catch (Exception e) {
            // TODO: handle exception
        }


        if (spinner1.getSelectedItemPosition() != 0 && eventeDate != null) {

            Date now = new Date();

            Log.d("VREME moje", now.toString());

            //Event event = new Event(spinner.getSelectedItem().toString().replaceAll(" ", "_"), spinner1.getSelectedItem().toString(), new EventData(comment, "JPEG", data_string, new GPSCoordinates(lat1, lon1), eventeDate, eventExpDate.getTime(), comment));
            //Event event = new Event(spinner.getSelectedItem().toString().replaceAll(" ", "_"), Integer.toString(spinner1.getSelectedItemPosition() - 1), new EventData(comment, "JPEG", data_string, new GPSCoordinates(lat1, lon1), eventeDate, 0, comment, address));
            Event event = new Event(spinner.getSelectedItem().toString().replaceAll(" ", "_"), categories.get(spinner1.getSelectedItemPosition() - 1).getCategoryId(), new EventData(comment, "JPEG", data_string, new GPSCoordinates(lat1, lon1), eventeDate, 0, comment, address));
            Log.d("VREME", eventeDate.toString());
            if (img != null && img.exists()) {
                bmp.recycle();
                img.delete();
                System.gc();
            }

            setEvent(event);
            return true;
        } else {
            Toast.makeText(ReportActivity.this, getString(R.string.emptyfield), Toast.LENGTH_LONG).show();
            return false;
        }

    }

    public void setEvent(Event event){
        reportEvent = event;
    }

    public Event getReportEvent(){
        return reportEvent;
    }

    private Bitmap getResizedBitmap(Bitmap bm, int newWidth) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float aspect = (float)width / height;
        float scaleWidth = newWidth;
        float scaleHeight = scaleWidth / aspect;        // yeah!
        // create a matrix for the manipulation
        Matrix matrix = new Matrix();
        // resize the bit map
        matrix.postScale(scaleWidth / width, scaleHeight / height);
        // recreate the new Bitmap
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
        bm.recycle();
        return resizedBitmap;
    }

    @Override
    public void onResponseReceived(String response) {
        if(getMode() == TOPIC_RESPONSE){
            if(response.equals("")){
                Toast.makeText(ReportActivity.this, getString(R.string.nointernetaccess), Toast.LENGTH_LONG).show();
            }else {
                topics = Topics.fromStringJson(response);
                ArrayList<String> topicsString = new ArrayList<>();
                topicsString.add("-");
                for (Topics cat : topics) {
                    topicsString.add(cat.getName());
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                        android.R.layout.simple_spinner_item, topicsString);
                spinner.setAdapter(adapter);
            }

        }
        if(getMode() == CATEGORIES_RESPONSE) {
            if(response.equals("")){
                Toast.makeText(ReportActivity.this, getString(R.string.nointernetaccess), Toast.LENGTH_LONG).show();
            }else {
                categories = Categories.fromStringJson(response);
                ArrayList<String> categoriesString = new ArrayList<>();
                categoriesString.add("-");
                for (Categories cat : categories) {
                    categoriesString.add(cat.getName());
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                        android.R.layout.simple_spinner_item, categoriesString);
                spinner1.setAdapter(adapter);
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(parent == spinner){
            if(position != 0) {
                setMode(CATEGORIES_RESPONSE);
                new LoadCategories(this, Integer.parseInt(topics.get(position-1).getTopicId())).execute();
            }else{
                ArrayList<String> categoriesString = new ArrayList<>();
                categoriesString.add("-");
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                        android.R.layout.simple_spinner_item, categoriesString);
                spinner1.setAdapter(adapter);
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}