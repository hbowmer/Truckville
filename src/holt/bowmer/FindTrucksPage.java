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

public class FindTrucksPage extends ListActivity
{
	FoodTrucksActivity ob;
	final CharSequence[] items = {"Twitter", "Favorite", "Back"};
	static final int DIALOG_BASIC = 0;
	static final int DIALOG_ADVANCED = 1;
	private String Listname;
	static int count = 0;
	static final int ID_TWITTER = 0;
	static final int ID_FAVORITE = 1;
	static final int ID_BACK = 2;
	
	@Override
		public void onCreate(Bundle savedInstanceState)
		{
			super.onCreate(savedInstanceState);
			
			getWindow().setBackgroundDrawableResource(R.drawable.background);
			setContentView(R.layout.tryer);
			setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1 ,Constants.TRUCKS));
			
			ListView lv = getListView();
			//lv.setTextFilterEnabled(true);
			//LayoutInflater inflater = getLayoutInflater();
			//ViewGroup header = (ViewGroup)inflater.inflate(R.layout.list_item, lv, false);
			//lv.addHeaderView(header, null, false);

			lv.setOnItemClickListener(new OnItemClickListener() {
			    public void onItemClick(AdapterView<?> parent, View view,
			        int position, long id) {
			      // When clicked, get string name and show the alertDialog
			    	Listname = (String) parent.getItemAtPosition(position);
			    	showDialog(DIALOG_BASIC);
			    }
			  });
			
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
							Toast.makeText(FindTrucksPage.this, "Twitter integration coming", Toast.LENGTH_LONG).show();
							removeDialog(DIALOG_BASIC);
							break;
							
						//Set up the favorite button action
						case ID_FAVORITE:
							String NewFavorite = Listname;
							Constants.newFav[count] = NewFavorite;
							Toast.makeText(FindTrucksPage.this, NewFavorite + " was added to your favorites", Toast.LENGTH_LONG).show();
							System.out.println(Constants.newFav[count]);
							System.out.println(count);
							count++;
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
		
	public void setOb (FoodTrucksActivity obA) {
		this.ob=obA;
		
	}
}