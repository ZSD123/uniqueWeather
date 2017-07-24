package activity;

import com.uniqueweather.app.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

public class mapActivity extends baseFragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.mapfragment);
	}

	@Override
	protected void onDestroy() {
		
		super.onDestroy();
		fragmentMap.yongbDb.deleteAll();
	}
     
}
