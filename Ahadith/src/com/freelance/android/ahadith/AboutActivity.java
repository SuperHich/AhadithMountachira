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

import android.annotation.SuppressLint;
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
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("ShowToast")
public class AboutActivity extends Activity {

	Button desktop, mail, facebook, twitter;
	ImageView adImage;
	String prfName = "prefs";
	String link;
	URL imageurl = null;

	SQLiteDatabase db1 = null;
	String TABLE = "Data";
	private static String DBNAME = "Ahadith.db";

	ArrayList<Integer> hadids = new ArrayList<Integer>();

	// public JSONObject jObj = null;

	// alldata JSONArray
	JSONArray alldata = null;
	boolean flag = false;
	int no = 0;
	JSONParse jParser;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
		desktop = (Button) findViewById(R.id.desktop);
		mail = (Button) findViewById(R.id.mailbtn);
		twitter = (Button) findViewById(R.id.twitbtn);
		facebook = (Button) findViewById(R.id.fbbtn);
		adImage = (ImageView) findViewById(R.id.adimage);

		TextView tv1 = (TextView) findViewById(R.id.iv1);
		TextView tv2 = (TextView) findViewById(R.id.iv2);
		TextView tv3 = (TextView) findViewById(R.id.iv3);
		TextView tv4 = (TextView) findViewById(R.id.iv4);
		TextView tv5 = (TextView) findViewById(R.id.iv5);
		TextView tv6 = (TextView) findViewById(R.id.iv6);
		TextView tv7 = (TextView) findViewById(R.id.iv7);
		TextView tv8 = (TextView) findViewById(R.id.iv8);
		TextView tv9 = (TextView) findViewById(R.id.iv9);
		TextView tv10 = (TextView) findViewById(R.id.iv10);
		TextView tv11 = (TextView) findViewById(R.id.iv11);
		TextView tv12 = (TextView) findViewById(R.id.iv12);
		TextView tv13 = (TextView) findViewById(R.id.iv13);
		TextView tv14 = (TextView) findViewById(R.id.iv14);
		TextView tv15 = (TextView) findViewById(R.id.iv15);
		TextView tv16 = (TextView) findViewById(R.id.iv16);

		Typeface face = Typeface.createFromAsset(this.getAssets(),
				"univers_next_arabic.ttf");

		tv1.setTypeface(face);
		tv2.setTypeface(face);
		tv3.setTypeface(face);
		tv4.setTypeface(face);
		tv5.setTypeface(face);
		tv6.setTypeface(face);
		tv7.setTypeface(face);
		tv8.setTypeface(face);
		tv9.setTypeface(face);
		tv10.setTypeface(face);
		tv11.setTypeface(face);
		tv12.setTypeface(face);
		tv13.setTypeface(face);
		tv14.setTypeface(face);
		tv15.setTypeface(face);
		tv16.setTypeface(face);

		no = getIntent().getExtras().getInt("no");

		if (isNetworkAvailable()) {
			// Creating JSON Parser instance
			jParser = new JSONParse(this);

			// getting JSON string from URL
			jParser.execute("http://dorar.net/banner/api");

			System.out.println("network available");
		} else {
			Toast.makeText(getApplicationContext(), "ﻻ يوجد اتصال بالانترنت ",
					Toast.LENGTH_LONG);
		}
		// System.out.println("number for start download : " + no);
		startDownlod(no);

