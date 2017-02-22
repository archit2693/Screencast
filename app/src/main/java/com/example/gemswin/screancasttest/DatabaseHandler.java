package com.example.gemswin.screancasttest;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by this pc on 15-02-17.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "FileDatabase";
    private static final String TABLE_FILES = "Files";
    private static final String KEY_ID = "id";
    private static final String KEY_EXTENSION = "extension";
    private static final String KEY_PATH = "path";
    private static final String KEY_NAME = "name";


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //3rd argument to be passed is CursorFactory instance
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_FILES + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_EXTENSION + " TEXT,"+ KEY_NAME + " TEXT,"
                + KEY_PATH + " TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FILES);

        // Create tables again
        onCreate(db);
    }

    // code to add the new contact
    void addFile(FileDatabase fileDatabase) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_EXTENSION, fileDatabase.getExtension()); // Contact Name
        values.put(KEY_NAME, fileDatabase.getName());//name
        values.put(KEY_PATH, fileDatabase.getPath()); // Contact Phone

        // Inserting Row
        db.insert(TABLE_FILES, null, values);
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
    }

    // code to get the single contact
    FileDatabase getFiles(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_FILES, new String[] { KEY_ID,
                        KEY_EXTENSION,KEY_NAME, KEY_PATH }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null,null);
        if (cursor != null)
            cursor.moveToFirst();

        FileDatabase fileDatabase = new FileDatabase(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2),cursor.getString(3));
        // return contact
        return fileDatabase;
    }

    // code to get all contacts in a list view
    public List<FileDatabase> getAllContacts() {
        List<FileDatabase> contactList = new ArrayList<FileDatabase>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_FILES;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                FileDatabase fileDatabase = new FileDatabase();
                fileDatabase.setId(Integer.parseInt(cursor.getString(0)));
                fileDatabase.setExtension(cursor.getString(1));
                fileDatabase.setName(cursor.getString(2));
                fileDatabase.setPath(cursor.getString(3));
                // Adding contact to list
                contactList.add(fileDatabase);
            } while (cursor.moveToNext());
        }

        // return contact list
        return contactList;
    }

    // code to update the single contact
    public int updateContact(FileDatabase fileDatabase) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_EXTENSION, fileDatabase.getExtension());
        values.put(KEY_NAME,fileDatabase.getName());
        values.put(KEY_PATH, fileDatabase.getPath());

        // updating row
        return db.update(TABLE_FILES, values, KEY_ID + " = ?",
                new String[] { String.valueOf(fileDatabase.getId()) });
    }

    // Deleting single contact
    public void deleteContact(FileDatabase fileDatabase) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FILES, KEY_ID + " = ?",
                new String[] { String.valueOf(fileDatabase.getId()) });
        db.close();
    }

    // Getting contacts Count
    public int getContactsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_FILES;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }

}
