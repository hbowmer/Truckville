package holt.bowmer;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.OverlayItem;

public class CheckInPage extends MapActivity
{
	
	LinearLayout linearLayout;
	MapView mapView;
	MyLocation myLocation = new MyLocation();
	
	//New
	private MapController mapController;
	private LocationManager locationManager;
	private MyOverlays itemizedoverlay;
	private MyLocationOverlay myLocationOverlay;
	
	
	FoodTrucksActivity ob;
		@SuppressWarnings("null")
		public void onCreate(Bundle savedInstanceState)
		{
			super.onCreate(savedInstanceState);
			setContentView(R.layout.checkin);
			
			//Configure the map
			mapView = (MapView) findViewById(R.id.mapview);
			mapView.setBuiltInZoomControls(true);
			mapView.setSatellite(true);
			mapController = mapView.getController();
			mapController.setZoom(14); //Zoom 1 is worldview
			locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
			//GeoPoint point = null;
			//new GeoPoint(point.getLatitudeE6(), point.getLongitudeE6());
			//createMarker();
			//mapController.animateTo(point);
			//locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new GeoUpdateHandler());
			myLocationOverlay = new MyLocationOverlay(this, mapView);
			mapView.getOverlays().add(myLocationOverlay);
			
			myLocationOverlay.runOnFirstFix(new Runnable() {
				public void run() {
					mapView.getController().animateTo(myLocationOverlay.getMyLocation());
				}
			});
			
			Drawable drawable = this.getResources().getDrawable(R.drawable.ic_launcher);
			itemizedoverlay = new MyOverlays(this, drawable);
			createMarker();
			
			getWindow().setBackgroundDrawableResource(R.drawable.background);
			
			Button b = (Button) findViewById(R.id.checkinBack);
			b.setOnClickListener(new View.OnClickListener() {
				public void onClick(View arg0) {
		setResult(RESULT_OK);
			finish();
			}
		});
	}
		
		@Override
		protected boolean isRouteDisplayed() {
			return false;
		}
		
	
	/*	
	public class GeoUpdateHandler implements LocationListener {
		
		@Override
		public void onLocationChanged(Location location) {
			int lat = (int) (location.getLatitude() * 1E6);
			int lng = (int) (location.getLongitude() * 1E6);
			GeoPoint point = new GeoPoint(lat, lng);
			createMarker();
			mapController.animateTo(point); //mapController.setCenter(point);
		}
		
		@Override
		public void onProviderDisabled(String provider) {
		}
		
		@Override
		public void onProviderEnabled(String provider) {
		}
		
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
	}
	*/
	
	private void createMarker() {
		GeoPoint p = mapView.getMapCenter();
		OverlayItem overlayitem = new OverlayItem(p, "", "");
		itemizedoverlay.addOverlay(overlayitem);
		if (itemizedoverlay.size() > 0) {
			mapView.getOverlays().add(itemizedoverlay);
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		myLocationOverlay.enableMyLocation();
		myLocationOverlay.enableCompass();
	}
	
	@Override
	protected void onPause() {
		super.onResume();
		myLocationOverlay.disableMyLocation();
		myLocationOverlay.disableCompass();
	}
		
		
		
	/*
	//Define a listener that responds to location updates
	LocationListener locationListener = new LocationListener() {
		public void onLocationChanged(Location location) {
			//Called when a new location is found by the network location provider
			makeUseOfNewLocation(location);
		}
		
		public void onStatusChanged(String provider, int status, Bundle extras) {}
		
		public void onProviderEnabled(String provider) {}
		
		public void onProviderDisabled(String provider) {}
	};
	*/
		
	public void setOb (FoodTrucksActivity obA) {
		this.ob=obA;
		
	}
}