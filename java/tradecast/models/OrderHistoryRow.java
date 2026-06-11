package tradecast.models;

import java.time.LocalDate;

public final class OrderHistoryRow {

    private final String accountNo;
    private final String clientName;
    private final String orderType;
    private final String scrip;
    private final long quantity;
    private final double grossRate;
    private final double netAmount;
    private final LocalDate clearingDate;

    public OrderHistoryRow(String accountNo, String clientName, String orderType, String scrip,
            long quantity, double grossRate, double netAmount, LocalDate clearingDate) {
        this.accountNo = accountNo;
        this.clientName = clientName;
        this.orderType = orderType;
        this.scrip = scrip;
        this.quantity = quantity;
        this.grossRate = grossRate;
        this.netAmount = netAmount;
        this.clearingDate = clearingDate;
    }

    public String getAccountNo() { return accountNo; }
    public String getClientName() { return clientName; }
    public String getOrderType() { return orderType; }
    public String getScrip() { return scrip; }
    public long getQuantity() { return quantity; }
    public double getGrossRate() { return grossRate; }
    public double getNetAmount() { return netAmount; }
    public LocalDate getClearingDate() { return clearingDate; }
}
