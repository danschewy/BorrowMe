package hkucs.borrowmetest;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.ByteArrayOutputStream;
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
    private static final String USER_password = "password";

    //Items Table Column Names
    private static final String ITEM_id = "id";
    private static final String ITEM_title = "title";
    private static final String ITEM_description = "description";
    private static final String ITEM_isAvailable = "isAvailable";
    private static final String ITEM_price = "price";
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
                    USER_password + " TEXT, " +
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
                    "PRIMARY KEY (" + IC_I_id + ", " + IC_C_id + ")," +
                    "FOREIGN KEY (" + IC_I_id + ") REFERENCES " + TABLE_ITEMS + "(" + ITEM_id + ")," +
                    "FOREIGN KEY (" + IC_C_id + ") REFERENCES " + TABLE_CATEGORIES + "(" + CAT_id + ")); ";
    ;

    private static final String TABLE_CREATE_USER_ITEMS =
            "CREATE TABLE " + TABLE_USER_ITEMS + " ( " +
                    UI_U_id + " INTEGER, " +
                    UI_I_id + " INTEGER, " +
                    "PRIMARY KEY (" + UI_U_id + ", " + UI_I_id + ")," +
                    "FOREIGN KEY (" + UI_U_id + ") REFERENCES " + TABLE_USERS + "(" + USER_id + ")," +
                    "FOREIGN KEY (" + UI_I_id + ") REFERENCES " + TABLE_ITEMS + "(" + ITEM_id + ")); ";

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
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEM_CATEGORIES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER_ITEMS);

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
        values.put(USER_password, user.getPassword());

        // insert row
        long user_id = db.insert(TABLE_USERS, null, values);

        return user_id;
    }

    public User getUserById(int id) {
        String selectQuery = "SELECT * FROM " + TABLE_USERS + " WHERE "+ USER_id +" = ?";

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, new String[]{Integer.toString(id)});
        User u = new User();

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                u.setId(c.getInt(c.getColumnIndex(USER_id)));
                u.setFirst_name(c.getString(c.getColumnIndex(USER_first_name)));
                u.setLast_name(c.getString(c.getColumnIndex(USER_last_name)));
                u.setEmail(c.getString(c.getColumnIndex(USER_email)));
                u.setPassword(c.getString(c.getColumnIndex(USER_password)));
                String address = c.getString(c.getColumnIndex(USER_address));
                if (!address.isEmpty()) {
                    u.setAddress(address);
                }

            } while (c.moveToNext());
        }
        return u;
    }

    public User getUserByEmail(String email) {
        String selectQuery = "SELECT * FROM " + TABLE_USERS + " WHERE email = ?";

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, new String[]{email});
        User u = new User();

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                u = getUserById(c.getInt(c.getColumnIndex(USER_id)));
            } while (c.moveToNext());
        }
        return u;
    }

    public User getUserByEmailAndPassword(String email, String password){
        String selectQuery = "SELECT * FROM " + TABLE_USERS + " WHERE email = ? AND password = ?";

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, new String[]{email, password});
        User u = new User();

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                u = getUserById(c.getInt(c.getColumnIndex(USER_id)));
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

    public int updateItem(RentItem item) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ITEM_isAvailable, item.isAvailable());

        // updating row
        return db.update(TABLE_ITEMS, values, ITEM_id + " = ?",
                new String[] { String.valueOf(item.getId()) });
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
                items.add(getItemById(c.getInt(c.getColumnIndex(ITEM_id))));
            } while (c.moveToNext());
        }
        return items;
    }

    public ArrayList<RentItem> getItemsByCategory(int category_id){
        String selectQuery = "SELECT * FROM " + TABLE_ITEM_CATEGORIES + " WHERE " + IC_C_id + " = ?";

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, new String[]{Integer.toString(category_id)});
        ArrayList<RentItem> items = new ArrayList<>();

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                items.add(getItemById(c.getInt(c.getColumnIndex(IC_I_id))));
                Log.e(LOG, "item added to cat");
            } while (c.moveToNext());
        }
        return items;
    }

    public RentItem getItemById(int id) {
        String selectQuery = "SELECT * FROM " + TABLE_ITEMS + " WHERE id = ?";

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, new String[]{Integer.toString(id)});
        RentItem i = new RentItem();

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                i.setId(c.getInt(c.getColumnIndex(ITEM_id)));
                i.setTitle(c.getString(c.getColumnIndex(ITEM_title)));
                i.setDescription(c.getString(c.getColumnIndex(ITEM_description)));
                i.setPricePerHour(c.getFloat(c.getColumnIndex(ITEM_price)));
                i.setImage(c.getBlob(c.getColumnIndex(ITEM_image)));
                i.setCategoryId(getItemCategory(c.getInt(c.getColumnIndex(ITEM_id))).getId());
                i.setOwnerId(getItemUser(c.getColumnIndex(ITEM_id)).getId());
            } while (c.moveToNext());
        }
        return i;
    }

    public Category getItemCategory(int item_id){
        String selectQuery = "SELECT * FROM " + TABLE_ITEM_CATEGORIES + " WHERE " + IC_I_id + " = ?";

        Log.e(LOG, selectQuery);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, new String[]{Integer.toString(item_id)});
        Category r = new Category();

        if (c.moveToFirst()) {
            do {
                r = getCategoryById(c.getInt(c.getColumnIndex(IC_C_id)));
            } while (c.moveToNext());
        }
        return r;
    }

    public long createItemCategory(int item_id,int cat_id){
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(IC_I_id, item_id);
            values.put(IC_C_id, cat_id);

            // insert row
            long ic_id = db.insert(TABLE_ITEM_CATEGORIES, null, values);
            return ic_id;
        }



    public long createCategory(Category cat) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(CAT_name, cat.getTitle());
        // insert row
        long category_id = db.insert(TABLE_CATEGORIES, null, values);

        return category_id;
    }

    // get category by id
    public Category getCategoryById(int id) {
        String selectQuery = "SELECT * FROM " + TABLE_CATEGORIES + " WHERE " +CAT_id+" = ?";

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, new String[]{Integer.toString(id)});
        Category cat = new Category();

        if (c.moveToFirst()) {
            do {
                cat.setId(c.getInt(c.getColumnIndex(CAT_id)));
                cat.setTitle(c.getString(c.getColumnIndex(CAT_name)));
            } while (c.moveToNext());
        }
        return cat;
    }

    // get category by name
    public Category getCategoryByName(String name) {
        String selectQuery = "SELECT * FROM " + TABLE_CATEGORIES + " WHERE " + CAT_name + " = ?";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, new String[]{name});
        Category cat = new Category();

        if (c.moveToFirst()) {
            do {
                cat=getCategoryById(c.getInt(c.getColumnIndex(CAT_id)));
            } while (c.moveToNext());
        }
        return cat;
    }


    //get all categories
    public ArrayList<Category> getAllCategories(){
        String selectQuery = "SELECT * FROM " + TABLE_CATEGORIES;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery,null);
        ArrayList<Category> categories = new ArrayList<>();

        //loop through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                categories.add(getCategoryById(c.getInt(c.getColumnIndex(CAT_id))));
            } while (c.moveToNext());
        }
        return categories;
    }

    public void deleteAllCategories() {
        String deleteQuery = "DELETE FROM " + TABLE_CATEGORIES;

        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL(deleteQuery);
    }

    public long createUserItem(int user_id, int item_id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(UI_U_id, user_id);
        values.put(UI_I_id, item_id);

        // insert row
        long ui_id = db.insert(TABLE_USER_ITEMS, null, values);

        return ui_id;
    }

    public User getItemUser(int item_id) {
        String selectQuery = "SELECT * FROM " + TABLE_USER_ITEMS + " WHERE "+UI_I_id+" = ?";
        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, new String[]{Integer.toString(item_id)});
        User r = new User();

        if (c.moveToFirst()) {
            do {
                r = getUserById(c.getInt(c.getColumnIndex(UI_U_id)));
            } while (c.moveToNext());
        }
        return r;
    }

    public ArrayList<RentItem> getAllUserItems(int user_id) {
        String selectQuery = "SELECT * FROM " + TABLE_USER_ITEMS + " WHERE "+UI_U_id+" = ?";
        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, new String[]{Integer.toString(user_id)});
        ArrayList<RentItem> r = new ArrayList<>();

        if (c.moveToFirst()) {
            do {
                r.add(getItemById(c.getInt(c.getColumnIndex(UI_I_id))));
            } while (c.moveToNext());
        }
        return r;
    }

    public static byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

    // convert from byte array to bitmap
    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }
}


