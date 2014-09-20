package com.avatar.presentteacher;

import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.avatar.presentteacher.data.AttendanceContract;


public class CreateStudentActivity extends ActionBarActivity {

    private String studentClassId ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_student);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            studentClassId = extras.getString(StudentsActivity.CLASS_KEY);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.create_student, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void saveStudent(View button) {

        final EditText studentIdField = (EditText)  findViewById(R.id.student_id_edittext);
        String studentId = studentIdField.getText().toString();

        final EditText rollNoField = (EditText)  findViewById(R.id.roll_no_edittext);
        String rollNo = rollNoField.getText().toString();

        final EditText studentNameField = (EditText)  findViewById(R.id.student_name_edittext);
        String studentName = studentNameField.getText().toString();

        // First, check if the student with this student id exists in the db
        Cursor cursor = getContentResolver().query(
                AttendanceContract.StudentEntry.CONTENT_URI,
                new String[]{AttendanceContract.StudentEntry._ID},
                AttendanceContract.StudentEntry.COLUMN_STUDENT_ID + " = ?",
                new String[]{studentId},
                null);

        if (cursor.moveToFirst()) {
            int classIdIndex = cursor.getColumnIndex(AttendanceContract.StudentEntry._ID);
            Toast.makeText(getApplicationContext(), "This Student already exists",
                    Toast.LENGTH_SHORT).show();
        } else {
            ContentValues classValues = new ContentValues();
            classValues.put(AttendanceContract.StudentEntry.COLUMN_STUDENT_ID, studentId);
            classValues.put(AttendanceContract.StudentEntry.COLUMN_ROLL_NO, rollNo);
            classValues.put(AttendanceContract.StudentEntry.COLUMN_STUDENT_NAME, studentName);
            classValues.put(AttendanceContract.StudentEntry.COLUMN_CLASS_KEY, studentClassId);

            Uri classInsertUri = getContentResolver()
                    .insert(AttendanceContract.StudentEntry.CONTENT_URI, classValues);

            long newClassId = ContentUris.parseId(classInsertUri);
            Toast.makeText(getApplicationContext(), "This Student "+studentName+" is inserted into "+studentClassId,
                    Toast.LENGTH_SHORT).show();
            if(newClassId != 0){
                studentIdField.setText("");
                rollNoField.setText("");
                studentNameField.setText("");
            }
        }

    }
}
