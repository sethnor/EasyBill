package eu.epitech.sami.easybill;

import android.content.Context;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
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
    private static Context              mContext;

    public synchronized static Client   getInstance(Context context)
    {
        if (_instance == null)
            _instance = new Client(context.getApplicationContext());
        return _instance;
    }

    private Client(Context context) {
        mContext = context;
    }

    public static void                  connect() {
        Log.d("status", "init");
        new Thread (new Runnable() {
            public void run() {
                try {
                    s = new Socket("10.14.59.180", 2345);

                    Log.d("status", "wait");

                    outp = new PrintWriter(s.getOutputStream());
                    inp = new BufferedInputStream(s.getInputStream());

                    Log.d("status", "connected");

                    readString();

                    if (str != null)
                        Log.d("status", str);
                    else
                        Log.d("status", "fail");
                }
                catch (IOException e) {
                    Log.d("test", "no");
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static void   readString()
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
                        Log.d("response", new String(line, 0, tmp));
                    }
                    if (line != null) {
                        Log.d("string", new String (line, 0, tmp));
                        flag = false;
                    }
                }
                str = new String(line, 0, tmp);
            }
        }).start();
    }

    public static void     write(String str)
    {
        if (str != null) {
            outp.write(str);
            outp.flush();
        }
    }

    public static void     update()
    {
        try {
            write("UPDATE_0");
            Log.d("status", "update sent");

            OutputStreamWriter writer = new OutputStreamWriter(mContext.openFileOutput("questions.txt", mContext.MODE_PRIVATE));

            writer.write(str);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String    getString()
    {
        return str;
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

    public static void      sendPic(String path) {

        try {
            OutputStream outputStream = s.getOutputStream();
            FileInputStream in;

            in = new FileInputStream(path);
            // Write to the stream:
            byte[] buffer = new byte[1000024]; // 1KB buffer size
            int length = 0;
            while ( (length = in.read(buffer, 0, buffer.length)) != -1 ){
                outputStream.write(buffer, 0, length);
            }
            outputStream.flush();

            if (in != null) in.close();
//            s.close(); // Will close the outputStream, too.

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
