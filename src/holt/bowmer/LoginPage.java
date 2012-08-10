package holt.bowmer;

import java.util.HashMap;

import oauth.signpost.OAuth;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import oauth.signpost.basic.DefaultOAuthProvider;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.exception.OAuthNotAuthorizedException;

import org.json.JSONException;
import org.json.JSONObject;

import winterwell.jtwitter.OAuthSignpostClient;
import winterwell.jtwitter.Twitter;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.badgeville.helper.BVHelper;

public class LoginPage extends Activity
{
	private static final String TAG = "Truckville";
	
	private static final String CONSUMER_KEY = "l3RNdf4rGp7P8QEMEA";
	private static final String CONSUMER_SECRET = "eNXzsX3KyAZu60hN5fWdqkrssAFJJO7q6vxED2Bn604";
	private static final String CALLBACK_SCHEME = "twitter-OAUTH-Truckville";
	private static final String CALLBACK_URL = CALLBACK_SCHEME + "://callback";
	private static final String TWITTER_USER = "hbowmer@badgeville.com";
	
	static final int DIALOG_NEWACCOUNT = 0;
	static final int DIALOG_ALTERNATE = 1;
	static final int DIALOG_TEXT_ENTRY = 2;
	
	public static final String PREFS_NAME = "PlayerInformation";
	public static final String PREF_USERNAME = "username";
	public static final String PREF_EMAIL = "email";
	public static final String PREF_ID = "PlayerId";
	public static final String PREF_POINTS = "points";
	
	private OAuthSignpostClient oauthClient;
	private OAuthConsumer mConsumer;
	private OAuthProvider mProvider;
	private Twitter twitter;
	SharedPreferences prefs;
	
	FoodTrucksActivity ob;
	
	EditText name, mail;
	String username, email, points;
	CheckBox checkBox;
	
	public static JSONObject responseJson;
	
	public Handler mHandler = new Handler() {
		public void handleMessage(final Message msg) {
			String responseString = msg.getData().getString("RESPONSE");
			try {
				responseJson = new JSONObject(responseString);
				Log.i("JSON OBJECT", responseJson.toString());
				try {
					FoodTrucksActivity.playerId = ((JSONObject) responseJson.get("data")).get("id").toString();
					points = ((JSONObject) ((JSONObject) responseJson.get("data")).get("units")).get("points_all").toString();
					username = ((JSONObject) responseJson.get("data")).get("display_name").toString();
					getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
						.edit()
						.putString(PREF_USERNAME, username)
						.putString(PREF_EMAIL, email)
						.putString(PREF_ID, FoodTrucksActivity.playerId)
						.putString(PREF_POINTS, points)
						.commit();
					
					if(getSharedPreferences(FoodTrucksActivity.PREFS_NAME_2, MODE_PRIVATE)
							.getBoolean(username + "Favorites", false) == false) {
						getSharedPreferences(FoodTrucksActivity.PREFS_NAME_2, MODE_PRIVATE)
							.edit()
							.putBoolean(username + "Favorites", false)
							.commit();
					}
					
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            	
            	Log.i("PLAYER ID", FoodTrucksActivity.playerId);
				//Do something!
			} catch (JSONException je) {
				//Handle!
			}
		}
	};
	
	BVHelper helper = new BVHelper("staging.badgeville.com",
			"558fe29afda4e10a12ee71824dfd6033", mHandler);
	
	@Override
	
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		getWindow().setBackgroundDrawableResource(R.drawable.background);
		
//		BVHelper helper = new BVHelper("holtbow.com",
//        		"e020e02a4c076a06eb6a2786a0700fbb", mHandler);
			
		mConsumer = new CommonsHttpOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);
			
		mProvider = new DefaultOAuthProvider(
				"http://api.twitter.com/oauth/request_token",
				"http://api.twitter.com/oauth/access_token",
				"http://api.twitter.com/oauth/authorize");

		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		String token = prefs.getString("token", null);
		String tokenSecret = prefs.getString("tokenSecret", null);

