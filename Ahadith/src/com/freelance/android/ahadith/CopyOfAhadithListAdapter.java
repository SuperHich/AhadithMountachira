
package com.freelance.android.ahadith;

import android.R.id;
import android.R.integer;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

@SuppressLint("NewApi")
public class CopyOfAhadithListAdapter extends BaseAdapter {
	
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

	public CopyOfAhadithListAdapter(Activity context,
			ArrayList<AhadithListModel> hadList) {

		this.con = context;
		ahdithListModel = hadList;

	}
	
	public void setTextSize(float size){
		
		Toast.makeText(con, "" + listView.getChildCount(), Toast.LENGTH_LONG).show();
		
	}

	public CopyOfAhadithListAdapter(int currentSize) {
		counter = currentSize;
		System.out.println("counter : " + currentSize);
	}

	public CopyOfAhadithListAdapter(float textSize) {
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
		Button btnooo;
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
		//Toast.makeText(applicationContext, "wwww",  Toast.LENGTH_LONG).show();
        
        System.out.print("reload all");
		rowView = convertView;
		
		
		
		//expanded = true;
	
	
		//expanded = false;
		curPosition = position;
		Typeface face = Typeface.createFromAsset(con.getAssets(),
				"Bahij_Droid_Naskh-Bold.ttf");
		LayoutInflater mInflater = (LayoutInflater) 
				this.con.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		//if (convertView == null) {
			

			//LayoutInflater inflater = con.getLayoutInflater();
			//convertView  = inflater.inflate(R.layout.hadith_list_row, null);
			convertView = mInflater.inflate(R.layout.hadith_list_row, null);
			holder = new viewHolder();
            holder.hadithTV = (TextView) convertView
					.findViewById(R.id.hadith_txt);
            holder.hadithDg = (TextView) convertView
					.findViewById(R.id.hadith_deg);
            holder.readmorebtn = (Button) convertView
					.findViewById(R.id.readmorebtn);
            
            holder.r=(RelativeLayout) convertView.findViewById(R.id.hhhhh);

            holder.readmorebtn.setTag(holder);
          

            holder.hadithDegTitle = (TextView) convertView
					.findViewById(R.id.hadith_deg_title);


            listView = (ListView) parent.findViewById(android.R.id.list);

			// hadithFontSize=viewHolder.hadithTV.getTextSize();
            hadithFontSize=AhadithList.fonting;

			if (hadithFontSize != 0) {
				
                holder.hadithTV.setTextSize(hadithFontSize);
                holder.hadithDg.setTextSize(hadithFontSize);
                holder.hadithDegTitle.setTextSize(hadithFontSize);

			}
            holder.facebtn = (Button) convertView.findViewById(R.id.fbookbtn);
			holder.twitbtn = (Button) convertView.findViewById(R.id.twiterbtn);
			holder.smsbtn = (Button) convertView.findViewById(R.id.smsbtn);
			holder.whatappbtn = (Button) convertView
					.findViewById(R.id.whatappbtn);
			holder.copybtn = (Button) convertView.findViewById(R.id.copybtn);

			// hadithTV = (TextView) rowView.findViewById(R.id.hadith_txt);
			// hadithDg = (TextView) rowView.findViewById(R.id.hadith_deg);
				
			convertView.setTag(holder);
		//}
			/*else {
			
			convertView = mInflater.inflate(R.layout.hadith_list_row, null);
			holder = new viewHolder();
            holder.hadithTV = (TextView) convertView
					.findViewById(R.id.hadith_txt);
            holder.hadithDg = (TextView) convertView
					.findViewById(R.id.hadith_deg);
            holder.readmorebtn = (Button) convertView
					.findViewById(R.id.readmorebtn);
            
            holder.r=(RelativeLayout) convertView.findViewById(R.id.hhhhh);

            holder.readmorebtn.setTag(holder);
          

            holder.hadithDegTitle = (TextView) convertView
					.findViewById(R.id.hadith_deg_title);


            listView = (ListView) parent.findViewById(android.R.id.list);

			// hadithFontSize=viewHolder.hadithTV.getTextSize();
            hadithFontSize=AhadithList.fonting;

			if (hadithFontSize != 0) {
				
                holder.hadithTV.setTextSize(hadithFontSize);
                holder.hadithDg.setTextSize(hadithFontSize);
                holder.hadithDegTitle.setTextSize(hadithFontSize);

			}
            holder.facebtn = (Button) convertView.findViewById(R.id.fbookbtn);
			holder.twitbtn = (Button) convertView.findViewById(R.id.twiterbtn);
			holder.smsbtn = (Button) convertView.findViewById(R.id.smsbtn);
			holder.whatappbtn = (Button) convertView
					.findViewById(R.id.whatappbtn);
			holder.copybtn = (Button) convertView.findViewById(R.id.copybtn);

			// hadithTV = (TextView) rowView.findViewById(R.id.hadith_txt);
			// hadithDg = (TextView) rowView.findViewById(R.id.hadith_deg);

			convertView.setTag(holder);
			//Toast.makeText(con, ar.length + "", Toast.LENGTH_LONG).show();
        }*/
		holder.pos=position;

		// final LauncherActivity.ListItem item = ahdithListModel.get(position);
try{
				if(arr[position]==1){
					expanded = false;
					holder.expanded =false;
					
				}
				else{
					expanded = true;
					holder.expanded =true;
				}
		}
		catch(Exception ar){
			arr[position]=1;
			expanded= false;
			holder.expanded =false;
					
		}
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
		//holder.btnooo = (Button) convertView.findViewById(R.id.btnooo);
		holder.btnooo.setText(ahdithListModel.get(position).getHadithID()+ "" );
		

		
		
		final int bb = position;
		
        holder.readmorebtn.setOnClickListener(new View.OnClickListener() {
        	
            @Override
            public void onClick(View v) {

                holder = (viewHolder) v.getTag();

                if (holder.expanded == false) {
                //if( holder.hadithTV.getMaxLines()<10){
                    
                	if (Build.VERSION.SDK_INT < 16)
                        con.getResources().getDrawable(R.drawable.up);
                    else
                       holder.readmorebtn.setBackground(con.getResources()
                                .getDrawable(R.drawable.up));
                    
                    holder.hadithTV.setMaxLines(500);
                    holder.hadithDg.setMaxLines(100);
                    
                   
                                       
                  
                  
                  
                          holder.expanded = true;

                } else {

                    if (Build.VERSION.SDK_INT < 16)
                        con.getResources().getDrawable(R.drawable.down);
                    else
                        holder.readmorebtn.setBackground(con.getResources()
                                .getDrawable(R.drawable.down));
                    

                    			
                    holder.hadithTV.setMaxLines(5);
                    holder.hadithDg.setMaxLines(100);
                    

                   holder.expanded = false;
                    try{
                    	System.out.print("width is : " + v.getTop());
                    }
                    catch(Exception ex){
                    System.out.print(ex.toString());	
                    }
                    
                    listView.setSelection(holder.pos);
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

				Intent email = new Intent(Intent.ACTION_SEND);
				email.putExtra(Intent.EXTRA_EMAIL,
						new String[] { "" });
				email.putExtra(Intent.EXTRA_SUBJECT, "الدرر السنية - من تطبيق احاديث منتشره لا تصح");
				email.putExtra(
						Intent.EXTRA_TEXT,
						s.substring(4)
								+ "\n"
								+ "الدرجة : "
								+ degree
								+ "\n"
								+ "( لتحميل التطبيق : http://dorar.net/article/1692   )");
				email.setType("message/rfc822");
				con.startActivity(Intent.createChooser(email,
						"Choose an Email client :"));

			}
		});
		holder.whatappbtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent waIntent = new Intent(Intent.ACTION_SEND);
				waIntent.setType("text/plain");
				waIntent.setPackage("com.whatsapp");
				waIntent.putExtra(Intent.EXTRA_SUBJECT, " الدرر السنية – من تطبيق أحاديث منتشرة لا تصح :\n");
				waIntent.putExtra(Intent.EXTRA_TEXT,
						s.substring(4)
						+ "\n"
						+ "الدرجة : "
						+ degree
						+ "\n"
						+ "( لتحميل التطبيق : http://dorar.net/article/1692 )");
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

		return convertView;

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
