package fatec.com.br.appprofangela;

import android.app.Application;
import android.util.Log;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class App extends Application {

    public void onCreate() {
        super.onCreate();

        String applicationId = getString(R.string.parse_application_id);
        String clientKey = getString(R.string.parse_client_key);
        String serverParse = getString(R.string.parse_application_server);

        Parse.enableLocalDatastore(this);

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(applicationId)
                // if defined
                .clientKey(clientKey)
                .server(serverParse)
                .build()
        );
/*
        ParseObject object = new ParseObject("TesteCalsse");
        object.put("myNumber", "123456");
        object.put("myString", "berna");

        object.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException ex) {
                if (ex == null) {
                    Log.i("Parse Result", "Successful!");
                } else {
                    Log.i("Parse Result", "Failed" + ex.toString());
                }
            }
        });

*/
        //ParseUser.enableAutomaticUser();

        ParseACL defaultACL = new ParseACL();
        defaultACL.setPublicReadAccess(true);
        defaultACL.setPublicWriteAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);

    }
}
