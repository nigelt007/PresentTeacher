package com.avatar.presentteacher;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.avatar.presentteacher.data.AttendanceContract;

/**
 * Created by nigthoma on 9/13/2014.
 */
public class StudFragment  extends android.support.v4.app.Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = StudFragment.class.getSimpleName();
    private static final int STUD_LOADER = 0;
    private StudentsAdapter mStudentsAdapter;

    private static final String[] STUDENTS_COLUMNS = {
            AttendanceContract.StudentEntry.TABLE_NAME + "." + AttendanceContract.StudentEntry._ID,
            AttendanceContract.StudentEntry.COLUMN_STUDENT_ID,
            AttendanceContract.StudentEntry.COLUMN_STUDENT_NAME,
            AttendanceContract.StudentEntry.COLUMN_ROLL_NO,
            AttendanceContract.StudentEntry.COLUMN_CLASS_KEY
    };

    // These indices are tied to STUDENT_COLUMNS.  If CLASS_COLUMNS changes, these
    // must change.
    public static final int COL_STUDENT_PK_ID = 0;
    public static final int COL_STUDENT_ID = 1;
    public static final int COL_STUDENT_NAME = 2;
    public static final int COL_ROLL_NO = 3;
    public static final int COL_CLASS_KEY = 4;

    private String mClassStr;
    private ListView mListView;
    private int mPosition = ListView.INVALID_POSITION;
    private static final String SELECTED_KEY = "selected_position";


    public interface Callback {
        /**
         * ClassFragmentCallback for when an item has been selected.
         */
        public void onItemSelected(String classSelected);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mStudentsAdapter = new StudentsAdapter(getActivity(), null, 0);
        View rootView = inflater.inflate(R.layout.fragment_stud, container, false);
        mListView = (ListView) rootView.findViewById(R.id.listview_students);
        mListView.setAdapter(mStudentsAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Cursor cursor = mStudentsAdapter.getCursor();
                if (cursor != null && cursor.moveToPosition(position)) {
                    ((Callback)getActivity())
                            .onItemSelected(cursor.getString(COL_STUDENT_NAME));
                }
                mPosition = position;
            }
        });
        // If there's instance state, mine it for useful information.
        // The end-goal here is that the user never knows that turning their device sideways
        // does crazy lifecycle related things.  It should feel like some stuff stretched out,
        // or magically appeared to take advantage of room, but data or place in the app was never
        // actually *lost*.
        if (savedInstanceState != null && savedInstanceState.containsKey(SELECTED_KEY)) {
            // The listview probably hasn't even been populated yet.  Actually perform the
            // swapout in onLoadFinished.
            mPosition = savedInstanceState.getInt(SELECTED_KEY);
        }
        return rootView;
    }
    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

        Log.v(LOG_TAG, "Inside onCreateLoader FIRST mClassStr " + mClassStr);
        // Sort order:  Ascending, by student name.
        String sortOrder = AttendanceContract.StudentEntry.COLUMN_STUDENT_NAME + " ASC";

        Uri weatherForLocationUri = AttendanceContract.StudentEntry.buildStudentWithClass(
                mClassStr);
        Log.v(LOG_TAG, "Inside onCreateLoader mClassStr " +mClassStr);
        // Now create and return a CursorLoader that will take care of
        // creating a Cursor for the data being displayed.
        return new CursorLoader(
                getActivity(),
                weatherForLocationUri,
                STUDENTS_COLUMNS,
                mClassStr,
                null,
                sortOrder
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }
}
