package ps.getit.app.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ps.getit.app.Custom.CustomMapView;
import ps.getit.app.R;


public class DeliveryAddOfferFragment extends Fragment {


    @BindView(R.id.price)
    EditText price;

    @BindView(R.id.mapView)
    CustomMapView mMapView;


    @BindView(R.id.order_id)
    TextView order_id;

    @BindView(R.id.recipient_address)
    TextView recipient_address;

    @BindView(R.id.sender_address)
    TextView sender_address;


    public DeliveryAddOfferFragment() {
        // Required empty public constructor
    }



    public static DeliveryAddOfferFragment newInstance() {
        DeliveryAddOfferFragment fragment = new DeliveryAddOfferFragment();
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
        View view = inflater.inflate(R.layout.fragment_delivery_add_offer, container, false);

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
