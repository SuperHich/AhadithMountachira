package com.freelance.android.ahadith;

public class AhadithListModel {

	public void setHadith(String had) {
		hadith = had;

	}

	public void setHadDeg(String deg) {

		hadDegree = deg;
	}

	public void setClickedMore(boolean more) {
		clicked = more;

	}

	public String getHadith() {

		return hadith;
	}

	public String getHadDeg() {

		return hadDegree;
	}
	


	public String getHadithID() {
		return hadithID;
	}

	public void setHadithID(String hadithID) {
		this.hadithID = hadithID;
	}

	/*public boolean getClickedMore() {
		return clicked;

	}*/

	public String hadith;
	public String hadDegree;
	public boolean clicked;
	public String hadithID;

}
