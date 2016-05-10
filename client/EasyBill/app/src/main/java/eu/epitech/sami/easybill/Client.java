package eu.epitech.sami.easybill;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by Sami on 09/05/2016.
 */
public class Client extends AsyncTask<Void, Void, Void> {

    private Socket s;

    @Override
    protected Void doInBackground(Void... param) {
        try {
            s = new Socket("10.14.59.120", 2345);

            Log.d("test", "yes");

        }
        catch (IOException e) {
            Log.d("test", "no");
            e.printStackTrace();
        }

        return null;
    }
}
