package ru.aim.anotheryetbashclient.helper;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Random;

@SuppressWarnings("unused")
public class DbHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "quote_db";
    public static final String QUOTE_TABLE = "quotes";
    public static final String QUOTE_ID = "_id";
    public static final String QUOTE_PUBLIC_ID = "quote_public_id";
    public static final String QUOTE_DATE = "quote_date";
    public static final String QUOTE_IS_NEW = "quote_is_new";
    public static final String QUOTE_TEXT = "quote_text";

    public DbHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + QUOTE_TABLE +
                " (" + QUOTE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                QUOTE_PUBLIC_ID + " TEXT, " + QUOTE_DATE + " TEXT, " +
                QUOTE_IS_NEW + " INTEGER, " +
                QUOTE_TEXT + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
    }

    public void markRead(long innerId) {
        SQLiteDatabase db = getWritableDatabase();
        assert db != null;
        db.execSQL("update " + QUOTE_TABLE + " set " + QUOTE_IS_NEW + " = 0 where " + QUOTE_ID + " = " + innerId);
    }

    public Cursor getUnread() {
        SQLiteDatabase database = getReadableDatabase();
        assert database != null;
        Cursor cursor = database.rawQuery("select * from " + QUOTE_TABLE + " where " + QUOTE_IS_NEW + " = ? limit 10", new String[]{"1"});
        if (cursor.getCount() < 10) {
            // todo: if we have no unread quotes then we need return old stuff
            if (cursor.getCount() > 0) {
                long[] arr = new long[cursor.getCount()];
                int counter = 0;
                while (cursor.moveToNext()) {
                    arr[counter] = cursor.getLong(cursor.getColumnIndex(QUOTE_ID));
                }
                cursor.close();
                StringBuilder sb = new StringBuilder("(");
                for (int i = 0; i < arr.length; i++) {
                    sb.append(Long.toString(arr[i]));
                    if (i + 1 != arr.length) {
                        sb.append(",");
                    }
                }
                sb.append(")");
                Cursor cursor1 = database.rawQuery("select _id from " + QUOTE_TABLE + " where _id not in " + sb.toString(), null);
                long[] tmpArr = new long[cursor1.getCount()];
                for (int i = 0; i < tmpArr.length; i++) {
                    tmpArr[i] = cursor1.getLong(cursor1.getColumnIndex(QUOTE_ID));
                }
                cursor1.close();
                long[] result = new long[10];
                counter = 0;
                for (int i = 0; i < arr.length; i++) {
                    result[i] = arr[i];
                    counter++;
                }
                Random random = new Random();
                int justForCheck = 0;
                while (counter != result.length) {
                    long tmpId = tmpArr[random.nextInt(tmpArr.length)];
                    if (!Utils.contains(result, tmpId)) {
                        result[counter] = tmpId;
                        counter++;
                    }
                    justForCheck++;
                    if (justForCheck == counter + 20) {
                        // if something bad happened
                        // we will spinning only 20+ times
                        break;
                    }
                }
                sb.delete(1, sb.length());
                for (int i = 0; i < result.length; i++) {
                    sb.append(Long.toString(result[i]));
                    if (i + 1 != result.length) {
                        sb.append(",");
                    }
                }
                sb.append(")");
                //noinspection UnnecessaryLocalVariable
                Cursor resultCursor = database.rawQuery("select * from " + QUOTE_TABLE + " where _id in " + sb.toString(), null);
                return resultCursor;
            } else {
            }
            return cursor;
        } else {
            return cursor;
        }
    }
}
