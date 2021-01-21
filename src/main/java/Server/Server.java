package Server;

import controller.Controller;
import controller.Serialize;
import model.Category;
import model.Dish;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    /**
     * интерфейс управления потоками
     */
    static ExecutorService executeIt = Executors.newCachedThreadPool();
    private int serverPort;

    public Server(int serverPort) {
        this.serverPort = serverPort;
    }


    public static void main(String[] args) {
        Server server = new Server(8000);
        try {
            ServerSocket serverSocket = new ServerSocket(server.serverPort);
            Socket serverClient = serverSocket.accept();
            executeIt.execute(new ServerRun(serverClient));
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private static class ServerRun implements Runnable {
        /**
         * поле для сокета клиента
         */
        private final Socket clientSocket;
        /**
         * поле для списка блюд
         */
        public List<Dish> dishes;
        /**
         * поле для списка категорий
         */
        public List<Category> categories;
        /**
         * поле для файла блюд
         */
        private File file;

        /**
         * конструктор - создание потока
         *
         * @param client - сокет клиента
         */
        public ServerRun(Socket client) {
            this.clientSocket = client;
        }

        /**
         * метод запуска потока
         */
        @Override
        public void run() {
            try {
                DataInputStream in = new DataInputStream(clientSocket.getInputStream());
                DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
                String nameFile = in.readUTF();
                long dataModifFile = 0;

                if (!nameFile.equals("newDish")) {
                    file = new File(nameFile);
                    dataModifFile = file.lastModified();
                    if (file.length() > 0) {
                        dishes = Serialize.deserialize(file);
                        categories = List.of(dishes.get(0).getCategory());
                        Controller.addCategoryByDish(dishes, categories);
                        out.writeUTF("not");
                    } else {
                        out.writeUTF("empty");
                    }
                } else {
                    out.writeUTF("empty");
                }
                out.flush();

                while (!clientSocket.isClosed()) {
                    if (dataModifFile != 0) {
                        if (dataModifFile != file.lastModified()) {
                            out.writeUTF("Yes");
                        } else {
                            out.writeUTF("No");
                        }
                    } else {
                        out.writeUTF("No");
                    }
                    out.flush();
                    String method = in.readUTF();
                    if (method.equals("Stop")) {
                        out.flush();
                        break;
                    }
                    int numDish;
                    Dish dish;
                    String name;
                    String nameCategory;
                    double price;
                    String clientReaction;
                    switch (method) {
                        case "setDataByNumber":
                            numDish = in.readInt();
                            dish = Serialize.deserializeDish(in);
                            Controller.setDataByNumber(numDish, dish, dishes);
                        case "setDataByName":
                            name = in.readUTF();
                            dish = Serialize.deserializeDish(in);
                            Controller.setDataByName(name, dish, dishes);
                        case "setCategoryByName":
                            name = in.readUTF();
                            nameCategory = in.readUTF();
                            Controller.setCategoryByName(name, new Category(nameCategory), dishes);
                        case "setNameByName":
                            name = in.readUTF();
                            String newName = in.readUTF();
                            Controller.setNameByName(name, newName, dishes);
                        case "setPriceByName":
                            name = in.readUTF();
                            price = in.readDouble();
                            Controller.setPriceByName(name, price, dishes);
                        case "addData":
                            dish = Serialize.deserializeDish(in);
                            if (Controller.compareCategories(categories, dish.getCategory())) {
                                out.writeUTF("newCategory");
                                out.flush();
                                clientReaction = in.readUTF();
                                if (clientReaction.equals("Yes")) {
                                    if (Controller.addData(dish, dishes, categories)) {
                                        out.writeUTF("Yes");
                                    } else {
                                        out.writeUTF("No");
                                    }
                                    out.flush();
                                }
                            } else {
                                if (Controller.addData(dish, dishes, categories)) {
                                    out.writeUTF("Yes");
                                } else {
                                    out.writeUTF("No");
                                }
                                out.flush();
                            }
                        case "deleteData":
                            name = in.readUTF();
                            Controller.deleteData(name, dishes);
                        case "printDish":
                            out.writeUTF(Controller.printDish(dishes).toString());
                            out.flush();
                        case "addFile":
                            name = in.readUTF();
                            File otherFile = new File(name);
                            if (otherFile.length() > 0) {
                                List<Dish> otherDishes = Serialize.deserialize(otherFile);
                                if (Controller.addFile(dishes, otherDishes)) {
                                    out.writeUTF("Yes");
                                } else {
                                    out.writeUTF("No");
                                }

                            } else {
                                out.writeUTF("No");
                            }
                            out.flush();
                        case "addCategory":
                            nameCategory = in.readUTF();
                            if (Controller.addCategory(new Category(nameCategory), categories)) {
                                out.writeUTF("Yes");
                            } else {
                                out.writeUTF("No");
                            }
                            out.flush();
                        case "printDishByCategory":
                            nameCategory = in.readUTF();
                            out.writeUTF(Controller.printDishByCategory(nameCategory, dishes).toString());
                            out.flush();
                        case "printCategory":
                            out.writeUTF(Controller.printCategory(categories).toString());
                            out.flush();
                        case "getDataByName":
                            name = in.readUTF();
                            out.writeUTF(Controller.getDataByName(name, dishes).toString());
                            out.flush();
                        case "getDataByCategory":
                            nameCategory = in.readUTF();
                            out.writeUTF(Controller.getDataByCategory(nameCategory, dishes).toString());
                            out.flush();
                        case "saveFile":
                            Serialize.serialize(dishes, file);
                            dataModifFile = file.lastModified();
                        case "saveNewFile":
                            name = in.readUTF();
                            File newFile = new File(name);
                            Serialize.serialize(dishes, newFile);
                            dataModifFile = newFile.lastModified();
                        case "openNewFile":
                            name = in.readUTF();
                            newFile = new File(name);
                            if (newFile.length() > 0) {
                                dishes = Serialize.deserialize(newFile);
                                categories = List.of(dishes.get(0).getCategory());
                                Controller.addCategoryByDish(dishes, categories);
                                dataModifFile = newFile.lastModified();
                            }
                    }
                }
                in.close();
                out.close();
                System.out.println("Завершение работы");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}


