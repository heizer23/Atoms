package app.lerner2.projects.my.lerner4;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by Philip on 07.06.2015.
 */
public class ItemViewAct extends Activity implements View.OnClickListener{

    EditText tvScore, tv2, tv3, tv4;
    EditText etItem, etAnswer, etUrl;
    Button buttonSave;
    FrageDatum actFrage;
    String[] data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itemview);

        tvScore = findViewById(R.id.textView1);
        tv2 = findViewById(R.id.textView2);
        tv3 = findViewById(R.id.textView3);
        tv4 = findViewById(R.id.textView4);
        etItem = findViewById(R.id.etItem);
        etAnswer = findViewById(R.id.etAnswer);
        etUrl = findViewById(R.id.etUrl);
        buttonSave = findViewById(R.id.buttonSave);
        buttonSave.setOnClickListener(this);

        Intent intent = getIntent();
        int id = intent.getIntExtra("_id", 1);

        actFrage = new FrageDatum(this, this, id);

        setUpFields();

    }

    private void setUpFields(){
        etItem.setText(actFrage.getItem());
        etAnswer.setText(String.format("%d", actFrage.getDatum()));
        etUrl.setText(actFrage.getUrl()[1]);
        tvScore.setText(String.format("%s", actFrage.getScore()));
        tv2.setText(String.format("%d", actFrage.getCounter()));
        double ratio = actFrage.getScore()/actFrage.getCounter();
        ratio = Math.round(ratio*10.0)/10.0;
        tv3.setText(String.format("%s", ratio));
        MathStuff ms  = new MathStuff();
        tv4.setText(ms.getTimingRelative(actFrage.getNext()));
    }

    private void saveData(){
        actFrage.setItem(String.valueOf(etItem.getText()));
        actFrage.setDatum(Integer.parseInt(String.valueOf(etAnswer.getText())));
        actFrage.setScore(Double.parseDouble(String.valueOf(tvScore.getText())));
        actFrage.setUrl(String.valueOf(etUrl.getText()));
        actFrage.saveInfos();
    }

    @Override
    public void onClick(View v) {
        saveData();
    }
}
