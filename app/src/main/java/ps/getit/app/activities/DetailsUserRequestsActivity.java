package ps.getit.app.activities;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
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
import de.hdodenhof.circleimageview.CircleImageView;
import ps.getit.app.App;
import ps.getit.app.Dialogs.ViewImage;
import ps.getit.app.R;
import ps.getit.app.models.Transactions;
import ps.getit.app.models.points;
import ps.getit.app.utils.Utility;

public class DetailsUserRequestsActivity extends BaseActivity {

    @BindView(R.id.order_id1)
    TextView order_id1;

    String id_Order = "";

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

    @BindView(R.id.orfertr)
    TextView orfertr;


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

    @BindView(R.id.image)
    CircleImageView image;



    String delivery_id="",sender_name = "", sender_lat = "0.0", sender_lng = "0.0", receiver_name = "", receiver_lat = "0.0", receiver_lng = "0.0", driver_lat = "0.0", driver_lng = "0.0", driver_mobile = "", driver_name = "";
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
        setContentView(R.layout.activity_details_user_requests);
        ButterKnife.bind(this);

        Utility.setTitle("حالة الطرد", this);
        if (getIntent().getExtras() != null)
            id_Order = getIntent().getExtras().getString("id_Order");

        order_id1.setText("رقم الطلبية : "+id_Order);


        Map<String, String> params = new HashMap<>();
        params.put("order_id", id_Order);
        new App.CustomRequest(this, "get_order_details", params) {
            @Override
            public void onSuccess(JSONObject obj) {

                try {
                    final JSONObject data = new JSONObject(obj.optJSONObject("data").toString());
                    details.setText(data.optString("details"));

                    sender_lat = data.optString("sender_lat");
                    sender_lng = data.optString("sender_lng");
                    receiver_lat = data.optString("receiver_lat");
                    receiver_lng = data.optString("receiver_lng");
                    sender_name = data.optString("sender_name");
                    receiver_name = data.optString("receiver_name");

                    sendername.setText(sender_name);
                    recipient_name.setText(receiver_name);

                    sender_addresst.setText(data.optString("sender_city_name")+"\n"+data.optString("sender_section_name")+"\n"+data.optString("sender_address"));
                    recipient_address.setText(data.optString("receiver_city_name")+"\n"+data.optString("receiver_section_name")+"\n"+data.optString("receiver_address"));


                    Log.d("image",Utility.SITE_ROOT+"uploads/orders/"+ data.optString("image"));
                    Glide.with(App.getInstance())
                            .load(  Utility.SITE_ROOT+"uploads/orders/"+ data.optString("image"))
                            .error(R.drawable.placeholder)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(image);


                    image.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ViewImage bb= new ViewImage();
                            Bundle b=new Bundle();
                            b.putString("image",Utility.SITE_ROOT+"uploads/orders/"+ data.optString("image"));
                            bb.setArguments(b);
                            bb.show(getSupportFragmentManager(),"sd");
                        }
                    });

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


                    image.setVisibility(View.GONE);
                    if (transactions.size() == 1) {
                        img_stage0.setImageResource(R.drawable.stage);
                        time0.setText(transactions.get(0).getDate());
                    } else if (transactions.size() == 2) {
                        img_stage0.setImageResource(R.drawable.stage);
                        img_stage1.setImageResource(R.drawable.stage);


                        time0.setText(transactions.get(0).getDate());
                        time1.setText(transactions.get(1).getDate());

                    } else if (transactions.size() == 3) {
                        img_stage0.setImageResource(R.drawable.stage);
                        img_stage1.setImageResource(R.drawable.stage);
                        img_stage2.setImageResource(R.drawable.stage);


                        time0.setText(transactions.get(0).getDate());
                        time1.setText(transactions.get(1).getDate());
                        time2.setText(transactions.get(2).getDate());



                    } else if (transactions.size() == 4) {
                        img_stage0.setImageResource(R.drawable.stage);
                        img_stage1.setImageResource(R.drawable.stage);
                        img_stage2.setImageResource(R.drawable.stage);
                        img_stage3.setImageResource(R.drawable.stage);

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

                        time0.setText(transactions.get(0).getDate());
                        time1.setText(transactions.get(1).getDate());
                        time2.setText(transactions.get(2).getDate());
                        time3.setText(transactions.get(3).getDate());
                        time4.setText(transactions.get(4).getDate());


                    }else if (transactions.size() == 6) {
                        img_stage0.setImageResource(R.drawable.stage);
                        img_stage1.setImageResource(R.drawable.stage);
                        img_stage2.setImageResource(R.drawable.stage);
                        img_stage3.setImageResource(R.drawable.stage);
                        img_stage4.setImageResource(R.drawable.stage);
                        img_stage5.setImageResource(R.drawable.stage1);


                        time0.setText(transactions.get(0).getDate());
                        time1.setText(transactions.get(1).getDate());
                        time2.setText(transactions.get(2).getDate());
                        time3.setText(transactions.get(3).getDate());
                        time4.setText(transactions.get(4).getDate());
                        time5.setText(transactions.get(5).getDate());

                        orfertr.setVisibility(View.GONE);
                        image.setVisibility(View.VISIBLE);
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
        i.putExtra("driver_mobile",driver_mobile);
        i.putExtra("driver_name",driver_name);
        i.putExtra("driver_estimated_time",driver_estimated_time);
        i.putExtra("delivery_id",delivery_id);
        i.putExtra("order_id",id_Order);


startActivity(i);

}
}
