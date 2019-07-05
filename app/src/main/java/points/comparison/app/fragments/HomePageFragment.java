package points.comparison.app.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import points.comparison.app.Custom.CustomMapView;
import points.comparison.app.R;
import points.comparison.app.utils.Utility;


public class HomePageFragment extends Fragment {



    @BindView(R.id.back)
    ImageView back;
    @OnClick(R.id.go_to_next)
    public void go_to_next(){
        Point1Fragment point1Fragment = new Point1Fragment();

        getFragmentManager().beginTransaction().replace(R.id.container, point1Fragment).addToBackStack("").commit();

    }

    public HomePageFragment() {
        // Required empty public constructor
    }



    public static HomePageFragment newInstance() {
        HomePageFragment fragment = new HomePageFragment();
        return fragment;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        back.setImageResource(R.drawable.back_home);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fargment_home, container, false);

        ButterKnife.bind(this, view);






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
