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

import app.lerner2.projects.my.lerner4.Data.DbHelper;

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
        void onUpdate(String[] infoStrings);
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
        tvFrage = rootView.findViewById(R.id.tvFrage);
        tvInfo = rootView.findViewById(R.id.tvInfo);
        //  tvFrage = (TextView) findViewById(R.id.tvFrage);

        bChoice[0] = rootView.findViewById(R.id.button1);
        bChoice[1] = rootView.findViewById(R.id.button2);
        bChoice[2] = rootView.findViewById(R.id.button3);
        bChoice[3] = rootView.findViewById(R.id.button4);
        bChoice[4] = rootView.findViewById(R.id.button5);
        bChoice[5] = rootView.findViewById(R.id.button6);
        bChoice[6] = rootView.findViewById(R.id.button7);
        bChoice[7] = rootView.findViewById(R.id.button8);
        bChoice[8] = rootView.findViewById(R.id.button9);
        bChoice[9] = rootView.findViewById(R.id.button10);

        layRechts = rootView.findViewById(R.id.linlayrechts);

        bChoice[0].setHapticFeedbackEnabled(true);
        for (int i = 0; i < bChoice.length; i++) {
            bChoice[i].setOnClickListener(this);
        }
        logic = new LogicMC10(ourContext, act);
        neueFrage();
        return rootView;
    }

    public void neueFrage(){
        tvFrage.setText(logic.neueFrage());
        setUpButtonsMC();
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

           // tvInfo.setBackgroundColor(color[(int)eingrenzRight[1]]);
            if (eingrenzRight[0] == 0 ) {      // Eingrenzungsantwort
                tvInfo.setText("Korrekt ");
                setUpButtonsMC();
            } else{
                String[] infoStrings = logic.getRundenInfo();
                mCallback.onUpdate(infoStrings);
                tvInfo.setText(infoStrings[6]);
                neueFrage();
            }
        }
    }




}


