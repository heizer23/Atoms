package app.lerner2.projects.my.lerner4;

import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import app.lerner2.projects.my.lerner4.Data.DatabaseEvents;
import app.lerner2.projects.my.lerner4.Data.DatabaseHelper;

public class OverviewActivity extends ListActivity implements
        LoaderManager.LoaderCallbacks<Cursor>, OnItemSelectedListener {

    private static final int GO_TO = Menu.FIRST + 1;
    private static final int CONNECT_ID= Menu.FIRST + 2;
    private static final int SELECT_ID = Menu.FIRST + 3;
    private static final int DELETE_ID = Menu.FIRST + 4;
    private String[] from = new String[]{"Datum", "Item", "Next"};
    private int[] to = new int[]{R.id.label, R.id.label2, R.id.label3};
    private DatabaseEvents dbEvents;
    private Cursor cursor;
    private boolean newStart = true;
    // private Cursor cursor;
    private AdapterOverview adapter;
    private Spinner spinnerRechts;
    private Spinner spinnerLinks;
    private Spinner spinnerMitte;
    private EditText etNameOrInfo;
    private String links;
    private String mitte;
    private String rechts;
    private int chosenId = 33;
    private String whereSQL = "";

    private boolean select = false;
    private Resources res;
    private int[] connected;
    /**
     * Called when the activity is first created.
     */

    public OverviewActivity() {
    }


    private void getConnected(int auswahl){
        DatabaseHelper databaseHelper = new DatabaseHelper(this, this);
        databaseHelper.open();
        connected = databaseHelper.getLinks(auswahl);
        databaseHelper.close();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.overview);
        // requestWindowFeature(Window.FEATURE_NO_TITLE);

        spinnerRechts = findViewById(R.id.spinnerRechts);
        spinnerRechts.getBaseline();
        res = getResources();
        ListView listView = findViewById(android.R.id.list);
        listView.setDividerHeight(2);

        registerForContextMenu(listView);

        dbEvents = new DatabaseEvents(this, this);
        dbEvents.open();
        spinnerMitte =  findViewById(R.id.spinnerMitte);
        spinnerLinks =  findViewById(R.id.spinnerLinks);
        etNameOrInfo =  findViewById(R.id.etNameOrInfo);

        String itemVorwahl = dbEvents.getItemSQL("_id = " + chosenId, null)[1];
        etNameOrInfo.setText(itemVorwahl);
        getConnected(chosenId);
        setSpinner();
        newStart = false;

    }



    private void setSpinner() {
        spinnerRechts.setOnItemSelectedListener(this);
        spinnerMitte.setOnItemSelectedListener(this);
        spinnerLinks.setOnItemSelectedListener(this);
        List<String> list = new ArrayList<String>();
        list.add("Datum");
        list.add("Next");
        list.add("Score");
        list.add("_id");
        list.add("Counter");
        List<String> listMitte = new ArrayList<String>();
        listMitte.add("Item");
        listMitte.add("Ort");

        ArrayAdapter<String> dataAdapterRechst = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapterRechst
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRechts.setAdapter(dataAdapterRechst);
        ArrayAdapter<String> dataAdapterLinks = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapterLinks
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLinks.setAdapter(dataAdapterLinks);

        ArrayAdapter<String> dataAdapterMitte = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, listMitte);
        dataAdapterMitte
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMitte.setAdapter(dataAdapterMitte);
        whereSQL = "Next>-1";
        dbEvents.open();
        cursor = dbEvents.getCursor(whereSQL, rechts);
        cursor.moveToFirst();
        dbEvents.close();
        adapter = new AdapterOverview(this, R.layout.todo_row, cursor,
                from, to, chosenId, connected);
        setListAdapter(adapter);
    }

    // Reaction to the menu selection
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.seen:
                //todo hier wieder die Sortierung nach Next reinbringen "Next>-1"
                whereSQL = "Datum >-10000";
                dbEvents.open();
                cursor = dbEvents.getCursor(whereSQL, rechts);
                dbEvents.close();
                adapter = new AdapterOverview(this, R.layout.todo_row, cursor,
                        from, to,chosenId, connected);
                setListAdapter(adapter);
                return true;
            case R.id.new_ones:
                whereSQL = "Datum >-10000";
                dbEvents.open();
                String order = rechts;
                cursor = dbEvents.getCursor(whereSQL, order);
                dbEvents.close();
                adapter = new AdapterOverview(this, R.layout.todo_row, cursor,
                        from, to, chosenId,connected);
                setListAdapter(adapter);
                return true;
            case R.id.search:
