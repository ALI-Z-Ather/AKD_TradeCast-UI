package tradecast.data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import tradecast.models.OrderHistoryRow;

public final class MockOrderHistoryData {

    private MockOrderHistoryData() {
    }

    public static List<String> accountNumbers() {
        return Arrays.asList("(All)", "ACC-1001", "ACC-1002", "ACC-2001", "ACC-3005");
    }

    public static List<String> clientNames() {
        return Arrays.asList("(All)", "Ali Khan", "Sara Malik", "Usman Raza", "Fatima Noor", "Imran Shah");
    }

    public static List<String> orderTypes() {
        return Arrays.asList("(All)", "Buy", "Sell");
    }

    public static List<String> scrips() {
        return Arrays.asList("(All)", "NBP", "OGDC", "ENGRO", "HBL", "PSO", "LUCK", "MCB", "UBL");
    }

    public static List<OrderHistoryRow> allRows() {
        List<OrderHistoryRow> list = new ArrayList<>();
        LocalDate today = LocalDate.now();
        list.add(row("ACC-1001", "Ali Khan", "Buy", "OGDC", 200, 98.50, 19720.00, today.minusDays(2)));
        list.add(row("ACC-1001", "Ali Khan", "Sell", "NBP", 500, 85.40, 42680.00, today.minusDays(5)));
        list.add(row("ACC-1002", "Sara Malik", "Buy", "ENGRO", 50, 310.00, 15500.00, today.minusDays(1)));
        list.add(row("ACC-1002", "Sara Malik", "Buy", "HBL", 300, 142.25, 42675.00, today.minusDays(3)));
        list.add(row("ACC-2001", "Usman Raza", "Sell", "PSO", 120, 255.00, 30600.00, today.minusDays(7)));
        list.add(row("ACC-2001", "Usman Raza", "Buy", "LUCK", 25, 546.00, 13650.00, today.minusDays(4)));
        list.add(row("ACC-3005", "Fatima Noor", "Sell", "MCB", 400, 177.50, 71000.00, today));
        list.add(row("ACC-3005", "Fatima Noor", "Buy", "UBL", 150, 168.00, 25200.00, today.minusDays(6)));
        list.add(row("ACC-1001", "Ali Khan", "Buy", "MCB", 100, 176.00, 17600.00, today.minusDays(10)));
        list.add(row("ACC-1002", "Sara Malik", "Sell", "OGDC", 150, 99.00, 14850.00, today.minusDays(8)));
        list.add(row("ACC-2001", "Usman Raza", "Buy", "NBP", 800, 84.90, 67920.00, today.minusDays(12)));
        list.add(row("ACC-3005", "Imran Shah", "Sell", "ENGRO", 75, 312.50, 23437.50, today.minusDays(9)));
        list.add(row("ACC-1001", "Ali Khan", "Buy", "PSO", 200, 254.00, 50800.00, today.minusDays(14)));
        list.add(row("ACC-1002", "Sara Malik", "Sell", "HBL", 250, 141.80, 35450.00, today.minusDays(11)));
        list.add(row("ACC-2001", "Usman Raza", "Buy", "UBL", 90, 169.25, 15232.50, today.minusDays(15)));
        list.add(row("ACC-3005", "Fatima Noor", "Sell", "LUCK", 40, 544.00, 21760.00, today.minusDays(13)));
        return list;
    }

    private static OrderHistoryRow row(String acc, String name, String type, String scrip,
            long qty, double gross, double net, LocalDate clearing) {
        return new OrderHistoryRow(acc, name, type, scrip, qty, gross, net, clearing);
    }
}
