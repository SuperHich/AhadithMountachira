package com.freelance.android.ahadith;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.bugsense.trace.BugSenseHandler;

public class ShareActivity extends Activity {

	Button facebtn, twitbtn, smsbtn,whatappbtn,mailbtn;

	ArrayList<String> hadList = new ArrayList<String>();
	ArrayList<Integer> hadids = new ArrayList<Integer>();


	boolean flag = false;
	int no = 0;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        BugSenseHandler.initAndStartSession(ShareActivity.this, "bfdfcbf3");

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_share);
		facebtn = (Button) findViewById(R.id.fbookbtn);
		twitbtn = (Button) findViewById(R.id.twiterbtn);
		smsbtn = (Button) findViewById(R.id.smsbtn);
		whatappbtn=(Button) findViewById(R.id.whatappbtn);
		mailbtn=(Button) findViewById(R.id.mailbtn);
		
		no = getIntent().getExtras().getInt("no");


		facebtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String outCipherText = "الدرر السنية  –\n بادر بتحميل تطبيق أحاديث منتشرة لا تصح – http://dorar.net/article/1692 ";

				Intent sharingIntent = new Intent(Intent.ACTION_SEND);
				sharingIntent.setType("text/plain");
                sharingIntent.setPackage("com.facebook.katana");
				sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT,outCipherText
						);
				startActivity(Intent.createChooser(sharingIntent,
						"Share using facebook"));
				finish();

			}
		});
		twitbtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				String outCipherText = "الدرر السنية  –\n بادر بتحميل تطبيق أحاديث منتشرة لا تصح – http://dorar.net/article/1692 ";

				Intent waIntent = new Intent(Intent.ACTION_SEND);
				waIntent.setType("text/plain");
				waIntent.setPackage("com.twitter.android");
				waIntent.putExtra(android.content.Intent.EXTRA_TEXT,outCipherText);
				startActivity(Intent.createChooser(waIntent,
						"Share with twitter"));

				finish();

			}
		});
		mailbtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
//				Intent twIntent = new Intent(Intent.ACTION_VIEW, Uri
//						.parse("https://www.twitter.com/"));
//				startActivity(twIntent);
//				Intent email = new Intent(Intent.ACTION_SEND);
//
//				email.putExtra(Intent.EXTRA_SUBJECT, "بادر بتحميل تطبيق أحاديث منتشرة لا تصح");
//				email.putExtra(
//						Intent.EXTRA_TEXT,"بادر بتحميل تطبيق أحاديث منتشرة لا تصح – http://dorar.net/article/1692"
//						);
//				email.setType("message/rfc822");
//				startActivity(Intent.createChooser(email,
//						"Choose an Email client :"));
				
				Utils.shareWithMail(ShareActivity.this, "", "بادر بتحميل تطبيق أحاديث منتشرة لا تصح", "بادر بتحميل تطبيق أحاديث منتشرة لا تصح – http://dorar.net/article/1692", "Choose an Email client :");
				finish();

			}
		});
		smsbtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String outCipherText = "الدرر السنية  –\n بادر بتحميل تطبيق أحاديث منتشرة لا تصح – http://dorar.net/article/1692 ";
				String phoneNumber = "";

				String uri = "smsto:" + phoneNumber;
				Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse(uri));
				intent.putExtra("sms_body", outCipherText);
				intent.putExtra("compose_mode", true);
				startActivity(intent);
				finish();

			}
		});
		whatappbtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String outCipherText = "الدرر السنية  –\n بادر بتحميل تطبيق أحاديث منتشرة لا تصح – http://dorar.net/article/1692 ";
				Intent waIntent = new Intent(Intent.ACTION_SEND);
				waIntent.setType("text/plain");
//				waIntent.setPackage("com.whatsapp");
				waIntent.putExtra(android.content.Intent.EXTRA_TEXT, outCipherText);
				startActivity(Intent.createChooser(waIntent,
						"Share with what's app"));
				finish();

			}
		});
	}


	@Override
	protected void onPause() {
		hadids.clear();
		hadList.clear();
		super.onPause();
	}

//    @Override
//    protected void onDestroy(){
//        super.onDestroy();
//
//        BugSenseHandler.flush(ShareActivity.this);
//
//    }

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// your code
			Intent intent = new Intent(ShareActivity.this, MainActivity.class);
			intent.putExtra("no", no);
			startActivity(intent);
			finish();
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}
	
}
