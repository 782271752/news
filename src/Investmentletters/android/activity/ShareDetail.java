package Investmentletters.android.activity;

import Investmentletters.android.R;
import Investmentletters.android.dao.ShareDetailDao;
import Investmentletters.android.dao.base.DataVisitors;
import Investmentletters.android.entity.Share;
import Investmentletters.android.utils.Constants;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 详细诊股
 * @author liang
 */
public class ShareDetail extends Activity implements DataVisitors.CallBack,OnClickListener{
	
	private ProgressDialog progressDialog = null;
	/**标尺容器*/
	private View vDirectionLayout = null;
	/**标尺位置:1-5*/
	private int position = 0;
	/**当前位置标尺*/
	private ImageView ivImg = null; 
	/**名称*/
	private TextView tvName = null;
	/**基本面*/
	private TextView tvDesc = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.share_detail);
		progressDialog = new ProgressDialog(this);
		progressDialog.setMessage("加载中");
		
		String number = null;//股票代码
		
		try{
			number = getIntent().getExtras().getString("NUMBER");
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			number = null;
		}
		
		if(number == null){
			Toast.makeText(this, "数据异常", Toast.LENGTH_SHORT).show();
			finish();
			return;
		}
		
		ivImg = (ImageView)findViewById(R.id.direction);
		
		vDirectionLayout = findViewById(R.id.direction_layout);
		
		tvName = (TextView)findViewById(R.id.name);
		tvDesc = (TextView)findViewById(R.id.desc);
		
		findViewById(R.id.back).setOnClickListener(this);
		
		ShareDetailDao dao = new ShareDetailDao();
		dao.setUrl(Constants.URL_GET_SHARE_DETAIL, number);
		new DataVisitors().doGet(dao, this, 0);
		
		progressDialog.show();
		new AsyncTask<Void, Void, Integer>(){

			@Override
			protected Integer doInBackground(Void... params) {
				// TODO Auto-generated method stub
				int width = 0;
				while((width = vDirectionLayout.getWidth()) < 1 || position<1){
					
					if(position>5){
						break;
					}
					
					try {
						Thread.sleep(200);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				return width;
			}
			
			protected void onPostExecute(Integer result){
				if(result<1 || position>5 || position<1){
					return;
				}
				
			    int onceWidth = result/5;//每单元格长度
			    
			    int imgWidth = ivImg.getWidth();
			    
			    //计算移动位置
			    int pos = (position - 1) * onceWidth + onceWidth/2 - imgWidth/2;
			    ivImg.setVisibility(View.VISIBLE);
				TranslateAnimation ta = new TranslateAnimation(result, pos, 0, 0);
				ta.setDuration(2000L);
				ta.setFillAfter(true);
				ivImg.setAnimation(ta);
				ivImg.startAnimation(ta);
				
			}
			
		}.execute();
	}
	
	@Override
	public void onResult(int what, Object res) {
		// TODO Auto-generated method stub
		progressDialog.dismiss();
		if(res == null){
			Toast.makeText(this, "数据异常", Toast.LENGTH_SHORT).show();
			position = 10;//关闭线程
			finish();
			return;
		}
		
		Share item = (Share)res;
		position = item.getArea();
		
		tvName.setText(item.getName());
		
		String content = item.getContent().trim();
		content = content.replaceAll("&&&", "\"");
		content = content.replaceAll("E投资机构平台", "数据");
		content = content.replaceAll("e投资机构平台", "数据");
		tvDesc.setText(content);
		
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		finish();
	}
	
}
