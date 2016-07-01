import org.json.JSONArray;
import org.opencv.core.Point;

import java.io.*;
import java.net.Socket;
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

    private String readResponse() {
        try {
            String response = "";
            byte[] b = new byte[8192];
            int stream = reader.read(b);
            if (stream == -1) {
                return ("KO");
            }
            response = new String(b, 0, stream);
            if (reader.available() > 0) {
                FileOutputStream f = new FileOutputStream("tmp.jpg");
                f.write(b);
                while (reader.available() > 0) {
                    b = new byte[8192];
                    stream += reader.read(b);
                    f.write(b);
                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException e) {

                    }
                }
                f.flush();
                f.close();
                Processor p = new Processor();
                List<List<List<Double>>> list = p.run("tmp.jpg");
                writer.write(list.toString());
                writer.flush();
                System.out.println("img:" + list.toString());
                return ("IMG");
            }
            return (response);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ("KO");
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

            String response = readResponse();
            if (response.equals("KO"))
                break;
            if (!response.equals("IMG")) {
                String toSend;
                switch (response.split("_")[0]) {
                    case "UPDATE":
                        toSend = updateCache(response);
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
                    try {
                        sock.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                }
                writer.write(toSend);
                writer.flush();
            }
        }
    }
}