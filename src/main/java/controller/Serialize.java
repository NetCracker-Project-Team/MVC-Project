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

}
