import org.json.JSONArray;
import org.opencv.core.Point;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;

public class ClientProcessor implements Runnable {

    private Socket sock;
    private PrintWriter writer = null;
    private BufferedInputStream reader = null;
    private JSONArray obj;

    public ClientProcessor(Socket pSock, JSONArray pObj) {
        sock = pSock;
        obj = pObj;
    }

    private String updateCache(String response) {
        if (response.split("_")[1].equals(obj.get(obj.length() - 1).toString()))
            return "UP TO DATE";
        return (obj.toString());
    }

    private String process(String response) {
        System.out.println(response);
        return "PAR ENCORE FAIT";
    }

    public void run() {
        boolean closeConnexion = false;
        try {
            writer = new PrintWriter(sock.getOutputStream());
            reader = new BufferedInputStream(sock.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (!sock.isClosed()) {
            try {

                String response = new String();
                byte[] b = new byte[4096];
                int stream = reader.read(b);
                if (stream > 0) {
                    response = new String(b, 0, stream);
                }
                if (reader.available() > 1) {
                    FileOutputStream f = new FileOutputStream("tmp.jpg");
                    f.write(b);
                    while (reader.available() > 1) {
                        b = new byte[4096];
                        reader.read(b);
                        f.write(b);
                    }
                    f.flush();
                    f.close();
                    Processor p = new Processor();
                    List<List<Point>> list = p.run("tmp.jpg");
                    writer.write(list.toString());
                    writer.flush();
//                    System.out.println(list.toString());
                } else {
                    String toSend;

                    switch (response.split("_")[0]) {
                        case "UPDATE":
                            toSend = updateCache(response);
                            break;
                        case "PROCESS":
                            toSend = process(response);
                            break;
                        case "CLOSE":
                            toSend = "";
                            closeConnexion = true;
                            break;
                        default:
                            toSend = "UNKNOWN";
                            break;
                    }

                    if (closeConnexion) {
                        writer = null;
                        reader = null;
                        sock.close();
                        break;
                    }
                    writer.write(toSend);
                    writer.flush();
                }
            } catch (SocketException e) {
                //e.printStackTrace();
                System.err.println("Problem with connexion");
                break;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}