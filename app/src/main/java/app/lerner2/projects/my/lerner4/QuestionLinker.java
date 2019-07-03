package app.lerner2.projects.my.lerner4;


import android.app.Activity;
import android.content.res.Resources;
import android.database.SQLException;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import app.lerner2.projects.my.lerner4.Data.DatabaseEvents;
import app.lerner2.projects.my.lerner4.Data.DatabaseHelper;


public class QuestionLinker extends Activity implements View.OnClickListener{

    private TextView tvSourceQuestion, tvRelation;
    private EditText etCopiedText, etDestQuestion, etAnswer, etConnection, etLink;
    private Button buttonSave, b1, b2, b3, buttonSwitch;
    private int idSource;
    private boolean earlier = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Resources res = getResources();
        Thread.setDefaultUncaughtExceptionHandler(new CustomExceptionHandler(
                res.getString(R.string.PathCrashReport), null));

        setContentView(R.layout.question_linker);
        tvSourceQuestion = findViewById(R.id.tvSourceQuestion);
        tvRelation = findViewById(R.id.tvRelation);
        etCopiedText = findViewById(R.id.etCopiedText);
        etDestQuestion = findViewById(R.id.etDestQuestion);
        etAnswer = findViewById(R.id.etAnswer);
        etConnection = findViewById(R.id.etConnection);
        etLink = findViewById(R.id.etLink);
        buttonSave = findViewById(R.id.buttonSave);

        buttonSwitch = findViewById(R.id.buttonSwitch);
        b1 = findViewById(R.id.button1);
        b2 = findViewById(R.id.button2);
        b3 = findViewById(R.id.button3);
        buttonSave.setOnClickListener(this);
        b1.setOnClickListener(this);
        b2.setOnClickListener(this);
        b3.setOnClickListener(this);
        buttonSwitch.setOnClickListener(this);

        Bundle extras;
        extras = getIntent().getExtras();
        if(extras !=null){
            idSource = extras.getInt("sourceId");
            FrageDatum frage = new FrageDatum(this, this, idSource);
            tvSourceQuestion.setText(frage.getItem());
            etCopiedText.setText(extras.getString("selectionText"));
            etLink.setText(extras.getString("url"));
        }
    }

    @Override
    public void onClick(View v) {
        int startSelection=etCopiedText.getSelectionStart();
        int endSelection=etCopiedText.getSelectionEnd();

        String selectedText = etCopiedText.getText().toString().substring(startSelection, endSelection);
        switch (v.getId()){
            case R.id.button1:
                etDestQuestion.setText(selectedText);
                break;
            case R.id.button2:
                etAnswer.setText(selectedText);
                break;
            case R.id.button3:
                etConnection.setText(selectedText);
                break;
            case R.id.buttonSave:
                saveQuestion();
                break;
            case R.id.buttonSwitch:
                if(earlier){
                    tvRelation.setText("später");
                    earlier = false;
                }else{
                    tvRelation.setText("früher");
                    earlier = true;
                }
                break;
        }
    }

    private void saveQuestion(){
        //try {
            String item = etDestQuestion.getText().toString();
            String datumString = etAnswer.getText().toString().trim();
            int datum = Integer.parseInt(datumString);
            String ort = etLink.getText().toString();
            String verbindung = etConnection.getText().toString();
            DatabaseEvents dbEvents = new DatabaseEvents(this, this);
            DatabaseHelper dbHelper = new DatabaseHelper(this, this);

            dbEvents.open();
            int newID = dbEvents.makeNewFrage(item, datum, ort);
            dbEvents.close();

            if(earlier){
                dbHelper.linkItemsPlus(newID, idSource, verbindung, ort);
            }else{
                dbHelper.linkItemsPlus(idSource, newID, verbindung, ort);
            }
      //  } catch (SQLException e) {
//            Toast.makeText(this.getApplicationContext(), "Error",Toast.LENGTH_LONG).show();
//            e.printStackTrace();
//        }
    }
}
