package view.edit.input.gaad_db;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hp on 5/28/2018.
 */

public class UserDB extends SQLiteOpenHelper {

    static final int DB_VERSION = 1;
    static final String DB_NAME = "user_db";

    public class UserInfoEntry implements BaseColumns {
        static final String TABLE_NAME = "UserInfo";

        static final String COLUMN_NAME = "User_Name";
        static final String COLUMN_ADDR = "User_Address";
        static final String COLUMN_DESG = "User_Designation";
    }

    private class UserPhoneEntry implements BaseColumns {
        static final String TABLE_NAME = "UserPhone";

        static final String COLUMN_CON_NAME = "User_Con_Name";
        static final String COLUMN_PHN = "User_Phone";
    }

    private String CREATE_USER_INFO_TABLE = "CREATE TABLE " + UserInfoEntry.TABLE_NAME +
            " (" + UserInfoEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            UserInfoEntry.COLUMN_NAME + " TEXT, " +
            UserInfoEntry.COLUMN_ADDR + " TEXT, " +
            UserInfoEntry.COLUMN_DESG + " TEXT, " +
            "FOREIGN KEY (" + UserInfoEntry.COLUMN_NAME + ") REFERENCES "
            + UserPhoneEntry.TABLE_NAME + "(" + UserInfoEntry.COLUMN_NAME+") " + ")";

    private String CREATE_USER_PHONE_TABLE = "CREATE TABLE " + UserPhoneEntry.TABLE_NAME +
            " (" + UserPhoneEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            UserPhoneEntry.COLUMN_CON_NAME + " TEXT, " +
            UserPhoneEntry.COLUMN_PHN + " TEXT)";

    public UserDB(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_USER_PHONE_TABLE);
        sqLiteDatabase.execSQL(CREATE_USER_INFO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public long insertAll(UserInfoModel userInfoModel) {

        long phoneContact = insertContact(userInfoModel);

        if (phoneContact != -1) {
            ContentValues contentValues = new ContentValues();

            contentValues.put(UserInfoEntry.COLUMN_NAME, userInfoModel.getUserName());
            contentValues.put(UserInfoEntry.COLUMN_ADDR, userInfoModel.getUserAddress());
            contentValues.put(UserInfoEntry.COLUMN_DESG, userInfoModel.getUserDesignation());

            SQLiteDatabase sqLiteDatabase = getWritableDatabase();
            return sqLiteDatabase.insert(UserInfoEntry.TABLE_NAME,
                    null, contentValues);
        }

        return phoneContact;
    }

    Cursor getAllUserCursor() {

        SQLiteDatabase sqLiteDatabase = getReadableDatabase();

        String query = "SELECT " + UserPhoneEntry.COLUMN_CON_NAME + ","
                + UserPhoneEntry.COLUMN_PHN + "," + UserInfoEntry.COLUMN_ADDR
                + "," + UserInfoEntry.TABLE_NAME + "." + UserInfoEntry._ID
                + " FROM " + UserInfoEntry.TABLE_NAME + " INNER JOIN "
                + UserPhoneEntry.TABLE_NAME + " ON "
                + UserPhoneEntry.TABLE_NAME + "." + UserPhoneEntry.COLUMN_CON_NAME + " = "
                + UserInfoEntry.TABLE_NAME + "." + UserInfoEntry.COLUMN_NAME;

        return sqLiteDatabase.rawQuery(query, null);
    }

    List<UserInfoModel> getAllUsers() {

        List<UserInfoModel> userInfoModels = new ArrayList<>();

        Cursor cursor = getAllUserCursor();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String userName = cursor.getString(
                        cursor.getColumnIndexOrThrow(UserPhoneEntry.COLUMN_CON_NAME));
                String userPhn = cursor.getString(
                        cursor.getColumnIndexOrThrow(UserPhoneEntry.COLUMN_PHN));
                String userAddress = cursor.getString(
                        cursor.getColumnIndexOrThrow(UserInfoEntry.COLUMN_ADDR));

                int id = cursor.getInt(cursor.getColumnIndexOrThrow(UserInfoEntry._ID));

                UserInfoModel userInfoModel = new UserInfoModel()
                        .setUserName(userName).setUserContactNumber(userPhn)
                        .setUserAddress(userAddress).setUserId(id);

                userInfoModels.add(userInfoModel);
            } while (cursor.moveToNext());
        }

        if (cursor != null) {
            cursor.close();
        }

        return userInfoModels;
    }

    public long insert(UserInfoModel userInfoModel) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(UserPhoneEntry.COLUMN_CON_NAME, userInfoModel.getUserName());
        contentValues.put(UserPhoneEntry.COLUMN_PHN, userInfoModel.getUserContactNumber());

        contentValues.put(UserInfoEntry.COLUMN_NAME, userInfoModel.getUserName());
        contentValues.put(UserInfoEntry.COLUMN_ADDR, userInfoModel.getUserAddress());
        contentValues.put(UserInfoEntry.COLUMN_DESG, userInfoModel.getUserDesignation());

        return insertOperation(contentValues);
    }

    public long insertOperation(ContentValues contentValues) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        long contactId = sqLiteDatabase.insert(UserPhoneEntry.TABLE_NAME,
                null, contentValues);

        if (contactId != -1) {
            return sqLiteDatabase.insert(UserInfoEntry.TABLE_NAME,
                    null, contentValues);
        }

        return contactId;
    }

    public int update(ContentValues contentValues, String s, String[] strings) {
        return 0;
    }

    public int delete(String whereClause, String[] whereValues) {
        return 0;
    }

    private long insertContact(UserInfoModel userInfoModel) {

        SQLiteDatabase sqLiteDatabase = getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(UserPhoneEntry.COLUMN_CON_NAME, userInfoModel.getUserName());
        contentValues.put(UserPhoneEntry.COLUMN_PHN, userInfoModel.getUserContactNumber());

        return sqLiteDatabase.insert(UserPhoneEntry.TABLE_NAME,
                null, contentValues);
    }
}
