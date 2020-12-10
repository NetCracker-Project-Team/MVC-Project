package View;

import Controller.Controller;
import Model.Category;
import Model.Dish;
import Controller.Serialize;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import javax.imageio.IIOException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
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
    private static String wrongChoice = "\n\t!!Вы неправильно выбрали пункт меню!!";
    /** Поле для пустого меню */
    private static String emptyMenu = "\n\tВы не можете выполнить операцию. Так как меню пустое!\n" +
                                      "\tПопробуйте снова.";
    /** Поле для файла блюд */
    private static File file;
    /** Поле для файла категорий*/
    private static File fileCategory = new File("fileC.json");


    /**
    *Метод главного меню
     */
    public static void mainMenu(){
        System.out.println(selectionRule);
        System.out.println("\n\t\t---Главное меню:---");
        System.out.println("\t1 - Загрузить меню ресторана\n" +
                           "\t2 - Сохранить меню ресторана\n" +
                           "\t3 - Посмотреть данные\n" +
                           "\t4 - Добавление нового блюда\n" +
                           "\t5 - Редактировать меню ресторана\n " +
                           "\t6 - Поиск данных в ресторане\n" +
                           "\t7 - Выйти из программы" );
        System.out.print("\n---Ваш выбор: ");
        selection = in.next();
        switch(selection){
            case "1":
                download(0);
                break;
            case "2":
                if (file == null || file.length() == 0){
                    System.out.println(emptyMenu);
                    mainMenu();
                }
                else{
                    save();
                }
                break;
            case "3":
                if (file == null || file.length() == 0){
                    System.out.println(emptyMenu);
                    mainMenu();
                }
                else{
                    showMenu();
                }
                break;
            case "4":
                if (file == null){
                    System.out.println("\n\tНет файла!");
                    mainMenu();
                }
                else{
                    newDish();
                }
                break;
            case "5":
                if (file == null || file.length() == 0){
                    System.out.println(emptyMenu);
                    mainMenu();
                }
                else{
                    editMenu();
                }
                break;
            case "6":
                if (file == null || file.length() == 0){
                    System.out.println(emptyMenu);
                    mainMenu();
                }
                else{
                    search();
                }
                break;
            case "7":
                return;
            default:
                System.out.println(wrongChoice);
                mainMenu();
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
            Controller.addCategoryByDish(file,fileCategory);
            System.out.println("\t\tЗагрузка выполнена успешно!\n" );
        }
        catch (Exception e){
            System.out.println("\t\t"+e.toString()+"\n");
        }
        if (k == 0) {
            System.out.println("\t1 - попробовать снова\n" +
                    "\t2 - вернуться в главное меню");
            selection = in.next();
            if (selection.equals("1")) {
                download(k);
            } else {
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
                    "\tлюбая другая последовательность - вернуться в главное меню");
            System.out.print("\n---Ваш выбор: ");
            selection = in.next();
            mainMenu();
        }
        catch(IOException e) {
            System.out.println("\t\tЧто-то пошло не так при сохранении...:(\n" +
                    "\t1 - попробовать снова\n" +
                    "\tлюбая другая последовательность - вернуться в главное меню");
            System.out.print("\n---Ваш выбор: ");
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
                System.out.println(Controller.print(inputName("Название категории: ",1),file));
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
     *Метод добавления блюда
     */
    private static void newDish(){

        System.out.println("\n\t\tДобавление блюда");
        int k = 2;
        if (file.length() == 0) k = 5;
        String name = inputName("Название блюда: ", k);
        double price = inputPrice();
        Category category = new Category(inputName("Категория блюда: ", 1));
        Dish dish5 = new Dish(name, category, price);
        Controller.addData(dish5, file, fileCategory);
        System.out.println("\n\t\tОперация успешно выполнена!");
        System.out.println("\n\t\tХотите повторить?\n\t1 - да\n\tлюбая другая последовательность - вернуться в меню");
        System.out.print("\n---Ваш выбор: ");
        if (in.next().equals("1")) editMenu();
        else  mainMenu();
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
                "\t6 - удалить блюдо\n " +
                "\t7 - добавить категорию\n" +
                "\t8 - добавить данные из файла\n" +
                "\t9 - вернуться в главное меню");
        System.out.print("\n---Ваш выбор: ");
        selection = in.next();
        switch (selection) {
            case "1": {
                int i = inputNumber("\nНомер блюда: ");
                String name = inputName("Название блюда: ", 0);
                double price = inputPrice();
                Category category = new Category(inputName("Категория блюда: ", 1));
                Dish dish = new Dish(name, category, price);
                Controller.setDataByNumber(i, dish, file);
                break;
            }
            case "2": {
                String name1 = inputName("Название блюда: ", 0);
                String name = inputName("Название нового блюда: ", 2);
                double price = inputPrice();
                Category category = new Category(inputName("Категория нового блюда: ", 1));
                Dish dish = new Dish(name, category, price);
                Controller.setDataByName(name1, dish, file);
                break;
            }
            case "3": {
                String name = inputName("Название блюда: ", 0);
                Category category = new Category(inputName("Новая категория блюда: ", 3));
                Controller.setCategoryByName(name, category, file);
                break;
            }
            case "4": {
                String name = inputName("Название блюда: ", 0);
                String name1 = inputName("Новое название блюда: ", 2);
                Controller.setNameByName(name, name1, file);
            }
            case "5": {
                String name = inputName("Название блюда: ", 0);
                double price = inputPrice();
                Controller.setPriceByName(name, price, file);
                break;
            }
            case "6": {
                String name = inputName("Название блюда: ", 0);
                Controller.deleteData(name, file);
                break;
            }
            case "7": {
                String name = inputName("Название категории: ", 1);
                Controller.addCategory(new Category(name), fileCategory);
                break;
            }
            case "8": {
                System.out.print("Имя файла: ");
                File file1 = new File(in.next());
                if (!Controller.addFile(file, file1)){
                    System.out.println("\n\tЧто-то пошло не так!");
                    editMenu();
                    return;
                }
                break;
            }
            case "9": {
                mainMenu();
                return;
            }
            default: {
                System.out.println(wrongChoice);
                editMenu();
            }
        }
        System.out.println("\n\t\tОперация успешно выполнена!");
        System.out.println("\n\t\tХотите продолжить редактирование?\n\t1 - да\n\tлюбая клавиша - вернуться в меню");
        System.out.print("\n---Ваш выбор: ");
        if (in.next().equals("1")) editMenu();
        else   mainMenu();
    }

    /**
     *Метод ввода имени
     * @param s - строка для ввода
     * @param k - показывает, что вводится( 0 - название блюда, 1 - названик категории, 2 - новое название блюда, 3 - новое название категории)
     */
    private static String inputName(String s, int k){
        System.out.print(s);
        String name = in.next();
        String t = name + "*";
        if (k == 0 && Controller.getDataByName(t,file)==""){
            System.out.println("\n\tТакого блюда нет!");
        }
        else if (k == 2 && Controller.getDataByName(t,file)!=""){
            System.out.println("\n\tТакое блюдо уже есть!");
        }
        else return name;
        System.out.println("\n\t1 - попробовать ввести заново имя\n" +
                "\tлюбая клавиша - вернуться в меню");
        if (in.next().equals("1")) return inputName(s,k);
        else mainMenu();
        return null;
    }

    /**
     *Метод ввода цены блюда
     * @throws Exception
     */
    private static double inputPrice(){
        System.out.print("Цена блюда: ");
        try {
            double price = in.nextDouble();
            if (price <= 0) throw new Exception("Отрицательное число!");
            return price;
        } catch (Exception e){
            if (!e.toString().equals("Отрицательное число!")) in.next();
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
     * @throws Exception
     */
    private static int inputNumber(String s){
        System.out.print(s);
        try{
            int i = in.nextInt();
            if (i < 0) throw new Exception("Отрицательное число!");
            if (i > Serialize.deserialize(file).size()) throw new Exception("Такого блюда нет!");
            return i;
        } catch (Exception e) {
            if (!e.toString().equals("Отрицательное число!") && !e.toString().equals("Такого блюда нет!")) in.next();
            System.out.println("\t\tВы неправильно ввели номер!\n" +
                    "\t\tНомер - это целое положительное число! Номер не должен превышать количества блюд");
            System.out.println("\n\t1 - попробовать ввести заново номер\n" +
                    "\tлюбая клавиша - вернуться в меню");
            if (in.next().equals("1")) return inputNumber(s);
            else mainMenu();
        }
        return -1;
    }

    /**
     *Метод поиска данных
     */
    private static void search(){
        System.out.println("\n\t\tПoиск данных:\n" +
                "\t1 - поиск блюда\n" +
                "\t2 - поиск по категории\n" +
                "\t другая последовательность - выход в меню ");
        System.out.print("\n---Ваш выбор: ");
        selection = in.next();
        System.out.print("\n\tПример шаблона: name, nameCategory, *\n" +
                "Шаблон:");
        String template = in.next();
        String result;
        switch (selection){
            case "1":
                result = Controller.getDataByName(template,file);
                break;
            case "2":
                result = Controller.getDataByCategory(template,file);
                break;
            default:
                mainMenu();
                return;
        }
        if (result.equals("")){
            System.out.println("\n\tДанных нет:(");
        }
        else {
            System.out.println("\n\tНайденные данные:");
            System.out.println(result);
        }
        System.out.println("\n\t\tХотите продолжить редактирование?\n\t1 - да\n\tлюбая другая последовательность - вернуться в меню");
        System.out.print("\n---Ваш выбор: ");
        if (in.next().equals("1")) search();
        else   mainMenu();
    }

}