		if (token != null && tokenSecret != null) {
			mConsumer.setTokenWithSecret(token, tokenSecret);
			oauthClient = new OAuthSignpostClient(CONSUMER_KEY, CONSUMER_SECRET, token, tokenSecret);
			twitter = new Twitter(TWITTER_USER, oauthClient);
		} else {
			Log.d(TAG, "onCreate. Not Authenticated Yet " );
			
		
			
		Button b = (Button) findViewById(R.id.loginBack);
		b.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				setResult(RESULT_OK);
				finish();
		}
		});
		}
		
		Button login = (Button) findViewById(R.id.loginbutton);
		login.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// Call the twitter OAuth verification
//				new OAuthAuthorizeTask().execute();
				showDialog(DIALOG_TEXT_ENTRY);
				
			}
		});
		
		Button newaccount = (Button) findViewById(R.id.newaccount);
		newaccount.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(LoginPage.this, NewAccount.class);
				startActivity(i);
			}
		});
	}
	
	public Dialog onCreateDialog(int id) {
		Dialog dialog = null;
		//Set up case switch
		switch (id) {
		case DIALOG_TEXT_ENTRY:
        // This example shows how to add a custom layout to an AlertDialog
        LayoutInflater factory = LayoutInflater.from(this);
        final View textEntryView = factory.inflate(R.layout.custom_dialog, null);
        return new AlertDialog.Builder(LoginPage.this)
            .setView(textEntryView)
            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {

                    /* User clicked OK so do some stuff */
//                	name = (EditText) textEntryView.findViewById(R.id.username);
//                	username = name.getText().toString();
                	mail = (EditText) textEntryView.findViewById(R.id.email);
                	email = mail.getText().toString();
                	
                	HashMap<String, String> player = new HashMap<String, String>();
                	player.put("site", "holtbow.com");
                	player.put("email", email);
                	
                	helper.read(BVHelper.PLAYERS, "info", player);
                }
            })
            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {

                    /* User clicked cancel so do some stuff */
                }
            })
            .create();
		case DIALOG_NEWACCOUNT:
		AlertDialog.Builder alert = new AlertDialog.Builder(this);                 
		alert.setTitle("Login");  
		alert.setMessage("Username");                

		 // Set an EditText view to get user input   
		 final EditText input = new EditText(this); 
		 alert.setView(input);

		    alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {  
		    public void onClick(DialogInterface dialog, int whichButton) {  
		        String value = input.getText().toString();
		        Log.d( TAG, "Username: " + value);
		        return;                  
		       }  
		     });  

		    alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

		        public void onClick(DialogInterface dialog, int which) {
		            // TODO Auto-generated method stub
		            return;   
		        }
		    });
		            alert.create();
		}
		return dialog;
	}
	
	/*
	protected Dialog onCreateDialog(int id) {
		Dialog dialog = null;
		switch (id) {
		case DIALOG_NEWACCOUNT:
			return new AlertDialog.Builder(LoginPage.this)
			.setTitle("New Account")
			.setContentView(R.layout.custom_dialog)
			dialog.setContentView(R.layout.custom_dialog);
			dialog.setTitle("New Account");
			
			EditText text = (EditText) dialog.findViewById(R.id.editText1);
			ImageView image = (ImageView) dialog.findViewById(R.id.image);
			image.setImageResource(R.drawable.ic_launcher);
		}
	};
	*/
	
		
	//Responsible for starting the Twitter authorization
	class OAuthAuthorizeTask extends AsyncTask<Void, Void, String> {

		@Override
		protected String doInBackground(Void... params) {
			String authUrl;
			String message = null;
			Log.d(TAG, "OAuthAuthorizeTask mConsumer: " + mConsumer);
			Log.d(TAG, "OAuthAuthorizeTask mProvider: " + mProvider);
			try {
				authUrl = mProvider.retrieveRequestToken(mConsumer,
						CALLBACK_URL);
				Intent intent = new Intent(Intent.ACTION_VIEW,
						Uri.parse(authUrl));
				startActivity(intent);
			} catch (OAuthMessageSignerException e) {
				message = "OAuthMessageSignerException";
				e.printStackTrace();
			} catch (OAuthNotAuthorizedException e) {
				message = "OAuthNotAuthorizedException";
				e.printStackTrace();
			} catch (OAuthExpectationFailedException e) {
				message = "OAuthExpectationFailedException";
				e.printStackTrace();
			} catch (OAuthCommunicationException e) {
				message = "OAuthCommunicationException";
				e.printStackTrace();
			}
			return message;
		}
		
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if (result != null) {
				Toast.makeText(LoginPage.this, result,
						Toast.LENGTH_LONG).show();
			}
		}
	}

	// Responsible for retrieving access tokens from twitter 
	class RetrieveAccessTokenTask extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... params) {
			String message = null;
			String oauthVerifier = params[0];
			try {
				// Get the token
				Log.d(TAG, " RetrieveAccessTokenTask mConsumer: " + mConsumer);
				Log.d(TAG, " RetrieveAccessTokenTask mProvider: " + mProvider);
				Log.d(TAG, " RetrieveAccessTokenTask verifier: " + oauthVerifier);
				mProvider.retrieveAccessToken(mConsumer, oauthVerifier);
				String token = mConsumer.getToken();
				String tokenSecret = mConsumer.getTokenSecret();
				mConsumer.setTokenWithSecret(token, tokenSecret);

				Log.d(TAG, String.format(
						"verifier: %s, token: %s, tokenSecret: %s", oauthVerifier,
						token, tokenSecret));
				
				// Store token in prefs
				prefs.edit().putString("token", token)
						.putString("tokenSecret", tokenSecret).commit();

				// Make a Twitter object
				oauthClient = new OAuthSignpostClient(CONSUMER_KEY,
						CONSUMER_SECRET, token, tokenSecret);
				twitter = new Twitter(null, oauthClient);

				Log.d(TAG, "token: " + token);
			} catch (OAuthMessageSignerException e) {
				message = "OAuthMessageSignerException";
				e.printStackTrace();
			} catch (OAuthNotAuthorizedException e) {
				message = "OAuthNotAuthorizedException";
				e.printStackTrace();
			} catch (OAuthExpectationFailedException e) {
				message = "OAuthExpectationFailedException";
				e.printStackTrace();
			} catch (OAuthCommunicationException e) {
				message = "OAuthCommunicationException";
				e.printStackTrace();
			}
			return message;
		}
		
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if (result != null) {
				Toast.makeText(LoginPage.this, result,
						Toast.LENGTH_LONG).show();
			}
		}
	}
		
	 //Callback once we are done with the authorization of this app with
	 //Twitter.
	@Override
	public void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		Log.d(TAG, "intent: " + intent);

		// Check if this is a callback from OAuth
		Uri uri = intent.getData();
		if (uri != null && uri.getScheme().equals(CALLBACK_SCHEME)) {
			Log.d(TAG, "callback: " + uri.getPath());

			String verifier = uri.getQueryParameter(OAuth.OAUTH_VERIFIER);
			Log.d(TAG, "verifier: " + verifier);
			Log.d(TAG, " xxxxxxxxxxx mConsumer access token: " + mConsumer.getToken());
			Log.d(TAG, " xxxxxxxxxxxx mConsumer access token secret: " + mConsumer.getTokenSecret());
			Log.d(TAG, " xxxxxxxxxxxxx OAuth.OAUTH_TOKEN: " + OAuth.OAUTH_TOKEN);
			Log.d(TAG, " xxxxxxxxxxxxx OAuth.OAUTH_TOKEN_SECRET: " + OAuth.OAUTH_TOKEN_SECRET);

			new RetrieveAccessTokenTask().execute(verifier);
		}
	}
		
	public void logout(View v){
			
		getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
			.edit()
			.putString(PREF_USERNAME, "Guest")
			.putString(PREF_EMAIL, null)
			.putString(PREF_ID, null)
			.putString(PREF_POINTS, "0.0")
			.commit();
		
		FoodTrucksActivity.playerId = null;
		
		getSharedPreferences(FoodTrucksActivity.PREFS_NAME_2, MODE_PRIVATE)
			.edit()
			.putBoolean("Favorite", false)
			.commit();
		
		Toast.makeText(LoginPage.this, "You have logged out", Toast.LENGTH_LONG).show();
		
//		SharedPreferences.Editor editor = prefs.edit();
//		editor.putString("token", null);
//		editor.putString("tokenSecret", null);
//		editor.commit();
//		finish();
	}
	
	public void setOb (FoodTrucksActivity obA) {
		this.ob=obA;
		
	}
}