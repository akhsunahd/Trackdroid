package com.example.trackdroid;


import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

//import com.example.mysqltest.JSONParser;
//import com.example.mysqltest.Register;
//import com.example.mysqltest.Register.CreateUser;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.SupportMapFragment;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * This demo shows how GMS Location can be used to check for changes to the users location.  The
 * "My Location" button uses GMS Location to set the blue dot representing the users location. To
 * track changes to the users location on the map, we request updates from the
 * {@link LocationClient}.
 */
public class ShowMyLocation extends FragmentActivity
        implements
        ConnectionCallbacks,
        OnConnectionFailedListener,
        LocationListener,
        OnMyLocationButtonClickListener {

    private GoogleMap mMap;
   // private Button Members;
    private Button Members;
    private LocationClient mLocationClient;
    private TextView mMessageView;
    private String latitude;
    private TextView temp;
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private String longitude;
  //  private String id="new";
    private String username="";
    JSONParser jsonParser = new JSONParser();
   // private static final String LOGIN_URL = "http://myapptrackdroi.hostoi.com/location.php";
    private static final String LOGIN_URL = "http://10.0.3.2/trackdroid/location.php";
 //   http://10.0.3.2/trackdroid/
    // private 
    // These settings are the same as the settings for the map. They will in fact give you updates
    // at the maximal rates currently possible.
    private static final LocationRequest REQUEST = LocationRequest.create()
            .setInterval(5000)         // 5 seconds
            .setFastestInterval(16)    // 16ms = 60fps
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_location);
        mMessageView = (TextView) findViewById(R.id.message_text);
      //  Members=(Button) findViewById(R.id.show_members);
        temp= (TextView) findViewById(R.id.display);
       /*
        Members.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
               
            	SharedPreferences example = getSharedPreferences("P1", 0);
            	username = example.getString("username", "defValue");

            	// the following will print it out in console
            //	Logger.getLogger("Name of a OutputClass".class.getName()).log(Level.INFO, userString);
            	//System.out.println("--------------------------"+username);
                               	

            	if (mLocationClient != null && mLocationClient.isConnected()) {
                   // String msg = "Location = " + mLocationClient.getLastLocation();
                    latitude=""+mLocationClient.getLastLocation().getLatitude();
                    longitude=""+mLocationClient.getLastLocation().getLongitude();
                  
                   
                  //  temp.setText(latitude+" aaaaaaaa   "+longitude+"   ");
                   new LocationUpdate().execute();
                    
                    
                  Toast.makeText(getApplicationContext(),"", Toast.LENGTH_SHORT).show();
                }
                 //startActivity(intent); //<<< start Activity here
               }
           });
        
        */
        
         
    }
    public void showMyMembers(){
    //	new LocationUpdate().execute();	
    }

    
    
    class LocationUpdate extends AsyncTask<String, String, String> {

		 /**
        * Before starting background thread Show Progress Dialog
        * */
		boolean failure = false;

       @Override
      /* protected void onPreExecute() {
           super.onPreExecute();
           pDialog = new ProgressDialog(Register.this);
           pDialog.setMessage("Creating User...");
           pDialog.setIndeterminate(false);
           pDialog.setCancelable(true);
           pDialog.show();
       }
       */

		
		protected String doInBackground(String... args) {
			// TODO Auto-generated method stub
			 // Check for success tag
           int success;
        //   String username = user.getText().toString();
          // String password = pass.getText().toString();
           try {
               // Building Parameters
               List<NameValuePair> params = new ArrayList<NameValuePair>();
               params.add(new BasicNameValuePair("username", username));
               params.add(new BasicNameValuePair("latitude", latitude));
               params.add(new BasicNameValuePair("longitude", longitude));

               Log.d("request!", "starting");

               //Posting user data to script
               JSONObject json = jsonParser.makeHttpRequest(
                      LOGIN_URL, "POST", params);

               // full json response
               Log.d("Login attempt", json.toString());

               // json success element
               success = json.getInt(TAG_SUCCESS);
               if (success == 1) {
               	Log.d("User Created!", json.toString());
               	finish();
               	return json.getString(TAG_MESSAGE);
               }else{
               	Log.d("Login Failure!", json.getString(TAG_MESSAGE));
               	return json.getString(TAG_MESSAGE);

               }
           } catch (JSONException e) {
               e.printStackTrace();
           }

           return null;

		}
		/**
        * After completing background task Dismiss the progress dialog
        * *
       protected void onPostExecute(String file_url) {
           // dismiss the dialog once product deleted
           pDialog.dismiss();
           if (file_url != null){
           	Toast.makeText(Register.this, file_url, Toast.LENGTH_LONG).show();
           }

       }
       */

	}
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    public void showMyMembers(View view) {
        if (mLocationClient != null && mLocationClient.isConnected()) {
            String msg = "Location = " + mLocationClient.getLastLocation();
            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
        }
    }
    
    
  
    
    
    
    
    
    
    
    
    
    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
        setUpLocationClientIfNeeded();
        mLocationClient.connect();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mLocationClient != null) {
            mLocationClient.disconnect();
        }
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                mMap.setMyLocationEnabled(true);
                mMap.setOnMyLocationButtonClickListener(this);
            }
        }
    }

    private void setUpLocationClientIfNeeded() {
        if (mLocationClient == null) {
            mLocationClient = new LocationClient(
                    getApplicationContext(),
                    this,  // ConnectionCallbacks
                    this); // OnConnectionFailedListener
        }
    }

    /**
     * Button to get current Location. This demonstrates how to get the current Location as required
     * without needing to register a LocationListener.
     */
    public void showMyLocation(View view) {
        if (mLocationClient != null && mLocationClient.isConnected()) {
            String msg = "Location = " + mLocationClient.getLastLocation();
            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Implementation of {@link LocationListener}.
     */
    @Override
    public void onLocationChanged(Location location) {
        mMessageView.setText("Location = " + location);
    }

    /**
     * Callback called when connected to GCore. Implementation of {@link ConnectionCallbacks}.
     */
    @Override
    public void onConnected(Bundle connectionHint) {
        mLocationClient.requestLocationUpdates(
                REQUEST,
                this);  // LocationListener
    }

    /**
     * Callback called when disconnected from GCore. Implementation of {@link ConnectionCallbacks}.
     */
    @Override
    public void onDisconnected() {
        // Do nothing
    }

    /**
     * Implementation of {@link OnConnectionFailedListener}.
     */
    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // Do nothing
    }

    @Override
    public boolean onMyLocationButtonClick() {
      //  Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
    }
}

