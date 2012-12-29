package com.drorg8.calculatoractivity;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SQLUtils extends SQLiteOpenHelper {

	private static final String DB_NAME = "data.db";
	private static final int DB_VERSION = 4;
	private static final String TABLE = "history";
	
	public SQLUtils(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.d("DB", "onCreate");
		
		String sql = "create table " + TABLE + "(historyLine VARCHAR( 15 ));";
		
		db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}
	
	public void insertData(String historyLine) {
		Log.d("DB", "onInsert");
		
		SQLiteDatabase db = this.getWritableDatabase();
		
		String sql = "INSERT into " + TABLE + 
			" (historyLine) VALUES ('" + historyLine + "')";
		
		db.execSQL(sql);
		
		db.close();
	}
	
	public ArrayList<String> getHistoryLines() 
	{
		ArrayList<String> HistoryLines = new ArrayList<String>();
		
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.rawQuery("select * from " + TABLE, null);
		cursor.moveToFirst();
		
		while(!cursor.isAfterLast()) {
			HistoryLines.add(cursor.getString(0));
		     
		    cursor.moveToNext();
		}
		
		cursor.close();
		db.close();	
		
		return HistoryLines;
	}
		
	public ArrayList<String> getReverseHistoryLines() 
	{
		ArrayList<String> HistoryLines = new ArrayList<String>();
		
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.rawQuery("select * from " + TABLE, null);
		cursor.moveToLast();
		
		while(!cursor.isBeforeFirst()) {
			HistoryLines.add(cursor.getString(0));
		     
		    cursor.moveToPrevious();
		}
		
		cursor.close();
		db.close();	
		
		return HistoryLines;
	}
		
		public ArrayList<String> getHistoryLines(int numOfRows)
		{
			int i=0;
			ArrayList<String> HistoryLines = new ArrayList<String>();
			
			SQLiteDatabase db = this.getReadableDatabase();

			Cursor cursor = db.rawQuery("select * from " + TABLE, null);
			cursor.moveToFirst();
			
			while( (!cursor.isAfterLast()) && (i<numOfRows) ) {
				HistoryLines.add(cursor.getString(0));
			     
			    cursor.moveToNext();
			    i++;
			}
			
			cursor.close();
			db.close();	
			
			return HistoryLines;
		}


		public void clearHistory()
		{
			//ArrayList<String> HistoryLines = new ArrayList<String>();
			
			SQLiteDatabase db = this.getWritableDatabase();
			db.delete(TABLE, null, null);
			db.close();	
		}
		
		
}
