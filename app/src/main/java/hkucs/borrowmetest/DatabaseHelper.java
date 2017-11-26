package hkucs.borrowmetest;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.Blob;
import java.util.ArrayList;

/*
https://www.androidhive.info/2013/09/android-sqlite-database-with-multiple-tables/
http://www.sqlitetutorial.net/sqlite-create-table/
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String LOG = "DatabaseHelper";
    private static final String DATABASE_NAME = "MyDatabase";
    private static final int DATABASE_VERSION = 1;

    //Tables Names
    private static final String TABLE_USERS = "users";
    private static final String TABLE_ITEMS = "items";
    private static final String TABLE_CATEGORIES = "categories";
    private static final String TABLE_ITEM_CATEGORIES = "item_categories";
    private static final String TABLE_USER_ITEMS = "user_items";



    //Users Table Column Names
    private static final String USER_id = "id";
    private static final String USER_first_name = "first_name";
    private static final String USER_last_name = "last_name";
    private static final String USER_address = "address";
    private static final String USER_email = "email";

    //Items Table Column Names
    private static final String ITEM_id = "id";
    private static final String ITEM_title = "title";
    private static final String ITEM_description = "description";
    private static final String ITEM_isAvailable = "isAvailable";
    private static final String ITEM_price= "price";
    private static final String ITEM_image = "image";

    //Categories Table Column Names
    private static final String CAT_id = "id";
    private static final String CAT_name = "name";

    //Item_Categories Table Column Names
    private static final String IC_I_id = "item_id";
    private static final String IC_C_id = "category_id";

    //Item_Categories Table Column Names
    private static final String UI_U_id = "user_id";
    private static final String UI_I_id = "item_id";


    //User_Items Table Column Names

    //Table Create Statements
    private static final String TABLE_CREATE_USERS =
            "CREATE TABLE " + TABLE_USERS + " ( " +
                    USER_id + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    USER_first_name + " TEXT NOT NULL, " +
                    USER_last_name + " TEXT NOT NULL, " +
                    USER_address + " TEXT, " +
                    USER_email + " TEXT NOT NULL UNIQUE ); ";

    private static final String TABLE_CREATE_ITEMS =
            "CREATE TABLE " + TABLE_ITEMS + " ( " +
                    ITEM_id + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    ITEM_title + " TEXT NOT NULL, " +
                    ITEM_description + " TEXT NOT NULL, " +
                    ITEM_isAvailable + " INTEGER, " +
                    ITEM_price + " FLOAT, " +
                    ITEM_image + " BLOB ); ";

    private static final String TABLE_CREATE_CATEGORIES =
            "CREATE TABLE " + TABLE_CATEGORIES + " ( " +
                    CAT_id + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    CAT_name + " TEXT NOT NULL UNIQUE ); ";

    private static final String TABLE_CREATE_ITEM_CATEGORIES =
            "CREATE TABLE " + TABLE_ITEM_CATEGORIES + " ( " +
                    IC_I_id + " INTEGER, " +
                    IC_C_id + " INTEGER," +
                    "PRIMARY KEY ("+IC_I_id+", "+IC_C_id+"),"+
                    "FOREIGN KEY ("+IC_I_id+") REFERENCES "+TABLE_ITEMS+"("+ITEM_id+"),"+
                    "FOREIGN KEY ("+IC_C_id+") REFERENCES "+TABLE_CATEGORIES+"("+CAT_id+")); ";
    ;

    private static final String TABLE_CREATE_USER_ITEMS =
            "CREATE TABLE " + TABLE_USER_ITEMS + " ( " +
                    UI_U_id + " INTEGER, " +
                    UI_I_id + " INTEGER, " +
                    "PRIMARY KEY ("+UI_U_id+", "+UI_I_id+"),"+
                    "FOREIGN KEY ("+UI_U_id+") REFERENCES "+TABLE_USERS+"("+USER_id+"),"+
                    "FOREIGN KEY ("+UI_I_id+") REFERENCES "+TABLE_ITEMS+"("+ITEM_id+")); ";

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE_USERS);
        db.execSQL(TABLE_CREATE_ITEMS);
        db.execSQL(TABLE_CREATE_CATEGORIES);
        db.execSQL(TABLE_CREATE_ITEM_CATEGORIES);
        db.execSQL(TABLE_CREATE_USER_ITEMS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIES);

        onCreate(db);
    }

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null,
                DATABASE_VERSION);
    }

    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }

    public long createUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(USER_first_name, user.getFirst_name());
        values.put(USER_last_name, user.getLast_name());
        values.put(USER_email, user.getEmail());
        values.put(USER_address, user.getAddress());

        // insert row
        long user_id = db.insert(TABLE_USERS, null, values);

        return user_id;
    }

    public User getUserById(int id){
        String selectQuery = "SELECT * FROM " + TABLE_USERS + " WHERE id = ?";

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, new String[]{Integer.toString(id)});
        User u = new User();

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                u.setId(c.getInt((c.getColumnIndex(USER_id))));
                u.setFirst_name(c.getString(c.getColumnIndex(USER_first_name)));
                u.setLast_name(c.getString(c.getColumnIndex(USER_last_name)));
                u.setEmail(c.getString(c.getColumnIndex(USER_email)));
                String address = c.getString(c.getColumnIndex(USER_address));
                if(!address.isEmpty()){
                    u.setAddress(address);
                }

            } while (c.moveToNext());
        }
        return u;
    }

    public User getUserByEmail(String email){
        String selectQuery = "SELECT * FROM " + TABLE_USERS + " WHERE email = ?";

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, new String[]{email});
        User u = new User();

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                u.setId(c.getInt((c.getColumnIndex(USER_id))));
                u.setFirst_name(c.getString(c.getColumnIndex(USER_first_name)));
                u.setLast_name(c.getString(c.getColumnIndex(USER_last_name)));
                u.setEmail(c.getString(c.getColumnIndex(USER_email)));
                String address = c.getString(c.getColumnIndex(USER_address));
                if(!address.isEmpty()){
                    u.setAddress(address);
                }

            } while (c.moveToNext());
        }
        return u;
    }

    public long createItem(RentItem item) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ITEM_title, item.getTitle());
        values.put(ITEM_description, item.getDescription());
        values.put(ITEM_price, item.getPricePerHour());
        values.put(ITEM_isAvailable, item.isAvailable());
        values.put(ITEM_image, item.getImage());

        // insert row
        long item_id = db.insert(TABLE_ITEMS, null, values);

        return item_id;
    }

    public ArrayList<RentItem> getAllItems(){
        String selectQuery = "SELECT * FROM " + TABLE_ITEMS;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        ArrayList<RentItem> items = new ArrayList<>();

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                RentItem i = new RentItem();
                i.setId(c.getInt((c.getColumnIndex(ITEM_id))));
                i.setTitle(c.getString(c.getColumnIndex(ITEM_title)));
                i.setDescription(c.getString(c.getColumnIndex(ITEM_description)));
                i.setPricePerHour(c.getFloat(c.getColumnIndex(ITEM_price)));
                i.setImage(c.getBlob(c.getColumnIndex(ITEM_image)));

                items.add(i);
                Log.e(LOG, i.getTitle());
            } while (c.moveToNext());
        }
        return items;
    }

    public RentItem getItemById(int id){
        String selectQuery = "SELECT * FROM " + TABLE_ITEMS + " WHERE id = ?";

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, new String[]{Integer.toString(id)});
        RentItem i = new RentItem();

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                i.setId(c.getInt((c.getColumnIndex(ITEM_id))));
                i.setTitle(c.getString(c.getColumnIndex(ITEM_title)));
                i.setDescription(c.getString(c.getColumnIndex(ITEM_description)));
                i.setPricePerHour(c.getFloat(c.getColumnIndex(ITEM_price)));
                i.setImage(c.getBlob(c.getColumnIndex(ITEM_image)));

            } while (c.moveToNext());
        }
        return i;
    }

    public long createCategory(Category cat) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(CAT_name, cat.getTitle());

        // insert row
        long category_id = db.insert(TABLE_CATEGORIES, null, values);

        return category_id;
    }
}

