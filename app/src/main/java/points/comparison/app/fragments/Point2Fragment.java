package points.comparison.app.fragments;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.NetworkError;
import com.android.volley.error.NoConnectionError;
import com.android.volley.error.ParseError;
import com.android.volley.error.ServerError;
import com.android.volley.error.TimeoutError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import points.comparison.app.App;
import points.comparison.app.Custom.CustomMapView;
import points.comparison.app.Dialogs.ProgressDialog;
import points.comparison.app.R;
import points.comparison.app.utils.Utility;

import static android.app.Activity.RESULT_OK;

/**
 * Created by techmix on 20/11/2015.
 */
public class Point2Fragment extends Fragment {
    private final static int REQUEST_ID_MULTIPLE_PERMISSIONS=0x2;

    boolean isFirest=true;


    String lat2="",lng2="";



    @BindView(R.id.mapView)
    CustomMapView mMapView;


    private GoogleMap googleMap;
    float  zoomLevel = 18;

    GoogleApiClient gac;
    int PLACE_PICKER_REQUEST = 22;


    String region1="",region2="";


    @Override
    public void onResume() {
        super.onResume();

        if (gac != null)
            gac.connect();
    }

    @Override
    public void onPause() {
        super.onPause();
        gac.stopAutoManage(getActivity());
        gac.disconnect();
    }
    /*      @Override
      public void onPause() {
          super.onPause();
          gac.stopAutoManage(getActivity());
          gac.disconnect();
      }
  */
    @Override
    public void onStop() {
        super.onStop();
        if (gac != null && gac.isConnected()) {
            gac.stopAutoManage(getActivitty());
            gac.disconnect();
        }
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public void onStart() {
        super.onStart();
       // mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        if (gac != null)
            gac.connect();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (gac == null || !gac.isConnected()){


        }




    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                               Bundle savedInstanceState) {

        View view = View.inflate(getContext(), R.layout.
                fragmnt_point2, null);

        ButterKnife.bind(this, view);

        gac = new GoogleApiClient
                .Builder(getActivity())
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(getActivity(), new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Utility.showToast("connection error in Google Map",false);

                        Log.d("mappp",connectionResult.getErrorMessage());
                    }
                })
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(@Nullable Bundle bundle) {

                        if (isFirest) {
                            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

                            try {
                                startActivityForResult(builder.build(getActivity()), PLACE_PICKER_REQUEST);

                                //startActivityForResult(builder.build(getActivity()), PLACE_PICKER_REQUEST);
                            } catch (GooglePlayServicesRepairableException e) {
                                Log.d("Map", "GooglePlayServicesRepairableException thrown");
                            } catch (GooglePlayServicesNotAvailableException e) {
                                Log.d("Map", "GooglePlayServicesNotAvailableException thrown");
                            }

                            isFirest=false;
                        }
                    }
                    @Override
                    public void onConnectionSuspended(int i) {

                    }
                })
                .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Log.d("mappp",connectionResult.getErrorMessage());

                    }
                })
                .build();





        try {
            mMapView.onCreate(savedInstanceState);
            mMapView.onResume();
            MapsInitializer.initialize(getActivity());
            iniMap();


        } catch (Exception e) {
            e.printStackTrace();
        }


        Bundle bundle = this.getArguments();
        if (bundle != null) {

            region1 =  bundle.getString("region1");


        }

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            if (requestCode == PLACE_PICKER_REQUEST) {
                googleMap.clear();
                Place place=   PlacePicker.getPlace(data, getActivity());
                lng2 = place.getLatLng().longitude + "";
                lat2 =  place.getLatLng().latitude  + "";
                LatLng sydney = new LatLng( place.getLatLng().latitude ,place.getLatLng().longitude);
                BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.pin_location);

                googleMap.addMarker(new MarkerOptions().position(sydney).visible(true).title("").snippet("").icon(icon));

                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(zoomLevel).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                //    displayPlace();


                Log.d("placeLast",lat2 +"  "+lng2);

            }

        }


    }


