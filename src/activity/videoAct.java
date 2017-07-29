package activity;


import com.uniqueweather.app.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.VideoView;

public class videoAct extends Activity {
	 private VideoView videoView;
	 MediaController mMediaCtrl;
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.video_view);
		String path=getIntent().getStringExtra("path");
	    videoView= (VideoView) findViewById(R.id.video_view);
	    
	      mMediaCtrl = new MediaController(videoAct.this,false);
	      mMediaCtrl.setAnchorView(videoView);
	      mMediaCtrl.setMediaPlayer(videoView);
	      videoView.setMediaController(mMediaCtrl);
	      videoView.requestFocus();
	      videoView.setVideoPath(path);
	      videoView.start();
	}
     
}  
