package points.comparison.app.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.NetworkError;
import com.android.volley.error.NoConnectionError;
import com.android.volley.error.ParseError;
import com.android.volley.error.ServerError;
import com.android.volley.error.TimeoutError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import points.comparison.app.App;
import points.comparison.app.CustomAdapter.Properties_View_Adapter;
import points.comparison.app.Dialogs.ProgressDialog;
import points.comparison.app.R;
import points.comparison.app.models.Properties;
import points.comparison.app.models.Result;
import points.comparison.app.utils.Utility;


public class ViewPropertiesFragment extends Fragment {

    public static String places="places";
    public static String properties="properties";

    public int totalSelction=0,totalRuslut=0;
    public String region1="",region2="";
    Properties_View_Adapter adpterList;

    public static ViewPropertiesFragment newInstance() {
        ViewPropertiesFragment fragment = new ViewPropertiesFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

   public static ArrayList<Properties> listPerties=new ArrayList<>();

    public static ArrayList<Properties> listPertiesSelection=new ArrayList<>();

    ArrayList<Result> listResult=new ArrayList<>();




    @BindView(R.id.notfi_list)
    RecyclerView notfi_list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fargment_properties, container, false);
        ButterKnife.bind(this, view);

        listPertiesSelection.clear();
        listResult.clear();
        listPerties.clear();

        Bundle bundle = this.getArguments();
        if (bundle != null) {

            region1 =  bundle.getString("region1");
            region2 = bundle.getString("region2");


        }


        listPerties.add(new Properties("Choose Places ","0","0",false));

        listPerties.add(new Properties("schools",places,"0",false));
        listPerties.add(new Properties("hospitals",places,"0",false));
        listPerties.add(new Properties("clubs",places,"0",false));
        listPerties.add(new Properties("theaters",places,"0",false));
        listPerties.add(new Properties("super markets",places,"0",false));
        listPerties.add(new Properties("art galleries",places,"0",false));
        listPerties.add(new Properties("universities",places,"0",false));
        listPerties.add(new Properties("bus stations",places,"0",false));
        listPerties.add(new Properties("museums",places,"0",false));

        listPerties.add(new Properties("Choose Properties","0","0",false));
        listPerties.add(new Properties("risk and crimes",properties,"0",false));
        listPerties.add(new Properties("cleanliness",properties,"0",false));
        listPerties.add(new Properties("crowdedness",properties,"0",false));
        listPerties.add(new Properties("prices",properties,"0",false));
        listPerties.add(new Properties("water and electricity service quality",properties,"0",false));
        listPerties.add(new Properties("salarles",properties,"0",false));


        Properties_View_Adapter adpterList = new Properties_View_Adapter((AppCompatActivity)getActivity(), listPerties);

        notfi_list.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        notfi_list.setAdapter(adpterList);


        return view;

    }


    @OnClick(R.id.go_to_next)
    public void btn_cancel(){


        for (int i = 0; i <listPerties.size() ; i++) {
            if(listPerties.get(i).isSelected()){
                totalSelction++;
                listPertiesSelection.add(listPerties.get(i));
                Log.d("isSelected",listPerties.get(i).getName() +"  "+listPerties.get(i).isSelected());
            }
        }
        totalSelction=totalSelction*2;

        if(totalSelction<1){
            Toast.makeText(getActivity(), "Please select at least one item", Toast.LENGTH_SHORT).show();
        }else {


            ProgressDialog.show((AppCompatActivity)getActivity());

            for (int i = 0; i < listPerties.size(); i++) {
                if(listPerties.get(i).isSelected()) {
                        getResult(listPerties.get(i).getType(),listPerties.get(i).getName(),region1);
                        getResult(listPerties.get(i).getType(),listPerties.get(i).getName(),region2);

                }

            }
        }
    }


    public void getResult(final String type, final String featureName, final String regionName){

        String url = Utility.API_ROOT+type+"/"+featureName+"/"+regionName;
        Log.d("paymentStatus", url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Region2", response);

                totalRuslut++;
                listResult.add(new Result(featureName,type,response,regionName));


                Log.d("totalCounter", totalRuslut+"  "+totalSelction);

                if(totalRuslut==totalSelction) {
                    goToViewResult();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();

                String message = null;
                Log.d("onErrorResponse", error.toString() + "");
                String json = null;
                Log.d("error.getMessage()", error.getMessage() + "");
                if (error instanceof NetworkError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (error instanceof ServerError) {
                    message = "The server could not be found. Please try again after some time!!";
                } else if (error instanceof AuthFailureError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (error instanceof ParseError) {
                    message = "Parsing error! Please try again after some time!!";
                } else if (error instanceof NoConnectionError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (error instanceof TimeoutError) {
                    message = "Connection TimeOut! Please check your internet connection.";
                } else {

                    Toast.makeText(getActivity(),error.getMessage(), Toast.LENGTH_SHORT).show();

                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                // params.put("resourcePath",resourcePath);
                //Log.d("params.toString()", params.toString() + "");
                return checkParams(params);
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headersSys = super.getHeaders();
                Map<String, String> headers = new HashMap<String, String>();
                //  headers.put("Access-Token",token);
                headers.putAll(headersSys);
                Log.d("headersSys", headers.toString() + "");
                return headers;
            }

            private Map<String, String> checkParams(Map<String, String> map) {
                Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<String, String> pairs = (Map.Entry<String, String>) it.next();
                    if (pairs.getValue() == null) {
                        map.put(pairs.getKey(), "");
                    }
                }
                return map;
            }

        };

        App.getInstance().addToRequestQueue(stringRequest);
    }

    public void goToViewResult(){
        Log.d("goToViewResult", "goToViewResult  "+new Gson().toJson(listResult));

        ProgressDialog.hide();
        totalRuslut=0;
        totalSelction=0;


        ViewResultFragment point1Fragment = new ViewResultFragment();

        Bundle bundle =new Bundle();
        bundle.putString("Result",new Gson().toJson(listResult));
        bundle.putString("region1",region1);
        bundle.putString("region2",region2);
        bundle.putString("listPertiesSelection",new Gson().toJson(listPertiesSelection));



        region1 =  bundle.getString("region1");
        region2 = bundle.getString("region2");


        point1Fragment.setArguments(bundle);

        getFragmentManager().beginTransaction().replace(R.id.container, point1Fragment).addToBackStack("").commit();

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
