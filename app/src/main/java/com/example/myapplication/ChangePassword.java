package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.snackbar.Snackbar;
import com.swiftsynq.otpcustomview.CustomOtpView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static com.google.android.material.snackbar.Snackbar.LENGTH_LONG;

public class ChangePassword extends AppCompatActivity {
    EditText token,new_Pass,confirm_pass;
    TextView timer;
    Button change_btn;
    private CountDownTimer countDownTimer;
    CoordinatorLayout coordinatorLayout;
    private CountDownTimer mCountDownTimer;
    private boolean mTimerRunning;
    private long mTimeLeftInMillis = START_TIME_IN_MILLIS;
    private static final long START_TIME_IN_MILLIS = 600000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        token=findViewById(R.id.token);
        new_Pass=findViewById(R.id.pass_edt);
        confirm_pass=findViewById(R.id.confirm_pass_edt);
        change_btn=findViewById(R.id.btn_newpass_confirm);
        coordinatorLayout = findViewById(R.id.coordinator);
        timer=findViewById(R.id.timer);
        setTimer();
        CustomOtpView customOtpView = (CustomOtpView) findViewById(R.id.token);
        customOtpView.setAnimationEnable(true);// start animation when adding text
        customOtpView.setCursorVisible(false);
        customOtpView.setItemCount(6);
        customOtpView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {

            }});
        change_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePassword();
            }
        });
    }

    private void changePassword() {
        final String token_code=token.getText().toString().trim();
        final String new_password=new_Pass.getText().toString().trim();
        final String conf_Pass=confirm_pass.getText().toString().trim();
        StringRequest stringRequest=new StringRequest(Request.Method.POST, URL.RESET_PASSWORD, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        Toast.makeText(getApplicationContext(), jObj.getString("message"), Toast.LENGTH_LONG).show();
                        finish();
                        startActivity(new Intent(getApplication(),LoginActivity.class));
                    } else {
                        Toast.makeText(getApplicationContext(), jObj.getString("message"), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(ChangePassword.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ChangePassword.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String>map=new HashMap<>();
                map.put("token",token_code);
                map.put("password",new_password);
                map.put("confirmpassword",conf_Pass);
                return map;
            }
        }; VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    public void setTimer(){
        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                int minutes = (int) (mTimeLeftInMillis / 2000) / 60;
                int seconds = (int) (mTimeLeftInMillis / 1000) % 60;
                String timeLeftFormatted = "Time remaining "+String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
                timer.setText(timeLeftFormatted);
            }
            @Override
            public void onFinish() {
                Snackbar.make(coordinatorLayout, "Time Out ! Request again to reset password.", LENGTH_LONG).show();
                Intent x = new Intent(getApplication(),LoginActivity.class);
                startActivity(x);
                finish();
            }
        }.start();
    }
}
