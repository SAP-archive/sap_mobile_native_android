package com.sap.dcode.online.ui.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.sap.dcode.online.R;

public class SplashScreen extends Activity{
	protected boolean active = true;
	protected int splashTime = 2000;
	protected String smpAppId;
	protected Boolean splashed = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash_screen);
		

		// thread for displaying the SplashScreen
		Thread splashThread = new Thread() {
			@Override
			public void run() {
				try {
					long startTime = System.currentTimeMillis();
					sleep(100);
					long waited = System.currentTimeMillis() - startTime;
					while (active && (waited < splashTime)) {
						sleep(100);
						if (active) {
							waited += 100;
						}
					}
				} catch (InterruptedException e) {
					// do nothing
				} finally {
					splashed = true;
					startMainActivity();
				}
			}
		};

		splashThread.start();

	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if (splashed) {
			startMainActivity();
		}
	}

    /*
     * Start the MainActivity... this gives the user access to the home screen and the
     * other parts of the app user interface.
     */
    private void startMainActivity() {
        Intent intent = new Intent();
        intent.setClass(SplashScreen.this, MAFLogonActivity.class);
        startActivity(intent);
        finish();
    }


}
