package com.zq.log;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Vector;

import com.zq.db.LoadUtil;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.TimePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.Spinner;
import android.widget.Toast;

@SuppressLint("HandlerLeak")
@SuppressWarnings("unused")
public class QueryActivity extends Activity {
	private ArrayAdapter<String> spada1;
	private EditText Title = null;
	private Spinner spinner1;
	private EditText showDate = null;
	private Button pickDate = null;
	private String[] quyu1 = { "约会", "会议", "上课", "活动", "其他", "" };

	private static final int SHOW_DATAPICK = 0;
	private static final int DATE_DIALOG_ID = 1;
	private static final int SHOW_TIMEPICK = 2;
	private static final int TIME_DIALOG_ID = 3;

	private int mYear;
	private int mMonth;
	private int mDay;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.query);
		Button back = (Button) findViewById(R.id.back3);
		Button conmit = (Button) findViewById(R.id.conmit);
		Title = (EditText) findViewById(R.id.editText1);
		pickDate = (Button) findViewById(R.id.button1);
		showDate = (EditText) findViewById(R.id.queryByDate2);

		spinner1 = (Spinner) findViewById(R.id.spinner1);
		spada1 = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, quyu1);
		// 设置下拉列表风格
		spada1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// 将adapter添加到spinner中
		spinner1.setAdapter(spada1);
		spinner1.setSelection(5, false);
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
				if (!isEmpty()) {
					if (!Title.getText().toString().equals("")) {
						queryByTitle(Title.getText().toString().trim());
					} else {
						if (!spinner1.getSelectedItem().toString().equals("")) {
							queryByType(spinner1.getSelectedItem().toString());
						} else {
							if (!showDate.getText().toString().equals("")) {
								queryByDate(showDate.getText().toString());
							}
						}
					}
					if (MainActivity.list.isEmpty()) {
						ErrorDailog().show();
					} else {
						finish();
					}
				} else {
					ErrorDialog2().show();
				}
			}
		});
		initializeViews();
		setDateTime();
	}

	private void setDateTime() {
		final Calendar c = Calendar.getInstance();

		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	public void queryByDate(String date) {
		String sql = "select * from schedule where Tdate = '" + date + "'";
		query(sql);
	}

	public void queryByTitle(String title) {
		String sql = "select * from schedule where Ttitle = '" + title + "'";
		query(sql);
	}

	public void queryByType(String type) {
		String sql = "select * from schedule where Ttype = '" + type + "'";
		query(sql);
	}

	public AlertDialog ErrorDailog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(
				QueryActivity.this);
		builder.setTitle("提示").setIcon(R.drawable.ic_launcher)
				.setCancelable(false)
				// 不响应back按钮
				.setMessage("为查询到符合条件日程！")
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

	public AlertDialog ErrorDialog2() {
		AlertDialog.Builder builder = new AlertDialog.Builder(
				QueryActivity.this);
		builder.setTitle("提示").setIcon(R.drawable.ic_launcher)
				.setCancelable(false).setMessage("请输入查询条件！")
				.setNeutralButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				});
		AlertDialog dlg = builder.create();
		return dlg;
	}

	public void query(String sql) {
		MainActivity.list.clear();
		Vector<Vector<String>> temp = LoadUtil.query(sql);
		for (int i = 0; i < temp.size(); i++) {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("title", temp.get(i).get(1));
			map.put("type", temp.get(i).get(2));
			map.put("date", temp.get(i).get(4));
			map.put("time", temp.get(i).get(5));
			MainActivity.list.add(map);
		}
		MainActivity.listAdapter.notifyDataSetChanged();
	}

	private void initializeViews() {
		pickDate.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Message msg = new Message();
				if (pickDate.equals((Button) v)) {
					msg.what = QueryActivity.SHOW_DATAPICK;
				}
				QueryActivity.this.dateandtimeHandler.sendMessage(msg);
			}
		});
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

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_DIALOG_ID:
			return new DatePickerDialog(this, mDateSetListener, mYear, mMonth,
					mDay);
		}
		return null;
	}

	@Override
	protected void onPrepareDialog(int id, Dialog dialog) {
		switch (id) {
		case DATE_DIALOG_ID:
			((DatePickerDialog) dialog).updateDate(mYear, mMonth, mDay);
			break;
		default:
			break;
		}
	}

	Handler dateandtimeHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case QueryActivity.SHOW_DATAPICK:
				showDialog(DATE_DIALOG_ID);
				break;
			default:
				break;
			}
		}
	};

	public boolean isEmpty() {
		if (Title.getText().toString().equals("")) {
			if (spinner1.getSelectedItem().toString().equals("")) {
				if (showDate.getText().toString().equals("")) {
					return true;
				}
			}
		}
		return false;
	}

}
