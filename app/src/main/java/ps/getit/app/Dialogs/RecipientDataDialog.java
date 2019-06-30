package ps.getit.app.Dialogs;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

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
import ps.getit.app.R;
import ps.getit.app.activities.MainActivity;
import ps.getit.app.models.cities;
import ps.getit.app.utils.Utility;

import android.support.design.widget.BottomSheetDialog;

import static android.app.Activity.RESULT_OK;

/**
 * Created by techmix on 20/11/2015.
 */
public class RecipientDataDialog extends BottomSheetDialogFragment  {
    private final static int REQUEST_ID_MULTIPLE_PERMISSIONS=0x2;

    ArrayList<OnItemSelectedListener> listener=new ArrayList<>();
    TextView tv;

    String order_id="";

    String mobile="",name="",address="",section_id="",city_id="",lat="",lng="",weight="",details="",coupon="";
    int TypeLocation;


    @BindView(R.id.name)
    EditText receiver_name;

    @BindView(R.id.mobile)
    EditText receiver_mobile;


    @BindView(R.id.address)
    EditText receiver_address;


    public  String receiver_lng="";
    public  String receiver_lat="";


    ArrayList<cities> list_countries =new  ArrayList<cities>();
    List<String> list_City = new ArrayList<String>();
    List<String> list_Sections = new ArrayList<String>();

    int SectionsPositionSp=0,CityPositionSp=0;


    @BindView(R.id.contry)
    Spinner citiesSp;

    @BindView(R.id.cityy)
    Spinner sectionsSp;

    @BindView(R.id.mapView)
    CustomMapView mMapView;

    int c_lang;

    private GoogleMap googleMap;
    float  zoomLevel = 18;

    private BottomSheetBehavior mBehavior;
    GoogleApiClient gac;
    int PLACE_PICKER_REQUEST = 22;



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
        mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        if (gac != null)
            gac.connect();
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);

        View view = View.inflate(getContext(), R.layout.dialog_recipient_data, null);

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



        c_lang = Utility.getIntFromPref(getActivity() , "lang"  );
        if (c_lang==-1)c_lang=1;


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

            mobile = bundle.getString("mobile");
            name = bundle.getString("name");
            address = bundle.getString("address");
            section_id = bundle.getString("section_id");
            city_id = bundle.getString("city_id");
            lat = bundle.getString("lat");
            lng = bundle.getString("lng");
            weight = bundle.getString("weight");
            details = bundle.getString("details");
            TypeLocation= bundle.getInt("TypeLocation");
            coupon= bundle.getString("coupon");



        }


        getListCountries();
        dialog.setContentView(view);
        mBehavior = BottomSheetBehavior.from((View) view.getParent());

        return dialog;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            if (requestCode == PLACE_PICKER_REQUEST) {
                googleMap.clear();
                Place place=   PlacePicker.getPlace(data, getActivity());
                receiver_lng = place.getLatLng().longitude + "";
                receiver_lat =  place.getLatLng().latitude  + "";
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
    public void getListCountries(){


        list_City=new ArrayList<>();

        list_countries=new ArrayList<>();
        list_City=new ArrayList<>();
        citiesSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
                    sectionsSp.setAdapter(adapter_list_city);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sectionsSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                SectionsPositionSp=position;

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

                        ArrayList<cities> countries = new Gson().fromJson(jsonObject.optJSONArray("cities").toString(),new TypeToken<ArrayList<cities>>() {
                        }.getType());

                        list_countries.add(new cities( "اختر المدينة","Choose Country "));
                        list_countries.addAll(countries);

                        for (int j = 0; j < list_countries.size(); j++) {
                            list_City.add(c_lang==1?list_countries.get(j).getName():list_countries.get(j).getName_en());
                        }

                        ArrayAdapter<String> adapter_list_countries = new ArrayAdapter<String>(getActivity(),  R.layout.simple_spinner, list_City);
                        citiesSp.setAdapter(adapter_list_countries);



                        list_Sections.clear();
                        list_Sections.add(c_lang == 1 ? "اختر الحي" : "Choose City");
                        ArrayAdapter<String> adapter_list_city = new ArrayAdapter<String>(getActivity(), R.layout.simple_spinner, list_Sections);
                        sectionsSp.setAdapter(adapter_list_city);



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


@OnClick(R.id.go_to_next)
public void btn_cancel(){

       // this.dismissAllowingStateLoss();
    if (receiver_name.getText().toString().isEmpty()) {
        receiver_name.setError("أدخل الاسم");
    } else if (receiver_mobile.getText().toString().isEmpty()) {
        receiver_mobile.setError("أدخل رقم المحمول");
    } else if (receiver_address.getText().toString().isEmpty()) {
        receiver_address.setError("أدخل العنوان");
    } else if (CityPositionSp == 0) {
        Utility.showToast("الرجاء اختيار المدينة", false);
    } else if (SectionsPositionSp == 0) {
        Utility.showToast("الرجاء اختيار الحي", false);
    } else {


        Regust(  TypeLocation);
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
                      //      if(TypeLocation==1) {

                      /*  googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
                                latLng.latitude, latLng.longitude), zoomLevel));*/

                           // }
                        }
                    });

                    receiver_lng = lng + "";
                    receiver_lat =lat + "";
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

 public void setOnItemSelectedListener(OnItemSelectedListener listener){

     this.listener.add(listener);
 }

    public interface  OnItemSelectedListener{

    public void onItemSelected(String cur, float hh);
    }




    public void Regust(int typeLocation){

        HashMap params = new HashMap();


     //   if(typeLocation==1){

            params.put("mobile", mobile);
            params.put("name",name );
            params.put("address", address);
            params.put("section_id",section_id );
            params.put("city_id",city_id);
            params.put("lat", lat);
            params.put("lng", lng);

      //  }


        if (!coupon.isEmpty())
            params.put("coupon",coupon);


        params.put("weight",weight);


        params.put("details",details);
        params.put("receiver_section_id",list_countries.get(CityPositionSp).getSections().get(SectionsPositionSp-1).getId() );
        params.put("receiver_city_id",list_countries.get(CityPositionSp).getId() );

        params.put("receiver_lng", receiver_lng);
        params.put("receiver_lat", receiver_lat);
        params.put("receiver_address", receiver_address.getText().toString());
        params.put("receiver_mobile", receiver_mobile.getText().toString());
        params.put("receiver_name", receiver_name.getText().toString());

        params.put("type",""+typeLocation);




        new App.CustomRequest(getActivitty(),"request", params) {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                Utility.showToast(jsonObject.optString("message"), true);


                ((MainActivity) getActivitty()).goToMyOrder();
                dismissAllowingStateLoss();
            }


        };
    }
    public AppCompatActivity getActivitty(){
        return (AppCompatActivity) getActivity();
    }

}


