package Investmentletters.android.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * 图片处理类
 * @author liang
 */
public class ImageUtils {
	
	/**网络处理类*/
	private Http http = null;
	
	public ImageUtils(){
		http = new Http();
	}
	
	/**
	 * 从临时文件夹获取图片
	 * @param fileName 文件名
	 * @param width 需要显示的宽度
	 * @param height 需要显示的高度
	 * @return
	 */
	public Bitmap getBitmapFromTempDir(String fileName ,int width ,int height){
		Bitmap img = null;
		if(Utils.isTempFileExist(fileName)){ //文件存在
			File file = new File(Constants.TEMP_DIR_PATH + "/" + fileName);
			
			System.out.println("zoom 图片大小 " + file.length());
			
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = false;
			if(file.length() > 2097152){
				options.inSampleSize = 4;
			}else{
				options.inSampleSize = getSampleFromLocal(fileName, width, height);
			}
			
			img = BitmapFactory.decodeFile(Constants.TEMP_DIR_PATH+"/"+fileName, options);
			
		}
		return img;
	}
	
	
	/**
	 * 从微博临时文件夹获取图片
	 * @param fileName 文件名
	 * @param width 需要显示的宽度
	 * @param height 需要显示的高度
	 * @return
	 */
	public Bitmap getBitmapFromTempDir_wb(String fileName ,int width ,int height){
		Bitmap img = null;
		if(Utils.isTempFileExist(fileName)){ //文件存在
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = false;
			options.inSampleSize = getSampleFromLocal(fileName, width, height);
			
			img = BitmapFactory.decodeFile(Constants.TEMP_DIR_PATH+"/"+fileName, options);
			
		}
		return img;
	}
	
	/**
	 * 从网络上获取图片
	 * @param url 地址
	 * @param width 要显示的宽度
	 * @param height 要显示的高度
	 * @return
	 */
	public Bitmap getBitmapFromWeb(String url,int width,int height){
		Bitmap img = null;		
		if(Utils.isHttpUrl(url)){//验证地址是否正确
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = false;
			File file = new File(url);
			if(file.length() > 2097152){
				options.inSampleSize = 4;
			}else{
				options.inSampleSize = getSample(url, width, height);
			}
			
			InputStream is = http.getInputStream(url);
			if(is != null){
				img = BitmapFactory.decodeStream(is, null, options);
				try{
					is.close();
				}catch(IOException e){
					e.printStackTrace();
				}
			}
			
		}
		return img;
	}
	
	/**
	 * 从网络获取图片缩放倍数
	 * @param url 地址
	 * @param reqWidth 要显示的宽度
	 * @param reqHeight 要显示的高度
	 * @return
	 */
	private int getSample(String url , int reqWidth,int reqHeight){
		int sample = 1;
		if(reqWidth > 0 && reqHeight > 0){
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			InputStream is = http.getInputStream(url);
			if(is != null){
				BitmapFactory.decodeStream(is, null, options);
				
				final int width = options.outWidth;//图片实际宽度
				final int height = options.outHeight;//图片实际高度
				System.out.println("imageutils image w = " + width);
				System.out.println("imageutils iamge h = " + height);
				if (height > reqHeight || width > reqWidth) {

			        final int heightRatio = Math.round((float) height / (float) reqHeight);
			        final int widthRatio = Math.round((float) width / (float) reqWidth);

			        sample = heightRatio < widthRatio ? heightRatio : widthRatio; //算出缩放倍数
			    }
				
				try{
					is.close();
				}catch(IOException e){
					e.printStackTrace();
				}
				
			}
		}
		
		return sample; 
	}
	
	/**
	 * 从本地获取图片缩放倍数
	 * @param fileName 文件名
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	private int getSampleFromLocal(String fileName , int reqWidth ,int reqHeight){
		int sample = 1;
		
		if(reqWidth > 0 && reqHeight > 0){
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			
			BitmapFactory.decodeFile(Constants.TEMP_DIR_PATH+"/"+fileName, options);
				
			final int width = options.outWidth;//图片实际宽度
			final int height = options.outHeight;//图片实际高度
				
			if (height > reqHeight || width > reqWidth) {

			    final int heightRatio = Math.round((float) height / (float) reqHeight);
			    final int widthRatio = Math.round((float) width / (float) reqWidth);

			     sample = heightRatio < widthRatio ? heightRatio : widthRatio; //算出缩放倍数
			 }
				
		}
		
		return sample;
	}
	/**
	 * 从网上下载图片本sd卡缓存
	 * @param url 图片的绝对路径
	 * @return null:下载失败, 其他：文件名
	 */
	public String downLoadwbImage(String url,String ex_filename){
		String fileName = null;
		boolean success = false;//是否下载成功
		if(Utils.isHttpUrl(url)){//验证地址是否正确
			File file = null;
			InputStream is = http.getInputStream(url);
			FileOutputStream fos = null; //文件输出流
			byte[] data = new byte[8192];
			int len = 0;
			if(is != null){	
				try {

					fileName = ex_filename + Utils.getFileNameFromUrl(url);
					
					file = new File(Constants.TEMP_DIR_PATH+"/"+fileName);
					
					fos = new FileOutputStream(file);
					
					try {
						while((len = is.read(data, 0, data.length)) != -1){
							fos.write(data, 0, len);
							fos.flush();
						}
						success = true;
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				
				if(is != null){
					try{
						is.close();
					}catch(IOException e){
						e.printStackTrace();
					}	
				}
				
				if(fos != null){
					try{
						fos.close();
					}catch(IOException e){
						e.printStackTrace();
					}	
				}
				
				if(!success){ //下载失败，删除文件
					file.delete();
					fileName = null;
				}
				
			}
		}
		return fileName;
	}
	/**
	 * 从网上下载图片本sd卡缓存
	 * @param url 图片的绝对路径
	 * @return null:下载失败, 其他：文件名
	 */
	public String downLoadImage(String url){
		String fileName = null;
		boolean success = false;//是否下载成功
		if(Utils.isHttpUrl(url)){//验证地址是否正确
			File file = null;
			InputStream is = http.getInputStream(url);
			FileOutputStream fos = null; //文件输出流
			byte[] data = new byte[8192];
			int len = 0;
			if(is != null){
				
				try {
					fileName = Utils.getFileNameFromUrl(url);
					
					file = new File(Constants.TEMP_DIR_PATH+"/"+fileName);
					
					fos = new FileOutputStream(file);
					
					try {
						while((len = is.read(data, 0, data.length)) != -1){
							fos.write(data, 0, len);
							fos.flush();
						}
						success = true;
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				
				if(is != null){
					try{
						is.close();
					}catch(IOException e){
						e.printStackTrace();
					}	
				}
				
				if(fos != null){
					try{
						fos.close();
					}catch(IOException e){
						e.printStackTrace();
					}	
				}
				
				if(!success){ //下载失败，删除文件
					file.delete();
					fileName = null;
				}
				
			}
		}
		return fileName;
	}
	
	
}
