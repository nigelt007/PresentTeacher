package com.avatar.presentteacher;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import android.util.Log;

import com.avatar.presentteacher.data.AttendanceContract;
import com.avatar.presentteacher.data.AttendanceDbHelper;

/**
 * Created by nigthoma on 9/8/2014.
 */
public class TestQueryDb extends AndroidTestCase {
    private static final String LOG_TAG = TestQueryDb.class.getSimpleName();

    public void testQueryClass(){

        AttendanceDbHelper dbHelper = new AttendanceDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // A cursor is your primary interface to the query results.
        Cursor cursor = db.query(
                AttendanceContract.ClassEntry.TABLE_NAME,  // Table to Query
                null, // all columns
                null, // Columns for the "where" clause
                null, // Values for the "where" clause
                null, // columns to group by
                null, // columns to filter by row groups
                null // sort order
        );

        int rowCount = cursor.getCount();
        if(rowCount != 0) {
            for (int i = 0; i < rowCount; i++) {
                if (cursor.moveToPosition(i)) {
                    int classIndex = cursor.getColumnIndex(AttendanceContract.ClassEntry.COLUMN_CLASS_NAME);
                    String className = cursor.getString(classIndex);

                    int semIndex = cursor.getColumnIndex(AttendanceContract.ClassEntry.COLUMN_SEM);
                    String semester = cursor.getString(semIndex);

                    int subIndex = cursor.getColumnIndex(AttendanceContract.ClassEntry.COLUMN_SUBJECT);
                    String subject = cursor.getString(subIndex);

                    int batchIndex = cursor.getColumnIndex(AttendanceContract.ClassEntry.COLUMN_BATCH);
                    String batch = cursor.getString(batchIndex);
                    Log.d(LOG_TAG,"Class Values "+className+semester+subject+batch );
                }
            }
        }
        else{
            fail("No values returned :( ");
        }
    }

    public void testQueryStudent(){

        AttendanceDbHelper dbHelper = new AttendanceDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // A cursor is your primary interface to the query results.
        Cursor studentCursor = db.query(
                AttendanceContract.StudentEntry.TABLE_NAME,  // Table to Query
                null, // all columns
                null, // Columns for the "where" clause
                null, // Values for the "where" clause
                null, // columns to group by
                null, // columns to filter by row groups
                null // sort order
        );

        int rowCount = studentCursor.getCount();
        if(rowCount != 0) {
            for (int i = 0; i < rowCount; i++) {
                if (studentCursor.moveToPosition(i)) {
                    int studentIdIndex = studentCursor.getColumnIndex(AttendanceContract.StudentEntry.COLUMN_STUDENT_ID);
                    long studentId = studentCursor.getLong(studentIdIndex);

                    int studClassIdIndex = studentCursor.getColumnIndex(AttendanceContract.StudentEntry.COLUMN_CLASS_KEY);
                    long studClassId = studentCursor.getLong(studClassIdIndex);

                    int rollNoIndex = studentCursor.getColumnIndex(AttendanceContract.StudentEntry.COLUMN_ROLL_NO);
                    long rollNo = studentCursor.getLong(rollNoIndex);

                    int nameIndex = studentCursor.getColumnIndex(AttendanceContract.StudentEntry.COLUMN_STUDENT_NAME);
                    String name = studentCursor.getString(nameIndex);

                    Log.d(LOG_TAG,"Student Values "+studentId+studClassId+rollNo+name );
                }
            }
        }
        else{
            fail("No values returned :( ");
        }

    }
}
