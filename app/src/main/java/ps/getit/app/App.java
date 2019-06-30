package ps.getit.app;

import android.app.Activity;
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
import com.google.firebase.FirebaseApp;
import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.AlbumConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import ps.getit.app.CustomAdapter.MediaLoader;
import ps.getit.app.Dialogs.ProgressDialog;
import ps.getit.app.activities.BaseActivity;
import ps.getit.app.utils.Utility;
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
        FirebaseApp.initializeApp(this);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/DINNEXTLTW23REGULAR2.TTF")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );


        Album.initialize(AlbumConfig.newBuilder(this)
                        .setAlbumLoader(new MediaLoader())
                        .setLocale(Locale.getDefault())
                        .build());
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

    public abstract static class CustomRequest {
        boolean auth  = true;
        String action ;
        StringRequest stringRequest;
        AppCompatActivity activity;
        Map<String, String> params = new HashMap<>() ;


        public CustomRequest(AppCompatActivity activity,String action, String url , Map<String,String> params) {
            this( activity,action, true,  url , params) ;
        }

        public CustomRequest(AppCompatActivity activity,String action , Map<String,String> params) {
            this(activity, action, true,  null , params) ;
        }
        public CustomRequest(AppCompatActivity activity,String action, boolean auth , Map<String,String> params) {
            this( activity,action, auth,  null , params) ;
        }

        CustomRequest(AppCompatActivity activity,String action, boolean auth, String url , Map<String,String> params) {
            SuccessRequest successRequest = new SuccessRequest();
            Response.ErrorListener errorListener = new ErrorRequest();

            this.action = action;
            this.params = getParams();
            this.params.putAll(params);
            this.auth = auth;
            this.activity=activity;

            if (url == null)
                url =   Utility.getGenericStringFromPref(activity ,"host" ,Utility.API_ROOT)+action;


            Log.d("url",url);

            stringRequest = new StringRequest(Request.Method.POST , url,successRequest,errorListener ){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    return CustomRequest.this.getParams();

                }
            };

            ProgressDialog.show(activity);
            getInstance().addToRequestQueue(stringRequest);

        }



        Map<String, String> getParams()  {

            params.put("action", action);

            if (auth) {
                params.put("token", Utility.getStringFromPref(getInstance(),"token",""));
                params.put("lang",Utility.getLangDefult(getInstance()));

            }

            Log.d("params",params+"");
            return params;
        }


        class SuccessRequest implements Response.Listener<String> {


            @Override
            public void onResponse(String response) {
                Log.d("response","response: "+response);

                ProgressDialog.hide();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.has("status")){

                        switch (jsonObject.getString("status")){

                            case "success" :
                                onSuccess(jsonObject);
                                break;
                            case "error" :
                                if( jsonObject.has("message"))
                                    Utility.showToast(jsonObject.optString("message"),false);
                                else
                                    onError(response);
                                break;


                        }

                    }else{

                        onError(response);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    onError(response);

                }

            }
        }


        class ErrorRequest implements Response.ErrorListener {

            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                ProgressDialog.hide();

                onError();
            }

        }


        public abstract void onSuccess(JSONObject jsonObject);


        void onError(){
            ProgressDialog.hide();

            Utility.showToast(activity.getString(R.string.connection_error)+" "+activity.getString(R.string.no_internet),false);
        }


        public void onError(String response){
            ProgressDialog.hide();

            Utility.showToast(activity.getString(R.string.connection_error)+" "+activity.getString(R.string.no_internet),false);
        }






    }



}
