package adapter;

import java.util.List;

import model.shareObject;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sharefriend.app.R;

public class useHistoryAdapter extends Adapter<RecyclerView.ViewHolder>{
	private Context mContext;
    private int mCount;
    private List<shareObject> list;
	private OnRecyclerViewListener onRecyclerViewListener;
    
    
    public useHistoryAdapter(Context context,int count,List<shareObject> sObject) {
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
		    ((useHistoryViewHolder)holder).time.setText(list.get(position).getCreatedAt().substring(6,10));
		    ((useHistoryViewHolder)holder).host.setText(list.get(position).getMyUser().getNick());
		    ((useHistoryViewHolder)holder).user.setText(list.get(position).getReceiveUser().getNick());
		    ((useHistoryViewHolder)holder).shareObject.setText(list.get(position).getTitle());
		    ((useHistoryViewHolder)holder).location.setText(list.get(position).getObjectionPoint().toString());
		    ((useHistoryViewHolder)holder).isComplete.setText(""+list.get(position).getIsComplete());
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
	    
	    	return new useHistoryViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_sharehistory, parent,false));
	  
	}
	
	
	class useHistoryViewHolder extends ViewHolder implements View.OnClickListener,View.OnLongClickListener{
       
		TextView time;             //时间
		TextView host;            //持有者
		TextView user;            //使用者
        TextView shareObject;           //名字
        TextView location;         //地点
        TextView isComplete;        //  是否完成
        
		public useHistoryViewHolder(View view) {
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
