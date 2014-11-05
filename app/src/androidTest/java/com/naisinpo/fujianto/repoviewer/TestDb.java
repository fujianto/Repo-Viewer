package com.naisinpo.fujianto.repoviewer;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import android.util.Log;

import com.naisinpo.fujianto.repoviewer.data.RepoContract.RepoEntry;
import com.naisinpo.fujianto.repoviewer.data.RepoContract.RepoUserEntry;
import com.naisinpo.fujianto.repoviewer.data.RepoDbHelper;

/**
 * Created by fujianto on 04/11/14.
 */
public class TestDb extends AndroidTestCase{
    public static final String LOG_TAG = TestDb.class.getSimpleName();

    public void testCreateDb() throws Throwable{
        mContext.deleteDatabase(RepoDbHelper.DATABASE_NAME);
        SQLiteDatabase db = new RepoDbHelper(this.mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());
        db.close();
    }

    public void testInsertReadDb() throws Throwable{
        // Test data we're going to insert into the DB to see if it works.
        String testUserName = "yoda";

        //If there's an error in those massive SQL table creation Strings,
        // errors will be thrown here when you try to get a writable database.
        RepoDbHelper dbHelper = new RepoDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(RepoUserEntry.COLUMN_USERNAME, testUserName);

        long userRowId;
        userRowId = db.insert(RepoUserEntry.TABLE_NAME, null, values);

        // Verify we got a row back.
        assertTrue(userRowId != -1);
        Log.d(LOG_TAG, "New row id" + userRowId );

        // Data's inserted. IN THEORY. Now pull some out to stare at it and verify it made
        // the round trip.

        String[] columns = {
                RepoUserEntry._ID,
                RepoUserEntry.COLUMN_USERNAME
        };

        // A cursor is your primary interface to the query results.
        Cursor cursor = db.query(
            RepoUserEntry.TABLE_NAME, // Table to Query
            columns, // all columns
            null, // Columns for the "where" clause
            null, // Values for the "where" clause
            null, // columns to group by
            null, // columns to filter by row groups
            null // sort order
        );

        // If possible, move to the first row of the query results.
        if(cursor.moveToFirst()) {
            int usernameIndex = cursor.getColumnIndex(RepoUserEntry.COLUMN_USERNAME);
            String username = cursor.getString(usernameIndex);

            // Hooray, data was returned! Assert that it's the right data, and that the database
            // creation code is working as intended.
            // Then take a break. We both know that wasn't easy.

            assertEquals(testUserName, username);

        } else{
            fail("No values returned :(");
        }

        //TODO Put dummy data to ContentValues
        ContentValues repoValues = new ContentValues();
        repoValues.put(RepoEntry.COLUMN_USER_KEY, userRowId);
        repoValues.put(RepoEntry.COLUMN_REPO_NAME, "Matteria Apps");
        repoValues.put(RepoEntry.COLUMN_REPO_DESC, "An revolutionary App that re-imagine what matters on your life.");
        repoValues.put(RepoEntry.COLUMN_REPO_URL, "https://github.com/fujianto/matteria");
        repoValues.put(RepoEntry.COLUMN_REPO_LANG, "Java");

        //TODO insert to Database with RowId
        Long repoRowId;
        repoRowId = db.insert(RepoEntry.TABLE_NAME, null, repoValues);

        //TODO verify getting Row Back
        assertTrue(repoRowId != -1);

        //TODO user cursors to query results
        Cursor repoCursor = db.query(
                RepoEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );

        //TODO Check if Repo data returned
        if(!repoCursor.moveToFirst()){
            fail("No Repo data returned!");
        }

        //TODO Check if data on DB same with dummy data
        assertEquals(repoCursor.getInt(repoCursor.getColumnIndex(RepoEntry.COLUMN_USER_KEY)), userRowId);
        assertEquals(repoCursor.getString(repoCursor.getColumnIndex(RepoEntry.COLUMN_REPO_NAME)), "Matteria Apps");
        assertEquals(repoCursor.getString(repoCursor.getColumnIndex(RepoEntry.COLUMN_REPO_DESC)), "An revolutionary App that re-imagine what matters on your life.");
        assertEquals(repoCursor.getString(repoCursor.getColumnIndex(RepoEntry.COLUMN_REPO_URL)), "https://github.com/fujianto/matteria");
        assertEquals(repoCursor.getString(repoCursor.getColumnIndex(RepoEntry.COLUMN_REPO_LANG)), "Java");

        //TODO Close DB connection
        repoCursor.close();
        dbHelper.close();
    }
}
