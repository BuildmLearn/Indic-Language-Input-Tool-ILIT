package org.buildmlearn.indickeyboard.settings;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;

import org.buildmlearn.indickeyboard.R;

import java.util.List;

import io.fabric.sdk.android.Fabric;

public class SettingsActivity extends PreferenceActivity {
	public static boolean isTablet;
	private boolean isDefault = false;
	private boolean isEnabled = false;
	private EditText previewEditText;
	private TextView instructionTextView;
	private RadioGroup radioGroup;
	private SharedPreferences prefs;
	private SharedPreferences.Editor editor;
	private boolean inEnglish = false;
	private ScrollView layout;
	private Button rateus;
	private TextView doYouLikeTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Fabric.with(this, new Crashlytics());
		if (getIntent().getExtras() != null) {
			inEnglish = getIntent().getExtras().getBoolean("inEnglish", false);
		}

		layout = (ScrollView) getLayoutInflater().inflate(
				R.layout.settings_layout, null);
		previewEditText = (EditText) layout
				.findViewById(R.id.preview_edit_text);
		instructionTextView = (TextView) layout.findViewById(R.id.instruction);

		rateus = (Button) layout.findViewById(R.id.rateus);
		doYouLikeTextView = (TextView) layout.findViewById(R.id.likeus);





		String instruction = getStringResourceByName("settings_instruction");
		instructionTextView.setText(instruction);

		String doyoulikeustext=getStringResourceByName("do_you_like");
		String rateustext=getStringResourceByName("rate_us");

		doYouLikeTextView.setText(doyoulikeustext);
		rateus.setText(rateustext);

		setContentView(layout);

		overridePendingTransition(R.anim.activity_open_translate,
				R.anim.activity_close_scale);

		checkKeyboardStatus();

		prefs = UserSettings.getPrefs();

		String key = getString(R.string.tablet_layout_setting_key);
		Boolean isBig = prefs.getBoolean(key, false);

		rateus.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent rate = new Intent(Intent.ACTION_VIEW);
				//Will add the link
				rate.setData(Uri.parse("https://play.google.com/store/apps/") );
				startActivity(rate);
			}
		});



		if (isDefault && isEnabled) {
			isTablet = isTablet(this);
			previewEditText.requestFocus();
		} else {
			startMainActivity();
		}
//		getActionBar().setTitle(getStringResourceByName("title"));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_activity_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
		case R.id.action_language:
			if (!inEnglish) {
				inEnglish = true;
				String languageName = getResources().getString(
						R.string.language_name);
				int resId = getResources().getIdentifier(
						languageName + "_" + "menu_language", "string",
						getPackageName());
				String title = getResources().getString(resId);
				item.setTitle(title);
			} else {
				inEnglish = false;
				String title = getResources().getString(R.string.menu_language);
				item.setTitle(title);
			}
			setCorrectText();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public String getStringResourceByName(String aString) {
		String packageName = getPackageName();
		String languageName = getResources().getString(R.string.language_name);
		int resId = getResources().getIdentifier("hindi" + "_" + aString,
				"string", packageName);
		if (inEnglish)
			resId = 0;
		if (resId == 0) {
			resId = getResources()
					.getIdentifier(aString, "string", packageName);
		}
		return getString(resId);
	}



	@Override
	public void onBackPressed() {
		moveTaskToBack(true);
	}

	@Override
	public void onResume() {
		super.onResume();
		checkKeyboardStatus();
		if (isDefault && isEnabled) {

		} else {
			startMainActivity();
		}
	}

	public void startMainActivity() {
		Intent intent = new Intent(this, MainActivity.class);
		intent.putExtra("inEnglish", inEnglish);
		startActivity(intent);
	}

	public void setCorrectText() {

		String smallRadioText = getStringResourceByName("settings_layout_small");
		String bigRadioText = getStringResourceByName("settings_layout_big");

		

		String instruction = getStringResourceByName("settings_instruction");
		instructionTextView.setText(instruction);

		String doyoulikeustext=getStringResourceByName("do_you_like");
		String rateustext=getStringResourceByName("rate_us");
		doYouLikeTextView.setText(doyoulikeustext);
		rateus.setText(rateustext);
	}

	@Override
	public void onPause() {
		super.onPause();
		overridePendingTransition(R.anim.activity_open_scale,
				R.anim.activity_close_translate);
	}

	public void checkKeyboardStatus() {
		InputMethodManager mgr = (InputMethodManager) this
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		List<InputMethodInfo> lim = mgr.getEnabledInputMethodList();
		isEnabled = false;
		isDefault = false;

		for (InputMethodInfo im : lim) {
			if (im.getPackageName().equals(getPackageName())) {
				isEnabled = true;
				final String currentImeId = Settings.Secure.getString(
						getContentResolver(),
						Settings.Secure.DEFAULT_INPUT_METHOD);

				if (im != null && im.getId().equals(currentImeId)) {
					isDefault = true;
				}
			}
		}
	}

	public static boolean isTablet(Context context) {
		return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
	}

	private void showPreview() {
		previewEditText.requestFocus();
		previewEditText.setText(null);

		InputMethodManager imm = (InputMethodManager) getApplicationContext()
				.getSystemService(Context.INPUT_METHOD_SERVICE);

		imm.showSoftInput(previewEditText, 0);
	}

}
