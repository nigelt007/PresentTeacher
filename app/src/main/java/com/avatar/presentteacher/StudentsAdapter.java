package com.avatar.presentteacher;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;


/**
 * Created by nigthoma on 9/8/2014.
 */
public class StudentsAdapter extends CursorAdapter {

    // Flag to determine if we want to use a single pane.
    private boolean mSinglePaneLayout = true;

   /* private static final String[] STUDENTS_FROM_COLUMNS = {
            StudentEntry.COLUMN_STUDENT_NAME,
            StudentEntry.COLUMN_ROLL_NO,
    };


    private static final int[] VIEW_ITEMS = {R.id.list_item_student_name_textview,R.id.list_item_roll_no_textview }; */

    /**
     * Cache of the children views for a Student list item.
     */
    public static class ViewHolder {

        public final TextView studentNameView;
        public final TextView rollNoView;
        public final RadioButton presentRadio;
        public final RadioButton absentRadio;


        public ViewHolder(View view) {

            studentNameView = (TextView) view.findViewById(R.id.list_item_student_name_textview);
            rollNoView = (TextView) view.findViewById(R.id.list_item_roll_no_textview);
            presentRadio = (RadioButton)view.findViewById(R.id.radio_button_present);
            absentRadio = (RadioButton)view.findViewById(R.id.radio_button_absent);
        }
    }

    public StudentsAdapter(Context context, Cursor c, int flags) {
        super(context,c,flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        View view = LayoutInflater.from(context).inflate(R.layout.list_item_students, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        ViewHolder viewHolder = (ViewHolder) view.getTag();

        // Read student name from cursor
        String studentNameString = cursor.getString(StudentsFragment.COL_STUDENT_NAME);
        // Find TextView and set student name on it
        viewHolder.studentNameView.setText(studentNameString);

        // Read roll no. from cursor
        String rollNo = cursor.getString(StudentsFragment.COL_ROLL_NO);
        // Find TextView and set roll no. on it
        viewHolder.rollNoView.setText(rollNo);

        //Read attendance from cursor
        String attendance = cursor.getString(StudentsFragment.C)

    }

    public void setUseSinglePaneLayout(boolean useSinglePane) {
        mSinglePaneLayout = useSinglePane;
    }

}
