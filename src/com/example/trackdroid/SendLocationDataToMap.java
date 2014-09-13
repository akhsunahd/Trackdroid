package com.example.trackdroid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.trackdroid.ShowMyLocation.LocationUpdate;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
//import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class SendLocationDataToMap extends FragmentActivity implements
ConnectionCallbacks,
OnConnectionFailedListener,
LocationListener,
OnMyLocationButtonClickListener {

	// Progress Dialog
	private ProgressDialog pDialog;
 
	//php read comments script
	  private GoogleMap mMap;
	   // private Button Members;
	    private Button Members, Members1;
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
    //localhost :  
    //testing on your device
    //put your local ip instead,  on windows, run CMD > ipconfig
    //or in mac's terminal type ifconfig and look for the ip under en0 or en1
   // private static final String READ_LOCATIONS_URL = "http://xxx.xxx.x.x:1234/webservice/comments.php";
    
    //testing on Emulator:
    private static final String READ_LOCATIONS_URL1 = "http://10.0.3.2/trackdroid/getlocationdata.php";
    
  //testing from a real server:
    //private static final String READ_LOCATIONS_URL = "http://www.mybringback.com/webservice/comments.php";
   
  //JSON IDS:
  //  private static final String TAG_SUCCESS = "success";
    private static final String TAG_TITLE = "title";
    private static final String TAG_POSTS = "posts";
    private static final String TAG_POST_ID = "post_id";
    private static final String TAG_USERNAME = "username";
//    private static final String TAG_MESSAGE = "message";
    private static final String LOGIN_URL = "http://10.0.3.2/trackdroid/location.php";
    //   http://10.0.3.2/trackdroid/
       // private 
       // These settings are the same as the settings for the map. They will in fact give you updates
       // at the maximal rates currently possible.
       private static final LocationRequest REQUEST = LocationRequest.create()
               .setInterval(5000)         // 5 seconds
               .setFastestInterval(16)    // 16ms = 60fps
               .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    //it's important to note that the message is both in the parent branch of 
    //our JSON tree that displays a "Post Available" or a "No Post Available" message,
    //and there is also a message for each individual post, listed under the "posts"
    //category, that displays what the user typed as their message.
    

   //An array of all of our comments
    private JSONArray mLocations= null;
    //manages all of our comments in a list.
    private ArrayList<HashMap<String, String>> mLocationsList;
     private ArrayList<String> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //note that use read_comments.xml instead of our single_post.xml
        setContentView(R.layout.send_location_data);  
        
        
        
        
        
        Members=(Button) findViewById(R.id.button1);
        
        
        Members.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	System.out.println("*****-----------------------****************************");
            	SharedPreferences example = getSharedPreferences("P1", 0);
            	username = example.getString("username", "defValue");

            	// the following will print it out in console
            //	Logger.getLogger("Name of a OutputClass".class.getName()).log(Level.INFO, userString);
            	//System.out.println("--------------------------"+username);
                               	

            	if (mLocationClient != null && mLocationClient.isConnected()) {
                    String msg = "Location = " + mLocationClient.getLastLocation();
                    
            		
            		latitude=""+mLocationClient.getLastLocation().getLatitude();
                    longitude=""+mLocationClient.getLastLocation().getLongitude();
                  
                   
                  //  temp.setText(latitude+" aaaaaaaa   "+longitude+"   ");
                   new LocationUpdate().execute();
                    
                   System.out.println("***********/////////////////*****************");
                 
            	}
            	
           
               }
           });
        
        
        
        Members1=(Button) findViewById(R.id.button2);
        
        
        Members1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
        
            	//System.out.println("*****11111111111111111111****************************");
            	new LoadComments().execute();
            	//System.out.println("***222222222222222222222***************************");
         /*
            	Thread timer=new Thread(){
           	        public void run(){
           	        	try{
           	        		sleep(2000);
           	        	}
           	        	catch(InterruptedException e ){
           	        		e.printStackTrace();
           	        	}
           	        	finally{
           	        		
           	        		System.out.println("******************************************");
           	        		Senddata();
           	        		
           	        	}
           	        	
           	        	
           	        }
           	        
           	        };
           	        timer.start();
            	*/
        
        
            }
        });
        
       
        
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
              // 	finish();
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
        * */
       protected void onPostExecute(String file_url) {
           // dismiss the dialog once product deleted
          
    	   new LoadComments().execute();
    	   pDialog.dismiss();
           //if (file_url != null){
           //	Toast.makeText(Register.this, file_url, Toast.LENGTH_LONG).show();
          // }

       }
      

	}
    @Override
    protected void onResume() {
    	// TODO Auto-generated method stub
    	super.onResume();
    	 setUpLocationClientIfNeeded();
         mLocationClient.connect();
    	//loading the comments via AsyncTask
    	/*new LoadComments().execute();
    	 Thread timer=new Thread(){
    	        public void run(){
    	        	try{
    	        		sleep(2000);
    	        	}
    	        	catch(InterruptedException e ){
    	        		e.printStackTrace();
    	        	}
    	        	finally{
    	        		Senddata();
    	        		
    	        	}
    	        	
    	        	
    	        }
    	        
    	        };
    	        timer.start();*/
        // printdata();
    }

    
    
    
    private void setUpLocationClientIfNeeded() {
        if (mLocationClient == null) {
            mLocationClient = new LocationClient(
                    getApplicationContext(),
                    this,  // ConnectionCallbacks
                    this); // OnConnectionFailedListener
        }
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
	public void addComment(View v)
    {
        //Intent i = new Intent(ReadComments.this, AddComment.class);
        //startActivity(i);
    }

    /**
     * Retrieves json data of comments
     */
	
	 /**
	     * Retrieves recent post data from the server.
	     */
	    public void updateJSONdata() {

	        // Instantiate the arraylist to contain all the JSON data.
	    	// we are going to use a bunch of key-value pairs, referring
	    	// to the json element name, and the content, for example,
	    	// message it the tag, and "I'm awesome" as the content..
	    	
	        mLocationsList = new ArrayList<HashMap<String, String>>();
	        list= new ArrayList<String>();
	        // Bro, it's time to power up the J parser 
	        JSONParser jParser = new JSONParser();
	        // Feed the beast our comments url, and it spits us
	        //back a JSON object.  Boo-yeah Jerome.
	        JSONObject json = jParser.getJSONFromUrl(READ_LOCATIONS_URL1);

	        //when parsing JSON stuff, we should probably
	        //try to catch any exceptions:
	        try {
	            
	        	//I know I said we would check if "Posts were Avail." (success==1)
	        	//before we tried to read the individual posts, but I lied...
	        	//mLocationswill tell us how many "posts" or comments are
	        	//available
	            mLocations= json.getJSONArray(TAG_POSTS);

	            // looping through all posts according to the json object returned
	            for (int i = 0; i < mLocations.length(); i++) {
	                JSONObject c = mLocations.getJSONObject(i);

	                //gets the content of each tag
	                String username = c.getString("username");
	                String latitude= c.getString("latitude");
	                String longitude= c.getString("longitude");
	                
	               // System.out.println("-----------------"+username);
	               // System.out.println("-----------------"+latitude);
	             //   System.out.println("-----------------"+longitude);
	                // creating new HashMap
	                HashMap<String, String> map = new HashMap<String, String>();
	              
	                map.put("username", username);
	                map.put("latitude", latitude);
	                map.put("longitude", longitude);
	             
	                // adding HashList to ArrayList
	                mLocationsList.add(map);
	                
	                //annndddd, our JSON data is up to date same with our array list
	            }

	        } catch (JSONException e) {
	            e.printStackTrace();
	        }
	    }
	

    /**
     * Inserts the parsed data into our listview
     */
	    /**
		 * Inserts the parsed data into the listview.
		 */
	
    public class LoadComments extends AsyncTask<Void, Void, Boolean> {

    	@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(SendLocationDataToMap.this);
			pDialog.setMessage("Loading Map...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}
        @Override
        protected Boolean doInBackground(Void... arg0) {
        	//we will develop this method in version 2
            updateJSONdata();
            return null;

        }


        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            Senddata();
            pDialog.dismiss();
           
          //we will develop this method in version 2
           // updateList();
        }
    }
    
    public void Senddata(){
    	for(int j=0;j<mLocationsList.size();j++){
    	   HashMap<String, String> m = mLocationsList.get(j);//it will get the first HashMap Stored in array list 

    	   String username= m.get("username");
    	   list.add(username);
    	    String lat= m.get("latitude");
    	    list.add(lat);
    	   String lon= m.get("longitude");
    	   list.add(lon);
    	  // System.out.println("*****"+username+"*****"+"*******"+lat+"*************"+lon);
    	   
    	}
    	Intent i = new Intent(SendLocationDataToMap.this,ShowInMap.class);
       i.putStringArrayListExtra("arraylist", list);
       startActivity(i);
    	
    	    }
    /**
     * Implementation of {@link LocationListener}.
     */
    @Override
    public void onLocationChanged(Location location) {
     //   mMessageView.setText("Location = " + location);
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
    //    Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
    }
}

