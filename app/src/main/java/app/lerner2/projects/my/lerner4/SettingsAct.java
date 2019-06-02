package app.lerner2.projects.my.lerner4;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class SettingsAct extends Activity implements OnClickListener,
        OnSeekBarChangeListener {

    TextView tvLin, tvExp, tvRundenPunkte, tvNoOfQuestions;
    Button bVorschau, bPosAuswahl;
    Button[] buttNeueFrage = new Button[30];
    TextView tvVorsch;
    LinearLayout linNeueFrage;
    LinearLayout linVorschub;
    boolean[] neueFragen = new boolean[30];
    TextView tvVorschubLin;
    SeekBar sbVorschubLin;
    TextView tvVorschubExp;
    SeekBar sbVorschubExp;
    TextView tvVorschubGrenze;
    SeekBar sbVorschubGrenze;
    TextView tvDnDFrequenz;
    SeekBar sbDnDFrequenz;
    TextView tvDnDSize;
    SeekBar sbDnDSize;
    TextView tvMcTop2;
    SeekBar sbMcTop2;
    TextView tvMcBottom2;
    SeekBar sbMcBottom2;
    int lin;
    int exp;
    int noOfQuestions;
    int roundForPoints;
    int[] posNeueFragen;
    private Resources res;
    View buttonGrid;
    private int standardColor;
    ListView lvVorschau;
    TextView tvVorschau;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        res = getResources();
        Thread.setDefaultUncaughtExceptionHandler(new CustomExceptionHandler(
                res.getString(R.string.PathCrashReport), null));
        setContentView(R.layout.settings);
        linNeueFrage = (LinearLayout) findViewById(R.id.lin_neue_frage);
        linVorschub = (LinearLayout) findViewById(R.id.linlay_setting_vorschub);
        bPosAuswahl = (Button) findViewById(R.id.butt_setting_poswahl);
        bVorschau = (Button) findViewById(R.id.butt_setting_vorsch);
        tvVorschubLin = (TextView) findViewById(R.id.textViewVorschLin);
        sbVorschubLin = (SeekBar) findViewById(R.id.sb_vorschub_in);
        sbVorschubLin.setOnSeekBarChangeListener(this);
        sbVorschubLin.setProgress(MySingleton.getInstance().getVorschubLin());
        sbVorschubLin.setMax(20);
        tvVorschubLin.setText("" + sbVorschubLin.getProgress());
        sbVorschubExp = (SeekBar) findViewById(R.id.sb_vorschub_exp);
        tvVorschubExp = (TextView) findViewById(R.id.textViewvorschExp);
        sbVorschubExp.setOnSeekBarChangeListener(this);
        sbVorschubExp.setProgress(MySingleton.getInstance().getVorschubExp());
        sbVorschubExp.setMax(20);
        tvVorschubExp.setText("" + sbVorschubExp.getProgress());
        sbVorschubGrenze = (SeekBar) findViewById(R.id.sb_vorschubgrenze);
        tvVorschubGrenze = (TextView) findViewById(R.id.tvVorschubGrenze);
        sbVorschubGrenze.setOnSeekBarChangeListener(this);
        sbVorschubGrenze.setProgress(MySingleton.getInstance().getVorschubGrenze());
        sbVorschubGrenze.setMax(40);
        tvVorschubGrenze.setText("" + sbVorschubGrenze.getProgress());
        sbDnDFrequenz = (SeekBar) findViewById(R.id.sb_sort_freq);
        tvDnDFrequenz = (TextView) findViewById(R.id.textViewdndfreq);
        sbDnDFrequenz.setOnSeekBarChangeListener(this);
        //sbDnDFrequenz.setProgress(MySingleton.getInstance().getZzDnDFreq());
        sbDnDFrequenz.setMax(30);
        tvDnDFrequenz.setText("" + sbDnDFrequenz.getProgress());
        sbDnDSize = (SeekBar) findViewById(R.id.sb_sort_anz);
        tvDnDSize = (TextView) findViewById(R.id.textViewdndanz);
        sbDnDSize.setOnSeekBarChangeListener(this);
        //sbDnDSize.setProgress(MySingleton.getInstance().getZzDnDAnzFragen());
        sbDnDSize.setMax(20);
        tvDnDSize.setText("" + sbDnDSize.getProgress());
        sbMcTop2 = (SeekBar) findViewById(R.id.sb_versuche_top2);
        tvMcTop2 = (TextView) findViewById(R.id.textViewmcvers2);
        sbMcBottom2 = (SeekBar) findViewById(R.id.sb_versuche_bottom2);
        tvMcBottom2 = (TextView) findViewById(R.id.textViewmcversbo2);
        sbMcBottom2.setOnSeekBarChangeListener(this);
        //sbMcBottom2.setProgress(MySingleton.getInstance().getZzzMCbottom2());
        sbMcBottom2.setMax(20);
        tvMcBottom2.setText("" + sbMcBottom2.getProgress());
        sbMcTop2.setOnSeekBarChangeListener(this);
        //sbMcTop2.setProgress(MySingleton.getInstance().getZzzMCTop2());
        sbMcTop2.setMax(10);
        tvMcTop2.setText("" + sbMcTop2.getProgress());

        bPosAuswahl.setOnClickListener(this);
        bVorschau.setOnClickListener(this);

    }

    public void onClick(View v) {
        int temp = -1;
        switch (v.getId()) {
            case R.id.butt_setting_vorsch:
                if (tvVorschau == null) {
                    tvVorschau = new TextView(this);
                    String[] values = new String[] { "Android", "iPhone",
                            "WindowsMobile", "Blackberry", "WebOS", "Ubuntu",
                            "Windows7", "Max OS X", "Linux", "OS/2" };

                    tvVorschau.setTypeface(Typeface.SERIF);
                    setTvVorschau();
                    linVorschub.addView(tvVorschau);
                } else {
                    if (tvVorschau.getVisibility() == View.VISIBLE) {
                        tvVorschau.setVisibility(View.GONE);
                    } else {
                        tvVorschau.setVisibility(View.VISIBLE);
                    }
                }
                break;

        }

    }



    @Override
    public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
        switch (arg0.getId()) {
            case R.id.sb_vorschub_in:
                MySingleton.getInstance().setVorschubLin(arg1);
                tvVorschubLin.setText("" + arg1);

                try {
                    setTvVorschau();
                } catch (Exception e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                break;
            case R.id.sb_vorschub_exp:
                MySingleton.getInstance().setVorschubExp(arg1);
                tvVorschubExp.setText("" + arg1);

                try {
                    setTvVorschau();
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                break;
            case R.id.sb_vorschubgrenze:
                MySingleton.getInstance().setVorschubGrenze(arg1);
                tvVorschubGrenze.setText("" + arg1);
                break;

        }

    }

    @Override
    public void onStartTrackingTouch(SeekBar arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStopTrackingTouch(SeekBar arg0) {

    }



    private void setTvVorschau() {
        String out = "";
        int lin = MySingleton.getInstance().getVorschubLin();
        int exp = MySingleton.getInstance().getVorschubExp();
        for (int i = 0; i < 40; i++) {
            double uwf = i * 0.5;
            out = out + "\n     UWF    " + uwf + ": (linear) " + lin * uwf
                    + " + (exp) " + (int) Math.pow(1 + 0.1 * exp, uwf) + " = "
                    + (int) ((lin * uwf + Math.pow(1 + 0.1 * exp, uwf)));
        }
        tvVorschau.setText(out);
    }
}