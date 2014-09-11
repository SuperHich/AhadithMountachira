package com.freelance.android.ahadith;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.widget.ProgressBar;
import android.widget.Toast;

public class doImport extends AsyncTask<String, String, String> {
	
	String URL;
	String p;
	String d;
	private static final String TAG = "MyService";

    SQLiteDatabase db1 = null;
    String TABLE = "Data";
    private static String DBNAME = "Ahadith.db";
    // public JSONObject jObj = null;

    // alldata JSONArray
    JSONArray alldata = null;
    boolean flag = false;
    int no = 0;
    ProgressBar pb;
    boolean incrementflag = true;
    String json = "";
	Context con;
	
	InputStream is = null;
	public doImport(Context co) {
		// TODO Auto-generated constructor stub
		this.con=co;
		System.out.print("wwwwwwwwwwwwwwwwwwerwerwerwe");
	}

	@SuppressWarnings("null")
	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		//Toast.makeText(con, "Welcom to our website", Toast.LENGTH_LONG).show();
		
		URL=params[0];
		p=params[1];
		d=params[2];
		
		
		System.out.println("Wellcom------------------------------");
		
		try {
            // defaultHttpClient
            if (isNetworkAvailable()) {

                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httpreq = new HttpPost(params[0]+"/?p="+params[1]+"&d="+params[2]);
                HttpResponse httpResponse = httpclient.execute(httpreq);
                HttpEntity httpEntity = httpResponse.getEntity();
                 is = httpEntity.getContent();
            }
        } catch (UnsupportedEncodingException e) {
          //  Log.d(TAG, "Message 1 : " + e.getMessage());
           // saveLogcatToFile(con);
            e.printStackTrace();
        } catch (ClientProtocolException e) {
          //  Log.d(TAG, "Message 2 : " + e.getMessage());
           // saveLogcatToFile(getApplicationContext());
            e.printStackTrace();
        } catch (IOException e) {
           // Log.d(TAG, "Message 3 : " + e.getMessage());
          //  saveLogcatToFile(getApplicationContext());
            e.printStackTrace();
        }
        // Get data from server in string
		try {


            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(is, "UTF-8"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");

            }
            is.close();

           // json = sb.toString();

            if (json == "[]") {

                

            } else {
                json = sb.toString();
                
            }
            
            try {
                // Getting Array of alldata

                alldata = new JSONArray(json);
                Pattern p = Pattern.compile("\\p{Mn}");
                int i;
                
                // looping through All alldata
                for (i = 0; i < alldata.length(); i++) {

              
                	System.out.println(i);
                   JSONObject c = (JSONObject) alldata.get(i);

                  //Storing each json item in variable
                   String hadithid = c.getString("hadith_id");
                   String hadithtext = c.getString("hadith");
                   String hadithdegree = c.getString("degree");
                   String hadithmod = c.getString("modified");
                   System.out.println("the last id inserted : " + hadithid + " in page no " + no);
                
                   

                    Matcher m = p.matcher(hadithtext);
                    m.reset();
                    String mHadith = m.replaceAll("");
                    
                    System.out.println("Hadith NO " + hadithid +"-"+alldata.length());
                   // db1 = this.con.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE,null);
                   // db1 = m2.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE,null);
                    if(!isFound(hadithid)){
                    	db1 = this.con.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE,null);
                    	db1.execSQL("INSERT INTO "
                                  + TABLE
                                 + "(HadithID ,Hadith ,MHadith ,Degree ,Modified) VALUES ("
                                  + hadithid + " , '" + hadithtext + "' , '"
                                  + mHadith + "' , '" + hadithdegree + " ', '"
                                 + hadithmod + "')");
                          	
                          db1.close();
                    }
                    else{
                    	db1 = this.con.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE,null);
                    	db1.execSQL("INSERT INTO "
                                  + TABLE
                                 + "(HadithID ,Hadith ,MHadith ,Degree ,Modified) VALUES ("
                                  + hadithid + " , '" + hadithtext + "' , '"
                                  + mHadith + "' , '" + hadithdegree + " ', '"
                                 + hadithmod + "')");
                          	
                          db1.close();
                    	
                    }
                    
                    //m2.m_hadith(hadithtext, hadithtext, hadithdegree, hadithmod);

                    //db1.execSQL(String.format("DELETE FROM %s",
                   //   TABLE));

                   // db1.execSQL("INSERT INTO "
                          //  + TABLE
                        //    + "(HadithID ,Hadith ,MHadith ,Degree ,Modified) VALUES ("
                      //      + hadithid + " , '" + hadithtext + "' , '"
                        //    + mHadith + "' , '" + hadithdegree + " ', '"
                       //     + hadithmod + "')");
                    	
                   // db1.close();
                    //db1 = openOrCreateDatabase(DBNAME,
                    //	Context.MODE_PRIVATE, null);
                    //	Cursor cursor = db1.rawQuery("SELECT * FROM " + TABLE,
                    //	null);
                    //System.out.println("size of db : " + cursor.getCount());
                    //db1.close();

                }System.out.println(i + " counter" );

            } catch (JSONException e) {
                System.out.println("Problem here");
            	e.printStackTrace();
            } finally {
                is.close();
            }

        } catch (Exception e) {
        	System.out.println("Problem here ------ "+ e.getMessage());
            e.printStackTrace();
        }
		return json;
	}
	  private boolean isFound(String hadith_id){
	    	db1 = this.con.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE,null);
	   
	    			Cursor cursor = db1.rawQuery("SELECT * FROM  " + TABLE
	                        + " WHERE  HadithID = "
	                        + Integer.parseInt(hadith_id), null);
	    			
	    			if(cursor.getCount()>0){
	    				db1.execSQL("Delete FROM " + TABLE + " WHERE HadithID = " + Integer.parseInt(hadith_id));
	    				db1.close();
	    				return true;
	    			}
	    			
	    	return false;
	    }
	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		if(result.length()> 3){
		System.out.print("HiShAm " + result.length());
		p+=1;
		new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				doImport im = new doImport(con);
				
				im.execute(URL,p,d);
			}
		}.run();
		
		}
		else{
			//Toast.makeText(con, "No Updates", Toast.LENGTH_LONG).show();
			//System.out.println("no Updates");
			
		}
		
	}
	
	  private boolean isNetworkAvailable() {
	        boolean haveConnectedWifi = false;
	        boolean haveConnectedMobile = false;

	        ConnectivityManager cm = (ConnectivityManager) con.getSystemService(Context.CONNECTIVITY_SERVICE);
	        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
	        for (NetworkInfo ni : netInfo) {
	            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
	                if (ni.isConnected())
	                    haveConnectedWifi = true;
	            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
	                if (ni.isConnected())
	                    haveConnectedMobile = true;
	        }
	        return haveConnectedWifi || haveConnectedMobile;
	    }

	

}
