package holt.bowmer;

import oauth.signpost.OAuth;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import oauth.signpost.basic.DefaultOAuthProvider;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.exception.OAuthNotAuthorizedException;
import winterwell.jtwitter.OAuthSignpostClient;
import winterwell.jtwitter.Twitter;
import winterwell.jtwitter.TwitterException;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginPage extends Activity
{
	private static final String TAG = "Truckville";
	
	private static final String CONSUMER_KEY = "l3RNdf4rGp7P8QEMEA";
	private static final String CONSUMER_SECRET = "eNXzsX3KyAZu60hN5fWdqkrssAFJJO7q6vxED2Bn604";
	private static final String CALLBACK_SCHEME = "twitter-OAUTH-Truckville";
	private static final String CALLBACK_URL = CALLBACK_SCHEME + "://callback";
	private static final String TWITTER_USER = "hbowmer@badgeville.com";
	
	private OAuthSignpostClient oauthClient;
	private OAuthConsumer mConsumer;
	private OAuthProvider mProvider;
	private Twitter twitter;
	SharedPreferences prefs;
	
	FoodTrucksActivity ob;
	
	@Override
	
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		getWindow().setBackgroundDrawableResource(R.drawable.twitterauth);
			
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
				new OAuthAuthorizeTask().execute();
				
			}
		});
	}

		
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
		
	public void logout(View view){
			
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString("token", null);
		editor.putString("tokenSecret", null);
		editor.commit();
		finish();
	}
	
	public void setOb (FoodTrucksActivity obA) {
		this.ob=obA;
		
	}
}