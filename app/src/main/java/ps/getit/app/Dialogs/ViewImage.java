package ps.getit.app.Dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import ps.getit.app.App;
import ps.getit.app.R;
import ps.getit.app.utils.Utility;


/**
 * Created by techmix on 20/11/2015.
 */
public class ViewImage extends DialogFragment {

    ArrayList<OnItemSelectedListener> listener=new ArrayList<>();


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

     //   codeInput.requestFocus();

      //  ((InputMethodManager) (getActivity()).getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

    }

ImageView image;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        LayoutInflater mInflater = (LayoutInflater) getActivity().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view= mInflater.inflate(R.layout.dialog_view_image,null,false);

        image  =(ImageView) view.findViewById(R.id.image);



        Bundle bundle = this.getArguments();
        if (bundle != null) {

            ;

            Glide.with(App.getInstance())
                    .load(   bundle.getString("image"))
                    .error(R.drawable.placeholder)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(image);
        }


        builder.setView(view);


        Dialog dialog= builder.create() ;
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        return dialog ;
    }

    @Override
    public void onResume() {
        super.onResume();
    }


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

    public void onItemSelected(String cur);
    }





}


