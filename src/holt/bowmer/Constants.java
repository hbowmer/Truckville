package holt.bowmer;

import android.view.Menu;

public class Constants {
	
	//Menu item IDs
	public static final int ACTIVITY_PROFILE = Menu.FIRST;
	public static final int ACTIVITY_FRIENDS = Menu.FIRST + 1;
	public static final int ACTIVITY_REPLIES = Menu.FIRST + 2;
	public static final int ACTIVITY_LOGIN = Menu.FIRST + 3; 
	public static final int ACTIVITY_REFRESH = Menu.FIRST + 4;
	public static final int ACTIVITY_MENU_SCREEN = Menu.FIRST + 5;
	public static final int ACTIVITY_LATEST_TWEETS = Menu.FIRST + 6;
	
	//context menu item IDs
	public static final int CONTEXT_PROFILE = Menu.FIRST;
	public static final int CONTEXT_TWEETS = Menu.FIRST+1;
	public static final int CONTEXT_REPLY = Menu.FIRST+2;
	public static final int CONTEXT_RETWEET = Menu.FIRST+3;
	
	public static final String CONSUMER_KEY = "l3RNdf4rGp7P8QEMEA";
	public static final String CONSUMER_SECRET = "eNXzsX3KyAZu60hN5fWdqkrssAFJJO7q6vxED2Bn604";
	
	
	//SharedPreference user logon ID
	public static final String PREFS_NAME = "TwitterLogin";
	
	public static String[] TRUCKS = new String[] {
			"Annabelita's Pupusa Truck", "Antojitos Mexicanos", "Bulgogi BBQ", "Butterscotch On The Go", "Chairman Bao",
			"Ciao Bella Trolley", "Eat on Monday Truck", "Edgewood Eats", "Foster Brothers", "Golden Ice Cream",
			"Harold's Ribs", "MoBowl", "Net Appetit", "Oaxacan Kitchen Mobile", "Playa Azul Taco Truck",
			"Sam's Chowdermobile", "ShackMobile", "Super Tacos El Conrro", "TacoMania", "Tacos El Grullo",
			"Tony's Tacos"
		};
	
	public static String[] newFav = new String[100];

}