import org.json.JSONArray;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;

public class Server {

    private int port = 2345;
    private String host = "127.0.0.1";
    private ServerSocket server = null;
    private boolean isRunning = true;

    public Server(String pHost, int pPort){
        host = pHost;
        port = pPort;
        try {
            server = new ServerSocket(port, 100, InetAddress.getByName(host));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private JSONArray getQuestionFile() {
        Charset charset = Charset.forName("UTF-8");
        String jsonCode = "";
        try (BufferedReader reader = Files.newBufferedReader(FileSystems.getDefault().getPath("", "questions.json"), charset)) {
            String line;
            while ((line = reader.readLine()) != null) {
                jsonCode += line;
            }
        } catch (IOException e) {
            e.printStackTrace();
            jsonCode = "[]";
        }
        JSONArray obj = new JSONArray(jsonCode);
        long date = new File("questions.json").lastModified();
        //obj.put(date);
        return obj;
    }

    public void start(){
        JSONArray obj = getQuestionFile();
        //System.out.println(obj);
        if (server == null) {
            System.out.println("Cannot start server, maybe ip is not valid");
            return;
        }
        System.out.println("Server running on " + host + ":" + port);
        Thread t = new Thread(new Runnable(){
            public void run(){
                while(isRunning){
                    try {
                        Socket client = server.accept();
                        System.out.println("Connexion from " + client.getRemoteSocketAddress());
                        Thread t = new Thread(new ClientProcessor(client, obj));
                        t.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    server.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    server = null;
                }
            }
        });
        t.start();
    }
}