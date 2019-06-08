package app.lerner2.projects.my.lerner4;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import app.lerner2.projects.my.lerner4.Data.DbHelper;

public class SimpleBrowserActiv extends Activity {

    WebView ourBrow;
    private Bundle extras;
    int id;
    String url = "nichts";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.simplebrowser);

        ourBrow = (WebView) findViewById(R.id.wvBrowser);
        ourBrow.setWebViewClient(new WebViewClient());
        extras = getIntent().getExtras();
        if(extras !=null){
            url = extras.getString("StringInfo");
            id = extras.getInt("IntId");
        }
        ourBrow.loadUrl("https://www.google.de/");
        ourBrow.loadUrl(url);
    }


    @Override
    public void onBackPressed() {
        // do something on back.
        DbHelper dataBase = new DbHelper(this, this);
        String temp = ourBrow.getUrl();
        dataBase.saveOrt(id, ourBrow.getUrl());
        finish();
    }

}
