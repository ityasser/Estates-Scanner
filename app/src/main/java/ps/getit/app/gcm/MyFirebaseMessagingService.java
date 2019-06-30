package ps.getit.app.gcm;


import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;

import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import ps.getit.app.App;
import ps.getit.app.R;
import ps.getit.app.activities.DetailsDdliveryRequestsActivity;
import ps.getit.app.activities.DetailsUserRequestsActivity;
import ps.getit.app.activities.LoginActivity;
import ps.getit.app.activities.MainActivity;
import ps.getit.app.activities.ResponsesFromNotificationsActivity;
import ps.getit.app.utils.Utility;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages containing both notification
        // and data payloads are treated as notification messages. The Firebase console always sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ

        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {

            Log.d("gcm", "Message data payload: " + remoteMessage.getData());

                sendNotification(remoteMessage.getData());



            }





    }



    // [END receive_message]

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param data
     */

    private void sendNotification(Map<String, String> data) {


        Intent ds= new Intent(App.getInstance(), ResponsesFromNotificationsActivity.class);

        ds.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Intent intent = null;

        if(data.get("type").equals("update_location")) {
            Intent ff= new Intent("notification.view.badge");
            ff.putExtra("delivery_id",data.get("delivery_id"));
            ff.putExtra("lat",data.get("lat"));
            ff.putExtra("lng",data.get("lng"));
            ff.putExtra("estimated_time",data.get("estimated_time"));
            ff.putExtra("order_id",data.get("order_id"));


            App.getInstance().sendBroadcast(ff);

            return;

        }else if(data.get("type").equals("delivery")&& Utility.getStringFromPref(App.getInstance(),"is_admin").equals("1")){
            intent = new Intent(App.getInstance(), DetailsDdliveryRequestsActivity.class);
            intent.putExtra("id_Order", data.get("order_id") );
            intent.putExtra("is_from_gcm",true);

        } else if (data.get("type").equals("traking")&&Utility.getStringFromPref(App.getInstance(),"is_admin").equals("0")) {

            intent = new Intent(App.getInstance(), DetailsUserRequestsActivity.class);
            intent.putExtra("id_Order", data.get("order_id") );
            intent.putExtra("is_from_gcm",true);

        } else{



            intent = new Intent(App.getInstance(), MainActivity.class);

            if (data.get("type").equals("notfiy")) {
                intent.putExtra("type", "notfiy");
            }else if (data.get("type").equals("responds")) {
                intent.putExtra("id_Order", data.get("order_id") );
                intent .putExtra("type", "responds");
                intent.putExtra("status", data.get("status"));

                ds.putExtra("id_Order", data.get("order_id") );
                ds .putExtra("type", "responds");
                ds.putExtra("status", data.get("status"));
                ds.putExtra("body",  data.get("body"));

                startActivity(ds);

                Intent i= new Intent("responds.view.badge");
                i.putExtra("id_Order", data.get("order_id") );

                App.getInstance().sendBroadcast(i);


            } else if (data.get("type").equals("traking")) {
                intent.putExtra("type", "traking");
            } else if (data.get("type").equals("contact")) {
                intent.putExtra("type", "contact");
                App.getInstance().sendBroadcast(new Intent("contact.view.badge"));


            }

       }



        int reqstcode=(int) System.currentTimeMillis();

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setAction(""+ reqstcode);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        String channelId = getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.logo)
                        .setTicker(App.getInstance().getString(R.string.app_name))
                        .setContentTitle(data.get("title"))
                        //.setWhen(112)
                        .setContentText(data.get("body")  )


                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.

        if(!Utility.is_user_logged(this)) return;

        final String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        if (refreshedToken==null)return;
        Log.i("send",token);

        refreshToken(token);
    }
    private static void refreshToken(final String refreshedToken) {
        String url="";
        if(Utility.getStringFromPref(App.getInstance(),"is_admin").equals("-1")  ) {
            url = Utility.API_ROOT + "reg_keys_market";
        }else if(Utility.getStringFromPref(App.getInstance(),"is_admin").equals("0")){
            url   = Utility.API_ROOT+"reg_keys";
        }else if(Utility.getStringFromPref(App.getInstance(),"is_admin").equals("1")){
            url   = Utility.API_ROOT+"reg_keys_delivery";
        }
        Log.d("refreshTokenx",   Utility.getStringFromPref(App.getInstance(), "token")+"   "+  Utility.getStringFromPref(App.getInstance(),"is_admin")) ;

        StringRequest login_req = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("tokensend", response);
                if(Utility.checkRequestStatus(response)){
                    //saveUserData(new JSONObject(response));

                }


            }
        }, new Response.ErrorListener() {



            @Override
            public void onErrorResponse(VolleyError error) {




                  /*  Utility.handleErrors(LoginActivity.this , VolleyErrorHelper.getMessage(error ,LoginActivity.this));
                    Log.i( "error" , VolleyErrorHelper.getMessage(error ,LoginActivity.this) );
*/

            }


        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap();

                map.put( "key" , refreshedToken );

                map.put("lang",Utility.getLangDefult(App.getInstance()));


                map.put("token", Utility.getStringFromPref(App.getInstance(), "token"));



                return map;
            }



        };

        // ((App) getApplication()).cancelPendingRequests();

        (App.getInstance()).addToRequestQueue(login_req);










    }
    public static void sendRegistrationToServer(final Application context ) {
        // TODO: Implement this method to send token to your app server.


        if(!Utility.is_user_logged(context)) return;

        final String refreshedToken =  FirebaseInstanceId.getInstance().getToken();

        if (refreshedToken==null)return;


        Log.i("send",refreshedToken);

        refreshToken(refreshedToken);



    }
}
