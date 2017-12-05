package com.svspjptsi;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.svspjptsi.library.UserFunctions;
import com.maps.*;
import com.scan.*;

public class DashboardActivity extends Activity {
	UserFunctions userFunctions;
	Button btnLogout;
	Button btnScan;
	View scanQR;
	Button btnReport;
	public String arrdigit[];
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Cek login status pada database
        //userFunctions = new UserFunctions();
       // if(userFunctions.isUserLoggedIn(getApplicationContext())){
        	setContentView(R.layout.dashboard);
        	btnLogout = (Button) findViewById(R.id.btnLogout);
        	btnScan = (Button) findViewById(R.id.btnScan);
        	btnReport = (Button) findViewById(R.id.btnReport);
        	scanQR = findViewById(R.id.btnScan);
        	btnLogout.setOnClickListener(new View.OnClickListener() {
    			public void onClick(View arg0) {
    				//userFunctions.logoutUser(getApplicationContext());
    				Intent login = new Intent(getApplicationContext(), LoginActivity.class);
    	        	login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    	        	startActivity(login);
    	        	finish();
    			}
    		});
        	// Link to scan login screen
			btnScan.setOnClickListener(new View.OnClickListener() {
				public void onClick(View view){
					Intent i = new Intent (getApplicationContext(),DecoderActivity.class); 	
			    	startActivity(i);
			    	finish();
				}
			});
			// Link to report login screen
			btnReport.setOnClickListener(new View.OnClickListener() {
				public void onClick(View view) {
					Intent i = new Intent(getApplicationContext(),ReportActivity.class);
					startActivity(i);
				}
			});
        /*
        }
        
         else{// user is not logged in show login screen
        	Intent login = new Intent(getApplicationContext(), LoginActivity.class);
        	login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        	startActivity(login);
        	// Closing dashboard screen
        	finish();
        }
        */
        }
     
        private void cekspj() {new RetrieveTask().execute();}
        private class RetrieveTask extends AsyncTask<Void, Void, String>{
     		@Override
     		protected String doInBackground(Void... params) {
     			String strUrl = "http://ptsi.besaba.com/location_marker_mysql/getspj.php";				
     			URL url = null;
     			StringBuffer sb = new StringBuffer();
     			try{url = new URL(strUrl);
     				HttpURLConnection connection = (HttpURLConnection) url.openConnection();
     				connection.connect();
     				InputStream iStream = connection.getInputStream();				
     				BufferedReader reader = new BufferedReader(new InputStreamReader(iStream));			
     				String line = "";				
     				while((line=reader.readLine())!=null){sb.append(line);} //baca baris
     				reader.close();
     				iStream.close();							
     				} 
     			catch (MalformedURLException e){e.printStackTrace();} 
     			catch (IOException e){			e.printStackTrace();}		
     			return sb.toString();
     		}
     		
     		@Override
     		protected void onPostExecute(String result){
     			Toast.makeText(DashboardActivity.this, result, Toast.LENGTH_LONG).show();
     			super.onPostExecute(result);
     			new ParserTask().execute(result);
     		}
     	}
     	
     	// Background thread to parse the JSON data retrieved from MySQL server
     	private class ParserTask extends AsyncTask<String, Void, List<HashMap<String, String>>>{
     		@Override
     		protected List<HashMap<String,String>> doInBackground(String... params) {
     			MarkerJSONParser markerParser = new MarkerJSONParser();
     			JSONObject json = null;
     			try{json = new JSONObject(params[0]);} 
     			catch(JSONException e){e.printStackTrace();}
     			List<HashMap<String, String>> markersList = markerParser.parse(json);
     			return markersList;
     		}
     		
     		@Override
     		protected void onPostExecute(List<HashMap<String, String>> result) {
     			for(int i=0; i<result.size();i++){
     				HashMap<String, String>marker = result.get(i);
     				arrdigit[i]=marker.get("no");
     			}
     		}
     	
    }   
}