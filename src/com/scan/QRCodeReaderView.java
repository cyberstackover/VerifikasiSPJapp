package com.scan;

import java.io.IOException;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.PointF;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.zxing.BinaryBitmap;
import com.zxing.ChecksumException;
import com.zxing.FormatException;
import com.zxing.NotFoundException;
import com.zxing.PlanarYUVLuminanceSource;
import com.zxing.Result;
import com.zxing.ResultPoint;
import com.zxing.client.android.camera.open.CameraManager;
import com.zxing.common.HybridBinarizer;
import com.zxing.qrcode.QRCodeReader;

public class QRCodeReaderView extends SurfaceView implements SurfaceHolder.Callback,Camera.PreviewCallback {

	public interface OnQRCodeReadListener {		
		public void onQRCodeRead(String text, PointF[] points);
		public void cameraNotFound();
		public void QRCodeNotFoundOnCamImage();
	}
	
	private OnQRCodeReadListener mOnQRCodeReadListener;
	
	private static final String TAG = QRCodeReaderView.class.getName();
	
	private QRCodeReader mQRCodeReader;
    private int mPreviewWidth; 
    private int mPreviewHeight; 
    private SurfaceHolder mHolder;
    private CameraManager mCameraManager;
    
	public QRCodeReaderView(Context context) {
		super(context);
		init();
	}
	
	public QRCodeReaderView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	public void setOnQRCodeReadListener(OnQRCodeReadListener onQRCodeReadListener) {
		mOnQRCodeReadListener = onQRCodeReadListener;
	}
	
	public CameraManager getCameraManager() {
		return mCameraManager;
	}

	@SuppressWarnings("deprecation")
	private void init() {
		if (checkCameraHardware(getContext())){
			mCameraManager = new CameraManager(getContext());
			mHolder = this.getHolder();
			mHolder.addCallback(this);
			mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);  // Need to set this flag despite it's deprecated
		} else {
			Log.e(TAG, "Error: Camera not found");
			mOnQRCodeReadListener.cameraNotFound();
		}
	}
		
//	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		try {// Indicate camera, our View dimensions
				mCameraManager.openDriver(holder,this.getWidth(),this.getHeight());
		} catch (IOException e) {
				Log.w(TAG, "Can not openDriver: "+e.getMessage());
				mCameraManager.closeDriver();
		}
		try {	mQRCodeReader = new QRCodeReader();
				mCameraManager.startPreview();
		} catch (Exception e) {
			Log.e(TAG, "Exception: " + e.getMessage());
			mCameraManager.closeDriver();
		}
	}
	
//	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		Log.d(TAG, "surfaceDestroyed");
		mCameraManager.getCamera().setPreviewCallback(null);
		mCameraManager.getCamera().stopPreview();
		mCameraManager.getCamera().release();
		mCameraManager.closeDriver();
	}
	
	// Called when camera take a frame 
//	@Override
	public void onPreviewFrame(byte[] data, Camera camera) {
		PlanarYUVLuminanceSource source = mCameraManager.buildLuminanceSource(data, mPreviewWidth, mPreviewHeight);
		HybridBinarizer hybBin = new HybridBinarizer(source);
		BinaryBitmap bitmap = new BinaryBitmap(hybBin);
		try {Result result = mQRCodeReader.decode(bitmap);  	
			// Notify We're found a QRCode
			if (mOnQRCodeReadListener != null) {
					// Transform resultPoints to View coordinates
					PointF[] transformedPoints = transformToViewCoordinates(result.getResultPoints());
					mOnQRCodeReadListener.onQRCodeRead(result.getText(), transformedPoints);
			}
		} catch (ChecksumException e) {
			Log.d(TAG, "ChecksumException");
			e.printStackTrace();
		} catch (NotFoundException e) {
			// Notify QR not found
			if (mOnQRCodeReadListener != null) {
				mOnQRCodeReadListener.QRCodeNotFoundOnCamImage();
			}
		} catch (FormatException e) {
			Log.d(TAG, "FormatException");
			e.printStackTrace();
		} finally {
			mQRCodeReader.reset();
		}
	}
//	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		Log.d(TAG, "surfaceChanged");
		if (mHolder.getSurface() == null){
			Log.e(TAG, "Error: preview surface does not exist");
			return;
		}
		mPreviewWidth = mCameraManager.getPreviewSize().x;
		mPreviewHeight = mCameraManager.getPreviewSize().y;
		mCameraManager.stopPreview();
		mCameraManager.getCamera().setPreviewCallback(this);
		mCameraManager.getCamera().setDisplayOrientation(90); // Portrait mode
		mCameraManager.startPreview();
	}
	private PointF[] transformToViewCoordinates(ResultPoint[] resultPoints) {	
		PointF[] transformedPoints = new PointF[resultPoints.length];
		int index = 0;
		if (resultPoints != null){
			float previewX = mCameraManager.getPreviewSize().x;
			float previewY = mCameraManager.getPreviewSize().y;
			float scaleX = this.getWidth()/previewY;
			float scaleY = this.getHeight()/previewX;
			for (ResultPoint point :resultPoints){
				PointF tmppoint = new PointF((previewY- point.getY())*scaleX, point.getX()*scaleY);
				transformedPoints[index] = tmppoint;
				index++;
			}
		}
		return transformedPoints;		
	}
	
	private boolean checkCameraHardware(Context context) {
		if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
			return true;
		} 
		else if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)){
			return true;
		}
		else if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)){
			return true;
		}
		else{return false;}
	}
	
}
