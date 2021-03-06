
package com.google.zxing.client.android;

import java.util.Collection;
import java.util.HashSet;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewParent;

import com.example.qrcodedemo.R;
import com.google.zxing.ResultPoint;
import com.google.zxing.client.android.camera.CameraManager;

public final class ViewfinderView extends View {

  private static final int[] SCANNER_ALPHA = {0, 64, 128, 192, 255, 192, 128, 64};
  private static final long ANIMATION_DELAY = 100L;
  private static final int OPAQUE = 0xFF;

  private final Paint paint;
  private Bitmap resultBitmap;
  private final int maskColor;
  private final int resultColor;
  private final int frameColor;
  private final int laserColor;
  private final int resultPointColor;
  private final int white;
  private int scannerAlpha;
  private Collection<ResultPoint> possibleResultPoints;
  private Collection<ResultPoint> lastPossibleResultPoints;
  private boolean laserLinePortrait=true;
  public ViewfinderView(Context context, AttributeSet attrs) {
    super(context, attrs);

    // Initialize these once for performance rather than calling them every time in onDraw().
    paint = new Paint();
    Resources resources = getResources();
    maskColor = resources.getColor(R.color.viewfinderMask);
    resultColor = resources.getColor(R.color.resultView);
    frameColor = resources.getColor(R.color.viewfinderBorder);
    laserColor = resources.getColor(R.color.viewfinderLaser);
    //resultPointColor = resources.getColor(R.color.points);
    white = resources.getColor(R.color.white);
    resultPointColor = resources.getColor(R.color.bcColor);
    scannerAlpha = 0;
    possibleResultPoints = new HashSet<ResultPoint>(5);
  }
  
  @Override
  protected void onLayout (boolean changed, int left, int top, int right, int bottom)
  {
	  int ret = 1;
	  ret = 0;
		ViewParent p = this.getParent();
		while(p != null){
			p = p.getParent();
		}	  
  }
  
