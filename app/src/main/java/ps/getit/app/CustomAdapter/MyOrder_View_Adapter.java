package ps.getit.app.CustomAdapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;

import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ps.getit.app.App;
import ps.getit.app.R;
import ps.getit.app.activities.DetailsDdliveryRequestsActivity;
import ps.getit.app.activities.DetailsUserRequestsActivity;
import ps.getit.app.fragments.DriverResponsesFragment;
import ps.getit.app.models.orders;
import ps.getit.app.utils.Utility;


public class MyOrder_View_Adapter extends  RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private AppCompatActivity mcontext;
        private ArrayList<orders> items;

         private View lastView;

boolean isFinsh;
    public MyOrder_View_Adapter(AppCompatActivity context, ArrayList<orders> itemsf) {
            mcontext = context;
            items=itemsf;

          //  this.isFinsh=isFinsh;
        }


        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_my_order, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

            final MyViewHolder myViewHolder = (MyViewHolder) holder;
            final orders   item = items.get(position);


            myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (Utility.getStringFromPref(App.getInstance(), "is_admin").equals("0")) {

                        if(item.getStatus().equals("0")){
                           mcontext. getSupportFragmentManager().beginTransaction().addToBackStack("").replace(R.id.container, DriverResponsesFragment.newInstance(item.getId())).commit();
                           }else {
                            Intent i = new Intent(mcontext, DetailsUserRequestsActivity.class);
                            i.putExtra("id_Order", item.getId());
                            mcontext.startActivity(i);
                        }
                    }else {
                        Intent i = new Intent(mcontext, DetailsDdliveryRequestsActivity.class);
                        i.putExtra("id_Order", item.getId());
                        mcontext.startActivity(i);
                    }

                }
            });


            myViewHolder.name.setText("رقم الطلبية : "+item.getId());
            myViewHolder.date.setText(item.getCreated_at());
            myViewHolder.sender_name.setText(item.getSender_name());
            myViewHolder.price.setText(item.getPrice()+" "+Utility.getStringFromPref(mcontext,"currency","₪"));

            myViewHolder.time.setText(item.getTime());
          //  myViewHolder.dist.setText(""+Utility.distanceBetweenTwoLocation(Utility.getStringFromPref(mcontext, "last_lat", "0.0"),Utility.getStringFromPref(mcontext, "last_lng", "0.0"),item.getReceiver_lat()==null?item.getReceiver_lat():"0.0",item.getReceiver_lng()==null?item.getReceiver_lng():"0.0"));
           // myViewHolder.dist.setText(mcontext.getString(R.string.dist)+" "+Utility.distanceBetweenTwoLocation(Utility.getStringFromPref(mcontext, "last_lat", "0.0"),Utility.getStringFromPref(mcontext, "last_lng", "0.0"),item.getReceiver_lat()==null?item.getReceiver_lat():"0.0",item.getReceiver_lng()==null?item.getReceiver_lng():"0.0")+" "+mcontext.getString(R.string.KM));
            myViewHolder.dist.setText(mcontext.getString(R.string.dist)+" "+item.getDistance());

/*
            myViewHolder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new SweetAlertDialog(mcontext, SweetAlertDialog.WARNING_TYPE)
                            .setCancelText(mcontext.getString(R.string.cancel))
                            .setConfirmText(mcontext.getString(R.string.confirm))
                            .setContentText("هل انت متأكد من إلغاء الطلبية ")
                            .setTitleText("")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    delelte(item.getId());
                                    sweetAlertDialog.dismissWithAnimation();
                                }
                            }).show();
                }
            });*/





