import org.json.JSONArray;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;

public class ClientProcessor implements Runnable{

    private Socket sock;
    private PrintWriter writer = null;
    private BufferedInputStream reader = null;
    private JSONArray obj;

    public ClientProcessor(Socket pSock, JSONArray pObj){
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

    public void run(){
        boolean closeConnexion = false;
        while(!sock.isClosed()){
            try {
                writer = new PrintWriter(sock.getOutputStream());
                reader = new BufferedInputStream(sock.getInputStream());

                String response = read();
                String toSend;

                switch(response.split("_")[0]){
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
                    default :
                        toSend = "UNKNOWN";
                        break;
                }

                if(closeConnexion){
                    writer = null;
                    reader = null;
                    sock.close();
                    break;
                }
                writer.write(toSend);
                writer.flush();

            } catch(SocketException e){
                System.err.println("Problem with connexion");
                break;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String read() throws IOException{
        String response;
        int stream;
        byte[] b = new byte[4096];
        stream = reader.read(b);
        response = new String(b, 0, stream);
        return response;
    }
}