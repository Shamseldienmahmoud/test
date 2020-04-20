package com.example.myapplication;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.example.myapplication.Fragment.FragmentAccount;
import com.example.myapplication.Fragment.FragmentHome;
import com.example.myapplication.Fragment.FragmentPrices;
import com.example.myapplication.Fragment.MapsFragment;
import com.example.myapplication.Fragment.PaymentMethodFragment;

public class HomeActivity extends AppCompatActivity {
    MeowBottomNavigation bottomNavigation;
    private final static int ID_HOME=1;
    private final static int ID_MAPS=2;
    private final static int ID_ACC=3;
    private final static int ID_PRICE=4;
    private final static int ID_Pocket=5;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bottomNavigation=findViewById(R.id.meow);
        bottomNavigation.add(new MeowBottomNavigation.Model(1, R.drawable.ic_iconhometb));
        bottomNavigation.add(new MeowBottomNavigation.Model(2,R.drawable.ic_location));
        bottomNavigation.add(new MeowBottomNavigation.Model(3, R.drawable.ic_iconprofil));
        bottomNavigation.add(new MeowBottomNavigation.Model(4, R.drawable.ic_attach_money_black_24dp));
        bottomNavigation.add(new MeowBottomNavigation.Model(5, R.drawable.ic_wallet));
       getSupportFragmentManager().beginTransaction().replace(R.id.frame,new FragmentHome()).commit();

        bottomNavigation.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
            @Override
            public void onClickItem(MeowBottomNavigation.Model item) {

            }
        });
        bottomNavigation.setOnReselectListener(new MeowBottomNavigation.ReselectListener() {
            @Override
            public void onReselectItem(MeowBottomNavigation.Model item) {
            }
        });
        bottomNavigation.setOnShowListener(new MeowBottomNavigation.ShowListener() {
            @Override
            public void onShowItem(MeowBottomNavigation.Model item) {
                Fragment selected = null;
                switch (item.getId()){
                    case ID_HOME:
                        selected = new FragmentHome();
                        break;
                    case  ID_ACC:
                        selected = new FragmentAccount();
                        break;
                    case  ID_PRICE:
                        selected = new FragmentPrices();
                        break;
                    case  ID_MAPS :
                        selected = new MapsFragment();
                        break;
                    case ID_Pocket:
                        selected = new PaymentMethodFragment();
                        break;

                }getSupportFragmentManager().beginTransaction().replace(R.id.frame,selected).commit();
            }

        });

        bottomNavigation.show(1,true);

    }
}
