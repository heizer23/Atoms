package app.lerner2.projects.my.lerner4;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import app.lerner2.projects.my.lerner4.Data.DbHelper;

public class AdapterOverview extends SimpleCursorAdapter {

    private Cursor cursor;
    private int layout;
    private LayoutInflater mInflater;
    private String[] from;
    private int auswahl;
    private int[] connected = new int[0];

    public AdapterOverview(Context context, int layout, Cursor cursor,
                           String[] from, int[] to, int auswahl, int[] connected) {
        super(context, layout, cursor, from, to);
        this.from = from;
        this.cursor = cursor;
        this.layout = layout;
        mInflater = LayoutInflater.from(context);
        this.auswahl = auswahl;
        this.connected = connected;


    }

public void setConnected(int chosenId, int[] connected){
    this.connected = connected;
    this.auswahl = chosenId;
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

        id = cursor.getInt(iRow);

        holder.text2.setBackgroundColor(Color.WHITE);
        if (id== auswahl){
            holder.text2.setBackgroundColor(Color.GREEN);
        }
        for(int i = 0; i< connected.length;i++){
            if(id== connected[i]){
                holder.text2.setBackgroundColor(Color.LTGRAY);
            }
        }
        int[] choices = new int[3];
        choices[0] = cursor.getColumnIndex(from[0]);
        choices[1] = cursor.getColumnIndex(from[1]);
        choices[2] = cursor.getColumnIndex(from[2]);

        String holderLeft = "";
        holder.text2.setText(cursor.getString(choices[1]));

        holder.text3.setText(cursor.getString(choices[2]));
        if(from[2].equals("LastDate")){
            long dv = Long.valueOf(cursor.getInt(choices[0])) * 1000;
            Date df = new Date(dv);
            holderLeft = new SimpleDateFormat("dd.MM.yy").format(df);
            holder.text3.setText(holderLeft);
        }else{
            holder.text3.setText(cursor.getString(choices[2]));
        }

        if(from[0].equals("LastDate")){
            long dv = Long.valueOf(cursor.getInt(choices[0])) * 1000;
            Date df = new Date(dv);
            holderLeft = new SimpleDateFormat("dd.MM.yy").format(df);
            holder.text1.setText(holderLeft);
        }else{
            holder.text1.setText(cursor.getString(choices[0]));
        }
        return (convertView);
    }
}