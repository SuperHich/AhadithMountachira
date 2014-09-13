package com.freelance.android.ahadith;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;


@SuppressLint({ "NewApi", "CutPasteId" })
public class MainActivity extends Activity {

    Button raslnaBtn, browseBtn;
    EditText homeSearchText;
    LinearLayout mainLayout;
    Button about, add, search, share, homeSearchBtn;
    public static boolean refreshDoing=false;
    ProgressDialog pd;

    SQLiteDatabase db1 = null;
    String TABLE = "Data";
    private static String DBNAME = "Ahadith.db";

    ArrayList<String> hadList = new ArrayList<String>();
    ArrayList<Integer> hadids = new ArrayList<Integer>();

    int no = 0;
    boolean isAddHadith = false;
    static boolean a=false;
    String MaxDate ="";
    doImport importData;
    
    private String getDataMax(){
    	String dateString = "";
    	
        db1 = openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
        Cursor cursor = db1.rawQuery("SELECT max(Modified) FROM " + TABLE +"", null);
    	cursor.moveToFirst();
    	dateString = cursor.getString(0);
    	return dateString;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
     
    try{
    	if (isNetworkAvailable()){
     		String URL = "http://dorar.net/spreadH/api/u&";
    			
    			if(!a){
     			importData = new doImport(MainActivity.this);
    			
    			importData.execute(URL,"0",getDataMax());
    			//Toast.makeText(getApplicationContext(), "There is Network", Toast.LENGTH_LONG).show();
    			a=true;
    			}
    			
     	}
     		
    }
    catch(Exception ex){
    	
    }
  	
    	
    	
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
       //Toast.makeText(getApplicationContext(), "Created", Toast.LENGTH_LONG).show();
        Locale locale = new Locale("en_US");
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getApplicationContext().getResources().updateConfiguration(config, null);
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        Locale.setDefault(new Locale("en", "US"));


        raslnaBtn = (Button) findViewById(R.id.raslnabtn);
        browseBtn = (Button) findViewById(R.id.browsebtn);
        mainLayout = (LinearLayout) findViewById(R.id.mainlayout);
        about = (Button) findViewById(R.id.aboutbtn);
        add = (Button) findViewById(R.id.addbtn);
        search = (Button) findViewById(R.id.searchbtn);
        share = (Button) findViewById(R.id.sharebtn);

        homeSearchBtn = (Button) findViewById(R.id.homesearchbtn);
        homeSearchText = (EditText) findViewById(R.id.home_search_txt);
        homeSearchText.clearFocus();

        if (getIntent().getExtras() != null) {
            no = getIntent().getExtras().getInt("no");
        }
        homeSearchText.setOnEditorActionListener(new OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                // TODO Auto-generated method stub
                search();
                return false;
            }
        });

        homeSearchBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                search();
            }
        });
        raslnaBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // Intent raslnaIntent = new Intent(MainActivity.this,
                // MailusActivity.class);
                // raslnaIntent.putExtra("no", no);
                // startActivity(raslnaIntent);

//                Intent email = new Intent(Intent.ACTION_SEND);
//                email.putExtra(Intent.EXTRA_EMAIL,
//                        new String[]{"apps@dorar.net"});
//                email.putExtra(Intent.EXTRA_SUBJECT,
//                        "رسالة بخصوص تطبيق احاديث منتشرة");
//                email.putExtra(Intent.EXTRA_TEXT, " ");
//                email.setType("message/rfc822");
//                startActivity(Intent.createChooser(email,
//                        "Choose an Email client :"));
                
            	Utils.shareWithMail(MainActivity.this, "apps@dorar.net", "رسالة بخصوص تطبيق احاديث منتشرة", " ", "Choose an Email client :");

            }
        });


        browseBtn.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
            	
                Intent browseIntent = new Intent(MainActivity.this,
                        AhadithList.class);

                browseIntent.putExtra("status", "all");
                browseIntent.putExtra("no", no);
                startActivity(browseIntent);
            }


        });
        about.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {


                if (Build.VERSION.SDK_INT < 16)
                    getApplicationContext().getResources().getDrawable(R.drawable.home_about);
                else
                    mainLayout.setBackground(getApplicationContext().getResources()
                            .getDrawable(R.drawable.home_about));


                // TODO Auto-generated method stub

                return false;
            }
        });
        about.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT < 16)
                    getApplicationContext().getResources().getDrawable(R.drawable.home_about);
                else
                    mainLayout.setBackground(getApplicationContext().getResources()
                            .getDrawable(R.drawable.home_about));


                Intent aboutintent = new Intent(MainActivity.this,
                        AboutActivity1.class);
                aboutintent.putExtra("no", no);
                startActivity(aboutintent);


            }
        });
        add.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub

                if (Build.VERSION.SDK_INT < 16)
                    getApplicationContext().getResources().getDrawable(R.drawable.home_add);
                else
                    mainLayout.setBackground(getApplicationContext().getResources()
                            .getDrawable(R.drawable.home_add));


                return false;
            }
        });
        add.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // if (Build.VERSION.SDK_INT < 16)
                // getApplicationContext().getResources().getDrawable(
                // R.drawable.home_add_with_bar2);
                // else
                if (Build.VERSION.SDK_INT < 16)
                    getApplicationContext().getResources().getDrawable(R.drawable.home_add);
                else
                    mainLayout.setBackground(getApplicationContext().getResources()
                            .getDrawable(R.drawable.home_add));

                // Intent addintent = new Intent(MainActivity.this,
                // AddHadithActivity.class);
                //
                // addintent.putExtra("no", no);
                // startActivity(addintent);
                
                isAddHadith = true;
