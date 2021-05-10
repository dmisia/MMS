package pl.edu.pwr.lab46.i236764;

import android.app.Application;

import pl.edu.pwr.lab46.i236764.QuizApp;

public class MyApplication extends Application {
    @Override
    public void onCreate()
    {
        super.onCreate();
        QuizApp.init(this);
    }

}
