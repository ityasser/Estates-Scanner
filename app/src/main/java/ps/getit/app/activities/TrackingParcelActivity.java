package ps.getit.app.activities;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.intentfilter.androidpermissions.PermissionManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ps.getit.app.Custom.CustomMapView;
import ps.getit.app.Custom.CustomMarkerDrow;
import ps.getit.app.R;
import ps.getit.app.drawroutemap.DrawRouteMaps;
import ps.getit.app.utils.Utility;

public class TrackingParcelActivity extends BaseActivity {
    String order_id="", delivery_id="", sender_name = "", sender_lat = "", sender_lng = "", receiver_name = "", receiver_lat = "", receiver_lng = "", driver_lat = "", driver_lng = "", driver_mobile = "", driver_name = "";
    int driver_estimated_time = 0;

    private GoogleMap googleMap;

    @BindView(R.id.mapView)
    CustomMapView mMapView;

    @BindView(R.id.time_trimnat)
    TextView time_trimnat;

    @Override
    protected void onDestroy() {
        super.onDestroy();


        if(mNotificationBadgeReciever!=null){


            unregisterReceiver(mNotificationBadgeReciever);
        }



    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking_parcel);
        Utility.setTitle("تتبع الطرد",this);
        ButterKnife.bind(this);


        mNotificationBadgeReciever = new NotificationBadgeReciever();
        mFilter = new IntentFilter("notification.view.badge");


        registerReceiver(mNotificationBadgeReciever , mFilter);


        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }


        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            order_id=extras.getString("order_id");
            sender_name=extras.getString("sender_name");
            sender_lat=extras.getString("sender_lat");
            sender_lng=extras.getString("sender_lng");
            receiver_name=extras.getString("receiver_name");
            receiver_lat=extras.getString("receiver_lat");
            receiver_lng=extras.getString("receiver_lng");
            driver_lat=extras.getString("driver_lat");
            driver_lng=extras.getString("driver_lng");
            driver_mobile=extras.getString("driver_mobile");
            driver_name=extras.getString("driver_name");
            driver_estimated_time=extras.getInt("driver_estimated_time");
            Log.d("driver_estimated_time",""+driver_estimated_time);
            delivery_id=extras.getString("delivery_id");
            time_trimnat.setText(Utility.formatHoursAndMinutes(driver_estimated_time));

        }

        iniMap();
    }
    private void setMyLocationEnabled() {

        List<String> permissionsNeeded2 = new ArrayList<String>();
        permissionsNeeded2.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
        permissionsNeeded2.add(android.Manifest.permission.ACCESS_COARSE_LOCATION);
        PermissionManager permissionManager2 = PermissionManager.getInstance(this);
        permissionManager2.checkPermissions(permissionsNeeded2, new PermissionManager.PermissionRequestListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onPermissionGranted() {

                Log.i("location","PermissionGranted user");

                googleMap.setMyLocationEnabled(true);


            }
            @Override
            public void onPermissionDenied() {

            }
        });
    }
    Marker marker_driver;

    public void iniMap(){

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                mMap.clear();
                googleMap = mMap;
                // For showing a move to my location button
                setMyLocationEnabled();

                LatLng sydney = new LatLng( Double.parseDouble(driver_lat), Double.parseDouble(driver_lng));
                BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.car1);
            //    googleMap.addMarker(new MarkerOptions().position(sydney).visible(true).title(driver_name).snippet("").icon(icon));
                marker_driver = googleMap.addMarker(new MarkerOptions().position(sydney).visible(true).title(driver_name).snippet("").icon(icon));


                LatLng sender = new LatLng(Double.parseDouble(sender_lat), Double.parseDouble(sender_lng));
                LatLng receiver = new LatLng(Double.parseDouble(receiver_lat), Double.parseDouble(receiver_lng));


                DrawRouteMaps.getInstance(TrackingParcelActivity.this)
                        .draw(sender, receiver, mMap);

                CustomMarkerDrow.getInstance(TrackingParcelActivity.this).draw(mMap, sender, getResources().getDrawable(  R.drawable.pin_user), "من "+sender_name);
                CustomMarkerDrow.getInstance(TrackingParcelActivity.this).draw(mMap, receiver, getResources().getDrawable(  R.drawable.pin_delvary), "الى "+receiver_name);

                LatLngBounds bounds = new LatLngBounds.Builder()
                        .include(sender)
                        .include(receiver).build();
                Point displaySize = new Point();
                getWindowManager().getDefaultDisplay().getSize(displaySize);
                mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, displaySize.x, 250, 30));

                UiSettings uiSettings = googleMap.getUiSettings();
                uiSettings.setIndoorLevelPickerEnabled(true);
                uiSettings.setMyLocationButtonEnabled(true);
                uiSettings.setMapToolbarEnabled(true);

                uiSettings.setCompassEnabled(true);
                uiSettings.setZoomControlsEnabled(true);
            }
        });


    }

        @OnClick(R.id.telephone)
        public void telephone(){


            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", driver_mobile, null));
            startActivity(intent);
        }

    @OnClick(R.id.sms)
    public void sms(){
        Intent smsIntent = new Intent(android.content.Intent.ACTION_VIEW);
        smsIntent.setType("vnd.android-dir/mms-sms");
        smsIntent.putExtra("address",driver_mobile);
        smsIntent.putExtra("sms_body","");
        startActivity(smsIntent);


    }

    NotificationBadgeReciever  mNotificationBadgeReciever;
    IntentFilter mFilter ;

    class NotificationBadgeReciever extends BroadcastReceiver {


        @Override
        public void onReceive(Context context, Intent intent) {



            Bundle extras = intent.getExtras();
            if(extras != null) {


                if(order_id.equals(extras.getString("order_id"))) {
                    if (marker_driver != null)
                        marker_driver.remove();


                    driver_lat = extras.getString("lat");
                    driver_lng = extras.getString("lng");

                    time_trimnat.setText(Utility.formatHoursAndMinutes(Integer.parseInt(extras.getString("estimated_time"))));


                    LatLng sydney = new LatLng(Double.parseDouble(driver_lat), Double.parseDouble(driver_lng));
                    BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.car1);
                    //    googleMap.addMarker(new MarkerOptions().position(sydney).visible(true).title(driver_name).snippet("").icon(icon));
                    marker_driver = googleMap.addMarker(new MarkerOptions().position(sydney).visible(true).title(driver_name).snippet("").icon(icon));


                    Log.d("delivery_gcm", extras.getString("delivery_id") + "  " + driver_lat + "  " + driver_lng);
                }
            }



        }
    }
}
