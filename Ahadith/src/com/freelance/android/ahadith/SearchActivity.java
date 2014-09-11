/*
package com.freelance.android.ahadith;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class SearchActivity extends Activity implements OnClickListener {

	ImageView adImage;
	Button btnSearch;
	EditText searchBox;
	String prfName = "prefs";
	String link;
	LinearLayout wordBar, partOfWordBar, allWordBar, anyWordBar;
	CheckBox wordCb, partOfWordCb, allWordCb, anyWordCb;
	int sel = 0;

	ArrayList<String> hadList = new ArrayList<String>();
	ArrayList<Integer> hadids = new ArrayList<Integer>();
	URL imageurl = null;
	String url;

	SQLiteDatabase db1 = null;
	String TABLE = "Data";
	private static String DBNAME = "Ahadith.db";

	// public JSONObject jObj = null;

	// alldata JSONArray
	JSONArray alldata = null;
	boolean flag = false;
	int no = 0;
	JSONParse jParser;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search);

		adImage = (ImageView) findViewById(R.id.adimage);
		searchBox = (EditText) findViewById(R.id.search_et);
		btnSearch = (Button) findViewById(R.id.btn_search);

		wordBar = (LinearLayout) findViewById(R.id.wordbar);
		partOfWordBar = (LinearLayout) findViewById(R.id.part_of_word_bar);
		allWordBar = (LinearLayout) findViewById(R.id.all_word_bar);
		anyWordBar = (LinearLayout) findViewById(R.id.any_word_bar);

		wordCb = (CheckBox) findViewById(R.id.cb_word);
		partOfWordCb = (CheckBox) findViewById(R.id.cb_partofword);
		allWordCb = (CheckBox) findViewById(R.id.cb_allword);
		anyWordCb = (CheckBox) findViewById(R.id.cb_anyword);

		wordBar.setOnClickListener(this);
		partOfWordBar.setOnClickListener(this);
		allWordBar.setOnClickListener(this);
		anyWordBar.setOnClickListener(this);

		SharedPreferences prfs = getSharedPreferences(prfName,
				Context.MODE_PRIVATE);
		url = prfs.getString("image_url", "");
		link = prfs.getString("image_link", "");
		System.out.println(url);

		no = getIntent().getExtras().getInt("no");

		searchBox.setText("إبليس اللعين");
		if (isNetworkAvailable()) {
			// Creating JSON Parser instance
			jParser = new JSONParse(this);

			// getting JSON string from URL
			jParser.execute("http://dorar.net/banner/api");

			System.out.println("network available");
		}
		// System.out.println("number for start download : " + no);
		startDownlod(no);

		new LoadImage().execute(url);
		adImage.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri
						.parse(link));
				startActivity(browserIntent);

			}
		});

		Button homebtn = (Button) findViewById(R.id.homebtn);
		homebtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent homeintent = new Intent(SearchActivity.this,
						MainActivity.class);
				homeintent.putExtra("no", no);
				startActivity(homeintent);

			}
		});

		btnSearch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (searchBox.getText().toString().equals("")
						|| searchBox.getText().toString().equals(null)) {
					Toast.makeText(SearchActivity.this, "ادخل قيمه البحث",
							Toast.LENGTH_LONG).show();

				} else if (sel == 0) {
					Toast.makeText(SearchActivity.this, "اختار طريقه البحث",
							Toast.LENGTH_LONG).show();

				} else if (sel == 1) {
					String searchValue = searchBox.getText().toString();
					ArrayList<String> allAhadith = getAllAhadith();
					for (int i = 0; i < allAhadith.size(); i++) {
						if (allAhadith.get(i).contains(searchValue)) {
							System.out.println("the value is : "
									+ allAhadith.get(i).contains(searchValue));
							try {
								db1 = openOrCreateDatabase(DBNAME,
										Context.MODE_PRIVATE, null);
								Cursor cursor = db1.rawQuery("SELECT * FROM  "
										+ TABLE + " WHERE MHadith ='"
										+ allAhadith.get(i).toString() + "'",
										null);
								System.out.println("the value is : "
										+ allAhadith.get(i).contains(
												searchValue));
								System.out.println("the  hadith is : "
										+ allAhadith.get(i));

								if (cursor != null) {
									if (cursor.moveToFirst()
											&& cursor.getCount() > 0) {

										do {

											// whole data of column
											// is fetched by
											// getColumnIndex()
											int hadid = cursor.getInt(cursor
													.getColumnIndex("HadithID"));

											// System.out.println("hadith id : "+hadid);

											hadids.add(hadid);
										} while (cursor.moveToNext());
									}
								}
							} catch (SQLiteException e) {
								e.printStackTrace();

							} finally {
								db1.close();
							}
						}

					}

				} else if (sel == 2) {
					System.out.println("2");
					String searchValue = searchBox.getText().toString();
					ArrayList<String> allAhadith = getAllAhadith();
					for (int i = 0; i < allAhadith.size(); i++) {
						String currentHadith = allAhadith.get(i);

						String[] currentHadithWords = currentHadith.split(" ");
						boolean isExist = false;
						for (String currentWord : currentHadithWords) {

							isExist = currentWord.trim().contains(
									searchValue.trim());
							if (isExist)
								break;
						}
						if (isExist) {
							try {
								db1 = openOrCreateDatabase(DBNAME,
										Context.MODE_PRIVATE, null);
								Cursor cursor = db1.rawQuery(
										"SELECT HadithID FROM  " + TABLE
												+ " WHERE MHadith = '"
												+ allAhadith.get(i) + "'", null);
								if (cursor != null) {
									if (cursor.moveToFirst()
											&& cursor.getCount() > 0) {

										do {
											// whole data of column
											// is fetched by
											// getColumnIndex()
											int hadid = cursor.getInt(cursor
													.getColumnIndex("HadithID"));

											// System.out.println("HadithID : "+hadid);

											hadids.add(hadid);

										} while (cursor.moveToNext());
									}
								}
							} catch (SQLiteException e) {
								e.printStackTrace();
							} finally {
								db1.close();
							}
						}
						// else if (allAhadith.get(i).startsWith(searchValue)) {
						//
						// try {
						// db1 = openOrCreateDatabase(DBNAME,
						// Context.MODE_PRIVATE, null);
						// Cursor cursor = db1.rawQuery(
						// "SELECT HadithID FROM  " + TABLE
						// + " WHERE Hadith = '"
						// + allAhadith.get(i) + "'", null);
						// if (cursor != null) {
						// if (cursor.moveToFirst()
						// && cursor.getCount() > 0) {
						//
						// do {
						// // whole data of column
						// // is fetched by
						// // getColumnIndex()
						// String hadid = cursor.getString(cursor
						// .getColumnIndex("HadithID"));
						//
						// // System.out.println("HadithID : "+hadid);
						// int ID = Integer.parseInt(hadid);
						// hadids.add(ID);
						//
						// } while (cursor.moveToNext());
						// }
						// }
						// } catch (SQLiteException e) {
						// e.printStackTrace();
						// } finally {
						//
						// db1.close();
						// }
						// }

					}

				} else if (sel == 4) {
					String searchValue = searchBox.getText().toString();
					String[] tokens = searchValue.split(" ");
					
					ArrayList<String> allAhadith = getAllAhadith();
					for (int i = 0; i < allAhadith.size(); i++) {
						String currentHadith = allAhadith.get(i);
						
						ArrayList<Boolean> isAllSearchWordsExist = new ArrayList<Boolean>();
						for (int j = 0; j < tokens.length; j++) {
							String currentSearchValue = tokens[j];
							boolean isExist = currentHadith
									.contains(currentSearchValue);
							System.out.println("isExist : "+isExist);
							isAllSearchWordsExist.add(isExist);
						}
						if (!isAllSearchWordsExist.contains(false)) {
							try {
								db1 = openOrCreateDatabase(DBNAME,
										Context.MODE_PRIVATE, null);
								Cursor cursor = db1.rawQuery(
										"SELECT HadithID FROM  " + TABLE
												+ " WHERE MHadith = '"
												+ allAhadith.get(i) + "'", null);
								if (cursor != null) {
									if (cursor.moveToFirst()
											&& cursor.getCount() > 0) {

										do {
											// whole data of column
											// is fetched by
											// getColumnIndex()
											int hadid = cursor.getInt(cursor
													.getColumnIndex("HadithID"));

											// System.out.println("HadithID : "+hadid);

											hadids.add(hadid);

										} while (cursor.moveToNext());
									}
								}
							} catch (SQLiteException e) {
								e.printStackTrace();
							} finally {
								db1.close();
							}
						}
						isAllSearchWordsExist.clear();

					}

					// }

				}
				// else if (sel == 5) {
				// String searchValue = searchBox.getText().toString();
				// String[] tokens = searchValue.split("\\s+");
				// for (int j = 0; j < tokens.length; j++) {
				// System.out.println("tokens 5 : " + tokens[j]);
				// ArrayList<String> allAhadith = getAllAhadith();
				// for (int i = 0; i < allAhadith.size(); i++) {
				// System.out.println(" hadith : " + allAhadith.get(i));
				// if (allAhadith.contains(tokens[j])) {
				//
				// try {
				// db1 = openOrCreateDatabase(DBNAME,
				// Context.MODE_PRIVATE, null);
				// Cursor cursor = db1.rawQuery(
				// "SELECT HadithID FROM  " + TABLE
				// + " WHERE MHadith = '"
				// + allAhadith.get(i) + "'",
				// null);
				// if (cursor != null) {
				// if (cursor.moveToFirst()
				// && cursor.getCount() > 0) {
				//
				// do {
				// // whole data of
				// // column is fetched
				// // by
				// // getColumnIndex()
				// int hadid = cursor.getInt(cursor
				// .getColumnIndex("HadithID"));
				//
				// // System.out.println("HadithID : "+hadid);
				//
				// hadids.add(hadid);
				//
				// } while (cursor.moveToNext());
				// }
				// }
				// } catch (SQLiteException e) {
				// e.printStackTrace();
				// } finally {
				// db1.close();
				// }
				// }
				//
				// }
				//
				// }
				//
				// }
				else if (sel == 6) {
					String searchValue = searchBox.getText().toString();
					String[] tokens = searchValue.split(" ");
					for (int j = 0; j < tokens.length; j++) {
						System.out.println("token : " + tokens[j]);
						ArrayList<String> allAhadith = getAllAhadith();
						for (int i = 0; i < allAhadith.size(); i++) {
							String currentHadith = allAhadith.get(i);

							String[] currentHadithWords = currentHadith
									.split(" ");
							boolean isExist = false;
							for (String currentWord : currentHadithWords) {
								System.out.println(" currentWord : token : "
										+ currentWord + " : " + tokens[j]);
								isExist = currentWord.trim().contains(
										tokens[j].trim());
								if (isExist)
									break;
							}
							if (isExist) {
								try {
									db1 = openOrCreateDatabase(DBNAME,
											Context.MODE_PRIVATE, null);
									Cursor cursor = db1.rawQuery(
											"SELECT HadithID FROM  " + TABLE
													+ " WHERE MHadith = '"
													+ allAhadith.get(i) + "'",
											null);
									if (cursor != null) {
										if (cursor.moveToFirst()
												&& cursor.getCount() > 0) {

											do {
												// whole data of column
												// is fetched by
												// getColumnIndex()
												int hadid = cursor.getInt(cursor
														.getColumnIndex("HadithID"));

												// System.out.println("HadithID : "+hadid);

												hadids.add(hadid);

											} while (cursor.moveToNext());
										}
									}
								} catch (SQLiteException e) {
									e.printStackTrace();
								} finally {
									db1.close();
								}
							}

						}

					}
				}
				if (!hadids.isEmpty()) {
					Intent intent = new Intent(SearchActivity.this,
							AhadithList.class);
					intent.putExtra("status", "search");
					intent.putIntegerArrayListExtra("idList", hadids);
					intent.putExtra("no", no);
					startActivity(intent);
				} else {
					Toast.makeText(SearchActivity.this, "لا توجد نتائج للبحث",
							Toast.LENGTH_LONG).show();

				}

			}
		});

	}

	public ArrayList<String> getAllAhadith() {
		try {
			db1 = openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
			Cursor cursor = db1.rawQuery("SELECT MHadith FROM  " + TABLE, null);
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
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.wordbar:
			wordCb.setChecked(true);
			partOfWordCb.setChecked(false);
			allWordCb.setChecked(false);
			anyWordCb.setChecked(false);
			sel = 1;
			break;
		case R.id.part_of_word_bar:
			wordCb.setChecked(false);
			partOfWordCb.setChecked(true);
			allWordCb.setChecked(false);
			anyWordCb.setChecked(false);
			sel = 2;
			break;

		case R.id.all_word_bar:
			wordCb.setChecked(false);
			partOfWordCb.setChecked(false);
			allWordCb.setChecked(true);
			anyWordCb.setChecked(false);
			sel = 4;
			break;
		case R.id.any_word_bar:
			wordCb.setChecked(false);
			partOfWordCb.setChecked(false);
			allWordCb.setChecked(false);
			anyWordCb.setChecked(true);
			sel = 6;
			break;

		default:
			break;
		}

	}

	private class LoadImage extends AsyncTask<String, String, String> {
		Bitmap bmp;

		@Override
		protected String doInBackground(String... params) {
			try {
				String parameter = params[0];
				parameter = parameter.replace("\\/", "/");
				imageurl = new URL(parameter);
				bmp = BitmapFactory.decodeStream(imageurl.openConnection()
						.getInputStream());
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						adImage.setImageBitmap(bmp);

					}
				});

			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

	}

	@Override
	protected void onPause() {
		hadids.clear();
		hadList.clear();
		super.onPause();
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
		protected String doInBackground(String... params) {

			// Making HTTP request
			try {
				// defaultHttpClient
				DefaultHttpClient httpClient = new DefaultHttpClient();
				HttpPost httpPost = new HttpPost(params[0]);

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
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(is, "UTF-8"), 8);
				StringBuilder sb = new StringBuilder();
				String line = null;
				while ((line = reader.readLine()) != null) {
					sb.append(line + "\n");

				}
				is.close();
				if (json == "[]") {
					Toast.makeText(SearchActivity.this, "اختار طريقه البحث",
							Toast.LENGTH_LONG).show();
					runOnUiThread(new Runnable() {

						@Override
						public void run() {

						}
					});

				} else {
					json = sb.toString();
					System.out.println(json);
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
						JSONObject c = (JSONObject) alldata.get(i);

						// Storing each json item in variable
						String hadithid = c.getString("hadith_id");
						String hadithtext = c.getString("hadith");
						String hadithdegree = c.getString("degree");
						String hadithmod = c.getString("modified");
						Matcher m = p.matcher(hadithtext);
						m.reset();
						String mHadith = m.replaceAll("");

						// System.out.println("Title + \n" + hadithid);

						db1 = openOrCreateDatabase(DBNAME,
								Context.MODE_PRIVATE, null);
						db1.execSQL("INSERT INTO "
								+ TABLE
								+ "(HadithID ,Hadith ,MHadith ,Degree ,Modified) VALUES ("
								+ hadithid + " , '" + hadithtext + "' , '"
								+ mHadith + "' , '" + hadithdegree + " ', '"
								+ hadithmod + "')");
						db1.close();
						// db1 = openOrCreateDatabase(DBNAME,
						// Context.MODE_PRIVATE, null);
						// Cursor cursor = db1.rawQuery("SELECT * FROM " +
						// TABLE,
						// null);
						// if (cursor != null) {
						// if (cursor.moveToFirst()) {
						// do {
						// String TITLE = cursor.getString(cursor
						// .getColumnIndex("HadithID"));
						// //System.out.println("HadithID : "+TITLE);
						// } while (cursor.moveToNext());
						// }
						// }
						// db1.close();

					}

				} catch (JSONException e) {
					e.printStackTrace();
				}

			} catch (Exception e) {
				Log.e("Buffer Error", "Error converting result " + e.toString());
			}

			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			if (no < 4) {
				no++;
				startDownlod(no);

			} else {
				runOnUiThread(new Runnable() {

					@Override
					public void run() {

					}
				});

			}

		}
	}

	private boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager = (ConnectivityManager) this
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}

	@Override
	protected void onResume() {

		super.onResume();
	}

	public void startDownlod(int no) {
		if (isNetworkAvailable()) {

			// check what section is to get url?'
			// System.out.println("Staaaaaaaaaaatus " + isNetworkAvailable());

			String urlmoshrf = "http://dorar.net/spreadH/api?p=" + no;
			getDataFromJason(urlmoshrf);

		} else {

			try {
				db1 = openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
				Cursor cursor = db1.rawQuery("SELECT * FROM  " + TABLE, null);
				if (cursor != null) {
					if (cursor.moveToFirst() && cursor.getCount() > 0) {

						do {
							// whole data of column is fetched by
							// getColumnIndex()

							// System.out.println("Title \n" + TITLE);

						} while (cursor.moveToNext());
					}
				}

			} catch (SQLiteException e) {
				if (e.getMessage().toString().contains("no such table")) {
					Log.e("ERROR", "Creating table " + TABLE
							+ "because it doesn't exist!");
					Toast.makeText(SearchActivity.this,
							"No Internet Connection", Toast.LENGTH_LONG).show();

				}
			} finally {
				db1.close();
			}

		}

		//

	}

	public void getDataFromJason(String url) {

		// Creating JSON Parser instance
		jParser = new JSONParse(this);

		// getting JSON string from URL
		jParser.execute(url);

		// JSONObject json = jObj;
		flag = true;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// your code
			Intent intent = new Intent(SearchActivity.this, MainActivity.class);
			intent.putExtra("no", no);
			startActivity(intent);
			finish();
			return true;
		} else {
			return false;
		}
	}

	public static boolean sameCharacters(String left, String right) {
		return Arrays.equals(sortCharacters(left), sortCharacters(right));
	}

	private static char[] sortCharacters(String s) {
		final char[] chars = s.toCharArray();
		Arrays.sort(chars);
		return chars;
	}
}
 */

