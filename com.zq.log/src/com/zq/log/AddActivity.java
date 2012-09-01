package com.zq.log;

import java.util.Calendar;
import java.util.HashMap;

import com.zq.db.LoadUtil;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

@SuppressLint("HandlerLeak")
public class AddActivity extends Activity {
	private EditText showDate = null;
	private Button pickDate = null;
	private EditText showTime = null;
	private Button pickTime = null;
	private EditText Title = null;
	private EditText Content = null;

	private static final int SHOW_DATAPICK = 0;
	private static final int DATE_DIALOG_ID = 1;
	private static final int SHOW_TIMEPICK = 2;
	private static final int TIME_DIALOG_ID = 3;

	private int mYear;
	private int mMonth;
	private int mDay;
	private int mHour;
	private int mMinute;

	private Spinner spinner1;
	private ArrayAdapter<String> spada1;
	private String[] quyu1 = { "约会", "会议", "上课", "活动", "其他" };

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add);

		Title = (EditText) findViewById(R.id.TitleAdd);
		Content = (EditText) findViewById(R.id.editText2);
		Button conmit = (Button) findViewById(R.id.button2);
		Button back = (Button) findViewById(R.id.button1);
		spinner1 = (Spinner) findViewById(R.id.spinner1);
		spada1 = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, quyu1);
		// 设置下拉列表风格
		spada1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// 将adapter添加到spinner中
		spinner1.setAdapter(spada1);
		spinner1.setSelection(0, false);
		// 添加Spinner事件监听
		spinner1.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				// 设置显示当前选择的项
				arg0.setVisibility(View.VISIBLE);
			}

			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});

		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				MainActivity.iniTLisit();
				MainActivity.listAdapter.notifyDataSetChanged();
				finish();
			}
		});
		conmit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!Title.getText().toString().equals("")) {
					StringToInsert();
					finish();
				} else {
					ErrorDailog().show();
				}
			}
		});

		initializeViews();

		final Calendar c = Calendar.getInstance();
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);

		mHour = c.get(Calendar.HOUR_OF_DAY);
		mMinute = c.get(Calendar.MINUTE);

		setDateTime();
		setTimeOfDay();

	}

	private void initializeViews() {
		showDate = (EditText) findViewById(R.id.editText1);
		pickDate = (Button) findViewById(R.id.button3);
		showTime = (EditText) findViewById(R.id.editText3);
		pickTime = (Button) findViewById(R.id.button4);
		pickDate.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Message msg = new Message();
				if (pickDate.equals((Button) v)) {
					msg.what = AddActivity.SHOW_DATAPICK;
				}
				AddActivity.this.dateandtimeHandler.sendMessage(msg);
			}
		});

		pickTime.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Message msg = new Message();
				if (pickTime.equals((Button) v)) {
					msg.what = AddActivity.SHOW_TIMEPICK;
				}
				AddActivity.this.dateandtimeHandler.sendMessage(msg);
			}
		});
	}

	private void setDateTime() {
		final Calendar c = Calendar.getInstance();

		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);
		updateDateDisplay();
	}

	private void updateDateDisplay() {
		showDate.setText(new StringBuilder().append(mYear).append("-")
				.append((mMonth + 1) < 10 ? "0" + (mMonth + 1) : (mMonth + 1))
				.append("-").append((mDay < 10) ? "0" + mDay : mDay));
	}

	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			mYear = year;
			mMonth = monthOfYear;
			mDay = dayOfMonth;
			updateDateDisplay();
		}
	};

	private void setTimeOfDay() {
		final Calendar c = Calendar.getInstance();
		mHour = c.get(Calendar.HOUR_OF_DAY);
		mMinute = c.get(Calendar.MINUTE);
		updateTimeDisplay();
	}

	private void updateTimeDisplay() {
		showTime.setText(new StringBuilder().append(mHour).append(":")
				.append((mMinute < 10) ? "0" + mMinute : mMinute));
	}

	private TimePickerDialog.OnTimeSetListener mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {

		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			mHour = hourOfDay;
			mMinute = minute;
			updateTimeDisplay();
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_DIALOG_ID:
			return new DatePickerDialog(this, mDateSetListener, mYear, mMonth,
					mDay);
		case TIME_DIALOG_ID:
			return new TimePickerDialog(this, mTimeSetListener, mHour, mMinute,
					true);
		}
		return null;
	}

	@Override
	protected void onPrepareDialog(int id, Dialog dialog) {
		switch (id) {
		case DATE_DIALOG_ID:
			((DatePickerDialog) dialog).updateDate(mYear, mMonth, mDay);
			break;
		case TIME_DIALOG_ID:
			((TimePickerDialog) dialog).updateTime(mHour, mMinute);
			break;
		default:
			break;
		}
	}

	Handler dateandtimeHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case AddActivity.SHOW_DATAPICK:
				showDialog(DATE_DIALOG_ID);
				break;
			case AddActivity.SHOW_TIMEPICK:
				showDialog(TIME_DIALOG_ID);
				break;
			default:
				break;
			}
		}
	};

	public AlertDialog ErrorDailog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(AddActivity.this);
		builder.setTitle("提示").setIcon(R.drawable.ic_launcher)
				.setCancelable(false)
				// 不响应back按钮
				.setMessage("行程标题不能为空！")
				.setNeutralButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				});
		// 对话框显示内容
		// 创建Dialog对象
		AlertDialog dlg = builder.create();
		return dlg;
	}

	public void StringToInsert() {
		String sql = "insert into schedule (Ttitle,Ttype,Ttext,Tdate,Ttime)values('"
				+ Title.getText().toString().trim()
				+ "','"
				+ spinner1.getSelectedItem().toString().trim()
				+ "','"
				+ Content.getText().toString().trim()
				+ "','"
				+ showDate.getText().toString().trim()
				+ "','"
				+ showTime.getText().toString().trim() + "')";
		LoadUtil.insert(sql);

		HashMap<String, String> map = new HashMap<String, String>();
		map.put("title", Title.getText().toString().trim());
		map.put("type", spinner1.getSelectedItem().toString().trim());
		map.put("date", showDate.getText().toString().trim());
		map.put("time", showTime.getText().toString().trim());
		MainActivity.list.add(map);
		MainActivity.listAdapter.notifyDataSetChanged();
	}
}