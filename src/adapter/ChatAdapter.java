package adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.uniqueweather.app.R;

import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMMessageType;
import cn.bmob.v3.BmobUser;

/**
 * @author :smile
 * @project:ChatAdapter
 * @date :2016-01-22-14:18
 */
public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    //�ı�
    private  final int TYPE_RECEIVER_TXT = 0;
    private final int TYPE_SEND_TXT = 1;
    //ͼƬ
    private final int TYPE_SEND_IMAGE = 2;
    private final int TYPE_RECEIVER_IMAGE = 3;
    //λ��
    private final int TYPE_SEND_LOCATION = 4;
    private final int TYPE_RECEIVER_LOCATION = 5;
    //����
    private final int TYPE_SEND_VOICE =6;
    private final int TYPE_RECEIVER_VOICE = 7;
    //��Ƶ
    private final int TYPE_SEND_VIDEO =8;
    private final int TYPE_RECEIVER_VIDEO = 9;

    //ͬ����Ӻ��ѳɹ������ʽ
    private int TYPE_AGREE = 10;
    private Context mContext;
    /**
     * ��ʾʱ����:10����
     */
    private final long TIME_INTERVAL = 10 * 60 * 1000;
    
    private List<BmobIMMessage> msgs = new ArrayList<BmobIMMessage>();

    private String currentUid="";
    BmobIMConversation c;

    public ChatAdapter(Context context,BmobIMConversation c) {
        try {
            currentUid = BmobUser.getCurrentUser().getObjectId();
            mContext=context;
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.c =c;
       
    }
    public int findPosition(BmobIMMessage message) {
        int index = this.getCount();
        int position = -1;
        while(index-- > 0) {
            if(message.equals(this.getItem(index))) {
                position = index;
                break;
            }
        }
        return position;
    }

    public int findPosition(long id) {
        int index = this.getCount();
        int position = -1;
        while(index-- > 0) {
            if(this.getItemId(index) == id) {
                position = index;
                break;
            }
        }
        return position;
    }

    public int getCount() {
        return this.msgs == null?0:this.msgs.size();
    }

    public void addMessages(List<BmobIMMessage> messages) {
        msgs.addAll(0, messages);
        notifyDataSetChanged();
    }

    public void addMessage(BmobIMMessage message) {
        msgs.addAll(Arrays.asList(message));
        notifyDataSetChanged();
    }

    /**��ȡ��Ϣ
     * @param position
     * @return
     */
    public BmobIMMessage getItem(int position){
        return this.msgs == null?null:(position >= this.msgs.size()?null:this.msgs.get(position));
    }

    /**�Ƴ���Ϣ
     * @param position
     */
    public void remove(int position){
        msgs.remove(position);
        notifyDataSetChanged();
    }

    public BmobIMMessage getFirstMessage() {
        if (null != msgs && msgs.size() > 0) {
            return msgs.get(0);
        } else {
            return null;
        }
    }
    public List<BmobIMMessage> getMessages(){
    	  if (null != msgs && msgs.size() > 0) {
              return msgs;
          } else {
              return null;
          }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_SEND_TXT) {
            return new SendTextHolder(parent.getContext(), parent,c,onRecyclerViewListener,LayoutInflater.from(mContext).inflate(R.layout.item_chat_sent_message,parent,false));//�����Ƿ����ı�������
        } else if (viewType == TYPE_SEND_IMAGE) {
            return new SendImageHolder(parent.getContext(), parent,c,onRecyclerViewListener,LayoutInflater.from(mContext).inflate(R.layout.item_chat_sent_image,parent,false));
        }else if (viewType == TYPE_SEND_VOICE) {
            return new SendVoiceHolder(parent.getContext(), parent,c,onRecyclerViewListener,LayoutInflater.from(mContext).inflate(R.layout.item_chat_sent_voice,parent,false));
        } else if (viewType == TYPE_RECEIVER_TXT) {
            return new ReceiveTextHolder(parent.getContext(), parent,onRecyclerViewListener,LayoutInflater.from(mContext).inflate(R.layout.item_chat_received_message,parent,false));
        } else if (viewType == TYPE_RECEIVER_IMAGE) {
            return new ReceiveImageHolder(parent.getContext(), parent,onRecyclerViewListener,LayoutInflater.from(mContext).inflate(R.layout.item_chat_received_image,parent,false));
        } else if (viewType == TYPE_RECEIVER_VOICE) {
            return new ReceiveVoiceHolder(parent.getContext(), parent,onRecyclerViewListener,LayoutInflater.from(mContext).inflate(R.layout.item_chat_received_voice,parent,false));
        } else if (viewType == TYPE_SEND_VIDEO) {
            return new SendVideoHolder(parent.getContext(), parent,c,onRecyclerViewListener,LayoutInflater.from(mContext).inflate(R.layout.item_chat_sent_video,parent,false));
        } else if (viewType == TYPE_RECEIVER_VIDEO) {
            return new ReceiveVideoHolder(parent.getContext(), parent,onRecyclerViewListener,LayoutInflater.from(mContext).inflate(R.layout.item_chat_received_video,parent,false));
        }else if(viewType ==TYPE_AGREE) {
            return new AgreeHolder(parent.getContext(),parent,onRecyclerViewListener,LayoutInflater.from(mContext).inflate(R.layout.item_chat_agree,parent,false));
        }else{//�������Զ�����������ͣ������д���
            return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((BaseViewHolder)holder).bindData(msgs.get(position));
        if (holder instanceof ReceiveTextHolder) {
            ((ReceiveTextHolder)holder).showTime(shouldShowTime(position));
        } else if (holder instanceof ReceiveImageHolder) {
            ((ReceiveImageHolder)holder).showTime(shouldShowTime(position));
        }else if (holder instanceof ReceiveVoiceHolder) {
            ((ReceiveVoiceHolder)holder).showTime(shouldShowTime(position));
        }else if (holder instanceof SendTextHolder) {
            ((SendTextHolder)holder).showTime(shouldShowTime(position));
        }else if (holder instanceof SendImageHolder) {
            ((SendImageHolder)holder).showTime(shouldShowTime(position));
        }else if (holder instanceof SendVoiceHolder) {
            ((SendVoiceHolder)holder).showTime(shouldShowTime(position));
        }else if (holder instanceof SendVideoHolder) {//���ģ�����Ƶ����
            ((SendVideoHolder)holder).showTime(shouldShowTime(position));
        }else if (holder instanceof ReceiveVideoHolder) {
            ((ReceiveVideoHolder)holder).showTime(shouldShowTime(position));
        }else if (holder instanceof AgreeHolder) {//ͬ����Ӻ��ѳɹ������Ϣ
            ((AgreeHolder)holder).showTime(shouldShowTime(position));
        }
    }

    @Override
    public int getItemViewType(int position) {
        BmobIMMessage message = msgs.get(position);
        if(message.getMsgType().equals(BmobIMMessageType.IMAGE.getType())){
            return message.getFromId().equals(currentUid) ? TYPE_SEND_IMAGE: TYPE_RECEIVER_IMAGE;
        }else if(message.getMsgType().equals(BmobIMMessageType.LOCATION.getType())){
            return message.getFromId().equals(currentUid) ? TYPE_SEND_LOCATION: TYPE_RECEIVER_LOCATION;
        }else if(message.getMsgType().equals(BmobIMMessageType.VOICE.getType())){
            return message.getFromId().equals(currentUid) ? TYPE_SEND_VOICE: TYPE_RECEIVER_VOICE;
        }else if(message.getMsgType().equals(BmobIMMessageType.TEXT.getType())){
            return message.getFromId().equals(currentUid) ? TYPE_SEND_TXT: TYPE_RECEIVER_TXT;
        }else if(message.getMsgType().equals(BmobIMMessageType.VIDEO.getType())){
            return message.getFromId().equals(currentUid) ? TYPE_SEND_VIDEO: TYPE_RECEIVER_VIDEO;
        }else if(message.getMsgType().equals("agree")) {//��ʾ��ӭ
            return TYPE_AGREE;
        }else{
            return -1;
        }
    }

    @Override
    public int getItemCount() {
        return msgs.size();
    }

    private OnRecyclerViewListener onRecyclerViewListener;

    public void setOnRecyclerViewListener(OnRecyclerViewListener onRecyclerViewListener) {
        this.onRecyclerViewListener = onRecyclerViewListener;
    }

    private boolean shouldShowTime(int position) {
        if (position == 0) {
            return true;
        }
        long lastTime = msgs.get(position - 1).getCreateTime();
        long curTime = msgs.get(position).getCreateTime();
        return curTime - lastTime > TIME_INTERVAL;
    }
}
