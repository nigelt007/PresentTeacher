package com.avatar.presentteacher;


import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;

import com.avatar.presentteacher.data.AttendanceContract;
import com.avatar.presentteacher.data.AttendanceContract.ClassEntry;
import com.avatar.presentteacher.data.AttendanceContract.StudentEntry;
import com.avatar.presentteacher.data.AttendanceContract.AttendanceEntry;


/**
 * A simple {@link Fragment} subclass.
 *
 */
public class StudentsFragment extends android.support.v4.app.Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private boolean mUseSinglePaneLayout;
    private StudentsAdapter mStudentsAdapter;

    private static final String LOG_TAG = StudentsFragment.class.getSimpleName();
    private static final int STUDENTS_LOADER = 0;
    private static final int ATTENDANCE_LOADER = 1;

    private static final String[] STUDENTS_COLUMNS = {
            StudentEntry.TABLE_NAME + "." + AttendanceContract.StudentEntry._ID,
            StudentEntry.COLUMN_STUDENT_ID,
            StudentEntry.COLUMN_STUDENT_NAME,
            StudentEntry.COLUMN_ROLL_NO,
            ClassEntry.COLUMN_CLASS_NAME
    };

    private static final String[] ATTENDANCE_COLUMNS = {
            AttendanceEntry.TABLE_NAME + "." + AttendanceEntry._ID,
            AttendanceEntry.COLUMN_DATETEXT,
            AttendanceEntry.COLUMN_CLASS_KEY,
            AttendanceEntry.COLUMN_STU_KEY,
            AttendanceEntry.COLUMN_ATTENDANCE
    };

    // These indices are tied to STUDENT_COLUMNS.  If STUDENT_COLUMNS changes, these
    // must change.
    public static final int COL_STUDENT_PK_ID = 0;
    public static final int COL_STUDENT_ID = 1;
    public static final int COL_STUDENT_NAME = 2;
    public static final int COL_ROLL_NO = 3;
    public static final int COL_CLASS_KEY = 4;

    private TextView mStudentIdView;
    private TextView mStudentRollNoView;
    private TextView mStudentNameView;
    private String mClassIdStr;
    private String mDate;
    private ListView mListView;
    private DatePicker datePicker;
    private int mPosition = ListView.INVALID_POSITION;
    private static final String SELECTED_KEY = "selected_position";


    public StudentsFragment() {
        // Required empty public constructor
            }

    /**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of item
     * selections.
     */
    public interface Callback {
        /**
         * ClassFragmentCallback for when an item has been selected.
         */
        public void onItemSelected(String studentSelected);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(LOG_TAG, "Inside onCreate of Students Fragment....");
        // Add this line in order for this fragment to handle menu events.
        setHasOptionsMenu(true);

        Bundle arguments = getArguments();
        if (arguments != null) {
            mClassIdStr = arguments.getString(StudentsActivity.CLASS_KEY);
            Log.v(LOG_TAG, "M CLASS Id Str "+mClassIdStr);
        }

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.v(LOG_TAG, "Inside onCreateView of Students Fragment....");

        // The ArrayAdapter will take data from a source and
        // use it to populate the ListView it's attached to.
        Bundle arguments = getArguments();
        if (arguments != null) {
            mClassIdStr = arguments.getString(StudentsActivity.CLASS_KEY);
        }
        mStudentsAdapter = new StudentsAdapter(getActivity(), null, 0);
        Intent intent = getActivity().getIntent();
        View rootView = inflater.inflate(R.layout.fragment_students, container, false);
        if(intent != null && intent.hasExtra(StudentsActivity.CLASS_KEY)){
          String classStr = intent.getStringExtra(StudentsActivity.CLASS_KEY);
          Log.v(LOG_TAG, "Class String "+classStr);
        }
        // Get a reference to the ListView, and attach this adapter to it.
        mListView = (ListView) rootView.findViewById(R.id.listview_students);
        mListView.setAdapter(mStudentsAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Cursor cursor = mStudentsAdapter.getCursor();
                if (cursor != null && cursor.moveToPosition(position)) {
                    ((Callback)getActivity())
                            .onItemSelected(cursor.getString(COL_STUDENT_PK_ID));
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
        mStudentsAdapter.setUseSinglePaneLayout(mUseSinglePaneLayout);
        datePicker = (DatePicker)rootView.findViewById(R.id.date_picker);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(STUDENTS_LOADER, null, this);
        getLoaderManager().initLoader(ATTENDANCE_LOADER,null,this);
        super.onActivityCreated(savedInstanceState);
    }

  
    @Override
    public void onSaveInstanceState(Bundle outState) {
        // When tablets rotate, the currently selected list item needs to be saved.
        // When no item is selected, mPosition will be set to Listview.INVALID_POSITION,
        // so check for that before storing.
        if (mPosition != ListView.INVALID_POSITION) {
            outState.putInt(SELECTED_KEY, mPosition);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        Log.v(LOG_TAG, "Inside onCreateLoader of Students Fragment FIRST mClassStr " +mClassIdStr);
        // Sort order:  Ascending, by student name.
        String sortOrder = AttendanceContract.StudentEntry.COLUMN_STUDENT_NAME + " ASC";
        Uri studentsForClassUri = AttendanceContract.StudentEntry.buildStudentWithClass(
                mClassIdStr);
        Log.v(LOG_TAG, "Inside onCreateLoader mClassStr " +mClassIdStr);
        // Now create and return a CursorLoader that will take care of
        // creating a Cursor for the data being displayed.
        Loader<Cursor> studentsForClassLoader =
        new CursorLoader(getActivity(),studentsForClassUri, STUDENTS_COLUMNS,null,null,sortOrder );

        int day =datePicker.getDayOfMonth();int month = datePicker.getMonth()+1;int year = datePicker.getYear();
        Log.v(LOG_TAG, " \n Hello123 \n Day :"+day+", \n Month :"+month+ ", \n Year :"+year);
        StringBuilder builder = new StringBuilder();builder.append(day).append(month).append(year);
        mDate = builder.toString();
        builder.setLength(0);
        Log.v(LOG_TAG,"Date selected is "+ mDate);

        Uri attendanceForClassAndDateUri = AttendanceContract.AttendanceEntry.buildAttendanceWithDateAndClass(mClassIdStr, mDate);

        // Now create and return a CursorLoader that will take care of
        // creating a Cursor for the data being displayed.
        Loader<Cursor> attendanceForClassAndDateLoader =
                new CursorLoader(getActivity(),attendanceForClassAndDateUri,ATTENDANCE_COLUMNS,null,null,null);

        if(attendanceForClassAndDateLoader != null){
            return attendanceForClassAndDateLoader;
        }
        return studentsForClassLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        Log.v(LOG_TAG, "Inside onLoadFinished Students Fragment....");

        mStudentsAdapter.swapCursor(data);
        if (mPosition != ListView.INVALID_POSITION) {
            // If we don't need to restart the loader, and there's a desired position to restore
            // to, do so now.
            mListView.smoothScrollToPosition(mPosition);
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        mStudentsAdapter.swapCursor(null);
    }

    public void setUseSinglePaneLayout(boolean useTodayLayout) {
        mUseSinglePaneLayout = useTodayLayout;
        if (mStudentsAdapter != null) {
            mStudentsAdapter.setUseSinglePaneLayout(mUseSinglePaneLayout);
        }
    }
}
