package app.lerner2.projects.my.lerner4;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import app.lerner2.projects.my.lerner4.Data.DbHelper;


public class Quizzer extends AppCompatActivity implements FragMC.OnUpdateListener {



    /**
     * The serialization (saved instance state) Bundle key representing the
     * current dropdown position.
     */
    private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";

    private TextView tvO1;
    private TextView tvO2;
    private TextView tvu1;
    private TextView tvu2;
    private TextView tvu3;
    private TextView tvu4;
    private TextView tvu5;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quizzer);

        tvO1 = findViewById(R.id.tv_o1);
        tvO2 = findViewById(R.id.tv_o2);
        tvu1 = findViewById(R.id.tv_u1);
        tvu2 = findViewById(R.id.tv_u2);
        tvu3 = findViewById(R.id.tv_u3);
        tvu4 = findViewById(R.id.tv_u4);
        tvu5 = findViewById(R.id.tv_u5);

        getFragmentManager().beginTransaction()
                .replace(R.id.container, new FragMC())
                .commit();


    }


//    @Override
//    public void onRestoreInstanceState(Bundle savedInstanceState) {
//        // Restore the previously serialized current dropdown position.
//        if (savedInstanceState.containsKey(STATE_SELECTED_NAVIGATION_ITEM)) {
//            getSupportActionBar().setSelectedNavigationItem(
//                    savedInstanceState.getInt(STATE_SELECTED_NAVIGATION_ITEM));
//        }
//    }
//
//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        // Serialize the current dropdown position.
//        outState.putInt(STATE_SELECTED_NAVIGATION_ITEM,
//                getSupportActionBar().getSelectedNavigationIndex());
//    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.quizzer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent intent;
        switch (id){
            case  R.id.action_settings:
                intent = new Intent("lerner.lerner.SETTINGS");
                startActivity(intent);
            break;
            case  R.id.action_lections:
                intent = new Intent("lerner.lerner.LEKAUSWAHL");
                startActivity(intent);
                break;
            case  R.id.action_info:
                intent = new Intent("lerner.lerner.OVERVIEW");
                startActivity(intent);
                break;
            case  R.id.action_sqlactiv:
                intent = new Intent("lerner.lerner.SQLACTIV");
                startActivity(intent);
                break;
            case  R.id.action_showtiming:
                intent = new Intent("lerner.lerner.TIMINGLIST");
                startActivity(intent);
                break;
            case  R.id.action_extra:
                DbHelper dbHelper1 = new DbHelper(this,this);
                dbHelper1.putInQuestions();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onUpdate(String[] infoStrings) {
        tvO1.setText(infoStrings[0]);
        tvO2.setText(infoStrings[1]);
        tvu1.setText(infoStrings[2]);
        tvu2.setText(infoStrings[3]);
        tvu3.setText(infoStrings[4]);
        tvu4.setText(infoStrings[5]);
        tvu5.setText(infoStrings[6]);
    }
}
