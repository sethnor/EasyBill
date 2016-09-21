public class Main {

    public static void main(String[] args) {

        String host = "192.168.43.32";//"10.14.60.28";
        int port = 80;

        Server ts = new Server(host, port);
        ts.start();

        /* Testing */
        /*
        for(int i = 0; i < 1; i++){
            Thread t = new Thread(new ClientConnexion(host, port));
            t.start();
        }
        */
    }
}