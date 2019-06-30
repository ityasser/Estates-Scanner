package ps.getit.app.Dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ps.getit.app.R;
import ps.getit.app.codeinputlib.CodeInput;


/**
 * Created by techmix on 20/11/2015.
 */
public class Review extends DialogFragment {

    ArrayList<OnItemSelectedListener> listener=new ArrayList<>();


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

     //   codeInput.requestFocus();

      //  ((InputMethodManager) (getActivity()).getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

    }
    CodeInput codeInput;
    String rest_id;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        LayoutInflater mInflater = (LayoutInflater) getActivity().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view= mInflater.inflate(R.layout.review,null,false);

         codeInput =(CodeInput) view.findViewById(R.id.code);
        codeInput.requestFocus();
        codeInput.setFocusable(true);
        codeInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                codeInput.postDelayed(mShowSoftInputRunnable, 400);

            }
        });

        builder.setView(view);


        Dialog dialog= builder.create() ;
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        return dialog ;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private Runnable mShowSoftInputRunnable = new Runnable() {
        @Override
        public void run() {


            InputMethodManager input = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            input.showSoftInput(codeInput, InputMethodManager.SHOW_IMPLICIT);

        }
    };

    public void showSoftKeyboard(View view){
        if(view.requestFocus()){
            InputMethodManager imm =(InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view,InputMethodManager.SHOW_IMPLICIT);
        }
    }


 public void setOnItemSelectedListener(OnItemSelectedListener listener){

     this.listener.add(listener);
 }

    public interface  OnItemSelectedListener{

    public void onItemSelected(String cur, float hh);
    }





}


