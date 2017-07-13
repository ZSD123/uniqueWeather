package activity;

import Util.CommonUtils;
import Util.FaceTextUtils;
import adapter.ChatAdapter;
import adapter.EmoViewPagerAdapter;
import adapter.EmoteAdapter;
import adapter.OnRecyclerViewListener;
import android.R.integer;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.ContactsContract.Contacts.Data;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import butterknife.Bind;
import butterknife.OnClick;


import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import myCustomView.EmoticonsEditText;


import com.koushikdutta.async.Util;
import com.uniqueweather.app.R;


import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMAudioMessage;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMFileMessage;
import cn.bmob.newim.bean.BmobIMImageMessage;
import cn.bmob.newim.bean.BmobIMLocationMessage;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMTextMessage;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.bean.BmobIMVideoMessage;
import cn.bmob.newim.core.BmobIMClient;
import cn.bmob.newim.core.BmobRecordManager;
import cn.bmob.newim.event.MessageEvent;
import cn.bmob.newim.listener.MessageListHandler;
import cn.bmob.newim.listener.MessageSendListener;
import cn.bmob.newim.listener.MessagesQueryListener;
import cn.bmob.newim.listener.ObseverListener;
import cn.bmob.newim.listener.OnRecordChangeListener;
import cn.bmob.newim.notification.BmobNotificationManager;
import cn.bmob.push.config.Constant;
import cn.bmob.v3.exception.BmobException;
import db.FaceText;

/**�������
 * @author :smile
 * @project:ChatActivity
 * @date :2016-01-25-18:23
 */
public class ChatActivity extends baseFragmentActivity implements ObseverListener,MessageListHandler{

    private static final List<View> listViews = null;

	LinearLayout ll_chat;

    SwipeRefreshLayout sw_refresh;

    RecyclerView rc_view;
    EmoticonsEditText edit_msg;

    Button btn_chat_add;
    Button btn_chat_emo;

    Button btn_speak;

    Button btn_chat_voice;

    Button btn_chat_keyboard;
    
    Button btn_chat_keyboard1;

    Button btn_chat_send;


    LinearLayout layout_more;
     
    String path;
    
    LinearLayout layout_add;
    LinearLayout layout_emo;

    // �����й�

    RelativeLayout layout_record;
 
    TextView tv_voice_tips;

    ImageView iv_record;
    TextView tv_picture;
    TextView tv_camera;
    
    ViewPager emoPager;
    TextView talkpartername;

   
    private Drawable[] drawable_Anims;// ��Ͳ����
    BmobRecordManager recordManager;
    
    ChatAdapter adapter;
    protected LinearLayoutManager layoutManager;
    BmobIMConversation c;
    
    
    
