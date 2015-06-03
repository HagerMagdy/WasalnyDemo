package com.Wasalny.wasalnydemo.activites;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.ByteArrayBuffer;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.Wasalny.wasalnydemo.R;
import com.Wasalny.wasalnydemo.controllers.ImageBitmap;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_HYBRID;
import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_SATELLITE;
public class MapScreen extends FragmentActivity {
	LocationManager locationManager;
	static Bitmap bitmap_Image;;
	static GoogleMap map;
	double currentLatitude, currentLongitude;
	final String CLIENT_ID = "2BIRUXRJEIDFPTJT5YV11WHFUQMC15VJZRAV04CUFR11Y2Z3";
	final String CLIENT_SECRET = "HKKMKN1VHGB3SWFBZWC0GVFJPCQPFSNTDCICVH0KG0MCYO5R";
	ArrayList<FoursquareVenue> venuesList;
	Button search_btn;
	ArrayAdapter<String> myAdapter;
	 String AccesToken;
	public String Firstname;
	public String lastname;
	public String place;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		if (android.os.Build.VERSION.SDK_INT > 9) {
		    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		    StrictMode.setThreadPolicy(policy);
		}
		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		search_btn = (Button) findViewById(R.id.search_btn);
		search_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(Constants.isNetworkOnline(MapScreen.this)){
					map.clear();
				new fourquare().execute();}
				else {
					
					
					Toast.makeText(MapScreen.this, "Please Check your Internet Connection",
					Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.e("onResume", "FOUND");
		if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			
			SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.map);

			// Getting Map for the SupportMapFragment
			map = fm.getMap();
			 map.setMapType(MAP_TYPE_HYBRID);
			if (map != null) {

				// Enable MyLocation Button in the Map
				map.setMyLocationEnabled(true);

			}
			getMyLocation();
		} else {
			showGPSDisabledAlertToUser();
		}
	}

	private void showGPSDisabledAlertToUser() {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder
				.setMessage(
						"GPS is disabled in your device. Would you like to enable it?")
				.setCancelable(false)
				.setPositiveButton("Goto Settings Page To Enable GPS",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								Intent callGPSSettingIntent = new Intent(
										android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
								startActivity(callGPSSettingIntent);
							}
						});
		alertDialogBuilder.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		AlertDialog alert = alertDialogBuilder.create();
		alert.show();

	}

	public void getMyLocation() {
		LocationManager locationManager = (LocationManager) this
				.getSystemService(Context.LOCATION_SERVICE);
		LocationListener locationListener = new LocationListener() {
			public void onLocationChanged(Location location) {
				updateLocation(location);

			}

			public void onStatusChanged(String provider, int status,
					Bundle extras) {
			}

			public void onProviderEnabled(String provider) {
			}

			public void onProviderDisabled(String provider) {
			}
		};
		locationManager.requestLocationUpdates(
				LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);

		
	}

	void updateLocation(Location location) {
		// currentLocation = location;
		currentLatitude = location.getLatitude();
		currentLongitude = location.getLongitude();
		map.addMarker(new MarkerOptions().position(new LatLng(currentLatitude,
				currentLongitude)));

		map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(
				(currentLatitude), (currentLongitude))));
		map.animateCamera(CameraUpdateFactory.zoomTo(12.0f));
	}
	private class fourquare extends AsyncTask<View, Void, String> {

		String temp;
		ProgressDialog mDialog ;

		@Override
		protected String doInBackground(View... urls) {
			// make Call to the url
			temp = makeCall("https://api.foursquare.com/v2/venues/search?client_id=" + CLIENT_ID + "&client_secret=" + CLIENT_SECRET + "&v=20130815&ll="+currentLatitude+","+currentLongitude);
			return "";
		}

		@Override
		protected void onPreExecute() {
			
			 mDialog = new ProgressDialog(MapScreen.this);
			mDialog.setCancelable(false);
			mDialog.setMessage("Loading...");
			mDialog.show();
		}

		@Override
		protected void onPostExecute(String result) {
			if (mDialog.isShowing())
				mDialog.dismiss();
			if (temp == null) {
				
			} else {
				
				
				venuesList = (ArrayList<FoursquareVenue>) parseFoursquare(temp);

				List<String> listTitle = new ArrayList<String>();

				for ( int i = 0 ; i < venuesList.size(); i++) {
					
					listTitle.add(i, venuesList.get(i).getName() + ", " + venuesList.get(i).getCategory() + "" + venuesList.get(i).getCity());
					if(isEmpty(venuesList.get(i).getLatitude())||isEmpty(venuesList.get(i).getLongtude())){
						
					}else{
			
				
				 ImageBitmap imageBitmap = new ImageBitmap();
				 
				 bitmap_Image = imageBitmap.getBitmapImage(venuesList.get(i).getPrifex()+"32"+venuesList.get(i).getSuffix());
				 
					//set bitmap task
				 Log.e(" bitmap is ",venuesList.get(i).getPrifex()+"88"+venuesList.get(i).getSuffix() +"foiund");
				 if (bitmap_Image== null) {
						
								}else {
								 Log.d("Not null bitmap","foiund");
										Bitmap bhalfsize=Bitmap.createScaledBitmap(bitmap_Image, bitmap_Image.getWidth()*4,bitmap_Image.getHeight()*4, false);
								map.addMarker(new MarkerOptions().position(new LatLng( Double.parseDouble( venuesList.get(i).getLatitude()),
											Double.parseDouble( venuesList.get(i).getLongtude()))).icon(BitmapDescriptorFactory.fromBitmap(bhalfsize)).title(venuesList.get(i).getName()).snippet(venuesList.get(i).getId()));
								map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(
										( Double.parseDouble( venuesList.get(i).getLatitude())), (Double.parseDouble( venuesList.get(i).getLongtude())))));
								map.animateCamera(CameraUpdateFactory.zoomTo(16.0f));
								map.setInfoWindowAdapter(new InfoWindowAdapter() {
									
									@Override
									public View getInfoWindow(Marker marker) {
										// TODO Auto-generated method stub
										return null;
									}
									
									@Override
									public View getInfoContents(Marker marker) {
										// TODO Auto-generated method stub
										View v = getLayoutInflater().inflate(
												R.layout.info_window_layout, null);
										TextView note = (TextView) v.findViewById(R.id.note);
										note.setText(marker.getTitle());
										return v;
									}
								});
								}
				
					}}

				map.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {
			        @Override
			        public void onInfoWindowClick(Marker marker) {
			          
			        	SharedPreferences prefs = getSharedPreferences("wdemo",
								Context.MODE_PRIVATE);
			        	
			        AccesToken=prefs.getString("Token", "");
			       if(isEmpty(AccesToken))
			       {			      	
			        	Intent intent = new Intent(MapScreen.this, ActivityWebView.class);
			            startActivity(intent);
			        }else {
			        	
			        	new CheckInTask().execute(marker.getSnippet());
			        }
			        }
			    });
			}
		}
	}
	public static String makeCall(String url) {
				StringBuffer buffer_string = new StringBuffer(url);
				String replyString = "";
				HttpClient httpclient = new DefaultHttpClient();
				HttpGet httpget = new HttpGet(buffer_string.toString());

				try {
					HttpResponse response = httpclient.execute(httpget);
					InputStream is = response.getEntity().getContent();

					BufferedInputStream bis = new BufferedInputStream(is);
					ByteArrayBuffer baf = new ByteArrayBuffer(20);
					int current = 0;
					while ((current = bis.read()) != -1) {
						baf.append((byte) current);
					}
					replyString = new String(baf.toByteArray());
				} catch (Exception e) {
					e.printStackTrace();
				}
				Log.e("ADDRESS",replyString.trim() +"herheu");
				return replyString.trim();
			}
	private static ArrayList<FoursquareVenue> parseFoursquare(final String response) {

		ArrayList<FoursquareVenue> temp = new ArrayList<FoursquareVenue>();
		try {

			JSONObject jsonObject = new JSONObject(response);
			if (jsonObject.has("response")) {
				if (jsonObject.getJSONObject("response").has("venues")) {
					JSONArray jsonArray = jsonObject.getJSONObject("response").getJSONArray("venues");

					for (int i = 0; i < jsonArray.length(); i++) {
						final FoursquareVenue poi = new FoursquareVenue();
						if (jsonArray.getJSONObject(i).has("name")) {
							poi.setName(jsonArray.getJSONObject(i).getString("name"));
							poi.setId(jsonArray.getJSONObject(i).getString("id"));
							if (jsonArray.getJSONObject(i).has("location")) {
								if (jsonArray.getJSONObject(i).getJSONObject("location").has("address")) {
									
									if (jsonArray.getJSONObject(i).getJSONObject("location").has("city")) {
										poi.setCity(jsonArray.getJSONObject(i).getJSONObject("location").getString("city"));
										poi.setLatitude(jsonArray.getJSONObject(i).getJSONObject("location").getString("lat"));
										poi.setLongtude(jsonArray.getJSONObject(i).getJSONObject("location").getString("lng"));
									}
									if (jsonArray.getJSONObject(i).has("categories")) {
										if (jsonArray.getJSONObject(i).getJSONArray("categories").length() > 0) {
											if (jsonArray.getJSONObject(i).getJSONArray("categories").getJSONObject(0).has("icon")) {
												poi.setCategory(jsonArray.getJSONObject(i).getJSONArray("categories").getJSONObject(0).getString("name"));
												JSONObject mImageObject=new JSONObject();
												mImageObject=jsonArray.getJSONObject(i).getJSONArray("categories").getJSONObject(0);
												poi.setPrifex(mImageObject.getJSONObject("icon").getString("prefix"));
												poi.setSuffix(mImageObject.getJSONObject("icon").getString("suffix"));
												
											}
										}
									}
									temp.add(poi);
								}
							}
						}
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			
			return new ArrayList<FoursquareVenue>();
		}
		return temp;

	}
	public boolean isEmpty(String text) {
		if (TextUtils.isEmpty(text)) {
			return true;
		}
		return false;
	}
	public class CheckInTask extends AsyncTask<String, Void, String>
	
    {private ProgressDialog mDialog;
    
    @Override
    protected void onPreExecute() {
    	// TODO Auto-generated method stub
    	super.onPreExecute();
    	mDialog = new ProgressDialog(MapScreen.this);
		mDialog.setCancelable(false);
		mDialog.setMessage("Loading...");
		mDialog.show();
    }

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			postData(params[0]);
			
			return null;
		}
		@Override
			protected void onPostExecute(String result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				if (mDialog.isShowing())
					mDialog.dismiss();
			}
		public void postData(String venueId) {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost("https://api.foursquare.com/v2/checkins/add");
			String str = "";
			try {
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("venueId", venueId));
			nameValuePairs.add(new BasicNameValuePair("oauth_token", AccesToken));
			nameValuePairs.add(new BasicNameValuePair("v", "20140212"));
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			 
			HttpResponse response = httpclient.execute(httppost);
				InputStream is = response.getEntity().getContent();
				BufferedInputStream bis = new BufferedInputStream(is);
				ByteArrayBuffer baf = new ByteArrayBuffer(20);
				int current = 0;
				while ((current = bis.read()) != -1) {
					baf.append((byte) current);
				}
		String		replyString = new String(baf.toByteArray());
		 ParseCheckin(replyString);
			} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			} catch (IOException e) {
			// TODO Auto-generated catch block
			}
			}
		public void ParseCheckin(String response){
			try {
				JSONObject jsonObject = new JSONObject(response);
				JSONObject jsonrespObject = new JSONObject(jsonObject.getString("response"));
				
				if (jsonrespObject.has("checkin")) {
					JSONObject jsoncheckpObject = new JSONObject(jsonrespObject.getString("checkin"));
					if (jsoncheckpObject.has("user")) {
						JSONObject jsonuserObject = new JSONObject(jsoncheckpObject.getString("user"));
						
						if (jsonuserObject.has("firstName")) {
							
							 Firstname=jsonuserObject.getString("firstName");
							 lastname=jsonuserObject.getString("lastName");
						}
						if (jsoncheckpObject.has("venue")) {
							JSONObject jsonVenuObject = new JSONObject(jsoncheckpObject.getString("venue"));	
							 place=jsonVenuObject.getString("name");
							
							
						}
					}
				
					}
				runOnUiThread(new Runnable(){

			          @Override
			          public void run(){
			        	  Toast.makeText(MapScreen.this,Firstname+"  "+lastname +"  "+"has Checked in "+place ,
									Toast.LENGTH_LONG).show();
			          }
			       });
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
    }
}
