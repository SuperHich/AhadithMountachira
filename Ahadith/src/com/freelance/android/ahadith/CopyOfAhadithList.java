
package com.freelance.android.ahadith;

import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bugsense.trace.BugSenseHandler;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

@SuppressLint("NewApi")
public class CopyOfAhadithList extends ListActivity {

	ImageView adImage;
	Button btnNext;
	Button btnPrev;
	Button btnMin;
	Button btnMax;
	Button btnHome;
	TextView txtTitle;
	TextView txtHadith;

	TextView txtStatus;

	float hadithFontSize;
	int hadithCounter = 0;
	int pagesCount;
	int currentPage = 1;
	int searchResultsCount=1;
	public static float fonting;
	String prfName = "prefs";
	String link;
	String status;
	SQLiteDatabase db1 = null;
	String TABLE = "Data";
	private static String DBNAME = "Ahadith.db";

	boolean fromSearch = false;
	AhadithListModel model;

	ArrayList<ArrayList<String>> pagesCintentLists = new ArrayList<ArrayList<String>>();

	int no = 0;

	static Context applicationContext;
	ArrayList<String> hadList = new ArrayList<String>();
	ArrayList<String> hadListIDs = new ArrayList<String>();
	ArrayList<String> degList = new ArrayList<String>();
	URL imageurl = null;
	int remain = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		BugSenseHandler.initAndStartSession(CopyOfAhadithList.this, "bfdfcbf3");

		setContentView(R.layout.ahadith_list);

		applicationContext = this.getApplicationContext();

		hadithFontSize = 14;

		btnNext = (Button) findViewById(R.id.btn_prev);
		btnPrev = (Button) findViewById(R.id.btn_next);
		btnMax = (Button) findViewById(R.id.plusbtn);
		btnMin = (Button) findViewById(R.id.minbtn);
		btnNext.setVisibility(View.GONE);
		btnPrev.setVisibility(View.GONE);
		adImage = (ImageView) findViewById(R.id.adimage);
		btnHome = (Button) findViewById(R.id.homebtn);
		txtTitle = (TextView) findViewById(R.id.txt_title);
		txtHadith = (TextView) findViewById(R.id.hadith_txt);
		txtStatus = (TextView) findViewById(R.id.txt_status);



		Typeface face = Typeface.createFromAsset(this.getAssets(),
				"hacen_saudi_arabia.ttf");

		txtTitle.setTypeface(face);

		status = getIntent().getExtras().getString("status");
		no = getIntent().getExtras().getInt("no");

		fromSearch = getIntent().getExtras().getBoolean("search");
		searchResultsCount=getIntent().getExtras().getInt("count");

		if (isNetworkAvailable()) {

			System.out.println("network available");

			new LoadImage().execute("http://www.dorar.net/banner/api");

		}

		SharedPreferences prfs = getSharedPreferences(prfName,
				Context.MODE_PRIVATE);
		String url = prfs.getString("image_url", "");
		link = prfs.getString("image_link", "");

