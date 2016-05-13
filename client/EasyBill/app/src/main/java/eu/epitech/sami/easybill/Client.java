package eu.epitech.sami.easybill;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by Sami on 09/05/2016.
 */
public class Client {
    private static Client               _instance;

    private static Socket               s;
    private static PrintWriter          outp;
    private static BufferedInputStream  inp;
    private static String               str;

    public synchronized static Client   getInstance()
    {
        if (_instance == null)
            _instance = new Client();
        return _instance;
    }

    private Client() {

    }

    public static void                  connect() {
        Log.d("status", "init");
        new Thread (new Runnable() {
            public void run() {
                try {
                    s = new Socket("10.14.59.120", 2345);

                    Log.d("status", "wait");

                    outp = new PrintWriter(s.getOutputStream());
                    inp = new BufferedInputStream(s.getInputStream());

                    Log.d("status", "connected");

                    readString();

                    Log.d("status", "confirmed");
                }
                catch (IOException e) {
                    Log.d("test", "no");
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static String   readString()
    {
        new Thread (new Runnable() {
            public void run() {
                byte[] line = new byte[4096];
                boolean flag = true;
                int tmp = 0;

                while (flag) {
                    try {
                        tmp = inp.read(line);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (line != null) {
                        flag = false;
                    }
                }
                str = new String(line, 0, tmp);
            }
        }).start();
        return str;
    }

    public static void     write(String str)
    {
        outp.write(str);
        outp.flush();
    }

    protected void  closeClient() {
        try {
            write("CLOSE");
            s.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
