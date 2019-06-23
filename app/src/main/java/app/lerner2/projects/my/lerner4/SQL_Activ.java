package app.lerner2.projects.my.lerner4;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import app.lerner2.projects.my.lerner4.Data.DatabaseHelper;

/**
 * Created by Philip on 19.07.2015.
 */
public class SQL_Activ extends Activity implements View.OnClickListener{

    EditText etSQL;
    Button butExecute, butExport;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_sql);
        etSQL = (EditText) findViewById(R.id.etSQL);
        butExecute = (Button) findViewById(R.id.butExecute);
        butExport = (Button) findViewById(R.id.butExport);

        butExport.setOnClickListener(this);
        butExecute.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.butExecute:
                DatabaseHelper databaseHelper = new DatabaseHelper(this,this);
                databaseHelper.open();
                databaseHelper.runSQL(etSQL.getText().toString());
                databaseHelper.close();
                break;
            case R.id.butExport:
                Utilities uts = new Utilities(this);
                uts.expDb.export(this, "erfolg");
                break;
        }
    }
}
