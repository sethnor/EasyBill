package eu.epitech.sami.easybill;

import android.util.Log;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by Sami on 09/05/2016.
 */
public class Client {

    public Client() {
        try {
            Socket s = new Socket("10.14.59.120", 2345);

            Log.d("test", "yes");
            s.close();
        }
        catch (IOException e) {
            Log.d("test", "no");
            return ;
        }
    }
}
