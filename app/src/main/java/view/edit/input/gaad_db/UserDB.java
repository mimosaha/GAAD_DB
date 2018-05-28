package view.edit.input.gaad_db;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

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

    public long insert(UserInfoModel userInfoModel) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(UserInfoEntry.COLUMN_NAME, userInfoModel.getUserName());
        contentValues.put(UserInfoEntry.COLUMN_ADDR, userInfoModel.getUserAddress());
        contentValues.put(UserInfoEntry.COLUMN_DESG, userInfoModel.getUserDesignation());
        contentValues.put(UserPhoneEntry.COLUMN_PHN, userInfoModel.getUserContactNumber());

        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        long dbID = sqLiteDatabase.insert(UserInfoEntry.TABLE_NAME, null, contentValues);

        return dbID;
    }
}
