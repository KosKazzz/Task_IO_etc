import java.io.File;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String[] products = {"Хлеб", "Молоко", "Мясо", "Картофель", "Конфета"};
        int[] prices = {56, 79, 550, 65, 200};
        Basket bs;
        File basketIsHere = new File(".\\src\\main\\resources\\basket.txt");
        if (basketIsHere.canRead()) {
            //bs = Basket.loadFromTxtFile(".\\src\\main\\resources\\basket.txt");
            bs = Basket.loadFromJson();
            System.out.println("Ваша корзина : ");
            bs.printCart();
            System.out.println();
        } else {
            System.out.println("Ваша корзина пуста!");
            bs = new Basket(prices, products);
            System.out.println();
        }
        for (int i = 0; i < products.length; i++) {
            System.out.println((i + 1) + ". " + products[i] + " - " + prices[i] + " руб/шт.");
        }
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Выберите номер товара и количество или для выхода наберите \"end\" .");
            String input = scanner.nextLine();
            if (input.equals("end")) {
                break;
            }
            if (input.isEmpty()) {
                System.out.println("Надо что-нибудь ввести =)");
            }
            if (input.length() < 3) {
                System.out.println("Надо вводить правильно(номер продукта пробел количество!)");
                continue;
            }
            int nomOfProd;
            int countOfProd;
            if (input.contains(" ")) {
                if (input.startsWith(" ") || input.endsWith(" ")) {
                    System.out.println("Надо вводить правильно(номер продукта пробел количество!)");
                } else {
                    int countOfRegex = 0;
                    for (int i = 1; i < input.length() - 1; i++) {
                        if (input.charAt(i) == ' ') {
                            countOfRegex++;
                        }
                    }
                    if (countOfRegex >= 2) {
                        System.out.println("Ввели лишнее.Надо вводить правильно(номер продукта пробел количество!)");
                        continue;
                    }
                    String[] parts = input.split(" ");
                    try {
                        nomOfProd = Integer.parseInt(parts[0]); // productNum
                        countOfProd = Integer.parseInt(parts[1]); //amount
                    } catch (NumberFormatException e) {
                        System.out.println("Надо вводить цифры...");
                        continue;
                    }
                    if (nomOfProd > products.length || nomOfProd <= 0) {
                        System.out.println("Не верный номер продукта!");
                        continue;
                    }
                    if (countOfProd > 100) {
                        System.out.println(" Введено не приемлемо большое количество продуктов!");
                        continue;
                    }
                    if (countOfProd == 0) {
                        bs.getCountOfProducts()[nomOfProd - 1] = 0;
                        bs.logging(nomOfProd,countOfProd);
                        bs.addToCart(nomOfProd,countOfProd);
                    } else {
                        if (bs.getCountOfProducts()[nomOfProd - 1] + countOfProd < 0) {
                            System.out.println("В корзине не может быть отрицательное количество!!!");
                        } else {
                            bs.addToCart(nomOfProd, countOfProd);
                        }
                    }
                }
            } else {
                System.out.println("Необходимо номер продукта и количество разделить пробелом!");
            }
        }
        bs.printCart();
        bs.saveLog();
    }
}
