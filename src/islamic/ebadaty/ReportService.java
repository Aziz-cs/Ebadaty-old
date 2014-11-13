package islamic.ebadaty;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class ReportService extends Service {
	static NotificationManager notification;
	Notification notify;
	public static AlarmManager alarmManager;
	public static int notificationDay = 0;
	public static PendingIntent pendingIntent;
	public static boolean MainAlarmState;
	boolean ReportState;
	Intent intentService;
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

	}
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();

		stopSelf();
	}
	
	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
		alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
		intentService = new Intent(this, ReportService.class);
	    pendingIntent = PendingIntent.getService(this, 0, intent, 0);
		
		SharedPreferences reportState = getSharedPreferences("STATE", 0);
		ReportState = reportState.getBoolean("REPORTSTATE", false);
		if(ReportState == true && ReportBroadcast.phoneSwitched == true){
			alarmStartUp();
			Log.i("ALARM", "if condition ok");
		} else if(ReportState == true) {
			Log.i("ALARM", "if condition false");
		showNotification();
		}
		Log.i("Report", "selected days " + Main.getUserSelectedDayNumber());
		SharedPreferences SelectedDays = getSharedPreferences("SelectedDays", 0);
		int selectedDays = SelectedDays.getInt("SELECTEDDAYS", 3);
		
		SharedPreferences MainAlarm = getSharedPreferences("AlarmService", 0);
		MainAlarmState = MainAlarm.getBoolean("ALARM_STATE", false);
		
		if(notificationDay == 3 && selectedDays == 3){
			if(MainAlarmState){
				Main.stopReportNotification();
			}else{
				stopReportNotificationBroadCast();
			}
		}
		else if(notificationDay == 5 && selectedDays == 5){
			if(MainAlarmState){
				Main.stopReportNotification();
			}else{
				stopReportNotificationBroadCast();
			}
		}
		else if(notificationDay == 7 && selectedDays== 7){
			if(MainAlarmState){
				Main.stopReportNotification();
			}else{
				stopReportNotificationBroadCast();
			}
		}
		stopSelf();
	}

	
	public void showNotification(){
		SharedPreferences notifyData = getSharedPreferences("notify", 0);
		notificationDay = notifyData.getInt("notifyKey", 0);
		SharedPreferences.Editor notifyDataEdit = notifyData.edit();
		notificationDay++;
		notifyDataEdit.putInt("notifyKey", notificationDay);
		notifyDataEdit.commit();
		
		notification = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		
		notify = new Notification(R.drawable.notify, getResources().getString(R.string.notification), System.currentTimeMillis());
		notify.flags = Notification.FLAG_AUTO_CANCEL;
		notify.defaults = Notification.DEFAULT_ALL  ;
		//notify.sound = Uri.parse("android.resource//com.example.motaba3a/" + R.);
		
		Context context = ReportService.this;
		CharSequence title = getResources().getString(R.string.notify_title) + " " + notificationDay;
		CharSequence details = getResources().getString(R.string.notify_details);
		Intent notifyIntent = new Intent(Intent.ACTION_MAIN);
		notifyIntent.setClass(ReportService.this, ReportPreferences.class);
		
		PendingIntent pending = PendingIntent.getActivity(context, 0, notifyIntent, 0);


		notify.setLatestEventInfo(context, title, details, pending);
		notification.notify(notificationDay, notify);

	}
	
	public static void clearNotification(){
		try{
		notification.cancelAll();
		}catch (Exception e) {
			// TODO: handle exception
		}
	}



	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		Toast.makeText(this, "MyAlarmService.bind()", Toast.LENGTH_LONG).show();
		return null;
	}

	
	public void alarmStartUp(){
		Log.i("ALARM", "method");
		Long startingNewTime = 0L;
		intentService = new Intent(this, ReportService.class);
	    pendingIntent = PendingIntent.getService(this, 0, intentService, 0);
		SharedPreferences time = getSharedPreferences("TIME", 0);
		long alarmTime = time.getLong("timeC", 0);

		SharedPreferences notifyData = getSharedPreferences("notify", 0);
		int notificationDay = notifyData.getInt("notifyKey", 0);
	
		SharedPreferences MainAlarm = getSharedPreferences("AlarmService", 0);
		SharedPreferences.Editor MainAlarmEditor = MainAlarm.edit();
		MainAlarmEditor.clear();
		MainAlarmEditor.putBoolean("MAIN_ALARM", false);
		MainAlarmEditor.commit();
		
		
		switch (notificationDay) {
		case 1:
			startingNewTime = (alarmTime + AlarmManager.INTERVAL_DAY);
			break;
		case 2:
			startingNewTime = (alarmTime + AlarmManager.INTERVAL_DAY*2);
			break;
		case 3:
			startingNewTime = (alarmTime + AlarmManager.INTERVAL_DAY*3);
			break;
		case 4:
			startingNewTime = (alarmTime + AlarmManager.INTERVAL_DAY*4);
			break;
		case 5:
			startingNewTime = (alarmTime + AlarmManager.INTERVAL_DAY*5);
			break;
		case 6:
			startingNewTime = (alarmTime + AlarmManager.INTERVAL_DAY*6);
			break;
		case 7:
			startingNewTime = (alarmTime + AlarmManager.INTERVAL_DAY*7);
			break;
		default:
			break;
		}
		
		alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
		alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, startingNewTime, AlarmManager.INTERVAL_DAY, pendingIntent);
		ReportState = false;
		ReportBroadcast.phoneSwitched = false;
		stopSelf();
	}
	
	 public static void stopReportNotificationBroadCast(){
		 try{
	    		alarmManager.cancel(ReportService.pendingIntent);
		 }catch (Exception e) {
			// TODO: handle exception
		}
		     ReportBroadcast.phoneSwitched = false;
		     
		     Main.state = false;
	   }
	    
}

