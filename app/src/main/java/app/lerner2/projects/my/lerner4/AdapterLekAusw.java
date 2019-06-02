package app.lerner2.projects.my.lerner4;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class AdapterLekAusw extends ArrayAdapter<String[]> {

    private final Context context;
    private final String[][] values;
    private int[] selectedItemColor;
    private int[] alpha;
    private Resources res;
    private boolean[] ausw;

    public AdapterLekAusw(Context context, String[][] values, int layout,
                          int[] alpha) {
        super(context, layout, values);
        this.context = context;
        this.values = values;
        this.alpha = alpha;
        selectedItemColor = new int[1];
        try {
            for (int i = 0; i < values.length; i++) {
                if(true){
                    selectedItemColor[i] = Color.argb(alpha[i], 0, 90, 0);
                }else{
                    selectedItemColor[i] = Color.argb(alpha[i], 50, 50, 50);
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        res = context.getResources();
    }

    static class Type1Holder {
        public TextView text;
    }


    public void changeText(String text, int position) {
        if(ausw[position]){
            selectedItemColor[position] = Color.argb(alpha[position], 100, 100, 100);
            ausw[position] = false;
        }else{
            selectedItemColor[position] = Color.argb(alpha[position], 0, 90, 0);
            ausw[position] = true;
        }

    }

    public String getLekName(int position){
        return values[position][0];
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Type1Holder holder1;
        View v = convertView;

        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.lektionrow, parent, false);
            holder1 = new Type1Holder();
            holder1.text = (TextView) v.findViewById(R.id.textview);
            v.setTag(holder1);
        } else {
            holder1 = (Type1Holder) v.getTag();
        }
        String[] s = values[position];
        holder1.text.setText(s[0]);
        holder1.text.setBackgroundColor(selectedItemColor[position]);
        return v;
    }

}