package com.freelance.android.ahadith;

import android.app.Activity;
import android.app.ProgressDialog;
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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
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
import java.util.Arrays;

public class SearchActivity extends Activity implements OnClickListener {

	TextView txtTitle,tv1,tv2,tv3,tv4;
	ImageView adImage;
	Button btnSearch;
	EditText searchBox;
	String prfName = "prefs";
	String link;
	LinearLayout wordBar, partOfWordBar, allWordBar, anyWordBar;
	CheckBox wordCb, partOfWordCb, allWordCb, anyWordCb;
	int sel = 0;
    ProgressDialog pd;

	int searchResultsCount=0;

	ArrayList<String> hadList = new ArrayList<String>();
	ArrayList<Integer> hadids = new ArrayList<Integer>();
	URL imageurl = null;
	String url;

	SQLiteDatabase db1 = null;
	String TABLE = "Data";
	private static String DBNAME = "Ahadith.db";

	boolean flag = false;
	int no = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		BugSenseHandler.initAndStartSession(SearchActivity.this, "bfdfcbf3");

		setContentView(R.layout.search);

		adImage = (ImageView) findViewById(R.id.adimage);
		searchBox = (EditText) findViewById(R.id.search_et);
		btnSearch = (Button) findViewById(R.id.btn_search);

