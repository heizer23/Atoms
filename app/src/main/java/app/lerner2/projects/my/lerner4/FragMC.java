package app.lerner2.projects.my.lerner4;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FragMC extends Fragment implements View.OnClickListener{
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private TextView tvFrage;
    private TextView tvInfo;
    private LinearLayout layRechts;

    private Button[] bChoice = new Button[10];

    private Context ourContext;
    private Activity act;
    private View rootView;
    private LogicMC10 logic;
    private String sButtText;


    OnUpdateListener mCallback;

    // Container Activity must implement this interface
    public interface OnUpdateListener {
        public void onUpdate(String[] infoStrings);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnUpdateListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnUpdateListener");
        }
    }



    public FragMC() {
       }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_main, container, false);
        act = getActivity();
        ourContext = act ;
        tvFrage = (TextView) rootView.findViewById(R.id.tvFrage);
        tvInfo = (TextView) rootView.findViewById(R.id.tvInfo);
        //  tvFrage = (TextView) findViewById(R.id.tvFrage);

        bChoice[0] = (Button) rootView.findViewById(R.id.button1);
        bChoice[1] = (Button) rootView.findViewById(R.id.button2);
        bChoice[2] = (Button) rootView.findViewById(R.id.button3);
        bChoice[3] = (Button) rootView.findViewById(R.id.button4);
        bChoice[4] = (Button) rootView.findViewById(R.id.button5);
        bChoice[5] = (Button) rootView.findViewById(R.id.button6);
        bChoice[6] = (Button) rootView.findViewById(R.id.button7);
        bChoice[7] = (Button) rootView.findViewById(R.id.button8);
        bChoice[8] = (Button) rootView.findViewById(R.id.button9);
        bChoice[9] = (Button) rootView.findViewById(R.id.button10);

        layRechts = (LinearLayout) rootView.findViewById(R.id.linlayrechts);

        bChoice[0].setHapticFeedbackEnabled(true);
        for (int i = 0; i < bChoice.length; i++) {
            bChoice[i].setOnClickListener(this);
        }
        logic = new LogicMC10(ourContext, act);
        neueFrage();
        return rootView;
    }

    public void neueFrage(){
        int fragenId;
        String[] buttTexts;

        DbHelper dbHelper = new DbHelper(ourContext,act);
        dbHelper.open();
        fragenId = dbHelper.nextFrage();
        dbHelper.close();

        tvFrage.setText(logic.neueFrage(fragenId));

//        if( logic.getVorschub()< MySingleton.getInstance().getVorschubGrenze()) {
//            layRechts.setVisibility(View.GONE);
//            buttTexts = logic.getButtonTextsMC4();
//        }else{
            layRechts.setVisibility(View.VISIBLE);
            buttTexts = logic.getButtonTexts();
  //      }

        for (int i = 0; i < buttTexts.length; i++) {
            bChoice[i].setText(buttTexts[i]);
        }

    }

    public void setUpButtonsMC() {
        String[] texts = logic.getButtonTexts();
        for (int i = 0; i < texts.length; i++) {
            bChoice[i].setText(texts[i]);
        }
    }

    @Override
    public void onClick(View view) {
//        Utilities ut = new Utilities(ourContext);
//        ut.expDb.export(ourContext, "1");

        if (view.getId() == R.id.tvFrage ) {

        }else {
            int[] color = {Color.GREEN, Color.RED, Color.BLUE};
            Button bTemp = (Button) view;
            sButtText = bTemp.getText().toString();
            double[] eingrenzRight = logic.checkAnswer(sButtText);
            tvInfo.performHapticFeedback(3);

            String[] infoStrings = new String[8];

            infoStrings[0] = " Round: " + MySingleton.getInstance().getNext();
            infoStrings[1] = " Count: " + MySingleton.getInstance().getCount() + " (" + MySingleton.getInstance().getAktiviert() + ")";
            infoStrings[2] = " S: " + eingrenzRight[2] ;
            infoStrings[3] = " C: " + (int)eingrenzRight[3] ;
            infoStrings[4] = " R: " + eingrenzRight[4] ;
            infoStrings[5] = " V:  "  + (int)eingrenzRight[5] ;
            infoStrings[6] = " N: "  + (int)eingrenzRight[6] ;

            mCallback.onUpdate(infoStrings);

            String infoText =  MySingleton.getInstance().vorschubText;
            tvInfo.setText(infoText);

            tvInfo.setBackgroundColor(color[(int)eingrenzRight[1]]);
            if (eingrenzRight[0] == 0 ) {      // Eingrenzungsantwort
                setUpButtonsMC();
            } else if (eingrenzRight[1] == 0) { //Antwort richtig
                neueFrage();
            } else {                          //Antwort falsch
                neueFrage();
            }
            ;
        }
    }




}


