package app.lerner2.projects.my.lerner4;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import app.lerner2.projects.my.lerner4.Data.DatabaseEvents;

/**
 * Created by Philip on 07.06.2015.
 */
public class ItemViewAct extends Activity{

    EditText tv1, tv2, tv3, tv4;
    EditText etItem, etAnswer;
    DatabaseEvents dbHelper;
    String[] data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itemview);

        tv1 = (EditText) findViewById(R.id.textView1);
        tv2 = (EditText) findViewById(R.id.textView2);
        tv3 = (EditText) findViewById(R.id.textView3);
        tv4 = (EditText) findViewById(R.id.textView4);
        etItem = (EditText) findViewById(R.id.etItem);
        etAnswer = (EditText) findViewById(R.id.etAnswer);

        Intent intent = getIntent();
        int id = intent.getIntExtra("_id", 1);

        dbHelper = new DatabaseEvents(this,this);
        dbHelper.open();
        data = dbHelper.getItemSQL("_id = "+ id, null);
        dbHelper.close();

        etItem.setText(data[1]);
        etAnswer.setText(data[2]);
        tv1.setText(data[4]);
        tv2.setText(data[5]);
        double ratio = Double.parseDouble(data[4])/Double.parseDouble(data[5]);
        ratio = Math.round(ratio*10.0)/10.0;
        tv3.setText(""+ ratio);
        tv4.setText(data[3]);

    }


}
