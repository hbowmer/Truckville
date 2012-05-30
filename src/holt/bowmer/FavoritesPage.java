package holt.bowmer;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import holt.bowmer.Constants;

public class FavoritesPage extends ListActivity
{
	FoodTrucksActivity ob;
	final CharSequence[] items = {"Twitter", "Back"};
	static final int ID_TWITTER = 0;
	static final int ID_BACK = 1;
	static final int DIALOG_BASIC = 0;
	private String Listname;
	
		public void onCreate(Bundle savedInstanceState)
		{
			super.onCreate(savedInstanceState);
			
			getWindow().setBackgroundDrawableResource(R.drawable.background);
			setContentView(R.layout.favorites);
			setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, Constants.newFav));
			
			ListView lv = getListView();
			lv.setOnItemClickListener(new OnItemClickListener () {
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					//When clicked, open alertDialog
					Listname = (String) parent.getItemAtPosition(position);
					showDialog(DIALOG_BASIC);
				}
			});
			
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
						Toast.makeText(FavoritesPage.this, "Twitter integration coming", Toast.LENGTH_LONG).show();
						removeDialog(DIALOG_BASIC);
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

	public void setOb (FoodTrucksActivity obA) {
		this.ob=obA;
		
	}

}