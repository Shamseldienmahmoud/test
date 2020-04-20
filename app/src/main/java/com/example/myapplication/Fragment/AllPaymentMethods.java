package com.example.myapplication.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.braintreepayments.api.dropin.DropInActivity;
import com.braintreepayments.api.dropin.DropInRequest;
import com.braintreepayments.api.dropin.DropInResult;
import com.braintreepayments.api.interfaces.HttpResponseCallback;
import com.braintreepayments.api.internal.HttpClient;
import com.braintreepayments.api.models.PaymentMethodNonce;
import com.example.myapplication.R;
import com.example.myapplication.URL;
import com.example.myapplication.VolleySingleton;

import java.util.HashMap;
import java.util.Map;

public class AllPaymentMethods extends AppCompatActivity {
    private static final int REQUES_TCODE = 1234;
    String token,amount;
    HashMap<String,String>params;
    EditText editTextamount;
    Button payment;
    LinearLayout groupwating,grouppayment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_payment_methods);
        grouppayment=(LinearLayout)findViewById(R.id.paymentgroup);
        groupwating=(LinearLayout)findViewById(R.id.groupwating);
        payment=findViewById(R.id.btnPay);
        editTextamount=findViewById(R.id.etPrice);
        new getToken().execute();
        payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitPayment();
            }
        });
    }

    private void submitPayment() {
        DropInRequest dropInRequest=new DropInRequest().clientToken(token);
        startActivityForResult(dropInRequest.getIntent(this),REQUES_TCODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUES_TCODE) {
            if (resultCode == RESULT_OK){
                DropInResult result=data.getParcelableExtra(DropInResult.EXTRA_DROP_IN_RESULT);
                PaymentMethodNonce nonce=result.getPaymentMethodNonce();
                String stNonce = nonce.getNonce();
                if (!editTextamount.getText().toString().isEmpty()){
                    amount=editTextamount.getText().toString();
                    params=new HashMap<>();
                    params.put("amount",amount);
                    params.put("nonce",stNonce);
                    sendPayment();
                }else {
                    Toast.makeText(this, "please enter valid amount", Toast.LENGTH_SHORT).show();
                }
            }else if (resultCode == RESULT_CANCELED)
                Toast.makeText(this, "user canceled", Toast.LENGTH_SHORT).show();
                else {
                    Exception exception= (Exception) data.getSerializableExtra(DropInActivity.EXTRA_ERROR);
                    Log.d("PAYMENT_ERROR",exception.toString());


            }
        }
    }

    private void sendPayment() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL.PAYMENT_CHECKOUT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.toString().contains("Successful"))
                    Toast.makeText(getApplication(), "PAYMENT DONE", Toast.LENGTH_LONG).show();
                else {
                    Toast.makeText(getApplication(), "Payment Field", Toast.LENGTH_SHORT).show();

                }
                Log.d("VOLLEY_ERROR",response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("VOLLEY_ERROR",error.toString());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                if (params == null)
                    return null;
                    Map<String,String>stringMap=new HashMap<>();
                    for (String key:params.keySet())
                    {
                        stringMap.put(key,params.get(key));
                    }
                    return stringMap;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String>params=new HashMap<>();
                params.put("Content-Type","application/x-www-from-urlencoded");
                return params;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    private class getToken extends AsyncTask {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog=new ProgressDialog(AllPaymentMethods.this,android.R.style.Theme_DeviceDefault_Dialog);
            progressDialog.setCancelable(false);
            progressDialog.setTitle("please wait");
            progressDialog.show();
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            HttpClient httpClient = new HttpClient();
            httpClient.get(URL.PAYMENT_TOKEN, new HttpResponseCallback() {
                @Override
                public void success(final String responseBody) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            groupwating.setVisibility(View.GONE);
                            grouppayment.setVisibility(View.VISIBLE);
                            token=responseBody;
                        }
                    });
                }

                @Override
                public void failure(Exception exception) {
                    Log.d("PAYMENT_ERROR",exception.toString());

                }
            });

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            progressDialog.dismiss();
        }
    }
}
