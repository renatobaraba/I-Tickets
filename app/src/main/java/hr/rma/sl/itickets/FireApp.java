package hr.rma.sl.itickets;

import android.app.Application;

import com.firebase.client.Firebase;

/**
 * Created by Darko on 21-May-18.
 */

public class FireApp extends Application {

    @Override
    public void onCreate(){
        super.onCreate();

        Firebase.setAndroidContext(this);
    }
}
