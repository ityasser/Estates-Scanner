package ps.getit.app.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.media.MediaScannerConnection;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.ActionMenuItemView;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import es.dmoral.toasty.Toasty;
import ps.getit.app.App;
import ps.getit.app.R;
import ps.getit.app.activities.LoginActivity;
import ps.getit.app.activities.RegUserActivity;
import ps.getit.app.activities.TermsPoliciesActivity;


/**
 * Created by techmix on 08/01/2016.
 */




public class Utility {



    public  final static String API_ROOT = "http://gitit.info/Api/";
    public static final int REQ_T_OUT = 500000;
    public static final String SITE_ROOT = "http://gitit.info/";

    public static String REFRESH_TABLE_NAME  = "com.mms.rest.pro.tables" ;

    public static final int REAM_MORE_THRESHOLD = 100;
    public static final int login = 521;
    public static final int item_to_cart = 325;

    public static final String CART_REFRESH = "com.mms.restuaranttalabati.refreshcart2" ;

    public static final String ORDERS_REFRESH = "com.mms.restuaranttalabati.refreshorders";
    public static final String NADEL_REFRESH = "com.mms.restuaranttalabati.nadel";

    public static final String LOCK_APP = "com.mms.restuaranttalabati.lockapp";

    public static String REFRESH_CART_BADGE  = "com.mms.restuaranttalabati.refreshcart" ;

    public static String REFRESH_MEALS_LIST  = "com.mms.restuaranttalabati.refresh_meals" ;

    public static String formatHoursAndMinutes(int totalMinutes) {

        if( totalMinutes<60){
            return totalMinutes +"  دقيقة";
        }else {
            String minutes = Integer.toString(totalMinutes % 60);

            minutes = minutes.length() == 1 ? "0" + minutes : minutes;
            return (totalMinutes / 60) + ":" + minutes +"  ساعة";
        }
    }


    public static double distanceBetweenTwoLocation(String lat1,String lng1,String lat2,String lng2){
        Location loc1 = new Location("");
        loc1.setLatitude(Double.parseDouble(lat1));
        loc1.setLongitude(Double.parseDouble(lng1));

        Location loc2 = new Location("");
        loc2.setLatitude(Double.parseDouble(lat2));
        loc2.setLongitude(Double.parseDouble(lng2));

        float distanceInMeters = loc1.distanceTo(loc2);

        double distance  =distanceInMeters/1000;

        distance=Math.round (distance*100.0) / 100.0;

        return distance;
    }

    public static boolean checkNotNull(String s){
        if(s != null && !s.equals("") && !s.isEmpty() && URLUtil.isValidUrl(s)) {
            return true;
        } else {

            Utility.showToast("لا يوجد رابط او الرابط غير صحيح", false);
            return false;

        }
    }

    public static void  setFont(Context context, View v){
    Typeface typeface = Typeface.createFromAsset(context.getAssets(),
            "fonts/DINNEXTLTW23REGULAR2.TTF");
    if (v instanceof TextView || v instanceof Button)
        ((TextView) v).setTypeface(typeface);

}
    public static String parseCode(String message) {
        Pattern p = Pattern.compile("\\b\\d{4}\\b");
        Matcher m = p.matcher(message);
        String code = "";
        while (m.find()) {
            code = m.group(0);
        }
        return code;
    }
    public static void setFontJF(Context context, ViewGroup group) {
        Typeface typeface = Typeface.createFromAsset(context.getAssets(),
                "fonts/JF_Flat.ttf");

        int count = group.getChildCount();
        View v;
        for (int i = 0; i < count; i++) {
            v = group.getChildAt(i);
            if (v instanceof TextView || v instanceof Button)
                ((TextView) v).setTypeface(typeface);
            else if (v instanceof ViewGroup)
                setFontJF(context, (ViewGroup) v);
        }


    }

    public static void setFont(Context context, ViewGroup group) {
        Typeface typeface = Typeface.createFromAsset(context.getAssets(),
                "fonts/DINNEXTLTW23REGULAR2.TTF");

        int count = group.getChildCount();
        View v;
        for (int i = 0; i < count; i++) {
            v = group.getChildAt(i);
            if (v instanceof TextView || v instanceof Button)
                ((TextView) v).setTypeface(typeface);
            else if (v instanceof ViewGroup)
                setFont(context, (ViewGroup) v);
        }


    }
    public static void  setFontLight(Context context, View v){
        Typeface typeface = Typeface.createFromAsset(context.getAssets(),
                "fonts/JF_Flat.ttf");
        if (v instanceof TextView || v instanceof Button)
            ((TextView) v).setTypeface(typeface);

    }

