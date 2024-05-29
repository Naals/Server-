import java.io.*;
import java.net.*;
import java.util.*;

public class TCPServer {
    public static LinkedList<Server> servers = new LinkedList<>();
    private static Socket client;
    private static ServerSocket server;
    private static BufferedWriter writer;
    private static BufferedReader reader;
    private final HashMap<String, String> keyValStore=new HashMap<>();
    private static final String QUIT="QUIT";
    private static final String KEYS="KEYS";
    private static final String PUT="PUT";
    private static final String DELETE="DELETE";
    private static final String GET="GET";
    public static final int PORT=4041;


    public static void main(String[] args) {
        // Main method implementation

        try {
            try {
                server = new ServerSocket(PORT);
                System.out.println("Сервер запущен!");
                try {
                    while(true) {
                        client = server.accept();
                        System.out.println("Client accepted");
                        try {
                            servers.add(new Server(client));
                        } catch (IOException e) {


                        }

                        reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
                        writer = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
                    }

                } finally {
                    client.close();
                    reader.close();
                    writer.close();
                }
            }finally{
                System.out.println("Сервер закрыт!");
                server.close();
            }

        }
        catch(IOException e){
            System.err.println(e);
        }
    }
    public HashMap<String, String> getKeyValStore() {
        return keyValStore;
    }

}

