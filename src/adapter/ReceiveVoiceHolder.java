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
import cn.bmob.newim.bean.BmobIMAudioMessage;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.core.BmobDownloadManager;
import cn.bmob.newim.listener.FileDownloadListener;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;

/**
 * ���յ����ı�����
 */
public class ReceiveVoiceHolder extends BaseViewHolder {

  @Bind(R.id.iv_avatar)
  protected ImageView iv_avatar;

  @Bind(R.id.tv_time)
  protected TextView tv_time;

  @Bind(R.id.tv_voice_length)
  protected TextView tv_voice_length;
  @Bind(R.id.iv_voice)
  protected ImageView iv_voice;

  @Bind(R.id.progress_load)
  protected ProgressBar progress_load;

  private String currentUid="";

  public ReceiveVoiceHolder(Context context, ViewGroup root,OnRecyclerViewListener onRecyclerViewListener,View view) {
    super(context, root,onRecyclerViewListener,view);
    try {
      currentUid = BmobUser.getCurrentUser().getObjectId();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void bindData(Object o) {
    BmobIMMessage msg = (BmobIMMessage)o;
    //�û���Ϣ�Ļ�ȡ������buildFromDB֮ǰ������ᱨ��'Entity is detached from DAO context'
    final BmobIMUserInfo info = msg.getBmobIMUserInfo();
    ImageLoaderFactory.getLoader().loadAvator(iv_avatar,info != null ? info.getAvatar() : null, R.drawable.head);
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy��MM��dd�� HH:mm");
    String time = dateFormat.format(msg.getCreateTime());
    tv_time.setText(time);
    iv_avatar.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        toast("���" + info.getName() + "��ͷ��");
      }
    });
    //��ʾ��������
    final BmobIMAudioMessage message = BmobIMAudioMessage.buildFromDB(false, msg);
    boolean isExists = BmobDownloadManager.isAudioExist(currentUid, message);
    if(!isExists){//��ָ����ʽ��¼���ļ������ڣ�����Ҫ���أ���Ϊ���ļ��Ƚ�С���ʷ��ڴ�����
        BmobDownloadManager downloadTask = new BmobDownloadManager(getContext(),msg,new FileDownloadListener() {

          @Override
          public void onStart() {
              progress_load.setVisibility(View.VISIBLE);
              tv_voice_length.setVisibility(View.GONE);
              iv_voice.setVisibility(View.INVISIBLE);//ֻ��������ɲ���ʾ���ŵİ�ť
          }

          @Override
          public void done(BmobException e) {
            if(e==null){
                progress_load.setVisibility(View.GONE);
                tv_voice_length.setVisibility(View.VISIBLE);
                tv_voice_length.setText(message.getDuration()+"\''");
                iv_voice.setVisibility(View.VISIBLE);
            }else{
                progress_load.setVisibility(View.GONE);
                tv_voice_length.setVisibility(View.GONE);
                iv_voice.setVisibility(View.INVISIBLE);
            }
          }
        });
        downloadTask.execute(message.getContent());
    }else{
        tv_voice_length.setVisibility(View.VISIBLE);
        tv_voice_length.setText(message.getDuration() + "\''");
    }
    iv_voice.setOnClickListener(new NewRecordPlayClickListener(getContext(), message, iv_voice));

    iv_voice.setOnLongClickListener(new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            if (onRecyclerViewListener != null) {
                 onRecyclerViewListener.onItemLongClick(getPosition());
            }
            return true;
        }
    });

  }

  public void showTime(boolean isShow) {
    tv_time.setVisibility(isShow ? View.VISIBLE : View.GONE);
  }
}