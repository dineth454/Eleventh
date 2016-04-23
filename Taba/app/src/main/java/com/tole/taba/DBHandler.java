package com.tole.taba;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHandler extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Student.db";
    public static final String TABLE_NAME = "student";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "Card";
    public static final String COL_3 = "Name";
    public static final String COL_4 = "tpNo";
    public static final String COL_5 = "desc";
    public static final String COL_6 = "image";
    public static final String COL_7 = "email";
    public static final String COL_8 = "bday";

    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + " (" +
                COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_2 + " TEXT UNIQUE, " +
                COL_3 + " TEXT, " +
                COL_4 + " TEXT, " +
                COL_5 + " TEXT, " +
                COL_6 + " BLOB, " +
                COL_7 + " TEXT, " +
                COL_8 + " DATE)";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);

    }

    public boolean insertData(String card, String name, String tpno, String desc, byte[] image, String email, String bday){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_2, card);
        values.put(COL_3, name);
        values.put(COL_4, tpno);
        values.put(COL_5, desc);
        values.put(COL_6, image);
        values.put(COL_7, email);
        values.put(COL_8, bday);
        long result = db.insert(TABLE_NAME, null, values);
        if(result == -1)
            return false;
        else
            return true;
    }

    public Cursor getData(String card){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE Card = ?", new String[] { card });
        return res;
    }

    public boolean updateData(String id, String name){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_1, id);
        values.put(COL_2, name);
        db.update(TABLE_NAME, values, "stdID = ?", new String[]{ id });
        return true;
    }

    public Integer deleteData(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "stdID = ?", new String[]{ id });
    }
}
