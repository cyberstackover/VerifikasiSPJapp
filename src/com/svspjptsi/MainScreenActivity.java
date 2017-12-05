package com.svspjptsi;

import com.scan.DecoderActivity;
import com.svspjptsi.library.UserFunctions;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class MainScreenActivity extends Activity{
	
	UserFunctions userFunctions;
	Button btnViewUser;
	Button btnNewUser;
	Button btnLogutUser;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		//userFunctions = new UserFunctions();
		// Buttons
				
		//   if(userFunctions.isUserLoggedIn(getApplicationContext())){
			   setContentView(R.layout.main_screen);
	        	btnViewUser = (Button) findViewById(R.id.btnViewUser);
	    		btnNewUser = (Button) findViewById(R.id.btnCreateUser);
	    		btnLogutUser = (Button) findViewById(R.id.btnLogoutUser);
	        	
	        	btnLogutUser.setOnClickListener(new View.OnClickListener() {
	    			
	    			public void onClick(View arg0) {
	    				// TODO Auto-generated method stub
	    				//userFunctions.logoutUser(getApplicationContext());
	    				Intent login = new Intent(getApplicationContext(), LoginActivity.class);
	    	        	login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	    	        	startActivity(login);
	    	        	// Closing dashboard screen
	    	        	finish();
	    			}
	    		});
		
		// view products click event
		btnViewUser.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View view) {
				// Launching All products Activity
				Intent i = new Intent(getApplicationContext(), AllUserActivity.class);
				startActivity(i);
				
			}
		});
		
		// view products click event
		btnNewUser.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View view) {
				// Launching create new product activity
				Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
				startActivity(i);
				
			}
		});
		
		//  }
		/*
		else{
			// user is not logged in show login screen
	        	Intent login = new Intent(getApplicationContext(), LoginActivity.class);
	        	login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	        	startActivity(login);
	        	// Closing dashboard screen
	        	finish();
	        }
	      */
	        
	    }
	    
}
