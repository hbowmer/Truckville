package holt.bowmer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class TrucksDB { //This class encapsulates data access to a SQLite database that will hold the trucks data and allow the user to update it.
	
	/*
	 * These constants will be used in the application to look up data
	 * from the proper field names in the database. There is also a
	 * database creation string defined, which is used to create a new
	 * database schema if one doesn't exist already.
	 * 
	 */
   public static final String KEY_TITLE = "title"; //These are three fields found in the "trucks" table
   public static final String KEY_BODY = "body";
   public static final String KEY_ROWID = "_id"; //The _id usually has to be specified when querying or updating the database
   
   private static final String TAG = "TrucksDB";
   private DatabaseHelper mDbHelper;
   private SQLiteDatabase mDb;
   
   private static final String DATABASE_NAME = "data"; //The database will have the name data with a single table
   private static final String DATABASE_TABLE_TRUCKS = "trucks"; //The table will have the name trucks
   private static final String DATABASE_TABLE_FAVORITES = "favorites";
   private static final int DATABASE_VERSION = 1;
   
   private static final String TRUCKS_CREATE =
		   " create table " + DATABASE_TABLE_TRUCKS +  " (_id integer primary key autoincrement, "
			+ "title text not null, body text not null);";
   private static final String FAVORITES_CREATE =
		   " create table " + DATABASE_TABLE_FAVORITES + " (_id integer primary key autoincrement,"
		   + " title text not null, body text not null);";

   

   private final Context mCtx;
   
   private static class DatabaseHelper extends SQLiteOpenHelper {
	   DatabaseHelper(Context context) {
		   super(context, DATABASE_NAME, null, DATABASE_VERSION);
	   }
	   
	   @Override
	   public void onCreate(SQLiteDatabase db) {
		   
		   db.execSQL(TRUCKS_CREATE);
		   db.execSQL(FAVORITES_CREATE);
	   }
	   
	   @Override
	   public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		   Log.w(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
		   db.execSQL("DROP TABLE IF EXISTS notes");
		   onCreate(db);
	   }
   }
   
   public TrucksDB(Context ctx) { //The activity class implements the context class, so usually "this" will just be passed when needing a context
       this.mCtx = ctx;
   }
   
   /*
    * The open() method calls up an instance of DatabaseHelper, which is the local implementation of
    * the SQLiteOpenHelper class. It calls getWritableDatabase(), which handles creating/opening a
    * database.
    */
   
   public TrucksDB open() throws SQLException {
       mDbHelper = new DatabaseHelper(mCtx);
       mDb = mDbHelper.getWritableDatabase();
       return this;
   }
   
   /*
    * The close() method simply closes the database, releasing resources related to the connection.
    */
   
   public void close() {
       mDbHelper.close();
   }
   
   /*
    * createFavorite() takes strings for the title and body of a new favorite, then stores that favorite in
    * the database. Assuming the new favorite is created successfully, the method also returns the row _id
    * value for the newly created favorite.
    */
   
   public long createFavorite(String title, String body) {
       ContentValues initialValues = new ContentValues();
       initialValues.put(KEY_TITLE, title);
       initialValues.put(KEY_BODY, body);

       return mDb.insert(DATABASE_TABLE_FAVORITES, null, initialValues);
   }
   
   public long createTrucks (String title, String body) {
	   ContentValues initialValues = new ContentValues();
	   initialValues.put(KEY_TITLE, title);
	   initialValues.put(KEY_BODY, body);
	   
	   return mDb.insert(DATABASE_TABLE_TRUCKS, null, initialValues);
   }
   
   /*
    * deleteNote() takes a "rowId" for a particular favorite, and deletes that favorite from the database.
    */
   
   public boolean deleteNote(long rowId) {

       return mDb.delete(DATABASE_TABLE_FAVORITES, KEY_ROWID + "=" + rowId, null) > 0;
   }
   
   /*
    * fetchAllFavorites() issues a query to return a cursor over all favorites in the database. the query()
    * call is worth examination and understanding. The first field is the name of the database table to 
    * query (in this case DATABASE_TABLE is "trucks"). The next is the list of columns the user wants
    * returned, in this case this is the _id, title, and body columns, so these are specified in the String
    * array. The remaining fields are, in order, selection, selectionArgs, groupBy, having and orderBy.
    * Having these null means we want all data, need no grouping, and will take the default order. 				//Check and see if we can group alphabetically.
    */
   
   public Cursor fetchAllFavorites() {

       return mDb.query(DATABASE_TABLE_FAVORITES, new String[] {KEY_ROWID, KEY_TITLE,
               KEY_BODY}, null, null, null, null, null);
   }
   
   public Cursor fetchAllTrucks() {
	   
	   return mDb.query(DATABASE_TABLE_TRUCKS, new String[] {KEY_ROWID, KEY_TITLE,
			   KEY_BODY}, null, null, null, null, null);
   }
   
   /*
    * fetchFavorite() is similar to fetchAllFavorites() but just gets one favorite with the rowID that is
    * specified. It uses a slightly different version of the SQLiteDatabase query() method. The first
    * parameter (set true) indicates that the user is interested in one distinct result. The selection
    * parameter (the fourth parameter) has been specified to search only for the row "where _id=" the rowId
    * that was passed in. Therefore, the user is returned a Cursor on the one row.
    */
   
   public Cursor fetchFavorite(long rowId) throws SQLException {

       Cursor mCursor =

           mDb.query(true, DATABASE_TABLE_FAVORITES, new String[] {KEY_ROWID,
                   KEY_TITLE, KEY_BODY}, KEY_ROWID + "=" + rowId, null,
                   null, null, null, null);
       if (mCursor != null) {
           mCursor.moveToFirst();
       }
       return mCursor;

   }
   
   public Cursor fetchTrucks(long rowId) throws SQLException {
	   
	   Cursor mCursor = 
			   
			   mDb.query(true, DATABASE_TABLE_TRUCKS, new String[] {KEY_ROWID,
					   KEY_TITLE, KEY_BODY}, KEY_ROWID + "=" + rowId, null,
					   null, null, null, null);
	   if (mCursor !=null) {
		   mCursor.moveToFirst();
	   }
	   return mCursor;
   }
   
   /*
    * updateFavorite() takes a rowId, title, and body, and uses a ContentValues instance to update the note 
    * of the given rowId.
    */
   
   public boolean updateFavorite(long rowId, String title, String body) {
       ContentValues args = new ContentValues();
       args.put(KEY_TITLE, title);
       args.put(KEY_BODY, body);

       return mDb.update(DATABASE_TABLE_FAVORITES, args, KEY_ROWID + "=" + rowId, null) > 0;
   }
   
   public boolean updateTrucks(long rowId, String title, String body) {
	   ContentValues args = new ContentValues();
	   args.put(KEY_TITLE, title);
	   args.put(KEY_BODY, body);
	   
	   return mDb.update(DATABASE_TABLE_TRUCKS, args, KEY_ROWID + "=" + rowId, null) > 0;
   }
}