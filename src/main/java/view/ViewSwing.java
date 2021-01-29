package view;

import Client.Client;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class ViewSwing extends JFrame {

    /** Поле для окна */
    private static JFrame frame;
    /** Поле для файла блюд */
    private static File file;
    /** Поле для файла клиента*/
    private static Client client;

    /**
     * Конструктор - создает клиент и запускает программу
     */
    ViewSwing() throws IOException {
        client = new Client(8000);
        begin();
    }

    /**
     * Метод окна с кнопками открыть и загрузить
     */
    private void begin(){
        frame = new JFrame("Меню");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(500, 500));

        JPanel plain = new JPanel();
        plain.setLayout(new BoxLayout(plain, BoxLayout.Y_AXIS));

        JButton open = new JButton("Открыть");
        open.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

            }
        });
        plain.add(open);

        JButton download = new JButton("Загрузить");
        download.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    download(0);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        plain.add(download);

        frame.add(plain);
        frame.pack();
        frame.setVisible(true);
    }

    /**
     * Метод получения файла
     */
    private void download(int k) throws IOException {
        JFileChooser dialog = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "json file", "json");
        dialog.setFileFilter(filter);
        int result = dialog.showOpenDialog(this);
        file = dialog.getSelectedFile();
        if (result == JFileChooser.APPROVE_OPTION ){
            if (k == 0) {
                String s = client.file(file.getAbsolutePath());
            } else if (k == 1){
                client.save("saveNewFile",file.getAbsolutePath());
                JOptionPane.showMessageDialog(null, "Сохранение прошло успешно!");
            } else{
                String s  = client.addFile("addFile",file.getAbsolutePath());
                if (s.equals("No")){
                    JOptionPane.showMessageDialog(null, "Что-то пошло не так!");
                } else{
                    JOptionPane.showMessageDialog(null, "Добавление прошло успешно!");
                }
            }
            menu();
        }
    }


    /**
     * Метод с меню
     */
    private void menu(){
        JPanel plain = new JPanel();
        plain.setLayout(new BoxLayout(plain, BoxLayout.Y_AXIS));

        JButton view = new JButton("Посмотреть меню");
        view.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                view();
            }
        });
        plain.add(view);

        JButton add = new JButton("Добавить данные");
        add.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                add();
            }
        });
        plain.add(add);

        JButton edit = new JButton("Редактировать меню");
        edit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    edit();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

            }
        });
        plain.add(edit);

        JButton search = new JButton("Поиск по меню");
        search.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                search();
            }
        });
        plain.add(search);

        JButton save = new JButton("Сохранить");
        save.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                save();
            }
        });
        plain.add(save);

        frame.getContentPane().removeAll();
        frame.getContentPane().invalidate();
        frame.getContentPane().add(plain);
        frame.getContentPane().revalidate();
    }

    /**
     * Метод с меню для просмотра
     */
    private void view(){
        JPanel plain = new JPanel();
        plain.setLayout(new BoxLayout(plain, BoxLayout.Y_AXIS));

        JButton viewFull = new JButton("Все меню");
        viewFull.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    viewFullMenu();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        plain.add(viewFull);

        JButton viewMC = new JButton("Меню по категориям");
        viewMC.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    viewDishByCategory();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

            }
        });
        plain.add(viewMC);

        JButton viewCat = new JButton("Категории");
        viewCat.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    viewCategory();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

            }
        });
        plain.add(viewCat);

        JButton cancel = new JButton("Назад");
        cancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                menu();
            }
        });
        plain.add(cancel);

        frame.getContentPane().removeAll();
        frame.getContentPane().invalidate();
        frame.getContentPane().add(plain);
        frame.getContentPane().revalidate();
    }

    /**
     * Метод для просмотра всего меню
     */
    private void viewFullMenu() throws IOException {
        JPanel plain = new JPanel();
        plain.setLayout(new BoxLayout(plain, BoxLayout.Y_AXIS));

        Object[] columnsHeader = new String[] {"№","Блюдо", "Категория", "Цена"};
        String[] dish_arr = client.print("printDish").split("\\*");
        Object[][] array = new String[dish_arr.length/4][4];
        for (int i = 0; i < dish_arr.length/4;i++){
            for (int k = 0; k < 4; k ++){
                array[i][k] = dish_arr[i*4+k];
            }
        }

        // Таблица с настройками
        JTable table = new JTable(array,columnsHeader){
            @Override
            public boolean isCellEditable(int i, int i1) {
                return false;
            }
        };
        table.setFillsViewportHeight(true);
        JScrollPane table_scroll = new JScrollPane(table);
        table_scroll.setPreferredSize(new Dimension(500,300));

        JButton cancel = new JButton("Назад");
        cancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                view();
            }
        });
        plain.add(table_scroll);
        plain.add(cancel);

        frame.getContentPane().removeAll();
        frame.getContentPane().invalidate();
        frame.getContentPane().add(plain);
        frame.getContentPane().revalidate();
    }

    private void viewDishByCategory() throws IOException {
        JPanel plain = new JPanel();
        plain.setLayout(new BoxLayout(plain, BoxLayout.Y_AXIS));

        String[] category = client.print("printCategory").split("\\*");

        JComboBox comboBox = new JComboBox(category);
        comboBox.setEditable(false);
        plain.add(comboBox);


        Object[] columnsHeader = new String[] {"№","Блюдо", "Категория",
                "Цена"};
        Object[][] array = new String[0][0];
        DefaultTableModel model = new DefaultTableModel(array, columnsHeader);
        // Таблица с настройками
        JTable table = new JTable(model){
            @Override
            public boolean isCellEditable(int i, int i1) {
                return false;
            }
        };
        table.setFillsViewportHeight(true);
        JScrollPane table_scroll = new JScrollPane(table);
        table_scroll.setPreferredSize(new Dimension(500,300));
        plain.add(table_scroll);

        JButton ok = new JButton("Показать");
        ok.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    String[] dish_arr = client.print("printDishByCategory",comboBox.getSelectedItem().toString()).split("\\*");
                    Object[][] arrayNew = new String[dish_arr.length/4][4];
                    for (int i = 0; i < dish_arr.length/4;i++){
                        for (int k = 0; k < 4; k ++){
                            arrayNew[i][k] = dish_arr[i*4+k];
                        }
                    }
                    model.setRowCount(0);
                    DefaultTableModel modelNew = (DefaultTableModel)table.getModel();
                    for (Object[] row : arrayNew) {
                        modelNew.addRow(row);
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        plain.add(ok);

        JButton cancel = new JButton("Назад");
        cancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               view();
            }
        });
        plain.add(cancel);

        frame.getContentPane().removeAll();
        frame.getContentPane().invalidate();
        frame.getContentPane().add(plain);
        frame.getContentPane().revalidate();
    }

    private void viewCategory() throws IOException {
        JPanel plain = new JPanel();
        plain.setLayout(new BoxLayout(plain, BoxLayout.Y_AXIS));

        Object[] columnsHeader = new String[] {"№","Категории"};
        String[] category = client.print("printCategory").split("\\*");
        Object[][] array = new String[category.length][2];
        for (int i = 0; i < category.length; i++){
            array[i][0] = Integer.toString(i+1);
            array[i][1] = category[i];
        }
        // Таблица с настройками
        JTable table = new JTable(array,columnsHeader){
            @Override
            public boolean isCellEditable(int i, int i1) {
                return false;
            }
        };
        table.setFillsViewportHeight(true);
        JScrollPane table_scroll = new JScrollPane(table);
        table_scroll.setPreferredSize(new Dimension(500,300));

        JButton cancel = new JButton("Назад");
        cancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                view();
            }
        });
        plain.add(table_scroll);
        plain.add(cancel);

        frame.getContentPane().removeAll();
        frame.getContentPane().invalidate();
        frame.getContentPane().add(plain);
        frame.getContentPane().revalidate();
    }

    private void add(){
        JPanel plain = new JPanel();

        plain.setLayout(new BoxLayout(plain, BoxLayout.Y_AXIS));

        JButton addDish = new JButton("Добавить блюдо");
        addDish.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    addDish();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        plain.add(addDish);

        JButton addCategory = new JButton("Добавить категорию");
        addCategory.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addCategory();

            }
        });
        plain.add(addCategory);

        JButton addFile = new JButton("Добавить данные из файла");
        addFile.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Выберите файл с данными");
                try {
                    download(2);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

            }
        });
        plain.add(addFile);

        JButton cancel = new JButton("Назад");
        cancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                menu();
            }
        });

        plain.add(cancel);

        frame.getContentPane().removeAll();
        frame.getContentPane().invalidate();
        frame.getContentPane().add(plain);
        frame.getContentPane().revalidate();
    }

    private void addDish() throws IOException {
        JPanel plain = new JPanel();
        plain.setLayout(new BoxLayout(plain, BoxLayout.Y_AXIS));

        JTextField name = new JTextField(25);
        name.setFont(new Font("Dialog", Font.PLAIN, 14));
        name.setHorizontalAlignment(JTextField.LEFT);

        String[] category = client.print("printCategory").split("\\*");

        JComboBox comboBox = new JComboBox(category);
        comboBox.setEditable(true);
        plain.add(comboBox);

        NumberFormat price =  new DecimalFormat("##0.###");
        JFormattedTextField numberField = new JFormattedTextField(new NumberFormatter(price));
        numberField.setColumns(10);

        plain.add(new JLabel("Название категории :"));
        plain.add(name);
        plain.add(new JLabel("Категория :"));
        plain.add(comboBox);
        plain.add(new JLabel("Цена :"));
        plain.add(numberField);

        JButton add = new JButton("Добавить");
        add.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               Dish dish = new Dish(name.getText(),
                       new Category(comboBox.getSelectedItem().toString()),
                       new Double(numberField.getValue().toString()));
                try {
                    String res = client.addData("addData",dish);
                    if (res.equals("Yes")){
                        JOptionPane.showMessageDialog(null, "Добавление прошло успешно");
                    }
                    else{
                        JOptionPane.showMessageDialog(null, "Такое блюдо уже есть!");
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        plain.add(add);

        JButton cancel = new JButton("Назад");
        cancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                menu();
            }
        });

        plain.add(cancel);

        frame.getContentPane().removeAll();
        frame.getContentPane().invalidate();
        frame.getContentPane().add(plain);
        frame.getContentPane().revalidate();
    }

    private void addCategory(){
        JPanel plain = new JPanel();
        plain.setLayout(new BoxLayout(plain, BoxLayout.Y_AXIS));

        JTextField name = new JTextField(25);
        name.setFont(new Font("Dialog", Font.PLAIN, 14));
        name.setHorizontalAlignment(JTextField.LEFT);


        plain.add(new JLabel("Название категории :"));
        plain.add(name);

        JButton add = new JButton("Добавить");
        add.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    String res = client.addData("addCategory",name.getText());
                    if (res.equals("Yes")){
                        JOptionPane.showMessageDialog(null, "Добавление прошло успешно");
                    }
                    else{
                        JOptionPane.showMessageDialog(null, "Такая категория уже есть!");
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        plain.add(add);

        JButton cancel = new JButton("Назад");
        cancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                menu();
            }
        });

        plain.add(cancel);

        frame.getContentPane().removeAll();
        frame.getContentPane().invalidate();
        frame.getContentPane().add(plain);
        frame.getContentPane().revalidate();
    }

    private void search(){
        JPanel plain = new JPanel();

        plain.setLayout(new BoxLayout(plain, BoxLayout.Y_AXIS));
        plain.add(new Label("Введите запрос для поиска(Например, тор?ик*)"));
        JTextField search = new JTextField(25);
        search.setFont(new Font("Dialog", Font.PLAIN, 14));
        search.setHorizontalAlignment(JTextField.LEFT);
        plain.add(search);

        plain.add(new Label("Как искать: "));

        JComboBox comboBox = new JComboBox(new String[]{"по категории","по блюду"});
        comboBox.setEditable(false);
        plain.add(comboBox);


        Object[] columnsHeader = new String[] {"№","Блюдо", "Категория",
                "Цена"};
        Object[][] array = new String[0][0];
        DefaultTableModel model = new DefaultTableModel(array, columnsHeader);
        // Таблица с настройками
        JTable table = new JTable(model){
            @Override
            public boolean isCellEditable(int i, int i1) {
                return false;
            }
        };
        table.setFillsViewportHeight(true);
        JScrollPane table_scroll = new JScrollPane(table);
        table_scroll.setPreferredSize(new Dimension(500,300));
        plain.add(table_scroll);

        JButton searchB = new JButton("Найти");
        searchB.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    String method = "getDataByCategory";
                    if (comboBox.getSelectedIndex() == 1){
                        method = "getDataByName";
                    }
                    String[] dish_arr = client.print(method,search.getText()).split("\\*");
                    Object[][] arrayNew = new String[dish_arr.length/4][4];
                    for (int i = 0; i < dish_arr.length/4;i++){
                        for (int k = 0; k < 4; k ++){
                            arrayNew[i][k] = dish_arr[i*4+k];
                        }
                    }
                    model.setRowCount(0);
                    DefaultTableModel modelNew = (DefaultTableModel)table.getModel();
                    for (Object[] row : arrayNew) {
                        modelNew.addRow(row);
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        plain.add(searchB);

        JButton cancel = new JButton("Назад");
        cancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                menu();
            }
        });

        plain.add(cancel);

        frame.getContentPane().removeAll();
        frame.getContentPane().invalidate();
        frame.getContentPane().add(plain);
        frame.getContentPane().revalidate();
    }

    private void save(){
        JPanel plain = new JPanel();

        plain.setLayout(new BoxLayout(plain, BoxLayout.Y_AXIS));

        JButton save = new JButton("сохранить");
        save.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    client.save("saveFile");
                    JOptionPane.showMessageDialog(null, "Сохранение прошло успешно!");

                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        plain.add(save);

        JButton addCategory = new JButton("сохранить как копию");
        addCategory.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Выберите файл для сохранения");
                try {
                    download(1);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        plain.add(addCategory);

        JButton cancel = new JButton("Назад");
        cancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                menu();
            }
        });

        plain.add(cancel);

        frame.getContentPane().removeAll();
        frame.getContentPane().invalidate();
        frame.getContentPane().add(plain);
        frame.getContentPane().revalidate();
    }

    private void edit() throws IOException {
        JPanel plain = new JPanel();
        plain.setLayout(new BoxLayout(plain, BoxLayout.Y_AXIS));

        Object[] columnsHeader = new String[] {"№","Блюдо", "Категория", "Цена"};
        String ff =  client.print("printDish");
        String[] dish_arr =ff.split("\\*");
        Object[][] array = new String[dish_arr.length/4][4];
        Object[][] arrayCopy = new String[dish_arr.length/4][4];
        for (int i = 0; i < dish_arr.length/4;i++){
            for (int k = 0; k < 4; k ++){
                array[i][k] = dish_arr[i*4+k];
                arrayCopy[i][k] = dish_arr[i*4+k];
            }
        }
        DefaultTableModel model = new DefaultTableModel(array, columnsHeader);
        // Таблица с настройками
        JTable table = new JTable(model);
        table.setFillsViewportHeight(true);
        JScrollPane table_scroll = new JScrollPane(table);
        table_scroll.setPreferredSize(new Dimension(500,300));
        plain.add(table_scroll);

        JButton edit = new JButton("Сохранить изменения");
        edit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    for (int i = 0; i < dish_arr.length/4; i ++){
                        for (int k = 1; k < 4; k++){
                            if (!array[i][k].equals(arrayCopy[i][k])){
                                if (k == 1) {
                                    client.setData("setNameByName",arrayCopy[i][1].toString(), array[i][k].toString());
                                } else if (k == 2) {
                                    client.setData("setCategoryByName",arrayCopy[i][1].toString(), array[i][k].toString());
                                } else if (k == 3) {
                                    client.setData("setPriceByName",arrayCopy[i][1].toString(),Double.parseDouble(array[i][k].toString()));
                                }
                            }
                        }
                    }
                    JOptionPane.showMessageDialog(null, "Изменение прошло успешно!");
                    frame.getContentPane().removeAll();
                    frame.getContentPane().invalidate();
                    edit();
                }catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        plain.add(edit);

        JButton discard = new JButton("Сбросить");
        discard.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    edit();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        plain.add(discard);

        JButton delete = new JButton("Удалить выделенную строку");
        delete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    int idx = table.getSelectedRow();
                    model.removeRow(idx);
                    client.setData("deleteData",array[idx][1].toString());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        plain.add(delete);



        JButton cancel = new JButton("Назад");
        cancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                menu();
            }
        });
        plain.add(cancel);

        frame.getContentPane().removeAll();
        frame.getContentPane().invalidate();
        frame.getContentPane().add(plain);
        frame.getContentPane().revalidate();
    }

    public static void main (String [] args) throws IOException {

        ViewSwing windowApplication = new ViewSwing();
    }
}
