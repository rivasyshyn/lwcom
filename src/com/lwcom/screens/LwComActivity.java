package com.lwcom.screens;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.lwcom.R;
import com.lwcom.application.LwComApp;
import com.lwcom.model.ClockSettings;
import com.lwcom.model.SettingStorage;

public class LwComActivity extends Activity implements OnClickListener {
	/** Called when the activity is first created. */

	private ClockSettings mSettings;
	private static final int PICK_PHOTO = 0x13;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);
		ClockSettings sets = (ClockSettings) SettingStorage.restore(
				LwComApp.SETTINGS, this);
		if (sets == null) {
			mSettings = new ClockSettings();
			mSettings.init();
		} else {
			mSettings = sets;
		}
		init(mSettings);

	}

	private void init(ClockSettings sett) {

		CheckedTextView sec = (CheckedTextView) findViewById(R.id.line_seconds);
		sec.setOnClickListener(this);
		sec.setChecked(sett.isSeconds());

		CheckedTextView min = (CheckedTextView) findViewById(R.id.line_minutes);
		min.setOnClickListener(this);
		min.setChecked(sett.isMinutes());

		CheckedTextView hou = (CheckedTextView) findViewById(R.id.line_hours);
		hou.setOnClickListener(this);
		hou.setChecked(sett.isHours());

		CheckedTextView mon = (CheckedTextView) findViewById(R.id.line_months);
		mon.setOnClickListener(this);
		mon.setChecked(sett.isMonths());

		CheckedTextView lin = (CheckedTextView) findViewById(R.id.line_lines);
		lin.setOnClickListener(this);
		lin.setChecked(sett.isLines());

		CheckedTextView fil = (CheckedTextView) findViewById(R.id.line_fill);
		fil.setOnClickListener(this);
		fil.setChecked(sett.isFill());

		CheckedTextView sha = (CheckedTextView) findViewById(R.id.line_shadow);
		sha.setOnClickListener(this);
		sha.setChecked(sett.isShadow());

		TextView v = (TextView) findViewById(R.id.position_val);
		v.setText(String.valueOf(sett.getPosition()));

		v = (TextView) findViewById(R.id.background_val);
		v.setText((sett.getPath() == null) ? getString(R.string.sub_color) : getString(R.string.sub_image));
		v.setTextColor((sett.getPath() == null) ? Color.rgb(mSettings.getRed(),
				mSettings.getGreen(), mSettings.getBlue()) : 0x5555ff);

		v = (TextView) findViewById(R.id.text_val);
		v.setTextColor(Color.rgb(mSettings.getRedT(), mSettings.getGreenT(),
				mSettings.getBlueT()));

		View pos = findViewById(R.id.line_position);
		pos.setOnClickListener(this);
		View path = findViewById(R.id.line_path);
		path.setOnClickListener(this);
		View text = findViewById(R.id.line_text);
		text.setOnClickListener(this);
	}

	public void onClick(View v) {
		if (v instanceof CheckedTextView) {
			((CheckedTextView) v).toggle();

			switch (v.getId()) {
			case R.id.line_seconds:
				mSettings.setSeconds(((CheckedTextView) v).isChecked());
				break;

			case R.id.line_minutes:
				mSettings.setMinutes(((CheckedTextView) v).isChecked());
				break;

			case R.id.line_hours:
				mSettings.setHours(((CheckedTextView) v).isChecked());
				break;

			case R.id.line_months:
				mSettings.setMonths(((CheckedTextView) v).isChecked());
				break;

			case R.id.line_lines:
				mSettings.setLines(((CheckedTextView) v).isChecked());
				break;

			case R.id.line_fill:
				mSettings.setFill(((CheckedTextView) v).isChecked());
				break;

			case R.id.line_shadow:
				mSettings.setShadow(((CheckedTextView) v).isChecked());
				break;

			default:
				break;
			}
		} else {

			switch (v.getId()) {
			case R.id.line_path:
				// Toast.makeText(getApplicationContext(),
				// "not implemented yet",
				// Toast.LENGTH_SHORT).show();
				showBackDialog();
				break;

			case R.id.line_position:
				showPositionDialog();
				break;

			case R.id.button1:
				mSettings.setPosition(mSeekBar.getProgress());
				mDialog.dismiss();
				init(mSettings);
				break;

			case R.id.button2:
				mDialog.dismiss();
				break;

			case R.id.line_color:
				mDialog.dismiss();
				showColorDialog(false);
				break;

			case R.id.line_image:
				// Toast.makeText(getApplicationContext(),
				// "not implemented yet",
				// Toast.LENGTH_SHORT).show();
				mDialog.dismiss();
				pickImage();
				break;

			case R.id.viewS:
				mDialog.dismiss();
				if (!mDt)
					mSettings.setPath(null);
				init(mSettings);
				break;

			case R.id.line_text:
				showColorDialog(true);
				break;

			default:
				break;
			}

		}
	}

	private Dialog mDialog;
	private SeekBar mSeekBar;
	private static boolean mDt;

	private void showColorDialog(boolean tr) {
		mDt = tr;

		final int a, r, g, b;

		if (tr) {
			a = mSettings.getTr();
			r = mSettings.getRedT();
			g = mSettings.getGreenT();
			b = mSettings.getBlueT();
		} else {
			a = 255;
			r = mSettings.getRed();
			g = mSettings.getGreen();
			b = mSettings.getBlue();
		}

		mDialog = new Dialog(this);
		mDialog.setTitle("Color Picker");
		View v = getLayoutInflater().inflate(R.layout.color_picker, null);
		mDialog.addContentView(v, new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT));
		mDialog.show();
		// final Color c = new Color();
		// c.rgb(mSettings.getRed(), mSettings.getGreen(), mSettings.getBlue());
		final View sample = v.findViewById(R.id.viewS);
		sample.setBackgroundColor(Color.argb(a, r, g, b));
		sample.setOnClickListener(this);
		final OnSeekBarChangeListener listener = new OnSeekBarChangeListener() {

			public void onStopTrackingTouch(SeekBar paramSeekBar) {
				// TODO Auto-generated method stub

			}

			public void onStartTrackingTouch(SeekBar paramSeekBar) {
				// TODO Auto-generated method stub

			}

			public void onProgressChanged(SeekBar paramSeekBar, int paramInt,
					boolean paramBoolean) {
				switch (paramSeekBar.getId()) {
				case R.id.seekBarR:
					if (mDt) {
						mSettings.setRedT(paramInt);
					} else {
						mSettings.setRed(paramInt);
					}
					break;

				case R.id.seekBarG:
					if (mDt) {
						mSettings.setGreenT(paramInt);
					} else {
						mSettings.setGreen(paramInt);
					}
					break;

				case R.id.seekBarB:
					if (mDt) {
						mSettings.setBlueT(paramInt);
					} else {
						mSettings.setBlue(paramInt);
					}
					break;

				case R.id.seekBarT:
					if (mDt) {
						mSettings.setTr(paramInt);
					}
					break;

				default:
					break;
				}
				if (mDt) {
					sample.setBackgroundColor(Color.argb(mSettings.getTr(),
							mSettings.getRedT(), mSettings.getGreenT(),
							mSettings.getBlueT()));
				} else {
					sample.setBackgroundColor(Color.rgb(mSettings.getRed(),
							mSettings.getGreen(), mSettings.getBlue()));
				}
			}
		};
		SeekBar sb = ((SeekBar) v.findViewById(R.id.seekBarR));
		sb.setOnSeekBarChangeListener(listener);
		sb.setProgress(r);
		sb = ((SeekBar) v.findViewById(R.id.seekBarG));
		sb.setOnSeekBarChangeListener(listener);
		sb.setProgress(g);
		sb = ((SeekBar) v.findViewById(R.id.seekBarB));
		sb.setOnSeekBarChangeListener(listener);
		sb.setProgress(b);
		sb = ((SeekBar) v.findViewById(R.id.seekBarT));
		sb.setOnSeekBarChangeListener(listener);
		sb.setProgress(a);
		sb.setVisibility((mDt) ? View.VISIBLE : View.GONE);

		v.findViewById(R.id.textView4).setVisibility(
				(mDt) ? View.VISIBLE : View.GONE);

	}

	private void showPositionDialog() {
		mDialog = new Dialog(this);
		View v = getLayoutInflater().inflate(R.layout.main, null);
		mDialog.addContentView(v, new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT));
		mDialog.setCancelable(true);
		mDialog.setTitle(getString(R.string.msg_position));
		mDialog.show();
		mSeekBar = (SeekBar) v.findViewById(R.id.pos_seek_id);
		mSeekBar.setProgress(mSettings.getPosition());
		((TextView) v.findViewById(R.id.percent)).setText(mSettings
				.getPosition() + "%");
		mSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			public void onStopTrackingTouch(SeekBar paramSeekBar) {
				// TODO Auto-generated method stub

			}

			public void onStartTrackingTouch(SeekBar paramSeekBar) {
				// TODO Auto-generated method stub

			}

			public void onProgressChanged(SeekBar paramSeekBar, int paramInt,
					boolean paramBoolean) {
				// TODO Auto-generated method stub
				((TextView) mDialog.findViewById(R.id.percent))
						.setText(paramInt + "%");
			}
		});
		Button bb = (Button) v.findViewById(R.id.button1);
		bb.setOnClickListener(this);
		bb = (Button) v.findViewById(R.id.button2);
		bb.setOnClickListener(this);

	}

	private void pickImage() {
		Intent picker = new Intent(Intent.ACTION_PICK);
		picker.setType("image/*");
		startActivityForResult(picker, PICK_PHOTO);
	}

	private void showBackDialog() {
		mDialog = new Dialog(this);
		mDialog.setTitle(getString(R.string.msg_background));
		View v = getLayoutInflater().inflate(R.layout.back_dialog, null);
		View bb = v.findViewById(R.id.line_color);
		bb.setOnClickListener(this);
		bb = v.findViewById(R.id.line_image);
		bb.setOnClickListener(this);
		mDialog.addContentView(v, new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT));
		mDialog.show();
	}

	@Override
	public void onBackPressed() {
		SettingStorage.store(mSettings, LwComApp.SETTINGS, this);
		super.onBackPressed();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == PICK_PHOTO) {
			if (data == null) {
			} else {
				Uri photo = data.getData();
				mSettings.setPath((photo == null) ? null : photo.toString());
				init(mSettings);
			}
		}
	}
}