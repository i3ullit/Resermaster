package ch.i3ullit.resermaster;

import java.util.concurrent.ExecutionException;

import ch.i3ullit.resermaster.nfc.NdefReadTask;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentFilter.MalformedMimeTypeException;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private static final String TAG = MainActivity.class.getSimpleName();
	public static final String MIME_TEXT_PLAIN = "text/plain";

	private TextView mTextView;
	private EditText mEditText;
	private NfcAdapter mNfcAdapter;
	private NdefReadTask readTask;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "@MainActivity.onResume");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mTextView = (TextView) findViewById(R.id.textView_explanation);
		mEditText = (EditText) findViewById(R.id.editTextField);

		mNfcAdapter = NfcAdapter.getDefaultAdapter(this);



		if (mNfcAdapter == null) {
			// Stop here, we definitely need NFC
			Toast.makeText(this, "This device doesn't support NFC.", Toast.LENGTH_LONG).show();
			finish();
			return;

		}

		if (!mNfcAdapter.isEnabled()) {
			mTextView.setText(R.string.nfc_disabled);
		} else {
			mTextView.setText(R.string.nfc_enabled);
		}

		handleIntent(getIntent());
	}

	private void handleIntent(Intent intent) {
		String action = intent.getAction();
		readTask = null;
		if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {

			String type = intent.getType();
			if (MIME_TEXT_PLAIN.equals(type)) {

				Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
				setMText(tag);
			} else {
				Log.d(TAG, "Wrong mime type: " + type);
			}
		} else if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {

			// In case we would still use the Tech Discovered Intent
			Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
			String[] techList = tag.getTechList();
			String searchedTech = Ndef.class.getName();

			for (String tech : techList) {
				if (searchedTech.equals(tech)) {
					setMText(tag);
					break;
				}
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void onResume(){
		super.onResume();

		Log.d(TAG, "onResume");

		/**
		 * It's important, that the activity is in the foreground (resumed). Otherwise
		 * an IllegalStateException is thrown. 
		 */
		setupForegroundDispatch(this, mNfcAdapter);
	}

	protected void onPause(){
		/**
		 * Call this before onPause, otherwise an IllegalArgumentException is thrown as well.
		 */
		stopForegroundDispatch(this, mNfcAdapter);
		super.onPause();
	}


	protected void onNewIntent(Intent intent) { 
		/**
		 * This method gets called, when a new Intent gets associated with the current activity instance.
		 * Instead of creating a new activity, onNewIntent will be called. For more information have a look
		 * at the documentation.
		 * 
		 * In our case this method gets called, when the user attaches a Tag to the device.
		 */
		handleIntent(intent);
	}

	/**
	 * @param activity The corresponding {@link Activity} requesting the foreground dispatch.
	 * @param adapter The {@link NfcAdapter} used for the foreground dispatch.
	 */
	public static void setupForegroundDispatch(final Activity activity, NfcAdapter adapter) {
		final Intent intent = new Intent(activity.getApplicationContext(), activity.getClass());
		intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

		final PendingIntent pendingIntent = PendingIntent.getActivity(activity.getApplicationContext(), 0, intent, 0);

		IntentFilter[] filters = new IntentFilter[1];
		String[][] techList = new String[][]{};

		// Notice that this is the same filter as in our manifest.
		filters[0] = new IntentFilter();
		filters[0].addAction(NfcAdapter.ACTION_NDEF_DISCOVERED);
		filters[0].addCategory(Intent.CATEGORY_DEFAULT);
		try {
			filters[0].addDataType(MIME_TEXT_PLAIN);
		} catch (MalformedMimeTypeException e) {
			throw new RuntimeException("Check your mime type.");
		}

		adapter.enableForegroundDispatch(activity, pendingIntent, filters, techList);
	}

	/**
	 * @param activity The corresponding {@link BaseActivity} requesting to stop the foreground dispatch.
	 * @param adapter The {@link NfcAdapter} used for the foreground dispatch.
	 */
	public static void stopForegroundDispatch(final Activity activity, NfcAdapter adapter) {
		adapter.disableForegroundDispatch(activity);
	}

	public void onButtonDeveloperActivityClick(View view){
		Intent intent = new Intent(this, DeveloperActivity.class);
		startActivity(intent);
	}

	private void setMText(Tag tag){
		NdefReadTask rt = new NdefReadTask();
		readTask = (NdefReadTask) rt.execute(tag);
		String ndefText = null;
		try {
			ndefText = readTask.get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if( ndefText!= null){
			mEditText.setText(ndefText);
		}
	}
}