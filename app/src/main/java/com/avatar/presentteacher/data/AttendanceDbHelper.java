package com.avatar.presentteacher.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.avatar.presentteacher.data.AttendanceContract.AttendanceEntry;
import com.avatar.presentteacher.data.AttendanceContract.ClassEntry;
import com.avatar.presentteacher.data.AttendanceContract.StudentEntry;


/**
 * Created by nigthoma on 8/15/2014.
 */
public class AttendanceDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "attendance.db";



    public AttendanceDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_CLASS_TABLE =
                "CREATE TABLE "+ ClassEntry.TABLE_NAME+" ("+
                        ClassEntry._ID +" INTEGER PRIMARY KEY AUTOINCREMENT , " +

                        ClassEntry.COLUMN_CLASS_NAME + " TEXT NOT NULL, " +
                        ClassEntry.COLUMN_SEM + " TEXT NOT NULL," +
                        ClassEntry.COLUMN_SUBJECT + " TEXT NOT NULL, " +
                        ClassEntry.COLUMN_BATCH + " TEXT NOT NULL," +
                        " UNIQUE (" + ClassEntry.COLUMN_CLASS_NAME + ") ON CONFLICT REPLACE);";

        final String SQL_CREATE_STUDENT_TABLE =
                "CREATE TABLE "+ StudentEntry.TABLE_NAME+" ("+
                        StudentEntry._ID +" INTEGER PRIMARY KEY AUTOINCREMENT , "+

                        StudentEntry.COLUMN_STUDENT_ID + " TEXT NOT NULL," +
                        StudentEntry.COLUMN_ROLL_NO + " TEXT NOT NULL," +
                        StudentEntry.COLUMN_STUDENT_NAME + " TEXT NOT NULL, " +
                        StudentEntry.COLUMN_CLASS_KEY + " INTEGER NOT NULL, " +

                        // Set up the class column as a foreign key to class table.
                        " FOREIGN KEY (" + StudentEntry.COLUMN_CLASS_KEY + ") REFERENCES " +
                        ClassEntry.TABLE_NAME  + " (" +ClassEntry._ID  + "), " +

                        // UNIQUE constraint with IGNORE strategy
                        "UNIQUE (" + StudentEntry.COLUMN_STUDENT_NAME +") ON CONFLICT IGNORE"+
                        " );";

        final String SQL_CREATE_ATTENDANCE_TABLE =
                "CREATE TABLE "+ AttendanceEntry.TABLE_NAME+" ("+
                        AttendanceEntry._ID+" INTEGER PRIMARY KEY AUTOINCREMENT ,"+

                        AttendanceEntry.COLUMN_DATETEXT + " TEXT NOT NULL, " +
                        AttendanceEntry.COLUMN_CLASS_KEY + " INTEGER NOT NULL, " +
                        AttendanceEntry.COLUMN_STU_KEY +  " INTEGER NOT NULL, " +
                        AttendanceEntry.COLUMN_ATTENDANCE +  " TEXT NOT NULL, " +

                        // Set up the class column as a foreign key to class table.
                        " FOREIGN KEY (" + AttendanceEntry.COLUMN_CLASS_KEY + ") REFERENCES " +
                        ClassEntry.TABLE_NAME  + " (" +ClassEntry._ID  + "), " +

                        // Set up the student column as a foreign key to student table.
                        " FOREIGN KEY (" + AttendanceEntry.COLUMN_STU_KEY + ") REFERENCES " +
                        StudentEntry.TABLE_NAME  + " (" +StudentEntry._ID  + ") "+

                        // To assure the application have just one attendance entry per day
                        // per student, it's created a UNIQUE constraint with REPLACE strategy
                       // " UNIQUE (" + AttendanceEntry.COLUMN_DATETEXT + ", " +
                       // AttendanceEntry.COLUMN_STU_KEY + ") ON CONFLICT REPLACE);";
                        " );";


        sqLiteDatabase.execSQL(SQL_CREATE_CLASS_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_STUDENT_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_ATTENDANCE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ ClassEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ StudentEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ AttendanceEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);

    }
}
