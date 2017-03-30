package dunavnet.com.mojbac.activities.fragments;


import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.FIFOLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import dunavnet.com.mojbac.R;
import dunavnet.com.mojbac.model.MyEvents;

public class MapFragment extends Fragment implements OnMapReadyCallback, LocationListener{

    public static final double BELGRADE_CITY_LATITUDE = 44.818438;
    public static final double BELGRADE_CITY_LONGITUDE = 20.456958;
    private static final double LAT_CENT_MAP = 45.2644, LON_CENT_MAP = 19.8317; //novisad
    public ArrayList<MyEvents> mEventsList;
    public Activity activity;
    public LocationManager mlocManager;
    public ProgressDialog loadingDialog;
    long locatingTime = 0;
    boolean isGPS = false;
    boolean isEnabled = false;
    private GoogleMap mMap;
    public Marker mMarker;
    public long refreshTime = 0;
    public boolean flag = false;
    private ImageLoader imageLoader;
    private DisplayImageOptions options;
    public Marker openedMarker = null;

    LatLng defaultLocation = new LatLng(LAT_CENT_MAP, LON_CENT_MAP);

    // TODO: Rename and change types and number of parameters
    public static MapFragment newInstance(ArrayList<MyEvents> eventsList) {
        MapFragment fragment = new MapFragment();
        Bundle args = new Bundle();
        args.putSerializable("events", eventsList);
        fragment.setArguments(args);
        return fragment;
    }

    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity = getActivity();

        int status = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(activity);

