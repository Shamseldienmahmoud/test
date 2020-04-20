package com.example.myapplication.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.ConfirmationActivity;
import com.example.myapplication.PaypalConfig;
import com.example.myapplication.R;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

import java.math.BigDecimal;

public class PaypalMethodActivity extends AppCompatActivity {
    private static final int PATPAL_REQUEST_CODE=7171;
    private static PayPalConfiguration configuration = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(PaypalConfig.PAYPAL_CLIENT_ID);
    String amount = "";
    Button pay;
    EditText amountEditText;
    @Override
    public void onDestroy() {
        stopService(new Intent(getApplication(), PayPalService.class));
        super.onDestroy();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paypal_method);
        pay=findViewById(R.id.btn_payment);
        amountEditText=findViewById(R.id.amount);
        Intent intent = new Intent(getApplication(),PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,configuration);
        getApplication().startService(intent);
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paymentNow();
            }
        });
    }
    private void paymentNow() {
        amount=amountEditText.getText().toString();
        if (!amount.isEmpty()){
            PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(String.valueOf(amount)),"USD","Payment Now"
                    ,PayPalPayment.PAYMENT_INTENT_SALE);
            Intent intent = new Intent(getApplication(), PaymentActivity.class);
            intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,configuration);
            intent.putExtra(PaymentActivity.EXTRA_PAYMENT,payPalPayment);
            startActivityForResult(intent,PATPAL_REQUEST_CODE);
        }else
            Toast.makeText(getApplication(), "Please enter amount", Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PATPAL_REQUEST_CODE){
            if (resultCode == RESULT_OK){
                PaymentConfirmation paymentConfirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (paymentConfirmation != null) {

                    try {
                       // JSONObject object=paymentConfirmation.toJSONObject();
                        //JSONObject object1=object.getJSONObject("response");
                        //String payment_id = object1.getString("id");
                        //String payment_state=object1.getString("state");
                        String detils=paymentConfirmation.toJSONObject().toString(4);
                        finish();
                        startActivity(new Intent(this, ConfirmationActivity.class)
                        .putExtra("paymentDetails",detils)
                        .putExtra("paymentamount",amount));


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(getApplication(), "Cancel", Toast.LENGTH_SHORT).show();
            }
        } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
            Toast.makeText(getApplication(), "INVALID", Toast.LENGTH_SHORT).show();
        }
    }
}
