package mapAct;

import java.util.ArrayList;
import java.util.List;

import model.shareObject;
import myCustomView.DividerItemDecoration;
import myCustomView.myChatPager;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

import com.sharefriend.app.R;

import activity.MyHorizontalView;
import activity.MyUser;
import activity.baseActivity;
import adapter.shareHistoryAdapter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

public class share_historyAct extends baseActivity {
    private myChatPager sharePager;
    private List<View> views;
    private shareHistoryAdapter shareAdapter;
    private MyUser currentUser;    //当前用户
    
    private Button history_share;
    private Button history_use;
    
    private ImageButton imageButton;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.share_history);
		
		currentUser=MyUser.getCurrentUser(MyUser.class);
		
		
		history_share=(Button)findViewById(R.id.history_share);
		history_use=(Button)findViewById(R.id.history_use);
		imageButton=(ImageButton)findViewById(R.id.imagebutton);
		
		sharePager=(myChatPager)findViewById(R.id.sharePager);
		
		View view3;    //共享View
		View view4;    //使用View
		
		view3=getLayoutInflater().inflate(R.layout.list_share_history,null);
		view4=getLayoutInflater().inflate(R.layout.list_use_history, null);
		
		final RecyclerView shareRecyclerView=(RecyclerView)view3.findViewById(R.id.rc_view);
        
		shareRecyclerView.setLayoutManager(new LinearLayoutManager(this));
		shareRecyclerView.setItemAnimator(new DefaultItemAnimator());
		shareRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
		
		BmobQuery<shareObject> query=new BmobQuery<shareObject>();
		query.addWhereEqualTo("myUser", currentUser);
		query.findObjects(new FindListener<shareObject>() {
			
			@Override
			public void done(List<shareObject> list, BmobException e) {
				if(e==null){
				   shareAdapter=new shareHistoryAdapter(share_historyAct.this, list.size(),list);
				   shareRecyclerView.setAdapter(shareAdapter);
				   
				}else {
					Toast.makeText(share_historyAct.this,"出现错误，"+e.getMessage(),Toast.LENGTH_SHORT).show();
				}
			}
		});
		
        final RecyclerView useRecyclerView=(RecyclerView)view4.findViewById(R.id.rc_view);
        
        useRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        useRecyclerView.setItemAnimator(new DefaultItemAnimator());
        useRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
		
		query.addWhereEqualTo("receiveUser", currentUser);
		query.findObjects(new FindListener<shareObject>() {
			
			@Override
			public void done(List<shareObject> list, BmobException e) {
				if(e==null){
				   shareAdapter=new shareHistoryAdapter(share_historyAct.this, list.size(),list);
				   shareRecyclerView.setAdapter(shareAdapter);
				   
				}else {
					Toast.makeText(share_historyAct.this,"出现错误，"+e.getMessage(),Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		views=new ArrayList<View>();
		views.add(view3);
		views.add(view4);
		
		
		
		PagerAdapter pagerAdapter=new PagerAdapter() {
			
			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				return arg0==arg1;
			}
			
			@Override
			public int getCount() {
				
				return views.size();
			}
			
			@Override
			public void destroyItem(ViewGroup container, int position,
					Object object) {
				container.removeView(views.get(position));
			}

			@Override
			public Object instantiateItem(final ViewGroup container, int position) {  //将参数里给定的position的视图，增加到container中，以及返回当前position的view作为此图的key
			    container.addView(views.get(position));
				return views.get(position);
			}
		};
		
		sharePager.setAdapter(pagerAdapter);
		
		history_share.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				sharePager.setCurrentItem(0);
			 
				history_share.setBackgroundResource(R.drawable.danblue);
				history_share.setTextColor(Color.parseColor("#68B4FF"));
				
				history_use.setBackgroundResource(0);
				history_use.setTextColor(Color.parseColor("#000000"));
				
			}
		});
		history_use.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				sharePager.setCurrentItem(1);
				 
				history_share.setBackgroundResource(0);
				history_share.setTextColor(Color.parseColor("#000000"));
				
				history_use.setBackgroundResource(R.drawable.danblue);
				history_use.setTextColor(Color.parseColor("#68B4FF"));
				
				
			}
		});
		
		imageButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
				
			}
		});
		
	}

	  

}
