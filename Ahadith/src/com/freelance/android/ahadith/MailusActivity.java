package com.freelance.android.ahadith;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;

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

public class MailusActivity extends Activity {

//	Button sendbtn;
//	EditText name, email, message;
	
	WebView wvMailUs;
	
	String messaText;

	ImageView adImage;
	String prfName = "prefs";
	String link;
	URL imageurl = null;

	ArrayList<Integer> hadids = new ArrayList<Integer>();

	int no = 0;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        BugSenseHandler.initAndStartSession(MailusActivity.this, "bfdfcbf3");

        setContentView(R.layout.mailus_new);
		
		wvMailUs=(WebView) findViewById(R.id.wv_mail_us);
		wvMailUs.loadUrl("http://dorar.net/feedback/contact?c4app=1");

//		sendbtn = (Button) findViewById(R.id.sendbtn);
//		name = (EditText) findViewById(R.id.nametxt);
//		email = (EditText) findViewById(R.id.mailtxt);
//		message = (EditText) findViewById(R.id.messtxt);

		adImage = (ImageView) findViewById(R.id.adimage);

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

		Button homebtn = (Button) findViewById(R.id.homebtn);
		homebtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent homeintent = new Intent(MailusActivity.this,
						MainActivity.class);
				startActivity(homeintent);

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
//        BugSenseHandler.flush(MailusActivity.this);
//
//    }

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// your code
			Intent intent = new Intent(MailusActivity.this, MainActivity.class);
			intent.putExtra("no", no);
			startActivity(intent);
			finish();
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}

}
