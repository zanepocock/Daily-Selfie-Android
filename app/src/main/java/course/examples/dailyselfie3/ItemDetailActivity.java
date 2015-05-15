package course.examples.dailyselfie3;

import java.io.FileNotFoundException;
import java.io.IOException;

import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;
import android.app.Activity;
import android.graphics.Bitmap;

public class ItemDetailActivity extends Activity {

    static final String TAG = "ItemDetailActivity";
    private Bitmap mBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);
        Uri imageUri = getIntent().getData();

        if (imageUri != null) {
            ImageView mImageView = (ImageView) findViewById(R.id.detail);
            try {
                mBitmap = MediaStore.Images.Media.getBitmap(
                        getContentResolver(), imageUri);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mImageView.setImageBitmap(mBitmap);
            Log.i(TAG, "Set Image");
        }

    }

}