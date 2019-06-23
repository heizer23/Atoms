package app.lerner2.projects.my.lerner4;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import app.lerner2.projects.my.lerner4.Data.DatabaseEvents;

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
            url = extras.getString("url");
            id = extras.getInt("id");
        }
        ourBrow.loadUrl(url);
    }


    @Override
    public void onBackPressed() {
        DatabaseEvents dbEvents = new DatabaseEvents(this, this);
        String temp = ourBrow.getUrl();
        dbEvents.saveOrt(id, ourBrow.getUrl());
        finish();
    }

}