        if (status != ConnectionResult.SUCCESS) {
            int requestCode = 10;
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status,
                    activity, requestCode);
            dialog.show();

        } else {
            if (mlocManager == null) {
                mlocManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
                mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
                mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
                loadingDialog = new ProgressDialog(activity);
                loadingDialog.setMessage(getString(R.string.locating));
                loadingDialog.show();
                locatingTime = System.currentTimeMillis();
                startTimmer();
            }
            if (!mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                isGPS = buildAlertMessageNoGps();
            }
        }

        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        initImageLoader();

        imageLoader = ImageLoader.getInstance();

        options = new DisplayImageOptions.Builder()      //    Display Stub Image
                .showImageForEmptyUri(R.drawable.icon)    //    If Empty image found
                .build();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mEventsList = (ArrayList<MyEvents>) getArguments().getSerializable("events");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            mlocManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
            mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
            if(loadingDialog != null) {
                if (loadingDialog.isShowing())
                    loadingDialog.dismiss();
            }
            loadingDialog = new ProgressDialog(activity);
            loadingDialog.setMessage(getString(R.string.locating));
            loadingDialog.show();
            locatingTime = System.currentTimeMillis();
            startTimmer();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_map, container, false);
    }


    @Override
    public void onLocationChanged(Location location) {
        if (loadingDialog.isShowing())
            loadingDialog.dismiss();

        if (refreshTime == 0) {

            refreshTime = 1;
            String text = "My current location is: " + "Latitude = " + location.getLatitude() + " Longitude = " + location.getLongitude();

            System.out.println(text);

            defaultLocation = new LatLng(location.getLatitude(), location.getLongitude());
            //animateMarker(mMarker, current, true);
            if (!flag) {
                flag = true;
                //mMarker.setPosition(current);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(defaultLocation));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(15));

            }
            //mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
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
    public void onMapReady(GoogleMap map) {

        mMap = map;

        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter());

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                float container_height = getResources().getDimension(R.dimen.DIP_300);

                Projection projection = mMap.getProjection();

                Point markerScreenPosition = projection.toScreenLocation(marker.getPosition());
                Point pointHalfScreenAbove = new Point(markerScreenPosition.x,(int) (markerScreenPosition.y - (container_height / 2)));

                LatLng aboveMarkerLatLng = projection.fromScreenLocation(pointHalfScreenAbove);

                CameraUpdate center = CameraUpdateFactory.newLatLng(aboveMarkerLatLng);
                mMap.moveCamera(center);
                mMap.animateCamera(center);
                marker.showInfoWindow();
                openedMarker = marker;
                return true;
            }
        });

        for(MyEvents event: mEventsList){
            double lat = Double.parseDouble(event.getGpsx());
            double lng = Double.parseDouble(event.getGpsy());
            LatLng eventLocation = new LatLng(lat, lng);

            mMarker = mMap.addMarker(new MarkerOptions().position(eventLocation).title(getString(R.string.marker_position))
                    .draggable(false)
                    .icon(BitmapDescriptorFactory
                            .fromResource(R.drawable.pin3)));


        }
        mMap.moveCamera(CameraUpdateFactory.newLatLng(defaultLocation));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
    }

    public void startTimmer(){
        new MyThread().start();
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

    private boolean buildAlertMessageNoGps() {
        loadingDialog.dismiss();
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                activity);
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
    public void onPause() {
        super.onPause();
        refreshTime = 0;
        if (mlocManager != null)
            mlocManager.removeUpdates(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        refreshTime = 0;
        if (mlocManager != null)
            mlocManager.removeUpdates(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mlocManager != null)
            mlocManager.removeUpdates(this);
    }

    public boolean isInfoWindowOpen(){
        return openedMarker.isInfoWindowShown();
    }

    public void hideInfoWindow(){
        openedMarker.hideInfoWindow();
    }

    private class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

        private View view;

        public CustomInfoWindowAdapter() {
            view = activity.getLayoutInflater().inflate(R.layout.info_content,
                    null);
        }

        @Override
        public View getInfoContents(Marker marker) {

            if (mMarker != null
                    && mMarker.isInfoWindowShown()) {
                mMarker.hideInfoWindow();
                mMarker.showInfoWindow();
            }
            return null;
        }

        @Override
        public View getInfoWindow(final Marker marker) {
            mMarker = marker;
            double latitude = marker.getPosition().latitude;
            double longitude = marker.getPosition().longitude;
            MyEvents e = null;
            for(MyEvents event: mEventsList){
                double lat = Double.parseDouble(event.getGpsx());
                double lng = Double.parseDouble(event.getGpsy());
                if(lat == latitude && lng == longitude) {
                    e = event;
                    break;
                }
            }

            ImageView ivPhoto = (ImageView) view.findViewById(R.id.info_window_image);
            TextView date = (TextView) view.findViewById(R.id.observation_date);
            TextView time = (TextView) view.findViewById(R.id.observation_time_text);
            TextView desc = (TextView) view.findViewById(R.id.observation_description_text);
            TextView category = (TextView) view.findViewById(R.id.observation_category_text);

            Date date1 = null;
            String datetime = "";
            SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
            SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy");
            try {
                date1 = format.parse(e.getDate().split(" ")[0]);

                datetime = dateFormat.format(date1);
            } catch (Exception ex) {
                // TODO Auto-generated catch block
                ex.printStackTrace();
            }

            date.setText(datetime);
            time.setText(e.getTime());
            desc.setText(e.getDescription());
            category.setText(e.getIssueCategoryName());

            if ( e.getImagePath() != null && !e.getImagePath().equalsIgnoreCase("null")
                    && !e.getImagePath().equalsIgnoreCase("")) {
                imageLoader.displayImage(e.getImagePath(), ivPhoto, options, new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingComplete(String imageUri, View view,
                                                  Bitmap loadedImage) {
                        super.onLoadingComplete(imageUri, view, loadedImage);
                        getInfoContents(marker);
                    }
                });
            } else {
                ivPhoto.setImageResource(R.drawable.icon);
            }


            return view;
        }
    }

    private void initImageLoader() {
        int memoryCacheSize;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
            int memClass = ((ActivityManager)
                    activity.getSystemService(Context.ACTIVITY_SERVICE))
                    .getMemoryClass();
            memoryCacheSize = (memClass / 8) * 1024 * 1024;
        } else {
            memoryCacheSize = 2 * 1024 * 1024;
        }

        final ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(activity).
                threadPoolSize(5)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .memoryCacheSize(memoryCacheSize)
                .memoryCache(new FIFOLimitedMemoryCache(memoryCacheSize-1000000))
                .denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .build();

        ImageLoader.getInstance().init(config);
    }
}
