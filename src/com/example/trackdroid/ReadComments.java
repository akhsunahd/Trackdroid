package com.example.trackdroid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
//import android.preference.PreferenceManager;
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

public class ReadComments extends ListActivity {

	// Progress Dialog
	private ProgressDialog pDialog;
 
	//php read comments script
    
    //localhost :  
    //testing on your device
    //put your local ip instead,  on windows, run CMD > ipconfig
    //or in mac's terminal type ifconfig and look for the ip under en0 or en1
   // private static final String READ_COMMENTS_URL = "http://xxx.xxx.x.x:1234/webservice/comments.php";
    
    //testing on Emulator:
    private static final String READ_COMMENTS_URL = "http://10.0.3.2/trackdroid/getlocationdata.php";
    
  //testing from a real server:
    //private static final String READ_COMMENTS_URL = "http://www.mybringback.com/webservice/comments.php";
   
  //JSON IDS:
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_TITLE = "title";
    private static final String TAG_POSTS = "posts";
    private static final String TAG_POST_ID = "post_id";
    private static final String TAG_USERNAME = "username";
    private static final String TAG_MESSAGE = "message";
    //it's important to note that the message is both in the parent branch of 
    //our JSON tree that displays a "Post Available" or a "No Post Available" message,
    //and there is also a message for each individual post, listed under the "posts"
    //category, that displays what the user typed as their message.
    

   //An array of all of our comments
    private JSONArray mComments = null;
    //manages all of our comments in a list.
    private ArrayList<HashMap<String, String>> mCommentList;
     private ArrayList<String> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //note that use read_comments.xml instead of our single_post.xml
        setContentView(R.layout.read_comments);  
       
        
    }
    
    @Override
    protected void onResume() {
    	// TODO Auto-generated method stub
    	super.onResume();
    	//loading the comments via AsyncTask
    	new LoadComments().execute();
    	 Thread timer=new Thread(){
    	        public void run(){
    	        	try{
    	        		sleep(2000);
    	        	}
    	        	catch(InterruptedException e ){
    	        		e.printStackTrace();
    	        	}
    	        	finally{
    	        		printdata();
    	        		
    	        	}
    	        	
    	        	
    	        }
    	        
    	        };
    	        timer.start();
        // printdata();
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
	    	
	        mCommentList = new ArrayList<HashMap<String, String>>();
	        list= new ArrayList<String>();
	        // Bro, it's time to power up the J parser 
	        JSONParser jParser = new JSONParser();
	        // Feed the beast our comments url, and it spits us
	        //back a JSON object.  Boo-yeah Jerome.
	        JSONObject json = jParser.getJSONFromUrl(READ_COMMENTS_URL);

	        //when parsing JSON stuff, we should probably
	        //try to catch any exceptions:
	        try {
	            
	        	//I know I said we would check if "Posts were Avail." (success==1)
	        	//before we tried to read the individual posts, but I lied...
	        	//mComments will tell us how many "posts" or comments are
	        	//available
	            mComments = json.getJSONArray(TAG_POSTS);

	            // looping through all posts according to the json object returned
	            for (int i = 0; i < mComments.length(); i++) {
	                JSONObject c = mComments.getJSONObject(i);

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
	                mCommentList.add(map);
	                
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
			pDialog = new ProgressDialog(ReadComments.this);
			pDialog.setMessage("Loading Comments...");
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
            pDialog.dismiss();
          //we will develop this method in version 2
           // updateList();
        }
    }
    
    public void printdata(){
    	for(int j=0;j<mCommentList.size();j++){
    	   HashMap<String, String> m = mCommentList.get(j);//it will get the first HashMap Stored in array list 

    	   String username= m.get("username");
    	   list.add(username);
    	    String lat= m.get("latitude");
    	    list.add(lat);
    	   String lon= m.get("longitude");
    	   list.add(lon);
    	  // System.out.println("*****"+username+"*****"+"*******"+lat+"*************"+lon);
    	   
    	}
    	Intent i = new Intent(ReadComments.this,MarkerDemoActivity.class);
       i.putStringArrayListExtra("arraylist", list);
       startActivity(i);
    	
    	    }
}