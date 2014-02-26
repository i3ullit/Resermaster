package ch.i3ullit.resermaster;

import ch.i3ullit.NFC_Utils.NFCUtil;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.Bundle;
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
//	NdefMessage[] msgs;
	NfcAdapter nfc;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "@MainActivity.onResume");
		super.onCreate(savedInstanceState);
		nfc = NfcAdapter.getDefaultAdapter(getApplicationContext());
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
		Intent i = new Intent(this, MainActivity.class);
		i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		PendingIntent intent = PendingIntent.getActivity(this, 0, i, 0);
		nfc.enableForegroundDispatch(this, intent, null, null);
		Log.d(TAG, "enableForegroundDispatch");
		
	}
	
	protected void onPause(){
		nfc.disableForegroundDispatch(this);
		Log.d(TAG, "disableForegroundDispatch");
		super.onPause();
	}
	
	protected void onNewIntent(Intent intent){
		Log.d(TAG, "@MainActivity.onNewIntent");
		Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
		
		Log.d(TAG, "From Tag: " +tag.toString());
		
		String techs[] = tag.getTechList();
		boolean containsNdef = false;
		boolean containsNdefFormatable = false;
		for (String tech : techs) {
			if (tech.equals("android.nfc.tech.Ndef"))containsNdef = true;
			if (tech.equals("android.nfc.tech.NdefFormatable"))containsNdefFormatable = true;
			}
		if (containsNdef) {Ndef ndef = Ndef.get(tag);
		Log.d("demo", "NdeF Tag Tech discovered");
		Log.d("demo", "NFC Forum Tag Type: " + ndef.getType());
		Log.d("demo", "Max size in bytes: " + ndef.getMaxSize());
		Log.d("demo", "Can tag be made read only? " + ndef.canMakeReadOnly());
		Log.d("demo", "Is writable?: " + ndef.isWritable());
		
		}
		if (containsNdefFormatable) {NdefFormatable ndef = NdefFormatable.get(tag);
		Log.d("demo", "NdefFormatable Tag Tech discovered\n");
		}
		
		Ndef ndef = Ndef.get(tag);
		NdefMessage message = ndef.getCachedNdefMessage(); 
		for (NdefRecord record : message.getRecords())
		{
		Log.d("Tag Reading", "Record of the Tag: " +record.getPayload().toString());
		
		StringBuffer b = null;
		
	//	b.append(String.format("TNF_ABSOLUTE_URI: type(%1$s), id(%2$s), payload(%3$s)\n",
	//			new String(record.getType()), tag.getId().toString(),
	//			new String(record.getPayload())));
		Log.d("Tag Content", "Printing Tag Content");
		Log.d("Tag Content", "Tag Content: " +record.getPayload());
		
		}
			
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