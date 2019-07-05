package points.comparison.app.activities;

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
import points.comparison.app.App;
import points.comparison.app.R;
import points.comparison.app.utils.Utility;

public class SplashActivity extends BaseActivity {



    @BindView(R.id.mpb)
    MagicProgressBar mpb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ButterKnife.bind(this);


        mpb.setSmoothPercent(1 ,3000);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {


                startMain();

            }
        } , 3200);




    }
    public void startMain(){
        Intent i = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(i);
        finish();



    }




}
