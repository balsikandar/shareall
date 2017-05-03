package camera.touchtalent.com.shareall;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

import camera.touchtalent.com.shareutils.ShareUtil;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int WRITE_EXTERNAL_STORAGE_CODE = 1001;
    final String PREFERENCE_NAME = "sticker_preference";
    final String IMAGE_STORED_STATUS = "image_stored_status";
    final String IMAGE_URI = "image_uri";
    SharedPreferences preferences;
    String stickerPath;
    int permissionCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView displayText = (TextView) findViewById(R.id.displayText);
        Button pickFile = (Button) findViewById(R.id.pickFile);
        Button shareFile = (Button) findViewById(R.id.shareFile);

        permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        preferences = getSharedPreferences(PREFERENCE_NAME, MODE_PRIVATE);
        stickerPath = preferences.getString(IMAGE_URI, "");
        shareFile.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (permissionCheck == PackageManager.PERMISSION_DENIED) {
            askUserForPermission();
        }
    }


    private void askUserForPermission() {
        //requesting user to grant permission
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                WRITE_EXTERNAL_STORAGE_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case WRITE_EXTERNAL_STORAGE_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    storeImageDrawableToDirectory();
                } else {
                    Toast.makeText(this, "Need Storage permission to function properly", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
        }
    }


    public boolean storeImageDrawableToDirectory() {
        boolean isImageStored = preferences.getBoolean(IMAGE_STORED_STATUS, false);

        //storing image from drawable to Media Store
        if (!isImageStored) {
            Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.whatsapp_sticker);
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            b.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            String imagePath = MediaStore.Images.Media.insertImage(getContentResolver(),
                    b, "whatsapp_sticker", null);
            //if image stored successfully
            if (imagePath != null) {
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean(IMAGE_STORED_STATUS, true);
                editor.putString(IMAGE_URI, imagePath);
                editor.apply();
                return true;
            }

            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.shareFile:
                Uri uri = Uri.parse(stickerPath);

                ShareUtil.builder(MainActivity.this)
                        .setUri(uri)
                        .setShareText("hello bali")
                        .share();
                break;
        }

    }
}
