/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.trackdroid;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.FragmentActivity;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This shows how to place markers on a map.
 */
public class ShowInMap extends FragmentActivity implements
		OnMarkerClickListener, OnInfoWindowClickListener, OnMarkerDragListener,
		OnSeekBarChangeListener, ConnectionCallbacks,
		OnConnectionFailedListener, LocationListener,
		OnMyLocationButtonClickListener {
	private static final LatLng BRISBANE = new LatLng(-27.47093, 153.0235);
	private static final LatLng MELBOURNE = new LatLng(-37.81319, 144.96298);
	private static final LatLng SYDNEY = new LatLng(-33.87365, 151.20689);
	private static final LatLng ADELAIDE = new LatLng(-34.92873, 138.59995);
	private static final LatLng PERTH = new LatLng(-31.952854, 115.857342);

	
	private static final String TAG_POSTS = "posts";

	private ProgressDialog pDialog;

	// php read comments script
	// private GoogleMap mMap;
	// private Button Members;

	private LocationClient mLocationClient;
	
	private String latitude;
	
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_MESSAGE = "message";
	private String longitude;
	
	private String username = "";
	private static final String LOCATION_URL = "http://10.0.3.2/trackdroid/location.php";
	JSONParser jsonParser = new JSONParser();

	private static final LocationRequest REQUEST = LocationRequest.create()
			.setInterval(5000) // 5 seconds
			.setFastestInterval(16) // 16ms = 60fps
			.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
	// it's important to note that the message is both in the parent branch of
	// our JSON tree that displays a "Post Available" or a "No Post Available"
	// message,
	// and there is also a message for each individual post, listed under the
	// "posts"
	// category, that displays what the user typed as their message.

	// An array of all of our comments
	private JSONArray mLocations = null;
	// manages all of our comments in a list.
	private ArrayList<HashMap<String, String>> mLocationsList;
	// private ArrayList<String> list;

	private static final String READ_LOCATIONS_URL1 = "http://10.0.3.2/trackdroid/getlocationdata.php";

	/** Demonstrates customizing the info window and/or its contents. */
	class CustomInfoWindowAdapter implements InfoWindowAdapter {
		//private final RadioGroup mOptions;
		private int NumOfElements;
		// public LatLng[] positions;
		// These a both viewgroups containing an ImageView with id "badge" and
		// two TextViews with id
		// "title" and "snippet".
		//private final View mWindow;
	//	private final View mContents;

		CustomInfoWindowAdapter() {
		//	mWindow = getLayoutInflater().inflate(R.layout.custom_info_window,
			//		null);
		//	mContents = getLayoutInflater().inflate(
			//		R.layout.custom_info_contents, null);
		//	mOptions = (RadioGroup) findViewById(R.id.custom_info_window_options);
		}

		@Override
		public View getInfoWindow(Marker marker) {
			/*if (mOptions.getCheckedRadioButtonId() != R.id.custom_info_window) {
				// This means that getInfoContents will be called.
				return null;
			}
			*/
		//	render(marker, mWindow);
			//return mWindow;
		return null;
		}

		@Override
		public View getInfoContents(Marker marker) {
		/*	if (mOptions.getCheckedRadioButtonId() != R.id.custom_info_contents) {
				// This means that the default info contents will be used.
				return null;
			}
*/			
	//		render(marker, mContents);
		//	return mContents;
			return null;
		}

		private void render(Marker marker, View view) {
			int badge;
			// Use the equals() method on a Marker to check for equals. Do not
			// use ==.
			if (marker.equals(mBrisbane)) {
				badge = R.drawable.badge_qld;
			} else if (marker.equals(mAdelaide)) {
				badge = R.drawable.badge_sa;
			} else if (marker.equals(mSydney)) {
				badge = R.drawable.badge_nsw;
			} else if (marker.equals(mMelbourne)) {
				badge = R.drawable.badge_victoria;
			} else if (marker.equals(mPerth)) {
				badge = R.drawable.badge_wa;
			} else {
				// Passing 0 to setImageResource will clear the image view.
				badge = 0;
			}
			((ImageView) view.findViewById(R.id.badge)).setImageResource(badge);

			String title = marker.getTitle();
			TextView titleUi = ((TextView) view.findViewById(R.id.title));
			if (title != null) {
				// Spannable string allows us to edit the formatting of the
				// text.
				SpannableString titleText = new SpannableString(title);
				titleText.setSpan(new ForegroundColorSpan(Color.RED), 0,
						titleText.length(), 0);
				titleUi.setText(titleText);
			} else {
				titleUi.setText("");
			}

			String snippet = marker.getSnippet();
			TextView snippetUi = ((TextView) view.findViewById(R.id.snippet));
			if (snippet != null && snippet.length() > 12) {
				SpannableString snippetText = new SpannableString(snippet);
				snippetText.setSpan(new ForegroundColorSpan(Color.MAGENTA), 0,
						10, 0);
				snippetText.setSpan(new ForegroundColorSpan(Color.BLUE), 12,
						snippet.length(), 0);
				snippetUi.setText(snippetText);
			} else {
				snippetUi.setText("");
			}
		}
	}

	private GoogleMap mMap;

	private Marker mPerth;
	private Marker mSydney;
	private Marker mBrisbane;
	private Marker mAdelaide;
	private Marker mMelbourne;

	private final List<Marker> mMarkerRainbow = new ArrayList<Marker>();

	//private TextView mTopText;
	//private SeekBar mRotationBar;
	//private CheckBox mFlatBox;
	private ArrayList<String> list;
	private ArrayList<String> usernames;
	private final Random mRandom = new Random();
	private LatLng[] positions;
	private Marker[] marker;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_in_map);
		SharedPreferences example = getSharedPreferences("P1", 0);
		username = example.getString("username", "defValue");
		System.out.println(username);
		list = new ArrayList<String>();
		usernames = new ArrayList<String>();
		// markers=new Marker[];
		list = (ArrayList<String>) getIntent()
				.getSerializableExtra("arraylist");
		
		
		
		
		
		
		
		
		
		positions = new LatLng[list.size() / 3];
		marker = new Marker[list.size() / 3];
		extractlist();
	
	//	mTopText = (TextView) findViewById(R.id.top_text);

	//	mRotationBar = (SeekBar) findViewById(R.id.rotationSeekBar);
		//mRotationBar.setMax(360);
	//	mRotationBar.setOnSeekBarChangeListener(this);

	//	mFlatBox = (CheckBox) findViewById(R.id.flat);

		setUpMapIfNeeded();
	}

	public void extractlist() {
		positions = new LatLng[list.size() / 3];
		marker = new Marker[list.size() / 3];
		usernames = new ArrayList<String>();
		int i = 0, j = 0;
		while (i < list.size()) {
			// System.out.println("*****************************"+list.get(i));
			usernames.add(list.get(i));

			positions[j] = new LatLng(Double.parseDouble(list.get(i + 1)),
					Double.parseDouble(list.get(i + 2)));
			System.out.println("*****************************");
			System.out.println(Double.parseDouble(list.get(i + 1)));
			System.out.println("*****************************");
			System.out.println(Double.parseDouble(list.get(i + 2)));
			// Double.parseDouble(list.get(i+2))
			System.out.println("-----------------------------------");

			i = i + 3;
			j++;

		}

	}

	@Override
	protected void onResume() {
		super.onResume();
		setUpMapIfNeeded();
		setUpLocationClientIfNeeded();
		mLocationClient.connect();
	}

	private void setUpLocationClientIfNeeded() {
		if (mLocationClient == null) {
			mLocationClient = new LocationClient(getApplicationContext(), this, // ConnectionCallbacks
					this); // OnConnectionFailedListener
		}
	}

	private void setUpMapIfNeeded() {
		// Do a null check to confirm that we have not already instantiated the
		// map.
		if (mMap == null) {
			// Try to obtain the map from the SupportMapFragment.
			mMap = ((SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.map)).getMap();
			// Check if we were successful in obtaining the map.
			if (mMap != null) {
				setUpMap();
			}
		}
	}

	private void setUpMap() {
		// Hide the zoom controls as the button panel will cover it.
		mMap.getUiSettings().setZoomControlsEnabled(false);

		// Add lots of markers to the map.
		addMarkersToMap();

		// Setting an info window adapter allows us to change the both the
		// contents and look of the
		// info window.
		mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter());

		// Set listeners for marker events. See the bottom of this class for
		// their behavior.
		mMap.setOnMarkerClickListener(this);
		mMap.setOnInfoWindowClickListener(this);
		mMap.setOnMarkerDragListener(this);

		// Pan to see all markers in view.
		// Cannot zoom to bounds until the map has a size.
		final View mapView = getSupportFragmentManager().findFragmentById(
				R.id.map).getView();
		if (mapView.getViewTreeObserver().isAlive()) {
			mapView.getViewTreeObserver().addOnGlobalLayoutListener(
					new OnGlobalLayoutListener() {
						@SuppressWarnings("deprecation")
						// We use the new method when supported
						@SuppressLint("NewApi")
						// We check which build version we are using.
						@Override
						public void onGlobalLayout() {
							/*
							 * LatLngBounds bounds = new LatLngBounds.Builder()
							 * .include(PERTH) .include(SYDNEY)
							 * .include(ADELAIDE) .include(BRISBANE)
							 * .include(MELBOURNE) .build();
							 */
							LatLngBounds.Builder builder = new LatLngBounds.Builder();

							for (int i = 0; i < positions.length; i++) {
								builder.include(positions[i]);

							}
							LatLngBounds bound = builder.build();

							// LatLngBounds bounds = new LatLngBounds.Builder()
							// .include(positions[0]).include(positions[1]).build();

							if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
								mapView.getViewTreeObserver()
										.removeGlobalOnLayoutListener(this);
							} else {
								mapView.getViewTreeObserver()
										.removeOnGlobalLayoutListener(this);
							}
							mMap.moveCamera(CameraUpdateFactory
									.newLatLngBounds(bound, 50));
						}
					});
		}
	}

	private void addMarkersToMap() {
		
		
		
		
		System.out.println("((((((((((((((((((((((((((((((((((((((((((((");
		// Uses a colored icon.
		int i = 0, j = 0;
		marker = new Marker[list.size() / 3];
		mMap.clear();
		while (i < list.size()) {
		 System.out.println("\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"+list.get(i));
			// username.add(list.get(i));
			// positions[j] = new LatLng( Double.parseDouble(list.get(i+1)) ,
			// Double.parseDouble(list.get(i+2)));
	
		 
		 marker[j] = mMap.addMarker(new MarkerOptions()
					.position(positions[j])
					.title(list.get(i))
					.snippet(":D :D")
					.icon(BitmapDescriptorFactory
							.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

			
			
			//marker[j].showInfoWindow();
			/*
			 * marker[1]= mMap.addMarker(new MarkerOptions()
			 * .position(positions[1]) .title(list.get(3)) .snippet(":D :D")
			 * .icon(BitmapDescriptorFactory.fromResource(R.drawable.arrow))
			 * .infoWindowAnchor(0.5f, 0.5f));
			 */

			i = i + 3;
			j++;

		}
		marker[0].showInfoWindow();
		marker[1].showInfoWindow();
		marker[2].showInfoWindow();
		/*
		
		for(int k=0;k<j;k++){
			marker[j].showInfoWindow();
		}
		*/
		
/*
		
		  mBrisbane = mMap.addMarker(new MarkerOptions() .position(BRISBANE)
		  .title("Brisbane") .snippet("Population: 2,074,200")
		  .icon(BitmapDescriptorFactory
		  .defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
		  
		  // Uses a custom icon with the info window popping out of the center of the icon.
		  mSydney = mMap.addMarker(new MarkerOptions()
		  .position(SYDNEY) .title("Sydney") .snippet("Population: 4,627,300")
		  .icon(BitmapDescriptorFactory.fromResource(R.drawable.arrow))
		  .infoWindowAnchor(0.5f, 0.5f));
		 
		  // Creates a draggable marker. Long press to drag. mMelbourne =
		  mMap.addMarker(new MarkerOptions() .position(MELBOURNE)
		  .title("Melbourne") .snippet("Population: 4,137,400")
		  .draggable(true));
		  
		  // A few more markers for good measure. 
		  mPerth = mMap.addMarker(new MarkerOptions() .position(PERTH) .title("Perth")
		  .snippet("Population: 1,738,800")); mAdelaide = mMap.addMarker(new
		  MarkerOptions() .position(ADELAIDE) .title("Adelaide")
		  .snippet("Population: 1,213,000"));
		 
		// Creates a marker rainbow demonstrating how to create default marker
		// icons of different
		// hues (colors).*/
	//	float rotation = mRotationBar.getProgress();
	//	boolean flat = mFlatBox.isChecked();
		/*
		 * int numMarkersInRainbow = 12; for (int i = 0; i <
		 * numMarkersInRainbow; i++) { mMarkerRainbow.add(mMap.addMarker(new
		 * MarkerOptions() .position(new LatLng( -30 + 10 * Math.sin(i * Math.PI
		 * / (numMarkersInRainbow - 1)), 135 - 10 * Math.cos(i * Math.PI /
		 * (numMarkersInRainbow - 1)))) .title("Marker " + i)
		 * .icon(BitmapDescriptorFactory.defaultMarker(i * 360 /
		 * numMarkersInRainbow)) .flat(flat) .rotation(rotation)));
		 * 
		 * }
		 */
	}

	private boolean checkReady() {
		if (mMap == null) {
			Toast.makeText(this, R.string.map_not_ready, Toast.LENGTH_SHORT)
					.show();
			return false;
		}
		return true;
	}

	/** Called when the Clear button is clicked. */
	public void onClearMap(View view) {
		if (!checkReady()) {
			return;
		}
		mMap.clear();
	}

	/** Called when the Reset button is clicked. */
	public void onResetMap(View view) {
		if (!checkReady()) {
			return;
		}

		// Clear the map because we don't want duplicates of the markers.
		mMap.clear();

		if (mLocationClient != null && mLocationClient.isConnected()) {
			// String msg = "Location = " + mLocationClient.getLastLocation();
			latitude = "" + mLocationClient.getLastLocation().getLatitude();
			longitude = "" + mLocationClient.getLastLocation().getLongitude();

			// temp.setText(latitude+" aaaaaaaa   "+longitude+"   ");
			new LocationUpdate().execute();

			//System.out.println("***********/////////////////*****************");

		}

	//extractlist();
		
		
		for(int i=0;i<list.size();i++)
		{
			System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$"+list.get(i));
		}
		extractlist();
		//setUpMapIfNeeded();
		addMarkersToMap();
	}

	/** Called when the Reset button is clicked. */
	public void onToggleFlat(View view) {
		if (!checkReady()) {
			return;
		}
	//	boolean flat = mFlatBox.isChecked();
		//for (Marker marker : mMarkerRainbow) {
			//marker.setFlat(flat);
	//	}
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		if (!checkReady()) {
			return;
		}
		//float rotation = seekBar.getProgress();
		//for (Marker marker : mMarkerRainbow) {
			//marker.setRotation(rotation);
	//	}
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// Do nothing.
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// Do nothing.
	}

	//
	// Marker related listeners.
	//

	@Override
	public boolean onMarkerClick(final Marker marker) {
		if (marker.equals(mPerth)) {
			// This causes the marker at Perth to bounce into position when it
			// is clicked.
			final Handler handler = new Handler();
			final long start = SystemClock.uptimeMillis();
			final long duration = 1500;

			final Interpolator interpolator = new BounceInterpolator();

			handler.post(new Runnable() {
				@Override
				public void run() {
					long elapsed = SystemClock.uptimeMillis() - start;
					float t = Math.max(
							1 - interpolator.getInterpolation((float) elapsed
									/ duration), 0);
					marker.setAnchor(0.5f, 1.0f + 2 * t);

					if (t > 0.0) {
						// Post again 16ms later.
						handler.postDelayed(this, 16);
					}
				}
			});
		} else if (marker.equals(mAdelaide)) {
			// This causes the marker at Adelaide to change color and alpha.
			marker.setIcon(BitmapDescriptorFactory.defaultMarker(mRandom
					.nextFloat() * 360));
			marker.setAlpha(mRandom.nextFloat());
		}
		// We return false to indicate that we have not consumed the event and
		// that we wish
		// for the default behavior to occur (which is for the camera to move
		// such that the
		// marker is centered and for the marker's info window to open, if it
		// has one).
		return false;
	}

	@Override
	public void onInfoWindowClick(Marker marker) {
		Toast.makeText(this, "Click Info Window", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onMarkerDragStart(Marker marker) {
	//	mTopText.setText("onMarkerDragStart");
	}

	@Override
	public void onMarkerDragEnd(Marker marker) {
	//	mTopText.setText("onMarkerDragEnd");
	}

	@Override
	public void onMarkerDrag(Marker marker) {
	//	mTopText.setText("onMarkerDrag.  Current Position: "
	//			+ marker.getPosition());
	}

	/*
	 * 
	 * 
	 * 
	 * 
	 * 
	 * newly added
	 */

	class LocationUpdate extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		boolean failure = false;

		@Override
		/*
		 * protected void onPreExecute() { super.onPreExecute(); pDialog = new
		 * ProgressDialog(Register.this);
		 * pDialog.setMessage("Creating User...");
		 * pDialog.setIndeterminate(false); pDialog.setCancelable(true);
		 * pDialog.show(); }
		 */
		protected String doInBackground(String... args) {
			// TODO Auto-generated method stub
			// Check for success tag
			int success;
			// String username = user.getText().toString();
			// String password = pass.getText().toString();
			try {
				// Building Parameters
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("username", username));
				params.add(new BasicNameValuePair("latitude", latitude));
				params.add(new BasicNameValuePair("longitude", longitude));

				Log.d("request!", "starting");

				// Posting user data to script
				JSONObject json = jsonParser.makeHttpRequest(LOCATION_URL,
						"POST", params);

				// full json response
				Log.d("Location updated", json.toString());

				// json success element
				success = json.getInt(TAG_SUCCESS);
				if (success == 1) {
					Log.d("User Created!", json.toString());
					// finish();
					return json.getString(TAG_MESSAGE);
				} else {
					Log.d("Login Failure!", json.getString(TAG_MESSAGE));
					return json.getString(TAG_MESSAGE);

				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;

		}

		protected void onPostExecute(String file_url) {
			// dismiss the dialog once product deleted

			new GetData().execute();
			pDialog.dismiss();
			// if (file_url != null){
			// Toast.makeText(Register.this, file_url,
			// Toast.LENGTH_LONG).show();
			// }

		}
	}

	public void updateJSONdata() {

		// Instantiate the arraylist to contain all the JSON data.
		// we are going to use a bunch of key-value pairs, referring
		// to the json element name, and the content, for example,
		// message it the tag, and "I'm awesome" as the content..

		mLocationsList = new ArrayList<HashMap<String, String>>();
		list = new ArrayList<String>();
		// Bro, it's time to power up the J parser
		JSONParser jParser = new JSONParser();
		// Feed the beast our comments url, and it spits us
		// back a JSON object. Boo-yeah Jerome.
		JSONObject json = jParser.getJSONFromUrl(READ_LOCATIONS_URL1);

		// when parsing JSON stuff, we should probably
		// try to catch any exceptions:
		try {

			// I know I said we would check if "Posts were Avail."
			// (success==1)
			// before we tried to read the individual posts, but I lied...
			// mLocationswill tell us how many "posts" or comments are
			// available
			mLocations = json.getJSONArray(TAG_POSTS);

			// looping through all posts according to the json object
			// returned
			for (int i = 0; i < mLocations.length(); i++) {
				JSONObject c = mLocations.getJSONObject(i);

				// gets the content of each tag
				String username = c.getString("username");
				String latitude = c.getString("latitude");
				String longitude = c.getString("longitude");

				// System.out.println("-----------------"+username);
				// System.out.println("-----------------"+latitude);
				// System.out.println("-----------------"+longitude);
				// creating new HashMap
				HashMap<String, String> map = new HashMap<String, String>();

				map.put("username", username);
				map.put("latitude", latitude);
				map.put("longitude", longitude);

				// adding HashList to ArrayList
				mLocationsList.add(map);

				// annndddd, our JSON data is up to date same with our array
				// list
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public class GetData extends AsyncTask<Void, Void, Boolean> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(ShowInMap.this);
			pDialog.setMessage("Loading Map...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		@Override
		protected Boolean doInBackground(Void... arg0) {
			// we will develop this method in version 2
			updateJSONdata();
			return null;

		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			loadData();
			extractlist();
		    addMarkersToMap();
			pDialog.dismiss();

			// we will develop this method in version 2
			// updateList();
		}
	}

	public void loadData() {
		list = new ArrayList<String>();
		for (int j = 0; j < mLocationsList.size(); j++) {

			HashMap<String, String> m = mLocationsList.get(j);// it will get
																// the first
																// HashMap
																// Stored in
																// array
																// list

			String user = m.get("username");
			list.add(user);
			String lat = m.get("latitude");
			list.add(lat);
			String lon = m.get("longitude");
			list.add(lon);
			

		}
		// Intent i = new Intent(ShowInMap.this,ShowInMap.class);
		// i.putStringArrayListExtra("arraylist", list);
		// startActivity(i);

	}

	/**
	 * Implementation of {@link LocationListener}.
	 */

	/**
	 * Callback called when connected to GCore. Implementation of
	 * {@link ConnectionCallbacks}.
	 */

	/**
	 * Callback called when disconnected from GCore. Implementation of
	 * {@link ConnectionCallbacks}.
	 */

	/**
	 * Implementation of {@link OnConnectionFailedListener}.
	 */

	@Override
	public boolean onMyLocationButtonClick() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onLocationChanged(Location arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onConnected(Bundle connectionHint) {
		// TODO Auto-generated method stub
		mLocationClient.requestLocationUpdates(REQUEST, this); // LocationListener

	}

	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub

	}
}
