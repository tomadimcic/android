package dunavnet.com.mojbac.activities;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import dunavnet.com.mojbac.R;

public class GalleryActivity extends Activity {

    private static final int SELECT_PICTURE = 1;
    private String selectedImagePath;
 //   private String filemanagerstring;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery);

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Izaberite sliku"), SELECT_PICTURE);
        ((Button) findViewById(R.id.loadpicturebutton)).setOnClickListener(new OnClickListener() {
        	public void onClick(View arg0) {
        		ReportActivity.setImagePath(selectedImagePath);
                finish();
            }
        });
        
                      
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();

                //OI FILE Manager
           //     filemanagerstring = selectedImageUri.getPath();
                //MEDIA GALLERY
                selectedImagePath = getPath(selectedImageUri);

                Bitmap b = BitmapFactory.decodeFile(selectedImagePath);
                ImageView image = (ImageView)findViewById(R.id.imgView);
                image.setImageBitmap(b);
            }
        }
    }

    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if(cursor!=null)
        {
           
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        else return null;
    }
}