package com.example.myapplication;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

public class ZoomAnimation implements ViewPager.PageTransformer {
    private final float MIN_SCLE=0.70f;
    private final float MIN_LPHA=0.5f;
    @Override
    public void transformPage(@NonNull View page, float position) {
        int pagewidth=page.getWidth();
        int pageheight=page.getHeight();
        if (position < -1){
            page.setAlpha(0f);
        }else if (position <= 1){

            float sclefactor=Math.max(MIN_SCLE,1-Math.abs(position));
            float vertmargin=pageheight*(1-sclefactor)/2;
            float hertmargin=pagewidth*(1-sclefactor)/2;
            if (position<0){
                page.setTranslationX(hertmargin-vertmargin/2);
            }else {
                page.setTranslationX(-hertmargin+vertmargin/2);
            }
                page.setScaleX(sclefactor);
                page.setScaleY(sclefactor);
                page.setAlpha(MIN_LPHA+(sclefactor-MIN_SCLE)/(1-MIN_SCLE)*(1-MIN_LPHA));
        }else {
            page.setAlpha(0f);
        }

    }
}
