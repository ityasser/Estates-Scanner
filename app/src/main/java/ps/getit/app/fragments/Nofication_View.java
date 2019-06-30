package ps.getit.app.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import com.android.volley.error.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import ps.getit.app.App;
import ps.getit.app.CustomAdapter.Nofication_View_Adapter;
import ps.getit.app.Dialogs.ProgressDialog;
import ps.getit.app.R;
import ps.getit.app.models.Notifications;
import ps.getit.app.utils.Utility;


/**
 * Created by Yasser on 4/2/2016.
 */
public class Nofication_View extends Fragment {
    public static final String TAGPtfile= "ViewNotfiaction";

public Nofication_View_Adapter adpterList;
    ArrayList<Notifications> ordertrakinglist = new ArrayList<>();

    @BindView(R.id.notfi_list)
    RecyclerView notfi_list;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_notfication, container, false);
       Utility.setTitle(getString(R.string.notfcation), (AppCompatActivity) getActivity());

        ButterKnife.bind(this, v);



        adpterList=new Nofication_View_Adapter(getActivity(),ordertrakinglist);

        notfi_list.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        notfi_list.setAdapter(adpterList);



        refresh();
        return v;
    }
    public AppCompatActivity getActivitty(){
        return (AppCompatActivity) getActivity();
    }

    public  void refresh(){
        if (Utility.isConnectingToInternet(getActivity())) {

            final ProgressDialog shwo=new ProgressDialog();
            shwo.show(getActivitty().getSupportFragmentManager(),"dfdf");

            StringRequest request = new StringRequest(Request.Method.POST, Utility.API_ROOT + "notifications", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    shwo.dismiss();

                    Log.i("response", response);
                    try {


                        ordertrakinglist.clear();
                       ArrayList<Notifications> ordest = new Gson().fromJson((new JSONArray(response)).toString(), new TypeToken<ArrayList<Notifications>>() {
                        }.getType());

                        ordertrakinglist.addAll(ordest);
                        adpterList.notifyDataSetChanged();





                   if(ordest.size()<1) {

                        Utility.showToast(R.string.no_result, false);

                    }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Utility.showToast(R.string.some_error, false);
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

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    Map<String, String> params = new HashMap<>();
                    params.put("token", Utility.getStringFromPref(getActivity(), "token"));
                    params.put("rest_id",Utility.getStringFromPref(App.getInstance(),"rest_id")) ;
                    params.put("lang",Utility.getLangDefult(getActivity()));

               /* params.put("password", pass);
                params.put("email",email);
                params.put("mobile", mobile);
*/
                    return params;

                }
            };

            App.getInstance().addToRequestQueue(request);
        } else {

            Utility.showErrorDialog(getActivity(), getString(R.string.no_internet));

        }



    }

    public static Nofication_View newInstance() {

        Bundle args = new Bundle();

        Nofication_View fragment = new Nofication_View();
        fragment.setArguments(args);
        return fragment;
    }
}
