import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Basket implements Serializable {

    private int[] prices;
    private String[] products;
    private int[] countOfProducts;

    public Basket(int[] prices, String[] products) {
        this.prices = prices;
        this.products = products;
        this.countOfProducts = new int[products.length];
    }

    public int[] getCountOfProducts() {
        return countOfProducts;
    }

    public void setCountOfProducts(int[] countOfProducts) {
        this.countOfProducts = countOfProducts;
    }

    public void addToCart(int productNum, int amount) {
        this.countOfProducts[productNum - 1] += amount;
        //this.saveTxt("basket.txt");
        this.saveBin(".\\basket.bin");
    }

    public void printCart() {
        int count = 1;
        int sum = 0;
        for (int i = 0; i < products.length; i++) {
            if (countOfProducts[i] != 0) {
                System.out.println(count + ". " + products[i] + "-"
                        + countOfProducts[i] + "шт. "
                        + prices[i] + " руб/шт., "
                        + (countOfProducts[i] * prices[i]) + " руб. в сумме;");
                count++;
                sum += countOfProducts[i] * prices[i];
            }
        }
        System.out.println("  Итого: " + sum + " руб.");
    }

    void saveTxt(String filePathAndName) {
        filePathAndName = ".\\" + filePathAndName;
        File fileBasket = new File(filePathAndName);
        try (FileWriter writer = new FileWriter(fileBasket, false)) {
            for (int pr : prices) {
                writer.write(Integer.toString(pr));
                writer.append(' ');
            }
            writer.append('\n');
            for (String prod : products) {
                writer.write(prod);
                writer.append(' ');
            }
            writer.append('\n');
            for (int cnt : countOfProducts) {
                writer.write(Integer.toString(cnt));
                writer.append(' ');
            }
            writer.append('\n');
        } catch (IOException ex) {
            System.err.println("Something went wrong");
        }
    }

    public static Basket loadFromTxtFile(String fileName) {
        //fileName = ".\\" + fileName + ".txt";
        File fileBasket = new File(fileName);
        StringBuilder stringFromTxtBasket = new StringBuilder();
        try (FileReader reader = new FileReader(fileBasket)) {
            int c;
            while ((c = reader.read()) != -1) {
                stringFromTxtBasket.append((char) c);
            }
        } catch (IOException ex) {
            System.err.println("Не получается прочитать файл!");
        }
        String[] parts = stringFromTxtBasket.toString().split("\\n");
        int[] pricesFromFile = new int[parts[0].split(" ").length];
        String[] productsFromFile = new String[parts[1].split(" ").length];
        int[] countOfProductsFromFile = new int[parts[2].split(" ").length];
        for (int i = 0; i < pricesFromFile.length; i++) {
            try {
                pricesFromFile[i] = Integer.parseInt(parts[0].split(" ")[i]);
                productsFromFile[i] = parts[1].split(" ")[i];
                countOfProductsFromFile[i] = Integer.parseInt(parts[2].split(" ")[i]);
            } catch (NumberFormatException ex) {
                System.err.println("Не получается преобразовать инфорамцию из файла.");
            }
        }
        Basket basket = new Basket(pricesFromFile, productsFromFile);
        basket.setCountOfProducts(countOfProductsFromFile);
        //test
        /*System.out.println(" Тут  prices - " + Arrays.toString(pricesFromFile));
        System.out.println(" Тут  products - " + Arrays.toString(productsFromFile));
        System.out.println(" Тут  countOfProducts - " + Arrays.toString(countOfProductsFromFile));
        basket.printCart();*/
        return basket;
    }

    public void saveBin(String filePathAndName) {
        try (FileOutputStream fos = new FileOutputStream(filePathAndName);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(this);
        } catch (IOException ex) {
            ex.getStackTrace();
        }
    }

    public static Basket loadFromBinFile(String filePathAndName) {
        Basket bs = null;
        try (FileInputStream fis = new FileInputStream(filePathAndName);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            bs = (Basket) ois.readObject();
        } catch (IOException ex) {
            System.err.println("File not found!");
        } catch (ClassNotFoundException ex) {
            System.err.println("Class not found!!!");
        }
        return bs;
    }
}

