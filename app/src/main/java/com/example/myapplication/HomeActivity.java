package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.example.myapplication.Fragment.FragmentAccount;
import com.example.myapplication.Fragment.FragmentHome;
import com.example.myapplication.Fragment.FragmentPrices;

public class HomeActivity extends AppCompatActivity {
    MeowBottomNavigation bottomNavigation;
    private final static int ID_HOME=1;
    private final static int ID_PRICE=2;
    private final static int ID_ACC=3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        bottomNavigation=findViewById(R.id.meow);
        bottomNavigation.add(new MeowBottomNavigation.Model(1, R.drawable.ic_home));
        bottomNavigation.add(new MeowBottomNavigation.Model(2, R.drawable.ic_attach_money));
        bottomNavigation.add(new MeowBottomNavigation.Model(3, R.drawable.ic_account));
        getSupportFragmentManager().beginTransaction().replace(R.id.frame,new FragmentHome()).commit();

        bottomNavigation.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
            @Override
            public void onClickItem(MeowBottomNavigation.Model item) {
                // your codes
            }
        });
        bottomNavigation.setOnReselectListener(new MeowBottomNavigation.ReselectListener() {
            @Override
            public void onReselectItem(MeowBottomNavigation.Model item) {
                // your codes
            }
        });
        bottomNavigation.setOnShowListener(new MeowBottomNavigation.ShowListener() {
            @Override
            public void onShowItem(MeowBottomNavigation.Model item) {
                Fragment selected = null;
                switch (item.getId()){
                    case ID_HOME:
                        selected = new FragmentHome();
                    case  ID_ACC:
                        selected = new FragmentAccount();
                    case  ID_PRICE:
                        selected = new FragmentPrices();
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.frame,selected).commit();

            }
        });
        bottomNavigation.show(1,true);

    }
}
