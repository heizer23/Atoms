package app.lerner2.projects.my.lerner4;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;

import app.lerner2.projects.my.lerner4.Data.DatabaseEvents;

public class LekAuswActiv extends Activity implements View.OnClickListener {

    private Button bSave;
    private String[] allLektions;
    private String[][] datNames;
    private ListView listView;
    private ArrayAdapter<String[]> adapterRead;
    private ArrayAdapter<String[]> adapterToRead;
    private boolean nichtEinlesen = true;
    private Resources res;
    private Activity act;

    private SharedPreferences myPrefs;

    private void setAdapter() {
        int[] alphaEinlesen = null;
        int[] alphaLektionen;

        try {
            ArrayList<String> dateien = GetFiles(Environment
                    .getExternalStorageDirectory() + "/Quizzer/Lektionen");
            datNames = new String[dateien.size()][3];
            alphaEinlesen = new int[datNames.length];
            for (int i = 0; i < dateien.size(); i++) {
                datNames[i][0] = dateien.get(i);
                datNames[i][1] = "" + i;
                alphaEinlesen[i] = 100;
            }
        } catch (Exception e) {
            datNames = new String[0][3];
            e.printStackTrace();
        }

        adapterToRead = new AdapterLekAusw(this, datNames, R.layout.lektionrow,
                alphaEinlesen);
        listView.setAdapter(adapterToRead);
    }

    public ArrayList<String> GetFiles(String DirectoryPath) {
        ArrayList<String> MyFiles = new ArrayList<String>();
        File f = new File(DirectoryPath);

        f.mkdirs();
        File[] files = f.listFiles();
        if (files.length == 0)
            return null;
        else {
            for (int i = 0; i < files.length; i++)
                MyFiles.add(files[i].getName());
        }

        return MyFiles;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 1, 0, R.string.RB_newTable);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case 1:
                listView.setAdapter(adapterToRead);
                nichtEinlesen = false;
                break;
            case 3:
//			ausgewFragen = ((MySimpleArrayAdapter) listView.getAdapter())
//					.checkAuswahl();
//			DialogFragment newFragment = new DialogOverview();
//			Bundle args = new Bundle();
//			args.putStringArray("allLekNames", allLekNames);
//			args.putStringArray("allLektions", allLektions);
//			args.putString("typ", "Fragen");
//			args.putString("title",
//					(allLekNames[spinner.getSelectedItemPosition()]));
//			args.putInt("lekInArray", (spinner.getSelectedItemPosition()));
//			args.putIntArray("ausgewFragen", ausgewFragen);
//			newFragment.setArguments(args);
//			newFragment.show(getSupportFragmentManager(), "missiles");
                break;
        }
        return true;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.lektionauswahl);
        res = getResources();
        Thread.setDefaultUncaughtExceptionHandler(new CustomExceptionHandler(
                res.getString(R.string.PathCrashReport), null));
        listView = (ListView) findViewById(R.id.list);
        bSave = (Button) findViewById(R.id.button1);

        bSave.setOnClickListener(this);
        act = this;
        setAdapter();
      //  listView.setOnItemLongClickListener(onItemLongClickListener);
    }

//    AdapterView.OnItemLongClickListener onItemLongClickListener = new AdapterView.OnItemLongClickListener() {
//        public boolean onItemLongClick(AdapterView<?> parent, View view,
//                                       int position, long id) {
//            DatabaseEvents dbEvents = new DatabaseEvents(parent.getContext(), act);
//            dbEvents.newTable(datNames[position][0]);
//            finish();
//            return false;
//        }
//    };

    public void onClick(View v) {

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

}
