package Client;

import java.io.*;
import java.net.Socket;

public class Client {
    private int socketPort;
    private final String socketHost = "localhost";
    private Socket clientSocket;
    private DataInputStream dis;
    private DataOutputStream out;
    private BufferedReader reader =new BufferedReader(new InputStreamReader(System.in));


    public static void main(String[] args) throws IOException, InterruptedException {
        Client client = new Client(8000);
        while (client.clientSocket.isOutputShutdown()) {
            if (client.reader.ready()) {
                System.out.println("Введите команду: ");
                String clientCommand = client.reader.readLine();
                client.out.writeUTF(clientCommand);
                client.out.flush();
                System.out.println("Отправка команды \"" + clientCommand + "\" на сервер...");
                Thread.sleep(1000);
            }
        }
    }

    public Client(int socketPort) {
        this.socketPort = socketPort;
        try {
            clientSocket = new Socket(socketHost, socketPort);
            dis = new DataInputStream(clientSocket.getInputStream());
            out = new DataOutputStream(clientSocket.getOutputStream());
        } catch (IOException exception) {
            exception.printStackTrace();
            System.err.println("Ошибка при создании клиента!");
        }
        System.out.println("Клиент успешно создан на порту " + socketPort);
    }



    public String file(String nameFile) throws IOException {
        out.writeUTF(nameFile);
        return dis.readUTF();
    }

    public String print(String nameMethod) throws IOException {
        if (clientSocket.isOutputShutdown()) {
            return "stop";
        }
        out.writeUTF(nameMethod);
        return dis.readUTF();
    }

    public void setData(String nameMethod, String data) throws IOException {
        if (clientSocket.isOutputShutdown())  {
            return;
        }

        out.writeUTF(nameMethod);
        out.writeUTF(data);
    }

    public void setData(String nameMethod, String data1, String data2) throws IOException {
        if (clientSocket.isOutputShutdown()) {
            return;
        }
        out.writeUTF(nameMethod);
        out.writeUTF(data1);
        out.writeUTF(data2);
    }
}
