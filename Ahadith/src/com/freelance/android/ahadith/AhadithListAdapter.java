
package com.freelance.android.ahadith;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class AhadithListAdapter extends BaseAdapter {
	
	static Activity con;
	Context applicationContext;
	static ArrayList<AhadithListModel> ahdithListModel;
	boolean expanded = false;
	static viewHolder holder = null;
	int curPosition;
	int counter = 0;
	View rowView;
	public static  ListView listView;
	Button maxing;
	Button mining;
	static int [] ar; 
	
	

	// static TextView hadithTV;
	// static TextView hadithDg;
	float hadithFontSize=14;

	public AhadithListAdapter(Activity context,
			ArrayList<AhadithListModel> hadList) {

		this.con = context;
		ahdithListModel = hadList;

	}
	
	public void setTextSize(float size){
		
		Toast.makeText(con, "" + listView.getChildCount(), Toast.LENGTH_LONG).show();
		
	}

	public AhadithListAdapter(int currentSize) {
		counter = currentSize;
		System.out.println("counter : " + currentSize);
	}

	public AhadithListAdapter(float textSize) {
		//this(con, ahdithListModel);
		this.hadithFontSize = textSize;
	}
	
	public static void hisham(ListView lv , int fontSize ){
	
		//System.out.print(lv.getChildCount());
		
	}

	public class viewHolder {

		TextView hadithTV;
		TextView hadithDg;
		TextView hadithDegTitle;
		Button readmorebtn;
		Button facebtn;
		Button twitbtn;
		Button smsbtn;
		Button whatappbtn;
		Button copybtn;
		RelativeLayout r;
		ListView listView;
		boolean expanded = false;
		int pos = 0;
		
	}
	int[] arr = new int[15];
	/*
	@SuppressLint("NewApi")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		viewHolder holder;
		View view1 = convertView;
		String a;

			 listView = (ListView) parent.findViewById(android.R.id.list);
			 listView.setTranscriptMode(ListView.TRANSCRIPT_MODE_DISABLED);

		if(convertView==null)
	    {
			LayoutInflater mInflater = (LayoutInflater) 
					this.con.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			view1 = mInflater.inflate(R.layout.hadith_list_row, null);
			holder = new viewHolder();
			
			// listView = (ListView) parent.findViewById(android.R.id.list);
			
			
			a="Hisham";
			view1.setTag(holder);
			
	    }
		else{
			LayoutInflater mInflater = (LayoutInflater) 
					this.con.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			view1 = mInflater.inflate(R.layout.hadith_list_row, null);
			
			 holder=(viewHolder)view1.getTag();
			
			
			
			
			
			view1.setTag(holder);	
			a="ali";
			
			if(holder.expanded){
				
			}
		}
		 
		
		Button btn1 = (Button) view1.findViewById(R.id.btnForHIding);
		btn1.setText(a + "");	
		
		 
		btn1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			Button btn2 = (Button) v;
				btn2.setText("wwwwwwwww");
				
				
				View v3 = (View) btn2.getParent();
				v3.setAlpha(0);
				expanded=true;
			}
		});
		 
		 
		
		
		
	return view1;
	}
	*/

	@SuppressLint("NewApi")
	@Override
	
	public View getView(int position, View convertView, ViewGroup parent) {
		viewHolder holder = null;
		rowView = convertView;
        LayoutInflater mInflater = (LayoutInflater) 
            con.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (rowView == null) {
        	rowView = mInflater.inflate(R.layout.hadith_list_row, null);
            holder = new viewHolder();
            
            holder.expanded=false;
            expanded=false;
			
			
			
			
			
			System.out.print("Hisham GOOD");
			//Toast.makeText(applicationContext, "Good", Toast.LENGTH_LONG).show();
			
			
            rowView.setTag(holder);
            
            
        }
        else {
        	//Toast.makeText(applicationContext, "Good2", Toast.LENGTH_LONG).show();
        	System.out.print("Hisham Bad");
            holder = (viewHolder) rowView.getTag();
            System.out.print("we are here now ? " + position );
            holder.hadithTV.setMaxLines(8);
            holder.hadithDg.setMaxLines(100);
            if (Build.VERSION.SDK_INT < 16)
                con.getResources().getDrawable(R.drawable.down);
            else
                holder.readmorebtn.setBackground(con.getResources()
                        .getDrawable(R.drawable.down));
            

        	if(AhadithList.fonting>=20){
        		System.out.print("Here The Size");
        		holder.hadithTV.setMaxLines(8);
                holder.hadithDg.setMaxLines(100);
        	}
        	else{
        		holder.hadithTV.setMaxLines(8);
                holder.hadithDg.setMaxLines(100);	
        	}
        		
            
            

            holder.expanded=false;
            expanded=false;
        }
        
        // cuted
        holder.expanded=false;
        expanded=false;
        //holder.btnooo = (Button) rowView.findViewById(R.id.btnooo);
        holder.hadithTV = (TextView) rowView
				.findViewById(R.id.hadith_txt);
        holder.hadithDg = (TextView) rowView
				.findViewById(R.id.hadith_deg);
        holder.readmorebtn = (Button) rowView
				.findViewById(R.id.readmorebtn);
        
        holder.r=(RelativeLayout) rowView.findViewById(R.id.hhhhh);

        holder.readmorebtn.setTag(holder);
      

        holder.hadithDegTitle = (TextView) rowView
				.findViewById(R.id.hadith_deg_title);


        listView = (ListView) parent.findViewById(android.R.id.list);

		// hadithFontSize=viewHolder.hadithTV.getTextSize();
        hadithFontSize=AhadithList.fonting;
        

		if (hadithFontSize != 0) {
			
            holder.hadithTV.setTextSize(hadithFontSize);
            holder.hadithDg.setTextSize(hadithFontSize);
            holder.hadithDegTitle.setTextSize(hadithFontSize);

		}
        holder.facebtn = (Button) rowView.findViewById(R.id.fbookbtn);
		holder.twitbtn = (Button) rowView.findViewById(R.id.twiterbtn);
		holder.smsbtn = (Button) rowView.findViewById(R.id.smsbtn);
		holder.whatappbtn = (Button) rowView
				.findViewById(R.id.whatappbtn);
		holder.copybtn = (Button) rowView.findViewById(R.id.copybtn);
        
        
        
        
        
        
        
         // set the text
       
        holder.pos=position;
        curPosition = position;
		Typeface face = Typeface.createFromAsset(con.getAssets(),
				"Bahij_Droid_Naskh-Bold.ttf");
		// final LauncherActivity.ListItem item = ahdithListModel.get(position);

		final String s = (counter + position + 1) + " - "
				+ ahdithListModel.get(position).getHadith();
		
		//s=s.substring(0,);
		
		
		final String degree = ahdithListModel.get(position).getHadDeg();
		final String hadithID = ahdithListModel.get(position).getHadithID();
		// System.out.println("viewHolder.layout.getHeight()"+holder.layout.getHeight());
		holder.hadithTV.setTypeface(face);
		holder.hadithDg.setTypeface(face);
		holder.hadithDegTitle.setTypeface(face);
		holder.hadithTV.setText(s);
		holder.hadithDg.setText(ahdithListModel.get(position).getHadDeg());
        
        
        
        
        //holder.btnooo.setText(ahdithListModel.get(position).getHadithID()+ ""); 
        
        
        // expanding here 
holder.readmorebtn.setOnClickListener(new View.OnClickListener() {
        	
            @Override
            public void onClick(View v) {

                viewHolder holder2 = (viewHolder) v.getTag();

                if (holder2.expanded == false) {
                //if( holder.hadithTV.getMaxLines()<10){
                    
                	if (Build.VERSION.SDK_INT < 16)
                        con.getResources().getDrawable(R.drawable.up);
                    else
                       holder2.readmorebtn.setBackground(con.getResources()
                                .getDrawable(R.drawable.up));
                    
                    holder2.hadithTV.setMaxLines(500);
                    holder2.hadithDg.setMaxLines(100);
                    
                   
                                       
                  
                  
                  
                          holder2.expanded = true;

                } else {

                    if (Build.VERSION.SDK_INT < 16)
                        con.getResources().getDrawable(R.drawable.down);
                    else
                        holder2.readmorebtn.setBackground(con.getResources()
                                .getDrawable(R.drawable.down));
                    

                    			
                    holder2.hadithTV.setMaxLines(8);
                    holder2.hadithDg.setMaxLines(100);
                    

                   holder2.expanded = false;
                    try{
                    	System.out.print("width is : " + v.getTop());
                    }
                    catch(Exception ex){
                    System.out.print(ex.toString());	
                    }
                    
                    listView.setSelection(holder2.pos);
                    //RelativeLayout rr = (RelativeLayout) convertView.findViewById(R.id.header);
                    
                   // holder.listView.scrollTo(0);

                    //holder.listView.smoothScrollToPosition(v.getScrollY());
                   // holder.listView.smoothScrollByOffset(-1);
                    

                }

            }
        });

holder.facebtn.setOnClickListener(new View.OnClickListener() {

	@Override
	public void onClick(View v) {

		String tweetUrl = "http://dorar.net/spreadH/" + hadithID;

		Intent sharingIntent = new Intent(Intent.ACTION_SEND);
		sharingIntent.setType("text/plain");
        sharingIntent.setPackage("com.facebook.katana");
		sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT,tweetUrl
				);

		con.startActivity(Intent.createChooser(sharingIntent,
				"Share using facebook"));

	}
});
holder.twitbtn.setOnClickListener(new View.OnClickListener() {

	@Override
	public void onClick(View v) {
		String tweetUrl = "http://dorar.net/spreadH/" + hadithID;

		Intent waIntent = new Intent(Intent.ACTION_SEND);
		waIntent.setType("text/plain");
		waIntent.setPackage("com.twitter.android");
		waIntent.putExtra(android.content.Intent.EXTRA_TEXT,
				"الدرر السنية – من تطبيق أحاديث منتشرة لا تصح : "
						+ tweetUrl);
		con.startActivity(Intent.createChooser(waIntent,
				"Share with twitter"));

	}
});
final int pos = position;
holder.smsbtn.setOnClickListener(new View.OnClickListener() {
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		// String phoneNumber = "";
		// String uri = "smsto:" + phoneNumber;
		// Intent intent = new Intent(Intent.ACTION_SENDTO,
		// Uri.parse(uri));
		//
		// intent.putExtra("sms_body", s);
		// intent.putExtra("compose_mode", true);
		// con.startActivity(intent);

//		Intent email = new Intent(Intent.ACTION_SEND);
//		email.putExtra(Intent.EXTRA_EMAIL,
//				new String[] { "" });
//		email.putExtra(Intent.EXTRA_SUBJECT, "الدرر السنية - من تطبيق احاديث منتشره لا تصح");
//		email.putExtra(
//				Intent.EXTRA_TEXT,
//				ahdithListModel.get(pos).getHadith()
//						+ "\n"
//						+ "الدرجة : "
//						+ degree
//						+ "\n"
//						+ "( لتحميل التطبيق : http://dorar.net/article/1692   )");
//		email.setType("message/rfc822");
//		con.startActivity(Intent.createChooser(email,
//				"Choose an Email client :"));
		
		String title = "الدرر السنية - من تطبيق احاديث منتشره لا تصح"; 
		String content = ahdithListModel.get(pos).getHadith()
				+ "\n"
				+ "الدرجة : "
				+ degree
				+ "\n"
				+ "( لتحميل التطبيق : http://dorar.net/article/1692   )";
		Utils.shareWithMail(con, "", title, content, "Choose an Email client :");

	}
});
holder.whatappbtn.setOnClickListener(new View.OnClickListener() {

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		Intent waIntent = new Intent(Intent.ACTION_SEND);
		waIntent.setType("text/plain");
//		waIntent.setPackage("com.whatsapp");
		
		int textCount = s.length();
		
		//Toast.makeText(con, textCount + " all" ,Toast.LENGTH_LONG).show();
		int titlePostion;
		String title="";
		int descStart ,descEnd;
		String desc="";
		
		if(textCount > 2000){
			titlePostion = s.length()/2+(s.length()/2)/2;
			title = s.substring(0,titlePostion);
			title = title.substring(0,title.lastIndexOf(' '));
			//Toast.makeText(con, title.length() + " Start ",Toast.LENGTH_LONG).show();
			descStart = title.length() ;
			title=title.substring(4)+"";
			//Toast.makeText(con, descStart + " Start ",Toast.LENGTH_LONG).show();
			desc = s.substring(descStart);
			
		}
		else{
			title="";
			desc=s.substring(4);
			
		}
		
		
		int mom = 0;
		if(s.length()>6000){
			mom=6000;
		}
		
				
		waIntent.putExtra(Intent.EXTRA_SUBJECT, " الدرر السنية – من تطبيق أحاديث منتشرة لا تصح :\n" 
		+title 
		
				);
		
		
		waIntent.putExtra(android.content.Intent.EXTRA_TEXT,
				desc
				+ "\n"
				+ "الدرجة : "
				+ degree
				+ "\n"
				+ "( لتحميل التطبيق : http://dorar.net/article/1692 )");
		//waIntent.putExtra(android.content.Intent.EXTRA_TEXT,"wwwwwwwww");
		con.startActivity(Intent.createChooser(waIntent,
				"Share with what's app"));

	}
});
holder.copybtn.setOnClickListener(new View.OnClickListener() {

	@SuppressWarnings("deprecation")
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		String outCipherText = s;
		int sdk = android.os.Build.VERSION.SDK_INT;

		if (sdk < android.os.Build.VERSION_CODES.HONEYCOMB) {

			@SuppressWarnings("static-access")
			android.text.ClipboardManager clipboard = (android.text.ClipboardManager) con
					.getSystemService(con.CLIPBOARD_SERVICE);


                   String clip =
                                    "الدرر السنية – من تطبيق أحاديث منتشرة لا تصح : \n"
                                    +
                                    s.substring(3)
                                    + "\n"
                                    + "الدرجة : "
                                    + degree
                                    + "( لتحميل التطبيق : http://dorar.net/article/1692   )";

            clipboard.setText(clip);

			clipboard.setText(outCipherText);

		} else {

			@SuppressWarnings("static-access")
			android.content.ClipboardManager clipboard = (android.content.ClipboardManager) con
					.getSystemService(con.CLIPBOARD_SERVICE);
			android.content.ClipData clip = android.content.ClipData
					.newPlainText(
							"الدرر السنية – من تطبيق أحاديث منتشرة لا تصح : \n"
									,
                            "الدرر السنية – من تطبيق أحاديث منتشرة لا تصح : \n" +
							outCipherText.substring(4)
									+ "\n"
									+ "الدرجة : "
									+ degree
									+ "( لتحميل التطبيق : http://dorar.net/article/1692   )");
			clipboard.setPrimaryClip(clip);
		}

		Toast.makeText(con, "تم النسخ بنجاح", Toast.LENGTH_LONG).show();

	}
});
System.out.println("s.trim().length() +++++++++++++++++++++++++ : "
		+ s.replaceAll("\\s", "").length());  
if (s.replaceAll("\\s", "").length() < 220) {
	holder.readmorebtn.setVisibility(View.GONE);
} else {
	holder.readmorebtn.setVisibility(View.VISIBLE);
}

        return rowView;
    }
	

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (ahdithListModel.size() > 15) {
			return 15;
		} else {
			return ahdithListModel.size();
		}
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return ahdithListModel.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	
}
