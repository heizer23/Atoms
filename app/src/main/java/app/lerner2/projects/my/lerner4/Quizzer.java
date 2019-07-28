package app.lerner2.projects.my.lerner4;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import app.lerner2.projects.my.lerner4.Data.DatabaseEvents;


public class Quizzer extends AppCompatActivity implements View.OnClickListener {

    private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";

    private TextView tvt1;
    private TextView tvt2;
    private TextView tvt3;
    private TextView tvt4;
    private TextView tvt5;
    private TextView tvt6;
    private TextView tvt7;

    private TextView tvm1;
    private TextView tvm2;
    private TextView tvm3;
    private TextView tvm4;
    private TextView tvm5;
    private TextView tvm6;
    private TextView tvm7;

    private TextView tvmb1;
    private TextView tvmb2;
    private TextView tvmb3;
    private TextView tvmb4;
    private TextView tvmb5;
    private TextView tvmb6;
    private TextView tvmb7;

    private TextView tvu1;
    private TextView tvu2;
    private TextView tvu3;
    private TextView tvu4;
    private TextView tvu5;
    private TextView tvu6;
    private TextView tvu7;

    private TextView tvFrage;
    private TextView tvInfo;
    private LinearLayout layRechtsTop;
    private LinearLayout layLinksTop;

    private Button[] bChoice = new Button[10];

    private Context ourContext;
    private Activity act;
    private String sButtText;
    private FrageDatum actFrage;

    private DatabaseEvents dbEvents;

