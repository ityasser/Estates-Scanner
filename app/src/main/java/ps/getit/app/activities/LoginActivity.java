package ps.getit.app.activities;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ps.getit.app.App;
import ps.getit.app.Dialogs.ActivateAccount;
import ps.getit.app.Dialogs.Review;
import ps.getit.app.R;
import ps.getit.app.gcm.MyFirebaseMessagingService;
import ps.getit.app.utils.Utility;

public class LoginActivity extends BaseActivity {

    @BindView(R.id.username_edit)
    EditText usernameEdit;
    @BindView(R.id.password_edit)
    EditText passwordEdit;

    @BindView(R.id.is_tech)
    CheckBox is_tech;


    private String Rqsut="login";

    String is_admin="0";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);
        Utility.setTitle(getString(R.string.login),this);

        usernameEdit.setText(Utility.getGenericStringFromPrefUser(this,"username",""));
        passwordEdit.setText(Utility.getGenericStringFromPrefUser(this,"password",""));


        //finished_orders
        is_tech.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    Rqsut="login_worker";
                    is_admin="1";
                }else {
                    Rqsut="login";
                    is_admin="0";
                }
            }
        });


       /* Review nn=new Review();
    nn.show(getSupportFragmentManager(),"sd");*/
    }




    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(data !=null) {
            if (requestCode == Utility.login) {
                usernameEdit.setText(data.getStringExtra("username"));//Utility.getGenericStringFromPrefUser(this,"mobile",""));
                passwordEdit.setText(data.getStringExtra("pass"));//Utility.getGenericStringFromPrefUser(this,"password",""));
            }
        }
        // This is important, otherwise the result will not be passed to the fragment
        super.onActivityResult(requestCode, resultCode, data);

    }

    @OnClick(R.id.Reg_new)
    public void Reg_new(){

        Intent intt= new Intent(LoginActivity.this , TermsPoliciesActivity.class);
        startActivityForResult(intt,Utility.login);
    }

    @OnClick(R.id.skip)
    public void skip(){
        Utility.addToPref(LoginActivity.this, "is_admin",  "0");

        Utility.addToPref(LoginActivity.this, "isLogin",false);
        startActivity(new Intent(this,MainActivity.class));
    }


    private void sendLoginRequest() {


        HashMap params = new HashMap();

        params.put("mobile", usernameEdit.getText().toString());
        params.put("password", passwordEdit.getText().toString());


        new App.CustomRequest(this, Rqsut, params) {
            @Override
            public void onSuccess(JSONObject jsonObject) {

                try {

                    JSONObject user = new JSONObject(jsonObject.optJSONObject("user").toString());
                    if (jsonObject.optString("active").equals("0")) {

                        Utility.showToast(getResources().getString(R.string.virviction), false);
                        ActivateAccount fragment = new ActivateAccount();
                        Bundle bundle = new Bundle();
                        bundle.putString("username", usernameEdit.getText().toString());
                        bundle.putString("pass", passwordEdit.getText().toString());

                        fragment.setArguments(bundle);
                        fragment.show(getSupportFragmentManager(), "sdfvsdfsf");

                    } else {

                        //{11-12 15:56:08.375 9528-9528/ps.imarket.app I/response: {"status":"success","message":"\u062a\u0645 \u062a\u0633\u062c\u064a\u0644 \u0627\u0644\u062f\u062e\u0648\u0644","user":{"id":429,"access_token":"2266a513e945b94c61a56db5d5dfa1b67261fe9ee2a8bf23d4ac3f277b5ea915","name":"","mobile":"0599143432","email":"","gender":"","address":"","active":"1","image":"","city_id":"","country_id":"","lat":0,"lng":0}}

                        Utility.addUserPrefById(LoginActivity.this, user.optString("id"));

                        Utility.addToPref(LoginActivity.this, "lat", user.optString("lat"));
                        Utility.addToPref(LoginActivity.this, "lng", user.optString("lng"));

                        Utility.addToPref(LoginActivity.this, "is_busy", user.optString("is_busy"));


                        Utility.addGenericToPref(LoginActivity.this, "lat", user.optString("lat"));
                        Utility.addGenericToPref(LoginActivity.this, "lng", user.optString("lng"));
                        Utility.addToPref(LoginActivity.this, "invitation_code", user.optString("invitation_code"));


                        Utility.addToPref(LoginActivity.this, "token", user.optString("access_token"));
                        Utility.addToPref(LoginActivity.this, "id", user.optString("id"));
                        Utility.addToPref(LoginActivity.this, "firstName", user.optString("name"));
                        Utility.addToPref(LoginActivity.this, "currency", user.optString("currency"));


                        Utility.addToPref(LoginActivity.this, "manual_login", true);


                        Utility.addToPref(LoginActivity.this, "username", user.optString("username"));
                        Utility.addToPref(LoginActivity.this, "password", passwordEdit.getText().toString());


                        Utility.addGenericToPrefUser(LoginActivity.this, "username", usernameEdit.getText().toString());

                        Utility.addGenericToPrefUser(LoginActivity.this, "password", passwordEdit.getText().toString());
                        Utility.addToPref(LoginActivity.this, "email", user.optString("email"));
                        Utility.addToPref(LoginActivity.this, "mobile", user.optString("mobile"));
                        Utility.addToPref(LoginActivity.this, "address", user.optString("address"));
                        Utility.addToPref(LoginActivity.this, "about", user.optString("about"));
                        Utility.addToPref(LoginActivity.this, "city", user.optString("city"));
                        Utility.addToPref(LoginActivity.this, "section", user.optString("section"));

                        Utility.addToPref(LoginActivity.this, "id_no", user.optString("id_no"));
                        Utility.addToPref(LoginActivity.this, "gender", user.optString("gender"));
                        Utility.addToPref(LoginActivity.this, "type", user.optString("type"));


                        Utility.addToPref(LoginActivity.this, "invitation_code", user.optString("invitation_code"));


                        if (is_admin.equals("1")) {
                            Utility.addToPref(LoginActivity.this, "image", Utility.SITE_ROOT + "uploads/admin/" + user.optString("image"));
                        } else if (is_admin.equals("0")) {
                            Utility.addToPref(LoginActivity.this, "image", Utility.SITE_ROOT + "uploads/users/" + user.optString("image"));
                        }


                        Utility.addToPref(LoginActivity.this, "region", user.optString("region"));


                        Utility.addToPref(LoginActivity.this, "isLogin", true);

                        Utility.addToPref(LoginActivity.this, "section_id", user.optString("section_id"));
                        Utility.addToPref(LoginActivity.this, "city_id", user.optString("city_id"));



                      /*  Intent intent = new Intent(LoginActivity.this, TablesActivity.class);
                        startActivity(intent);*/


                        Utility.addToPref(LoginActivity.this, "is_admin", is_admin);


                        MyFirebaseMessagingService.sendRegistrationToServer(getApplication());

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Utility.showToast(R.string.some_error, false);
                }
            }

            ;


        };
    }

    @OnClick(R.id.reserve_table_btn)
    public void onViewClicked() {

        if (usernameEdit.getText().toString().isEmpty()) {

            usernameEdit.setError(getString(R.string.empty_username));

        } else if (passwordEdit.getText().toString().isEmpty()) {

            passwordEdit.setError(getString(R.string.empty_password));

        } else if (Utility.isConnectingToInternet(LoginActivity.this)) {

            sendLoginRequest();

        } else {

            Utility.showErrorDialog(LoginActivity.this, getString(R.string.no_internet));

        }
    }


    @Override
    protected void onNewIntent(Intent intent) {

        usernameEdit.setText(Utility.getGenericStringFromPrefUser(this,"username",""));
        passwordEdit.setText(Utility.getGenericStringFromPrefUser(this,"password",""));

        super.onNewIntent(intent);
    }
}
