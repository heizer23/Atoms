package app.lerner2.projects.my.lerner4;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import app.lerner2.projects.my.lerner4.Data.DatabaseEvents;

public class SimpleBrowserActiv extends Activity implements View.OnClickListener{

    WebView ourBrow;
    WebSettings webSettings;
    Button butBack, butSave, butLoad;
    Button buttConnect;
    EditText etUrl;
    Context myContext;
    private Bundle extras;
    int id;
    String url = "nichts";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.simplebrowser);
        myContext = this;
        ourBrow = findViewById(R.id.wvBrowser);
        butBack = findViewById(R.id.buttonBack);
        butLoad = findViewById(R.id.buttonLoad);
        butSave = findViewById(R.id.buttonSave);
        buttConnect = findViewById(R.id.buttonConnect);
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
        buttConnect.setOnClickListener(this);
        butSave.setOnClickListener(this);
        butLoad.setOnClickListener(this);
        webSettings = ourBrow.getSettings();
        ourBrow.clearSslPreferences();
        webSettings.setJavaScriptEnabled(true);

        ourBrow.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                try {
                   etUrl.setText(url);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return false;
            }
        });
    }







    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.buttonLoad:
                url = String.valueOf(etUrl.getText());
                ourBrow.loadUrl(url);

                //ourBrow.loadDataWithBaseURL("", url, null, null, "");
                break;
            case R.id.buttonConnect:
                getSelectedText();
                break;
            case R.id.buttonSave:
                String url = ourBrow.getUrl();
                Intent returnIntent = new Intent();
                returnIntent.putExtra("Url",url);
                setResult(Activity.RESULT_OK,returnIntent);
                finish();
                break;
            case R.id.buttonBack:
                ourBrow.goBack();
                break;
        }
    }

    private void getSelectedText(){
        ourBrow.evaluateJavascript("(function(){return window.getSelection().toString()})()",
                new ValueCallback<String>()
                {
                    @Override
                    public void onReceiveValue(String value)
                    {
                        String selectionString = value;
                        Intent intent = new Intent("lerner.lerner.QUESTIONLINKER");
                        intent.putExtra("sourceId", id);
                        intent.putExtra("url", url);
                        intent.putExtra("selectionText", value);
                        startActivity(intent);
                    }
                });
    }
}
