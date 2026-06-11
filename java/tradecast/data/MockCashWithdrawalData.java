package tradecast.data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import tradecast.models.CashWithdrawalStatementRow;

public final class MockCashWithdrawalData {

    private static final Map<String, Double> AVAILABLE_BY_ACCOUNT = new LinkedHashMap<>();

    static {
        AVAILABLE_BY_ACCOUNT.put("ACC-1001", 450_000.00);
        AVAILABLE_BY_ACCOUNT.put("ACC-1002", 128_750.50);
        AVAILABLE_BY_ACCOUNT.put("ACC-2001", 62_000.00);
        AVAILABLE_BY_ACCOUNT.put("ACC-3005", 910_250.75);
    }

    private MockCashWithdrawalData() {
    }

    public static List<String> accountNumbersForStatement() {
        List<String> list = new ArrayList<>();
        list.add("(All)");
        list.addAll(AVAILABLE_BY_ACCOUNT.keySet());
        return list;
    }

    public static List<String> accountNumbersForRequest() {
        return new ArrayList<>(AVAILABLE_BY_ACCOUNT.keySet());
    }

    public static double availableForAccount(String accountNo) {
        return AVAILABLE_BY_ACCOUNT.getOrDefault(accountNo, 0.0);
    }

    public static List<CashWithdrawalStatementRow> allStatementRows() {
        List<CashWithdrawalStatementRow> list = new ArrayList<>();
        LocalDate today = LocalDate.now();
        list.add(stmt("ACC-1001", "Ali Khan", today.minusDays(2), 25000, 25000, "Cheque Delivery", "Urgent", "Approved"));
        list.add(stmt("ACC-1001", "Ali Khan", today.minusDays(5), 10000, 0, "Online Fund Transfer", "Pending review", "In-Process"));
        list.add(stmt("ACC-1002", "Sara Malik", today.minusDays(1), 5000, 0, "Cheque Pickup", "Branch pickup", "Declined"));
        list.add(stmt("ACC-1002", "Sara Malik", today.minusDays(8), 15000, 15000, "Online Fund Transfer", "SCB link", "Approved"));
        list.add(stmt("ACC-2001", "Usman Raza", today.minusDays(3), 8000, 8000, "Cheque Delivery", "Registered post", "Approved"));
        list.add(stmt("ACC-2001", "Usman Raza", today, 12000, 0, "Cheque Pickup", "", "In-Process"));
        list.add(stmt("ACC-3005", "Fatima Noor", today.minusDays(12), 100000, 95000, "Online Fund Transfer", "Partial approval", "Approved"));
        list.add(stmt("ACC-3005", "Fatima Noor", today.minusDays(4), 75000, 0, "Cheque Delivery", "KYC pending", "Declined"));
        list.add(stmt("ACC-1001", "Ali Khan", today.minusDays(20), 3000, 3000, "Cheque Pickup", "OK", "Approved"));
        list.add(stmt("ACC-1002", "Sara Malik", today.minusDays(6), 22000, 0, "Cheque Delivery", "Awaiting signatory", "In-Process"));
        list.add(stmt("ACC-2001", "Usman Raza", today.minusDays(15), 4000, 4000, "Online Fund Transfer", "SCB", "Approved"));
        list.add(stmt("ACC-3005", "Imran Shah", today.minusDays(9), 60000, 0, "Cheque Delivery", "Declined: limit", "Declined"));
        return list;
    }

    private static CashWithdrawalStatementRow stmt(String acc, String name, LocalDate req, double requested,
            double approved, String mode, String comments, String status) {
        return new CashWithdrawalStatementRow(acc, name, req, requested, approved, mode, comments, status);
    }
}
