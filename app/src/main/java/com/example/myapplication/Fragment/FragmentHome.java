package com.example.myapplication.Fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.myapplication.Adapter.MyCustomPagerAdapter;
import com.example.myapplication.LoginActivity;
import com.example.myapplication.R;
import com.example.myapplication.SharedPrefManger;
import com.example.myapplication.StationsRecycelr;
import com.example.myapplication.URL;
import com.example.myapplication.VolleySingleton;
import com.example.myapplication.ZoomAnimation;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.marcoscg.dialogsheet.DialogSheet;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import static android.app.Activity.RESULT_OK;
import static android.graphics.Color.BLACK;
import static android.graphics.Color.WHITE;
import static com.example.myapplication.R.drawable.lineone;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentHome extends Fragment {
    ViewPager viewPager;
    int images[] = {lineone, R.drawable.linetwo, R.drawable.linethree};
    TextView start,end,cost;
    View view;
    Bitmap bitmap;
    int ticket_price;
    int p1,p2,numOfStations;
    String n,s;
    String AES="AES";
    public FragmentHome() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);
        viewPager = (ViewPager) view.findViewById(R.id.view_pager);
        viewPager.setPageTransformer(true, new ZoomAnimation());
        viewPager.setAdapter(new MyCustomPagerAdapter(getContext(), images));
        start = view.findViewById(R.id.startstationbtn);
        end = view.findViewById(R.id.endstation);
        cost = view.findViewById(R.id.tv_cost);
        if (!SharedPrefManger.getInstance(getActivity()).isLoggedIn()) {
            getActivity().finish();
            startActivity(new Intent(getActivity(), LoginActivity.class));
        }
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), StationsRecycelr.class);
                startActivityForResult(i,0);

            }
        });
        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Intent i = new Intent(getActivity(), StationsRecycelr.class);
                startActivityForResult(i,1);
            }
        });


        return view;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                s = data.getStringExtra("start_station");
                start.setText(s);
                String end_station_id= data.getStringExtra("station_id");
                p1= Integer.parseInt(end_station_id);
                 }
        }
        else if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                n = data.getStringExtra("end_station");
                end.setText(n);
                String end_station_id= data.getStringExtra("station_id");
                p2= Integer.parseInt(end_station_id);
                ticket_price=(Coast(p1,p2));
                String title="your trip price "+String.valueOf(ticket_price)+" EGP";
                numOfStations=Math.abs(p1-p2);
                new DialogSheet(getActivity())
                        .setTitle("Payment")
                        .setMessage(title)
                        .setColoredNavigationBar(true)
                        .setTitleTextSize(20) // In SP
                        .setCancelable(false)
                        .setPositiveButton(android.R.string.ok, new DialogSheet.OnPositiveClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    qrcodegenerator();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                popup();
                                tripDetails();
                            }
                        })
                        .setNegativeButton(android.R.string.cancel, new DialogSheet.OnNegativeClickListener() {
                            @Override
                            public void onClick(View v) {
                            }
                        })
                        .setBackgroundColor(BLACK) // Your custom background color
                        .setButtonsColorRes(R.color.colorPrimary) // You can use dialogSheetAccent style attribute instead
                        .show();

               // Toast.makeText(getActivity(),ticket_price, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void qrcodegenerator() throws Exception {
        QRCodeWriter writer = new QRCodeWriter();
        String price = encrypt(String.valueOf(ticket_price),"shamsmahmoudaboelmagd");
        try {
            BitMatrix bitMatrix = writer.encode(price, BarcodeFormat.QR_CODE, 500, 500);
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            int[] pixels = new int[width * height];
            for (int y = 0; y < height; y++) {
                int offset = y * width;
                for (int x = 0; x < width; x++) {
                    pixels[offset + x] = bitMatrix.get(x, y) ? BLACK : WHITE;
                }
            }
            bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }
    public void popup() {
        // Create custom dialog object
        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.qrcode_image);
        dialog.setTitle("QR Code for your trip");
        ImageView image = (ImageView) dialog.findViewById(R.id.qr_code_image);
        image.setImageBitmap(bitmap);
        Button save = dialog.findViewById(R.id.button1);
        Button cancel = dialog.findViewById(R.id.button2);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveImage(bitmap);
                dialog.dismiss();
            }
        });

        dialog.show();
    }
    public String saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File wallpaperDirectory = new File(
                Environment.getExternalStorageDirectory().getPath() + "/QRCode/");
        // have the object build the directory structure, if needed.

        if (!wallpaperDirectory.exists()) {
            Log.d("dirrrrrr", "" + wallpaperDirectory.mkdirs());
            wallpaperDirectory.mkdirs();
        }

        try {
            File f = new File(wallpaperDirectory, Calendar.getInstance()
                    .getTimeInMillis() + ".jpg");
            f.createNewFile();   //give read write permission
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(getContext(),
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            Log.d("TAG", "File Saved::--->" + f.getAbsolutePath());

            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";

    }
    private static int Coast(int position1, int position2) {
        int cost = 0;
        int num_of_stations;
        num_of_stations = Math.abs(position1 - position2);
       if (num_of_stations>=0 && num_of_stations <= 9) {
            cost = 3;
        } else if (num_of_stations > 9 && num_of_stations<= 14) {
            cost =5;
        } else if (num_of_stations > 14) {
            cost = 7;
        }
        return cost;
    }
   public void tripDetails() {
       final String ss = s.toString().trim();
       final String e =n.toString().trim();
       final String num= String.valueOf(numOfStations);
       final String c = String.valueOf(ticket_price);
       final String id=(SharedPrefManger.getInstance(getActivity()).getId().toString().trim());
       StringRequest stringRequest = new StringRequest(Request.Method.POST, URL.USER_TRIP, new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject= new JSONObject(response);

                        if (!jsonObject.getBoolean("error")){
                            Toast.makeText(getActivity(),jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                            //  popup();
                        }else{
                            Toast.makeText(getActivity(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                        }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(),e.getMessage(), Toast.LENGTH_LONG).show();
                    Log.e("JSON Parser", "Error parsing data " + e.toString());

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
               Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("startstation", ss);
                params.put("endstation",e);
                params.put("tripcost", c);
                params.put("num_of_station",num);
                params.put("user_id",id);
                return params;
            }
        };
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }

    private String encrypt(String data,String password) throws Exception{
        SecretKey secretKey = generatekey(password);
        Cipher cipher= Cipher.getInstance(AES);
        cipher.init(Cipher.ENCRYPT_MODE,secretKey);
        byte [] encryptdata=cipher.doFinal(data.getBytes());
        String encriptionValue= Base64.encodeToString(encryptdata,Base64.DEFAULT);
        return encriptionValue;
    }

    private SecretKey generatekey(String password) throws Exception {
        final MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] bytes=password.getBytes("UTF-8");
        digest.update(bytes,0,bytes.length);
        byte [] key=digest.digest();
        SecretKey secretKey=new SecretKeySpec(key,"AES");
        return secretKey;
    }
}