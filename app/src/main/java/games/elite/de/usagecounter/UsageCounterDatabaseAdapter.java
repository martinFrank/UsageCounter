package games.elite.de.usagecounter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class UsageCounterDatabaseAdapter {

    private static final String DATABASE_NAME = "usage.count.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_USAGE = "USAGE";
    private static final String TABLE_USAGE_ID = "ID";
    private static final String TABLE_USAGE_TEXT = "TEXT";
    private static final String TABLE_USAGE_COUNT = "COUNT";
    private static final String WHERE_ID_IS = TABLE_USAGE_ID + "=?";

    static final String DATABASE_CREATE = "create table " + TABLE_USAGE + "( " + TABLE_USAGE_ID + " integer primary key autoincrement," + TABLE_USAGE_TEXT + "  text," + TABLE_USAGE_COUNT + "  integer); ";
    public static SQLiteDatabase db;
    private static DataBaseHelper dbHelper;

    public UsageCounterDatabaseAdapter(Context context) {
        dbHelper = new DataBaseHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public UsageCounterDatabaseAdapter open() throws SQLException {
        db = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        db.close();
    }

    public SQLiteDatabase getDatabaseInstance() {
        return db;
    }

    public void deleteEntryById(int id) {
        try {
            int numberOFEntriesDeleted = db.delete(TABLE_USAGE, WHERE_ID_IS, new String[]{Integer.toString(id)});
        } catch (Exception ex) {
            Log.e(TABLE_USAGE, ex.toString());
        }
    }

    public Counter getSinlgeEntryById(Integer id) {
        try {
            db = dbHelper.getReadableDatabase();
            Cursor cursor = db.query(TABLE_USAGE, null, WHERE_ID_IS, new String[]{Integer.toString(id)}, null, null, null);
            if (cursor.getCount() < 1) {
                return null;
            }
            cursor.moveToFirst();
            Counter counter = getCounterFromCursor(cursor);
            cursor.close();
            return counter;
        } catch (Exception e) {
            Log.e(TABLE_USAGE, e.toString());
        }
        return null;
    }

    public List<Counter> getAllEntries() {
        List<Counter> counters = new ArrayList<>();
        try {
            db = dbHelper.getReadableDatabase();
            Cursor cursor = db.query(TABLE_USAGE, null, null, null, null, null, TABLE_USAGE_ID);
            if (cursor.getCount() < 1) {
                return null;
            }
            cursor.moveToFirst();
            do {
                Counter counter = getCounterFromCursor(cursor);
                counters.add(counter);
                Log.d("Database", "count=" + counter);
            } while (cursor.moveToNext());
            cursor.close();
        } catch (Exception e) {
            Log.e(TABLE_USAGE, e.toString());
        }
        return counters;
    }

    public void updateEntry(int id, String text, int count) {
        try {
            ContentValues updatedValues = new ContentValues();
            updatedValues.put(TABLE_USAGE_TEXT, text);
            updatedValues.put(TABLE_USAGE_COUNT, count);
            db.update(TABLE_USAGE, updatedValues, WHERE_ID_IS, new String[]{Integer.toString(id)});
        } catch (Exception e) {
            Log.e(TABLE_USAGE, e.toString());
        }
    }

    public void updateCount(Counter counter) {
        try {
            ContentValues contentValues = getContentValuesFromCounter(counter);
            db.update(TABLE_USAGE, contentValues, WHERE_ID_IS, new String[]{Long.toString(counter.getId())});
        } catch (Exception e) {
            Log.e(TABLE_USAGE, e.toString());
        }
    }


    public long insertCount(Counter counter) {
        try {
            ContentValues contentValues = getContentValuesFromCounter(counter);
            return db.insert(TABLE_USAGE, null, contentValues);
        } catch (Exception e) {
            Log.e(TABLE_USAGE, e.toString());
        }
        return -1;
    }

    private Counter getCounterFromCursor(Cursor cursor) {
        Integer id = cursor.getInt(cursor.getColumnIndex(TABLE_USAGE_ID));
        String text = cursor.getString(cursor.getColumnIndex(TABLE_USAGE_TEXT));
        Integer count = cursor.getInt(cursor.getColumnIndex(TABLE_USAGE_COUNT));
        Counter counter = new Counter();
        counter.setId(id);
        counter.setText(text);
        counter.setCount(count);
        return counter;
    }

    private ContentValues getContentValuesFromCounter(Counter counter) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TABLE_USAGE_TEXT, counter.getText());
        contentValues.put(TABLE_USAGE_COUNT, counter.getCount());
        return contentValues;
    }
}
