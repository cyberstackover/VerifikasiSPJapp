package com.svspjptsi.library;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.content.Context;

public class UserFunctions {
	
	private JSONParser jsonParser;
	
	//localhost
//	private static String loginURL = "http://10.0.2.2/ah_login_api/index.php";
//	private static String registerURL = "http://10.0.2.2/ah_login_api/index.php";
//	private static String LOCATION_UPDATE_URL = "http://10.0.2.2/ah_login_api/index.php";
	
	//server
	private static String loginURL = "http://192.168.203.1/ah_login_api/index.php";
	private static String registerURL = "http://192.168.203.1/ah_login_api/index.php";
	private static String LOCATION_UPDATE_URL = "http://192.168.203.1/ah_login_api/index.php";
	
	private static String login_tag = "login";
	private static String register_tag = "register";
	private static String LOCATION_UPDATE_TAG = "update_location";
	
	// constructor
	public UserFunctions(){
		jsonParser = new JSONParser();
	}
	
	public JSONObject loginUser(String email, String password){
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", login_tag));
		params.add(new BasicNameValuePair("email", email));
		params.add(new BasicNameValuePair("password", password));
		JSONObject json = jsonParser.getJSONFromUrl(loginURL, params);
		// return json
		// Log.e("JSON", json.toString());
		return json;
	}
	
	/**
	 * function make Register Request
	 * @param name
	 * @param email
	 * @param password
	 * @param tipe
	 * */
	public JSONObject registerUser(String name, String email, String password, String tipe){
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", register_tag));
		params.add(new BasicNameValuePair("name", name));
		params.add(new BasicNameValuePair("email", email));
		params.add(new BasicNameValuePair("password", password));
		params.add(new BasicNameValuePair("tipe", tipe));
		
		// getting JSON Object
		JSONObject json = jsonParser.getJSONFromUrl(registerURL, params);
		// return json
		return json;
	}
	

	/**
	 * Function get Login status
	 * */
	
	public JSONObject updateUserGeoLocation(String lon, String lat, String email, String password) {
	      // Building Parameters
	      List<NameValuePair> params = new ArrayList<NameValuePair>();
	      params.add(new BasicNameValuePair("tag", LOCATION_UPDATE_TAG));
	      params.add(new BasicNameValuePair("email", email));
	      params.add(new BasicNameValuePair("password", password));
	      params.add(new BasicNameValuePair("lon", lon));
	      params.add(new BasicNameValuePair("lat", lat));
	      JSONObject json = jsonParser.getJSONFromUrl(LOCATION_UPDATE_URL, params);
	      return json;
	   }
	
	
	/**
	 * Function get Login status
	 * */
	public boolean isUserLoggedIn(Context context){
		DatabaseHandler db = new DatabaseHandler(context);
		int count = db.getRowCount();
		if(count > 0){
			// user logged in
			return true;
		}
		return false;
	}
	
	/**
	 * Function to logout user
	 * Reset Database
	 * */
	public boolean logoutUser(Context context){
		DatabaseHandler db = new DatabaseHandler(context);
		db.resetTables();
		return true;
	}
	
}