		// new LoadImage().execute(url);
		adImage.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				try {

					Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri
							.parse(link));
					startActivity(browserIntent);

				} catch (Exception e) {
					e.printStackTrace();
				}
				// Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri
				// .parse(link));
				// startActivity(browserIntent);

			}
		});

		hadithCounter = 15;

        if (status.equals("all")) {

			hadList = getAllAhadith();
			if (hadList.size() > 15) {
				btnNext.setVisibility(View.VISIBLE);
				hadithCounter = 15;

			}

		} else if (status.equals("search")) {

			btnNext.setVisibility(View.VISIBLE);
			ArrayList<Integer> ids = getIntent().getExtras()
					.getIntegerArrayList("idList");
			// System.out.println("I'm in the list ID = " + ids.get(0));
			getSomeAhadith(ids);

		}

        int count = 0;
		int times = 1;

		ArrayList<String> pageContentList = new ArrayList<String>();
		for (int i = 0; i < hadList.size(); i++) {

			if (count == 15) {
				// System.out.println("ii : " + i);
				count = 0;
				times++;
				pagesCintentLists.add(pageContentList);
				pageContentList = new ArrayList<String>();
				pageContentList.add(hadList.get(i));
				count++;
			} else {
				// System.out.println("i : " + i);
				pageContentList.add(hadList.get(i));
				count++;
			}
			if ((times * 15) + count == hadList.size()) {
				// System.out.println("iii : " + i);
				pagesCintentLists.add(pageContentList);
			}

		}
		System.out.println("hadith List Size : ------------------ "
				+ pagesCintentLists.size());
		// pagesCount = Math.round(hadList.size() / 15);
		if (pagesCintentLists.size() > 0) {
			for (int i = 0; i < pagesCintentLists.get(0).size(); i++) {
				System.out.println("x + (i ) : " + (i));
				String currentHadith = hadList.get(i);
				String currentHagithDegree = degList.get(i);
				String currentHadithID = hadListIDs.get(i);
				sethadList(currentHadithID, currentHadith, currentHagithDegree);
			}
			setListAdapter(new AhadithListAdapter(CopyOfAhadithList.this,
					gethadList()));
		}
		pagesCount = pagesCintentLists.size();
		if (pagesCount == 0) {
			pagesCount = 1;
			btnNext.setVisibility(View.GONE);
		}

		txtStatus.setText("<<  " + (pagesCount) + " / " + currentPage  + "  >>");

		btnNext.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				btnPrev.setVisibility(View.VISIBLE);
				modelList.clear();
				hadithCounter += 15;
				if (currentPage == pagesCount - 1) {
					btnNext.setVisibility(View.INVISIBLE);
				}
				int x = (currentPage) * 15;
				for (int i = 0; i < pagesCintentLists.get(currentPage - 1)
						.size(); i++) {
					System.out.println("x + (i ) : " + (x + i ));
					if (hadList.size() > (x + i )) {
						String currentHadith = hadList.get(x + i);
						String currentHagithDegree = degList.get(x + i );
						String currentHadithID = hadListIDs.get(x + i );
						sethadList(currentHadithID, currentHadith,
								currentHagithDegree);
					}
				}

				setListAdapter(new AhadithListAdapter(hadithCounter - 15));
				currentPage++;
				txtStatus.setText("<<  " + currentPage + " / " + (pagesCount)
						+ "  >>");

			}
		});

		btnPrev.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				btnNext.setVisibility(View.VISIBLE);
				modelList.clear();
				if (currentPage == 2) {
					btnPrev.setVisibility(View.INVISIBLE);
				}

				currentPage--;
				System.out.println("currentPage fffffffffffffffffffff : "
						+ currentPage);
				int x = ((currentPage - 1) * 15);
				if (currentPage == 1) {
					for (int i = 0; i < pagesCintentLists.get(0).size(); i++) {
						System.out.println("x + (i ) : " + (i));
						String currentHadith = hadList.get(i);
						String currentHagithDegree = degList.get(i);
						String currentHadithID = hadListIDs.get(i);
						sethadList(currentHadithID, currentHadith,
								currentHagithDegree);
					}
				} else {
					for (int i = 0; i < pagesCintentLists.get(currentPage - 1)
							.size(); i++) {
						System.out.println("xxx + (i ) : " + ((x + i + 1)));
						String currentHadith = hadList.get(x + i + 1);
						String currentHagithDegree = degList.get(x + i + 1);
						String currentHadithID = hadListIDs.get(x + i + 1);
						sethadList(currentHadithID, currentHadith,
								currentHagithDegree);
					}
				}
				//setListAdapter(new AhadithListAdapter(AhadithList.this,
					//	gethadList()));

				hadithCounter -= 15;

				setListAdapter(new AhadithListAdapter(hadithCounter - 15));

				txtStatus.setText("<<  " + currentPage + " / " + (pagesCount)
						+ "  >>");

			}
		});

		btnHome.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(CopyOfAhadithList.this, MainActivity.class);
				intent.putExtra("no", no);
				startActivity(intent);
				finish();
			}
		});

		btnMax.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				hadithFontSize += 2;
				fonting = hadithFontSize;
				//TextView tv = (TextView)findViewById(R.id.hadith_deg);
				
				//tv.setTextSize(hadithFontSize);
				
				try{
					
					new  Runnable() {
						LinearLayout lr = (LinearLayout) findViewById(R.id.listing2);
						//lr.removeAllViews();
						ListView v1 =(ListView) lr.getChildAt(0);
						int i=0;
						@SuppressWarnings("null")
						public void run() {
							TextView tv1=null;
							TextView tv2=null;
							TextView tv3=null;
							View v2 = null;
							for(i=0;i<v1.getChildCount();i++){
								
								v2= v1.getChildAt(i);
								tv1 = (TextView) v2.findViewById(R.id.hadith_txt);
								tv2=(TextView) v2.findViewById(R.id.hadith_deg_title);
								tv3=(TextView) v2.findViewById(R.id.hadith_deg);
								
								tv1.setTextSize(hadithFontSize);
								tv2.setTextSize(hadithFontSize);
								tv3.setTextSize(hadithFontSize);
								tv1.setTextSize(hadithFontSize);
								tv2.setTextSize(hadithFontSize);
								tv3.setTextSize(hadithFontSize);
								tv1.setTextSize(hadithFontSize);
								tv2.setTextSize(hadithFontSize);
								tv3.setTextSize(hadithFontSize);
								//v2.setAlpha(0);
								try {
									Thread.sleep(100);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								}	
							
							
							
						}
					}.run();
					
					//System.out.print("Count : " + AhadithListAdapter.listView.getChildCount());
					
					
					
					
				}
				catch(Exception ex){
					
					
					
					
					
					
				}
				//setListAdapter(new AhadithListAdapter(hadithFontSize));

			}
		});
		btnMin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				TextView tv1 = (TextView) findViewById(R.id.hadith_txt);
				float fontsize = tv1.getTextSize();
				Toast.makeText(getApplicationContext(), "هذا الحد الادنى لحجم الخط" + fontsize, Toast.LENGTH_LONG).show();
				if(fontsize>36){
					hadithFontSize -= 2;
					fonting = hadithFontSize;
					Toast.makeText(getApplicationContext(), "هذا الحد الادنى لحجم الخط" + hadithFontSize, Toast.LENGTH_LONG).show();
				}
				//else{
				
				//}
				
				//setListAdapter(new AhadithListAdapter(hadithFontSize));
