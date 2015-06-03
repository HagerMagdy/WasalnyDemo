package com.Wasalny.wasalnydemo.activites;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.widget.EditText;

public class Constants {
	

	//Address;CompanyID,DoctorID,DoctorName,EmailAddress,GenderID,MobileNumber,PatientDuration,Specialty
	public static boolean isNetworkOnline(Context context) {
		boolean status = false;
		try {
			// ConnectivityManager connectivity = (ConnectivityManager) context
			// .getSystemService(Context.CONNECTIVITY_SERVICE);
			ConnectivityManager cm = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo netInfo = cm.getNetworkInfo(0);
			if (netInfo != null
					&& netInfo.getState() == NetworkInfo.State.CONNECTED) {
				status = true;
			} else {
				netInfo = cm.getNetworkInfo(1);
				if (netInfo != null
						&& netInfo.getState() == NetworkInfo.State.CONNECTED)
					status = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return status;

	}
	public static boolean isEmpty(String text) {
		if (TextUtils.isEmpty(text)
				|| TextUtils.isEmpty(text)) {
			return true;
		}
		return false;
	}
}
