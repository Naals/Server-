import java.io.*;
import java.net.*;

public class TCPClient {
    private static final String QUIT="QUIT";
    private static final String KEYS="KEYS";
    private static final String PUT="PUT";
    private static final String DELETE="DELETE";
    private static final String GET="GET";
    private static BufferedWriter out;
    private static BufferedReader in;
    private static BufferedReader reader;
    private static Socket client;
    public static final int PORT=4041;
    public static final String HOST="localhost";

    public static void main(String[] args) {
        try {
            client = new Socket(HOST, PORT);
            reader = new BufferedReader(new InputStreamReader(System.in));
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));

            while (true) {
                System.out.print("\nPlease Input Command in either of the following forms:\n");
                System.out.print(String.format("%S <key>\n%S <key> <val>\n%S <key>\n%S\n%S\n",GET,PUT,DELETE,KEYS,QUIT));
                System.out.print("Enter Command: ");
                String command = reader.readLine();
                // Отправляем команду серверу
                out.write(command + "\n");
                out.flush();

                // Если команда - выход, то выходим из цикла
                if (command.equalsIgnoreCase(QUIT)) {
                    break;
                }

                // Получаем и выводим ответ от сервера
                String response=in.readLine();
                System.out.println("Server response: " + response);


            }
        } catch (IOException e) {
            System.err.println(e);
        } finally {
            try {
                System.out.println("Клиент был закрыт...");
                client.close();
                in.close();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
}

