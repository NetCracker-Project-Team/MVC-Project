package View;

import Controller.Controller;
import Model.Category;
import Model.Dish;
import Controller.Serialize;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

/**
 * Класс - представление
 */
public class View {

    /** Поле для сканнера */
    private static Scanner in = new Scanner(System.in);
    /** Поле для правила выбора пунктов меню */
    private static String selectionRule = " *выберите пункт меню, чтобы вы хотели сделать";
    /** Поле для выбора пункта меню пользователем */
    private static String selection;
    /** Поле для неправильного выбора пункта меню */
    private static String wrongChoice = "\n\t!!Вы неправильно выбрали пункт меню!!" +
                                        "\n\tХотите попробовать снова?\n\t1 - да\n\t2 - нет";
    /** Поле для пустого меню */
    private static String emptyMenu = "\n\tВы не можете выполнить операцию. Так как меню пустое!\n" +
                                      "\tПопробуйте снова.";
    /** Поле для проверки наличия меню в ресторане. true - блюда есть, false - блюд нет */
    private static  boolean ok = false;
    /** Поле для файла блюд */
    private static File file;
    /** Поле для файла категорий*/
    private static File fileCategory;


    /**
    *Метод главного меню
     */
    public static void mainMenu(){
        System.out.println(selectionRule);
        System.out.println("\n\t\t---Главное меню:---");
        System.out.println("\t1 - Загрузить меню ресторана\n" +
                           "\t2 - Cохранить меню ресторана\n" +
                           "\t3 - Посмотреть данных\n" +
                           "\t4 - Редактировать меню ресторана\n" +
                           "\t5 - Выйти из программы" );
        System.out.print("\n---Ваш выбор: ");
        selection = in.next();
        switch(selection){
            case "1":
                download(0);
                break;
            case "2":
                if (!ok){
                    System.out.println(emptyMenu);
                    mainMenu();
                }
                else{
                    save();
                }
                break;
            case "3":
                if (!ok){
                    System.out.println(emptyMenu);
                    mainMenu();
                }
                else{
                    showMenu();
                }
                break;
            case "4":
                if (!ok){
                    System.out.println(emptyMenu);
                    mainMenu();
                }
                else{
                    editMenu();
                }
                break;
            case "5":
                return;
            default:
                System.out.println(wrongChoice);
                System.out.print("\n---Ваш выбор: ");
                selection = in.next();
                if (selection.equals("1")){
                    mainMenu();
                }
        }
    }

    /**
     *Метод для загрузки меню ресторана
     * @throws IOException
     */
    private static void download(int k){
        System.out.println("\n\t\t---Загрузка меню ресторона---");
        try {
            System.out.print("\nПуть к файлу для меню: ");
            file = new File(in.next());
            System.out.print("\nПуть к файлу для категорий: ");
            fileCategory = new File(in.next());
            Serialize.deserialize(file);
            Serialize.deserializeCategory(fileCategory);
            System.out.println("\t\tЗагрузка выполнена успешно!\n" );
            if (k == 0) {
                System.out.println("\t1 - посмотреть меню ресторана\n" +
                                   "\t2 - вернуться в главное меню");
                selection = in.next();
                ok = true;
                if (selection.equals("1")) {
                    showMenu();
                } else {
                    mainMenu();
                }
            }
        }
        catch(IOException e) {
            System.out.println("\t\tЧто-то пошло не так при загрузке...:(\n" +
                               "\t1 - попробовать снова\n" +
                               "\tлюбая клавиша - вернуться в главное меню");
            selection = in.next();
            if (selection.equals("1")){
                download(k);
            }
            else{
                mainMenu();
            }
        }


    }

    /**
     *Метод для сохранения меню ресторона в выбранный файл
     *@throws IOException
     */
    private static void save(){
        System.out.println("\n\t\t---Сохранение меню ресторона---");
        System.out.print("\nПуть к файлу: ");
        String nameFile = in.next();
        File file0 = new File(nameFile);
        try {
            Serialize.serialize(Serialize.deserialize(file),file0);
            System.out.println("\t\tСохранение выполнено успешно!\n" +
                    "\tлюбая клавиша - вернуться в главное меню");
            selection = in.next();
            mainMenu();
        }
        catch(IOException e) {
            System.out.println("\t\tЧто-то пошло не так при сохранении...:(\n" +
                    "\t1 - попробовать снова\n" +
                    "\tлюбая клавиша - вернуться в главное меню");
            selection = in.next();
            if (selection.equals("1")){
                save();
            }
            else{
                mainMenu();
            }
        }
    }

