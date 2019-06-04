package app.lerner2.projects.my.lerner4;

import android.database.Cursor;

public class FrageDatum {
    private DbHelper dbHelper;

    private int id;
    private String item;
    private int datum;
    private Long next;
    private double score;
    private int counter;
    private int rahmen= 1000;
    private int eingrenzungen = 3;

    public FrageDatum(int id){
        this.id = id;

        dbHelper.open();
        String[] values = dbHelper.getFrageInfos(id);
        dbHelper.close();

        item = values[1];
        datum = Integer.parseInt(values[2]);
        next = Long.parseLong(values[3]);
        score = Double.parseDouble(values[4]);
        counter = Integer.parseInt(values[5]);
        rahmen = 1000;
        if (datum > 1900) {
            rahmen = 100;
            eingrenzungen=2;
        }
    }

    public String getItem(){
        return item;
    }

    public int getId(){
        return id;
    }

//    id = Integer.parseInt(values[0]);
//    item = values[1];
//    datum = Integer.parseInt(values[2]);
//    next = Long.parseLong(values[3]);
//    score = Double.parseDouble(values[4]);
//    counter = Integer.parseInt(values[5]);

}
