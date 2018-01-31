package adapter;

import java.util.List;

import model.shareObject;
import myCustomView.CircleImageView;

import com.sharefriend.app.R;

import adapter.messageAdapter.conversationViewHolder;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class shareHistoryAdapter extends Adapter<RecyclerView.ViewHolder>{
	private Context mContext;
    private int mCount;
    private List<shareObject> list;
	private OnRecyclerViewListener onRecyclerViewListener;
    
    
    public shareHistoryAdapter(Context context,int count,List<shareObject> sObject){
    	mContext=context;
    	mCount=count;
    	list=sObject;
    }
    
    public void setOnRecyclerViewListener(OnRecyclerViewListener onRecyclerViewListener) {
        this.onRecyclerViewListener = onRecyclerViewListener;
    }
      
	@Override
	public int getItemCount() {
		
		return mCount;
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		    ((shareHistoryViewHolder)holder).time.setText(list.get(position).getCreatedAt().substring(6,10));
		    ((shareHistoryViewHolder)holder).host.setText(list.get(position).getMyUser().getNick());
		    ((shareHistoryViewHolder)holder).user.setText(list.get(position).getReceiveUser().getNick());
		    ((shareHistoryViewHolder)holder).shareObject.setText(list.get(position).getTitle());
		    ((shareHistoryViewHolder)holder).location.setText(list.get(position).getObjectionPoint().toString());
		    ((shareHistoryViewHolder)holder).isComplete.setText(""+list.get(position).getIsComplete());
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
	    
	    	return new shareHistoryViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_sharehistory, parent,false));
	  
	}
	
	 
	class shareHistoryViewHolder extends ViewHolder implements View.OnClickListener,View.OnLongClickListener{
       
		TextView time;             //时间
		TextView host;            //持有者
		TextView user;            //使用者
        TextView shareObject;           //名字
        TextView location;         //地点
        TextView isComplete;        //  是否完成
        
		public shareHistoryViewHolder(View view) {
			super(view);
			itemView.setOnClickListener(this);
			itemView.setOnLongClickListener(this);
			time=(TextView)view.findViewById(R.id.time);
			host=(TextView)view.findViewById(R.id.host);
			user=(TextView)view.findViewById(R.id.user);
			shareObject=(TextView)view.findViewById(R.id.shareObject);
			location=(TextView)view.findViewById(R.id.location);
			isComplete=(TextView)view.findViewById(R.id.isComplete);
		}
		@Override
		public boolean onLongClick(View v) {
			
			return true;
		}
		@Override
		public void onClick(View v) {
			
			
		}
	
		
	}
	
	

}