    public static void setFontLight(Context context, ViewGroup group) {
        Typeface typeface = Typeface.createFromAsset(context.getAssets(),
                "fonts/JF_Flat.ttf");

        int count = group.getChildCount();
        View v;
        for (int i = 0; i < count; i++) {
            v = group.getChildAt(i);
            if (v instanceof TextView || v instanceof Button)
                ((TextView) v).setTypeface(typeface);
            else if (v instanceof ViewGroup)
                setFontLight(context, (ViewGroup) v);
        }


    }
    public static int getLocationMode(Context context) throws Settings.SettingNotFoundException {
        return Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);

    }
    public static boolean isGPSEnabled(Context mContext) {
        return ((LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE)).isProviderEnabled(LocationManager.GPS_PROVIDER);
    }
    public static void applyFontedTab(Activity activity, ViewPager viewPager, TabLayout tabLayout) {

        for (int i = 0; i < viewPager.getAdapter().getCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if (tab != null) {
                tab.setCustomView(R.layout.tab_text);
                tab.setText(viewPager.getAdapter().getPageTitle(i));

            }
        }
    }
    public static Bitmap createDrawableFromView(Context context, View view) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        view.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.buildDrawingCache();

        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

        // bitmap=scaleDown(bitmap,720,true);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);

        return bitmap;
    }
    public static void setTitle(String title, AppCompatActivity mContext){
        ActionBar ab = mContext.getSupportActionBar();

        // Create a TextView programmatically.
        TextView tv = new TextView(mContext.getApplicationContext());

        // Create a LayoutParams for TextView
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, // Width of TextView
                RelativeLayout.LayoutParams.MATCH_PARENT); // Height of TextView
        lp.addRule(RelativeLayout.CENTER_HORIZONTAL);

        // Apply the layout parameters to TextView widget
        tv.setLayoutParams(lp);

        // Set text to display in TextView
        tv.setText(title); // ActionBar title text

        // Set the text color of TextView to black
        tv.setTextColor(Color.WHITE);
        tv.setTextSize(15);
        tv.setGravity(Gravity.CENTER );
        // Set the monospace font for TextView text
        // This will change ActionBar title text font
        Typeface typeface = Typeface.createFromAsset(mContext.getAssets(),
                "fonts/DINNEXTLTW23REGULAR2.TTF");
        tv.setTypeface(typeface, Typeface.BOLD);

        // Set the ActionBar display option
        ab.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

        // Finally, set the newly created TextView as ActionBar custom view
        ab.setCustomView(tv);

    }

    public static boolean getBooleanFromPref(Context ctx, String key , boolean fallback) {

        SharedPreferences prefs = getUserPref(ctx);

        return prefs.getBoolean(key, fallback);

    }
    public static boolean isMyServiceRunning(Context m, Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) m.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public static void SetNumbNotfi(Context mcontex, int numb){
        SharedPreferences preferences = mcontex.getSharedPreferences("app", mcontex.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putInt("NumbNotfi", numb);
        editor.commit();

    }


    public static int  getNumbNotfi(Context mcontex){

        SharedPreferences preferences = mcontex.getSharedPreferences("app", mcontex.MODE_PRIVATE);

        return preferences.getInt("NumbNotfi", 0);

    }


    public static void ViewMsgReqerLogin(final Context context){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

        // Setting Dialog Title
        alertDialog.setTitle(context.getResources().getString(R.string.sorry));

        // Setting Dialog Message
        alertDialog.setMessage((context.getResources().getString(R.string.must_login)));

        // On pressing Settings button

        alertDialog.setPositiveButton(context.getResources().getString(R.string.login_), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                context.startActivity( new Intent(context , TermsPoliciesActivity.class));



                ((Activity)context).finishAffinity();


            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton(context.getResources().getString(R.string.Sign_Up), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                context. startActivity( new Intent(context, RegUserActivity.class));
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }





    public static String getStringByteSize(int size)
    {
        if(size > 1024 * 1024)  //mega
        {
            return String.format("%.1f MB", size / (float)(1024 * 1024));
        }
        else if(size > 1024)  //kilo
        {
            return String.format("%.1f KB", size / 1024.0f);
        }
        else
        {
            return String.format("%d B",size);
        }
    }

    public static int getFileSizeAtURL(URL url)
    {
        int filesize = -1;
        try
        {
            HttpURLConnection http = (HttpURLConnection)url.openConnection();
            filesize = http.getContentLength();
            http.disconnect();
        }
        catch(Exception e)
        {
            //Logger.e(e.toString());
            return -1;
        }
        return filesize;
    }


    public static  boolean isConnectingToInternet(Context _context){
        ConnectivityManager connectivity = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null)
        {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED)
                    {
                        return true;
                    }

        }
        return false;
    }



    public static void addToPref(Context ctx, String key, boolean value) {
        SharedPreferences prefs = getUserPref(ctx);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static void addGenericToPrefUser(Context ctx, String key, String value) {
        SharedPreferences app_prefs = ctx.getSharedPreferences("UserPre", Context.MODE_PRIVATE);;

        SharedPreferences.Editor editor = app_prefs.edit();
        editor.putString(key, value);
        editor.commit();
    }
    public static String getGenericStringFromPrefUser(Context ctx, String key , String fallback ) {

        SharedPreferences app_prefs = ctx.getSharedPreferences("UserPre", Context.MODE_PRIVATE);;

        return app_prefs.getString(key, fallback);

    }
    public static void addGenericToPref(Context ctx, String key, String value) {
        SharedPreferences app_prefs = ctx.getSharedPreferences("app", Context.MODE_PRIVATE);;

        SharedPreferences.Editor editor = app_prefs.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static void addGenericToPref(Context ctx, String key, boolean value) {
        SharedPreferences app_prefs = ctx.getSharedPreferences("app", Context.MODE_PRIVATE);;

        SharedPreferences.Editor editor = app_prefs.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static String getGenericStringFromPref(Context ctx, String key , String fallback ) {

        SharedPreferences app_prefs = ctx.getSharedPreferences("app", Context.MODE_PRIVATE);;

        return app_prefs.getString(key, fallback);

    }

    public static boolean getGenericBooleanFromPref(Context ctx, String key , boolean fallback ) {

        SharedPreferences app_prefs = ctx.getSharedPreferences("app", Context.MODE_PRIVATE);;

        return app_prefs.getBoolean(key, fallback);

    }

    public static void addToPref(Context ctx, String key, String value) {
        SharedPreferences prefs = getUserPref(ctx);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.commit();

    }

    public static void addToPref(Context ctx, String key, int value) {
        SharedPreferences prefs = getUserPref(ctx);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(key, value);
        editor.commit();
    }



    public static boolean getBooleanFromPref(Context ctx, String key) {

        SharedPreferences prefs = getUserPref(ctx);

        return prefs.getBoolean(key, true);

    }

    public static String getStringFromPref(Context ctx, String key ) {

        SharedPreferences prefs = getUserPref(ctx);

        return prefs.getString(key, "");

    }


    public static String getStringFromPref(Context ctx, String key , String fallback ) {

        SharedPreferences prefs = getUserPref(ctx);

        return prefs.getString(key, fallback);

    }
    public static int getIntFromPref(Context ctx, String key) {

        SharedPreferences prefs = getUserPref(App.getInstance());

        return prefs.getInt(key, -1);

    }



    public static SharedPreferences getUserPref(Context ctx) {

        SharedPreferences app_prefs = ctx.getSharedPreferences("app", Context.MODE_PRIVATE);;

        // app_prefs.getString("current_user", "");

        SharedPreferences prefs = ctx.getSharedPreferences("user"+ app_prefs.getString("current_user", ""), Context.MODE_PRIVATE);

        return  prefs;

    }

public static  boolean is_user_logged(Context ctx){

    SharedPreferences prefs = ctx.getSharedPreferences("app", Context.MODE_PRIVATE);
    return (!prefs.getString("current_user","").equalsIgnoreCase(""));

}


    public static SharedPreferences addUserPrefById(Context ctx, String user_id) {
        SharedPreferences prefs = ctx.getSharedPreferences("app", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("current_user",user_id );
        editor.apply();
        return  prefs;
    }

    public static void clearPrefs(Context ctx) {

        getUserPref(ctx).edit().clear().commit();

        SharedPreferences prefs = ctx.getSharedPreferences("app", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("current_user","" );
        editor.apply();

    }

    public static boolean checkFields(ArrayList<EditText> fields) {
        for (EditText editText : fields){
            if (editText.getText().toString().isEmpty())return false;
        }
        return true;
    }



    public static void showErrorDialog(Activity activity , String msg) {


        new SweetAlertDialog( activity , SweetAlertDialog.ERROR_TYPE)
                .setTitleText(activity.getString(R.string.sorry))
                .setContentText(msg)
                .show();


    }


    public static void showSuccessDialog(Activity activity , String msg) {


        new SweetAlertDialog( activity , SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("")
                .setContentText(msg)
                .show();


    }


    public static void watchYoutubeVideo(Context ctx , String id){
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + id));
        try {
            ctx. startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            ctx. startActivity(webIntent);
        }
    }

    public  static String getDeviceId(Context context){

        return Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);

    }


    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        }
        return capitalize(manufacturer) + " " + model;
    }

    private static String capitalize(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        char[] arr = str.toCharArray();
        boolean capitalizeNext = true;
        String phrase = "";
        for (char c : arr) {
            if (capitalizeNext && Character.isLetter(c)) {
                phrase += Character.toUpperCase(c);
                capitalizeNext = false;
                continue;
            } else if (Character.isWhitespace(c)) {
                capitalizeNext = true;
            }
            phrase += c;
        }
        return phrase;
    }




    public static boolean checkRequestStatus(String response) {

        JSONObject jsonObject = null;
        try {

            jsonObject = new JSONObject(new String(response));




        return (jsonObject.has("status") && jsonObject.getString("status").equalsIgnoreCase("success")) ;

        } catch (JSONException e) {

            return false;

        }

    }


    public  static String getToken(Context ctx){

        return getUserPref(ctx).getString("token", "");

    }


    public  static String getUserType(Context ctx){

        return getUserPref(ctx).getString("type", "");

    }

    public  static String getUserId(Context ctx){

        return getUserPref(ctx).getString("id", "");

    }



    public  static String getLink (Context ctx){

        return getUserPref(ctx).getString("link", "http://marahapp.sky-develop.net/");

    }




    public static void logout(final Activity context) {

/*
        OrderConfirmationDialog orderConfirmationDialog = new OrderConfirmationDialog();

        orderConfirmationDialog.setMessage(context.getString(R.string.enter_pass_to_action));
        orderConfirmationDialog.setOnOrderConfirmListener(new OrderConfirmationDialog.OnOrderConfirmListener() {
            @Override
            public void onOrderConfirmed() {


            }
        });


       orderConfirmationDialog.show(context.getSupportFragmentManager() , "" );


*/

        File file = new File(context.getFilesDir().getAbsoluteFile()+ "/products");

        delete(file);


        //App.getInstance().closeDB();

        clearPrefs(context);

        Intent intent = new Intent(context , LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //

        context.startActivity(intent);
        context.finishAffinity();//
         ActivityCompat.finishAffinity(context);



    }

    public static void showSettingAcurcysAlert(final Activity mcontext) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mcontext);

        // Setting Dialog Title
        alertDialog.setTitle(R.string.location_settings);

        // Setting Dialog Message
        alertDialog.setMessage(mcontext.getString(R.string.gps_message_body_3) +"\n"+
                mcontext.getString(R.string.gps_message_body_4));

        // On pressing Settings button
        alertDialog.setPositiveButton(R.string.settings, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                mcontext. startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS),1000);


            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton(R.string.discard, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();

            }
        });

        // Showing Alert Message
        alertDialog.setCancelable(false);

        alertDialog.show();


    }

    public static void showSettingsAlert(final Activity mcontext) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mcontext);

        // Setting Dialog Title
        alertDialog.setTitle(R.string.location_settings);

        // Setting Dialog Message
        alertDialog.setMessage(mcontext.getString(R.string.gps_message_body) +"\n"+
                mcontext.getString(R.string.gps_message_body_2));

        // On pressing Settings button
        alertDialog.setPositiveButton(R.string.settings, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mcontext.startActivityForResult(intent , 1000);
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton(R.string.discard, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();

            }
        });

        // Showing Alert Message
        alertDialog.setCancelable(false);

        alertDialog.show();


    }
  /*  public static void logout2(final AppCompatActivity context) {


        OrderConfirmationDialog orderConfirmationDialog = new OrderConfirmationDialog();

        orderConfirmationDialog.setMessage(context.getString(R.string.enter_pass_to_action));
        orderConfirmationDialog.setOnOrderConfirmListener(new OrderConfirmationDialog.OnOrderConfirmListener() {
            @Override
            public void onOrderConfirmed() {

                App.getInstance().closeDB();

                clearPrefs(context);

                Intent intent = new Intent(context , LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
                //

                context.startActivity(intent);
               ActivityCompat.finishAffinity(context);


            }
        });


       orderConfirmationDialog.show(context.getFragmentManager() , "" );









    }

*/



    public static String milliSecondsToTimer(long milliseconds){
        String finalTimerString = "";
        String secondsString = "";

        // Convert total duration into time
        int hours = (int)( milliseconds / (1000*60*60));
        int minutes = (int)(milliseconds % (1000*60*60)) / (1000*60);
        int seconds = (int) ((milliseconds % (1000*60*60)) % (1000*60) / 1000);
        // Add hours if there
        if(hours > 0){
            finalTimerString = hours + ":";
        }

        // Prepending 0 to seconds if it is one digit
        if(seconds < 10){
            secondsString = "0" + seconds;
        }else{
            secondsString = "" + seconds;}

        finalTimerString = finalTimerString + minutes + ":" + secondsString;

        // return timer string
        return finalTimerString;
    }

    /**
     * Function to get Progress percentage
     * @param currentDuration
     * @param totalDuration
     * */
    public  static int getProgressPercentage(long currentDuration, long totalDuration){
        Double percentage = (double) 0;

        long currentSeconds = (int) (currentDuration / 1000);
        long totalSeconds = (int) (totalDuration / 1000);

        // calculating percentage
        percentage =(((double)currentSeconds)/totalSeconds)*100;

        // return percentage
        return percentage.intValue();
    }

    /**
     * Function to change progress to timer
     * @param progress -
     * @param totalDuration
     * returns current duration in milliseconds
     * */
    public static int progressToTimer(int progress, int totalDuration) {
        int currentDuration = 0;
        totalDuration = (int) (totalDuration / 1000);
        currentDuration = (int) ((((double)progress) / 100) * totalDuration);

        // return current duration in milliseconds
        return currentDuration * 1000;
    }


    public static void changeRoundedBackColor (View view , String color){

        GradientDrawable drawable = (GradientDrawable) view.getBackground();
        drawable.setColor(Color.parseColor(color));

    }



    public static void setRoundedBack (View view , String color , String borderColor , float raduis ){


        GradientDrawable gd = new GradientDrawable();

        gd.setColor(Color.parseColor(color));

        gd.setCornerRadius(raduis);

        gd.setStroke( 3 , Color.parseColor(borderColor)  );

        view.setBackground(gd);

      //  gd.setStroke(2, Color.WHITE);

    }

    public static void setRoundedBack (View view , String color , float raduis ){


        GradientDrawable gd = new GradientDrawable();

        gd.setColor(Color.parseColor(color));

        gd.setCornerRadius(raduis);

        view.setBackground(gd);

        //  gd.setStroke(2, Color.WHITE);

    }





    public static void setRoundedBack(View v, String backgroundColor, String borderColor , float[] raduis)
    {
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.RECTANGLE);
        shape.setCornerRadii(raduis); //new float[] { 8, 8, 8, 8, 0, 0, 0, 0 } top-left, top-right, bottom-right, bottom-left
        shape.setColor(Color.parseColor(backgroundColor));
        shape.setStroke(3, Color.parseColor(borderColor));
        v.setBackground(shape);
    }

    /**
       * @param raduis new float[] { 8, 8, 8, 8, 0, 0, 0, 0 } top-left, top-right, bottom-right, bottom-left
     */

    public static void setRoundedBack(View v, String backgroundColor, float[] raduis)
    {
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.RECTANGLE);
        shape.setCornerRadii(raduis); //new float[] { 8, 8, 8, 8, 0, 0, 0, 0 } top-left, top-right, bottom-right, bottom-left
        shape.setColor(Color.parseColor(backgroundColor));
        v.setBackground(shape);
    }



    public static void setLanguage(FragmentActivity activity, int selectedlanguage ) {

        int c_lang = Utility.getIntFromPref(activity , "lang"  );


        if(c_lang == -1 ) c_lang=1;


        Log.i("c_lang" , c_lang+"" ) ;

        if(selectedlanguage == c_lang) {
            return;
        }



        Utility.addToPref(activity , "lang" , selectedlanguage );



       // selectedlanguage=1;

        final String[] lang = { "en_us" , "ar" };

        Locale locale = new Locale(lang[selectedlanguage]);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        activity.getApplicationContext().getResources()
                .updateConfiguration(config, null);

        activity.recreate();
          //  activity.finishAffinity();

            //activity.startActivity(new Intent(activity, SplashActivity.class));



    }
    public static String getLangDefult(Context mcontext){
        int c_lang = Utility.getIntFromPref(mcontext , "lang"  );

        if(c_lang == -1){
            return "1";
        }else {
          return String.valueOf(c_lang);
        }
    }

    public static void setLanguageFromPrefs(FragmentActivity activity ) {

        int c_lang = Utility.getIntFromPref(activity , "lang"  );


        if(c_lang == -1 ) c_lang=1;

        Log.i("c_lang" , c_lang+"" ) ;

        //c_lang=1;

        final String[] lang = { "en_us" , "ar" };

        Locale locale = new Locale(lang[c_lang]);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        activity.getApplicationContext().getResources()
                .updateConfiguration(config, null);






    }

    public static void setLanguageFromPrefs( ) {


        int c_lang = Utility.getIntFromPref(App.getInstance() , "lang"  );



        if(c_lang == -1 ) c_lang=1;


        //c_lang=1;

        final String[] lang = { "en_us" , "ar" };

        Log.i("c_lang" , c_lang+"" ) ;

        Locale locale = new Locale(lang[c_lang]);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.setLocale( locale);
        /*App.getInstance().getBaseContext().getApplicationContext().getResources()
                .updateConfiguration(config, null);
*/




        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
          App.getInstance().getBaseContext().getApplicationContext().createConfigurationContext(config);
        } else {
            App.getInstance().getBaseContext().getApplicationContext().getResources().updateConfiguration(config, App.getInstance().getBaseContext().getApplicationContext().getResources().getDisplayMetrics());
        }


    }

    public static int dpToPx(final float dp) {
        return Math.round(dp * (Resources.getSystem().getDisplayMetrics().xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public static void SchedulaAllAlarms(Context context) {
    }

/*
    public static class NukeSSLCerts {
        protected static final String TAG = "NukeSSLCerts";

        public static void nuke() {
            try {
                TrustManager[] trustAllCerts = new TrustManager[] {
                        new X509TrustManager() {
                            public X509Certificate[] getAcceptedIssuers() {
                                X509Certificate[] myTrustedAnchors = new X509Certificate[0];
                                return myTrustedAnchors;
                            }

                            @Override
                            public void checkClientTrusted(X509Certificate[] certs, String authType) {}

                            @Override
                            public void checkServerTrusted(X509Certificate[] certs, String authType) {}
                        }
                };

                SSLContext sc = SSLContext.getInstance("SSL");
                sc.init(null, trustAllCerts, new SecureRandom());
                HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
                HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String arg0, SSLSession arg1) {
                        return true;
                    }
                });
            } catch (Exception e) {
            }
        }
    }
*/

    public static String getRealPathFromURI(Context context, Uri contentUri) {
        String[] projection = { MediaStore.Audio.Media.DATA };
        CursorLoader loader = new CursorLoader(context, contentUri, projection, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);

    }




    /**
     * The extension separator character.
     */
    private static final char EXTENSION_SEPARATOR = '.';

    /**
     * The Unix separator character.
     */
    private static final char UNIX_SEPARATOR = '/';

    /**
     * The Windows separator character.
     */
    private static final char WINDOWS_SEPARATOR = '\\';

    /**
     * The system separator character.
     */
    private static final char SYSTEM_SEPARATOR = File.separatorChar;

    /**
     * Gets the extension of a filename.
     * <p>
     * This method returns the textual part of the filename after the last dot.
     * There must be no directory separator after the dot.
     * <pre>
     * foo.txt      --> "txt"
     * a/b/c.jpg    --> "jpg"
     * a/b.txt/c    --> ""
     * a/b/c        --> ""
     * </pre>
     * <p>
     * The output will be the same irrespective of the machine that the code is running on.
     *
     * @param filename the filename to retrieve the extension of.
     * @return the extension of the file or an empty string if none exists.
     */
    public static String getExtension(String filename) {
        if (filename == null) {
            return null;
        }
        int index = indexOfExtension(filename);
        if (index == -1) {
            return "";
        } else {
            return filename.substring(index + 1);
        }
    }

    /**
     * Returns the index of the last extension separator character, which is a dot.
     * <p>
     * This method also checks that there is no directory separator after the last dot.
     * To do this it uses {@link #indexOfLastSeparator(String)} which will
     * handle a file in either Unix or Windows format.
     * <p>
     * The output will be the same irrespective of the machine that the code is running on.
     *
     * @param filename  the filename to find the last path separator in, null returns -1
     * @return the index of the last separator character, or -1 if there
     * is no such character
     */
    public static int indexOfExtension(String filename) {
        if (filename == null) {
            return -1;
        }
        int extensionPos = filename.lastIndexOf(EXTENSION_SEPARATOR);
        int lastSeparator = indexOfLastSeparator(filename);
        return (lastSeparator > extensionPos ? -1 : extensionPos);
    }

    /**
     * Returns the index of the last directory separator character.
     * <p>
     * This method will handle a file in either Unix or Windows format.
     * The position of the last forward or backslash is returned.
     * <p>
     * The output will be the same irrespective of the machine that the code is running on.
     *
     * @param filename  the filename to find the last path separator in, null returns -1
     * @return the index of the last separator character, or -1 if there
     * is no such character
     */
    public static int indexOfLastSeparator(String filename) {
        if (filename == null) {
            return -1;
        }
        int lastUnixPos = filename.lastIndexOf(UNIX_SEPARATOR);
        int lastWindowsPos = filename.lastIndexOf(WINDOWS_SEPARATOR);
        return Math.max(lastUnixPos, lastWindowsPos);
    }


    public static boolean isAvailable(Context ctx, Intent intent) {
        final PackageManager mgr = ctx.getPackageManager();
        List<ResolveInfo> list =
                mgr.queryIntentActivities(intent,
                        PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }




    public static void scanFile(Context context , String path , MediaScannerConnection.OnScanCompletedListener onScanCompletedListener) {

        MediaScannerConnection.scanFile(context,
                new String[] { path }, null,

                onScanCompletedListener
        );

    }





    public static boolean isRTL() {
        return isRTL(Locale.getDefault());
    }



    public static boolean isRTL(Locale locale) {
        final int directionality = Character.getDirectionality(locale.getDisplayName().charAt(0));
        return directionality == Character.DIRECTIONALITY_RIGHT_TO_LEFT ||
                directionality == Character.DIRECTIONALITY_RIGHT_TO_LEFT_ARABIC;
    }






    /**
     * Use this method to colorize toolbar icons to the desired target color
     * @param toolbarView toolbar view being colored
     * @param toolbarIconsColor the target color of toolbar icons
     * @param activity reference to activity needed to register observers
     */
    public static void colorizeToolbar(Toolbar toolbarView, int toolbarIconsColor, Activity activity) {
        final PorterDuffColorFilter colorFilter
                = new PorterDuffColorFilter(toolbarIconsColor, PorterDuff.Mode.MULTIPLY);

        for(int i = 0; i < toolbarView.getChildCount(); i++) {
            final View v = toolbarView.getChildAt(i);

            //Step 1 : Changing the color of back button (or open drawer button).
            if(v instanceof ImageButton) {
                //Action Bar back button
                ((ImageButton)v).getDrawable().setColorFilter(colorFilter);
            }

            if(v instanceof ActionMenuView) {
                for(int j = 0; j < ((ActionMenuView)v).getChildCount(); j++) {

                    //Step 2: Changing the color of any ActionMenuViews - icons that
                    //are not back button, nor text, nor overflow menu icon.
                    final View innerView = ((ActionMenuView)v).getChildAt(j);

                    if(innerView instanceof ActionMenuItemView) {
                        int drawablesCount = ((ActionMenuItemView)innerView).getCompoundDrawables().length;
                        for(int k = 0; k < drawablesCount; k++) {
                            if(((ActionMenuItemView)innerView).getCompoundDrawables()[k] != null) {
                                final int finalK = k;

                                //Important to set the color filter in seperate thread,
                                //by adding it to the message queue
                                //Won't work otherwise.
                                innerView.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        ((ActionMenuItemView) innerView).getCompoundDrawables()[finalK].setColorFilter(colorFilter);
                                    }
                                });
                            }
                        }
                    }
                }
            }

            //Step 3: Changing the color of title and subtitle.
            toolbarView.setTitleTextColor(toolbarIconsColor);
            toolbarView.setSubtitleTextColor(toolbarIconsColor);

            //Step 4: Changing the color of the Overflow Menu icon.
            //setOverflowButtonColor(activity, colorFilter);
            setOverflowButtonColor(activity,toolbarView,toolbarIconsColor);
        }
    }


    private static void setOverflowButtonColor2(final Activity activity, final PorterDuffColorFilter colorFilter) {
        final String overflowDescription = activity.getString(R.string.abc_action_menu_overflow_description);
        final ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
        final ViewTreeObserver viewTreeObserver = decorView.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                final ArrayList<View> outViews = new ArrayList<View>();
                decorView.findViewsWithText(outViews, overflowDescription,
                        View.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
                if (outViews.isEmpty()) {
                    return;
                }
                AppCompatImageView overflow=(AppCompatImageView) outViews.get(0);
                overflow.setColorFilter(colorFilter);
                removeOnGlobalLayoutListener(decorView,this);
            }
        });
    }

    private static void setOverflowButtonColor(final Activity activity, final Toolbar toolbar, final int toolbarIconsColor) {
        final ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
        final ViewTreeObserver viewTreeObserver = decorView.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                if(toolbar != null && toolbar.getOverflowIcon() != null){
                    Drawable bg = DrawableCompat.wrap(toolbar.getOverflowIcon());
                    DrawableCompat.setTint(bg, toolbarIconsColor);
                }
                removeOnGlobalLayoutListener(decorView,this);
            }
        });
    }



    private static void removeOnGlobalLayoutListener(View v, ViewTreeObserver.OnGlobalLayoutListener listener) {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            v.getViewTreeObserver().removeGlobalOnLayoutListener(listener);
        }

        else {
            v.getViewTreeObserver().removeOnGlobalLayoutListener(listener);
        }

    }



    public static long getDate(String post_date) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm a", Locale.US);
