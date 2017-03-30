package dunavnet.com.mojns.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import dunavnet.com.mojns.R;
import dunavnet.com.mojns.util.SettingsConstants;

public class ProfileActivity extends Activity {

	SharedPreferences prefs;
	public EditText profileName, profileLastName, profileEmail, profilePhone;
	public ImageView logoutImage;
	public String oldAccount;

		
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

		prefs = getSharedPreferences(SettingsConstants.PARTICIPATORY_SENSING_PREFERENCES, Context.MODE_PRIVATE);
        
        Button buttonEditProfile = (Button) findViewById(R.id.buttonEditProfile);
        Button buttonCancelProfile = (Button) findViewById(R.id.buttonCancelProfile);
        profileName = (EditText)findViewById(R.id.profileName);
        profileLastName = (EditText)findViewById(R.id.profileLastName);
        profileEmail = (EditText)findViewById(R.id.profileEmail);
        profilePhone = (EditText)findViewById(R.id.profilePhone);

		profileEmail.setKeyListener(null);

		logoutImage = (ImageView) findViewById(R.id.logout);

		oldAccount = prefs.getString("old", "");
		String email = prefs.getString("email", "");

		if(oldAccount.equals("") || !oldAccount.equals(email)){
			prefs.edit().putString("name", "").commit();
			prefs.edit().putString("surname", "").commit();
			prefs.edit().putString("phone", "").commit();
		}

		profileName.setText(prefs.getString("name", ""));
		profileLastName.setText(prefs.getString("surname", ""));
		profileEmail.setText(prefs.getString("email", ""));
		profilePhone.setText(prefs.getString("phone", ""));
        
        buttonEditProfile.setOnClickListener(new View.OnClickListener() {
			private Context context;

			@Override
			public void onClick(View v) {
				String name = profileName.getText().toString();
				String lastName = profileLastName.getText().toString();
				String email = profileEmail.getText().toString();
				String telefon = profilePhone.getText().toString();

				if (name.equals("") || lastName.equals("") || email.equals("")) {
					Toast.makeText(ProfileActivity.this, getString(R.string.emptyfield), Toast.LENGTH_LONG).show();
				} else {
					prefs.edit().putString("name", name).commit();
					prefs.edit().putString("surname", lastName).commit();
					prefs.edit().putString("email", email).commit();
					prefs.edit().putString("phone", telefon).commit();
					prefs.edit().putString("old", email).commit();

					finish();
				}
			}

		});
        
        buttonCancelProfile.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}

		});

		logoutImage.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//prefs.edit().putString("access_token", "").commit();
				prefs.edit().putInt("signed", 0).commit();
				Intent intent = new Intent(getApplicationContext(), LoginActivityNew.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				ActivityCompat.finishAffinity(ProfileActivity.this);
			}
		});
       
	}
    		
}

