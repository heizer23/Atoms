package app.lerner2.projects.my.lerner4;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;

public class FrageImage extends Fragment implements View.OnClickListener{
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private ImageView imageView;
    private TextView tvInfo;
    private Button[] bChoice = new Button[5];

    private Context ourContext;
    private View rootView;
    private LogicMC10 logic2;
    private String sButtText;
    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public FrageImage() {

        ourContext = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_image, container, false);
        imageView = (ImageView) rootView.findViewById(R.id.imageview);
        tvInfo = (TextView) rootView.findViewById(R.id.tvInfo);
        //  tvFrage = (TextView) findViewById(R.id.tvFrage);

        bChoice[0] = (Button) rootView.findViewById(R.id.button1);
        bChoice[1] = (Button) rootView.findViewById(R.id.button2);
        bChoice[2] = (Button) rootView.findViewById(R.id.button3);
        bChoice[3] = (Button) rootView.findViewById(R.id.button4);
        bChoice[4] = (Button) rootView.findViewById(R.id.button5);

        bChoice[0].setHapticFeedbackEnabled(true);
        for (int i = 0; i < bChoice.length; i++) {
            bChoice[i].setOnClickListener(this);
        }
        imageView.setOnClickListener(this);
        neueFrage();
        return rootView;
    }

    private void neueFrage(){
    //    logic2 = new LogicMC10(this,this);
        new DownloadImageTask(imageView).execute("http://media.designerpages.com/3rings/wp-content/themes/3rings-otto/i/?w=546&h=346&src=http%3A%2F%2Fmedia.designerpages.com%2F3rings%2Fwp-content%2Fuploads%2F2010%2F07%2Fhyundai-s-kitchen-nano-garden-dp.jpg");
        //setUpButtons();
        tvInfo.setText( MySingleton.getInstance().getInfo(sButtText));
    }




     public void setUpButtonsMC() {
        //String[] texts = logic2.getNextDates();// .getButtonTexts();
        String[] texts = logic2.getButtonTexts();
        for (int i = 0; i < 5; i++) {
            bChoice[i].setText(texts[i]);
        }
    }



    @Override
    public void onClick(View view) {
//        Utilities ut = new Utilities(ourContext);
//        ut.expDb.export(ourContext, "1");
        int temp = view.getId();
        if (view.getId() !=R.id.tvFrage ) {
            Button bTemp = (Button) view;
            sButtText = bTemp.getText().toString();
            boolean eingrenzRight = true;//= logic2.checkAnswer(sButtText);
            // erster bool bringt richtig/falsch, zweiter ob es eine eingrenzung war
            String infoText = " aktiv:" ;//+ MySingleton.getInstance().aktiviert + " deactiv: " + MySingleton.getInstance().deaktiviert + " Score: " + MySingleton.getInstance().score + " Next: " + MySingleton.getInstance().next + "\n MaxDiff: " + MySingleton.getInstance().maxDifference + " Antwort: " + sButtText;
            infoText = infoText + MySingleton.getInstance().vorschubText;

            tvInfo.setText(infoText);
            if (eingrenzRight) {
                bChoice[0].performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                tvInfo.setBackgroundColor(Color.GREEN);
                neueFrage();
            } else {
                tvInfo.setBackgroundColor(Color.RED);
                Vibrator v = (Vibrator)ourContext.getSystemService(Context.VIBRATOR_SERVICE);
                v.vibrate(250);
                neueFrage();
            }
            ;
        } else {
            Intent intent = new Intent();
            intent.setClass(ourContext, SimpleBrowserActiv.class);
            startActivity(intent);
        }
    }





private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    ImageView bmImage;

    public DownloadImageTask(ImageView bmImage) {
        this.bmImage = bmImage;
    }

    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        Bitmap mIcon11 = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return mIcon11;
    }

    protected void onPostExecute(Bitmap result) {
        bmImage.setImageBitmap(result);
    }
}
}

