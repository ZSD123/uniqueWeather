package activity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import model.Black;
import myCustomView.CircleImageView;

import Util.Utility;
import Util.download;
import activity.manageAct.ViewHolder;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

import com.sharefriend.app.R;

public class blackAct extends baseActivity {
	//这是账户管理修改密码什么的
    class ViewHolder{
    	
        CircleImageView circleImageView;
    	TextView textView;
    	
    }
    ViewHolder viewHolder;
    List<Black> blackUsers;
    
    private SharedPreferences pre;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.black);
		RelativeLayout relativeLayout=(RelativeLayout)findViewById(R.id.relative);
		CustomFontTextView textView=(CustomFontTextView)findViewById(R.id.account);
		
		pre=PreferenceManager.getDefaultSharedPreferences(this);
		
		final int designNum=pre.getInt("design", 0);
		if(designNum==4){
			relativeLayout.setBackgroundColor(Color.parseColor("#051C3D"));
			textView.setTextColor(Color.parseColor("#A2C0DE"));
		}
		
		fragmentChat.refreshBlack(2);  //为2的时候只刷新自己拉黑的人
	    blackUsers=new ArrayList<Black>();
		blackUsers=fragmentChat.converdb.getBlackFriends();
		ListView listView=(ListView)findViewById(R.id.listview);
		final BaseAdapter baseAdapter=new BaseAdapter() {
			
			@Override
			public View getView(final int position, View convertView, ViewGroup parent) {
				   if(convertView==null){
						convertView=getLayoutInflater().inflate(R.layout.item_user_friend, null);
						viewHolder=new ViewHolder();
		                viewHolder.circleImageView=(CircleImageView)convertView.findViewById(R.id.user_friend_image);
		                viewHolder.textView=(TextView)convertView.findViewById(R.id.user_friend_name);
						convertView.setTag(viewHolder);
						}else {
							viewHolder=(ViewHolder)convertView.getTag();
						}
				 	String path=Environment.getExternalStorageDirectory()+"/sharefriend/"+(String)MyUser.getObjectByKey("username")+"/head/"+blackUsers.get(position).getBlackUser().getObjectId()+".jpg_";
			       
			    	File file=new File(path);
					if(file.exists()){
						
						  try {
	  							InputStream is=new FileInputStream(file);
	  							
	  							BitmapFactory.Options opts=new BitmapFactory.Options();
	  							opts.inTempStorage=new byte[100*1024];   //为位图设置100K的缓存
	  							
	  							opts.inPreferredConfig=Bitmap.Config.RGB_565;//设置位图颜色显示优化方式
	  							opts.inPurgeable=true;//.设置图片可以被回收，创建Bitmap用于存储Pixel的内存空间在系统内存不足时可以被回收
	  							
	  							opts.inSampleSize=2;
	  							opts.inInputShareable=true;//设置解码位图的尺寸信息
	  							
	  							Bitmap bitmap2=BitmapFactory.decodeStream(is, null, opts);
	  							viewHolder.circleImageView.setImageBitmap(bitmap2);
	  						} catch (FileNotFoundException e1) {
	  							// TODO Auto-generated catch block
	  							e1.printStackTrace();
	  						}
					
					}else {
							if(blackUsers.get(position).getBlackUser().getTouXiangUrl()!=null){
						    new Thread(new Runnable() {
								
								@Override
								public void run() {
									final Bitmap bitmap=Utility.getPicture(blackUsers.get(position).getBlackUser().getTouXiangUrl());
                                     runOnUiThread(new Runnable() {
								 		
										@Override
										public void run() {
											if(bitmap!=null){
										     	viewHolder.circleImageView.setImageBitmap(bitmap);
										     	download.saveYonghuPic(bitmap, blackUsers.get(position).getBlackUser().getObjectId());
								
											}
											
										}
									});
								
								}
							}).start();
							}else {
								viewHolder.circleImageView.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.userpicture));
							}
					}
					if(designNum==4){
						viewHolder.textView.setTextColor(Color.parseColor("#A2C0DE"));
					}
					
					viewHolder.textView.setText(blackUsers.get(position).getBlackUser().getNick());
					
				return convertView;
			}
			
			@Override
			public long getItemId(int position) {
				// TODO Auto-generated method stub
				return position;
			}
			
			@Override
			public Object getItem(int position) {
				// TODO Auto-generated method stub
				return blackUsers.get(position);
			}
			
			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return blackUsers.size();
			}
		};
		listView.setAdapter(baseAdapter);
		
		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					final int position, long id) {
				  AlertDialog.Builder builder=new AlertDialog.Builder(blackAct.this);
				     final String[] xuanzeweizhi={"拉出黑名单"};
				     builder.setItems(xuanzeweizhi, new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							 BmobQuery<Black> bmobQuery=new BmobQuery<Black>();
		
					    	 MyUser currentUser=BmobUser.getCurrentUser(MyUser.class);
					    	 bmobQuery.addWhereEqualTo("myUser", currentUser);
					    	 bmobQuery.addWhereEqualTo("blackUser", blackUsers.get(position).getBlackUser());
					    	 
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
													if(e!=null){
														
													}
													
												}
											});
										}
									}else {
										Log.d("Main",e.getMessage());
									}
									
								}
							});
					    	 
							  fragmentChat.converdb.updateIsFriendI(blackUsers.get(position).getBlackUser().getObjectId(), 1);
							  fragmentChat.refreshNewFriend();
							  fragmentChat.refreshConversations(2, null);
							  blackUsers=fragmentChat.converdb.getBlackFriends();
							  baseAdapter.notifyDataSetChanged();
						}
					});
				     builder.show();
				return false;
			}
			
		});
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
			     
			}
		});
}
}
