package ps.getit.app.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.intentfilter.androidpermissions.PermissionManager;
import com.yanzhenjie.album.Action;
import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.AlbumFile;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.ParseException;
import de.hdodenhof.circleimageview.CircleImageView;
import ps.getit.app.App;
import ps.getit.app.Dialogs.PamentDialog;
import ps.getit.app.Dialogs.ProgressDialog;
import ps.getit.app.R;
import ps.getit.app.models.Transactions;
import ps.getit.app.utils.Utility;

public class DetailsDdliveryRequestsActivity extends BaseActivity {

    String id_Order="";

    String imgPrfile="";

    @BindView(R.id.image)
    CircleImageView image;

    @BindView(R.id.order_id1)
    TextView order_id1;


    ArrayList<Transactions> transactions;


    @BindView(R.id.details)
    TextView details;

    @BindView(R.id.img_stage0)
    ImageView img_stage0;

    @BindView(R.id.img_stage1)
    ImageView img_stage1;

    @BindView(R.id.img_stage2)
    ImageView img_stage2;

    @BindView(R.id.img_stage3)
    ImageView img_stage3;

    @BindView(R.id.img_stage4)
    ImageView img_stage4;

    @BindView(R.id.img_stage5)
    ImageView img_stage5;

    @BindView(R.id.time0)
    TextView time0;

    @BindView(R.id.time1)
    TextView time1;

    @BindView(R.id.time2)
    TextView time2;

    @BindView(R.id.time3)
    TextView time3;

    @BindView(R.id.time4)
    TextView time4;

    @BindView(R.id.time5)
    TextView time5;

    @BindView(R.id.layR1)
    RelativeLayout layR1;

    @BindView(R.id.layR2)
    RelativeLayout layR2;

    @BindView(R.id.layR3)
    RelativeLayout layR3;

    @BindView(R.id.layR4)
    RelativeLayout layR4;

    @BindView(R.id.layR5)
    RelativeLayout layR5;


    @BindView(R.id.estalm)
    TextView estalm;

    @BindView(R.id.delivery_begin_order)
    TextView delivery_begin_order;

    @BindView(R.id.orfertr)
    TextView orfertr;



    String delivery_id="",sender_mobile="",receiver_mobile="",sender_name = "", sender_lat = "0.0", sender_lng = "0.0", receiver_name = "", receiver_lat = "0.0", receiver_lng = "0.0", driver_lat = "0.0", driver_lng = "0.0", driver_mobile = "", driver_name = "";
    int driver_estimated_time = 0;

    @Nullable
    @BindView(R.id.sender_address)
    TextView sender_addresst;
    @Nullable
    @BindView(R.id.recipient_address)
    TextView recipient_address;


    @Nullable
    @BindView(R.id.sender_name)
    TextView sendername;

