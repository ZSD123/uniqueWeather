package activity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import message.AddFriendMessage;
import model.Black;
import model.Jubao;
import myCustomView.CircleImageView;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.core.BmobIMClient;
import cn.bmob.newim.listener.ConversationListener;
import cn.bmob.newim.listener.MessageSendListener;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

import com.sharefriend.app.R;

import Util.Http;
import Util.HttpCallbackListener;
import Util.Utility;
import Util.download;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class searchResultAct extends Activity {
    private CircleImageView circleImageView;
    private TextView textView;
    private Button button;
    private TextView textView2;
    private boolean flag=false;  //�ж��Ƿ�������
    
    private String city;
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.searchresult);
		
		circleImageView=(CircleImageView)findViewById(R.id.userPicture);
		textView=(TextView)findViewById(R.id.text);
		button=(Button)findViewById(R.id.button);
		textView2=(TextView)findViewById(R.id.text1);
		
		final TextView textCity=(TextView)findViewById(R.id.textCity);
	
		
		final String nick=getIntent().getExtras().getString("nick");
		final String url=getIntent().getExtras().getString("touxiang");
		final String objectId=getIntent().getExtras().getString("objectId");
		
		final double lat=getIntent().getExtras().getDouble("lat");
		final double lon=getIntent().getExtras().getDouble("lon");
		
		if(lat!=0&&lon!=0){
			Http.queryAreaByXY(lat,lon,"http://route.showapi.com/238-2", new HttpCallbackListener() {
				
				@Override
				public void onFinish(String response) {
					try{
					 JSONObject jsonObject=new JSONObject(response);
		    		 JSONObject jsonObject2=jsonObject.getJSONObject("showapi_res_body");
		    		 JSONObject jsonObject3=jsonObject2.getJSONObject("addressComponent");
		    		 String locProvince=jsonObject3.getString("province");
		    		 String locCity=jsonObject3.getString("city");
		    		 
		    		 city=locProvince+locCity;
					}catch(Exception e){
						e.printStackTrace();
					}
					
					runOnUiThread(new Runnable() {
						public void run() {
							textCity.setVisibility(View.VISIBLE);
							textCity.setText(city);
						}
					});
					
				}
			});
		}
		
		BmobQuery<Jubao> bmobQuery=new BmobQuery<Jubao>();
		MyUser myUser=new MyUser();
		myUser.setObjectId(objectId);
		bmobQuery.addWhereEqualTo("myUser",myUser);
		bmobQuery.findObjects(new FindListener<Jubao>() {

			@Override
			public void done(List<Jubao> list, BmobException e) {
				if(e==null){
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
							textView2.setVisibility(View.VISIBLE);
							textView2.setText("����ǰ�������û����ٱ���Σ���ʱ�޷���ѯ");
							circleImageView.setVisibility(View.INVISIBLE);
							textView.setVisibility(View.INVISIBLE);
							flag=true;
						}else if(i>100){
							textView2.setVisibility(View.VISIBLE);
							textView2.setText("����ǰ�������û����ٱ���Σ���ʱ�޷���ѯ");
							circleImageView.setVisibility(View.INVISIBLE);
							textView.setVisibility(View.INVISIBLE);
							flag=true;
						}else if(j>100){
							textView2.setVisibility(View.VISIBLE);
							textView2.setText("����ǰ�������û����ٱ���Σ���ʱ�޷���ѯ");
							circleImageView.setVisibility(View.INVISIBLE);
							textView.setVisibility(View.INVISIBLE);
							flag=true;
						}else if(k>100){
							textView2.setVisibility(View.VISIBLE);
							textView2.setText("����ǰ�������û����ٱ���Σ���ʱ�޷���ѯ");
							circleImageView.setVisibility(View.INVISIBLE);
							textView.setVisibility(View.INVISIBLE);
							flag=true;
						}
					
						if(!flag){
						
							load(objectId, url, nick);
							
						}
						
					}else if(list.size()==0){
					    	load(objectId, url, nick);
					}
				}else {
					Toast.makeText(searchResultAct.this,"���ִ���,"+e.getErrorCode()+e.getMessage(),Toast.LENGTH_SHORT).show();
				}
				
			}
		});
	
		
	
	}
	 private void sendAddFriendMessage(BmobIMUserInfo bmobIMUserInfo){    //������Ӻ�������
	   	 BmobIMConversation c=BmobIM.getInstance().startPrivateConversation(bmobIMUserInfo, true,new ConversationListener() {
				
				@Override
				public void done(BmobIMConversation arg0, BmobException e) {
					if(e==null){
						  
					}else {
						Toast.makeText(searchResultAct.this, "ʧ�ܣ�"+e.getMessage(),Toast.LENGTH_SHORT).show();

					}
					
				}
			});
	   
	   		 BmobIMConversation conversation=BmobIMConversation.obtain(BmobIMClient.getInstance(),c);
	   		 
	   	 AddFriendMessage msg=new AddFriendMessage();
	        MyUser currentUser=BmobUser.getCurrentUser(MyUser.class);
	        msg.setContent("�ܸ�����ʶ�㣬���ԼӸ�������");
	        Map<String,Object> map=new HashMap<String,Object>();
	        map.put("name", currentUser.getNick());
	        map.put("avatar", currentUser.getTouXiangUrl());
	        map.put("uid", currentUser.getObjectId());
	        msg.setExtraMap(map);
	        conversation.sendMessage(msg, new MessageSendListener() {
				
				@Override
				public void done(BmobIMMessage msg, BmobException e) {
					if(e==null){
						Toast.makeText(searchResultAct.this, "���������ͳɹ����ȴ���֤",Toast.LENGTH_SHORT).show();
						finish();
					}else {
						Toast.makeText(searchResultAct.this, "����ʧ�ܣ�"+e.getMessage(),Toast.LENGTH_SHORT).show();

					}
					
				}
			});
	    }
	 private void load(final String objectId,final String url,final String nick){
		 

			
		    final String path=Environment.getExternalStorageDirectory()+"/sharefriend/"+(String)MyUser.getObjectByKey("username")+"/head/"+objectId+".jpg_";
	        final File file=new File(path);
			
	        if(file.exists()){
	        	try {
	    			InputStream is=new FileInputStream(file);
	    			
	    			BitmapFactory.Options opts=new BitmapFactory.Options();
	    			opts.inTempStorage=new byte[100*1024];   //Ϊλͼ����100K�Ļ���
	    			
	    			opts.inPreferredConfig=Bitmap.Config.RGB_565;//����λͼ��ɫ��ʾ�Ż���ʽ
	    			opts.inPurgeable=true;//.����ͼƬ���Ա����գ�����Bitmap���ڴ洢Pixel���ڴ�ռ���ϵͳ�ڴ治��ʱ���Ա�����
	    			
	    			opts.inSampleSize=2;
	    			opts.inInputShareable=true;//���ý���λͼ�ĳߴ���Ϣ
	    			
	    			Bitmap bitmap2=BitmapFactory.decodeStream(is, null, opts);
	    			circleImageView.setImageBitmap(bitmap2);
	    		} catch (FileNotFoundException e1) {
	    			// TODO Auto-generated catch block
	    			e1.printStackTrace();
	    		}

	        }
	        
	        textView.setVisibility(View.VISIBLE);
	        textView.setText(nick);
	        
			 if(url!=null)
			        new Thread(new Runnable() {
						
						@Override
						public void run() {
							 final Bitmap bitmap= Utility.getPicture(url);
			                 runOnUiThread(new Runnable() {
						 		
								@Override
								public void run() {
									if(bitmap!=null){
								     	circleImageView.setImageBitmap(bitmap);
								     	download.saveYonghuPic(bitmap, objectId);
									} else if(file.exists()){
							        	try {
							    			InputStream is=new FileInputStream(file);
							    			
							    			BitmapFactory.Options opts=new BitmapFactory.Options();
							    			opts.inTempStorage=new byte[100*1024];   //Ϊλͼ����100K�Ļ���
							    			
							    			opts.inPreferredConfig=Bitmap.Config.RGB_565;//����λͼ��ɫ��ʾ�Ż���ʽ
							    			opts.inPurgeable=true;//.����ͼƬ���Ա����գ�����Bitmap���ڴ洢Pixel���ڴ�ռ���ϵͳ�ڴ治��ʱ���Ա�����
							    			
							    			opts.inSampleSize=2;
							    			opts.inInputShareable=true;//���ý���λͼ�ĳߴ���Ϣ
							    			
							    			Bitmap bitmap2=BitmapFactory.decodeStream(is, null, opts);
							    			circleImageView.setImageBitmap(bitmap2);
							    		} catch (FileNotFoundException e1) {
							    			// TODO Auto-generated catch block
							    			e1.printStackTrace();
							    		}

									}else {
										 try {
												InputStream is=new FileInputStream(file);
												
												BitmapFactory.Options opts=new BitmapFactory.Options();
												opts.inTempStorage=new byte[100*1024];   //Ϊλͼ����100K�Ļ���
												
												opts.inPreferredConfig=Bitmap.Config.RGB_565;//����λͼ��ɫ��ʾ�Ż���ʽ
												opts.inPurgeable=true;//.����ͼƬ���Ա����գ�����Bitmap���ڴ洢Pixel���ڴ�ռ���ϵͳ�ڴ治��ʱ���Ա�����
												
												opts.inSampleSize=2;
												opts.inInputShareable=true;//���ý���λͼ�ĳߴ���Ϣ
												
												Bitmap bitmap2=BitmapFactory.decodeResource(getResources(),R.drawable.userpicture, opts);
												circleImageView.setImageBitmap(bitmap2);
											} catch (FileNotFoundException e1) {
												// TODO Auto-generated catch block
												e1.printStackTrace();
											}
									  }
									
									
								}
							});
						
						}
					}).start();
		 
		 if(fragmentChat.converdb.getIsFriend(objectId)==0&&!objectId.equals(weather_info.objectId)){
				button.setVisibility(View.VISIBLE);
				button.setText("���Ϊ����");
				button.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						 BmobIMUserInfo bmobIMUserInfo=new BmobIMUserInfo();   //������ֵ����������ĥ���ˣ��Ҳ��˺þã����ںþã�ԭ������String��nizhenû�취����setName��
						 bmobIMUserInfo.setUserId(objectId);
						 bmobIMUserInfo.setAvatar(url);
						  if(nick==null||nick.equals("")){
							  bmobIMUserInfo.setName(objectId);
						  }else{
						     bmobIMUserInfo.setName(nick);
						  }
						sendAddFriendMessage(bmobIMUserInfo);
					}
				});
			}else if(fragmentChat.converdb.getIsFriend(objectId)==1){
				
				textView2.setVisibility(View.VISIBLE);
				textView2.setText("�Է������ĺ���");
				button.setVisibility(View.VISIBLE);
				button.setText("����Ϣ");
				button.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						final BmobIMUserInfo bmobIMUserInfo=new BmobIMUserInfo();
						bmobIMUserInfo.setUserId(objectId);
						if(nick!=null&&!nick.equals(""))
					    	bmobIMUserInfo.setName(nick);
						else if(objectId!=null){
							bmobIMUserInfo.setName(objectId);
						}
						bmobIMUserInfo.setAvatar(url);
				     	BmobIM.getInstance().startPrivateConversation(bmobIMUserInfo ,new ConversationListener() {
							
							@Override
							public void done(BmobIMConversation c, BmobException e) {
								if(e==null){
									Bundle bundle=new Bundle();
									bundle.putSerializable("c",c);
									bundle.putSerializable("userInfo",bmobIMUserInfo);
									Intent intent=new Intent(searchResultAct.this,ChatActivity.class);
									intent.putExtra("bundle", bundle);
									startActivity(intent);
									
								}else {
	                                   Toast.makeText(searchResultAct.this, e.getMessage(),Toast.LENGTH_SHORT).show();
								}
								
							}
						});
					
						
					}
				});
			}else if(fragmentChat.converdb.getIsFriend(objectId)==2){
				textView2.setVisibility(View.VISIBLE);
				textView2.setText("�Է������ĺ�����");
				button.setVisibility(View.VISIBLE);
				button.setText("�Ƴ�������");
				button.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						 BmobQuery<Black> bmobQuery=new BmobQuery<Black>();
							
				    	 MyUser currentUser=BmobUser.getCurrentUser(MyUser.class);
				    	 bmobQuery.addWhereEqualTo("myUser", currentUser);
				    	 
				    	 MyUser blackUser=new MyUser();
				    	 blackUser.setObjectId(objectId);
				    	 
				    	 bmobQuery.addWhereEqualTo("blackUser", blackUser);
				    	 
				    	 bmobQuery.findObjects(new FindListener<Black>() {

							@Override
							public void done(List<Black> list, BmobException e) {
								if(e==null){
									if(list.size()>0){
										String objectId= list.get(0).getObjectId();
										Black black=new Black();
										black.delete(objectId, new UpdateListener() {
											
											@Override
											public void done(BmobException e) {
												if(e!=null&&e.getErrorCode()!=9015){
													Toast.makeText(searchResultAct.this,"���ִ���,"+e.getErrorCode()+e.getMessage(),Toast.LENGTH_SHORT).show();
												}else {
													Toast.makeText(searchResultAct.this,"�Է����Ƴ�������",Toast.LENGTH_SHORT).show();
													finish();
												}
												
											}
										});
									}
								}else {
									Log.d("Main",e.getMessage());
								}
								
							}
						});
				    	 
						  fragmentChat.converdb.updateIsFriendI(objectId, 1);
						  fragmentChat.refreshNewFriend();
						  fragmentChat.refreshConversations(2, null);
						
					}
				});
			}else if(fragmentChat.converdb.getIsFriend(objectId)==3){
				textView2.setVisibility(View.VISIBLE);
				textView2.setText("�Է��ѽ������������");
			}
			
			
	 }

}
