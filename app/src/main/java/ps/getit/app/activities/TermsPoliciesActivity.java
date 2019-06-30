package ps.getit.app.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ps.getit.app.R;
import ps.getit.app.utils.Utility;

public class TermsPoliciesActivity extends BaseActivity {


    @BindView(R.id.aboutTv)
    TextView aboutTv;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_terms_policies);
        ButterKnife.bind(this);
        Utility.setTitle("الشروط والسياسات",this);

        aboutTv.setText(Html.fromHtml(Utility.getGenericStringFromPref(this , "policy"  , "")));


    }


    @OnClick(R.id.go_to_next)
    public void go_to_next(){
        Intent intt= new Intent(TermsPoliciesActivity.this , RegUserActivity.class);
        startActivityForResult(intt,Utility.login);
        finish();

    }

    @OnClick(R.id.cancel_action)
    public void cancel_action(){

        finish();
    }


}
