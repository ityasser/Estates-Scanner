package ps.getit.app.fragments;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.request.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
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
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;
import com.scottyab.showhidepasswordedittext.ShowHidePasswordEditText;
import com.yanzhenjie.album.Action;
import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.AlbumFile;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.ParseException;
import de.hdodenhof.circleimageview.CircleImageView;
import ps.getit.app.App;
import ps.getit.app.Custom.CustomMapView;
import ps.getit.app.Dialogs.ProgressDialog;
import ps.getit.app.R;
import ps.getit.app.activities.MainActivity;
import ps.getit.app.models.cities;
import ps.getit.app.utils.Utility;


public class ProfileUserFragment extends Fragment {


    public static ProfileUserFragment newInstance() {
        ProfileUserFragment fragment = new ProfileUserFragment();
        return fragment;
    }


    @BindView(R.id.logo)
    CircleImageView img_prfile;


    @BindView(R.id.tx_edit)
    TextView tx_edit;




    @BindView(R.id.city)
    Spinner citySp;

    @BindView(R.id.section)
    Spinner sectionSp;


    @BindView(R.id.mapView)
    CustomMapView mMapView;


    @BindView(R.id.name)
    EditText name;

    @BindView(R.id.mobile)
    EditText mobile;



    @BindView(R.id.address)
    EditText address;


    @BindView(R.id.PassUser)
    ShowHidePasswordEditText PassUser;




    String imgPrfile="";
    String Gender="male";
    int SectionsPositionSp=0,CityPositionSp=0;
    int c_lang;


    ArrayList<cities> list_countries =new  ArrayList<cities>();
    List<String> list_City = new ArrayList<String>();
    List<String> list_Sections = new ArrayList<String>();

    String section_id="00",city_id="00";

    private final static int REQUEST_ID_MULTIPLE_PERMISSIONS=0x2;

    private GoogleMap googleMap;
    float  zoomLevel = 18;
    Location locationg=null;

    public String lng="";
    public String lat="";

    @BindView(R.id.type_car)
    RadioGroup type_car;

    @BindView(R.id.id_no)
    EditText id_no;

    @BindView(R.id.motor)
    RadioButton motor;

    @BindView(R.id.car)
    RadioButton car;

    @BindView(R.id.truck)
    RadioButton truck;

    String typeCar="0",is_admin="0";

