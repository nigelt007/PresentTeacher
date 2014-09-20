package com.avatar.presentteacher;

import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.avatar.presentteacher.data.AttendanceContract.ClassEntry;


public class NewClassActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_class);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.new_class, menu);
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

    public void saveClass(View button) {
        // Do click handling here

        final EditText classNameField = (EditText)  findViewById(R.id.class_name_edittext);
        String className = classNameField.getText().toString();

        final EditText subjectNameField = (EditText)  findViewById(R.id.subject_name_edittext);
        String subjectName = subjectNameField.getText().toString();

        final EditText batchNameField = (EditText)  findViewById(R.id.batch_name_edittext);
        String batchName = batchNameField.getText().toString();

        final EditText semesterNameField = (EditText)  findViewById(R.id.semester_name_edittext);
        String semester = semesterNameField.getText().toString();

        // First, check if the class with this class name exists in the db
        Cursor cursor = getContentResolver().query(
                ClassEntry.CONTENT_URI,
                new String[]{ClassEntry._ID},
                ClassEntry.COLUMN_CLASS_NAME + " = ?",
                new String[]{className},
                null);

        if (cursor.moveToFirst()) {
            int locationIdIndex = cursor.getColumnIndex(ClassEntry._ID);
            Toast.makeText(getApplicationContext(), "This Class Name already exists",
                    Toast.LENGTH_SHORT).show();
        } else {
            ContentValues classValues = new ContentValues();
            classValues.put(ClassEntry.COLUMN_CLASS_NAME, className);
            classValues.put(ClassEntry.COLUMN_SUBJECT, subjectName);
            classValues.put(ClassEntry.COLUMN_BATCH, batchName);
            classValues.put(ClassEntry.COLUMN_SEM, semester);

            Uri classInsertUri = getContentResolver()
                    .insert(ClassEntry.CONTENT_URI, classValues);

              long newClassId =ContentUris.parseId(classInsertUri);
            Toast.makeText(getApplicationContext(), "This Class "+className+" is created",
                    Toast.LENGTH_SHORT).show();
            if(newClassId != 0){
                classNameField.setText("");
                batchNameField.setText("");
                subjectNameField.setText("");
                semesterNameField.setText("");
            }
        }
    }
}
