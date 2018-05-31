package view.edit.input.gaad_db;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by hp on 5/31/2018.
 */

public class UserInfoContentProvider extends ContentProvider {

    public static final String AUTHORITY = "view.edit.input.gaad_db";

    private static final String PATH_ALL_CONTACTS = "PATH_ALL_CONTACTS";
    private static final String PATH_CONTACTS = "PATH_CONTACTS";

    private static final String MIME_TYPE_ALL_CONTACT = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + "vnd.com.contentprovider.allcontact";
    private static final String MIME_TYPE_CONTACT = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + "vnd.com.contentprovider.contact";

    private static final int USER_ALL_CONTACT = 1;
    private static final int USER_CONTACT = 2;

    private UserDB userDB;
    private static UriMatcher MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        MATCHER.addURI(AUTHORITY, PATH_ALL_CONTACTS, USER_ALL_CONTACT);
        MATCHER.addURI(AUTHORITY, PATH_CONTACTS, USER_CONTACT);
    }

    @Override
    public boolean onCreate() {
        userDB = new UserDB(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings,
                        @Nullable String s, @Nullable String[] strings1,
                        @Nullable String s1) {

        Cursor cursor = null;

        switch (MATCHER.match(uri)) {
            case USER_ALL_CONTACT:
                cursor = userDB.getAllUserCursor();
                break;

            case USER_CONTACT:

                break;
        }

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (MATCHER.match(uri)) {
            case USER_ALL_CONTACT:
                return MIME_TYPE_ALL_CONTACT;

            case USER_CONTACT:
                return MIME_TYPE_CONTACT;
        }
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        switch (MATCHER.match(uri)) {
            case USER_CONTACT:
                return insertToDo(uri, contentValues);
        }
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        int deleteCount = -1;
        switch (MATCHER.match(uri)) {
            case USER_CONTACT:
                deleteCount = delete(s, strings);
                break;
        }
        return deleteCount;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues,
                      @Nullable String s, @Nullable String[] strings) {
        int updateCount = -1;
        switch (MATCHER.match(uri)) {
            case USER_ALL_CONTACT:
                updateCount = update(contentValues, s, strings);
                break;
        }
        return updateCount;
    }

    private Uri insertToDo(Uri uri, ContentValues contentValues) {
        long id = userDB.insertOperation(contentValues);
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse("content://" + AUTHORITY + "/" + PATH_CONTACTS + "/" + id);
    }

    private int update(ContentValues contentValues, String whereCluase, String[] strings) {
        return userDB.update(contentValues, whereCluase, strings);
    }

    private int delete(String whereClause, String[] whereValues) {
        return userDB.delete(whereClause, whereValues);
    }
}