    @BindView(R.id.switchForActionBar)
    SwitchCompat switchAB;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view= inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, view);

        if (Utility.getStringFromPref(getActivitty(), "is_admin").equals("1")) {
            switchAB.setVisibility(View.VISIBLE);

        }else {
            switchAB.setVisibility(View.GONE);
        }



        if(Utility.getStringFromPref(getActivitty(),"is_busy").equals("1")){
            switchAB.setChecked(false);
          //  ss.setText(getString(R.string.unavailable));
        }else {

            switchAB.setChecked(true);
          //  ss.setText(getString(R.string.available));

        }


        switchAB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if (isChecked) {
                   // ss.setText(getString(R.string.available));
                    setavalbel("0");

                } else {
                  //  ss.setText(getString(R.string.unavailable));
                    setavalbel("1");

                }
            }
        });
        Utility.setTitle(getString(R.string.prfile), (AppCompatActivity) getActivity());
        is_admin= Utility.getStringFromPref(getActivitty(), "is_admin");
        if (is_admin.equals("0")) {
            id_no.setVisibility(View.GONE);
            type_car.setVisibility(View.GONE);

        }else {
            id_no.setVisibility(View.VISIBLE);
            type_car.setVisibility(View.VISIBLE);

        }
            c_lang = Utility.getIntFromPref(getActivity() , "lang"  );
        if (c_lang==-1)c_lang=1;

        lat= Utility.getGenericStringFromPref(getActivity(), "lat","");
        lng= Utility.getGenericStringFromPref(getActivity(), "lng","");

        typeCar=Utility.getStringFromPref(getActivity(),"firstName","0");
        id_no.setText( Utility.getStringFromPref(getActivity(),"id_no"));
        name.setText( Utility.getStringFromPref(getActivity(),"firstName"));
        mobile.setText( Utility.getStringFromPref(getActivity(),"mobile"));
        address.setText(Utility.getStringFromPref(getActivity(),"address"));
        PassUser.setText(Utility.getStringFromPref(getActivity(),"password"));

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



        if(typeCar.equals("0")){
            motor.setChecked(true);
        }else if(typeCar.equals("1")){
            car.setChecked(true);
        }else if(typeCar.equals("2")) {
            truck.setChecked(true);
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



        getListCountries();


        Glide.with(App.getInstance())
                .load( Utility.getStringFromPref(getActivity(),"image"))
                .error(R.drawable.placeholder)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(img_prfile);

        return view;
    }


    @OnClick(R.id.tx_edit)
    public void tx_edit() {
        List<String> permissionsNeeded2 = new ArrayList<String>();
        permissionsNeeded2.add(android.Manifest.permission.READ_EXTERNAL_STORAGE);
        permissionsNeeded2.add(android.Manifest.permission.CAMERA);

        PermissionManager permissionManager2 = PermissionManager.getInstance(getActivity());
        permissionManager2.checkPermissions(permissionsNeeded2, new PermissionManager.PermissionRequestListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onPermissionGranted() {
                Album.image(getActivity()) // Image selection.
                        .singleChoice()
                        .camera(true)
                        .columnCount(2)

                        .onResult(new Action<ArrayList<AlbumFile>>() {
                            @Override
                            public void onAction(@NonNull ArrayList<AlbumFile> result) {


                                imgPrfile=result.get(0).getPath();
                                Glide.with(getActivity())
                                        .load(imgPrfile)
                                        .error(R.drawable.placeholder)
                                        // .placeholder(R.drawable.placeholder)
                                        .crossFade()
                                        .into(img_prfile);
                            }
                        })
                        .onCancel(new Action<String>() {
                            @Override
                            public void onAction(@NonNull String result) {
                                imgPrfile="";
                            }
                        })
                        .start();

            }
            @Override
            public void onPermissionDenied() {

            }
        });
    }
    @OnClick(R.id.logo)
    public void logo() {
        List<String> permissionsNeeded2 = new ArrayList<String>();
        permissionsNeeded2.add(android.Manifest.permission.READ_EXTERNAL_STORAGE);
        permissionsNeeded2.add(android.Manifest.permission.CAMERA);

        PermissionManager permissionManager2 = PermissionManager.getInstance(getActivity());
        permissionManager2.checkPermissions(permissionsNeeded2, new PermissionManager.PermissionRequestListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onPermissionGranted() {
                Album.image(getActivity()) // Image selection.
                        .singleChoice()
                        .camera(true)
                        .columnCount(2)

                        .onResult(new Action<ArrayList<AlbumFile>>() {
                            @Override
                            public void onAction(@NonNull ArrayList<AlbumFile> result) {


                                imgPrfile=result.get(0).getPath();
                                Glide.with(getActivity())
                                        .load(imgPrfile)
                                        .error(R.drawable.placeholder)
                                        // .placeholder(R.drawable.placeholder)
                                        .crossFade()
                                        .into(img_prfile);
                            }
                        })
                        .onCancel(new Action<String>() {
                            @Override
                            public void onAction(@NonNull String result) {
                                imgPrfile="";
                            }
                        })
                        .start();

            }
            @Override
            public void onPermissionDenied() {

            }
        });
    }


    @OnClick(R.id.go_to_next)
    public void go_to_next() {



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
        }else if (id_no.getText().toString().isEmpty() && is_admin.equals("1")) {
            id_no.setError("أدخل رقم الهوية");

        } else {
            try {
                post_Reg();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }


    }


    public void iniMap(){
        try {



            mMapView.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap mMap) {
                    mMap.clear();
                    googleMap = mMap;
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

                            googleMap.clear();
                            lng=latLng.longitude+"";
                            lat= latLng.latitude+"";
                            LatLng sydney = new LatLng( latLng.latitude, latLng.longitude);
                            BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.pin_user);
                            googleMap.addMarker(new MarkerOptions().position(sydney).visible(true).title("").snippet("").icon(icon));

// For zooming automatically to the location of the marker
                            CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(zoomLevel).build();
                            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                      /*  googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
                                latLng.latitude, latLng.longitude), zoomLevel));*/

                            googleMap.getUiSettings().setZoomControlsEnabled(true);
                        }
                    });


                    LatLng sydney = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
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

    public boolean Check(String fullName , String email, String mobile, String username, String city, String age ) {
        boolean flag = false;

            if (fullName.isEmpty()  || email.isEmpty() ||mobile.isEmpty()  ||username.isEmpty() ||city.isEmpty() ||age.isEmpty() ) {
                flag = false;
            } else {

                flag = true;

            }


        return flag;
    }
    public AppCompatActivity getActivitty(){
        return (AppCompatActivity) getActivity();
    }

    public void post_Reg()throws FileNotFoundException {
        final ProgressDialog shwo=new ProgressDialog();
        shwo.show(getActivitty().getSupportFragmentManager(),"dfd");
        String url="";
        if (Utility.getStringFromPref(getActivity(), "is_admin").equals("0")) {
            url="update_profile";
        }else {
            url="update_delivery_account";
        }
        SimpleMultiPartRequest request = new SimpleMultiPartRequest(Request.Method.POST, Utility.API_ROOT + url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
              shwo.dismissAllowingStateLoss();
                Log.i("response", response);
                JSONObject jsonObject = null;
                try {

                    jsonObject = new JSONObject(response);
                    if (Utility.checkRequestStatus(jsonObject)) {


                        Utility.showToast(jsonObject.getString("message"), true);
                        Utility.addGenericToPrefUser(getActivity(), "mobile",mobile.getText().toString());

                        if(!PassUser.getText().toString().isEmpty())
                            Utility.addGenericToPrefUser(getActivity(), "password", PassUser.getText().toString());

                        if(!PassUser.getText().toString().isEmpty()){
                            Utility.addToPref(getActivity(), "password", PassUser.getText().toString());
                        }

                        Utility.addToPref(getActivity(), "firstName", name.getText().toString());



                        Utility.addToPref(getActivity(), "id_no", id_no.getText().toString());

                        Utility.addToPref(getActivity(), "type", typeCar);

                        Utility.addToPref(getActivity(), "address", address.getText().toString());
                        Utility.addGenericToPref(getActivity(), "lat", lat);
                        Utility.addGenericToPref(getActivity(), "lng", lng);




                        Utility.addToPref(getActivity(), "city_id",list_countries.get(CityPositionSp).getSections().get(SectionsPositionSp-1).getId());
                        Utility.addToPref(getActivity(), "country_id",list_countries.get(CityPositionSp).getId());


                            if (Utility.getStringFromPref(getActivity(), "is_admin").equals("0")) {
                                Utility.addToPref(getActivity(), "image", Utility.SITE_ROOT + "uploads/users/" + jsonObject.optString("image"));

                            }else {
                                Utility.addToPref(getActivity(), "image",Utility.SITE_ROOT + "uploads/admin/" +  jsonObject.optString("image"));

                            }






                    }else if (jsonObject.has("message")) {

                        Utility.showToast(jsonObject.getString("message"), false);

                    } else {

                        Utility.showToast(getString(R.string.some_error), false);

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), e.getMessage() , Toast.LENGTH_LONG).show();


                } catch (ParseException e) {
                    e.printStackTrace();
                }





            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                shwo.dismiss();
                error.printStackTrace();
                Utility.showToast(R.string.connection_error, false);



            }
        }) {


        };
        if(!PassUser.getText().toString().isEmpty())
            request.addStringParam("password", PassUser.getText().toString());

        request.addStringParam("mobile", mobile.getText().toString());
        request.addStringParam("name",name.getText().toString() );
        request.addStringParam("firstName",name.getText().toString() );

        request.addStringParam("address", address.getText().toString());
        request.addStringParam("lat", lat);
        request.addStringParam("lng", lng);
        request.addStringParam("lang",Utility.getLangDefult(getActivity()));
        request.addStringParam("token", Utility.getStringFromPref(getActivity(), "token"));

        if(is_admin.equals("1")) {
            request.addStringParam("id_no", id_no.getText().toString());
            request.addStringParam("type", typeCar);
        }
        request.addStringParam("section_id",list_countries.get(CityPositionSp).getSections().get(SectionsPositionSp-1).getId() );
        request.addStringParam("city_id",list_countries.get(CityPositionSp).getId() );




        if(imgPrfile.length()>2) {
            request.addFile("image[]", imgPrfile);

        }



        request.setRetryPolicy(new DefaultRetryPolicy(
                Utility.REQ_T_OUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        App.getInstance().addToRequestQueue(request);




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


    private void setavalbel(final String s) {

        if (Utility.isConnectingToInternet(getActivity())) {

            final ProgressDialog shwo=new ProgressDialog();
            shwo.show(getFragmentManager(),"dfdf");

            StringRequest request = new StringRequest(Request.Method.POST, Utility.API_ROOT + "delivery_change_active_status", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    shwo.dismiss();
                    Log.i("response", response);
                    try {
                        JSONObject jsonObject = null;
                        jsonObject = new JSONObject(response);
                        if (Utility.checkRequestStatus(jsonObject)) {
                            Utility.showToast(jsonObject.optString("message"), true);
                            Utility.addToPref(getActivity(), "is_busy", s);

                        }else{
                            Utility.showToast(jsonObject.optString("message"), false);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Utility.showToast(R.string.some_error, false);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    shwo.dismiss();
                    error.printStackTrace();
                    Utility.showToast(R.string.connection_error, false);
                }
            }) {

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    Map<String, String> params = new HashMap<>();
                    params.put("token", Utility.getStringFromPref(getActivity(), "token"));
                    params.put("is_busy", s);
                    params.put("lang",Utility.getLangDefult(getActivity()));

               /* params.put("password", pass);
                params.put("email",email);
                params.put("mobile", mobile);
*/
                    return params;

                }
            };

            App.getInstance().addToRequestQueue(request);
        } else {

            Utility.showErrorDialog(getActivity(), getString(R.string.no_internet));

        }

    }
}
