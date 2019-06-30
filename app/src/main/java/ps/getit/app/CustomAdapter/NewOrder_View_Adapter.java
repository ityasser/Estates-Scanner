package ps.getit.app.CustomAdapter;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ps.getit.app.App;
import ps.getit.app.Dialogs.BottomSheetDialog;
import ps.getit.app.R;
import ps.getit.app.activities.DeliveryAddOfferActivity;
import ps.getit.app.activities.DetailsDdliveryRequestsActivity;
import ps.getit.app.activities.DetailsUserRequestsActivity;
import ps.getit.app.activities.MainActivity;
import ps.getit.app.activities.TrackingParcelActivity;
import ps.getit.app.fragments.DriverResponsesFragment;
import ps.getit.app.models.orders;
import ps.getit.app.utils.Utility;


public class NewOrder_View_Adapter extends  RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private AppCompatActivity mcontext;
        private ArrayList<orders> items;

         private View lastView;


    public NewOrder_View_Adapter(AppCompatActivity context, ArrayList<orders> itemsf) {
            mcontext = context;
            items=itemsf;

        }


        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_new_order, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

            final MyViewHolder myViewHolder = (MyViewHolder) holder;
            final orders   item = items.get(position);



            myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(item.getStatus().equals("1") && item.getIs_delivery_confirm().equals("0")){
                        ( (MainActivity)mcontext). to_Coinferm(item.getId());
                        BottomSheetDialog show=new BottomSheetDialog();

                        show.setOnItemSelectedListener(new BottomSheetDialog.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(String cur) {

                                if(cur.equals("1")){
                                    Fun(item.getId(),"delivery_confirm_order",1);
                                }else if(cur.equals("2")){


                                }else if(cur.equals("3")){

                                    Fun(item.getId(),"delivery_reject_order",0);


                                }
                            }
                        });
                       // show.show(mcontext.getSupportFragmentManager(),"sed");

                    }else {
                    Intent i = new Intent(mcontext, DeliveryAddOfferActivity.class);
                    i.putExtra("sender_name", item.getSender_name());
                    i.putExtra("sender_address", item.getSender_address());
                    i.putExtra("order_id", item.getId());

                    i.putExtra("sender_lat", item.getSender_lat());
                    i.putExtra("sender_lng", item.getSender_lng());
                    i.putExtra("receiver_name", item.getReceiver_name());
                    i.putExtra("receiver_address", item.getReceiver_address());
                    i.putExtra("receiver_lat", item.getReceiver_lat());
                    i.putExtra("receiver_lng", item.getReceiver_lng());
                    i.putExtra("details", item.getDetails());

                    i.putExtra("sender_city_name", item.getSender_city_name());
                    i.putExtra("sender_section_name", item.getSender_section_name());
                    i.putExtra("receiver_city_name", item.getReceiver_city_name());
                    i.putExtra("receiver_section_name", item.getReceiver_section_name());



                        mcontext.startActivity(i);

                }
/*
                    if (Utility.getStringFromPref(App.getInstance(), "is_admin").equals("0")) {
                        mcontext. getSupportFragmentManager().beginTransaction().addToBackStack("").replace(R.id.container, DriverResponsesFragment.newInstance(item.getId())).commit();

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
*/
                }
            });
            myViewHolder.itemView.setBackgroundColor(Color.parseColor("#ffffff"));
            if (item.getStatus().equals("-1") || item.getStatus().equals("-2")){
                myViewHolder.tx_state_text.setText("تم الإلغاء");
            } else if(item.getStatus().equals("0") && item.getOffer_id()!=null && !item.getOffer_id().equals("0") ){
                myViewHolder.tx_state_text.setText("تم ارسال عرض سعر");
            }else if(item.getStatus().equals("0")){
                myViewHolder.tx_state_text.setText("ارسل عرض سعر");
                myViewHolder.itemView.setBackgroundColor(Color.parseColor("#FFC107"));

            }else if(item.getStatus().equals("1")&& item.getIs_delivery_confirm().equals("0")){
                myViewHolder.tx_state_text.setText("بإنتظار التأكيد");
            }else if(item.getStatus().equals("1")){
                myViewHolder.tx_state_text.setText("قيد الإستلام");
            }else if(item.getStatus().equals("2")){
                myViewHolder.tx_state_text.setText("تم الإستلام");
            }else if(item.getStatus().equals("3")){
                myViewHolder.tx_state_text.setText("بإنتظار بدأ التوصيل");
            }else if(item.getStatus().equals("4")){
                myViewHolder.tx_state_text.setText("جاري التوصيل");
            }else if(item.getStatus().equals("5")){
                myViewHolder.tx_state_text.setText("تم التسليم");
            }



            myViewHolder.name.setText("رقم الطلبية : "+item.getId());
            myViewHolder.date.setText(item.getCreated_at());
            myViewHolder.state.setVisibility(View.VISIBLE);

            if(item.getWeight().equals("0")){
                myViewHolder.state.setText("طرد خفيف");

            }else if(item.getWeight().equals("1")){
                myViewHolder.state.setText("طرد ثقيل");

            }else if(item.getWeight().equals("2")){
                myViewHolder.state.setText("طرد مفتوح");

            }else {
                myViewHolder.state.setVisibility(View.GONE);
            }


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







        }

        @Override
        public int getItemCount() {
            return items.size();
        }




        public void Fun(final String order_id, String fun, final int isConferm){
            Map<String, String> params = new HashMap<>() ;

            params.put("order_id",order_id);

            new App.CustomRequest(mcontext,fun,params) {
                @Override
                public void onSuccess(JSONObject obj) {

                    Utility.showToast(obj.optString("message"), true);


                    if(isConferm==1){
                        Intent i = new Intent(mcontext, DetailsDdliveryRequestsActivity.class);
                        i.putExtra("id_Order", order_id);
                        mcontext.startActivity(i);
                    }


                }



            };
        }
        public class MyViewHolder extends RecyclerView.ViewHolder {

            TextView name,state,tx_state_text,date;
            public MyViewHolder(View vi) {
                super(vi);
                date= (TextView) vi.findViewById(R.id.time);
                state= (TextView) vi.findViewById(R.id.tx_state);
                tx_state_text= (TextView) vi.findViewById(R.id.tx_state_text);

                name = (TextView) vi.findViewById(R.id.namec);


            }
        }




    }


