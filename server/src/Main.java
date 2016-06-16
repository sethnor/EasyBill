public class Main {

    public static void main(String[] args) {

        String host = "10.14.59.180";
        int port = 2345;

        Server ts = new Server(host, port);
        ts.start();


        for(int i = 0; i < 0; i++){
            Thread t = new Thread(new ClientConnexion(host, port));
            t.start();
        }

    }
}