package com.Wasalny.wasalnydemo.activites;

import android.graphics.Bitmap;

public class FoursquareVenue {
	private String name;
	private String city;
	private String latitude;
	private String longtude;
	private String image;
	private String category;
	private String prifex;
	private String suffix;
	private	Bitmap mbitmap;
	private	String id;
	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Bitmap getMbitmap() {
		return mbitmap;
	}

	public void setMbitmap(Bitmap mbitmap) {
		this.mbitmap = mbitmap;
	}

	public String getPrifex() {
		return prifex;
	}

	public void setPrifex(String prifex) {
		this.prifex = prifex;
	}

	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	public FoursquareVenue() {
		this.name = "";
		this.city = "";
		this.setCategory("");
	}

	public String getCity() {
		if (city.length() > 0) {
			return city;
		}
		return city;
	}

	public void setCity(String city) {
		if (city != null) {
			this.city = city.replaceAll("\\(", "").replaceAll("\\)", "");
			;
		}
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongtude() {
		return longtude;
	}

	public void setLongtude(String longtude) {
		this.longtude = longtude;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

}
