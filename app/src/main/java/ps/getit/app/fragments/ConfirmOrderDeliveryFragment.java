package ps.getit.app.fragments;


import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ps.getit.app.App;
import ps.getit.app.Custom.CustomMapView;
import ps.getit.app.Custom.CustomMarkerDrow;
import ps.getit.app.R;
import ps.getit.app.activities.DeliveryAddOfferActivity;
import ps.getit.app.activities.DetailsDdliveryRequestsActivity;
import ps.getit.app.activities.MainActivity;
import ps.getit.app.drawroutemap.DrawRouteMaps;
import ps.getit.app.models.Transactions;
import ps.getit.app.utils.Utility;


public class ConfirmOrderDeliveryFragment extends Fragment {

    public static ConfirmOrderDeliveryFragment newInstance(String Order_id) {

        Bundle args = new Bundle();
        args.putString("order_id",Order_id);
        ConfirmOrderDeliveryFragment fragment = new ConfirmOrderDeliveryFragment();
        fragment.setArguments(args);
        return fragment;
    }



    @BindView(R.id.details)
    TextView details;

    

    @Nullable
    @BindView(R.id.order_id)
    TextView order_idt;
    @Nullable
    @BindView(R.id.sender_address)
    TextView sender_addresst;
    @Nullable
    @BindView(R.id.recipient_address)
    TextView recipient_address;

    private GoogleMap googleMap;

    @BindView(R.id.mapView)
    CustomMapView mMapView;


    String order_id="";
    String sender_mobile="",sender_name = "", sender_lat = "0.0", sender_lng = "0.0", receiver_name = "", receiver_lat = "0.0", receiver_lng = "0.0", driver_lat = "0.0", driver_lng = "0.0", driver_mobile = "", driver_name = "";
    int driver_estimated_time = 0;

    public ConfirmOrderDeliveryFragment() {
        // Required empty public constructor
    }



    public static ConfirmOrderDeliveryFragment newInstance() {
        ConfirmOrderDeliveryFragment fragment = new ConfirmOrderDeliveryFragment();
        return fragment;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    public AppCompatActivity getActivitty(){
        return (AppCompatActivity) getActivity();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_confirm_order_delivery, container, false);

        ButterKnife.bind(this, view);

       // ((MainActivity)getActivitty()).setTitle("تأكيد او رفض الطلبية");

        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        

                Bundle bundle = this.getArguments();
        if (bundle != null) {

            order_id= bundle.getString("order_id");
            order_idt.setText("رقم الطلبية : "+order_id);

            Map<String, String> params = new HashMap<>();
            params.put("order_id", order_id);
            new App.CustomRequest(getActivitty(), "get_order_details_delivery", params) {
                @Override
                public void onSuccess(JSONObject obj) {

                    try {
                        JSONObject data = new JSONObject(obj.optJSONObject("data").toString());
                        details.setText(data.optString("details"));

                        sender_lat = data.optString("sender_lat");
                        sender_lng = data.optString("sender_lng");
                        receiver_lat = data.optString("receiver_lat");
                        receiver_lng = data.optString("receiver_lng");
                        sender_name = data.optString("sender_name");
                        receiver_name = data.optString("receiver_name");
                        sender_mobile= data.optString("sender_mobile");
                        if (data.has("driver")){
                            JSONObject driver = new JSONObject(data.optJSONObject("driver").toString());
                            driver_lat = driver.optString("lat");
                            driver_lng = driver.optString("lat");
                            driver_mobile = driver.optString("mobile");
                            Log.d("driver_mobile","llllllll  "+driver_mobile);

                            driver_estimated_time = driver.optInt("estimated_time");
                            driver_name = driver.optString("name");
                        }



                        sender_addresst.setText(data.optString("sender_city_name")+"\n"+data.optString("sender_section_name")+"\n"+data.optString("sender_address"));
                        recipient_address.setText(data.optString("receiver_city_name")+"\n"+data.optString("receiver_section_name")+"\n"+data.optString("receiver_address"));


                        iniMap();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }


            };
        }
        return view;

    }

    public void iniMap(){

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                mMap.clear();
                googleMap = mMap;
                // For showing a move to my location button




                LatLng sender = new LatLng(Double.parseDouble(sender_lat), Double.parseDouble(sender_lng));
                LatLng receiver = new LatLng(Double.parseDouble(receiver_lat), Double.parseDouble(receiver_lng));


                DrawRouteMaps.getInstance(getActivitty())
                        .draw(sender, receiver, mMap);

                CustomMarkerDrow.getInstance(getActivitty()).draw(mMap, sender, getResources().getDrawable(  R.drawable.pin_delvary), "من "+sender_name);
                CustomMarkerDrow.getInstance(getActivitty()).draw(mMap, receiver, getResources().getDrawable(  R.drawable.pin_delvary), "الى "+receiver_name);

                LatLngBounds bounds = new LatLngBounds.Builder()
                        .include(sender)
                        .include(receiver).build();
                Point displaySize = new Point();
                getActivitty().getWindowManager().getDefaultDisplay().getSize(displaySize);
                mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, displaySize.x, 250, 30));


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

    @OnClick(R.id.confirm)
    public void confirm(){
        Fun(order_id, "delivery_confirm_order", 1);

    }

    @OnClick(R.id.cancel_action)
    public void cancel_action(){
        Fun(order_id, "delivery_reject_order", 0);

    }
    public void Fun(final String order_id, String fun, final int isConferm){
        Map<String, String> params = new HashMap<>() ;

        params.put("order_id",order_id);

        new App.CustomRequest(getActivitty(),fun,params) {
            @Override
            public void onSuccess(JSONObject obj) {

                Utility.showToast(obj.optString("message"), true);


                if(isConferm==1){
                    Intent i = new Intent(getActivitty(), DetailsDdliveryRequestsActivity.class);
                    i.putExtra("id_Order", order_id);
                    startActivity(i);
                }


            }



        };
    }


}
