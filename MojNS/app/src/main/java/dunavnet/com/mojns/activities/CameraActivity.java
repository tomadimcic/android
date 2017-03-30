package dunavnet.com.mojns.activities;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

public class CameraActivity extends Activity{

	private static final int CAMERA_PIC_REQUEST = 1313;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

			Intent cameraIntent = new Intent(
					MediaStore.ACTION_IMAGE_CAPTURE);
			cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File("/sdcard/%d.jpg")));
			startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);

	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == CAMERA_PIC_REQUEST && resultCode != 0) {
            ReportActivity.setImageCheck(true);
			finish();
			
		}
		else{
			if(resultCode == 0)
				finish();
			}
	}
	

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
	}
	
}
