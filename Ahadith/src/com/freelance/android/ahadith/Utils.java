package com.freelance.android.ahadith;

import java.util.ArrayList;
import java.util.List;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.LabeledIntent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;

public class Utils {

	public static void shareWithMail(Context con, String emailTo, String title, String content, String chooserTitle){
		// Intents with SEND action
		PackageManager packageManager = con.getPackageManager();
		Intent sendIntent = new Intent(Intent.ACTION_SEND);
		sendIntent.setType("text/plain");
		List<ResolveInfo> resolveInfoList = packageManager.queryIntentActivities(sendIntent, 0);

		List<LabeledIntent> intentList = new ArrayList<LabeledIntent>();

		for (int j = 0; j < resolveInfoList.size(); j++) {
		    ResolveInfo resolveInfo = resolveInfoList.get(j);
		    String packageName = resolveInfo.activityInfo.packageName;
		    Intent intent = new Intent();
		    intent.setAction(Intent.ACTION_SEND);
		    intent.setComponent(new ComponentName(packageName, resolveInfo.activityInfo.name));
		    intent.setType("text/plain");

		    if (packageName.contains("twitter")) {
		        intent.putExtra(Intent.EXTRA_TEXT, "com.twitter.android" + "https://play.google.com/store/apps/details?id=" + con.getPackageName());
		    } else {
		        // skip android mail and gmail to avoid adding to the list twice
		        if (packageName.contains("android.email") || packageName.contains("android.gm")) {
		            continue;
		        }
		        intent.putExtra(Intent.EXTRA_TEXT, "com.facebook.katana" + "https://play.google.com/store/apps/details?id=" + con.getPackageName());
		    }

		    if ((packageName.contains("android.email") || packageName.contains("android.gm"))){
		    	intentList.add(new LabeledIntent(intent, packageName, resolveInfo.loadLabel(packageManager), resolveInfo.icon));
		    }
		}

		Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:".concat(emailTo)));
		emailIntent.putExtra(Intent.EXTRA_SUBJECT, title);
		emailIntent.putExtra(Intent.EXTRA_TEXT, content);

		con.startActivity(Intent.createChooser(emailIntent, chooserTitle).putExtra(Intent.EXTRA_INITIAL_INTENTS, intentList.toArray(new LabeledIntent[intentList.size()])));
	}
	
//	public static void shareWithoutMail_FB_TW(Context con, String title, String content, String chooserTitle){
//		// Intents with SEND action
//		PackageManager packageManager = con.getPackageManager();
//		Intent sendIntent = new Intent(Intent.ACTION_SEND);
//		sendIntent.setType("text/plain");
//		List<ResolveInfo> resolveInfoList = packageManager.queryIntentActivities(sendIntent, 0);
//
//		List<LabeledIntent> intentList = new ArrayList<LabeledIntent>();
//
//		for (int j = 0; j < resolveInfoList.size(); j++) {
//		    ResolveInfo resolveInfo = resolveInfoList.get(j);
//		    String packageName = resolveInfo.activityInfo.packageName;
//		    Intent intent = new Intent();
//		    intent.setAction(Intent.ACTION_SEND);
//		    intent.setComponent(new ComponentName(packageName, resolveInfo.activityInfo.name));
//		    intent.setType("text/plain");
//
//		    if (packageName.contains("twitter")) {
//		        intent.putExtra(Intent.EXTRA_TEXT, "com.twitter.android" + "https://play.google.com/store/apps/details?id=" + con.getPackageName());
//		    } else if (packageName.contains("facebook")) {
//		        // skip android mail and gmail to avoid adding to the list twice
////		        if (!packageName.contains("android.email") && !packageName.contains("android.gm")) {
////		            continue;
////		        }
//		        intent.putExtra(Intent.EXTRA_TEXT, "com.facebook.katana" + "https://play.google.com/store/apps/details?id=" + con.getPackageName());
//		    } else if(packageName.contains("android.email")){
//		    	intent.putExtra(Intent.EXTRA_TEXT, "android.email");
//		    } else if(packageName.contains("android.gm")){
//		    	intent.putExtra(Intent.EXTRA_TEXT, "android.gm");
//		    } 
//		    
//		    if (packageName.contains("android.email") || packageName.contains("android.gm")) {
//	            continue;
////	        }else if (!packageName.contains("android.email") && !packageName.contains("android.gm") && !packageName.contains("twitter") && !packageName.contains("facebook")){
//		    }else {
//		    	intentList.add(new LabeledIntent(intent, packageName, resolveInfo.loadLabel(packageManager), resolveInfo.icon));
//		    }
//		}
//
//		Intent shareIntent = new Intent(Intent.ACTION_SEND);
//		shareIntent.putExtra(Intent.EXTRA_SUBJECT, title);
//		shareIntent.putExtra(Intent.EXTRA_TEXT, content);
//
//		con.startActivity(Intent.createChooser(shareIntent, chooserTitle).putExtra(Intent.EXTRA_INITIAL_INTENTS, intentList.toArray(new LabeledIntent[intentList.size()])));
//	}
	
	  public static String cleanPonctuation(String text){
	    	text = text.replace("َ", ""); // fatha
	    	text = text.replace("ُ", ""); // dhamma
	        text = text.replace("ِ", ""); // kasra
	        text = text.replace("ً", ""); // fatha double
	        text = text.replace("ٌ", ""); // dhamma double
	        text = text.replace("ٍ", ""); // kasra double
	        text = text.replace("ْ", ""); // soukoun
	        text = text.replace("ّ", ""); // chadda
	        return text;
	    }
}
