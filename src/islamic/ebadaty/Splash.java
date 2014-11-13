package islamic.ebadaty;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class Splash extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
/*	      AdBuddiz.setPublisherKey("60034a1c-0118-4b45-b28c-978864c3f867");
	      AdBuddiz.cacheAds(this); // this = current Activity
*/		 Thread timer = new Thread(){
		     	@Override
		     	public void run() {
		     		try{
		     			sleep(2200);
		     		}catch(InterruptedException e){
		     			e.printStackTrace();
		     		}finally{
		     			startActivity(new Intent(Splash.this, Main.class));
		     		}
		     	}

		     };
		     timer.start();
		     
		 }
	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		finish();
	}
	


}
