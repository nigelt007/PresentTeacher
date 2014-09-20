package com.avatar.presentteacher.data;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by nigthoma on 8/15/2014.
 */
public class AttendanceContract {

    // The "Content authority" is a name for the entire content provider, similar to the
    // relationship between a domain name and its website.  A convenient string to use for the
    // content authority is the package name for the app, which is guaranteed to be unique on the
    // device.
    public static final String CONTENT_AUTHORITY = "com.avatar.presentteacher";

    // Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
    // the content provider.
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_CLASS = "class";
    public static final String PATH_STUDENT = "student";
    public static final String PATH_ATTENDANCE = "attendance";


    public static final class AttendanceEntry implements BaseColumns{

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_ATTENDANCE).build();

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_ATTENDANCE;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + PATH_ATTENDANCE;

        public static final String TABLE_NAME ="attendance";

        public static final String COLUMN_DATETEXT = "date";

        public static final String COLUMN_STU_KEY = "student_id";

        public static final String COLUMN_CLASS_KEY = "class_id";

        public static final String COLUMN_ATTENDANCE = "attendance";

        public static Uri buildAttendanceUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildAttendanceWithClass(long classId) {
            return CONTENT_URI.buildUpon().appendPath(Long.toString(classId)).build();
        }

        public static Uri buildAttendanceWithStudent(long studentId) {
            return CONTENT_URI.buildUpon().appendPath(Long.toString(studentId)).build();
        }

        public static Uri buildStudentAttendanceWithQuery (long studentId, long classId){
            return CONTENT_URI.buildUpon().appendPath(Long.toString(studentId))
                    .appendQueryParameter(COLUMN_CLASS_KEY, Long.toString(classId)).build();
        }

        public static Uri buildStudentAttendanceWithClass (long studentId, long classId){
            return CONTENT_URI.buildUpon().appendPath(Long.toString(studentId))
                    .appendPath( Long.toString(classId)).build();
        }

        public static Uri buildStudentAttendanceWithDateClass (long studentId, long classId, String dateText){
            return CONTENT_URI.buildUpon().appendPath(Long.toString(studentId))
                    .appendPath( Long.toString(classId)).appendPath(dateText).build();
        }

        public static String getStudentFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }

        public static String getDateFromUri(Uri uri) {
            return uri.getPathSegments().get(3);
        }
    }

    public static final class StudentEntry implements BaseColumns{

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_STUDENT).build();

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_STUDENT;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + PATH_STUDENT;

        public static final String TABLE_NAME ="student";

        public static final String COLUMN_STUDENT_ID = "student_id";

        public static final String COLUMN_STUDENT_NAME = "student_name";

        public static final String COLUMN_ROLL_NO = "roll_no";

        public static final String COLUMN_CLASS_KEY = "class_id";

        public static Uri buildStudentUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildStudentWithClass(String className) {
            return CONTENT_URI.buildUpon().appendPath(className).build();
        }

        public static String getClassFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }


    }

    public static final class ClassEntry implements BaseColumns{

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_CLASS).build();

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_CLASS;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + PATH_CLASS;

        public static final String TABLE_NAME ="class";

        public static final String COLUMN_CLASS_NAME="class_name";

        public static final String COLUMN_SEM="sem";

        public static final String COLUMN_SUBJECT="subject";

        public static final String COLUMN_BATCH ="batch";

        public static Uri buildClassUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

    }
}
