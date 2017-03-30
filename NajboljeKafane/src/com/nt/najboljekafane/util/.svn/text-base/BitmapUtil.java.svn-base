package com.nt.najboljekafane.util;

import java.io.File;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.util.Log;

import com.bugsense.trace.BugSenseHandler;

public class BitmapUtil {
	public static int getImageExifOrientation(Context context, String imagePath) {
		try {
			File imageFile = new File(imagePath);
			ExifInterface exif = new ExifInterface(imageFile.getAbsolutePath());
			int orientation = exif.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);

			Log.v("ImageRotationUtil", "Exif orientation: " + orientation);
			return orientation;
		} catch (Exception e) {
			BugSenseHandler.sendException(e);
			BugSenseHandler.flush(context);
		}
		return ExifInterface.ORIENTATION_NORMAL;
	}

	public static Bitmap rotateBitmap(Bitmap bitmap, int orientation) {
		if (orientation == 1) {
			return bitmap;
		}

		Matrix matrix = new Matrix();
		switch (orientation) {
		case 2:
			matrix.setScale(-1, 1);
			break;
		case 3:
			matrix.setRotate(180);
			break;
		case 4:
			matrix.setRotate(180);
			matrix.postScale(-1, 1);
			break;
		case 5:
			matrix.setRotate(90);
			matrix.postScale(-1, 1);
			break;
		case 6:
			matrix.setRotate(90);
			break;
		case 7:
			matrix.setRotate(-90);
			matrix.postScale(-1, 1);
			break;
		case 8:
			matrix.setRotate(-90);
			break;
		default:
			return bitmap;
		}

		try {
			Bitmap oriented = Bitmap.createBitmap(bitmap, 0, 0,
					bitmap.getWidth(), bitmap.getHeight(), matrix, true);
			bitmap.recycle();
			return oriented;
		} catch (OutOfMemoryError e) {
			BugSenseHandler.sendException(new Exception(e));
			return bitmap;
		}
	}
}
