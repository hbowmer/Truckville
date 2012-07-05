package holt.bowmer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class trucksadd extends Activity {

	private EditText mTitleText;
	private EditText mBodyText;
	private Long mRowId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.trucks_add);
		
		setTitle("Add Truck");
		
		mTitleText = (EditText) findViewById(R.id.title);
		mBodyText = (EditText) findViewById(R.id.body);
		
		Button confirmButton = (Button) findViewById(R.id.confirm);
		
		mRowId = null;
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			String title = extras.getString(TrucksDB.KEY_TITLE);
			String body = extras.getString(TrucksDB.KEY_BODY);
			mRowId = extras.getLong(TrucksDB.KEY_ROWID);
			
			if (title != null) {
				mTitleText.setText(title);
			}
			if (body != null) {
				mBodyText.setText(body);
			}
		}
		
		confirmButton.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				Bundle bundle = new Bundle();
				
				bundle.putString(TrucksDB.KEY_TITLE, mTitleText.getText().toString());
				bundle.putString(TrucksDB.KEY_BODY, mBodyText.getText().toString());
				if (mRowId != null) {
					bundle.putLong(TrucksDB.KEY_ROWID, mRowId);
				}
				
				Intent mIntent = new Intent();
				mIntent.putExtras(bundle);
				setResult (RESULT_OK, mIntent);
				finish();
				
			}
		});
	}

}
