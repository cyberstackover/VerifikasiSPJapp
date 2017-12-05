package com.maps;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import com.svspjptsi.*;
import com.svspjptsi.LoginActivity;


@SuppressLint("NewApi")
public class Mapsv4 extends FragmentActivity {
    private GoogleMap googleMap;
    private int mapType = GoogleMap.MAP_TYPE_NORMAL;
    // Variabel utk menampilkan koordinat posisi driver
    	public Double slat,slng;
    // Variabel utk menyimpan koordinat posisi driver pada database
    	public Double lat=-7.268670,lng=112.768831;
  
    LoginActivity login = new LoginActivity();
    public String namedriver=login.namadriver;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maps_activity);
        FragmentManager fragmentManager = getSupportFragmentManager();
        SupportMapFragment mapFragment  = (SupportMapFragment)
        fragmentManager.findFragmentById(R.id.map);
        googleMap = mapFragment.getMap();

        //ambil data yg di parsing
        Bundle getLoc = getIntent().getExtras();
        slat = getLoc.getDouble("showlat");
        slng = getLoc.getDouble("showlong");
        //lat = getLoc.getDouble("latude");
        //lng = getLoc.getDouble("lotude");
        
        //tampilkan lokasi driver saat itu juga
        LatLng scoord = new LatLng(slat, slng);
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.addMarker(new MarkerOptions()
                .position(scoord)
                .title("Posisi Driver")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        
        LatLng coord = new LatLng(lat, lng);
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.addMarker(new MarkerOptions()
                .position(coord)
                .title("Posisi Toko")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE)));
        //interaksi dengan db
        //sendToServer(coord);
        //getToServer();
        
        googleMap.getUiSettings().setCompassEnabled(true);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);

        LatLng cameraLatLng = coord;
        float cameraZoom = 10;
        if(savedInstanceState!=null){
            mapType = savedInstanceState.getInt("map_type", GoogleMap.MAP_TYPE_NORMAL);
            double savedLat = savedInstanceState.getDouble("lat");
            double savedLng = savedInstanceState.getDouble("lng");
            cameraLatLng = new LatLng(savedLat, savedLng);
            cameraZoom = savedInstanceState.getFloat("zoom", 10);
        }
        googleMap.setMapType(mapType);
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(cameraLatLng, cameraZoom));
    }
    @Override
    public void onBackPressed() {
    	onResume();
    	new AlertDialog.Builder(this)
    		.setIcon(android.R.drawable.ic_dialog_alert)
    		.setTitle("Tutup Map")
    		.setMessage("Anda yakin?")
    		.setCancelable(false)
    		.setPositiveButton("Ya",new DialogInterface.OnClickListener() {
    			public void onClick(DialogInterface dialog, int id) {
    				Intent dash = new Intent(getApplicationContext(), DashboardActivity.class);
    	        	startActivity(dash);
    	        	finish();
    			}
    		})
    		.setNegativeButton("Tidak", null).show();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // agar map fokus ke posisi driver
        LatLng cameraLatLng = googleMap.getCameraPosition().target;
        float cameraZoom = googleMap.getCameraPosition().zoom;
        outState.putInt("map_type", mapType);
        outState.putDouble("lat", cameraLatLng.latitude);
        outState.putDouble("lng", cameraLatLng.longitude);
        outState.putFloat("zoom", cameraZoom);
    }
   
 	//private void sendToServer(LatLng latlng) {	new SaveTask().execute(latlng);}
 	//public void getToServer(){					new RetrieveTask().execute();}

 	private class SaveTask extends AsyncTask<LatLng, Void, Void> {
 		@Override
 		protected Void doInBackground(LatLng... params) {
 			String lat = Double.toString(params[0].latitude);
 			String lng = Double.toString(params[0].longitude);
 			String nama = namedriver;
 			String strUrl = "http://ptsi.besaba.com/location_marker_mysql/save.php";					

 			URL url = null;
 			try{url = new URL(strUrl);
 				HttpURLConnection connection = (HttpURLConnection) url.openConnection();
 				connection.setRequestMethod("POST");
 				connection.setDoOutput(true);
 				OutputStreamWriter outputStreamWriter = new OutputStreamWriter(
 				connection.getOutputStream());
 				outputStreamWriter.write("lat=" + lat + "&lng="+ lng + "&nama="+ nama);				
 				outputStreamWriter.flush();
 				outputStreamWriter.close();
 				
 				InputStream iStream = connection.getInputStream();
 				BufferedReader reader = new BufferedReader(new InputStreamReader(iStream));
 				
 				StringBuffer sb = new StringBuffer();
 				String line = "";
 				
 				while((line=reader.readLine())!=null){sb.append(line);} //baca baris
 				reader.close();
 				iStream.close();				
 				}
 			catch(MalformedURLException e){e.printStackTrace();}
 			catch(IOException e){		   e.printStackTrace();}
 			return null;
 		}
 	}
 	
 // Background task to retrieve locations from remote mysql server
 	private class RetrieveTask extends AsyncTask<Void, Void, String>{
 		@Override
 		protected String doInBackground(Void... params) {
 			String strUrl = "http://ptsi.besaba.com/location_marker_mysql/retrieve.php";				
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
 			 LatLng latlng = new LatLng(Double.parseDouble(marker.get("lat")), Double.parseDouble(marker.get("lng")));
 			 googleMap.addMarker(new MarkerOptions()
               .position(latlng)
               .title("Posisi Toko")
               .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
 			}
 		}
 	}
    
}