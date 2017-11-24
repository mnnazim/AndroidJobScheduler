package com.example.androidjobscheduler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by USER on 11/19/2017.
 */

public class StatusDatabase extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "StatusDatabase";
    public static final String TABLE_NAME = "StatusLog";

    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_DATE = "Date";
    public static final String COLUMN_STATUS="Status";
    

    Context context;

    public StatusDatabase(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context=context;
        //db=getWritableDatabase();
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_DATE + " TEXT, " +
                COLUMN_STATUS + " TEXT" + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    void InsertData(String status){
        String date=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime()).toString();

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_DATE,date);
        values.put(COLUMN_STATUS,status);
        // insert
        long value=db.insert(TABLE_NAME,null, values);
        //Toast.makeText(context,""+value,Toast.LENGTH_LONG).show();
        db.close();
    }

    List<StatusGetSet> getAllData(){
        List<StatusGetSet> ldatas=new LinkedList<StatusGetSet>();

        String query = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        StatusGetSet ldata = null;

        if (cursor.moveToFirst()) {
            do {
                ldata = new StatusGetSet();
                ldata.setID(Integer.parseInt(cursor.getString(0)));
                ldata.setDateTime(cursor.getString(1));
                ldata.setStatus(cursor.getString(2));
                ldatas.add(ldata);
            } while (cursor.moveToNext());
        }
        return ldatas;
    }

    public int update(StatusGetSet ldata) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_DATE,ldata.getDateTime());
        int i = db.update(TABLE_NAME, // table
                values, // column/value
                "id = ?", // selections
                new String[] { String.valueOf(ldata.getID()) });

        if(db.isOpen()){
            db.close();
        }
        return i;
    }

    void deleteAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        try{
            db.execSQL("delete from "+ TABLE_NAME);
            //Toast.makeText(context,"Delete Complete",Toast.LENGTH_SHORT).show();
        }catch(Exception e){
            Toast.makeText(context,e.getMessage(),Toast.LENGTH_SHORT).show();
        }finally {
            try{
                if(db.isOpen()){
                    db.close();
                }
            }catch (Exception e){

            }
        }
    }

    void deleteList(List<StatusGetSet> list){
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            for (StatusGetSet data : list) {
                db.execSQL("delete from " + TABLE_NAME + " where " + COLUMN_ID + "=" + data.getID());
            }
        }catch (Exception e){
            Toast.makeText(context,"Error Deteting from Local Database",Toast.LENGTH_SHORT).show();
        }finally {
            try {
                if (db.isOpen()) {
                    db.close();
                }
            } catch (Exception e) {

            }
        }
    }

    void delete(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        try{
            //db.delete();
            db.execSQL("delete from "+ TABLE_NAME+" where "+COLUMN_ID+"="+id);
            //Toast.makeText(context,"Delete Complete",Toast.LENGTH_SHORT).show();
        }catch(Exception e){
            Toast.makeText(context,e.getMessage(),Toast.LENGTH_SHORT).show();
        }finally {
            try{
                if(db.isOpen()){
                    db.close();
                }
            }catch (Exception e){

            }
        }

    }

    public static void QuickInsertData(Context context,String status){
        StatusDatabase statusDatabase=new StatusDatabase(context);
        statusDatabase.InsertData(status);
    }

    public static void QuickDelete(Context context){
        StatusDatabase statusDatabase=new StatusDatabase(context);
        statusDatabase.deleteAllData();
    }
}