@OnClick(R.id.go_to_next)
public void btn_cancel(){

       // this.dismissAllowingStateLoss();
    if (lat2.isEmpty() || lng2.isEmpty()) {
        Utility.showToast("Please choose a location", false);

    }  else {

        requestGetgetRegion(lat2,lng2);
       // Regust(  TypeLocation);
    }
}



    private void requestGetgetRegion(String lat,String lng) {
        ProgressDialog.show((AppCompatActivity)getActivity());

        String url = Utility.API_ROOT+"getRegion/"+lat+"/"+lng;
        Log.d("paymentStatus", url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Region2", response);
                ProgressDialog.hide();

                region2=response.trim();

                ViewPropertiesFragment point1Fragment = new ViewPropertiesFragment();

                Bundle bundle =new Bundle();
                bundle.putString("region1",region1);
                bundle.putString("region2",region2);

                point1Fragment.setArguments(bundle);

                getFragmentManager().beginTransaction().replace(R.id.container, point1Fragment).addToBackStack("").commit();


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ProgressDialog.hide();

                error.printStackTrace();
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();

                String message = null;
                Log.d("onErrorResponse", error.toString() + "");
                String json = null;
                Log.d("error.getMessage()", error.getMessage() + "");
                if (error instanceof NetworkError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (error instanceof ServerError) {
                    message = "The server could not be found. Please try again after some time!!";
                } else if (error instanceof AuthFailureError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (error instanceof ParseError) {
                    message = "Parsing error! Please try again after some time!!";
                } else if (error instanceof NoConnectionError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (error instanceof TimeoutError) {
                    message = "Connection TimeOut! Please check your internet connection.";
                } else {

                    Toast.makeText(getActivity(),error.getMessage(), Toast.LENGTH_SHORT).show();

                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                // params.put("resourcePath",resourcePath);
                //Log.d("params.toString()", params.toString() + "");
                return checkParams(params);
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headersSys = super.getHeaders();
                Map<String, String> headers = new HashMap<String, String>();
                //  headers.put("Access-Token",token);
                headers.putAll(headersSys);
                Log.d("headersSys", headers.toString() + "");
                return headers;
            }

            private Map<String, String> checkParams(Map<String, String> map) {
                Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<String, String> pairs = (Map.Entry<String, String>) it.next();
                    if (pairs.getValue() == null) {
                        map.put(pairs.getKey(), "");
                    }
                }
                return map;
            }

        };

        App.getInstance().addToRequestQueue(stringRequest);



    }



    public void iniMap(){
        try {



            mMapView.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap mMap) {
                    mMap.clear();
                    googleMap = mMap;
                    googleMap.getUiSettings().setZoomControlsEnabled(true);

                    if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                            PackageManager.PERMISSION_GRANTED &&
                            ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) ==
                                    PackageManager.PERMISSION_GRANTED) {
                       // googleMap.setMyLocationEnabled(true);
                    } else {
                        ActivityCompat.requestPermissions(getActivity(), new String[] {
                                        android.Manifest.permission.ACCESS_FINE_LOCATION,
                                        android. Manifest.permission.ACCESS_COARSE_LOCATION },
                                REQUEST_ID_MULTIPLE_PERMISSIONS);
                    }
// For dropping a marker at a point on the Map

                    //  zoomLevel=googleMap.getCameraPosition().zoom;

                    googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                        @Override
                        public void onMapClick(LatLng latLng) {

                            if (gac == null || !gac.isConnected()){

                                Utility.showToast("connection error in Google Map",false);
                                return;
                            }
                            ;

                            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

                            try {




                                startActivityForResult(builder.build(getActivity()), PLACE_PICKER_REQUEST);

                                //startActivityForResult(builder.build(getActivity()), PLACE_PICKER_REQUEST);
                            } catch (GooglePlayServicesRepairableException e) {
                                Log.d("Map", "GooglePlayServicesRepairableException thrown");
                            } catch (GooglePlayServicesNotAvailableException e) {
                                Log.d("Map", "GooglePlayServicesNotAvailableException thrown");
                            }
                      //      if(TypeLocation==1) {

                      /*  googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
                                latLng.latitude, latLng.longitude), zoomLevel));*/

                           // }
                        }
                    });

                   /* lng = lng + "0.0";
                    lat =lat + "0.0";
                    LatLng sydney = new LatLng(Double.parseDouble(lat),Double.parseDouble(lng));
                    BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.pin_location);
                    googleMap.clear();
                    googleMap.addMarker(new MarkerOptions().position(sydney).visible(true).title("").snippet("").icon(icon));

// For zooming automatically to the location of the marker
                    CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(zoomLevel).build();
                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
*/
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public AppCompatActivity getActivitty(){
        return (AppCompatActivity) getActivity();
    }

}


