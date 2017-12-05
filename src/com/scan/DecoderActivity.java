package com.scan;
import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;

import android.text.format.Time;
import android.graphics.PointF;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

import com.maps.Mapsv4;

import com.svspjptsi.*;
import com.scan.QRCodeReaderView;
import com.scan.QRCodeReaderView.OnQRCodeReadListener;

public class DecoderActivity extends Activity implements OnQRCodeReadListener {
	private QRCodeReaderView mydecoderview;
	private LocationManager locationManager;
	
    public int jam,menit,detik,bln;
    public String hr="",mn="",sc="";
    public String lokasi="",mon="",date="";
    public String digit="",tmp="";
    public String ada="7484623490";
    public String tidakada="8493763309";
    //Variabel penanda agar sekali scan
    public int flag=0;
    //Variabel penanda ada tidaknya digit yg discan pada database
    
    public Double lat,lng;
    Time tnow = new Time();
    Calendar cnow = Calendar.getInstance();
    
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decoder);
        mydecoderview = (QRCodeReaderView) findViewById(R.id.qrdecoderview);
        mydecoderview.setOnQRCodeReadListener(this);
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new MyLocationListener());
    }

	public void cekwaktu(){
		tnow.setToNow();
        jam = tnow.hour; menit = tnow.minute; detik = tnow.second;
        if(jam<10){hr="0"+String.valueOf(jam);}else hr=String.valueOf(jam);
        if(menit<10){mn="0"+String.valueOf(menit);}else mn=String.valueOf(menit);
        if(detik<10){sc="0"+String.valueOf(detik);}else sc=String.valueOf(detik);
        bln = cnow.get(Calendar.MONTH)+1;
        switch(bln){	case 1 : mon ="Januari";break;
        				case 2 : mon ="Februari";break;
        				case 3 : mon ="Maret";break;
        				case 4 : mon ="April";break;
        				case 5 : mon ="Mei";break;
        				case 6 : mon ="Juni";break;
        				case 7 : mon ="Juli";break;
        				case 8 : mon ="Agustus";break;
        				case 9 : mon ="September";break;
        				case 10 : mon ="Oktober";break;
        				case 11 : mon ="Nopember";break;
        				case 12 : mon ="Desember";break;
        				default : break;
        }
		date = cnow.get(Calendar.DAY_OF_MONTH)+" "+mon+" "+cnow.get(Calendar.YEAR);
	}
	public void lihatpeta(Double lat,Double lng){
		Intent iMap = new Intent(this,Mapsv4.class);
		Bundle getLoc = new Bundle();
		getLoc.putDouble("showlat", lat);
		getLoc.putDouble("showlong", lng);
		//getLoc.putDouble("latude", lat);
		//getLoc.putDouble("lotude", lng);
		iMap.putExtras(getLoc); 
    	startActivity(iMap);
    	finish();
	}
	@Override
    public void onQRCodeRead(final String text, PointF[] points) {
    locationManager.requestLocationUpdates(
    LocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {
    	public void onLocationChanged(Location locate) {}
        public void onStatusChanged(String s, int i, Bundle b){Toast.makeText(DecoderActivity.this, "Status provider berubah",Toast.LENGTH_LONG).show();}
        public void onProviderDisabled(String s) {Toast.makeText(DecoderActivity.this, "Provider dinonaktifkan oleh user, GPS off",Toast.LENGTH_LONG).show();}
        public void onProviderEnabled(String s) {Toast.makeText(DecoderActivity.this, "Provider diaktifkan oleh user, GPS on",Toast.LENGTH_LONG).show();}
     });
    
    Location locate = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
    	if (locate != null) {
    		lat = locate.getLatitude();
	    	lng = locate.getLongitude();
            lokasi = String.format("Latitude     : %2$s \n Longitude  : %1$s",lng, lat);
        }
        else if(locate == null){lokasi = "Tidak dapat menentukan lokasi, periksa GPS";}
    	
    	final AlertDialog.Builder hasilscan=new AlertDialog.Builder(DecoderActivity.this);
        Toast.makeText(DecoderActivity.this, "QR Code ditemukan",Toast.LENGTH_LONG).show();
        cekwaktu();  
        hasilscan.setTitle("Hasil Scan");
        hasilscan.setMessage("Digit SPJ  : "+text+
        					 "\nLokasi   : \n "+lokasi+
        					 "\nTanggal  : "+date+
        					 "\nWaktu    : "+hr+":"+mn+":"+sc);
        hasilscan.setIcon(android.R.drawable.ic_dialog_info);
        onPause();	flag++; digit=text; 
        hasilscan.setNeutralButton("OK",new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if(flag==1&&digit.contains(text)==true){//baru login & scan, trdpt qr baru
		        	tmp=text; 
		        	final AlertDialog.Builder ceknew=new AlertDialog.Builder(DecoderActivity.this);
		        	ceknew.setTitle("Verifikasi Digit SPJ");
		        	ceknew.setMessage("Digit SPJ baru telah diterima");
		        	ceknew.setPositiveButton("Cek", new OnClickListener() {
//     				@Override
		            public void onClick(DialogInterface dialog, int which) {
		            	// fungsi untuk verifikasi data ke database
		            	// terdapat pesan apakah data yg di scan cocok dengan data yg ada di database
		            	// comparespj(text);
		            	if(tmp.contentEquals(ada)){
		            	final AlertDialog.Builder hasilada=new AlertDialog.Builder(DecoderActivity.this);
		                hasilada.setTitle("Data SPJ");
		            	hasilada.setMessage("Data tersebut tersedia dalam database");
		            	hasilada.setIcon(android.R.drawable.ic_dialog_info);
		            	hasilada.setNeutralButton("OK, tampilkan pada Map", new OnClickListener() {
//						@Override
							public void onClick(DialogInterface dialog, int which) {
								// Fungsi tampil map
								lihatpeta(lat,lng);
								digit="";
							}
		            	});
		            	hasilada.show();
						}
		            	else if(tmp.contentEquals(tidakada)){
		            		final AlertDialog.Builder hasiltidakada=new AlertDialog.Builder(DecoderActivity.this);
		            		hasiltidakada.setMessage("Data tersebut tidak dikenali");
		            		hasiltidakada.setIcon(android.R.drawable.ic_dialog_info);
		            		hasiltidakada.setNeutralButton("Scan lagi", new OnClickListener() {
		     					@Override
		            			public void onClick(DialogInterface dialog, int which) {
		            				// Fungsi tampil map
		            				jam=menit=detik=bln=0;
				                	mon="";date="";digit="";
				                	onResume();
		            			}
		            		  	});
		            		hasiltidakada.show();
		            	}
		            }
		        	});
		        	// Tombol untuk coba scan lagi
		        	ceknew.setNegativeButton("Scan Lagi", new OnClickListener() {
		      //          @Override
		                public void onClick(DialogInterface dialog, int which) {
		                	jam=menit=detik=bln=0;mon="";date="";	onResume();
		                }
		            });
		        	ceknew.show();
		        }
		        
		        else if(flag>1){
		        	if(tmp.contains(digit)==true){//sudah login & scan, tp trdpt QR lama
		        		final AlertDialog.Builder cekold=new AlertDialog.Builder(DecoderActivity.this);
			        	cekold.setTitle("Verifikasi Digit SPJ");
			        	cekold.setMessage("Digit SPJ tersebut sudah di scan");
			        	cekold.setNeutralButton("OK",new OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {onResume();}
						});
			        	cekold.show();
		        	}
		        	else if(tmp.contains(digit)==false){ //sudah login & scan, tp trdpt QR baru
		        		flag=1; onResume();
		        	}
		        }
			}
		});
        hasilscan.show();
    }
    @Override
    public void onBackPressed() {
    	onResume();
    	new AlertDialog.Builder(this)
    		.setIcon(android.R.drawable.ic_dialog_alert)
    		.setTitle("Tutup SPJ QR Scanner")
    		.setMessage("Anda yakin?")
    		.setCancelable(false)
    		.setPositiveButton("Ya",new DialogInterface.OnClickListener() {
    			public void onClick(DialogInterface dialog, int id) {
    				Toast.makeText(DecoderActivity.this, "Terimakasih :)",Toast.LENGTH_LONG).show();
    				DecoderActivity.this.finish();
    				Intent dash = new Intent(getApplicationContext(), DashboardActivity.class);
    	        	startActivity(dash);finish();
    			}
    		})
    		.setNegativeButton("Tidak", null).show();
    }
    private class MyLocationListener implements LocationListener {
        public void onLocationChanged(Location locate) {
        	if (locate != null) {
        		lat = locate.getLatitude();	lng = locate.getLongitude();
        		lokasi = String.format("Latitude     : %2$s \n Longitude  : %1$s",lng, lat);}
	        else if(locate == null){lokasi = "Tidak dapat menentukan lokasi, periksa GPS";}
        }
        public void onStatusChanged(String s, int i, Bundle b) {Toast.makeText(DecoderActivity.this, "Status provider berubah",Toast.LENGTH_LONG).show();}
        public void onProviderDisabled(String s) {Toast.makeText(DecoderActivity.this, "Provider dinonaktifkan oleh user, GPS off",Toast.LENGTH_LONG).show();}
        public void onProviderEnabled(String s) {Toast.makeText(DecoderActivity.this, "Provider diaktifkan oleh user, GPS on",Toast.LENGTH_LONG).show();}
    }
    @Override
    public void cameraNotFound() {}
    @Override
    public void QRCodeNotFoundOnCamImage() {}
    @Override
    protected void onResume() {
        super.onResume();
        mydecoderview.getCameraManager().startPreview();
    }
    @Override
    protected void onPause() {
        super.onPause();
        mydecoderview.getCameraManager().stopPreview();
    }
}
