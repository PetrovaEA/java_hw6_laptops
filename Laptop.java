package test.GB_seminars.HW6;

import java.util.ArrayList;
import java.util.Formatter;
import java.util.Map;
import java.util.Objects;

public class Laptop {
    int ID;
    String brand;
    String color;
    int ram;
    double display_size;
    int battery_life;
    String os;
    Boolean wifi;
    Boolean bluetooth;
    double weight;
    int price;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Laptop laptop = (Laptop) o;
        return ram >= laptop.ram &&
                Double.compare(display_size, laptop.display_size) == 1 &&
                battery_life >= laptop.battery_life &&
                (wifi == laptop.wifi || laptop.wifi == null) &&
                (bluetooth == laptop.bluetooth | laptop.bluetooth == null) &&
                (Double.compare(weight, laptop.weight) == -1 || laptop.weight == 0) &&
                (price <= laptop.price | laptop.price == 0) &&
                (laptop.brand == null || Objects.equals(brand.toLowerCase(), laptop.brand.toLowerCase())) &&
                (laptop.color == null || Objects.equals(color.toLowerCase(), laptop.color.toLowerCase())) &&
                (laptop.os == null || Objects.equals(os.toLowerCase(), laptop.os.toLowerCase()));
    }


    @Override
    public int hashCode() {
        return Objects.hash(brand, color, ram, display_size, battery_life, os, wifi, bluetooth, weight, price);
    }

    @Override
    public String toString() {
        StringBuilder sb = new  StringBuilder();
        Formatter formatter = new Formatter(sb);
        formatter.format("|%10d |" +
                "%10s |" +
                "%15s |" +
                "%24s ГБ |" +
                "%20s\" |" +
                "%24s ч. |" +
                "%25s |" +
                "%6s |" +
                "%12s |" +
                "%7s кг |" +
                "%15s руб. |", ID, brand, color, ram, display_size, battery_life, os, getTextForBool(wifi), getTextForBool(bluetooth), weight, price);
        return sb.toString();
    }

    public String getTextForBool(Boolean bool) {
        String str = "";
        if (bool==null) {
            str = "не задано";
        }
        else if (bool) {
            str = "да";
        }
        else {
            str = "нет";
        }
        return str;
    }

    public Laptop(int ID, ArrayList<String> values) {
        this.ID = ID;
        brand = values.get(0);
        color = values.get(1);
        ram = Integer.parseInt(values.get(2));
        display_size = Double.parseDouble(values.get(3));
        battery_life = Integer.parseInt(values.get(4));
        os = values.get(5);
        if (values.get(6)!=null) {
            wifi = Boolean.parseBoolean(values.get(6));
        }
        else {
            wifi = null;
        }
        if (values.get(7)!=null) {
            bluetooth = Boolean.parseBoolean(values.get(7));
        }
        else {
            bluetooth = null;
        }
        weight = Double.parseDouble(values.get(8));
        price = Integer.parseInt(values.get(9));
    }

}
