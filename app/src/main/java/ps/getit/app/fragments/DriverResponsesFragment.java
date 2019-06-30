package ps.getit.app.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import ps.getit.app.CustomAdapter.Driver_Offers_View_Adapter;
import ps.getit.app.CustomAdapter.MyOrder_View_Adapter;
import ps.getit.app.Dialogs.ActivateAccount;
import ps.getit.app.R;
import ps.getit.app.activities.LoginActivity;
import ps.getit.app.activities.MainActivity;
import ps.getit.app.gcm.MyFirebaseMessagingService;
import ps.getit.app.models.OffersDelivery;
import ps.getit.app.models.orders;
import ps.getit.app.utils.Utility;


public class DriverResponsesFragment extends Fragment {

ArrayList<OffersDelivery> servicesList =new ArrayList<>();

    public static DriverResponsesFragment newInstance(String order_id) {
        DriverResponsesFragment fragment = new DriverResponsesFragment();
        Bundle bundle =new Bundle();
        bundle.putString("order_id",order_id);
        fragment.setArguments(bundle);
        return fragment;
    }

    @BindView(R.id.service_list)
    RecyclerView service_list;

    @BindView(R.id.SwipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;


    String order_id="";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    Driver_Offers_View_Adapter services_view_adapter;
    public AppCompatActivity getActivitty(){
        return (AppCompatActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_driver_responses, container, false);
        ButterKnife.bind(this, view);

        Utility.setTitle(getString(R.string.driver_responses), (AppCompatActivity) getActivity());


        Bundle bundle = this.getArguments();
        if (bundle != null) {

            order_id = bundle.getString("order_id");

        }
        swipeRefreshLayout.setColorScheme(R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                                                    @Override
                                                    public void onRefresh() {
                                                        swipeRefreshLayout.setRefreshing(true);
                                                        getRests();
                                                    }
                                                });



                getRests();

        return view;


    }


    @OnClick(R.id.go_to_next)
    public void go_to_next(){

        Log.d("id_delvary",Driver_Offers_View_Adapter.IdOfferSelcted);



            HashMap params = new HashMap();

            params.put("order_id", order_id);
        params.put("offer_id", Driver_Offers_View_Adapter.IdOfferSelcted);


            new App.CustomRequest(getActivitty(), "accept_offer", params) {
                @Override
                public void onSuccess(JSONObject jsonObject) {


                    Utility.showToast(jsonObject.optString("message"),true);

                    getActivitty().getSupportFragmentManager().beginTransaction().addToBackStack("").replace(R.id.container, MyOrderFragment.newInstance()).commit();


                }




            };
        }


    public void getRests(){

        Map<String, String> params = new HashMap<>() ;
        params.put("order_id",order_id);
        new App.CustomRequest(getActivitty(),"view_delivery_offers",params) {


            @Override
            public void onError(String response) {
                super.onError(response);
                swipeRefreshLayout.setRefreshing(false);

            }

            @Override
            public void onSuccess(JSONObject obj) {

              //  Utility.showToast(jsonObject.optString("message"), true)
                servicesList = new Gson().fromJson((obj.optJSONArray("offers")).toString(), new TypeToken<ArrayList<OffersDelivery>>() {
                }.getType());

                if (servicesList.size() < 1) {
                    Utility.showToast(R.string.no_result, false);
                } else {
                    int numberOfColumns = 1;
                    service_list.setLayoutManager(new GridLayoutManager(getActivity(), numberOfColumns));
                    services_view_adapter = new Driver_Offers_View_Adapter(getActivitty(), servicesList);
                    service_list.setAdapter(services_view_adapter);

                }
                swipeRefreshLayout.setRefreshing(false);

            }



        };





    }

    @Override
    public void onDetach() {

        super.onDetach();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }


}
