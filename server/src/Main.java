public class Main {

    public static void main(String[] args) {

        String host = "192.168.2.10";//"10.14.59.180";
        int port = 9090;

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