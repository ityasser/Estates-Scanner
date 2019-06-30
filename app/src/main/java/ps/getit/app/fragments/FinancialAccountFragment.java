package ps.getit.app.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import ps.getit.app.App;
import ps.getit.app.CustomAdapter.Credit_View_Adapter;
import ps.getit.app.CustomAdapter.FinancialAccount_View_Adapter;
import ps.getit.app.Dialogs.ProgressDialog;
import ps.getit.app.R;
import ps.getit.app.models.CreditLog;
import ps.getit.app.models.FinancialAccount;
import ps.getit.app.utils.Utility;


public class FinancialAccountFragment extends Fragment {

    public static FinancialAccountFragment newInstance() {
        FinancialAccountFragment fragment = new FinancialAccountFragment();
        return fragment;
    }
    ArrayList<FinancialAccount> ordertrakinglist = new ArrayList<>();


    @BindView(R.id.notfi_list)
    RecyclerView notfi_list;

    FinancialAccount_View_Adapter adpterList;


    @BindView(R.id.cash_total)
    TextView cash_total;

    @BindView(R.id.card_total)
    TextView card_total;

    @BindView(R.id.name)
    TextView name;

    @BindView(R.id.on_you)
    TextView on_you;

    @BindView(R.id.on_you_total)
    TextView on_you_total;

    @BindView(R.id.on_you_total_lay)
    LinearLayout on_you_total_lay;


    String is_admin="";
    public AppCompatActivity getActivitty(){
        return (AppCompatActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_financial_account, container, false);
        // getFragmentManager().popBackStack()

        ButterKnife.bind(this, view);
        is_admin=Utility.getStringFromPref(getActivity(), "is_admin");

        if(is_admin.equals("0")){
            name.setText("اسم السائق");
            on_you.setVisibility(View.GONE    );
            on_you_total_lay.setVisibility(View.GONE);
        }else {
            name.setText("اسم الزبون");
            on_you.setVisibility(View.VISIBLE    );
            on_you_total_lay.setVisibility(View.VISIBLE);

        }
        adpterList=new FinancialAccount_View_Adapter(getActivitty(),ordertrakinglist,is_admin);

        notfi_list.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        notfi_list.setAdapter(adpterList);



        getRests();


        return view;
    }


    public void getRests(){

        final ProgressDialog shwoz=new ProgressDialog();
        shwoz.setCancelable(false);
        shwoz.show(getActivitty().getSupportFragmentManager(),"dfdf");
        if (Utility.isConnectingToInternet(getActivity())) {

            String url="";
            if (Utility.getStringFromPref(getActivity(), "is_admin").equals("0")) {
                url="get_orders_credit_log";
            }else {
                url="get_orders_credit_log";
            }
            StringRequest request = new StringRequest(Request.Method.POST, Utility.API_ROOT + url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    shwoz.dismissAllowingStateLoss();

                    Log.i("getMarkets", response);
                    try {
                        JSONObject obj = new JSONObject(response);

                        if(Utility.checkRequestStatus(response)) {


                            ordertrakinglist.clear();
                            ArrayList<FinancialAccount> ordest = new Gson().fromJson(obj.optJSONArray("orders").toString(), new TypeToken<ArrayList<FinancialAccount>>() {
                            }.getType());

                            ordertrakinglist.addAll(ordest);
                            adpterList.notifyDataSetChanged();


                            cash_total.setText(obj.optJSONObject("data").optString("cash_total")+Utility.getStringFromPref(getActivity(),"currency","₪"));
                            card_total.setText(obj.optJSONObject("data").optString("card_total")+Utility.getStringFromPref(getActivity(),"currency","₪"));
                            on_you_total.setText(obj.optJSONObject("data").optString("on_you_total")+Utility.getStringFromPref(getActivity(),"currency","₪"));
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
                    shwoz.dismissAllowingStateLoss();
                    error.printStackTrace();
                    Utility.showToast(R.string.connection_error, false);

                }
            }) {

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    Map<String, String> params = new HashMap<>();
                    params.put("token", Utility.getStringFromPref(getActivity(), "token"));
                    params.put("lang",Utility.getLangDefult(getActivity()));




                    return params;

                }
            };

            App.getInstance().addToRequestQueue(request);
        } else {

            shwoz.dismissAllowingStateLoss();
            Utility.showErrorDialog(getActivity(), getString(R.string.no_internet));

        }

    }






}
