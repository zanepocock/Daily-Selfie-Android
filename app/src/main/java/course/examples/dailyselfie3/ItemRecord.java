package course.examples.dailyselfie3;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class ItemRecord {
    private Bitmap mPictureBitmap;
    private String mDate;

    private String Uri;

    static final String TAG = "ItemRecord";

    public ItemRecord(Bitmap pictureBitmap, String date) {
        this.mPictureBitmap = pictureBitmap;
        this.mDate = date;
    }

    public ItemRecord(Bitmap pictureBitmap) {
        this.mPictureBitmap = pictureBitmap;
    }

    public ItemRecord(String url, String date) {

        Bitmap bm = null;
        InputStream is = null;
        BufferedInputStream bis = null;
        try {
            URLConnection conn = new URL(url).openConnection();
            conn.connect();
            is = conn.getInputStream();
            bis = new BufferedInputStream(is, 8192);
            bm = BitmapFactory.decodeStream(bis);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        this.mPictureBitmap = bm;
        this.mDate = date;
        this.Uri = url;
    }



    public Bitmap getPicture() {

        return mPictureBitmap;
    }

    public void setPicture(Bitmap pictureBitmap) {
        this.mPictureBitmap = pictureBitmap;
    }

    public String getUri() {
        return Uri;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        this.mDate = date;
    }

}