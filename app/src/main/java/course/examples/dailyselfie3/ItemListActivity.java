package course.examples.dailyselfie3;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;
import android.app.AlarmManager;
import android.app.ListActivity;
import android.app.PendingIntent;

public class ItemListActivity extends ListActivity {

    ItemListAdapter mAdapter;
    String mCurrentPhotoPath;
    File photoFile;

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final String TAG = "Daily Selfie";

    private PendingIntent mNotificationReceiverPendingIntent;
    private Intent mNotificationReceiverIntent;

    private static final long INITIAL_ALARM_DELAY = 2 * 60 * 1000L;
    private static final long REPEAT_ALARM_DELAY = 2 * 60 * 1000L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAdapter = new ItemListAdapter(getApplicationContext());

        getListView().setAdapter(mAdapter);
        Log.i(TAG, "We're running!");
        File storageDir = Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        for (File f : storageDir.listFiles()) {
            if (f.isFile()) {
                Log.i(TAG, f.getAbsolutePath());
                Date lastModDate = new Date(f.lastModified());
                String timeStamp = new SimpleDateFormat("yyyy-MM-d_HH:mm")
                        .format(lastModDate);
                mAdapter.add(new ItemRecord("file:" + f.getAbsolutePath(),
                        timeStamp.toString()));
            }
        }
        getListView().setOnItemClickListener(
                new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        ItemRecord s = (ItemRecord) mAdapter.getItem(position);
                        showBigPicture(s.getUri());
                    }
                });

        setupAlarm();
    }

    private void setupAlarm() {
        mNotificationReceiverIntent = new Intent(ItemListActivity.this,
                AlarmNotificationReceiver.class);

        mNotificationReceiverPendingIntent = PendingIntent.getBroadcast(
                ItemListActivity.this, 0, mNotificationReceiverIntent, 0);

        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        am.setRepeating(AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime() + INITIAL_ALARM_DELAY,
                REPEAT_ALARM_DELAY, mNotificationReceiverPendingIntent);
    }

    private void cancelAlarm() {
        if (null == mNotificationReceiverPendingIntent)
            return;
        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        am.cancel(mNotificationReceiverPendingIntent);
    }

    private void showBigPicture(String imageUri) {
        Intent intent = new Intent();
        // intent.setAction(android.content.Intent.ACTION_SEND);
        intent.setClass(ItemListActivity.this, ItemDetailActivity.class);
        intent.setData(Uri.parse(imageUri));
        // intent.setDataAndType(Uri.parse(imageUri), "image/jpg");
        startActivity(intent);
    }

    private void dispatchTakePictureIntent() {
        Log.i(TAG, "Create Picture Intent");
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(TAG, "On Activity Result");
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Log.i(TAG, "Item recorded and currentPhotoPath is"
                    + mCurrentPhotoPath);
            mAdapter.add(new ItemRecord(mCurrentPhotoPath,
                    new SimpleDateFormat("yyyy-MM-d_HH:mm").format(new Date())));

        }
    }

    private File createImageFile() throws IOException {

        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                .format(new Date());

        String imageFileName = "JPEG_" + timeStamp + "_";

        File storageDir = Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, /* prefix */
                ".jpg", /* suffix */
                storageDir /* directory */
        );

        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        Log.i(TAG, "mCurrentPhotoPath: " + mCurrentPhotoPath);
        return image;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id) {
            case R.id.delete_all:
                mAdapter.removeAllViews();
                Toast.makeText(ItemListActivity.this,
                        "All pictures have been deleted", Toast.LENGTH_SHORT)
                        .show();
                break;
            case R.id.action_camera:
                dispatchTakePictureIntent();
                break;
            case R.id.cancel_alarm:
                cancelAlarm();
                Toast.makeText(ItemListActivity.this,
                        "Your alarm is cancelled", Toast.LENGTH_SHORT).show();
                break;
            case R.id.start_alarm:
                setupAlarm();
                Toast.makeText(ItemListActivity.this,
                        "Your alarm is set up", Toast.LENGTH_SHORT).show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

}