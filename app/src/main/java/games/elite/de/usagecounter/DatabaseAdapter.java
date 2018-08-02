package games.elite.de.usagecounter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DatabaseAdapter {

    private static final String DATABASE_NAME = "usage.count.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_COUNTER = "COUNTER";
    private static final String TABLE_COUNTER_ID = "ID";
    private static final String TABLE_COUNTER_TEXT = "TEXT";
    private static final String TABLE_COUNTER_COUNT = "COUNT";
    private static final String WHERE_COUNTERID_IS = TABLE_COUNTER_ID + "=?";

    private static final String TABLE_TIMESTAMP = "TIMESTAMP";
    private static final String TABLE_TIMESTAMP_ID = "ID";
    private static final String TABLE_TIMESTAMP_COUNTERID = "COUNTERID";
    private static final String TABLE_TIMESTAMP_DELTA = "DELTA";
    private static final String TABLE_TIMESTAMP_TIME = "TIME";
    private static final String WHERE_TIMESTAMP_COUNTERID_IS = TABLE_TIMESTAMP_COUNTERID + "=?";

    static final String DATABASE_CREATE_COUNTER = "create table " + TABLE_COUNTER + "( " +
            TABLE_COUNTER_ID + " integer primary key autoincrement," +
            TABLE_COUNTER_TEXT + "  text," +
            TABLE_COUNTER_COUNT + "  integer); ";
    static final String DATABASE_CREATE_TIMESTAMPS = "create table " + TABLE_TIMESTAMP + "( " +
            TABLE_TIMESTAMP_ID + " integer primary key autoincrement," +
            TABLE_TIMESTAMP_COUNTERID + "  integer,"
            + TABLE_TIMESTAMP_DELTA + "  integer," +
            TABLE_TIMESTAMP_TIME + "  integer); ";

    private static SQLiteDatabase db;
    private static DataBaseHelper dbHelper;

    DatabaseAdapter(Context context) {
        dbHelper = new DataBaseHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public DatabaseAdapter open() throws SQLException {
        if (db == null) {
            db = dbHelper.getWritableDatabase();
        }
        return this;
    }

    public void deleteCounterById(int id) {
        try {
            deleteTimeStampByCounterId(id);
            db.delete(TABLE_COUNTER, WHERE_COUNTERID_IS, new String[]{Integer.toString(id)});
        } catch (Exception ex) {
            Log.e(TABLE_COUNTER, ex.toString());
        }
    }

    public List<Counter> getAllCounter() {
        List<Counter> counters = new ArrayList<>();
        try {
            db = dbHelper.getReadableDatabase();
            Cursor cursor = db.query(TABLE_COUNTER, null, null, null, null, null, null);
            if (cursor.getCount() < 1) {
                return null;
            }
            cursor.moveToFirst();
            do {
                Counter counter = getCounterFromCursor(cursor);
                counters.add(counter);
            } while (cursor.moveToNext());
            cursor.close();
        } catch (Exception e) {
            Log.e(TABLE_COUNTER, e.toString());
        }
        return counters;
    }

    public void updateCounter(Counter counter) {
        try {
            ContentValues contentValues = getContentValuesFromCounter(counter);
            db.update(TABLE_COUNTER, contentValues, WHERE_COUNTERID_IS, new String[]{Long.toString(counter.getId())});
        } catch (Exception e) {
            Log.e(TABLE_COUNTER, e.toString());
        }
    }

    public long insertCounter(Counter counter) {
        try {
            ContentValues contentValues = getContentValuesFromCounter(counter);
            return db.insert(TABLE_COUNTER, null, contentValues);
        } catch (Exception e) {
            Log.e(TABLE_COUNTER, e.toString());
        }
        return -1;
    }

    public void insertTimeStamp(TimeStamp timeStamp) {
        try {
            ContentValues contentValues = getContentValuesFromTimeStamp(timeStamp);
            db.insert(TABLE_TIMESTAMP, null, contentValues);
        } catch (Exception e) {
            Log.e(TABLE_TIMESTAMP, e.toString());
        }
    }

    public List<TimeStamp> getAllTimeStampsByCounterId(int counterId) {
        List<TimeStamp> timeStamps = new ArrayList<>();
        try {
            db = dbHelper.getReadableDatabase();
            Cursor cursor = db.query(TABLE_TIMESTAMP, null, WHERE_TIMESTAMP_COUNTERID_IS, new String[]{Integer.toString(counterId)}, null, null, TABLE_TIMESTAMP_TIME+" ASC");
            if (cursor.getCount() < 1) {
                return null;
            }
            cursor.moveToFirst();
            do {
                TimeStamp timeStamp = getTimeStampFromCursor(cursor);
                timeStamps.add(timeStamp);
            } while (cursor.moveToNext());
            cursor.close();
        } catch (Exception e) {
            Log.e(TABLE_TIMESTAMP, e.toString());
        }
        return timeStamps;
    }

    public void deleteTimeStampByCounterId(int id) {
        try {
            long amount = db.delete(TABLE_TIMESTAMP, WHERE_TIMESTAMP_COUNTERID_IS, new String[]{Integer.toString(id)});
            Log.d("deleteTimeStampByCounte", "amount="+amount);
        } catch (Exception ex) {
            Log.e(TABLE_COUNTER, ex.toString());
        }
    }

    private Counter getCounterFromCursor(Cursor cursor) {
        Integer id = cursor.getInt(cursor.getColumnIndex(TABLE_COUNTER_ID));
        String text = cursor.getString(cursor.getColumnIndex(TABLE_COUNTER_TEXT));
        Integer count = cursor.getInt(cursor.getColumnIndex(TABLE_COUNTER_COUNT));
        return new Counter(id, text, count);
    }

    private ContentValues getContentValuesFromCounter(Counter counter) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TABLE_COUNTER_TEXT, counter.getText());
        contentValues.put(TABLE_COUNTER_COUNT, counter.getCount());
        return contentValues;
    }

    private TimeStamp getTimeStampFromCursor(Cursor cursor) {
        Integer id = cursor.getInt(cursor.getColumnIndex(TABLE_TIMESTAMP_ID));
        Integer counterId = cursor.getInt(cursor.getColumnIndex(TABLE_TIMESTAMP_COUNTERID));
        Integer delta = cursor.getInt(cursor.getColumnIndex(TABLE_TIMESTAMP_DELTA));
        Long time = cursor.getLong(cursor.getColumnIndex(TABLE_TIMESTAMP_TIME));
        return new TimeStamp(id, counterId, delta, time);
    }

    private ContentValues getContentValuesFromTimeStamp(TimeStamp timeStamp) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TABLE_TIMESTAMP_COUNTERID, timeStamp.getCounterId());
        contentValues.put(TABLE_TIMESTAMP_DELTA, timeStamp.getDelta());
        contentValues.put(TABLE_TIMESTAMP_TIME, timeStamp.getTime());
        return contentValues;
    }

}
