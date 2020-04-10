package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.example.myapplication.Models.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {
    EditText first_name,last_name,national_id,phone_no,username,email,pass,repass;
    TextView signin,giude;
    Button signup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        first_name=findViewById(R.id.firstName);
        last_name=findViewById(R.id.LastName);
        national_id=findViewById(R.id.NationalID);
        phone_no=findViewById(R.id.Phone);
        username=findViewById(R.id.username);
        email=findViewById(R.id.Email);
        pass=findViewById(R.id.passwordEdittext);
        repass=findViewById(R.id.passwordEdittext2);
        signin=findViewById(R.id.tv_signin);
        giude=findViewById(R.id.tv_giude2);
        signup=findViewById(R.id.btn_signUp);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateUser();
            }
        });
    }

    private void CreateUser() {
        final String name = username.getText().toString().trim();
        final String password = pass.getText().toString().trim();
        final String repassword = repass.getText().toString().trim();
        final String nationalid = national_id.getText().toString().trim();
        final String mail = email.getText().toString().trim();
        final String firstname = first_name.getText().toString().trim();
        final String lastname = last_name.getText().toString().trim();
        final String phone = phone_no.getText().toString().trim();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL.URL_REGISTER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    Log.i("tagconvertstr", "["+jsonObject+"]");
                    if (!jsonObject.getBoolean("error")){
                        Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                      JSONObject object = jsonObject.getJSONObject("user");
                        User user = new User(
                                object.getInt("id"),
                                object.getString("fname"),
                                object.getString("lname"),
                                object.getString("username"),
                                object.getString("email"),
                                object.getString("mobile")
                        );
                        SharedPrefManger.getInstance(getApplicationContext()).userLogin(user);

                        //starting the profile activity
                        finish();
                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));

                    }
                    else {
                        Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(SignupActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        }){
            @Override
            protected Map<String, String> getPostParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("username", name);
                params.put("email", mail);
                params.put("password", password);
                params.put("confirmpassword", repassword);
                params.put("nationalid", nationalid);
                params.put("fname", firstname);
                params.put("lname", lastname);
                params.put("mobile", phone);
                return params;
            }
        }; VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);

    }
}
