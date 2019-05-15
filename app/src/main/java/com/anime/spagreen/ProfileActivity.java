package com.anime.spagreen;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.anime.spagreen.utils.ApiResources;
import com.anime.spagreen.utils.ToastMsg;
import com.anime.spagreen.utils.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

public class ProfileActivity extends AppCompatActivity {


    private EditText etName,etEmail,etPass;
    private Button btnUpdate;
    private ProgressDialog dialog;
    private String URL="",strGender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("My Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait");
        dialog.setCancelable(false);

        etName=findViewById(R.id.name);
        etEmail=findViewById(R.id.email);
        etPass=findViewById(R.id.password);
        btnUpdate=findViewById(R.id.signup);

        SharedPreferences preferences=getSharedPreferences("user",MODE_PRIVATE);
        final String id = "&&id="+preferences.getString("id","0");
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etEmail.getText().toString().equals("")){
                    Toast.makeText(ProfileActivity.this,"Please enter valid email",Toast.LENGTH_LONG).show();
                }else if (etName.getText().toString().equals("")){
                    Toast.makeText(ProfileActivity.this,"Please enter name",Toast.LENGTH_LONG).show();
                }else {
                    String email = "&&email="+etEmail.getText().toString();
                    String pass = "&&password="+etPass.getText().toString();
                    String name = "&&name="+etName.getText().toString();

                    if (etPass.getText().toString().equals("")){
                        URL = new ApiResources().getProfileUpdateURL()+id+email+name+pass;
                    }else {
                        URL = new ApiResources().getProfileUpdateURL()+id+email+name;
                    }

                    updateProfile(URL);
                }
            }
        });
        String urlProfile = new ApiResources().getProfileURL()+preferences.getString("email","null");


        getProfile(urlProfile);

    }



    private void getProfile(String url){


        dialog.show();
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET,url,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                dialog.cancel();

                try {

                    Log.e("PROFILE", String.valueOf(response));

                    etName.setText(response.getString("name"));
                    etEmail.setText(response.getString("email"));
                    strGender = "&&gender="+response.getString("gender");


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.cancel();
                Toast.makeText(ProfileActivity.this,getString(R.string.error_toast),Toast.LENGTH_LONG).show();
            }
        });

        new VolleySingleton(ProfileActivity.this).addToRequestQueue(jsonObjectRequest);


    }

    private void updateProfile(String url){
        dialog.show();
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET,url,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                dialog.cancel();
                try {
                    Log.e("SIGN UP RES:::::", String.valueOf(response));
                    if (response.getString("status").equals("success")){
                        new ToastMsg(ProfileActivity.this).toastIconSuccess("successfully updated");
                        startActivity(new Intent(ProfileActivity.this,MainActivity.class));
                        finish();
                    }else if (response.getString("status").equals("error")){
                        Toast.makeText(ProfileActivity.this,response.getString("data"),Toast.LENGTH_LONG).show();
                        new ToastMsg(ProfileActivity.this).toastIconError(response.getString("data"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.cancel();
                new ToastMsg(ProfileActivity.this).toastIconError("something went wrong ! try later");
            }
        });

        new VolleySingleton(ProfileActivity.this).addToRequestQueue(jsonObjectRequest);
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
}
