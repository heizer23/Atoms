package app.lerner2.projects.my.lerner4;

import android.app.Application;

public class MyApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		// Initialize the singletons so their instances
		// are bound to the application process.
		//initSingletons();
	}

	public void customAppMethod() {
	}

}
