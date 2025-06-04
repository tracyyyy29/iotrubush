package com.example.du.deepPart;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * 
 * NK2023 Embedded System Design Body IV Design Deep Part
 * 
 * for PSI Input data based on SQLite
 * 
 * @version 1.0
 * 
 * @author achdu0000<achdu0000@163.com>
 * 
 */

public class PSIInputUtil extends SQLiteOpenHelper {
	private static final String CREATE_PSI = "create table PSI("
			+ " id integer primary key autoincrement, " + " time text, "
			+ " p_in_theory integer, " + " s_in_theory integer, "
			+ " i_in_theory integer, " + " p_in_fact integer, "
			+ " s_in_fact integer, " + " i_in_fact integer) ";

	public static final String TABLE_NAME = "PSI";
	public static final String ID = "id";
	public static final String TIME = "time";
	public static final String P_IN_THEORY = "p_in_theory";
	public static final String S_IN_THEORY = "s_in_theory";
	public static final String I_IN_THEORY = "i_in_theory";
	public static final String P_IN_FACT = "p_in_fact";
	public static final String S_IN_FACT = "s_in_fact";
	public static final String I_IN_FACT = "i_in_fact";
	private Context context;

	public PSIInputUtil(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_PSI);
		Toast.makeText(context, "Create PSI successed", Toast.LENGTH_SHORT)
				.show();
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}
