package holt.bowmer;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CheckInPage extends Activity
{
	FoodTrucksActivity ob;
		public void onCreate(Bundle icicle)
		{
			super.onCreate(icicle);
			setContentView(R.layout.checkin);
			
			getWindow().setBackgroundDrawableResource(R.drawable.background);
			
			Button b = (Button) findViewById(R.id.checkinBack);
			b.setOnClickListener(new View.OnClickListener() {
				public void onClick(View arg0) {
		setResult(RESULT_OK);
			finish();
			}
		});
	}
		
	public void setOb (FoodTrucksActivity obA) {
		this.ob=obA;
		
	}
}