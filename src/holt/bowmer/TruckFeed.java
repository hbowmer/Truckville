package holt.bowmer;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class TruckFeed extends Activity
{
	FindTrucksPage ob;
		public void onCreate(Bundle savedInstanceState)
		{
			super.onCreate(savedInstanceState);
			setContentView(R.layout.truckslist);
			
			getWindow().setBackgroundDrawableResource(R.drawable.background);
			
			
			Button b = (Button) findViewById(R.id.favoritesBack);
			b.setOnClickListener(new View.OnClickListener() {
				public void onClick(View arg0) {
		setResult(RESULT_OK);
			finish();
			}
		});
	}
		
	public void setOb (FindTrucksPage obA) {
		this.ob=obA;
		
	}
}