		wordBar = (LinearLayout) findViewById(R.id.wordbar);
		partOfWordBar = (LinearLayout) findViewById(R.id.part_of_word_bar);
		allWordBar = (LinearLayout) findViewById(R.id.all_word_bar);
		anyWordBar = (LinearLayout) findViewById(R.id.any_word_bar);

		wordCb = (CheckBox) findViewById(R.id.cb_word);
		partOfWordCb = (CheckBox) findViewById(R.id.cb_partofword);
		allWordCb = (CheckBox) findViewById(R.id.cb_allword);
		anyWordCb = (CheckBox) findViewById(R.id.cb_anyword);

		wordBar.setOnClickListener(this);
		partOfWordBar.setOnClickListener(this);
		allWordBar.setOnClickListener(this);
		anyWordBar.setOnClickListener(this);
		txtTitle = (TextView) findViewById(R.id.txt_title);
		tv1 = (TextView) findViewById(R.id.tv1);
		tv2 = (TextView) findViewById(R.id.tv2);
		tv3 = (TextView) findViewById(R.id.tv3);
		tv4 = (TextView) findViewById(R.id.tv4);
		
		Typeface face = Typeface.createFromAsset(this.getAssets(),
				"hacen_saudi_arabia.ttf");

		txtTitle.setTypeface(face);
		tv1.setTypeface(face);
		tv2.setTypeface(face);
		tv3.setTypeface(face);
		tv4.setTypeface(face);

