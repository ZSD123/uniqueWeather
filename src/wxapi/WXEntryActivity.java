package wxapi;

import rx.internal.util.UtilityFunctions;

import com.koushikdutta.async.Util;
import com.sharefriend.app.R;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXTextObject;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import Util.Utility;
import activity.fragmentChat;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler{

    
	private final String APP_ID="wx7cd3e1850b8efb17" ;
	private IWXAPI api;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.weixinshare);
		
		
		api=WXAPIFactory.createWXAPI(this, APP_ID,true);
		api.registerApp(APP_ID);
		api.handleIntent(getIntent(), this);
		
		
		WXWebpageObject webpage=new WXWebpageObject();
		webpage.webpageUrl="http://sharefriend.bmob.site/";
		
		WXMediaMessage msg=new WXMediaMessage(webpage);
		msg.title="朋友，我在等你哦";
		msg.description="这里有个好玩的app，一起来玩吧";
		Bitmap thumb=BitmapFactory.decodeResource(getResources(), R.drawable.log);
		msg.thumbData=Utility.Bitmap2Bytes(thumb);
		
		
		SendMessageToWX.Req req=new SendMessageToWX.Req();
		req.transaction=buildTransaction("webpage");
		req.message=msg;
		
		req.scene=SendMessageToWX.Req.WXSceneTimeline;
		
		api.sendReq(req);
		finish();
	}
	

	@Override
	protected void onNewIntent(Intent intent) {
		
		super.onNewIntent(intent);
		 setIntent(intent);
	     api.handleIntent(intent, this);
	}


	@Override
	public void onReq(BaseReq arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onResp(BaseResp resp) {
		String result;
        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                result = "分享成功";
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                result = "取消分享";
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                result = "分享被拒绝";
                break;
            default:
                result = "发送返回";
                break;
        }
        Log.d("Main","result="+result);
        Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
        finish();

	
	}
	private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }
	


       

}
