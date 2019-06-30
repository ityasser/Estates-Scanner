package ps.getit.app.activities;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
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
import de.hdodenhof.circleimageview.CircleImageView;
import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import ps.getit.app.App;
import ps.getit.app.CustomAdapter.CustomDrawerAdapter;
import ps.getit.app.Dialogs.BottomSheetDialog;
import ps.getit.app.Dialogs.ProgressDialog;
import ps.getit.app.Dialogs.ResponseDialog;
import ps.getit.app.R;
import ps.getit.app.fragments.AboutFragment;
import ps.getit.app.fragments.ConfirmOrderDeliveryFragment;
import ps.getit.app.fragments.CreditFragment;
import ps.getit.app.fragments.DriverResponsesFragment;
import ps.getit.app.fragments.FinancialAccountFragment;
import ps.getit.app.fragments.FinishedOrdersFragment;
import ps.getit.app.fragments.HomeMapFragment;
import ps.getit.app.fragments.MyOrderFragment;
import ps.getit.app.fragments.NewOrderFragment;
import ps.getit.app.fragments.Nofication_View;
import ps.getit.app.fragments.PointsPrizesFragment;
import ps.getit.app.fragments.PointsSaleFragment;
import ps.getit.app.fragments.ProfileUserFragment;
import ps.getit.app.fragments.ServiceRequestFragment;
import ps.getit.app.fragments.ViewCreditFragment;
import ps.getit.app.models.CreditLog;
import ps.getit.app.models.DrawerItem;
import ps.getit.app.utils.LocationService;
import ps.getit.app.utils.Utility;

public class MainActivity extends BaseActivity {
    public final static int  Home=111;
    public final static int logout=2222;
    public final static int about_app=3333;
    public final static int prfile=444;
    public final static int my_order=5555;
    public final static int FinancialAccount=66666;
    public final static int order_traking=77777;
    public final static int service_add=88888;
    public final static int recent_order=99999;
    public final static int points_exchange=101010010;
    public final static int notifications=1111111111;
    public final static int contacut_us=12123123;
    public final static int friend_invite=100000;
    public final static int wallet=256251566;
    public final static int new_order=1356251566;


    public final static int prfileMarket=54568523;
    public final static int prfileDelivery=5434453;
    public final static int list_new_order=12364453;
    public final static int credit=685324523;

    @BindView(R.id.lielift)
    LinearLayout lielift;

    @BindView(R.id.left_drawer)
    ListView mDrawerList;

    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    @BindView(R.id.price)
    TextView price;