		SharedPreferences prfs = getSharedPreferences(prfName,
				Context.MODE_PRIVATE);
		String url = prfs.getString("image_url", "");
		link = prfs.getString("image_link", "");
		new LoadImage().execute(url);
		adImage.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri
						.parse(link));
				startActivity(browserIntent);

			}
		});

		facebook.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent fbIntent = new Intent(Intent.ACTION_VIEW, Uri
						.parse("https://www.facebook.com"));
				startActivity(fbIntent);

			}
		});
		twitter.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent twIntent = new Intent(Intent.ACTION_VIEW, Uri
						.parse("https://twitter.com/"));
				startActivity(twIntent);
			}
		});
		mail.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(Intent.ACTION_SEND);
				i.setType("message/rfc822");

				i.putExtra(Intent.EXTRA_SUBJECT, "Ahadith App");
				i.putExtra(Intent.EXTRA_TEXT, "body of email");
				try {
					startActivity(Intent.createChooser(i, "Send mail..."));
				} catch (android.content.ActivityNotFoundException ex) {

				}

			}
		});
		Button homebtn = (Button) findViewById(R.id.homebtn);
		homebtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent homeintent = new Intent(AboutActivity.this,
						MainActivity.class);
				startActivity(homeintent);

			}
		});
	}

	private class LoadImage extends AsyncTask<String, String, String> {
		Bitmap bmp;

		@Override
		protected String doInBackground(String... params) {
			try {
				imageurl = new URL(params[0]);
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
					runOnUiThread(new Runnable() {

						@Override
						public void run() {

						}
					});

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
						// System.out.println("HadithID : "+TITLE);
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

	@Override
	protected void onPause() {
		hadids.clear();
		super.onPause();
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
					Handler handler = new Handler();
					handler.postDelayed(new Runnable() {

						@Override
						public void run() {

						}
					}, 2000);

				}
				db1.close();

			} catch (SQLiteException e) {
				if (e.getMessage().toString().contains("no such table")) {
					Log.e("ERROR", "Creating table " + TABLE
							+ "because it doesn't exist!");
					Toast.makeText(AboutActivity.this,
							"No Internet Connection", Toast.LENGTH_LONG).show();

				}
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
			Intent intent = new Intent(AboutActivity.this, MainActivity.class);
			intent.putExtra("no", no);
			startActivity(intent);
			finish();
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}

}
*/

package com.freelance.android.ahadith;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.TextView;

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

@SuppressLint("ShowToast")
public class AboutActivity extends Activity {

    Button desktop, mail, facebook, twitter, maximizeText, minimizeText;
    ImageView adImage;
    String prfName = "prefs";
    String link;
    URL imageurl = null;
    int textSize = 14;

    ArrayList<Integer> hadids = new ArrayList<Integer>();

    boolean flag = false;
    int no = 0;


    TextView tv1, tv2, tv3, tv4, tv5, tv6, tv7, tv8, tv9, tv10, tv11, tv12,
            tv13, tv14, tv15, tv16;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        BugSenseHandler.initAndStartSession(AboutActivity.this, "bfdfcbf3");

        setContentView(R.layout.about);
        desktop = (Button) findViewById(R.id.desktop);
        mail = (Button) findViewById(R.id.mailbtn);
        twitter = (Button) findViewById(R.id.twitbtn);
        facebook = (Button) findViewById(R.id.fbbtn);
        adImage = (ImageView) findViewById(R.id.adimage);
        maximizeText = (Button) findViewById(R.id.plusbtn);
        minimizeText = (Button) findViewById(R.id.minbtn);

        tv1 = (TextView) findViewById(R.id.iv1);
        tv2 = (TextView) findViewById(R.id.iv2);
        tv3 = (TextView) findViewById(R.id.iv3);
        tv4 = (TextView) findViewById(R.id.iv4);
        tv5 = (TextView) findViewById(R.id.iv5);
        tv6 = (TextView) findViewById(R.id.iv6);
        tv7 = (TextView) findViewById(R.id.iv7);
        tv8 = (TextView) findViewById(R.id.iv8);
        tv9 = (TextView) findViewById(R.id.iv9);
        tv10 = (TextView) findViewById(R.id.iv10);
        tv11 = (TextView) findViewById(R.id.iv11);
        tv12 = (TextView) findViewById(R.id.iv12);
        tv13 = (TextView) findViewById(R.id.iv13);
        tv14 = (TextView) findViewById(R.id.iv14);
        tv15 = (TextView) findViewById(R.id.iv15);
        tv16 = (TextView) findViewById(R.id.iv16);

        Typeface face = Typeface.createFromAsset(this.getAssets(),
                "univers_next_arabic.ttf");

        tv1.setTypeface(face);
        tv2.setTypeface(face);
        tv3.setTypeface(face);
        tv4.setTypeface(face);
        tv5.setTypeface(face);
        tv6.setTypeface(face);
        tv7.setTypeface(face);
        tv8.setTypeface(face);
        tv9.setTypeface(face);
        tv10.setTypeface(face);
        tv11.setTypeface(face);
        tv12.setTypeface(face);
        tv13.setTypeface(face);
        tv14.setTypeface(face);
        tv15.setTypeface(face);
        tv16.setTypeface(face);

        String text="عن الدرر السنية : مؤسسة علمية ، دعوية ، إعـلامية ، غير ربحية ، لهـا: غايـةٌ عظيمة : الحفاظ على السنة وميراث النبوة. ورؤيةٌ مستقبلية : نسعى لأن نؤسس: منهجاً مؤصلاً، ونقلاً موثقاً، وعلماً شاملاً، بمحتوى عربيٍ، وانتشارٍ عالميٍ. ورسـالةٌ واضحةٌ : الريادة والتميز في إيجاد مرجعية علمية على منهج أهل السنة والجماعة لكافة المسلمين في أنحاء العالم. وهـدف محـدد : بناء أوثق وأكبر قواعد بيانات إلكترونية للموسوعات العلمية وتيسير الوصول إليها من خلال التقنيات الحديثة.عن التطبيق :مجموعة من الأحاديث التي لا تصح والمنتشرة بين أوساط الناس في الشبكة العالمية (الإنترنت)وغيرها من وسائل التواصل. تُحدث باستمرار.";
        
        
        no = getIntent().getExtras().getInt("no");

        if (isNetworkAvailable()) {

            System.out.println("network available");

            new LoadImage().execute("http://www.dorar.net/banner/api");

        }


        SharedPreferences prfs = getSharedPreferences(prfName,
                Context.MODE_PRIVATE);
        String url = prfs.getString("image_url", "");
        link = prfs.getString("image_link", "");
        //new LoadImage().execute(url);
        adImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {

                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri
                            .parse(link));
                    startActivity(browserIntent);

                }catch (Exception e) {
                    e.printStackTrace();
                }
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri
                        .parse(link));
                startActivity(browserIntent);
            }
        });

        facebook.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent fbIntent = new Intent(Intent.ACTION_VIEW, Uri
                        .parse("http://www.facebook.com/pages/mwq-ldrr-lsny-Dorarnet/251615861901?ref=nf"));
                startActivity(fbIntent);

            }
        });
        twitter.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent twIntent = new Intent(Intent.ACTION_VIEW, Uri
                        .parse("http://twitter.com/#!/dorarnet"));
                startActivity(twIntent);
            }
        });

        desktop.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent twIntent = new Intent(Intent.ACTION_VIEW, Uri
                        .parse("http://www.dorar.net"));
                startActivity(twIntent);
            }
        });


        mail.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("message/rfc822");
                i.putExtra(Intent.EXTRA_EMAIL,"apps@dorar.net");
                i.putExtra(Intent.EXTRA_SUBJECT, "رسالة بخصوص تطبيق احاديث منتشره");
                i.putExtra(Intent.EXTRA_TEXT, "body of email");
                try {
                    startActivity(Intent.createChooser(i, "Send mail..."));
                } catch (android.content.ActivityNotFoundException ex) {

                }

            }
        });
        Button homebtn = (Button) findViewById(R.id.homebtn);
        homebtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent homeintent = new Intent(AboutActivity.this,
                        MainActivity.class);
                startActivity(homeintent);

            }
        });

        maximizeText.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                textSize += 2;
                tv1.setTextSize(textSize);
                tv2.setTextSize(textSize);
                tv3.setTextSize(textSize);
                tv4.setTextSize(textSize);
                tv5.setTextSize(textSize);
                tv6.setTextSize(textSize);
                tv7.setTextSize(textSize);
                tv8.setTextSize(textSize);
                tv9.setTextSize(textSize);
                tv10.setTextSize(textSize);
                tv11.setTextSize(textSize);
                tv12.setTextSize(textSize);
                tv13.setTextSize(textSize);
                tv14.setTextSize(textSize);
                tv15.setTextSize(textSize);
                tv16.setTextSize(textSize);

            }
        });
        minimizeText.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                textSize -= 2;
                tv1.setTextSize(textSize);
                tv2.setTextSize(textSize);
                tv3.setTextSize(textSize);
                tv4.setTextSize(textSize);
                tv5.setTextSize(textSize);
                tv6.setTextSize(textSize);
                tv7.setTextSize(textSize);
                tv8.setTextSize(textSize);
                tv9.setTextSize(textSize);
                tv10.setTextSize(textSize);
                tv11.setTextSize(textSize);
                tv12.setTextSize(textSize);
                tv13.setTextSize(textSize);
                tv14.setTextSize(textSize);
                tv15.setTextSize(textSize);
                tv16.setTextSize(textSize);

            }
        });
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
                HttpPost httpPost = new HttpPost("http://www.dorar.net/banner/api");

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
                System.out.println("the image is : "+image);
                System.out.println("the link is : "+link);

                imageurl = new URL(image);
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
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
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

    @Override
    protected void onPause() {
        hadids.clear();
        super.onPause();
    }

//    @Override
//    protected void onDestroy(){
//        super.onDestroy();
//
//        BugSenseHandler.flush(AboutActivity.this);
//
//    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // your code
            Intent intent = new Intent(AboutActivity.this, MainActivity.class);
            intent.putExtra("no", no);
            startActivity(intent);
            finish();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

}
