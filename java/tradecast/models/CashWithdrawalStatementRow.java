package tradecast.models;

import java.time.LocalDate;

public final class CashWithdrawalStatementRow {

    private final String accountNo;
    private final String clientName;
    private final LocalDate requestedDate;
    private final double amountRequested;
    private final double amountApproved;
    private final String withdrawalMode;
    private final String comments;
    private final String status;

    public CashWithdrawalStatementRow(String accountNo, String clientName, LocalDate requestedDate,
            double amountRequested, double amountApproved, String withdrawalMode, String comments, String status) {
        this.accountNo = accountNo;
        this.clientName = clientName;
        this.requestedDate = requestedDate;
        this.amountRequested = amountRequested;
        this.amountApproved = amountApproved;
        this.withdrawalMode = withdrawalMode;
        this.comments = comments;
        this.status = status;
    }

    public String getAccountNo() { return accountNo; }
    public String getClientName() { return clientName; }
    public LocalDate getRequestedDate() { return requestedDate; }
    public double getAmountRequested() { return amountRequested; }
    public double getAmountApproved() { return amountApproved; }
    public String getWithdrawalMode() { return withdrawalMode; }
    public String getComments() { return comments; }
    public String getStatus() { return status; }
}
