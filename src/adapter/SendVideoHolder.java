package adapter;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;

import com.uniqueweather.app.R;

import butterknife.Bind;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMSendStatus;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.listener.MessageSendListener;
import cn.bmob.v3.exception.BmobException;

/**
 * ���͵���Ƶ����---���Ǿٸ����ӣ���û��չʾ����Ƶ����ͼ����Ϣ�������߿�����ʵ��
 */
public class SendVideoHolder extends BaseViewHolder implements View.OnClickListener,View.OnLongClickListener {

  @Bind(R.id.iv_avatar)
  protected ImageView iv_avatar;

  @Bind(R.id.iv_fail_resend)
  protected ImageView iv_fail_resend;

  @Bind(R.id.tv_time)
  protected TextView tv_time;

  @Bind(R.id.tv_message)
  protected TextView tv_message;
  @Bind(R.id.tv_send_status)
  protected TextView tv_send_status;

  @Bind(R.id.progress_load)
  protected ProgressBar progress_load;

  BmobIMConversation c;

  public SendVideoHolder(Context context, ViewGroup root, BmobIMConversation c, OnRecyclerViewListener listener,View view) {
    super(context, root, listener,view);
    this.c =c;
  }

  @Override
  public void bindData(Object o) {
    final BmobIMMessage message = (BmobIMMessage)o;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    final BmobIMUserInfo info = message.getBmobIMUserInfo();
    ImageLoaderFactory.getLoader().loadAvator(iv_avatar,info != null ? info.getAvatar() : null, R.drawable.head);

    String time = dateFormat.format(message.getCreateTime());
    String content = message.getContent();
    tv_message.setText("���͵���Ƶ�ļ���"+content);
    tv_time.setText(time);

    int status =message.getSendStatus();
    if (status == BmobIMSendStatus.SENDFAILED.getStatus()) {
      iv_fail_resend.setVisibility(View.VISIBLE);
      progress_load.setVisibility(View.GONE);
    } else if (status== BmobIMSendStatus.SENDING.getStatus()) {
      iv_fail_resend.setVisibility(View.GONE);
      progress_load.setVisibility(View.VISIBLE);
    } else {
      iv_fail_resend.setVisibility(View.GONE);
      progress_load.setVisibility(View.GONE);
    }

    tv_message.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        toast("���"+message.getContent());
        if(onRecyclerViewListener!=null){
          onRecyclerViewListener.onItemClick(getPosition());
        }
      }
    });

    tv_message.setOnLongClickListener(new View.OnLongClickListener() {
      @Override
      public boolean onLongClick(View v) {
        if (onRecyclerViewListener != null) {
          onRecyclerViewListener.onItemLongClick(getPosition());
        }
        return true;
      }
    });

    iv_avatar.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        toast("���" + info.getName() + "��ͷ��");
      }
    });

    //�ط�
    iv_fail_resend.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        c.resendMessage(message, new MessageSendListener() {
          @Override
          public void onStart(BmobIMMessage msg) {
            progress_load.setVisibility(View.VISIBLE);
            iv_fail_resend.setVisibility(View.GONE);
            tv_send_status.setVisibility(View.INVISIBLE);
          }

          @Override
          public void done(BmobIMMessage msg, BmobException e) {
            if(e==null){
              tv_send_status.setVisibility(View.VISIBLE);
              tv_send_status.setText("�ѷ���");
              iv_fail_resend.setVisibility(View.GONE);
              progress_load.setVisibility(View.GONE);
            }else{
              iv_fail_resend.setVisibility(View.VISIBLE);
              progress_load.setVisibility(View.GONE);
              tv_send_status.setVisibility(View.INVISIBLE);
            }
          }
        });
      }
    });
  }

  public void showTime(boolean isShow) {
    tv_time.setVisibility(isShow ? View.VISIBLE : View.GONE);
  }
}
