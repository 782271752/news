package Investmentletters.android.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * ͼƬ������
 * @author liang
 */
public class ImageUtils {
	
	/**���紦����*/
	private Http http = null;
	
	public ImageUtils(){
		http = new Http();
	}
	
	/**
	 * ����ʱ�ļ��л�ȡͼƬ
	 * @param fileName �ļ���
	 * @param width ��Ҫ��ʾ�Ŀ��
	 * @param height ��Ҫ��ʾ�ĸ߶�
	 * @return
	 */
	public Bitmap getBitmapFromTempDir(String fileName ,int width ,int height){
		Bitmap img = null;
		if(Utils.isTempFileExist(fileName)){ //�ļ�����
			File file = new File(Constants.TEMP_DIR_PATH + "/" + fileName);
			
			System.out.println("zoom ͼƬ��С " + file.length());
			
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
	 * ��΢����ʱ�ļ��л�ȡͼƬ
	 * @param fileName �ļ���
	 * @param width ��Ҫ��ʾ�Ŀ��
	 * @param height ��Ҫ��ʾ�ĸ߶�
	 * @return
	 */
	public Bitmap getBitmapFromTempDir_wb(String fileName ,int width ,int height){
		Bitmap img = null;
		if(Utils.isTempFileExist(fileName)){ //�ļ�����
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = false;
			options.inSampleSize = getSampleFromLocal(fileName, width, height);
			
			img = BitmapFactory.decodeFile(Constants.TEMP_DIR_PATH+"/"+fileName, options);
			
		}
		return img;
	}
	
	/**
	 * �������ϻ�ȡͼƬ
	 * @param url ��ַ
	 * @param width Ҫ��ʾ�Ŀ��
	 * @param height Ҫ��ʾ�ĸ߶�
	 * @return
	 */
	public Bitmap getBitmapFromWeb(String url,int width,int height){
		Bitmap img = null;		
		if(Utils.isHttpUrl(url)){//��֤��ַ�Ƿ���ȷ
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
	 * �������ȡͼƬ���ű���
	 * @param url ��ַ
	 * @param reqWidth Ҫ��ʾ�Ŀ��
	 * @param reqHeight Ҫ��ʾ�ĸ߶�
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
				
				final int width = options.outWidth;//ͼƬʵ�ʿ��
				final int height = options.outHeight;//ͼƬʵ�ʸ߶�
				System.out.println("imageutils image w = " + width);
				System.out.println("imageutils iamge h = " + height);
				if (height > reqHeight || width > reqWidth) {

			        final int heightRatio = Math.round((float) height / (float) reqHeight);
			        final int widthRatio = Math.round((float) width / (float) reqWidth);

			        sample = heightRatio < widthRatio ? heightRatio : widthRatio; //������ű���
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
	 * �ӱ��ػ�ȡͼƬ���ű���
	 * @param fileName �ļ���
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
				
			final int width = options.outWidth;//ͼƬʵ�ʿ��
			final int height = options.outHeight;//ͼƬʵ�ʸ߶�
				
			if (height > reqHeight || width > reqWidth) {

			    final int heightRatio = Math.round((float) height / (float) reqHeight);
			    final int widthRatio = Math.round((float) width / (float) reqWidth);

			     sample = heightRatio < widthRatio ? heightRatio : widthRatio; //������ű���
			 }
				
		}
		
		return sample;
	}
	/**
	 * ����������ͼƬ��sd������
	 * @param url ͼƬ�ľ���·��
	 * @return null:����ʧ��, �������ļ���
	 */
	public String downLoadwbImage(String url,String ex_filename){
		String fileName = null;
		boolean success = false;//�Ƿ����سɹ�
		if(Utils.isHttpUrl(url)){//��֤��ַ�Ƿ���ȷ
			File file = null;
			InputStream is = http.getInputStream(url);
			FileOutputStream fos = null; //�ļ������
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
				
				if(!success){ //����ʧ�ܣ�ɾ���ļ�
					file.delete();
					fileName = null;
				}
				
			}
		}
		return fileName;
	}
	/**
	 * ����������ͼƬ��sd������
	 * @param url ͼƬ�ľ���·��
	 * @return null:����ʧ��, �������ļ���
	 */
	public String downLoadImage(String url){
		String fileName = null;
		boolean success = false;//�Ƿ����سɹ�
		if(Utils.isHttpUrl(url)){//��֤��ַ�Ƿ���ȷ
			File file = null;
			InputStream is = http.getInputStream(url);
			FileOutputStream fos = null; //�ļ������
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
				
				if(!success){ //����ʧ�ܣ�ɾ���ļ�
					file.delete();
					fileName = null;
				}
				
			}
		}
		return fileName;
	}
	
	
}