    class VoiceTouchListener implements View.OnTouchListener {
        @Override   
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (!CommonUtils.checkSdCard()) {
                        Toast.makeText(ChatActivity.this, "����������Ҫsdcard֧�֣�", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    try {
                        v.setPressed(true);
                        layout_record.setVisibility(View.VISIBLE);
                        tv_voice_tips.setText(getString(R.string.voice_cancel_tips));
                        // ��ʼ¼��
                        recordManager.startRecording(c.getConversationId());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return true;
                case MotionEvent.ACTION_MOVE: {
                    if (event.getY() < 0) {
                        tv_voice_tips.setText(getString(R.string.voice_cancel_tips));
                        tv_voice_tips.setTextColor(Color.RED);
                    } else {
                        tv_voice_tips.setText(getString(R.string.voice_up_tips));
                        tv_voice_tips.setTextColor(Color.WHITE);
                    }
                    return true;
                }
                case MotionEvent.ACTION_UP:
                    v.setPressed(false);
                    layout_record.setVisibility(View.INVISIBLE);
                    try {
                        if (event.getY() < 0) {// ����¼��
                            recordManager.cancelRecording();
                            Log.i("voice", "������������");
                        } else {
                            int recordTime = recordManager.stopRecording();
                            if (recordTime > 1) {
                                // ���������ļ�
                                sendVoiceMessage(recordManager.getRecordFilePath(c.getConversationId()),recordTime);
                            } else {// ¼��ʱ����̣�����ʾ¼�����̵���ʾ
                                layout_record.setVisibility(View.GONE);
                                showShortToast().show();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return true;
                default:
                    return false;
            }
        }
    }


 
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_chat);
        edit_msg=(EmoticonsEditText)findViewById(R.id.edit_user_comment);
        ll_chat=(LinearLayout)findViewById(R.id.ll_chat);
        sw_refresh=(SwipeRefreshLayout)findViewById(R.id.sw_refresh);
        rc_view=(RecyclerView)findViewById(R.id.rc_view);
        btn_chat_add=(Button)findViewById(R.id.btn_chat_add);
        btn_chat_emo=(Button)findViewById(R.id.btn_chat_emo);
        btn_speak=(Button)findViewById(R.id.btn_speak);
        btn_chat_voice=(Button)findViewById(R.id.btn_chat_voice);
        btn_chat_keyboard=(Button)findViewById(R.id.btn_chat_keyboard);
        btn_chat_keyboard1=(Button)findViewById(R.id.btn_chat_keyboard1);
        btn_chat_send=(Button)findViewById(R.id.btn_chat_send);
        layout_more=(LinearLayout)findViewById(R.id.layout_more);
        layout_add=(LinearLayout)findViewById(R.id.layout_add);
        
        layout_emo=(LinearLayout)findViewById(R.id.layout_emo);
        layout_record=(RelativeLayout)findViewById(R.id.layout_record);
        tv_voice_tips=(TextView)findViewById(R.id.tv_voice_tips);
        iv_record=(ImageView)findViewById(R.id.iv_record);
        tv_picture=(TextView)findViewById(R.id.tv_picture);
        tv_camera=(TextView)findViewById(R.id.tv_camera);
        emoPager=(ViewPager)findViewById(R.id.pager_emo);
        talkpartername=(TextView)findViewById(R.id.talkpartername);
        
        c= BmobIMConversation.obtain(BmobIMClient.getInstance(), (BmobIMConversation) getIntent().getBundleExtra("bundle").getSerializable("c"));
        BmobIMUserInfo userInfo=(BmobIMUserInfo)getIntent().getBundleExtra("bundle").getSerializable("userInfo");   
        talkpartername.setText(userInfo.getName());
        
      // initNaviView();
        initEmoView();
        initSwipeLayout();
        initVoiceView();   
        initBottomView();
        edit_msg.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				 if (layout_more.getVisibility() == View.VISIBLE) {
			            layout_add.setVisibility(View.GONE);
			            layout_emo.setVisibility(View.GONE);
			            layout_more.setVisibility(View.GONE);
			        }
				
			}
		});
        
