package tradecast.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import tradecast.models.MarketQuoteRow;

public final class MockMarketQuotes {

    private MockMarketQuotes() {
    }

    public static List<String> knownScrips() {
        return Arrays.asList(
                "NBP", "ENGRO", "OGDC", "LUCK", "MCB", "HBL", "PSO", "UBL", "PPL", "AICL", "MARI", "FFC", "SYS", "DOL");
    }

    public static List<MarketQuoteRow> sampleRows() {
        List<MarketQuoteRow> rows = new ArrayList<>();
        rows.add(row("REG", "NBP", 85.20, -0.45, 85.15, 12500, 85.25, 8900, 86.00, 84.90, 2100000, 85.10, 85.65));
        rows.add(row("REG", "ENGRO", 312.50, 2.30, 312.40, 4200, 312.60, 5100, 314.00, 309.00, 980000, 311.80, 310.20));
        rows.add(row("REG", "OGDC", 98.75, -1.10, 98.70, 15600, 98.80, 12300, 100.20, 97.50, 3200000, 98.90, 99.85));
        rows.add(row("REG", "LUCK", 545.00, 0.00, 544.50, 800, 545.50, 1200, 548.00, 540.00, 450000, 544.20, 545.00));
        rows.add(row("REG", "MCB", 178.30, 0.85, 178.20, 6700, 178.40, 5400, 179.10, 176.80, 1500000, 177.60, 177.45));
        rows.add(row("REG", "HBL", 142.10, -0.20, 142.05, 9200, 142.15, 7800, 143.00, 141.50, 1800000, 142.00, 142.30));
        rows.add(row("REG", "PSO", 256.40, 1.55, 256.30, 3100, 256.50, 2900, 257.80, 254.00, 620000, 255.90, 254.85));
        rows.add(row("REG", "UBL", 168.90, -0.75, 168.85, 4500, 168.95, 4100, 170.00, 168.00, 890000, 168.70, 169.65));
        rows.add(row("REG", "PPL", 112.25, 0.40, 112.20, 5800, 112.30, 6200, 113.00, 111.50, 1100000, 112.10, 111.85));
        rows.add(row("REG", "AICL", 421.00, -3.25, 420.50, 2100, 421.50, 2600, 428.00, 419.00, 380000, 422.10, 424.25));
        rows.add(row("REG", "MARI", 1896.50, 12.00, 1895.00, 900, 1898.00, 750, 1910.00, 1870.00, 125000, 1892.00, 1884.50));
        rows.add(row("REG", "FFC", 72.40, 0.15, 72.35, 11000, 72.45, 9800, 73.10, 71.90, 2400000, 72.30, 72.25));
        rows.add(row("REG", "SYS", 456.75, -2.05, 456.50, 1800, 457.00, 2200, 462.00, 455.00, 290000, 457.80, 458.80));
        rows.add(row("REG", "DOL", 38.60, 0.05, 38.55, 24000, 38.65, 19800, 39.00, 38.20, 5100000, 38.58, 38.55));
        return rows;
    }

    private static MarketQuoteRow row(String mkt, String scrip, double last, double chg, double buy, long bVol,
            double sell, long sVol, double high, double low, long totVol, double avg, double close) {
        return new MarketQuoteRow(mkt, scrip, last, chg, buy, bVol, sell, sVol, high, low, totVol, avg, close);
    }
}
