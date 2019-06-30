package ps.getit.app.Dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import ps.getit.app.App;
import ps.getit.app.R;
import ps.getit.app.activities.MainActivity;
import ps.getit.app.utils.Utility;


/**
 * Created by techmix on 20/11/2015.
 */
public class ActivateAccount extends DialogFragment {
    EditText code;
    Button active,skip;
    String titleAlterError;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


     //   Utility.setLanguage(getActivity(),1);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater mInflater = (LayoutInflater) getActivity().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = mInflater.inflate(R.layout.dilogs_active_acount, null, false);
         titleAlterError=getActivity().getResources().getString(R.string.sorry);
        final String masgAlterError=getActivity().getResources().getString(R.string.empty_msg);
        final String checknetAlterError=getActivity().getResources().getString(R.string.no_internet);

        final String username= getArguments().getString("username");
        final String pass= getArguments().getString("pass");

        code = (EditText) view.findViewById(R.id.codeact);
        active = (Button) view.findViewById(R.id.activete);
        skip= (Button) view.findViewById(R.id.skip);
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utility.addToPref(getActivity(), "isLogin",false);
                Intent intent = new Intent(getActivity(), MainActivity.class);
                //  intent.putExtra("isnotfiy", "non");
                startActivity(intent);
            }
        });
        active.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (Utility.isConnectingToInternet(getActivity())) {

                    if ( code.getText().toString().isEmpty()) {
                        Utility.showToast( masgAlterError,false);

                    } else {

                        post_Reg( code.getText().toString(),username,pass);
                    }

                } else {
                    Utility.showToast( checknetAlterError,false);

                }


            }
        });


        builder.setView(view);
        return builder.create();
    }


    public void post_Reg(final String code, final String username, final String pass) {
        final ProgressDialog shwo=new ProgressDialog();
        shwo.show(getActivity().getSupportFragmentManager(),"dfdf");

        StringRequest request = new StringRequest(Request.Method.POST, Utility.API_ROOT + "activate", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                shwo.dismissAllowingStateLoss();
                JSONObject jsonObject = null;

                try {
                    jsonObject = new JSONObject(response);

                    if (Utility.checkRequestStatus(jsonObject)) {

                        if(jsonObject.getString("active").equals("0")){

                            Utility.showToast(getResources().getString(R.string.not_actve), true);

                        }else {

                            Utility.showToast(getResources().getString(R.string.activated ), true);

                            dismiss();

                        }

                    }else if (jsonObject.has("message")) {

                        Utility.showToast(jsonObject.getString("message"), false);

                    } else {

                        Utility.showToast(R.string.some_error, false);

                    }
                    } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Utility.showToast(R.string.connection_error, false);
                shwo.dismissAllowingStateLoss();


            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();

                params.put("code", code);
                params.put("mobile", username);
                params.put("password",pass);
                params.put("lang",Utility.getLangDefult(getActivity()));


                return params;

            }
        };
        App.getInstance().addToRequestQueue(request);

    }


}