/*02/28/2017 12:00 AM*/



        Date date = null;
        try {

            date = simpleDateFormat.parse(post_date);

          //  Log.i( "start" , "format " + simpleDateFormat.format(new Date(date.getTime())) );

            return  date.getTime();

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return 0;


    }

    public static void showToast(String s, boolean b) {

         if(b)
         Toasty.success(App.getInstance(), s, Toast.LENGTH_SHORT, true).show();
         else
         Toasty.error(App.getInstance(), s, Toast.LENGTH_SHORT, true).show();

    }

    public static void showToast(int string_res, boolean b) {

       // Toast.makeText(App.getInstance() , string_res , Toast.LENGTH_LONG).show();
        if(b)
            Toasty.success(App.getInstance(), App.getInstance().getString(string_res), Toast.LENGTH_SHORT, true).show();
        else
            Toasty.error(App.getInstance(), App.getInstance().getString(string_res), Toast.LENGTH_SHORT, true).show();
    }

    public static boolean checkRequestStatus(JSONObject jsonObject ) throws JSONException {

        if(jsonObject.has("message") && jsonObject.getString("status").equalsIgnoreCase("token error"))
        {
            Utility.showToast(R.string.Session, false);


        }

        return (jsonObject.has("status") && jsonObject.getString("status").equalsIgnoreCase("success")) ;

    }


    public static int lighten(int color, double fraction) {
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        red = lightenColor(red, fraction);
        green = lightenColor(green, fraction);
        blue = lightenColor(blue, fraction);
        int alpha = Color.alpha(color);
        return Color.argb(alpha, red, green, blue);
    }

    public static int darken(int color, double fraction) {
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        red = darkenColor(red, fraction);
        green = darkenColor(green, fraction);
        blue = darkenColor(blue, fraction);
        int alpha = Color.alpha(color);

        return Color.argb(alpha, red, green, blue);
    }

    private static int darkenColor(int color, double fraction) {
        return (int) Math.max(color - (color * fraction), 0);
    }

    private static int lightenColor(int color, double fraction) {

        return (int) Math.min(color + (color * fraction), 255);

    }

