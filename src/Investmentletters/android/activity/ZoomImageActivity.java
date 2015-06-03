package Investmentletters.android.activity;

import Investmentletters.android.R;
import Investmentletters.android.utils.ImageLoadHandler;
import Investmentletters.android.utils.CNImageLoadThead;
import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.FloatMath;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.ImageView;

public class ZoomImageActivity extends Activity implements OnTouchListener,
		View.OnClickListener {
	private static final String TAG = "Touch";
	// These matrices will be used to move and zoom image
	Matrix matrix = new Matrix();
	Matrix savedMatrix = new Matrix();
	// We can be in one of these 3 states
	static final int NONE = 0;
	static final int DRAG = 1;
	static final int ZOOM = 2;
	int mode = NONE;
	ProgressDialog Dialog = null;
	// Remember some things for zooming
	PointF start = new PointF();
	PointF mid = new PointF();
	float oldDist = 1f;
	private ImageLoadHandler handler = null;
	private static ImageView view;
	public static double screenWidth;
	public static double screenHeigh;
	private boolean isquit = false;
	private int image_w;// 图片原宽高
	private int image_h;
	private int zoom_image_w;// 当前图片宽高
	private int zoom_image_h;
	private int image_x;//图片左上角坐标
	private int image_y;
	private int ZoomImageW;//图片缩放过程中的宽高
	private int ZoomImageH;
	private float scale = 0;//图片缩放比例

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.zoomimage);

		handler = new ImageLoadHandler();
		String img_url = getIntent().getExtras().getString("url");

		view = (ImageView) findViewById(R.id.imageView);
		view.setOnTouchListener(this);
		DisplayMetrics dm = new DisplayMetrics();
		// 获取屏幕信息
		getWindowManager().getDefaultDisplay().getMetrics(dm);

		screenWidth = dm.widthPixels;
		screenHeigh = dm.heightPixels;

		Dialog = new ProgressDialog(ZoomImageActivity.this);
		if (Dialog != null) {
			Dialog.setMessage("请稍候");
		}

		Dialog.show();
		Dialog.setCanceledOnTouchOutside(false);
//		new CNImageLoadThead(this, ZoomActivity.this, img_url, view, 0,
//				handler, ImageLoadHandler.HANDLER_LOAD_IMG_WH, "large_", Dialog)
//				.start();

	}

	public void setImageWH(int w, int h) {
		image_w = w;
		image_h = h;
		float zoom = (float) (screenWidth / w);
		if (zoom > 1) {
			image_x = (int) ((screenWidth - w) / 2);
			if ((screenHeigh - h) > 0) {
				matrix.postTranslate((float) ((screenWidth - w) / 2),
						(float) ((screenHeigh - h) / 2));
				image_y = (int) ((screenHeigh - h) / 2);
			} else {
				matrix.postTranslate((float) ((screenWidth - w) / 2), 0);
				image_y = 0;
			}
		} else {
			image_x = 0;
			if (h * zoom >= screenHeigh) {
				matrix.postScale(zoom, zoom);
				image_y = 0;
			} else {
				matrix.postScale(zoom, zoom);
				matrix.postTranslate(0, (float) ((screenHeigh - h * zoom) / 2));
				image_y = (int) ((screenHeigh - h * zoom) / 2);
			}
		}
		view.setImageMatrix(matrix);
		if (zoom <= 1) {
			zoom_image_w = (int) (w * zoom);
		}else{
			zoom_image_w = image_w;
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		ImageView view = (ImageView) v;
	
		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:
			isquit = true;
			savedMatrix.set(matrix);
			// 設置初始點位置
			start.set(event.getX(), event.getY());
			Log.d(TAG, "mode=DRAG");
			mode = DRAG;
			break;
		case MotionEvent.ACTION_POINTER_DOWN:
			isquit = false;
			oldDist = spacing(event);
			Log.d(TAG, "oldDist=" + oldDist);
			if (oldDist > 10f) {
				savedMatrix.set(matrix);
				midPoint(mid, event);
				mode = ZOOM;
				Log.d(TAG, "mode=ZOOM");
			}
			break;
		case MotionEvent.ACTION_UP:
			mode = NONE;
			Log.d(TAG, "mode=NONE");
			System.out.println("zoom ACTION_UP");
			if (isquit == true) {
				finish();
			}
			break;
		case MotionEvent.ACTION_POINTER_UP:
			mode = NONE;
			Log.d(TAG, "mode=NONE");
			image_x = (int) (image_x + (event.getX() - start.x));
			zoom_image_w = (int) (zoom_image_w*scale);
			System.out.println("zoom up scale = " + scale);
			System.out.println("zoom up zoom_image_w = " + zoom_image_w);
			if (isquit == true) {
				finish();
			}
			break;
//			private int image_w;// 图片原宽高
//			private int image_h;
//			private int zoom_image_w;// 当前图片宽高
//			private int zoom_image_h;
//			private int image_x;//图片左上角坐标
//			private int image_y;
//			private int ZoomImageW;//图片缩放过程中的宽高
//			private int ZoomImageH;
		case MotionEvent.ACTION_MOVE:
			isquit = false;
			if (mode == DRAG) {
				// ...
				matrix.set(savedMatrix);
				matrix.postTranslate(event.getX() - start.x, event.getY() - start.y);
				
				 // 下面的代码是为了查看matrix中的元素  
			    float[] matrixValues = new float[9];  
			    matrix.getValues(matrixValues);  
			    for(int i = 0; i < 3; ++i)  
			    {  
			        String temp = new String();  
			        for(int j = 0; j < 3; ++j)  
			        {  
			            temp += matrixValues[3 * i + j ] + "\t";  
			        }  
				     System.out.println("zoom temp i="+i+ temp);
			    } 
			} else if (mode == ZOOM) {
				float newDist = spacing(event);
				Log.d(TAG, "newDist=" + newDist);
				if (newDist > 10f) {
					matrix.set(savedMatrix);
					scale = newDist / oldDist;
					ZoomImageW = (int) (zoom_image_w*scale);

					if(ZoomImageW > image_w*2){
						return true;
					}
					matrix.postScale(scale, scale, mid.x, mid.y);
				}
			}
			view.setImageMatrix(matrix);
			break;
		}

		return true; // indicate event was handled
	}
 
	/** Determine the space between the first two fingers */
	private float spacing(MotionEvent event) {
		float x = event.getX(0) - event.getX(1);
		float y = event.getY(0) - event.getY(1);
		return FloatMath.sqrt(x * x + y * y);
	}

	/** Calculate the mid point of the first two fingers */
	private void midPoint(PointF point, MotionEvent event) {
		float x = event.getX(0) + event.getX(1);
		float y = event.getY(0) + event.getY(1);
		point.set(x / 2, y / 2);
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
			finish();
		}
		return true;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}
}
