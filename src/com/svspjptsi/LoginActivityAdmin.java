package com.svspjptsi;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.svspjptsi.library.DatabaseHandlerAdmin;
import com.svspjptsi.library.UserFunctionsAdmin;

public class LoginActivityAdmin extends Activity {
	Button btnLogin;
	EditText inputEmail;
	EditText inputPassword;
	TextView loginErrorMsg;

	// JSON Response node names
	private static String KEY_SUCCESS = "success";
	private static String KEY_ERROR = "error";
	private static String KEY_ERROR_MSG = "error_msg";
	private static String KEY_UID = "uid";
	private static String KEY_NAME = "name";
	private static String KEY_EMAIL = "email";
	private static String KEY_CREATED_AT = "created_at";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_admin);

		// Importing all assets like buttons, text fields
		inputEmail = (EditText) findViewById(R.id.loginEmailAdmin);
		inputPassword = (EditText) findViewById(R.id.loginPasswordAdmin);
		btnLogin = (Button) findViewById(R.id.btnLogin_Admin);
		loginErrorMsg = (TextView) findViewById(R.id.login_error_Admin);

		// Login button Click Event
		btnLogin.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {
				String email = inputEmail.getText().toString();
				String password = inputPassword.getText().toString();
				/*
				UserFunctionsAdmin userFunction = new UserFunctionsAdmin();
				Log.d("Button", "Login");
				JSONObject json = userFunction.loginAdmin(email, password);

				// check for login response
				try{if (json.getString(KEY_SUCCESS) != null) {
						loginErrorMsg.setText("");
						String res = json.getString(KEY_SUCCESS); 
						if(Integer.parseInt(res) == 1){
							// user successfully logged in
							// Store user details in SQLite Database
							DatabaseHandlerAdmin db = new DatabaseHandlerAdmin(getApplicationContext());
							JSONObject json_user = json.getJSONObject("user");
							
							// Clear all previous data in database
							userFunction.logoutAdmin(getApplicationContext());
							db.addUser(json_user.getString(KEY_NAME), json_user.getString(KEY_EMAIL), json.getString(KEY_UID), json_user.getString(KEY_CREATED_AT));						
							
							// Launch Dashboard Screen
							Intent dashboard = new Intent(getApplicationContext(), MainScreenActivity.class);
							
							// Close all views before launching Dashboard
							dashboard.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							startActivity(dashboard);
							
							// Close Login Screen
							finish();
						}else{
							// Error in login
							loginErrorMsg.setText("Incorrect username/password");
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				*/
				
				if(email.contentEquals("admin")&&password.contentEquals("admin")){
					// Launch Dashboard Screen
					Intent dashboard = new Intent(getApplicationContext(), MainScreenActivity.class);
					// Close all views before launching Dashboard
					dashboard.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(dashboard);
					// Close Login Screen
					finish();
				}
				else{
					// Error in login
					loginErrorMsg.setText("Incorrect username/password");
				}
				
			}
		});

	}
}
