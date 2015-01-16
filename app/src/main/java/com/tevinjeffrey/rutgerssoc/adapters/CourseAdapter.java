package com.tevinjeffrey.rutgerssoc.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tevinjeffrey.rutgerssoc.R;
import com.tevinjeffrey.rutgerssoc.model.Course;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;

import java.util.ArrayList;

/**
 * Created by Tevin on 1/14/2015.
 */
public class CourseAdapter extends ArrayAdapter {

    private Context context;
    private String[] navTitles;
    private TypedArray navIcons;
    private int type;
    private ImageView imgIcon = null;
    private TextView txtTitle = null;
    private ArrayList<Course> item;



    static class ViewHolder {
        public TextView text;
        public ImageView image;
        public TextView courseTitle;
    }


    public CourseAdapter(Context context, ArrayList<Course> item){
        super(context, -1, item);
        this.context = context;
        this.item = item;
    }


    @Override
    public int getCount() {
        return item.size();
    }

    @Override
    public Object getItem(int position) {
        return item.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View rowView = convertView;

        ViewHolder viewHolder;

        if (rowView == null) {

            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

            viewHolder = new ViewHolder();

            rowView = mInflater.inflate(R.layout.course_info, null);

            viewHolder.courseTitle = (TextView) rowView.findViewById(R.id.textView);


            rowView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) rowView.getTag();
        }

        String title = item.get(position).getExpandedTitle() == null? item.get(position).getTitle():
                item.get(position).getExpandedTitle();

        String text = title.toLowerCase();

        viewHolder.courseTitle.setText(WordUtils.capitalize(text));

        return rowView;

    }

}