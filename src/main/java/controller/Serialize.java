package controller;

import model.Category;
import model.Dish;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.util.List;

/** Класс предназначенный для сериализации и десериализации
 */
public class Serialize {
    /**
     * Метод серилизации в JSON
     * @param dishes - список блюд
     * @param file - файл
     * @throws IOException
     */
    public static void serialize(List<Dish> dishes,File file) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, dishes);
    }
    /**
     * Метод серилизации в JSON категорий
     * @param categories - список категорий
     * @param file - файл
     * @throws IOException
     */
    public static void serializeCategory(List<Category> categories, File file) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, categories);
    }
    /**
     * Метод десерилизации из JSON
     * @param file - файл
     * @return возвращает список блюд
     * @throws IOException
     */
    public static List<Dish> deserialize(File file) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        List<Dish> dishes =mapper.readValue(file, mapper.getTypeFactory().constructCollectionType(List.class, Dish.class));
        return dishes;
    }
    /**
     * Метод десерилизации из JSON категорий
     * @param file - файл
     * @return возвращает список категорий
     * @throws IOException
     */
    public static List<Category> deserializeCategory(File file) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        List<Category> categories =mapper.readValue(file, mapper.getTypeFactory().constructCollectionType(List.class, Category.class));
        return categories;
    }
    /**
     * Метод сериализации блюда в байтовый поток
     * @param dish - блюдо
     * @param out - поток
     */
    public static void serializeDish (Dish dish, OutputStream out){
        try {
            ObjectOutputStream stream = new ObjectOutputStream(out);
            stream.writeObject(dish);
        }
        catch (IOException e){
            System.out.println("Some error occurred!");
        }
    }

    /**
     * Метод десериализации блюда из байтового потока
     * @param in - поток
     * @return
     */
    public static Dish deserializeDish (InputStream in){
        try {
            ObjectInputStream stream = new ObjectInputStream(in);
            Dish dish = (Dish) stream.readObject();
            return dish;
        }
        catch (IOException e){
            System.out.println("Some error occurred!");
            return null;
        }
        catch(ClassNotFoundException e) {
            System.out.println("Wrong object type");
            return null;
        }
    }
    /**
     * Метод сжатия файла с помощью GZIP
     * @param nameFile - имя файла
     * @param nameGZIPFile - имя файла GZIP
     */
    public static void compressionFile (String nameFile,String nameGZIPFile){
        try {
            FileInputStream fileInputStream = new FileInputStream(nameFile);
            FileOutputStream fileOutputStream = new FileOutputStream(nameGZIPFile);
            GZIPOutputStream gzipOutputStream = new GZIPOutputStream(fileOutputStream);
            byte[] buffer = new byte[8*1024];
            int len;
            while((len=fileInputStream.read(buffer)) != -1){
                gzipOutputStream.write(buffer, 0, len);
            }
            gzipOutputStream.close();
            fileOutputStream.close();
            fileInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Метод распаковки файла с помощью GZIP
     * @param nameGZIPFile - имя файла GZIP
     * @param nameFile - имя файла
     */
    public static void decompressionFile(String nameGZIPFile, String nameFile) {
        try {
            FileInputStream fileInputStream = new FileInputStream(nameGZIPFile);
            GZIPInputStream gzipInputStream = new GZIPInputStream(fileInputStream);
            FileOutputStream fileOutputStream = new FileOutputStream(nameFile);
            byte[] buffer = new byte[8*1024];
            int len;
            while((len = gzipInputStream.read(buffer)) != -1){
                fileOutputStream.write(buffer, 0, len);
            }
            fileOutputStream.close();
            gzipInputStream.close();
            fileInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Метод сериализации GZIP в байтовый поток
     * @param nameGZIPFile - имя файла GZIP
     * @param out - поток
     */
    public static void serializeDZIP(String nameGZIPFile,OutputStream out){
        try {
            FileInputStream fileInputStream = new FileInputStream(nameGZIPFile);
            GZIPOutputStream gzipOutputStream = new GZIPOutputStream(out);
            byte[] buffer = new byte[8*1024];
            int len;
            while((len=fileInputStream.read(buffer)) != -1){
                gzipOutputStream.write(buffer, 0, len);
            }
            gzipOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Метод десериализации GZIP в байтовый поток
     * @param nameGZIPFile - имя файла GZIP
     * @param in - поток
     */
    public  static void deserializeDZIP(String nameGZIPFile,InputStream in){
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(nameGZIPFile);
            GZIPInputStream gzipInputStream = new GZIPInputStream(in);
            byte[] buffer = new byte[8*1024];
            int len;
            while((len = gzipInputStream.read(buffer)) != -1){
                fileOutputStream.write(buffer, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
