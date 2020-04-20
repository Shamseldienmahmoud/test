package com.example.myapplication.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.myapplication.LoginActivity;
import com.example.myapplication.R;
import com.example.myapplication.SharedPrefManger;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentAccount extends Fragment {
    TextView tv_username,tv_email,tv_fullname,id;
    Button logout;

    public FragmentAccount() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        tv_email=view.findViewById(R.id.email);
        tv_fullname=view.findViewById(R.id.fullname);
        tv_username=view.findViewById(R.id.username);
        id=view.findViewById(R.id.user_id);
        logout=view.findViewById(R.id.bt_logout);
        tv_username.setText(SharedPrefManger.getInstance(getActivity()).getusername());
        id.setText(SharedPrefManger.getInstance(getActivity()).getId());
        tv_fullname.setText(SharedPrefManger.getInstance(getActivity()).getfirstname()+" "+SharedPrefManger.getInstance(getActivity()).getlastname());
        tv_email.setText(SharedPrefManger.getInstance(getActivity()).getemail());
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPrefManger.getInstance(getActivity()).logout();
                getActivity().finish();
                startActivity(new Intent(getActivity(), LoginActivity.class));
            }
        });
        return view;
    }
}
