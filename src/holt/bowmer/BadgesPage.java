package holt.bowmer;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class BadgesPage extends ListActivity
{
	FoodTrucksActivity ob;
	SharedPreferences badge;
	private TrucksDB mDbHelper;
	private String Listname;
	Cursor c;
//	ListView list = (ListView) findViewById(R.id.badgelist);
	ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
	
	static final int DIALOG_BASIC = 0;
	
	@Override
		public void onCreate(Bundle savedInstanceState)
		{
			super.onCreate(savedInstanceState);
			setContentView(R.layout.badges);
			
			mDbHelper = new TrucksDB (this);
			mDbHelper.open();
			fillBadges();
			registerForContextMenu (getListView());
			
			getWindow().setBackgroundDrawableResource(R.drawable.background);
			
			Button b = (Button) findViewById(R.id.badgesback);
			b.setOnClickListener(new View.OnClickListener() {
				public void onClick(View arg0) {
		setResult(RESULT_OK);
			finish();
			}
		});
	}
	
	protected Dialog onCreateDialog(int id) {
		Dialog dialog = null;
		switch(id) {
		case DIALOG_BASIC:
		LayoutInflater factory = LayoutInflater.from(this);
		final View badgesView = factory.inflate(R.layout.badgedialog, null);
		return new AlertDialog.Builder(BadgesPage.this)
			.setView(badgesView)
			.setTitle("Favorites")
			.setIcon(R.drawable.badge1)
			.create();
		}
		return dialog;
	}
		
	private void fillBadges() {
		//Get the badges from the SQLite database
		Cursor c = mDbHelper.fetchAllBadges();
		startManagingCursor(c);
		
		//Create an array to specify the fields we want to display in the list (TITLE AND BODY)
		String [] from = new String[]{TrucksDB.KEY_TITLE, TrucksDB.KEY_BODY};
		
		//and an array of the fields we want to bind those fields to
		int[] to = new int[] {R.id.text1};
		
		//Create a simple cursor adapter and set it to display
		SimpleCursorAdapter badges =
				new SimpleCursorAdapter(this, R.layout.notes_row, c, from, to);
		setListAdapter(badges);
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Cursor cursor = (Cursor) l.getItemAtPosition(position);
		startManagingCursor(cursor);
		Listname = cursor.getString(cursor.getColumnIndexOrThrow(TrucksDB.KEY_TITLE));
		showDialog(DIALOG_BASIC);
	}
		
	public void setOb (FoodTrucksActivity obA) {
		this.ob=obA;
		
	}
}