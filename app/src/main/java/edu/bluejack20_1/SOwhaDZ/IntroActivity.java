package edu.bluejack20_1.SOwhaDZ;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager.widget.ViewPager.SimpleOnPageChangeListener;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;

import static androidx.viewpager.widget.ViewPager.*;

public class IntroActivity extends AppCompatActivity {

        private ViewPager mSlideViewPager;
        private LinearLayout mDotLayout;
        private SliderAdapter slideAdapter;
        private Button mNextBtn;
        private Button mPrevBtn;
        private Button signInBtn;
        private TextView[] mDots;
        private SliderAdapter sliderAdapter;
        GoogleSignInClient mGoogleSignInClient;

        private int mCurrentPage;

        @Override
        protected void onCreate(Bundle savedInstanceState) {

                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_main);

                if (SplashScreen.sharedRef.loadNighModeState()) {
                        setTheme(R.style.darkTheme);
                } else setTheme(R.style.lightTheme);


                mSlideViewPager = (ViewPager) findViewById(R.id.slideViewPager);
                        mDotLayout = (LinearLayout) findViewById(R.id.dotsLayout);
                        addDotsIndicator(0);

                        mNextBtn = (Button) findViewById(R.id.btn_intro_next);
                        mPrevBtn = (Button) findViewById(R.id.btn_intro_prev);
                        signInBtn = (Button) findViewById(R.id.sign_in_button);

                        slideAdapter = new SliderAdapter(this);
                mSlideViewPager.setAdapter(slideAdapter);
                mSlideViewPager.addOnPageChangeListener(viewListener);

//                OnCLickListener
                mNextBtn.setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                        mSlideViewPager.setCurrentItem(mCurrentPage + 1);
                                }
                });

                mPrevBtn.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                        mSlideViewPager.setCurrentItem(mCurrentPage - 1);
                        }
                });
                
                signInBtn.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                switch (v.getId()) {
                                        case R.id.sign_in_button:
                                                signIn();
                                                break;
                                        // ...
                                }
                        }
                });

                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestEmail()
                        .build();
                mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
                GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
                updateUI(account);
        }

        private void signIn() {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, 0);
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
                super.onActivityResult(requestCode, resultCode, data);

                // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
                if (requestCode == 0) {
                        // The Task returned from this call is always completed, no need to attach
                        // a listener.
                        try{
                                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                                handleSignInResult(task);
                        }catch (Exception e){

                        }
                }
        }

        private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
                try {
                        GoogleSignInAccount account = completedTask.getResult(ApiException.class);

                        // Signed in successfully, show authenticated UI.
//                        Toast.makeText(IntroActivity.this,"HAI",Toast.LENGTH_SHORT).show();
                        updateUI(account);
                } catch (ApiException e) {
                        // The ApiException status code indicates the detailed failure reason.
                        // Please refer to the GoogleSignInStatusCodes class reference for more information.
//                        Log.w("ERORLogin", "signInResult:failed code=" + e.getStatusCode());
//                        Toast.makeText(IntroActivity.this,e.getStatusCode(),Toast.LENGTH_SHORT).show();
                        updateUI(null);
                }
        }

        public void addDotsIndicator(int position){
                mDots = new TextView[3];
                mDotLayout.removeAllViews();

                for (int i = 0; i < mDots.length;i++){
                        mDots[i] = new TextView(this);
                        mDots[i].setText(Html.fromHtml("&#8226;"));
                        mDots[i].setTextSize(32);
                        mDots[i].setTextColor(getResources().getColor(R.color.colorTransparentWhite));
                        mDotLayout.addView(mDots[i]);
                }

                if (mDots.length > 0){
                        mDots[position].setTextColor(Color.GRAY);
                }
        }

        OnPageChangeListener viewListener = new SimpleOnPageChangeListener(){
                @Override
                public void onPageSelected(int i){
                        addDotsIndicator(i);
                        mCurrentPage = i;
                        if (i == 0){
                                mNextBtn.setEnabled(true);
                                mPrevBtn.setEnabled(false);
                                mPrevBtn.setVisibility(View.INVISIBLE);
                                mNextBtn.setVisibility(View.VISIBLE);
                                signInBtn.setVisibility(View.GONE);

                                mNextBtn.setText(R.string.next);
                                mPrevBtn.setText("");
                        } else if (i == mDots.length - 1){
                                mNextBtn.setEnabled(false);
                                mPrevBtn.setEnabled(true);
                                signInBtn.setEnabled(true);
                                mPrevBtn.setVisibility(View.GONE);
                                mNextBtn.setVisibility(View.GONE);
                                signInBtn.setVisibility(View.VISIBLE);

                                mNextBtn.setText("");
                                mPrevBtn.setText(R.string.back);
                        } else {
                                mNextBtn.setEnabled(true);
                                mPrevBtn.setEnabled(true);
                                signInBtn.setEnabled(false);
                                mPrevBtn.setVisibility(View.VISIBLE);
                                mNextBtn.setVisibility(View.VISIBLE);
                                signInBtn.setVisibility(View.GONE);

                                mNextBtn.setText(R.string.next);
                                mPrevBtn.setText(R.string.back);
                        }
                }
        };

        private void updateUI(GoogleSignInAccount account) {
                if(account != null) {
                        Intent intent = new Intent(this, MainActivity.class);
                        startActivity(intent);
                        finish();
                }
        }
}