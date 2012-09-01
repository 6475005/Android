package com.zq.log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import com.zq.db.LoadUtil;
import com.zq.db.CreatTable;

import android.os.Bundle;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class MainActivity extends ListActivity {
	public static ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
	public static SimpleAdapter listAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		CreatTable.creattable();// ����
		
		iniTLisit();// ��ʼ������
		
		listAdapter = new SimpleAdapter(this, list, R.layout.mylist,
				new String[] { "type", "title","date","time" }, new int[] { R.id.my_type,R.id.my_title,
						R.id.my_date,R.id.my_time });
		setListAdapter(listAdapter);

		Button add = (Button) findViewById(R.id.add);
		Button query = (Button) findViewById(R.id.query);
		Button back = (Button) findViewById(R.id.back);

		add.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, AddActivity.class);
				startActivity(intent);
			}
		});
		query.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, QueryActivity.class);
				startActivity(intent);
			}
		});
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		String date = list.get(position).get("date");
		String title = list.get(position).get("title");
		ScheduleDailog(title,date,position).show();
	}

	public static void iniTLisit()// ��ʼ������������Ҫ�����ݵĺ���
	{
		list.clear();
		String sql = "select * from schedule";
		Vector<Vector<String>> temp = LoadUtil.query(sql);
		for (int i = 0; i < temp.size(); i++) {
			HashMap<String, String> map = new HashMap<String, String>();			
			map.put("type", temp.get(i).get(2));
			map.put("title", temp.get(i).get(1));
			map.put("date", temp.get(i).get(4));
			map.put("time", temp.get(i).get(5));
			list.add(map);
		}
	}

	public AlertDialog ScheduleDailog(final String title,final String date,final int position) {
		String schedule = LoadUtil.query(title, date);
		String[] str = schedule.split("/");
		String type = str[1];
		String content = str[2];
		String time = str[4];
		AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
		builder.setTitle("�ճ�")
				.setIcon(R.drawable.ic_launcher)
				.setCancelable(false)
				// ����Ӧback��ť
				.setMessage(
						"���⣺" + title + "\n���ͣ�" + type + "\n���ݣ�\n\t"
								+ content + "\n\nʱ�䣺\n\t" + date + "\t\t"
								+ time)
				.setNeutralButton("ȷ��", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				})
				.setPositiveButton("ɾ��", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						LoadUtil.delete(title, date);
						list.remove(position);
						listAdapter.notifyDataSetChanged();
						Toast.makeText(MainActivity.this, "ɾ���ɹ���",
								Toast.LENGTH_SHORT).show();
					}
				});
		// �Ի�����ʾ����
		// ����Dialog����
		AlertDialog dlg = builder.create();
		return dlg;
	}
}
