package com.avatar.presentteacher;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.avatar.presentteacher.data.AttendanceContract.ClassEntry;


/**
 * Created by nigthoma on 8/17/2014.
 */
public class ClassAdapter extends CursorAdapter {

    // Flag to determine if we want to use a single pane.
    private boolean mSinglePaneLayout = true;

    private static final String[] CLASS_FROM_COLUMNS = {
            ClassEntry.COLUMN_CLASS_NAME,
            ClassEntry.COLUMN_BATCH,
            ClassEntry.COLUMN_SUBJECT,
            ClassEntry.COLUMN_SEM
    };


    private static final int[] VIEW_ITEMS = {R.id.list_item_class_textview,R.id.list_item_batch_textview,
            R.id.list_item_subject_textview,R.id.list_item_semester_textview };

    /**
     * Cache of the children views for a Class list item.
     */
    public static class ViewHolder {

        public final TextView classView;
        public final TextView subjectView;
        public final TextView batchView;
        public final TextView semesterView;

        public ViewHolder(View view) {

            classView = (TextView) view.findViewById(R.id.list_item_class_textview);
            subjectView = (TextView) view.findViewById(R.id.list_item_subject_textview);
            batchView = (TextView) view.findViewById(R.id.list_item_batch_textview);
            semesterView = (TextView) view.findViewById(R.id.list_item_semester_textview);
        }
    }

    public ClassAdapter(Context context, Cursor c, int flags) {
        super(context,c,flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        View view = LayoutInflater.from(context).inflate(R.layout.list_item_class, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        ViewHolder viewHolder = (ViewHolder) view.getTag();

        // Read class from cursor
        String classString = cursor.getString(ClassFragment.COL_CLASS_NAME);
        // Find TextView and set class on it
        viewHolder.classView.setText(classString);

        // Read subject from cursor
        String subject = cursor.getString(ClassFragment.COL_SUBJECT);
        // Find TextView and set subject on it
        viewHolder.subjectView.setText(subject);

        // Read batch from cursor
        String batch = cursor.getString(ClassFragment.COL_BATCH);
        viewHolder.batchView.setText(batch);

        // Read low temperature from cursor
        String sem = cursor.getString(ClassFragment.COL_SEM);
        viewHolder.semesterView.setText(sem);

    }

    public void setUseSinglePaneLayout(boolean useTodayLayout) {
        mSinglePaneLayout = useTodayLayout;
    }
}