        btn_chat_emo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				btn_chat_keyboard1.setVisibility(View.GONE);
				 if (layout_more.getVisibility() == View.GONE) {
			            showEditState(true);
			        } else {
			            if (layout_add.getVisibility() == View.VISIBLE) {
			                layout_add.setVisibility(View.GONE);
			                layout_emo.setVisibility(View.VISIBLE);
			            } else {
			                layout_more.setVisibility(View.GONE);
			            }
			        }
			}
		});
        
        btn_chat_add.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				edit_msg.setVisibility(View.VISIBLE);
				btn_speak.setVisibility(View.GONE);
				  if (layout_more.getVisibility() == View.GONE) {
			            layout_more.setVisibility(View.VISIBLE);
			            layout_add.setVisibility(View.VISIBLE);
			            layout_emo.setVisibility(View.GONE);
			            hideSoftInputView();
			        } else {
			            if (layout_emo.getVisibility() == View.VISIBLE) {
			                layout_emo.setVisibility(View.GONE);
			                layout_add.setVisibility(View.VISIBLE);
			            } else {
			                layout_more.setVisibility(View.GONE);
			            }
			        }
			}
		});
        btn_chat_voice.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				   edit_msg.setVisibility(View.GONE);
			        layout_more.setVisibility(View.GONE);
			        btn_chat_voice.setVisibility(View.GONE);
			        btn_chat_keyboard1.setVisibility(View.VISIBLE);
			        btn_speak.setVisibility(View.VISIBLE);
			        hideSoftInputView();
			}
		});
        btn_chat_keyboard.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				 showEditState(false);
				 btn_chat_emo.setVisibility(View.VISIBLE);
			}
		});
        btn_chat_keyboard1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				 showEditState(false);
				 btn_chat_keyboard1.setVisibility(View.GONE);
				 
			}
		});
        
        btn_chat_send.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				 sendMessage();
				
			}
		});
        tv_picture.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				sendLocalImageMessage();
				
			}
		});
        tv_camera.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				openTakePhoto();
				
			}
		});
       
        
    }
    List<FaceText> emos;
    private void initEmoView() {
		emoPager = (ViewPager) findViewById(R.id.pager_emo);  //emoPagerʵ����
		emos = FaceTextUtils.faceTexts;   //���������б�����Ӧ���ַ�����

		List<View> views = new ArrayList<View>();
		for (int i = 0; i < 2; ++i) {
			views.add(getGridView(i));
		}
		emoPager.setAdapter(new EmoViewPagerAdapter(views));
	}
    
    private View getGridView(final int i) {
		View view = View.inflate(this, R.layout.include_emo_gridview, null);
		GridView gridview = (GridView) view.findViewById(R.id.gridview);
		List<FaceText> list = new ArrayList<FaceText>();
		if (i == 0) {
			list.addAll(emos.subList(0, 21));
		} else if (i == 1) {
			list.addAll(emos.subList(21, emos.size()));
		}
		final EmoteAdapter gridAdapter = new EmoteAdapter(ChatActivity.this,
				list);
		gridview.setAdapter(gridAdapter);  
		gridview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				FaceText name = (FaceText) gridAdapter.getItem(position);
				String key = name.text.toString();
				try {
					if (edit_msg != null && !TextUtils.isEmpty(key)) {
						int start = edit_msg.getSelectionStart();
						CharSequence content = edit_msg.getText()
								.insert(start, key);
						edit_msg.setText(content);
						// ��λ���λ��
						CharSequence info = edit_msg.getText();
						if (info instanceof Spannable) {
							Spannable spanText = (Spannable) info;
							Selection.setSelection(spanText,
									start + key.length());
						}
					}
				} catch (Exception e) {

				}

			}
		});
		return view;
	}
    
    
    private void openTakePhoto(){    
    	 /**
    	 * ����������֮ǰ������ж�һ��sdcard�Ƿ����
    	 */
    	 String state = Environment.getExternalStorageState(); //�õ�sdcard�Ƿ���õ�״̬��
    	 if (state.equals(Environment.MEDIA_MOUNTED)){   //�������
    	  Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
    	  
    	  SimpleDateFormat format=new SimpleDateFormat("yyyyMMddHHmmss");
    	  Date curDate=new Date(System.currentTimeMillis());
    	  String str=format.format(curDate);
    	  
    	  path=Environment.getExternalStorageDirectory().getAbsoluteFile()+"/EndRain/"+str+".jpg";
    	  Uri uri=Uri.fromFile(new File(path));
    	  intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
    	  startActivityForResult(intent,3);
    	 }else {
    	  Toast.makeText(ChatActivity.this,"sdcard������",Toast.LENGTH_SHORT).show();
    	 }
    	}
    private void initSwipeLayout(){
        sw_refresh.setEnabled(true);
        layoutManager = new LinearLayoutManager(this);
        rc_view.setLayoutManager(layoutManager);
        adapter = new ChatAdapter(this,c);
        rc_view.setAdapter(adapter);
        ll_chat.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                ll_chat.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                sw_refresh.setRefreshing(true);
                //�Զ�ˢ��
                queryMessages(null);
            }
        });
        //��������
        sw_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                BmobIMMessage msg = adapter.getFirstMessage();
                queryMessages(msg);
            }
        });
        //����RecyclerView�ĵ���¼�
        adapter.setOnRecyclerViewListener(new OnRecyclerViewListener() {
            @Override
            public void onItemClick(int position) {
                Log.i("Main",""+position);
            }

            @Override
            public boolean onItemLongClick(int position) {
                //����ʡ�˸�����ֱ�ӳ�����ɾ���˸���Ϣ
                c.deleteMessage(adapter.getItem(position));
                adapter.remove(position);
                return true;
            }
        });
    }

    private void initBottomView(){
        edit_msg.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()==MotionEvent.ACTION_DOWN||event.getAction()==MotionEvent.ACTION_UP){
                    scrollToBottom();
                }
                return false;
            }
        });
        edit_msg.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                scrollToBottom();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s)) {
                    btn_chat_send.setVisibility(View.VISIBLE);
                    btn_chat_keyboard.setVisibility(View.GONE);
                    btn_chat_add.setVisibility(View.GONE);
                } else {
                  
                        btn_chat_voice.setVisibility(View.VISIBLE);
                        btn_chat_send.setVisibility(View.GONE);
                        btn_chat_keyboard.setVisibility(View.GONE);
                        btn_chat_add.setVisibility(View.VISIBLE);
                    
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    /**
     * ��ʼ����������
     * @param
     * @return void
     */
    private void initVoiceView() {
        btn_speak.setOnTouchListener(new VoiceTouchListener());
        initVoiceAnimRes();
        initRecordManager();
    }

    /**
     * ��ʼ������������Դ
     * @Title: initVoiceAnimRes
     * @param
     * @return void
     */
    private void initVoiceAnimRes() {
        drawable_Anims = new Drawable[] {
                getResources().getDrawable(R.drawable.chat_icon_voice2),
                getResources().getDrawable(R.drawable.chat_icon_voice3),
                getResources().getDrawable(R.drawable.chat_icon_voice4),
                getResources().getDrawable(R.drawable.chat_icon_voice5),
                getResources().getDrawable(R.drawable.chat_icon_voice6) };
    }

    private void initRecordManager(){
        // ������ع�����
        recordManager = BmobRecordManager.getInstance(this);
        // ����������С����--�����￪���߿����Լ�ʵ�֣���ʣ��10������µĸ��û�����ʾ������΢�ŵ���������
        recordManager.setOnRecordChangeListener(new OnRecordChangeListener() {

            @Override
            public void onVolumnChanged(int value) {
                iv_record.setImageDrawable(drawable_Anims[value]);
            }

            @Override
            public void onTimeChanged(int recordTime, String localPath) {
                Log.i("voice", "��¼������:" + recordTime);
                if (recordTime >= BmobRecordManager.MAX_RECORD_TIME) {// 1���ӽ�����������Ϣ
                    // ��Ҫ���ð�ť
                    btn_speak.setPressed(false);
                    btn_speak.setClickable(false);
                    // ȡ��¼����
                    layout_record.setVisibility(View.INVISIBLE);
                    // ����������Ϣ
                    sendVoiceMessage(localPath, recordTime);
                    //��Ϊ�˷�ֹ����¼��ʱ��󣬻�෢һ��������ȥ�������
                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            btn_speak.setClickable(true);
                        }
                    }, 1000);
                }
            }
        });
    }

    /**
     * ����˵��
     * @author smile
     * @date 2014-7-1 ����6:10:16
     */
    
    Toast toast;

    /**
     * ��ʾ¼��ʱ����̵�Toast
     * @Title: showShortToast
     * @return void
     */
    private Toast showShortToast() {
        if (toast == null) {
            toast = new Toast(this);
        }
        View view = LayoutInflater.from(this).inflate(
                R.layout.include_chat_voice_short, null);
        toast.setView(view);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        return toast;
    }


    /**
     * �����Ƿ���Ц������ʾ�ı�������״̬
     * @param  isEmo �����������ֺͱ���
     * @return void
     */
    private void showEditState(boolean isEmo) {
        edit_msg.setVisibility(View.VISIBLE);
        btn_chat_keyboard.setVisibility(View.GONE);
        btn_chat_voice.setVisibility(View.VISIBLE);
        btn_speak.setVisibility(View.GONE);
        edit_msg.requestFocus();
        if (isEmo) {
            layout_more.setVisibility(View.VISIBLE);
            layout_more.setVisibility(View.VISIBLE);
            layout_emo.setVisibility(View.VISIBLE);
            layout_add.setVisibility(View.GONE);
            hideSoftInputView();
        } else {
            layout_more.setVisibility(View.GONE);
            showSoftInputView();
        }
    }

    /**
     * ��ʾ�����
     */
    public void showSoftInputView() {
        if (getWindow().getAttributes().softInputMode == WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getCurrentFocus() != null)
                ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                        .showSoftInput(edit_msg, 0);
        }
    }

    /**
     * �����ı���Ϣ
     */
    private void sendMessage(){
        String text=edit_msg.getText().toString();
        if(TextUtils.isEmpty(text.trim())){
            Toast.makeText(ChatActivity.this, "����������",Toast.LENGTH_SHORT).show();
            return;
        }
        BmobIMTextMessage msg =new BmobIMTextMessage();
        msg.setContent(text);
        //�����ö�����Ϣ
        
  //      Map<String,Object> map =new HashMap<String, Object>();
    //    map.put("level",talkpartername);//����������Ϣ
      //  msg.setExtraMap(map);
        c.sendMessage(msg, listener);
        
    }

    /**
     * ֱ�ӷ���Զ��ͼƬ��ַ
     */
    public void sendRemoteImageMessage(){
        BmobIMImageMessage image =new BmobIMImageMessage();
        image.setRemoteUrl("http://img.lakalaec.com/ad/57ab6dc2-43f2-4087-81e2-b5ab5681642d.jpg");
        c.sendMessage(image, listener);
    }

    /**
     * ���ͱ���ͼƬ��ַ
     */
    public void sendLocalImageMessage(){
        //��������£���Ҫ����ϵͳ��ͼ������չ��ܻ�ȡ��ͼƬ�ı��ص�ַ��������ֻ��Ҫ�����ص��ļ���ַ����ȥ�Ϳ��Է����ļ����͵���Ϣ
    	Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/jpeg");
        startActivityForResult(intent, 2);
        
    }

    @Override
	protected void onActivityResult(int requestCode,int resultCode,Intent data) {
	     switch (requestCode) {
		case 2:
			if(resultCode==RESULT_OK){
				Uri uri=data.getData();
				String path=weather_info.getPath(ChatActivity.this, uri);
				 BmobIMImageMessage image =new BmobIMImageMessage(path);
			     c.sendMessage(image, listener);
			}
			break;
		case 3:
			if(resultCode==RESULT_OK){
				File file=new File(path);
				if(file.exists()){
					 BmobIMImageMessage image =new BmobIMImageMessage(path);
				     c.sendMessage(image, listener);
				}
			}
		default:
			break;
		}
	}

	/**
     * ����������Ϣ
     * @Title: sendVoiceMessage
     * @param  local
     * @param  length
     * @return void
     */
    private void sendVoiceMessage(String local, int length) {
        BmobIMAudioMessage audio =new BmobIMAudioMessage(local);
        //�����ö�����Ϣ-���������õĶ�����Ϣ����Ҫ�������Լ���extra��ȡ����
        Map<String,Object> map =new HashMap<String, Object>();
        map.put("from", "�ſ�");
        audio.setExtraMap(map);
        //���������ļ�ʱ������ѡ
//        audio.setDuration(length);
        c.sendMessage(audio, listener);
    }

    /**
     * ������Ƶ�ļ�
     */
    private void sendVideoMessage(){
        BmobIMVideoMessage video =new BmobIMVideoMessage("/storage/sdcard0/bimagechooser/11.png");
        c.sendMessage(video, listener);
    }

    /**
     * ���͵���λ��
     */
