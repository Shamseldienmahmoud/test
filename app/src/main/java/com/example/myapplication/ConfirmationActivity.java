package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.myapplication.Fragment.PaymentMethodFragment;

import org.json.JSONException;
import org.json.JSONObject;

public class ConfirmationActivity extends AppCompatActivity {
    TextView tv_order,tv_amount,tv_status;
    Button back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);
        back=findViewById(R.id.back_to_wallet);
        tv_amount=(TextView)findViewById(R.id.amount);
        tv_order=(TextView)findViewById(R.id.order);
        tv_status=(TextView)findViewById(R.id.status);
        Intent intent = getIntent();
        try {
            JSONObject jsonObject = new JSONObject(intent.getStringExtra("paymentDetails"));
            showdetils(jsonObject.getJSONObject("response"),intent.getStringExtra("paymentamount"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Fragment fragment = new PaymentMethodFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_contanir, fragment, fragment.getClass().getSimpleName())
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    private void showdetils(JSONObject response, String paymentamount) {

        try {
            tv_order.setText(response.getString("id"));
            tv_status.setText(response.getString("state"));
            tv_amount.setText("EGP "+paymentamount);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
