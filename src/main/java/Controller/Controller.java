package Controller;

import Model.Category;
import Model.Dish;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Класс - контроллер
 */
public class Controller {
    /**
     * Метод изменения блюда по его номеру
     * @param i - номер блюда по его положению в списке
     * @param dish - блюдо
     * @param file - файл
     * @throws IOException
     */
    public static void setDataByNumber(int i,Dish dish,File file){
        try {
            List<Dish> dishes=Serialize.deserialize(file);
            dishes.set(i,dish);
            Serialize.serialize(dishes,file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Метод изменения блюда по его названию
     * @param name - название блюда
     * @param dish - блюдо
     * @param file - файл
     * @throws IOException
     */
    public static void setDataByName(String name,Dish dish,File file){
        try {
            List<Dish> dishes=Serialize.deserialize(file);
            int i=0;
            while(i<dishes.size()){
                if(dishes.get(i).getName().equals(name)){
                    dishes.set(i,dish);
                    i= dishes.size();
                }
                i++;
            }
            Serialize.serialize(dishes,file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Метод изменения категории блюда по названию
     * @param name - название блюда
     * @param category - категория блюда
     * @param file - файл
     * @throws IOException
     */
    public static void setCategoryByName(String name,Category category,File file){
        try {
            List<Dish> dishes=Serialize.deserialize(file);
            Dish dish = new Dish(name,category,0);
            int i=0;
            while(i<dishes.size()){
                if(dishes.get(i).getName().equals(dish.getName())){
                    dishes.get(i).setCategory(dish.getCategory());
                    i= dishes.size();
                }
                i++;
            }
            Serialize.serialize(dishes,file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Метод изменения названия блюда по названию
     * @param name - название блюда
     * @param nname- категория блюда
     * @param file - файл
     * @throws IOException
     */
    public static void setNameByName(String name,String nname,File file){
        try {
            List<Dish> dishes=Serialize.deserialize(file);
            int i=0;
            while(i<dishes.size()){
                if(dishes.get(i).getName().equals(name)){
                    dishes.get(i).setName(nname);
                    i= dishes.size();
                }
                i++;
            }
            Serialize.serialize(dishes,file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Метод изменения цены блюда по названию
     * @param name - название блюда
     * @param price - цена блюда
     * @param file - файл
     * @throws IOException
     */
    public static void setPriceByName(String name,Double price,File file){
        try {
            List<Dish> dishes=Serialize.deserialize(file);
            int i=0;
            while(i<dishes.size()){
                if(dishes.get(i).getName().equals(name)){
                    dishes.get(i).setPrice(price);
                    i= dishes.size();
                }
                i++;
            }
            Serialize.serialize(dishes,file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Метод добавления блюда
     * @param dish - блюдо
     * @param file - файл
     * @param file1 - файл категорий
     * @return значение истина или ложь
     * @throws IOException
     */
    public static boolean addData(Dish dish, File file, File file1) {
        try {
            if (file.length()!=0) {
                List<Dish> dishes = Serialize.deserialize(file);
                if(compare1(dishes,dish)) {
                    dishes.add(dish);
                    for(int i = 0;i< dishes.size();i++) {
                        addCategory(dishes.get(i).getCategory(), file1);
                    }
                    Serialize.serialize(dishes, file);
                    return  true;
                }
                else{return false;}
            }else {
                List<Dish>dishes= List.of(dish);
                Serialize.serialize(dishes, file);
                addCategory(dish.getCategory(),file1);
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    /**
     * Метод добавления категорий из файла
     * @param file - файл
     * @param file1 - файл категорий
     * @return значение истина или ложь
     * @throws IOException
     */
    public static boolean addCategoryByDish(File file, File file1) {
        try {
            if (file.length()!=0) {
                List<Dish> dishes = Serialize.deserialize(file);
                for(int i = 0;i< dishes.size();i++) {
                    addCategory(dishes.get(i).getCategory(), file1);
                }
                return  true;
            }else {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    /**
     * Метод удаления блюда по его названию
     * @param name - название блюда
     * @param file - файл
     * @throws IOException
     */
    public static void deleteData(String name, File file) {
        try {
            List<Dish> dishes=Serialize.deserialize(file);
            int i=0;
            while(i<dishes.size()){
                if(dishes.get(i).getName().equals(name)){
                    dishes.remove(i);
                    i= dishes.size();
                }
                i++;
            }
            Serialize.serialize(dishes,file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Метод просмотра данных
     * @param file - файл
     * @return возвращает строку содержащую список блюд
     * @throws IOException
     */
    public static String print(File file){
        try {
            String s=Serialize.deserialize(file).toString();
            String s1="";
            char[] t=s.toCharArray();
            for(int i=1;i<s.length()-1;i++){
                if('D' == t[i]){
                    if(t[i+1] == 'i'){
                        if(t[i+2] == 's'){
                            if(t[i+3]=='h'){
                                s1+='\n';
                            }
                        }
                    }
                }
                s1+=t[i];
            }
            s=s1;
            return s;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * Метод проверки данных
     * @param o1 - блюдо одного из файлов
     * @param o2 - блюдо из другого файла
     * @return возвращает значение истина или ложь
     */
    private static boolean compare(Dish o1,Dish o2){
        if(o1.getName().equals(o2.getName())){
            return false;
        }else{
            return true;
        }
    }
    /**
     * Метод проверки данных из другого файла
     * @param o1 - меню одного из файлов
     * @param o2 - блюдо из другого файла
     * @return возвращает значение истина или ложь
     */
    private static boolean compare1(List<Dish> o1,Dish o2){
        int i=0;
        boolean bool=true;
        while(i<o1.size() && bool){
            bool=compare(o1.get(i),o2);
            i++;
        }
        return bool;
    }
    /**
     * Метод добавления данных из другого файла
     * @param file - файл для добавления данных
     * @param file1 - файл
     * @return возвращает значение истина или ложь
     * @throws IOException
     */
    public static boolean addFile(File file,File file1){
        try {
            if(file.length()!=0 && file1.length()!=0) {
                boolean bool = false;
                List<Dish> dishes = Serialize.deserialize(file);
                List<Dish> dishes1 = Serialize.deserialize(file1);
                int i = 0;
                while (i < dishes1.size()) {
                    if(dishes.add(dishes1.get(i))) {
                        bool = true;
                    }
                    i++;
                }
                Serialize.serialize(dishes, file);
                return bool;
            }else {
                if(file1.length()!=0){
                    List<Dish> dishes = Serialize.deserialize(file1);
                    Serialize.serialize(dishes, file);
                    return true;
                }
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    /**
     * Метод проверки данных
     * @param o1 - категория
     * @param o2 - категория
     * @return возвращает значение истина или ложь
     */
    private static boolean compare(Category o1,Category o2){
        if(o1.getNameCategory().equals(o2.getNameCategory())){
            return false;
        }else{
            return true;
        }
    }
    /**
     * Метод проверки данных из другого файла
     * @param o1 - лист категорий
     * @param o2 - категория
     * @return возвращает значение истина или ложь
     */
    private static boolean compare1(List<Category> o1,Category o2){
        int i=0;
        boolean bool=true;
        while(i<o1.size() && bool){
            bool=compare(o1.get(i),o2);
            i++;
        }
        return bool;
    }
    /**
     * Метод добавления категории
     * @param category - категория
     * @param file - файл
     * @return возвращает значение истина или ложь
     * @throws IOException
     */
    public static boolean addCategory(Category category, File file) {
        try {
            if (file.length()!=0) {
                List<Category> categories = Serialize.deserializeCategory(file);
                if(compare1(categories,category)) {
                    categories.add(category);
                    Serialize.serializeCategory(categories, file);
                    return  true;
                }
                else{ return false;}
            }else {
                List<Category> categories= List.of(category);
                Serialize.serializeCategory(categories, file);
                return  true;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    /**
     * Метод просмотра данных по категории
     * @param category - название категории
     * @param file -файл
     * @return возвращает строку содержащую список блюд
     * @throws IOException
     */
    public static String print(String category,File file){
        try {
            List<Dish> dishes=Serialize.deserialize(file);
            String s=category + '\n';
            for(int i=0;i<dishes.size();i++){
                if(dishes.get(i).getCategory().getNameCategory().equals(category)){
                    s+=dishes.get(i).toString();
                    s+='\n';
                }
            }
            return s;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * Метод просмотра категорий
     * @param file - файл категорий
     * @return возвращает строку содержащую список категорий
     * @throws IOException
     */
    public static String printCategory(File file){
        try {
            List<Category> categories=Serialize.deserializeCategory(file);
            String s="";
            for(int i=0;i<categories.size();i++){
                    s+=categories.get(i).toString();
                    s+='\n';
            }
            return s;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Метод поиска данных по названию
     * @param name - название блюда
     * @param file - файл
     * @return возвращает строку, содержащую все блюда, подходящие под шаблон названия
     * @throws IOException
     */
    public static String getDataByName(String name,File file){
        try {
            String s="";
            char[] t=name.toCharArray();
            List<Dish> dishes = Serialize.deserialize(file);
            for(int i=0;i<dishes.size();i++){
                int n=0,k=0; boolean bool=false;
                while(n<t.length) {
                    if (t[n] == '*' || t[n] == '?') {
                        n++;
                    }
                    else{
                        bool=false;
                        while (k<dishes.get(i).getName().length() && !bool){
                            char [] d =dishes.get(i).getName().toCharArray();
                            if(t[n] == d[k]){
                                bool=true;
                            }
                            k++;
                        }
                        n++;
                    }
                }
                if (bool){
                    s+=dishes.get(i).toString();
                    s+='\n';
                }
            }
            return s;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * Метод поиска данных по категории
     * @param category - название категории
     * @param file - файл
     * @return возвращает строку, содержащую все блюда, подходящие под шаблон категории
     * @throws IOException
     */
    public static String getDataByCategory(String category,File file){
        try {
            String s="";
            char[] t=category.toCharArray();
            List<Dish> dishes = Serialize.deserialize(file);
            for(int i=0;i<dishes.size();i++){
                int n=0,k=0; boolean bool=false;
                while(n<t.length) {
                    if (t[n] == '*' || t[n] == '?') {
                        n++;
                    }
                    else{
                        bool=false;
                        while (k<dishes.get(i).getCategory().getNameCategory().length() && !bool){
                            char [] d =dishes.get(i).getCategory().getNameCategory().toCharArray();
                            if(t[n] == d[k]){
                                bool=true;
                            }
                            k++;
                        }
                        n++;
                    }
                }
                if (bool){
                    s+=dishes.get(i).toString();
                    s+='\n';
                }
            }
            return s;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
