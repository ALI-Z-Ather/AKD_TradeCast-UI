package tradecast.models;

public final class MarketQuoteRow {

    private final String mkt;
    private final String scrip;
    private final double lastPrice;
    private final double change;
    private final double buy;
    private final long bVol;
    private final double sell;
    private final long sVol;
    private final double high;
    private final double low;
    private final long totalVolume;
    private final double average;
    private final double closePrice;

    public MarketQuoteRow(String mkt, String scrip, double lastPrice, double change, double buy, long bVol,
            double sell, long sVol, double high, double low, long totalVolume, double average, double closePrice) {
        this.mkt = mkt;
        this.scrip = scrip;
        this.lastPrice = lastPrice;
        this.change = change;
        this.buy = buy;
        this.bVol = bVol;
        this.sell = sell;
        this.sVol = sVol;
        this.high = high;
        this.low = low;
        this.totalVolume = totalVolume;
        this.average = average;
        this.closePrice = closePrice;
    }

    public String getMkt() { return mkt; }
    public String getScrip() { return scrip; }
    public double getLastPrice() { return lastPrice; }
    public double getChange() { return change; }
    public double getBuy() { return buy; }
    public long getBVol() { return bVol; }
    public double getSell() { return sell; }
    public long getSVol() { return sVol; }
    public double getHigh() { return high; }
    public double getLow() { return low; }
    public long getTotalVolume() { return totalVolume; }
    public double getAverage() { return average; }
    public double getClosePrice() { return closePrice; }
}
