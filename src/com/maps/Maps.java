package com.maps;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.svspjptsi.*;
//class ini tidak digunakan
@SuppressLint("NewApi")
public class Maps extends Activity {
	private GoogleMap map;
	@Override
    protected void onCreate(Bundle savedInstanceState) {
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                             WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maps_activity);
    
    	Bundle getLoc = getIntent().getExtras();
        Double lat = getLoc.getDouble("lotude");
        Double lot = getLoc.getDouble("latude");
        final LatLng lokasi = new LatLng(lot, lat);
  	  	map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
  	  	MarkerOptions marker = new MarkerOptions()
        	.position(lokasi)
        	.title("Lokasi Anda")
        	.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
        map.addMarker(marker);
        map.getUiSettings().setCompassEnabled(true);
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(lokasi, 15));
        map.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);    	
    }
    
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        LatLng cameraLatLng = map.getCameraPosition().target;
        float cameraZoom = map.getCameraPosition().zoom;
        outState.putInt("map_type", GoogleMap.MAP_TYPE_NORMAL);
        outState.putDouble("lat", cameraLatLng.latitude);
        outState.putDouble("lng", cameraLatLng.longitude);
        outState.putFloat("zoom", cameraZoom);
    }
}
