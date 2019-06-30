package ps.getit.app.fragments;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ps.getit.app.App;
import ps.getit.app.Custom.CustomMapView;
import ps.getit.app.Dialogs.RecipientDataDialog;
import ps.getit.app.R;
import ps.getit.app.models.cities;
import ps.getit.app.utils.Utility;

import static android.app.Activity.RESULT_OK;

public class ServiceRequestFragment extends Fragment {

    private final static int REQUEST_ID_MULTIPLE_PERMISSIONS=0x2;

    @BindView(R.id.my_location)
    Button my_location;

    @BindView(R.id.location_ouher)
    Button location_ouher;

    @BindView(R.id.name)
    EditText name;

    @BindView(R.id.mobile)
    EditText mobile;

    @BindView(R.id.city)
    Spinner citySp;

    @BindView(R.id.section)
    Spinner sectionSp;

    @BindView(R.id.address)
    EditText address;

    @BindView(R.id.coupon)
    EditText coupon;

    @BindView(R.id.details)
    EditText details;

    @BindView(R.id.mapView)
    CustomMapView mMapView;

    @BindView(R.id.radioGroup)
    RadioGroup radioGroup;

    String weight="0";
    private GoogleMap googleMap;
    float  zoomLevel = 18;

    public  String lng="";
    public  String lat="";

    int PLACE_PICKER_REQUEST = 22;

    boolean isFoundMyLocation=false;


    ArrayList<cities> list_countries =new  ArrayList<cities>();
    List<String> list_City = new ArrayList<String>();
    List<String> list_Sections = new ArrayList<String>();

    String section_id="00",city_id="00";


//String receiver_name="",receiver_mobile="",receiver_address="",receiver_lat="",receiver_lng="",receiver_city_id="",receiver_section_id="";

    int SectionsPositionSp=0,CityPositionSp=0;
    int c_lang;

    int TypeLocation=0;

    GoogleApiClient gac;
    @Override
    public void onStart() {
        super.onStart();
        if (gac != null)
            gac.connect();
    }

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



    public AppCompatActivity getActivitty(){
        return (AppCompatActivity) getActivity();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_service_request, container, false);

