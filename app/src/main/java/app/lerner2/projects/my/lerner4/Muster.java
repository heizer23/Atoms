package app.lerner2.projects.my.lerner4;

import android.app.ListActivity;
import android.content.res.Resources;
import android.os.Bundle;

public class Muster extends ListActivity {

    private Resources res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        // requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.overview);
        res = getResources();
        Thread.setDefaultUncaughtExceptionHandler(new CustomExceptionHandler(
                res.getString(R.string.PathCrashReport), null));

    }
}
