package islamic.ebadaty;

import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

public class Main extends Activity implements OnClickListener {
	ReportPreferences myPref;
	public static AlarmManager alarmManager;
	public static PendingIntent pendingIntent;
	public static int numberOfReportSaved = 0;
	public static int SelectedDaysSpinner;
	public static int UserSelectedDays;
	public static boolean state = false;
	public static Context context;
	SharedPreferences ReportState;
	Button newReport, fada2el, howTo, exit;

//	 private InterstitialAd interstitial;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);    
        /*   	
    	AdView adView = (AdView)this.findViewById(R.id.adView1);
    	AdRequest adRequest = new AdRequest.Builder().build();
    	adView.loadAd(adRequest);
//     	AdRequest adRequest = new AdRequest.Builder().addTestDevice("39404A261727478EC8049EE6B039EABC").build();
    	
        // Create the interstitial.
        interstitial = new InterstitialAd(this);
        interstitial.setAdUnitId("ca-app-pub-9911896226381783/4251730552");

        // Begin loading your interstitial.
        interstitial.loadAd(adRequest);*/
/*    	final Handler handler = new Handler();
    	handler.postDelayed(new Runnable() {
    		
    		public void run() {
    			// TODO Auto-generated method stub
    			AdBuddiz.showAd(Main.this);
    		}
    	}, 2200);*/
        ReportBroadcast.phoneSwitched = false;

