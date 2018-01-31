package mapAct;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.shareObject;

import cn.bmob.newim.bean.BmobIMImageMessage;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobGeoPoint;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMapUtils;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.GeocodeSearch.OnGeocodeSearchListener;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.Inputtips.InputtipsListener;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.sharefriend.app.R;

import activity.ChatActivity;
import activity.MyUser;
import activity.baseActivity;
import activity.chooseAreaActivity;
import activity.fragmentChat;
import activity.fragmentMap;
import activity.myAccountAct;
import activity.weather_info;
import android.R.integer;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class iwantShareAct extends baseActivity implements InputtipsListener,OnGeocodeSearchListener{
    
	private Spinner spinner1;    
	private ArrayAdapter<String> arrayAdapter1;
	private ImageButton imageButton;
	private ImageView imageView;
    String path;
    private InputtipsQuery query;
    private EditText editText1;    //描述edittext
    private EditText editText2;    //地理位置edittext
    
    private SharedPreferences.Editor editor;
    private SharedPreferences pre; 
	
    private String city;
    private Inputtips inputtips;
    
    private ListView listView;
    private SimpleAdapter simpleAdapter;
    private  List<Map<String, Object>> listems;
    
    private Button share;   //共享按钮
    private GeocodeSearch geocodeSearch;
    private GeocodeQuery query1;
    private RegeocodeQuery query2;
    
    private double lat;
    private double lon;
    
    private double lat1;  //本人所在位置
    private double lon1;  //本人所在位置
    
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.iwantshare);
		
		geocodeSearch=new GeocodeSearch(this);
		geocodeSearch.setOnGeocodeSearchListener(this);

		
		
		listems = new ArrayList<Map<String, Object>>();  
		
		  final String arr[]=new String[]{
			    	"雨伞",
			    	"衣服",
			    	"食物",
			    	"汽车",
			    	"书籍",
			    	"摄像机",
			    	"电脑",
			    	"手机",
			    	"卡券"
			    };
		
		editText1=(EditText)findViewById(R.id.descriptionEdit);
		editText2=(EditText)findViewById(R.id.dili);
		  
		share=(Button)findViewById(R.id.share);
		
		listView=(ListView)findViewById(R.id.list);
		
		spinner1=(Spinner)findViewById(R.id.spinner1);
		imageButton=(ImageButton)findViewById(R.id.imagebutton);
		imageView=(ImageView)findViewById(R.id.image);
		
		pre=PreferenceManager.getDefaultSharedPreferences(iwantShareAct.this);
		editor=PreferenceManager.getDefaultSharedPreferences(this).edit();
	
		
		
	   city=pre.getString("locCity", "北京");
		
	   lat1=pre.getFloat("lat", 39);
	   lon1=pre.getFloat("lon", 116);
	   
	   arrayAdapter1=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,arr);
		
	   query2=new RegeocodeQuery(new LatLonPoint(lat1, lon1), 200, GeocodeSearch.AMAP);
		
		geocodeSearch.getFromLocationAsyn(query2);
		
		spinner1.setAdapter(arrayAdapter1);
		
		share.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				String address=editText2.getText().toString();
				query1=new GeocodeQuery(address, city);
				
				geocodeSearch.getFromLocationNameAsyn(query1);
				
				Log.d("Main", "1");
			}
		});
		
		
		editText2.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
			      
				String dili=s.toString();
				if(!dili.equals("")){
					 
					beginTip(dili);
					
				}else {
					 listView.setVisibility(View.GONE);
				 }
				
			}
		});
		
		
		
		
		imageButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
				
			}
		});
		
		imageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				AlertDialog.Builder builder=new AlertDialog.Builder(iwantShareAct.this);
			     final String[] xuanzeweizhi={"拍照","图库"};
			     builder.setItems(xuanzeweizhi, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						  if(which==0){
							  
						      openTakePhoto();       //拍照
						     
						      
						  }else if(which==1){
							  
						     	Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
								intent.addCategory(Intent.CATEGORY_OPENABLE);
					            intent.setType("image/jpeg");
					            startActivityForResult(intent, 2);
					        
					            
						  }
					}
				});
			     builder.show();
				
			}
		});
		
		
		
		
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
			case 1 :            //拍照后返回
				
				if(resultCode==RESULT_OK){
					
					File file=new File(path);
					ContentResolver cr=this.getContentResolver();
					
					
					if(file.exists()){
						try{ 
						    BitmapFactory.Options opts=new BitmapFactory.Options();
						    opts.inTempStorage=new byte[100*1024];   //为位图设置100K的缓存
						
						    opts.inPreferredConfig=Bitmap.Config.RGB_565;//设置位图颜色显示优化方式
					     	opts.inPurgeable=true;//.设置图片可以被回收，创建Bitmap用于存储Pixel的内存空间在系统内存不足时可以被回收
						
					     	opts.inSampleSize=2;
						    opts.inInputShareable=true;//设置解码位图的尺寸信息
			
						    Bitmap bitmap=BitmapFactory.decodeFile(path, opts);
				        
						    bitmap=weather_info.compressImage(bitmap);
		                    imageView.setImageBitmap(bitmap);
					        
		                	
		                    
					}catch(Exception e){
						e.printStackTrace();
					}
						
					 
				}
					
				}
				break;
			case 2:
				
			  if(resultCode==RESULT_OK){	
				final Uri uri=data.getData();
				path=weather_info.getPath(iwantShareAct.this, uri);
				ContentResolver cr=this.getContentResolver();
				try{ 
					    BitmapFactory.Options opts=new BitmapFactory.Options();
					    opts.inTempStorage=new byte[100*1024];   //为位图设置100K的缓存
					
					    opts.inPreferredConfig=Bitmap.Config.RGB_565;//设置位图颜色显示优化方式
				     	opts.inPurgeable=true;//.设置图片可以被回收，创建Bitmap用于存储Pixel的内存空间在系统内存不足时可以被回收
					
				     	opts.inSampleSize=2;
					    opts.inInputShareable=true;//设置解码位图的尺寸信息
		
					Bitmap bitmap=BitmapFactory.decodeStream(cr.openInputStream(uri),null,opts);
			        
					bitmap=weather_info.compressImage(bitmap);
	                
					imageView.setImageBitmap(bitmap);
				}catch(Exception e){
					e.printStackTrace();
				}
				
				}
				break;
			default :
				break;
		}
	}
	
	 private void openTakePhoto(){    
    	 /**
    	 * 在启动拍照之前最好先判断一下sdcard是否可用
    	 */
    	 String state = Environment.getExternalStorageState(); //拿到sdcard是否可用的状态码
    	 if (state.equals(Environment.MEDIA_MOUNTED)){   //如果可用
    	  Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
    	  
    	  SimpleDateFormat format=new SimpleDateFormat("yyyyMMddHHmmss");
    	  Date curDate=new Date(System.currentTimeMillis());
    	  String str=format.format(curDate);
    	  
    	  path=Environment.getExternalStorageDirectory().getAbsoluteFile()+"/sharefriend/"+(String)MyUser.getObjectByKey("username")+"/picture/"+str+".jpg";
    	  Uri uri=Uri.fromFile(new File(path));
    	  intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
    	  startActivityForResult(intent,1);
    	  
    	 }else {
    	  Toast.makeText(iwantShareAct.this,"sdcard不可用",Toast.LENGTH_SHORT).show();
    	 }
    	}


	


	@Override
	public void onGetInputtips(final List<Tip> list, int code) {
		   if(code==1000){
			   
			    listems.clear();  
			  
			    
		        for (int i = 0; i < list.size(); i++) {  
		            Map<String, Object> listem = new HashMap<String, Object>();  
		            listem.put("text",  list.get(i).getName());  
		            listems.add(listem);  
		        }  
			   
			 
			   
			   listView.setVisibility(View.VISIBLE);
               
			   simpleAdapter=new SimpleAdapter(iwantShareAct.this, listems, R.layout.simpletextview ,  new String[] { "text" }, new int[] {R.id.text}); //数据   
			   
			   listView.setAdapter(simpleAdapter);
			   setListViewHeightBasedOnChildren(listView);
			   
			   listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					
					editText2.setText(list.get(position).getName());
					
					
				}
			});
			  
		   }
		
	}
	
	private void beginTip(String s){   //开始提示
		

		query=new InputtipsQuery(s, city);
		query.setCityLimit(true);
		
		inputtips=new Inputtips(iwantShareAct.this, query);
		inputtips.setInputtipsListener(this);
		
		inputtips.requestInputtipsAsyn();
		
	}
	
	private  void setListViewHeightBasedOnChildren(ListView listView) { 
		// 获取ListView对应的Adapter 
		ListAdapter listAdapter = listView.getAdapter(); 
		if (listAdapter == null) {
		 return; 
		} 
		int totalHeight = 0;
		 for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
		 // listAdapter.getCount()返回数据项的数目
		 View listItem = listAdapter.getView(i, null, listView);
		 // 计算子项View 的宽高
		 listItem.measure(0, 0); 
		// 统计所有子项的总高度 
		totalHeight += listItem.getMeasuredHeight();
		 }
		 ViewGroup.LayoutParams params = listView.getLayoutParams(); 
		params.height = totalHeight + (listView.getDividerHeight() * 
		(listAdapter.getCount() - 1)); 
		// listView.getDividerHeight()获取子项间分隔符占用的高度 
		// params.height最后得到整个ListView完整显示需要的高度
		 listView.setLayoutParams(params); 
		}



	@Override
	public void onGeocodeSearched(GeocodeResult result, int code) {
		
		if(code==1000){
			lat=result.getGeocodeAddressList().get(0).getLatLonPoint().getLatitude();
			lon=result.getGeocodeAddressList().get(0).getLatLonPoint().getLongitude();
			
			float distance=AMapUtils.calculateLineDistance(new LatLng(lat, lon), new LatLng(lat1, lon1));
			
			if(distance>500){
	
				
				Toast.makeText(iwantShareAct.this,"不好意思，只能在您附近的位置设置",Toast.LENGTH_LONG).show();
			}else {
				
				//先上传相应的共享物品的图片
				if(path==null){
					
					Toast.makeText(iwantShareAct.this,"文件不存在", Toast.LENGTH_SHORT).show();
				
				}else {
					
				
				File file=new File(path);
				if(file.exists()){
			    final BmobFile bmobFile=new BmobFile(file);
				bmobFile.uploadblock(new UploadFileListener() {
					
					
					@Override
					public void onProgress(Integer value){
						  
						Log.d("Main", "value="+value);
						
					}

					@Override
					public void done(BmobException e) {
						   if(e==null){
						String fileUrl=bmobFile.getFileUrl();
                       		
                        shareObject myShareObject=new shareObject();
						myShareObject.setImageUrl(fileUrl);    //设置共享物品图片
					    myShareObject.setTitle(spinner1.getSelectedItem().toString());
					    myShareObject.setDescription(editText1.getText().toString());
					    myShareObject.setIsComplete(false);
					    myShareObject.setObjectionPoint(new BmobGeoPoint(lon, lat));
					    myShareObject.setMyUser(MyUser.getCurrentUser(MyUser.class));
					    myShareObject.setIsWorking(false);
						myShareObject.save(new SaveListener<String>() {
							
							@Override
							public void done(String objectId, BmobException e) {
								if(e==null){
									  Toast.makeText(iwantShareAct.this,"共享成功", Toast.LENGTH_SHORT).show();
									  finish();
									  
								}else {
									Toast.makeText(iwantShareAct.this,"失败，"+e.getMessage(), Toast.LENGTH_SHORT).show();
								}
								
							}
						});
						
						   } else {
							   Toast.makeText(iwantShareAct.this,"失败，"+e.getMessage(), Toast.LENGTH_SHORT).show();
							
						}
					}
				});
				
				
				
			}else {
				 Toast.makeText(iwantShareAct.this,"文件不存在", Toast.LENGTH_SHORT).show();
			}
			
				
			}
				
			}
				
		}else {
			Toast.makeText(iwantShareAct.this,"不好意思，出了点问题，请待会再试",Toast.LENGTH_SHORT).show();
		}
		
	}


	@Override
	public void onRegeocodeSearched(RegeocodeResult result, int code) {
		
		if(code==1000){
			String address= result.getRegeocodeAddress().getFormatAddress();
			editText2.setText(address);
		}
		
	}

		
	

}
