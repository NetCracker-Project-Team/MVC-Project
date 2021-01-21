package Client;

import java.io.*;
import java.net.Socket;

public class Client {
    private Socket clientSocket;
    private DataInputStream dis;
    private DataOutputStream out;
    private final BufferedReader reader =new BufferedReader(new InputStreamReader(System.in));


    public static void main(String[] args) throws IOException, InterruptedException {
        Client client = null;
        try {
            client = new Client(8000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(client!=null) {
            System.out.println("Введите команду: ");
            String clientCommand = client.reader.readLine();
            client.out.writeUTF(clientCommand);
            client.out.flush();
            System.out.println("Отправка команды \"" + clientCommand + "\" на сервер...");
            Thread.sleep(1000);
            System.out.println("reading...");
            String in = client.dis.readUTF();
            System.out.println("Ответ с сервера: " + in);
            System.out.println("Завершение работы клиентской стороны...");

        } else {
            System.out.println("Завершение работы клиентской стороны...");
        }
    }


    public Client(int socketPort) {
        try {
            String socketHost = "localhost";
            clientSocket = new Socket(socketHost, socketPort);
            dis = new DataInputStream(clientSocket.getInputStream());
            out = new DataOutputStream(clientSocket.getOutputStream());
            System.out.println("Клиент успешно создан на порту " + socketPort);
        } catch (IOException exception) {
            exception.printStackTrace();
            System.err.println("Ошибка при создании клиента!");
        }
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
