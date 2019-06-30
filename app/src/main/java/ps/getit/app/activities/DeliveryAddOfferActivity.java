package ps.getit.app.activities;

import android.content.Intent;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ps.getit.app.App;
import ps.getit.app.Custom.CustomMapView;
import ps.getit.app.Custom.CustomMarkerDrow;
import ps.getit.app.Dialogs.TimeDialog;
import ps.getit.app.R;
import ps.getit.app.drawroutemap.DrawRouteMaps;
import ps.getit.app.utils.Utility;

public class DeliveryAddOfferActivity extends BaseActivity {



    String detailst="",TimeExpected="",order_id="", sender_name = "", sender_lat = "0.0", sender_lng = "0.0", receiver_name = "", receiver_lat = "0.0", receiver_lng = "0.0", sender_address = "", receiver_address = "";

    private String sender_city_name;
    private String sender_section_name;
    private String receiver_city_name;
    private String receiver_section_name;
    @BindView(R.id.order_id)
    TextView order_idt;
    @BindView(R.id.sender_address)
    TextView sender_addresst;
    @BindView(R.id.recipient_address)
    TextView recipient_address;

    private GoogleMap googleMap;

    @BindView(R.id.mapView)
    CustomMapView mMapView;

    @BindView(R.id.time)
    Button time;

    @BindView(R.id.price)
    EditText price;

    @BindView(R.id.details)
    TextView details;

    @BindView(R.id.time2)
    EditText time2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_add_offer);
        ButterKnife.bind(this);
        Utility.setTitle("إرسال نموذج",this);

        Calendar cal = Calendar.getInstance();

        time.setText(new SimpleDateFormat("yyy-MM-dd", Locale.ENGLISH).format(cal.getTime()));
        TimeExpected=new SimpleDateFormat("yyy-MM-dd", Locale.ENGLISH).format(cal.getTime());

        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            sender_name=extras.getString("sender_name");
            sender_lat=extras.getString("sender_lat");
            sender_lng=extras.getString("sender_lng");
            receiver_name=extras.getString("receiver_name");
            receiver_lat=extras.getString("receiver_lat");
            receiver_lng=extras.getString("receiver_lng");
            sender_address=extras.getString("sender_address");
            receiver_address=extras.getString("receiver_address");
            order_id=extras.getString("order_id");
            detailst=extras.getString("details");
            sender_city_name=extras.getString("sender_city_name");
            sender_section_name=extras.getString("sender_section_name");
            receiver_city_name=extras.getString("receiver_city_name");
            receiver_section_name=extras.getString("receiver_section_name");

            order_idt.setText("رقم الطلبية : "+order_id);

            sender_addresst.setText(sender_city_name+"\n"+sender_section_name+"\n"+sender_address);
            recipient_address.setText(receiver_city_name+"\n"+receiver_section_name+"\n"+receiver_address);
            details.setText(detailst);

        }

        iniMap();
    }



    @OnClick(R.id.go_to_next)
    public void go_to_next(){
        if(TimeExpected.isEmpty()){
            Utility.showToast("برجاء اختيار تاريخ بدء التوصيل",false);
        }else if(price.getText().toString().isEmpty()){
            Utility.showToast("برجاء اختيار السعر المطلوب",false);
        }else if(time2.getText().toString().isEmpty()){
            Utility.showToast("برجاء اختيار الوقت المتوقع",false);
        }else {
            Map<String, String> params = new HashMap<>() ;
            params.put("price",price.getText().toString());
            params.put("date",TimeExpected);
            params.put("time",time2.getText().toString());


            params.put("order_id",order_id);

            new App.CustomRequest(this,"delivery_add_offer",params) {
                @Override
                public void onSuccess(JSONObject obj) {

                    Utility.showToast(obj.optString("message"), true);

                    finish();

                }



            };
        }
    }
    @OnClick(R.id.time)
    public void time(){
        Calendar cal = Calendar.getInstance();

        DatePickerDialog dpd1 = DatePickerDialog.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                Calendar cal = Calendar.getInstance();
                cal.set(year, monthOfYear, dayOfMonth);


                time.setText(new SimpleDateFormat("yyy-MM-dd", Locale.ENGLISH).format(cal.getTime()));
                TimeExpected=new SimpleDateFormat("yyy-MM-dd", Locale.ENGLISH).format(cal.getTime());

            }
        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
        dpd1.setMinDate(cal);

        cal.add(Calendar.DAY_OF_MONTH, 3);

        dpd1.setMaxDate(cal);

        dpd1.show(getFragmentManager(), "Datepickerdialog");
      /*  TimeDialog timeDialog=new TimeDialog();
        timeDialog.setOnItemSelectedListener(new TimeDialog.OnItemSelectedListener() {
            @Override
            public void onItemSelected(String Tim1e) {
                time.setText(Tim1e);
                TimeExpected=Tim1e;

            }
        });
        timeDialog.show(getFragmentManager(),"sfsdf");*/
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


                DrawRouteMaps.getInstance(DeliveryAddOfferActivity.this)
                        .draw(sender, receiver, mMap);

                CustomMarkerDrow.getInstance(DeliveryAddOfferActivity.this).draw(mMap, sender, getResources().getDrawable(  R.drawable.pin_delvary), "من "+sender_name);
                CustomMarkerDrow.getInstance(DeliveryAddOfferActivity.this).draw(mMap, receiver, getResources().getDrawable(  R.drawable.pin_delvary), "الى "+receiver_name);

                LatLngBounds bounds = new LatLngBounds.Builder()
                        .include(sender)
                        .include(receiver).build();
                Point displaySize = new Point();
                getWindowManager().getDefaultDisplay().getSize(displaySize);
                mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, displaySize.x, 250, 30));


            }
        });


    }

}
