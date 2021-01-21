package view;

import Client.Client;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

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
        frame.setPreferredSize(new Dimension(150, 150));

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
                frame.setVisible(false);
                try {
                    download();
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
    private void download() throws IOException {
        JFileChooser dialog = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "json file", "json");
        dialog.setFileFilter(filter);
        int result = dialog.showOpenDialog(this);
        file = dialog.getSelectedFile();
        if (result == JFileChooser.APPROVE_OPTION ){
            String s = client.file(file.getAbsolutePath());
            menu();

        }
    }

    /**
     * Метод с меню
     */
    private void menu(){
        frame = new JFrame("Меню");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(300, 300));

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

            }
        });
        plain.add(add);

        JButton edit = new JButton("Редактировать меню");
        edit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

            }
        });
        plain.add(edit);

        JButton search = new JButton("Поиск по меню");
        search.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

            }
        });
        plain.add(search);

        JButton save = new JButton("Поиск по меню");
        save.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

            }
        });
        plain.add(save);

        frame.add(plain);
        frame.pack();

        frame.setVisible(true);
    }

    /**
     * Метод с меню для просмотра
     */
    private void view(){
        frame.setVisible(false);
        frame = new JFrame("Меню");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(300, 300));

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

            }
        });
        plain.add(viewMC);

        JButton viewCat = new JButton("Категории");
        viewCat.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

            }
        });
        plain.add(viewCat);

        frame.add(plain);
        frame.pack();

        frame.setVisible(true);
    }

    /**
     * Метод для просмотра всего меню
     */
    private void viewFullMenu() throws IOException {
        frame.setVisible(false);
        frame = new JFrame("Меню");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //frame.setPreferredSize(new Dimension(500, 500));

       /* Object[] columnsHeader = new String[] {"Блюдо", "Категория",
                "Цена"};
        Object[][] array = new String[][] {{ "Сахар" , "кг", "1.5" },
                { "Мука"  , "кг", "4.0" },
                { "Молоко", "л" , "2.2" }};

        // Таблица с настройками
        JTable table = new JTable(array,columnsHeader);
        table.setFillsViewportHeight(true);
        JScrollPane table_scroll = new JScrollPane(table);
        table_scroll.setPreferredSize(new Dimension(500,300));

        frame.add(table_scroll);*/


        frame.pack();
        frame.setVisible(true);
    }


    public static void main (String [] args) throws IOException {

        ViewSwing windowApplication = new ViewSwing();
    }
}
