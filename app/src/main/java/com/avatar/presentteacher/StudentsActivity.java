package com.avatar.presentteacher;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;



public class StudentsActivity extends ActionBarActivity {

    private static final String LOG_TAG = StudentsActivity.class.getSimpleName();
    private boolean mTwoPane;

    public static final String CLASS_KEY = "class_id";
    public static String studentClassId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v(LOG_TAG, "Inside onCreate of StudentsActivity ....");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_students);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            studentClassId = extras.getString(StudentsActivity.CLASS_KEY);
        }
        if (findViewById(R.id.fragment_students) != null) {
            // The detail container view will be present only in the large-screen layouts
            // (res/layout-sw600dp). If this view is present, then the activity should be
            // in two-pane mode.
            mTwoPane = true;
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            if (savedInstanceState == null) {

                Bundle arguments = new Bundle();
                arguments.putString(StudentsActivity.CLASS_KEY, studentClassId);

                Log.v(LOG_TAG, "**INSIDE Student Class name "+studentClassId);

                StudentsFragment fragment = new StudentsFragment();
                fragment.setArguments(arguments);

                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragment_students, fragment)
                        .commit();
            }
        } else {
            mTwoPane = false;
        }
        Bundle arguments = new Bundle();
        arguments.putString(StudentsActivity.CLASS_KEY, studentClassId);
        Log.v(LOG_TAG, "**OUTSIDE Student Class name "+studentClassId);
        StudentsFragment studentsFragment =  ((StudentsFragment)getSupportFragmentManager()
                .findFragmentById(R.id.fragment_students));
        studentsFragment.setUseSinglePaneLayout(!mTwoPane);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.students, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_add_student) {
            Intent intent = new Intent(this, CreateStudentActivity.class);
            intent.putExtra(StudentsActivity.CLASS_KEY, studentClassId);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
