package hkucs.borrowmetest;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MySQLiteOpenHelper extends SQLiteOpenHelper {


    private static final String DATABASE_NAME = "ItemsUsers";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "USERS";
    private static final String COL_id = "id";
    private static final String COL_uid = "uid";
    private static final String COL_name = "name";
    private static final String COL_contact = "contact";
    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_NAME + " ( " +
                    COL_id + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COL_uid + " TEXT NOT NULL, " +
                    COL_name + " TEXT NOT NULL, " +
                    COL_contact + " TEXT ); ";

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //implement
    }

    public MySQLiteOpenHelper(Context context) {
        super(context, DATABASE_NAME, null,
                DATABASE_VERSION);
    }
}

