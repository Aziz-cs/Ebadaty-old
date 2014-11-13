package islamic.ebadaty;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ReportBroadcast extends BroadcastReceiver {
	public static boolean phoneSwitched;
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		phoneSwitched = true;
        Intent startServiceIntent = new Intent(context, ReportService.class);
        context.startService(startServiceIntent);
		
	}

}
