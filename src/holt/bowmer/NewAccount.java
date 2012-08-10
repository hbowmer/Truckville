package holt.bowmer;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.badgeville.helper.BVHelper;

public class NewAccount extends Activity {
	
	String usename, maile, point;
	SharedPreferences prefs;
	
	private final Handler mHandler = new Handler() {
		public void handleMessage(final Message msg) {
			String responseString = 
					msg.getData().getString("RESPONSE");
			try {
				// Do something!
				Toast.makeText(NewAccount.this, "Account Created Successfully", Toast.LENGTH_LONG).show();
				JSONObject responseJson = new JSONObject(responseString);
				Log.i("RESPONSE JSON", responseJson.toString());
				
				FoodTrucksActivity.playerId = ((JSONObject) responseJson.get("data")).get("id").toString();
				point = ((JSONObject) ((JSONObject) responseJson.get("data")).get("units")).get("points_all").toString();
				usename = ((JSONObject) responseJson.get("data")).get("display_name").toString();
				
				getSharedPreferences(LoginPage.PREFS_NAME, MODE_PRIVATE)
					.edit()
					.putString(LoginPage.PREF_USERNAME, usename)
					.putString(LoginPage.PREF_EMAIL, email)
					.putString(LoginPage.PREF_ID, FoodTrucksActivity.playerId)
					.putString(LoginPage.PREF_POINTS, point)
					.commit();
				
				getSharedPreferences(FoodTrucksActivity.PREFS_NAME_2, MODE_PRIVATE)
					.edit()
					.putBoolean(getSharedPreferences(LoginPage.PREFS_NAME, MODE_PRIVATE)
					.getString("username", null) + "Favorite", false)
					.commit();
				
				finish();
			} catch (JSONException je) {
				// Handle!
				Toast.makeText(NewAccount.this, 
						responseString, Toast.LENGTH_LONG).show();
			}
		}
	};
	
	
	BVHelper helper = new BVHelper("staging.badgeville.com",
			"558fe29afda4e10a12ee71824dfd6033", mHandler);
	
	EditText name, mail, last, first;
	String username, email, lastname, firstname;
	
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.newaccount);
		getWindow().setBackgroundDrawableResource(R.drawable.background);
		
		Button b = (Button) findViewById(R.id.newaccountBack);
		b.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				setResult(RESULT_OK);
				finish();
			}
		});
	}
	
	
	public void createaccount(View V) { //This method is called by the "Create Account" button
		
		//Extract data from the editText fields
		name = (EditText) findViewById(R.id.username_text);
		username = name.getText().toString();
		mail = (EditText) findViewById(R.id.email_text);
		email = mail.getText().toString();
		last = (EditText) findViewById(R.id.last_name);
		lastname = last.getText().toString();
		first= (EditText) findViewById(R.id.first_name);
		firstname = first.getText().toString();
		
		//Pass user information to the BVHelper class
		HashMap<String, String> user = new HashMap<String, String>();
		user.put("user[email]", email);
		
		helper.create(BVHelper.USERS, user);
		
		HashMap<String, String> player = new HashMap<String, String>();
		player.put("email", email);
		player.put("site", "holtbow.com");
		player.put("player[display_name]", username);
		player.put("player[first_name]", firstname);
		player.put("player[last_name]", lastname);
		
		helper.create(BVHelper.PLAYERS, player);
		
	}

}
