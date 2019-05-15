package com.anime.spagreen;

import android.app.ProgressDialog;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.anime.spagreen.utils.ApiResources;
import com.anime.spagreen.utils.ToastMsg;
import com.anime.spagreen.utils.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

public class SignUpActivity extends AppCompatActivity {

    private EditText etName,etEmail,etPass;
    private Button btnSignup;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("SignUp");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait");
        dialog.setCancelable(false);

        etName=findViewById(R.id.name);
        etEmail=findViewById(R.id.email);
        etPass=findViewById(R.id.password);
        btnSignup=findViewById(R.id.signup);

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isValidEmailAddress(etEmail.getText().toString())){
                    new ToastMsg(SignUpActivity.this).toastIconError("please enter valid email");
                }else if(etPass.getText().toString().equals("")){
                    new ToastMsg(SignUpActivity.this).toastIconError("please enter password");
                }else if (etName.getText().toString().equals("")){
                    new ToastMsg(SignUpActivity.this).toastIconError("please enter name");
                }else {
                    String email = "&&email="+etEmail.getText().toString();
                    String pass = "&&password="+etPass.getText().toString();
                    String name = "&&name="+etName.getText().toString();
                    String url = new ApiResources().getSignup()+email+pass+name;
                    signUp(url);
                }
            }
        });
    }

    private void signUp(String url){
        dialog.show();
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET,url,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                dialog.cancel();
                try {
                    Log.e("SIGN UP RES:::::", String.valueOf(response));
                    if (response.getString("status").equals("success")){
                        new ToastMsg(SignUpActivity.this).toastIconSuccess("Successfully registered");
                        startActivity(new Intent(SignUpActivity.this,LoginActivity.class));
                        finish();
                    }else if (response.getString("status").equals("error")){
                        new ToastMsg(SignUpActivity.this).toastIconError(response.getString("data"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.cancel();
                new ToastMsg(SignUpActivity.this).toastIconError(getString(R.string.error_toast));
            }
        });

        new VolleySingleton(SignUpActivity.this).addToRequestQueue(jsonObjectRequest);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }

}
