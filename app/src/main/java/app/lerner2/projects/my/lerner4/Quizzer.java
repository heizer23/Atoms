package app.lerner2.projects.my.lerner4;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


public class Quizzer extends AppCompatActivity implements View.OnClickListener {

    private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";

    private TextView tvO1;
    private TextView tvO2;
    private TextView tvu1;
    private TextView tvu2;
    private TextView tvu3;
    private TextView tvu4;
    private TextView tvu5;

    private TextView tvFrage;
    private TextView tvInfo;
    private LinearLayout layRechtsTop;
    private LinearLayout layLinksTop;

    private Button[] bChoice = new Button[10];

    private Context ourContext;
    private Activity act;
    private Logic logic;
    private String sButtText;

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

        act = this;
        ourContext = this ;
        tvFrage = findViewById(R.id.tvFrage);
        tvInfo = findViewById(R.id.tvInfo);

        bChoice[0] = findViewById(R.id.button1);
        bChoice[1] = findViewById(R.id.button2);
        bChoice[2] = findViewById(R.id.button3);
        bChoice[3] = findViewById(R.id.button4);
        bChoice[4] = findViewById(R.id.button5);
        bChoice[5] = findViewById(R.id.button6);
        bChoice[6] = findViewById(R.id.button7);
        bChoice[7] = findViewById(R.id.button8);
        bChoice[8] = findViewById(R.id.button9);
        bChoice[9] = findViewById(R.id.button10);

        layRechtsTop = findViewById(R.id.linlayrechtstop);
        layLinksTop = findViewById(R.id.linlaylinkstop);

        bChoice[0].setHapticFeedbackEnabled(true);
        for (int i = 0; i < bChoice.length; i++) {
            bChoice[i].setOnClickListener(this);
        }
        tvFrage.setOnClickListener(this);
        logic = new LogicMC10(ourContext, act);
        neueFrage();
    }

    public void neueFrage(){
        logic.neueFrage();
        tvFrage.setText(logic.getItem());
        setUpButtonsMC();
    }

    public void setUpButtonsMC() {
        String[] texts = logic.getButtonTexts();
        for (int i = 0; i < texts.length; i++) {
            bChoice[i].setText(texts[i]);
        }
    }

    private void hideButtons(){
        layLinksTop.setVisibility(View.GONE);
        layRechtsTop.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View view) {
//        Utilities ut = new Utilities(ourContext);
//        ut.expDb.export(ourContext, "1");

        if (view.getId() == R.id.tvFrage ) {
            Intent intent = new Intent();
            String[] urlInfo = logic.getUrl();
            intent.putExtra("id", Integer.parseInt(urlInfo[0]));
            intent.putExtra("url", urlInfo[1]);
            intent.setClass(ourContext, SimpleBrowserActiv.class);
            startActivity(intent);
        }else {
            Button bTemp = (Button) view;
            sButtText = bTemp.getText().toString();
            double[] eingrenzRight = logic.checkAnswer(sButtText);
            tvInfo.performHapticFeedback(3);

            // tvInfo.setBackgroundColor(color[(int)eingrenzRight[1]]);
            if (eingrenzRight[0] == 0 ) {      // Eingrenzungsantwort
                tvInfo.setText("Korrekt ");
                setUpButtonsMC();
            } else{
                String[] infoStrings = logic.getRundenInfo();
                onUpdateInfo(infoStrings);
                tvInfo.setText(infoStrings[6]);
                neueFrage();
            }
        }
    }

    public void onUpdateInfo(String[] infoStrings) {
        tvO1.setText(infoStrings[0]);
        tvO2.setText(infoStrings[1]);
        tvu1.setText(infoStrings[2]);
        tvu2.setText(infoStrings[3]);
        tvu3.setText(infoStrings[4]);
        tvu4.setText(infoStrings[5]);
       // tvu5.setText(infoStrings[6]);
    }

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
                //DbHelper dbHelper1 = new DbHelper(this,this);
                // dbHelper1.putInQuestions();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
