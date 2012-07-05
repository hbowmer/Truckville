package holt.bowmer;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class FavoritesPage extends ListActivity {
	
	FoodTrucksActivity ob;
	final CharSequence[] items = {"Twitter", "Delete", "Back"};
	static final int ID_TWITTER = 0;
	static final int ID_DELETE = 1;
	static final int ID_BACK = 2;
	static final int DIALOG_BASIC = 0;
	private String Listname;
	//new
	private int mNoteNumber = 1;
	private TrucksDB mDbHelper;
	static int deleteposition = 0;
	public static final String KEY_ROWID = "_id";
	
	
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		getWindow().setBackgroundDrawableResource(R.drawable.background);
		setContentView(R.layout.favorites);
		
		
		//New
		mDbHelper = new TrucksDB(this);
		mDbHelper.open();
		fillData();
		
		Button b = (Button) findViewById(R.id.favoritesBack);
		b.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
	setResult(RESULT_OK);
		finish();
		}
		});
	}
		
	protected Dialog onCreateDialog(int id) {
		id = DIALOG_BASIC;
		return new AlertDialog.Builder(FavoritesPage.this)
			.setTitle(Listname)
			.setItems(items, new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					//set up switch
					switch (which) {
					
					//set up twitter button action
					case ID_TWITTER:
						Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://mobile.twitter.com/chowdermobile"));
						startActivity(browserIntent);
						Toast.makeText(FavoritesPage.this, "Twitter loading", Toast.LENGTH_LONG).show();
						removeDialog(DIALOG_BASIC);
						break;
						
					//set up the delete button action
					case ID_DELETE:
						//deleteposition(deleteposition);
						mDbHelper.deleteNote(deleteposition);
						System.out.println("After delete position is: " + deleteposition);
						removeDialog(DIALOG_BASIC);
						fillData();
						break;
						
						
					//set up the back button action
					case ID_BACK:
						removeDialog(DIALOG_BASIC);
						break;
					}
					
				}
			})
			.create();
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		deleteposition = (int) position;
		System.out.println("Position is: " + position);
		System.out.println("Stored delete position is: " + deleteposition);
		Cursor cursor = (Cursor) l.getItemAtPosition(position);
		startManagingCursor(cursor);
        Listname = cursor.getString(cursor.getColumnIndexOrThrow(TrucksDB.KEY_TITLE));
		showDialog(DIALOG_BASIC);
	} 
	
	private void fillData() {
		//Get all of the notes from the database and create the item list
		Cursor c = mDbHelper.fetchAllFavorites();
		startManagingCursor(c);
		
		String[] from = new String[] {TrucksDB.KEY_TITLE };
		int[] to = new int[] {R.id.text1 };
		
		//Now create an array adapter and set it to display using our row
		SimpleCursorAdapter favorites =
				new SimpleCursorAdapter(this, R.layout.notes_row, c, from, to);
		setListAdapter(favorites);
	}
	
	private void deleteposition(final long id) {
		mDbHelper.deleteNote(id);
	}

	public void setOb (FoodTrucksActivity obA) {
		this.ob=obA;
		
	}

}