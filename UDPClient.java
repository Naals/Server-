import java.io.*;
import java.net.*;

public class UDPClient {
    private static DatagramSocket clientSocket;
    private static byte[] receiveData;
    private static byte[] sendData;
    private static DatagramPacket receivePacket;
    private static DatagramPacket sendPacket;
    private static final String QUIT="QUIT";
    private static final String KEYS="KEYS";
    private static final String PUT="PUT";
    private static final String DELETE="DELETE";
    private static final String GET="GET";
    private static final int PORT=4004;
    private static InetAddress IPAddress;

    public static void main(String[] args) throws IOException {
        // Main method to run the UDP client
        clientSocket = new DatagramSocket ();

        IPAddress = InetAddress. getByName("localhost");

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));){
            while (true) {
                System.out.print("\nPlease Input Command in either of the following forms:\n");
                System.out.print(String.format("%S <key>\n%S <key> <val>\n%S <key>\n%S\n%S\n",GET,PUT,DELETE,KEYS,QUIT));
                System.out.print("Enter Command: ");

                String command = reader.readLine();
                // Отправляем команду серверу

                sendData = command.getBytes();

                sendPacket = new DatagramPacket (sendData, sendData.length, IPAddress, PORT) ;

                clientSocket.send (sendPacket);

                // Если команда - выход, то выходим из цикла
                if (command.equalsIgnoreCase(QUIT)) {
                    break;
                }
                // Получаем и выводим ответ от сервера
                receiveData = new byte[256];

                receivePacket = new DatagramPacket (receiveData, receiveData.length) ;

                clientSocket.receive (receivePacket);

                String response = new String (receivePacket.getData ());

                System.out.println("Server response: " + response);
            }
        } catch (IOException e) {
            System.err.println(e);
        } finally {
            System.out.println("Клиент был закрыт...");
            clientSocket.close();
        }
    }

}

