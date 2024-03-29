package app.lerner2.projects.my.lerner4;

import android.app.ListActivity;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListView;

import app.lerner2.projects.my.lerner4.Data.DatabaseEvents;

public class TimingListAct extends ListActivity {

    private Resources res;
    private DatabaseEvents dbEvents;
    private String[] from = {"_id","Item", "Next"};
    private int[] to = new int[]{R.id.label, R.id.label2, R.id.label3};
    private TimingListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        // requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.act_timing_list);
        res = getResources();
        ListView listView =  (ListView) findViewById(android.R.id.list);
        listView.setDividerHeight(2);
        registerForContextMenu(listView);


        Thread.setDefaultUncaughtExceptionHandler(new CustomExceptionHandler(
                res.getString(R.string.PathCrashReport), null));

        dbEvents = new DatabaseEvents(this,this);
        dbEvents.open();
        Cursor cursor = dbEvents.getNextEvents();
        adapter = new TimingListAdapter(this, R.layout.todo_row, cursor,
                from, to);
        setListAdapter(adapter);
        dbEvents.close();

    }
}
