package ps.getit.app.fragments;


import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;

import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.intentfilter.androidpermissions.PermissionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import ps.getit.app.App;
import ps.getit.app.Dialogs.ProgressDialog;
import ps.getit.app.R;
import ps.getit.app.activities.LoginActivity;
import ps.getit.app.activities.MainActivity;
import ps.getit.app.activities.RegUserActivity;
import ps.getit.app.activities.SplashActivity;
import ps.getit.app.models.delivery;
import ps.getit.app.utils.Utility;


public class HomeMapFragment extends Fragment {






    public static HomeMapFragment newInstance() {
        HomeMapFragment fragment = new HomeMapFragment();
        return fragment;
    }



    @BindView(R.id.mapView)
    MapView mMapView;





    private GoogleMap googleMap;
    float  zoomLevel = 18;

    boolean isFoundMyLocation=false;
    String lat,lng="";
    String query="";
    ArrayList<delivery> ordertrakinglist = new ArrayList<>();

    LatLng lastLocatoin;
    public ProgressDialog shwoz;
    Marker markerLast;
    Marker markerMy;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_map, container, false);
        ButterKnife.bind(this, view);

        isFoundMyLocation=false;
        Utility.setTitle(getString(R.string.home), (AppCompatActivity) getActivity());

        lat= Utility.getGenericStringFromPref(getActivity(), "lat","");
        lng= Utility.getGenericStringFromPref(getActivity(), "lng","");

        mMapView.onCreate(savedInstanceState);

        mMapView.onResume();

        try {
            MapsInitializer.initialize(getActivity());
        } catch (Exception e) {
            e.printStackTrace();
        }


        getRests();
        return view;

    }
    public AppCompatActivity getActivitty(){
        return (AppCompatActivity) getActivity();
    }

    public void getRests(){

        shwoz=new ProgressDialog();
        shwoz.setCancelable(false);
        shwoz.show(getActivitty().getSupportFragmentManager(),"dfdf");


            if (Utility.isConnectingToInternet(getActivity())) {


                StringRequest request = new StringRequest(Request.Method.POST, Utility.API_ROOT + "get_nearest_delivery", new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        shwoz.dismiss();
                        Log.i("view_delivery", response);
                        try {
                            JSONObject obj=new JSONObject(response);

                            if (Utility.checkRequestStatus(obj)) {
                                ordertrakinglist.clear();


                                ArrayList<delivery> ordest = new Gson().fromJson((obj.optJSONArray("data")).toString(), new TypeToken<ArrayList<delivery>>() {
                                }.getType());
                                ordertrakinglist.addAll(ordest);
                                //Go to Map for add
                                if (ordest.size() < 1) {
                                    Utility.showToast(R.string.no_result, false);
                                } else {
                                }
                                setMarker();

                            }else {
                                Utility.showToast(obj.optString("message"), false);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Utility.showToast(R.string.some_error, false);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        shwoz.dismissAllowingStateLoss();
                        error.printStackTrace();
                        Utility.showToast(R.string.connection_error, false);

                    }
                }) {

                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {

                        Map<String, String> params = new HashMap<>();
                         params.put("token", Utility.getStringFromPref(getActivitty(), "token"));




                        params.put("lang",Utility.getLangDefult(getActivity()));


                        if (!query.equals(""))
                            params.put("query",query );

                        if (lat!=null)
                            params.put("lat",lat);


                        if (lng!=null)
                            params.put("lng",lng);


                        return params;

                    }
                };

                App.getInstance().addToRequestQueue(request);
            } else {

                shwoz.dismissAllowingStateLoss();
                Utility.showErrorDialog(getActivity(), getString(R.string.no_internet));

            }

    }

    @OnClick(R.id.view_service)
    public void view_service(){

        if( !Utility.is_user_logged(getActivity())  || Utility.getBooleanFromPref( getActivity(), "test" , false ) ) {


            Intent ic = new Intent(getActivitty(), LoginActivity.class);
            startActivity(ic);
            getActivity().finish();
        }else {
            ServiceRequestFragment homeFragment = new ServiceRequestFragment();

            getFragmentManager().beginTransaction().replace(R.id.container, homeFragment).addToBackStack("").commit();
        }
    }
    public void setMarker() {

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                mMap.clear();
                googleMap = mMap;





             //   MyInfoWindowAdapter customInfoWindow= new MyInfoWindowAdapter(getActivity()); ;

                for (int i = 0; i <ordertrakinglist.size() ; i++) {

                    final LatLng locatoin = new LatLng(Double.parseDouble(ordertrakinglist.get(i).getLat()), Double.parseDouble(ordertrakinglist.get(i).getLng()));

                    lastLocatoin=new LatLng(Float.parseFloat(Utility.getStringFromPref(getActivity(),"lat",ordertrakinglist.get(0).getLat()+"")), Float.parseFloat(Utility.getStringFromPref(getActivity(),"lng",ordertrakinglist.get(0).getLng()+"")));
                    MarkerOptions markerOptions = new MarkerOptions();
                    BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.pin_delvary);

                    markerOptions.position(locatoin)
                            .title(ordertrakinglist.get(i).getFirstName())
                            .snippet(ordertrakinglist.get(i).getMobile())
                            .icon(icon);


                    Marker marker;
                    marker = googleMap.addMarker(markerOptions);
                    marker.setTag(ordertrakinglist.get(i));
                    marker.hideInfoWindow();


                    googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                        @Override
                        public boolean onMarkerClick(Marker marker) {


                            final delivery item = (delivery) marker.getTag();


                            if (marker.isInfoWindowShown()) {
                                marker.hideInfoWindow();
                            } else {
                                marker.showInfoWindow();
                            }
                            markerLast=marker;
                            return true;
                        }
                    });


                    //googleMap.setInfoWindowAdapter(customInfoWindow);





                }

                googleMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {

                    @Override
                    public void onMyLocationChange(Location arg0) {
                        // TODO Auto-generated method stub
                        if(!isFoundMyLocation) {
                            if (markerMy != null)
                                markerMy.remove();


                            lastLocatoin = new LatLng(arg0.getLatitude(), arg0.getLongitude());
                            MarkerOptions markerOptions = new MarkerOptions();
                            BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.pin_user);
                            markerOptions.position(lastLocatoin)
                                    .title("موقعي")
                                    .snippet(" ")
                                    .icon(icon);
                            markerMy = googleMap.addMarker(markerOptions);
                            CameraPosition cameraPosition = new CameraPosition.Builder().target(lastLocatoin).zoom(zoomLevel).build();
                            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                            isFoundMyLocation = true;
                        }
                    }
                });


                if(lastLocatoin!=null) {

             //       if(lat!=null && !lat.equals("")  ) {

                      //  lastLocatoin = new LatLng(Float.parseFloat(lat), Float.parseFloat(lng));
                       /* googleMap.addCircle(new CircleOptions()
                                .center(lastLocatoin)
                                .radius(2000f)
                                .strokeWidth(1f)
                                .fillColor(0x70273f7d));*/




                        MarkerOptions markerOptions = new MarkerOptions();
                        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.pin_user);

                        markerOptions.position(lastLocatoin)
                                .title("موقعي")
                                .snippet("")
                                .icon(icon);

                    markerMy = googleMap.addMarker(markerOptions);
                  //  }


                    CameraPosition cameraPosition = new CameraPosition.Builder().target(lastLocatoin).zoom(zoomLevel).build();
                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
