package com.avatar.presentteacher.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

/**
 * Created by nigthoma on 8/16/2014.
 */
public class AttendanceProvider extends ContentProvider {

    private static final String LOG_TAG = AttendanceProvider.class.getSimpleName();
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private AttendanceDbHelper mOpenHelper;
    private static final SQLiteQueryBuilder sAttendanceByDateQueryBuilder;
    private static final SQLiteQueryBuilder sStudentsByClassQueryBuilder;
    static{
        sAttendanceByDateQueryBuilder = new SQLiteQueryBuilder();
        sAttendanceByDateQueryBuilder.setTables(
                AttendanceContract.AttendanceEntry.TABLE_NAME + " INNER JOIN " +
                        AttendanceContract.StudentEntry.TABLE_NAME +
                        " ON " + AttendanceContract.AttendanceEntry.TABLE_NAME +
                        "." + AttendanceContract.AttendanceEntry.COLUMN_CLASS_KEY +
                        " = " + AttendanceContract.StudentEntry.TABLE_NAME +
                        "." + AttendanceContract.StudentEntry._ID);
    }
    static{
        sStudentsByClassQueryBuilder = new SQLiteQueryBuilder();
        sStudentsByClassQueryBuilder.setTables(
                AttendanceContract.StudentEntry.TABLE_NAME + " INNER JOIN " +
                        AttendanceContract.ClassEntry.TABLE_NAME +
                        " ON " + AttendanceContract.StudentEntry.TABLE_NAME +
                        "." + AttendanceContract.StudentEntry.COLUMN_CLASS_KEY +
                        " = " + AttendanceContract.ClassEntry.TABLE_NAME +
                        "." + AttendanceContract.ClassEntry._ID);
    }

    private static final String sLocationSettingAndDaySelection =
            AttendanceContract.ClassEntry.TABLE_NAME +
                    "." + AttendanceContract.StudentEntry.COLUMN_STUDENT_ID + " = ? AND " +
                    AttendanceContract.AttendanceEntry.COLUMN_DATETEXT + " = ? ";


    private static final int ATTENDANCE = 100;
    private static final int ATTENDANCE_WITH_CLASS = 101;
    private static final int ATTENDANCE_WITH_CLASS_AND_STUDENT = 102;
    private static final int ATTENDANCE_WITH_STUDENT_AND_DATE = 103;
    private static final int CLASS = 300;
    private static final int CLASS_ID = 301;
    private static final int STUDENT = 400;
    private static final int STUDENT_ID = 401;
    private static final int STUDENTS_WITH_CLASS=402;

