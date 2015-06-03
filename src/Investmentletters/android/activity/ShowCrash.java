package Investmentletters.android.activity;

import Investmentletters.android.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.WindowManager;

public class ShowCrash extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.crash);
	}
	
	@Override
	public void onAttachedToWindow() {
		// TODO Auto-generated method stub
		getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD);   
		super.onAttachedToWindow();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		
		if(keyCode == KeyEvent.KEYCODE_HOME){
			System.out.println("°´home¼ü...");
		}
		
		System.exit(0);
		
		return true;
	}
	
}
