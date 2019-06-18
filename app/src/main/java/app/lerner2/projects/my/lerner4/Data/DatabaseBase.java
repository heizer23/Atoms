package app.lerner2.projects.my.lerner4.Data;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import app.lerner2.projects.my.lerner4.R;


public class DatabaseBase {
	protected static class DbHelper extends SQLiteOpenHelper {

		private static final String DATABASE_TABLE = null;

		public DbHelper(Context context) {
			super(context, DB_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
		}

	}

	private static final String TAG = "DatabaseBase ";

	private static String DB_NAME;
	private static String DB_PATH;
	private static final int DATABASE_VERSION = 1;

	public static final String KEY_ROWID = "_id";
    private Resources res;

	private final Context ourContext;

	SQLiteDatabase mySQLDB;
	DbHelper ourHelper;

    static final String TABLE_EVENTS = "Events";
    public static final String TABLE_RUNDEN = "runden";

	public DatabaseBase(Context c, Activity act) {
		ourContext = c;
        res = c.getResources();
        DB_NAME = res.getString(R.string.DatabaseName);
        DB_PATH = "/data/data/" +  ourContext.getPackageName() + "/databases/" + DB_NAME;
	}

	public void close() {
		mySQLDB.close();
	}

    public DatabaseBase open() throws SQLException {
        ourHelper = new DbHelper(ourContext);
        mySQLDB = ourHelper.getWritableDatabase();
        return this;
    }

    public void update(int id, ContentValues values) {
        ourHelper = new DbHelper(ourContext);
        mySQLDB = ourHelper.getWritableDatabase();
        mySQLDB.update(TABLE_EVENTS, values, KEY_ROWID + "=" + id, null);
        mySQLDB.close();
    }

	public void addItem(String tableName, ContentValues values) {
		ourHelper = new DbHelper(ourContext);
		mySQLDB = ourHelper.getWritableDatabase();
		mySQLDB.insert(tableName, null, values);
		mySQLDB.close();
	}

    public String[] getFirstOfCursor(Cursor cursor, String[] fields){
        int[] iIndex = new int[fields.length];
        String[] data = new String[fields.length];
        for (int i = 0; i < fields.length; i++) {
            iIndex[i] = cursor.getColumnIndex(fields[i]);
        }
        cursor.moveToFirst();
        for (int i = 0; i < fields.length; i++) {
            data[i] = cursor.getString(iIndex[i]);
        }

        return data;
    }

    public String[][] getAllOfCursor(Cursor cursor, String[] fields){
        int[] iIndex = new int[fields.length];

        ArrayList<String[]> dataList = new ArrayList<String[]>();
        String[][] dataAll;

        for (int i = 0; i < fields.length; i++) {
            iIndex[i] = cursor.getColumnIndex(fields[i]);
        }
        //cursor.moveToFirst();
        while(cursor.moveToNext()){
            String[] dataEintrag = new String[fields.length];
            for (int i = 0; i < fields.length; i++) {
                dataEintrag[i] = cursor.getString(iIndex[i]);
            }
            dataList.add(dataEintrag);
        }
        dataAll = new String[dataList.size()][fields.length];
        for (int i = 0; i < dataList.size(); i++) {
            dataAll[i] = dataList.get(i);
        }

        return dataAll;
    }


	public String createNewDataBase() {
		String result = null;
		InputStream assetsDB = null;
        File myFile;
		// read from SD
		try {

			FileInputStream instream = null;
			instream = new FileInputStream(
					Environment.getExternalStorageDirectory()
							+ "/Quizzer/Backup/" + DB_NAME);
            myFile = new File(DB_PATH);
            if (!myFile.exists()) {
                myFile.getParentFile().mkdirs();
                myFile.createNewFile();
            }
            OutputStream dbOut = new FileOutputStream(DB_PATH);

			byte[] buffer = new byte[1024];
			int length;
			while ((length = instream.read(buffer)) > 0) {
				dbOut.write(buffer, 0, length);
			}
			dbOut.flush();
			dbOut.close();
			instream.close();
			result = "Old Database read";

			Log.i(TAG, "Old Database read");
		} catch (FileNotFoundException e1) {

			File exportDir = new File(
					Environment.getExternalStorageDirectory(),
					"/Quizzer/Lektionen");
			if (!exportDir.exists()) {

				exportDir.mkdirs();
			}
			// new from assets

			result = "Custom Database not found";
			Log.e(TAG, "Custom Database not found");
			e1.printStackTrace();
			try {


				assetsDB = ourContext.getAssets().open(DB_NAME);

				OutputStream dbOut = new FileOutputStream(DB_PATH);

				byte[] buffer = new byte[1024];
				int length;
				while ((length = assetsDB.read(buffer)) > 0) {
					dbOut.write(buffer, 0, length);
				}
				dbOut.flush();
				dbOut.close();
				assetsDB.close();
				result = "Custom Database not found, new Database created";
				Log.i(TAG, "New Database created");
			} catch (IOException e) {
				result = "Could not create Database";
				Log.e(TAG, "Could not create Database");
				e.printStackTrace();
			}
		} catch (IOException e) {
			result = "Could not create Database";
			Log.e(TAG, "Could not create Database");
			e.printStackTrace();
		}

		return result;
	}

	public void checkDB(){
		ourHelper = new DbHelper(ourContext);
		mySQLDB = ourHelper.getWritableDatabase();
		Cursor cursor = mySQLDB.rawQuery("SELECT name FROM sqlite_master WHERE type ='table' AND name NOT LIKE 'sqlite_%';", null);

		cursor.moveToFirst();
		while(!cursor.isAfterLast()) {
			cursor.getString(0);
			Log.i(TAG, cursor.getString(0));
			cursor.moveToNext();
		}
		mySQLDB.close();
	}

	public int[] getIntsFromSQL(String sqlQuery){
		int[] result = null;
		ourHelper = new DbHelper(ourContext);
		mySQLDB = ourHelper.getWritableDatabase();
		Cursor cursor = mySQLDB.rawQuery(sqlQuery, null);
		result = new int[cursor.getCount()];
		cursor.moveToFirst();
		int i = 0;
		while(!cursor.isAfterLast()) {
			result[i] = cursor.getInt(0);
			i=i+1;
			cursor.moveToNext();
		}
		mySQLDB.close();
		return result;
	}

	public void runSQL(String sqlString){
		try {
			open();
			mySQLDB.execSQL(sqlString);
			close();
		} catch (SQLException e) {
			String fehler = e.toString();
			e.printStackTrace();
		}
	}

    public void deleteItem(String id) {
        try {
            mySQLDB.execSQL("DELETE FROM GM1 WHERE rowid=" + id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

	public void deleteItems(String tableName, int[] questionIds) {
		String sql = "DELETE FROM " + tableName + "  WHERE _id="
				+ questionIds[0];
		for (int i = 1; i < questionIds.length; i++) {
			sql = sql + " or _id=" + questionIds[i];
		}
		mySQLDB.execSQL(sql);
	}

    public SQLiteDatabase getWritableDatabase() {
        ourHelper = new DbHelper(ourContext);
        mySQLDB = ourHelper.getWritableDatabase();
        return mySQLDB;
    }

}
