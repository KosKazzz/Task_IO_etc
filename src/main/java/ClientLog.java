import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ClientLog {

    private List<Integer> productNums = new ArrayList<>();
    private List<Integer> amounts = new ArrayList<>();

    public void log(int productNum, int amount) {
        this.productNums.add(productNum);
        this.amounts.add(amount);
    }

    public void exportAsCSV(File txtFile) {
        StringBuilder logString = new StringBuilder();
        logString.append("productNums").append(",").append("amounts").append(" ");
        for (int i = 0; i < productNums.size(); i++) {
            logString.append(productNums.get(i)).append(",").append(amounts.get(i)).append(" ");
        }
        String[] logStringArr = logString.toString().split(" ");
        try (CSVWriter writer = new CSVWriter(new FileWriter(txtFile), '\n', ' ', ' ', "")) {
            writer.writeNext(logStringArr);
        } catch (IOException ex) {
            System.err.println("Parsing error!");
        }

    }
}
