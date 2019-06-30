package ps.getit.app.fragments;


import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import ps.getit.app.CustomAdapter.MyOrder_View_Adapter;
import ps.getit.app.R;
import ps.getit.app.activities.MainActivity;
import ps.getit.app.models.orders;
import ps.getit.app.utils.Utility;


public class MyOrderFragment extends Fragment {

ArrayList<orders> servicesList =new ArrayList<>();

    public static MyOrderFragment newInstance() {
        MyOrderFragment fragment = new MyOrderFragment();
        return fragment;
    }

    @BindView(R.id.service_list)
    RecyclerView service_list;

    @BindView(R.id.SwipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    MyOrder_View_Adapter services_view_adapter;

    public AppCompatActivity getActivitty(){
        return (AppCompatActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_services, container, false);
        ButterKnife.bind(this, view);

        Utility.setTitle(getString(R.string.my_order), (AppCompatActivity) getActivity());
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

    public void getRests(){
        String url="";
        if (Utility.getStringFromPref(getActivity(), "is_admin").equals("0")) {
            url="get_user_orders";
        }else {
            url="get_delivery_orders";

        }
        Map<String, String> params = new HashMap<>() ;
        new App.CustomRequest(getActivitty(),url,params) {


            @Override
            public void onError(String response) {
                super.onError(response);
                swipeRefreshLayout.setRefreshing(false);

            }

            @Override
            public void onSuccess(JSONObject obj) {

              //  Utility.showToast(jsonObject.optString("message"), true)
                servicesList = new Gson().fromJson((obj.optJSONArray("orders")).toString(), new TypeToken<ArrayList<orders>>() {
                }.getType());

                if (servicesList.size() < 1) {
                    Utility.showToast(R.string.no_result, false);
                } else {
                    int numberOfColumns = 1;
                    service_list.setLayoutManager(new GridLayoutManager(getActivity(), numberOfColumns));
                    services_view_adapter = new MyOrder_View_Adapter(getActivitty(), servicesList);
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
