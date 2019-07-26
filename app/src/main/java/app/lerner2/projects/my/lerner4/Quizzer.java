package app.lerner2.projects.my.lerner4;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

    private TextView tvO1;
    private TextView tvO2;
    private TextView tvu1;
    private TextView tvu2;
    private TextView tvu3;
    private TextView tvu4;
    private TextView tvu5;
    private TextView tvu6;

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
        tvu6 = findViewById(R.id.tv_u6);

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
        neueFrage();
    }

    public void neueFrage(){
        actFrage = new FrageDatum(this, this, null);
        tvFrage.setText(actFrage.logic.getQuestion());
        setGui();
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
            MathStuff ms = new MathStuff();

            tvO1.setText("Next: " + ms.getTimingRelative(actFrage.getNext()));
            // long delta = MySingleton.getInstance().getDelta();
            long delta = dbEvents.getTotalVorschub();
            tvO2.setText("Delta: " + ms.getTimingAbsolute(delta));
            int[] statusInfo = actFrage.getActHistogram();
            tvu1.setText("C "+statusInfo[0]);
            tvu2.setText("s "+statusInfo[1]);
            tvu3.setText("m "+statusInfo[2]);
            tvu4.setText("h "+statusInfo[3]);
            tvu5.setText("d "+statusInfo[4]);
            tvu6.setText("m "+statusInfo[5]);

            tvInfo.setText(actFrage.getFeedbackString());
        }
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
