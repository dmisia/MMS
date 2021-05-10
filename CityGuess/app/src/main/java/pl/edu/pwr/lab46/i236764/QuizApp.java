package pl.edu.pwr.lab46.i236764;

import android.app.Application;
import android.content.Context;


public class QuizApp extends Application {

    private static QuizApp app;
    public static PreferencesHelper preferencesHelper;

    public static void init(Context context) {
        if (app == null)
            app = new QuizApp(context);
    }

    public QuizApp(Context context) {
        preferencesHelper = new PreferencesHelper(context);
    }

}