        ButterKnife.bind(this, view);
        gac = new GoogleApiClient
                .Builder(getActivity())
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(getActivity(), new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                        Log.d("mappp",connectionResult.getErrorMessage());
                    }
                })
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(@Nullable Bundle bundle) {

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

      //  gac.connect();


        c_lang = Utility.getIntFromPref(getActivity() , "lang"  );
        if (c_lang==-1)c_lang=1;

       /* citySp.setEnabled(false);
        sectionSp.setEnabled(false);*/

        name.setEnabled(true);
        mobile.setEnabled(true);
        address.setEnabled(true);
        citySp.setEnabled(true);
        sectionSp.setEnabled(true);

        name.setText( Utility.getStringFromPref(getActivity(),"firstName"));
        mobile.setText( Utility.getStringFromPref(getActivity(),"mobile"));
        address.setText(Utility.getStringFromPref(getActivity(),"address"));

        lat= Utility.getGenericStringFromPref(getActivity(), "lat","");
        lng= Utility.getGenericStringFromPref(getActivity(), "lng","");

        section_id=Utility.getStringFromPref(getActivity(),"section_id","00") ;
        city_id=Utility.getStringFromPref(getActivity(),"city_id","00") ;

        try {
            mMapView.onCreate(savedInstanceState);
            mMapView.onResume();
            MapsInitializer.initialize(getActivity());
            iniMap();


        } catch (Exception e) {
            e.printStackTrace();
        }

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch(checkedId) {
                    case R.id.light:
                        weight="0";
                        break;
                    case R.id.heavy:
                        weight="1";
                            break;
                    case R.id.open:
                        weight="2";
                            break;
                }

            }
        });

        getListCountries();
        return view;
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            if (requestCode == PLACE_PICKER_REQUEST) {
                googleMap.clear();
                Place place=   PlacePicker.getPlace(data, getActivity());
                lng = place.getLatLng().longitude + "";
                lat =  place.getLatLng().latitude  + "";
                LatLng sydney = new LatLng( place.getLatLng().latitude ,place.getLatLng().longitude);
                BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.pin_user);

                googleMap.addMarker(new MarkerOptions().position(sydney).visible(true).title("").snippet("").icon(icon));

                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(zoomLevel).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            //    displayPlace();

            }

        }


    }

    @OnClick(R.id.my_location)
    public void my_location(){
        TypeLocation=0;
        name.setText( Utility.getStringFromPref(getActivity(),"firstName"));
        mobile.setText( Utility.getStringFromPref(getActivity(),"mobile"));
        address.setText(Utility.getStringFromPref(getActivity(),"address"));
       /* citySp.setEnabled(false);
        sectionSp.setEnabled(false);

        name.setEnabled(false);
        mobile.setEnabled(false);
        address.setEnabled(false);*/

        name.setEnabled(true);
        mobile.setEnabled(true);
        address.setEnabled(true);
        citySp.setEnabled(true);
        sectionSp.setEnabled(true);

        getListCountries();
        my_location.setTextColor(getResources().getColor(R.color.white));
        my_location.setBackgroundResource(R.drawable.bacbluel);

        location_ouher.setTextColor(getResources().getColor(R.color.colorPrimary));
        location_ouher.setBackgroundResource(R.color.transparent);
    }
    @OnClick(R.id.location_ouher)
    public void location_ouher(){

        TypeLocation=1;

        name.setText( "");
        mobile.setText("");
        address.setText("");
        name.setEnabled(true);
        mobile.setEnabled(true);
        address.setEnabled(true);
        citySp.setEnabled(true);
        sectionSp.setEnabled(true);
        citySp.setSelection(0);
        CityPositionSp=0;
        location_ouher.setTextColor(getResources().getColor(R.color.white));
        location_ouher.setBackgroundResource(R.drawable.bacbluel);

        my_location.setTextColor(getResources().getColor(R.color.colorPrimary));
        my_location.setBackgroundResource(R.color.transparent);
    }


    public void getListCountries(){



        citySp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                list_Sections.clear();
                CityPositionSp = position;
                list_Sections.add(c_lang == 1 ? "اختر الحي" : "Choose City");

                if (list_countries.get(position).getSections() != null) {
                    for (int j = 0; j < list_countries.get(position).getSections().size(); j++) {
                        list_Sections.add(c_lang == 1 ? list_countries.get(position).getSections().get(j).getName() : list_countries.get(position).getSections().get(j).getName_en());
                    }

                    ArrayAdapter<String> adapter_list_city = new ArrayAdapter<String>(getActivity(), R.layout.simple_spinner, list_Sections);
                    sectionSp.setAdapter(adapter_list_city);

                    for (int j = 0; j <list_countries.get(position).getSections().size() ; j++) {
                        Log.d("ciy_id",list_countries.get(position).getSections().get(j).getId()+"  "+city_id);
                        if(section_id.equals(list_countries.get(position).getSections().get(j).getId())){
                            int o=j+1;
                            sectionSp.setSelection(o);
                        }
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sectionSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                SectionsPositionSp=position;
                Log.d("dddddddddd",position+"");

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if (Utility.isConnectingToInternet(getActivity())) {


            StringRequest request = new StringRequest(Request.Method.POST, Utility.API_ROOT + "getGeneralData", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {


                    Log.i("response", response);
                    try {

                        JSONObject jsonObject = new JSONObject(response);

                        list_countries.clear();
                        list_City.clear();


                        ArrayList<cities> countries = new Gson().fromJson(jsonObject.optJSONArray("cities").toString(), new TypeToken<ArrayList<cities>>() {
                        }.getType());


                        list_countries.add(new cities("اختر المدينة", "Choose Country "));
                        list_countries.addAll(countries);
                        for (int j = 0; j < list_countries.size(); j++) {
                            list_City.add(c_lang == 1 ? list_countries.get(j).getName() : list_countries.get(j).getName_en());
                        }


                        ArrayAdapter<String> adapter_list_countries = new ArrayAdapter<String>(getActivity(), R.layout.simple_spinner, list_City);
                    citySp.setAdapter(adapter_list_countries);


                        list_Sections.clear();
                        list_Sections.add(c_lang == 1 ? "اختر الحي" : "Choose City");
                        ArrayAdapter<String> adapter_list_city = new ArrayAdapter<String>(getActivity(), R.layout.simple_spinner, list_Sections);
                                sectionSp.setAdapter(adapter_list_city);

                        for (int i = 0; i < list_countries.size() ; i++) {
                            if(city_id.equals(list_countries.get(i).getId())){
                                citySp.setSelection(i);
                                CityPositionSp = i;

                            }


                        }




                    } catch (JSONException e) {
                        e.printStackTrace();
                        Utility.showToast(getString(R.string.some_error), false);
                    }

                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    Utility.showToast(R.string.connection_error, false);

                }
            }) {

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    Map<String, String> params = new HashMap<>();
                    params.put("lang",Utility.getLangDefult(getActivity()));

               /* params.put("password", pass);
                params.put("email",email);
                params.put("mobile", mobile);
*/
                    return params;

                }
            };

            App.getInstance().addToRequestQueue(request);
        }

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
                        googleMap.setMyLocationEnabled(true);
                    } else {
                        ActivityCompat.requestPermissions(getActivity(), new String[] {
                                        android.Manifest.permission.ACCESS_FINE_LOCATION,
                                        android. Manifest.permission.ACCESS_COARSE_LOCATION },
                                REQUEST_ID_MULTIPLE_PERMISSIONS);
                    }
