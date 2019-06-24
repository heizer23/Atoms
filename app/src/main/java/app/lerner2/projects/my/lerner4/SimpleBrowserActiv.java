package app.lerner2.projects.my.lerner4;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;

import app.lerner2.projects.my.lerner4.Data.DatabaseEvents;

public class SimpleBrowserActiv extends Activity implements View.OnClickListener{

    WebView ourBrow;
    Button butBack;
    EditText etUrl;
    private Bundle extras;
    int id;
    String url = "nichts";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.simplebrowser);

        ourBrow = findViewById(R.id.wvBrowser);
        butBack = findViewById(R.id.buttonBack);
        etUrl = findViewById(R.id.etUrl);

        ourBrow.setWebViewClient(new WebViewClient());
        extras = getIntent().getExtras();
        if(extras !=null){
            url = extras.getString("url");
            id = extras.getInt("id");
        }
        ourBrow.loadUrl(url);
        etUrl.setText(url);
        butBack.setOnClickListener(this);
    }


    @Override
    public void onBackPressed() {
        String url = ourBrow.getUrl();
        Intent returnIntent = new Intent();
        returnIntent.putExtra("Url",url);
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }

    @Override
    public void onClick(View v) {
        url = String.valueOf(etUrl.getText());
        ourBrow.loadUrl(url);
    }
}
