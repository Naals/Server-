import java.io.*;
import java.net.Socket;


public class Server extends Thread {
    private static final String QUIT = "QUIT";
    private static final String KEYS = "KEYS";
    private static final String PUT = "PUT";
    private static final String DELETE = "DELETE";
    private static final String GET = "GET";
    private Socket socket;
    private TCPServer tcpServer;
    private BufferedReader in;
    private BufferedWriter out;

    public Server(Socket socket) throws IOException {
        this.socket = socket;
        tcpServer = new TCPServer();
        // если потоку ввода/вывода приведут к генерированию исключения, оно пробросится дальше
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        start();
    }

    public void run() {
        while (socket.isConnected()) {
            try {
                String line = in.readLine();


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

            } catch (IOException e) {
            }
        }
    }

    private void handelKeys() {
        if(tcpServer.getKeyValStore().size()!=0){
            StringBuilder keys=new StringBuilder();
            for(String s:tcpServer.getKeyValStore().keySet()){
                keys.append(s+" ");
            }
            send(keys.toString());
        }
        else{
            send("Key did not find!!! Try Again!!!");
        }
    }

    private void handelDel(String line) {
        String[] str = line.split(" ");
        if(str.length!=2){
            send("Wrong format of command.");
        }
        else {
            if (tcpServer.getKeyValStore().size() != 0) {
                if (tcpServer.getKeyValStore().containsKey(str[1])) {
                    send("Key with \""+ tcpServer.getKeyValStore().remove(str[1])+ "\" deleted successfully");
                } else {
                    send("The key \"" + str[1] + "\" does not exists in the store");
                }
            } else {
                send("There is nothing!!! Try Again!!!");
            }
        }
    }

    private void handelPut(String line) {
        String[] str=line.split(" ");
        if(str.length!=3){
            send("Wrong format of command.");
        }
        else{
            if(check(str)){
                tcpServer.getKeyValStore().put(str[1],str[2]);
                send("Success: Key-Value Pair saved on the server. \""+str[1]+"\" with value \""+str[2]+"\" saved successfully");
            }
            else{
                send("Error: Key or value too long (max 10 characters).");
            }
        }

    }

    private void handleGet(String line) {
        String[] str = line.split(" ");
        if(str.length!=2){
            send("Wrong format of command.");
        }
        else {
            if (tcpServer.getKeyValStore().size() != 0) {
                if (tcpServer.getKeyValStore().containsKey(str[1])) {
                    send(tcpServer.getKeyValStore().get(str[1]));
                } else {
                    send("The key \"" + str[1] + "\" does not exists in the store");
                }
            } else {
                send("There is nothing!!! Try Again!!!");
            }
        }
    }
    private boolean check(String[] str){
        boolean boo=true;
        for(String s:str){
            if(s.length()>10){
                boo= false;
            }
        }
        return boo;
    }
    private void send(String word) {
        try {
            out.write(word + "\n");
            out.flush();
        } catch (IOException e) {
            System.err.println(e);
        }
    }
    public void timeOut(){
        try{
            Thread.sleep(1);
        }catch(InterruptedException e){
            send(e.toString());
        }
    }


}