// For dropping a marker at a point on the Map

                    //  zoomLevel=googleMap.getCameraPosition().zoom;

                    googleMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {

                        @Override
                        public void onMyLocationChange(Location arg0) {
                            // TODO Auto-generated method stub
                            if(!isFoundMyLocation) {
                                googleMap.clear();

                                lng = arg0.getLongitude() + "";
                                lat = arg0.getLatitude() + "";
                                LatLng lastLocatoin = new LatLng(arg0.getLatitude(), arg0.getLongitude());
                                MarkerOptions markerOptions = new MarkerOptions();
                                BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.pin_user);
                                markerOptions.position(lastLocatoin)
                                        .title("موقعي")
                                        .snippet(" ")
                                        .icon(icon);
                                 googleMap.addMarker(markerOptions);
                                CameraPosition cameraPosition = new CameraPosition.Builder().target(lastLocatoin).zoom(zoomLevel).build();
                                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                                isFoundMyLocation = true;
                            }
                        }
                    });
                    googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                        @Override
                        public void onMapClick(LatLng latLng) {
                            if (gac == null || !gac.isConnected()){

                                Utility.showToast("هناء خطأ في اتصال خريطة جوجل",false);
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
                        //    if(TypeLocation==1) {


                      /*  googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
                                latLng.latitude, latLng.longitude), zoomLevel));*/

                          //  }
                        }
                    });


                    LatLng sydney = new LatLng(Double.parseDouble(lat),Double.parseDouble(lng));
                    BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.pin_user);
                    googleMap.clear();
                    googleMap.addMarker(new MarkerOptions().position(sydney).visible(true).title("").snippet("").icon(icon));

                    // For zooming automatically to the location of the marker
                    CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(zoomLevel).build();
                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @OnClick(R.id.go_to_next)
    public void go_to_next() {

        if (gac != null && gac.isConnected()) {
            gac.stopAutoManage(getActivitty());
            gac.disconnect();

        }
        if(TypeLocation==0){

            if (name.getText().toString().isEmpty()) {
                name.setError("أدخل الاسم");
            } else if (mobile.getText().toString().isEmpty()) {
                mobile.setError("أدخل رقم المحمول");
            } else if (address.getText().toString().isEmpty()) {
                address.setError("أدخل العنوان");
            } else if (CityPositionSp == 0) {
                Utility.showToast("الرجاء اختيار المدينة", false);
            } else if (SectionsPositionSp == 0) {
                Utility.showToast("الرجاء اختيار الحي", false);
            } else if (details.getText().toString().isEmpty()) {
                details.setError("الرجاء أدخل التفاصيل");
            }else {
                showDilog();
            }

        }else {
            if (details.getText().toString().isEmpty()) {
                address.setError("الرجاء أدخل التفاصيل");
            }else {
                showDilog();
            }
        }




    }
    public void showDilog(){
        RecipientDataDialog dataDialog =new RecipientDataDialog();

        Bundle bundle =new Bundle();
        bundle.putString("mobile",mobile.getText().toString());
        bundle.putString("address",address.getText().toString());
        bundle.putString("name",name.getText().toString());
        bundle.putString("section_id",list_countries.get(CityPositionSp).getSections().get(SectionsPositionSp-1).getId() );
        bundle.putString("city_id",list_countries.get(CityPositionSp).getId() );

        bundle.putString("coupon",coupon.getText().toString()+"");


        bundle.putString("lat",lat);
        bundle.putString("lng",lng);
        bundle.putString("weight",weight);
        bundle.putString("details",details.getText().toString());
        bundle.putInt("TypeLocation",TypeLocation);

        dataDialog.setArguments(bundle);
        dataDialog.show(getFragmentManager(),"sefsdc");

    }

    public boolean receiverIsEmpty(String receiver_name,String receiver_mobile,String receiver_address,String receiver_lat,String receiver_lng,String receiver_city_id,String receiver_section_id){


        boolean flag = false;

        if (receiver_name.isEmpty()  || receiver_mobile.isEmpty() ||receiver_address.isEmpty()  ||receiver_lat.isEmpty() ||receiver_lng.isEmpty() ||receiver_city_id.isEmpty() ||receiver_section_id.isEmpty() ) {
            flag = false;
        } else {

            flag = true;

        }


        return flag;
    }



}
