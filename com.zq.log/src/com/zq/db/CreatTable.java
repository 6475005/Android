package com.zq.db;

public class CreatTable {
	public static void creattable() {
		try {
			String sqll[] = new String[] {

					"create table if not exists schedule "
							+ // 建立train表
							"(Sid integer primary key,Ttitle char(20),"
							+ "Ttype char(8),Ttext char(200),Tdate char(20),Ttime char(20))",

			};
			for (String o : sqll) {// 循环所有SQL语句，进行建表和初始化一些数据操作
				LoadUtil.createTable(o);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
