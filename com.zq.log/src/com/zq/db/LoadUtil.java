package com.zq.db;

import java.util.Vector;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class LoadUtil {

	public static SQLiteDatabase createOrOpenDatabase()// �������ݿ�
	{
		SQLiteDatabase sld = null;
		try {
			sld = SQLiteDatabase.openDatabase// ���Ӳ��������ݿ⣬����������򴴽�
					("/data/data/com.zq.log/log_db", null,
							SQLiteDatabase.OPEN_READWRITE
									| SQLiteDatabase.CREATE_IF_NECESSARY);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return sld;// ���ظ�����
	}

	public static void createTable(String sql) {// ������
		SQLiteDatabase sld = createOrOpenDatabase();// �������ݿ�
		try {
			sld.execSQL(sql);// ִ��SQL���
			sld.close();// �ر�����
		} catch (Exception e) {
		}
	}

	public static boolean insert(String sql)// ��������
	{
		SQLiteDatabase sld = createOrOpenDatabase();// �������ݿ�
		try {
			sld.execSQL(sql);
			sld.close();
			return true;
		} catch (Exception e) {
			return false;
		}

	}

	public static boolean delete(String title, String date) {
		SQLiteDatabase sld = createOrOpenDatabase();// �������ݿ�
		String sql = "delete from schedule where Ttitle = '" + title
				+ "'AND Tdate='" + date + "'";
		try {
			sld.execSQL(sql);
			sld.close();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static String query(String title, String date) {
		String sql = "select * from schedule where Ttitle = '" + title
				+ "'AND Tdate='" + date + "'";
		Vector<Vector<String>> result = query(sql);
		// String title = result.get(0).get(1);
		String type = result.get(0).get(2);
		String content = result.get(0).get(3);
		// String date = result.get(0).get(4);
		String time = result.get(0).get(5);
		return title + "/" + type + "/" + content + "/" + date + "/" + time;
	}

	public static Vector<Vector<String>> query(String sql)// ��ѯ
	{
		Vector<Vector<String>> vector = new Vector<Vector<String>>();// �½���Ų�ѯ���������
		SQLiteDatabase sld = createOrOpenDatabase();// �õ��������ݿ������
		try {
			Cursor cur = sld.rawQuery(sql, new String[] {});// �õ������
			while (cur.moveToNext())// ���������һ��
			{
				Vector<String> v = new Vector<String>();
				int col = cur.getColumnCount(); // �����������
				for (int i = 0; i < col; i++) {
					v.add(cur.getString(i));
				}
				vector.add(v);
			}
			cur.close();// �رս����
			sld.close();// �ر�����
		} catch (Exception e) {
			e.printStackTrace();
		}
		return vector;
	}

}
