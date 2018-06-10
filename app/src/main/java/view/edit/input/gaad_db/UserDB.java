package view.edit.input.gaad_db;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hp on 5/28/2018.
 */

public class UserDB extends SQLiteOpenHelper {

    static final int DB_VERSION = 1;
    static final String DB_NAME = "user_db";

    public static class UserInfoEntry implements BaseColumns {
        static final String TABLE_NAME = "UserInfo";

        static final String COLUMN_NAME = "User_Name";
        static final String COLUMN_ADDR = "User_Address";
        static final String COLUMN_DESG = "User_Designation";
    }

    public class UserPhoneEntry implements BaseColumns {
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


            return insertUserOperation(contentValues);
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

    public long insertContactOperation(ContentValues contentValues) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        return sqLiteDatabase.insert(UserPhoneEntry.TABLE_NAME,
                null, contentValues);
    }

    public long insertUserOperation(ContentValues contentValues) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        return sqLiteDatabase.insert(UserInfoEntry.TABLE_NAME,
                null, contentValues);
    }

    public int update(ContentValues contentValues, String where, String[] args) {

        int updateContact = updateContact(contentValues, where, args);

        if (updateContact > 0) {
            where = UserInfoEntry.COLUMN_NAME + "=?";
            return updateUserInfo(contentValues, where, args);
        }

        return 0;
    }

    private int updateContact(ContentValues contentValues, String where, String[] args) {

        SQLiteDatabase sqLiteDatabase = getWritableDatabase();

        ContentValues updateContentValues = new ContentValues();
        getDesireContentValues(updateContentValues, UserPhoneEntry.COLUMN_CON_NAME,
                contentValues.getAsString(UserPhoneEntry.COLUMN_CON_NAME));
        getDesireContentValues(updateContentValues, UserPhoneEntry.COLUMN_PHN,
                contentValues.getAsString(UserPhoneEntry.COLUMN_PHN));

        return sqLiteDatabase.update(UserPhoneEntry.TABLE_NAME, updateContentValues, where, args);

    }

    private int updateUserInfo(ContentValues contentValues, String where, String[] args) {

        SQLiteDatabase sqLiteDatabase = getWritableDatabase();


        ContentValues userContent = new ContentValues();
        getDesireContentValues(userContent, UserInfoEntry.COLUMN_NAME,
                contentValues.getAsString(UserInfoEntry.COLUMN_NAME));
        getDesireContentValues(userContent, UserInfoEntry.COLUMN_DESG,
                contentValues.getAsString(UserInfoEntry.COLUMN_DESG));
        getDesireContentValues(userContent, UserInfoEntry.COLUMN_ADDR,
                contentValues.getAsString(UserInfoEntry.COLUMN_ADDR));

        return sqLiteDatabase.update(UserInfoEntry.TABLE_NAME, userContent, where, args);

    }

    private ContentValues getDesireContentValues(ContentValues contentValues,
                                                 String key, String value) {

        if (!TextUtils.isEmpty(value)) {
            contentValues.put(key, value);
        }
        return contentValues;
    }

    public int delete(String whereClause, String[] whereValues) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        int deleteContact = sqLiteDatabase.delete(UserPhoneEntry.TABLE_NAME,
                whereClause, whereValues);

        if (deleteContact > 0) {
            whereClause = UserInfoEntry.COLUMN_NAME + "=?";
            return sqLiteDatabase.delete(UserInfoEntry.TABLE_NAME, whereClause, whereValues);
        }

        return 0;
    }

    private long insertContact(UserInfoModel userInfoModel) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(UserPhoneEntry.COLUMN_CON_NAME, userInfoModel.getUserName());
        contentValues.put(UserPhoneEntry.COLUMN_PHN, userInfoModel.getUserContactNumber());

        return insertContactOperation(contentValues);
    }
}