    @BindView(R.id.count)
    TextView count;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.prof)
    CircleImageView prof;

    @BindView(R.id.uname)
    TextView uname;

    View last_view;

    @BindView(R.id.lay_bputom)
    LinearLayout lay_bputom;

    NotificationBadgeReciever  mLocationBadgeReciever;
    IntentFilter mFilter ;

    @BindView(R.id.switchForActionBar)
    SwitchCompat switchAB;

    ResponseDialog  responseDialog;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    CustomDrawerAdapter adapter;
    List<DrawerItem> itemsDrawer;
    public ActionBarDrawerToggle mDrawerToggle;


    DriverResponses driverResponses;
    IntentFilter driverResponsesFilter2 ;
    class DriverResponses extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            responseDialog.dismissAllowingStateLoss();
            final Bundle extras=intent.getExtras();
            if (extras!=null) {
                getSupportFragmentManager().beginTransaction().addToBackStack("").replace(R.id.container, DriverResponsesFragment.newInstance(extras.getString("id_Order"))).commit();

            }

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

         responseDialog =new ResponseDialog();

        beginUpdates();

        Glide.with(App.getInstance())
                .load(  Utility.getStringFromPref(this,"image"))
                .error(R.drawable.placeholder)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(prof);

        uname.setText( Utility.getStringFromPref(this,"firstName"));

        itemsDrawer = new ArrayList<DrawerItem>();
        Log.d("is_admin", Utility.getStringFromPref(MainActivity.this, "is_admin") );

        if (Utility.getStringFromPref(MainActivity.this, "is_admin").equals("0")) {
            switchAB.setVisibility(View.GONE);

            lay_bputom.setVisibility(View.GONE);
            itemsDrawer.add(new DrawerItem(getString(R.string.home), R.drawable.home,Home));
            itemsDrawer.add(new DrawerItem("اطلب خدمة", R.drawable.home,service_add));

            itemsDrawer.add(new DrawerItem(getString(R.string.my_order), R.drawable.my_order,my_order));
            itemsDrawer.add(new DrawerItem(getString(R.string.recent_order), R.drawable.recent_order,recent_order));
            itemsDrawer.add(new DrawerItem(getString(R.string.notfcation), R.drawable.notification,notifications));
            itemsDrawer.add(new DrawerItem(getString(R.string.financial_account), R.drawable.price,FinancialAccount));
            itemsDrawer.add(new DrawerItem(getString(R.string.credit), R.drawable.credit,credit));
            itemsDrawer.add(new DrawerItem(getString(R.string.wallet), R.drawable.wallet,wallet));
            itemsDrawer.add(new DrawerItem(getString(R.string.friend_invite), R.drawable.friend_invite,friend_invite));
            itemsDrawer.add(new DrawerItem(getString(R.string.prfile), R.drawable.profile,prfile));
            itemsDrawer.add(new DrawerItem(getString(R.string.points_exchange), R.drawable.points_exchange,points_exchange));
            itemsDrawer.add(new DrawerItem(getString(R.string.about_app), R.drawable.about,about_app));
            itemsDrawer.add(new DrawerItem(getString(R.string.logout), R.drawable.logout,logout));


        } else if (Utility.getStringFromPref(MainActivity.this, "is_admin").equals("1")) {
            switchAB.setVisibility(View.VISIBLE);


            if(Utility.getStringFromPref(MainActivity.this,"is_busy").equals("1")){
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


                List<String> permissionsNeeded2 = new ArrayList<String>();
                permissionsNeeded2.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
                permissionsNeeded2.add(android.Manifest.permission.ACCESS_COARSE_LOCATION);
                PermissionManager permissionManager2 = PermissionManager.getInstance(MainActivity.this);
                permissionManager2.checkPermissions(permissionsNeeded2, new PermissionManager.PermissionRequestListener() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onPermissionGranted() {
                        if(!isGPSEnabled()){
                            Utility.showSettingsAlert(MainActivity.this);
                        }else {
                            try {
                                if (Utility.getLocationMode(MainActivity.this) != 3) {
                                    Utility.showSettingAcurcysAlert(MainActivity.this);
                                } else {
                                    if (!Utility.isMyServiceRunning(MainActivity.this, LocationService.class)) {
                                        Intent serviceIntent = new Intent(MainActivity.this, LocationService.class);
                                        startService(serviceIntent);
                                    }
                                }
                            } catch (Settings.SettingNotFoundException e) {
                                e.printStackTrace();
                            }



                        mLocationBadgeReciever = new NotificationBadgeReciever();
                        mFilter = new IntentFilter("notification.view.badge");

                        registerReceiver(mLocationBadgeReciever, mFilter);


                    }
                    }




                    @Override
                    public void onPermissionDenied() {

                    }
                });



            getRests();
            lay_bputom.setVisibility(View.VISIBLE);

            itemsDrawer.add(new DrawerItem(getString(R.string.home), R.drawable.home,new_order));

            itemsDrawer.add(new DrawerItem(getString(R.string.my_order), R.drawable.my_order,my_order));
            itemsDrawer.add(new DrawerItem(getString(R.string.financial_account), R.drawable.price,FinancialAccount));

            itemsDrawer.add(new DrawerItem(getString(R.string.wallet), R.drawable.wallet,wallet));

            itemsDrawer.add(new DrawerItem(getString(R.string.prfile), R.drawable.profile,prfile));

            itemsDrawer.add(new DrawerItem(getString(R.string.logout), R.drawable.logout,logout));



        }




        /*
        itemsDrawer.add(new DrawerItem(getString(R.string.home), R.drawable.home,Home));
        itemsDrawer.add(new DrawerItem(getString(R.string.suppliers), R.drawable.suppliers,suppliers));
        itemsDrawer.add(new DrawerItem(getString(R.string.service_add), R.drawable.notfication, service_add));
        itemsDrawer.add(new DrawerItem(getString(R.string.my_order), R.drawable.careers, order_traking));
        itemsDrawer.add(new DrawerItem(getString(R.string.consultation_add), R.drawable.consultation, consultation));
        itemsDrawer.add(new DrawerItem(getString(R.string.consultation_view), R.drawable.consultation, consultation_view));
        itemsDrawer.add(new DrawerItem(getString(R.string.business_calnder), R.drawable.business_calnder, business_calnder));
        itemsDrawer.add(new DrawerItem(getString(R.string.prfile), R.drawable.careers,prfile));

        itemsDrawer.add(new DrawerItem(getString(R.string.contactus), R.drawable.contacut_us,contacut_us));
        itemsDrawer.add(new DrawerItem(getString(R.string.about_app), R.drawable.logo,about_app));

        itemsDrawer.add(new DrawerItem(getString(R.string.logout), R.drawable.logout,logout));
*/
        mTitle = mDrawerTitle = getTitle();
        adapter = new CustomDrawerAdapter(this, R.layout.item_custom_drawer, itemsDrawer);
        mDrawerList.setAdapter(adapter);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        setSupportActionBar(toolbar);
        this.getSupportActionBar().setHomeButtonEnabled(true);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        this.getSupportActionBar().setDisplayShowHomeEnabled(true);

        this.getSupportActionBar().setDisplayShowTitleEnabled(true);



        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                toolbar, R.string.drawer_open,
                R.string.drawer_close) {
            public void onDrawerClosed(View view) {


                Utility.setTitle(mTitle+"", MainActivity.this);

                Log.d("mTitle",mTitle+"");
                invalidateOptionsMenu(); // creates call to
                // onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                // MainActivity.this.getSupportActionBar().setTitle(mDrawerTitle);
                Log.d("sdsadsadsada","sdsdsdsdsdsdsd");
                Utility.setTitle("القائمة الرئيسية", MainActivity.this);

                invalidateOptionsMenu(); // creates call to

            }
        };


        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerToggle.syncState();


        final Bundle extras=getIntent().getExtras();
        if (extras!=null){



            if( extras.getString("type").equals("notfiy")){

                Nofication_View nofication_view =new Nofication_View();
                getSupportFragmentManager().beginTransaction().replace(R.id.container,nofication_view).commitAllowingStateLoss();
                Utility.setTitle(getString(R.string.notfcation),MainActivity.this);

            }else if(extras.getString("type").equals("traking")){

                MyOrderFragment orderTrakingFragment =new MyOrderFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.container,orderTrakingFragment).commitAllowingStateLoss();
                Utility.setTitle(getString(R.string.order_traking),MainActivity.this);

            }else if (extras.getString("type").equals("responds")){
                if(Utility.getStringFromPref(MainActivity.this, "is_admin").equals("0")) {
                    getSupportFragmentManager().beginTransaction().addToBackStack("").replace(R.id.container, DriverResponsesFragment.newInstance(extras.getString("id_Order"))).commit();
                }else {
                    NewOrderFragment servicesFragment = new NewOrderFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, servicesFragment).commit();
                    mTitle=getString(R.string.home);

                    Log.d("status_new_order",extras.getString("status"));
                    if (extras.getString("status").equals("1")) {
                        Bundle args = new Bundle();
                        args.putString("order_id",extras.getString("id_Order"));
                        ConfirmOrderDeliveryFragment fragment = new ConfirmOrderDeliveryFragment();
                        fragment.setArguments(args);

                        getSupportFragmentManager().beginTransaction().addToBackStack("").replace(R.id.container,fragment ).commit();


                        BottomSheetDialog show = new BottomSheetDialog();
                        show.setOnItemSelectedListener(new BottomSheetDialog.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(String cur) {

                                if (cur.equals("1")) {
                                    Fun(extras.getString("id_Order"), "delivery_confirm_order", 1);
                                } else if (cur.equals("2")) {
                                } else if (cur.equals("3")) {
                                    Fun(extras.getString("id_Order"), "delivery_reject_order", 0);
                                }
                            }
                        });
                    //    show.show(getSupportFragmentManager(), "sed");

                    }
                }

            }else if(extras.getString("type").equals("contact")) {
           /* ContactUs_Fragment contactUs_fragment =new ContactUs_Fragment();
            fragmentManager.beginTransaction().replace(R.id.container,contactUs_fragment).commitAllowingStateLoss();
            Utility.setTitle(getString(R.string.contactus),MainActivity.this);*/
            }



        }else {

            if (Utility.getStringFromPref(MainActivity.this, "is_admin").equals("0")) {
                HomeMapFragment homeMapFragment = new HomeMapFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.container, homeMapFragment).commit();
                mTitle=getString(R.string.service);
            }else {
                NewOrderFragment servicesFragment = new NewOrderFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.container, servicesFragment).commit();
                mTitle=getString(R.string.home);
            }

        }



    }

    public void setTitle(String title){
        Utility.setTitle(title+"", MainActivity.this);

    }
    public void to_Coinferm(String order_id){
        getSupportFragmentManager().beginTransaction().addToBackStack("").replace(R.id.container, ConfirmOrderDeliveryFragment.newInstance(order_id)).commit();

    }
    private void setavalbel(final String s) {
        Log.d("active", s);

        if (Utility.isConnectingToInternet(MainActivity.this)) {

            final ProgressDialog shwo=new ProgressDialog();
            shwo.show(getSupportFragmentManager(),"dfdf");

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
                            Utility.addToPref(MainActivity.this, "is_busy", s);

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
                    params.put("token", Utility.getStringFromPref(MainActivity.this, "token"));
                    params.put("is_busy", s);
                    params.put("lang",Utility.getLangDefult(MainActivity.this));

               /* params.put("password", pass);
                params.put("email",email);
                params.put("mobile", mobile);
*/
                    return params;

                }
            };

            App.getInstance().addToRequestQueue(request);
        } else {

            Utility.showErrorDialog(MainActivity.this, getString(R.string.no_internet));

        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();


        if(driverResponses!=null){
            unregisterReceiver(driverResponses);
        }

        if(mLocationBadgeReciever!=null){
            unregisterReceiver(mLocationBadgeReciever);
        }
        if (Utility.isMyServiceRunning(MainActivity.this, LocationService.class)) {
            Intent intent = new Intent(MainActivity.this, LocationService.class);
            stopService(intent);
        }
    }

    public boolean isGPSEnabled() {
        return ((LocationManager) MainActivity.this.getSystemService(Context.LOCATION_SERVICE)).isProviderEnabled(LocationManager.GPS_PROVIDER);
    }
    class NotificationBadgeReciever extends BroadcastReceiver {


        @Override
        public void onReceive(Context context, final Intent intent) {

            Log.d("updatLoction", intent.getStringExtra("Latitude")+"  "+ intent.getStringExtra("Longitude"));

           // Toast.makeText( getApplicationContext(), "Location updatLoction", Toast.LENGTH_SHORT).show();


            update_delivery_position( intent.getStringExtra("Latitude"), intent.getStringExtra("Longitude"));


        }
    }
    public void update_delivery_position(final String lat, final String lng){
        StringRequest request = new StringRequest(Request.Method.POST, Utility.API_ROOT + "update_delivery_position", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.i("response_upate_deliver", response);
                //Toast.makeText( getApplicationContext(), "response "+response, Toast.LENGTH_SHORT).show();


            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();



            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("token", Utility.getStringFromPref(MainActivity.this, "token"));
                params.put("lng", lng);
                params.put("lat",lat);
                params.put("lang",Utility.getLangDefult(MainActivity.this));

                // params.put("rest_id",Utility.getStringFromPref(App.getInstance(),"rest_id")) ;

               /* params.put("password", pass);
                params.put("email",email);
                params.put("mobile", mobile);
*/
                return params;

            }
        };

        App.getInstance().addToRequestQueue(request);



    }
    @Override
    protected void onNewIntent(Intent intent) {
        final Bundle extras=intent.getExtras();

        if( extras.getString("type").equals("notfiy")){

            Nofication_View nofication_view =new Nofication_View();
            getSupportFragmentManager().beginTransaction().replace(R.id.container,nofication_view).commitAllowingStateLoss();
            Utility.setTitle(getString(R.string.notfcation),MainActivity.this);

        }else if(extras.getString("type").equals("traking")){

            MyOrderFragment orderTrakingFragment =new MyOrderFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.container,orderTrakingFragment).commitAllowingStateLoss();
            Utility.setTitle(getString(R.string.order_traking),MainActivity.this);

        }else if (extras.getString("type").equals("responds")){

            if(Utility.getStringFromPref(MainActivity.this, "is_admin").equals("0")) {
                getSupportFragmentManager().beginTransaction().addToBackStack("").replace(R.id.container, DriverResponsesFragment.newInstance(extras.getString("id_Order"))).commit();
            }else {
                Log.d("status_new_order",extras.getString("status"));

                if (extras.getString("status").equals("1")) {
                    NewOrderFragment servicesFragment = new NewOrderFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, servicesFragment).commit();

                    mTitle=getString(R.string.home);

                    Bundle args = new Bundle();
                    args.putString("order_id",extras.getString("id_Order"));
                    ConfirmOrderDeliveryFragment fragment = new ConfirmOrderDeliveryFragment();
                    fragment.setArguments(args);

                    getSupportFragmentManager().beginTransaction().addToBackStack("").replace(R.id.container,fragment ).commit();


                    BottomSheetDialog show = new BottomSheetDialog();
                    show.setOnItemSelectedListener(new BottomSheetDialog.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(String cur) {

                            if (cur.equals("1")) {
                                Fun(extras.getString("order_id"), "delivery_confirm_order", 1);
                            } else if (cur.equals("2")) {
                            } else if (cur.equals("3")) {
                                Fun(extras.getString("order_id"), "delivery_reject_order", 0);
                            }
                        }
                    });
                   // show.show(getSupportFragmentManager(), "sed");
                }
            }
        }else if(extras.getString("type").equals("contact")) {
           /* ContactUs_Fragment contactUs_fragment =new ContactUs_Fragment();
            fragmentManager.beginTransaction().replace(R.id.container,contactUs_fragment).commitAllowingStateLoss();
            Utility.setTitle(getString(R.string.contactus),MainActivity.this);*/
        }
        super.onNewIntent(intent);
    }

    public void getRests(){

        if (Utility.isConnectingToInternet(MainActivity.this)) {

            String url="";
            if (Utility.getStringFromPref(MainActivity.this, "is_admin").equals("0")) {
                url="get_delivery_orders_total";
            }else {
                url="get_delivery_orders_total";
            }
            StringRequest request = new StringRequest(Request.Method.POST, Utility.API_ROOT + url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {


                    Log.i("getMarkets", response);
                    try {
                        JSONObject obj = new JSONObject(response);

                        if(Utility.checkRequestStatus(response)) {





                            count.setText(obj.optJSONObject("data").optString("count"));
                            price.setText(obj.optJSONObject("data").optString("total")+" "+Utility.getStringFromPref(MainActivity.this,"currency","₪"));
                        }else {
                            Utility.showToast(obj.optString("massage"), false);

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Utility.showToast(R.string.some_error, false);
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
                    params.put("token", Utility.getStringFromPref(MainActivity.this, "token"));
                    params.put("lang",Utility.getLangDefult(MainActivity.this));




                    return params;

                }
            };

            App.getInstance().addToRequestQueue(request);
        } else {

            Utility.showErrorDialog(MainActivity.this, getString(R.string.no_internet));

        }

    }


    public void Fun(final String order_id, String fun, final int isConferm){
        Map<String, String> params = new HashMap<>() ;

        params.put("order_id",order_id);

        new App.CustomRequest(MainActivity.this,fun,params) {
            @Override
            public void onSuccess(JSONObject obj) {

                Utility.showToast(obj.optString("message"), true);


                if(isConferm==1){
                    Intent i = new Intent(MainActivity.this, DetailsDdliveryRequestsActivity.class);
                    i.putExtra("id_Order", order_id);
                    startActivity(i);
                }


            }



        };
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {

            TextView ItemName;

            if(last_view!=null){
                ItemName = (TextView) last_view .findViewById(R.id.drawer_itemName);

                last_view.setBackgroundColor(Color.parseColor("#f4f4f4"));


            }

            last_view =  parent.getChildAt(position);

            last_view.setBackgroundColor(Color.parseColor("#c0d7da"));



        /*    for(int i=0; i<parent.getCount();i++)
            {
                parent.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);
            }

            view.setBackgroundColor(Color.parseColor("#f2f2f2"));*/

            Log.d("ddd",position+"");

            SelectItem(position);


        }
    }


    public void goToMyOrder(){

        driverResponses = new DriverResponses();
        driverResponsesFilter2 = new IntentFilter("responds.view.badge");

        registerReceiver(driverResponses , driverResponsesFilter2);


        responseDialog.show(getSupportFragmentManager(),"sdfs");

      //  getSupportFragmentManager().beginTransaction().addToBackStack("").replace(R.id.container, SubCatFilterFragment.newInstance(id_cat,name)).commitAllowingStateLoss();


    }

    public void SelectItem(int possition) {
        SlectItemMain(possition);

        mDrawerList.setItemChecked(possition, true);
        mDrawerLayout.closeDrawer(lielift);
        Utility.setTitle(itemsDrawer.get(possition).getItemName(),this);

    }
    public void SlectItemMain(final int possition){

        switch (itemsDrawer.get(possition).getIdItem()) {

            case points_exchange:


                if(!Utility.getBooleanFromPref(MainActivity.this, "isLogin",false)){
                    Utility.ViewMsgReqerLogin(MainActivity.this);
                }else {
                    PointsPrizesFragment pointsPrizesFragment = new PointsPrizesFragment();
                    getSupportFragmentManager().beginTransaction().addToBackStack("").replace(R.id.container, pointsPrizesFragment).commit();

                    mTitle = itemsDrawer.get(possition).getItemName();
                }
                break;
            case service_add:
                if(!Utility.getBooleanFromPref(MainActivity.this, "isLogin",false)){
                    Utility.ViewMsgReqerLogin(MainActivity.this);
                }else {
                    ServiceRequestFragment servicesFragment = new ServiceRequestFragment();
                    getSupportFragmentManager().beginTransaction().addToBackStack("").replace(R.id.container, servicesFragment).commit();
                    mTitle = itemsDrawer.get(possition).getItemName();
                }
                break;

            case logout:
                Utility.logout(MainActivity.this);
                break;

            case my_order:
                if(!Utility.getBooleanFromPref(MainActivity.this, "isLogin",false)){
                    Utility.ViewMsgReqerLogin(MainActivity.this);
                }else {
                    getSupportFragmentManager().beginTransaction().addToBackStack("").replace(R.id.container, MyOrderFragment.newInstance()).commit();
                    mTitle = itemsDrawer.get(possition).getItemName();
                }
                break;

            case prfile:
                if(!Utility.getBooleanFromPref(MainActivity.this, "isLogin",false)){
                    Utility.ViewMsgReqerLogin(MainActivity.this);
                }else {
                    getSupportFragmentManager().beginTransaction().addToBackStack("").replace(R.id.container, ProfileUserFragment.newInstance()).commit();

                    mTitle = itemsDrawer.get(possition).getItemName();
                }
                break;

            case credit:
                if(!Utility.getBooleanFromPref(MainActivity.this, "isLogin",false)){
                    Utility.ViewMsgReqerLogin(MainActivity.this);
                }else {
                    getSupportFragmentManager().beginTransaction().addToBackStack("").replace(R.id.container, CreditFragment.newInstance()).commit();
                    mTitle = itemsDrawer.get(possition).getItemName();
                }
                break;


            case wallet:
                if(!Utility.getBooleanFromPref(MainActivity.this, "isLogin",false)){
                    Utility.ViewMsgReqerLogin(MainActivity.this);
                }else {
                    getSupportFragmentManager().beginTransaction().addToBackStack("").replace(R.id.container, ViewCreditFragment.newInstance()).commit();
                    mTitle = itemsDrawer.get(possition).getItemName();
                }
                break;
            case new_order:
                if(!Utility.getBooleanFromPref(MainActivity.this, "isLogin",false)){
                    Utility.ViewMsgReqerLogin(MainActivity.this);
                }else {
                    getSupportFragmentManager().beginTransaction().addToBackStack("").replace(R.id.container, NewOrderFragment.newInstance()).commit();
                    mTitle = itemsDrawer.get(possition).getItemName();
                }
                break;
            case recent_order:
                if(!Utility.getBooleanFromPref(MainActivity.this, "isLogin",false)){
                    Utility.ViewMsgReqerLogin(MainActivity.this);
                }else {
                    getSupportFragmentManager().beginTransaction().addToBackStack("").replace(R.id.container, FinishedOrdersFragment.newInstance()).commit();
                    mTitle = itemsDrawer.get(possition).getItemName();
                }
                break;

            case friend_invite:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT,

                        "سجل في تطبيق Git It  من خلال الكود " +
                                Utility.getStringFromPref(this,"invitation_code")+
                                "\nhttps://play.google.com/store/apps/details?id="+getPackageName());
                sendIntent.setType("text/plain");
                startActivity(sendIntent);


                break;



            case Home:

                HomeMapFragment homeMapFragment = new HomeMapFragment();
                getSupportFragmentManager().beginTransaction().addToBackStack("").replace(R.id.container, homeMapFragment).commit();
                mTitle=itemsDrawer.get(possition).getItemName();
                break;

            case FinancialAccount:

                if(!Utility.getBooleanFromPref(MainActivity.this, "isLogin",false)){
                    Utility.ViewMsgReqerLogin(MainActivity.this);
                }else {
                    FinancialAccountFragment financialAccountFragment = new FinancialAccountFragment();
                    getSupportFragmentManager().beginTransaction().addToBackStack("").replace(R.id.container, financialAccountFragment).commit();
                    mTitle = itemsDrawer.get(possition).getItemName();
                }
                break;

            case about_app:
                getSupportFragmentManager().beginTransaction().addToBackStack("").replace(R.id.container, AboutFragment.newInstance()).commit();

                mTitle=itemsDrawer.get(possition).getItemName();

                break;

            case notifications:
                getSupportFragmentManager().beginTransaction().addToBackStack("").replace(R.id.container, Nofication_View.newInstance()).commit();

                mTitle=itemsDrawer.get(possition).getItemName();

                break;

/*

            case prfile:
                getSupportFragmentManager().beginTransaction().addToBackStack("").replace(R.id.container, ViewPagerProfile.newInstance()).commit();

                mTitle=itemsDrawer.get(possition).getItemName();

                break;




            case service_add:
                getSupportFragmentManager().beginTransaction().addToBackStack("").replace(R.id.container, AddServiceFragment.newInstance()).commit();

                mTitle=itemsDrawer.get(possition).getItemName();


                break;
            case contacut_us:
                getSupportFragmentManager().beginTransaction().addToBackStack("").replace(R.id.container, CuntactUsFragment.newInstance()).commit();

                mTitle=itemsDrawer.get(possition).getItemName();

                break;


            case consultation:
                getSupportFragmentManager().beginTransaction().addToBackStack("").replace(R.id.container, AddConsultFragment.newInstance()).commit();

                mTitle=itemsDrawer.get(possition).getItemName();

                break;

            case business_calnder:
                getSupportFragmentManager().beginTransaction().addToBackStack("").replace(R.id.container, AddEvaluationFragment.newInstance()).commit();

                mTitle=itemsDrawer.get(possition).getItemName();

                break;


            case suppliers:
                getSupportFragmentManager().beginTransaction().addToBackStack("").replace(R.id.container, ViewPartnersFragment.newInstance()).commit();

                mTitle=itemsDrawer.get(possition).getItemName();

                break;

            case consultation_view:
                getSupportFragmentManager().beginTransaction().addToBackStack("").replace(R.id.container, ViewConsultsFragment.newInstance()).commit();
                mTitle=itemsDrawer.get(possition).getItemName();
                break;


        */


        }
    }

    @Override
    public void onBackPressed() {
        if(mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(Gravity.START); //CLOSE Nav Drawer!
        }else{
            super.onBackPressed();
        }
    }


    public void  beginUpdates(){
        List<String> permissionsNeeded2 = new ArrayList<String>();
        permissionsNeeded2.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
        permissionsNeeded2.add(android.Manifest.permission.ACCESS_COARSE_LOCATION);
        PermissionManager permissionManager2 = PermissionManager.getInstance(MainActivity.this);
        permissionManager2.checkPermissions(permissionsNeeded2, new PermissionManager.PermissionRequestListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onPermissionGranted() {

                Log.i("location","PermissionGranted user");

                SmartLocation.with(MainActivity.this).location()
                        .oneFix()
                        .start(new OnLocationUpdatedListener() {
                            @Override
                            public void onLocationUpdated(Location location) {
                                if (location != null) {

                                    Utility.addToPref(MainActivity.this, "last_lat", location.getLatitude()+"");
                                    Utility.addToPref(MainActivity.this, "last_lng", location.getLongitude()+"");


                                    Log.d("last_location","last location : " + location.getLatitude() + " , " +location.getLongitude());
                                    SmartLocation.with(MainActivity.this).location().stop();

                                }

                            }});


            }
            @Override
            public void onPermissionDenied() {

            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            if(Utility.getLocationMode(MainActivity.this) == 3) {
                if (!Utility.isMyServiceRunning(MainActivity.this, LocationService.class)) {
                    Intent serviceIntent = new Intent(MainActivity.this, LocationService.class);
                    startService(serviceIntent);
                }
            }else {
                Utility.showSettingAcurcysAlert(MainActivity.this);
            }




        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }

    }
}
