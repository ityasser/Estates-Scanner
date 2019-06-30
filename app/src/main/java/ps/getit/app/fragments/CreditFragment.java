package ps.getit.app.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ps.getit.app.App;
import ps.getit.app.CustomAdapter.MyOrder_View_Adapter;
import ps.getit.app.R;
import ps.getit.app.models.orders;
import ps.getit.app.utils.Utility;


public class CreditFragment extends Fragment {

    public static CreditFragment newInstance() {
        CreditFragment fragment = new CreditFragment();
        return fragment;
    }


    @BindView(R.id.credit_t)
    EditText credit_t;

    public AppCompatActivity getActivitty(){
        return (AppCompatActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_credit, container, false);
        // getFragmentManager().popBackStack()

        ButterKnife.bind(this, view);







        return view;
    }


    @OnClick(R.id.points)
    public void points() {
       getActivitty(). getSupportFragmentManager().beginTransaction().addToBackStack("").replace(R.id.container, PointsSaleFragment.newInstance()).commit();

    }
        @OnClick(R.id.credit)
    public void credit(){

        if(credit_t.getText().toString().isEmpty()){
            credit_t.setError("برجاء ادخل كود البطاقة");
        }else {
            Map<String, String> params = new HashMap<>() ;
            params.put("card",credit_t.getText().toString());
            new App.CustomRequest(getActivitty(),"check_card",params) {
                @Override
                public void onSuccess(JSONObject obj) {

                    Utility.showToast(obj.optString("message"), true);


                }



            };



        }
        }


}
