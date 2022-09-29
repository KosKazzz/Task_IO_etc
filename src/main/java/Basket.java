import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.Arrays;


public class Basket {

    private int[] prices;
    private String[] products;
    private int[] countOfProducts;
    private ClientLog clientLog;

    public Basket(int[] prices, String[] products) {
        this.prices = prices;
        this.products = products;
        this.countOfProducts = new int[products.length];
        this.clientLog = new ClientLog();
    }

    public int[] getCountOfProducts() {
        return countOfProducts;
    }

    public void setCountOfProducts(int[] countOfProducts) {
        this.countOfProducts = countOfProducts;
    }

    public void addToCart(int productNum, int amount) {
        this.countOfProducts[productNum - 1] += amount;
        this.logging(productNum, amount);
    }

    void saveLog(File logFile) {
        this.clientLog.exportAsCSV(logFile);
    }

    public void logging(int productNum, int amount) {
        this.clientLog.log(productNum, amount);
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

    public void saveTxt(File fileName) {
        try (FileWriter writer = new FileWriter(fileName, false)) {
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

    public static Basket loadFromTxtFile(File fileName) {
        StringBuilder stringFromTxtBasket = new StringBuilder();
        try (FileReader reader = new FileReader(fileName)) {
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
        return basket;
    }

    void saveJson(File fileName) {
        JSONObject jsnObj = new JSONObject();
        JSONArray jsnPrices = new JSONArray();
        JSONArray jsnProducts = new JSONArray();
        JSONArray jsnCountOfProducts = new JSONArray();
        for (int pr : prices) {
            jsnPrices.add(pr);
        }
        jsnProducts.addAll(Arrays.asList(products));
        for (int cnt : countOfProducts) {
            jsnCountOfProducts.add(cnt);
        }
        jsnObj.put("prices", jsnPrices);
        jsnObj.put("products", jsnProducts);
        jsnObj.put("countOfProducts", jsnCountOfProducts);
        try (FileWriter fileWriter = new FileWriter(fileName)) {
            fileWriter.write(jsnObj.toJSONString());
            fileWriter.flush();
        } catch (IOException ex) {
            System.err.println("Json файл не записывается...");
            ex.printStackTrace();
        }
    }

    public static Basket loadFromJson(File fileName) {
        JSONParser parser = new JSONParser();
        Basket basket = null;
        try (FileReader reader = new FileReader(fileName)) {
            Object obj = parser.parse(reader);
            JSONObject jsnObj = (JSONObject) obj;
            JSONArray pricesJson = (JSONArray) jsnObj.get("prices");
            JSONArray productsJson = (JSONArray) jsnObj.get("products");
            JSONArray countOfProductsJson = (JSONArray) jsnObj.get("countOfProducts");
            long[] pricesLongFromJson = new long[pricesJson.size()];
            String[] productsStringFromJson = new String[pricesJson.size()];
            long[] countOfProductsLongFromJson = new long[pricesJson.size()];
            for (int i = 0; i < productsJson.size(); i++) {
                pricesLongFromJson[i] = (Long) pricesJson.get(i);
                productsStringFromJson[i] = (String) productsJson.get(i);
                countOfProductsLongFromJson[i] = (Long) countOfProductsJson.get(i);
            }
            int[] pricesIntFromJson = new int[pricesJson.size()];
            int[] countOfProductsFromJson = new int[pricesJson.size()];
            for (int i = 0; i < productsJson.size(); i++) {
                pricesIntFromJson[i] = (int) pricesLongFromJson[i];
                countOfProductsFromJson[i] = (int) countOfProductsLongFromJson[i];
            }
            basket = new Basket(pricesIntFromJson, productsStringFromJson);
            basket.setCountOfProducts(countOfProductsFromJson);
        } catch (IOException | ParseException ex) {
            System.err.println("Can't read json file!!!");
            ex.printStackTrace();
        }
        return basket;
    }

    public static Basket BasketIsEmpty(int[] pri, String[] prod) {
        System.out.println("Ваша корзина пуста!");
        return new Basket(pri, prod);
    }

    public void saveBasket(boolean enabled, File fileName, String format) {
        if (enabled) {
            if (format.equals("json")) {
                saveJson(fileName);
            } else if (format.equals("txt")) {
                saveTxt(fileName);
            } else {
                System.out.println("Не корректный формат файла для записи!");
            }
        } else {
            System.out.println("Ваша корзина не сохранена!");
        }
    }

    public void logBasket(boolean enabled, File fileName) {
        if (enabled) {
            this.saveLog(fileName);
        }

    }
}

