package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.myapplication.Models.User;
import com.example.myapplication.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    EditText user,pass;
    Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login=findViewById(R.id.bt_signin);
        user=findViewById(R.id.email_editText);
        pass=findViewById(R.id.pass_editText);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login();
            }
        });
    }

    private void Login() {
        final String username=user.getText().toString().trim();
        final String password=pass.getText().toString().trim();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL.URL_LOGIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    if (!jsonObject.getBoolean("error")){
                   Toast.makeText(LoginActivity.this, ""+jsonObject.get("message"), Toast.LENGTH_SHORT).show();
                            SharedPrefManger.getInstance(getApplicationContext())
                                    .userLogin(
                                            jsonObject.getInt("id"),
                                            jsonObject.getString("fname"),
                                            jsonObject.getString("lname"),
                                            jsonObject.getString("email"),
                                            jsonObject.getString("username")
                                    );

                       Intent i=new Intent(getApplication(),HomeActivity.class);
                       startActivity(i);
                       finish();
                    }
                    else {
                        Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LoginActivity.this,error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
               Map<String,String>params=new HashMap<>();
               params.put("username",username);
               params.put("password",password);
                return params;
            }
        };VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    public void register(View view) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent x=new Intent(getApplication(),SignupActivity.class);
                startActivity(x);

            }
        });
    }

    public void resetpass(View view) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent x=new Intent(getApplication(),ResetPassword.class);
                startActivity(x);

            }
        });

    }
}
