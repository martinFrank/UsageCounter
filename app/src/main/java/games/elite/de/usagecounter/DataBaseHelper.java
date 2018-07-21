package games.elite.de.usagecounter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataBaseHelper extends SQLiteOpenHelper {

    DataBaseHelper(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(UsageCounterDatabaseAdapter.DATABASE_CREATE);
        }catch(Exception e){
            Log.e("DataBaseHelper.onCreate",e.toString());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        Log.w("TaskDBAdapter", "Upgrading from version " +oldVersion + " to " +newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + "LOGIN");
        onCreate(db);
    }

}
