
/**
 * Created by Hex on 2/18/14.
 */

package com.freelance.android.ahadith;

import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.ProgressBar;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class downloadData extends Service {

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
    JSONParse jParser;
    boolean incrementflag = true;


    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onCreate() {

        // Toast.makeText(this, "Congrats! MyService Created", Toast.LENGTH_LONG).show();
        Log.d(TAG, "onCreate");

        
        
        
        
        
        
        if (isNetworkAvailable()) {
            // Creating JSON Parser instance
            //	jParser = new JSONParse(this);

            // getting JSON string from URL
            //	jParser.execute("http://dorar.net/banner/api");

            System.out.println("network available");
            createTable(TABLE);

            //start the service from here //MyService is your service class name

        }
        Log.d(TAG, "number for start download : " + no);
        startDownlod(no);

    }

    @Override
    public void onStart(Intent intent, int startId) {
        //  Toast.makeText(this, "My Service Started", Toast.LENGTH_LONG).show();
        Log.d(TAG, "onStart");


    }

    @Override
    public void onDestroy() {
        // Toast.makeText(this, "MyService Stopped", Toast.LENGTH_LONG).show();
        Log.d(TAG, "onDestroy");
    }

    public void createTable(String tablename) {

//		try {
//			db1 = openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
//			db1.execSQL("DROP TABLE IF EXISTS " + tablename);
//
//		} catch (SQLException sqle) {
//			sqle.printStackTrace();
//		} finally {
//			db1.close();
//		}
        try {
        	 DataBaseHelper myDbHelper = new DataBaseHelper(this);
             myDbHelper = new DataBaseHelper(this);
      
             try {
      
             	myDbHelper.createDataBase();
      
      	} catch (IOException ioe) {
      
      		throw new Error("Unable to create database");
      
      	}
      
      	try {
      
      		myDbHelper.openDataBase();
      
      	}catch(SQLException sqle){
      
      		throw sqle;
      
      	}
            db1 = openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
            if (db1.isOpen()) {
                db1.execSQL("CREATE TABLE IF NOT EXISTS  "
                        + tablename
                        + "(ID INTEGER PRIMARY KEY,HadithID INTEGER , Hadith VARCHAR, MHadith VARCHAR ,Degree VARCHAR ,Modified VARCHAR  ); ");
            }
            db1.close();
        } catch (Exception e) {
            // Toast.makeText(context, "Error in creating table",
            // Toast.LENGTH_LONG).show();
            e.printStackTrace();
        } finally {
            db1.close();
        }
    }

    public void startDownlod(int no) {
        if (isNetworkAvailable()) {

            // check what section is to get url?'
            String connectionURL = "http://www.dorar.net/spreadH/api?p=" + no;
            getDataFromJason(connectionURL);

        } else {

//            try {
//                db1 = openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
//                Cursor cursor = db1.rawQuery("SELECT * FROM  " + TABLE, null);
//                if (cursor != null) {
//
//                    System.out.println("db connected : " + cursor.getCount());
//
//                    if (cursor.moveToFirst() && cursor.getCount() > 0) {
//
//                        do {
//                            // whole data of column is fetched by
//                            // getColumnIndex()
//
//                            // System.out.println("Title \n" + TITLE);
//
//                        } while (cursor.moveToNext());
//                    }
//                    // Handler handler = new Handler();
//                    // handler.postDelayed(new Runnable() {
//                    //
//                    // @Override
//                    // public void run() {
//                    // Intent intent = new Intent(SplashActivity.this,
//                    // MainActivity.class);
//                    // startActivity(intent);
//                    //
//                    // }
//                    // }, 2000);
//
//                } else {
//                    System.out.println("db noconnected");
//                }
//            } catch (SQLiteException e) {
//                e.printStackTrace();
//                // if (e.getMessage().toString().contains("no such table")) {
//                // Log.e("ERROR", "Creating table " + TABLE
//                // + "because it doesn't exist!");
//                // Toast.makeText(SplashActivity.this,
//                // "No Internet Connection", Toast.LENGTH_LONG).show();
//                //
//                // }
//            } finally {
//                db1.close();
//            }

        }

    }

    public void getDataFromJason(String url) {

        // Creating JSON Parser instance
        jParser = new JSONParse(this);

        // getting JSON string from URL
        jParser.execute(url);

        // JSONObject json = jObj;
        flag = true;
    }

    private boolean isNetworkAvailable() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
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

    private class JSONParse extends AsyncTask<String, String, String> {

        Context context;
        ProgressDialog progress;
        InputStream is = null;

        String json = "";

        public JSONParse(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
        }

        @Override
        public void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            if (incrementflag) {


                no++;
                System.out.print(no + " Hisham Ahmed Nasher");

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //Do something after 100ms
                        startDownlod(no);
                    }
                }, 500);


            } else {

                stopSelf();
            }
        }


        @Override
        protected String doInBackground(String... params) {

            System.out.println("URL IS : " + params[0]);

            try {
                // defaultHttpClient
                if (isNetworkAvailable()) {

                    HttpClient httpclient = new DefaultHttpClient();
                    HttpPost httpreq = new HttpPost(params[0]);
                    HttpResponse httpResponse = httpclient.execute(httpreq);
                    HttpEntity httpEntity = httpResponse.getEntity();
                    is = httpEntity.getContent();
                }
            } catch (UnsupportedEncodingException e) {
                Log.d(TAG, "Message 1 : " + e.getMessage());
                saveLogcatToFile(getApplicationContext());
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                Log.d(TAG, "Message 2 : " + e.getMessage());
                saveLogcatToFile(getApplicationContext());
                e.printStackTrace();
            } catch (IOException e) {
                Log.d(TAG, "Message 3 : " + e.getMessage());
                saveLogcatToFile(getApplicationContext());
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

                    incrementflag = false;
                    // Toast.makeText(this, "ﻻ توجد بيانات",
                    //  Toast.LENGTH_LONG);

                } else {
                    json = sb.toString();
                    // System.out.println(json);
                }
                // try parse the string to a JSON object
                // try {
                // jObj = new JSONObject(json);
                // } catch (JSONException e) {
                // Log.e("JSON Parser", "Error parsing data " + e.toString());
                // }
                try {
                    // Getting Array of alldata

                    alldata = new JSONArray(json);
                    Pattern p = Pattern.compile("\\p{Mn}");
                    

                    // looping through All alldata
                    for (int i = 0; i < alldata.length(); i++) {

                        System.out.println(i);
                        JSONObject c = (JSONObject) alldata.get(i);

                        // Storing each json item in variable
                        String hadithid = c.getString("hadith_id");
                        String hadithtext = c.getString("hadith");
                        String hadithdegree = c.getString("degree");
                        String hadithmod = c.getString("modified");
                        

                        Matcher m = p.matcher(hadithtext);
                        m.reset();
                        String mHadith = m.replaceAll("");

                        db1 = openOrCreateDatabase(DBNAME,
                                Context.MODE_PRIVATE, null);


                        db1.execSQL(String.format("DELETE FROM %s WHERE %s = %d",
                                TABLE, "HadithID", Integer.parseInt(hadithid)));

                        db1.execSQL("INSERT INTO "
                                + TABLE
                                + "(HadithID ,Hadith ,MHadith ,Degree ,Modified) VALUES ("
                                + hadithid + " , '" + hadithtext + "' , '"
                                + mHadith + "' , '" + hadithdegree + " ', '"
                                + hadithmod + "')");
                        	System.out.print("the last id inserted : " + hadithid + " in page no " + no);
                        db1.close();
                        //db1 = openOrCreateDatabase(DBNAME,
                        //	Context.MODE_PRIVATE, null);
                        //	Cursor cursor = db1.rawQuery("SELECT * FROM " + TABLE,
                        //	null);
                        //System.out.println("size of db : " + cursor.getCount());
                        //db1.close();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    is.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return json;
        }

    }


    public static void saveLogcatToFile(Context context) {
        String fileName = "logcat_" + System.currentTimeMillis() + ".txt";
        File outputFile = new File(context.getExternalCacheDir(), fileName);
        try {
            @SuppressWarnings("unused")
            Process process = Runtime.getRuntime().exec("logcat -f " + outputFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}