package com.eebbk.senior.greenbrowser.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Window;

import com.eebbk.senior.greenbrowser.R;
import com.eebbk.welcomeview.WelcomeView;

public class OperatingtipsActivity extends Activity {

	private static final String KEY_STRING = "visit";
	public static final String NAME_STRING = "data";

	private boolean isMenu = false;

	private WelcomeView wv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Intent uriIntent = getIntent();
		if (uriIntent.getData() != null) {
			Intent homeIntent = new Intent(this, HomeActivity.class);
			homeIntent.setData(uriIntent.getData());
			this.startActivity(homeIntent);
			this.finish();
		} else {

			wv = (WelcomeView) findViewById(R.id.viewpager);

			isMenu = getIntent().getBooleanExtra(HomeActivity.KEYCODE_MENU,
					false);
			if (!isMenu) {
				SharedPreferences sharedata = getSharedPreferences(NAME_STRING,
						MODE_PRIVATE);
				int visited = sharedata.getInt(KEY_STRING, 0);
				if (visited == 1) {
					Intent homeIntent = new Intent(this, HomeActivity.class);
					this.startActivity(homeIntent);
					this.finish();
				}
			}
		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);

		if (intent.getData() != null) {
			Intent homeIntent = new Intent(this, HomeActivity.class);
			homeIntent.setData(intent.getData());
			this.startActivity(homeIntent);
			this.finish();
		}
	}

}