//                AlertDialog.Builder builder = new AlertDialog.Builder(this);
//                builder.setMessage("Search");
//                View dialogView = getLayoutInflater().inflate(R.layout.overview_dialog_search, null);
//                final EditText etSearch = (EditText) dialogView.findViewById(R.id.overview_dia_et_search);
//                builder.setView(dialogView);
//                builder.setPositiveButton("Search", new Dialog.OnClickListener() {
//
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        searchSetAdapter(etSearch.getText().toString());
//                        dialog.cancel();
//                    }
//
//                });
//
//                builder.setNegativeButton("Cancel", new Dialog.OnClickListener() {
//
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.cancel();
//                    }
//
//                });
//                builder.show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
                .getMenuInfo();
        switch (item.getItemId()) {
            case GO_TO:
                Intent intent = new Intent(this, ItemViewAct.class);
                int fragenId = (int)info.id;
                intent.putExtra("_id", fragenId);
                startActivity(intent);
                return true;
            case DELETE_ID:
                Uri uri = Uri.parse(MyContentProvider.CONTENT_URI + "/"
                        + info.id);
                this.getContentResolver().delete(uri, null, null);
                fillDataIds(whereSQL);
                 return true;
            case SELECT_ID:
                return true;
            case CONNECT_ID:
                DatabaseHelper databaseHelper =  new DatabaseHelper(this,this);
                databaseHelper.linkItems(chosenId, (int)info.id);
                getConnected(chosenId);
                adapter.setConnected(chosenId,  connected);
                  getListView().invalidateViews();
                return true;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        dbEvents.open();
        String itemVorwahl = dbEvents.getItemSQL("_id = " + id, null)[1];
        dbEvents.close();
        etNameOrInfo.setText(itemVorwahl);
        chosenId = (int)id;
        getConnected(chosenId);
        adapter.setConnected(chosenId, connected);


            getListView().invalidateViews();
            // Klicken auf ein Item wählt alle Chapter dieses Items aus und sorgt damit dafür, dass alle related fragen farblich markiert werden
        }

    private void fillDataIds(String fragenIds) {
        dbEvents.open();
        cursor = dbEvents.getCursor(fragenIds, "Datum");
        Toast.makeText(this.getApplicationContext(), "COUNT: " + cursor.getCount(),
                Toast.LENGTH_LONG).show();
        dbEvents.close();
        adapter = new AdapterOverview(this, R.layout.todo_row, cursor,
                from, to,chosenId, connected);
        setListAdapter(adapter);
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        menu.add(0, DELETE_ID, 0, R.string.menu_delete);
        menu.add(0, GO_TO, 0, R.string.menu_goto);
        menu.add(0, CONNECT_ID, 0, R.string.menu_connect);
    }

    // Creates a new loader after the initLoader () call
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {DatabaseEvents.KEY_ROWID, DatabaseEvents.KEY_ITEM,
                DatabaseEvents.KEY_DATUM, DatabaseEvents.KEY_SCORE, DatabaseEvents.KEY_INFO
               , DatabaseEvents.KEY_COUNTER, DatabaseEvents.KEY_NEXT
               };
        CursorLoader cursorLoader = new CursorLoader(this,
                MyContentProvider.CONTENT_URI, projection, null, null,
                null);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // data is not available anymore, delete reference
        adapter.swapCursor(null);
    }

    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                               long arg3) {
        int listPosition = getListView().getFirstVisiblePosition();
        if(!newStart){
        switch (arg0.getId()) {
            case R.id.spinnerRechts:
                links = String.valueOf(spinnerLinks.getSelectedItem());
                mitte = String.valueOf(spinnerMitte.getSelectedItem());
                rechts = String.valueOf(spinnerRechts.getSelectedItem());
                from = new String[]{links, mitte, rechts};
                dbEvents.open();

                String sqlSelect = "Datum > -10000 ";
                cursor = dbEvents.getCursor(sqlSelect, rechts);
                adapter = new AdapterOverview(this, R.layout.todo_row, cursor,
                        from, to,chosenId, connected);
                setListAdapter(adapter);
                dbEvents.close();
                break;
            case R.id.spinnerMitte:
                links = String.valueOf(spinnerLinks.getSelectedItem());
                mitte = String.valueOf(spinnerMitte.getSelectedItem());
                rechts = String.valueOf(spinnerRechts.getSelectedItem());
                from = new String[]{links, mitte, rechts};
                dbEvents.open();
                cursor = dbEvents.getCursor(whereSQL, mitte);
                adapter = new AdapterOverview(this, R.layout.todo_row, cursor,
                        from, to, chosenId,connected);
                setListAdapter(adapter);
                dbEvents.close();
                break;
            case R.id.spinnerLinks:
                links = String.valueOf(spinnerLinks.getSelectedItem());
                mitte = String.valueOf(spinnerMitte.getSelectedItem());
                rechts = String.valueOf(spinnerRechts.getSelectedItem());
                from = new String[]{links, mitte, rechts};
                dbEvents.open();
                    String sqlSelect2 = "Next>10000 ";
                cursor = dbEvents.getCursor(sqlSelect2, links);
                adapter = new AdapterOverview(this, R.layout.todo_row, cursor,
                        from, to,chosenId, connected);
                setListAdapter(adapter);
                dbEvents.close();


            break;
        }
        }
        getListView().invalidateViews();
        select = false;
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
    }



}