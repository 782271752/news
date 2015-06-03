package Investmentletters.android.activity;

import Investmentletters.android.R;
import Investmentletters.android.utils.CNImageLoadThead;
import Investmentletters.android.utils.ImageLoadHandler;
import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.FloatMath;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

/** * ͼƬ��������š��϶����Զ����� */
public class ZoomActivity extends Activity implements OnTouchListener {
	Matrix matrix = new Matrix();
	Matrix savedMatrix = new Matrix();
	DisplayMetrics dm;
	ImageView imgView;
	Bitmap bitmap;
	float minScaleR;// ��С���ű���
	static final float MAX_SCALE = 4f;// ������ű���
	static final int NONE = 0;// ��ʼ״̬
	static final int DRAG = 1;// �϶�
	static final int ZOOM = 2;// ����
	int mode = NONE;
	PointF prev = new PointF();
	PointF mid = new PointF();
	float dist = 1f;
	ProgressDialog Dialog = null;

	private ImageLoadHandler handler = null;
	private boolean isquit = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.zoomimage);

		imgView = (ImageView) findViewById(R.id.imageView);// ��ȡ�ؼ�
		Dialog = new ProgressDialog(ZoomActivity.this);
		if (Dialog != null) {
			Dialog.setMessage("���Ժ�");
		}

		handler = new ImageLoadHandler();
		Dialog.show();
		Dialog.setCanceledOnTouchOutside(false);

		String img_url = getIntent().getExtras().getString("url");
		System.out.println("url = " + img_url);
		new CNImageLoadThead(this, ZoomActivity.this, img_url, imgView, 0,
				handler, ImageLoadHandler.HANDLER_LOAD_IMG_WH, "large_", Dialog)
				.start();
		imgView.setImageBitmap(bitmap);// ���ؼ�
		imgView.setOnTouchListener(this);// ���ô�������

	}

	public void getBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
		dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);// ��ȡ�ֱ���
		minZoom();
		center();
		// setImageWH(bitmap.getWidth(),bitmap.getHeight());
		imgView.setImageMatrix(matrix);
	}

	/** * �������� */
	public boolean onTouch(View v, MotionEvent event) {
		switch (event.getAction() & MotionEvent.ACTION_MASK) { // ���㰴��
		case MotionEvent.ACTION_DOWN:
			isquit = true;
			savedMatrix.set(matrix);
			prev.set(event.getX(), event.getY());
			mode = DRAG;
			break; // ���㰴��
		case MotionEvent.ACTION_POINTER_DOWN:
			dist = spacing(event); // �����������������10�����ж�Ϊ���ģʽ
			isquit = false;
			if (spacing(event) > 10f) {
				savedMatrix.set(matrix);
				midPoint(mid, event);
				mode = ZOOM;
			}
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_POINTER_UP:
			mode = NONE;
			if (isquit == true) {
				finish();
			}
			break;
		case MotionEvent.ACTION_MOVE:
			isquit = false;
			if (mode == DRAG) {
				matrix.set(savedMatrix);
				matrix.postTranslate(event.getX() - prev.x, event.getY()
						- prev.y);
			} else if (mode == ZOOM) {
				float newDist = spacing(event);
				if (newDist > 10f) {
					matrix.set(savedMatrix);
					float tScale = newDist / dist;
					matrix.postScale(tScale, tScale, mid.x, mid.y);
				}
			}
			break;
		}
		imgView.setImageMatrix(matrix);
		CheckView();
		return true;
	}

	/** * ���������С���ű������Զ����� */
	private void CheckView() {
		float p[] = new float[9];
		matrix.getValues(p);
		if (mode == ZOOM) {
			if (p[0] < minScaleR) {
				matrix.setScale(minScaleR, minScaleR);
			}
			if (p[0] > MAX_SCALE) {
				matrix.set(savedMatrix);
			}
		}
		center();
	}

	/** * ��С���ű��������Ϊ100% */
	private void minZoom() {
		minScaleR = Math.min(
				(float) dm.widthPixels / (float) bitmap.getWidth(),
				(float) dm.heightPixels / (float) bitmap.getHeight());
		if (minScaleR < 1.0) {
			float scale = (float) dm.widthPixels / (float) bitmap.getWidth();
			matrix.postScale(scale, scale);
		}
	}

	private void center() {
		center(true, true);
	}

	/** * ����������� */
	protected void center(boolean horizontal, boolean vertical) {
		Matrix m = new Matrix();
		m.set(matrix);
		RectF rect = new RectF(0, 0, bitmap.getWidth(), bitmap.getHeight());
		m.mapRect(rect);
		float height = rect.height();
		float width = rect.width();
		float deltaX = 0, deltaY = 0;
		if (vertical) {
			// ͼƬС����Ļ��С���������ʾ��������Ļ���Ϸ������������ƣ��·�������������
			int screenHeight = dm.heightPixels;
			if (height < screenHeight) {
				deltaY = (screenHeight - height) / 2 - rect.top;
			} else if (rect.top > 0) {
				deltaY = -rect.top;
			} else if (rect.bottom < screenHeight) {
				deltaY = imgView.getHeight() - rect.bottom;
			}
		}
		if (horizontal) {
			int screenWidth = dm.widthPixels;
			if (width < screenWidth) {
				deltaX = (screenWidth - width) / 2 - rect.left;
			} else if (rect.left > 0) {
				deltaX = -rect.left;
			} else if (rect.right < screenWidth) {
				deltaX = screenWidth - rect.right;
			}
		}
		matrix.postTranslate(deltaX, deltaY);
	}

	/** * ����ľ��� */
	private float spacing(MotionEvent event) {
		float x = event.getX(0) - event.getX(1);
		float y = event.getY(0) - event.getY(1);
		return FloatMath.sqrt(x * x + y * y);
	}

	/** * ������е� */
	private void midPoint(PointF point, MotionEvent event) {
		float x = event.getX(0) + event.getX(1);
		float y = event.getY(0) + event.getY(1);
		point.set(x / 2, y / 2);
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
			System.out.println("zoom  onKeyUp");
			// TODO Auto-generated method stub
			finish();
		}
		return true;
	}

	@Override
	protected void onDestroy() {
		System.out.println("zoom  onDestroy");
		// TODO Auto-generated method stub
		System.out.println("zoom bitmap " + bitmap);
		if (bitmap != null && !bitmap.isRecycled()) {
			bitmap.recycle();
			bitmap = null;
		}
		super.onDestroy();
		
	}
}
