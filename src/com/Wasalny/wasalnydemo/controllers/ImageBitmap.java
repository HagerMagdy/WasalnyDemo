package com.Wasalny.wasalnydemo.controllers;



	import java.io.IOException;
	import java.io.InputStream;

	import org.apache.http.HttpEntity;
	import org.apache.http.HttpResponse;
	import org.apache.http.HttpStatus;
	import org.apache.http.client.methods.HttpGet;
	import org.apache.http.impl.client.DefaultHttpClient;

	import android.graphics.Bitmap;
	import android.graphics.BitmapFactory;
	import android.util.Log;

	/*
	 * This Class used to accept URL and return BitmapImage  
	 * */
	public class ImageBitmap {
	    
	    private DefaultHttpClient client = new DefaultHttpClient();
	    
	    public Bitmap getBitmapImage(String url) {
	        Bitmap bitmap = null;
	        try {
	            if (url == null || url.equals("")) {
	                bitmap = null;
	                Log.e("Bitmap Error",url);
	            } else {
	             Log.e("Bitmap good", "url is " + url);
	                InputStream is = OpenHttpConnection(url);
	                bitmap = BitmapFactory.decodeStream(is);
	            }
	        } catch (Exception e) {
	            // TODO: handle exception
	         Log.e("getBitmapImage", "Exception occurred, " + e.getMessage());
	         e.printStackTrace();
	         bitmap=null;
	        }
	        
	        return bitmap;
	    }
	    
	    private InputStream OpenHttpConnection(String url) {
	        HttpGet getRequest = new HttpGet(url);
	        try {
	            HttpResponse getResponse = client.execute(getRequest);
	            final int statusCode = getResponse.getStatusLine().getStatusCode();
	            if (statusCode != HttpStatus.SC_OK) {
	                return null;
	            }
	            
	            HttpEntity getResponseEntity = getResponse.getEntity();
	            if (getResponseEntity != null) {
	                return getResponseEntity.getContent();
	            }
	            
	        } catch (IOException e) {
	            getRequest.abort();
	            Log.w(getClass().getSimpleName(), "Error for URL " + url, e);
	        }
	        return null;
	        
	    }
	    
	    // public InputStream ConvertStringTIS(String imageData) {
	    // InputStream is = null;
	    // try {
	    // is = new ByteArrayInputStream(imageData.getBytes());
	    // Log.e("here:::::::::::::::::    ",""+is.toString());
	    // } catch (UnsupportedEncodingException e) {
	    // e.printStackTrace();
	    //
	    // }
	    // return is;
	    //
	    // }
	}/*
	 String thumbUrl = adsArray.getJSONObject(i)
	         .getString("photo").trim();

	       if (thumbUrl != null || thumbUrl != "") {
	        ImageBitmap imageBitmap = new ImageBitmap();
	        bitmap_Image = imageBitmap
	          .getBitmapImage(thumbUrl);
	        Log.d("bitmapimage", thumbUrl + "");
	        Log.d("bitmapthumb", bitmap_Image + "");
	       }

	       if (bitmap_Image == null) {
	        bitmap_Image = BitmapFactory.decodeResource(
	          getApplicationContext().getResources(),
	          R.drawable.photo);
	       } else {
	        ad.setThumbImage(bitmap_Image);
	        catList.add(ad);
	       }

}*/
