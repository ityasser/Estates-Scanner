package ps.getit.app.activities;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ps.getit.app.Dialogs.BottomSheetDialog;
import ps.getit.app.R;
import ps.getit.app.fragments.DriverResponsesFragment;
import ps.getit.app.fragments.HomeMapFragment;
import ps.getit.app.fragments.MyOrderFragment;
import ps.getit.app.fragments.NewOrderFragment;
import ps.getit.app.fragments.Nofication_View;
import ps.getit.app.utils.Utility;

public class ResponsesFromNotificationsActivity extends AppCompatActivity {


    @BindView(R.id.response)
    TextView response;

    @BindView(R.id.accept)
    ImageView accept;

    @BindView(R.id.cancel)
    ImageView cancel;

    String order_id="",status="";

    MediaPlayer mp;
    int SorahAudio;


    @BindView(R.id.order_id1)
    TextView order_id1;

    PowerManager.WakeLock wl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_responses_from_notifications);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN |
                        WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_FULLSCREEN |
                        WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        ButterKnife.bind(this);

        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
         wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "My Tag");
        wl.acquire();

        SorahAudio = R.raw.daniel_simon;

        mp = MediaPlayer.create(getApplicationContext(), SorahAudio);
        mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mp.setLooping(true);
        AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        switch (am.getRingerMode()) {
            case AudioManager.RINGER_MODE_SILENT:
                mp.setVolume(0, 0);
                Log.i("MyApp", "Silent mode");

                break;
            case AudioManager.RINGER_MODE_VIBRATE:
                Log.i("MyApp", "Vibrate mode");
                mp.setVolume(0, 0);
                break;
            case AudioManager.RINGER_MODE_NORMAL:
                // mp.setVolume(100, 100);
                Log.i("MyApp", "Normal mode");
                break;
        }

        mp.start();

        final Bundle extras=getIntent().getExtras();


        if (extras!=null){

            response.setText(extras.getString("body"));

            if( extras.getString("type").equals("notfiy")){

    
            }else if(extras.getString("type").equals("traking")){

            

            }else if (extras.getString("type").equals("responds")){
                status=extras.getString("status");
                order_id= extras.getString("id_Order");
                order_id1.setText(order_id);


            }else if(extras.getString("type").equals("contact")) {
    
            }



        }else {



        }
    }
    
    @OnClick(R.id.cancel)
    public void cancel(){
        if (mp != null && mp.isPlaying()) {
            mp.stop();
        }
        finish();
    }



    @OnClick(R.id.accept)
    public void accept(){
        Intent intent =new Intent(this,MainActivity.class);
        intent .putExtra("type", "responds");
        intent .putExtra("status", status);
        intent .putExtra("id_Order", order_id);



        startActivity(intent);
        finish();

        if (mp != null && mp.isPlaying()) {
            mp.stop();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        wl.release();

    }
}
