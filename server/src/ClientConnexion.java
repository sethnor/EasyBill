import org.json.JSONArray;

import java.io.*;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;

public class ClientConnexion implements Runnable{

    private Socket connexion = null;
    private PrintWriter writer = null;
    private BufferedInputStream reader = null;

    private static int count = 0;
    private String name = "Client-";

    public ClientConnexion(String host, int port){
        name += ++count;
        try {
            connexion = new Socket(host, port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run(){
        Charset charset = Charset.forName("US-ASCII");
        String jsonCode = "";
        try (BufferedReader reader = Files.newBufferedReader(FileSystems.getDefault().getPath("", "cache-" + count + ".json"), charset)) {
            String line;
            while ((line = reader.readLine()) != null) {
                jsonCode += line;
            }
        } catch (NoSuchFileException e) {
            System.out.println("Create cache file : " + "cache-" + count + ".json");
            jsonCode = "[0]";
        } catch (IOException e) {
            e.printStackTrace();
        }
        JSONArray obj = new JSONArray(jsonCode);
        try {
            writer = new PrintWriter(connexion.getOutputStream(), true);
            reader = new BufferedInputStream(connexion.getInputStream());


            String cmd = "UPDATE_" + obj.get(obj.length() - 1).toString();
            writer.write(cmd);
            writer.flush();

            System.out.println("Commande " + cmd + " envoyée au serveur");

            String response = read();
            System.out.println("\t * " + name + " : Réponse reçue " + response);

            if (!response.equals("UP TO DATE")) {
                JSONArray res = new JSONArray(response);
                System.out.println("Save cache for date : " + res.get(res.length() - 1).toString());

                FileWriter f = new FileWriter(new File("cache-" + count + ".json"));
                f.write(res.toString());
                f.flush();
                f.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            writer = new PrintWriter(connexion.getOutputStream(), true);
            reader = new BufferedInputStream(connexion.getInputStream());

            String cmd = "PROCESS";
            writer.write(cmd);
            writer.flush();

            System.out.println("Commande " + cmd + " envoyée au serveur");

            String response = read();
            System.out.println("\t * " + name + " : Réponse reçue " + response);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        writer.write("CLOSE");
        writer.flush();
        writer.close();
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