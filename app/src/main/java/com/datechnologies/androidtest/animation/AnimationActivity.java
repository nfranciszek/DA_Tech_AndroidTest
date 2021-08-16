package com.datechnologies.androidtest.animation;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.datechnologies.androidtest.MainActivity;
import com.datechnologies.androidtest.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Screen that displays the D & A Technologies logo.
 * The icon can be moved around on the screen as well as animated.
 * */

public class AnimationActivity extends AppCompatActivity {

    private final long BACKGROUND_ANIM_STARTER_TIME = 4700;

    //==============================================================================================
    // Class Properties
    //==============================================================================================

    //==============================================================================================
    // Static Class Methods
    //==============================================================================================

    public static void start(Context context) {
        Intent starter = new Intent(context, AnimationActivity.class);
        context.startActivity(starter);
    }

    //==============================================================================================
    // Lifecycle Methods
    //==============================================================================================

    private RelativeLayout mRLBackground;

    private ImageView mCompanyLogo;
    private Button mFadeButton;


    private int _xDelta;
    private int _yDelta;

    MediaPlayer mPlayer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation);

        ActionBar actionBar = getSupportActionBar();

        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        // TODO: Make the UI look like it does in the mock-up. Allow for horizontal screen rotation.
        // TODO: Add a ripple effect when the buttons are clicked

        // TODO: When the fade button is clicked, you must animate the D & A Technologies logo.
        // TODO: It should fade from 100% alpha to 0% alpha, and then from 0% alpha to 100% alpha

        // TODO: The user should be able to touch and drag the D & A Technologies logo around the screen.

        // TODO: Add a bonus to make yourself stick out. Music, color, fireworks, explosions!!!


        mCompanyLogo = findViewById(R.id.CompanyLogo);
        mRLBackground = findViewById(R.id.RLBackground);


        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Animation anim = new ScaleAnimation(
                        0.3f, 1.0f,
                        0.3f, 1.0f,
                        Animation.RELATIVE_TO_SELF, 0.5f,
                        Animation.RELATIVE_TO_SELF, 0.5f);
                anim.setFillAfter(true);
                anim.setDuration(3000);
                mCompanyLogo.startAnimation(anim);

            }
        };

        runnable.run();


        if (mPlayer == null) {
            prepareBackgroundAnimation();
            playMusic();

        }


        mCompanyLogo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(900, 120);

                layoutParams.leftMargin = 90;
                layoutParams.topMargin = 280;
                mCompanyLogo.setLayoutParams(layoutParams);

                final int X = (int) motionEvent.getRawX();
                final int Y = (int) motionEvent.getRawY();
                switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
                        _xDelta = X - lParams.leftMargin;
                        _yDelta = Y - lParams.topMargin;
                        break;

                    case MotionEvent.ACTION_MOVE:
                        layoutParams.leftMargin = X - _xDelta;
                        layoutParams.topMargin = Y - _yDelta;
                        view.setLayoutParams(layoutParams);
                        break;
                }
                return true;
            }
        });
        mFadeButton = findViewById(R.id.FadeButton);

        mFadeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                activateFade();


            }
        });


    }


    private void playMusic() {

        mPlayer = MediaPlayer.create(getApplicationContext(), R.raw.bad_habits_ed_sheeran);
        mPlayer.start();

    }


    @Override
    protected void onPause() {
        super.onPause();
        mPlayer.stop();
        mPlayer.release();

    }


    private void activateFade() {

        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setInterpolator(new DecelerateInterpolator()); //add this
        fadeIn.setDuration(8000);

        Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setInterpolator(new AccelerateInterpolator()); //and this
        fadeOut.setStartOffset(1000);
        fadeOut.setDuration(20000);

        AnimationSet animation = new AnimationSet(false); //change to false
        animation.addAnimation(fadeIn);
        animation.addAnimation(fadeOut);
        mCompanyLogo.startAnimation(animation);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // API 5+ solution
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


    private void prepareBackgroundAnimation() {

        final Handler handler = new Handler();
        final Runnable r = new Runnable() {
            public void run() {
                mRLBackground.setBackgroundColor(getResources().getColor(R.color.colorPrimary));


                secondBackAnim();

            }
        };
        handler.postDelayed(r, BACKGROUND_ANIM_STARTER_TIME);


    }

    private void secondBackAnim() {

        final Handler handler = new Handler();
        final Runnable r = new Runnable() {
            public void run() {
                mRLBackground.setBackgroundColor(getResources().getColor(R.color.background));

            }
        };
        handler.postDelayed(r, 400);
    }


}
