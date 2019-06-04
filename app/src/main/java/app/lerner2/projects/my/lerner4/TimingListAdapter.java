package app.lerner2.projects.my.lerner4;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimingListAdapter extends SimpleCursorAdapter {

    private Cursor cursor;
    private Context context;
    private Activity act;
    private int layout;
    private LayoutInflater mInflater;
    private String[] from;

    private long nowInMS;
    long secondsInMilli = 1000;
    long minutesInMilli = secondsInMilli * 60;
    long hoursInMilli = minutesInMilli * 60;
    long daysInMilli = hoursInMilli * 24;

    public TimingListAdapter(Context context, int layout, Cursor cursor,
                             String[] from, int[] to) {
        super(context, layout, cursor, from, to);
        this.from = from;
        this.cursor = cursor;
        this.context = context;
        act = (Activity) context;
        this.layout = layout;
        mInflater = LayoutInflater.from(context);
        Time now = new Time();
        now.setToNow();
        nowInMS = now.toMillis(true);
    }

    static class ViewHolder {
        TextView text1;
        TextView text2;
        TextView text3;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        int id;
        int iRow = cursor.getColumnIndex(DbHelper.KEY_ROWID);

        if (convertView == null) {
            convertView = mInflater.inflate(layout, null);
            holder = new ViewHolder();
            holder.text1 = (TextView) convertView.findViewById(R.id.label);
            holder.text2 = (TextView) convertView.findViewById(R.id.label2);
            holder.text3 = (TextView) convertView.findViewById(R.id.label3);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        cursor.moveToPosition(position);


        int[] choices = new int[3];
        choices[0] = cursor.getColumnIndex(from[0]);
        choices[1] = cursor.getColumnIndex(from[1]);
        choices[2] = cursor.getColumnIndex(from[2]);

        String holderLeft = "";
        holder.text1.setText(cursor.getString(choices[0]));
        holder.text2.setText(cursor.getString(choices[1]));

        holder.text3.setText(cursor.getString(choices[2]));

        long nextTime = Long.parseLong(cursor.getString(choices[2]));
        MathStuff Ms = new MathStuff();
        holder.text3.setText(Ms.getTimings(nextTime));
        return (convertView);
    }

}