package ch.i3ullit.resermaster;

import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;

public class DeveloperActivity extends Activity {
	private static final String TAG = DeveloperActivity.class.getSimpleName();
	NfcAdapter nfc = NfcAdapter.getDefaultAdapter(getApplicationContext());
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_developer);
		
		// NFC Settings if NFC is not enabled
		if(!nfc.isEnabled()){
			startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
		}
	}
	
	public void onResume(){
		Log.d(TAG, "@DeveloperActivity.onResume");
		//Intent i = new Intent(this, .getClass());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.developer, menu);
		return true;
	}
	
	// Go back to MainActivity
	public void onButtonMainActivityClick(View view){
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
	}
	

}
