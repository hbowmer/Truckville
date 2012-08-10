package holt.bowmer;

import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class FoodTrucksActivity extends Activity
{
	private TrucksDB mDbHelper;
	private Bundle mDb;
	
	SharedPreferences pref;
	
	public static String playerId;
	
	public static final String PREFS_NAME_2 = "Activities";
	public static final String PREFS_BADGES = "Badges";
	
//	SharedPreferences pref = getSharedPreferences(LoginPage.PREFS_NAME, MODE_PRIVATE);
	
	private final Handler mHandler = new Handler() {
		public void handleMessage(final Message msg) {
			String responseString = msg.getData().getString("RESPONSE");
			try {
				JSONObject responseJson = new JSONObject(responseString);
				//Do something!
			} catch (JSONException je) {
				//Handle!
			}
		}
	};
	
	/**
	 * The onCreate method defines five main onClickListeners, one for
	 * each button: Check In, Find Trucks, Badges, Favorites, and Sign
	 * In. Each listener calls a respective activity.
	 * 
	 * The SQLite database is also opened and maintained in the onCreate
	 * method.
	 */
	@Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mDbHelper = new TrucksDB(this);
        mDbHelper.open();
        
        final TextView display_username = (TextView) findViewById(R.id.textView3);
        final TextView display_points = (TextView) findViewById(R.id.textView4);
        
        pref = getSharedPreferences("PlayerInformation", MODE_PRIVATE);
        if(pref.getString("PlayerId", null) != null) {
        	playerId = pref.getString("PlayerId", null);
        }
        
        new Timer().schedule(new TimerTask() {
        	
        	@Override
        	public void run() {
        		runOnUiThread(new Runnable() {
        			public void run() {
        				display_username.setText("Username: " + pref.getString("username", null));
        				display_points.setText("Points: " + pref.getString("points", null));
        			}
        		});
        	}
        }, 0, 100);
        
        
        
        
//        if(pref.getString(LoginPage.PREF_ID, null) != null) {
//        	playerId = pref.getString(LoginPage.PREF_ID, null)
//        }
        
//        BVHelper helper = new BVHelper("holtbow.com",
//        		"e020e02a4c076a06eb6a2786a0700fbb", mHandler);
        
        getWindow().setBackgroundDrawableResource(R.drawable.background);
        
        ImageButton findtrucksbutton = (ImageButton) findViewById(R.id.findtrucksbutton);
        findtrucksbutton.setOnClickListener(new View.OnClickListener() {
        	
        	public void onClick(View v) {
        		//Call the new screen
        		Intent i = new Intent(FoodTrucksActivity.this, FindTrucksPage.class);
        		startActivity(i);
        		
        	}
        });
        
        ImageButton checkinbutton = (ImageButton) findViewById(R.id.checkinbutton);
        checkinbutton.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				//Here I call the new screen
				Intent i = new Intent(FoodTrucksActivity.this, CheckInPage.class);
				startActivity(i);
			}
		});
        
        ImageButton badgesbutton = (ImageButton) findViewById(R.id.badgesbutton);
        badgesbutton.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				//Call the new screen
				//mDbHelper.clearTable();
				//onCreate(mDb);
				Intent i = new Intent(FoodTrucksActivity.this, BadgesPage.class);
				startActivity(i);
				
			}
		});
        
        ImageButton favoritesbutton = (ImageButton) findViewById(R.id.favoritesbutton);
        favoritesbutton.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				//Call the new screen
				//mDb.execSQL("DROP TABLE IF EXISTS favorites");
				Intent i = new Intent(FoodTrucksActivity.this, FavoritesPage.class);
				startActivity(i);
				
			}
		});
        
        Button loginbutton = (Button) findViewById(R.id.loginbutton);
        loginbutton.setOnClickListener(new View.OnClickListener() {
        	
        	public void onClick(View v) {
        		//Call the log in screen
        		Intent i = new Intent(FoodTrucksActivity.this, LoginPage.class);
        		startActivity(i);
        	}
        });
    }
}