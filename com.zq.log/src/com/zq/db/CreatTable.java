package com.zq.db;

public class CreatTable {
	public static void creattable() {
		try {
			String sqll[] = new String[] {

					"create table if not exists schedule "
							+ // ����train��
							"(Sid integer primary key,Ttitle char(20),"
							+ "Ttype char(8),Ttext char(200),Tdate char(20),Ttime char(20))",

			};
			for (String o : sqll) {// ѭ������SQL��䣬���н���ͳ�ʼ��һЩ���ݲ���
				LoadUtil.createTable(o);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
