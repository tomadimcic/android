package com.nt.najboljekafane.gui;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentTransaction;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuItem.OnMenuItemClickListener;
import com.bugsense.trace.BugSenseHandler;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Request.Callback;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.Session.NewPermissionsRequest;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphObject;
import com.facebook.widget.LoginButton;
import com.nt.najboljekafane.R;
import com.nt.najboljekafane.gui.adapter.GalleryGridAdapter;
import com.nt.najboljekafane.model.FacebookImage;
import com.nt.najboljekafane.util.BitmapUtil;

public class GalerijaActivity extends SherlockFragmentActivity implements
		OnItemClickListener, ActionBar.TabListener {

	private static final int TAB_SVE_SLIKE = 0;
	// private static final int TAB_VASE_SLIKE = 1;

	public static final String EXTRA_FACEBOOK_IMAGE_LIST = "fbImageList";
	public static final String EXTRA_SELECTED_POSITION = "selPos";

	private static final String FQL_MULTIQUERY = "{"
			+ "'posts' : 'SELECT post_id, actor_id, target_id, message, attribution, created_time, attachment FROM stream WHERE source_id=406074909466689 and attribution=\"Najbolje Kafane\" and created_time>1386024490 LIMIT 48',"
			+ "'users' : 'SELECT uid, name FROM user WHERE uid IN (SELECT actor_id FROM #posts)',}";
	private static final int CAMERA_REQUEST = 1888;
	private static final int SELECT_PICTURE_REQUEST = 1231;
	private static final int LOAD_ALBUMS_REQUEST = 1234;
	private static final String FB_ALL_ALBUMS_REQ = "406074909466689/photos";
	private UiLifecycleHelper uiHelper;
	private ProgressDialog loadingDialog;
	private LoginButton login;
	private GridView galleryGridView;
	private GalleryGridAdapter galleryGridAdapter;
	private Uri capturedImageUri;

	private int currentRequestForLogin = 0;

	private ArrayList<FacebookImage> sveSlikeList;
	private ArrayList<FacebookImage> korisnickeSlikeList;

	private Session.StatusCallback callback = new Session.StatusCallback() {
		@Override
		public void call(Session session, SessionState state,
				Exception exception) {
			onSessionStateChange(session, state, exception);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_galerija);
		galleryGridView = (GridView) findViewById(R.id.gridview);

		loadingDialog = new ProgressDialog(this);
		sveSlikeList = new ArrayList<FacebookImage>();
		korisnickeSlikeList = new ArrayList<FacebookImage>();
		setActionBar();
		try {
			PackageInfo info = getPackageManager().getPackageInfo(
					"com.nt.najboljekafane", PackageManager.GET_SIGNATURES);
			for (Signature signature : info.signatures) {
				MessageDigest md = MessageDigest.getInstance("SHA");
				md.update(signature.toByteArray());
				Log.w("KeyHash:",
						Base64.encodeToString(md.digest(), Base64.DEFAULT));
			}
		} catch (NameNotFoundException e) {
		} catch (NoSuchAlgorithmException e) {
		}

		uiHelper = new UiLifecycleHelper(this, callback);
		uiHelper.onCreate(savedInstanceState);

		login = (LoginButton) findViewById(R.id.authButton);
		login.setReadPermissions(Arrays.asList("basic_info"));
		login.setVisibility(View.INVISIBLE);
		loadingDialog.setMessage(getString(R.string.ucitavanje_galerije));
		loadingDialog.show();
		loadAppGallery();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.galerija_menu, menu);

		MenuItem addPhoto = menu.findItem(R.id.menu_add);
		MenuItem takePhoto = menu.findItem(R.id.menu_camera);

		addPhoto.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				if (isSessionStatusOpen()) {
					pickPhotoForUpload();
				} else {
					handleLoginForRequest(SELECT_PICTURE_REQUEST);
				}
				return true;
			}
		});

		takePhoto.setOnMenuItemClickListener(new OnMenuItemClickListener() {

			@Override
			public boolean onMenuItemClick(MenuItem item) {
				if (isSessionStatusOpen()) {
					takePhotoForUpload();
				} else
					handleLoginForRequest(CAMERA_REQUEST);
				return true;
			}
		});

		return true;
	}

	@Override
	public void onResume() {
		super.onResume();
		uiHelper.onResume();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		uiHelper.onActivityResult(requestCode, resultCode, data);
		if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
			handleImageUri(capturedImageUri);
		} else if (requestCode == SELECT_PICTURE_REQUEST
				&& resultCode == RESULT_OK) {
			Uri selectedImageUri = data.getData();
			handleImageUri(selectedImageUri);
		}
	}

	private void handleImageUri(Uri imageUri) {
		if (imageUri != null) {
			int targetW;
			int targetH;
			
			BitmapFactory.Options bmOptions = new BitmapFactory.Options();
			bmOptions.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(getPath(imageUri), bmOptions);
			int photoW = bmOptions.outWidth;
			int photoH = bmOptions.outHeight;

			if (photoW > photoH) {
				// LANDSCAPE MODE
				targetW = 2048;
				targetH = 1536;
			} else {
				targetW = 1536;
				targetH = 2048;
			}
			int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

			bmOptions.inJustDecodeBounds = false;
			bmOptions.inSampleSize = scaleFactor;
			bmOptions.inPurgeable = true;

			Bitmap bitmap = BitmapFactory.decodeFile(getPath(imageUri),
					bmOptions);
			int orientation = BitmapUtil.getImageExifOrientation(this, getPath(imageUri));
			bitmap = BitmapUtil.rotateBitmap(bitmap, orientation);
			if (bitmap != null)
				uploadPhoto(bitmap);
			else {
				// BugSenseHandler.sendException(new
				// Exception("Bitmap is null!"));
				// BugSenseHandler.flush(GalerijaActivity.this);
				Toast.makeText(this,
						getString(R.string.ucitavanje_slike_greska),
						Toast.LENGTH_SHORT).show();
			}
		} else {
			Toast.makeText(this, getString(R.string.ucitavanje_slike_greska),
					Toast.LENGTH_SHORT).show();
		}
	}

	public String getPath(Uri uri) {
		try {
			String[] proj = { MediaStore.Images.Media.DATA };
			Cursor cursor = getContentResolver().query(uri, proj, null, null,
					null);
			int columnIndex = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			return cursor.getString(columnIndex);
		} catch (Exception e) {
			return uri.getPath();
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		uiHelper.onPause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		uiHelper.onDestroy();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		uiHelper.onSaveInstanceState(outState);
	}

	private void setActionBar() {
		getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		// TODO Move color to resources
		getSupportActionBar().setBackgroundDrawable(
				new ColorDrawable(Color.parseColor("#b11944")));
		getSupportActionBar().setStackedBackgroundDrawable(
				new ColorDrawable(Color.parseColor("#b11944")));

		ActionBar.Tab tab1 = getSupportActionBar().newTab();
		tab1.setText(getString(R.string.sve_slike));
		tab1.setTabListener(this);
		getSupportActionBar().addTab(tab1);

		ActionBar.Tab tab2 = getSupportActionBar().newTab();
		tab2.setText(getString(R.string.vase_slike));
		tab2.setTabListener(this);
		getSupportActionBar().addTab(tab2);
	}

	private void onSessionStateChange(Session session, SessionState state,
			Exception exception) {
		if (exception != null) {
			BugSenseHandler.sendException(exception);
			BugSenseHandler.flush(this);
		}

		if (state.isOpened()) {
			if (hasAllPermissions(session)) {
				if (currentRequestForLogin == CAMERA_REQUEST) {
					takePhotoForUpload();
					currentRequestForLogin = 0;
				} else if (currentRequestForLogin == SELECT_PICTURE_REQUEST) {
					pickPhotoForUpload();
					currentRequestForLogin = 0;
				} else if (currentRequestForLogin == LOAD_ALBUMS_REQUEST) {
					loadUserGallery();
					currentRequestForLogin = 0;
				}
			} else {
				// FIXME Obrisati BugSense liniju da ne bi dobijali Exception,
				// ovo je samo za debugging
				BugSenseHandler.sendException(new Exception(
						"Publish permissions don't exist."));
				NewPermissionsRequest permReq = new NewPermissionsRequest(
						GalerijaActivity.this, Arrays.asList("publish_stream",
								"publish_actions", "photo_upload"));
				session.requestNewPublishPermissions(permReq);
			}

		} else if (state.isClosed()) {
			Log.e("FB_LOGIN_CALLBACK", "SESSION NOT OPEN!!!");
		}
	}

	private boolean hasAllPermissions(Session session) {
		HashMap<String, Integer> permissionMap = new HashMap<String, Integer>();
		List<String> perms = session.getPermissions();
		for (String perm : perms) {
			permissionMap.put(perm, 100);
		}
		return (permissionMap.get("publish_stream") != null
				&& permissionMap.get("publish_actions") != null && permissionMap
					.get("photo_upload") != null);
	}

	private void loadUserGallery() {
		loadingDialog.setMessage(getString(R.string.ucitavanje_galerije));
		loadingDialog.show();
		Bundle params = new Bundle();
		params.putString("q", FQL_MULTIQUERY);

		Session session = Session.getActiveSession();
		Request request = new Request(session, "/fql", params, HttpMethod.GET,
				new Request.Callback() {
					public void onCompleted(Response response) {
						if (response.getError() == null) {
							GraphObject respObject = response.getGraphObject();
							JSONObject appPosts = respObject
									.getInnerJSONObject();

							korisnickeSlikeList.clear();
							try {
								HashMap<String, String> uidNameMap = new HashMap<String, String>();
								JSONArray data = appPosts.getJSONArray("data");
								JSONObject userResult = data.getJSONObject(1);
								JSONArray userSet = userResult
										.getJSONArray("fql_result_set");
								for (int i = 0; i < userSet.length(); i++) {
									JSONObject userJson = userSet
											.getJSONObject(i);
									String uid = userJson.getString("uid");
									String name = userJson.getString("name");
									uidNameMap.put(uid, name);
								}

								JSONObject postResult = data.getJSONObject(0);
								JSONArray posts = postResult
										.getJSONArray("fql_result_set");
								for (int i = 0; i < posts.length(); i++) {
									JSONObject post = posts.getJSONObject(i);
									FacebookImage fbImg = FacebookImage
											.fromPost(post);
									fbImg.name = uidNameMap.get(post
											.getString("actor_id"));

									korisnickeSlikeList.add(fbImg);
								}
								galleryGridAdapter = new GalleryGridAdapter(
										GalerijaActivity.this,
										korisnickeSlikeList,
										GalleryGridAdapter.FOTKE_KORISNIKA);
								galleryGridView.setAdapter(galleryGridAdapter);
								if (loadingDialog != null
										&& loadingDialog.isShowing())
									loadingDialog.dismiss();
							} catch (JSONException e) {
								Toast.makeText(
										GalerijaActivity.this,
										getString(R.string.ucitavanje_galerije_greska),
										Toast.LENGTH_SHORT).show();
								BugSenseHandler.sendException(e);
							}
						} else {
							Toast.makeText(
									GalerijaActivity.this,
									getString(R.string.ucitavanje_galerije_greska),
									Toast.LENGTH_SHORT).show();
							BugSenseHandler
									.sendExceptionMessage("FBLoadGallery",
											response.getError()
													.getErrorMessage(),
											new Exception());
							BugSenseHandler.flush(GalerijaActivity.this);
						}

						loadingDialog.dismiss();
					}
				});
		Request.executeBatchAsync(request);
	}

	private void loadAppGallery() {
		Request galleryRequest = Request.newGraphPathRequest(null,
				FB_ALL_ALBUMS_REQ, new Callback() {

					@Override
					public void onCompleted(Response response) {
						if (response.getError() == null) {
							GraphObject respObject = response.getGraphObject();
							JSONObject galleryJson = respObject
									.getInnerJSONObject();
							try {
								parseGalleryResult(galleryJson);
							} catch (JSONException e) {
								Toast.makeText(
										GalerijaActivity.this,
										getString(R.string.ucitavanje_galerije_greska),
										Toast.LENGTH_SHORT).show();
								BugSenseHandler.sendException(e);
							}
						} else {
							Toast.makeText(
									GalerijaActivity.this,
									getString(R.string.ucitavanje_galerije_greska),
									Toast.LENGTH_SHORT).show();
							BugSenseHandler
									.sendExceptionMessage("FBLoadGallery",
											response.getError()
													.getErrorMessage(),
											new Exception());
							BugSenseHandler.flush(GalerijaActivity.this);
						}
						if (loadingDialog != null && loadingDialog.isShowing())
							loadingDialog.dismiss();
					}
				});
		Bundle bundle = galleryRequest.getParameters();
		bundle.putString("type", "uploaded");
		bundle.putString("fields", "id,picture,source,images,link,likes");
		bundle.putString("limit", "48");
		galleryRequest.setParameters(bundle);
		galleryRequest.executeAsync();
	}

	private void uploadPhoto(final Bitmap image) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		View dialog = getLayoutInflater().inflate(
				R.layout.dialog_facebook_upload, null);
		builder.setView(dialog);
		final EditText comment = (EditText) dialog
				.findViewById(R.id.comment_edittext);
		builder.setCancelable(false)
				.setPositiveButton(getString(R.string.btn_posalji_sliku),
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								InputMethodManager imm = (InputMethodManager)getSystemService(
									      Context.INPUT_METHOD_SERVICE);
									imm.hideSoftInputFromWindow(comment.getWindowToken(), 0);
								uploadPhotoToFacebook(image, comment.getText()
										.toString().trim());
							}
						}).show();
		;
	}

	private void uploadPhotoToFacebook(Bitmap image, String comment) {
		loadingDialog.setMessage(getString(R.string.slanje_slike));
		loadingDialog.show();
		Bundle parameters = new Bundle();
		parameters.putParcelable("picture", image);
		parameters.putString("message", comment);
		Request request = new Request(Session.getActiveSession(),
				FB_ALL_ALBUMS_REQ, parameters, HttpMethod.POST,
				new Request.Callback() {
					@Override
					public void onCompleted(Response response) {
						if (response.getError() != null) {
							Toast.makeText(GalerijaActivity.this,
									getString(R.string.slanje_slike_greska),
									Toast.LENGTH_SHORT).show();
							BugSenseHandler
									.sendExceptionMessage("FBUploadImage",
											response.getError()
													.getErrorMessage(),
											new Exception());
							BugSenseHandler.flush(GalerijaActivity.this);
						} else {
							Toast.makeText(GalerijaActivity.this,
									getString(R.string.slanje_slike_uspesno),
									Toast.LENGTH_SHORT).show();
							if (tabSelected == 1)
								loadUserGallery();
						}
						loadingDialog.dismiss();
					}
				});
		request.executeAsync();
	}

	private void parseGalleryResult(JSONObject galleryJson)
			throws JSONException {
		sveSlikeList.clear();
		JSONArray imageArrayJson = galleryJson.optJSONArray("data");
		for (int i = 0; i < imageArrayJson.length(); i++) {
			FacebookImage fbImg = FacebookImage.fromJson(imageArrayJson
					.getJSONObject(i));
			sveSlikeList.add(fbImg);
		}

		galleryGridAdapter = new GalleryGridAdapter(this, sveSlikeList,
				GalleryGridAdapter.NASE_FOTKE);

		galleryGridView.setAdapter(galleryGridAdapter);
		galleryGridView.setOnItemClickListener(this);
	}

	public boolean isSessionStatusOpen() {
		Session session = Session.getActiveSession();
		if (session.isOpened())
			return true;
		return false;
	}

	private void pickPhotoForUpload() {
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(Intent.createChooser(intent, "Izaberite sliku"),
				SELECT_PICTURE_REQUEST);
	}

	private void takePhotoForUpload() {
		Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		ContentValues values = new ContentValues();
		values.put(MediaStore.Images.Media.TITLE, "NajboljeKafane");
		capturedImageUri = getContentResolver().insert(
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
		cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, capturedImageUri);
		startActivityForResult(cameraIntent, CAMERA_REQUEST);
	}

	private void handleLoginForRequest(int requestCode) {
		currentRequestForLogin = requestCode;
		login.performClick();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent i = new Intent(this, FullScreenViewActivity.class);
		if (getSupportActionBar().getSelectedNavigationIndex() == TAB_SVE_SLIKE) {
			i.putExtra(EXTRA_FACEBOOK_IMAGE_LIST, sveSlikeList);
		} else {
			i.putExtra(EXTRA_FACEBOOK_IMAGE_LIST, korisnickeSlikeList);
		}
		i.putExtra(EXTRA_SELECTED_POSITION, position);
		startActivity(i);
	}

	int tabSelected;

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		tabSelected = tab.getPosition();
		if (tab.getPosition() == TAB_SVE_SLIKE) {
			galleryGridAdapter = new GalleryGridAdapter(GalerijaActivity.this,
					sveSlikeList, GalleryGridAdapter.NASE_FOTKE);
			galleryGridView.setAdapter(galleryGridAdapter);
		} else {
			if (korisnickeSlikeList == null || korisnickeSlikeList.size() == 0) {
				if (isSessionStatusOpen()) {
					loadUserGallery();
				} else {
					handleLoginForRequest(LOAD_ALBUMS_REQUEST);
				}

			} else {
				galleryGridAdapter = new GalleryGridAdapter(
						GalerijaActivity.this, korisnickeSlikeList,
						GalleryGridAdapter.FOTKE_KORISNIKA);
				galleryGridView.setAdapter(galleryGridAdapter);
			}

		}

	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub

	}

}
