package arbalest.sample;

import android.app.Application;

import arbalest.Arbalest;

public class ArbalestApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Arbalest.initialize(this, "your application id here", "your rest api key here");
    }

    @Override
    public void onTerminate() {
        Arbalest.destroy();
        super.onTerminate();
    }
}
