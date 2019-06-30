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
import butterknife.OnClick;
import ps.getit.app.App;
import ps.getit.app.CustomAdapter.PointsPrizes_View_Adapter;
import ps.getit.app.CustomAdapter.PointsSale_View_Adapter;
import ps.getit.app.Dialogs.ProgressDialog;
import ps.getit.app.R;
import ps.getit.app.models.Prizes;
import ps.getit.app.models.points;
import ps.getit.app.utils.Utility;


public class PointsSaleFragment extends Fragment {

    public static PointsSaleFragment newInstance() {
        PointsSaleFragment fragment = new PointsSaleFragment();
        return fragment;
    }
    ArrayList<points> ordertrakinglist = new ArrayList<>();


    @BindView(R.id.order_traking_list)
    RecyclerView notfi_list;

    PointsSale_View_Adapter adpterList;

    public AppCompatActivity getActivitty(){
        return (AppCompatActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_point_of_sale, container, false);
        // getFragmentManager().popBackStack()

        ButterKnife.bind(this, view);

        adpterList=new PointsSale_View_Adapter(getActivitty(),ordertrakinglist);

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


            StringRequest request = new StringRequest(Request.Method.POST, Utility.API_ROOT + "get_sales_points", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    shwoz.dismissAllowingStateLoss();

                    Log.i("getMarkets", response);
                    try {
                        JSONObject obj = new JSONObject(response);

                        if(Utility.checkRequestStatus(response)) {


                            ordertrakinglist.clear();
                            ArrayList<points> ordest = new Gson().fromJson(obj.optJSONArray("points").toString(), new TypeToken<ArrayList<points>>() {
                            }.getType());

                            ordertrakinglist.addAll(ordest);
                            adpterList.notifyDataSetChanged();

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


    @OnClick(R.id.go_to_next)
    public void CreditFragment() {
        getActivitty(). getSupportFragmentManager().beginTransaction().addToBackStack("").replace(R.id.container, CreditFragment.newInstance()).commit();

    }


    @OnClick(R.id.log_credit)
    public void log_credit() {
        getActivitty(). getSupportFragmentManager().beginTransaction().addToBackStack("").replace(R.id.container, ViewCreditFragment.newInstance()).commit();

    }



}
