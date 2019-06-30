package ps.getit.app.Dialogs;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.Calendar;

import ps.getit.app.R;


/**
 * Created by techmix on 20/11/2015.
 */
public class TimeDialog extends DialogFragment implements TimePickerDialog.OnTimeSetListener{
    ArrayList<OnItemSelectedListener> listener=new ArrayList<>();

    String new_time="";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {



        Calendar mcurrentDate = Calendar.getInstance();

        final int hour = mcurrentDate.get(Calendar.HOUR_OF_DAY);
        final int minute = mcurrentDate.get(Calendar.MINUTE);



        TimePickerDialog time =   new TimePickerDialog(getActivity(),  this, hour, minute, true);

        time.setCancelable(true);
        time.setTitle(getString(R.string.chois_time));
        return time;

      /*  builder.setView(view);
        return builder.create() ;*/
    }


 public void setOnItemSelectedListener(OnItemSelectedListener listener){

     this.listener.add(listener);
 }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        if (listener != null) {
            for (OnItemSelectedListener l : listener)
                l.onItemSelected(null);
        }
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        String hourString = hourOfDay < 10 ? "0"+hourOfDay : ""+hourOfDay;
        String minuteString = minute < 10 ? "0"+minute : ""+minute;
        new_time=hourString+":"+minuteString;//+":"+"00";
        Log.d("new_time1",new_time) ;




        if (listener != null) {
            for (OnItemSelectedListener l : listener)
                l.onItemSelected(new_time);
        }


        TimeDialog.this.dismiss();
    }

    public interface  OnItemSelectedListener{

    public void onItemSelected(String Time);
    }






}


