package com.example.swornim.kawadi;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class CustomViewPager extends ViewPager {


        public CustomViewPager(Context context) {
            super(context);
        }

        public CustomViewPager(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
           return false;
           //avoid swapping
        }

        @Override
        public boolean onInterceptTouchEvent(MotionEvent event) {
            return false;
            //avoid swapping

        }

    }