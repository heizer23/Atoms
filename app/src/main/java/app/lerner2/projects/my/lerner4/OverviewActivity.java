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

public class OverviewActivity extends ListActivity implements
        LoaderManager.LoaderCallbacks<Cursor>, OnItemSelectedListener {

    private static final int GO_TO = Menu.FIRST + 1;
    private static final int CONNECT_ID= Menu.FIRST + 2;
    private static final int SELECT_ID = Menu.FIRST + 3;
    private static final int DELETE_ID = Menu.FIRST + 4;
    private String[] from = new String[]{"Datum", "Item", "Next"};
    private int[] to = new int[]{R.id.label, R.id.label2, R.id.label3};
    private DbHelper database;
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
        DbHelper dbHelper = new DbHelper(this, this);
        dbHelper.open();
        connected =dbHelper.getLinks(auswahl);
        dbHelper.close();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.overview);
        // requestWindowFeature(Window.FEATURE_NO_TITLE);

        spinnerRechts = (Spinner) findViewById(R.id.spinnerRechts);
        spinnerRechts.getBaseline();
        res = getResources();
        ListView listView =  (ListView) findViewById(android.R.id.list);
        listView.setDividerHeight(2);

        registerForContextMenu(listView);

        database = new DbHelper(this, this);
        database.open();
        spinnerMitte = (Spinner) findViewById(R.id.spinnerMitte);
        spinnerLinks = (Spinner) findViewById(R.id.spinnerLinks);
        etNameOrInfo = (EditText) findViewById(R.id.etNameOrInfo);

        String itemVorwahl = database.getItemSQL("_id = " + chosenId, null)[1];
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
        list.add("Next");
        list.add("Score");
        list.add("Datum");
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
        database.open();
        cursor = database.getCursor(whereSQL, rechts);
        cursor.moveToFirst();
        database.close();
        adapter = new AdapterOverview(this, R.layout.todo_row, cursor,
                from, to, chosenId, connected);
        setListAdapter(adapter);
    }

    // Reaction to the menu selection
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.seen:
                whereSQL = "Next>-1";
                database.open();
                cursor = database.getCursor(whereSQL, rechts);
                database.close();
                adapter = new AdapterOverview(this, R.layout.todo_row, cursor,
                        from, to,chosenId, connected);
                setListAdapter(adapter);
                return true;
            case R.id.new_ones:
                whereSQL = "Next<1";
                database.open();
                String order = rechts;
                cursor = database.getCursor(whereSQL, order);
                database.close();
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
                DbHelper dbHelper =  new DbHelper(this,this);
                dbHelper.linkItems(chosenId, (int)info.id);
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

        database.open();
        String itemVorwahl = database.getItemSQL("_id = " + id, null)[1];
        database.close();
        etNameOrInfo.setText(itemVorwahl);
        chosenId = (int)id;
        getConnected(chosenId);
        adapter.setConnected(chosenId, connected);


            getListView().invalidateViews();
            // Klicken auf ein Item wählt alle Chapter dieses Items aus und sorgt damit dafür, dass alle related fragen farblich markiert werden
        }

    private void fillDataIds(String fragenIds) {
        database.open();
        cursor = database.getCursor(fragenIds, "Next");
        Toast.makeText(this.getApplicationContext(), "COUNT: " + cursor.getCount(),
                Toast.LENGTH_LONG).show();
        database.close();
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
        String[] projection = {DbHelper.KEY_ROWID, DbHelper.KEY_ITEM,
                DbHelper.KEY_DATUM, DbHelper.KEY_SCORE, DbHelper.KEY_INFO
               , DbHelper.KEY_COUNTER, DbHelper.KEY_NEXT
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
                database.open();

                String sqlSelect = "Next>0 ";
                cursor = database.getCursor(sqlSelect, rechts);
                adapter = new AdapterOverview(this, R.layout.todo_row, cursor,
                        from, to,chosenId, connected);
                setListAdapter(adapter);
                database.close();
                break;
            case R.id.spinnerMitte:
                links = String.valueOf(spinnerLinks.getSelectedItem());
                mitte = String.valueOf(spinnerMitte.getSelectedItem());
                rechts = String.valueOf(spinnerRechts.getSelectedItem());
                from = new String[]{links, mitte, rechts};
                database.open();
                cursor = database.getCursor(whereSQL, mitte);
                adapter = new AdapterOverview(this, R.layout.todo_row, cursor,
                        from, to, chosenId,connected);
                setListAdapter(adapter);
                database.close();
                break;
            case R.id.spinnerLinks:
                links = String.valueOf(spinnerLinks.getSelectedItem());
                mitte = String.valueOf(spinnerMitte.getSelectedItem());
                rechts = String.valueOf(spinnerRechts.getSelectedItem());
                from = new String[]{links, mitte, rechts};
                database.open();
                    String sqlSelect2 = "Next<=0 ";
                cursor = database.getCursor(sqlSelect2, links);
                adapter = new AdapterOverview(this, R.layout.todo_row, cursor,
                        from, to,chosenId, connected);
                setListAdapter(adapter);
                database.close();


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