  @Override
  public void onDraw(Canvas canvas) {
    Rect frame = CameraManager.get().getFramingRect();
    if (frame == null) {
      return;
    }
    
    int width = canvas.getWidth();
    int height = canvas.getHeight();
    
    //Log.d("", "frame width,frame height,canvas w, canvas h, density :"+frame.width()+","+frame.height()+","+canvas.getWidth()+","+canvas.getHeight()+","+canvas.getDensity());
    //Log.d("", "view w, view h:"+this.getWidth()+","+this.getHeight());

    int whiteWidth = width / 50;
    // Draw the exterior (i.e. outside the framing rect) darkened
    paint.setColor(resultBitmap != null ? resultColor : maskColor);
    canvas.drawRect(0, 0, width, frame.top, paint);
    canvas.drawRect(0, frame.top, frame.left, frame.bottom + 1, paint);
    canvas.drawRect(frame.right + 1, frame.top, width, frame.bottom + 1, paint);
    canvas.drawRect(0, frame.bottom + 1, width, height, paint);
    //Log.d("", "frame rect:"+ frame);
    
    //canvas.drawRect(frame.left + whiteWidth, frame.top + whiteWidth, frame.right - whiteWidth, frame.top + whiteWidth*3, paint);
    //canvas.drawRect(frame.left + whiteWidth, frame.bottom - whiteWidth*3, frame.right - whiteWidth, frame.bottom - whiteWidth, paint);
    canvas.drawRect(frame.left, frame.top+whiteWidth*3, frame.left+whiteWidth, frame.bottom - whiteWidth*3, paint);
    canvas.drawRect(frame.right - whiteWidth, frame.top + whiteWidth, frame.right+1, frame.bottom - whiteWidth, paint);
    

    if (resultBitmap != null) {
      // Draw the opaque result bitmap over the scanning rectangle
      paint.setAlpha(OPAQUE);
      canvas.drawBitmap(resultBitmap, frame.left, frame.top, paint);
    } else {

      // Draw a two pixel solid black border inside the framing rect
      //閻㈣鍤鎴ｅ濡楀棜绔熷锟�    
    /*paint.setColor(frameColor);
      canvas.drawRect(frame.left, frame.top, frame.right + 1, frame.top + 2, paint);
      canvas.drawRect(frame.left, frame.top + 2, frame.left + 2, frame.bottom - 1, paint);
      canvas.drawRect(frame.right - 1, frame.top, frame.right + 1, frame.bottom - 1, paint);
      canvas.drawRect(frame.left, frame.bottom - 1, frame.right + 1, frame.bottom + 1, paint);*/
    	
    paint.setColor(white);
    canvas.drawRect(frame.left, frame.top, frame.left + whiteWidth*3, frame.top + whiteWidth, paint);
    canvas.drawRect(frame.right - whiteWidth*3, frame.top, frame.right , frame.top + whiteWidth, paint);
    canvas.drawRect(frame.left, frame.top + whiteWidth, frame.left + whiteWidth, frame.top + whiteWidth*3, paint);
    canvas.drawRect(frame.right - whiteWidth, frame.top + whiteWidth, frame.right , frame.top + whiteWidth*3, paint);
    canvas.drawRect(frame.left, frame.bottom - whiteWidth, frame.left + whiteWidth*3, frame.bottom, paint);
    canvas.drawRect(frame.right - whiteWidth*3, frame.bottom - whiteWidth, frame.right, frame.bottom, paint);
    canvas.drawRect(frame.left, frame.bottom - whiteWidth*3, frame.left + whiteWidth, frame.bottom - whiteWidth, paint);
    canvas.drawRect(frame.right - whiteWidth, frame.bottom - whiteWidth*3, frame.right, frame.bottom - whiteWidth, paint);
    paint.setColor(resultPointColor);//娑撱倖娼紒鑳閻ㄥ嫮鍤�    
    canvas.drawRect(frame.left + whiteWidth*3, frame.top, frame.right - whiteWidth*3, frame.top + whiteWidth, paint);
    canvas.drawRect(frame.left + whiteWidth*3, frame.bottom - whiteWidth, frame.right - whiteWidth*3, frame.bottom, paint);
    
    //canvas.drawRect(frame.left, frame.top+whiteWidth*3, frame.left+whiteWidth, frame.bottom - whiteWidth*3, paint);
    //canvas.drawRect(frame.right - whiteWidth, frame.top + whiteWidth, frame.right, frame.bottom - whiteWidth, paint);
    	
        	 
      // Draw a red "laser scanner" line through the middle to show decoding is active
      /*paint.setColor(laserColor);
      paint.setAlpha(SCANNER_ALPHA[scannerAlpha]);
      scannerAlpha = (scannerAlpha + 1) % SCANNER_ALPHA.length;
      int middle = frame.height() / 2 + frame.top;
      
      canvas.drawRect(frame.left + 2, middle - 1, frame.right - 1, middle + 2, paint);*/
      
      /*Collection<ResultPoint> currentPossible = possibleResultPoints;
      Collection<ResultPoint> currentLast = lastPossibleResultPoints;
      if (currentPossible.isEmpty()) {
        lastPossibleResultPoints = null;
      } else {
        possibleResultPoints = new HashSet<ResultPoint>(5);
        lastPossibleResultPoints = currentPossible;
        paint.setAlpha(OPAQUE);
        paint.setColor(resultPointColor);
        for (ResultPoint point : currentPossible) {
//          canvas.drawCircle(frame.left + point.getX(), frame.top + point.getY(), 6.0f, paint);
        	 canvas.drawCircle(frame.left + point.getY(), frame.top + point.getX(), 6.0f, paint);
        }
      }
      if (currentLast != null) {
        paint.setAlpha(OPAQUE / 2);
        paint.setColor(resultPointColor);
        for (ResultPoint point : currentLast) {
          canvas.drawCircle(frame.left + point.getX(), frame.top + point.getY(), 3.0f, paint);
        }
      }*/

      // Request another update at the animation interval, but only repaint the laser line,
      // not the entire viewfinder mask.
      postInvalidateDelayed(ANIMATION_DELAY, frame.left, frame.top, frame.right, frame.bottom);
    }
  }

  public void changeLaser(){
	   if(laserLinePortrait){
		   laserLinePortrait=false;
	   }else{
		   laserLinePortrait=true;
	   }
  }
  public void drawViewfinder() {
    resultBitmap = null;
    invalidate();
  }

  /**
   * Draw a bitmap with the result points highlighted instead of the live scanning display.
   * @param barcode An image of the decoded barcode.
   */
  public void drawResultBitmap(Bitmap barcode) {
    resultBitmap = barcode;
    invalidate();
  }

  public void addPossibleResultPoint(ResultPoint point) {
    possibleResultPoints.add(point);
  }

}
