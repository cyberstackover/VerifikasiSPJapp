package com.zxing.client.android.camera.open;

import java.io.IOException;

import android.content.Context;
import android.graphics.Point;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;

import com.zxing.PlanarYUVLuminanceSource;
public final class CameraManager {

  private static final String TAG = CameraManager.class.getSimpleName();

 // private static final int MIN_FRAME_WIDTH = 240;
 // private static final int MIN_FRAME_HEIGHT = 240;
 // private static final int MAX_FRAME_WIDTH = 600;
 // private static final int MAX_FRAME_HEIGHT = 400;

  private final Context context;
  private final CameraConfigurationManager configManager;
  private Camera camera;
  private AutoFocusManager autoFocusManager;
  private boolean initialized;
  private boolean previewing;
  
 
  // PreviewCallback references are also removed from original ZXING authors work, since We're using our own interface
  // FramingRects references are also removed from original ZXING authors work, since We're using all view size while detecting QR-Codes
  
  public CameraManager(Context context) {
    this.context = context;
    this.configManager = new CameraConfigurationManager(context);
  }
  
  public Camera getCamera() {
	return camera;
}

  public Point getPreviewSize(){
	  return configManager.getCameraResolution();
  }
  /**
   * Opens the camera driver and initializes the hardware parameters.
   *
   * @param holder The surface object which the camera will draw preview frames into.
   * @throws IOException Indicates the camera driver failed to open.
   */
  public synchronized void openDriver(SurfaceHolder holder,int viewWidth,int viewHeight) throws IOException {
    Camera theCamera = camera;
    if (theCamera == null) {
      theCamera = new OpenCameraManager().build().open();
      if (theCamera == null) {
        throw new IOException();
      }
      camera = theCamera;
    }
    theCamera.setPreviewDisplay(holder);

    if (!initialized) {
      initialized = true;
      configManager.initFromCameraParameters(theCamera,viewWidth,viewHeight);
    }

    Camera.Parameters parameters = theCamera.getParameters();
    String parametersFlattened = parameters == null ? null : parameters.flatten(); // Save these, temporarily
    try {
      configManager.setDesiredCameraParameters(theCamera, false);
    } catch (RuntimeException re) {
      // Driver failed
      Log.w(TAG, "Camera rejected parameters. Setting only minimal safe-mode parameters");
      Log.i(TAG, "Resetting to saved camera params: " + parametersFlattened);
      // Reset:
      if (parametersFlattened != null) {
        parameters = theCamera.getParameters();
        parameters.unflatten(parametersFlattened);
        try {
          theCamera.setParameters(parameters);
          configManager.setDesiredCameraParameters(theCamera, true);
        } catch (RuntimeException re2) {
          // Well, darn. Give up
          Log.w(TAG, "Camera rejected even safe-mode parameters! No configuration");
        }
      }
    }

  }

  public synchronized boolean isOpen() {
    return camera != null;
  }

  /**
   * Closes the camera driver if still in use.
   */
  public synchronized void closeDriver() {
    if (camera != null) {
      camera.release();
      camera = null;
      // Make sure to clear these each time we close the camera, so that any scanning rect
      // requested by intent is forgotten.
      // framingRect = null;
      // framingRectInPreview = null;
    }
  }

  /**
   * Asks the camera hardware to begin drawing preview frames to the screen.
   */
  public synchronized void startPreview() {
    Camera theCamera = camera;
    if (theCamera != null && !previewing) {
      theCamera.startPreview();
      previewing = true;
      autoFocusManager = new AutoFocusManager(context, camera);
    }
  }

  /**
   * Tells the camera to stop drawing preview frames.
   */
  public synchronized void stopPreview() {
    if (autoFocusManager != null) {
      autoFocusManager.stop();
      autoFocusManager = null;
    }
    if (camera != null && previewing) {
      camera.stopPreview();
      //previewCallback.setHandler(null, 0);
      previewing = false;
    }
  }


  // All references to Torch are removed from original ZXING authors work since we're not using them.
  
  // All references to FramingRects are removed from original ZXING authors work since we're not using them.
  
  /**
   * A factory method to build the appropriate LuminanceSource object based on the format
   * of the preview buffers, as described by Camera.Parameters.
   *
   * @param data A preview frame.
   * @param width The width of the image.
   * @param height The height of the image.
   * @return A PlanarYUVLuminanceSource instance.
   */
  public PlanarYUVLuminanceSource buildLuminanceSource(byte[] data, int width, int height) {

    return new PlanarYUVLuminanceSource(data, width, height,0,0,width,height, false); // Search QR in all image along, not only in Framing Rect as original code done
    	 
  }

}
