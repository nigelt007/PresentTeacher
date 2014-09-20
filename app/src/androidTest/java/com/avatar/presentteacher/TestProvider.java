package com.avatar.presentteacher;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import android.util.Log;

import com.avatar.presentteacher.data.AttendanceContract.AttendanceEntry;
import com.avatar.presentteacher.data.AttendanceContract.ClassEntry;
import com.avatar.presentteacher.data.AttendanceContract.StudentEntry;
import com.avatar.presentteacher.data.AttendanceDbHelper;

/**
 * Created by nigthoma on 8/15/2014.
 */
public class TestProvider extends AndroidTestCase {

    public static final String LOG_TAG = TestProvider.class.getSimpleName();

    public void testDeleteDb() throws Throwable {
        mContext.deleteDatabase(AttendanceDbHelper.DATABASE_NAME);
    }


    public void testInsertReadDb(){
        String testClass ="S8B";
        String testSem = "8";
        String testSub = "Project";
        String testBatch = "2011";

        AttendanceDbHelper dbHelper = new AttendanceDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues testValues = new ContentValues();
        testValues.put(ClassEntry.COLUMN_CLASS_NAME, testClass);
        testValues.put(ClassEntry.COLUMN_SEM, testSem);
        testValues.put(ClassEntry.COLUMN_SUBJECT, testSub);
        testValues.put(ClassEntry.COLUMN_BATCH, testBatch);

        long classId ;
        classId = db.insert(ClassEntry.TABLE_NAME, null,testValues);

        assertTrue(classId != -1);
        Log.d(LOG_TAG, "New row id :" +classId);


        // A cursor is your primary interface to the query results.
        Cursor cursor = db.query(
                ClassEntry.TABLE_NAME,  // Table to Query
                null, // all columns
                null, // Columns for the "where" clause
                null, // Values for the "where" clause
                null, // columns to group by
                null, // columns to filter by row groups
                null // sort order
        );

        if(cursor.moveToFirst()){
            int classIndex = cursor.getColumnIndex(ClassEntry.COLUMN_CLASS_NAME);
            String className = cursor.getString(classIndex);

            int semIndex = cursor.getColumnIndex(ClassEntry.COLUMN_SEM);
            String semester = cursor.getString(semIndex);

            int subIndex = cursor.getColumnIndex(ClassEntry.COLUMN_SUBJECT);
            String subject = cursor.getString(subIndex);

            int batchIndex = cursor.getColumnIndex(ClassEntry.COLUMN_BATCH);
            String batch = cursor.getString(batchIndex);

            assertEquals(testClass,className);
            assertEquals(testSem,semester);
            assertEquals(testSub,subject);
            assertEquals(testBatch,batch);
        }

        else{
            fail("No values returned :( ");
        }

        ContentValues studentValues = createStudentValues(classId);
        long studentRowId ;
        studentRowId = db.insert(StudentEntry.TABLE_NAME,null,studentValues);

        assertTrue(studentRowId != -1);
        Log.d(LOG_TAG, "New Student row id :" +studentRowId);

        // A cursor is your primary interface to the query results.
        Cursor studentCursor = db.query(
                StudentEntry.TABLE_NAME,  // Table to Query
                null, // all columns
                null, // Columns for the "where" clause
                null, // Values for the "where" clause
                null, // columns to group by
                null, // columns to filter by row groups
                null // sort order
        );

        if(studentCursor.moveToFirst()){
            int studentIdIndex = studentCursor.getColumnIndex(StudentEntry.COLUMN_STUDENT_ID);
            long studentId = studentCursor.getLong(studentIdIndex);

            int studClassIdIndex = studentCursor.getColumnIndex(StudentEntry.COLUMN_CLASS_KEY);
            long studClassId = studentCursor.getLong(studClassIdIndex);

            int rollNoIndex = studentCursor.getColumnIndex(StudentEntry.COLUMN_ROLL_NO);
            long rollNo = studentCursor.getLong(rollNoIndex);

            int nameIndex = studentCursor.getColumnIndex(StudentEntry.COLUMN_STUDENT_NAME);
            String name = studentCursor.getString(nameIndex);

            assertEquals(1234,studentId);
            assertEquals(classId,studClassId);
            assertEquals(1,rollNo);
            assertEquals("Nigel",name);
        }

        else{
            fail("No values returned :( ");
        }

        ContentValues attendanceValues = createAttendanceValues(classId,studentRowId);

        long AttendanceId ;
        AttendanceId = db.insert(AttendanceEntry.TABLE_NAME,null,attendanceValues);

        assertTrue(AttendanceId != -1);
        Log.d(LOG_TAG, "New Attendance row id :" +studentRowId);

        // A cursor is your primary interface to the query results.
        Cursor attendCursor = db.query(
                AttendanceEntry.TABLE_NAME,  // Table to Query
                null, // all columns
                null, // Columns for the "where" clause
                null, // Values for the "where" clause
                null, // columns to group by
                null, // columns to filter by row groups
                null // sort order
        );

        if(attendCursor.moveToFirst()){
            int dateIndex = attendCursor.getColumnIndex(AttendanceEntry.COLUMN_DATETEXT);
            String date = attendCursor.getString(dateIndex);

            int ClassIdIndex = attendCursor.getColumnIndex(AttendanceEntry.COLUMN_CLASS_KEY);
            long studClass = attendCursor.getLong(ClassIdIndex);

            int stuIndex = attendCursor.getColumnIndex(AttendanceEntry.COLUMN_STU_KEY);
            long student = attendCursor.getLong(stuIndex);

            int attIndex = attendCursor.getColumnIndex(AttendanceEntry.COLUMN_ATTENDANCE);
            String attend = attendCursor.getString(attIndex);

            assertEquals("20141205",date);
            assertEquals(classId,studClass);
            assertEquals(studentRowId,student);
            assertEquals("Present",attend);
        }

        else{
            fail("No values returned :( ");
        }

    }

    static ContentValues createStudentValues(long classId){

        ContentValues studentValues = new ContentValues();
        studentValues.put(StudentEntry.COLUMN_STUDENT_ID, 1234);
        studentValues.put(StudentEntry.COLUMN_CLASS_KEY,classId);
        studentValues.put(StudentEntry.COLUMN_ROLL_NO, 1);
        studentValues.put(StudentEntry.COLUMN_STUDENT_NAME,"Nigel");

        return studentValues;

    }

    static ContentValues createAttendanceValues(long classId, long studentId){
        ContentValues attendanceValues = new ContentValues();
        attendanceValues.put(AttendanceEntry.COLUMN_DATETEXT,"20141205");
        attendanceValues.put(AttendanceEntry.COLUMN_CLASS_KEY,classId);
        attendanceValues.put(AttendanceEntry.COLUMN_STU_KEY,studentId);
        attendanceValues.put(AttendanceEntry.COLUMN_ATTENDANCE,"Present");

        return attendanceValues;
    }
}
