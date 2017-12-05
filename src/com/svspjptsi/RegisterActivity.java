package com.svspjptsi;

import org.json.JSONException;
import org.json.JSONObject;

import com.svspjptsi.library.DatabaseHandler;
import com.svspjptsi.library.UserFunctions;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;


public class RegisterActivity extends Activity {
	Button btnRegister;
	Button btnLinkToLogin;
	EditText inputFullName;
	EditText inputEmail;
	EditText inputPassword;
	TextView registerErrorMsg;
	TextView registerBerhasilMsg;
	public String tipe = "";
	RadioGroup rgroup;
	Button button3;
	
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
		setContentView(R.layout.register);

		// Importing all assets like buttons, text fields
		inputFullName = (EditText) findViewById(R.id.registerName);
		inputEmail = (EditText) findViewById(R.id.registerEmail);
		inputPassword = (EditText) findViewById(R.id.registerPassword);
		btnRegister = (Button) findViewById(R.id.btnRegister);
		registerBerhasilMsg = (TextView) findViewById(R.id.register_berhasil);
		registerErrorMsg = (TextView) findViewById(R.id.register_error);
		
		Button button3 = (Button) findViewById(R.id.buttonUser);
		
		final RadioGroup rgroup = (RadioGroup)findViewById(R.id.radioUser);
	    final RadioButton Toko = (RadioButton)findViewById(R.id.radioToko);
	    final RadioButton Driver = (RadioButton)findViewById(R.id.radioDriver);
		
	    button3.setOnClickListener(new View.OnClickListener() {         
	        public void onClick(View view) {

	            String name = inputFullName.getText().toString();
	            String email = inputEmail.getText().toString();
	            String password = inputPassword.getText().toString();

	            if (rgroup.getCheckedRadioButtonId() == Toko.getId()){tipe = "Toko";}
	            else if(rgroup.getCheckedRadioButtonId() == Driver.getId()){tipe = "Driver";}

	            if (name.contentEquals("")||email.contentEquals("")||password.contentEquals("")){
	                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
	                builder.setMessage(R.string.nullAlert)
	                	   .setTitle(R.string.alertTitle);
	                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
	                   public void onClick(DialogInterface dialog, int id) {}
	                });
	                AlertDialog dialog = builder.show();
	            }
	            else{new UserFunctions(); //.execute();
	            }           
	        }
	    });
	    
		// Register Button Click event
		btnRegister.setOnClickListener(new View.OnClickListener() {			
			public void onClick(View view) {
				String name = inputFullName.getText().toString();
				String email = inputEmail.getText().toString();
				String password = inputPassword.getText().toString();
				UserFunctions userFunction = new UserFunctions();
				JSONObject json = userFunction.registerUser(name, email, password, tipe);
				
				// check for login response
				try {if (json.getString(KEY_SUCCESS) != null) {
						registerErrorMsg.setText("");
						registerBerhasilMsg.setText("");
						String res = json.getString(KEY_SUCCESS); 
						if(Integer.parseInt(res) == 1){
							// user successfully registred
							// Store user details in SQLite Database
							DatabaseHandler db = new DatabaseHandler(getApplicationContext());
							JSONObject json_user = json.getJSONObject("user");
							
							// Clear all previous data in database
							userFunction.logoutUser(getApplicationContext());
							db.addUser(json_user.getString(KEY_NAME), json_user.getString(KEY_EMAIL), json.getString(KEY_UID), json_user.getString(KEY_CREATED_AT));						
							// Launch Dashboard Screen
							//Intent dashboard = new Intent(getApplicationContext(), DashboardActivity.class);
							//dashboard.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							//startActivity(dashboard);
							// Error in registration
							registerBerhasilMsg.setText("Berhasil ditambahkan user baru");
							//startActivity(RegisterActivity.class);
							// Close all views before launching Dashboard
							
							// Close Registration Screen
							//finish();
						}else{// Error in registration
							registerErrorMsg.setText("Gagal menambahkan user baru");
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});	    
	}
}
