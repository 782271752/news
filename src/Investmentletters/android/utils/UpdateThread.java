package Investmentletters.android.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

/**¸üÏ¸Thread*/
public class UpdateThread extends Thread {

	private Context context = null;
	private String url = null;
	
	public UpdateThread(Context context,String url){
		this.context = context;
		this.url = url;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		
		Http http = new Http();
		InputStream is = http.getInputStream(url);
		String fileName = Constants.TEMP_DIR_PATH+"/news.apk";
		boolean isSuccess = false;
		if(is != null){
			
			byte[] b = new byte[8192];
			int len = 0;
			FileOutputStream fos = null;
			try {
				File file = new File(fileName);
				fos = new FileOutputStream(file);
				while((len = is.read(b, 0, 8192))!= -1){
					fos.write(b, 0, len);
					fos.flush();
				}
				
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				Toast.makeText(context, "¸üÏ¸Ê§°Ü", Toast.LENGTH_SHORT).show();
			}finally{
				try {
					fos.close();
					isSuccess = true;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Toast.makeText(context, "¸üÏ¸Ê§°Ü", Toast.LENGTH_SHORT).show();
				}
			}
			
			
			try {
				is.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if(isSuccess){
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setDataAndType(Uri.parse("file://"+fileName), "application/vnd.android.package-archive");
			context.startActivity(intent);
		}
	}
}