try{
					
	if(hadithFontSize>14){				
	new  Runnable() {
						LinearLayout lr = (LinearLayout) findViewById(R.id.listing2);
						//lr.removeAllViews();
						ListView v1 =(ListView) lr.getChildAt(0);
						int i=0;
						@SuppressWarnings("null")
						public void run() {
							TextView tv1=null;
							TextView tv2=null;
							TextView tv3=null;
							View v2 = null;
							for(i=0;i<v1.getChildCount();i++){
								
								v2= v1.getChildAt(i);
								tv1 = (TextView) v2.findViewById(R.id.hadith_txt);
								tv2=(TextView) v2.findViewById(R.id.hadith_deg_title);
								tv3=(TextView) v2.findViewById(R.id.hadith_deg);
								if(tv1.getTextSize()>14){
									tv1.setTextSize(hadithFontSize);
									tv2.setTextSize(hadithFontSize);
									tv3.setTextSize(hadithFontSize);
									tv1.setTextSize(hadithFontSize);
									tv2.setTextSize(hadithFontSize);
									tv3.setTextSize(hadithFontSize);
									tv1.setTextSize(hadithFontSize);
									tv2.setTextSize(hadithFontSize);
									tv3.setTextSize(hadithFontSize);
								}
								else{
									
									Toast.makeText(getApplicationContext(), "هذا الحد الادنى لحجم الخط" + hadithFontSize, Toast.LENGTH_LONG).show();
								}
								
								//v2.setAlpha(0);
								try {
									Thread.sleep(100);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								}	
							
							
							
						}
					}.run();
	}
					
					//System.out.print("Count : " + AhadithListAdapter.listView.getChildCount());
					
					
					
					
				}
				catch(Exception ex){
					
					
					
					
					
					
				}
			}
		});
		if (fromSearch) {
			if (hadList.size() == 1) {
				txtTitle.setText((searchResultsCount ) + " نتيجةبحث");
			} else {
				txtTitle.setText((searchResultsCount) + " نتيجةبحث");
			}
		}
	}

	public ArrayList<String> getSomeAhadith(ArrayList<Integer> listofids) {
		for (int i = 0; i < listofids.size(); i++) {

			try {
				db1 = openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
				Cursor cursor = db1.rawQuery("SELECT * FROM  " + TABLE
						+ " WHERE HadithID =" + (listofids.get(i)), null);
				if (cursor != null) {
					if (cursor.moveToFirst() && cursor.getCount() > 0) {

						do {
							// whole data of column is fetched by
							// getColumnIndex()
							String had = cursor.getString(cursor
									.getColumnIndex("Hadith"));
							String deg = cursor.getString(cursor
									.getColumnIndex("Degree"));
							String hadithId = cursor.getString(cursor
									.getColumnIndex("HadithID"));

							hadList.add(had);
							degList.add(deg);
							hadListIDs.add(hadithId);
							sethadList(hadithId, had, deg);
						} while (cursor.moveToNext());
					}
				}
				db1.close();
			} catch (SQLiteException e) {
				e.printStackTrace();
			}
		}
		setListAdapter(new AhadithListAdapter(CopyOfAhadithList.this, gethadList()));

		return hadList;
	}

	public ArrayList<String> getAllAhadith() {
		try {
			db1 = openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
			Cursor cursor = db1.rawQuery("SELECT * FROM  " + TABLE + " order by HadithID desc", null);
			if (cursor != null) {
				if (cursor.moveToFirst() && cursor.getCount() > 0) {

					do {
						// whole data of column is fetched by
						// getColumnIndex()
						String had = cursor.getString(cursor
								.getColumnIndex("Hadith"));
						String deg = cursor.getString(cursor
								.getColumnIndex("Degree"));
						String hadithId = cursor.getString(cursor
								.getColumnIndex("ID"));
						String had_id =cursor.getString(cursor.getColumnIndex("HadithID"));
						

						hadList.add(had);
						degList.add(deg);
						hadListIDs.add(had_id);
						sethadList(had_id, had, deg);

					} while (cursor.moveToNext());
				}
			}
			db1.close();
		} catch (SQLiteException e) {
			e.printStackTrace();
		}

		return hadList;
	}

	private class LoadImage extends AsyncTask<String, String, String> {
		Bitmap bmp;
		InputStream is = null;
		String image;

		String json = "";

		@Override
		protected String doInBackground(String... params) {
			try {

				// Making HTTP request
				// defaultHttpClient
				DefaultHttpClient httpClient = new DefaultHttpClient();
				HttpPost httpPost = new HttpPost(
						"http://www.dorar.net/banner/api");

				HttpResponse httpResponse = httpClient.execute(httpPost);
				HttpEntity httpEntity = httpResponse.getEntity();
				is = httpEntity.getContent();

			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			// Get data from server in string
			try {
				if (is != null) {
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(is, "UTF-8"), 8);
					StringBuilder sb = new StringBuilder();
					String line = null;
					while ((line = reader.readLine()) != null) {
						sb.append(line + "\n");

					}
					is.close();
					if (json == "[]") {
						runOnUiThread(new Runnable() {

							@Override
							public void run() {

							}
						});

					} else {
						json = sb.toString();
						// System.out.println(json);
					}

					image = new JSONObject(json).getString("image");
					link = new JSONObject(json).getString("link");
					System.out.println("the image is : " + image);
					System.out.println("the link is : " + link);

					imageurl = new URL(image);
					bmp = BitmapFactory.decodeStream(imageurl.openConnection()
							.getInputStream());
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							adImage.setImageBitmap(bmp);

						}
					});
				}
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// your code
			Intent intent = null;
			if (fromSearch) {
				intent = new Intent(CopyOfAhadithList.this, SearchActivity.class);
			} else {
				intent = new Intent(CopyOfAhadithList.this, MainActivity.class);
			}

			intent.putExtra("no", no);
			startActivity(intent);
			finish();
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}

	ArrayList<AhadithListModel> modelList = new ArrayList<AhadithListModel>();

	public ArrayList<AhadithListModel> gethadList() {
		return modelList;
	}

	public void sethadList(String hadithID, String had, String deg) {
		model = new AhadithListModel();
		model.setHadith(had);
		model.setHadDeg(deg);
		model.setHadithID(hadithID);
		model.setClickedMore(false);
		modelList.add(model);

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

	@Override
	protected void onResume() {

		super.onResume();
	}

	@Override
	protected void onPause() {

		super.onPause();
	}

}
