package receiver;

import service.autoUqdateService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class uqdateReceive extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Intent intent1=new Intent(context,autoUqdateService.class);
        context.startService(intent1);
	}

}
