package Investmentletters.android.fragment;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import Investmentletters.android.R;
import Investmentletters.android.activity.InterfaceActivity;
import Investmentletters.android.activity.MyApplication;
import Investmentletters.android.utils.Constants;
import Investmentletters.android.utils.CustomerImageLoadHandler;
import Investmentletters.android.utils.Http;
import Investmentletters.android.utils.ImageLoadThread;
import Investmentletters.android.utils.SharePreferenceUtils;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * �޸��û�
 * 
 * @author Administrator
 */
public class Fragment_Account extends Fragment implements View.OnClickListener {

	/** �û��� */
	private TextView tvUserName;
	/** �����:ԭ���� */
	private EditText etOldPasswd;
	/** �����:������ */
	private EditText etNewPasswd;
	/** �����:ȷ������ */
	private EditText etConfirm;
	/** �û�ͷ�� */
	private ImageView headimg;

	/** �޸��û�ͷ��ѡ��� */
	private AlertDialog selectDialog = null;

	/** ͼƬ���ź󷵻��� */
	private final int ZONE_REQUEST_CODE = 3;
	/** ����ȡͼƬ������ */
	private final int PICTURE_REQUEST_CODE = 1;
	/** �������ȡͼƬ������ */
	private final int CAMERAL_REQUEST_CODE = 2;

	/** �û�ͷ������·�� */
	private final String HEAD_IMG_FILE_PATH = Constants.TEMP_DIR_PATH
			+ "/user_header_img.jpg";

	private ProgressDialog progressDialog = null;

	/** �û��� */
	private String userName = null;
	/** ���� */
	private String passwd = null;

	/** ͷ�����handler */
	private CustomerImageLoadHandler cusImgHandler = null;
	/** ���ݹ����� */
	private MyApplication app = null;
	Button logout;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Context context = getActivity();

		app = (MyApplication) context.getApplicationContext();
		userName = app.getUserName();
		passwd = app.getPasswd();

		AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
		dialogBuilder.setTitle("����ͷ��...");
		dialogBuilder.setNegativeButton("���",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						Intent intent = new Intent(Intent.ACTION_PICK, null);
						intent.setDataAndType(
								MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
								"image/*");
						startActivityForResult(intent, PICTURE_REQUEST_CODE);
					}
				});

		dialogBuilder.setPositiveButton("����",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						dialog.dismiss();
						// ////////////////////////////////////////
						Intent intent = new Intent(
								MediaStore.ACTION_IMAGE_CAPTURE);

						intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri
								.fromFile(new File(Environment
										.getExternalStorageDirectory()
										+ "/com.hykj.investmentletters",
										"user_header_img.jpg")));
						startActivityForResult(intent,
								Activity.DEFAULT_KEYS_DIALER);

						// //////////////////////////////////////////
						// intent.putExtra(MediaStore.EXTRA_OUTPUT,Uri
						// .fromFile(new File(HEAD_IMG_FILE_PATH)));
					}
				});

		selectDialog = dialogBuilder.create();

		progressDialog = new ProgressDialog(context);
		progressDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
			@Override
			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event) {
				// TODO Auto-generated method stub
				return true;
			}
		});

		cusImgHandler = new CustomerImageLoadHandler(R.drawable.user);
	}

	// /**
	// * ѡ����ʾ�Ի���
	// */
	// private void ShowPickDialog() {
	// new
	// AlertDialog.Builder(getActivity()).setTitle("����ͷ��...").setNegativeButton("���",
	// new DialogInterface.OnClickListener() {
	// public void onClick(DialogInterface dialog, int which) {
	// dialog.dismiss();
	// Intent intent = new Intent(Intent.ACTION_PICK, null);
	// intent.setDataAndType(
	// MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
	// "image/*");
	// startActivityForResult(intent, 1);
	//
	// }
	// })
	// .setPositiveButton("����", new DialogInterface.OnClickListener() {
	// public void onClick(DialogInterface dialog, int whichButton) {
	// dialog.dismiss();
	// Intent intent = new Intent(
	// MediaStore.ACTION_IMAGE_CAPTURE);
	// //�������ָ������������պ����Ƭ�洢��·��
	// intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri
	// .fromFile(new File(Environment
	// .getExternalStorageDirectory(),
	// "xiaoma.jpg")));
	// startActivityForResult(intent, 2);
	// }
	// }).show();
	// }
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View mView = inflater.inflate(R.layout.page_account, container, false);
		mView.findViewById(R.id.showRight).setOnClickListener(this);// ��ʾ�ұ�
		tvUserName = (TextView) mView.findViewById(R.id.user_name);
		etOldPasswd = (EditText) mView.findViewById(R.id.old_passwd);
		etNewPasswd = (EditText) mView.findViewById(R.id.new_passwd);
		etConfirm = (EditText) mView.findViewById(R.id.confirm_passwd);
		mView.findViewById(R.id.account_button).setOnClickListener(this);// ȷ���޸İ�ť
		mView.findViewById(R.id.logout_button).setOnClickListener(this);
		headimg = (ImageView) mView.findViewById(R.id.head);
		headimg.setOnClickListener(this);

		loadUserHeaderImg();

		tvUserName.setText("�û���:" + userName);

		return mView;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		switch (requestCode) {
		case PICTURE_REQUEST_CODE:// �����ֱ�Ӵ�����ȡ
			if (data != null && data.getData() != null) {
				startPhotoZoom(data.getData());
			}
			break;

		case CAMERAL_REQUEST_CODE:// ����ǵ����������ʱ
			File temp = new File(HEAD_IMG_FILE_PATH);
			startPhotoZoom(Uri.fromFile(temp));
			break;

		case ZONE_REQUEST_CODE:// ȡ�òü����ͼƬ
			if (data != null) {
				uploadViewHeadImage(data);
			}
			break;

		default:
			break;

		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * �ü�ͼƬ����ʵ��
	 * 
	 * @param uri
	 */
	public void startPhotoZoom(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// �������crop=true�������ڿ�����Intent��������ʾ��VIEW�ɲü�
		intent.putExtra("crop", "true");
		// aspectX aspectY �ǿ�ߵı���
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY �ǲü�ͼƬ���
		intent.putExtra("outputX", 150);
		intent.putExtra("outputY", 150);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, ZONE_REQUEST_CODE);
	}

	/**
	 * �ϴ����ú��ͼƬ
	 * 
	 * @param picdata
	 */
	private void uploadViewHeadImage(Intent picdata) {
		Bundle extras = picdata.getExtras();
		if (extras != null) {
			Bitmap photo = extras.getParcelable("data");

			File file = new File(HEAD_IMG_FILE_PATH);
			// ����ͼƬ������
			FileOutputStream fos = null;
			try {
				fos = new FileOutputStream(file);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}

			if (fos != null) {
				photo.compress(CompressFormat.JPEG, 80, fos);
				try {
					fos.flush();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					fos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			// ��ʼ�ϴ�ͷ��
			new AsyncTask<File, Boolean, Boolean>() {

				@Override
				protected void onPreExecute() {
					// TODO Auto-generated method stub
					super.onPreExecute();
					progressDialog.setMessage("�����ϴ�ͷ��");
				}

				@Override
				protected Boolean doInBackground(File... params) {
					// TODO Auto-generated method stub
					boolean result = false;
					final String endLine = "\r\n";// ���з�
					final String begin = "--";// �ֶο�ʼ
					final String boundary = "----invandroidclient123987645";// �ֶηָ���

					HttpURLConnection conn = null;
					try {
						URL url = new URL(Constants.URL_MODIFY_USER);
						conn = (HttpURLConnection) url.openConnection();
						conn.setRequestMethod("POST");
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}

					if (conn != null) {
						conn.setDoInput(true);
						conn.setDoOutput(true);
						conn.setUseCaches(false);
						conn.setRequestProperty("Connection", "Keep-Alive");
						conn.setRequestProperty("Charset", "UTF-8");
						conn.setRequestProperty("Content-Type",
								"multipart/form-data; boundary=" + boundary);

						DataOutputStream dos = null;
						FileInputStream fis = null;
						InputStream is = null;
						try {
							dos = new DataOutputStream(conn.getOutputStream());
							dos.writeBytes(begin + boundary + endLine);

							int len = 0;
							byte[] buffer = new byte[8192];
							dos.writeBytes("Content-Disposition:form-data;name=\"usersImg\";filename=\""
									+ params[0].getName()
									+ "\""
									+ endLine
									+ endLine);

							fis = new FileInputStream(params[0]);
							while ((len = fis.read(buffer, 0, 8192)) != -1) {
								dos.write(buffer, 0, len);
								dos.flush();
							}
							dos.writeBytes(endLine);

							// �û���
							dos.writeBytes(begin + boundary + endLine);
							dos.writeBytes("Content-Disposition: form-data; name=\"User\""
									+ endLine + endLine);
							dos.writeBytes(userName);
							dos.writeBytes(endLine);
							dos.writeBytes(begin + boundary + begin + endLine);
							dos.flush();

							is = conn.getInputStream();
							StringBuffer sb = new StringBuffer();
							while ((len = is.read(buffer, 0, 8192)) != -1) {
								sb.append(new String(buffer, 0, len));
							}

							result = sb.toString().trim().equals("1");

						} catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
						}

						if (fis != null) {
							try {
								fis.close();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}

						if (is != null) {
							try {
								is.close();
							} catch (Exception e) {
								// TODO: handle exception
								e.printStackTrace();
							}
						}

						if (dos != null) {
							try {
								dos.close();
							} catch (Exception e) {
								// TODO: handle exception
								e.printStackTrace();
							}
						}

						conn.disconnect();
					}
					return result;
				}

				@Override
				protected void onPostExecute(Boolean result) {
					// TODO Auto-generated method stub
					super.onPostExecute(result);
					progressDialog.dismiss();
					Context context = getActivity();
					if(result){ //�޸ĳɹ�
						loadUserHeaderImg();//Ԥ��ͼƬ
						Toast.makeText(context, "�޸�ͷ��ɹ�", Toast.LENGTH_SHORT).show();
						app.setModifyHeadImg(true);
					}else{//�޸�ʧ��
						Toast.makeText(context, "�޸�ͷ��ʧ��", Toast.LENGTH_SHORT).show();
					}
				}

			}.execute(file);

		}
	}

	/** ��������û�ͷ����Ϣ */
	private void loadUserHeaderImg() {
		new AsyncTask<String, String, String>() {
			@Override
			protected String doInBackground(String... params) {
				// TODO Auto-generated method stub
				Http http = new Http();
				List<NameValuePair> httpParams = new ArrayList<NameValuePair>();
				httpParams.add(new BasicNameValuePair("User", params[0]));
				String res = http.doPost(Constants.URL_MODIFY_USER, httpParams);
				if (res != null) {
					res = res.trim();
					// ��ʾͷ��
					new ImageLoadThread(getActivity(), res, headimg, 0,
							cusImgHandler,
							CustomerImageLoadHandler.HANDLER_LOAD_IMG).start();
				}

				return null;
			}

		}.execute(userName);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		InputMethodManager m = (InputMethodManager) getActivity()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		m.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
		switch (v.getId()) {
		case R.id.logout_button:
			FragmentTransaction ft = getFragmentManager().beginTransaction();
			Fragment_Kuaibao kuaibao = new Fragment_Kuaibao();
			ft.replace(R.id.id_framelayout, kuaibao);
			ft.commit();
			break;
		case R.id.head: // �޸��û�ͷ��
			// ShowPickDialog();
			selectDialog.show();
			break;

		case R.id.account_button: // �޸����밴ť
			String oldPasswd = etOldPasswd.getText().toString().trim();// ������
			String newPasswd = etNewPasswd.getText().toString().trim();// ������
			String confirmPasswd = etConfirm.getText().toString().trim();// ȷ������

			if (oldPasswd.equals("") || newPasswd.equals("")
					|| confirmPasswd.equals("")) {
				Toast.makeText(getActivity(), "���ݲ���Ϊ��", Toast.LENGTH_SHORT)
						.show();
				return;
			}

			if (!oldPasswd.equals(passwd)) {
				Toast.makeText(getActivity(), "ԭ�������", Toast.LENGTH_SHORT)
						.show();
				return;
			}

			if (!newPasswd.equals(confirmPasswd)) {
				Toast.makeText(getActivity(), "ȷ���������", Toast.LENGTH_SHORT)
						.show();
				return;
			}

			// ��ʼ�޸�����
			new AsyncTask<String, String, Boolean>() {

				@Override
				protected void onPreExecute() {
					// TODO Auto-generated method stub
					super.onPreExecute();
					progressDialog.setMessage("�����޸�����");
				}

				@Override
				protected Boolean doInBackground(String... params) {
					// TODO Auto-generated method stub
					Http http = new Http();
					List<NameValuePair> httpParams = new ArrayList<NameValuePair>();
					httpParams.add(new BasicNameValuePair("User", params[0]));
					httpParams.add(new BasicNameValuePair("pwd", params[2]));
					httpParams.add(new BasicNameValuePair("Oldpwd", params[1]));
					httpParams.add(new BasicNameValuePair("usersImg", ""));
					httpParams.add(new BasicNameValuePair("phone", params[0]));
					String res = http.doPost(Constants.URL_MODIFY_USER,
							httpParams);

					boolean result = false;
					if (res != null) {
						result = res.trim().equals("1");
					}

					return result;
				}

				@Override
				protected void onPostExecute(Boolean result) {
					// TODO Auto-generated method stub
					super.onPostExecute(result);
					progressDialog.dismiss();
					Context context = getActivity();
					if (result) { // �޸ĳɹ�
						Toast.makeText(context, "�޸�����ɹ�", Toast.LENGTH_SHORT)
								.show();
						SharePreferenceUtils spu = new SharePreferenceUtils(
								context);
						String newPasswd = etNewPasswd.getText().toString()
								.trim();// ������
						passwd = newPasswd;
						spu.savePasswd(newPasswd);
						app.setPasswd(newPasswd);

					} else {// �޸�ʧ��
						Toast.makeText(context, "�޸�����ʧ��", Toast.LENGTH_SHORT)
								.show();
					}
				}
			}.execute(userName, passwd, newPasswd);

			break;

		case R.id.showRight:// ��ʾ�ұ�
			((InterfaceActivity) getActivity()).showRight();
			break;

		default:
			break;
		}
	}

}
