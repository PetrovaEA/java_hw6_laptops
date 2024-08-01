package test.GB_seminars.HW6;

import java.io.*;
import java.lang.reflect.Field;
import java.util.*;

public class Start {

    public static void main(String[] args) {
        try {
            Set<Laptop> base = ReadData();
            Map<String,String> search_fields = new HashMap<>();
           String[] field_for_search = GetFieldFromUser(search_fields);
            while (field_for_search.length > 0) {
                search_fields.put(field_for_search[0], field_for_search[1]);
                field_for_search = GetFieldFromUser(search_fields);
            }
            if (!search_fields.isEmpty()) {
                Set<Laptop> result = FilterData(base, search_fields);
                if (!result.isEmpty()) {
                    PrintData(FilterData(base, search_fields));
                }
                else {
                    System.out.println("Ноутбуков с такими параметрами не найдено");
                }
            }
            else {
                PrintData(base);
            }
            //
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    public static Set<Laptop> FilterData(Set<Laptop> base, Map<String,String> search_fields) {
        Set<Laptop> filter = new HashSet<>();
        Field[] fields = Laptop.class.getDeclaredFields();
        ArrayList<String> search = new ArrayList<>();
        for (Field field : fields) {
            if (field.getType().equals(Boolean.class)) {
                if (!search_fields.containsKey(field.getName())) {
                    search.add(null);
                } else if (search_fields.get(field.getName()).equals("да")) {
                    search.add("true");
                }
                else {
                    search.add("false");
                }
            }
            else if ((field.getType().equals(String.class))) {
                search.add(search_fields.getOrDefault(field.getName(), null));
            }
            else {
                search.add(search_fields.getOrDefault(field.getName(), "0"));
            }
        }
        Laptop temp = new Laptop(Integer.parseInt(search.removeFirst()), search);
        for (Laptop laptop : base) {
            if (laptop.equals(temp)) {
                filter.add(laptop);
            }
        }
        return filter;
    }
    public static Set<Laptop> ReadData() throws FileNotFoundException {
        Set<Laptop> laptops = new HashSet<>();
        String file_path = System.getProperty("user.dir") + "/Data.txt";
        File file = new File(file_path);
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine();
            while (line != null) {
                ArrayList<String> data = new ArrayList<>(Arrays.asList(line.split(",")));
                Laptop laptop = new Laptop(Integer.parseInt(data.removeFirst()), data);
                laptops.add(laptop);
                line = reader.readLine();
            }
            return laptops;
        }
        catch (IOException e) {
            System.out.println("Ошибка чтения файла");
        }

        return laptops;
    }
    public static void PrintData(Set<Laptop> laptops) {
        StringBuilder sb = new StringBuilder();
        Formatter formatter = new Formatter(sb);
        formatter.format("|%10s |" +
                "%10s |" +
                "%15s |" +
                "%27s |" +
                "%21s |" +
                "%27s |" +
                "%25s |" +
                "%6s |" +
                "%12s |" +
                "%10s |" +
                "%20s |",
                "ID", "Бренд", "Цвет", "Объем оперативной памяти", "Диагональ экрана", "Время автономной работы", "Операционная система", "wifi","bluetooth", "Вес", "Цена");
        String header_line="";
        for (int i = 0; i < sb.length(); i++) {
            header_line+="-";
        }
        System.out.println(header_line);
        System.out.println(sb);
        System.out.println(header_line);
        laptops.forEach(System.out::println);
        System.out.println(header_line);
    }
    public static String[] GetFieldFromUser(Map<String,String> search_fields) {
        Scanner scanner = new Scanner(System.in);
        Field[] fields = Laptop.class.getDeclaredFields();
        String[] names = new String[]{"ID", "Бренд", "Цвет", "Объем оперативной памяти","Диагональ экрана","Время автономной работы","Операционная система","Наличие wifi","Наличие bluetooth","Вес","Цена"};
        if (search_fields.size() == fields.length - 1) {
            return new String[0];
        }
        else {
            if (search_fields.isEmpty()) {
                System.out.println("Критерии поиска не заданы.");
                System.out.println();
            }
            else {
                System.out.println();
                System.out.println("Выбранные критерии для поиска: ");
                for (int i = 1; i < fields.length; i++) {
                    if (search_fields.containsKey(fields[i].getName())) {
                            String show = names[i];
                            if (fields[i].getName().equals("price") || fields[i].getName().equals("weight")) {
                                show += " ≤ ";
                            }
                            else if (fields[i].getType().equals(int.class) || fields[i].getType().equals(double.class)) {
                                show += " ≥ ";
                            }
                            else {
                                show += " = ";
                            }
                            show += "\"" + search_fields.get(fields[i].getName()) + "\"";
                            System.out.println(show);
                    }
                }
                System.out.println();
            }
                ArrayList<Integer> available_numbers = new ArrayList<>();
                System.out.println("Доступные критерии для поиска: ");
                for (int i = 1; i < fields.length; i++) {
                    if (!search_fields.containsKey(fields[i].getName())) {
                        System.out.println(i + ": " + names[i]);
                        available_numbers.add(i);
                    }
                }
                int number = GetNumberFromUser(available_numbers);
                if (number == 0) {
                    return new String[0];
                }
                else {
                    String help = GetTypeHelp(fields, number);
                    System.out.print("Введите значение для параметра \"" + names[number] + "\" " + help + ": ");
                    String value = scanner.nextLine();
                    while (!isValidForLaptop(fields, number, value)) {
                        System.out.println("Ошибка ввода. Значение не распознано.");
                        System.out.print("Введите значение для параметра \"" + names[number] + "\" " + help + ": ");
                        value = scanner.nextLine();
                    }
                    String[] answer = new String[]{fields[number].getName(), value};
                    return answer;
                }
            }
    }

    private static boolean isValidForLaptop(Field[] fields, int number, String value) {
        if (fields[number].getType().equals(Boolean.class) && (value.equals("да") || value.equals("нет"))) {
            return true;
        }
        else if (fields[number].getType().equals(int.class) && isInteger(value)) {
            return true;
        }
        else if (fields[number].getType().equals(double.class) && isDouble(value)) {
            return true;
        }
        else if (fields[number].getType().equals(String.class)) {
            return true;
        }
        return false;
    }
    public static boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
    public static boolean isDouble(String str) {
        try {
            Double.parseDouble(str);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static String GetTypeHelp(Field[] fields, int number) {
        String help = new String();
        if (fields[number].getType().equals(Boolean.class)) {
            help = "(да/нет)";
        }
        else if (fields[number].getType().equals(int.class)) {
            help = "(целое число)";
        }
        else if (fields[number].getType().equals(double.class)) {
            help = "(число)";
        }
        else {
            help = "";
        }
        return help;
    }
    public static int GetNumberFromUser(ArrayList<Integer> available_numbers) {
        int number;
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("Для выхода и отображения результата введите \"0\" или ");
            System.out.print("введите число, соответствующее необходимому критерию: ");
            try {
                number = Integer.parseInt(sc.next());
                if (available_numbers.contains(number) || number == 0) {
                    return number;
                }
                else {
                    System.out.println("Ошибка ввода.");
                }
            }
            catch (Exception e) {
                System.out.println("Ошибка ввода.");
            }
        }
    }


}
