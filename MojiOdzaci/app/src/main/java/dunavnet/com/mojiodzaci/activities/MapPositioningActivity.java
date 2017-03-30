package dunavnet.com.mojiodzaci.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import dunavnet.com.mojiodzaci.R;
import dunavnet.com.mojiodzaci.interfaces.ResponseHandler;
import dunavnet.com.mojiodzaci.model.Event;
import dunavnet.com.mojiodzaci.model.GPSCoordinates;
import dunavnet.com.mojiodzaci.model.LogData;
import dunavnet.com.mojiodzaci.tasks.LogTask;
import dunavnet.com.mojiodzaci.tasks.ReportTask;
import dunavnet.com.mojiodzaci.util.SettingsConstants;
import dunavnet.com.mojiodzaci.util.Util;

public class MapPositioningActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener, GoogleMap.OnMarkerDragListener, ResponseHandler {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    LocationManager mlocManager;
    LocationListener mlocListener;
    Location mLocation;
    Marker mMarker;
    public static final double BELGRADE_CITY_LATITUDE = 44.818438;
    public static final double BELGRADE_CITY_LONGITUDE = 20.456958;
    private static final double LAT_CENT_MAP = 45.2644, LON_CENT_MAP = 19.8317; //novisad
    Drawable icon;
    public ProgressDialog loadingDialog;
    long refreshTime = 0;
    long locatingTime = 0;
    boolean isGPS = false;
    boolean isEnabled = false;
    Button reportBtn;
    Event event;
    String token, email, name, surname;
    SharedPreferences prefs;
    public ImageView logoutImage;

    LatLng defaultLocation = new LatLng(LAT_CENT_MAP, LON_CENT_MAP);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_positioning);
        reportBtn = (Button) findViewById(R.id.reportbtn);
        logoutImage = (ImageView) findViewById(R.id.logout);
        prefs = getSharedPreferences(SettingsConstants.PARTICIPATORY_SENSING_PREFERENCES, Context.MODE_PRIVATE);
        Bundle extras = getIntent().getExtras();
        event = (Event) extras.getSerializable("event");
        token = extras.getString("token");
        email = extras.getString("email");
        name = extras.getString("name");
        surname = extras.getString("surname");
        int status = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(this);

        if (status != ConnectionResult.SUCCESS) {
            int requestCode = 10;
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status,
                    this, requestCode);
            dialog.show();

        } else {
            if (mlocManager == null) {
                mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
                mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
                loadingDialog = new ProgressDialog(this);
                loadingDialog.setMessage(getString(R.string.locating));
                loadingDialog.show();
                locatingTime = System.currentTimeMillis();
                startTimmer();
            }
            if (!mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                isGPS = buildAlertMessageNoGps();
            }
        }

        icon = this.getResources().getDrawable(R.drawable.pin3);
        //setUpMapIfNeeded();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        reportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                event.getEventData().setEventLocation(new GPSCoordinates(mMarker.getPosition().latitude, mMarker.getPosition().longitude));
                new ReportTask(MapPositioningActivity.this, token, event, email, name, surname).execute();
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
                ActivityCompat.finishAffinity(MapPositioningActivity.this);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (mlocManager != null)
            mlocManager.removeUpdates(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mlocManager != null)
            mlocManager.removeUpdates(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
            if(loadingDialog != null) {
                if (loadingDialog.isShowing())
                    loadingDialog.dismiss();
            }
            loadingDialog = new ProgressDialog(this);
            loadingDialog.setMessage(getString(R.string.locating));
            loadingDialog.show();
            locatingTime = System.currentTimeMillis();
            startTimmer();
        }
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
        mMarker = mMap.addMarker(new MarkerOptions().position(defaultLocation).title(getString(R.string.marker_position))
                .draggable(true)
                .icon(BitmapDescriptorFactory
                        .fromResource(R.drawable.pin3)));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(defaultLocation));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        mMap.setOnMarkerDragListener(this);
    }

    public void startTimmer(){
        new MyThread().start();
    }

    public boolean flag = false;
    @Override
    public void onLocationChanged(Location location) {
        if (loadingDialog.isShowing())
            loadingDialog.dismiss();

        if (refreshTime == 0
                || (refreshTime - System.currentTimeMillis()) > 20000) {

            refreshTime = System.currentTimeMillis();
            String text = "My current location is: " + "Latitude = " + location.getLatitude() + "Longitude = " + location.getLongitude();

            System.out.println(text);

            LatLng current = new LatLng(location.getLatitude(), location.getLongitude());
            //animateMarker(mMarker, current, true);
            if (!flag) {
                flag = true;
                mMarker.setPosition(current);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(current));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
            }

        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onMarkerDragStart(Marker marker) {
    }

    @Override
    public void onMarkerDrag(Marker marker) {
    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        mMarker = marker;
    }

    private boolean buildAlertMessageNoGps() {
        loadingDialog.dismiss();
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);
        alertDialogBuilder
                .setMessage(R.string.gps_warning)
                .setCancelable(false)
                .setPositiveButton(getString(R.string.settings),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                isEnabled = true;
                                Intent callGPSSettingIntent = new Intent(
                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(callGPSSettingIntent);
                            }
                        });
        alertDialogBuilder.setNegativeButton(getString(R.string.no),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
        return isEnabled;
    }

    @Override
    public void onResponseReceived(String response) {
        if(response == "") {
            popupAlert(R.string.communication_error);
        }
        else {
            if(response.contains("\"IsError\":false")) {
                //{"Data":"USPEŠNO STE EVIDENTIRALI PRIJAVU ID: 18316/2015","IsError":false,"ErrorMessage":null}

                sendLogData(response.split(":")[2].split(",")[0].trim());
                popupAlert(R.string.published);

            }
            else {
                popupAlert(R.string.could_not_publish);
            }
        }
    }

    private void popupAlert(int resourceString) {
        AlertDialog alert = new AlertDialog.Builder(this).create();
        alert.setCancelable(false);
        alert.setMessage(getResources().getString(resourceString));
        alert.setButton(getResources().getString(R.string.okButton), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(MapPositioningActivity.this,
                        MainActivityList.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                ActivityCompat.finishAffinity(MapPositioningActivity.this);

            }
        });
        alert.setIcon(R.drawable.icon);
        alert.show();
    }

    public void sendLogData(String id){
        //"183/2015#{\"IssueCategoryID\":5,\"GPSX\":44.87724548,\"GPSY\":20.65775185,\"Description\": \"Log poruka\",\"ProblemAddress\":\"Default\"}"
        id = id.replace("\"", "");
        String message = id + "#{\\\"IssueCategoryID\\\":" + event.getTitle() +
                ",\\\"GPSX\\\":" + Double.toString(event.getEventData().getEventLocation().getLatitude()) +
                ",\\\"GPSY\\\":" + Double.toString(event.getEventData().getEventLocation().getLongitude()) +
                ",\\\"Description\\\":\\\"" + event.getEventData().getText() + "\\\"" +
                ",\\\"ProblemAddress\\\":\\\"" + event.getEventData().getAddress() + "\\\"}";
        LogData log = LogData.createLog(6, message, "CommunityPoliceSubmitIssueMojNS()", "NOMp02", Util.getipv4Address(this), "NOMs08");
        System.out.println("+++++++++*************+++++++++++++ " + Util.getipv6Address());
        new LogTask(this, log).execute();
    }

    public class MyThread extends Thread {
        @Override
        public void run() {
            try {
                new Thread().sleep(20000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                if (loadingDialog.isShowing())
                    loadingDialog.dismiss();
            }
        }
    }
}