//                Intent email = new Intent(Intent.ACTION_SEND);
//                email.putExtra(Intent.EXTRA_EMAIL,
//                        new String[]{"montashera@dorar.net"});
//                email.putExtra(Intent.EXTRA_SUBJECT, "أضف حديثا منتشرا ");
//                email.putExtra(Intent.EXTRA_TEXT, " ");
//                email.setType("message/rfc822");
//                startActivity(Intent.createChooser(email,
//                        "Choose an Email client :"));
                
                
                Utils.shareWithMail(MainActivity.this, "montashera@dorar.net", "أضف حديثا منتشرا ", " ", "Choose an Email client :");
                
//				mainLayout.setBackground(getApplicationContext().getResources()
//						.getDrawable(R.drawable.home_default4));
            }
        });
        search.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                if (Build.VERSION.SDK_INT < 16)
                    getApplicationContext().getResources().getDrawable(R.drawable.home_search2);
                else
                    mainLayout.setBackground(getApplicationContext().getResources()
                            .getDrawable(R.drawable.home_search2));

                return false;
            }
        });
        search.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT < 16)
                    getApplicationContext().getResources().getDrawable(R.drawable.home_search2);
                else
                    mainLayout.setBackground(getApplicationContext().getResources()
                            .getDrawable(R.drawable.home_search2));

                Intent searchintent = new Intent(MainActivity.this,
                        SearchActivity.class);

                searchintent.putExtra("no", no);
                startActivity(searchintent);
            }
        });
        share.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                if (Build.VERSION.SDK_INT < 16)
                    getApplicationContext().getResources().getDrawable(R.drawable.home_share);
                else
                    mainLayout.setBackground(getApplicationContext().getResources()
                            .getDrawable(R.drawable.home_share));

                return false;
            }
        });
        share.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                // if (Build.VERSION.SDK_INT < 16)
                // getApplicationContext().getResources().getDrawable(
                // R.drawable.home_share_with_bar);
                // else
                if (Build.VERSION.SDK_INT < 16)
                    getApplicationContext().getResources().getDrawable(R.drawable.home_share);
                else
                    mainLayout.setBackground(getApplicationContext().getResources()
                            .getDrawable(R.drawable.home_share));

                Intent shareIntent = new Intent(MainActivity.this,
                        ShareActivity.class);
                shareIntent.putExtra("no", no);
                startActivity(shareIntent);
            }
        });

        if (Build.VERSION.SDK_INT < 16)
            getApplicationContext().getResources().getDrawable(R.drawable.home_default);
        else
            mainLayout.setBackground(getApplicationContext().getResources()
                    .getDrawable(R.drawable.home_default));
        
       	

    }

    public ArrayList<String> getAllAhadith() {
        try {
        	
            db1 = openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
            Cursor cursor = db1.rawQuery("SELECT MHadith FROM " + TABLE +"WHERE HadithID < 700 ORDER BY hadith_id ", null);
            if (cursor != null) {
                if (cursor.moveToFirst() && cursor.getCount() > 0) {

                    do {
                        // whole data of column is fetched by
                        // getColumnIndex()
                        String had = cursor.getString(cursor
                                .getColumnIndex("MHadith"));

                        hadList.add(had);

                    } while (cursor.moveToNext());
                }
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {

            db1.close();
        }
        return hadList;
    }

    @Override
    protected void onPause() {

        int height = mainLayout.getHeight();
        int width = mainLayout.getWidth();
        
        System.out.println(height);
        System.out.println(width);
        //Toast.makeText(getApplicationContext(), "onPause", Toast.LENGTH_LONG).show();
        hadids.clear();
        hadList.clear();
        // finish();
        super.onPause();
    }

    @Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		//Toast.makeText(getApplicationContext(), "onRestart", Toast.LENGTH_LONG).show();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		//Toast.makeText(getApplicationContext(), "onStart", Toast.LENGTH_LONG).show();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		//Toast.makeText(getApplicationContext(), "onStop", Toast.LENGTH_LONG).show();
	}

	@Override
    protected void onResume() {
        // TODO Auto-generated method stub
    	// Toast.makeText(getApplicationContext(), "onResume", Toast.LENGTH_LONG).show();
        if (Build.VERSION.SDK_INT < 16)
            getApplicationContext().getResources().getDrawable(R.drawable.home_default);
        else
            mainLayout.setBackground(getApplicationContext().getResources()
                    .getDrawable(R.drawable.home_default));

        super.onResume();
    }

    private void search() {

       try{
    	   if (homeSearchText.getText().toString().equals("")
                   || homeSearchText.getText().toString().equals(null)) {

               Toast.makeText(MainActivity.this, "أدخل كلمة / عبارة البحث ",
                       Toast.LENGTH_LONG).show();
               return ;

           } else {
//           	pd = ProgressDialog.show(this,"","جاري البحث ...",true);
//   			String searchValue = homeSearchText.getText().toString();
//   			searchValue.replaceAll("'"," ");
//            searchValue.replaceAll(".", " ");
//            String b1 = "\\(";
//            String b2 = "\\)";
//            searchValue.replaceAll(",", " ");	
//            searchValue.replaceAll("،", " ");
//            searchValue.replaceAll(b1, " ");
//            searchValue.replaceAll(b2, " ");
//            String[] ar = searchValue.split(" ");
//   			try{
//   				String sss="REPLACE(REPLACE(REPLACE(REPLACE(MHadith,'،',' '),'.',' '),'" + b1 + "',' '),'\\)',' ')";
//                   db1 = openOrCreateDatabase(DBNAME,
//                           Context.MODE_PRIVATE, null);
//                   Cursor cursor = db1.rawQuery("SELECT * FROM  " + TABLE
//                           + " WHERE REPLACE("+ sss +","+ "'و'" +",' ') like '% "
//                           + searchValue + " %'", null);
//
//                   
//                   if(cursor.getCount()!=0 && cursor!=null){
//                   	cursor.moveToFirst();
//                   	do{
//                   		
//                   		int hadid = cursor.getInt(cursor.getColumnIndex("HadithID"));
//                   		hadids.add(hadid);	
//                   		
//                   	}while(cursor.moveToNext());
//                   	
//                   	
//                   	
//                   	//Toast.makeText(getApplicationContext(), String.valueOf(cursor.getCount()), Toast.LENGTH_LONG).show();
//                   	
//                   }
//                   else{
//                   
//                  // Toast.makeText(getApplicationContext(), "لا يوجد للبحث نتائج", Toast.LENGTH_LONG).show();
//                   }
//                  // Toast.makeText(getApplicationContext(), String.valueOf(cursor.getCount()), Toast.LENGTH_LONG).show();
//
//   			}
//   			catch(Exception ex){
//   				//Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
//   				pd.cancel();
//   				
//   			}
//   			finally{
//   				 pd.cancel();
//   				db1.close();
//   			}
        	   
        	   pd = ProgressDialog.show(this,"","جاري البحث ...",true);
   			String searchValue =homeSearchText.getText().toString();
   			
   			try{
                   db1 = openOrCreateDatabase(DBNAME,
                           Context.MODE_PRIVATE, null);
                   Cursor cursor = db1.rawQuery("SELECT * FROM  " + TABLE
                           + " WHERE MHadith like '%"
                           + searchValue + "%'", null);

                   
                   if(cursor.getCount()!=0 && cursor!=null){
                   	cursor.moveToFirst();
                   	do{
                   		
                   		int hadid = cursor.getInt(cursor.getColumnIndex("HadithID"));
                   		hadids.add(hadid);	
                   		
                   	}while(cursor.moveToNext());
                   	
                   	
                   	
                   	//Toast.makeText(getApplicationContext(), String.valueOf(cursor.getCount()), Toast.LENGTH_LONG).show();
                   	
                   }
                   else{
//                   	Toast.makeText(getApplicationContext(), "لا يوجد للبحث نتائج", Toast.LENGTH_LONG).show();
                   }
                   

   			}
   			catch(Exception ex){
   				//Toast.makeText(getApplicationContext(), "لا يوجد للبحث نتائج", Toast.LENGTH_LONG).show();
   				//pd.cancel();
   				
   			}
   			finally{
   				 pd.cancel();
   				db1.close();
   			}
              


               }

    	   
               if (!hadids.isEmpty()) {
            	   pd.cancel();

                   Intent intent = new Intent(MainActivity.this, AhadithList.class);
                   intent.putExtra("status", "search");
                   intent.putExtra("search", true);
                   intent.putIntegerArrayListExtra("idList", hadids);
                   intent.putExtra("no", no);
                   intent.putExtra("count", hadids.size());
                   startActivity(intent);
               } else {pd.cancel();
               	
            	   Toast.makeText(getApplicationContext(), "لا يوجد للبحث نتائج", Toast.LENGTH_LONG).show();
               	
               	

                 
                   

               }
       }
       catch(Exception ex){
    	   //Toast.makeText(getApplicationContext(), "تأكد من اتصالاك بالانترنت", Toast.LENGTH_LONG).show(); 
       }
    	

        }

    private static boolean IsMatch(String s, String pattern) {
        try {
            Pattern patt = Pattern.compile(pattern);
            Matcher matcher = patt.matcher(s);
            return matcher.matches();
        } catch (RuntimeException e) {
            return false;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK ) {

           moveTaskToBack(true);
        }
        return super.onKeyDown(keyCode, event);
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
    
    public  boolean m_hadith(String hadith_id ,String hadith, String degree,String modified){
    	isFound(hadith_id);

    	return true;
    }
    
    
    private boolean isFound(String hadith_id){
    	db1 = openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
   
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
    
}



