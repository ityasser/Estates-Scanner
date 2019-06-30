package ps.getit.app.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
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
import ps.getit.app.Custom.CustomMapView;
import ps.getit.app.Dialogs.CodeDialog;
import ps.getit.app.R;
import ps.getit.app.models.cities;
import ps.getit.app.utils.Utility;

public class RegUserActivity extends BaseActivity {
    @BindView(R.id.name)
    EditText name;

    @BindView(R.id.mobile)
    EditText mobile;

    @BindView(R.id.address)
    EditText address;

    @BindView(R.id.PassUser)
    EditText PassUser;


    @BindView(R.id.id_no)
    EditText id_no;


    @BindView(R.id.contry)
    Spinner citiesSp;

    @BindView(R.id.cityy)
    Spinner sectionsSp;

    @BindView(R.id.radioSex)
    RadioGroup radioSex;

    @BindView(R.id.mapView)
    CustomMapView mMapView;

    String Gender="male";
    int SectionsPositionSp=0,CityPositionSp=0;
    int c_lang;

    ArrayList<cities> list_countries =new  ArrayList<cities>();
    List<String> list_City = new ArrayList<String>();
    List<String> list_Sections = new ArrayList<String>();



    private GoogleMap googleMap;
    float  zoomLevel = 18;

    public  String lng="";
    public  String lat="";
    @BindView(R.id.radioMale)
    RadioButton radioMale;

    @BindView(R.id.type_car)
    RadioGroup type_car;

    @BindView(R.id.user)
    Button user;

    @BindView(R.id.delivery)
    Button delivery;

    int typeUser=0;
String typeCar="0";
    String fun="register";

    String invitation_code="";
    private final static int REQUEST_ID_MULTIPLE_PERMISSIONS=0x2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_user);

        ButterKnife.bind(this);
        Utility.setTitle("تسجيل جديد",this);



