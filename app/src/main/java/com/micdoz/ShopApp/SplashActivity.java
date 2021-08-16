package com.micdoz.ShopApp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * This class is used to display the animation when the application is first launched.
 */


public class SplashActivity extends AppCompatActivity {


    Animation animation;
    ImageView cart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        cart = (ImageView) findViewById(R.id.fireCart);
        Glide.with(this)
                .load(R.drawable.shopping_cart)
                .into(cart);


        animation = AnimationUtils.loadAnimation(getBaseContext(), R.anim.slider);
        cart.startAnimation(animation);
        Intent i = new Intent(this, LoginActivity.class);
        cart.postOnAnimationDelayed(new Runnable() {
            @Override
            public void run() {
                cart = null;
                startActivity(i);
            }
        }, 4700);


    }
}