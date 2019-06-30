package ps.getit.app.Dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

import ps.getit.app.R;


public class ResponseDialog extends android.support.v4.app.DialogFragment {


    static ResponseDialog dialog;
    static boolean isShowing = false;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // TODO Auto-generated method stub



        LayoutInflater mInflater = (LayoutInflater) getActivity()
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = mInflater.inflate(R.layout.resp_dialog, null);


        setCancelable(false);

        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(view); //ContentView(R.layout.popup_window);




        dialog.setContentView(view); //ContentView(R.layout.popup_window);



        return dialog;
    }


    @Override
    public void show(FragmentManager manager, String tag) {
        if(isAdded())
        {
            return; //or return false/true, based on where you are calling from
        }

        FragmentTransaction fragmentTransaction =  manager.beginTransaction();
        fragmentTransaction.add(this, tag);
        fragmentTransaction.commitAllowingStateLoss();

        // super.show(manager, tag);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        //  super.onSaveInstanceState(outState);
    }

    @Override
    public void dismissAllowingStateLoss() {

        if(getActivity()!=null && !(getActivity()).isFinishing() )
            super.dismissAllowingStateLoss();
        isShowing = false;

    }

    @Override
    public void dismiss() {

        // if(getActivity()!=null && !(getActivity()).isFinishing() )
        isShowing = false;
        super.dismiss();

    }

    public static void show (AppCompatActivity context){

        if(dialog == null)
            dialog = new ResponseDialog();

        if(!isShowing) {
            dialog.show(context.getSupportFragmentManager(), "");
            isShowing = true;
        }
    }
    public static void hide (){

        if(dialog != null)
            dialog.dismissAllowingStateLoss();

        isShowing = false;

    }

}

