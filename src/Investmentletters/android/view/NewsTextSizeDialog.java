package Investmentletters.android.view;

import Investmentletters.android.R;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;

/**
 * 新闻详细界面字体选择框
 * @author liang
 */
public class NewsTextSizeDialog implements OnClickListener{
	
	private Dialog dialog = null;
	
	private View contentView = null;
	private CheckBox cbSmall = null;
	private CheckBox cbNormal = null;
	private CheckBox cbBig = null;
	
	public static enum TextSize {TEXT_SIZE_SMALL , TEXT_SIZE_NORMAL , TEXT_SIZE_LARGE};
	
	/**字体大小*/
	private TextSize textSize = TextSize.TEXT_SIZE_NORMAL;
	
	private boolean isShow = false;
	
	/**字体大小改变监听器*/
	private OnTextSizeChangeListener listener = null;
	
	public NewsTextSizeDialog(Context context){
		
		
		contentView = View.inflate(context, R.layout.news_text_size_dialog, null);
		
		cbSmall = (CheckBox)contentView.findViewById(R.id.small);
		cbSmall.setOnClickListener(this);
		
		cbNormal = (CheckBox)contentView.findViewById(R.id.normal);
		cbNormal.setOnClickListener(this);
		
		cbBig = (CheckBox)contentView.findViewById(R.id.big);
		cbBig.setOnClickListener(this);
		
		dialog = new Dialog(context,R.style.text_size_dialog);
		dialog.setContentView(contentView);
		dialog.setCanceledOnTouchOutside(true);
		
	}
	
	/**显示对话框*/
	public void show(){
		
		if(!isShow){
			if(listener != null){
				listener.onTextSizeChanged(textSize);
			}
			cbNormal.setChecked(true);
			isShow = true;
		}
		
		dialog.show();
	}
	
	/**隐藏对话框*/
	public void dismiss(){
		dialog.dismiss();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		TextSize tempSize = null;
		
		switch(v.getId()){
			case R.id.small:
				cbSmall.setChecked(true);
				cbNormal.setChecked(false);
				cbBig.setChecked(false);
				
				tempSize = TextSize.TEXT_SIZE_SMALL;
				break;
				
			case R.id.normal:
				cbSmall.setChecked(false);
				cbNormal.setChecked(true);
				cbBig.setChecked(false);
				
				tempSize = TextSize.TEXT_SIZE_NORMAL;
				break;
				
			case R.id.big:
				cbSmall.setChecked(false);
				cbNormal.setChecked(false);
				cbBig.setChecked(true);
				
				tempSize = TextSize.TEXT_SIZE_LARGE;
				break;
		
		}
		
		if(listener != null && tempSize != textSize){
			textSize = tempSize;
			listener.onTextSizeChanged(textSize);
		}
		
		dismiss();
	}
	
	public void setListener(OnTextSizeChangeListener listener){
		this.listener = listener;
	}
	
	/**字体大小改变监听器*/
	public interface OnTextSizeChangeListener{
		
		/**
		 * 字体改变
		 * @param size
		 */
		public void onTextSizeChanged(TextSize size);
		
	}
	
}