    @Nullable
    @BindView(R.id.recipient_name)
    TextView recipient_name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_ddlivery_requests);
        ButterKnife.bind(this);

        Utility.setTitle("حالة الطرد", this);

        if (getIntent().getExtras()!=null)
            id_Order= getIntent().getExtras().getString("id_Order");


        order_id1.setText("رقم الطلبية : "+id_Order);

        getData();
    }


    public void getData(){
        Map<String, String> params = new HashMap<>();
        params.put("order_id", id_Order);
        new App.CustomRequest(this, "get_order_details_delivery", params) {
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
                    receiver_mobile=data.optString("receiver_mobile");

                    sendername.setText(sender_name);
                    recipient_name.setText(receiver_name);

                    sender_addresst.setText(data.optString("sender_city_name")+"\n"+data.optString("sender_section_name")+"\n"+data.optString("sender_address"));
                    recipient_address.setText(data.optString("receiver_city_name")+"\n"+data.optString("receiver_section_name")+"\n"+data.optString("receiver_address"));

                    Glide.with(App.getInstance())
                            .load(  Utility.SITE_ROOT+"uploads/orders/"+ data.optString("image"))
                            .error(R.drawable.take_photo)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(image);

                    if (data.has("driver")){
                        JSONObject driver = new JSONObject(data.optJSONObject("driver").toString());
                        driver_lat = driver.optString("lat");
                        driver_lng = driver.optString("lng");
                        driver_mobile = driver.optString("mobile");
                        Log.d("driver_mobile","llllllll  "+driver_mobile);

                        driver_estimated_time = driver.optInt("estimated_time");
                        driver_name = driver.optString("name");

                        delivery_id= driver.optString("id");
                    }
                    ;

                    transactions = new Gson().fromJson(data.optJSONArray("transactions").toString(), new TypeToken<ArrayList<Transactions>>() {
                    }.getType());


                    if (transactions.size() == 1) {
                        img_stage0.setImageResource(R.drawable.stage);
                        time0.setText(transactions.get(0).getDate());
                        delivery_begin_order.setVisibility(View.VISIBLE);
                        estalm.setVisibility(View.VISIBLE);

                    } else if (transactions.size() == 2) {
                        img_stage0.setImageResource(R.drawable.stage);
                        img_stage1.setImageResource(R.drawable.stage);


                        time0.setText(transactions.get(0).getDate());
                        time1.setText(transactions.get(1).getDate());
                        delivery_begin_order.setVisibility(View.VISIBLE);
                        estalm.setVisibility(View.VISIBLE);
                    } else if (transactions.size() == 3) {
                        img_stage0.setImageResource(R.drawable.stage);
                        img_stage1.setImageResource(R.drawable.stage);
                        img_stage2.setImageResource(R.drawable.stage);

                        time0.setText(transactions.get(0).getDate());
                        time1.setText(transactions.get(1).getDate());
                        time2.setText(transactions.get(2).getDate());


                        delivery_begin_order.setVisibility(View.VISIBLE);
                        estalm.setVisibility(View.VISIBLE);

                    } else if (transactions.size() == 4) {
                        img_stage0.setImageResource(R.drawable.stage);
                        img_stage1.setImageResource(R.drawable.stage);
                        img_stage2.setImageResource(R.drawable.stage);
                        img_stage3.setImageResource(R.drawable.stage);

                        estalm.setVisibility(View.GONE);
                        delivery_begin_order.setVisibility(View.VISIBLE);
                        time0.setText(transactions.get(0).getDate());
                        time1.setText(transactions.get(1).getDate());
                        time2.setText(transactions.get(2).getDate());
                        time3.setText(transactions.get(3).getDate());



                    } else if (transactions.size() == 5) {
                        img_stage0.setImageResource(R.drawable.stage);
                        img_stage1.setImageResource(R.drawable.stage);
                        img_stage2.setImageResource(R.drawable.stage);
                        img_stage3.setImageResource(R.drawable.stage);
                        img_stage4.setImageResource(R.drawable.stage);

                        estalm.setVisibility(View.GONE);
                        delivery_begin_order.setVisibility(View.GONE);

                        time0.setText(transactions.get(0).getDate());
                        time1.setText(transactions.get(1).getDate());
                        time2.setText(transactions.get(2).getDate());
                        time3.setText(transactions.get(3).getDate());
                        time4.setText(transactions.get(4).getDate());

                        estalm.setVisibility(View.GONE);
                        delivery_begin_order.setVisibility(View.GONE);

                    }else if (transactions.size() == 6) {
                        img_stage0.setImageResource(R.drawable.stage);
                        img_stage1.setImageResource(R.drawable.stage);
                        img_stage2.setImageResource(R.drawable.stage);
                        img_stage3.setImageResource(R.drawable.stage);
                        img_stage4.setImageResource(R.drawable.stage);
                        img_stage5.setImageResource(R.drawable.stage1);
                        orfertr.setVisibility(View.GONE);

                        time0.setText(transactions.get(0).getDate());
                        time1.setText(transactions.get(1).getDate());
                        time2.setText(transactions.get(2).getDate());
                        time3.setText(transactions.get(3).getDate());
                        time4.setText(transactions.get(4).getDate());
                        time5.setText(transactions.get(5).getDate());

                        estalm.setVisibility(View.GONE);
                        delivery_begin_order.setVisibility(View.GONE);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }


        };
    }
    @OnClick(R.id.orfertr)
    public void orfertr() {

        Intent i =new Intent(this,TrackingParcelActivity.class);
        i.putExtra("sender_name",sender_name);
        i.putExtra("sender_lat",sender_lat);
        i.putExtra("sender_lng",sender_lng);
        i.putExtra("receiver_name",receiver_name);
        i.putExtra("receiver_lat",receiver_lat);
        i.putExtra("receiver_lng",receiver_lng);
        i.putExtra("driver_lat",driver_lat);
        i.putExtra("driver_lng",driver_lng);
        i.putExtra("driver_mobile",receiver_mobile);
        i.putExtra("driver_name",driver_name);
        i.putExtra("driver_estimated_time",driver_estimated_time);
        i.putExtra("delivery_id",delivery_id);
        i.putExtra("order_id",id_Order);




        startActivity(i);

    }
    @OnClick(R.id.go_to_next)
    public void go_to_next() {


        if(imgPrfile.isEmpty()){
            Utility.showToast("الرجاء اضافة صورة",false);
        }else {
            PamentDialog vv=new PamentDialog();
            vv.setOnItemSelectedListener(new PamentDialog.OnItemSelectedListener() {
                @Override
                public void onItemSelected(String cur) {
                    try {
                        post_Reg( cur) ;
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            });
            vv.show(getSupportFragmentManager(),"dfdf");
        }

    }


    public void post_Reg(String pay_type)throws FileNotFoundException {
        final ProgressDialog shwo=new ProgressDialog();
        shwo.show(getSupportFragmentManager(),"dfd");

        SimpleMultiPartRequest request = new SimpleMultiPartRequest(Request.Method.POST, Utility.API_ROOT + "delivery_finish_order", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                shwo.dismissAllowingStateLoss();
                Log.i("response", response);
                JSONObject jsonObject = null;
                try {

                    jsonObject = new JSONObject(response);
                    if (Utility.checkRequestStatus(jsonObject)) {
                        getData();
                        Utility.showToast(jsonObject.getString("message"), true);

                    }else if (jsonObject.has("message")) {

                        Utility.showToast(jsonObject.getString("message"), false);

                    } else {

                        Utility.showToast(getString(R.string.some_error), false);

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(DetailsDdliveryRequestsActivity.this, e.getMessage() , Toast.LENGTH_LONG).show();


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
        request.addStringParam("pay_type", pay_type);
        request.addStringParam("order_id",id_Order);
        request.addStringParam("token", Utility.getStringFromPref(DetailsDdliveryRequestsActivity.this, "token"));
        if(imgPrfile.length()>2) {
            request.addFile("image[]", imgPrfile);
        }

        request.setRetryPolicy(new DefaultRetryPolicy(
                Utility.REQ_T_OUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        App.getInstance().addToRequestQueue(request);




    }


    @OnClick(R.id.estalm)
    public void estalm() {
        Map<String, String> params = new HashMap<>() ;
        params.put("order_id",id_Order);
        new App.CustomRequest(DetailsDdliveryRequestsActivity.this,"delivery_catch_order",params) {
            @Override
            public void onSuccess(JSONObject obj) {

                Utility.showToast(obj.optString("message"), true);

                getData();

            }



        };
    }
    @OnClick(R.id.delivery_begin_order)
    public void delivery_begin_order() {

        Map<String, String> params = new HashMap<>() ;
        params.put("order_id",id_Order);
        new App.CustomRequest(DetailsDdliveryRequestsActivity.this,"delivery_begin_order",params) {
            @Override
            public void onSuccess(JSONObject obj) {

                getData();
                Utility.showToast(obj.optString("message"), true);



            }



        };
    }


    @OnClick(R.id.image)
    public void image() {
        List<String> permissionsNeeded2 = new ArrayList<String>();
        permissionsNeeded2.add(android.Manifest.permission.READ_EXTERNAL_STORAGE);
        permissionsNeeded2.add(android.Manifest.permission.CAMERA);

        PermissionManager permissionManager2 = PermissionManager.getInstance(DetailsDdliveryRequestsActivity.this);
        permissionManager2.checkPermissions(permissionsNeeded2, new PermissionManager.PermissionRequestListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onPermissionGranted() {
                Album.image(DetailsDdliveryRequestsActivity.this) // Image selection.
                        .singleChoice()
                        .camera(true)
                        .columnCount(2)

                        .onResult(new Action<ArrayList<AlbumFile>>() {
                            @Override
                            public void onAction(@NonNull ArrayList<AlbumFile> result) {


                                imgPrfile = result.get(0).getPath();
                                Glide.with(DetailsDdliveryRequestsActivity.this)
                                        .load(imgPrfile)
                                        .error(R.drawable.placeholder)
                                        // .placeholder(R.drawable.placeholder)
                                        .crossFade()
                                        .into(image);
                            }
                        })
                        .onCancel(new Action<String>() {
                            @Override
                            public void onAction(@NonNull String result) {
                                imgPrfile = "";
                            }
                        })
                        .start();

            }

            @Override
            public void onPermissionDenied() {

            }
        });
    }
}