/*  public static int getCartItemsCount() {

        return  ((ArrayList<CartEntity>)new Gson().fromJson(Utility.getStringFromPref(App.getInstance(), "cart", "[]"), new TypeToken<ArrayList<CartEntity>>() {
        }.getType())).size();


    }*/

/*
    public static void updateCartBadge(Activity activity  , Menu menu ) {
        if(menu==null)return;

        MenuItem menuItem = menu.findItem(R.id.action_cart);
        if(menuItem==null)return;

        int cart_items_count = Utility.getCartItemsCount();
        //    if (cart_items_count > 0) {
     //   ActionItemBadge.update(activity, menuItem, activity.getResources().getDrawable(R.drawable.ic_shopping_cart  ), ActionItemBadge.BadgeStyles.RED, cart_items_count);
     //   } else {
          //  ActionItemBadge.hide(menuItem);
        //}
    }

*/

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }



    public static void makeCollapsingToolbarLayoutLooksGood(CollapsingToolbarLayout collapsingToolbarLayout) {

        final Typeface tf = Typeface.createFromAsset(App.getInstance().getAssets(), "fonts/DINNEXTLTW23REGULAR2.TTF");
        collapsingToolbarLayout.setCollapsedTitleTypeface(tf);
        collapsingToolbarLayout.setExpandedTitleTypeface(tf);
        collapsingToolbarLayout.setExpandedTitleTypeface(Typeface.createFromAsset(App.getInstance().getAssets(),
                "fonts/DINNEXTLTW23REGULAR2.TTF"));
        collapsingToolbarLayout.setCollapsedTitleGravity(Gravity.LEFT);


    /*   try {
            final Field field = collapsingToolbarLayout.getClass().getDeclaredField("mCollapsingTextHelper");
            field.setAccessible(true);

            final Object object = field.get(collapsingToolbarLayout);
            final Field tpf = object.getClass().getDeclaredField("mTextPaint");
            tpf.setAccessible(true);

            ((TextPaint) tpf.get(object)).setTypeface(Typeface.createFromAsset(App.getInstance().getAssets(), "fonts/DINNEXTLTW23REGULAR2.TTF"));
            ((TextPaint) tpf.get(object)).setColor(App.getInstance().getResources().getColor(R.color.white));
        } catch (Exception ignored) {
            ignored.printStackTrace();
            Log.i("excep","excep") ;
        }*/

    }


    public static Bitmap textAsBitmap(String text, float textSize, int textColor) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(textSize);
        paint.setColor(textColor);
        paint.setTextAlign(Paint.Align.LEFT);
        float baseline = -paint.ascent(); // ascent() is negative
        int width = (int) (paint.measureText(text) + 0.0f); // round
        int height = (int) (baseline + paint.descent() + 0.0f);
        Bitmap image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(image);
        canvas.drawText(text, 0, baseline, paint);
        return image;
    }


    public static String getAdminId(Context context) {

        if( getStringFromPref(context , "type").equals("2"))
            return getStringFromPref(context , "id") ;
        else
            return  getStringFromPref(context , "parent_id") ;


    }


    public static boolean hasOrders(Context context) {


        Log.i( "is_confirm_order" , getStringFromPref(context , "is_confirm_order")+" /?") ;



            return true;



    }




    public static String getAdminUserName(Context context) {

        if( getStringFromPref(context , "type").equals("2"))
            return getStringFromPref(context , "username") ;
        else
            return  getStringFromPref(context , "parent_user_name") ;


    }





    public static boolean isEmailValid(String email) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }


    public static String getHesabateLink(){

        return  Utility.getGenericStringFromPref(App.getInstance(), "api_link" , "http://mms.mms.ps" );

    }




    public static String encode_base_64(String text){

        // Sending side
        byte[] data = new byte[0];
        try {
            data = text.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String base64 = Base64.encodeToString(data, Base64.DEFAULT);

        return base64;

    }



    public static String decode_base_64(String base64 ){


// Receiving side
        byte[] data = Base64.decode(base64, Base64.DEFAULT);
        String text = null;
        try {
            text = new String(data, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return text;



    }

    public static boolean delete(File file) {

        Log.d("delete_file",""+file.toString());


        File[] flist = null;

        if(file == null){
            return false;
        }

        if (file.isFile()) {
            return file.delete();
        }

        if (!file.isDirectory()) {
            return false;
        }

        flist = file.listFiles();
        if (flist != null && flist.length > 0) {
            for (File f : flist) {


                if (!delete(f)) {
                    return false;
                }
            }
        }

        return file.delete();
    }



}
