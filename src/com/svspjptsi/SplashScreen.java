package com.svspjptsi;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

public class SplashScreen extends Activity {
	private static final int TIME = 1 * 2000;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                                WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.splashscreen);
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				Intent intent = new Intent(SplashScreen.this,LoginActivity.class);
				startActivity(intent);
				SplashScreen.this.finish();
			}
		}, TIME);
		new Handler().postDelayed(new Runnable() {
			  @Override
			  public void run() {} 
		}, TIME);
	}
	@Override
	public void onBackPressed() {
		this.finish();
		super.onBackPressed();
	}
	@Override
	  protected void onPause(){
	    super.onPause();
	    //closing transition animations
	    //this.overridePendingTransition(R.anim.fadein,R.anim.fadeout);
	    this.overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);
	    finish();
	  }
}
