import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client {
    private static Socket socket;
    private static DataInputStream dis;
    private static DataOutputStream out;

    /**
     * Конструктор для создания клиента
     */
    public Client() throws IOException {
        socket = new Socket("localhost",8000);
        dis = new DataInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());
    }

    public static String file(String nameFile) throws IOException {
        out.writeUTF(nameFile);
        return dis.readUTF();
    }

    public static String print(String nameMethod) throws IOException {
        if (socket.isOutputShutdown()) return "stop";

        out.writeUTF(nameMethod);
        return dis.readUTF();
    }

    public static void setData(String nameMethod, String data) throws IOException {
        if (socket.isOutputShutdown()) return;

        out.writeUTF(nameMethod);
        out.writeUTF(data);
    }

    public static void setData(String nameMethod, String data1, String data2) throws IOException {
        if (socket.isOutputShutdown()) return;

        out.writeUTF(nameMethod);
        out.writeUTF(data1);
        out.writeUTF(data2);
    }
}
