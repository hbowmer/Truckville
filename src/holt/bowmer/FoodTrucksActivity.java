package holt.bowmer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class FoodTrucksActivity extends Activity
{
	
	@Override
	//Below are unnecessary for the time being:
	//CheckInPage obB = new CheckInPage();
	//FindTrucksPage obB = new FindTrucksPage();
	//BadgesPage obB = new BadgesPage();
	
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
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
				Intent i = new Intent(FoodTrucksActivity.this, BadgesPage.class);
				startActivity(i);
				
			}
		});
        
        ImageButton favoritesbutton = (ImageButton) findViewById(R.id.favoritesbutton);
        favoritesbutton.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				//Call the new screen
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