    /**
     *Метод просмотра меню ресторана
     */
    private static void showMenu(){
        System.out.println("\n\t\tПросмотр данных:\n" +
                "\t1 - меню ресторана\n" +
                "\t2 - блюда по категории\n" +
                "\t3 - категории ресторана\n" +
                "\t другая последовательность - выход в меню ");
        System.out.print("\n---Ваш выбор: ");
        selection = in.next();
        switch (selection){
            case "1":
                System.out.println(Controller.print(file));
                break;
            case "2":
                System.out.println(Controller.print(inputName("Название категории: "),file));
                break;
            case "3":
                System.out.println(Controller.printCategory(fileCategory));
                break;
            default:
                mainMenu();
        }
        System.out.print("\n\tВведите любое значение, чтобы выйти в главное меню - ");
        in.next();
        mainMenu();
    }

    /**
     *Метод редактирования меню ресторана
     */
    private static void editMenu(){
        System.out.println(selectionRule);
        System.out.println("\n\t\t---Редактор блюд в ресторане:---");
        System.out.println("\t1 - Изменить блюдо по номеру\n" +
                "\t2 - Изменить блюдо по названию\n" +
                "\t3 - Изменить категорию блюда\n" +
                "\t4 - Изменить название блюда по названию\n" +
                "\t5 - Изменить цену блюда по названию\n" +
                "\t6 - добавить блюдо\n" +
                "\t7 - удалить блюдо\n " +
                "\t8 - добавить категорию\n" +
                "\t9 - вернуться в главное меню");
        System.out.print("\n---Ваш выбор: ");
        selection = in.next();
        switch (selection) {
            case "1": {
                int i = inputNumber("\nНомер блюда: ");
                String name = inputName("Название блюда: ");
                double price = inputPrice();
                Category category = new Category(inputName("Категория блюда: "));
                Dish dish = new Dish(name, category, price);
                Controller.setDataByNumber(i, dish, file);
                break;
            }
            case "2": {
                String name1 = inputName("Название блюда: ");
                String name = inputName("Название нового блюда: ");
                double price = inputPrice();
                Category category = new Category(inputName("Категория нового блюда: "));
                Dish dish = new Dish(name, category, price);
                Controller.setDataByName(name1, dish, file);
                break;
            }
            case "3": {
                String name = inputName("Название блюда: ");
                Category category = new Category(inputName("Новая категория блюда: "));
                Controller.setCategoryByName(name, category, file);
                break;
            }
            case "4": {
                String name = inputName("Название блюда: ");
                String name1 = inputName("Новое название блюда: ");
                Controller.setNameByName(name,name1,file);
            }
            case "5":{
                String name = inputName("Название блюда: ");
                double price = inputPrice();
                Controller.setPriceByName(name, price, file);
                break;
            }
            case "6": {
                String name = inputName("Название блюда: ");
                double price = inputPrice();
                Category category = new Category(inputName("Категория блюда: "));
                Dish dish5 = new Dish(name, category, price);
                Controller.addData(dish5, file, fileCategory);
                break;
            }
            case "7": {
                String name = inputName("Название блюда: ");
                Controller.deleteData(name, file);
                break;
            }
            case "8": {
                String name = inputName("Название категории: ");
                Controller.addCategory(new Category(name),fileCategory);
                break;
            }
            case "9": {
                mainMenu();
                return;
            }
            default: {
                System.out.println(wrongChoice);
                System.out.print("\n---Ваш выбор: ");
                selection = in.next();
                if (selection.equals("1")) {
                    editMenu();
                    return;
                }
            }
        }
        System.out.println("\n\t\tХотите продолжить редактирование?\n\t1 - да\n\tлюбая клавиша - вернуться в меню");
        if (in.next().equals("1")) editMenu();
        else   mainMenu();
    }

    /**
     *Метод ввода имени
     */
    private static String inputName(String s){
        System.out.println(s);
        String name = in.next();
        return name;
    }

    /**
     *Метод ввода цены блюда
     */
    private static double inputPrice(){
        System.out.println("Цена блюда: ");
        try {
            double price = in.nextDouble();
            if (price <= 0) throw new Exception("Отрицательное число!");
            return price;
        } catch (Exception ex){
            in.next();
            System.out.println("\t\tВы неправильно ввели цену!\n" +
                    "\t\tЦена - это вещественное положительное число!");
            System.out.println("\n\t1 - попробовать ввести заново цену\n" +
                    "\tлюбая клавиша - вернуться в меню");
            if (in.next().equals("1")) return inputPrice();
            mainMenu();
        }
        return -1;
    }

    /**
     *Метод ввода номера блюда
     */
    private static int inputNumber(String s){
        System.out.println(s);
        try{
            int i = in.nextInt();
            if (i < 0) throw new Exception("Отрицательное число!");
            if (i > Serialize.deserialize(file).size()) throw new Exception("Такого блюда нет!");
            return i;
        } catch (Exception e) {
            in.next();
            System.out.println("\t\tВы неправильно ввели номер!\n" +
                    "\t\tНомер - это целое положительное число! Номер не должен превышать количества блюд");
            System.out.println("\n\t1 - попробовать ввести заново номер\n" +
                    "\tлюбая клавиша - вернуться в меню");
            if (in.next().equals("1")) return inputNumber(s);
            else mainMenu();
        }
        return -1;
    }

}


