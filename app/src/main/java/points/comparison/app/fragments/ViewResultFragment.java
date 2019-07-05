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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;
import points.comparison.app.CustomAdapter.Properties_View_Adapter;
import points.comparison.app.CustomAdapter.Result_View_Adapter;
import points.comparison.app.R;
import points.comparison.app.models.Properties;
import points.comparison.app.models.Result;
import points.comparison.app.utils.Utility;


public class ViewResultFragment extends Fragment {




    ArrayList<Result> listResult =new ArrayList<>();

    ArrayList<Properties> listPertiesSelection=new ArrayList<>();

    ArrayList<Properties> listplaces=new ArrayList<>();
    ArrayList<Properties> listPerties=new ArrayList<>();


    ArrayList<Properties> finalListResult =new ArrayList<>();


    public ViewResultFragment() {
        // Required empty public constructor
    }



    public static ViewResultFragment newInstance() {
        ViewResultFragment fragment = new ViewResultFragment();
        return fragment;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    public String region1="",region2="";

    @BindView(R.id.notfi_list)
    RecyclerView notfi_list;

    @BindView(R.id.scor1)
    TextView scor1;

    @BindView(R.id.scor2)
    TextView scor2;

    @BindView(R.id.winning)
    TextView winning;


    public static String places="places";
    public static String properties="properties";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fargment_view_result, container, false);

        ButterKnife.bind(this, view);

        finalListResult.clear();
        listPertiesSelection.clear();
        listResult.clear();
        listplaces.clear();
        listPerties.clear();

        Bundle bundle = this.getArguments();
        if (bundle != null) {

            region1 =  bundle.getString("region1");
            region2 = bundle.getString("region2");

            scor1.setText(region1);
            scor2.setText(region2);



            listResult = new Gson().fromJson(bundle.getString("Result"), new TypeToken<ArrayList<Result>>() { }.getType());
            listPertiesSelection= new Gson().fromJson(bundle.getString("listPertiesSelection"), new TypeToken<ArrayList<Properties>>() { }.getType());


            listplaces.add(new Properties("Places: ","0","0",false));
            for (int i = 0; i < listPertiesSelection.size(); i++) {

                if(listPertiesSelection.get(i).getType().equals(places)){
                    listplaces.add(listPertiesSelection.get(i));
                }
            }

            listPerties.add(new Properties("Properties:","0","0",false));

            for (int i = 0; i < listPertiesSelection.size(); i++) {

                if(listPertiesSelection.get(i).getType().equals(properties)){
                    listPerties.add(listPertiesSelection.get(i));
                }
            }
            finalListResult.addAll(listplaces);
            finalListResult.addAll(listPerties);




        }

        Log.d("listResult",listResult.size()+"");
        Log.d("listResult",listPertiesSelection.size()+"");

        Result_View_Adapter adpterList = new Result_View_Adapter((AppCompatActivity)getActivity(), finalListResult,region1,region2,listResult
        );

        notfi_list.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        notfi_list.setAdapter(adpterList);


        winning.setText("the winning region: "+region1);
        return view;

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