    private Cursor getAttendanceByStudentAndDate(
            Uri uri, String[] projection, String sortOrder) {
        String student =AttendanceContract.AttendanceEntry.getStudentFromUri(uri);
        String date = AttendanceContract.AttendanceEntry.getDateFromUri(uri);

        return sAttendanceByDateQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                sLocationSettingAndDaySelection,
                new String[]{student, date},
                null,
                null,
                sortOrder
        );
    }

    private Cursor getAttendanceByStudent(Uri uri, String[] projection, String sortOrder) {
        String studentId = AttendanceContract.AttendanceEntry.getStudentFromUri(uri);
        String selection = null;
        if (studentId != null) {
            selection = studentId;
        }
        return sAttendanceByDateQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                selection,
                null,
                null,
                null,
                sortOrder
        );
    }

    private static final String sStudentByClassName = AttendanceContract.StudentEntry.TABLE_NAME+
            "."+AttendanceContract.StudentEntry.COLUMN_CLASS_KEY+ " = ?";

    private Cursor getStudentsByClass(Uri uri, String[] projection, String sortOrder){
        String classId = AttendanceContract.StudentEntry.getClassFromUri(uri);
        Log.v(LOG_TAG,"Attendance Provider Class Id "+classId);
        String[] selectionArgs = new String[0];
        String selection = null;
        if (classId != null){
            selection =sStudentByClassName;
            selectionArgs=  new String[] {classId};
        }
        return  sStudentsByClassQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }

    private static UriMatcher buildUriMatcher() {
        // I know what you're thinking.  Why create a UriMatcher when you can use regular
        // expressions instead?  Because you're not crazy, that's why.

        // All paths added to the UriMatcher have a corresponding code to return when a match is
        // found.  The code passed into the constructor represents the code to return for the root
        // URI.  It's common to use NO_MATCH as the code for this case.
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = AttendanceContract.CONTENT_AUTHORITY;

        // For each type of URI you want to add, create a corresponding code.
        matcher.addURI(authority, AttendanceContract.PATH_ATTENDANCE, ATTENDANCE);
        matcher.addURI(authority, AttendanceContract.PATH_ATTENDANCE + "/*", ATTENDANCE_WITH_CLASS);
        matcher.addURI(authority, AttendanceContract.PATH_ATTENDANCE + "/*/*", ATTENDANCE_WITH_CLASS_AND_STUDENT);
        matcher.addURI(authority, AttendanceContract.PATH_ATTENDANCE + "/*/*", ATTENDANCE_WITH_STUDENT_AND_DATE);

        matcher.addURI(authority, AttendanceContract.PATH_CLASS, CLASS);
        matcher.addURI(authority, AttendanceContract.PATH_CLASS + "/*", CLASS_ID);

        matcher.addURI(authority, AttendanceContract.PATH_STUDENT, STUDENT);
        matcher.addURI(authority, AttendanceContract.PATH_STUDENT + "/*", STUDENT_ID);
        matcher.addURI(authority, AttendanceContract.PATH_STUDENT +"/*",CLASS_ID);
        matcher.addURI(authority, AttendanceContract.PATH_STUDENT+"/*",STUDENTS_WITH_CLASS);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new AttendanceDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        // Here's the switch statement that, given a URI, will determine what kind of request it is,
        // and query the database accordingly.
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            // "attendance/*/*"
            case ATTENDANCE_WITH_STUDENT_AND_DATE:
            {
                retCursor = getAttendanceByStudentAndDate(uri, projection, sortOrder);
                break;
            }
            // "attendance/*"
            case ATTENDANCE_WITH_CLASS_AND_STUDENT: {
                retCursor = getAttendanceByStudent(uri, projection, sortOrder);
                break;
            }
            case CLASS: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        AttendanceContract.ClassEntry.TABLE_NAME,
                        projection,//columns to be returned in the result set
                        selection, // selection like where class = 'className' or something
                        selectionArgs, //selectionArgs
                        null,         // groupBy
                        null,         //having
                        sortOrder     // sort order
                );
                break;
            }
            case STUDENT: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        AttendanceContract.StudentEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case CLASS_ID: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        AttendanceContract.StudentEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case STUDENTS_WITH_CLASS:
            {
                retCursor = getStudentsByClass(uri,projection,sortOrder);
                break;
            }


            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case ATTENDANCE_WITH_STUDENT_AND_DATE:
                return AttendanceContract.AttendanceEntry.CONTENT_ITEM_TYPE;
            case ATTENDANCE_WITH_CLASS_AND_STUDENT:
                return AttendanceContract.AttendanceEntry.CONTENT_TYPE;
            case ATTENDANCE_WITH_CLASS:
                return AttendanceContract.AttendanceEntry.CONTENT_TYPE;
            case ATTENDANCE:
                return AttendanceContract.AttendanceEntry.CONTENT_TYPE;
            case CLASS:
                return AttendanceContract.ClassEntry.CONTENT_TYPE;
            case CLASS_ID:
                return AttendanceContract.ClassEntry.CONTENT_ITEM_TYPE;
            case STUDENT_ID:
                return AttendanceContract.StudentEntry.CONTENT_ITEM_TYPE;
            case STUDENT:
                return AttendanceContract.StudentEntry.CONTENT_TYPE;
            case STUDENTS_WITH_CLASS:
                return AttendanceContract.StudentEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case ATTENDANCE: {
                long _id = db.insert(AttendanceContract.AttendanceEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = AttendanceContract.AttendanceEntry.buildAttendanceUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case STUDENT: {
                long _id = db.insert(AttendanceContract.StudentEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = AttendanceContract.StudentEntry.buildStudentUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case CLASS: {
                long _id = db.insert(AttendanceContract.ClassEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = AttendanceContract.ClassEntry.buildClassUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;
        switch (match) {
            case ATTENDANCE:
                rowsDeleted = db.delete(
                        AttendanceContract.AttendanceEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case STUDENT:
                rowsDeleted = db.delete(
                        AttendanceContract.StudentEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case CLASS:
                rowsDeleted = db.delete(
                        AttendanceContract.ClassEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        // Because a null deletes all rows
        if (selection == null || rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case ATTENDANCE:
                rowsUpdated = db.update(AttendanceContract.AttendanceEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case STUDENT:
                rowsUpdated = db.update(AttendanceContract.StudentEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case CLASS:
                rowsUpdated = db.update(AttendanceContract.ClassEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case ATTENDANCE:
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(AttendanceContract.AttendanceEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            default:
                return super.bulkInsert(uri, values);
        }
    }
}
