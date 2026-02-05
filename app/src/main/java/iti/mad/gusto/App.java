package iti.mad.gusto;

import android.app.Application;
import android.util.Log;

import com.google.firebase.FirebaseApp;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(getApplicationContext());

        Log.d("TAG", "onCreate: Application");
    }
}
