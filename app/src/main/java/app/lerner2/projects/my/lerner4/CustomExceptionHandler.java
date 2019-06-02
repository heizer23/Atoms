package app.lerner2.projects.my.lerner4;

import android.os.Environment;
import android.text.format.Time;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;


public class CustomExceptionHandler implements UncaughtExceptionHandler {

	private String localPath;
//	private String url;
	private UncaughtExceptionHandler defaultUEH;

	public CustomExceptionHandler(String localPath, String url){
		this.localPath = localPath;
	//	this.url = url;
		this.defaultUEH = Thread.getDefaultUncaughtExceptionHandler();
	}
	
		
	public void uncaughtException(Thread thread, Throwable ex) {
		final Writer result = new StringWriter();
		final PrintWriter printWriter = new PrintWriter(result);
		ex.printStackTrace(printWriter);
		String stacktrace = result.toString();
		printWriter.close();
		Time now = new Time();
		now.setToNow();
		
		String filename  =   now.toString().substring(4, 13)+".txt";
		if (localPath != null){
			writeToFile(stacktrace, filename);
		}
		defaultUEH.uncaughtException(thread, ex);
	}


	private void writeToFile(String stacktrace, String filename) {
	
		try{
			File f = new File(Environment.getExternalStorageDirectory(), "/Quizzer/Debug");
			f.mkdirs();
			f = new File(f,  "/" + filename);
			BufferedWriter bos = new BufferedWriter(new FileWriter(f));
			bos.write(stacktrace);
			bos.flush();
			bos.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}

}
