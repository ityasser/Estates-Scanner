package ps.getit.app.Dialogs;

import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.OnClick;
import ps.getit.app.R;


/**
 * Created by techmix on 20/11/2015.
 */
public class BottomSheetDialog extends BottomSheetDialogFragment {

    ArrayList<OnItemSelectedListener> listener=new ArrayList<>();
    TextView tv;

    String order_id="";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.modal_bottomsheet, container, false);
        ButterKnife.bind(this, view);



        return view;
    }



@OnClick(R.id.btn_cancel)
public void btn_cancel(){

        this.dismissAllowingStateLoss();
}
    @OnClick(R.id.details)
    public void details(){
        if(listener!=null){
            for(OnItemSelectedListener l :listener)
                l.onItemSelected("2");
        }
        this.dismissAllowingStateLoss();
    }

    @OnClick(R.id.request_code)
    public void request_code(){
        if(listener!=null){
            for(OnItemSelectedListener l :listener)
                l.onItemSelected("1");
        }
        this.dismissAllowingStateLoss();
    }


    @OnClick(R.id.remove)
    public void remove(){

        if(listener!=null){
            for(OnItemSelectedListener l :listener)
                l.onItemSelected("3");
        }
        this.dismissAllowingStateLoss();    }

 public void setOnItemSelectedListener(OnItemSelectedListener listener){

     this.listener.add(listener);
 }

    public interface  OnItemSelectedListener{

    public void onItemSelected(String cur);
    }





}


