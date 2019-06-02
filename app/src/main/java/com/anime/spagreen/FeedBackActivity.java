package com.anime.spagreen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.anime.spagreen.utils.ApiResources;
import com.anime.spagreen.utils.ToastMsg;

import org.json.JSONObject;

public class FeedBackActivity extends AppCompatActivity {

    private AppCompatEditText etName,etEmail,etMsg;
    private Button btnSend;
    private String strName,strEmail,strMSg;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);


        etName=findViewById(R.id.et_name);
        etEmail=findViewById(R.id.et_email);
        etMsg=findViewById(R.id.et_msg);
        btnSend=findViewById(R.id.btn_send);

        dialog=new ProgressDialog(this);
        dialog.setMessage("Sending...");
        dialog.setCancelable(false);


        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                strName=etName.getText().toString();
                strEmail=etEmail.getText().toString();
                strMSg=etMsg.getText().toString();


                if(strMSg.equals("")){
                    new ToastMsg(FeedBackActivity.this).toastIconError("please enter your message");
                }else if (strName.equals("")){
                    new ToastMsg(FeedBackActivity.this).toastIconError("please enter your namel");
                }else if (strEmail.equals("")){
                    new ToastMsg(FeedBackActivity.this).toastIconError("please enter your email");
                }else {

                    String url = new ApiResources().getSendFeedback()+"&&name="+strName+"&&email="+strEmail+"&&message="+strMSg;

                    sendFeedback(url);

                }




            }
        });


    }

    private void sendFeedback(String url) {

        dialog.show();

        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                dialog.cancel();

                try {

                    if (response.getString("status").equals("success")){
                        new ToastMsg(FeedBackActivity.this).toastIconSuccess("Send Successfully");

                    }else {
                        new ToastMsg(FeedBackActivity.this).toastIconError("something went wrong !");

                    }

                }catch (Exception e){

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.cancel();
            }
        });

        Volley.newRequestQueue(FeedBackActivity.this).add(jsonObjectRequest);


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
