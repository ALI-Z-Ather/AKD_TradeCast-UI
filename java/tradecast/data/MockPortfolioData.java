package tradecast.data;

import java.util.ArrayList;
import java.util.List;

import tradecast.models.PortfolioHoldingRow;

public final class MockPortfolioData {

    public static final double LEDGER_BALANCE = 248_750.35;
    public static final double SOLD_COLLATERALS = 62_500.00;

    private MockPortfolioData() {
    }

    public static List<PortfolioHoldingRow> sampleHoldings() {
        List<PortfolioHoldingRow> rows = new ArrayList<>();
        rows.add(new PortfolioHoldingRow("OGDC", 500, 96.20, 98.75));
        rows.add(new PortfolioHoldingRow("NBP", 1200, 86.10, 85.20));
        rows.add(new PortfolioHoldingRow("ENGRO", 200, 308.00, 312.50));
        rows.add(new PortfolioHoldingRow("HBL", 800, 145.00, 142.10));
        rows.add(new PortfolioHoldingRow("PSO", 350, 252.00, 256.40));
        rows.add(new PortfolioHoldingRow("LUCK", 50, 552.00, 545.00));
        rows.add(new PortfolioHoldingRow("MCB", 400, 175.50, 178.30));
        return rows;
    }
}
