package ch.i3ullit.resermaster;

import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Parcelable;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends Activity {
	private static final String TAG = MainActivity.class.getSimpleName();
	NdefMessage[] msgs;
	NfcAdapter nfc = NfcAdapter.getDefaultAdapter(getApplicationContext());
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void onResume(){
		super.onResume();
		
		Log.d(TAG, "@MainActivity.onResume");
		
		//	Context activity;
		Intent i = new Intent(this, DeveloperActivity.class);
		i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		PendingIntent intent = PendingIntent.getActivity(this, 0, i, 0);
		nfc.enableForegroundDispatch(this, intent, null, null);
	}
	
	public void onPause(){
		nfc.disableForegroundDispatch(this);
	}
	
	public void onNewIntent(Intent intent){
		Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
		Log.d(TAG, "From Tag: " +tag.toString());
	}
	
/*	public void onResume(){
		super.onResume();
		NfcAdapter nfc = NfcAdapter.getDefaultAdapter(getApplicationContext());
		Log.d(TAG, "@MainActivity.onResume");
		Intent intent = getIntent();
		// Versuch den Inhalt des Tags zu lesen
	    if (nfc.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
	        Parcelable[] rawMsgs = intent.getParcelableArrayExtra(nfc.EXTRA_NDEF_MESSAGES);
	        Log.d(TAG, "Raw Msgs: "+rawMsgs.toString());
	        if (rawMsgs != null) {
	            msgs = new NdefMessage[rawMsgs.length];
	            Log.d(TAG, "msgs: " +msgs.toString() + "Number of Msgs: " + rawMsgs.length);
	            for (int i = 0; i < rawMsgs.length; i++) {
	                msgs[i] = (NdefMessage) rawMsgs[i];
	                Log.d(TAG, "Msg Counter: " +msgs.length );
	            }
	        }
	        
	    }
	    if (msgs != null){
	    	
	    EditText textField = (EditText) findViewById(R.id.editTextField);
	    Log.d(TAG, "Setting text");
	   // Log.d(TAG, "Setting text: " +msgs.length );
	 //   textField.setText(msgs[0].toString());
	    }
	    else{
	    	Log.d(TAG, "No Messages");
	    }
	    
	}
	*/
	public void onButtonDeveloperActivityClick(View view){
		Intent intent = new Intent(this, DeveloperActivity.class);
		startActivity(intent);
	}

}