/*1 قيد الإستلام
2 تم الإستلام
3 بإنتظار بدأ التوصيل
4جاري التوصيل
5 تم التسليم*/

            if (Utility.getStringFromPref(App.getInstance(), "is_admin").equals("0")) {
               /* Glide.with(App.getInstance())
                        .load(Utility.SITE_ROOT + "uploads/category/" +  item.getService_image())
                        .error(R.drawable.placeholder)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(myViewHolder.cat_img);*/

                if (item.getStatus().equals("-1") || item.getStatus().equals("-2")) {
                    myViewHolder.state.setText("تم الإلغاء");
                    myViewHolder.state.setBackgroundResource(R.drawable.bac_read_radius_80);
                } else if(item.getStatus().equals("0")){
                        myViewHolder.state.setText("قيد الإنتظار");
                        myViewHolder.state.setBackgroundResource(R.drawable.bac_yaowll_radius_80);
                }else if(item.getStatus().equals("1")){
                    myViewHolder.state.setText("بإنتظار تأكيد السائق");
                    myViewHolder.state.setBackgroundResource(R.drawable.bac_yaowll_radius_80);
                }else if(item.getStatus().equals("2")){
                    myViewHolder.state.setText("قيد الإستلام");
                    myViewHolder.state.setBackgroundResource(R.drawable.bac_orange_radius_80);
                }else if(item.getStatus().equals("3")){
                    myViewHolder.state.setText("بإنتظار بدأ التوصيل");
                    myViewHolder.state.setBackgroundResource(R.drawable.bac_blue_radius_80);
                }else if(item.getStatus().equals("4")){
                    myViewHolder.state.setText("جاري التوصيل");
                    myViewHolder.state.setBackgroundResource(R.drawable.bac_orange_radius_80);
                }else if(item.getStatus().equals("5")){
                    myViewHolder.state.setText("تم التسليم");
                    myViewHolder.state.setBackgroundResource(R.drawable.bac_yaowll_radius_80);
                }

            }else {
               /* Glide.with(App.getInstance())
                        .load(Utility.SITE_ROOT + "uploads/users/" +  item.getUser_image())
                        .error(R.drawable.placeholder)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(myViewHolder.cat_img);
                myViewHolder.delete.setVisibility(View.GONE);*/


                if (item.getStatus().equals("-1") || item.getStatus().equals("-2")){
                    myViewHolder.state.setText("تم الإلغاء");
                    myViewHolder.state.setBackgroundResource(R.drawable.bac_read_radius_80);
                }else if(item.getStatus().equals("1")){
                    myViewHolder.state.setText("قيد الإستلام");
                    myViewHolder.state.setBackgroundResource(R.drawable.bac_yaowll_radius_80);
                }else if(item.getStatus().equals("2")){
                    myViewHolder.state.setText("تم الإستلام");
                    myViewHolder.state.setBackgroundResource(R.drawable.bac_orange_radius_80);
                }else if(item.getStatus().equals("3")){
                    myViewHolder.state.setText("بإنتظار بدأ التوصيل");
                    myViewHolder.state.setBackgroundResource(R.drawable.bac_blue_radius_80);
                }else if(item.getStatus().equals("4")){
                    myViewHolder.state.setText("جاري التوصيل");
                    myViewHolder.state.setBackgroundResource(R.drawable.bac_orange_radius_80);
                }else if(item.getStatus().equals("5")){
                    myViewHolder.state.setText("تم التسليم");
                    myViewHolder.state.setBackgroundResource(R.drawable.bac_yaowll_radius_80);
                }

            }


        }

        @Override
        public int getItemCount() {
            return items.size();
        }




        public class MyViewHolder extends RecyclerView.ViewHolder {

            TextView name,state,date,sender_name,dist,time,price;
            public MyViewHolder(View vi) {
                super(vi);
                dist=(TextView) vi.findViewById(R.id.dist);
                date= (TextView) vi.findViewById(R.id.date);
                state= (TextView) vi.findViewById(R.id.tx_state);
                name = (TextView) vi.findViewById(R.id.namec);
                sender_name=(TextView) vi.findViewById(R.id.tx_sender_name);
                time=(TextView) vi.findViewById(R.id.tx_time);
                price=(TextView) vi.findViewById(R.id.tx_price);


            }
        }




    }