/*
        Uri data = getIntent().getData();
        String scheme = data.getScheme(); // "http"
        String host = data.getHost(); // "twitter.com"
        List<String> params = data.getPathSegments();
        String first = params.get(0); // "status"
        String second = params.get(1); // "1234"
                Utility.showToast(second,true);

*/
        c_lang = Utility.getIntFromPref(RegUserActivity.this , "lang"  );
        if (c_lang==-1)c_lang=1;

        try {


            mMapView.onCreate(savedInstanceState);
            mMapView.onResume();
            MapsInitializer.initialize(getApplicationContext());
            mMapView.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap mMap) {
                    mMap.clear();
                    googleMap = mMap;
                    if (ContextCompat.checkSelfPermission(RegUserActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                            PackageManager.PERMISSION_GRANTED &&
                            ContextCompat.checkSelfPermission(RegUserActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) ==
                                    PackageManager.PERMISSION_GRANTED) {
                        googleMap.setMyLocationEnabled(true);
                    } else {
                        ActivityCompat.requestPermissions(RegUserActivity.this, new String[] {
                                        android.Manifest.permission.ACCESS_FINE_LOCATION,
                                        android. Manifest.permission.ACCESS_COARSE_LOCATION },
                                REQUEST_ID_MULTIPLE_PERMISSIONS);
                    }
                    //  zoomLevel=googleMap.getCameraPosition().zoom;
                    googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                        @Override
                        public void onMapClick(LatLng latLng) {
                            googleMap.clear();
                            lng=latLng.longitude+"";
                            lat= latLng.latitude+"";
                            LatLng sydney = new LatLng( latLng.latitude, latLng.longitude);
                            BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.pin_user);
                            googleMap.addMarker(new MarkerOptions().position(sydney).visible(true).title("").snippet("").icon(icon));
                            CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(zoomLevel).build();
                            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                      /*  googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
                                latLng.latitude, latLng.longitude), zoomLevel));*/

                            googleMap.getUiSettings().setZoomControlsEnabled(true);
                        }
                    });

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }



        type_car.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch(checkedId) {
                    case R.id.motor:
                        typeCar="0";
                        break;
                    case R.id.car:
                        typeCar="1";
                        break;
                    case R.id.truck:
                        typeCar="2";
                        break;
                }

            }
        });


        getLocation();
        getListCountries();


    }



    @OnClick(R.id.user)
    public void my_location(){
        typeUser=0;
        fun="register";
        radioSex.setVisibility(View.VISIBLE);
        id_no.setVisibility(View.GONE);
        type_car.setVisibility(View.GONE);

        user.setTextColor(getResources().getColor(R.color.white));
        user.setBackgroundResource(R.drawable.bacbluel);

        delivery.setTextColor(getResources().getColor(R.color.colorPrimary));
        delivery.setBackgroundResource(R.color.transparent);


    }
    @OnClick(R.id.delivery)
    public void location_ouher(){

        typeUser=1;
        fun="create_delivery_account";
        radioSex.setVisibility(View.GONE);
        id_no.setVisibility(View.VISIBLE);

        delivery.setTextColor(getResources().getColor(R.color.white));
        delivery.setBackgroundResource(R.drawable.bacbluel);

        user.setTextColor(getResources().getColor(R.color.colorPrimary));
        user.setBackgroundResource(R.color.transparent);

        type_car.setVisibility(View.VISIBLE);


    }
    @OnClick(R.id.reg_frind)
    public void reg_frind()  {
        CodeDialog vv=new CodeDialog();
        vv.setOnItemSelectedListener(new CodeDialog.OnItemSelectedListener() {
            @Override
            public void onItemSelected(String cur) {
                invitation_code=cur;
            }
        });
        vv.show(getSupportFragmentManager(),"sdcas");
    }



    @OnClick(R.id.go_to_next)
    public void go_to_next()  {

        if (name.getText().toString().isEmpty()) {
            name.setError("أدخل الاسم");
        }else if (mobile.getText().toString().isEmpty()) {
            mobile.setError("أدخل رقم المحمول");
        } else if (PassUser.getText().toString().isEmpty()) {
            PassUser.setError("أدخل كلمة المرور");
        } else if (address.getText().toString().isEmpty()) {
            address.setError("أدخل العنوان");
        }else  if (CityPositionSp == 0) {
            Utility.showToast("الرجاء اختيار المدينة", false);
        } else if (SectionsPositionSp == 0) {
            Utility.showToast("الرجاء اختيار الحي", false);
        } else if (id_no.getText().toString().isEmpty() && typeUser==1) {
            id_no.setError("أدخل رقم الهوية");

        } else {



            HashMap params = new HashMap();

            params.put("password", PassUser.getText().toString());
            params.put("mobile", mobile.getText().toString());
            params.put("name",name.getText().toString() );
            params.put("firstName",name.getText().toString() );


            params.put("address", address.getText().toString());

            if(typeUser==0){
                if(radioMale.isChecked()){
                    params.put("gender","female" );
                }else {
                    params.put("gender","male" );
                }
            }else {
                params.put("id_no", id_no.getText().toString());
                params.put("type", typeCar);


            }
            params.put("lat", lat);
            params.put("lng", lng);
            params.put("lang",Utility.getLangDefult(RegUserActivity.this));

            params.put("section_id",list_countries.get(CityPositionSp).getSections().get(SectionsPositionSp-1).getId() );
            params.put("city_id",list_countries.get(CityPositionSp).getId() );

            if(!invitation_code.isEmpty())
                params.put("invitation_code", invitation_code);



            new App.CustomRequest(this,fun, params) {
                @Override
                public void onSuccess(JSONObject jsonObject) {
                    Utility.showToast(jsonObject.optString("message"), true);
                    Utility.addGenericToPrefUser(RegUserActivity.this, "username",mobile.getText().toString());
                    Utility.addGenericToPrefUser(RegUserActivity.this, "password", PassUser.getText().toString());
                    //   startActivity( new Intent(RegUserActivity.this , LoginActivity.class));
                    Intent resultIntent = new Intent();

                    resultIntent.putExtra("username",mobile.getText().toString() );
                    resultIntent.putExtra("pass",PassUser.getText().toString());

                    setResult(Activity.RESULT_OK, resultIntent);


                    finish();

                }


            };


        }




    }
    public void getLocation() {
        if (!Utility.isGPSEnabled(RegUserActivity.this)){
            Utility.showSettingsAlert(RegUserActivity.this);
        } else {
            beginUpdates();

            //    fusedLocation.getLastKnownLocation(60000, 5);
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

                    ArrayAdapter<String> adapter_list_city = new ArrayAdapter<String>(RegUserActivity.this, R.layout.simple_spinner, list_Sections);
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




        if (Utility.isConnectingToInternet(RegUserActivity.this)) {

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

                        ArrayAdapter<String> adapter_list_countries = new ArrayAdapter<String>(RegUserActivity.this,  R.layout.simple_spinner, list_City);
                        citiesSp.setAdapter(adapter_list_countries);



                        list_Sections.clear();
                        list_Sections.add(c_lang == 1 ? "اختر الحي" : "Choose City");
                        ArrayAdapter<String> adapter_list_city = new ArrayAdapter<String>(RegUserActivity.this, R.layout.simple_spinner, list_Sections);
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
                    params.put("lang",Utility.getLangDefult(RegUserActivity.this));

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

    public void  beginUpdates(){
        List<String> permissionsNeeded2 = new ArrayList<String>();
        permissionsNeeded2.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
        permissionsNeeded2.add(android.Manifest.permission.ACCESS_COARSE_LOCATION);
        PermissionManager permissionManager2 = PermissionManager.getInstance(RegUserActivity.this);
        permissionManager2.checkPermissions(permissionsNeeded2, new PermissionManager.PermissionRequestListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onPermissionGranted() {

                Log.i("location","PermissionGranted user");

                SmartLocation.with(RegUserActivity.this).location()
                        .oneFix()
                        .start(new OnLocationUpdatedListener() {
                            @Override
                            public void onLocationUpdated(Location location) {
                                Log.i("location","onActivityResult Locationxxx " + location.getLatitude() + " , " +location.getLongitude());
                                if (location != null) {
                                    lat=location.getLatitude()+"";
                                    lng=location.getLongitude()+"";
                                    setLocation(location.getLatitude() ,location.getLongitude());
                                    Log.i("location","onLocationResult" + location.getLatitude() + " , " +location.getLongitude());
                                    SmartLocation.with(RegUserActivity.this).location().stop();

                                }

                            }});


            }
            @Override
            public void onPermissionDenied() {

            }
        });
    }


    private void setLocation(double Latt,double lngg) {

        LatLng sydney = new LatLng(Latt,lngg);
        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.pin_user);
        googleMap.clear();
        googleMap.addMarker(new MarkerOptions().position(sydney).visible(true).title("").snippet("").icon(icon));

// For zooming automatically to the location of the marker
        CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(zoomLevel).build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        /*
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
                location.getLatitude(), location.getLongitude()), zoomLevel));
                */
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.i("result",requestCode+" / "+resultCode +" / "+ data);

        if (Utility.isGPSEnabled(RegUserActivity.this)){
            //   md.show(getFragmentManager(),"tt");
            beginUpdates();
            Log.d("onActivityResult","fusedLocation");
        }else {
            //  Utility.showSettingsAlert(com.mms.menuapp.CartActivity.this);

        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
