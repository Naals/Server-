import java.io.IOException;
import java.net.*;
import java.util.HashMap;

public class UDPServer {
    private static HashMap<String, String> keyValStore;
    private static final int PORT=4004;
    private static InetAddress IPAddress;
    private static DatagramPacket receivePacket;
    private static DatagramPacket sendPacket;
    private static final String QUIT = "QUIT";
    private static final String KEYS = "KEYS";
    private static final String PUT = "PUT";
    private static final String DELETE = "DELETE";
    private static final String GET = "GET";
    private static byte[] sendData;
    private static byte[] receiveData;
    private static DatagramSocket serverSocket;
    private static InetAddress IP;
    private static int port;

    static {
        try {
            serverSocket = new DatagramSocket(PORT);
            keyValStore=new HashMap<>();
            System.out.println("Сервер запущен!");
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws IOException {
        // Main method to run the UDP server
        while (true) {
            receiveData = new byte[256];
            receivePacket = new DatagramPacket(receiveData, receiveData.length);
            serverSocket.receive(receivePacket);
            IP=receivePacket.getAddress();
            port=receivePacket.getPort();
            String line = new String(receivePacket.getData(), 0, receivePacket.getLength());

            if (line.equalsIgnoreCase(QUIT)) {
                break;
            } else if (line.startsWith(GET)) {
                timeOut();
                handleGet(line);
            } else if (line.startsWith(PUT)) {
                timeOut();
                handelPut(line);
            } else if (line.startsWith(DELETE)) {
                timeOut();
                handelDel(line);
            } else if (line.startsWith(KEYS)) {
                timeOut();
                handelKeys();
            } else {
                send("Unknown type!!!");
            }
        }

    }

    private static void handelKeys() {
        if (keyValStore.size() != 0) {
            StringBuilder keys = new StringBuilder();
            for (String s : keyValStore.keySet()) {
                keys.append(s + " ");
            }
            send(keys.toString());
        } else {
            send("There is nothing!!! Try Again!!!");
        }
    }

    private static void handelDel(String line) {
        String[] str = line.split(" ");
        if (str.length != 2) {
            send("Wrong format of command.");
        } else {
            if (keyValStore.size() != 0) {
                if (keyValStore.containsKey(str[1])) {
                    send("Key with \""+ keyValStore.remove(str[1])+ "\" deleted successfully");
                } else {
                    send("The key \"" + str[1] + "\" does not exists in the store");
                }
            } else {
                send("There is nothing!!! Try Again!!!");
            }
        }
    }

    private static void handelPut(String line) {
        String[] str = line.split(" ");
        if (str.length != 3) {
            send("Wrong format of command.");
        } else {
            if (check(str)) {
                keyValStore.put(str[1], str[2]);
                send("Success: Key-Value Pair saved on the server. \"" + str[1] + "\" with value \"" + str[2] + "\" saved successfully");
            } else {
                send("Error: Key or value too long (max 10 characters).");
            }
        }

    }

    private static void handleGet(String line) {
        String[] str = line.split(" ");
        if (str.length != 2) {
            send("Wrong format of command.");
        } else {
            if (keyValStore.size() != 0) {
                if (keyValStore.containsKey(str[1])) {
                    send(keyValStore.get(str[1]));
                } else {
                    send("The key \"" + str[1] + "\" does not exists in the store");
                }
            } else {
                send("There is nothing!!! Try Again!!!");
            }
        }
    }

    private static boolean check(String[] str) {
        boolean boo = true;
        for (String s : str) {
            if (s.length() > 10) {
                boo = false;
            }
        }
        return boo;
    }

    private static void send(String word) {
        try {
            // Отправляем команду серверу
            sendData = (word).getBytes();
            sendPacket = new DatagramPacket(sendData, sendData.length, IP, port);
            serverSocket.send(sendPacket);

        } catch (IOException e) {
            System.err.println(e);
        }
    }

    public static void timeOut() {
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            send(e.toString());
        }
    }
}
