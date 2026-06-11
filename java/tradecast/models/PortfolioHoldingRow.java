package tradecast.models;

public final class PortfolioHoldingRow {

    private final String scrip;
    private final long netQuantity;
    private final double avgPrice;
    private final double currentPrice;

    public PortfolioHoldingRow(String scrip, long netQuantity, double avgPrice, double currentPrice) {
        this.scrip = scrip;
        this.netQuantity = netQuantity;
        this.avgPrice = avgPrice;
        this.currentPrice = currentPrice;
    }

    public String getScrip() { return scrip; }
    public long getNetQuantity() { return netQuantity; }
    public double getAvgPrice() { return avgPrice; }
    public double getCurrentPrice() { return currentPrice; }

    public double getCurrentWorth() {
        return netQuantity * currentPrice;
    }

    public double getInvestment() {
        return netQuantity * avgPrice;
    }

    public double getProfitLoss() {
        return getCurrentWorth() - getInvestment();
    }
}
