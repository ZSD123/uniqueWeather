package adapter;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;

import com.uniqueweather.app.R;

import butterknife.Bind;
import cn.bmob.newim.bean.BmobIMMessage;

/**
 * 同意添加好友的agree类型
 */
public class AgreeHolder extends BaseViewHolder implements View.OnClickListener,View.OnLongClickListener {

  @Bind(R.id.tv_time)
  protected TextView tv_time;

  @Bind(R.id.tv_message)
  protected TextView tv_message;

  public AgreeHolder(Context context, ViewGroup root, OnRecyclerViewListener listener,View view) {
    super(context, root, listener,view);
    tv_time=(TextView)view.findViewById(R.id.tv_time);
    tv_message=(TextView)view.findViewById(R.id.tv_message);
  }

  @Override
  public void bindData(Object o) {
    final BmobIMMessage message = (BmobIMMessage)o;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    String time = dateFormat.format(message.getCreateTime());
    String content = message.getContent();
    	tv_message.setText(content);
	
    tv_time.setText(time);
  }

  public void showTime(boolean isShow) {
    tv_time.setVisibility(isShow ? View.VISIBLE : View.GONE);
  }
}