        context = getApplicationContext();
        
        
		 ReportState = getSharedPreferences("STATE", 0);
	     state = ReportState.getBoolean("REPORTSTATE", false);
	     
        
        fada2el = (Button) findViewById(R.id.btn_fada2el);
        howTo = (Button) findViewById(R.id.btn_howTo);
        fada2el.setOnClickListener(this);
        howTo.setOnClickListener(this);

        
        newReport = (Button) findViewById(R.id.btn_newReport);
        newReport.setOnClickListener(new OnClickListener() {
			
			public void onClick(View arg0) {
				

				//Starting Dialog
				LayoutInflater inflater = getLayoutInflater();
				View dialoglayout = inflater.inflate(R.layout.dialog_settings, (ViewGroup) getCurrentFocus());
				AlertDialog.Builder b = new AlertDialog.Builder(Main.this);
				b.setTitle(R.string.dialog_title);
				b.setIcon(android.R.drawable.ic_dialog_info);
				b.setView(dialoglayout);
				//spinner
				final Spinner spinner = (Spinner) dialoglayout.findViewById(R.id.sp_days);
				// Create an ArrayAdapter using the string array and a default spinner layout
				ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(Main.this, R.array.sp_days, android.R.layout.simple_spinner_item);
				// Specify the layout to use when the list of choices appears
				adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				// Apply the adapter to the spinner
				spinner.setAdapter(adapter);
				
				//================
				final TimePicker timerPicker = (TimePicker) dialoglayout.findViewById(R.id.timer_reportTime);
				
				b.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
					
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						//Starting service
						try{
							stopReportNotification();
							ReportService.clearNotification();
							}catch (Exception e) {
								// TODO: handle exception
								
						}
						resetReportNumber();
						Intent intent = new Intent(Main.this, ReportService.class);
						alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
					        pendingIntent = PendingIntent.getService(Main.this, 0, intent, 0);//268435456
					        SelectedDaysSpinner = (int)spinner.getSelectedItemId();
					        Log.i("Report", "spinner id" + SelectedDaysSpinner);
							switch (SelectedDaysSpinner){
							case 0:
								UserSelectedDays = 3;
								break;
							case 1:
								UserSelectedDays = 5;
								break;
							case 2:
								UserSelectedDays = 7;
								break;
							}
							SharedPreferences SelectedDays = getSharedPreferences("SelectedDays", 0);
					        SharedPreferences.Editor SelectedDaysEditor = SelectedDays.edit();
					        SelectedDaysEditor.putInt("SELECTEDDAYS", UserSelectedDays);
					        SelectedDaysEditor.commit();
							
					        state = true;
							 ReportState = getSharedPreferences("STATE", 0);
						     SharedPreferences.Editor ReportStateEditor = ReportState.edit();	//43200000 12 hours
						     ReportStateEditor.putBoolean("REPORTSTATE", true);
						     ReportStateEditor.commit();
						     state = ReportState.getBoolean("REPORTSTATE", true);
					        
					        
							Calendar c = Calendar.getInstance();
							Date alarmTime = new Date(System.currentTimeMillis());
					        alarmTime.setHours(timerPicker.getCurrentHour());
					        alarmTime.setMinutes(timerPicker.getCurrentMinute());
					        c.setTimeInMillis(alarmTime.getTime());
					        SharedPreferences time = getSharedPreferences("TIME", 0);
					        SharedPreferences.Editor timeEditor = time.edit();
					        timeEditor.putLong("timeC", c.getTimeInMillis());
					        timeEditor.commit();
					        	//86400000 24 hours 
					        	//43200000 12 hours
//					     alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), Alarm. , pendingIntent);
					        
					     alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
							SharedPreferences MainAlarm = getSharedPreferences("AlarmService", 0);
							SharedPreferences.Editor MainAlarmEditor = MainAlarm.edit();
							MainAlarmEditor.clear();
							MainAlarmEditor.putBoolean("MAIN_ALARM", true);
							MainAlarmEditor.commit();
					     Toast.makeText(Main.this, R.string.report_created, 6000).show();
					     ReportService.notificationDay = 0;
					     ReportBroadcast.phoneSwitched = false;
					}
				});
				b.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
					
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.cancel();
					}
				});
				AlertDialog alert = b.create();
				alert.show();

			}
		});
        

    }

	public static void addReport(){
		numberOfReportSaved++;
		Log.i("pref", "add report method "+ numberOfReportSaved);
	}
	
	public static int getUserSelectedDayNumber(){
		return UserSelectedDays;
	}
	
	public static int getReportNumber() {
		// TODO Auto-generated method stub
		Log.i("pref", "get report method "+ numberOfReportSaved);

		return numberOfReportSaved;
	}
	
	public void resetReportNumber(){
		SharedPreferences notifyData = getSharedPreferences("notify", 0);
		SharedPreferences.Editor notifyDataEdit = notifyData.edit();
		notifyDataEdit.putInt("notifyKey", 0);
		notifyDataEdit.commit();
	}
	
    public static void stopReportNotification(){
    	try{
       	     alarmManager.cancel(pendingIntent);
    	} catch (Exception e) {
			// TODO: handle exception

		}
	     state = false;
	     ReportBroadcast.phoneSwitched = false;
	     
	     
   }
    

    
    public void onClick(View buttonPressed) {
    	// TODO Auto-generated method stub
		switch (buttonPressed.getId()) {
		case R.id.btn_fada2el:
			startActivity(new Intent(this, islamic.ebadaty.reward.WorshipReward.class));
			break;
		case R.id.btn_howTo:
			startActivity(new Intent(this, About.class));
			break;
		default:
			break;
		}
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	// TODO Auto-generated method stub
    	menu.add(""+getResources().getString(R.string.mu_cancel)).setIcon(android.R.drawable.ic_input_delete);
    	return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	// TODO Auto-generated method stub
    	try {
          	stopReportNotification();
    		ReportService.clearNotification();
		} catch (Exception e) {
			// TODO: handle exception
		}

    	return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
    	// TODO Auto-generated method stub
    	super.onBackPressed();
//    	displayInterstitial();
    }
    

/*
    // Invoke displayInterstitial() when you are ready to display an interstitial.
    public void displayInterstitial() {
      if (interstitial.isLoaded()) {
        interstitial.show();
      }
    }*/
    
}
