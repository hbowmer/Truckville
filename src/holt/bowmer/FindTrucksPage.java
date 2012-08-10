package holt.bowmer;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.badgeville.helper.BVHelper;

public class FindTrucksPage extends ListActivity
{
	FoodTrucksActivity ob;
	SharedPreferences pref, user, badge;
	private String Listname;
	private TrucksDB mDbHelper;
	private Cursor mNotesCursor;
	private SimpleCursorAdapter trucks;
	
	static final int DIALOG_BASIC = 0;
	static final int DIALOG_ADVANCED = 1;
	
	static int count = 0;
	
	static final int ID_TWITTER = 0;
	static final int ID_FAVORITE = 1;
	static final int ID_BACK = 2;
	
	private static final int INSERT_ID = Menu.FIRST;
	private static final int ACTIVITY_CREATE = 0;
	private static final int ACTIVITY_EDIT = 1;
	private static final int DELETE_ID = Menu.FIRST + 1;
	
	final CharSequence[] items = {"Twitter", "Favorite", "Back"};
			
	
	private final Handler mHandler = new Handler() {
		public void handleMessage(final Message msg) {
			String responseString = 
					msg.getData().getString("RESPONSE");
			try {
				// Do something!
				JSONObject responseJson = new JSONObject(responseString);
				Log.i("RESPONSE JSON", responseJson.toString());
				
				finish();
			} catch (JSONException je) {
				// Handle!
			}
		}
	};
	
	
	BVHelper helper = new BVHelper("staging.badgeville.com",
			"558fe29afda4e10a12ee71824dfd6033", mHandler);
	
	@Override
		public void onCreate(Bundle savedInstanceState)
		{
			super.onCreate(savedInstanceState);
			
			getWindow().setBackgroundDrawableResource(R.drawable.background);
			setContentView(R.layout.tryer);
			
			mDbHelper = new TrucksDB (this);
	        mDbHelper.open();
	        fillBaseData();
	        registerForContextMenu (getListView());

			Button b = (Button) findViewById(R.id.backbutton);
			b.setOnClickListener(new View.OnClickListener() {
				public void onClick(View arg0) {
		setResult(RESULT_OK);
			finish();
			}
		});
	}
	
	protected Dialog onCreateDialog(int id) {
		Dialog dialog;
		//Set up case switch
		switch (id) {
		//Called if the Dialog is basic
		case DIALOG_BASIC:
			return new AlertDialog.Builder(FindTrucksPage.this)
				.setTitle(Listname)
				.setItems(items, new DialogInterface.OnClickListener() {
					
					public void onClick(DialogInterface dialog, int which) {
						//set up the "which" switch
						switch (which) {
						
						//Set up the twitter button action
						case ID_TWITTER:
							Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://mobile.twitter.com/oaxacankitchen"));
							startActivity(browserIntent);
							Toast.makeText(FindTrucksPage.this, "Twitter loading", Toast.LENGTH_LONG).show();
							removeDialog(DIALOG_BASIC);
							break;
							
						//Set up the favorite button action
						case ID_FAVORITE:
							String NewFavorite = Listname;
							Toast.makeText(FindTrucksPage.this, NewFavorite + " was added to your favorites", Toast.LENGTH_LONG).show();
							createFavorite();
							removeDialog(DIALOG_BASIC);
							break;
							
						//Set up back button action
						case ID_BACK:
							Listname = null;
							removeDialog(DIALOG_BASIC);
							break;
						}
					}
				})
				.create();
		case DIALOG_ADVANCED:
			break;
		}
		return null;
	}
	
	private void createFavorite() {
		String favoriteName = Listname;
		mDbHelper.createFavorite(favoriteName, "");
		fillBaseData();
		
		pref = getSharedPreferences("Activities", MODE_PRIVATE);
		user = getSharedPreferences(LoginPage.PREFS_NAME, MODE_PRIVATE);
		badge = getSharedPreferences("Badges", MODE_PRIVATE);
		
		if (FoodTrucksActivity.playerId != null && pref.getBoolean(user.getString("username", null) + "Favorites", false) == false) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("activity[verb]", "favorite");
		params.put("player_id", FoodTrucksActivity.playerId);
		helper.create(BVHelper.ACTIVITIES, params);
		
		pref.edit()
			.putBoolean(getSharedPreferences(LoginPage.PREFS_NAME, MODE_PRIVATE)
			.getString("username", null) + "Favorites", true)
			.commit();
		
		mDbHelper.createBadges("Favorite", "Favorite a food truck!");
		fillBaseData();
		
		Toast.makeText(FindTrucksPage.this, "You earned the Favorites badge! +5 points",
				Toast.LENGTH_LONG).show();
		}
	}
	
	private void createTrucks() {
		Intent i = new Intent(this, trucksadd.class);
		startActivityForResult (i, ACTIVITY_CREATE);
	}
	
	
	private void fillBaseData() {
		//Get all the truck names from the data base and create the item list
		Cursor c = mDbHelper.fetchAllTrucks();
		startManagingCursor(c);
		
		//Create an array to specify the fields we want to display in the list (only TITLE)
		String [] from = new String[]{TrucksDB.KEY_TITLE};
		
		//and an array of the fields we want to bind those fields to (just text1)
		int[] to = new int[] {R.id.text1};
		
		//Create a simple cursor adapter and set it to display
		SimpleCursorAdapter trucks =
				new SimpleCursorAdapter(this, R.layout.notes_row, c, from, to);
		setListAdapter(trucks);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, INSERT_ID, 0, "Add Truck");
		return true;
	}
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch(item.getItemId()) {
        case INSERT_ID:
            createTrucks();
            return true;
        }
        
        return super.onMenuItemSelected(featureId, item);
    }
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.add(0, DELETE_ID, 0, "Delete");
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
    	switch(item.getItemId()) {
        case DELETE_ID:
            AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
            mDbHelper.deleteNote(info.id);
            fillBaseData();
            return true;
        }
		return super.onContextItemSelected(item);
	}
	
	@Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Cursor cursor = (Cursor) l.getItemAtPosition(position);
        startManagingCursor(cursor);
        Listname = cursor.getString(cursor.getColumnIndexOrThrow(TrucksDB.KEY_TITLE));
        showDialog(DIALOG_BASIC);
        
    }
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        Bundle extras = intent.getExtras();

        switch(requestCode) {
        case ACTIVITY_CREATE:
            String title = extras.getString(TrucksDB.KEY_TITLE);
            String body = extras.getString(TrucksDB.KEY_BODY);
            mDbHelper.createTrucks(title, body);
            fillBaseData();
            break;
        case ACTIVITY_EDIT:
            Long mRowId = extras.getLong(TrucksDB.KEY_ROWID);
            if (mRowId != null) {
                String editTitle = extras.getString(TrucksDB.KEY_TITLE);
                String editBody = extras.getString(TrucksDB.KEY_BODY);
                mDbHelper.updateTrucks(mRowId, editTitle, editBody);
            }
            fillBaseData();
            break;
        }
        
    }
		
	public void setOb (FoodTrucksActivity obA) {
		this.ob=obA;
		
	}
}