		SharedPreferences prfs = getSharedPreferences(prfName,
				Context.MODE_PRIVATE);
		url = prfs.getString("image_url", "");
		link = prfs.getString("image_link", "");
		System.out.println(url);

		no = getIntent().getExtras().getInt("no");

		// searchBox.setText("إبليس اللعين");

		if (isNetworkAvailable()) {

			System.out.println("network available");

			new LoadImage().execute("http://www.dorar.net/banner/api");

		}

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

		Button homebtn = (Button) findViewById(R.id.homebtn);
		homebtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent homeintent = new Intent(SearchActivity.this,
						MainActivity.class);
				homeintent.putExtra("no", no);
				startActivity(homeintent);

			}
		});
		searchBox.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				// TODO Auto-generated method stub
				search();

				return false;
			}
		});

		btnSearch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				search();
			}
		});

	}

	public ArrayList<String> getAllAhadith() {
		try {
			db1 = openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
			Cursor cursor = db1.rawQuery("SELECT MHadith FROM  " + TABLE, null);
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
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.wordbar:
			wordCb.setChecked(true);
			partOfWordCb.setChecked(false);
			allWordCb.setChecked(false);
			anyWordCb.setChecked(false);
			sel = 1;
			break;
		case R.id.part_of_word_bar:
			wordCb.setChecked(false);
			partOfWordCb.setChecked(true);
			allWordCb.setChecked(false);
			anyWordCb.setChecked(false);
			sel = 2;
			break;

		case R.id.all_word_bar:
			wordCb.setChecked(false);
			partOfWordCb.setChecked(false);
			allWordCb.setChecked(true);
			anyWordCb.setChecked(false);
			sel = 4;
			break;
		case R.id.any_word_bar:
			wordCb.setChecked(false);
			partOfWordCb.setChecked(false);
			allWordCb.setChecked(false);
			anyWordCb.setChecked(true);
			sel = 6;
			break;

		default:
			break;
		}

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
	protected void onPause() {
		hadids.clear();
		hadList.clear();
		super.onPause();
	}

	// @Override
	// protected void onDestroy(){
	// super.onDestroy();
	//
	//
	//
	// BugSenseHandler.flush(SearchActivity.this);
	//
	// }

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
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// your code
			Intent intent = new Intent(SearchActivity.this, MainActivity.class);
			intent.putExtra("no", no);
			startActivity(intent);
			finish();
			return true;
		} else {
			return false;
		}
	}

	public static boolean sameCharacters(String left, String right) {
		return Arrays.equals(sortCharacters(left), sortCharacters(right));
	}

	private static char[] sortCharacters(String s) {
		final char[] chars = s.toCharArray();
		Arrays.sort(chars);
		return chars;
	}
	
	
	private void search2() {
		if (searchBox.getText().toString().equals("")
				|| searchBox.getText().toString().equals(null)) {
			Toast.makeText(SearchActivity.this, "أدخل كلمة / عبارة البحث ",
					Toast.LENGTH_LONG).show();

		} else if (sel == 0) {




            Toast.makeText(SearchActivity.this, "اختار طريقه البحث",
					Toast.LENGTH_LONG).show();

		} else if (sel == 1) {
            pd = ProgressDialog.show(this,"","جاري البحث ...",true);


			String searchValue = searchBox.getText().toString();
			System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa : "
					+ searchValue);
			String[] searchWords = searchValue.split(" ");

			ArrayList<String> allAhadith = getAllAhadith();

            for (int i = 0; i < allAhadith.size(); i++) {

                String cuHadith = allAhadith.get(i).toString();

                String b1 = "\\(";
                String b2 = "\\)";

                cuHadith.replaceAll("'"," ");
                cuHadith.replaceAll(".", " ");

                cuHadith.replaceAll(",", " ");
                cuHadith.replaceAll("،", " ");
                cuHadith.replaceAll(b1, " ");
                cuHadith.replaceAll(b2, " ");

                String regex = searchValue;

                if (cuHadith.contains(regex)) {


                    System.out.println("the value is : "
                            + cuHadith.contains(searchValue));
                    try {

                        db1 = openOrCreateDatabase(DBNAME,
                                Context.MODE_PRIVATE, null);
                        Cursor cursor = db1.rawQuery("SELECT * FROM  " + TABLE
                                + " WHERE MHadith ='"
                                + allAhadith.get(i).toString() + "'", null);
                        //  System.out.println("the value is : "
                        //   + allAhadith.get(i).contains(searchValue));
                        //  System.out.println("the  hadith is : "
                        //  + allAhadith.get(i));

                        if (cursor != null && cursor.getCount() > 0) {
                            if (cursor.moveToFirst()) {

                                // whole data of column
                                // is fetched by
                                // getColumnIndex()
                                int hadid = cursor.getInt(cursor	
                                        .getColumnIndex("HadithID"));

                                System.out.println("Jadith id : " + hadid);

                                hadids.add(hadid);

                            }
                        }
                    } catch (SQLiteException e) {
                        System.out.println("Message 4 : " + e.getMessage());
                        e.printStackTrace();

                    } finally {
                        db1.close();
                    }

                }


			}

		} else if (sel == 2) {

            pd = ProgressDialog.show(this,"","جاري البحث ...",true);

			System.out.println("2");
			String searchValue = searchBox.getText().toString();
			ArrayList<String> allAhadith = getAllAhadith();
			for (int i = 0; i < allAhadith.size(); i++) {
				String cuHadith = allAhadith.get(i);

                String b1 = "\\(";
                String b2 = "\\)";

                cuHadith.replaceAll("'"," ");
                cuHadith.replaceAll(".", " ");

                cuHadith.replaceAll(",", " ");
                cuHadith.replaceAll("،", " ");
                cuHadith.replaceAll(b1, " ");
                cuHadith.replaceAll(b2, " ");

				String[] currentHadithWords = cuHadith.split(" ");
				boolean isExist = false;
				for (String currentWord : currentHadithWords) {

					isExist = currentWord.trim().contains(searchValue.trim());
					if (isExist)
						break;
				}
				if (isExist) {
					try {
						db1 = openOrCreateDatabase(DBNAME,
								Context.MODE_PRIVATE, null);
						Cursor cursor = db1.rawQuery(
								"SELECT HadithID FROM  " + TABLE
										+ " WHERE MHadith = '"
										+ allAhadith.get(i) + "'", null);
						if (cursor != null) {
							if (cursor.moveToFirst() && cursor.getCount() > 0) {

								do {
									// whole data of column
									// is fetched by
									// getColumnIndex()
									int hadid = cursor.getInt(cursor
											.getColumnIndex("HadithID"));

									// System.out.println("HadithID : "+hadid);

									hadids.add(hadid);

								} while (cursor.moveToNext());
							}
						}
					} catch (SQLiteException e) {
						e.printStackTrace();
					} finally {
						db1.close();
					}
				}

			}

		} else if (sel == 4) {
            pd = ProgressDialog.show(this,"","جاري البحث ...",true);

            String searchValue = searchBox.getText().toString();
			String[] tokens = searchValue.split(" ");

			ArrayList<String> allAhadith = getAllAhadith();
			for (int i = 0; i < allAhadith.size(); i++) {
                String cuHadith = allAhadith.get(i);

                String b1 = "\\(";
                String b2 = "\\)";

                cuHadith.replaceAll("'"," ");
                cuHadith.replaceAll(".", " ");

                cuHadith.replaceAll(",", " ");
                cuHadith.replaceAll("،", " ");
                cuHadith.replaceAll(b1, " ");
                cuHadith.replaceAll(b2, " ");

				ArrayList<Boolean> isAllSearchWordsExist = new ArrayList<Boolean>();
				for (int j = 0; j < tokens.length; j++) {
					String currentSearchValue = tokens[j];
					boolean isExist = cuHadith
							.contains(currentSearchValue);
					System.out.println("isExist : " + isExist);
					isAllSearchWordsExist.add(isExist);
				}
				if (!isAllSearchWordsExist.contains(false)) {
					try {
						db1 = openOrCreateDatabase(DBNAME,
								Context.MODE_PRIVATE, null);
						Cursor cursor = db1.rawQuery(
								"SELECT HadithID FROM  " + TABLE
										+ " WHERE MHadith = '"
										+ allAhadith.get(i) + "'", null);
						if (cursor != null) {
							if (cursor.moveToFirst() && cursor.getCount() > 0) {

								do {
									// whole data of column
									// is fetched by
									// getColumnIndex()
									int hadid = cursor.getInt(cursor
											.getColumnIndex("HadithID"));

									// System.out.println("HadithID : "+hadid);

									hadids.add(hadid);

								} while (cursor.moveToNext());
							}
						}
					} catch (SQLiteException e) {
						e.printStackTrace();
					} finally {
						db1.close();
					}
				}
				isAllSearchWordsExist.clear();

			}

			// }

		}
		// else if (sel == 5) {
		// String searchValue = searchBox.getText().toString();
		// String[] tokens = searchValue.split("\\s+");
		// for (int j = 0; j < tokens.length; j++) {
		// System.out.println("tokens 5 : " + tokens[j]);
		// ArrayList<String> allAhadith = getAllAhadith();
		// for (int i = 0; i < allAhadith.size(); i++) {
		// System.out.println(" hadith : " + allAhadith.get(i));
		// if (allAhadith.contains(tokens[j])) {
		//
		// try {
		// db1 = openOrCreateDatabase(DBNAME,
		// Context.MODE_PRIVATE, null);
		// Cursor cursor = db1.rawQuery(
		// "SELECT HadithID FROM  " + TABLE
		// + " WHERE MHadith = '"
		// + allAhadith.get(i) + "'",
		// null);
		// if (cursor != null) {
		// if (cursor.moveToFirst()
		// && cursor.getCount() > 0) {
		//
		// do {
		// // whole data of
		// // column is fetched
		// // by
		// // getColumnIndex()
		// int hadid = cursor.getInt(cursor
		// .getColumnIndex("HadithID"));
		//
		// // System.out.println("HadithID : "+hadid);
		//
		// hadids.add(hadid);
		//
		// } while (cursor.moveToNext());
		// }
		// }
		// } catch (SQLiteException e) {
		// e.printStackTrace();
		// } finally {
		// db1.close();
		// }
		// }
		//
		// }
		//
		// }
		//
		// }
		else if (sel == 6) {
            pd = ProgressDialog.show(this,"","جاري البحث ...",true);

            String searchValue = searchBox.getText().toString();
			String[] tokens = searchValue.split(" ");


            ArrayList<String> allAhadith = getAllAhadith();
			for (int j = 0; j < tokens.length; j++) {

				System.out.println("token : " + tokens[j]);


                for (int i = 0; i < allAhadith.size(); i++) {

                    String cuHadith = allAhadith.get(i);

                    String b1 = "\\(";
                    String b2 = "\\)";

                    cuHadith.replaceAll("'"," ");
                    cuHadith.replaceAll(".", " ");

                    cuHadith.replaceAll(",", " ");
                    cuHadith.replaceAll("،", " ");
                    cuHadith.replaceAll(b1, " ");
                    cuHadith.replaceAll(b2, " ");



                    if (Arrays.asList(cuHadith.split(" ")).contains(tokens[j])) {


                        System.out.println("the value is : "
                                + Arrays.asList(cuHadith.split(" ")).contains(tokens[j]));
                        try {

                            db1 = openOrCreateDatabase(DBNAME,
                                    Context.MODE_PRIVATE, null);
                            Cursor cursor = db1.rawQuery("SELECT * FROM  " + TABLE
                                    + " WHERE MHadith ='"
                                    + allAhadith.get(i).toString() + "'", null);
                            //  System.out.println("the value is : "
                            //   + allAhadith.get(i).contains(searchValue));
                            //  System.out.println("the  hadith is : "
                            //  + allAhadith.get(i));

                            if (cursor != null && cursor.getCount() > 0) {
                                if (cursor.moveToFirst()) {

                                    // whole data of column
                                    // is fetched by
                                    // getColumnIndex()
                                    int hadid = cursor.getInt(cursor
                                            .getColumnIndex("HadithID"));

                                    System.out.println("Jadith id : " + hadid);

                                    hadids.add(hadid);

                                }
                            }
                        } catch (SQLiteException e) {
                            System.out.println("Message 4 : " + e.getMessage());
                            e.printStackTrace();

                        } finally {
                            db1.close();
                        }

                    }

                }

                }
		}

		if (sel != 0) {
			if (!hadids.isEmpty()) {

               // pd.cancel();
				Intent intent = new Intent(SearchActivity.this,
						AhadithList.class);
				intent.putExtra("status", "search");
				intent.putIntegerArrayListExtra("idList", hadids);
				intent.putExtra("no", no);
				intent.putExtra("search", true);
				intent.putExtra("count", hadids.size());
				startActivity(intent);
			} else {
				Toast.makeText(SearchActivity.this, "لا توجد نتائج للبحث",
						Toast.LENGTH_LONG).show();
                pd.cancel();

			}


		}
	}
	
	

	private void search() {
		hadids.clear();
		if (searchBox.getText().toString().equals("")
				|| searchBox.getText().toString().equals(null)) {
			Toast.makeText(SearchActivity.this, " أدخل كلمة / عبارة البحث",
					Toast.LENGTH_LONG).show();
			return ; 

		} else if (sel == 0) {




            Toast.makeText(SearchActivity.this, "اختار طريقه البحث",
					Toast.LENGTH_LONG).show();

					
              
		}
		
		else if(sel == 1){
			pd = ProgressDialog.show(this,"","جاري البحث ...",true);
			String searchValue =searchBox.getText().toString();
			String b1 = "\\(";
            String b2 = "\\)";

            searchValue.replaceAll("'"," ");
            searchValue.replaceAll(".", " ");

            searchValue.replaceAll(",", " ");
            searchValue.replaceAll("،", " ");
            searchValue.replaceAll(b1, " ");
            searchValue.replaceAll(b2, " ");
            String[] ar = searchValue.split(" ");
			try{
				String sss="REPLACE(REPLACE(REPLACE(REPLACE(MHadith,'،',' '),'.',' '),'" + b1 + "',' '),'\\)',' ')";
				 db1 = openOrCreateDatabase(DBNAME,
                         Context.MODE_PRIVATE, null);
                 Cursor cursor = db1.rawQuery("SELECT * FROM  " + TABLE
                         + " WHERE REPLACE("+ sss +","+ "'و'" +",' ') like '% "
                         + searchValue + " %'", null);
                
                if(cursor.getCount()!=0 && cursor!=null){
                	cursor.moveToFirst();
                	do{
                		
                		int hadid = cursor.getInt(cursor.getColumnIndex("HadithID"));
                		hadids.add(hadid);	
                		
                	}while(cursor.moveToNext());
                	
                	
                	
                	//Toast.makeText(getApplicationContext(), String.valueOf(cursor.getCount()), Toast.LENGTH_LONG).show();
                	
                }
                else{
                
               // Toast.makeText(getApplicationContext(), "لا يوجد للبحث نتائج", Toast.LENGTH_LONG).show();
                }

                
                
                //Toast.makeText(getApplicationContext(), String.valueOf(cursor.getCount()), Toast.LENGTH_LONG).show();

			}
			catch(Exception ex){
				//Toast.makeText(getApplicationContext(),"لا يوجد للبحث نتائج", Toast.LENGTH_LONG).show();
				//pd.cancel();
				
			}
			finally{
				pd.cancel();
				db1.close();
			}
			
			
		}else if(sel == 2){
			pd = ProgressDialog.show(this,"","جاري البحث ...",true);
			String searchValue =searchBox.getText().toString();
			
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
                
               // Toast.makeText(getApplicationContext(), "لا يوجد للبحث نتائج", Toast.LENGTH_LONG).show();
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
			
		
		}else if(sel == 4){
			
			pd = ProgressDialog.show(this,"","جاري البحث ...",true);
			String searchValue = searchBox.getText().toString();
			String b1 = "\\(";
            String b2 = "\\)";

            searchValue.replaceAll("'"," ");
            searchValue.replaceAll(".", " ");

            searchValue.replaceAll(",", " ");
            searchValue.replaceAll("،", " ");
            searchValue.replaceAll(b1, " ");
            searchValue.replaceAll(b2, " ");
            String[] ar = searchValue.split(" ");
            String where = "";
            for(int i =0 ;i <ar.length;i++){
            	//Toast.makeText(getApplicationContext(), ar[i], Toast.LENGTH_LONG).show();
            	where = where + " MHadith like '%" + ar[i] + "%' and ";
            	
            }
            where = where + " MHadith like '%" + ar[ar.length-1] + "%' ";
            
            //Toast.makeText(getApplicationContext(), "Hisham " + ar.length , Toast.LENGTH_LONG).show();
			try{
                db1 = openOrCreateDatabase(DBNAME,
                        Context.MODE_PRIVATE, null);
                Cursor cursor = db1.rawQuery("SELECT * FROM  " + TABLE
                        + " WHERE "
                        + where + "", null);

                
                if(cursor.getCount()!=0 && cursor!=null){
                	cursor.moveToFirst();
                	do{
                		
                		int hadid = cursor.getInt(cursor.getColumnIndex("HadithID"));
                		hadids.add(hadid);	
                		
                	}while(cursor.moveToNext());
                	
                	
                	
                	//Toast.makeText(getApplicationContext(), String.valueOf(cursor.getCount()), Toast.LENGTH_LONG).show();
                	
                }
                else{
                
               // Toast.makeText(getApplicationContext(), "لا يوجد للبحث نتائج", Toast.LENGTH_LONG).show();
                }
                //Toast.makeText(getApplicationContext(), String.valueOf(cursor.getCount()), Toast.LENGTH_LONG).show();

			}
			catch(Exception ex){
				//Toast.makeText(getApplicationContext(),"لا يوجد للبحث نتائج", Toast.LENGTH_LONG).show();
				//pd.cancel();
				
			}
			finally{
				 pd.cancel();
				db1.close();
			}
			
		
		}else if(sel == 3){
			pd = ProgressDialog.show(this,"","جاري البحث ...",true);
			String searchValue = searchBox.getText().toString();
			String b1 = "\\(";
            String b2 = "\\)";

            searchValue.replaceAll("'"," ");
            searchValue.replaceAll(".", " ");

            searchValue.replaceAll(",", " ");
            searchValue.replaceAll("،", " ");
            searchValue.replaceAll(b1, " ");
            searchValue.replaceAll(b2, " ");
            String[] ar = searchValue.split(" ");
            //Toast.makeText(getApplicationContext(), "Hisham " + ar.length , Toast.LENGTH_LONG).show();
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
                
                //Toast.makeText(getApplicationContext(), "لا يوجد للبحث نتائج", Toast.LENGTH_LONG).show();
                }
               // Toast.makeText(getApplicationContext(), String.valueOf(cursor.getCount()), Toast.LENGTH_LONG).show();

			}
			catch(Exception ex){
				//Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
				pd.cancel();
				
			}
			finally{
				pd.cancel();
				db1.close();
			}
			
		
		}else if(sel == 6){
			//Toast.makeText(getApplicationContext(), "you are in 6", Toast.LENGTH_LONG).show();
			pd = ProgressDialog.show(this,"","جاري البحث ...",true);
			String searchValue = searchBox.getText().toString();
			String b1 = "\\(";
            String b2 = "\\)";

            searchValue.replaceAll("'"," ");
            searchValue.replaceAll(".", " ");

            searchValue.replaceAll(",", " ");
            searchValue.replaceAll("،", " ");
            searchValue.replaceAll(b1, " ");
            searchValue.replaceAll(b2, " ");
            String[] ar = searchValue.split(" ");
            String where = "";
            for(int i =0 ;i <ar.length;i++){
            	//Toast.makeText(getApplicationContext(), ar[i], Toast.LENGTH_LONG).show();
            	where = where + " MHadith like '%" + ar[i] + "%' Or ";
            	
            }
            where = where + " MHadith like '%" + ar[ar.length-1] + "%' ";
            
           // Toast.makeText(getApplicationContext(), "Hisham " + ar.length , Toast.LENGTH_LONG).show();
			try{
                db1 = openOrCreateDatabase(DBNAME,
                        Context.MODE_PRIVATE, null);
                Cursor cursor = db1.rawQuery("SELECT * FROM  " + TABLE
                        + " WHERE "
                        + where + "", null);

                
                if(cursor.getCount()!=0 && cursor!=null){
                	cursor.moveToFirst();
                	do{
                		
                		int hadid = cursor.getInt(cursor.getColumnIndex("HadithID"));
                		hadids.add(hadid);	
                		
                	}while(cursor.moveToNext());
                	
                	
                	
                	//Toast.makeText(getApplicationContext(), String.valueOf(cursor.getCount()), Toast.LENGTH_LONG).show();
                	
                }
                else{
                
                //Toast.makeText(getApplicationContext(), "لا يوجد للبحث نتائج", Toast.LENGTH_LONG).show();
                }
                //Toast.makeText(getApplicationContext(), String.valueOf(cursor.getCount()), Toast.LENGTH_LONG).show();

			}
			catch(Exception ex){
				//Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
				pd.cancel();
				
			}
			finally{
				 pd.cancel();
				db1.close();
			}
			
		
		}

		if (sel != 0) {
			if (!hadids.isEmpty()) {

               // pd.cancel();
				Intent intent = new Intent(SearchActivity.this,
				AhadithList.class);
				intent.putExtra("status", "search");
				intent.putIntegerArrayListExtra("idList", hadids);
				intent.putExtra("no", no);
				intent.putExtra("search", true);
				intent.putExtra("count", hadids.size());
				startActivity(intent);
			} else {
				Toast.makeText(SearchActivity.this, "لا توجد نتائج للبحث",
						Toast.LENGTH_LONG).show();
               // pd.cancel();

			}


		}

	}
}
