package com.naisinpo.fujianto.repoviewer.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.naisinpo.fujianto.repoviewer.data.RepoContract.RepoUserEntry;
import com.naisinpo.fujianto.repoviewer.data.RepoContract.RepoEntry;
/**
 * Created by fujianto on 04/11/14.
 */
public class RepoDbHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "repository.db";

    /**
     * Create a helper object to create, open, and/or manage a database.
     * This method always returns very quickly.  The database is not actually
     * created or opened until one of {@link #getWritableDatabase} or
     * {@link #getReadableDatabase} is called.
     *
     * @param context to use to open or create the database
     */
    public RepoDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Called when the database is created for the first time. This is where the
     * creation of tables and the initial population of the tables should happen.
     *
     * @param db The database.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_REPO_TABLE = "CREATE TABLE "+ RepoEntry.TABLE_NAME +" (" +
                RepoEntry._ID +" INTEGER PRIMARY KEY AUTOINCREMENT, " +
                RepoEntry.COLUMN_USER_KEY+ " INTEGER NOT NULL, "+
                RepoEntry.COLUMN_REPO_NAME+ " TEXT NOT NULL, " +
                RepoEntry.COLUMN_REPO_URL+ " TEXT NOT NULL, " +
                RepoEntry.COLUMN_REPO_DESC+ " TEXT NOT NULL, " +
                RepoEntry.COLUMN_REPO_LANG+ " repository_lang TEXT NOT NULL, " +

                "FOREIGN KEY ("+ RepoEntry.COLUMN_USER_KEY +") REFERENCES " +
                RepoUserEntry.TABLE_NAME + " (" + RepoUserEntry._ID + "), " +

                "UNIQUE (" + RepoEntry.COLUMN_REPO_NAME + " , "+ RepoEntry.COLUMN_USER_KEY +") " +
                "ON CONFLICT REPLACE);";

        final String SQL_CREATE_USERNAME_TABLE = " CREATE TABLE " + RepoUserEntry.TABLE_NAME + "( " +
                RepoUserEntry._ID + " INTEGER PRIMARY KEY, " +
                RepoUserEntry.COLUMN_USERNAME + " TEXT UNIQUE NOT NULL, " +
                " UNIQUE (" + RepoUserEntry.COLUMN_USERNAME+ " )" +
                " ON CONFLICT IGNORE); ";

        db.execSQL(SQL_CREATE_USERNAME_TABLE);
        db.execSQL(SQL_CREATE_REPO_TABLE);
    }

    /**
     * Called when the database needs to be upgraded. The implementation
     * should use this method to drop tables, add tables, or do anything else it
     * needs to upgrade to the new schema version.
     * <p/>
     * <p>
     * The SQLite ALTER TABLE documentation can be found
     * <a href="http://sqlite.org/lang_altertable.html">here</a>. If you add new columns
     * you can use ALTER TABLE to insert them into a live table. If you rename or remove columns
     * you can use ALTER TABLE to rename the old table, then create the new table and then
     * populate the new table with the contents of the old table.
     * </p><p>
     * This method executes within a transaction.  If an exception is thrown, all changes
     * will automatically be rolled back.
     * </p>
     *
     * @param db         The database.
     * @param oldVersion The old database version.
     * @param newVersion The new database version.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ RepoUserEntry.TABLE_NAME);
        db.execSQL("DROP TANLE IF EXISTS "+ RepoEntry.TABLE_NAME);
        onCreate(db);
    }
}
