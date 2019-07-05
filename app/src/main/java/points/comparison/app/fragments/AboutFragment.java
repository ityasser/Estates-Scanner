package points.comparison.app.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import points.comparison.app.R;
import points.comparison.app.utils.Utility;


public class AboutFragment extends Fragment {


    @BindView(R.id.about_tv)
    TextView aboutTv;



    public AboutFragment() {
        // Required empty public constructor
    }



    public static AboutFragment newInstance() {
        AboutFragment fragment = new AboutFragment();
        return fragment;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fargment_about, container, false);

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
