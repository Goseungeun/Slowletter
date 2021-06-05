package org.techtown.slowletter;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class LetterDatabase {
    private static final String TAG = "LetterDatabase";
    public static String DATABASE_NAME = "letter.db";

    private static LetterDatabase database;
    public static String TABLE_LETTER = "LETTER";
    public static int DATABASE_VERSION = 1;

    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;
    private Context context;

    LetterDatabase(Context context){
        this.context = context;
    }

    public static LetterDatabase getInstance(Context context){
        if (database ==null){
            database = new LetterDatabase(context);
        }

        return database;
    }

    public boolean open(){
        println("opening database [" + DATABASE_NAME +"].");

        dbHelper = new DatabaseHelper(context);
        db = dbHelper.getWritableDatabase();

        return true;
    }

    public void close(){
        println("closing database [" + DATABASE_NAME +"].");
        db.close();

        database = null;
    }

    public Cursor rawQUery(String SQL) {
        println("\nexecuteQuery called.\n");

        Cursor cursor = null;
        try{
            cursor = db.rawQuery(SQL,null);
            println("cursor count : " + cursor.getCount());
        }catch (Exception ex){
            Log.d(TAG,"Exception in executeQuery",ex);
        }

        return cursor;
    }

    public boolean execSQL(String SQL){
        println("\nexecute called.\n");
        try {
            Log.d(TAG,"SQL : "+SQL);
            db.execSQL(SQL);
        }catch (Exception ex){
            Log.d(TAG,"Exception in executeQuery", ex);
            return false;
        }

        return true;
    }

    private class DatabaseHelper extends SQLiteOpenHelper{

        public DatabaseHelper(@Nullable Context context) {
            super(context, DATABASE_NAME,null,DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            println("creating database [" + DATABASE_NAME + "].");

            println("creating table [" + TABLE_LETTER + "].");

            String DROP_SQL = "drop table if exists " + TABLE_LETTER;
            try {
                db.execSQL(DROP_SQL);
            }catch (Exception ex){
                Log.d(TAG,"Exception in DROP_SQL", ex);
            }

            String CREATE_SQL = "create table " +TABLE_LETTER + "("
                    + "_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,"
                    + "WRITEDATE TEXT NOT NULL, "
                    + "RECEIVEDATE TEXT NOT NULL,"
                    + "CONTEXT TEXT NOT NULL,"
                    + "BACKCOLOR INTEGER NOT NULL,"
                    + "WEATHER TEXT DEFAULT '',"
                    + "PICTURE TEXT DEFAULT''"
                    +")";
            try {
                db.execSQL(CREATE_SQL);
            }catch (Exception ex){
                Log.e(TAG,"Exception in CREATE_SQL",ex);
            }

            String CREATE_INDEX_SQL = "create index " + TABLE_LETTER + "_IDX ON " + TABLE_LETTER + "("
                    +"CREATE_DATE"
                    +")";
            try {
                db.execSQL(CREATE_INDEX_SQL);
            }catch (Exception ex){
                Log.e(TAG,"Exception in CREATE_INDEX_SQL",ex);
            }
        }

        public void onOpen(SQLiteDatabase db){
            println("opened database [" + DATABASE_NAME+"]");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            println("Upgrading database from version" + oldVersion + "to " + newVersion + ".");
        }

    }

    private void println(String msg){
        Log.d(TAG,msg);
    }
}
