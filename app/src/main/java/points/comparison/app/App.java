package points.comparison.app;

import android.content.ComponentCallbacks2;
import android.content.res.Configuration;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import points.comparison.app.Dialogs.ProgressDialog;
import points.comparison.app.utils.Utility;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;



public class App extends MultiDexApplication {



    public static final String TAG = App.class
            .getSimpleName();



    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;



    private static App mInstance;



    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/DINNEXTLTW23REGULAR2.TTF")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );



      // Utility.NukeSSLCerts.nuke();


        mInstance = this;
      //  ApplicationStarter.initialize(this , true);
    }


/*
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
*/


    public static synchronized App getInstance() {
        return mInstance;
    }






    public  synchronized RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }


    public  synchronized <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }



    public synchronized <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        int socketTimeout = Utility.REQ_T_OUT;//50 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
       // req.setRetryPolicy(policy);
        getRequestQueue().add(req);
    }

    public synchronized void cancelPendingRequests() {
        if (mRequestQueue != null) {
           // mRequestQueue.cancelAll(TAG);
        }
    }





    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Utility.setLanguageFromPrefs();
    }



    @Override
    public void onTrimMemory(final int level) {
        if (level == ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN) {

            Log.i( "app" , "terminated" ) ;
        }
        super.onTrimMemory(level);
    }




}
