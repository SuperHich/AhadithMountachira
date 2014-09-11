package com.freelance.android.ahadith;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;

import com.bugsense.trace.BugSenseHandler;

public class SplashActivity extends Activity {

//	ProgressBar pb;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        BugSenseHandler.initAndStartSession(SplashActivity.this, "bfdfcbf3");

		setContentView(R.layout.splash);


        startService(new Intent(this, downloadData.class));

//        pb = (ProgressBar) findViewById(R.id.downprogress);
		CountDownTimer countDownTimer = new CountDownTimer(6000, 1000) {

			@Override
			public void onTick(long millisUntilFinished) {
				// TODO Auto-generated method stub

			}



			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				goNext();
			}
		};
		countDownTimer.start();



	}

//    @Override
//    protected void onDestroy(){
//        super.onDestroy();
//
//        BugSenseHandler.flush(SplashActivity.this);
//
//    }

    public void goNext() {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        intent.putExtra("no", 0);
        startActivity(intent);
        finish();

    }
}

