package activity;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URI;
import java.security.PublicKey;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;

import message.myMessageHandler;
import model.BmobIMApplication;
import model.Friend;
import model.Jubao;
import model.UniversalImageLoader;
import model.UserModel;

import cn.bmob.newim.BmobIM;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FetchUserInfoListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;


import com.amap.api.location.AMapLocationClient;
import com.amap.api.mapcore2d.bm;
import com.amap.api.services.a.bu;
import com.amap.api.services.a.m;
import com.sharefriend.app.R;

import db.myUserDB;
import db.myUserdbHelper;


import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class weather_info extends baseFragmentActivity {

	 public static myUserDB myUserdb;
     public static String objectId;      //ͳһ���õ�objectId
     private AlertDialog dialog1;
     public static MyUser currentUser;
     private java.util.Date date;
     private AlertDialog dialog2;
	@Override
	public void onCreate(Bundle savedInstance)
	{    currentUser=BmobUser.getCurrentUser(MyUser.class);
	     if(currentUser!=null){
	    	objectId=currentUser.getObjectId();
     	 }
	    zhudongLogin=getIntent().getIntExtra("login", 0);
		super.onCreate(savedInstance);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);
		if(weather_info.this!=null)
	    	loginAct.application.add(weather_info.this);
        init();	
	    
	 }		
	private void init() 
	{

	   myUserdb=myUserDB.getInstance(this);
	   
	   BmobQuery<MyUser> bmobQuery=new BmobQuery<MyUser>();
	   
	   bmobQuery.getObject(objectId,new QueryListener<MyUser>() {
		
		@Override
		public void done(final MyUser myUser, BmobException e) {
			if(e==null){
				if(myUser.getInstallationId()==null&&loginAct.installationId!=null){
					   MyUser newUser=new MyUser();
					   newUser.setInstallationId(loginAct.installationId);
					   newUser.update(currentUser.getObjectId(), new UpdateListener() {
						
				    	@Override
						public void done(BmobException e) {
							if(e==null){
                                 zhudongLogin=1;
                                 jiaKeFu();
                                 checkJuBao();
                                   checkEmailVerify(myUser);
                            }else if(e.getErrorCode()==206){
								Toast.makeText(weather_info.this, "Ϊ�������˻���ȫ�������µ�¼", Toast.LENGTH_SHORT).show();
							    MyUser.logOut();
				 				MyUser currentUser=BmobUser.getCurrentUser(MyUser.class);
								Intent intent=new Intent(getApplicationContext(),loginAct.class);
								startActivity(intent);
								finish();
						}
						}
					    });
				}else if(myUser.getInstallationId().equals(loginAct.installationId)){    
					zhudongLogin=0;
					checkJuBao();
					checkEmailVerify(myUser);
				  }else {
					   fragmentChat.refreshUserPicture(null, 1);  //Ϊ1��ʱ���ʾ��������
					   fragmentChat.refreshNewFriend();
					   fragmentChat.refreshConversations(2, null);
					   MyUser newUser=new MyUser();
					   newUser.setInstallationId(loginAct.installationId);
					   MyUser bmobUser=BmobUser.getCurrentUser(MyUser.class);
					   newUser.update(bmobUser.getObjectId(), new UpdateListener() {
						
						@Override
						public void done(BmobException e) {
							if(e==null){
								zhudongLogin=1;
								checkJuBao();
								checkEmailVerify(myUser);
								update();
								
							}else if(e.getErrorCode()==206){
								Toast.makeText(weather_info.this, "Ϊ�������˻���ȫ�������µ�¼", Toast.LENGTH_SHORT).show();
								MyUser.logOut();
								MyUser currentUser=BmobUser.getCurrentUser(MyUser.class);
								Intent intent=new Intent(getApplicationContext(),loginAct.class);
								startActivity(intent);
								finish();
							}
						}
					    });
				 }
			}else {
				Toast.makeText(weather_info.this,"ʧ��,"+e.getMessage(), Toast.LENGTH_SHORT).show();
			}
			
		}
	  });

	  //  BmobIM.registerDefaultMessageHandler(new myMessageHandler(weather_info.this));
	
		
	}
	private void checkEmailVerify(MyUser myUser){
		 if(loginAct.isEmail(currentUser.getUsername())&&!currentUser.getEmailVerified()){
			    if(!myUser.getEmailVerified()){
				 
			 
           	String time  = currentUser.getCreatedAt();
           
					SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					try {
					 	date = sdf.parse(time);
					} catch (ParseException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					long creatime=date.getTime();
			
           	Date    curDate    =   new    Date(System.currentTimeMillis());//��ȡ��ǰʱ��       
           	long  currenttime=curDate.getTime();
             	if((currenttime-creatime)/1000/60/60/24>10){
           		showDialog2();
              	}else if((currenttime-creatime)/1000/60/60/24>7){
             		requestVerifyEmail();
              	}
			  }else { 
				  
			   MyUser.fetchUserInfo(new FetchUserInfoListener<MyUser>() {

				@Override
				public void done(MyUser user, BmobException e) {
				     if(e==null){
				    	 Log.d("Main","ok");
				     }else {
						Log.d("Main", "no ok"+e.getMessage());
					}
					
				}
			});
			}
		 }
	}
	private void showDialog2(){
		AlertDialog.Builder builder=new AlertDialog.Builder(weather_info.this);
		builder.setMessage("����ǰ����δ����֤���޷���½������ע��ҳ����������ţ��ٵ��������֤������֤������Ϊ�˷�ֹռ���������䣬���˺Ž���ע��");
		builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				 Intent intent=new Intent(weather_info.this,loginAct.class);
				 startActivity(intent);
				 if(dialog2!=null&&dialog2.isShowing()){
					 dialog2.dismiss();
				 }
				 MyUser.logOut();
				 finish();
			}
		});
		builder.setCancelable(false);
		dialog2=builder.create();
		if(dialog2!=null&&!dialog2.isShowing())
	       dialog2.show();
	}
	private void requestVerifyEmail(){
		AlertDialog.Builder builder=new AlertDialog.Builder(weather_info.this);
	    builder.setMessage("����ǰ���䲢δ��֤�������˻�������������֤������֤������Ϊ�˷�ֹռ���������䣬���˺Ž������");
		builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				 if(dialog2!=null&&dialog2.isShowing()){
					 dialog2.dismiss();
				 }
			
			}
		});
		builder.setCancelable(false);
		dialog2=builder.create();
		if(dialog2!=null&&!dialog2.isShowing())
	       dialog2.show();
	}
	private void jiaKeFu(){   //��ӿͷ�Ϊ����
		MyUser currentUser=BmobUser.getCurrentUser(MyUser.class);
		Friend friend=new Friend();
		friend.setMyUser(currentUser);
		MyUser kefuUser=new MyUser();
		kefuUser.setObjectId("e5be088480");
		friend.setFriendUser(kefuUser);
		friend.save(new SaveListener<String>() {
			
			@Override
			public void done(String a, BmobException e) {
				
				if(e!=null){
					Toast.makeText(weather_info.this, e.getMessage(), Toast.LENGTH_SHORT).show();
				}
			}
		});
		friend.setMyUser(kefuUser);
		friend.setFriendUser(currentUser);
        friend.save(new SaveListener<String>() {
			
			@Override
			public void done(String a, BmobException e) {
				
				if(e!=null){
					Toast.makeText(weather_info.this, e.getMessage(), Toast.LENGTH_SHORT).show();
				}
			}
        });
        fragmentChat.converdb.saveId("e5be088480",1);  //����洢��ϵ�˵�id���ǳƣ�Ȼ���ȡ�Ự��ʱ��Ͳ��洢��ϵ�˵��ǳ��ˣ���ʱ���ǳƴ���Ȼ��������Դ洢isFriend���ʹ��ж��Ƿ�������
    	fragmentChat.converdb.saveNickById("e5be088480", "�ͷ�С����");
    	fragmentChat.converdb.saveTouXiangById("e5be088480","http://bmob-cdn-9223.b0.upaiyun.com/2017/08/12/6e9b816b04374cdf83523a64a6a72bd9.png");
    	
    	fragmentChat.refreshNewFriend();
	}
	
	
	
	@Override
	public void onBackPressed() {
		if(fragmentChat.myHorizontalView.getScrollX()!=MyHorizontalView.mMenuWidth){
			fragmentChat.myHorizontalView.smoothScrollTo(MyHorizontalView.mMenuWidth, 0);
		}else if(fragmentChat.popupWindow!=null&&fragmentChat.popupWindow.isShowing()){  //����������Ѻ�����������
			fragmentChat.popupWindow.dismiss();
	    } else {
	 		super.onBackPressed();
		}

	}
	private void update(){
		fragmentChat.refreshBlack(0); //Ϊ0��ʱ�򶼻��ѯ
		
	}
	
	private void checkJuBao(){
		BmobQuery<Jubao> bmobQuery=new BmobQuery<Jubao>();
		MyUser myUser=new MyUser();
		myUser.setObjectId(objectId);
		bmobQuery.addWhereEqualTo("myUser",myUser);
		bmobQuery.findObjects(new FindListener<Jubao>() {

			@Override
			public void done(List<Jubao> list, BmobException e) {
				if(list!=null&&list.size()>0){
					Integer i=list.get(0).getSex();
					if(i==null)
						i=0;
					
					Integer j=list.get(0).getSaorao();
					if(j==null)
						j=0;
					
					Integer k=list.get(0).getAnticountry();
					if(k==null)
						k=0;
					
					Integer sum=list.get(0).getSum();
					if(sum==null)
						sum=0;
					
					if(sum>300){
						showDialog1(null,1);
					}else if(i>100){
					   showDialog1("����ɫ�鱩��",0);
					}else if(j>100){
						showDialog1("����ɧ��",0);
					}else if(k>100){
						showDialog1("Υ�����κͷ���",0);
					}
				}
				
			}
		});
	}
	private void showDialog1(String content,int i){
		AlertDialog.Builder builder=new AlertDialog.Builder(weather_info.this);
		if(i==0)
	    	builder.setMessage("����ǰ�˻����ٱ���Σ�ԭ��:"+content+"����ʱ�޷���½��ʮ�����");
		else if(i==1)
			builder.setMessage("����ǰ�˻����ٱ���Σ������ײ��");
		builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				 Intent intent=new Intent(weather_info.this,loginAct.class);
				 startActivity(intent);
				 if(dialog1!=null&&dialog1.isShowing()){
					 dialog1.dismiss();
				 }
				 MyUser.logOut();
				 finish();
			}
		});
		builder.setCancelable(false);
		dialog1=builder.create();
		if(dialog1!=null&&!dialog1.isShowing())
	       dialog1.show();
	}
	@Override
	public void onActivityResult(int requestCode,int resultCode,Intent data)
	{ 
		switch(requestCode)
		{
		case 1:
			break;
		case 2:
			if(resultCode==RESULT_OK)
			{final Uri uri=data.getData();
			ContentResolver cr=this.getContentResolver();
			try{ 
				   BitmapFactory.Options opts=new BitmapFactory.Options();
				   opts.inTempStorage=new byte[100*1024];   //Ϊλͼ����100K�Ļ���
				
				   opts.inPreferredConfig=Bitmap.Config.RGB_565;//����λͼ��ɫ��ʾ�Ż���ʽ
			     	opts.inPurgeable=true;//.����ͼƬ���Ա����գ�����Bitmap���ڴ洢Pixel���ڴ�ռ���ϵͳ�ڴ治��ʱ���Ա�����
				
			     	opts.inSampleSize=2;
				    opts.inInputShareable=true;//���ý���λͼ�ĳߴ���Ϣ
	
				Bitmap bitmap=BitmapFactory.decodeStream(cr.openInputStream(uri),null,opts);
		        
				bitmap=compressImage(bitmap);
				
				fragmentChat.refreshUserPicture(bitmap,0);
				File file=new File(Environment.getExternalStorageDirectory()+"/sharefriend/"+(String)MyUser.getObjectByKey("username")+"/"+(String)MyUser.getObjectByKey("username")+"ͷ��.png");
				BufferedOutputStream bos=new BufferedOutputStream(new FileOutputStream(file));
				bitmap.compress(Bitmap.CompressFormat.PNG, 100,bos);
				bos.flush();
				bos.close();
				
             	final BmobFile bmobFile=new BmobFile(file);
				bmobFile.uploadblock(new UploadFileListener() {
					
					
					@Override
					public void onProgress(Integer value){
						
					}

					@Override
					public void done(BmobException e) {
						   if(e==null){
						String fileUrl=bmobFile.getFileUrl();
					    MyUser userInfo=BmobUser.getCurrentUser(MyUser.class);
						final String yuanUrl=userInfo.getTouXiangUrl();
						userInfo.setTouXiangUrl(fileUrl);
						
						userInfo.update(userInfo.getObjectId(),new UpdateListener() {
							
							@Override
							public void done(BmobException e) {
								  if(e==null){
							            Toast.makeText(weather_info.this, "����ɹ�",Toast.LENGTH_SHORT).show();
							            BmobFile file=new BmobFile();
							            if(yuanUrl!=null){
							            file.setUrl(yuanUrl);
							            file.delete(new UpdateListener() {
											
											@Override
											public void done(BmobException e) {
												if(e==null){
													
												}else {
													Toast.makeText(weather_info.this,e.getMessage(), Toast.LENGTH_SHORT).show();
												}
												
											}
										});
								  }
							        }else{
							           Toast.makeText(weather_info.this,"�����û���Ϣʧ��:" + e.getMessage(), Toast.LENGTH_SHORT).show();
							        }
								
							}
					    	});
						   } else {
							   Toast.makeText(weather_info.this,"ʧ�ܣ�"+e.getMessage(), Toast.LENGTH_SHORT).show();
							
						}
					}
				});
			}catch (Exception e) {
				e.printStackTrace();
			}
			
			break;
			}
		default:
			break;
		}
	}
	public static String getPath(final Context context, final Uri uri) {  
		  
	    final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;  
	  
	    // DocumentProvider  
	    if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {  
	        // ExternalStorageProvider  
	        if (isExternalStorageDocument(uri)) {  
	            final String docId = DocumentsContract.getDocumentId(uri);  
	            final String[] split = docId.split(":");  
	            final String type = split[0];  
	  
	            if ("primary".equalsIgnoreCase(type)) {  
	                return Environment.getExternalStorageDirectory() + "/" + split[1];  
	            }  
	  
	            // TODO handle non-primary volumes  
	        }  
	        // DownloadsProvider  
	        else if (isDownloadsDocument(uri)) {  
	  
	            final String id = DocumentsContract.getDocumentId(uri);  
	            final Uri contentUri = ContentUris.withAppendedId(  
	                    Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));  
	  
	            return getDataColumn(context, contentUri, null, null);  
	        }  
	        // MediaProvider  
	        else if (isMediaDocument(uri)) {  
	            final String docId = DocumentsContract.getDocumentId(uri);  
	            final String[] split = docId.split(":");  
	            final String type = split[0];  
	  
	            Uri contentUri = null;  
	            if ("image".equals(type)) {  
	                contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;  
	            } else if ("video".equals(type)) {  
	                contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;  
	            } else if ("audio".equals(type)) {  
	                contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;  
	            }  
	  
	            final String selection = "_id=?";  
	            final String[] selectionArgs = new String[] {  
	                    split[1]  
	            };  
	  
	            return getDataColumn(context, contentUri, selection, selectionArgs);  
	        }  
	    }  
	    // MediaStore (and general)  
	    else if ("content".equalsIgnoreCase(uri.getScheme())) {  
	  
	        // Return the remote address  
	        if (isGooglePhotosUri(uri))  
	            return uri.getLastPathSegment();  
	  
	        return getDataColumn(context, uri, null, null);  
	    }  
	    // File  
	    else if ("file".equalsIgnoreCase(uri.getScheme())) {  
	        return uri.getPath();  
	    }  
	  
	    return null;  
	}  
	  
	/** 
	 * Get the value of the data column for this Uri. This is useful for 
	 * MediaStore Uris, and other file-based ContentProviders. 
	 * 
	 * @param context The context. 
	 * @param uri The Uri to query. 
	 * @param selection (Optional) Filter used in the query. 
	 * @param selectionArgs (Optional) Selection arguments used in the query. 
	 * @return The value of the _data column, which is typically a file path. 
	 */  
	public static String getDataColumn(Context context, Uri uri, String selection,  
	        String[] selectionArgs) {  
	  
	    Cursor cursor = null;  
	    final String column = "_data";  
	    final String[] projection = {  
	            column  
	    };  
	  
	    try {  
	        cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,  
	                null);  
	        if (cursor != null && cursor.moveToFirst()) {  
	            final int index = cursor.getColumnIndexOrThrow(column);  
	            return cursor.getString(index);  
	        }  
	    } finally {  
	        if (cursor != null)  
	            cursor.close();  
	    }  
	    return null;  
	}  
	  
	  
	/** 
	 * @param uri The Uri to check. 
	 * @return Whether the Uri authority is ExternalStorageProvider. 
	 */  
	public static boolean isExternalStorageDocument(Uri uri) {  
	    return "com.android.externalstorage.documents".equals(uri.getAuthority());  
	}  
	  
	/** 
	 * @param uri The Uri to check. 
	 * @return Whether the Uri authority is DownloadsProvider. 
	 */  
	public static boolean isDownloadsDocument(Uri uri) {  
	    return "com.android.providers.downloads.documents".equals(uri.getAuthority());  
	}  
	  
	/** 
	 * @param uri The Uri to check. 
	 * @return Whether the Uri authority is MediaProvider. 
	 */  
	public static boolean isMediaDocument(Uri uri) {  
	    return "com.android.providers.media.documents".equals(uri.getAuthority());  
	}  
	  
	/** 
	 * @param uri The Uri to check. 
	 * @return Whether the Uri authority is Google Photos. 
	 */  
	public static boolean isGooglePhotosUri(Uri uri) {  
	    return "com.google.android.apps.photos.content".equals(uri.getAuthority());  
	}  
	public static  Bitmap compressImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//����ѹ������������100��ʾ��ѹ������ѹ��������ݴ�ŵ�baos��
        int options = 100;
        while ( baos.toByteArray().length /1024>1000) {    //ѭ���ж����ѹ����ͼƬ�Ƿ����100kb,���ڼ���ѹ��        
            baos.reset();//����baos�����baos
            options -= 10;//ÿ�ζ�����10
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//����ѹ��options%����ѹ��������ݴ�ŵ�baos��

        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//��ѹ���������baos��ŵ�ByteArrayInputStream��
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//��ByteArrayInputStream��������ͼƬ
        return bitmap;
    }
	private int checkLocalAndWebFriend(List<Friend> Webfriends,Friend friend){   
		  for (int i = 0; i < Webfriends.size(); i++) {
			    if(Webfriends.get(i).getFriendUser().getObjectId().equals(friend.getFriendUser().getObjectId())){
			    	  return 1;
			     }
	    	}
		  return 2;
	 }
	
}
