package com.avatar.presentteacher;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;



public class MainActivity extends ActionBarActivity implements ClassFragment.Callback {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v(LOG_TAG, "Inside onCreate of MainActivity ....");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ClassFragment classFragment =  ((ClassFragment)getSupportFragmentManager()
                .findFragmentById(R.id.fragment_main));
        classFragment.setUseSinglePaneLayout(!mTwoPane);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.allclass, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_add_class) {
            startActivity(new Intent(this, NewClassActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(String classSelected) {
        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            Log.d(LOG_TAG, "Inside mTwoPane onItemSelected MainActivity");
            Bundle args = new Bundle();
            args.putString(StudentsActivity.CLASS_KEY, classSelected);

            StudentsFragment fragment = new StudentsFragment();
            fragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_students, fragment)
                    .commit();
        } else {
            Log.d(LOG_TAG, "Inside \"else\" onItemSelected MainActivity");
            Intent intent = new Intent(this, StudentsActivity.class)
                    .putExtra(StudentsActivity.CLASS_KEY, classSelected);
            Log.d(LOG_TAG, "Class Selected "+classSelected);
            startActivity(intent);
        }
    }

}
