package com.avatar.presentteacher;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.avatar.presentteacher.data.AttendanceContract.ClassEntry;

/**
 * A simple {@link Fragment} subclass.
 *
 * to handle interaction events.
 *
 * create an instance of this fragment.
 *
 */
public class ClassFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private ClassAdapter mClassAdapter;
    private static final String LOG_TAG = ClassFragment.class.getSimpleName();


    private ListView mListView;
    private int mPosition = ListView.INVALID_POSITION;
    private static final String SELECTED_KEY = "selected_position";
    private boolean mUseSinglePaneLayout;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final int CLASS_LOADER = 0;

    private static final String[] CLASS_COLUMNS = {
            ClassEntry.TABLE_NAME + "." + ClassEntry._ID,
            ClassEntry._ID,
            ClassEntry.COLUMN_CLASS_NAME,
            ClassEntry.COLUMN_BATCH,
            ClassEntry.COLUMN_SUBJECT,
            ClassEntry.COLUMN_SEM
    };

    // These indices are tied to CLASS_COLUMNS.  If CLASS_COLUMNS changes, these
    // must change.
    public static final int COL_CLASS_ID = 1;
    public static final int COL_CLASS_NAME = 2;
    public static final int COL_BATCH = 3;
    public static final int COL_SUBJECT = 4;
    public static final int COL_SEM = 5;

    /**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of item
     * selections.
     */
    public interface Callback {
        /**
         * ClassFragmentCallback for when an item has been selected.
         */
        public void onItemSelected(String classSelected);
    }

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    //private OnFragmentInteractionListener mListener;



    public ClassFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add this line in order for this fragment to handle menu events.
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.allclass, menu);
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
        Log.v(LOG_TAG, "Inside onCreateView of Class Fragment 1 ....");
        // The ArrayAdapter will take data from a source and
        // use it to populate the ListView it's attached to.
        mClassAdapter = new ClassAdapter(getActivity(), null, 0);
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        // Get a reference to the ListView, and attach this adapter to it.
        mListView = (ListView) rootView.findViewById(R.id.listview_class);
        mListView.setAdapter(mClassAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Cursor cursor = mClassAdapter.getCursor();
                if (cursor != null && cursor.moveToPosition(position)) {
                    ((Callback)getActivity())
                            .onItemSelected(cursor.getString(COL_CLASS_ID));
                    Log.v(LOG_TAG, "Is Class Id null ? "+cursor.getString(COL_CLASS_ID));
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
        mClassAdapter.setUseSinglePaneLayout(mUseSinglePaneLayout);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
       getLoaderManager().initLoader(CLASS_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

  /*  @Override
    public void onResume(){
        super.onResume();

    }*/

    // TODO: Rename method, update argument and hook method into UI event
    /*public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }*/


   /* @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }*/

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

        Log.v(LOG_TAG, "Inside onCreateLoader Class Fragment ....");
       //Uri classListUri = AttendanceContract.ClassEntry.buildClassUri();

        // Create and return a CursorLoader that will take care of
        // creating a Cursor for the data being displayed.
        return new CursorLoader(
                getActivity(),   // Parent activity context
                ClassEntry.CONTENT_URI,        // Table to query
                CLASS_COLUMNS,     // Projection to return
                null,            // No selection clause
                null,            // No selection arguments
                null             // Default sort order
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        mClassAdapter.swapCursor(data);
        if (mPosition != ListView.INVALID_POSITION) {
            // If we don't need to restart the loader, and there's a desired position to restore
            // to, do so now.
            mListView.smoothScrollToPosition(mPosition);
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
            mClassAdapter.swapCursor(null);
    }

    public void setUseSinglePaneLayout(boolean useSinglePaneLayout) {
        mUseSinglePaneLayout = useSinglePaneLayout;
        if (mClassAdapter != null) {
            mClassAdapter.setUseSinglePaneLayout(mUseSinglePaneLayout);
        }
    }

}
