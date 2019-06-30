package ps.getit.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.liulishuo.magicprogresswidget.MagicProgressBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import ps.getit.app.App;
import ps.getit.app.R;
import ps.getit.app.utils.Utility;

public class SplashActivity extends BaseActivity {



    @BindView(R.id.mpb)
    MagicProgressBar mpb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ButterKnife.bind(this);

        getAbout();

        mpb.setSmoothPercent(1 ,3000);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {


                startMain();

            }
        } , 3200);




    }
    public void getAbout(){

        Log.i("getMarkets","getMarkets");
        if (Utility.isConnectingToInternet(SplashActivity.this)) {
            StringRequest request = new StringRequest(Request.Method.POST, Utility.API_ROOT + "about", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("getMarkets", response);
                    try {
                        JSONObject obj = new JSONObject(response);

                        Utility.addGenericToPref(App.getInstance(), "about" , obj.optString("details") );
                        Utility.addGenericToPref(App.getInstance(), "policy" , obj.optString("policy") );





                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    startMain();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    if( Utility.getBooleanFromPref(SplashActivity.this, "isLogin",false))
                        params.put("token", Utility.getStringFromPref(SplashActivity.this, "token"));

                    params.put("lang",Utility.getLangDefult(SplashActivity.this));

                    return params;

                }
            };

            App.getInstance().addToRequestQueue(request);
        }else {

            try {

                Thread.sleep(1500);
            }catch (InterruptedException e) {
                e.printStackTrace();
            }
            startMain();
        }

    }
    public void startMain(){


        if( !Utility.is_user_logged(SplashActivity.this)  || Utility.getBooleanFromPref(SplashActivity.this , "test" , false ) ) {


            Intent ic = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(ic);
            finish();
        }else {
            if (Utility.getStringFromPref(SplashActivity.this, "is_admin").equals("0")) {
                Intent i = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(i);
                finish();

            } else if (Utility.getStringFromPref(SplashActivity.this, "is_admin").equals("1")) {
                Intent i = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }else {
                Intent ic = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(ic);
                finish();
            }


        }

    }




}