    long[] oldPotential;
    long[] actPotential;
    long[] oldPunkte;
    long[] actPunkte;
    long[] oldTotal;
    long oldTotalLong;
    long[] actTotal;
    int[] oldStatus;
    int[] actStatus;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quizzer);


        tvt1 = findViewById(R.id.tv_t1);
        tvt2 = findViewById(R.id.tv_t2);
        tvt3 = findViewById(R.id.tv_t3);
        tvt4 = findViewById(R.id.tv_t4);
        tvt5 = findViewById(R.id.tv_t5);
        tvt6 = findViewById(R.id.tv_t6);
        tvt7 = findViewById(R.id.tv_t7);

        tvm1 = findViewById(R.id.tv_m1);
        tvm2 = findViewById(R.id.tv_m2);
        tvm3 = findViewById(R.id.tv_m3);
        tvm4 = findViewById(R.id.tv_m4);
        tvm5 = findViewById(R.id.tv_m5);
        tvm6 = findViewById(R.id.tv_m6);
        tvm7 = findViewById(R.id.tv_m7);

        tvmb1 = findViewById(R.id.tv_mb1);
        tvmb2 = findViewById(R.id.tv_mb2);
        tvmb3 = findViewById(R.id.tv_mb3);
        tvmb4 = findViewById(R.id.tv_mb4);
        tvmb5 = findViewById(R.id.tv_mb5);
        tvmb6 = findViewById(R.id.tv_mb6);
        tvmb7 = findViewById(R.id.tv_mb7);

        tvu1 = findViewById(R.id.tv_u1);
        tvu2 = findViewById(R.id.tv_u2);
        tvu3 = findViewById(R.id.tv_u3);
        tvu4 = findViewById(R.id.tv_u4);
        tvu5 = findViewById(R.id.tv_u5);
        tvu6 = findViewById(R.id.tv_u6);
        tvu7 = findViewById(R.id.tv_u7);

        act = this;
        ourContext = this ;
        dbEvents = new DatabaseEvents(this,this);

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
        oldTotalLong = dbEvents.getTotalVorschub();
        neueFrage();
    }

    public void neueFrage(){
        oldPotential = actPotential;
        oldPunkte = actPunkte;
     //   oldTotal = actTotal;
        oldStatus = actStatus;

        actFrage = new FrageDatum(this, this, null);
        tvFrage.setText(actFrage.logic.getQuestion());
        setGui();
        MathStuff ms = new MathStuff();
        oldTotal = ms.getTimingDigits(oldTotalLong);
        actPotential = ms.getTimingDigits(actFrage.getDeltaNextLastdate());

        GameMech gMech = new GameMech();
        long i = gMech.getTagesPunkte();
        i = i+1;
    }

    private void setGui(){
        switch (actFrage.getFrageModus()){
            case "compare":
                layLinksTop.setVisibility(View.GONE);
                layRechtsTop.setVisibility(View.GONE);
                setUpButtons();
                break;
            case "multipleChoice":
                layLinksTop.setVisibility(View.VISIBLE);
                layRechtsTop.setVisibility(View.VISIBLE);
                setUpButtons();
                break;
        }
    }

    public void setUpButtons() {
        String[] texts = actFrage.logic.getButtonTexts();
        for (int i = 0; i < texts.length; i++) {
            bChoice[i].setText(texts[i]);
        }
    }

    private void evaluateButton(Button view) {
        Button bTemp = view;
        sButtText = bTemp.getText().toString();
        tvInfo.performHapticFeedback(3);

        if (actFrage.logic.qualifyAnswer((sButtText))) {      // Eingrenzungsantwort
            tvInfo.setText(actFrage.getFeedbackString());
            tvFrage.setText(actFrage.logic.getQuestion());
            setUpButtons();
        } else{
            actFrage.logic.checkAnswer(sButtText);

            onUpdateInfo();
            neueFrage();
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.tvFrage ) {
            startBrowser();
        }else {
            evaluateButton((Button) view);
        }
    }

    public void onUpdateInfo(){
        if (!(actFrage == null)) {


//            long[] oldDetla = ms.getTimingRelative(actFrage.getNext());
//            long[] totalDelta = ms.getTimingAbsolute(dbEvents.getTotalVorschub());

            MathStuff ms = new MathStuff();
            actPunkte = ms.getTimingRelativeDigits(actFrage.getNext());
            actTotal = ms.getTimingDigits(dbEvents.getTotalVorschub());
            actStatus = actFrage.getActHistogram();


            tvt1.setText("Pot: ");
            tvt2.setText(actPotential[0] +"");
            tvt3.setText(actPotential[1] +"");
            tvt4.setText(actPotential[2] +"");
            tvt5.setText(actPotential[3] +"");
            tvt6.setText(actPotential[4] +"");
            tvt7.setText(actPotential[5] +"");


            tvm1.setText("Punkte");
            tvm2.setText(actPunkte[0] +"");
            tvm3.setText(actPunkte[1] +"");
            tvm4.setText(actPunkte[2] +"");
            tvm5.setText(actPunkte[3] +"");
            tvm6.setText(actPunkte[4] +"");
            tvm7.setText(actPunkte[5] +"");

            tvmb1.setText("Total");
            tvmb2.setText(actTotal[0] +"");
            tvmb3.setText(actTotal[1] +"");
            tvmb4.setText(actTotal[2] +"");
            tvmb5.setText(actTotal[3] +"");
            tvmb6.setText(actTotal[4] +"");
            tvmb7.setText(actTotal[5] +"");

            tvu1.setText("(" + actStatus[0] +")");
            tvu7.setText(actStatus[1] +"");
            tvu6.setText(actStatus[2] +"");
            tvu5.setText(actStatus[3] +"");
            tvu4.setText(actStatus[4] +"");
            tvu3.setText(actStatus[5] +"");
            tvu2.setText(actStatus[6] +"");


            if(!(oldTotal == null)){

                int[][] colorArray = getBackgroundColor(oldTotal, actTotal);

                int[] color = colorArray[0];
                tvmb2.setBackgroundColor(Color.argb(color[0], color[1],color[2],color[3]));
                color = colorArray[1];
                tvmb3.setBackgroundColor(Color.argb(color[0], color[1],color[2],color[3]));
                color = colorArray[2];
                tvmb4.setBackgroundColor(Color.argb(color[0], color[1],color[2],color[3]));
                color = colorArray[3];
                tvmb5.setBackgroundColor(Color.argb(color[0], color[1],color[2],color[3]));
                color = colorArray[4];
                tvmb6.setBackgroundColor(Color.argb(color[0], color[1],color[2],color[3]));
                color = colorArray[5];
                tvmb7.setBackgroundColor(Color.argb(color[0], color[1],color[2],color[3]));
            }

            tvInfo.setText(actFrage.getFeedbackString());
        }
    }


    private int[][] getBackgroundColor(long[] old, long[] act){

        int length = act.length;
        int[][] result  = new int[length][length];
        int[] timeframe = {5, 12, 30, 24, 60, 60};

        long delta;
        int alpha;
        int red;
        int green;
        int blue;

        for(int i = 0; i<length; i++){
            delta = act[i] - old[i];


            if(delta<0){
                alpha =  255+ (int)(((double)delta/ timeframe[i])*200);
                red = 250;
                green = 0;
            }else if(delta>0){

                alpha =  255- (int)(((double)delta/ timeframe[i])*200);

                red = 0;
                green = 100;
            }else{
                red = 0;
                green = 0;
                alpha = 0;
            }



            blue = 0;
            int[] color = {alpha, red, green, blue};
            result[i] =  color;
        }
        return result;
    }

    private void startBrowser() {
        Intent intent = new Intent();
        String[] urlInfo = actFrage.getUrl();
        intent.putExtra("id", Integer.parseInt(urlInfo[0]));
        intent.putExtra("url", urlInfo[1]);
        intent.setClass(ourContext, SimpleBrowserActiv.class);
        // startActivity(intent);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                String url=data.getStringExtra("Url");
                actFrage.setUrl(url);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
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
                intent = new Intent(this, ItemViewAct.class);
                intent.putExtra("_id", actFrage.getId());
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
