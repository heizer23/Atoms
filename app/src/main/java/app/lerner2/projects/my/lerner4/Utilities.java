package app.lerner2.projects.my.lerner4;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class Utilities {
	Context ourContext;
	public ExportDatabaseFileTask expDb;
	
public Utilities(Context ourContext){
	this.ourContext = ourContext;
	expDb = new ExportDatabaseFileTask();
}

public class ExportDatabaseFileTask {
    private final ProgressDialog dialog = new ProgressDialog(ourContext);
    private Resources res;
    private String DB_NAME;
    private String DB_PATH;

    public Boolean export(Context c, String runde) {
        ourContext = c;
        res = c.getResources();
        DB_NAME = res.getString(R.string.DatabaseName);
        DB_PATH  = ourContext.getDatabasePath(DB_NAME).getAbsolutePath();



        DB_NAME = res.getString(R.string.DatabaseName);
        File dbFile = ourContext.getDatabasePath(DB_NAME);
               // new File(DB_PATH);

        String test = dbFile.getAbsolutePath();

       File exportDir = new File(Environment.getExternalStorageDirectory(), "/Quizzer/Backup");
       if (!exportDir.exists()) {
          exportDir.mkdirs();
       }
       //File file = new File(exportDir, dbFile.getName() + runde);
       File file = new File(exportDir, dbFile.getName());

       try {
          file.createNewFile();
          this.copyFile(dbFile, file);
          return true;
       } catch (IOException e) {
          Log.e("Utilities_ExportDb", e.getMessage(), e);
          return false;
       }
    }

    // can use UI thread here
    protected void feedBack(final Boolean success) {
       if (this.dialog.isShowing()) {
          this.dialog.dismiss();
       }
       if (success) {
          Toast.makeText(ourContext, "Export successful!", Toast.LENGTH_SHORT).show();
       } else {
          Toast.makeText(ourContext, "Export failed", Toast.LENGTH_SHORT).show();
       }
    }

    void copyFile(File src, File dst) throws IOException {
       FileChannel inChannel = new FileInputStream(src).getChannel();
       FileChannel outChannel = new FileOutputStream(dst).getChannel();
       try {
          inChannel.transferTo(0, inChannel.size(), outChannel);
       } finally {
          if (inChannel != null)
             inChannel.close();
          if (outChannel != null)
             outChannel.close();
       }
    }

 }
}

