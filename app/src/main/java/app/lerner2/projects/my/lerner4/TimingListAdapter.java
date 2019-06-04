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

        int sec;
        int min;
        int hours;
        int days;
        long nextTime = Long.parseLong(cursor.getString(choices[2]));
        MathStuff Ms = new MathStuff();
        holder.text3.setText(Ms.getTimings(nextTime));
//        if(from[0].equals("lastTime") || from[0].equals("nextTime")){
//            long dv = cursor.getLong(choices[0]) ;
//            Date df = new Date(dv);
//            holder.text1.setText(getDifference(dv));
//        }else{
//            holder.text1.setText(cursor.getString(choices[2]));
//        }
//
//        if(from[2].equals("lastTime") || from[2].equals("nextTime")){
//            long dv = cursor.getLong(choices[2]) ;
//            Date df = new Date(dv);
//            holder.text3.setText(getDifference(dv));
//        }else{
//            holder.text3.setText(cursor.getString(choices[0]));
//        }
        return (convertView);
    }


private String getDifference(long dateInMills){
    String result = "";
    long tempKlein;
    long tempGross;
    Long difference = ( dateInMills-nowInMS);

    if(Math.abs(difference)<hoursInMilli){
        tempGross = (int)(difference/minutesInMilli);
        tempKlein = (int)((difference%minutesInMilli)/secondsInMilli);
        result = String.format("%d m\n %d s", tempGross, Math.abs(tempKlein));
    }else if (difference<daysInMilli){
        tempGross = (int)(difference/hoursInMilli);
        tempKlein = (int)((difference%hoursInMilli)/minutesInMilli);
        result = String.format("%d h\n %d m", tempGross, Math.abs(tempKlein));
    }else{
        tempGross = (int)(difference/daysInMilli);
        tempKlein = (int)((difference%daysInMilli)/hoursInMilli);
        result = String.format("%d d\n %d h", tempGross, Math.abs(tempKlein));

    }
    return result;
    }

}