//    public void sendLocationMessage(){
        //�������ݣ���ʵ������Ҫ�ӵ�ͼSDK�л�ȡ
  //      BmobIMLocationMessage location =new BmobIMLocationMessage("���ݷ�خ��",23.5,112.0);
    //    Map<String,Object> map =new HashMap<String, Object>();
      //  map.put("from", "�ٶȵ�ͼ");
       // location.setExtraMap(map);
     //   c.sendMessage(location, listener);
   // }

    /**
     * ��Ϣ���ͼ�����
     */
    public MessageSendListener listener =new MessageSendListener() {

        @Override
        public void onProgress(int value) {
            super.onProgress(value);
            //�ļ����͵���Ϣ���н���ֵ
            Log.i("Main","onProgress��"+value);
        }

        @Override
        public void onStart(BmobIMMessage msg) {
            super.onStart(msg);
            adapter.addMessage(msg);
            edit_msg.setText("");
            scrollToBottom();
        }

        @Override
        public void done(BmobIMMessage msg, BmobException e) {
            adapter.notifyDataSetChanged();
            edit_msg.setText("");
            scrollToBottom();
            if (e != null) {
                Toast.makeText(ChatActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
            }else {
            	Log.d("Main", "���ͳɹ�");
            }
        }
    };

    /**�״μ��أ�������msgΪnull������ˢ�µ�ʱ��Ĭ��ȡ��Ϣ��ĵ�һ��msg��Ϊˢ�µ���ʼʱ��㣬Ĭ�ϰ�����Ϣʱ��Ľ�������
     * @param msg
     */
    public void queryMessages(BmobIMMessage msg){
        c.queryMessages(msg, 10, new MessagesQueryListener() {
            @Override
            public void done(List<BmobIMMessage> list, BmobException e) {
                sw_refresh.setRefreshing(false);
                if (e == null) {
                    if (null != list && list.size() > 0) {
                        adapter.addMessages(list);
                        layoutManager.scrollToPositionWithOffset(list.size() - 1, 0);
                    }
                } else {
                    Toast.makeText(ChatActivity.this, e.getMessage() + "(" + e.getErrorCode() + ")",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void scrollToBottom() {
        layoutManager.scrollToPositionWithOffset(adapter.getItemCount() - 1, 0);
    }

    @Override
    public void onMessageReceive(List<MessageEvent> list) {
    	
        Log.i("Main","����ҳ����յ���Ϣ��" + list.size());
        //��ע��ҳ����Ϣ����ʱ������Ϣ������������Ϣ������ʱ��ص��÷���
        for (int i=0;i<list.size();i++){
        
            addMessage2Chat(list.get(i));
        }
    }

//    /**���յ�������Ϣ
//     * @param event
//     */
//    @Subscribe
//    public void onEventMainThread(MessageEvent event){
//        addMessage2Chat(event);
//    }
//
//    @Subscribe
//    public void onEventMainThread(OfflineMessageEvent event){
//        Map<String,List<MessageEvent>> map =event.getEventMap();
//        if(map!=null&&map.size()>0){
//            //ֻ��ȡ��ǰ��������������Ϣ
//            List<MessageEvent> list = map.get(c.getConversationId());
//            if(list!=null && list.size()>0){
//                for (int i=0;i<list.size();i++){
//                    addMessage2Chat(list.get(i));
//                }
//            }
//        }
//    }

    /**�����Ϣ�����������
     * @param event
     */
    private void addMessage2Chat(MessageEvent event){
        BmobIMMessage msg =event.getMessage();
        if(c!=null && event!=null && c.getConversationId().equals(event.getConversation().getConversationId()) //����ǵ�ǰ�Ự����Ϣ
                && !msg.isTransient()){//���Ҳ�Ϊ��̬��Ϣ
            if(adapter.findPosition(msg)<0){//���δ��ӵ�������
                adapter.addMessage(msg);
                //���¸ûỰ������Ѷ�״̬
                c.updateReceiveStatus(msg);

            }
            scrollToBottom();
        }else{
            Log.i("Main","�����뵱ǰ����������Ϣ");
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (layout_more.getVisibility() == View.VISIBLE) {
                layout_more.setVisibility(View.GONE);
                return false;
            } else {
                return super.onKeyDown(keyCode, event);
            }
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    protected void onResume() {
        //�����ڼ���յ���δ����Ϣ��Ҫ��ӵ����������
        addUnReadMessage();
        //���ҳ����Ϣ������
        BmobIM.getInstance().addMessageListHandler(this);
        // �п��������ڼ䣬������������֪ͨ������ʱ����Ҫ���֪ͨ
        BmobNotificationManager.getInstance(this).cancelNotification();
        super.onResume();
    }

    /**
     * ���δ����֪ͨ����Ϣ���������
     */
    private void addUnReadMessage(){
        List<MessageEvent> cache = BmobNotificationManager.getInstance(this).getNotificationCacheList();
        if(cache.size()>0){
            int size =cache.size();
            for(int i=0;i<size;i++){
                MessageEvent event = cache.get(i);
                addMessage2Chat(event);
            }
        }
        scrollToBottom();
    }

    @Override
    protected void onPause() {
        //�Ƴ�ҳ����Ϣ������
        BmobIM.getInstance().removeMessageListHandler(this);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        //������Դ
        if(recordManager!=null){
            recordManager.clear();
        }
        //���´˻Ự��������ϢΪ�Ѷ�״̬
        if(c!=null){
            c.updateLocalCache();
        }
        hideSoftInputView();
        fragmentPart.refreshConversations();
        super.onDestroy();
    }

}