//


                }
                googleMap.getUiSettings().setZoomControlsEnabled(true);



                setMyLocationEnabled();


                googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                    @Override
                    public void onInfoWindowClick(Marker marker) {
/*
                        final Market item = (Market) marker.getTag();
                        if(item.getIs_open().equals("1")){

                            Utility.addToPref(App.getInstance(),"rest_id",item.getId());
                            Utility.addToPref(App.getInstance(), "currency" , item.getCurrency() );
                            Utility.addToPref(App.getInstance(), "fee" , item.getFee() );
                            Utility.addToPref(App.getInstance(), "deliver_fee" , item.getDelivery_fee() );
                            Utility.addToPref(App.getInstance(), "Fee_threshold" , item.getFee_threshold() );
                            Utility.addToPref(App.getInstance(), "company" , item.getName() );


                            HomeFragment homeFragment= new HomeFragment();

                            MainActivity.fragmentManager.beginTransaction().replace(R.id.container,homeFragment).addToBackStack("").commit();
                        }else {
                            Utility.showErrorDialog(getActivity(),getString(R.string.msg_rest));
                        }


*/
                    }
                });
                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {



                    }
                });
                UiSettings uiSettings = googleMap.getUiSettings();
                uiSettings.setIndoorLevelPickerEnabled(true);
                uiSettings.setMyLocationButtonEnabled(true);
                uiSettings.setMapToolbarEnabled(true);
                uiSettings.setCompassEnabled(true);
                uiSettings.setZoomControlsEnabled(true);
            }
        });

    }

    private void setMyLocationEnabled() {

        List<String> permissionsNeeded2 = new ArrayList<String>();
        permissionsNeeded2.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
        permissionsNeeded2.add(android.Manifest.permission.ACCESS_COARSE_LOCATION);
        PermissionManager permissionManager2 = PermissionManager.getInstance(getActivity());
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

    @Override
    public void onDetach() {

        super.onDetach();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }


}
